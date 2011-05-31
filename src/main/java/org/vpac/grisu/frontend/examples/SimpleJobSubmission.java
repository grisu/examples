package org.vpac.grisu.frontend.examples;

import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;
import grisu.frontend.model.job.JobObject;
import grisu.model.GrisuRegistry;
import grisu.model.GrisuRegistryManager;

public class SimpleJobSubmission {

	public static void main(String[] args) throws Exception {

		// ServiceInterface si = LoginManager.loginCommandline();
		final ServiceInterface si = LoginManager
		.loginCommandline(LoginManager.SERVICEALIASES.get("LOCAL"));

		final GrisuRegistry registry = GrisuRegistryManager.getDefault(si);

		for (final String subLoc : registry.getResourceInformation()
				.getAllSubmissionLocations()) {
			System.out.println(subLoc);
		}

		// JobListDialog.open(si, null);

		final JobObject job = new JobObject(si);
		job.setApplication("java");
		job.setApplicationVersion("1.6.0-06");
		job.setUUIDJobname("java_job2_sfasfsafsadfsadffawefawefwfsdfsdfsdafsdfsdafsdfsd");
		job.setCommandline("java -version");

		job.setWalltimeInSeconds(60);

		job.addInputFileUrl("gsiftp://ng2hpc.ceres.auckland.ac.nz/home/grid-admin/DC_au_DC_org_DC_arcs_DC_slcs_O_ARCS_IdP_CN_Markus_Binsteiner_wArwA1Ra5jf6RG_HIZy45nORR_g/tmp/text0.txt");
		job.addInputFileUrl("gsiftp://ng2hpc.ceres.auckland.ac.nz/home/grid-admin/DC_au_DC_org_DC_arcs_DC_slcs_O_ARCS_IdP_CN_Markus_Binsteiner_wArwA1Ra5jf6RG_HIZy45nORR_g/tmp/text1.txt");
		job.addInputFileUrl("gsiftp://ng2hpc.ceres.auckland.ac.nz/home/grid-admin/DC_au_DC_org_DC_arcs_DC_slcs_O_ARCS_IdP_CN_Markus_Binsteiner_wArwA1Ra5jf6RG_HIZy45nORR_g/tmp/text2.txt");
		job.addInputFileUrl("gsiftp://ng2hpc.ceres.auckland.ac.nz/home/grid-admin/DC_au_DC_org_DC_arcs_DC_slcs_O_ARCS_IdP_CN_Markus_Binsteiner_wArwA1Ra5jf6RG_HIZy45nORR_g/tmp/text3.txt");
		job.addInputFileUrl("gsiftp://ng2hpc.ceres.auckland.ac.nz/home/grid-admin/DC_au_DC_org_DC_arcs_DC_slcs_O_ARCS_IdP_CN_Markus_Binsteiner_wArwA1Ra5jf6RG_HIZy45nORR_g/tmp/text4.txt");
		job.addInputFileUrl("gsiftp://ng2hpc.ceres.auckland.ac.nz/home/grid-admin/DC_au_DC_org_DC_arcs_DC_slcs_O_ARCS_IdP_CN_Markus_Binsteiner_wArwA1Ra5jf6RG_HIZy45nORR_g/tmp/text5.txt");

		job.createJob("/ARCS/NGAdmin");
		job.submitJob();

		System.out.println("Main thread finished.");
	}

}
