package com.nuggylib.enchantmentsseer.common;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ConfigSpecs {
    public static ForgeConfigSpec commonSpec;
    public static Common commonConf;


    public static class Common {
        /**
         * Determines the system to use for charging and recharging Seer's Stones.
         * The XP System (0) uses levels to charge Seer's Stones and the Hearts System (1)
         * uses hearts to charge Seer's Stone. Both systems support configuring
         * the cost of charging a Seer's Stone.
         *
         * Default: 0; Options: 0, 1
         */
        public final ForgeConfigSpec.ConfigValue<Integer> chargingSystem;

        /**
         * Determines the total number of levels it costs to charge a Seer's Stone
         * when using the XP System. If you set the value to -1, the Seer's Stone
         * is unchangeable.
         *
         * Default: 10; Range: -1 - 1000
         */
        public final ForgeConfigSpec.ConfigValue<Integer> xpChargingCost;

        /**
         * Determines the total number of hearts it costs to charge a Seer's Stone
         * when using the Hearts System. If you set the value to -1, the Seer's Stone
         * is unchangeable.
         *
         * Default: 5; Range: -1 - 500
         */
        public final ForgeConfigSpec.ConfigValue<Integer> heartsChargingCost;

        /**
         * Determines the default state of Seer's Stones upon crafting.
         * When set to 0 (UNCHARGED), the player must charge the Seer's Stone
         * after crafting it.
         * When set to 1 (CHARGED), the Seer's Stone starts off charged.
         *
         * Default: 0; Options: 0, 1
         */
        public final ForgeConfigSpec.ConfigValue<Integer> defaultStoneState;

        /**
         * How the Seer's Stone enchantment recharge cost scales.
         * When set to 0 (STATIC), the recharge cost remains the same no matter
         * the number of enchantments on the item.
         * When set to 1 (MULTIPLY), the cost of recharging a Seer's Stone
         * scales proportionately to the number of enchantments on an item.
         * For example, an item with three enchantments costs three times
         * the recharging cost.
         *
         * Default: 0; Options: 0, 1
         */
        public final ForgeConfigSpec.ConfigValue<Integer> costScalingSystem;


        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("general");

            this.chargingSystem = builder.comment("Determines the system to use for charging and recharging Seer's Stones.")
                    .translation("enchantments-seer.config.chargingSystem.title")
                    .define("Charging System", 0);

            this.xpChargingCost = builder.comment("Determines the number of levels to charge a Seer's Stone.")
                    .translation("enchantments-seer.config.xpChargingCost.title")
                    .define("XP Charging Cost", 10);

            this.heartsChargingCost = builder.comment("Determines the number of hearts to charge a Seer's Stone.")
                    .translation("enchantments-seer.config.heartsChargingCost.title")
                    .define("Hearts Charging Cost", 5);

            this.defaultStoneState = builder.comment("Determines the initial state of a Seer's Stone upon crafting (uncharged or charged)")
                    .translation("enchantments-seer.config.defaultStoneState.title")
                    .define("Default Seer's Stone State", 0);

            this.costScalingSystem = builder.comment("Determines how to scale the cost of recharging Seer's Stones.")
                    .translation("enchantments-seer.config.costScalingSystem.title").push("costScalingSystem")
                    .define("Cost Scaling System", 0);

            builder.pop();
        }

        static {

            Pair<Common, ForgeConfigSpec> specPair2 = new ForgeConfigSpec.Builder().configure(Common::new);
            commonSpec = specPair2.getRight();
            commonConf = specPair2.getLeft();
        }
    }
}
