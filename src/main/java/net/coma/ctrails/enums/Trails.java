package net.coma.ctrails.enums;

import net.coma.ctrails.interfaces.ITrails;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public enum Trails implements ITrails {
    TOTEM(0, "&e&lTOTEM &fÖsvény", Material.TOTEM_OF_UNDYING, Particle.TOTEM, 100),
    LAVA(1, "&c&lLÁVA &fÖsvény", Material.LAVA_BUCKET, Particle.DRIP_LAVA, 200),
    CRIT(2, "&b&lSEBZÉS &fÖsvény", Material.AMETHYST_BLOCK, Particle.CRIT_MAGIC, 500),
    HEART(3, "&c&lSZERELEM &fÖsvény", Material.APPLE, Particle.HEART, 700);

    public final int id;
    public final int price;
    public final String name;
    public final Material material;
    public final Particle particle;

    Trails(int id, @NotNull String name, @NotNull Material material, @NotNull Particle particle, int price) {
        this.id = id;
        this.name = name;
        this.material = material;
        this.price = price;
        this.particle = particle;
    }

    public static Trails parseId(int id) {
        for (Trails effects : Trails.values()) {
            if (effects.getId() == id) return effects;
        }

        return null;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public Particle getParticle() {
        return particle;
    }

    @Override
    public int getSellPrice() {
        return price / 2;
    }
}
