package de.typable.minecrafthub.util;

import javax.swing.text.Position;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.Location;

import de.typable.minecrafthub.constant.Constants;
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
		}, 0L, Constants.TICK);
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

	public static boolean isFarmable(Material material)
	{
		return material == Material.WHEAT || material == Material.CARROTS || material == Material.POTATOES || material == Material.BEETROOTS || material == Material.NETHER_WART || material == Material.COCOA;
	}

	public static boolean isStem(Material material)
	{
		return material == Material.ATTACHED_PUMPKIN_STEM || material == Material.ATTACHED_MELON_STEM;
	}

	public static boolean hasStem(Block block)
	{
		Location blockLocation = block.getLocation();
		Material blockNorth = blockLocation.clone().add(0, 0, -1).getBlock().getType();
		Material blockEast = blockLocation.clone().add(1, 0, 0).getBlock().getType();
		Material blockSouth = blockLocation.clone().add(0, 0, 1).getBlock().getType();
		Material blockWest = blockLocation.clone().add(-1, 0, 0).getBlock().getType();

		return isStem(blockNorth) || isStem(blockEast) || isStem(blockSouth) || isStem(blockWest);
	}
	
	public static Location calcBlockCenter(final Location location)
	{
		Location centered = location.clone();
		centered.setX(Location.locToBlock(location.getX()) + 0.5);
		centered.setY(Location.locToBlock(location.getY()));
		centered.setZ(Location.locToBlock(location.getZ()) + 0.5);

		return centered;
	}

	public static int calcTravelFee(final Location from, final Location to)
	{
		final Double distance = from.distance(to);
		int fee = (int) Math.floor(distance / (Constants.BLOCKS_PER_CHUNK * Constants.CHUNKS_PER_EMERALD));

		if(fee < Constants.MIN_FEE)
		{
			fee = Constants.MIN_FEE;
		}

		if(fee > Constants.MAX_FEE)
		{
			fee = Constants.MAX_FEE;
		}

		return fee;
	}
}
