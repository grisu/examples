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
backend = "BeSTGRID-DEV"

# Set the group under which this job is submitted
# these are also called "Virtual Organisation" or a VO
group= "/ARCS/BeSTGRID"

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
base_job_name="bacon"
batch_job_name = str(random.randint(10000,99999))+"-"+base_job_name
print "INFO: Base job name is "+base_job_name
print "INFO: Batch job name is "+batch_job_name

# Set some job settings
application = "python"

print "INFO: Creating a Batch Job Object called "+batch_job_name
batch_jobs = BatchJobObject(service_interface, batch_job_name, group, application, Constants.NO_VERSION_INDICATOR_STRING)
batch_jobs.setConcurrentInputFileUploadThreads(5)    # Set the number of concurrent uploads
batch_jobs.setConcurrentJobCreationThreads(5)        # Set the number of concurrent jobs
batch_jobs.setDefaultNoCpus(1);                      # Set the number of CPUs required
batch_jobs.setDefaultWalltimeInSeconds(300);         # Set the maximum walltime to 5 minutes
batch_jobs.setLocationsToExclude(["AUT"])            # Create a blacklist of sites to exclude
# Currently the AUT location is not behaving, so always exclude it

print "INFO: Adding common files to Batch Job Object "+batch_job_name
#batch_jobs.addInputFile()
#batch_jobs.addInputFile()

print("INFO: Kill and clean " + batch_job_name + " jobs")
try:
    service_interface.kill(batch_job_name, True);
    status = service_interface.getActionStatus(batch_job_name)
    while not status.isFinished():
        percentage = status.getCurrentElements() * 100 / status.getTotalElements()
        del_str = "\rDeletion "+str(percentage)+"%"
        print del_str,
        time.sleep(3)
        status = service_interface.getActionStatus(batch_job_name)
except:
    print "INFO: No need to kill and clean job " + batch_job_name

sys.exit()