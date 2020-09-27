package edivad.extrastorage.setup.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class Proxy implements IProxy {

    @Override
    public PlayerEntity getClientPlayer()
    {
        throw new IllegalStateException("This should only be called from client side");
    }

    @Override
    public World getClientWorld()
    {
        throw new IllegalStateException("This should only be called from client side");
    }
}
