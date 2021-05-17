package de.typable.minecrafthub.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import de.typable.minecrafthub.constant.DefaultConstants;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;


public class Util
{
	public static boolean isEmpty(ItemStack item)
	{
		return item == null || item.getType() == Material.AIR;
	}

	public static boolean isType(ItemStack item, Material type)
	{
		return !isEmpty(item) && item.getType() == type;
	}

	public static boolean compare(ItemStack item, ItemStack item2)
	{
		if(item == null || item2 == null)
		{
			return false;
		}

		if(item.getType() != item2.getType())
		{
			return false;
		}

		return true;
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

	public static boolean containsAtLeast(Inventory inventory, ItemStack item, int amount)
	{
		for(int i = 0; i < inventory.getSize(); i++)
		{
			ItemStack current = inventory.getItem(i);

			if(Util.compare(item, current))
			{
				amount -= current.getAmount();

				if(amount <= 0)
				{
					return true;
				}
			}
		}

		return false;
	}

	public static void sendActionMessage(Player player, String message)
	{
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
	}

	public static void sendCountdown(Plugin plugin, final String message, final int time, Runnable runnable)
	{
		Bukkit.getScheduler().runTaskTimer(plugin, new Runnable()
		{
			int count = time;
			
			@Override
			public void run()
			{
				if(count == 0)
				{
					runnable.run();
					return;
				}
				
				Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + message + " " + count + " sec");
				
				count--;
			}
		}, 0L, DefaultConstants.TICK);
	}

	public static Float convertFacingToYaw(BlockFace face)
	{
		switch(face)
		{
			case NORTH:
				return 0F;
			case EAST:
				return 90F;
			case SOUTH:
				return 180F;
			case WEST:
				return -90F;
			default:
				break;
		}

		return null;
	}

	public static boolean isAir(Material material)
	{
		return material == Material.AIR || material == Material.CAVE_AIR || material == Material.VOID_AIR;
	}
}
