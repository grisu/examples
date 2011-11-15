
import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;
import grisu.model.FileManager;
import grisu.model.GrisuRegistryManager;
import grisu.model.MountPoint;
import grisu.model.UserEnvironmentManager;

import java.io.File;
import java.util.Iterator;

public class FileUpload {

	public static void main(final String[] args) throws Exception {

		final ServiceInterface si = LoginManager
				.loginCommandline("BeSTGRID-DEV");

		final FileManager fm = GrisuRegistryManager.getDefault(si)
				.getFileManager();

		final UserEnvironmentManager uem = GrisuRegistryManager.getDefault(si)
				.getUserEnvironmentManager();

		// String filename = "1.6mbInput0.bin";
		// String filename = "46mbInput0.bin";
		final String filename = "4gbfile.tst";

		final File file = new File("/media/data/tmp/", filename);

		final Iterator<MountPoint> i = uem.getNonVolatileMountPoints()
				.iterator();
		MountPoint mp = null;
		do {
			mp = i.next();
			System.out.println(mp.getFqan());
		} while (mp.getFqan().contains("Drug"));

		final String target = mp.getRootUrl() + "/" + filename;

		System.out.println("target: " + target);

		fm.uploadFile(file, target, true);

	}

}
