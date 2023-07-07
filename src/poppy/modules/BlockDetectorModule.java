package poppy.modules;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Observer;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.inventory.ItemStack;


public class BlockDetectorModule implements Listener
{
	@EventHandler
	public void onBlockRedstone(BlockRedstoneEvent event)
	{
		final Block block = event.getBlock();

		if(block.getType() != Material.OBSERVER) return;

		final ItemFrame[] itemframes = HopperSorterModule.getBlockItemFrames(block);
		final ItemStack[] itemstacks = getItemsFromItemframes(itemframes);

		if(itemstacks.length <= 0) return;

		final Observer observer = (Observer) block.getBlockData();

		if(block.getBlockData() instanceof Directional)
		{
			final Location location = observer.getFacing().getDirection().toLocation(block.getWorld());
			final Block faceblock = block.getLocation().add(location.toVector()).getBlock();

			boolean check = false;

			for(ItemStack item : itemstacks)
			{
				if(item.getType() == faceblock.getType())
				{
					observer.setPowered(true);
					check = true;
				}
			}

			if(!check) event.setNewCurrent(0);
		}
	}

	public static ItemStack[] getItemsFromItemframes(ItemFrame[] itemframes)
	{
		final HashSet<ItemStack> itemstacks = new HashSet <ItemStack>();

		for(ItemFrame itemframe : itemframes)
		{
			itemstacks.add(itemframe.getItem());
		}
		return itemstacks.toArray(new ItemStack[itemstacks.size()]);
	}
}
