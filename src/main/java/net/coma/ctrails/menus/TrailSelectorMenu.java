package net.coma.ctrails.menus;

import net.coma.ctrails.CTrails;
import net.coma.ctrails.enums.Trails;
import net.coma.ctrails.item.IItemBuilder;
import net.coma.ctrails.processor.MessageProcessor;
import net.coma.ctrails.storage.ItemStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrailSelectorMenu {
    private static final Map<UUID, Inventory> playerInventories = new HashMap<>();
    private static final Map<Inventory, Integer> refreshTasks = new HashMap<>();

    public static void close(@NotNull Player player) {
        Inventory inventory = playerInventories.remove(player.getUniqueId());
        if (inventory != null) Bukkit.getScheduler().cancelTask(refreshTasks.remove(inventory));
    }

    public static Inventory getSession(@NotNull Player player) {
        return playerInventories.get(player.getUniqueId());
    }

    @SuppressWarnings("deprecation")
    public static void open(@NotNull Player player) {
        if (playerInventories.containsKey(player.getUniqueId())) return;

        Inventory inventory = Bukkit.createInventory(player, 9, MessageProcessor.process("&bVálasztható Ösvények"));
        playerInventories.put(player.getUniqueId(), inventory);

        refresh(player);
        run(player, inventory);

        inventory.setItem(8, ItemStorage.CLEAR_TRAIL);
        player.openInventory(inventory);
    }

    public static void refresh(@NotNull Player player) {
        Inventory inventory = playerInventories.get(player.getUniqueId());

        if (inventory != null) {
            updateItem(inventory, player, 0, Trails.TOTEM);
            updateItem(inventory, player, 1, Trails.LAVA);
            updateItem(inventory, player, 2, Trails.CRIT);
            updateItem(inventory, player, 3, Trails.HEART);
        }
    }

    private static void updateItem(@NotNull Inventory inventory, @NotNull Player player, int slot, @NotNull Trails trails) {
        inventory.setItem(slot, IItemBuilder.create(trails.getMaterial())
                .setName(MessageProcessor.process(trails.getName()))
                .addLore("")
                .addLore("&6Bal klikk | Vásárlás")
                .addLore("&6Jobb klikk | Eladás")
                .addLore("")
                .addLore(!CTrails.getInstance().getToken().hasTrail(player, trails) ? "&6Vásárlási ár &8» &a" + trails.getPrice() : CTrails.getDatabaseManager().isActiveTrail(player, trails) ? "&aKiválasztva" : "&aKattints a kiválasztáshoz!")
                .addLore("&6Eladási ár &8» &a" + trails.getSellPrice())
                .finish());
    }

    private static void run(@NotNull Player player, @NotNull Inventory inventory) {
        refreshTasks.put(inventory, Bukkit.getScheduler().scheduleSyncRepeatingTask(CTrails.getInstance(), () -> {
            if (!playerInventories.containsKey(player.getUniqueId())) Bukkit.getScheduler().cancelTask(refreshTasks.remove(inventory));
            refresh(player);
        }, 20L, 5L));
    }
}
