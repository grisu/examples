

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

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

public class BatchJobCat {

	public static void main(final String[] args) throws Exception {

		final ServiceInterface si = LoginManager
		.loginCommandline("BeSTGRID-DEV");

		final GrisuRegistry registry = GrisuRegistryManager.getDefault(si);

		final int numberOfJobs = 10;

		final String batchJobname = "batchJobCat";

		final BatchJobObject batchJob = new BatchJobObject(si, batchJobname,
				"/nz/nesi", "UnixCommands",
				Constants.NO_VERSION_INDICATOR_STRING);


		final String pathToInputFiles = batchJob.pathToInputFiles();

		for (int i = 0; i < numberOfJobs; i++) {

			final int frameNumber = i;

			final JobObject jo = new JobObject(si);

			jo.setCommandline("cat " + pathToInputFiles + "commonJobFile.txt"
					+ " singleJobFile.txt");
			jo.addInputFileUrl("/home/markus/tmp/singleJobFile.txt");

			batchJob.addJob(jo);
		}

		batchJob.addInputFile("/home/markus/tmp/commonJobFile.txt");

		batchJob.setDefaultNoCpus(1);
		batchJob.setDefaultWalltimeInSeconds(60);

		try {
			batchJob.prepareAndCreateJobs(true);
		} catch (final JobsException e) {
			for (final JobObject job : e.getFailures().keySet()) {
				System.out.println("Creation " + job.getJobname() + " failed: "
						+ e.getFailures().get(job).getLocalizedMessage());
			}
			System.exit(1);
		}

		System.out.println("Job distribution:");
		System.out.println(batchJob.getOptimizationResult());

		try {
			batchJob.submit();
		} catch (final Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Submission finished: " + new Date());
		int i = 0;
		final boolean resubmitted = false;

		while (!batchJob.isFinished(true)) {
			i = i + 1;
			System.out.println("Not finished yet...");
			System.out.println("Iteration: " + i);
			batchJob.getJobs().size();
			System.out.println(batchJob.getDetails());

			Thread.sleep(2000);
		}

		if (batchJob.failedJobs().size() > 0) {
			System.out.println("Failed jobs.");
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

	}

	public BatchJobCat() {
		AnnotationProcessor.process(this);
	}

	@EventSubscriber(eventClass = BatchJobEvent.class)
	public void onMultiPartJobEvent(BatchJobEvent event) {

		System.out.println("Event: " + event.getMessage());

	}

}
