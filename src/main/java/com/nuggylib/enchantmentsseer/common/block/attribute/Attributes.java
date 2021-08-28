package com.nuggylib.enchantmentsseer.common.block.attribute;

/**
 * Inspired by the Mekanism codebase
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/block/attribute/Attributes.java"
 */
public class Attributes {
    public static final Attribute INVENTORY = new AttributeInventory();

    private Attributes() {
    }

    /** If a block has an inventory. */
    public static class AttributeInventory implements Attribute {

        private AttributeInventory() {
        }
    }

}
