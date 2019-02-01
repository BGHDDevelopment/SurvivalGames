package me.ItsJasonn.SurvivalGames.Main;

import java.util.ArrayList;

import me.ItsJasonn.SurvivalGames.MetricsLite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import me.ItsJasonn.SurvivalGames.Commands.Hub;
import me.ItsJasonn.SurvivalGames.Commands.SG;
import me.ItsJasonn.SurvivalGames.Listener.EntityStats;
import me.ItsJasonn.SurvivalGames.Listener.PlayerInteract;
import me.ItsJasonn.SurvivalGames.Listener.PlayerJoin;
import me.ItsJasonn.SurvivalGames.Listener.PlayerQuit;
import me.ItsJasonn.SurvivalGames.Listener.ProjectileHit;
import me.ItsJasonn.SurvivalGames.Map.Map;

public class Core extends JavaPlugin implements PluginMessageListener {
	public static ArrayList<Player> chestMode = new ArrayList<Player>();
	
	@SuppressWarnings("deprecation")
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "-----------=======  Survival Games  =======-----------");
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "- All the files in this plugin are 'read-only'. This means that you are not allowed to open, remove, change, replace the file in any case!");
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "- If an error occurs you are supposed to contact the developer and DO NOT try out things yourself.");
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "- Report bugs to the developer if there are any.");
		Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "-----------=========================-----------");
		MetricsLite metrics = new MetricsLite(this);

		getCommand("Hub").setExecutor(new Hub());
		getCommand("SG").setExecutor(new SG());

		new Plugin(this);
		Bukkit.getServer().getPluginManager().registerEvents(new EntityStats(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteract(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerQuit(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new ProjectileHit(this), this);
		for (Player players : Bukkit.getOnlinePlayers()) {
			players.spigot().setCollidesWithEntities(false);
		}
		Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
		for (Player players : Bukkit.getOnlinePlayers()) {
			players.sendMessage(ChatColor.RED + "You have been disconnected from the server to avoid any problems during the reload process. After this process you will be able to join again.");
			me.ItsJasonn.SurvivalGames.Utils.Server server = new me.ItsJasonn.SurvivalGames.Utils.Server("lobby");
			server.sendPlayerToServer(players);
		}
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				for (World worlds : Bukkit.getServer().getWorlds()) {
					worlds.setStorm(false);
					worlds.setThundering(false);
				}
			}
		}, 0L, 8);
		
		Map.getMapManager().refillChests();
	}
	
	public void onDisable() {
		for (Player players : Bukkit.getOnlinePlayers()) {
			players.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
			if (Map.getMapManager().isInGame(players)) {
				Map.getMapManager().TeleportToHub(players);
				Map.getMapManager().removePlayer(players, false);
			}
		}
	}

	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
	}
}
