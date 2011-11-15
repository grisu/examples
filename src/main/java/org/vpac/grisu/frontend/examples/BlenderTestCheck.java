package org.vpac.grisu.frontend.examples;

import grisu.control.ServiceInterface;
import grisu.frontend.control.login.ServiceInterfaceFactory;
import grisu.frontend.model.job.BatchJobObject;
import grith.jgrith.control.LoginParams;

import java.io.File;
import java.util.Date;

public class BlenderTestCheck {

	public static void main(final String[] args) throws Exception {

		final String username = args[0];
		final char[] password = args[1].toCharArray();

		final Date startDate = new Date();

		final LoginParams loginParams = new LoginParams(
		// "http://localhost:8080/xfire-backend/services/grisu",
		// "https://ngportal.vpac.org/grisu-ws/soap/EnunciateServiceInterfaceService",
		// "https://ngportal.vpac.org/grisu-ws/services/grisu",
				"Local", username, password);

		final ServiceInterface si = ServiceInterfaceFactory
				.createInterface(loginParams);

		final String multiJobName = "PerformanceTest6";

		final BatchJobObject newObject = new BatchJobObject(si, multiJobName,
				false);

		newObject.monitorProgress(15, null, true);

		newObject.downloadResults(true, new File(
				"/home/markus/Desktop/multiTest"), new String[] { "stdout" },
				false, true);

		final Date endDate = new Date();

		System.out.println("Started: " + startDate.toString());
		System.out.println("Ended: " + endDate.toString() + "\n");

		for (final Date date : newObject.getLogMessages(false).keySet()) {
			System.out.println(date.toString() + ": "
					+ newObject.getLogMessages(false).get(date));
		}

	}

}
