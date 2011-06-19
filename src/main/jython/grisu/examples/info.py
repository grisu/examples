'''
Created on 11/12/2009

@author: markus
'''

from grisu.frontend.control.login import LoginManager
from grisu.model import GrisuRegistryManager
import sys


si = LoginManager.loginCommandline()

registry = GrisuRegistryManager.getDefault(si)

for app in sys.argv:
    
    info = registry.getApplicationInformation(app)
    print 'Application: '+app
    
    info = registry.getApplicationInformation(app)
    
    for subLoc in info.getAvailableAllSubmissionLocations():
        
        print("\tSubmissionLocation: "+subLoc)
        print("\tVersions: ")
        for version in info.getAvailableVersions(subLoc):
            
            print "\t\t" + version
            print "\t\t\tDetails:"
            
            for key in info.getApplicationDetails(subLoc, version).keySet():
                
                print "\t\t\t\t"+key+":\t"+info.getApplicationDetails(subLoc, version).get(key)
                
        print
        
    print 
    print 
    
    
        
    
