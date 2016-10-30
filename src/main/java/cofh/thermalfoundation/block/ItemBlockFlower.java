package cofh.thermalfoundation.block;

import cofh.lib.util.helpers.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockFlower extends ItemBlock {

	public ItemBlockFlower(Block block) {

		super(block);
		setHasSubtypes(true);
		setMaxDamage(0);
		setRegistryName(block.getRegistryName().toString() + "Item");
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {

		return "tile.thermalfoundation.flower." + BlockFlower.Type.byMetadata(ItemHelper.getItemDamage(stack)).getName();
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {

		return BlockFlower.Type.byMetadata(ItemHelper.getItemDamage(stack)).getRarity();
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

}
