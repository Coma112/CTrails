package net.coma.ctrails.commands;

import net.coma.ctrails.CTrails;
import net.coma.ctrails.language.MessageKeys;
import net.coma.ctrails.subcommand.CommandInfo;
import net.coma.ctrails.subcommand.PluginCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@CommandInfo(name = "trailreload", permission = "ctrail.reload", requiresPlayer = false)
public class CommandReload extends PluginCommand {
    public CommandReload() {
        super("trailreload");
    }

    @Override
    public boolean run(@NotNull CommandSender sender, @NotNull String[] args) {
        CTrails.getInstance().getLanguage().reload();
        CTrails.getInstance().getConfiguration().reload();
        sender.sendMessage(MessageKeys.RELOAD);
        return true;
    }
}
