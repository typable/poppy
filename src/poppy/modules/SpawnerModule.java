package poppy.modules;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class SpawnerModule implements Listener
{
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		final Block block = event.getBlock();
		final Player player = event.getPlayer();
		final ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
		final Location blockLocation = block.getLocation();

		if(itemInMainHand.containsEnchantment(Enchantment.SILK_TOUCH))
		{
			if(block.getType() == Material.SPAWNER)
			{
				final CreatureSpawner creatureSpawner = (CreatureSpawner) block.getState();
				final ItemStack spawner = new ItemStack(Material.SPAWNER);
				final ItemMeta spawnerItemMeta = spawner.getItemMeta();
				final ArrayList<String> spawneritemLore = new ArrayList<String>();
				final BlockStateMeta spawnerblockMeta = (BlockStateMeta) spawner.getItemMeta();

				spawneritemLore.add(creatureSpawner.getSpawnedType().toString());
				spawnerItemMeta.setLore(spawneritemLore);
				spawner.setItemMeta(spawnerItemMeta);
				spawnerblockMeta.setBlockState(creatureSpawner);
				blockLocation.getWorld().dropItemNaturally(blockLocation, spawner);
				block.setType(Material.AIR);
				event.setExpToDrop(0);
			}

			if(block.getType() == Material.BUDDING_AMETHYST)
			{
				blockLocation.getWorld().dropItemNaturally(blockLocation, new ItemStack(Material.BUDDING_AMETHYST));
			}
		}
	}

	@EventHandler
	public void onPlaceEvent(BlockPlaceEvent event)
	{
		final Block block = event.getBlockPlaced();
		final ItemStack item = event.getItemInHand();
		final ItemMeta itemMeta = item.getItemMeta();

		if(itemMeta.getLore() == null)
		{
			return;
		}

		if(block.getType() == Material.SPAWNER)
		{
			final CreatureSpawner creatureSpawnerBlock = (CreatureSpawner) block.getState();
			final EntityType entityType = EntityType.valueOf(itemMeta.getLore().get(0));

			creatureSpawnerBlock.setSpawnedType(entityType);
			creatureSpawnerBlock.update();
		}
	}
}
