package poppy.modules;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class ShulkerBoxModule implements Listener
{
	private Inventory viewinventoryState;
	private ShulkerBox shulkerboxState;
	private BlockStateMeta shulkerboxblockStateMeta;
	private ItemStack clickedItemState;
	private ItemStack item1;

	private Plugin plugin;

	public ShulkerBoxModule(Plugin plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event)
	{
		System.out.println("test_cursor:" + event.getCursor().getType());
		Player player = (Player) event.getWhoClicked();
		ItemStack clickedItem = event.getCurrentItem();
		Integer slotIndex = event.getSlot();
		Inventory inventory = event.getClickedInventory();
		Inventory inv = player.getOpenInventory().getTopInventory();

		// event.getClickedInventory() todo save inv

		if(inventory == null)
		{
			return;
		}

		if(clickedItem == null)
		{
			return;
		}


		if(inv != null && clickedItem.getType() == Material.SHULKER_BOX && clickedItem.equals(item1))
		{
			event.setCancelled(true);
		}

		if(event.getClick() == ClickType.RIGHT && clickedItem.getType() == Material.SHULKER_BOX)
		{
			event.setCancelled(true);
		
			ItemMeta itemMeta = clickedItem.getItemMeta();

			if(inventory.getItem(slotIndex) != null && itemMeta instanceof BlockStateMeta)
			{

				BlockStateMeta blockMeta = (BlockStateMeta) itemMeta;
				ShulkerBox shulkerBox = (ShulkerBox) blockMeta.getBlockState();

				if(inv != null && !clickedItem.equals(item1))
				{
					player.sendMessage("yee");
					shulkerBoxSetInv(inv, item1, player);
				}

				if(inv != null && clickedItem.equals(item1))
				{
					player.sendMessage("yee2");
					shulkerBox.getInventory().setContents(inv.getContents());
					blockMeta.setBlockState(shulkerBox);
					clickedItem.setItemMeta(blockMeta);
					event.setCurrentItem(clickedItem);
					// player.updateInventory();
				}

				event.setCurrentItem(new ItemStack(Material.AIR));
				event.getCursor().setType(Material.AIR);
				player.updateInventory();

				Inventory viewInventory = Bukkit.createInventory(player, 27, itemMeta.getDisplayName());
				viewinventoryState = viewInventory;

				viewInventory.setContents(shulkerBox.getInventory().getContents());

				Bukkit.getScheduler().runTaskLater(plugin, () -> {
					player.openInventory(viewInventory);
				}, 1);

				blockMeta.setBlockState(shulkerBox);
				clickedItem.setItemMeta(blockMeta);

				shulkerboxState = shulkerBox;
				shulkerboxblockStateMeta = blockMeta;
				clickedItemState = clickedItem;
				item1 = clickedItem;

				event.setCurrentItem(clickedItem);
			}
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event)
	{
		Player player = (Player) event.getPlayer();
		final Inventory closedInventory = event.getInventory();

		if(closedInventory.equals(viewinventoryState))
		{
			System.out.println("test");
			shulkerboxState.getInventory().setContents(closedInventory.getContents());
			shulkerboxblockStateMeta.setBlockState(shulkerboxState);

			int itemSlot = player.getInventory().first(clickedItemState);

			ItemStack item = player.getInventory().getItem(itemSlot);
			item.setItemMeta(shulkerboxblockStateMeta);
			player.updateInventory();

		}
	}

	// @EventHandler
	// public void onInventoryClick(InventoryClickEvent event)
	// {
	// 	Player player = (Player) event.getWhoClicked();
	// 	Inventory inv = player.getOpenInventory().getTopInventory();

	// }

	public void shulkerBoxSetInv(Inventory inventory, ItemStack itemStack, Player player)
	{
		if(itemStack == null)
		{
			return;
		}

		ItemMeta itemMeta = itemStack.getItemMeta();
		int itemSlot = player.getInventory().first(itemStack);

		System.out.println(itemSlot);

		if(itemSlot == -1)
		{
			return;
		}

		if(itemMeta instanceof BlockStateMeta)
		{
			System.out.println("test save");
			BlockStateMeta blockMeta = (BlockStateMeta) itemMeta;
			ShulkerBox shulkerBox = (ShulkerBox) blockMeta.getBlockState();

			shulkerBox.getInventory().setContents(inventory.getContents());
			
			blockMeta.setBlockState(shulkerBox);

			ItemStack item = player.getInventory().getItem(itemSlot);
			item.setItemMeta(blockMeta);
			//player.updateInventory();
		}
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

