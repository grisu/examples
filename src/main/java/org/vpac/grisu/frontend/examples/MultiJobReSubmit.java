package org.vpac.grisu.frontend.examples;

import grisu.control.ServiceInterface;
import grisu.frontend.control.login.ServiceInterfaceFactory;
import grisu.frontend.model.events.BatchJobEvent;
import grisu.frontend.model.job.BatchJobObject;
import grisu.frontend.model.job.JobObject;
import grisu.model.GrisuRegistry;
import grisu.model.GrisuRegistryManager;
import grith.jgrith.control.LoginParams;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

public class MultiJobReSubmit {

	public static void main(final String[] args) throws Exception {

		final ExecutorService executor = Executors.newFixedThreadPool(10);

		final String username = args[0];
		final char[] password = args[1].toCharArray();

		final LoginParams loginParams = new LoginParams(
		// "http://localhost:8080/xfire-backend/services/grisu",
		// "https://ngportal.vpac.org/grisu-ws/soap/EnunciateServiceInterfaceService",
		// "https://ngportal.vpac.org/grisu-ws/services/grisu",
		// "https://ngportal.vpac.org/grisu-ws/soap/GrisuService",
		// "http://localhost:8080/enunciate-backend/soap/GrisuService",
				"Local",
				// "ARCS_DEV",
				// "Dummy",
				username, password);

		final ServiceInterface si = ServiceInterfaceFactory
				.createInterface(loginParams);

		final GrisuRegistry registry = GrisuRegistryManager.getDefault(si);

		final Date start = new Date();
		final String multiJobName = "sleep100";

		si.restartBatchJob(multiJobName, null, null);

		final BatchJobObject multiPartJob = new BatchJobObject(si,
				multiJobName, true);

		while (!multiPartJob.isFinished(true)) {
			System.out.println("Not finished yet...");
			multiPartJob.getJobs().size();
			System.out.println(multiPartJob.getDetails());
			Thread.sleep(2000);
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

	public MultiJobReSubmit() {
		AnnotationProcessor.process(this);
	}

	@EventSubscriber(eventClass = BatchJobEvent.class)
	public void onMultiPartJobEvent(BatchJobEvent event) {

		System.out.println("Event: " + event.getMessage());

	}

}
