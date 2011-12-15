from grisu.frontend.control.login import LoginManager
from grisu.frontend.model.job import JobObject
import random
import sys

# si stands for serviceInterface and holds all session information
si = LoginManager.loginCommandline('bestgrid')

amount_of_jobs_total = 10
amount_of_jobs_concurrent = 4

submitted_jobs = []

# better make that unique for each run, so we can resume workflows easier if necessary (this introduces quite a bit more complexity though)
jobname_base = 'staging_test'

for total in range(1, amount_of_jobs_total+1):
    job = JobObject(si)
    job.setJobname(jobname_base+'_'+str(total))
    # always good to set the application if you know it, processing the job will be faster
    job.setApplication('UnixCommands')
    # also good to set queue if you know where you want to submit your job, not necessary, but processing of the job will be faster
    job.setSubmissionLocation('default:gram5.ceres.auckland.ac.nz')
    # create a random sized outfile
    mbsize = 1024*1024
    random_size = random.randrange(10, 100)
    size_string = str(mbsize*random_size)
    job.setCommandline('dd if=/dev/zero of=outfile bs='+size_string+' count=1')
    
    job.createJob('/nz/nesi')
    job.submitJob()
    print 'created and submitted job: '+ job.getJobname()+' (size: '+str(random_size)+'mb)'
    
    
    submitted_jobs.append(job)
    
    
print 'Submission finished'

sys.exit(0)
