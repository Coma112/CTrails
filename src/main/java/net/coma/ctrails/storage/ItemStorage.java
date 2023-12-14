package net.coma.ctrails.storage;

import net.coma.ctrails.item.IItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemStorage {
    public static final ItemStack TRAILS = IItemBuilder.create(Material.DIAMOND)
            .setName("&b&lTRAILS")
            .finish();

    public static final ItemStack CLEAR_TRAIL = IItemBuilder.create(Material.RED_DYE)
            .setName("&cClear all trail!")
            .finish();
}
