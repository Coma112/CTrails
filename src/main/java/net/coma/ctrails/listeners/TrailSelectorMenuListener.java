package net.coma.ctrails.listeners;

import net.coma.ctrails.CTrails;
import net.coma.ctrails.enums.Trails;
import net.coma.ctrails.menus.TrailSelectorMenu;
import net.coma.ctrails.storage.ItemStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

public class TrailSelectorMenuListener implements Listener {
    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (!event.getInventory().equals(TrailSelectorMenu.getSession(player))) return;

        TrailSelectorMenu.close(player);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        Inventory sessionInventory = TrailSelectorMenu.getSession(player);
        if (sessionInventory == null) return;
        if (!event.getInventory().equals(sessionInventory)) return;
        event.setCancelled(true);

        if (event.getClick() == ClickType.LEFT) {
            switch (event.getRawSlot()) {
                case 0 -> CTrails.getInstance().getToken().buyTrail(player, Trails.TOTEM);
                case 1 -> CTrails.getInstance().getToken().buyTrail(player, Trails.LAVA);
                case 2 -> CTrails.getInstance().getToken().buyTrail(player, Trails.CRIT);
                case 3 -> CTrails.getInstance().getToken().buyTrail(player, Trails.HEART);
            }
        } else {
            if (event.getClick() == ClickType.RIGHT) {
                switch (event.getRawSlot()) {
                    case 0 -> CTrails.getInstance().getToken().sellTrail(player, Trails.TOTEM);
                    case 1 -> CTrails.getInstance().getToken().sellTrail(player, Trails.LAVA);
                    case 2 -> CTrails.getInstance().getToken().sellTrail(player, Trails.CRIT);
                    case 3 -> CTrails.getInstance().getToken().sellTrail(player, Trails.HEART);
                }
            }
        }

        if (event.getRawSlot() == 8) CTrails.getDatabaseManager().clearTrails(player);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (player.getInventory().getItemInMainHand().equals(ItemStorage.TRAILS)) TrailSelectorMenu.open(player);
        }
    }
}
