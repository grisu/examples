package org.vpac.grisu.frontend.examples;

import grisu.control.ServiceInterface;
import grisu.control.exceptions.JobPropertiesException;
import grisu.control.exceptions.NoSuchJobException;
import grisu.control.exceptions.RemoteFileSystemException;
import grisu.control.exceptions.ServiceInterfaceException;
import grisu.frontend.control.login.LoginException;
import grisu.frontend.control.login.LoginManager;
import grisu.jcommons.constants.Constants;
import grisu.model.dto.DtoJobs;

public final class ArchiveJob {

	public static void main(final String[] args)
			throws ServiceInterfaceException, LoginException,
			NoSuchJobException {

		ServiceInterface si = null;

		si = LoginManager.loginCommandline();

		si.setUserProperty(
				Constants.DEFAULT_JOB_ARCHIVE_LOCATION,
				"gsiftp://ng2.vpac.org/home/grid-startup/DC_au_DC_org_DC_arcs_DC_slcs_O_VPAC_CN_Markus_Binsteiner_qTrDzHY7L1aKo3WSy8623-7bjgM/archive");

		final DtoJobs test = si.getActiveJobs(null, true);

		// System.out.println("ps");
		// for (DtoJob job : test.getAllJobs()) {
		// System.out.println(job.jobname());
		// }

		System.out.println("alljobnames");
		for (final String name : si.getAllJobnames(null).asArray()) {
			System.out.println(name);
		}

		System.out.println("all multipartjobnames");
		for (final String name : si.getAllBatchJobnames(null).asArray()) {
			System.out.println(name);
		}

		for (final String jobToArchive : si.getAllJobnames(null).asArray()) {

			System.out.println("Job to archive: " + jobToArchive);

			try {
				si.archiveJob(jobToArchive, null);
			} catch (final JobPropertiesException e) {
				e.printStackTrace();
			} catch (final RemoteFileSystemException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Finished.");

	}

	private ArchiveJob() {
	}

}
