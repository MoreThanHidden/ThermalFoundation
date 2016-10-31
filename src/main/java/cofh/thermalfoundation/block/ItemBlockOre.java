package cofh.thermalfoundation.block;

import cofh.lib.util.helpers.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockOre extends ItemBlock {

	public ItemBlockOre(Block block) {
		super(block);
		setHasSubtypes(true);
		setMaxDamage(0);
		setRegistryName(block.getRegistryName().toString());
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {

		return "tile.thermalfoundation.ore." + BlockOre.Type.byMetadata(ItemHelper.getItemDamage(stack)).getName();
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {

		return BlockOre.Type.byMetadata(ItemHelper.getItemDamage(stack)).getRarity();
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}
}
