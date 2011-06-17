'''
Created on 14/06/2011

@author: hicksa
'''

import sys
import random
import os
import time

# Launch this script with: java -jar ../lib/grisu-jython.jar submitworld.py
# otherwise, you will need to append the grisu-jython.jar to the path
# sys.path.append('../lib/grisu-jython.jar')

# Documentation on these objects can be found here: http://grisu.github.com/grisu/javadoc/
from grisu.jcommons.constants import Constants
from grisu.frontend.control.login import LoginManager
from grisu.frontend.model.job import JobObject, JobsException, BackendException, FileUploadException
from grisu.model import FileManager
from grisu.settings import ClientPropertiesManager
from grisu.control import ResubmitPolicy, ResubmitPolicy, DefaultResubmitPolicy

# If your netwok has a HTTP proxy you can configure Grisu-Jython to use it here
from grisu.jcommons.utils import HttpProxyManager
# The arguments are: address, port, username and password.
#HttpProxyManager.setHttpProxy("proxy.example.com", 8080, "", "")
#HttpProxyManager.setHttpProxy("202.27.240.31", 8080, "", "")

# Set the backend to which jobs will be submitted
backend = "BeSTGRID"

# Set the group under which this job is submitted
# these are also called "Virtual Organisation" or a VO
group= "/ARCS/BeSTGRID"

# We need an absolute path to the local directory
current_dir = os.path.abspath(os.path.curdir)

# NOTE: from this point on, some comments will be in the form of notification output
# as this is an example script, it's pretty verbose

print "INFO: Creating service inteface to " + backend
service_interface = LoginManager.loginCommandline(backend)
print "INFO: Service interface to " + backend + " Created."

# Setting the number of jobs to be submitted
job_count=5

# Since there may be many jobs submitted in the workshop, lets make them a bit unique to avoid issues.
base_job_name = str(random.randint(10000,99999))+'-hello-'
print "INFO: Base job name is "+base_job_name

# There are three stages, creating the jobs, submitting the jobs, then after they have finished, retrieving the job outputs

# Createing a list of jobs
jobs=[]
print "INFO: Defining "+str(job_count)+" helloworld jobs"
for i in range(1,job_count+1):
    print "INFO: Defining job "+str(i)+" of "+str(job_count)
    #The next lines define the actual job's parameters
    job=JobObject(service_interface)                                # Create a job
    job.setJobname(base_job_name+str(i))                            # Give it a (hopefully) unique name
    job.setApplication("python")                                    # Set the application being run
    job.setApplicationVersion("2.4")                                # Set the application version, note this is an exact match
    # job.setSubmissionLocation("all.q:ng2.scenzgrid.org#SGE")        # Set the location the job will be submitted to 
    job.addInputFileUrl(os.path.join(current_dir,"helloworld.py"))  # Add a file
    job.setCommandline("python helloworld.py")                      # Set the command to be run
    print"INFO: job "+job.getJobname()+" defined"
    jobs.append(job)

# Submit the jobs to be run
# Note the exception catching to give more information about a job failing
for job in jobs:
    time_start = time.time()
    try:
        print "INFO: Creating job "+job.getJobname()+" on "+backend+" backend, with "+group+" group"
        job.createJob(group)
        print "INFO: Submitting job "+job.getJobname()
        job.submitJob()
    except (JobsException), error:
        print "HALT: Exception submitting job!"
        print "Job: "+job.getJobname()+", Error: "+error.getFailures().get(job).getLocalizedMessage()
        print"========================"
        time.sleep(3)
        error.printStackTrace()
        sys.exit(1)
    except (BackendException), error:
        print "HALT: Exception from grisu backend!"
        print "Job: "+job.getJobname()+", Error: "+error.getFailures().get(job).getLocalizedMessage()
        print"========================"
        time.sleep(3)
        error.printStackTrace()
        sys.exit(1)        
    time_elapsed = time.time() - time_start
    print "INFO: Job submission for "+job.getJobname()+" took "+str(time_elapsed)+" seconds"

print "INFO: Wait for jobs to finish"
for job in jobs:
    print "INFO: Waiting for "+job.getJobname()+".",
    while not job.isFinished():
        print ".",
        time.sleep(3)
    print ".Status: "+job.getStatusString(False)
    
# Create an output directory
output_dir=base_job_name+'output'
try:
    os.mkdir(output_dir)
    print "INFO: Output directory is "+output_dir
except:
    print "HALT: Could not create output directory "+output_dir
    sys.exit(1)

# Retrieve job output
print "INFO: Downloading output to "+output_dir
for job in jobs:
    if job.isSuccessful():
        print "INFO: Downloading output for "+job.getJobname()
    else:
        print "INFO: "+job.getJobname()+" was not successful, skipping download"
        
print "EXIT: submitworld.py complete."
sys.exit()
