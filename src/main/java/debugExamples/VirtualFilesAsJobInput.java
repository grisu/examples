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
		job.setCommandline("cat reindent.py");

		job.setWalltimeInSeconds(60);

		// job.addInputFileUrl("grid://jobs/active/grisu_job_24/reindent.py");
		job.addInputFileUrl("grid://jobs/archived/grisu_job_25/reindent.py");
		// job.addInputFileUrl("gsiftp://ng2.auckland.ac.nz/home/grid-bestgrid/DC_nz_DC_org_DC_bestgrid_DC_slcs_O_The_University_of_Auckland_CN_Markus_Binsteiner__bK32o4Lh58A3vo9kKBcoKrJ7ZY/active-jobs/grisu_job_13/su_job.e124148");
		// job.addInputFileUrl("grid://groups/nz/nesi/reindent.py");
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
