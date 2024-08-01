package com.tiviacz.travelersbackpack.config;

import com.tiviacz.travelersbackpack.init.ModItems;
import com.tiviacz.travelersbackpack.inventory.menu.slot.BackpackSlotItemHandler;
import com.tiviacz.travelersbackpack.inventory.menu.slot.ToolSlotItemHandler;
import com.tiviacz.travelersbackpack.util.Reference;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class TravelersBackpackConfig
{
    public static class Server
    {
        private static final String REGISTRY_NAME_MATCHER = "([a-z0-9_.-]+:[a-z0-9_/.-]+)";

        public final BackpackSettings backpackSettings;
        public final World world;
        public final BackpackAbilities backpackAbilities;
        public final SlownessDebuff slownessDebuff;

        Server(final ForgeConfigSpec.Builder builder)
        {
            builder.comment("Server config settings")
                    .push("server");

            //Backpack Settings
            backpackSettings = new BackpackSettings(builder, "backpackSettings");

            //World
            world = new World(builder, "world");

            //Abilities
            backpackAbilities = new BackpackAbilities(builder, "backpackAbilities");

            //Slowness Debuff
            slownessDebuff = new SlownessDebuff(builder, "slownessDebuff");

            builder.pop();
        }

        public static class BackpackSettings
        {
            public final BackpackSettings.TierConfig leather;
            public final BackpackSettings.TierConfig iron;
            public final BackpackSettings.TierConfig gold;
            public final BackpackSettings.TierConfig diamond;
            public final BackpackSettings.TierConfig netherite;
            public final ForgeConfigSpec.BooleanValue enableTierUpgrades;
            public final BackpackSettings.CraftingUpgradeConfig craftingUpgrade;
            public final ForgeConfigSpec.BooleanValue rightClickEquip;
            public final ForgeConfigSpec.BooleanValue rightClickUnequip;
            public final ForgeConfigSpec.BooleanValue allowOnlyEquippedBackpack;
            public final ForgeConfigSpec.BooleanValue invulnerableBackpack;
            public final ForgeConfigSpec.BooleanValue toolSlotsAcceptSwords;
            public final ForgeConfigSpec.BooleanValue toolSlotsAcceptEverything;
            public final ForgeConfigSpec.ConfigValue<List<? extends String>> toolSlotsAcceptableItems;
            public final ForgeConfigSpec.ConfigValue<List<? extends String>> blacklistedItems;
            public final ForgeConfigSpec.BooleanValue allowShulkerBoxes;
            public final ForgeConfigSpec.BooleanValue voidProtection;
            public final ForgeConfigSpec.BooleanValue backpackDeathPlace;
            public final ForgeConfigSpec.BooleanValue backpackForceDeathPlace;
            public final ForgeConfigSpec.BooleanValue enableSleepingBagSpawnPoint;
            public final ForgeConfigSpec.BooleanValue curiosIntegration;

            BackpackSettings(final ForgeConfigSpec.Builder builder, final String path)
            {
                builder.push(path);

                //Backpack Settings

                leather = new BackpackSettings.TierConfig(builder, "Leather", 27, 2, 3000);
                iron = new BackpackSettings.TierConfig(builder, "Iron", 36, 3, 4000);
                gold = new BackpackSettings.TierConfig(builder, "Gold", 45, 4, 5000);
                diamond = new BackpackSettings.TierConfig(builder, "Diamond", 54, 5, 6000);
                netherite = new BackpackSettings.TierConfig(builder, "Netherite", 63, 6, 7000);

                enableTierUpgrades = builder
                        .define("enableTierUpgrades", true);

                craftingUpgrade = new CraftingUpgradeConfig(builder, "craftingUpgrade");

                rightClickEquip = builder
                        .comment("Enables equipping the backpack on right-click from the ground")
                        .define("rightClickEquip", true);

                rightClickUnequip = builder
                        .comment("Enables unequipping the backpack on right-click on the ground with empty hand")
                        .define("rightClickUnequip", false);

                allowOnlyEquippedBackpack = builder
                        .comment("Allows to use only equipped backpack")
                        .define("allowOnlyEquippedBackpack", false);

                invulnerableBackpack = builder
                        .comment("Backpack immune to any damage source (lava, fire), can't be destroyed, never disappears as floating item")
                        .define("invulnerableBackpack", true);

                toolSlotsAcceptSwords = builder
                        .define("toolSlotsAcceptSwords", true);

                toolSlotsAcceptEverything = builder
                        .comment("Tool slots accept any item")
                        .define("toolSlotsAcceptEverything", false);

                toolSlotsAcceptableItems = builder
                        .comment("List of items that can be put in tool slots (Use registry names, for example: \"minecraft:apple\", \"minecraft:flint\")")
                        .defineList("toolSlotsAcceptableItems", Collections.emptyList(), mapping -> ((String)mapping).matches(REGISTRY_NAME_MATCHER));

                blacklistedItems = builder
                        .comment("List of items that can't be put in backpack inventory (Use registry names, for example: \"minecraft:apple\", \"minecraft:flint\")")
                        .defineList("blacklistedItems", Collections.emptyList(), mapping -> ((String)mapping).matches(REGISTRY_NAME_MATCHER));

                allowShulkerBoxes = builder
                        .comment("Allows putting shulker boxes and other items with inventory in backpack")
                        .define("allowShulkerBoxes", false);

                voidProtection = builder
                        .comment("Prevents backpack disappearing in void, spawns floating backpack above minimum Y when player dies in void")
                        .define("voidProtection", true);

                backpackDeathPlace = builder
                        .comment("Places backpack at place where player died")
                        .define("backpackDeathPlace", true);

                backpackForceDeathPlace = builder
                        .comment("Places backpack at place where player died, replacing all blocks that are breakable and do not have inventory (backpackDeathPlace must be true in order to work)")
                        .define("backpackForceDeathPlace", false);

                enableSleepingBagSpawnPoint = builder
                        .define("enableSleepingBagSpawnPoint", false);

                curiosIntegration = builder
                        .comment("If true, backpack can only be worn by placing it in curios 'Back' slot", "WARNING - Remember to TAKE OFF BACKPACK BEFORE enabling or disabling this integration!! - if not you'll lose your backpack")
                        .define("curiosIntegration", false);

                builder.pop();
            }

            public static class TierConfig
            {
                public final ForgeConfigSpec.IntValue inventorySlotCount;
                public final ForgeConfigSpec.IntValue toolSlotCount;
                public final ForgeConfigSpec.IntValue tankCapacity;

                public TierConfig(ForgeConfigSpec.Builder builder, String tier, int inventorySlotCountDefault, int toolSlotCountDefault, int tankCapacityDefault)
                {
                    builder.comment(tier + " Tier Backpack Settings").push(tier.toLowerCase(Locale.ENGLISH) + "TierBackpack");

                    inventorySlotCount =
                            builder.comment("Number of inventory slots for the tier")
                                    .defineInRange("inventorySlotCount", inventorySlotCountDefault, 1, 63);

                    toolSlotCount =
                            builder.comment("Number of tool slots for the tier")
                                    .defineInRange("toolSlotCount", toolSlotCountDefault, 0, 6);

                    tankCapacity =
                            builder.comment("Tank capacity for the tier, 1000 equals 1 Bucket")
                                    .defineInRange("tankCapacity", tankCapacityDefault, 1, 128000);

                    builder.pop();
                }
            }

            public record Tier(int inventorySlotCount, int toolSlotCount, int tankCapacity) { }

            public static class CraftingUpgradeConfig
            {
                public final ForgeConfigSpec.BooleanValue enableUpgrade;
                public final ForgeConfigSpec.BooleanValue includeByDefault;
                public final ForgeConfigSpec.BooleanValue savesItems;

                public CraftingUpgradeConfig(ForgeConfigSpec.Builder builder, String path)
                {
                    builder.comment("Crafting Upgrade Settings").push(path);

                    //Crafting Upgrade

                    enableUpgrade = builder
                            .define("enableUpgrade", true);

                    includeByDefault = builder
                            .comment("Newly crafted backpacks will have crafting upgrade included by default")
                            .define("defaultUpgrade", false);

                    savesItems = builder
                            .comment("Whether crafting grid should save items")
                            .define("savesItems", true);

                    builder.pop();
                }
            }
        }

        public static class World
        {
            public final ForgeConfigSpec.BooleanValue spawnEntitiesWithBackpack;
            public final ForgeConfigSpec.ConfigValue<List<? extends String>> possibleOverworldEntityTypes;
            public final ForgeConfigSpec.ConfigValue<List<? extends String>> possibleNetherEntityTypes;
            public final ForgeConfigSpec.IntValue spawnChance;
            public final ForgeConfigSpec.ConfigValue<List<? extends String>> overworldBackpacks;
            public final ForgeConfigSpec.ConfigValue<List<? extends String>> netherBackpacks;

            World(final ForgeConfigSpec.Builder builder, final String path)
            {
                builder.push(path);

                spawnEntitiesWithBackpack = builder
                        .comment("Enables chance to spawn Zombie, Skeleton, Wither Skeleton, Piglin or Enderman with random backpack equipped")
                        .define("spawnEntitiesWithBackpack", true);

                possibleOverworldEntityTypes = builder
                        .comment("List of overworld entity types that can spawn with equipped backpack. DO NOT ADD anything to this list, because the game will crash, remove entries if mob should not spawn with backpack")
                        .defineList("possibleOverworldEntityTypes", this::getPossibleOverworldEntityTypes, mapping -> ((String)mapping).matches(REGISTRY_NAME_MATCHER));

                possibleNetherEntityTypes = builder
                        .comment("List of nether entity types that can spawn with equipped backpack. DO NOT ADD anything to this list, because the game will crash, remove entries if mob should not spawn with backpack")
                        .defineList("possibleNetherEntityTypes", this::getPossibleNetherEntityTypes, mapping -> ((String)mapping).matches(REGISTRY_NAME_MATCHER));


                spawnChance = builder
                        .comment("Defines spawn chance of entity with backpack (1 in [selected value])")
                        .defineInRange("spawnChance", 500, 0, Integer.MAX_VALUE);

                overworldBackpacks = builder
                        .comment("List of backpacks that can spawn on overworld mobs")
                        .defineList("overworldBackpacks", this::getOverworldBackpacksList, mapping -> ((String)mapping).matches(REGISTRY_NAME_MATCHER));

                netherBackpacks = builder
                        .comment("List of backpacks that can spawn on nether mobs")
                        .defineList("netherBackpacks", this::getNetherBackpacksList, mapping -> ((String)mapping).matches(REGISTRY_NAME_MATCHER));

                builder.pop();
            }

            private List<String> getPossibleOverworldEntityTypes()
            {
                List<String> ret = new ArrayList<>();
                ret.add("minecraft:zombie");
                ret.add("minecraft:skeleton");
                ret.add("minecraft:enderman");
                return ret;
            }

            private List<String> getPossibleNetherEntityTypes()
            {
                List<String> ret = new ArrayList<>();
                ret.add("minecraft:wither_skeleton");
                ret.add("minecraft:piglin");
                return ret;
            }


            private List<String> getOverworldBackpacksList()
            {
                List<String> ret = new ArrayList<>();
                ret.add("travelersbackpack:standard");
                ret.add("travelersbackpack:diamond");
                ret.add("travelersbackpack:gold");
                ret.add("travelersbackpack:emerald");
                ret.add("travelersbackpack:iron");
                ret.add("travelersbackpack:lapis");
                ret.add("travelersbackpack:redstone");
                ret.add("travelersbackpack:coal");
                ret.add("travelersbackpack:bookshelf");
                ret.add("travelersbackpack:sandstone");
                ret.add("travelersbackpack:snow");
                ret.add("travelersbackpack:sponge");
                ret.add("travelersbackpack:cake");
                ret.add("travelersbackpack:cactus");
                ret.add("travelersbackpack:hay");
                ret.add("travelersbackpack:melon");
                ret.add("travelersbackpack:pumpkin");
                ret.add("travelersbackpack:creeper");
                ret.add("travelersbackpack:enderman");
                ret.add("travelersbackpack:skeleton");
                ret.add("travelersbackpack:spider");
                ret.add("travelersbackpack:bee");
                ret.add("travelersbackpack:wolf");
                ret.add("travelersbackpack:fox");
                ret.add("travelersbackpack:ocelot");
                ret.add("travelersbackpack:horse");
                ret.add("travelersbackpack:cow");
                ret.add("travelersbackpack:pig");
                ret.add("travelersbackpack:sheep");
                ret.add("travelersbackpack:chicken");
                ret.add("travelersbackpack:squid");
                return ret;
            }

            private List<String> getNetherBackpacksList()
            {
                List<String> ret = new ArrayList<>();
                ret.add("travelersbackpack:quartz");
                ret.add("travelersbackpack:nether");
                ret.add("travelersbackpack:blaze");
                ret.add("travelersbackpack:ghast");
                ret.add("travelersbackpack:magma_cube");
                ret.add("travelersbackpack:wither");
                return ret;
            }
        }

        public static class BackpackAbilities
        {
            public final ForgeConfigSpec.BooleanValue enableBackpackAbilities;
            public final ForgeConfigSpec.BooleanValue forceAbilityEnabled;
            public final ForgeConfigSpec.ConfigValue<List<? extends String>> allowedAbilities;

            BackpackAbilities(final ForgeConfigSpec.Builder builder, final String path)
            {
                builder.push(path);

                enableBackpackAbilities = builder
                        .define("enableBackpackAbilities", true);

                forceAbilityEnabled = builder
                        .comment("Newly crafted backpacks will have ability enabled by default")
                        .define("forceAbilityEnabled", true);

                allowedAbilities = builder
                        .comment("List of backpacks that are allowed to have an ability. DO NOT ADD anything to this list, because the game will crash, remove entries if backpack should not have ability")
                        .defineList("allowedAbilities", this::getAllowedAbilities, mapping -> ((String)mapping).matches(REGISTRY_NAME_MATCHER));

                builder.pop();
            }

            private List<String> getAllowedAbilities()
            {
                List<String> ret = new ArrayList<>();
                ret.add("travelersbackpack:netherite");
                ret.add("travelersbackpack:diamond");
                ret.add("travelersbackpack:gold");
                ret.add("travelersbackpack:emerald");
                ret.add("travelersbackpack:iron");
                ret.add("travelersbackpack:lapis");
                ret.add("travelersbackpack:redstone");
                ret.add("travelersbackpack:bookshelf");
                ret.add("travelersbackpack:sponge");
                ret.add("travelersbackpack:cake");
                ret.add("travelersbackpack:cactus");
                ret.add("travelersbackpack:melon");
                ret.add("travelersbackpack:pumpkin");
                ret.add("travelersbackpack:creeper");
                ret.add("travelersbackpack:dragon");
                ret.add("travelersbackpack:enderman");
                ret.add("travelersbackpack:blaze");
                ret.add("travelersbackpack:ghast");
                ret.add("travelersbackpack:magma_cube");
                ret.add("travelersbackpack:spider");
                ret.add("travelersbackpack:wither");
                ret.add("travelersbackpack:bat");
                ret.add("travelersbackpack:bee");
                ret.add("travelersbackpack:ocelot");
                ret.add("travelersbackpack:cow");
                ret.add("travelersbackpack:chicken");
                ret.add("travelersbackpack:squid");
                return ret;
            }
        }

        public static class SlownessDebuff
        {
            public final ForgeConfigSpec.BooleanValue tooManyBackpacksSlowness;
            public final ForgeConfigSpec.IntValue maxNumberOfBackpacks;
            public final ForgeConfigSpec.DoubleValue slownessPerExcessedBackpack;

            SlownessDebuff(final ForgeConfigSpec.Builder builder, final String path)
            {
                builder.push(path);

                tooManyBackpacksSlowness = builder
                        .comment("Player gets slowness effect, if carries too many backpacks in inventory")
                        .define("tooManyBackpacksSlowness", false);

                maxNumberOfBackpacks = builder
                        .comment("Maximum number of backpacks, which can be carried in inventory, without slowness effect")
                        .defineInRange("maxNumberOfBackpacks", 3, 1, 37);

                slownessPerExcessedBackpack = builder
                        .defineInRange("slownessPerExcessedBackpack", 1, 0.1, 5);

                builder.pop();
            }
        }

        public void loadItemsFromConfig(List<? extends String> configList, List<Item> targetList)
        {
            for(String registryName : configList)
            {
                ResourceLocation res = ResourceLocation.tryParse(registryName);

                if(BuiltInRegistries.ITEM.containsKey(res))
                {
                    targetList.add(BuiltInRegistries.ITEM.get(res));
                }
            }
        }

        public void loadEntityTypesFromConfig(List<? extends String> configList, List<EntityType> targetList)
        {
            for(String registryName : configList)
            {
                ResourceLocation res = ResourceLocation.tryParse(registryName);

                if(BuiltInRegistries.ENTITY_TYPE.containsKey(res))
                {
                    targetList.add(BuiltInRegistries.ENTITY_TYPE.get(res));
                }
            }
        }

        private boolean initialized = false;

        public void initializeLists()
        {
            if(!serverSpec.isLoaded())
            {
                return;
            }

            if(!initialized)
            {
                //Container
                loadItemsFromConfig(TravelersBackpackConfig.SERVER.backpackSettings.toolSlotsAcceptableItems.get(), ToolSlotItemHandler.TOOL_SLOTS_ACCEPTABLE_ITEMS);
                loadItemsFromConfig(TravelersBackpackConfig.SERVER.backpackSettings.blacklistedItems.get(), BackpackSlotItemHandler.BLACKLISTED_ITEMS);

                //Spawns
                loadItemsFromConfig(TravelersBackpackConfig.SERVER.world.overworldBackpacks.get(), ModItems.COMPATIBLE_OVERWORLD_BACKPACK_ENTRIES);
                loadItemsFromConfig(TravelersBackpackConfig.SERVER.world.netherBackpacks.get(), ModItems.COMPATIBLE_NETHER_BACKPACK_ENTRIES);

                //Abilities
                loadItemsFromConfig(TravelersBackpackConfig.SERVER.backpackAbilities.allowedAbilities.get(), com.tiviacz.travelersbackpack.common.BackpackAbilities.ALLOWED_ABILITIES);

                //Entities
                loadEntityTypesFromConfig(TravelersBackpackConfig.SERVER.world.possibleOverworldEntityTypes.get(), Reference.ALLOWED_TYPE_ENTRIES);
                loadEntityTypesFromConfig(TravelersBackpackConfig.SERVER.world.possibleNetherEntityTypes.get(), Reference.ALLOWED_TYPE_ENTRIES);
            }

            initialized = true;
        }
    }

    public static class Common
    {
        public final ForgeConfigSpec.BooleanValue enableLoot;
        public final ForgeConfigSpec.BooleanValue enableVillagerTrade;

        Common(final ForgeConfigSpec.Builder builder)
        {
            builder.comment("Common config settings")
                    .push("common");

            enableLoot = builder
                    .comment("Enables backpacks spawning in loot chests")
                    .define("enableLoot", true);

            enableVillagerTrade = builder
                    .comment("Enables trade for Villager Backpack in Librarian villager trades")
                    .define("enableVillagerTrade", true);

            builder.pop();
        }
    }

    public static class Client
    {
        public final ForgeConfigSpec.BooleanValue sendBackpackCoordinatesMessage;
        public final ForgeConfigSpec.BooleanValue enableToolCycling;
        public final ForgeConfigSpec.BooleanValue disableScrollWheel;
        public final ForgeConfigSpec.BooleanValue enableLegacyGui;
        public final ForgeConfigSpec.BooleanValue obtainTips;
        public final ForgeConfigSpec.BooleanValue renderTools;
        public final ForgeConfigSpec.BooleanValue renderBackpackWithElytra;
        public final ForgeConfigSpec.BooleanValue disableBackpackRender;
        public final Overlay overlay;

        Client(final ForgeConfigSpec.Builder builder)
        {
            builder.comment("Client-only settings")
                    .push("client");

            sendBackpackCoordinatesMessage = builder
                    .comment("Sends a message to the player on death with backpack coordinates")
                    .define("sendBackpackCoordinatesMessage", true);

            enableToolCycling = builder
                    .comment("Enables tool cycling via keybind (Default Z) + scroll combination, while backpack is worn")
                    .define("enableToolCycling", true);

            disableScrollWheel = builder
                    .comment("Allows tool cycling using keybinding only (Default Z)")
                    .define("disableScrollWheel", false);

            enableLegacyGui = builder
                    .comment("Enables legacy GUI (Blue slots for storage, brown for crafting and green for tools)")
                    .define("enableLegacyGui", false);

            obtainTips = builder
                    .comment("Enables tip, how to obtain a backpack, if there's no crafting recipe for it")
                    .define("obtainTips", true);

            renderTools = builder
                    .comment("Render tools in tool slots on the backpack, while worn")
                    .define("renderTools", true);

            renderBackpackWithElytra = builder
                    .comment("Render backpack if elytra is present")
                    .define("renderBackpackWithElytra", true);

            disableBackpackRender = builder
                    .comment("Disable backpack rendering")
                    .define("disableBackpackRender", false);

            overlay = new Overlay(
                    builder,
                    "The position of the Overlay on the screen",
                    "overlay",
                    true, 20, 30
            );

            builder.pop();
        }

        public static class Overlay
        {
            public final ForgeConfigSpec.BooleanValue enableOverlay;
            public final ForgeConfigSpec.IntValue offsetX;
            public final ForgeConfigSpec.IntValue offsetY;

            Overlay(final ForgeConfigSpec.Builder builder, final String comment, final String path, final boolean defaultOverlay, final int defaultX, final int defaultY)
            {
                builder.comment(comment)
                        .push(path);

                enableOverlay = builder
                        .comment("Enables tanks and tool slots overlay, while backpack is worn")
                        .define("enableOverlay", defaultOverlay);

                offsetX = builder
                        .comment("Offsets to left side")
                        .defineInRange("offsetX", defaultX, Integer.MIN_VALUE, Integer.MAX_VALUE);

                offsetY = builder
                        .comment("Offsets to up")
                        .defineInRange("offsetY", defaultY, Integer.MIN_VALUE, Integer.MAX_VALUE);

                builder.pop();
            }
        }
    }

    //Server
    public static final ForgeConfigSpec serverSpec;
    public static final Server SERVER;

    static {
        final Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Server::new);
        serverSpec = specPair.getRight();
        SERVER = specPair.getLeft();
    }

    //Common
    public static final ForgeConfigSpec commonSpec;
    public static final Common COMMON;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    //Client
    public static final ForgeConfigSpec clientSpec;
    public static final Client CLIENT;

    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        clientSpec = specPair.getRight();
        CLIENT = specPair.getLeft();
    }
}