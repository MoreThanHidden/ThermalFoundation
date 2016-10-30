package cofh.thermalfoundation.item;

import cofh.api.block.IDismantleable;
import cofh.api.core.IInitializer;
import cofh.api.core.IModelRegister;
import cofh.api.item.IToolHammer;
import cofh.asm.relauncher.Implementable;
import cofh.core.StateMapper;
import cofh.lib.util.helpers.ServerHelper;
import cofh.thermalfoundation.ThermalFoundation;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Implementable("buildcraft.api.tools.IToolWrench")
public class ItemWrench extends Item implements IInitializer, IToolHammer, IModelRegister {

    public Map<Pair<Integer, String>, Item> items = new TreeMap<Pair<Integer, String>, Item>();
    public ItemWrench() {

        setUnlocalizedName("thermalfoundation.tool.wrenchBasic");
        setCreativeTab(ThermalFoundation.tabCommon);

        setHasSubtypes(true);
        setHarvestLevel("wrench", 1);
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
    public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return true;
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return player.canPlayerEdit(pos.offset(side), side, stack) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
    }


    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {

        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (block == null) {
            return EnumActionResult.FAIL;
        }
        PlayerInteractEvent event = new PlayerInteractEvent.RightClickBlock(player, hand, stack, pos, side, new Vec3d(hitX, hitY, hitZ));
        if (MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Result.DENY) {
            return EnumActionResult.FAIL;
        }
        if (ServerHelper.isServerWorld(world) && player.isSneaking() && block instanceof IDismantleable
                && ((IDismantleable) block).canDismantle(world, pos, state, player)) {
            ((IDismantleable) block).dismantleBlock(world, pos, state, player, false);
            return EnumActionResult.SUCCESS;
        }

        if (!player.isSneaking() && block.rotateBlock(world, pos, side)) {
            player.swingArm(hand);
            return ServerHelper.isServerWorld(world) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
        }
        return EnumActionResult.FAIL;
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap multimap = HashMultimap.create();
        //TODO figure out
//		multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Tool modifier", 1, 0));
        return multimap;
    }

	/* IModelRegister */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {

		ModelBakery.registerItemVariants(this);
		ModelLoader.setCustomMeshDefinition(this, new StateMapper("thermalfoundation", "tool", "wrenchbasic"));

        for (Map.Entry<Pair<Integer, String>, Item> entry : items.entrySet()) {
            ModelLoader.setCustomModelResourceLocation(this, entry.getKey().getLeft(), new ModelResourceLocation("thermalfoundation:tool", entry.getKey().getRight().toLowerCase()));
        }
	}

    /* IToolHammer */
    @Override
    public boolean isUsable(ItemStack item, EntityLivingBase user, BlockPos pos) {

        return true;
    }

    @Override
    public boolean isUsable(ItemStack item, EntityLivingBase user, Entity entity) {

        return true;
    }

    @Override
    public void toolUsed(ItemStack item, EntityLivingBase user, BlockPos pos) {

    }

    @Override
    public void toolUsed(ItemStack item, EntityLivingBase user, Entity entity) {

    }

    /* IToolWrench */
    public boolean canWrench(EntityPlayer player, BlockPos pos) {

        return true;
    }

    public boolean canWrench(EntityPlayer player, Entity entity) {

        return true;
    }

    public void wrenchUsed(EntityPlayer player, BlockPos pos) {

    }

    public void wrenchUsed(EntityPlayer player, Entity entity) {

    }

    /* IInitializer */
    @Override
    public boolean preInit() {

        wrenchBasic = addItem(0, "wrenchBasic");

        return true;
    }

    public ItemStack addItem(int number, String key) {
        if (items.containsKey(Pair.of(number, key))){
            return null;
        }
        items.put(Pair.of(number, key), this);

        ItemStack item = new ItemStack(this, 1, number);
        if(Item.REGISTRY.getNameForObject(this) == null) {
            GameRegistry.register(this, new ResourceLocation("thermalfoundation:wrench"));
        }

        return item;
    }

    @Override
    public boolean initialize() {

        return true;
    }

    @Override
    public boolean postInit() {
        GameRegistry.addRecipe(new ShapedOreRecipe(wrenchBasic, "I I", " T ", " I ", 'I', "ingotIron", 'T', "ingotTin"));

        return true;
    }

    /* REFERENCES */
    public static ItemStack wrenchBasic;

}
