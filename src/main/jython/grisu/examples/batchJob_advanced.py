'''
Created on 17/11/2009

For documentation on what methods are available for the MultiPartJob object, have a look here:
http://grisu.github.com/grisu/javadoc/grisu/frontend/model/job/BatchJobObject.html 
and here for a normal job object:
http://grisu.github.com/grisu/javadoc/grisu/frontend/model/job/JobObject.html

@author: Markus Binsteiner
'''

from grisu.jcommons.constants import Constants
from grisu.control import ResubmitPolicy, ResubmitPolicy, \
    DefaultResubmitPolicy
from grisu.control.exceptions import NoSuchJobException
from grisu.frontend.control.login import LoginManager, LoginParams
from grisu.frontend.model.job import JobObject, BatchJobObject, \
    JobsException
from grisu.frontend.view.swing.jobmonitoring.batch import \
    BatchJobDialog
from grisu.model import GrisuRegistryManager
import sys
import time

si = LoginManager.loginCommandline("BeSTGRID-DEV")

# how many jobs do we want
numberOfJobs = 10

# the (unique) name of the multijob
batch_job_name = "test_batch";

   

# to see whats going on we add a simple event listener. Hm. This doesn't seem to work reliably in jython. 
#SystemOutMultiJobLogger(multiJobName)

# create the multipart job 
batch_job = BatchJobObject(si, batch_job_name, "/nz/nesi", "cat", Constants.NO_VERSION_INDICATOR_STRING);

# now we can calculate the relative path (from every job directory) to the common input file folder
pathToInputFiles = batchJob.pathToInputFiles()

for i in range(0, numberOfJobs):

    # create the single job
    job = JobObject(si)

    # better to set the application to use explicitely because in that case we don't need to use mds (faster)
    job.setCommandline('cat ' + pathToInputFiles+'commonFile.txt ' + 'singleJobFile.txt')
    # adding a job-specific input file
    job.addInputFileUrl("/home/markus/tmp/singleJobFile.txt")
    # adding the job to the multijob
    batch_job.addJob(job)
    
# now we are adding a file that can be used by all of the child jobs. it needs to be referenced via the pathToInputFiles() method shown above
batch_job.addInputFile('/home/markus/tmp/commonJobFile.txt')
batch_job.setDefaultNoCpus(1);
batch_job.setDefaultWalltimeInSeconds(60);   
    
    
try:
    print "Creating jobs on the backend and staging files..."
    # by specifying "True" we tell the backend to automatically distribute the jobs to all available submission locations
    # this can be finetuned by exluding or including sites. another option would be to specifying the submission location 
    # for every single job and setting "False" below (this would make job submission faster since jobs don't need to be re-distributed/moved on the backend).
    batch_job.prepareAndCreateJobs(True)
except (JobsException), error:
    for job in error.getFailures().keySet():
        print "Job: "+job.getJobname()+", Error: "+error.getFailures().get(job).getLocalizedMessage()

    sys.exit()

print "Job distribution:"
for subLoc in batch_job.getOptimizationResult().keySet():
    print subLoc + " : " +batch_job.getOptimizationResult().get(subLoc)


print "Submitting jobs..."
batch_job.submit()

restarted = False

# now we wait for all jobs to finish. Actually, we probably should test whether the job was successful as well...
while not batch_job.isFinished(True):
    # printing some stats
    print batch_job.getProgress()
    
    # restart failed jobs everytime
    failedpolicy = DefaultResubmitPolicy()
    # to only resubmit failed jobs, we have to remove the waiting jobs resubmission that is set by default
    batch_job.restart(failedpolicy, True)

    # restart once after the jobsubmission is finished to optimize job distributions to queues where the job actually runs
    if not restarted:
        
        # actually, it probably would be a good idea to refresh the job status here because otherwise the restart will just 
        # restart failed jobs that were already submitted with the restart above...  not really sure...
        #multiPartJob.refresh()
        
        # this might not work the first few times because in the background the batchjob is still submitting...
        print "trying to restarting job..."
        
        policy = DefaultResubmitPolicy()
        # the next line doesn't make sense since it's the default anyway. Just to demonstrate.
        policy.setProperty(DefaultResubmitPolicy.RESTART_WAITING_JOBS, True)
        restarted = batch_job.restart(policy, True)

        if restarted:
            print "Job distribution for restarted jobs:"
            for subLoc in batch_job.getOptimizationResult().keySet():
                resubmitted = True
                print subLoc + " : " +batch_job.getOptimizationResult().get(subLoc)
        else:
            print "Job not restarted (yet)."
    
    print "Job not finished yet. Waiting..."
    time.sleep(3)

print "Multipartjob "+batch_job.getBatchJobname()+" finished."

# finally, everything is ready. We could do a lot more here, but you get the idea...
for job in batch_job.getJobs():
    print "Job: "+job.getJobname()+", Status: "+job.getStatusString(False)
    print
    print "Stdout: "
    print job.getStdOutContent()
    print
    print "Stderr: "
    print job.getStdErrContent()
    print
    print
    
print "Finished."
    
# don't forget to exit properly. this cleans up possible existing threads/executors
sys.exit()


