import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginException;
import grisu.frontend.control.login.LoginManager;
import grisu.jcommons.constants.JobSubmissionProperty;
import grisu.jcommons.interfaces.GridResource;
import grisu.jcommons.utils.SubmissionLocationHelpers;
import grisu.model.GrisuRegistry;
import grisu.model.GrisuRegistryManager;
import grisu.model.info.ApplicationInformation;

import java.util.HashMap;
import java.util.Set;

/**
 * Example on how to get submission locations for an application and also
 * submission locations for an application on a per VO basis.
 * 
 * @author Markus Binsteiner
 * 
 */
public class AllSubmissionLocationsForAnApplication {

	public static void main(String[] args) throws LoginException {

		// login
		// in this case we login via the commandline
		// final ServiceInterface si =
		// LoginManager.loginCommandline("BeSTGRID");
		// final ServiceInterface si = LoginManager
		// .loginCommandline("BeSTGRID-DEV");
		final ServiceInterface si = LoginManager.loginCommandline("BeSTGRID");

		// create a registry. the registry is used to get objects that can
		// provide all kinds of grid and user information as well
		// as a filemanager which wraps up the most common file transfer
		// use cases
		final GrisuRegistry registry = GrisuRegistryManager.getDefault(si);

		// getting an ApplicationInformation object
		// those contain all queryable information about a certain
		// application on the grid, e.g. where it is installed, which
		// versions...
		final ApplicationInformation info = registry
				.getApplicationInformation("gold");

		final Set<GridResource> grs = info
				.getAllSubmissionLocationsAsGridResources(
						new HashMap<JobSubmissionProperty, String>(),
						"/ARCS/BeSTGRID/Drug_discovery/Local");

		// Set<GridResource> grs = info.getBestSubmissionLocations(
		// new HashMap<JobSubmissionProperty, String>(), "/ARCS/BeSTGRID");

		for (final GridResource gr : grs) {

			System.out.println(SubmissionLocationHelpers
					.createSubmissionLocationString(gr));

		}

	}

}
