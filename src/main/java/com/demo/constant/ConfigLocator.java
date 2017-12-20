package com.demo.constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.igfay.jfig.JFigLocator;

public class ConfigLocator extends JFigLocator {
	private String earPath;
	private String configPath;

	public ConfigLocator(String fileName) {
		super(fileName);
		this.init();
	}

	public String getEARPath() {
		return this.earPath;
	}

	public InputStream getInputStream() {
		FileInputStream inputStream = null;
		File file = null;

		try {
			file = new File(this.configPath, this.getConfigFileName());
			inputStream = new FileInputStream(file);
			return inputStream;
		} catch (FileNotFoundException arg5) {
			if (inputStream != null) {
				try {
					inputStream.close();
					inputStream = null;
				} catch (Exception arg4) {
					;
				}
			}
			System.out.println("Don\'t found config file (" + file + ")");
			throw new RuntimeException("Don\'t found config file (" + file + ")", arg5);
		}
	}

	private void init() {
		String filePath = this.getClass().getResource("/").getPath();
		// try {
		// filePath =
		// Thread.currentThread().getContextClassLoader().getResource("whereami").toString();
		// System.out.println("filePath:" + filePath);
		// } catch (NullPointerException arg2) {
		// System.out.println("Please put ear-locator.jar into classPath.\nIt will indicate path information for configuration.\n");
		// throw new RuntimeException(
		// "Please put ear-locator.jar into classPath.It will indicate path information for configuration.",
		// arg2);
		// }

		// if (filePath != null) {
		// System.out
		// .println("Constants.EAR_PATH_FOLLOWING:lib/ear-locator.jar");
		// System.out.println("Constants.WHERE_PATH_FOLLOWING:!/whereami");
		// if (filePath.indexOf("lib/ear-locator.jar") <= 0
		// || filePath.indexOf("!/whereami") <= 0) {
		// System.out
		// .println("[error] Please put ear-locator.jar into root classPath.\nIt will indicate path information for configuration.\n");
		// throw new RuntimeException(
		// "Please put ear-locator.jar into root classPath.It will indicate path information for configuration.");
		// }
		//
		// filePath = filePath.substring(filePath.indexOf("file:") + 5,
		// filePath.indexOf("lib/ear-locator.jar"));
		// }

		if (filePath != null) {
			this.earPath = filePath;
			// this.configPath = filePath + "conf/";
			this.configPath = filePath;
		}

	}
}