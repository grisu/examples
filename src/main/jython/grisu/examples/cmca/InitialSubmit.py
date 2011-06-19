from grisu.jcommons.constants import Constants
from grisu.control import DefaultResubmitPolicy, JobnameHelpers
from grisu.frontend.control.login import LoginManager
from grisu.frontend.model.job import BatchJobObject, JobObject, \
    JobsException
from grisu.frontend.view.swing.jobmonitoring.batch import \
    BatchJobDialog
import sys
import time

# display commandline login menu if no local proxy exists
si = LoginManager.loginCommandline()

# how many jobs do we want
numberOfJobs = 20

# the (unique) name of the multijob
batchJobName = JobnameHelpers.calculateTimestampedJobname('exampleBatchJob')
  

print 'Creating batchjob '+batchJobName
# create the multipart job 
batchJob = BatchJobObject(si, batchJobName, '/ARCS/NGAdmin', 'UnixCommands', Constants.NO_VERSION_INDICATOR_STRING);

# now we can calculate the relative path (from every job directory) to the common input file folder
pathToInputFiles = batchJob.pathToInputFiles()

inputFile1Url = '/home/markus/test/inputFile1.txt'
inputFile1relPath = pathToInputFiles+'inputFile1.txt ' 

inputFile2Url = 'gsiftp://ng2.vpac.org/home/grid-vpac/DC_au_DC_org_DC_arcs_DC_slcs_O_VPAC_CN_Markus_Binsteiner_qTrDzHY7L1aKo3WSy8623-7bjgM/inputFile2.txt'
inputFile2relPath = pathToInputFiles+'inputFile2.txt' 

inputFile3Url = '/home/markus/test/errorFile.txt'
inputFile3relPath = pathToInputFiles + 'errorFile.txt'

for i in range(0, numberOfJobs):
    # create a unique jobname for every job
    jobname = batchJobName+"_"+ str(i)
    
    print 'Creating job: '+jobname
    
    # create the single job
    job = JobObject(si)
    job.setJobname(jobname)
    # better to set the application to use explicitely because in that case we don't need to use mds (faster)
    job.setApplication('UnixCommands')
    if i == 3 or i == 13:
        # this is just to demonstrate how to restart a failed job later on
        job.setCommandline('cat '+inputFile3relPath)
    else:
        job.setCommandline('cat '+ inputFile1relPath + ' ' + inputFile2relPath)

    job.setWalltimeInSeconds(60)
    # adding the job to the multijob
    batchJob.addJob(job)

# this should be set because it's used for the matchmaking/metascheduling
batchJob.setDefaultNoCpus(1);
batchJob.setDefaultWalltimeInSeconds(60);   
    
# now we add an input file that is common to all jobs
batchJob.addInputFile(inputFile1Url);
batchJob.addInputFile(inputFile2Url);
batchJob.addInputFile(inputFile3Url);
# we don't want to submit to tpac because it doesn't work
#multiPartJob.setSitesToExclude(["uq", "hpsc", "auckland", "canterbury"]);
    
try:
    print "Creating jobs on the backend and staging files..."
    batchJob.prepareAndCreateJobs(True)
except (JobsException), error:
    for job in error.getFailures().keySet():
        print "Job: "+job.getJobname()+", Error: "+error.getFailures().get(job).getLocalizedMessage()

    sys.exit()

# this is not really needed
print "Job distribution:"
for subLoc in batchJob.getOptimizationResult().keySet():
    print subLoc + " : " +batchJob.getOptimizationResult().get(subLoc)


print "Submitting jobs..."
batchJob.submit(True)

print 'Submission finished.'
print 'Name of submitted batchjob: '+batchJobName

# don't forget to exit properly. this cleans up possible existing threads/executors
sys.exit()