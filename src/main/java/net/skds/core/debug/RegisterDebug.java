package net.skds.core.debug;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.skds.core.SKDSCore;

public class RegisterDebug {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SKDSCore.MOD_ID);    
	public static final RegistryObject<Item> DEBUG_STICK = ITEMS.register("debug_item", () -> DebugItem.getForReg());

	static final String ID = "debug_entity";
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES,
			SKDSCore.MOD_ID);
	public static final RegistryObject<EntityType<?>> DEBUG_ENTITY = ENTITIES.register(ID,
			() -> DebugEntity.getForReg(ID));


	public static void register() {
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
