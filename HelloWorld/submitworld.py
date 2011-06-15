'''
Created on 14/06/2011

@author: hicksa
'''

import sys

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
