package poppy.modules;

import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import poppy.Utils;

public class HopperSorterModule implements Listener
{
	@EventHandler
	public void onInventoryMoveItem(InventoryMoveItemEvent event)
	{

		if(event.getDestination().getLocation() == null)
		{
			return;
		}

		final Location targetHopperLocation = event.getDestination().getLocation();
		final Block targetHopper = targetHopperLocation.getBlock();

		if(targetHopper.getType() != Material.HOPPER)
		{
			return;
		}

		ItemFrame[] itemFrames = getBlockItemFrames(targetHopper);

		if(itemFrames.length == 0)
		{
			return;
		}

		event.setCancelled(true);

		final HashSet<ItemStack> allowedItems = new HashSet <ItemStack>();
		final HashSet<ItemStack> allowedItemExact = new HashSet <ItemStack>();

		saveItemsInLists(itemFrames, allowedItems, allowedItemExact);
		addInventoryItems(allowedItems, allowedItemExact, event.getSource(), event.getDestination());

		for(ItemStack item : allowedItems)
		{
			if(event.getItem().getType().equals(item.getType()))
			{
				event.setCancelled(false);
			}
		}

		for(ItemStack item : allowedItemExact)
		{
			if(event.getItem().equals(item) && event.getItem().getItemMeta().equals(item.getItemMeta()))
			{
				event.setCancelled(false);
			}
		}
	}

	@EventHandler
	public void onInventoryPickupItem(InventoryPickupItemEvent event)
	{
		final Location targetHopperLocation = event.getInventory().getLocation().clone();
		final Block targetHopper = targetHopperLocation.getBlock();

		if(!event.getInventory().getType().equals(InventoryType.HOPPER))
		{
			return;
		}

		ItemFrame[] itemFrames = getBlockItemFrames(targetHopper);

		if(itemFrames.length == 0)
		{
			return;
		}

		event.setCancelled(true);

		final HashSet<ItemStack> allowedItems = new HashSet <ItemStack>();
		final HashSet<ItemStack> allowedItemExact = new HashSet <ItemStack>();

		saveItemsInLists(itemFrames, allowedItems, allowedItemExact);

		for(ItemStack item : allowedItems)
		{
			if(event.getItem().getItemStack().getType().equals(item.getType()))
			{
				event.setCancelled(false);
			}
		}

		for(ItemStack item : allowedItemExact)
		{
			if(event.getItem().getItemStack().getType().equals(item.getType()) && event.getItem().getItemStack().getItemMeta().equals(item.getItemMeta()))
			{
				event.setCancelled(false);
			}
		}
	}

	public void addInventoryItems(HashSet<ItemStack> allowedItems, HashSet<ItemStack> allowedItemsExact, Inventory inventory, Inventory destinationInv)
	{
		ItemStack dummyItem = null;
		final ListIterator<ItemStack> iterator = inventory.iterator();

		while(iterator.hasNext())
		{
			final ItemStack item = iterator.next();

			for(ItemStack allowedItemExact : allowedItemsExact)
			{
				if(Utils.isEmpty(item))
				{
					continue;
				}

				if(item.getType().equals(allowedItemExact.getType()) && item.getItemMeta().equals(allowedItemExact.getItemMeta()) && dummyItem == null)
				{
					final HashSet<ItemStack> nullItems = new HashSet <ItemStack>();
					final int slot = inventory.first(item);

					if(slot > 0)
					{
						for (int i = slot -1 ; i >= 0; i--)
						{
							Boolean filterdItem = false;
							final ItemStack nullItem = inventory.getItem(i);

							if(nullItem != null)
							{
								for(ItemStack allowedItem1 : allowedItems)
								{
									if(allowedItem1.getType().equals(nullItem.getType()))
									{
										filterdItem = true;
									}
								}

								for(ItemStack allowedItemExact1 : allowedItemsExact)
								{
									if(allowedItemExact1.getType().equals(nullItem.getType()) && allowedItemExact1.getItemMeta().equals(nullItem.getItemMeta()))
									{
										filterdItem = true;
									}
								}

								if(!filterdItem)
								{
									nullItems.add(inventory.getItem(i));
								}
							}
						}
					}

					if(nullItems.size() != 0)
					{
						if(!Utils.isInventoryFull(destinationInv, item))
						{
							final ItemStack clonedItem = item.clone();
							clonedItem.setAmount(1);

							destinationInv.addItem(clonedItem);
							item.setAmount(item.getAmount() - 1);
							dummyItem = item;
							break;
						}
					}
				}
			}

			for(ItemStack allowedItem : allowedItems)
			{
				if(Utils.isEmpty(item))
				{
					continue;
				}

				if(item.getType() == allowedItem.getType() && dummyItem == null)
				{
					final HashSet<ItemStack> nullItems = new HashSet <ItemStack>();
					final  int slot = inventory.first(item);

					if(slot > 0)
					{
						for (int i = slot - 1 ; i >= 0; i--)
						{
							Boolean filterdItem = false;
							final ItemStack nullItem = inventory.getItem(i);

							if(nullItem != null)
							{
								for(ItemStack allowedItem1 : allowedItems)
								{
									if(allowedItem1.getType().equals(nullItem.getType()))
									{
										filterdItem = true;
									}
								}

								for(ItemStack allowedItemExact1 : allowedItemsExact)
								{
									if(allowedItemExact1.getType().equals(nullItem.getType()) && allowedItemExact1.getItemMeta().equals(nullItem.getItemMeta()))
									{
										filterdItem = true;
									}
								}

								if(!filterdItem)
								{
									nullItems.add(inventory.getItem(i));
								}
							}
						}
					}

					if(nullItems.size() != 0)
					{
						if(!Utils.isInventoryFull(destinationInv, item))
						{
							final ItemStack clonedItem = item.clone();
							clonedItem.setAmount(1);

							destinationInv.addItem(clonedItem);
							item.setAmount(item.getAmount() - 1);
							dummyItem = item;
							break;
						}
					}
				}
			}
		}
	}

	public void saveItemsInLists(ItemFrame[] itemFrames, HashSet<ItemStack> allowedItems, HashSet<ItemStack> allowedItemExact)
	{
		for(ItemFrame itemFrame : itemFrames)
		{
			if(itemFrame.getType() == EntityType.GLOW_ITEM_FRAME)
			{
				allowedItemExact.add(itemFrame.getItem());

				for(ItemStack items : getShulkerboxContent(itemFrame.getItem()))
				{
					allowedItemExact.add(items);
				}
			}
			else if(itemFrame.getType() == EntityType.ITEM_FRAME)
			{
				allowedItems.add(itemFrame.getItem());

				for(ItemStack items : getShulkerboxContent(itemFrame.getItem()))
				{
					allowedItems.add(items);
				}
			}
		}
	}

	public static ItemFrame[] getBlockItemFrames(Block block)
	{
		final HashSet<ItemFrame> itemframes = new HashSet <ItemFrame>();

		for(Entity entity : getNearbyEntities(block.getLocation().add(.5, .5, .5), 1))
		{
			if(entity instanceof ItemFrame)
			{
				final ItemFrame itemFrame = (ItemFrame) entity;
				itemframes.add(itemFrame);
			}
		}
		return itemframes.toArray(new ItemFrame[itemframes.size()]);
	}

	public static ItemStack[] getShulkerboxContent(ItemStack itemStack)
	{
		final HashSet<ItemStack> itemList = new HashSet<ItemStack>();

		if(itemStack.getType().equals(Material.AIR))
		{
			return itemList.toArray(new ItemStack[itemList.size()]);
		}

		final ItemMeta itemMeta = itemStack.getItemMeta();
		final Set<Material> shulkerboxes = Tag.SHULKER_BOXES.getValues();

		for(Material material : shulkerboxes)
		{
			if(itemStack.getType().equals(material) && itemMeta instanceof BlockStateMeta)
			{
				final BlockStateMeta blockMeta = (BlockStateMeta) itemMeta;
				final ShulkerBox shulkerBox = (ShulkerBox) blockMeta.getBlockState();

				for(ItemStack item : shulkerBox.getInventory().getContents().clone())
				{
					if(item != null)
					{
						item.setAmount(1);
						itemList.add(item);
					}
				}
			}
		}
		return itemList.toArray(new ItemStack[itemList.size()]);
	}

	public static Entity[] getNearbyEntities(Location l, int radius)
	{
		int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
		HashSet <Entity> radiusEntities = new HashSet < Entity > ();
	 
		for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
			for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
				int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
				for (Entity e: new Location(l.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk().getEntities()) {
					if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock())
						radiusEntities.add(e);
				}
			}
		}
		return radiusEntities.toArray(new Entity[radiusEntities.size()]);
	}
}
