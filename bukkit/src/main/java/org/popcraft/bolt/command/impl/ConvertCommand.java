package org.popcraft.bolt.command.impl;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.popcraft.bolt.BoltPlugin;
import org.popcraft.bolt.command.Arguments;
import org.popcraft.bolt.command.BoltCommand;
import org.popcraft.bolt.data.MemoryStore;
import org.popcraft.bolt.data.Store;
import org.popcraft.bolt.data.migration.lwc.BoltMigration;
import org.popcraft.bolt.data.migration.lwc.LWCMigration;
import org.popcraft.bolt.protection.BlockProtection;
import org.popcraft.bolt.protection.EntityProtection;
import org.popcraft.bolt.util.BoltComponents;
import org.popcraft.bolt.lang.Translation;

import java.util.Collections;
import java.util.List;

public class ConvertCommand extends BoltCommand {
    public ConvertCommand(BoltPlugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(CommandSender sender, Arguments arguments) {
        final boolean convertBack = arguments.remaining() > 0 && arguments.next().equals("back");
        if (convertBack) {
            final BoltMigration migration = new BoltMigration(plugin);
            BoltComponents.sendMessage(sender, Translation.MIGRATION_STARTED, Placeholder.unparsed("source", "Bolt"), Placeholder.unparsed("destination", "LWC"));
            migration.convert();
        } else {
            final LWCMigration migration = new LWCMigration(plugin);
            final MemoryStore converted = migration.convert();
            final Store destination = plugin.getBolt().getStore();
            BoltComponents.sendMessage(sender, Translation.MIGRATION_STARTED, Placeholder.unparsed("source", "LWC"), Placeholder.unparsed("destination", "Bolt"));
            for (final BlockProtection blockProtection : converted.loadBlockProtections().join()) {
                destination.saveBlockProtection(blockProtection);
            }
            for (final EntityProtection entityProtection : converted.loadEntityProtections().join()) {
                destination.saveEntityProtection(entityProtection);
            }
        }
        BoltComponents.sendMessage(sender, Translation.MIGRATION_COMPLETED);
    }

    @Override
    public List<String> suggestions(Arguments arguments) {
        if (arguments.remaining() == 0) {
            return Collections.emptyList();
        }
        arguments.next();
        if (arguments.remaining() == 0) {
            return List.of("back");
        }
        return Collections.emptyList();
    }
}
