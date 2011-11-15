import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;
import grisu.model.GrisuRegistry;
import grisu.model.GrisuRegistryManager;
import grisu.model.UserEnvironmentManager;
import grisu.model.info.ResourceInformation;

import java.util.Map;
import java.util.Set;

/**
 * Example code that shows what kinds of information can be queried from the
 * ApplicationInfo object.
 * 
 * @author Markus Binsteiner
 */
public class AvailableExecutables {

	public static void main(String[] args) throws Exception {

		// login
		// in this case we login via the commandline
		final ServiceInterface si = LoginManager.loginCommandline("dev");

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

		final Map<String, Set<String>> exes = uem.getAllAvailableExecutables();
		for (final String app : exes.keySet()) {
			for (final String exe : exes.get(app)) {
				System.out.println(exe);
			}
		}
	}

}
