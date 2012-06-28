package info;

import grisu.control.info.SqlInfoManager;
import grisu.jcommons.model.info.IDirectory;

public class InfoConverter {

	public static void main(String[] args) throws Exception {

		SqlInfoManager im = new SqlInfoManager();


		for (IDirectory d : im.getDirectoriesForVO("/nz/nesi")) {
			System.out.println(d.getUrl());
		}

	}

}
