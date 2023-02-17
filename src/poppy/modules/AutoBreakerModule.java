package poppy.modules;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.data.Directional;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import poppy.Utils;

public class AutoBreakerModule implements Listener
{
	private Plugin plugin;

	public AutoBreakerModule(Plugin plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler
	public void onBlockDispenseEvent(BlockDispenseEvent event)
	{
		final Block block = event.getBlock();
		final BlockState blockState = block.getState();
		final ItemStack item = event.getItem();

		if(blockState instanceof Dispenser)
		{
			final Dispenser dispenser = (Dispenser) blockState;

			if(dispenser.getCustomName().toLowerCase().equals("breaker"))
			{
				event.setCancelled(true);

				if(block.getBlockData() instanceof Directional)
				{
					final Directional direction = (Directional) dispenser.getBlockData();
					final Block faceBlock = dispenser.getBlock().getRelative(direction.getFacing());

					final HashSet<ItemStack> drops = new HashSet<ItemStack>(faceBlock.getDrops(item));

					if(item.getType().toString().endsWith("_PICKAXE"))
					{
						if(Tag.MINEABLE_PICKAXE.isTagged(faceBlock.getType()))
						{
							if(!addItemToChest(direction, dispenser, drops))
							{
								setDamage(item, dispenser);
								faceBlock.breakNaturally(item);
							}
							else
							{
								setDamage(item, dispenser);
								faceBlock.setType(Material.AIR);
							}
						}
					}

					if(item.getType().toString().endsWith("_AXE"))
					{
						if(Tag.MINEABLE_AXE.isTagged(faceBlock.getType()))
						{
							if(!addItemToChest(direction, dispenser, drops))
							{
								setDamage(item, dispenser);
								faceBlock.breakNaturally(item);
							}
							else
							{
								setDamage(item, dispenser);
								faceBlock.setType(Material.AIR);
							}
						}
					}

					if(item.getType().toString().endsWith("_SHOVEL"))
					{
						if(Tag.MINEABLE_SHOVEL.isTagged(faceBlock.getType()))
						{
							if(!addItemToChest(direction, dispenser, drops))
							{
								setDamage(item, dispenser);
								faceBlock.breakNaturally(item);
							}
							else
							{
								setDamage(item, dispenser);
								faceBlock.setType(Material.AIR);
							}
						}
					}

					if(item.getType().toString().endsWith("_HOE"))
					{
						if(Tag.MINEABLE_HOE.isTagged(faceBlock.getType()))
						{
							if(!addItemToChest(direction, dispenser, drops))
							{
								setDamage(item, dispenser);
								faceBlock.breakNaturally(item);
							}
							else
							{
								setDamage(item, dispenser);
								faceBlock.setType(Material.AIR);
							}
						}
					}

					if(item.getType().toString().endsWith("_PICKAXE") && !Utils.isAir(faceBlock.getType())
					&& !Tag.MINEABLE_PICKAXE.isTagged(faceBlock.getType()) 
					&& !Tag.MINEABLE_HOE.isTagged(faceBlock.getType())
					&& !Tag.MINEABLE_AXE.isTagged(faceBlock.getType())
					&& !Tag.MINEABLE_SHOVEL.isTagged(faceBlock.getType()))
					 {
						setDamage(item, dispenser);
						faceBlock.breakNaturally(item);
					 }
				}
			}
		}
	}

	public static boolean isInventoryFullList(Inventory inventory, HashSet<ItemStack> items)
	{
		final HashSet<ItemStack> itemsCopy = new HashSet<ItemStack>(items);

		for(ItemStack item : itemsCopy)
		{
			if(!Utils.isInventoryFull(inventory, item))
			{
				itemsCopy.remove(item);
			}
		}
		return itemsCopy.size() != 0;
	}

	public static boolean addItemToChest(Directional direction, Dispenser dispenser, HashSet<ItemStack> items)
	{
		Chest chest = null;
		boolean succeeded = false;

		for(BlockFace face : faces)
		{
			if(!face.equals(direction.getFacing()) && chest == null)
			{
				final Block blockAtFacing = dispenser.getBlock().getRelative(face);
				final BlockState blockStateAtFacing = blockAtFacing.getState();

				if(blockAtFacing.getType().equals(Material.CHEST) && blockStateAtFacing instanceof Chest)
				{
					final Chest chestBlock = (Chest) blockStateAtFacing;

					if(!isInventoryFullList(chestBlock.getInventory(), items))
					{
						for(ItemStack item : items)
						{
							chestBlock.getInventory().addItem(item);
						}
						succeeded = true;
						break;
					}
					chest = chestBlock;
				}
			}
		}
		return succeeded;
	}

	public void setDamage(ItemStack item, Dispenser dispenser)
	{
		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			if(Utils.randomlyReduceDurability(item.getEnchantmentLevel(Enchantment.DURABILITY)))
			{
				final ItemMeta itemMeta = item.getItemMeta();
				final int slot = dispenser.getInventory().first(item);
				
				if(itemMeta instanceof Damageable && slot != -1)
				{
					final ItemStack itemstack = dispenser.getInventory().getItem(slot);
					final Damageable itemDamage = (Damageable) itemstack.getItemMeta();
					
					if(itemDamage.getDamage() == 0)
					{
						itemDamage.setDamage(1);
					}
					else if(itemDamage.getDamage() != 0)
					{
						final int addedDamage = itemDamage.getDamage() + 1;

						if(addedDamage < itemstack.getType().getMaxDurability())
						{
							itemDamage.setDamage(addedDamage);
						}
						else
						{
							itemstack.setType(Material.AIR);
						}
					}
					itemstack.setItemMeta(itemDamage);
					dispenser.getInventory().setItem(slot, itemstack);
				}
			}
		}, 1);
	}
	
	private static final BlockFace[] faces = {
		BlockFace.DOWN,
		BlockFace.UP,
		BlockFace.NORTH,
		BlockFace.EAST,
		BlockFace.SOUTH,
		BlockFace.WEST
	};
}
