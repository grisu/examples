import grisu.control.ServiceInterface;
import grisu.control.exceptions.NoSuchJobException;
import grisu.frontend.control.login.LoginException;
import grisu.frontend.control.login.LoginManager;
import grisu.frontend.model.job.JobObject;
import grisu.jcommons.constants.Constants;
import grisu.model.dto.DtoJob;
import grisu.model.dto.DtoJobs;

public class GetArchivedJobExample {

	/**
	 * @param args
	 * @throws LoginException
	 * @throws NoSuchJobException
	 */
	public static void main(String[] args) throws LoginException,
	NoSuchJobException {

		// login
		// in this case we login via the commandline
		ServiceInterface si = LoginManager.loginCommandline("BeSTGRID");

		// this method retrieves all archived jobs, regardless of the
		// application
		DtoJobs jobs = si.getArchivedJobs(null);

		for (DtoJob job : jobs.getAllJobs()) {

			System.out.println("Job: "
					+ DtoJob.getProperty(job, Constants.JOBNAME_KEY));

			// or, alternatively:
			JobObject jobObject = new JobObject(si, job);
			System.out.println("Job object: " + jobObject.getJobname());
			System.out.println("\t" + jobObject.getApplication());
			System.out.println("\t" + jobObject.getApplicationVersion());

			System.out.println("\t" + jobObject.getJobDirectoryUrl());

		}

	}
}
