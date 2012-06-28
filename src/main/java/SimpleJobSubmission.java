import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;
import grisu.frontend.model.job.JobObject;

/**
 * Example code that shows how to submit a job
 * 
 * @author Markus Binsteiner
 * 
 */
public class SimpleJobSubmission {

	public static void main(String[] args) throws Exception {

		// login
		// in this case we login via the commandline
		final ServiceInterface si = LoginManager.loginCommandline("Local");

		// here we create a new, empty job
		final JobObject job = new JobObject(si);
		// if we know it, we should set the name of the Application so Grisu
		// does not have to calculate it for us
		job.setApplication("java");
		// if we need a certain version of the application, we need to specify
		// it here
		job.setApplicationVersion("1.6");
		// every job has to have a unique jobname, JobObject (more exactly, it's
		// parent class) provide a few convenience methods to ensure that, like
		// the one below
		job.setUUIDJobname("java_job");
		// most importantly, we need to specify the command we want to run
		// obviously, this example command does not make much sense in an hpc
		// environment...
		job.setCommandline("java -version");
		// also, we need to set a walltime to help the scheduler on the cluster
		// do it's task (default is 600 which is too short for a normal job)
		job.setWalltimeInSeconds(60);

		// now we tell the Grisu backend that we want to create the job
		// this step is necessary so it can determine missing properties
		// (like maybe submission location, whether there are any
		// filesystems that are usable for the job and so on).
		// for that to work we also need to specify the VO we want to use to
		// submit the job
		job.createJob("/ARCS/BeSTGRID");
		// The last step is to actually tell the backend to submit the job
		// to the grid
		// this step also handles all the filestaging that is involved
		job.submitJob();

		System.out.println("Job submission finished finished.");

	}

}
