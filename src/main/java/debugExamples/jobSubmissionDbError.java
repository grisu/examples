package debugExamples;

import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;
import grisu.frontend.model.job.JobObject;

/**
 * Example code that shows how to submit a job
 * 
 * @author Markus Binsteiner
 * 
 */
public class jobSubmissionDbError {

	public static void main(String[] args) throws Exception {

		// login
		// in this case we login via the commandline
		final ServiceInterface si = LoginManager.loginCommandline("dev");

		// here we create a new, empty job
		final JobObject job = new JobObject(si);
		job.setApplication("UnixCommands");
		job.setCommandline("echo hello");

		job.setWalltimeInSeconds(60);

		job.createJob("/nz/nesi");

		job.submitJob();

		System.out.println("Job submission finished finished.");

	}

}
