from grisu.frontend.control.login import LoginManager
from grisu.frontend.model.job import JobObject
from grisu.model import GrisuRegistryManager

si = LoginManager.loginCommandline("LOCAL")

appInfo = GrisuRegistryManager.getDefault(si).getApplicationInformation("ImageJ")

print 'app: '+appInfo.getApplicationName()

for subloc in appInfo.getAvailableAllSubmissionLocations():
    print subloc

job = JobObject(si);
job.setTimestampJobname("imageJ");
job.setApplication("java");
job.setApplication("ImageJ");
job.setCommandline("echo Hello");

job.setSubmissionLocation("normal:ng2.ivec.org");

job.createJob("/ARCS/StartUp");
job.submitJob();

job.waitForJobToFinish(3);

print "Stdout: "+job.getStdOutContent()
print "Stderr: "+job.getStdErrContent()
