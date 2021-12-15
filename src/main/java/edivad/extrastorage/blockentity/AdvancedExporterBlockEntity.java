package edivad.extrastorage.blockentity;

import com.refinedmods.refinedstorage.blockentity.NetworkNodeBlockEntity;
import com.refinedmods.refinedstorage.blockentity.config.IComparable;
import com.refinedmods.refinedstorage.blockentity.config.IType;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationParameter;
import edivad.extrastorage.setup.Registration;
import edivad.extrastorage.nodes.AdvancedExporterNetworkNode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class AdvancedExporterBlockEntity extends NetworkNodeBlockEntity<AdvancedExporterNetworkNode>
{
    public static final BlockEntitySynchronizationParameter<Integer, AdvancedExporterBlockEntity> COMPARE = IComparable.createParameter();
    public static final BlockEntitySynchronizationParameter<Integer, AdvancedExporterBlockEntity> TYPE = IType.createParameter();

    public AdvancedExporterBlockEntity(BlockPos pos, BlockState state)
    {
        super(Registration.ADVANCED_EXPORTER_TILE.get(), pos, state);

        dataManager.addWatchedParameter(COMPARE);
        dataManager.addWatchedParameter(TYPE);
    }

    @Override
    public AdvancedExporterNetworkNode createNode(Level level, BlockPos pos)
    {
        return new AdvancedExporterNetworkNode(level, pos);
    }
}
