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
public class JobCreate {

	public static void main(String[] args) throws Exception {

		final ServiceInterface si = LoginManager.loginCommandline("dev");

		final JobObject job = new JobObject(si);
		job.setApplication("UnixCommands");
		job.setUUIDJobname("echo_job");
		job.setCommandline("env");
		job.setWalltimeInSeconds(60);

		job.addEnvironmentVariable("MARKUS", "Markus Binsteiner");

		job.createJob("/ARCS/BeSTGRID");
		job.submitJob();

		System.out.println("Job submission finished finished.");

	}

}
