package debugExamples;

import grisu.control.ServiceInterface;
import grisu.control.exceptions.JobPropertiesException;
import grisu.control.exceptions.RemoteFileSystemException;
import grisu.frontend.control.login.LoginException;
import grisu.frontend.control.login.LoginManager;
import grisu.frontend.model.job.JobObject;
import grisu.model.FileManager;
import grisu.model.GrisuRegistry;
import grisu.model.GrisuRegistryManager;
import grisu.settings.ClientPropertiesManager;
import grith.jgrith.credential.Credential;
import grith.jgrith.credential.X509Credential;

import java.util.List;

import com.google.common.collect.Lists;

public class ConcurrentSubmissionsDifferentUsers {

	/**
	 * Commented example code on how to submit a job and then access and work
	 * with the results.
	 * @throws InterruptedException
	 * @throws JobPropertiesException
	 */
	public static void main(String[] args) throws RemoteFileSystemException, InterruptedException, JobPropertiesException {

		ClientPropertiesManager.setConcurrentUploadThreads(10);

		System.out.println("Logging in...");

		String backend = "dev";
		String dev = "dev";
		String tomcat = "http://compute-dev.services.bestgrid.org:8080/grisu-ws/soap/GrisuService";
		String http = "http://compute-dev.services.bestgrid.org/soap/GrisuService";
		backend = http;

		Credential c1 = new X509Credential(
				"/home/markus/certs/test2.ceres.auckland.ac.nz_cert.pem",
				"/home/markus/certs/test2.ceres.auckland.ac.nz_key.pem",
				"".toCharArray(), 12, true);

		Credential c2 = new X509Credential(
				"/home/markus/certs/test2.ceres.auckland.ac.nz_cert.pem",
				"/home/markus/certs/test2.ceres.auckland.ac.nz_key.pem",
				"".toCharArray(), 12, true);

		ServiceInterface si1 = null;
		ServiceInterface si2 = null;
		try {
			si1 = LoginManager.login(c1, "bestgrid-test", false);
			si2 = LoginManager.login(c2, "bestgrid-test", false);
		} catch (final LoginException e) {
			System.err.println("Could not login: " + e.getLocalizedMessage());
			System.exit(1);
		}


		final GrisuRegistry registry1 = GrisuRegistryManager.getDefault(si1);
		final GrisuRegistry registry2 = GrisuRegistryManager.getDefault(si2);
		final FileManager fm1 = registry1.getFileManager();
		final FileManager fm2 = registry2.getFileManager();

		List<ServiceInterface> sis = Lists.newArrayList();
		List<JobObject> jobs = Lists.newArrayList();
		sis.add(si1);
		sis.add(si2);


		//for ( final ServiceInterface si : sis ) {
		for ( int i=0; i<sis.size(); i++) {
			final ServiceInterface si = sis.get(i);
			final int j = i;
			Thread t = new Thread() {
				@Override
				public void run() {
					//System.out.println(si.getDN());
					if ( j == 0 ) {
						// si.getAllHosts();
						System.out.println("00000");
					} else {
						si.getDN();
						System.out.println("1111");
					}

				}
			};
			t.setName("XXX_"+i);
			t.start();
			//			Thread.sleep(600);
		}
		//		Thread.sleep(800);
		//		System.out.println(sis.get(0).getDN());

		//		if ( true) {
		//			System.out.println("Exit");
		//			System.exit(0);
		//		}
		//
		//		System.out.println("Creating job...");
		//
		//
		//		for (int i = 0; i < sis.size(); i++) {
		//			ServiceInterface si = sis.get(i);
		//			final JobObject job = new JobObject(si);
		//			job.setApplication("UnixCommands");
		//			job.setUniqueJobname("yyyJob-"+i);
		//
		//			// job.addInputFileUrl("/home/markus/test/test.txt");
		//
		//			System.out.println("Set jobname to be: " + job.getJobname());
		//			job.setCommandline("find .");
		//
		//			job.setWalltimeInSeconds(60);
		//			jobs.add(job);
		//		}
		//
		//		for (JobObject job : jobs) {
		//
		//				System.out.println("Creating job on backend...");
		//				job.createJob("/nz/nesi");
		////				Thread.sleep(2000);
		//
		//
		//		}
		//
		//		// try {
		//		// System.out.println("Submitting job to the grid...");
		//		// job.submitJob();
		//		// } catch (final JobSubmissionException e) {
		//		// System.err.println("Could not submit job: "
		//		// + e.getLocalizedMessage());
		//		// System.exit(1);
		//		// } catch (final InterruptedException e) {
		//		// System.err.println("Jobsubmission interrupted: "
		//		// + e.getLocalizedMessage());
		//		// System.exit(1);
		//		// }
		//		//
		//		// System.out.println("Job submission finished.");
		//		// System.out.println("Job submitted to: "
		//		// + job.getJobProperty(Constants.SUBMISSION_SITE_KEY));
		//		//
		//		// System.out.println("Waiting for job to finish...");
		//		//
		//		// job.waitForJobToFinish(5);
		//		//
		//		// System.out.println("Job finished with status: "
		//		// + job.getStatusString(false));
		//		//
		//		// System.out.println("List job directory: ");
		//		//
		//		// final GridFile jobDirectory = job.listJobDirectory();
		//		//
		//		// for (final GridFile file : jobDirectory.getChildren()) {
		//		// System.out.println("Name: " + file.getName() + " / URL: "
		//		// + file.getUrl());
		//		// }
		//		//
		//		// System.out.println("Stdout: " + job.getStdOutContent());
		//		// System.out.println("Stderr: " + job.getStdErrContent());
		//
		//		System.exit(0);

	}
}
