'''
Created on 14/06/2011

@author: hicksa
'''

import sys
import random
import os

# Launch this script with: java -jar ../lib/grisu-jython.jar submitworld.py
# otherwise, you will need to append the grisu-jython.jar to the path
# sys.path.append('../lib/grisu-jython.jar')
from grisu.jcommons.constants import Constants
from grisu.frontend.control.login import LoginManager
from grisu.frontend.model.job import JobObject, JobsException, BackendException, FileUploadException
from grisu.model import FileManager
from grisu.settings import ClientPropertiesManager
from grisu.control import ResubmitPolicy, ResubmitPolicy, DefaultResubmitPolicy

# If your netwok has a HTTP proxy you can configure Grisu-Jython to use it here
# from grisu.jcommons.utils import HttpProxyManager
# The arguments are: address, port, username and password.
# HttpProxyManager.setHttpProxy("proxy.example.com", 8080, "", "")

# Set the backend to which jobs will be submitted
backend = "BeSTGRID"

# NOTE: from this point on, some comments will

print "INFO: Creating service inteface to " + backend
service_interface = LoginManager.loginCommandline(backend)
print "INFO: Service interface to " + backend + " Created."

# Setting the number of jobs to be submitted
job_count=5

# Since there may be many jobs submitted in the workshop, lets make them a bit unique to avoid issues.
base_job_name = str(random.randint(10000,99999))+'-hello-'
print "INFO: Base job name is "+base_job_name

# Create an output directory
output_dir=base_job_name+'output'
try:
    os.mkdir(output_dir)
    print "INFO: Output directory is "+output_dir
except:
    print "HALT: Could not create output directory "+output_dir
    sys.exit(1)

# Createing a list of jobs
jobs=[]
print "INFO: Creating "+str(job_count)+" helloworld jobs"
for i in range(1,job_count+1):
    print "INFO: Creating job "+str(i)+" of "+str(job_count)
    job=JobObject(service_interface)
    job.setJobname(base_job_name+str(i))
    job.addInputFileUrl("helloworld.py")
    job.setCommandline("python helloworld.py")
    print"INFO: job "+job.getJobname()+" created"
    jobs.append(job)
    
print "EXIT: submitworld.py complete."

