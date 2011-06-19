import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginException;
import grisu.frontend.control.login.LoginManager;
import grisu.model.GrisuRegistry;
import grisu.model.GrisuRegistryManager;
import grisu.model.UserEnvironmentManager;

/**
 * Example on how to get all applications that are available for a user
 * 
 * @author Markus Binsteiner
 * 
 */
public class AllApplicationsForUser {

	public static void main(String[] args) throws LoginException {

		// login
		// in this case we login via the commandline
		// final ServiceInterface si =
		// LoginManager.loginCommandline("BeSTGRID");
		final ServiceInterface si = LoginManager
				.loginCommandline("BeSTGRID-DEV");
		// final ServiceInterface si = LoginManager.loginCommandline("Local");

		// create a registry. the registry is used to get objects that can
		// provide all kinds of grid and user information as well
		// as a filemanager which wraps up the most common file transfer
		// use cases
		final GrisuRegistry registry = GrisuRegistryManager.getDefault(si);

		// getting an userenvironmentmanager
		UserEnvironmentManager uem = registry.getUserEnvironmentManager();

		String[] apps = uem.getAllAvailableApplications();

		for (String app : apps) {

			System.out.println(app);

		}

	}

}
