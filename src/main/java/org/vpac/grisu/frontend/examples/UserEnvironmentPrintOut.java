package org.vpac.grisu.frontend.examples;

import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;
import grisu.model.GrisuRegistry;
import grisu.model.GrisuRegistryManager;
import grisu.model.UserEnvironmentManager;
import grisu.model.files.FileSystemItem;
import grisu.model.info.ApplicationInformation;

import java.util.Date;

public class UserEnvironmentPrintOut {

	public static void main(final String[] args) throws Exception {

		final Date start = new Date();

		final ServiceInterface si = LoginManager.loginCommandline();

		final GrisuRegistry registry = GrisuRegistryManager.getDefault(si);
		final UserEnvironmentManager uem = GrisuRegistryManager.getDefault(si)
				.getUserEnvironmentManager();

		for (final FileSystemItem item : uem.getFileSystems()) {
			System.out.println(item.getAlias());
			System.out.println(item.getRootFile().getUrl());
			System.out.println(item.getType());
		}

		final ApplicationInformation info = registry
				.getApplicationInformation("gold");

		for (final String subLoc : info.getAvailableAllSubmissionLocations()) {
			System.out.println(subLoc);
		}

		System.out.println("XXXXX");
		for (final String subLoc : info
				.getAvailableSubmissionLocationsForFqan("/ARCS/BeSTGRID/Drug_discovery/Local")) {
			System.out.println(subLoc);
		}

		System.out.println("Started: " + start);
		System.out.println("Finished: " + new Date().toString());

	}
}
