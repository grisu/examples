import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;
import grisu.frontend.model.job.BatchJobObject;
import grisu.frontend.model.job.JobObject;
import grisu.frontend.model.job.JobsException;
import grisu.jcommons.constants.Constants;

import java.util.Date;

public class SimpleBatchJobSubmit {

	public static void main(final String[] args) throws Exception {

		// login
		// in this case we login via the commandline
		// ServiceInterface si = LoginManager.loginCommandline("BeSTGRID");
		ServiceInterface si = LoginManager.loginCommandline("Local");

		// how many jobs do we want?
		final int numberOfJobs = 5;

		// we want to have a unique jobname for this batchjob
		final Date start = new Date();
		final String multiJobName = "batchExample_" + start.getTime();

		// creating the batch job, not worrying about the version of the
		// application on the backend
		final BatchJobObject batchJob = new BatchJobObject(si, multiJobName,
				"/ARCS/BeSTGRID", "UnixCommands",
				Constants.NO_VERSION_INDICATOR_STRING);

		// now we need to create all the jobs manually, no way around that...
		for (int i = 0; i < numberOfJobs; i++) {

			final int jobNumber = i;

			// this creates a new, empty JobObject
			final JobObject jo = new JobObject(si);
			// the jobname of the single job needs to be unique in your
			// jobnamespace, we could also use one of the convenience jobname
			// creation methods from the JobObject class
			jo.setJobname(multiJobName + "_" + jobNumber);
			// now we attach an input file to this job.
			// if we attach it to the (single) job and not to the batchjob, only
			// the single job will have access to it.
			jo.addInputFileUrl("/home/markus/tmp/tmpSmall/text0.txt");
			// obviously, we need to set the command we want to run
			// the pathToInputFiles() method returns the (relative) path to the
			// input files that are attached to the parent batch job
			// in this example, we want to list the job directory, which will
			// list the file that is attached to the single job and also the job
			// directory of the batch job, which will list files attached to the
			// parent batchjob
			jo.setCommandline("ls -lah . " + batchJob.pathToInputFiles()
					+ "/temp_exampleFolder");

			// we set the submission location manually in this example
			// it's also possible to not set it and let Grisu decide where to
			// submit the job to...
			jo.setSubmissionLocation("route@er171.ceres.auckland.ac.nz:ng2.auckland.ac.nz");

			// now we need to attach the job to the batchjob
			batchJob.addJob(jo);

		}

		// we also attach an input file (folder in this case) to the parent
		// batch job. All child jobs will have access to this file by refering
		// to it in a relative way via the pathToInputFiles() method
		batchJob.addInputFile("/home/markus/tmp/tmpSmall", "temp_exampleFolder");

		// if all jobs have the same no of cpus, we can specify it here
		batchJob.setDefaultNoCpus(1);
		// if all jobs have the same walltime, we can specify it here
		batchJob.setDefaultWalltimeInSeconds(310);

		try {
			// now we need to prepare the batchjob on the backend
			// if you use "true" as a parameter, Grisu will distribute the job
			// accross all the sites that support the specified application.
			// if you specify "false", you should have specified a submission
			// location for each sub-job seperately ("true" would overwrite
			// that)
			// this will also upload any possible input files
			batchJob.prepareAndCreateJobs(false);
		} catch (final JobsException e) {
			for (final JobObject job : e.getFailures().keySet()) {
				System.out.println("Creation " + job.getJobname() + " failed: "
						+ e.getFailures().get(job).getLocalizedMessage());
			}
			System.exit(1);
		}

		try {
			// finally, we submit the job
			// the boolean parameter lets you decide whether to wait for the job
			// submission to finish (which could take quite a while if you have
			// 1000s of jobs) or not
			batchJob.submit(true);
		} catch (final Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Submission finished.");

		// now we wait for the job to finish. in real life, choose a longer
		// period inbetween checks (the int parameter is the wait time in
		// seconds), since this would probably overload the
		// backend
		// if you want to know in more detail what is happening, than you can
		// implement your own "while-job-is-still-running" loop to maybe
		// output how many jobs are already finished or re-submit failed jobs
		// while other jobs are still in the queue
		batchJob.waitForJobToFinish(10);

		// if one or more of the sub-jobs failed, we might need to do something
		// about it, maybe resubmit...
		if (batchJob.failedJobs().size() > 0) {
			System.out.println("Some of the jobs failed :-(");
		}

		// now we want to know the output for every sub-job
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
