package debugExamples;
import grisu.X;
import grisu.control.JobConstants;
import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginException;
import grisu.frontend.control.login.LoginManager;
import grisu.model.GrisuRegistry;
import grisu.model.GrisuRegistryManager;
import grisu.model.UserEnvironmentManager;
import grisu.model.dto.DtoJob;
import grith.jgrith.control.DirectMyProxyUpload;

import java.util.Set;

public class ExpiringProxy {

	/**
	 * Example code to display all available MountPoints (aka available
	 * filesystems) for the user and their properties.
	 */
	public static void main(String[] args) throws Exception {

		Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler());

		String username = "markus_test";
		char[] password = "lisaminelly43deanmartin".toCharArray();

		DirectMyProxyUpload.init(args[0].toCharArray(), "myproxy.arcs.org.au",
				7512, username,
				password, null, null, null, null, 360, false);

		X.p("Myproxy cred uploaded.");

		ServiceInterface si = null;
		try {
			si = LoginManager.myProxyLogin("dev", username,
					password);
		} catch (LoginException e) {
			X.p(e.getLocalizedMessage());
			System.exit(1);
		}

		final GrisuRegistry registry = GrisuRegistryManager.getDefault(si);

		final UserEnvironmentManager uem = registry.getUserEnvironmentManager();

		X.p("Logged in.");

		boolean stop = false;
		while (!stop) {
			Set<DtoJob> jobs = null;

			jobs = uem.getCurrentJobs(true);

			int running = 0;
			int done = 0;
			for (DtoJob j : jobs) {
				if (j.getStatus() < JobConstants.FINISHED_EITHER_WAY) {
					running = running + 1;
				} else {
					done = done + 1;
				}
			}
			X.p("Running: " + running);
			X.p("Done: " + done);

			Thread.sleep(2000);

		}


	}
}
