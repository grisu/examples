package org.vpac.grisu.frontend.examples;

import grisu.control.ServiceInterface;
import grisu.control.exceptions.NoSuchJobException;
import grisu.control.exceptions.ServiceInterfaceException;
import grisu.frontend.control.login.LoginException;
import grisu.frontend.control.login.LoginManager;
import grisu.model.dto.DtoStringList;

public final class CleanAllJobs {

	public static void main(final String[] args)
	throws ServiceInterfaceException, LoginException,
	NoSuchJobException {

		final ServiceInterface si = LoginManager.loginCommandline("Local");

		final DtoStringList allJobnames = si.getAllJobnames(null);

		si.killJobs(allJobnames, true);

	}

	private CleanAllJobs() {
	}

}
