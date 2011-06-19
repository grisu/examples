'''
Created on 17/11/2009

@author: markus
'''

from grisu.frontend.control.login import LoginManager
from grisu.frontend.model.job import JobObject
from grisu.model import FileManager
import sys

si = LoginManager.loginCommandline()

print 'Parsing commandline arguments...'
file1url = sys.argv[1]
file1Name = FileManager.getFilename(file1url)
file2url = sys.argv[2]
file2Name = FileManager.getFilename(file2url);

    
print 'Creating job...'
# create the job object
job = JobObject(si);
# set a unique jobname
job.setTimestampJobname("diff_job")
print 'Set jobname to: '+ job.getJobname()
# setting the application. this means that grisu can figure out the submission location and
# you don't have to do that manually
job.setApplication("UnixCommands")

# set the commandline that needs to be executed
job.setCommandline('diff ' + file1Name+ ' ' + file2Name)

job.addInputFileUrl(file1url);
job.addInputFileUrl(file2url);

# create the job on the backend and specify the VO to use
job.createJob("/ARCS/StartUp")
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