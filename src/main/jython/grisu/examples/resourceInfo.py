'''
Created on 11/12/2009

@author: markus
'''
from grisu.frontend.control.login import LoginManager
from grisu.model import GrisuRegistryManager
import sys


si = LoginManager.login()

resource = GrisuRegistryManager.getDefault(si)

subLocs = resource.getResourceInformation().getAllSubmissionLocations()

for subLoc in subLocs:
    print subLoc
    
# don't forget to exit properly. this cleans up possible existing threads/executors
sys.exit()