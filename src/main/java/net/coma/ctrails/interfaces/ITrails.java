package net.coma.ctrails.interfaces;

import org.bukkit.Material;
import org.bukkit.Particle;

public interface ITrails {
    int getId();

    String getName();

    int getPrice();

    Material getMaterial();

    Particle getParticle();

    int getSellPrice();
}
