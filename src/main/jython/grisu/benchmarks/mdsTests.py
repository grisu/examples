'''
Created on 28/06/2010

@author: markus
'''
from grisu.jcommons.constants import Constants
from grisu.frontend.model.job import JobObject
from grisu.jython.benchmarks.exampleJobs import action, defaultSubLoc
from grisu.model import GrisuRegistryManager
from grisu.model.job import JobSubmissionObjectImpl

class nonMdsJob(action):

    def __init__(self, si, subLoc=defaultSubLoc):
        super(nonMdsJob, self).__init__(si, subLoc)

    def prepare(self):

        job = JobSubmissionObjectImpl()
        job.setTimestampJobname("gt4_5_bench")
        job.setCommandline("echo hello")
        job.setApplication(Constants.GENERIC_APPLICATION_NAME)
        job.setSubmissionLocation(self.subLoc)
        fqan = GrisuRegistryManager.getDefault(self.si).getUserEnvironmentManager().getCurrentFqan()
        self.jobname = self.si.createJob(job.getJobDescriptionDocumentAsString(), fqan, Constants.FORCE_NAME_METHOD)

    def execute(self):

        self.si.submitJob(self.jobname)

    def description(self):
        return "Non-mds job without any input files."

    def name(self):

        return 'nonMdsJob'
    
class nonMdsJob5SmallInputFiles(action):

    def __init__(self, si, subLoc=defaultSubLoc):
        super(nonMdsJob5SmallInputFiles, self).__init__(si, subLoc)

    def prepare(self):

        job = JobObject(self.si);
        job.setUniqueJobname("echo_job1")
        job.setCommandline("echo \"Hello World\"")
        job.addInputFileUrl("gsiftp://ng2.canterbury.ac.nz/home/gridcloud061/tmp/text0.txt");
        job.addInputFileUrl("gsiftp://ng2.canterbury.ac.nz/home/gridcloud061/tmp/text1.txt");
        job.addInputFileUrl("gsiftp://ng2.canterbury.ac.nz/home/gridcloud061/tmp/text2.txt");
        job.addInputFileUrl("gsiftp://ng2.canterbury.ac.nz/home/gridcloud061/tmp/text3.txt");
        job.addInputFileUrl("gsiftp://ng2.canterbury.ac.nz/home/gridcloud061/tmp/text4.txt");
        job.setApplication(Constants.GENERIC_APPLICATION_NAME)
        job.setSubmissionLocation(self.subLoc)

        job.createJob()
        
        self.job = job        

    def execute(self):

        self.si.submitJob(self.job.getJobname())

    def description(self):
        return "Non-mds job with 5 small input files."

    def name(self):

        return 'Simple mds job, 5 small input files.'