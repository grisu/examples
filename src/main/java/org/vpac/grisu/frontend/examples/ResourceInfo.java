package org.vpac.grisu.frontend.examples;

import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;
import grisu.model.GrisuRegistry;
import grisu.model.GrisuRegistryManager;
import grisu.model.info.ApplicationInformation;

public class ResourceInfo {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		final ServiceInterface si = LoginManager.loginCommandline();

		final GrisuRegistry registry = GrisuRegistryManager.getDefault(si);

		for (final String app : args) {

			System.out.println("Application: " + app);
			final ApplicationInformation info = registry
					.getApplicationInformation(app);

			for (final String subLoc : info
					.getAvailableAllSubmissionLocations()) {

				System.out.println("\tSubmission location: " + subLoc);
				System.out.println("\t Versions:");
				for (final String version : info.getAvailableVersions(subLoc)) {
					System.out.println("\t\t" + version);
					System.out.println("\t\t\tDetails:");
					for (final String key : info.getApplicationDetails(subLoc,
							version).keySet()) {
						System.out.println("\t\t\t\t"
								+ key
								+ ":\t"
								+ info.getApplicationDetails(subLoc, version)
										.get(key));
					}

				}
				System.out.println();

			}
			System.out.println();
			System.out.println();

		}

	}

}
