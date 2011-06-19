'''
Created on 16/06/2010

@author: markus
'''


from exampleJobs import *
from grisu.frontend.control.login import LoginManager
from grisu.model import GrisuRegistryManager
import sys

# setup benchmark stuff
submitter = submitter()
si = LoginManager.loginCommandline("Local")
registry = GrisuRegistryManager.getDefault(si)
uem = registry.getUserEnvironmentManager()

uem.setCurrentFqan('/ARCS/NGAdmin')

#actionsGT4 = (simpleSubmitJob(si), simpleSubmitJobWith1SmallInputFile(si), simpleMdsSubmitJob(si), simpleMdsSubmitJobWith1SmallInputFile(si),
 #      simpleSubmitJobWith5SmallInputFiles(si), simpleSubmitJobWith1MediumInputFile(si),simpleSubmitJobWith5MediumInputFiles(si), simpleSubmitJobWith1_46mb_InputFile(si), simpleSubmitJobWith5_46mb_InputFiles(si) )
#actionsGT5 = (simpleSubmitJob(si, 'gt5test:ng1.canterbury.ac.nz'), simpleSubmitJobWith1SmallInputFile(si, 'gt5test:ng1.canterbury.ac.nz'), simpleMdsSubmitJob(si, 'gt5test:ng1.canterbury.ac.nz'), simpleMdsSubmitJobWith1SmallInputFile(si, 'gt5test:ng1.canterbury.ac.nz'),
 #      simpleSubmitJobWith5SmallInputFiles(si, 'gt5test:ng1.canterbury.ac.nz'), simpleSubmitJobWith1MediumInputFile(si, 'gt5test:ng1.canterbury.ac.nz'),simpleSubmitJobWith5MediumInputFiles(si, 'gt5test:ng1.canterbury.ac.nz'), simpleSubmitJobWith1_46mb_InputFile(si, 'gt5test:ng1.canterbury.ac.nz'), simpleSubmitJobWith5_46mb_InputFiles(si, 'gt5test:ng1.canterbury.ac.nz') )

actionsGT4 = (nonMdsJob(si), nonMdsJob5SmallInputFiles(si))
actionsGT5 = (nonMdsJob(si, 'gt5test:ng1.canterbury.ac.nz'), nonMdsJob5SmallInputFiles(si, 'gt5test:ng1.canterbury.ac.nz'))


for index in range(len(actionsGT4)):

    action = actionsGT4[index]
    print 'Action: '+action.name()
    print '\t('+action.description()+')\n'

    print 'GT4:'

    submitter.submit(si, action, 10)

    print

    print 'GT5:'
    action = actionsGT5[index]

    submitter.submit(si, action, 10)

    print

sys.exit()
