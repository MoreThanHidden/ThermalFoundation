package cofh.thermalfoundation.item;

import cofh.api.core.IInitializer;
import cofh.api.core.IModelRegister;
import cofh.lib.util.helpers.ItemHelper;
import cofh.thermalfoundation.ThermalFoundation;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ItemSecurity extends Item implements IInitializer, IModelRegister {
    public Map<Pair<Integer, String>, Item> items = new TreeMap<Pair<Integer, String>, Item>();
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

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        for (Map.Entry<Pair<Integer, String>, Item> entry : items.entrySet()) {
            subItems.add(new ItemStack(entry.getValue(), 1,entry.getKey().getLeft()));
        }
    }


    /* IModelRegister */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {

        for (Map.Entry<Pair<Integer, String>, Item> entry : items.entrySet()) {
            ModelBakery.registerItemVariants(this);
            ModelLoader.setCustomModelResourceLocation(this, entry.getKey().getLeft(), new ModelResourceLocation("thermalfoundation:security", entry.getKey().getRight()));
        }
    }

	/* IInitializer */
	@Override
	public boolean preInit() {

		lock = addItem(Type.LOCK.ordinal(), "lock");

		return true;
	}
	public ItemStack addItem(int number, String key) {
        if (items.containsKey(Pair.of(number, key))){
            return null;
        }
        items.put(Pair.of(number, key), this);

        ItemStack item = new ItemStack(this, 1, number);
        if(Item.REGISTRY.getNameForObject(this) == null) {
            GameRegistry.register(item.getItem(), new ResourceLocation("thermalfoundation:security"));
        }
		return item;
	}

	@Override
	public boolean initialize() {

		return true;
	}

	@Override
	public boolean postInit() {

        GameRegistry.addRecipe(new ShapedOreRecipe(lock, " S ", "SBS", "SSS", 'B', "ingotBronze", 'S', "nuggetSignalum"));
		return true;
	}

	/* REFERENCES */
	public static ItemStack lock;

	/* TYPE */
	enum Type {
		LOCK
	}

}
