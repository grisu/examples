'''
Created on 17/11/2009

@author: markus
'''

from grisu.frontend.control.login import LoginManager
from grisu.frontend.model.job import JobObject
import sys
from grisu.jcommons.utils import HttpProxyManager

HttpProxyManager.useSystemHttpProxy()

si = LoginManager.loginCommandline("BeSTGRID-DEV")
    
    
# create the job object
job = JobObject(si);
# set a unique jobname
job.setUniqueJobname("echo_job1")

# set the commandline that needs to be executed
job.setCommandline("echo \"Hello World\"")

# create the job on the backend and specify the VO to use
job.createJob("/nz/nesi")
# submit the job
job.submitJob()

# this waits until the job is finished. Checks every 10 seconds (which would be too often for a real job)
finished = job.waitForJobToFinish(10)

if not finished:
        print "not finished yet."
        # kill the job on the backend anyway
        job.kill(True);
else:
        # download and cache the jobs' stdout and display it's content
        print "Stdout: " + job.getStdOutContent()
        # download and cache the jobs' stderr and display it's content
        print "Stderr: " + job.getStdErrContent()
        # kill and clean the job on the backend
        job.kill(True)

# don't forget to exit properly. this cleans up possible existing threads/executors
sys.exit()