#!/bin/python
'''
Created on 14/06/2011

A HelloWorld example for the Scripted job submission workshop, eResearch Symposium 2011

@author: Aaron hicks, hicksa@landcarersearch.co.nz
'''

import subprocess
import sys.platform

hostname = subprocess.Popen('hostname', stdout=subprocess.PIPE).stdout.read()

if sys.platform == 'linux2':
    # NOTE: This is a standard Linux command, running it elsewhere will just crash
    whoami = subprocess.Popen('whoami', stdout=subprocess.PIPE).stdout.read()
else:
    # For not-Linux, do this instead:
    whoami = sys.platform

 
print 'Hello World from ' + whoami + ' on ' + hostname
