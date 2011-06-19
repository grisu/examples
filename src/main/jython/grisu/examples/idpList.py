'''
Created on 17/11/2009

@author: markus
'''
from grith.sibboleth import DummyIdpObject, DummyCredentialManager, \
    Shibboleth
from grisu.frontend.control.login import LoginManager
import sys
    
LoginManager.initEnvironment()

idpObj = DummyIdpObject();
cm = DummyCredentialManager();

shib = Shibboleth(idpObj, cm);

url = "https://slcs1.arcs.org.au/SLCS/login";
shib.openurl(url);
      
for idp in idpObj.getIdps():
   print idp

sys.exit()

