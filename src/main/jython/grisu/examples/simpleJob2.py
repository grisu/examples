'''
Created on 17/11/2009

@author: markus
'''

from grisu.frontend.control.login import LoginManager
from grisu.frontend.model.job import JobObject
import sys

si = LoginManager.loginCommandline()
    
print 'Creating job...'
# create the job object
job = JobObject(si);
# set a unique jobname
job.setUniqueJobname("echo_job1")
print 'Set jobname to: '+ job.getJobname()
# set the name of the application like it is published in mds. "generic" means not to use mds for the lookup.
job.setApplication("generic")
# since we are using a "generic" job, we need to specify a submission location. I'll make that easier later on...
job.setSubmissionLocation("dque@edda-m:ng2.vpac.org")

# set the commandline that needs to be executed
job.setCommandline("echo \"Hello World\"")

job.addInputFileUrl('/home/markus/test/singleJobFile_0.txt');

# create the job on the backend and specify the VO to use
job.createJob("/ARCS/NGAdmin")
print 'Submitting job...'
# submit the job
job.submitJob()

print 'Waiting for the job to finish...'
# this waits until the job is finished. Checks every 10 seconds (which would be too often for a real job)
finished = job.waitForJobToFinish(10)

if not finished:
        print "not finished yet."
        # kill the job on the backend anyway
        job.kill(True);
else:
        print 'Job finished. Status: '+job.getStatusString(False)
        # download and cache the jobs' stdout and display it's content
        print "Stdout: " + job.getStdOutContent()
        # download and cache the jobs' stderr and display it's content
        print "Stderr: " + job.getStdErrContent()
        # kill and clean the job on the backend
        job.kill(True)

# don't forget to exit properly. this cleans up possible existing threads/executors
sys.exit()