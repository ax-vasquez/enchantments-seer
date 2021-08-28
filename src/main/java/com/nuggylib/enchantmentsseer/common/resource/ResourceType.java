package com.nuggylib.enchantmentsseer.common.resource;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/resource/ResourceType.java"
 */
public enum ResourceType {
    SHARD("shard"),
    CRYSTAL("crystal"),
    DUST("dust"),
    DIRTY_DUST("dirty_dust"),
    CLUMP("clump"),
    INGOT("ingot"),
    NUGGET("nugget");

    private final String registryPrefix;
    private final String pluralPrefix;

    ResourceType(String prefix) {
        this(prefix, prefix + "s");
    }

    ResourceType(String prefix, String pluralPrefix) {
        this.registryPrefix = prefix;
        this.pluralPrefix = pluralPrefix;
    }

    public String getRegistryPrefix() {
        return registryPrefix;
    }

    public String getPluralPrefix() {
        return pluralPrefix;
    }
}
