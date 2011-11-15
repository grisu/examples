package tmp;

import grisu.control.ServiceInterface;
import grisu.control.exceptions.JobPropertiesException;
import grisu.control.exceptions.JobSubmissionException;
import grisu.control.exceptions.RemoteFileSystemException;
import grisu.frontend.control.login.LoginException;
import grisu.frontend.control.login.LoginManager;
import grisu.frontend.model.job.JobObject;
import grisu.jcommons.constants.Constants;
import grisu.model.FileManager;
import grisu.model.GrisuRegistry;
import grisu.model.GrisuRegistryManager;
import grisu.model.dto.GridFile;
import grisu.settings.Environment;

public class SimpleEndToEndWorkflow {

	/**
	 * Commented example code on how to submit a job and then access and work
	 * with the results.
	 */
	public static void main(String[] args) throws RemoteFileSystemException {

		Environment.transitionGrisuConfigDirs();

		System.out.println("Logging in...");
		ServiceInterface si = null;
		try {
			// login
			// in this case we login via the commandline
			// si = LoginManager.loginCommandline("BeSTGRID");
			si = LoginManager.loginCommandline("LOCAL");
		} catch (final LoginException e) {
			System.err.println("Could not login: " + e.getLocalizedMessage());
			System.exit(1);
		}

		// create a registry. the registry is used to get objects that can
		// provide all kinds of grid and user information as well
		// as a filemanager which wraps up the most common file transfer
		// use cases
		final GrisuRegistry registry = GrisuRegistryManager.getDefault(si);

		// getting a filemanager object, which encapsulates file related actions
		// we'll need that later once the job is finished
		final FileManager fm = registry.getFileManager();

		System.out.println("Creating job...");
		// this creates a new, empty JobObject
		final JobObject job = new JobObject(si);
		// if we know it, we should set the name of the Application so Grisu
		// does not have to calculate it for us
		job.setApplication("UnixCommands");
		// every job has to have a unique jobname, JobObject (more exactly, it's
		// parent class) provide a few convenience methods to ensure that, like
		// the one below
		job.setTimestampJobname("MyFirstJob");

		// we can set a submission location if we want to enforce where the job
		// should run. This can be omitted and Grisu will automatically choose a
		// suitable submission location for you
		// job.setSubmissionLocation("route@er171.ceres.auckland.ac.nz:ng2.auckland.ac.nz");

		// for this job, we upload an input folder into a (differently named)
		// folder (called "testfolder"). If we don't need a different name/path,
		// we can use the one-parameter version of this method.
		job.addInputFileUrl("/home/markus/test/test.txt");

		// for reference, we remember the auto-determined name of the job
		System.out.println("Set jobname to be: " + job.getJobname());
		// most importantly, we need to specify the command we want to run
		job.setCommandline("find .");

		// also, we need to set a walltime to help the scheduler on the cluster
		// do it's task (default is 600 which is too short for a normal job)
		job.setWalltimeInSeconds(60);

		try {
			System.out.println("Creating job on backend...");
			// now we tell the Grisu backend that we want to create the job
			// this step is necessary so it can determine missing properties
			// (like maybe submission location, whether there are any
			// filesystems that are usable for the job and so on).
			// for that to work we also need to specify the VO we want to use to
			// submit the job
			job.createJob("/nz/nesi");
		} catch (final JobPropertiesException e) {
			System.err.println("Could not create job: "
					+ e.getLocalizedMessage());
			System.exit(1);
		}

		try {
			System.out.println("Submitting job to the grid...");
			// The last step is to actually tell the backend to submit the job
			// to the grid
			// this step also handles all the filestaging that is involved
			job.submitJob();
		} catch (final JobSubmissionException e) {
			System.err.println("Could not submit job: "
					+ e.getLocalizedMessage());
			System.exit(1);
		} catch (final InterruptedException e) {
			System.err.println("Jobsubmission interrupted: "
					+ e.getLocalizedMessage());
			System.exit(1);
		}

		System.out.println("Job submission finished.");
		System.out.println("Job submitted to: "
				+ job.getJobProperty(Constants.SUBMISSION_SITE_KEY));

		System.out.println("Waiting for job to finish...");

		// We need to wait for the job to finish on the cluster so we can parse
		// its results.
		// for a real workflow, don't check every 5 seconds since that would
		// put too much load on the backend/gateways
		job.waitForJobToFinish(5);

		// once the job is finished, we print out the status of the job (whether
		// it failed or finished successfully)
		System.out.println("Job finished with status: "
				+ job.getStatusString(false));

		System.out.println("List job directory: ");
		// now we get a GridFile representing the job directory...
		final GridFile jobDirectory = job.listJobDirectory();
		// ...and then we traverse through the children of the jobdirectory and
		// list it's content
		for (final GridFile file : jobDirectory.getChildren()) {
			System.out.println("Name: " + file.getName() + " / URL: "
					+ file.getUrl());
		}

		// we also have direct access to the contents of the stdout and stderr
		// files, in case we need to do some parsing
		System.out.println("Stdout: " + job.getStdOutContent());
		System.out.println("Stderr: " + job.getStdErrContent());

		// it's pretty important to shutdown the jvm properly. There might be
		// some executors running in the background
		// and they need to know when to shutdown.
		// Otherwise, your application might not exit.
		System.exit(0);

	}
}
