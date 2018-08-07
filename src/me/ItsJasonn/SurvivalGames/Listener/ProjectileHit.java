package me.ItsJasonn.SurvivalGames.Listener;

import me.ItsJasonn.SurvivalGames.Main.Core;
import me.ItsJasonn.SurvivalGames.Map.Map;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ProjectileHit implements Listener {
	Core core;

	public ProjectileHit(Core core) {
		this.core = core;
	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		if(event.getEntity() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getEntity();
			if(arrow.getShooter() != null && (arrow.getShooter() instanceof Player)) {
				Player player = (Player) arrow.getShooter();
				if(Map.getMapManager().isInGame(player)) {
					arrow.remove();
				}
			}
		}
	}
}
