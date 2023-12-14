package net.coma.ctrails.listeners;

import net.coma.ctrails.CTrails;
import net.coma.ctrails.storage.ItemStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        CTrails.getDatabaseManager().createPlayer(player);
        player.getInventory().setItem(3, ItemStorage.TRAILS);
    }
}
