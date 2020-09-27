package edivad.extrastorage.items.fluid;

public enum FluidStorageType
{
    TIER_5(16384),
    TIER_6(65536),
    TIER_7(262144),
    TIER_8(1048576);

    private String name;
    private final int capacity;

    FluidStorageType(int capacity) {
        this.name = capacity + "k";
        this.capacity = capacity * 1000;
    }

    public String getName() {
        return this.name;
    }

    public int getCapacity() {
        return this.capacity;
    }
}
