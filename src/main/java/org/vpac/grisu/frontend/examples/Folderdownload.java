package org.vpac.grisu.frontend.examples;

import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;
import grisu.model.FileManager;
import grisu.model.GrisuRegistry;
import grisu.model.GrisuRegistryManager;

import java.io.File;

import org.apache.commons.io.FileUtils;

public class Folderdownload {

	public static void main(String[] args) throws Exception {

		final File target = new File("/home/markus/Desktop/temp");
		FileUtils.deleteDirectory(target);

		// ServiceInterface si = LoginManager.loginCommandline();
		final ServiceInterface si = LoginManager
				.loginCommandline(LoginManager.SERVICEALIASES.get("LOCAL"));

		final GrisuRegistry registry = GrisuRegistryManager.getDefault(si);

		final FileManager fm = registry.getFileManager();

		fm.downloadUrl("gsiftp://ng2.vpac.org/home/acc004/us", target.toURI()
				.toString(), true);

		System.out.println("Main thread finished.");
	}

}
