package me.ItsJasonn.SurvivalGames.Listener;

import java.io.File;
import java.util.ArrayList;
import me.ItsJasonn.SurvivalGames.Main.Core;
import me.ItsJasonn.SurvivalGames.Main.Plugin;
import me.ItsJasonn.SurvivalGames.Map.Map;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class EntityStats implements Listener {
	Core core;
	
	private File file = new File(Plugin.getCore().getDataFolder(), "/data/maps.yml");
	private YamlConfiguration config = YamlConfiguration.loadConfiguration(this.file);
	
	public EntityStats(Core core) {
		this.core = core;
	}

	public static ArrayList<Player> antiDamage = new ArrayList<Player>();

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if(Map.getMapManager().isInGame(player) && (Map.inLobby || Map.ending || Map.getMapManager().isSpectating(player) || event.getCause() == DamageCause.LIGHTNING || antiDamage.contains(player))) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if(Map.getMapManager().isInGame(player) && (Map.inLobby || Map.ending || Map.getMapManager().isSpectating(player))) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if(Map.getMapManager().isInGame(player) && (Map.inLobby || Map.ending || Map.getMapManager().isSpectating(player))) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		if(Map.getMapManager().isInGame(player) && (Map.inLobby || Map.ending || Map.getMapManager().isSpectating(player))) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if (Map.getMapManager().isInGame(player)) {
			if (player.getKiller() != null) {
				event.setDeathMessage(ChatColor.GOLD + player.getName() + " was killed by " + player.getKiller().getName());
			} else {
				event.setDeathMessage(ChatColor.GOLD + player.getName() + "'s death was caused by " + ChatColor.GOLD + WordUtils.capitalizeFully(player.getLastDamageCause().getCause().toString().replace("_", " ")));
			}
			event.setDroppedExp(0);
			
			player.getWorld().strikeLightningEffect(player.getLocation());
			
			Map.getMapManager().setSpectator(player);
			
			int alive = 0;
			for(Player players : Map.getMapManager().getPlayers()) {
				if(!Map.getMapManager().isSpectating(players)) {
					alive++;
				}
			}
			if(alive <= 1) {
				Map.getMapManager().End();
			}
		}
	}

	@SuppressWarnings("static-access")
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		if (Map.getMapManager().isInGame(player)) {
			if(player.getKiller() != null) {
				event.setRespawnLocation(player.getKiller().getLocation());
			} else {
				int index = Map.getMapManager().playerID.get(player);
				Location location = new Location(Bukkit.getServer().getWorld(config.getString("spawns." + index + ".world")), this.config.getDouble("spawns." + index + ".x"), this.config.getDouble("spawns." + index + ".y"), this.config.getDouble("spawns." + index + ".z"), (float) this.config.getDouble("spawns." + index + ".yaw"), (float) this.config.getDouble("spawns." + index + ".pitch"));
				event.setRespawnLocation(location);
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (Map.getMapManager().isInGame(player)) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (Map.getMapManager().isInGame(player)) {
			event.setCancelled(true);
		}
	}
	
	@SuppressWarnings("static-access")
	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent event) {
		Player player = (Player) event.getPlayer();
		if ((Map.getMapManager().isInGame(player) && Map.getMapManager().isSpectating(player) || Plugin.getCore().chestMode.contains(player)) && event.getInventory().getType() == InventoryType.CHEST) {
			event.setCancelled(true);
		}
	}
}
