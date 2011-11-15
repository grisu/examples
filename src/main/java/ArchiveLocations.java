import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginException;
import grisu.frontend.control.login.LoginManager;
import grisu.jcommons.constants.Constants;

import org.apache.commons.lang.StringUtils;

/**
 * Archive locations are folders/urls where a user archived jobs after they have
 * finished. Each archive location has a unique alias.
 * 
 * Usually a user would only have one such archive location where all the jobs
 * live she wants to keep, but it's possible to have several of them...
 * 
 * If a job gets archived Grisu automatically adds an archive location to the
 * users' properties. But it is possible to manually add some, for example for
 * jobs that were archived using a different Grisu backend.
 * 
 * This code shows how to manually add an archive location, retieve all
 * currently used archive locations for a user and the remove the manually added
 * one again.
 * 
 * @author Markus Binsteiner
 * 
 */
public class ArchiveLocations {

	/**
	 * @param args
	 * @throws LoginException
	 */
	public static void main(String[] args) throws LoginException {

		// login
		// in this case we login via the commandline
		final ServiceInterface si = LoginManager.loginCommandline("BeSTGRID");

		// add a new job archive location
		si.addArchiveLocation("test", "url of location");

		// print all archive aliases
		System.out.println("Archive aliases: "
				+ StringUtils.join(si.getArchiveLocations().propertiesAsMap()
						.keySet(), " - "));

		// print all archive locations
		System.out.println("Archive locations: "
				+ StringUtils.join(si.getArchiveLocations().propertiesAsMap()
						.values(), " - "));

		// remove the new archive location again
		si.setUserProperty(Constants.JOB_ARCHIVE_LOCATION, "test:");
	}

}
