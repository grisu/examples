import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;
import grisu.model.GrisuRegistry;
import grisu.model.GrisuRegistryManager;
import grisu.model.MountPoint;
import grisu.model.UserEnvironmentManager;

public class MountPointInfo {

	/**
	 * Example code to display all available MountPoints (aka available
	 * filesystems) for the user and their properties.
	 */
	public static void main(String[] args) throws Exception {

		// login
		// in this case we login via the commandline
		final ServiceInterface si = LoginManager.loginCommandline("BeSTGRID");

		// create a registry. the registry is used to get objects that can
		// provide all kinds of grid and user information as well
		// as a filemanager which wraps up the most common file transfer
		// use cases
		final GrisuRegistry registry = GrisuRegistryManager.getDefault(si);

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
		}

	}
}
