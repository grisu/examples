from grisu.control import DefaultResubmitPolicy
from grisu.frontend.control.login import LoginManager
from grisu.frontend.model.job import BatchJobObject
from grisu.frontend.model.job import JobException
import sys
import time

batchJobname  =  sys.argv[1]

si = LoginManager.loginCommandline()

# load (but not refresh yet) batchjob, this might take a while
batchJob = BatchJobObject(si, batchJobname, False)

while not batchJob.isFinished(True) and False:
    
    print batchJob.getProgress()
    
    print str(batchJob.getNumberOfFailedJobs())
    
    if batchJob.getNumberOfFailedJobs() > 0:
        
        print str(batchJob.getNumberOfFailedJobs()) + ' failed jobs found. restarting...'
        failedpolicy = DefaultResubmitPolicy()
        batchJob.restart(failedpolicy, True)
        print 'Restart finished.'
        
    time.sleep(5)

jobsToRestart = []

for job in batchJob.getJobs():
    print "Job: "+job.getJobname()+", Status: "+job.getStatusString(False)

    try:
        output = job.getStdOutContent()
        index = output.find('error')
        if index != -1:
            # it doesn't actually make any sense to restart this job, since it would
            # obviously have the same result again. This is just to demonstrate how to parse 
            # for example the stdout file
            # if you don't want to parse stdout but another output file, that's possible as well, of course
            jobsToRestart.append(job)
    except JobException:
        print 'Could not read stdout for job ' + job.getJobname()
    
        
print 'Jobs to restart:'
        
for job in jobsToRestart:
    
    print job.getJobname()
    
# don't forget to exit properly. this cleans up possible existing threads/executors
sys.exit()
    
        
        
    
    