package me.itsjasonn.survivalgames.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class CLUpdate {
	private UpdateResult result = UpdateResult.DISABLED;
	private String version;
	@SuppressWarnings("unused")
	private Plugin plugin;
	private String message = null;

	public static enum UpdateResult {
		NO_UPDATE, DISABLED, UPDATE_AVAILABLE;
	}

	public CLUpdate(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public UpdateResult getResult() {
		return this.result;
	}

	public String getVersion() {
		return this.version;
	}

	public String doCurl(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setInstanceFollowRedirects(true);
		con.setDoOutput(true);
		con.setDoInput(true);
		DataOutputStream output = new DataOutputStream(con.getOutputStream());
		output.close();
		DataInputStream input = new DataInputStream(con.getInputStream());

		StringBuilder resultBuf = new StringBuilder();
		int c;
		while ((c = input.read()) != -1) {
			resultBuf.append((char) c);
		}
		input.close();
		return resultBuf.toString();
	}

	public String getMessage() {
		return this.message;
	}
}
