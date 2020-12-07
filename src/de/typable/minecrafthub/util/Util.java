package de.typable.minecrafthub.util;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Util
{
	public static boolean isEmpty(ItemStack item)
	{
		return item == null || item.getType() == Material.AIR;
	}
	
	public static boolean isInventoryFull(Inventory inventory, ItemStack result)
	{
		for(ItemStack item : inventory.getContents())
		{
			if(Util.isEmpty(item))
			{
				return false;
			}
			
			if(result.isSimilar(item))
			{
				if(result.getAmount() + item.getAmount() <= item.getMaxStackSize())
				{
					return false;
				}
			}
		}
		
		return true;
	}
}
