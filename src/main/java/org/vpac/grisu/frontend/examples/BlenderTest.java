package org.vpac.grisu.frontend.examples;

import grisu.control.JobConstants;
import grisu.control.ServiceInterface;
import grisu.frontend.control.login.ServiceInterfaceFactory;
import grisu.frontend.model.events.JobStatusEvent;
import grisu.frontend.model.job.BatchJobObject;
import grisu.frontend.model.job.JobObject;
import grisu.frontend.model.job.JobsException;
import grisu.jcommons.constants.JobSubmissionProperty;
import grisu.jcommons.interfaces.GridResource;
import grisu.jcommons.utils.SubmissionLocationHelpers;
import grisu.model.GrisuRegistry;
import grisu.model.GrisuRegistryManager;
import grisu.model.info.ApplicationInformation;
import grith.jgrith.control.LoginParams;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

public class BlenderTest {

	public static void main(final String[] args) throws Exception {

		final BlenderTest test = new BlenderTest();

		final ExecutorService executor = Executors.newFixedThreadPool(10);

		final String username = args[0];
		final char[] password = args[1].toCharArray();

		final LoginParams loginParams = new LoginParams(
		// "http://localhost:8080/xfire-backend/services/grisu",
		// "https://ngportal.vpac.org/grisu-ws/soap/EnunciateServiceInterfaceService",
		// "https://ngportal.vpac.org/grisu-ws/services/grisu",
		// "https://ngportal.vpac.org/grisu-ws/soap/GrisuService",
		// "http://localhost:8080/enunciate-backend/soap/GrisuService",
				"Local",
				// "Dummy",
				username, password);

		final ServiceInterface si = ServiceInterfaceFactory
				.createInterface(loginParams);

		final GrisuRegistry registry = GrisuRegistryManager.getDefault(si);

		final ApplicationInformation blenderInfo = registry
				.getApplicationInformation("blender");
		final Set<String> versions = blenderInfo
				.getAllAvailableVersionsForFqan("/ARCS/NGAdmin");

		final Map<JobSubmissionProperty, String> jobProperties = new HashMap<JobSubmissionProperty, String>();
		jobProperties.put(JobSubmissionProperty.WALLTIME_IN_MINUTES, "21");
		jobProperties.put(JobSubmissionProperty.APPLICATIONVERSION, "2.49a");
		final GridResource[] resources = blenderInfo
				.getBestSubmissionLocations(jobProperties, "/ARCS/NGAdmin")
				.toArray(new GridResource[] {});

		final int numberOfJobs = 40;

		final String[] subLocs = new String[numberOfJobs];

		// int numberResources = resources.length;
		final int numberResources = 4;
		final int jobsPerResource = numberOfJobs / numberOfJobs;

		for (int i = 0; i < numberResources; i++) {

			final String subLoc;
			if (i >= 3) {
				subLoc = SubmissionLocationHelpers
						.createSubmissionLocationString(resources[i + 1]);
			} else {
				subLoc = SubmissionLocationHelpers
						.createSubmissionLocationString(resources[i]);
			}

			for (int j = 0; j < jobsPerResource; j++) {
				subLocs[(i * jobsPerResource) + j] = subLoc;
			}
		}
		final String subLoc = SubmissionLocationHelpers
				.createSubmissionLocationString(resources[0]);
		for (int i = numberResources * jobsPerResource; i < numberOfJobs; i++) {
			subLocs[i] = subLoc;
		}

		final Date start = new Date();
		final String multiJobName = "PerformanceTest8";
		try {
			si.kill(multiJobName, true);
		} catch (final Exception e) {
			// doesn't matter
			e.printStackTrace();
		}

		System.out.println("Start: " + start.toString());
		System.out.println("End: " + new Date().toString());

		// System.exit(1);
		final BatchJobObject multiPartJob = new BatchJobObject(si,
				multiJobName, "/ARCS/NGAdmin", "blender", "2.49a");

		// multiPartJob.setConcurrentJobCreationThreads(3);

		for (int i = 0; i < numberOfJobs; i++) {

			final int frameNumber = i;

			final JobObject jo = new JobObject(si);
			jo.setJobname(multiJobName + "_" + frameNumber);
			jo.setApplication("blender");
			jo.setCommandline("blender -b " + multiPartJob.pathToInputFiles()
					+ "/CubesTest.blend -F PNG -o cubes_ -f " + frameNumber);
			// jo.setCommandline("cat "+multiPartJob.pathToInputFiles()+"/test.txt "+multiPartJob.pathToInputFiles()+"/input.txt");
			jo.setSubmissionLocation(subLocs[i]);
			// jo.setModules(new String[]{"blender/2.49"});
			jo.setWalltimeInSeconds(1260);
			// jo.setWalltimeInSeconds(70);
			jo.setCpus(1);
			multiPartJob.addJob(jo);

		}

		multiPartJob.addInputFile("/home/markus/Desktop/CubesTest.blend");
		// multiPartJob.addInputFile("/home/markus/temp/test.txt");
		// multiPartJob.addInputFile("gsiftp://ng2.canterbury.ac.nz/home/grid-admin/C_AU_O_APACGrid_OU_VPAC_CN_Markus_Binsteiner/input.txt");

		try {
			multiPartJob.prepareAndCreateJobs(true);
		} catch (final JobsException e) {
			for (final JobObject job : e.getFailures().keySet()) {
				System.out.println("Creation " + job.getJobname() + " failed: "
						+ e.getFailures().get(job).getLocalizedMessage());
			}
			System.exit(1);
		}

		multiPartJob.submit();

		System.out.println("Submission finished...");

		// if (
		// HibernateSessionFactory.HSQLDB_DBTYPE.equals(HibernateSessionFactory.usedDatabase)
		// ) {
		// // for hqsqldb
		// Thread.sleep(10000);
		// }

		// MultiPartJobObject newObject = new MultiPartJobObject(si,
		// multiJobName);
		//
		// newObject.monitorProgress();
		//
		// newObject.downloadResults("logo");

	}

	public BlenderTest() {
		AnnotationProcessor.process(this);
	}

	@EventSubscriber(eventClass = JobStatusEvent.class)
	public void onEvent(JobStatusEvent statusEvent) {
		System.out.println("JobSubmissionNew got job statusEvent: "
				+ statusEvent.getJob().getJobname() + " submitted to "
				+ statusEvent.getJob().getSubmissionLocation() + ": "
				+ JobConstants.translateStatus(statusEvent.getNewStatus()));
	}

}
