package cofh.thermalfoundation.fluid;

import cofh.api.core.IInitializer;
import cofh.core.fluid.BlockFluidCoFHBase;
import cofh.thermalfoundation.core.ProxyClient;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.ArrayList;

public class TFFluids {

	private TFFluids() {

	}

	public static void preInit() {

		registerAllFluids();
		registerAllFluidBlocks();
		createBuckets();

		for (int i = 0; i < initList.size(); i++) {
			initList.get(i).preInit();
		}

	}

	public static void initialize() {

		for (int i = 0; i < initList.size(); i++) {
			initList.get(i).initialize();
		}
	}

	public static void postInit() {

		for (int i = 0; i < initList.size(); i++) {
			initList.get(i).postInit();
		}
	}

	/* HELPERS */
	public static void registerAllFluids() {

		fluidSteam = new Fluid("steam", new ResourceLocation("thermalfoundation:steam"), new ResourceLocation("thermalfoundation:steam")).setLuminosity(0).setDensity(-1000).setViscosity(200).setTemperature(750).setGaseous(true);
		fluidCoal = new Fluid("coal", new ResourceLocation("thermalfoundation:coal"), new ResourceLocation("thermalfoundation:coal")).setLuminosity(0).setDensity(900).setViscosity(2000).setTemperature(300);
		fluidRedstone = new Fluid("redstone", new ResourceLocation("thermalfoundation:redstone"), new ResourceLocation("thermalfoundation:redstone")).setLuminosity(7).setDensity(1200).setViscosity(1500).setTemperature(300)
				.setRarity(EnumRarity.UNCOMMON);
		fluidGlowstone = new Fluid("glowstone", new ResourceLocation("thermalfoundation:glowstone"), new ResourceLocation("thermalfoundation:glowstone")).setLuminosity(15).setDensity(-500).setViscosity(100).setTemperature(300)
				.setGaseous(true).setRarity(EnumRarity.UNCOMMON);
		fluidEnder = new Fluid("ender", new ResourceLocation("thermalfoundation:ender"), new ResourceLocation("thermalfoundation:ender")).setLuminosity(3).setDensity(4000).setViscosity(3000).setTemperature(300)
				.setRarity(EnumRarity.UNCOMMON);
		fluidPyrotheum = new Fluid("pyrotheum", new ResourceLocation("thermalfoundation:pyrotheum"), new ResourceLocation("thermalfoundation:pyrotheum")).setLuminosity(15).setDensity(2000).setViscosity(1200).setTemperature(4000)
				.setRarity(EnumRarity.RARE);
		fluidCryotheum = new Fluid("cryotheum", new ResourceLocation("thermalfoundation:cryotheum"), new ResourceLocation("thermalfoundation:cryotheum")).setLuminosity(0).setDensity(4000).setViscosity(3000).setTemperature(50)
				.setRarity(EnumRarity.RARE);
		fluidAerotheum = new Fluid("aerotheum", new ResourceLocation("thermalfoundation:aerotheum"), new ResourceLocation("thermalfoundation:aerotheum")).setLuminosity(0).setDensity(-800).setViscosity(100).setTemperature(300)
				.setGaseous(true).setRarity(EnumRarity.RARE);
		fluidPetrotheum = new Fluid("petrotheum", new ResourceLocation("thermalfoundation:petrotheum"), new ResourceLocation("thermalfoundation:petrotheum")).setLuminosity(0).setDensity(4000).setViscosity(1500).setTemperature(400)
				.setRarity(EnumRarity.RARE);
		fluidMana =new Fluid("mana", new ResourceLocation("thermalfoundation:mana"), new ResourceLocation("thermalfoundation:mana")).setLuminosity(15).setDensity(600).setViscosity(6000).setTemperature(350)
				.setRarity(EnumRarity.EPIC);

		registerFluid(fluidSteam, "steam");
		registerFluid(fluidCoal, "coal");
		registerFluid(fluidRedstone, "redstone");
		registerFluid(fluidGlowstone, "glowstone");
		registerFluid(fluidEnder, "ender");
		registerFluid(fluidPyrotheum, "pyrotheum");
		registerFluid(fluidCryotheum, "cryotheum");
		registerFluid(fluidAerotheum, "aerotheum");
		registerFluid(fluidPetrotheum, "petrotheum");
		registerFluid(fluidMana, "mana");
	}

	public static void registerAllFluidBlocks() {

		registerFluidBlock(blockFluidSteam, new BlockFluidSteam(fluidSteam));
		registerFluidBlock(blockFluidCoal, new BlockFluidCoal(fluidCoal));
		registerFluidBlock(blockFluidRedstone, new BlockFluidRedstone(fluidRedstone));
		registerFluidBlock(blockFluidGlowstone, new BlockFluidGlowstone(fluidGlowstone));
		registerFluidBlock(blockFluidEnder, new BlockFluidEnder(fluidEnder));
		registerFluidBlock(blockFluidPyrotheum, new BlockFluidPyrotheum(fluidPyrotheum));
		registerFluidBlock(blockFluidCryotheum, new BlockFluidCryotheum(fluidCryotheum));
		registerFluidBlock(blockFluidAerotheum, new BlockFluidAerotheum(fluidAerotheum));
		registerFluidBlock(blockFluidPetrotheum, new BlockFluidPetrotheum(fluidPetrotheum));
		registerFluidBlock(blockFluidMana, new BlockFluidMana(fluidMana));
	}

	public static void registerFluid(Fluid fluid, String fluidName) {

		if (!FluidRegistry.isFluidRegistered(fluidName)) {
			FluidRegistry.registerFluid(fluid);
		}
		fluid = FluidRegistry.getFluid(fluidName);
	}

	public static void registerFluidBlock(BlockFluidCoFHBase block, BlockFluidCoFHBase block2) {

		block = block2;

		initList.add(block);

		ProxyClient.modelList.add(block);
	}

	public static void createBuckets() {

		FluidRegistry.addBucketForFluid(fluidSteam);
		FluidRegistry.addBucketForFluid(fluidCoal);
		FluidRegistry.addBucketForFluid(fluidRedstone);
		FluidRegistry.addBucketForFluid(fluidGlowstone);
		FluidRegistry.addBucketForFluid(fluidEnder);
		FluidRegistry.addBucketForFluid(fluidPyrotheum);
		FluidRegistry.addBucketForFluid(fluidCryotheum);
		FluidRegistry.addBucketForFluid(fluidAerotheum);
		FluidRegistry.addBucketForFluid(fluidPetrotheum);
		FluidRegistry.addBucketForFluid(fluidMana);
	}

	static ArrayList<IInitializer> initList = new ArrayList<IInitializer>();

	/* REFERENCES */
	public static Fluid fluidSteam;
	public static Fluid fluidCoal;
	public static Fluid fluidRedstone;
	public static Fluid fluidGlowstone;
	public static Fluid fluidEnder;
	public static Fluid fluidPyrotheum;
	public static Fluid fluidCryotheum;
	public static Fluid fluidAerotheum;
	public static Fluid fluidPetrotheum;
	public static Fluid fluidMana;

	public static BlockFluidCoFHBase blockFluidSteam;
	public static BlockFluidCoFHBase blockFluidCoal;
	public static BlockFluidCoFHBase blockFluidRedstone;
	public static BlockFluidCoFHBase blockFluidGlowstone;
	public static BlockFluidCoFHBase blockFluidEnder;
	public static BlockFluidCoFHBase blockFluidPyrotheum;
	public static BlockFluidCoFHBase blockFluidCryotheum;
	public static BlockFluidCoFHBase blockFluidAerotheum;
	public static BlockFluidCoFHBase blockFluidPetrotheum;
	public static BlockFluidCoFHBase blockFluidMana;

}
