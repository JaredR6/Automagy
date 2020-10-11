package tuhljin.automagy.common.entities;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import thaumcraft.api.golems.GolemHelper;
import tuhljin.automagy.common.Automagy;
import tuhljin.automagy.common.entities.golems.SealCraft;
import tuhljin.automagy.common.entities.golems.SealCraftProvide;
import tuhljin.automagy.common.entities.golems.SealShear;
import tuhljin.automagy.common.entities.golems.SealShearAdvanced;
import tuhljin.automagy.common.entities.golems.SealSupply;
import tuhljin.automagy.common.lib.References;

public class ModEntities {

    public static void init() {
        byte id = 0;
        EntityRegistry.registerModEntity(References.ENTITY_AVARICEPEARL_REGISTRY, EntityAvaricePearl.class, References.ENTITY_AVARICEPEARL, id, Automagy.instance, 80, 3, true);
    }

    public static void registerSeals() {
        GolemHelper.registerSeal(new SealCraft());
        GolemHelper.registerSeal(new SealCraftProvide());
        GolemHelper.registerSeal(new SealShear());
        GolemHelper.registerSeal(new SealShearAdvanced());
        GolemHelper.registerSeal(new SealSupply());
    }
}
