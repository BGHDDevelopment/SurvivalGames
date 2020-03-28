package me.itsjasonn.survivalgames.listener;

import me.itsjasonn.survivalgames.main.Core;
import me.itsjasonn.survivalgames.map.Map;
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
