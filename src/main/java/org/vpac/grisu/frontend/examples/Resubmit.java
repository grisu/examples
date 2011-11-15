package org.vpac.grisu.frontend.examples;

import grisu.control.ServiceInterface;
import grisu.frontend.control.login.ServiceInterfaceFactory;
import grisu.frontend.model.job.JobObject;
import grisu.model.GrisuRegistry;
import grisu.model.GrisuRegistryManager;
import grith.jgrith.control.LoginParams;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Resubmit {

	public static void main(final String[] args) throws Exception {

		final ExecutorService executor = Executors.newFixedThreadPool(1);

		final String username = args[0];
		final char[] password = args[1].toCharArray();

		final LoginParams loginParams = new LoginParams(
		// "http://localhost:8080/xfire-backend/services/grisu",
		// "https://ngportal.vpac.org/grisu-ws/soap/EnunciateServiceInterfaceService",
		// "https://ngportaldev.vpac.org/grisu-ws/services/grisu",
				"Local", username, password);

		final ServiceInterface si = ServiceInterfaceFactory
				.createInterface(loginParams);

		final GrisuRegistry registry = GrisuRegistryManager.getDefault(si);

		final String[] subLocs = registry
				.getApplicationInformation("UnixCommands")
				.getAvailableSubmissionLocationsForFqan("/ARCS/NGAdmin")
				.toArray(new String[] {});

		final JobObject createJobObject = new JobObject(si);

		createJobObject.setJobname("Test5");

		createJobObject.setApplication("UnixCommands");
		createJobObject.setCommandline("cat test.txt");
		createJobObject.setWalltimeInSeconds(100);
		createJobObject.setCpus(1);
		createJobObject.addInputFileUrl("/home/markus/test.txt");
		createJobObject.setSubmissionLocation(subLocs[0]);
		System.out.println("Submissionlocation original: " + subLocs[0]);
		System.out.println("Submissionlocation resubmit: " + subLocs[3]);

		createJobObject.createJob("/ARCS/NGAdmin");

		createJobObject.submitJob();

		Thread.sleep(20000);

		createJobObject.setSubmissionLocation(subLocs[3]);
		createJobObject.restartJob();

	}

}
