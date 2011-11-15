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

public class EnvTestJobSubmission {

	/**
	 * Commented example code on how to submit a job and then access and work
	 * with the results.
	 */
	public static void main(String[] args) throws RemoteFileSystemException {

		Environment.transitionGrisuConfigDirs();

		System.out.println("Logging in...");
		ServiceInterface si = null;
		try {
			si = LoginManager.loginCommandline("LOCAL");
		} catch (final LoginException e) {
			System.err.println("Could not login: " + e.getLocalizedMessage());
			System.exit(1);
		}
		final GrisuRegistry registry = GrisuRegistryManager.getDefault(si);

		final FileManager fm = registry.getFileManager();

		System.out.println("Creating job...");
		final JobObject job = new JobObject(si);
		job.setApplication("UnixCommands");
		job.setTimestampJobname("EnvTestJob");

		System.out.println("Set jobname to be: " + job.getJobname());

		job.setCommandline("env");

		job.addJobProperty("property1", "value1");

		job.setWalltimeInSeconds(60);

		try {
			System.out.println("Creating job on backend...");
			job.createJob("/nz/nesi");
		} catch (final JobPropertiesException e) {
			System.err.println("Could not create job: "
					+ e.getLocalizedMessage());
			System.exit(1);
		}

		job.addJobProperty("property2", "value2");

		try {
			System.out.println("Submitting job to the grid...");
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
