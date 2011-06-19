import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;
import grisu.model.FileManager;
import grisu.model.GrisuRegistry;
import grisu.model.GrisuRegistryManager;
import grisu.model.MountPoint;
import grisu.model.UserEnvironmentManager;
import grisu.model.dto.GridFile;

import java.util.Date;

public class MountPointInfo {

	/**
	 * Example code to display all available MountPoints (aka available
	 * filesystems) for the user and their properties.
	 */
	public static void main(String[] args) throws Exception {

		// login
		// in this case we login via the commandline
		final ServiceInterface si = LoginManager
				.loginCommandline("BeSTGRID-DEV");
		// final ServiceInterface si = LoginManager.loginCommandline("Local");

		// create a registry. the registry is used to get objects that can
		// provide all kinds of grid and user information as well
		// as a filemanager which wraps up the most common file transfer
		// use cases
		final GrisuRegistry registry = GrisuRegistryManager.getDefault(si);

		final FileManager fm = registry.getFileManager();

		// or we can query which VOs a user can use to submit a job for a
		// certain application
		// for that we need a UserEnvironmentManager object, which contains
		// user-specific information
		// that is related to the grid
		UserEnvironmentManager uem = registry.getUserEnvironmentManager();

		System.out.println("All available mountpoints:\n");
		// getting all available mountpoints for this particular user
		for (MountPoint mp : uem.getMountPoints()) {
			System.out.println("\t" + mp.getAlias() + "\t" + mp.getFqan()
					+ "\t\t" + mp.getRootUrl());
			System.out.println("\tIs volatile: " + mp.isVolatileFileSystem());
		}

		System.out.println("\n");

		for (MountPoint mp : uem.getMountPoints()) {
			Date start = null;
			Date end = null;
			try {
				System.out
				.print("Accessing MountPoint \"" + mp.getAlias()
						+ "\": ");

				start = new Date();
				//				boolean folder = fm.isFolder(mp.getRootUrl());
				GridFile f = fm.ls(mp.getRootUrl());
				end = new Date();
				System.out.println("success");
				if (f.isFolder()) {
					System.out.println("\tTime: "
							+ (end.getTime() - start.getTime()) + " ms " + "("
							+ f.getChildren().size() + " children)");
				} else {
					System.out.println("\tTime: "
							+ (end.getTime() - start.getTime())
							+ " ms (Result: not a folder!)");
				}
			} catch (Exception e) {
				end = new Date();
				System.out.println("failed\n\t( " + e.getLocalizedMessage()
						+ " )");
				System.out.println("\tTime: "
						+ (end.getTime() - start.getTime()) + " ms ");
			}
			System.out.println("\n");
		}

	}
}
