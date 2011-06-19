'''
Created on 17/11/2009

This example shows how to create, submit and wait for the execution of a batchjob with input files.

In order to see what is going on behind the scenes, you can monitor $HOME/.grisu.beta/grisu-jython.debug

For documentation on what methods are available for the MultiPartJob object, have a look here:
http://grisu.github.com/grisu/javadoc/grisu/frontend/model/job/BatchJobObject.html 
and here for a normal job object:
http://grisu.github.com/grisu/javadoc/grisu/frontend/model/job/JobObject.html

@author: Markus Binsteiner
'''

from grisu.jcommons.constants import Constants
from grisu.frontend.control.login import LoginManager
from grisu.frontend.model.job import JobObject, BatchJobObject, \
    JobsException
import sys
import time

si = LoginManager.loginCommandline("BeSTGRID-DEV")

# how many jobs do we want
numberOfJobs = 10

# the (unique) name of the batchjob
batch_job_name = "test_batch";

# create the batchjob 
batch_job = BatchJobObject(si, batch_job_name, "/nz/nesi", "UnixCommands", Constants.NO_VERSION_INDICATOR_STRING);

# now we can calculate the relative path (from every job directory) to the common input file folder
pathToInputFiles = batch_job.pathToInputFiles()

for i in range(0, numberOfJobs):

    # create the single job
    job = JobObject(si)
    # better to set the application to use explicitely because in that case we don't need to use mds (faster)
    job.setCommandline('cat ' + pathToInputFiles+'commonJobFile.txt ' + 'singleJobFile.txt')
    # adding a job-specific input file
    job.addInputFileUrl("/home/markus/tmp/singleJobFile.txt")
    # adding the job to the multijob
    batch_job.addJob(job)
    
# now we are adding a file that can be used by all of the child jobs. it needs to be referenced via the pathToInputFiles() method shown above
batch_job.addInputFile('/home/markus/tmp/commonJobFile.txt')
batch_job.setDefaultNoCpus(1);
batch_job.setDefaultWalltimeInSeconds(60);   
   
batch_job.setLocationsToExclude(["gt5test:ng1.canterbury.ac.nz"])
    
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
print batch_job.getOptimizationResult()


print "Submitting jobs..."
batch_job.submit()


# now we wait for all jobs to be finished, checking for updates every 10 seconds. in real life we would set a much higher check intervall since we don't want to overload
# the backend and also it's not really necessary
batch_job.waitForJobToFinish(10)

print "BatchJob "+batch_job.getJobname()+" finished."

# finally, everything is ready. We could do a lot more here, but you get the idea...
for job in batch_job.getJobs():
    print "Job: "+job.getJobname()+", Status: "+job.getStatusString(False)
    print "Submitted to: "+job.getJobProperty(Constants.SUBMISSION_SITE_KEY)
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


