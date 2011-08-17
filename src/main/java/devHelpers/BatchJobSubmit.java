package devHelpers;


import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;
import grisu.frontend.model.events.BatchJobEvent;
import grisu.frontend.model.job.BatchJobObject;
import grisu.frontend.model.job.JobObject;
import grisu.frontend.model.job.JobsException;
import grisu.jcommons.constants.Constants;
import grisu.model.GrisuRegistry;
import grisu.model.GrisuRegistryManager;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

public class BatchJobSubmit {

	public static void main(final String[] args) throws Exception {

		final ExecutorService executor = Executors.newFixedThreadPool(10);

		final ServiceInterface si = LoginManager.loginCommandline("BeSTGRID");

		final GrisuRegistry registry = GrisuRegistryManager.getDefault(si);

		final int numberOfJobs = 200;

		String batchJobname = registry.getUserEnvironmentManager()
				.calculateUniqueJobname("deleteJobTest3");

		System.out.println("Jobname is: " + batchJobname);

		final BatchJobObject multiPartJob = new BatchJobObject(si,
				batchJobname, "/ARCS/BeSTGRID", "UnixCommands",
				Constants.NO_VERSION_INDICATOR_STRING);

		// multiPartJob.addJobProperty(Constants.DISTRIBUTION_METHOD,
		// Constants.DISTRIBUTION_METHOD_EQUAL);

		final String pathToInputFiles = multiPartJob.pathToInputFiles();

		for (int i = 0; i < numberOfJobs; i++) {

			final int frameNumber = i;

			final JobObject jo = new JobObject(si);
			jo.setJobname(batchJobname + "_" + frameNumber);
			jo.setApplication("UnixCommands");
			jo.setCommandline("cat " + multiPartJob.pathToInputFiles()
					+ "/test.txt");
			// jo.setCommandline("R --no-readline --no-restore --no-save -f "
			// + "Evaluation_Markov-ADF-Test-2011-05-09-mc50-test.r");
			// jo.setCommandline("cat "+pathToInputFiles+"multiJobFile.txt");
			// jo.setCommandline("cat singleJobFile.txt "+pathToInputFiles+"/multiJobFile.txt");
			// jo.setCommandline("cat singleJobFile_"+i+".txt "+pathToInputFiles+"/multiJobFile.txt");
			// jo.setCommandline("sleep 300");
			jo.setWalltimeInSeconds(1800);
			// jo.addInputFileUrl("/home/markus/test/singleJobFile_"+i+".txt");
			// jo.addInputFileUrl("/home/markus/test/singleJobFile.txt");
			jo.setSubmissionLocation("route@er171.ceres.auckland.ac.nz:ng2.auckland.ac.nz");

			multiPartJob.addJob(jo);


		}

		multiPartJob.addInputFile("/home/markus/test/test.txt");
		// multiPartJob.addInputFile("/home/markus/Desktop/R/"
		// + "Evaluation_Markov-ADF-Test-2011-05-09-mc50-test.r");
		// multiPartJob.setLocationsToExclude(new String[]{"serial"});
		multiPartJob.setLocationsToInclude(new String[] { "route" });

		multiPartJob.setDefaultNoCpus(1);
		multiPartJob.setDefaultWalltimeInSeconds(30);

		try {
			multiPartJob.prepareAndCreateJobs(false);
		} catch (final JobsException e) {
			for (final JobObject job : e.getFailures().keySet()) {
				System.out.println("Creation " + job.getJobname() + " failed: "
						+ e.getFailures().get(job).getLocalizedMessage());
			}
			System.exit(1);
		}

		System.out.println("Job distribution:");
		System.out.println(multiPartJob.getOptimizationResult());

		try {
			multiPartJob.submit();
		} catch (final Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Submission finished: " + new Date());
		// int i = 0;
		// final boolean resubmitted = false;
		//
		// while (!multiPartJob.isFinished(true)) {
		// i = i + 1;
		// System.out.println("Not finished yet...");
		// System.out.println("Iteration: " + i);
		// multiPartJob.getJobs().size();
		// System.out.println(multiPartJob.getDetails());
		//
		// Thread.sleep(2000);
		// }
		//
		// if (multiPartJob.failedJobs().size() > 0) {
		// System.out.println("Failed jobs.");
		// }
		//
		// for (final JobObject job : multiPartJob.getJobs()) {
		// System.out.println("-------------------------------");
		// System.out.println(job.getJobname() + ": "
		// + job.getStatusString(false));
		// System.out.println(job.getStdOutContent());
		// System.out.println("-------------------------------");
		// System.out.println(job.getStdErrContent());
		// System.out.println("-------------------------------");
		// System.out.println();
		// }

	}

	public BatchJobSubmit() {
		AnnotationProcessor.process(this);
	}

	@EventSubscriber(eventClass = BatchJobEvent.class)
	public void onMultiPartJobEvent(BatchJobEvent event) {

		System.out.println("Event: " + event.getMessage());

	}

}
