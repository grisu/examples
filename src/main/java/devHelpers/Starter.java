package devHelpers;
import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;

public class Starter {

	/**
	 * A test commandline client used for testing frontend-backend interaction.
	 * 
	 * Not really an example client, so please ignore.
	 */
	public static void main(String[] args) throws Exception {

		final ServiceInterface si = LoginManager
		.loginCommandline("BeSTGRID-DEV");

		WsSessionTestClient c = new WsSessionTestClient(si);

		String input = null;

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try {
			System.out.print("Input: ");
			input = br.readLine();
			if (StringUtils.isBlank(input)) {
				input = "ls";
			} else if ("exit".equals(input)) {
				System.exit(0);
			}
		} catch (IOException ioe) {
			System.out.println("IO error trying to read user input!");
			System.exit(1);
		}

		while (!"exit".equals(input)) {

			WsSessionTestClient.METHOD m = WsSessionTestClient
			.getEnumFromString(WsSessionTestClient.METHOD.class, input);
			if (m == null) {
				System.out.println("No method: " + input);
				continue;
			}

			try {
				switch (m) {
				case ls:
					c.ls();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				String lastInput = input;
				System.out.print("Input: ");
				input = br.readLine();
				if (StringUtils.isBlank(input)) {
					input = lastInput;
				} else if ("exit".equals(input)) {
					System.exit(0);
				}
			} catch (IOException ioe) {
				System.out.println("IO error trying to read user input!");
				System.exit(1);
			}


		}

		si.logout();

	}

}
