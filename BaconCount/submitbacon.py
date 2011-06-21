'''
Created on 14/06/2011

A dictionary counter submission script example for the Scripted job submission workshop, eResearch Symposium 2011

This script uses the BatchJobObject to demonstrate some of its features.

@author: Aaron hicks, hicksa@landcarersearch.co.nz
'''

import sys
import random
import os
import time

# Launch this script with: java -jar ../lib/grisu-jython.jar submitbacon.py
# otherwise, you will need to append the grisu-jython.jar to the path
# sys.path.append('../lib/grisu-jython.jar')

# Documentation on these objects can be found here: http://grisu.github.com/grisu/javadoc/
from grisu.jcommons.constants import Constants
from grisu.frontend.control.login import LoginManager
from grisu.frontend.model.job import JobObject, BatchJobObject, JobsException, BackendException, FileUploadException
from grisu.settings import ClientPropertiesManager

# If your netwok has a HTTP proxy you can configure Grisu-Jython to use it here
from grisu.jcommons.utils import HttpProxyManager
# The arguments are: address, port, username and password.
# HttpProxyManager.setHttpProxy("proxy.example.com", 8080, "", "")
HttpProxyManager.setHttpProxy("202.27.240.31", 8080, "", "")

# Set the backend to which jobs will be submitted
# Use BeSTGRID-DEV for testing and development
# Use BeSTGRID for live runs
backend = "BeSTGRID"

# Set the group under which this job is submitted
# these are also called "Virtual Organisation" or a VO
group = "/ARCS/BeSTGRID"

# We need an absolute path to the local directory
current_dir = os.path.abspath(os.path.curdir)

# Setting the number of files that can be concurrently uploaded
ClientPropertiesManager.setConcurrentUploadThreads(5)

print "START: Starting submitbacon dictionary processor submission script"

# First check that the script is called correctly
if (len(sys.argv[1:]) != 2):
    print "HALT: Incorrect usage!"
    print "Requires arguments for dictionary file and input path"
    print "e.g. java -jar ../lib/grisu-jython.jar countbacon.py dictionary.txt path/to/input"
    sys.exit(1)

# Process arguments and test they're valid
dictionary_path, input_path = sys.argv[1:]
print "INFO: Dictionary file path: " + dictionary_path
print "INFO: Input directory path: " + input_path

if not os.path.isfile(dictionary_path):
    print "HALT: Dictionary file " + dictionary_path + " is not a file"
    sys.exit(1)

if not os.path.isdir(input_path):
    print "HALT: Input path " + input_path + " is not a directory"
    sys.exit(1)
elif not os.listdir(input_path):
    print "FINISHED: Input directory is empty, no files to process"
    sys.exit(0)

# NOTE: from this point on, some comments will be in the form of notification output
# as this is an example script, it's pretty verbose

print "INFO: Creating service inteface to " + backend
service_interface = LoginManager.loginCommandline(backend)
print "INFO: Service interface to " + backend + " Created."
print "INFO: Service Interface connected as: " + service_interface.getDN()

# Create some base strings to build jobs with
base_job_name = "bacon"
batch_job_name = str(random.randint(10000, 99999)) + "-" + base_job_name
print "INFO: Base job name is " + base_job_name
print "INFO: Batch job name is " + batch_job_name

# Set some job settings
application = "python"
version = "2.4"

print "INFO: Creating a Batch Job Object called " + batch_job_name
batch_jobs = BatchJobObject(service_interface, batch_job_name, group, application, version)
batch_jobs.setConcurrentInputFileUploadThreads(5)    # Set the number of concurrent uploads
batch_jobs.setConcurrentJobCreationThreads(5)        # Set the number of concurrent jobs
batch_jobs.setDefaultNoCpus(1);                      # Set the number of CPUs required
batch_jobs.setDefaultWalltimeInSeconds(300);         # Set the maximum walltime to 5 minutes
batch_jobs.setLocationsToExclude(["AUT"])            # Create a blacklist of sites to exclude
# Currently the AUT location is not behaving, so always exclude it

print "INFO: Adding common files to Batch Job Object " + batch_job_name
batch_jobs.addInputFile(os.path.join(current_dir, dictionary_path))
batch_jobs.addInputFile(os.path.join(current_dir, "countbacon.py"))

print "INFO: Defining jobs from input directory"
job_count = 0
for file_name in os.listdir(input_path):
    print "INFO: Defining job for " + file_name
    job_name = base_job_name + "-" + file_name
    job = JobObject(service_interface)
    job.setJobname(job_name)
    job.setApplication("python")                                    # Set the application being run
    job.setApplicationVersion("2.4")                                # Set the application version, note this is an exact match
    job.addInputFileUrl(os.path.join(current_dir, input_path, file_name))
    job.setCommandline("python ../countbacon.py ../" + dictionary_path + " " + file_name)
    print "INFO: " + job.getJobname() + " defined"
    batch_jobs.addJob(job)
    print "INFO: " + job.getJobname() + " added to batch " + batch_jobs.getJobname()
    job_count += 1
print "INFO: " + str(job_count) + " jobs defined"

print "INFO: Sending batch " + batch_jobs.getJobname() + " to " + backend + " and staging files..."
try:
    batch_jobs.prepareAndCreateJobs(False)
except (JobsException), error:
    print("HALT: Exception submitting jobs from BatchJobObject " + batch_jobs.getJobname() + "!")
    for job in error.getFailures().keySet():
        print "Job: " + job.getJobname() + ", Error: " + error.getFailures().get(job).getLocalizedMessage()
    sys.exit(1)
except (BackendException), error:
    print("HALT: Exception from grisu backend " + backend + "!")
    print(error.getLocalizedMessage())
    print("========================")
    time.sleep(3)
    error.printStackTrace()
    sys.exit(1)
time.sleep(3)

print "INFO: Submitting jobs in batch " + batch_jobs.getJobname()
batch_jobs.submit()

restarted = False

print "INFO: Waiting for batch " + batch_jobs.getJobname() + " to finish"
while not batch_jobs.isFinished(True):
    print "\rWAITING: Running " + str(job_count) + " jobs:",
    print " Waiting [" + str(batch_jobs.getNumberOfWaitingJobs()) + "]",
    print " Active [" + str(batch_jobs.getNumberOfRunningJobs()) + "]",
    print " Successful [" + str(batch_jobs.getNumberOfSuccessfulJobs()) + "]",
    print " Failed [" + str(batch_jobs.getNumberOfFailedJobs()) + "]",
    time.sleep(3)

# Refresh status one last time    
print "\rWAITING: Running " + str(job_count) + " jobs:",
print " Waiting [" + str(batch_jobs.getNumberOfWaitingJobs()) + "]",
print " Active [" + str(batch_jobs.getNumberOfRunningJobs()) + "]",
print " Successful [" + str(batch_jobs.getNumberOfSuccessfulJobs()) + "]",
print " Failed [" + str(batch_jobs.getNumberOfFailedJobs()) + "]" 

print "INFO: batch jobs in " + batch_jobs.getJobname() + " finished."

print "INFO: Begin retrieving batch job outputs."
output_path = batch_job_name + "-output"
print "INFO: Writing output to " + output_path

if not os.path.isdir(output_path):
    os.mkdir(output_path)

for job in batch_jobs.getJobs():
    if job.isSuccessful(True):
        print "INFO: Downloading stdout for " + job.getJobname()
        stdout_file = open(os.path.join(output_path, job.getJobname() + "-stdout.txt"), 'w')
        stdout_file.write(job.getStdOutContent())
        stdout_file.close()
        print "INFO: Downloading stderr for " + job.getJobname()
        stderr_file = open(os.path.join(output_path, job.getJobname() + "-stderr.txt"), 'w')
        stderr_file.write(job.getStdErrContent())
        stderr_file.close()
    else:
        print "INFO: " + job.getJobname() + "failed! Nothing to download."    
print "INFO: Outputs retrieved" 

print("INFO: Kill and clean " + batch_job_name + " jobs")
del_str = "" # to put in scope
try:
    service_interface.kill(batch_job_name, True);
    status = service_interface.getActionStatus(batch_job_name)
    while not status.isFinished():
        percentage = status.getCurrentElements() * 100 / status.getTotalElements()
        del_str = "\rDeletion " + str(percentage) + "%"
        print del_str,
        time.sleep(3)
        status = service_interface.getActionStatus(batch_job_name)
except:
    print "INFO: No need to kill and clean job " + batch_job_name
print "\rDeletion 100%" # shh, faking this.
print "INFO: batch " + batch_job_name + " deleted"

print "FINISHED: submitbacon completed"
sys.exit()
