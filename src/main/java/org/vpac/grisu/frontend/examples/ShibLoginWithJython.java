package org.vpac.grisu.frontend.examples;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.ietf.jgss.GSSException;

public class ShibLoginWithJython {

	/**
	 * @param args
	 * @throws GeneralSecurityException
	 * @throws GSSException
	 * @throws IOException
	 */
	public static void main(String[] args) throws GeneralSecurityException,
			IOException, GSSException {

		final String username = args[0];
		final String password = args[1];
		// String idp = args[2];
		final String url = "https://slcs1.arcs.org.au/SLCS/login";

		// SLCS slcs = new SLCS(url);
		//
		// slcs.init(username, password.toCharArray(), "VPAC");
		//
		// GSSCredential cred = PlainProxy.init(slcs.getCertificate(),
		// slcs.getPrivateKey(), 12);
		//
		// CredentialHelpers.writeToDisk(cred, new File(LocalProxy.PROXY_FILE));

	}

}
