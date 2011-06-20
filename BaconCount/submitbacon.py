'''
Created on 14/06/2011

A dictionary counter submission script example for the Scripted job submission workshop, eResearch Symposium 2011

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
from grisu.frontend.model.job import JobObject, JobsException, BackendException

# If your netwok has a HTTP proxy you can configure Grisu-Jython to use it here
# from grisu.jcommons.utils import HttpProxyManager
# The arguments are: address, port, username and password.
# HttpProxyManager.setHttpProxy("proxy.example.com", 8080, "", "")

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