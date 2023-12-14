package net.coma.ctrails.listeners;

import net.coma.ctrails.CTrails;
import net.coma.ctrails.enums.Trails;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Random;

public class TrailListener implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Trails activeTrail = CTrails.getDatabaseManager().getActiveTrail(event.getPlayer());

        if (activeTrail != null) {
            Random random = new Random();
            int i;

            for (i = 0; i < 5; i++) event.getPlayer().getWorld().spawnParticle(activeTrail.getParticle(), event.getPlayer().getLocation().add(random.nextDouble() * 0.5D, random.nextDouble() * 0.5D, random.nextDouble() * 0.5D), 0);
            for (i = 0; i < 5; i++) event.getPlayer().getWorld().spawnParticle(activeTrail.getParticle(), event.getPlayer().getLocation().add(-1.0D * random.nextDouble() * 0.5D, random.nextDouble() * 0.5D, random.nextDouble() * 0.5D * -1.0D), 0);
        }
    }
}
