from grisu.frontend.control.login import LoginManager
from grisu.frontend.model.job import JobObject
import sys

# create a service interface to the BeSTGRID backend
service_interface = LoginManager.loginCommandline("BeSTGRID")

print 'Creating job object...'

job = JobObject(service_interface);

job.setJobname("echo_job1") # job name must be unique
print 'Set jobname to: '+ job.getJobname()
# set the name of the application as it is published in MDS.
# "generic" means not to use MDS for the lookup.
job.setApplication("generic")
# "generic" jobs require a submission location to be specified
job.setSubmissionLocation("all.q:ng2.scenzgrid.org#SGE")

# set the command that needs to be executed
job.setCommandline("echo \"Hello World\"")

# create the job on the backend and specify the VO to use
job.createJob("/ARCS/BeSTGRID")
print 'Submitting job...'
# submit the job
job.submitJob()

print 'Waiting for the job to finish...'
# this waits until the job is finished. Checks every 10 seconds (which would be too often for a real job)
finished = job.waitForJobToFinish(10)

print 'Job finished. Status: '+job.getStatusString(False)
# download and cache the jobs' stdout and display it's content
print "Stdout: " + job.getStdOutContent()
# download and cache the jobs' stderr and display it's content
print "Stderr: " + job.getStdErrContent()
# kill and clean the job on the backend
job.kill(True)

# don't forget to exit properly. this cleans up possible existing threads/executors
sys.exit()