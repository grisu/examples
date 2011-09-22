'''
@author: markus
'''

from grisu.frontend.control.login import LoginManager
from grisu.frontend.model.job import JobObject
import sys


si = LoginManager.loginCommandline("Local")
folder = '/home/markus/test/batch/'
original = folder + 'original.txt'

jobnames = []

for i in range(10):
    
    file = folder+'test'+str(i)+'.txt'
    
    job = JobObject(si);
    job.setUniqueJobname('diff_'+str(i))
    job.setApplication('UnixCommands')
    job.setCommandline('diff original.txt test'+str(i)+'.txt')
    job.createJob("/ARCS/BeSTGRID")
    job.addInputFileUrl(file)
    job.addInputFileUrl(original)
    job.submitJob()
    
    jobnames.append(job.getJobname())


for jobname in jobnames:
    
    finished = job.waitForJobToFinish(10)
    
    print 'Job: '+jobname
    print 
    print 'Stdout:' 
    print job.getStdOutContent()
    print
    print 'Stderr:'
    print job.getStdErrContent()
    print
    print


sys.exit()