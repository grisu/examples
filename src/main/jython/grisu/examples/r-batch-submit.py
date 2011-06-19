from grisu.frontend.control.login import LoginManager
from grisu.frontend.model.job import JobObject, BatchJobObject
from grisu.jcommons.constants import Constants

# constants
backend = 'BeSTGRID-DEV'
backend = 'Local'

redistribute = False
walltime = 1800
email = 'm.binsteiner@auckland.ac.nz'
basename = 'r-batch'
gen_jobs = 40

inputdir = '/home/markus/Desktop/R/'
#inputfilename = 'Evaluation_Markov-ADF-Test-2011-05-09-mc50.r'
inputfilename = 'Evaluation_Markov-ADF-Test-2011-05-09-mc50-test.r'

print 'logging in...'
si = LoginManager.loginCommandline(backend)

print 'starting job creation...'

group = '/nz/nesi'

#sub_loc = 'route@er171.ceres.auckland.ac.nz:ng2.auckland.ac.nz'

batch_job = BatchJobObject(si, basename, group, 'R', Constants.NO_VERSION_INDICATOR_STRING)

batch_job_name = batch_job.getJobname()
print 'jobname on backend: '+batch_job_name

path_to_inputfile = batch_job.pathToInputFiles()+inputfilename

for i in range(1,gen_jobs+1):
    job = JobObject(si)
    job.setEmail_address(email)
    job.setEmail_on_job_finish(True)

    job.setCommandline('R --no-readline --no-restore --no-save -f '+path_to_inputfile)

    batch_job.addJob(job)
    
batch_job.addInputFile('/home/markus/Desktop/R/'+inputfilename)
batch_job.setDefaultNoCpus(1)
batch_job.setDefaultWalltimeInSeconds(walltime)


print 'preparing jobs on backend...'

batch_job.prepareAndCreateJobs(redistribute)

if redistribute:
    print 'job distribution:'
    print batch_job.getOptimizationResult()

print 'submitting jobs to grid...'
batch_job.submit(True)

print 'submission finished...'




    
    
    

