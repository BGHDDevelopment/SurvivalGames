package me.itsjasonn.survivalgames.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import me.itsjasonn.survivalgames.main.Plugin;
import org.bukkit.entity.Player;

public class Server {
	private String server = "";

	public Server(String server) {
		this.server = server;
	}

	public void sendPlayerToServer(Player player) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF("Connect");
			out.writeUTF(this.server);
		} catch (Exception e) {
			e.printStackTrace();
		}
		player.sendPluginMessage(Plugin.getCore(), "BungeeCord", b.toByteArray());
	}

	public String getName() {
		return this.server;
	}
}
