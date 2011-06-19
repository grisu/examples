from grisu.frontend.control.login import LoginManager
from grisu.frontend.model.job import JobObject

si = LoginManager.loginCommandline("BeSTGRID-DEV")

print 'Logged in.'

job = JobObject(si);
job.setUniqueJobname("cat_job", si)
job.setCommandline("cat text0.txt")
job.addInputFileUrl('/home/markus/tmp/text0.txt');


job.createJob("/nz/nesi")
#job.setSubmissionLocation('route@er171.ceres.auckland.ac.nz:ng2.auckland.ac.nz')
job.submitJob()

print 'Job submitted.'

job.waitForJobToFinish(10)

print 'Job finished. Status: '+job.getStatusString(False)
print "Stdout: " + job.getStdOutContent()
print "Stderr: " + job.getStdErrContent()

job.kill(True)

