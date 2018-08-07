package me.ItsJasonn.SurvivalGames.Listener;

import me.ItsJasonn.SurvivalGames.Main.Core;
import me.ItsJasonn.SurvivalGames.Map.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {
	Core core;

	public PlayerQuit(Core core) {
		this.core = core;
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);

		Player player = event.getPlayer();
		if (Map.getMapManager().isInGame(player)) {
			Map.getMapManager().TeleportToHub(player);
			Map.getMapManager().removePlayer(player, true);
			if (Map.getMapManager().getPlayers().size() == 0) {
				Map.getMapManager().StopMatch();
			}
		}
	}
}
