#!/bin/python
'''
Created on 14/06/2011

A HelloWorld example for the Scripted job submission workshop, eResearch Symposium 2011

@author: Aaron hicks, hicksa@landcarersearch.co.nz
'''

import subprocess, platform, sys


hostname = subprocess.Popen('hostname', stdout=subprocess.PIPE).stdout.read()

if platform.system() == 'Linux':
    # NOTE: This is a standard Linux command, running it elsewhere will just crash
    whoami = subprocess.Popen('whoami', stdout=subprocess.PIPE).stdout.read()
else:
    # For not-Linux, do this instead:
    whoami = platform.system

 
print 'Hello World from ' + whoami + ' on ' + hostname
sys.exit()