package debugExamples;
import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;
import grisu.frontend.model.job.JobObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Example code that shows how to submit a job
 * 
 * @author Markus Binsteiner
 * 
 */
public class NoResourceAvailable {

	public static void main(String[] args) throws Exception {

		final ServiceInterface si = LoginManager.loginCommandline("local");

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input = "";
		while (!input.equals("q")) {

			final JobObject job = new JobObject(si);
			job.setApplication("UnixCommands");
			job.setUniqueJobname("debug_resource_job");
			job.setCommandline("echo hello");
			job.setWalltimeInSeconds(3600 * 24 * 10000);

			job.createJob("/nz/nesi");

			job.submitJob();

			System.out.println("Job submission finished finished.");

			input = br.readLine();

		}

		System.exit(0);

	}

}
