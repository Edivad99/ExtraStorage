package edivad.extrastorage.compat;

import com.refinedmods.refinedstorage.apiimpl.network.node.CrafterNetworkNode;
import com.refinedmods.refinedstorage.tile.CrafterTile;
import edivad.extrastorage.Main;
import edivad.extrastorage.blocks.CrafterTier;
import edivad.extrastorage.nodes.AdvancedCrafterNetworkNode;
import edivad.extrastorage.tiles.AdvancedCrafterTile;
import mcjty.theoneprobe.api.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.function.Function;

public class TOPIntegration implements IProbeInfoProvider, Function<ITheOneProbe, Void> {

    @Override
    public Void apply(ITheOneProbe probe)
    {
        probe.registerProvider(this);
        return null;
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData data)
    {
        TileEntity te = world.getTileEntity(data.getPos());
        int patterns = 0, speed = 0, slots = 0;

        if(te instanceof AdvancedCrafterTile) {
            AdvancedCrafterTile tile = (AdvancedCrafterTile) te;
            AdvancedCrafterNetworkNode node = tile.getNode();
            CrafterTier tier = tile.getTier();
            patterns = node.getPatterns().size();
            speed = node.getMaximumSuccessfulCraftingUpdates();
            slots = tier.getSlots();
        } else if (te instanceof CrafterTile) {
            CrafterTile tile = (CrafterTile) te;
            CrafterNetworkNode node = tile.getNode();
            patterns = node.getPatterns().size();
            speed = node.getMaximumSuccessfulCraftingUpdates();
            slots = 9;
        }
        if(te instanceof AdvancedCrafterTile || te instanceof CrafterTile) {
            probeInfo.horizontal().text(new StringTextComponent(String.format("Occupied space: %d/%d", patterns, slots)));
            probeInfo.horizontal().text(new StringTextComponent(String.format("Current speed: %dx", speed)));
        }
    }

    @Override
    public String getID()
    {
        return Main.MODID + ":default";
    }
}
