package cofh.thermalfoundation.item;

import cofh.api.block.IBlockConfigGui;
import cofh.api.block.IBlockInfo;
import cofh.api.core.IInitializer;
import cofh.api.core.IModelRegister;
import cofh.api.tileentity.ITileInfo;
import cofh.core.StateMapper;
import cofh.core.chat.ChatHelper;
import cofh.lib.util.helpers.ItemHelper;
import cofh.lib.util.helpers.ServerHelper;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermalfoundation.ThermalFoundation;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ItemMeter extends Item implements IInitializer, IModelRegister {
	public Map<Pair<Integer, String>, Item> items = new TreeMap<Pair<Integer, String>, Item>();

	public ItemMeter() {

//		super("thermalfoundation");

		setUnlocalizedName("thermalfoundation.tool.multimeter");
		setCreativeTab(ThermalFoundation.tabCommon);

		setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		for (Map.Entry<Pair<Integer, String>, Item> entry : items.entrySet()) {
			if(entry.getKey().getLeft() == stack.getItemDamage()){return "item.thermalfoundation.tool." + entry.getKey().getRight();}
		}
		return "item.thermalfoundation.tool";
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		for (Map.Entry<Pair<Integer, String>, Item> entry : items.entrySet()) {
			subItems.add(new ItemStack(entry.getValue(), 1,entry.getKey().getLeft()));
		}
	}


	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean advanced) {

		if (StringHelper.displayShiftForDetail && !StringHelper.isShiftKeyDown()) {
			tooltip.add(StringHelper.shiftForDetails());
		}
		if (!StringHelper.isShiftKeyDown()) {
			return;
		}
		switch (Type.values()[ItemHelper.getItemDamage(stack)]) {
		case MULTIMETER:
			tooltip.add(StringHelper.getInfoText("info.thermalfoundation.tool.multimeter.0"));
			tooltip.add(StringHelper.getNoticeText("info.thermalfoundation.tool.multimeter.1"));
			break;
		default:
		}
	}

	@Override
	public boolean isFull3D() {

		return true;
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return EnumActionResult.FAIL;
	}


	@Override
	public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		boolean ret = false;

		switch (Type.values()[ItemHelper.getItemDamage(stack)]) {
		case MULTIMETER:
			ret = doMultimeterUseFirst(stack, player, world, pos, side, hand);
			break;
		default:
			break;
		}
		return ret? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
	}

	private boolean doMultimeterUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, EnumHand hand) {

		player.swingArm(hand);

		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();

		ArrayList<ITextComponent> info = new ArrayList<ITextComponent>();

		if (ServerHelper.isClientWorld(world)) {
			if (block instanceof IBlockConfigGui || block instanceof IBlockInfo) {
				return true;
			}
			TileEntity tile = world.getTileEntity(pos);
			return tile instanceof ITileInfo;
		}
		if (player.isSneaking() && block instanceof IBlockConfigGui) {
			if (((IBlockConfigGui) block).openConfigGui(world, pos, side, player)) {
				return true;
			}
		}
		if (block instanceof IBlockInfo) {
			((IBlockInfo) (block)).getBlockInfo(info, world, pos, side, player, false);

			ChatHelper.sendIndexedChatMessagesToPlayer(player, info);
			info.clear();
			return true;
		} else {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof ITileInfo) {
				if (ServerHelper.isServerWorld(world)) {
					((ITileInfo) tile).getTileInfo(info, world, pos, side, player, false);
					ChatHelper.sendIndexedChatMessagesToPlayer(player, info);
				}
				info.clear();
				return true;
			}
		}
		info.clear();
		return false;

	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {

		switch (Type.values()[ItemHelper.getItemDamage(stack)]) {
		case MULTIMETER:
			return EnumRarity.COMMON;
		default:
			return EnumRarity.COMMON;
		}
	}

	/* IModelRegister */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {

		for (final Map.Entry<Pair<Integer, String>, Item> entry : items.entrySet()) {
			ModelBakery.registerItemVariants(entry.getValue());
			ModelLoader.setCustomMeshDefinition(entry.getValue(), new StateMapper("thermalfoundation", "tool", entry.getKey().getRight()));
			ModelLoader.setCustomModelResourceLocation(entry.getValue(), entry.getKey().getLeft(), new ModelResourceLocation("thermalfoundation:tool", entry.getKey().getRight()));
		}
	}

	/* IInitializer */
	@Override
	public boolean preInit() {

		multimeter = addItem(Type.MULTIMETER.ordinal(), "multimeter");
		return true;
	}
	public ItemStack addItem(int number, String key) {
		if (items.containsKey(Pair.of(number, key))){
			return null;
		}
		items.put(Pair.of(number, key), this);

		ItemStack item = new ItemStack(this, 1, number);
		if(Item.REGISTRY.getNameForObject(this) == null) {
			GameRegistry.register(this, new ResourceLocation("thermalfoundation:meter"));
		}

		return item;
	}

	@Override
	public boolean initialize() {

		return true;
	}

	@Override
	public boolean postInit() {
		GameRegistry.addRecipe(new ShapedOreRecipe(multimeter, "C C", "LPL", " G ", 'C', "ingotCopper", 'L', "ingotLead", 'P', ItemMaterial.powerCoilElectrum, 'G', "gearElectrum"));
		return true;
	}

	/* REFERENCES */
	public static ItemStack multimeter;

	/* TYPE */
	enum Type {
		MULTIMETER, DEBUGGER
	}

}
