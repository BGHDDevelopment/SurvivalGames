package me.ItsJasonn.SurvivalGames.Listener;

import me.ItsJasonn.SurvivalGames.Main.Core;
import me.ItsJasonn.SurvivalGames.Map.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
	Core core;

	public PlayerJoin(Core core) {
		this.core = core;
	}

	@SuppressWarnings("static-access")
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);

		Player player = event.getPlayer();
		Map.getMapManager().addPlayer(player, true);
		
		if(Map.getMapManager().inLobby) {
			Map.getMapManager().TeleportToLobby(player);
		} else {
			Map.getMapManager().setSpectator(player);
		}
	}
}
