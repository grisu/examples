'''
Created on 10/06/2010

@author: markus
'''
from __future__ import with_statement
from grisu.frontend.control.login import LoginManager
from grisu.frontend.model.job import JobObject
import sys
import time
from exampleJobs import *




# setup benchmark stuff
submitter = submitter()

#siNames = ["ARCS", "BeSTGRID"]
siNames = ["Local"]

serviceInterfaces = []

for siName in siNames:
    
    with timer:   
        temp = LoginManager.loginCommandline(siName)
         
    print 'Login '+siName+': '+str(timer.duration_in_seconds())
    serviceInterfaces.append(temp)
    GrisuRegistryManager.getDefault(temp).getUserEnvironmentManager().setCurrentFqan("/ARCS/NGAdmin")

print
print

#actionClasses = (simpleSubmitJob, simpleSubmitJobWith1SmallInputFile,  simpleSubmitJobWith5SmallInputFiles,
  #               simpleSubmitJobWith1MediumInputFile, simpleSubmitJobWith5MediumInputFiles, simpleSubmitJobWith1_46mb_InputFile, simpleSubmitJobWith5_46mb_InputFiles)

actionClasses = {dynamicStageJob : [True, 1,0,0], dynamicStageJob : [True, 5,0,0], dynamicStageJob : [True, 0,1,0], dynamicStageJob : [True, 0,5,0]}

actions = []
    

for classname, inputFiles in actionClasses.iteritems():
    for si in serviceInterfaces:
        temp = classname(si)
        
        temp.setInputFiles(inputFiles);
        
        actions.append(temp)
    
lastActionname = "first"
    
for action in actions:

    if lastActionname != action.name():
        
        print '\nAction: '+action.name()
        print '\t('+action.description()+')\n'

    print "\tUsing: "+action.siInfo
    
    lastActionname = action.name()


    submitter.submit(si, action, 4)
    
    print

sys.exit()
