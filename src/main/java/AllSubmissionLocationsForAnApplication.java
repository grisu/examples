import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginException;
import grisu.frontend.control.login.LoginManager;
import grisu.model.GrisuRegistry;
import grisu.model.GrisuRegistryManager;
import grisu.model.UserEnvironmentManager;
import grisu.model.info.ApplicationInformation;

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
		ApplicationInformation info = registry
		.getApplicationInformation("UnixCommands");

		// now we get all the submissionlocations for this application on the
		// grid
		// a submission location is a string that contains the queue and grid
		// gateway
		// to be used to submit a job. the format is:
		// queue:gateway[#SchedulerType] (default SchedulerType is 'PBS')
		// example would be:
		// grid_aix:ng2hpc.canterbury.ac.nz#Loadleveler
		Set<String> allSubLocs = info.getAvailableAllSubmissionLocations();

		for (String subLoc : allSubLocs) {
			System.out.println("Submission location for UnixCommands: "
					+ subLoc);
		}

		// or we can query which VOs a user can use to submit a job for a certain application
		// for that we need a UserEnvironmentManager object, which contains user-specific information
		// that is related to the grid
		UserEnvironmentManager uem = registry.getUserEnvironmentManager();
		Set<String> allVos = uem.getAllAvailableFqansForApplication("blast");

		System.out.println("Submission locations for 'blast' (per VO):");
		for (String vo : allVos) {
			System.out.println("VO: " + vo);
			Set<String> subLocs = info.getAvailableSubmissionLocationsForFqan(vo);
			for (String subLoc : subLocs) {
				System.out.println("\t" + subLoc);
			}
		}
	}

}
