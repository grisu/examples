import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;
import grisu.jcommons.constants.Constants;
import grisu.model.info.dto.DtoProperties;
import grisu.model.info.dto.DtoStringList;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class Admin {

	static ServiceInterface si;

	public static void execute(String command) {
		execute(command, new HashMap<String, String>());
	}

	public static void execute(String command, Map<String, String> config)
	{

		DtoStringList result = si.admin(command,
				DtoProperties.createProperties(config));
		String string = StringUtils.join(result.asArray(), "\n");
		System.out.println(string);


	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		// si = LoginManager
		// .loginCommandline("local_ws_jetty");
		// si = LoginManager.loginCommandline("local_ws_jetty");
		si = LoginManager.loginCommandline("nesi");

		System.out.println(si.getDN());

		execute(Constants.LIST_USERS);

		Map<String, String> config = new HashMap<String, String>();
		config.put(Constants.USER, Constants.ALL_USERS);
		execute(Constants.CLEAR_USER_CACHE, config);

		execute(Constants.REFRESH_CONFIG);

		execute(Constants.REFRESH_VOS);




		System.exit(0);

	}

}
