package edivad.extrastorage.items.item;

public enum ItemStorageType
{
    TIER_5(256),
    TIER_6(1024),
    TIER_7(4096),
    TIER_8(16384);

    private String name;
    private final int capacity;

    ItemStorageType(int capacity) {
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
