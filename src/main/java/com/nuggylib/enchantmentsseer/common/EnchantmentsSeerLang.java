package com.nuggylib.enchantmentsseer.common;

import com.nuggylib.enchantmentsseer.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.client.render.ILangEntry;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.Util;

/**
 * Shamelessly-copied from the Mekanism codebase (because they are awesome and their stuff works <3)
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/MekanismLang.java"
 */
public enum EnchantmentsSeerLang implements ILangEntry {
    //Vanilla lang strings we use, for purposes of not having to have them copy pasted all over the place
    REPAIR_COST("container.repair.cost"),
    REPAIR_EXPENSIVE("container.repair.expensive"),
    //Gui lang strings
    ENCHANTMENTS_SEER("constants", "mod_name"),
    DEBUG_TITLE("constants", "debug_title"),
    LOG_FORMAT("constants", "log_format"),
    FORGE("constants", "forge"),
    IC2("constants", "ic2"),
    ERROR("constants", "error"),
    ALPHA_WARNING("constants", "alpha_warning"),
    ALPHA_WARNING_HERE("constants", "alpha_warning.here"),
    //Equipment
    HEAD("equipment", "head"),
    BODY("equipment", "body"),
    LEGS("equipment", "legs"),
    FEET("equipment", "feet"),
    MAINHAND("equipment", "mainhand"),
    OFFHAND("equipment", "offhand"),
    //Compass Directions
    NORTH_SHORT("direction", "north.short"),
    SOUTH_SHORT("direction", "south.short"),
    WEST_SHORT("direction", "west.short"),
    EAST_SHORT("direction", "east.short"),
    //Multiblock Stuff
    MULTIBLOCK_INVALID_FRAME("multiblock", "invalid_frame"),
    MULTIBLOCK_INVALID_INNER("multiblock", "invalid_inner"),
    MULTIBLOCK_INVALID_CONTROLLER_CONFLICT("multiblock", "invalid_controller_conflict"),
    MULTIBLOCK_INVALID_NO_CONTROLLER("multiblock", "invalid_no_controller"),
    //QIO stuff
    SET_FREQUENCY("qio", "set_frequency"),
    QIO_FREQUENCY_SELECT("qio", "qio_frequency_select"),
    QIO_ITEMS_DETAIL("qio", "items_detail"),
    QIO_TYPES_DETAIL("qio", "types_detail"),
    QIO_ITEMS("qio", "items"),
    QIO_TYPES("qio", "types"),
    QIO_TRIGGER_COUNT("qio", "trigger_count"),
    QIO_STORED_COUNT("qio", "stored_count"),
    QIO_ITEM_TYPE_UNDEFINED("qio", "item_type_undefined"),
    QIO_IMPORT_WITHOUT_FILTER("qio", "import_without_filter"),
    QIO_EXPORT_WITHOUT_FILTER("qio", "export_without_filter"),
    QIO_COMPENSATE_TOOLTIP("qio", "compensate_tooltip"),
    LIST_SORT_COUNT("qio", "sort_count"),
    LIST_SORT_NAME("qio", "sort_name"),
    LIST_SORT_MOD("qio", "sort_mod"),
    LIST_SORT_NAME_DESC("qio", "sort_name_desc"),
    LIST_SORT_COUNT_DESC("qio", "sort_count_desc"),
    LIST_SORT_MOD_DESC("qio", "sort_mod_desc"),
    LIST_SORT_ASCENDING_DESC("qio", "sort_ascending_desc"),
    LIST_SORT_DESCENDING_DESC("qio", "sort_descending_desc"),
    LIST_SEARCH("qio", "list_search"),
    LIST_SORT("qio", "list_sort"),
    //JEI
    JEI_AMOUNT_WITH_CAPACITY("tooltip", "jei.amount.with.capacity"),
    //Built into JEI
    JEI_MISSING_ITEMS("jei.tooltip.error.recipe.transfer.missing"),
    JEI_INVENTORY_FULL("jei.tooltip.error.recipe.transfer.inventory.full"),
    JEI_RECIPE_ID("jei.tooltip.recipe.id"),
    //Key
    KEY_HAND_MODE("key", "mode"),
    KEY_HEAD_MODE("key", "head_mode"),
    KEY_CHEST_MODE("key", "chest_mode"),
    KEY_LEGS_MODE("key", "legs_mode"),
    KEY_FEET_MODE("key", "feet_mode"),
    KEY_DETAILS_MODE("key", "details"),
    KEY_DESCRIPTION_MODE("key", "description"),
    KEY_MODULE_TWEAKER("key", "module_tweaker"),
    KEY_BOOST("key", "key_boost"),
    KEY_HUD("key", "key_hud"),
    //Generic
    GENERIC_PERCENT("generic", "percent"),
    GENERIC_WITH_COMMA("generic", "with_comma"),
    GENERIC_STORED("generic", "stored"),
    GENERIC_STORED_MB("generic", "stored.mb"),
    GENERIC_MB("generic", "mb"),
    GENERIC_PRE_COLON("generic", "pre_colon"),
    GENERIC_SQUARE_BRACKET("generic", "square_bracket"),
    GENERIC_PARENTHESIS("generic", "parenthesis"),
    GENERIC_WITH_PARENTHESIS("generic", "with_parenthesis"),
    GENERIC_WITH_TWO_PARENTHESIS("generic", "with_two_parenthesis"),
    GENERIC_FRACTION("generic", "fraction"),
    GENERIC_TRANSFER("generic", "transfer"),
    GENERIC_PER_TICK("generic", "per_tick"),
    GENERIC_PER_MB("generic", "per_mb"),
    GENERIC_PRE_STORED("generic", "pre_pre_colon"),
    GENERIC_BLOCK_POS("generic", "block_pos"),
    GENERIC_HEX("generic", "hex"),
    //Hold for
    HOLD_FOR_DETAILS("tooltip", "hold_for_details"),
    HOLD_FOR_DESCRIPTION("tooltip", "hold_for_description"),
    HOLD_FOR_MODULES("tooltip", "hold_for_modules"),
    HOLD_FOR_SUPPORTED_ITEMS("tooltip", "hold_for_supported_items"),
    //Gui stuff
    HEIGHT("gui", "height"),
    WIDTH("gui", "width"),
    CRAFTING_TAB("gui", "crafting.tab"),
    CRAFTING_WINDOW("gui", "crafting.window"),
    PROGRESS("gui", "progress"),
    PROCESS_RATE("gui", "process_rate"),
    PROCESS_RATE_MB("gui", "process_rate_mb"),
    TICKS_REQUIRED("gui", "ticks_required"),
    MIN("gui", "min"),
    MAX("gui", "max"),
    INFINITE("gui", "infinite"),
    NONE("gui", "none"),
    EMPTY("gui", "empty"),
    MAX_OUTPUT("gui", "max_output"),
    STORING("gui", "storing"),
    USING("gui", "using"),
    NEEDED("gui", "needed"),
    NEEDED_PER_TICK("gui", "needed_per_tick"),
    FINISHED("gui", "finished"),
    NO_RECIPE("gui", "no_recipe"),
    EJECT("gui", "eject"),
    MOVE_UP("gui", "move_up"),
    MOVE_DOWN("gui", "move_down"),
    SET("gui", "set"),
    TRUE("gui", "true"),
    FALSE("gui", "false"),
    CLOSE("gui", "close"),
    OPACITY("gui", "opacity"),
    //GUI Issues
    ISSUES("gui", "issues"),
    ISSUE_NO_SPACE_IN_OUTPUT("gui", "issues.no_space"),
    ISSUE_NO_MATCHING_RECIPE("gui", "issues.no_recipe"),
    //Frequency
    FREQUENCY("frequency", "format"),
    NO_FREQUENCY("frequency", "none"),
    FREQUENCY_DELETE_CONFIRM("frequency", "delete_confirm"),
    //Tab
    MAIN_TAB("tab", "main"),
    //Status
    STATUS("status", "format"),
    STATUS_OK("status", "ok"),
    //Boolean values
    YES("boolean", "yes"),
    NO("boolean", "no"),
    ON("boolean", "on"),
    OFF("boolean", "off"),
    INPUT("boolean", "input"),
    OUTPUT("boolean", "output"),
    ACTIVE("boolean", "active"),
    DISABLED("boolean", "disabled"),
    ON_CAPS("boolean", "on_caps"),
    OFF_CAPS("boolean", "off_caps"),
    //Capacity
    CAPACITY("capacity", "generic"),
    CAPACITY_ITEMS("capacity", "items"),
    CAPACITY_MB("capacity", "mb"),
    CAPACITY_PER_TICK("capacity", "per_tick"),
    CAPACITY_MB_PER_TICK("capacity", "mb.per_tick"),
    //Crafting Formula
    INGREDIENTS("crafting_formula", "ingredients"),
    ENCODED("crafting_formula", "encoded"),
    //Security
    SECURITY("tooltip", "security"),
    SECURITY_OVERRIDDEN("security", "overridden"),
    SECURITY_OFFLINE("security", "offline"),
    SECURITY_ADD("security", "add"),
    SECURITY_OVERRIDE("security", "override"),
    NO_ACCESS("security", "no_access"),
    TRUSTED_PLAYERS("security", "trusted_players"),
    PUBLIC("security", "public"),
    TRUSTED("security", "trusted"),
    PRIVATE("security", "private"),
    PUBLIC_MODE("security", "public.mode"),
    TRUSTED_MODE("security", "trusted.mode"),
    PRIVATE_MODE("security", "private.mode"),
    //Configurator Modes
    CONFIGURATOR_VIEW_MODE("configurator", "view_mode"),
    CONFIGURATOR_TOGGLE_MODE("configurator", "toggle_mode"),
    CONFIGURATOR_CONFIGURATE("configurator", "configurate"),
    CONFIGURATOR_EMPTY("configurator", "empty"),
    CONFIGURATOR_ROTATE("configurator", "rotate"),
    CONFIGURATOR_WRENCH("configurator", "wrench"),
    //Modules
    MODULE_ENABLED("module", "enabled"),
    MODULE_ENABLED_LOWER("module", "enabled_lower"),
    MODULE_DISABLED_LOWER("module", "disabled_lower"),
    MODULE_DAMAGE("module", "damage"),
    MODULE_TWEAKER("module", "module_tweaker"),
    MODULE_INSTALLED("module", "installed"),
    MODULE_STACKABLE("module", "stackable"),
    MODULE_EXCLUSIVE("module", "exclusive"),
    MODULE_HANDLE_MODE_CHANGE("module", "handle_mode_change"),
    MODULE_RENDER_HUD("module", "render_hud"),
    MODULE_MODE("module", "mode"),
    MODULE_ATTACK_DAMAGE("module", "attack_damage"),
    MODULE_FARMING_RADIUS("module", "farming_radius"),
    MODULE_JUMP_BOOST("module", "jump_boost"),
    MODULE_STEP_ASSIST("module", "step_assist"),
    MODULE_RANGE("module", "range"),
    MODULE_SPRINT_BOOST("module", "sprint_boost"),
    MODULE_EXTENDED_MODE("module", "extended_mode"),
    MODULE_EXTENDED_ENABLED("module", "extended_enabled"),
    MODULE_EXCAVATION_RANGE("module", "mining_range"),
    MODULE_EFFICIENCY("module", "efficiency"),
    MODULE_BREATHING_HELD("module", "breathing.held"),
    MODULE_JETPACK_MODE("module", "jetpack_mode"),
    MODULE_GRAVITATIONAL_MODULATION("module", "gravitational_modulation"),
    MODULE_MAGNETIC_ATTRACTION("module", "magnetic_attraction"),
    MODULE_MODE_CHANGE("module", "mode_change"),
    MODULE_CHARGE_SUIT("module", "charge_suit"),
    MODULE_CHARGE_INVENTORY("module", "charge_inventory"),
    MODULE_SPEED_BOOST("module", "speed_boost"),
    MODULE_VISION_ENHANCEMENT("module", "vision_enhancement"),
    MODULE_PURIFICATION_BENEFICIAL("module", "purification.beneficial"),
    MODULE_PURIFICATION_NEUTRAL("module", "purification.neutral"),
    MODULE_PURIFICATION_HARMFUL("module", "purification.harmful"),
    MODULE_TELEPORT_REQUIRES_BLOCK("module", "teleportation_requires_block"),
    ;

    private final String key;

    EnchantmentsSeerLang(String type, String path) {
        this(Util.makeDescriptionId(type, EnchantmentsSeer.rl(path)));
    }

    EnchantmentsSeerLang(String key) {
        this.key = key;
    }

    @Override
    public String getTranslationKey() {
        return key;
    }

    public static EnchantmentsSeerLang get(EquipmentSlotType type) {
        switch (type) {
            case HEAD:
                return HEAD;
            case CHEST:
                return BODY;
            case LEGS:
                return LEGS;
            case FEET:
                return FEET;
            case MAINHAND:
                return MAINHAND;
            case OFFHAND:
                return OFFHAND;
        }
        return null;
    }
}
