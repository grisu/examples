import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;
import grisu.model.FileManager;
import grisu.model.GrisuRegistry;
import grisu.model.GrisuRegistryManager;
import grisu.model.dto.GridFile;

import java.util.Date;

public class FilePropertyExample {

	/**
	 * Example that shows how to get file properties like filesize and last
	 * modified timestamp.
	 */
	public static void main(String[] args) throws Exception {

		// login
		// in this case we login via the commandline
		final ServiceInterface si = LoginManager.loginCommandline("BeSTGRID");

		// create a registry. the registry is used to get objects that can
		// provide all kinds of grid and user information as well
		// as a filemanager which wraps up the most common file transfer
		// use cases
		final GrisuRegistry registry = GrisuRegistryManager.getDefault(si);

		// getting a filemanager object, which encapsulates file related actions
		final FileManager fm = registry.getFileManager();

		// need to know the full url to the source file
		// we could query the file properies directly via the filemanager or the
		// service interface, but creating a GridFile is probably a more elegant
		// way because it makes subsequent use of the object easier
		final GridFile source = fm
				.createGridFile("gsiftp://ng2.auckland.ac.nz/home/grid-workshop/DC_nz_DC_org_DC_bestgrid_DC_slcs_O_The_University_of_Auckland_CN_Markus_Binsteiner__bK32o4Lh58A3vo9kKBcoKrJ7ZY/700mbFile.bin");

		System.out.println("Filesize: " + source.getSize());
		System.out.println("Last modified: "
				+ new Date(source.getLastModified()).toGMTString());

	}
}
