package devHelpers;
import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;
import grisu.model.FileManager;
import grisu.model.GrisuRegistry;
import grisu.model.GrisuRegistryManager;
import grisu.model.UserEnvironmentManager;
import grisu.model.dto.GridFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;

public class FileListingExample {

	/**
	 * This example shows how to implement a (very basic) commandline file
	 * browser.
	 * 
	 * Users enter urls and it lists children files of those urls.
	 */
	public static void main(String[] args) throws Exception {

		// login
		// in this case we login via the commandline
		ServiceInterface si = LoginManager.loginCommandline("LOCAL");

		// create a registry. the registry is used to get objects that can
		// provide all kinds of grid and user information as well
		// as a filemanager which wraps up the most common file transfer
		// use cases
		final GrisuRegistry registry = GrisuRegistryManager.getDefault(si);

		// getting a filemanager object, which encapsulates file related actions
		FileManager fm = registry.getFileManager();

		// or we can query which VOs a user can use to submit a job for a
		// certain application
		// for that we need a UserEnvironmentManager object, which contains
		// user-specific information
		// that is related to the grid
		UserEnvironmentManager uem = registry.getUserEnvironmentManager();

		String input = "grid://";
		while (!"exit".equals(input)) {

			// reading user input
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));

			try {
				System.out.println("Enter url to list: ");
				String lastInput = input;
				input = br.readLine();
				if (StringUtils.isBlank(input)) {
					input = lastInput;
					System.out.println("Using last input: " + lastInput);
				} else if ("exit".equals(input)) {
					System.exit(0);
				}
			} catch (IOException ioe) {
				System.out.println("IO error trying to read user input!");
				System.exit(1);
			}

			try {
				// we use the filemanager to list the specified url
				GridFile f = fm.ls(input);
				for (GridFile c : f.getChildren()) {
					// we can print some more details of the file
					System.out.println("Child:\t" + c.getName());
					System.out.println("\tPath: " + c.getPath());
					System.out.println("\turl: " + c.getUrl());
					System.out.println("\tSites: "
							+ StringUtils.join(c.getSites(), " / "));
					System.out.println("\tVOs: "
							+ StringUtils.join(c.getFqans(), " / "));

					System.out.println("\tVirtual: " + c.isVirtual());
				}
			} catch (Exception e) {
				System.out.println(e.getLocalizedMessage());
			}

		}

	}
}
