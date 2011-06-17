'''
Created on 14/06/2011

@author: hicksa
'''

import sys
import os

current_dir = os.path.abspath(os.path.curdir)


# First check that the script is called correctly
if (len(sys.argv[1:]) != 2):
    print "HALT: Incorrect usage!"
    print "Requires arguments for dictionary and input files"
    print "e.g. java -jar ../lib/grisu-jython.jar countbacon.py dictionary.txt input/file.txt"
    sys.exit(1)

# Assign arguments and test they're valid
dictionary_path, input_path = sys.argv[1:]
print "Dictionary: "+dictionary_path
print "Input:      "+input_path

if not os.path.isfile(dictionary_path):
    print "HALT: Dictionary file "+dictionary_path+" is not a file"
    sys.exit(1)

if not os.path.isfile(input_path):
    print "HALT: Input file "+input_path+" is not a file"
    sys.exit(1)


