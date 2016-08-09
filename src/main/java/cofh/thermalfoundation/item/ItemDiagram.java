package cofh.thermalfoundation.item;

import cofh.api.core.IInitializer;
import cofh.api.tileentity.IPortableData;
import cofh.lib.util.helpers.ItemHelper;
import cofh.lib.util.helpers.ServerHelper;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermalfoundation.ThermalFoundation;
import cofh.thermalfoundation.util.PatternHelper;
import cofh.thermalfoundation.util.RedprintHelper;
import cofh.thermalfoundation.util.SchematicHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;

public class ItemDiagram extends Item implements IInitializer {

	public ItemDiagram() {

//		super("thermalfoundation");

		setUnlocalizedName("diagram");
		setCreativeTab(ThermalFoundation.tabCommon);

		setHasSubtypes(true);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean advanced) {

		switch (Type.values()[ItemHelper.getItemDamage(stack)]) {
		case SCHEMATIC:
			SchematicHelper.addInformation(stack, tooltip);
			break;
		case PATTERN:
			PatternHelper.addInformation(stack, tooltip);
			break;
		case REDPRINT:
			RedprintHelper.addInformation(stack, tooltip);
			break;
		default:
		}
	}

	@Override
	public boolean isFull3D() {

		return true;
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		return player.canPlayerEdit(pos.offset(facing), facing, stack)? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
	}

	@Override
	public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		if (player.isSneaking()) {
			if (stack.hasTagCompound()) {
				//TODO figure out soundEvent
//				world.playSound(player, "random.orb", 0.5F, 0.3F);
			}
			stack.setTagCompound(null);
			return EnumActionResult.SUCCESS;
		}
		switch (Type.values()[ItemHelper.getItemDamage(stack)]) {
		case REDPRINT:
			doRedprintUseFirst(stack, player, world, pos, side);
			break;
		default:
			return EnumActionResult.SUCCESS;
		}
		ServerHelper.sendItemUsePacket(stack, player, world, pos, side, hitX, hitY, hitZ);
		return EnumActionResult.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {

		if (player.isSneaking()) {
			if (stack.hasTagCompound()) {
				//TODO sound
//				world.playSoundAtEntity(player, "random.orb", 0.5F, 0.3F);
			}
			stack.setTagCompound(null);
		}
		player.swingArm(hand);
		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

	private void doRedprintUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side) {

		if (ServerHelper.isClientWorld(world)) {
			return;
		}
		TileEntity tile = world.getTileEntity(pos);

		if (tile instanceof IPortableData) {
			if (!stack.hasTagCompound()) {
				stack.setTagCompound(new NBTTagCompound());
				((IPortableData) tile).writePortableData(player, stack.getTagCompound());
				if (stack.getTagCompound().hasNoTags()) {
					stack.setTagCompound(null);
				} else {
					stack.getTagCompound().setString("Type", ((IPortableData) tile).getDataType());
					//TODO sound
//					world.playSoundAtEntity(player, "random.orb", 0.5F, 0.7F);
				}
			} else {
				if (stack.getTagCompound().getString("Type").equals(((IPortableData) tile).getDataType())) {
					((IPortableData) tile).readPortableData(player, stack.getTagCompound());
				}
			}
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {

		String baseName = StringHelper.localize(getUnlocalizedName(stack) + ".name");

		switch (Type.values()[ItemHelper.getItemDamage(stack)]) {
		case SCHEMATIC:
			baseName += SchematicHelper.getDisplayName(stack);
			break;
		case PATTERN:
			//baseName += PatternHelper.getDisplayName(stack);
			break;
		case REDPRINT:
			baseName += RedprintHelper.getDisplayName(stack);
			break;
		default:
		}
		return baseName;
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {

		return RedprintHelper.getDisplayName(stack).isEmpty() ? EnumRarity.COMMON : EnumRarity.UNCOMMON;
	}
//TODO figure this out
//	/* IModelRegister */
//	@Override
//	@SideOnly(Side.CLIENT)
//	public void registerModels() {
//
////		StateMapper mapper = new StateMapper(modName, "tool", name);
//		ModelBakery.registerItemVariants(this);
////		ModelLoader.setCustomMeshDefinition(this, mapper);
//
////		for (Map.Entry<Integer, ItemEntry> entry : itemMap.entrySet()) {
////			ModelLoader.setCustomModelResourceLocation(this, entry.getKey(), new ModelResourceLocation(modName + ":" + "tool", entry.getValue().name));
////		}
//	}

	/* IInitializer */
	@Override
	public boolean preInit() {
//TODO figure this out
		schematic = addItem(Type.SCHEMATIC.ordinal(), "schematic");
		redprint = addItem(Type.REDPRINT.ordinal(), "redprint");

		return true;
	}

	@Override
	public boolean initialize() {

		return true;
	}

	@Override
	public boolean postInit() {

//		addRecipe(ShapelessRecipe(schematic, new Object[] { Items.PAPER, Items.PAPER, "dyeBlue" }));
//		addRecipe(ShapelessRecipe(redprint, new Object[] { Items.PAPER, Items.PAPER, "dustRedstone" }));

		return true;
	}
	public ItemStack addItem(int number, String key) {

		ItemStack item = new ItemStack(new ItemDiagram(), 1, number);
		GameRegistry.register(item.getItem(), new ResourceLocation("thermalfoundation:" + key));
		return item;
	}
	/* REFERENCES */
	public static ItemStack schematic;
	public static ItemStack pattern;
	public static ItemStack redprint;
	public static ItemStack enderprint;

	/* TYPE */
	public enum Type {
		SCHEMATIC, PATTERN, REDPRINT, ENDERPRINT;
	}

}
