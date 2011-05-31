package org.vpac.grisu.frontend.examples;

import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginParams;
import grisu.frontend.control.login.ServiceInterfaceFactory;

import java.io.File;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

public class FileUpload {

	public static void main(final String[] args) throws Exception {

		final String username = args[0];
		final char[] password = args[1].toCharArray();

		final LoginParams loginParams = new LoginParams(
		// "http://localhost:8080/xfire-backend/services/grisu",
		// "https://ngportal.vpac.org/grisu-ws/soap/EnunciateServiceInterfaceService",
		// "https://ngportaldev.vpac.org/grisu-ws/services/grisu",
		// "Local",
		// "LOCAL_WS",
				"ARCS_DEV",
				// "http://localhost:8080/grisu-ws/soap/GrisuService",
				// "http://ngportal.vpac.org:8080/grisu-ws/soap/GrisuService",
				username, password);

		final ServiceInterface si = ServiceInterfaceFactory
				.createInterface(loginParams);

		final String url1 = "gsiftp://ng2.vpac.org/home/arcscloud001/C_AU_O_APACGrid_OU_VPAC_CN_Markus_Binsteiner/grisu-dir/ffff_0001/stdout.txt";
		final String url2 = "gsiftp://ng2.vpac.org/home/arcscloud001/C_AU_O_APACGrid_OU_VPAC_CN_Markus_Binsteiner/test";

		System.out.println(si.getDN());

		// fileUpload
		final DataHandler uploadDataHandler = new DataHandler(
				new FileDataSource(new File(System.getProperty("user.home"),
						"large.iso")));
		si.upload(
				uploadDataHandler,
				"gsiftp://ng2.vpac.org/home/arcscloud001/C_AU_O_APACGrid_OU_VPAC_CN_Markus_Binsteiner/test");

		// fileDownload
		// DataHandler downloadDataHandler = service.download(url2);
		// FileOutputStream outputStream = new FileOutputStream(new File(System
		// .getProperty("user.home"), "largeFileDownload.iso"));
		// InputStream inputStream = downloadDataHandler.getInputStream();
		//
		// try {
		// byte[] buffer = new byte[1024 * 4];
		// for (int n; (n = inputStream.read(buffer)) != -1;) {
		// outputStream.write(buffer, 0, n);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// } finally {
		// outputStream.close();
		// inputStream.close();
		// }
		// this doesn't seem to work... classcastexception
		// StreamingDataHandler sdh =
		//
		// (StreamingDataHandler)(service.download(url1));
		// try {
		// sdh.moveTo(new File("/home/markus/Desktop/largeFileDownloaded.iso"));
		// } catch (Throwable e) {
		// e.printStackTrace();
		// }

	}

}
