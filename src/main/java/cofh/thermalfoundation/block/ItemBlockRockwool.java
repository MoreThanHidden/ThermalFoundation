package cofh.thermalfoundation.block;

import cofh.lib.util.helpers.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockRockwool extends ItemBlock {

	public ItemBlockRockwool(Block block) {

		super(block);
		setHasSubtypes(true);
		setMaxDamage(0);
		setRegistryName(block.getRegistryName().toString() + "Item");
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {

		return "tile.thermalfoundation.rockwool." + BlockRockwool.Type.byMetadata(ItemHelper.getItemDamage(stack)).getName();
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}
}
