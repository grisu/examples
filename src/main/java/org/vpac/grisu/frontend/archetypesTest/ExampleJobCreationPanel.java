package org.vpac.grisu.frontend.archetypesTest;

import grisu.control.ServiceInterface;
import grisu.frontend.control.jobMonitoring.RunningJobManager;
import grisu.frontend.model.job.JobObject;
import grisu.frontend.view.swing.jobcreation.JobCreationPanel;
import grisu.frontend.view.swing.jobcreation.widgets.SingleInputFile;
import grisu.frontend.view.swing.jobcreation.widgets.SubmissionLogPanel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

public class ExampleJobCreationPanel extends JPanel implements JobCreationPanel {
	// the button to submit a job
	private JButton btnSubmit;
	// a ready-made widget that, once connected to a JobObject, tracks the
	// progress of a job submission...
	private SubmissionLogPanel submissionLogPanel;

	// the serviceinterface. gets set when the setServiceInterface method is
	// called. we need it in order to submit the job and do everything else...
	private ServiceInterface si;
	private JTextField commandlineField;
	private SingleInputFile singleInputFile;

	/**
	 * Create the panel.
	 */
	public ExampleJobCreationPanel() {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		add(getCommandlineField(), "2, 2, fill, default");
		add(getSingleInputFile(), "2, 4, fill, fill");
		add(getBtnSubmit(), "2, 7, right, default");
		add(getSubmissionLogPanel(), "2, 9, fill, fill");

	}

	public boolean createsBatchJob() {
		// we are only creating a single job with this panel
		return false;
	}

	public boolean createsSingleJob() {
		// yep
		return true;
	}

	// creating the submit button and connecting it with the submit action
	private JButton getBtnSubmit() {
		if (btnSubmit == null) {
			btnSubmit = new JButton("Submit");
			btnSubmit.setAlignmentX(Component.RIGHT_ALIGNMENT);
			btnSubmit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					submitJob();
				}
			});
		}
		return btnSubmit;
	}

	private JTextField getCommandlineField() {
		if (commandlineField == null) {
			commandlineField = new JTextField();
			commandlineField.setAlignmentX(Component.RIGHT_ALIGNMENT);
			commandlineField.setMaximumSize(new Dimension(2000, 100));
		}
		return commandlineField;
	}

	// usually you just return the object itself
	public JPanel getPanel() {
		return this;
	}

	// this is what is going to be displayed in the client on the upper left
	// side
	public String getPanelName() {
		return "Example";
	}

	private SingleInputFile getSingleInputFile() {
		if (singleInputFile == null) {
			singleInputFile = new SingleInputFile();
			singleInputFile.setAlignmentX(Component.RIGHT_ALIGNMENT);
			singleInputFile.setMaximumSize(new Dimension(2000, 140));
		}
		return singleInputFile;
	}

	// creating a submission log panel
	private SubmissionLogPanel getSubmissionLogPanel() {
		if (submissionLogPanel == null) {
			submissionLogPanel = new SubmissionLogPanel();
			submissionLogPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		}
		return submissionLogPanel;
	}

	// here you return the name of the application package that your client
	// submits jobs for.
	// e.g. Java, Python, MrBayes, ....
	public String getSupportedApplication() {
		return "UnixCommands";
	}

	// convenience method to disable and re-enable the button while the
	// submission is ongoing
	private void lockUI(final boolean lock) {

		SwingUtilities.invokeLater(new Thread() {

			@Override
			public void run() {
				getBtnSubmit().setEnabled(!lock);
			}

		});

	}

	// here we collect the reference to the serviceinterface. Also, usually
	// here's where you do some
	// initialization of your panel (if you need to). E.g. populating a combobox
	// with a list of submission locations or versions of an application
	// package...
	public void setServiceInterface(ServiceInterface si) {
		this.si = si;
		getSingleInputFile().setServiceInterface(si);
	}

	// the method to actually submit the job
	private void submitJob() {

		// we submit the job in it's own thread in order to not block the swing
		// ui
		new Thread() {
			@Override
			public void run() {
				try {
					// disable the submit button so the user can't inadvertently
					// submit 2 jobs in a row
					lockUI(true);

					// now, let's create the job
					final JobObject job = new JobObject(si);
					// ... and connect it to the submission log panel so the
					// user can see that there's something going on...
					getSubmissionLogPanel().setJobObject(job);
					// .. every job needs its own unique jobname. You can either
					// hardcode it (but then you'll only be able to submit one
					// job.
					// or you use the setUniqueJobname or setTimestampJobname
					// methods, which will do it for you. Or you ask the user...
					job.setTimestampJobname("helloworldJob");
					// this is required and the most important part of the job
					// creation process. Grisu needs to know what job you want
					// to submit, after all...
					job.setCommandline(getCommandlineField().getText());
					// setting the walltime of a job is also important
					job.setWalltimeInSeconds(60);
					job.addInputFileUrl(getSingleInputFile().getInputFileUrl());

					// the next step is optional, but it is recommended to set
					// the application if you know it. jobsubmission will be
					// faster, because otherwise Grisu has to figure out which
					// application package the executable you are using belongs
					// to...
					// job.setApplication("UnixCommands");

					// now we need to create the job on the backend. In order to
					// do this we need to
					// specify which group/vo we want to use.
					// you can either hardcode that (if the project the client
					// is for only has 1 VO anyway),
					// you can ask the user, or you can specify null in which
					// case the default/last used VO is used
					// there are 2 ways to create the job, either:
					// job.createJob() / job.createJob("/ARCS/BeSTGRID")
					// or, what is recommended if you use the provided swing
					// client library (as we do here):
					RunningJobManager.getDefault(si).createJob(job,
							"/ARCS/BeSTGRID");
					// this integrates better with the job management panel we
					// are using

					// last not least, we stage in files and submit the job
					job.submitJob();

				} catch (final Exception e) {
					// if something goes wrong, we want to show the user
					final ErrorInfo info = new ErrorInfo(
							"Job submission error", "Can't submit job:\n\n"
									+ e.getLocalizedMessage(), null, "Error",
							e, Level.SEVERE, null);

					final JXErrorPane pane = new JXErrorPane();
					pane.setErrorInfo(info);
					// the following line could be used to show a button to
					// submit
					// a bug/ticket to a bug tracking system
					// pane.setErrorReporter(new GrisuErrorReporter());

					JXErrorPane.showDialog(
							ExampleJobCreationPanel.this.getRootPane(), pane);
				} finally {
					// enable the submit button again
					lockUI(false);
				}

			}
		}.start();
	}
}
