package org.vpac.grisu.frontend.examples;

import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;
import grisu.model.FileManager;
import grisu.model.GrisuRegistry;
import grisu.model.GrisuRegistryManager;

import java.io.File;
import java.util.Date;

public class UploadLocalFolder {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		final ServiceInterface si = LoginManager
				.loginCommandline(LoginManager.SERVICEALIASES.get("local"));

		final GrisuRegistry registry = GrisuRegistryManager.getDefault(si);

		final FileManager fm = registry.getFileManager();

		fm.deleteFile("gsiftp://ng2.vpac.org/home/acc004/test/");

		final Date start = new Date();

		fm.cp(new File("/home/markus/Workspaces/Wicket"),
				"gsiftp://ng2.vpac.org/home/acc004/test/", true);

		final Date end = new Date();

		System.out.println("Time: " + (end.getTime() - start.getTime()));
	}

}
