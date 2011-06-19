from grisu.frontend.control.login import LoginManager
from grisu.frontend.model.job import BatchJobObject, JobObject
import sys

batchJobName  =  sys.argv[1]

# display commandline login menu if no local proxy exists
si = LoginManager.loginCommandline()

batchJob = BatchJobObject(si, batchJobName, False)

start = 30
end = 40

pathToInputFiles = batchJob.pathToInputFiles()

inputFile1relPath = pathToInputFiles+'inputFile1.txt ' 
inputFile2relPath = pathToInputFiles+'inputFile2.txt' 

for i in range(start, end):
    # create a unique jobname for every job
    jobname = batchJobName+"_"+ str(i)
    
    print 'Creating job: '+jobname
    
    # create the single job
    job = JobObject(si)
    job.setJobname(jobname)
    # better to set the application to use explicitely because in that case we don't need to use mds (faster)
    job.setApplication('UnixCommands')
    job.setCommandline('cat '+ inputFile1relPath + ' ' + inputFile2relPath)

    job.setWalltimeInSeconds(60)
    # adding the job to the multijob
    batchJob.addJob(job)

# only start the newly added jobs and wait for the restart to finish
batchJob.restart(False, False, True, True)


# don't forget to exit properly. this cleans up possible existing threads/executors
sys.exit()