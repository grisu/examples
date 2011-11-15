package org.vpac.grisu.frontend.examples;

import grisu.control.ServiceInterface;
import grisu.control.exceptions.NoSuchJobException;
import grisu.control.exceptions.ServiceInterfaceException;
import grisu.frontend.control.login.LoginException;
import grisu.frontend.control.login.LoginManager;
import grisu.model.dto.DtoJob;
import grisu.model.dto.DtoJobs;

public final class JobList {

	public static void main(final String[] args)
			throws ServiceInterfaceException, LoginException,
			NoSuchJobException {

		final ServiceInterface si = LoginManager.loginCommandline("ARCS");

		final DtoJobs test = si.getActiveJobs(null, true);

		System.out.println("ps");
		for (final DtoJob job : test.getAllJobs()) {
			System.out.println(job.jobname());
		}

		System.out.println("alljobnames");
		for (final String name : si.getAllJobnames(null).asArray()) {
			System.out.println(name);
		}

		System.out.println("all multipartjobnames");
		for (final String name : si.getAllBatchJobnames(null).asArray()) {
			System.out.println(name);
		}

		for (int i = 0; i < 20; i++) {
			System.out.println("Rechecking...");
			try {
				Thread.sleep(2000);
			} catch (final InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			si.getActiveJobs(null, true);
		}
	}

	private JobList() {
	}

}
