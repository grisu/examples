package org.vpac.grisu.frontend.examples;

import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;
import grisu.model.FileManager;
import grisu.model.GrisuRegistry;
import grisu.model.GrisuRegistryManager;
import grisu.model.dto.GridFile;

import java.util.Date;

public class FileListingTest {

	public static void main(final String[] args) throws Exception {

		final Date start = new Date();

		final ServiceInterface si = LoginManager.loginCommandline();

		final GrisuRegistry registry = GrisuRegistryManager.getDefault(si);
		final FileManager fileManager = registry.getFileManager();

		final GridFile folder = fileManager
				.ls("gsiftp://ng2.vpac.org/home/grid-admin/DC_au_DC_org_DC_arcs_DC_slcs_O_VPAC_CN_Markus_Binsteiner_qTrDzHY7L1aKo3WSy8623-7bjgM/grid-test-jobs/blast_2.2.21_d4fd6c4c-25e6-4cf0-8898-93cef219e996",
						1);

		for (final GridFile file : folder.getChildren()) {
			System.out.println("Name: " + file.getName() + ", size: "
					+ file.getSize());
		}

		System.out.println("Started: " + start);
		System.out.println("Finished: " + new Date().toString());

	}

}
