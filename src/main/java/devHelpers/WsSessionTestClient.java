package devHelpers;

import grisu.control.ServiceInterface;
import grisu.model.FileManager;
import grisu.model.GrisuRegistryManager;
import grisu.model.UserEnvironmentManager;
import grisu.model.dto.GridFile;

/**
 * Used to test frontend-backend interactions.
 * 
 * Please ignore this one for now.
 * 
 * @author Markus Binsteiner
 * 
 */
public class WsSessionTestClient {

	enum METHOD {
		ls
	}

	/**
	 * A common method for all enums since they can't have another base class
	 * 
	 * @param <T>
	 *            Enum type
	 * @param c
	 *            enum type. All enums must be all caps.
	 * @param string
	 *            case insensitive
	 * @return corresponding enum, or null
	 */
	public static <T extends Enum<T>> T getEnumFromString(Class<T> c,
			String string) {
		if ((c != null) && (string != null)) {
			try {
				return Enum.valueOf(c, string.trim().toLowerCase());
			} catch (final IllegalArgumentException ex) {
			}
		}
		return null;
	}

	private final ServiceInterface si;
	private final FileManager fm;
	private final UserEnvironmentManager uem;

	public WsSessionTestClient(ServiceInterface si) {
		this.si = si;
		this.fm = GrisuRegistryManager.getDefault(si).getFileManager();
		this.uem = GrisuRegistryManager.getDefault(si)
				.getUserEnvironmentManager();

	}

	public void ls() throws Exception {
		final GridFile f = fm.ls("grid://groups/ARCS/BeSTGRID");
		for (final GridFile fi : f.getChildren()) {
			System.out.println("Name: " + fi.getName());
			System.out.println("\tComment: " + fi.getComment());
		}
	}

}
