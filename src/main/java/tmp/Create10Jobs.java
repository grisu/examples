package tmp;

import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;
import grisu.frontend.model.job.JobObject;

/**
 * Example code that shows how to submit a job
 * 
 * @author Markus Binsteiner
 * 
 */
public class Create10Jobs {

	public static void main(String[] args) throws Exception {

		final ServiceInterface si = LoginManager.loginCommandline("local");
		// final ServiceInterface si = LoginManager.loginCommandline("local");

		final int max = 20;

		for (int i = 0; i < max; i++) {
			final JobObject job = new JobObject(si);
			job.setApplication("UnixCommands");
			job.setUUIDJobname("echo");
			job.setCommandline("echo hello");
			job.setWalltimeInSeconds(60);
			job.setJobname("TESTJOB");
			job.createJob("/nz/nesi");
			job.submitJob();
		}
		System.out.println("Job submission finished finished.");

	}

}
