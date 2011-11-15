import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;
import grisu.model.GrisuRegistry;
import grisu.model.GrisuRegistryManager;
import grisu.model.MountPoint;
import grisu.model.UserEnvironmentManager;
import grisu.model.info.ResourceInformation;

/**
 * Example code that shows what kinds of information can be queried from the
 * ApplicationInfo object.
 * 
 * @author Markus Binsteiner
 */
public class AvailableSubmissionLocations {

	public static void main(String[] args) throws Exception {

		// login
		// in this case we login via the commandline
		final ServiceInterface si = LoginManager
				.loginCommandline("BeSTGRID-DEV");

		// create a registry. the registry is used to get objects that can
		// provide all kinds of grid and user information as well
		// as a filemanager which wraps up the most common file transfer
		// use cases
		final GrisuRegistry registry = GrisuRegistryManager.getDefault(si);

		// create a ResourceInformation object
		// this contains information about resources available on the grid,
		// e.g. which sites are available, which applications, which
		// filesystems...
		final ResourceInformation ri = registry.getResourceInformation();

		final UserEnvironmentManager uem = registry.getUserEnvironmentManager();

		for (final String fqan : uem.getAllAvailableFqans()) {
			System.out.println("VO: " + fqan);
			for (final String subLoc : ri
					.getAllAvailableSubmissionLocations(fqan)) {
				System.out.println("\t" + subLoc);
			}
			System.out.println();
		}

		for (final String subLoc : ri.getAllSubmissionLocations()) {
			System.out.println("SubLoc: " + subLoc);
			for (final String sfs : ri
					.getStagingFilesystemsForSubmissionLocation(subLoc)) {
				System.out.println("\t" + sfs);
			}
			System.out.println();
		}

		for (final MountPoint mp : uem.getMountPoints()) {
			if ("/ARCS/BeSTGRID".equals(mp.getFqan())) {
				System.out.println("Mountpoint: " + mp.getAlias());
				System.out.println("\t" + mp.getRootUrl());
				System.out.println("\t" + mp.getFqan());
				System.out.println();
			}
		}

	}

}
