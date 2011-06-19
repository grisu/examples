'''
Created on 17/11/2009

@author: markus
'''

from grisu.frontend.control.login import LoginManager
import sys

si = LoginManager.loginCommandline("Local")
    
list = si.getAllJobnames(None)
si.killJobs(list, True)
    

#sys.exit()