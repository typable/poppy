package poppy.modules;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import poppy.Utils;

public class AutoPlacerModule implements Listener
{
	private Plugin plugin;

	public AutoPlacerModule(Plugin plugin)
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
			Dispenser dispenser = (Dispenser) blockState;

			if(dispenser.getCustomName() != null && dispenser.getCustomName().toLowerCase().equals("placer"))
			{
				event.setCancelled(true);

				if(block.getBlockData() instanceof Directional)
				{
					final Directional direction = (Directional) dispenser.getBlockData();
					final Block faceBlock = dispenser.getBlock().getRelative(direction.getFacing());

					if(item.getType().isBlock() && Utils.isAir(faceBlock.getType()) && !isShulkerboxContent(item))
					{
						faceBlock.setType(item.getType());
						removeSnapshotItem(dispenser, item.getType(), 1);
					}
				}

				if(isShulkerboxContent(item))
				{
					event.setCancelled(false);
				}
			}
		}
	}

	public boolean isShulkerboxContent(ItemStack itemStack)
	{
		final ItemMeta itemMeta = itemStack.getItemMeta();
		final Set<Material> shulkerboxes = Tag.SHULKER_BOXES.getValues();

		for(Material material : shulkerboxes)
		{
			if(itemStack.getType().equals(material) && itemMeta instanceof BlockStateMeta)
			{
				return true;
			}
		}
		return false;
	}
	
	public void removeSnapshotItem(Dispenser dispenser, Material material, int amount)
	{
		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			dispenser.getSnapshotInventory().addItem(new ItemStack(material, -amount));
			dispenser.update();
		}, 1);
	}
}
