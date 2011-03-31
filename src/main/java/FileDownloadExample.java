import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;
import grisu.model.FileManager;
import grisu.model.GrisuRegistry;
import grisu.model.GrisuRegistryManager;

import java.io.File;

public class FileDownloadExample {

	public static void main(String[] args) throws Exception {

		// login
		// in this case we login via the commandline
		ServiceInterface si = LoginManager.loginCommandline("BeSTGRID");

		// create a registry. the registry is used to get objects that can
		// provide all kinds of grid and user information as well
		// as a filemanager which wraps up the most common file transfer
		// use cases
		final GrisuRegistry registry = GrisuRegistryManager.getDefault(si);

		// getting a filemanager object, which encapsulates file related actions
		FileManager fm = registry.getFileManager();

		// we need to know the full url to the source file
		String sourceUrl = "gsiftp://ng2.auckland.ac.nz/home/grid-admin/DC_nz_DC_org_DC_bestgrid_DC_slcs_O_The_University_of_Auckland_CN_Markus_Binsteiner__bK32o4Lh58A3vo9kKBcoKrJ7ZY/700mbFile.bin";
		// target file can be specified as either path or url
		File targetFile = new File("/home/markus/test/");
		String targetFileUrl = targetFile.toURI().toString();

		System.out.println("Downloading file from:\n\t" + sourceUrl
				+ "\nto\n\t" + targetFileUrl);

		fm.cp(sourceUrl, targetFileUrl, true);

	}
}
