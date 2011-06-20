'''
Created on 14/06/2011

A dictionary counter example for the Scripted job submission workshop, eResearch Symposium 2011

@author: Aaron hicks, hicksa@landcarersearch.co.nz
'''

import sys
import os

current_dir = os.path.abspath(os.path.curdir)

print "START: Starting countbacon dictionary processor"

# First check that the script is called correctly
if (len(sys.argv[1:]) != 2):
    print "HALT: Incorrect usage!"
    print "Requires arguments for dictionary and input files"
    print "e.g. java -jar ../lib/grisu-jython.jar countbacon.py dictionary.txt input/file.txt"
    sys.exit(1)

# Process arguments and test they're valid
dictionary_path, input_path = sys.argv[1:]
print "INFO: Dictionary file path: " + dictionary_path
print "INFO: Input file path:      " + input_path

if not os.path.isfile(dictionary_path):
    print "HALT: Dictionary file " + dictionary_path + " is not a file"
    sys.exit(1)

if not os.path.isfile(input_path):
    print "HALT: Input file " + input_path + " is not a file"
    sys.exit(1)

print "INFO: Processing dictionary"
dictionary = []
dictionary_FILE = open(dictionary_path, 'r')
# We expect one word per line, so we can handle phrases too.
for line in dictionary_FILE:
    dictionary.append(line.rstrip())
print "INFO: dictionary loaded, " + str(len(dictionary)) + " items found."    

print "INFO: Initialising counter"
counter = {}
for phrase in dictionary:
    counter[phrase] = 0

# we are going to parse the input file for each phrase in the dictionary
print "INFO: Processing " + input_path
input_FILE = open(input_path)
sys.stdout.write("INFO: Processing lines")
for line in input_FILE:
    sys.stdout.write(".")
    for phrase in dictionary:
        counter[phrase] += line.lower().count(phrase.lower())
input_FILE.close()
print "Done!"
print "INFO: Finished processing " + input_path

for phrase, count in counter.iteritems():
    print "RESULT: the phrase \"" + phrase + "\" occurred " + str(count) + " times"

print "FINISHED: All the phrases are counted"
    
        

