package me.ItsJasonn.SurvivalGames.Listener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.ItsJasonn.SurvivalGames.Main.Core;
import me.ItsJasonn.SurvivalGames.Main.Plugin;

public class PlayerInteract implements Listener {
	Core core;
	public PlayerInteract(Core core) {
		this.core = core;
	}
	
	@SuppressWarnings({ "unchecked", "deprecation", "static-access" })
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if((event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) && Plugin.getCore().chestMode.contains(player)) {
			event.setCancelled(true);
			event.setUseInteractedBlock(Result.DENY);
			event.setUseItemInHand(Result.DENY);
			
			File file = new File(core.getDataFolder(), "/dat0/chests.yml");
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			if(player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR) {
				if(config.isList("content") && config.getList("content").contains(player.getItemInHand().getType().toString())) {
					player.sendMessage(ChatColor.GREEN + "This item has been removed from the list!");
					
					ArrayList<String> list = (ArrayList<String>) config.getList("content");
					list.remove(player.getItemInHand().getType().toString());
					
					config.set("content", list);
				} else {
					player.sendMessage(ChatColor.GREEN + "This item has been added to the list!");
					
					ArrayList<String> list = new ArrayList<String>();
					if(config.isList("content")) {
						list = (ArrayList<String>) config.getList("content");
					}
					list.add(player.getItemInHand().getType().toString());
					
					config.set("content", list);
				}
				try {
					config.save(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				player.sendMessage(ChatColor.RED + "You don't have an item in your hand to add it to the list!");
			}
		}
	}
}
