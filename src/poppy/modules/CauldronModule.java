package poppy.modules;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Levelled;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;


public class CauldronModule implements Listener
{
	private Plugin plugin;
	private Integer debounceEventCount = 0;

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

				if(debounceEventCount > 0)
				{
					debounceEventCount = 0;
					return;
				}

				if(item.getType() == Material.GLASS_BOTTLE)
				{
					debounceEventCount++;
					final Levelled cauldronData = (Levelled) cauldron.getBlockData();
					final ItemStack waterBottle = new ItemStack(Material.POTION);
					final PotionMeta potionMeta = (PotionMeta) waterBottle.getItemMeta();
					potionMeta.setBasePotionData(new PotionData(PotionType.WATER));
					waterBottle.setItemMeta(potionMeta);

					if(cauldronData.getLevel() == 1)
					{
						cauldron.setType(Material.CAULDRON);
					}
					else
					{
						cauldronData.setLevel(cauldronData.getLevel() - 1);
						System.out.println(cauldronData.getLevel());
						cauldron.setBlockData(cauldronData);
					}

					removeSnapshotItem(dispenser, item.getType(), 1);
					event.setItem(waterBottle);
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
