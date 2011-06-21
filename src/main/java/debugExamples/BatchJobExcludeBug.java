package debugExamples;
import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;
import grisu.frontend.model.job.BatchJobObject;
import grisu.frontend.model.job.JobObject;
import grisu.frontend.model.job.JobsException;
import grisu.jcommons.constants.Constants;

import java.util.Date;

public class BatchJobExcludeBug {

	public static void main(final String[] args) throws Exception {

		ServiceInterface si = LoginManager.loginCommandline("BeSTGRID");
		// ServiceInterface si = LoginManager.loginCommandline("Local");

		final int numberOfJobs = 5;

		final Date start = new Date();
		final String multiJobName = "batchExample_" + start.getTime();


		final BatchJobObject batchJob = new BatchJobObject(si, multiJobName,
				"/ARCS/BeSTGRID", "UnixCommands",
				Constants.NO_VERSION_INDICATOR_STRING);

		for (int i = 0; i < numberOfJobs; i++) {

			final int jobNumber = i;

			final JobObject jo = new JobObject(si);
			jo.setJobname(multiJobName + "_" + jobNumber);
			jo.setCommandline("echo hello world");

			batchJob.addJob(jo);
		}

		batchJob.setDefaultNoCpus(1);
		batchJob.setDefaultWalltimeInSeconds(310);

		batchJob.setLocationsToExclude(new String[] { "AUT" });

		try {
			batchJob.prepareAndCreateJobs(true);
		} catch (final JobsException e) {
			for (final JobObject job : e.getFailures().keySet()) {
				System.out.println("Creation " + job.getJobname() + " failed: "
						+ e.getFailures().get(job).getLocalizedMessage());
			}
			System.exit(1);
		}

		System.out.println(batchJob.getOptimizationResult());

		try {
			batchJob.submit(true);
		} catch (final Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Submission finished.");

		batchJob.waitForJobToFinish(10);


		if (batchJob.failedJobs().size() > 0) {
			System.out.println("Some of the jobs failed :-(");
		}

		for (final JobObject job : batchJob.getJobs()) {
			System.out.println("-------------------------------");
			System.out.println(job.getJobname() + ": "
					+ job.getStatusString(false));
			System.out.println(job.getStdOutContent());
			System.out.println("-------------------------------");
			System.out.println(job.getStdErrContent());
			System.out.println("-------------------------------");
			System.out.println();
		}

		System.exit(0);

	}

}
