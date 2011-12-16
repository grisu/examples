'''
Created on 16/12/2011

@author: markus
'''
from grisu.frontend.control.login import LoginManager
from grisu.frontend.model.job import JobObject
from grisu.model import GrisuRegistryManager
import sys

targetDir = 'gsiftp://df.auckland.ac.nz/BeSTGRID/home/markus.binsteiner2/stagingoutput'

si = LoginManager.loginCommandline('bestgrid')

uem = GrisuRegistryManager.getDefault(si).getUserEnvironmentManager()
fm = GrisuRegistryManager.getDefault(si).getFileManager()

allJobs = uem.getCurrentJobs(False)

myJobs = []
finishedJobs = []

# getting all the jobs for the run we are interested in
for job in allJobs: 
    
    name = job.jobname()
    if name.startswith('staging_test'):
        tempJob = JobObject(si, name)
        myJobs.append(tempJob)
    
fileUrls = []
    
while len(myJobs) > 0:
    
    for job in myJobs:
        
        if job.isFinished():
            if job.isFailed(False):
                print 'Job '+job.getJobname()+' failed. Not doing anything...'
                continue
                
            # stage out
            jobDirUrl = job.getJobDirectoryUrl()
            fileUrl = jobDirUrl + '/' + 'outfile'
            fileUrls.append(fileUrl)

            print 'Staging file: '+fileUrl            
            fm.cp(fileUrl, targetDir+'/'+job.getJobname(), True)
            # assuming there is no fault, we can clean up the job on the cluster here
            job.kill(True)
    
sys.exit(0)