package org.vpac.grisu.frontend.examples;

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

public class MultiJobSubmit {

	public static void main(final String[] args) throws Exception {

		final ExecutorService executor = Executors.newFixedThreadPool(10);

		final ServiceInterface si = LoginManager.loginCommandline();

		final GrisuRegistry registry = GrisuRegistryManager.getDefault(si);

		// registry.getApplicationInformation("povray").getAvailableSubmissionLocationsForFqan("/ARCS/NGAdmin");

		final int numberOfJobs = 20;

		final Date start = new Date();
		final String multiJobName = "jobTest20_7";
		try {
			System.out.println("Killing job: " + multiJobName);
			si.kill(multiJobName, true);

			registry.getUserEnvironmentManager().waitForActionToFinish(
					multiJobName);
			System.out.println("Killed job.");
		} catch (final Exception e) {
		}

		final BatchJobObject multiPartJob = new BatchJobObject(si,
				multiJobName, "/ARCS/NGAdmin", "Java",
				Constants.NO_VERSION_INDICATOR_STRING);

		// multiPartJob.addJobProperty(Constants.DISTRIBUTION_METHOD,
		// Constants.DISTRIBUTION_METHOD_EQUAL);

		final String pathToInputFiles = multiPartJob.pathToInputFiles();

		for (int i = 0; i < numberOfJobs; i++) {

			final int frameNumber = i;

			final JobObject jo = new JobObject(si);
			jo.setJobname(multiJobName + "_" + frameNumber);
			jo.setApplication("java");
			jo.setCommandline("java -version");
			// jo.setCommandline("cat "+pathToInputFiles+"multiJobFile.txt");
			// jo.setCommandline("cat singleJobFile.txt "+pathToInputFiles+"/multiJobFile.txt");
			// jo.setCommandline("cat singleJobFile_"+i+".txt "+pathToInputFiles+"/multiJobFile.txt");
			// jo.setCommandline("sleep 300");
			jo.setWalltimeInSeconds(60);
			// jo.addInputFileUrl("/home/markus/test/singleJobFile_"+i+".txt");
			// jo.addInputFileUrl("/home/markus/test/singleJobFile.txt");

			multiPartJob.addJob(jo);

		}

		// multiPartJob.addInputFile("/home/markus/test/multiJobFile.txt");
		// multiPartJob.setLocationsToExclude(new String[]{"serial"});

		multiPartJob.setDefaultNoCpus(1);
		multiPartJob.setDefaultWalltimeInSeconds(310);

		try {
			multiPartJob.prepareAndCreateJobs(true);
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
		int i = 0;
		final boolean resubmitted = false;

		while (!multiPartJob.isFinished(true)) {
			i = i + 1;
			System.out.println("Not finished yet...");
			System.out.println("Iteration: " + i);
			multiPartJob.getJobs().size();
			System.out.println(multiPartJob.getDetails());

			Thread.sleep(2000);
		}

		if (multiPartJob.failedJobs().size() > 0) {
			System.out.println("Failed jobs.");
		}

		for (final JobObject job : multiPartJob.getJobs()) {
			System.out.println("-------------------------------");
			System.out.println(job.getJobname() + ": "
					+ job.getStatusString(false));
			System.out.println(job.getStdOutContent());
			System.out.println("-------------------------------");
			System.out.println(job.getStdErrContent());
			System.out.println("-------------------------------");
			System.out.println();
		}

	}

	public MultiJobSubmit() {
		AnnotationProcessor.process(this);
	}

	@EventSubscriber(eventClass = BatchJobEvent.class)
	public void onMultiPartJobEvent(BatchJobEvent event) {

		System.out.println("Event: " + event.getMessage());

	}

}
