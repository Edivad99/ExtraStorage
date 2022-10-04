package edivad.extrastorage.items.storage.item;

public enum ItemStorageType {
    TIER_5(256),
    TIER_6(1024),
    TIER_7(4096),
    TIER_8(16384);

    private final int capacity;
    private final String name;

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
