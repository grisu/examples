'''
Created on 16/06/2010

@author: markus
'''
from __future__ import with_statement
from grisu.jcommons.constants import Constants
from grisu.frontend.model.job import JobObject
from grisu.model import GrisuRegistryManager
from grisu.model.job import JobSubmissionObjectImpl
import time


class Timer(object):
    def __enter__(self):
        self.__start = time.time()

    def __exit__(self, type, value, traceback):
        # Error handling here
        self.__finish = time.time()

    def duration_in_seconds(self):
        return self.__finish - self.__start

class submitter(object):

    def submit(self, si, job, times, includePreparation=False):

        exectimes = []

        for i in range(times):
            
            if includePreparation: 
                with timer:
                    job.prepare()
                    job.execute()
                print '\t\t\t\tSubmission time (iteration '+str(i)+'): '+str(timer.duration_in_seconds())
                exectimes.append(timer.duration_in_seconds())
            else:
                job.prepare()
                with timer:
                    job.execute()
                print '\t\t\t\tSubmission time (iteration '+str(i)+'): '+str(timer.duration_in_seconds())
                exectimes.append(timer.duration_in_seconds())

        return exectimes

timer = Timer()

defaultSubLoc = 'small:ng2.canterbury.ac.nz'


class action(object):
    
    def __init__(self, si, subLoc=defaultSubLoc):
        self.si = si
        self.subLoc = subLoc
        try:
            self.siInfo = si.getInterfaceInfo()
        except:
            self.siInfo = "ARCS"
    
class simpleStageJob(action):
    
    def __init__(self, si, subLoc=defaultSubLoc):
        super(simpleStageJob, self).__init__(si, subLoc)
        
        self.job = JobObject(self.si)
        self.job.setCommandline("echo \"hello world\"")
        self.job.setApplication(Constants.GENERIC_APPLICATION_NAME)
        self.job.setSubmissionLocation(self.subLoc)
        
    def prepare(self):

        self.job.setUniqueJobname(self.name())
        self.job.createJob()
        
    def execute(self):
        self.job.submitJob()


# actions to execute
class simpleSubmitJob(simpleStageJob):

    def getInputFiles(self):
        return []
    
    def description(self):
        return 'Simple job with no input file.'

    def name(self):
        return 'SimpleJob_no_input_file'
    
class dynamicStageJob(simpleStageJob):
    
    def __init__(self, si, subLoc=defaultSubLoc):
        super(dynamicStageJob, self).__init__(si, subLoc)
        self.smallFiles = 0
        self.mediumFiles = 0
        self.bigFiles = 0
        self.smallRemote = False
        self.mediumRemote = False
        self.bigRemote = False

    def setInputFiles(self, inputFiles):
        
        remote = inputFiles[0]
        smallFiles = inputFiles[1]
        mediumFiles = inputFiles[2]
        bigFiles = inputFiles[3]
        
        self.addSmallInputFiles(smallFiles, remote)
        self.addMediumInputFiles(mediumFiles, remote)
        self.addBigInputFiles(bigFiles, remote)

    def addSmallInputFiles(self, amount=0, remote=False):
        
        amount = amount -1
        if amount < 0:
            return

        self.smallRemote = remote
        self.smallFiles = self.smallFiles + amount + 1

        for i in range(amount):
            if remote:
                url = "gsiftp://ng2hpc.ceres.auckland.ac.nz/home/grid-admin/DC_au_DC_org_DC_arcs_DC_slcs_O_ARCS_IdP_CN_Markus_Binsteiner_wArwA1Ra5jf6RG_HIZy45nORR_g/tmp/text"+str(i)+".txt"
            else:
                url = "/home/markus/tmp/text"+str(i)+".txt"
                
            self.job.addInputFileUrl(url)
            
    def addMediumInputFiles(self, amount=0, remote=False):

        amount = amount -1
        if amount < 0:
            return

        self.mediumFiles = self.mediumFiles + amount + 1
        self.mediumRemote = remote

        for i in range(amount):
            if remote:
                url = "gsiftp://ng2hpc.ceres.auckland.ac.nz/home/grid-admin/DC_au_DC_org_DC_arcs_DC_slcs_O_ARCS_IdP_CN_Markus_Binsteiner_wArwA1Ra5jf6RG_HIZy45nORR_g/tmp/1.6mbInput"+str(i)+".bin"
            else:
                url = "/home/markus/tmp/1.6mbInput"+str(i)+".bin"
                
            self.job.addInputFileUrl(url)
            
    def addBigInputFiles(self, amount=0, remote=False):

        amount = amount -1
        if amount < 0:
            return
        
        self.bigFiles = self.bigFiles + amount + 1
        self.bigRemote = remote    
        
        for i in range(amount):
            if remote:
                url = "gsiftp://ng2hpc.ceres.auckland.ac.nz/home/grid-admin/DC_au_DC_org_DC_arcs_DC_slcs_O_ARCS_IdP_CN_Markus_Binsteiner_wArwA1Ra5jf6RG_HIZy45nORR_g/tmp/46mbInput"+str(i)+".bin"
            else:
                url = "/home/markus/tmp/46mbInput"+str(i)+".bin"
                
            self.job.addInputFileUrl(url)
    
    def name(self):
        
        if self.smallRemote:
            srString = "r"
        else: 
            srString = "l"
            
        if self.mediumRemote:
            mrString = "r"
        else:
            mrString = "l"
            
        if self.bigRemote:
            brString = "r"
        else:
            brString = "l"
        
        #return str(self.smallFiles)+srString+"_"+str(self.mediumFiles)+mrString+"_"+str(self.bigFiles)+brString
        return "Test"

    def description(self):
        
        if self.smallRemote:
            srString = "Remote"
        else: 
            srString = "Local"
            
        if self.mediumRemote:
            mrString = "Remote"
        else:
            mrString = "Local"
            
        if self.bigRemote:
            brString = "Remote"
        else:
            brString = "Local"
            
        return "Simple job submission with "+str(self.smallFiles)+" small input files ("+srString+"), "+str(self.mediumFiles)+" medium (1.6mb - "+mrString+") and "+str(self.bigFiles)+" (46mb - "+brString+") input files.";


class simpleSubmitJobWith1SmallInputFileLocal(simpleStageJob):
        
    def getInputFiles(self):
        return ["/home/markus/tmp/text0.txt"]

    def description(self):
        return "Simple job submission with one local small (text-)input file."

    def name(self):
        return 'SimpleJob_1_small_input_file_local'
    
class simpleSubmitJobWith1SmallInputFileGrid(simpleStageJob):
        
    def getInputFiles(self):
        return ["gsiftp://ng2hpc.ceres.auckland.ac.nz/home/grid-admin/DC_au_DC_org_DC_arcs_DC_slcs_O_ARCS_IdP_CN_Markus_Binsteiner_wArwA1Ra5jf6RG_HIZy45nORR_g/tmp/text0.txt"]

    def description(self):
        return "Simple job submission with one remote small (text-)input file."

    def name(self):
        return 'SimpleJob_1_small_input_file_grid'

class simpleSubmitJobWith1MediumInputFile(simpleStageJob):

    def __init__(self, si, subLoc=defaultSubLoc):
        super(simpleSubmitJobWith1MediumInputFile, self).__init__(si, subLoc)
        
    def getInputFiles(self):
        return ["/home/markus/tmp/1.6mbInput0.bin"]

    def description(self):
        return "Simple job submission with 1 medium (1.6 mb) input file. SubmissionLocation (Canterbury) and Application package specified (no mds needed to calculate)."

    def name(self):

        return 'SimpleJob_1_1.6mb_input_file'

class simpleSubmitJobWith5MediumInputFiles(simpleStageJob):

    def __init__(self, si, subLoc=defaultSubLoc):
        super(simpleSubmitJobWith5MediumInputFiles, self).__init__(si, subLoc)
        
    def prepare(self):
        job = JobObject(self.si);
        job.setUniqueJobname("echo_job1")
        job.setCommandline("echo \"Hello World\"")
        job.addInputFileUrl("/home/markus/tmp/1.6mbInput0.bin");
        job.addInputFileUrl("/home/markus/tmp/1.6mbInput1.bin");
        job.addInputFileUrl("/home/markus/tmp/1.6mbInput2.bin");
        job.addInputFileUrl("/home/markus/tmp/1.6mbInput3.bin");
        job.addInputFileUrl("/home/markus/tmp/1.6mbInput4.bin");
        job.setApplication(Constants.GENERIC_APPLICATION_NAME)
        job.setSubmissionLocation(self.subLoc)

        job.createJob()
        
        self.job = job
   
    def execute(self):

        self.job.submitJob()

    def description(self):
        return "Simple job submission with5  medium (1.6 mb) input files. SubmissionLocation (Canterbury) and Application package specified (no mds needed to calculate)."

    def name(self):

        return 'SimpleJob_5_1.6mb_input_files'


class simpleSubmitJobWith1_46mb_InputFile(action):

    def __init__(self, si, subLoc=defaultSubLoc):
        super(simpleSubmitJobWith1_46mb_InputFile, self).__init__(si, subLoc)
        
    def prepare(self):
        job = JobObject(self.si);
        job.setUniqueJobname("echo_job1")
        job.setCommandline("echo \"Hello World\"")
        job.addInputFileUrl("/home/markus/tmp/46mbInput0.bin");
        job.setApplication(Constants.GENERIC_APPLICATION_NAME)
        job.setSubmissionLocation(self.subLoc)

        job.createJob()

        self.job = job
   
    def execute(self):

        self.job.submitJob()

    def description(self):
        return "Simple job submission with 1 46mb input file. SubmissionLocation (Canterbury) and Application package specified (no mds needed to calculate)."

    def name(self):

        return 'SimpleJob_1_46mb_input_file'

class simpleSubmitJobWith5_46mb_InputFiles(action):

    def __init__(self, si, subLoc=defaultSubLoc):
        super(simpleSubmitJobWith5_46mb_InputFiles, self).__init__(si, subLoc)
        
    def prepare(self):
        job = JobObject(self.si);
        job.setUniqueJobname("echo_job1")
        job.setCommandline("echo \"Hello World\"")
        job.addInputFileUrl("/home/markus/tmp/46mbInput0.bin");
        job.addInputFileUrl("/home/markus/tmp/46mbInput1.bin");
        job.addInputFileUrl("/home/markus/tmp/46mbInput2.bin");
        job.addInputFileUrl("/home/markus/tmp/46mbInput3.bin");
        job.addInputFileUrl("/home/markus/tmp/46mbInput4.bin");
        job.setApplication(Constants.GENERIC_APPLICATION_NAME)
        job.setSubmissionLocation(self.subLoc)

        job.createJob()
        self.job = job
   
    def execute(self):

        self.job.submitJob()

    def description(self):
        return "Simple job submission with 5 46mb input files. SubmissionLocation (Canterbury) and Application package specified (no mds needed to calculate)."

    def name(self):

        return 'SimpleJob_5_46mb_input_files'


class simpleSubmitJobWith5SmallInputFiles(action):

    def __init__(self, si, subLoc=defaultSubLoc):
        super(simpleSubmitJobWith5SmallInputFiles, self).__init__(si, subLoc)
        
    def prepare(self):
        job = JobObject(self.si);
        job.setUniqueJobname("echo_job1")
        job.setCommandline("echo \"Hello World\"")
        job.addInputFileUrl("/home/markus/tmp/text0.txt");
        job.addInputFileUrl("/home/markus/tmp/text1.txt");
        job.addInputFileUrl("/home/markus/tmp/text2.txt");
        job.addInputFileUrl("/home/markus/tmp/text3.txt");
        job.addInputFileUrl("/home/markus/tmp/text4.txt");

        job.setApplication(Constants.GENERIC_APPLICATION_NAME)
        job.setSubmissionLocation(self.subLoc)

        job.createJob()
        self.job = job
   
    def execute(self):

        self.job.submitJob()

    def description(self):
        return "Simple job submission with 5 small (text-)input files. SubmissionLocation (Canterbury) and Application package specified (no mds needed to calculate)."

    def name(self):

        return 'SimpleJob_5_small_input_files'

class simpleMdsSubmitJob(action):

    def __init__(self, si, subLoc=defaultSubLoc):
        super(simpleMdsSubmitJob, self).__init__(si, subLoc)
        
    def prepare(self):
        job = JobObject(self.si);
        job.setUniqueJobname("echo_job1")
        job.setCommandline("echo \"Hello World\"")

        job.createJob()
        self.job = job
   
    def execute(self):

        self.job.submitJob()

    def description(self):

        return 'Simple job with no input file. Mds calculates the submissionLocation, application package and version to use from commandline.'

    def name(self):

        return 'SimpleJob_MDS'

class simpleMdsSubmitJobWith1SmallInputFile(action):

    def __init__(self, si, subLoc=defaultSubLoc):
        super(simpleMdsSubmitJobWith1SmallInputFile, self).__init__(si, subLoc)
        
    def prepare(self):

        job = JobObject(self.si);
        job.setUniqueJobname("echo_job1")
        job.setCommandline("echo \"Hello World\"")
        job.addInputFileUrl("/home/markus/tmp/text0.txt");

        job.createJob()
        self.job = job
   
    def execute(self):

        self.job.submitJob()

    def description(self):
        return "Simple job submission with one small (text-)input file. Mds calculates the submissionLocation, application package and version to use from commandline."

    def name(self):

        return 'SimpleJob_MDS_1_input_file'
