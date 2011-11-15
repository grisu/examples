import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;
import grisu.model.GrisuRegistry;
import grisu.model.GrisuRegistryManager;
import grisu.model.info.ApplicationInformation;
import grisu.model.info.ResourceInformation;

import java.util.Set;

/**
 * Example code that shows what kinds of information can be queried from the
 * ApplicationInfo object.
 * 
 * @author Markus Binsteiner
 */
public class ApplicationInfo {

	public static void main(String[] args) throws Exception {

		// login
		// in this case we login via the commandline
		final ServiceInterface si = LoginManager.loginCommandline("BeSTGRID");

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

		// now we retrieve all applications available on the grid
		final Set<String> apps = ri.getAllApplications();

		for (final String app : apps) {

			// for every application, we print out some details
			System.out.println("Application: " + app);

			// getting an ApplicationInformation object
			// those contain all queryable information about a certain
			// application on the grid, e.g. where it is installed, which
			// versions...
			final ApplicationInformation info = registry
					.getApplicationInformation(app);

			// now we get all the submissionlocations for this application on
			// the grid a submission location is a string that contains the
			// queue and grid gateway to be used to submit a job. the format is:
			//
			// queue:gateway[#SchedulerType] (default SchedulerType is 'PBS')
			//
			// example would be:
			// grid_aix:ng2hpc.canterbury.ac.nz#Loadleveler
			for (final String subLoc : info
					.getAvailableAllSubmissionLocations()) {

				System.out.println("\tSubmission location: " + subLoc);
				System.out.println("\t Versions:");
				// now we query for all available versions of this application
				// on the specified submission location
				for (final String version : info.getAvailableVersions(subLoc)) {
					System.out.println("\t\t" + version);
					System.out.println("\t\t\tDetails:");
					// and we want to find out whatever else can be queried for
					// this version of the application on this submission
					// location
					for (final String key : info.getApplicationDetails(subLoc,
							version).keySet()) {
						System.out.println("\t\t\t\t"
								+ key
								+ ":\t"
								+ info.getApplicationDetails(subLoc, version)
										.get(key));
					}

				}
				System.out.println();

			}
			System.out.println();
			System.out.println();

		}

	}

}
