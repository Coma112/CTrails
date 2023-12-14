package net.coma.ctrails.utils;

import net.coma.ctrails.CTrails;
import net.coma.ctrails.commands.CommandReload;
import net.coma.ctrails.subcommand.PluginCommand;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CommandRegister {
    @SuppressWarnings("deprecation")
    public static void registerCommands() {
        for (Class<? extends PluginCommand> clazz : getCommandClasses()) {
            try {
                PluginCommand commandInstance = clazz.getDeclaredConstructor().newInstance();
                Objects.requireNonNull(Bukkit.getCommandMap()).register(CTrails.getInstance().getDescription().getName(), commandInstance);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
                exception.printStackTrace();
            }
        }
    }

    private static Set<Class<? extends PluginCommand>> getCommandClasses() {
        Set<Class<? extends PluginCommand>> commandClasses = new HashSet<>();
        commandClasses.add(CommandReload.class);

        return commandClasses;
    }
}
