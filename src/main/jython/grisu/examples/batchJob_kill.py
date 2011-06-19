'''
Created on 17/11/2009

For documentation on what methods are available for the MultiPartJob object, have a look here:
https://code.arcs.org.au/hudson/job/Grisu-SNAPSHOT/javadoc/index.html?org/vpac/grisu/backend/model/job/MultiPartJob.html
and here for a normal job object:
https://code.arcs.org.au/hudson/job/Grisu-SNAPSHOT/javadoc/org/vpac/grisu/frontend/model/job/JobObject.html

@author: Markus Binsteiner
'''

from grisu.jcommons.constants import Constants
from grisu.control.exceptions import NoSuchJobException
from grisu.frontend.control.login import LoginManager, LoginParams
from grisu.frontend.model.job import JobObject, BatchJobObject, \
    JobsException
import sys
import time

# login stuff this uses a local proxy, if there is none, an exception is thrown
loginParams = LoginParams("Local", None, None);
si = LoginManager.login(None, None, None, None, loginParams)

# how many jobs do we want
numberOfJobs = 10

# the (unique) name of the multijob
multiJobName = "10catJobs";

# delete an (possibly existing) old job with the same name
try:
    si.kill(multiJobName, True);
except NoSuchJobException:
    print "No need to kill and clean job" + multiJobName

status = si.getActionStatus(multiJobName)
while not status.isFinished():
    print str(status.getCurrentElements()) + "/" + str(status.getTotalElements())
    print
 
sys.exit()


