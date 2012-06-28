package debugExamples;
import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;
import grisu.model.FileManager;
import grisu.model.GrisuRegistry;
import grisu.model.GrisuRegistryManager;
import grisu.model.dto.GridFile;
import grisu.model.status.StatusObject;

import java.util.Date;

public class FileCopyExample {

	public static void main(String[] args) throws Exception {

		// login
		// in this case we login via the commandline
		final ServiceInterface si = LoginManager.loginCommandline("local");

		// create a registry. the registry is used to get objects that can
		// provide all kinds of grid and user information as well
		// as a filemanager which wraps up the most common file transfer
		// use cases
		final GrisuRegistry registry = GrisuRegistryManager.getDefault(si);

		// getting a filemanager object, which encapsulates file related actions
		final FileManager fm = registry.getFileManager();

		// we need to know the full url to the source and target files
		//
		// you can figure those out via the Swing client for example, by
		// right-clicking a file in the file browser
		// in this example I create a GridFile object for each file, but the
		// filemanager can also handle urls directly
		// files can be local or remote, for either upload, cross-stage or
		// download
		final GridFile source = fm
				.createGridFile("grid://jobs/active/copytest");
		final GridFile target = fm
				.createGridFile("grid://groups/nz/nesi/targetLocation/");

		System.out.println("Downloading file from:\n\t" + source.getName()
				+ "\nto\n\t" + target.getName());

		// you can also just use the urls directly
		fm.cp(source, target, true);

		// in case you need to know when the file transfer is finished, you can
		// do that here
		// the handle for the copy action is the target url
		StatusObject.waitForActionToFinish(si, target.getUrl(), 4, true);

		System.out.println("Transfer finished.");

		// to check whether the file is actually where it is expected to be, we
		// can do an ls...
		final GridFile file = fm.ls(target.getUrl() + "/" + source.getName());

		System.out.println("Filesize: " + file.getSize());
		System.out.println("Last modified: "
				+ new Date(file.getLastModified()).toGMTString());

	}
}
