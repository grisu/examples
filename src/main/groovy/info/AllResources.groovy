package info

import grisu.frontend.control.login.LoginManager
import grisu.jcommons.interfaces.GridResource
import grisu.jcommons.utils.SubmissionLocationHelpers
import grith.jgrith.credential.Credential
import grith.jgrith.credential.CredentialFactory

import org.bestgrid.mds.SQLQueryClient
import org.globus.common.CoGProperties

import com.google.common.collect.Sets

CoGProperties.getDefault().setProperty(
		CoGProperties.ENFORCE_SIGNING_POLICY, "false");
LoginManager.initEnvironment()

Credential c = CredentialFactory.createFromCommandline();

//Credential c = new X509Credential("/home/markus/certs/test2.ceres.auckland.ac.nz_cert.pem",
//		"/home/markus/certs/test2.ceres.auckland.ac.nz_key.pem", "".toCharArray(), 24, true)

//def vos = c.getAvailableFqans().keySet()
def vos = [
	'/ARCS/LocalAccounts/CanterburyHPC'
]
//def vos = ['/ARCS/BeSTGRID']
//println "VOs: "+vos


SQLQueryClient client = new SQLQueryClient("jdbc:mysql://mysql-bg.ceres.auckland.ac.nz/mds_test?autoReconnect=true", "grisu_read", "password");
//QueryClient client = new QueryClient()

def sites = Sets.newTreeSet()

for ( def vo : vos ) {
	sites.addAll(client.getSitesForVO(vo))
}

println "Sites: "+sites

println ""
println "Filesystems"
println ""

for ( def site : sites ) {
	println "Site: "+site
	println '\tFileSystem'+client.getGridFTPServersAtSite(site)
}

println ""
println "Directories"
println ""


def datadirs = [:]

for ( def site : sites ) {
	def storageElements = client.getStorageElementsForSite(site)

	for ( def se : storageElements ) {
		for ( def group : vos ) {
			def dds = client.getDataDir(site, se, group)
			for ( def dd : dds ) {
				def values = datadirs.get(se+'/'+dd)
				if ( ! values ) {
					values = []
					datadirs.put(se+'/'+dd, values)
				}
				values.add(group)
			}
		}
	}
}

for ( def dd : datadirs.keySet() ) {
	println dd
	println '\t'+datadirs.get(dd)
}

println ""
println "Queues"
println ""


for (GridResource gr : client.getAllGridResources()) {
	def groups = Sets.newTreeSet()
	for ( def group : vos ) {
		String[] queues = client.getQueueNamesAtSite(gr.getSiteName(), group)
		if ( queues.asType(List.class).contains(gr.getQueueName())) {
			groups.add(group)
		}
	}


	if ( groups ) {
		def queueCodes = Sets.newTreeSet()
		def codes = client.getCodesAtSite(gr.getSiteName())
		for ( def code : codes ) {
			//			println 'CODE: '+code
			for ( def group : groups ) {
				//				println '\tGROUP: '+group
				def groupCodes = client.getCodesOnGridForVO(group).asType(List.class)
				//				println groupCodes
				if ( groupCodes.contains(code) ) {
					queueCodes.add(code)
				}
			}
		}
		def se = client.getDefaultStorageElementForQueueAtSite(gr.getSiteName(), gr.getQueueName())
		def subloc = SubmissionLocationHelpers.createSubmissionLocationString(gr)
		println subloc
		println '\tgroups:\t\t'+groups
		println '\tstorage:\t'+se
		for ( def qc : queueCodes ) {
			def versions = client.getVersionsOfCodeAtSite(gr.getSiteName(), qc)
			println "\tcode:\t\t"+qc+" - "+versions
			for (def v : versions) {
				println '\t\t\t\t'+v+':\t'+	client.getExeNameOfCodeForSubmissionLocation(subloc, qc, v)
			}
		}
		//println '\t'+queueCodes
		println ""
	}
}

System.exit(0)




