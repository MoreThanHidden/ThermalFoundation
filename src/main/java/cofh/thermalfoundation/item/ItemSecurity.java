package cofh.thermalfoundation.item;

import cofh.api.core.IInitializer;
import cofh.lib.util.helpers.ItemHelper;
import cofh.thermalfoundation.ThermalFoundation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;

public class ItemSecurity extends Item implements IInitializer {

	public ItemSecurity() {


		setUnlocalizedName("thermalfoundation:security");
		setCreativeTab(ThermalFoundation.tabCommon);

		setHasSubtypes(true);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean advanced) {

	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return EnumActionResult.FAIL;
	}

	@Override
	public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {

		boolean ret = false;

		switch (Type.values()[ItemHelper.getItemDamage(stack)]) {
		case LOCK:
			//ret = doLockUseFirst(stack, player, world, pos, side);
			break;
		default:
			break;
		}
		return ret ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
	}

	private boolean doLockUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side) {

		return false;
	}

	/* IModelRegister */
//	@Override
//	@SideOnly(Side.CLIENT)
//	public void registerModels() {
//
//		StateMapper mapper = new StateMapper(modName, "tool", name);
//		ModelBakery.registerItemVariants(this);
//		ModelLoader.setCustomMeshDefinition(this, mapper);
//
//		for (Map.Entry<Integer, ItemEntry> entry : itemMap.entrySet()) {
//			ModelLoader.setCustomModelResourceLocation(this, entry.getKey(), new ModelResourceLocation(modName + ":" + "tool", entry.getValue().name));
//		}
//	}

	/* IInitializer */
	@Override
	public boolean preInit() {

		lock = addItem(Type.LOCK.ordinal(), "lock");

		return true;
	}
	public ItemStack addItem(int number, String key) {
		ItemStack item = new ItemStack(new ItemSecurity(), 1, number);
		GameRegistry.register(item.getItem(), new ResourceLocation("thermalfoundation:" + key));
		return item;
	}

	@Override
	public boolean initialize() {

		return true;
	}

	@Override
	public boolean postInit() {

//		addRecipe(ShapedRecipe(lock, new Object[] { " S ", "SBS", "SSS", 'B', "ingotBronze", 'S', "nuggetSignalum" }));
//TODO fix
		return true;
	}

	/* REFERENCES */
	public static ItemStack lock;

	/* TYPE */
	enum Type {
		LOCK;
	}

}
