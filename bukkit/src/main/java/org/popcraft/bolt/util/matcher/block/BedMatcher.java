package org.popcraft.bolt.util.matcher.block;

import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Bed;
import org.popcraft.bolt.util.matcher.Match;

import java.util.Collections;
import java.util.Optional;

public class BedMatcher implements BlockMatcher {
    @Override
    public boolean canMatch(Block block) {
        return Tag.BEDS.isTagged(block.getType());
    }

    @Override
    public Optional<Match> findMatch(Block block) {
        if (block.getBlockData() instanceof final Bed bed) {
            if (Bed.Part.HEAD.equals(bed.getPart())) {
                final Block foot = block.getRelative(bed.getFacing());
                if (foot.getBlockData() instanceof Bed) {
                    return Optional.of(Match.ofBlocks(Collections.singleton(foot)));
                }
            } else {
                final Block head = block.getRelative(bed.getFacing().getOppositeFace());
                if (head.getBlockData() instanceof Bed) {
                    return Optional.of(Match.ofBlocks(Collections.singleton(head)));
                }
            }
        }
        return Optional.empty();
    }
}
