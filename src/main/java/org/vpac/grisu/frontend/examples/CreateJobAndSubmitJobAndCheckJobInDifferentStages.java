package org.vpac.grisu.frontend.examples;

import grisu.control.JobConstants;
import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;
import grisu.frontend.model.events.JobStatusEvent;
import grisu.frontend.model.job.JobObject;
import grisu.jcommons.constants.Constants;
import grisu.utils.SeveralXMLHelpers;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventTopicSubscriber;

public final class CreateJobAndSubmitJobAndCheckJobInDifferentStages implements
EventTopicSubscriber<JobStatusEvent> {

	/**
	 * @param args
	 */
	public static void main(final String[] args) throws Exception {



		final ServiceInterface si = LoginManager.login();

		final CreateJobAndSubmitJobAndCheckJobInDifferentStages eventHolder = new CreateJobAndSubmitJobAndCheckJobInDifferentStages();

		final JobObject createJobObject = new JobObject(si);

		createJobObject.setApplication("Java");
		createJobObject.setCommandline("java -version");
		createJobObject.setWalltimeInSeconds(3600 * 24 * 40);
		createJobObject.setCpus(1);
		createJobObject.addInputFileUrl("/home/markus/test.txt");
		createJobObject
		.addInputFileUrl("gsiftp://ng2.canterbury.ac.nz/home/grid-admin/C_AU_O_APACGrid_OU_VPAC_CN_Markus_Binsteiner/grix_splash_v1.1.jpg");

		// GrisuRegistry registry = GrisuRegistry.getDefault(si);
		// System.out.println(StringUtils.join(registry.getApplicationInformation("java").getAvailableSubmissionLocationsForFqan("/ARCS/NGAdmin"),"\n"));

		System.out.println(SeveralXMLHelpers
				.toStringWithoutAnnoyingExceptions(createJobObject
						.getJobDescriptionDocument()));

		final String newJobname = createJobObject.createJob("/ARCS/NGAdmin",
				Constants.TIMESTAMP_METHOD);

		EventBus.subscribe(newJobname, eventHolder);

		final JobObject submitJobObject = new JobObject(si, newJobname);

		System.out.println("Application: " + submitJobObject.getApplication());

		submitJobObject.submitJob();

		final JobObject checkJobObject = new JobObject(si, newJobname);

		final Thread waitThread = new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(80000);
					System.out.println("Sleeping over.");
					checkJobObject.stopWaitingForJobToFinish();
				} catch (final InterruptedException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
			}
		};

		waitThread.start();

		final boolean finished = checkJobObject.waitForJobToFinish(3);

		if (!finished) {
			System.out.println("not finished yet.");
			checkJobObject.kill(true);
			waitThread.interrupt();
		} else {
			System.out.println("Stdout: " + checkJobObject.getStdOutContent());
			System.out.println("Stderr: " + checkJobObject.getStdErrContent());
			checkJobObject.kill(true);
			waitThread.interrupt();
		}

	}

	private CreateJobAndSubmitJobAndCheckJobInDifferentStages() {
	}

	public void onEvent(String arg0, JobStatusEvent arg1) {

		System.out.println("Topic: " + arg0 + " Event: "
				+ JobConstants.translateStatus(arg1.getNewStatus()));
	}

}
