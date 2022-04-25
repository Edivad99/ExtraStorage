package edivad.extrastorage.compat;

import com.refinedmods.refinedstorage.apiimpl.network.node.CrafterNetworkNode;
import com.refinedmods.refinedstorage.blockentity.CrafterBlockEntity;
import edivad.extrastorage.Main;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.nodes.AdvancedCrafterNetworkNode;
import edivad.extrastorage.blockentity.AdvancedCrafterBlockEntity;
import mcjty.theoneprobe.api.*;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public class TOPIntegration implements IProbeInfoProvider, Function<ITheOneProbe, Void> {

    @Override
    public Void apply(ITheOneProbe probe)
    {
        probe.registerProvider(this);
        return null;
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level level, BlockState blockState, IProbeHitData data)
    {
        BlockEntity te = level.getBlockEntity(data.getPos());
        int patterns = 0, speed = 0, slots = 0;

        if(te instanceof AdvancedCrafterBlockEntity tile) {
            AdvancedCrafterNetworkNode node = tile.getNode();
            CrafterTier tier = tile.getTier();
            patterns = node.getPatterns().size();
            speed = node.getMaximumSuccessfulCraftingUpdates();
            slots = tier.getSlots();
        } else if (te instanceof CrafterBlockEntity tile) {
            CrafterNetworkNode node = tile.getNode();
            patterns = node.getPatterns().size();
            speed = node.getMaximumSuccessfulCraftingUpdates();
            slots = 9;
        }
        if(te instanceof AdvancedCrafterBlockEntity || te instanceof CrafterBlockEntity) {
            probeInfo.horizontal().text(new TextComponent(String.format("Occupied space: %d/%d", patterns, slots)));
            probeInfo.horizontal().text(new TextComponent(String.format("Current speed: %dx", speed)));
        }
    }

    @Override
    public ResourceLocation getID()
    {
        return new ResourceLocation(Main.MODID ,"default");
    }
}
