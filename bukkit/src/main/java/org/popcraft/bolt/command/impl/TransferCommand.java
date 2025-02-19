package org.popcraft.bolt.command.impl;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.popcraft.bolt.BoltPlugin;
import org.popcraft.bolt.command.Arguments;
import org.popcraft.bolt.command.BoltCommand;
import org.popcraft.bolt.util.Action;
import org.popcraft.bolt.util.BoltComponents;
import org.popcraft.bolt.lang.Translation;
import org.popcraft.bolt.util.BukkitAdapter;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class TransferCommand extends BoltCommand {
    public TransferCommand(BoltPlugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(CommandSender sender, Arguments arguments) {
        if (!(sender instanceof final Player player)) {
            BoltComponents.sendMessage(sender, Translation.COMMAND_PLAYER_ONLY);
            return;
        }
        if (arguments.remaining() < 1) {
            BoltComponents.sendMessage(sender, Translation.COMMAND_NOT_ENOUGH_ARGS);
            return;
        }
        final String owner = arguments.next();
        final UUID uuid = BukkitAdapter.findPlayerUniqueId(owner);
        if (uuid != null) {
            plugin.player(player).setAction(new Action(Action.Type.TRANSFER, uuid.toString()));
            BoltComponents.sendMessage(player, Translation.CLICK_TRANSFER);
        } else {
            BoltComponents.sendMessage(player, Translation.PLAYER_NOT_FOUND, Placeholder.unparsed("player", owner));
        }
    }

    @Override
    public List<String> suggestions(Arguments arguments) {
        if (arguments.remaining() == 0) {
            return Collections.emptyList();
        }
        arguments.next();
        if (arguments.remaining() == 0) {
            return plugin.getServer().getOnlinePlayers().stream().map(Player::getName).toList();
        }
        return Collections.emptyList();
    }
}
