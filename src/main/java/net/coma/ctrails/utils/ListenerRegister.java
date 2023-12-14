package net.coma.ctrails.utils;

import net.coma.ctrails.CTrails;
import net.coma.ctrails.listeners.PlayerListener;
import net.coma.ctrails.listeners.TrailListener;
import net.coma.ctrails.listeners.TrailSelectorMenuListener;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;

public class ListenerRegister {
    @SuppressWarnings("deprecation")
    public static void registerEvents() {
        Set<Class<? extends Listener>> listenerClasses = getListenerClasses();

        for (Class<? extends Listener> clazz : listenerClasses) {
            try {
                CTrails.getInstance().getServer().getPluginManager().registerEvents(clazz.newInstance(), CTrails.getInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static Set<Class<? extends Listener>> getListenerClasses() {
        Set<Class<? extends Listener>> listenerClasses = new HashSet<>();
        listenerClasses.add(PlayerListener.class);
        listenerClasses.add(TrailSelectorMenuListener.class);
        listenerClasses.add(TrailListener.class);

        return listenerClasses;
    }
}
