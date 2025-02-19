package org.popcraft.bolt.util;

import net.kyori.adventure.platform.bukkit.BukkitComponentSerializer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Nameable;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.popcraft.bolt.BoltPlugin;
import org.popcraft.bolt.protection.BlockProtection;
import org.popcraft.bolt.protection.EntityProtection;
import org.popcraft.bolt.protection.Protection;
import org.popcraft.bolt.lang.Strings;
import org.popcraft.bolt.lang.Translation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.popcraft.bolt.lang.Translator.translate;

public final class Protections {
    private Protections() {
    }

    public static String displayType(final Protection protection) {
        if (protection instanceof final BlockProtection blockProtection) {
            final World world = Bukkit.getWorld(blockProtection.getWorld());
            final int x = blockProtection.getX();
            final int y = blockProtection.getY();
            final int z = blockProtection.getZ();
            if (world == null || !world.isChunkLoaded(x >> 4, z >> 4)) {
                return Strings.toTitleCase(blockProtection.getBlock());
            } else {
                final Block block = world.getBlockAt(x, y, z);
                return displayType(block);
            }
        } else if (protection instanceof final EntityProtection entityProtection) {
            final Entity entity = Bukkit.getServer().getEntity(entityProtection.getId());
            if (entity == null) {
                return Strings.toTitleCase(entityProtection.getEntity());
            } else {
                return displayType(entity);
            }
        } else {
            return translate(Translation.UNKNOWN);
        }
    }

    public static String displayType(final Block block) {
        if (block.getState() instanceof final Nameable nameable) {
            final String customName = nameable.getCustomName();
            if (customName != null && !customName.isEmpty()) {
                return customName;
            }
        }
        final Component translatable = Component.translatable(block.getTranslationKey());
        return BukkitComponentSerializer.legacy().serialize(translatable);
    }

    public static String displayType(final Entity entity) {
        final String customName = entity.getCustomName();
        if (customName != null && !customName.isEmpty()) {
            return customName;
        }
        final Component translatable = Component.translatable(entity.getType().getTranslationKey());
        return BukkitComponentSerializer.legacy().serialize(translatable);
    }

    public static String accessList(final Protection protection) {
        final BoltPlugin boltPlugin = JavaPlugin.getPlugin(BoltPlugin.class);
        final Map<String, String> accessMap = protection.getAccess();
        if (accessMap == null || accessMap.isEmpty()) {
            return "";
        }
        final List<String> lines = new ArrayList<>();
        accessMap.forEach((source, access) -> {
            final String sourceType = Source.type(source);
            final String subject;
            if (Source.PLAYER.equals(sourceType)) {
                subject = boltPlugin.getProfileCache().getName(UUID.fromString(Source.identifier(source)));
            } else {
                subject = Strings.toTitleCase(Source.type(source));
            }
            lines.add("%s (%s)".formatted(subject, Strings.toTitleCase(access)));
        });
        return String.join("\n", lines);
    }
}
