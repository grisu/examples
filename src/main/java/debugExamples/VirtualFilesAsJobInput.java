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
public class VirtualFilesAsJobInput {

	public static void main(String[] args) throws Exception {


		final ServiceInterface si = LoginManager.loginCommandline("Local");

		final JobObject job = new JobObject(si);
		job.setApplication("UnixCommands");
		job.setCommandline("find .");

		job.setWalltimeInSeconds(60);

		job.addInputFileUrl("grid://groups/nz/nesi/reindent.py");
		// job.addInputFileUrl(
		// "gsiftp://df.auckland.ac.nz/BeSTGRID/home/markus.binsteiner2/reindent.py",
		// "test/whatever");

		job.createJob("/ARCS/BeSTGRID");

		job.submitJob();

		job.waitForJobToFinish(4);

		System.out.println(job.getStdOutContent());

		System.out.println("Job submission finished finished.");

	}

}
