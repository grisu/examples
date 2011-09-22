'''
@author: markus
'''

from grisu.frontend.control.login import LoginManager
from grisu.frontend.model.job import JobObject
import sys


si = LoginManager.loginCommandline("Local")
folder = '/home/markus/test/batchWrap/'
original = folder + 'original.txt'
script = folder + 'wrap.sh'

job = JobObject(si);
job.setUniqueJobname('diff_wrap')
job.setApplication('UnixCommands')
job.setCommandline('sh wrap.sh 0 9')
job.addInputFileUrl(original)
job.addInputFileUrl(script)
    
for i in range(10):
    
    file = folder+'test'+str(i)+'.txt'
    job.addInputFileUrl(file)
    
job.createJob("/ARCS/BeSTGRID")
job.submitJob()

finished = job.waitForJobToFinish(10)
    
print 'Job: '+job.getJobname()
print 
print 'Stdout:' 
print job.getStdOutContent()
print
print 'Stderr:'
print job.getStdErrContent()
print
print


sys.exit()