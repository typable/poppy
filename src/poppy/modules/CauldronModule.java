package poppy.modules;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;


public class CauldronModule implements Listener
{
	private Plugin plugin;

	public CauldronModule(Plugin plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler
	public void onBlockDispenseEvent(BlockDispenseEvent event)
	{
		final Block block = event.getBlock();
		final BlockState blockState = block.getState();

		if(blockState instanceof Dispenser)
		{
			final Dispenser dispenser = (Dispenser) blockState;
			final ItemStack item = event.getItem();

			if(block.getBlockData() instanceof Directional)
			{
				final Directional direction = (Directional) dispenser.getBlockData();
				final Block cauldron = dispenser.getBlock().getRelative(direction.getFacing());
	
				if(item.getType() == Material.BUCKET)
				{
					if(cauldron.getType() == Material.WATER_CAULDRON)
					{
						cauldron.setType(Material.CAULDRON);
						removeSnapshotItem(dispenser, item.getType(), 1);
						event.setItem(new ItemStack(Material.WATER_BUCKET));
					}
	
					if(cauldron.getType() == Material.LAVA_CAULDRON)
					{
						cauldron.setType(Material.CAULDRON);
						removeSnapshotItem(dispenser, item.getType(), 1);
						event.setItem(new ItemStack(Material.LAVA_BUCKET));
					}
				}
			}
		}
	}

	public void removeSnapshotItem(Dispenser dispenser, Material material, int amount)
	{
		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			dispenser.getSnapshotInventory().addItem(new ItemStack(material, -amount));
			dispenser.update();
		}, 1);
	}
}
