package com.nuggylib.enchantmentsseer.common;

public class Config {
    public static class Common {
        public static int chargingSystem;
        public static int xpChargingCost;
        public static int heartsChargingCost;
        public static int defaultStoneState;
        public static int costScalingSystem;

        public static void load() {

            chargingSystem = ConfigSpecs.commonConf.chargingSystem.get();
            xpChargingCost = ConfigSpecs.commonConf.xpChargingCost.get();
            heartsChargingCost = ConfigSpecs.commonConf.heartsChargingCost.get();
            defaultStoneState = ConfigSpecs.commonConf.defaultStoneState.get();
            costScalingSystem = ConfigSpecs.commonConf.costScalingSystem.get();


        }

    }
}
