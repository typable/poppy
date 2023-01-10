package poppy.modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.plugin.Plugin;

public class LeavesDecayModule implements Listener
{
	private static final int DELAY = 25;
	private static final List<BlockFace> NEIGHBORS = Arrays.asList(
		BlockFace.UP,
		BlockFace.NORTH,
		BlockFace.EAST,
		BlockFace.SOUTH,
		BlockFace.WEST,
		BlockFace.DOWN
	);

	private Plugin plugin;
	private List<Block> scheduled = new ArrayList<>();

	public LeavesDecayModule(Plugin plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		onBlockRemove(event.getBlock());
	}

	@EventHandler
	public void onLeavesDecay(LeavesDecayEvent event)
	{
		onBlockRemove(event.getBlock());
	}

	private void onBlockRemove(Block block)
	{
		if(!Tag.LOGS.isTagged(block.getType()) && !Tag.LEAVES.isTagged(block.getType()))
		{
			return;
		}

		Collections.shuffle(NEIGHBORS);

		for(BlockFace face : NEIGHBORS)
		{
			final Block neighbor = block.getRelative(face);

			if(!Tag.LEAVES.isTagged(neighbor.getType()))
			{
				continue;
			}

			Leaves leaves = (Leaves) neighbor.getBlockData();

			if(leaves.isPersistent())
			{
				continue;
			}

			if(scheduled.contains(neighbor))
			{
				continue;
			}

			plugin.getServer().getScheduler().runTaskLater(plugin, () -> decay(neighbor), DELAY);

			scheduled.add(neighbor);
		}
	}

	private boolean decay(Block block)
	{
		scheduled.remove(block);

		if(!block.getWorld().isChunkLoaded(block.getX() >> 4, block.getZ() >> 4))
		{
			return false;
		}

		if(!Tag.LEAVES.isTagged(block.getType()))
		{
			return false;
		}

		Leaves leaves = (Leaves) block.getBlockData();

		if(leaves.isPersistent())
		{
			return false;
		}

		if(leaves.getDistance()  < 7)
		{
			return false;
		}

		LeavesDecayEvent event = new LeavesDecayEvent(block);
		plugin.getServer().getPluginManager().callEvent(event);

		if(event.isCancelled())
		{
			return false;
		}

		block.breakNaturally();

		return true;
	}
}
