'''

Simple example of a workflow that submits x amount of jobs, always y at a time.

Documentation: http://grisu.github.com/grisu/javadoc/

Created on 16/11/2011

@author: markus
'''
from grisu.frontend.control.login import LoginManager
from grisu.frontend.model.job import JobObject
import random
import sys
import time

# si stands for serviceInterface and holds all session information
si = LoginManager.loginCommandline('bestgrid-test')

amount_of_jobs_total = 10
amount_of_jobs_concurrent = 4

created_jobs = []
submitted_jobs = []
finished_jobs = []

# better make that unique for each run, so we can resume workflows easier if necessary (this introduces quite a bit more complexity though)
jobname_base = 'workflow_test'

for total in range(1, amount_of_jobs_total+1):
    job = JobObject(si)
    job.setJobname(jobname_base+'_'+str(total))
    # always good to set the application if you know it, processing the job will be faster
    job.setApplication('UnixCommands')
    # also good to set queue if you know where you want to submit your job, not necessary, but processing of the job will be faster
    job.setSubmissionLocation('default:gram5.ceres.auckland.ac.nz')
    # job sleeps for a random time
    random_sleep = random.randrange(5, 75)
    job.setCommandline('sleep '+str(random_sleep))
    
    job.createJob('/nz/nesi')
    print 'created job: '+ job.getJobname()+' (sleeptime: '+str(random_sleep)+')'
    
    created_jobs.append(job)
    
finished = False
while not finished:
    
    # submit another bunch of jobs while there are some
    if len(created_jobs) > 0:
        print 'still '+str(len(created_jobs))+' jobs to submit...'
        while len(submitted_jobs) < amount_of_jobs_concurrent:
        
            if len(created_jobs) <= 0:
                break
            
            job = created_jobs[0]
            job.submitJob()
            print 'submitted job: '+job.getJobname()
            created_jobs.remove(job)
            submitted_jobs.append(job)
    else:
        print 'all jobs submitted'
        
    print ''
    
    # check whether there is anything left
    if len(created_jobs) > 0 or len(submitted_jobs) > 0:
        print 'not finished yet, continuing submitting/checking jobs...'
    else:
        print 'all jobs finished.'
        finished = True
        break;
    
    # sleep a bit, we don't want to make too many requests
    time.sleep(10)
    print ''

    # check all submitted jobs whether they are finished
    for job in submitted_jobs:
        print 'checking job: '+job.getJobname()
        if job.isFinished():
            print 'finished job: '+job.getJobname()
            submitted_jobs.remove(job)
            finished_jobs.append(job)
        else:
            print 'job still running: '+job.getJobname()
            
    print ''
    print ''
    
print 'Execution finished'

sys.exit(0)

