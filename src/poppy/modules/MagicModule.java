package poppy.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

public class MagicModule implements Listener
{
	private Plugin plugin;

	public MagicModule(Plugin plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		ItemStack mainHandItem = player.getInventory().getItemInMainHand();

		if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if(event.getHand() == EquipmentSlot.HAND && mainHandItem.getItemMeta() != null)
			{
				String name = mainHandItem.getItemMeta().getDisplayName();


				System.out.println(mainHandItem.getItemMeta());

				switch (name)
				{
					case "Staff":
						Staff(player, event);
					case "Staff1":
						Staff1(player, event);
					default:
						break;
				}
			}
		}
	}

	private boolean Staff(Player player, PlayerInteractEvent event)
	{
		Block block = player.rayTraceBlocks(500).getHitBlock();

		Random r = new Random();

		for (int i = 0; i < 1; i++) {
			int randomNum = r.nextInt(100) - 1;

			Location loc = block.getLocation().clone().add(0.5, 0 ,0.5).subtract(player.getLocation());
			double dis = block.getLocation().distance(player.getLocation());
			long dis1 = Math.round(dis);

			if(dis1 > 28) {
				dis1 = 28;
			}

			block.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, player.getLocation().add(0.5, 2, 0.5), 0, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 0.1);

			Bukkit.getScheduler().runTaskLater(plugin, () -> {
				block.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, block.getLocation().add(0.5, 1, 0.5), 1000, 0, 0, 0, 0.2, null, true);
				block.getWorld().createExplosion(block.getLocation(), 2);
			}, dis1);
		}
		
		return true;
	}

	boolean toggle = false;
	List<Entity> displays = new ArrayList<>();

	private boolean Staff1(Player player, PlayerInteractEvent event)
	{
		Block block = player.rayTraceBlocks(500).getHitBlock();

		//boolean toggle = false;

		toggle = !toggle;

		//player.sendMessage("" + toggle);

		// while(toggle)
		// {
		// 	player.getLocation().getWorld().spawnParticle(Particle.FIREWORKS_SPARK, player.getLocation(), 1, 0, 0, 0, 0.2, null, true);
		// }

		List<Block> blocks = getBlocksInRadius(player.getLocation().getBlock(), 16, Material.RESPAWN_ANCHOR);

		// BlockDisplay display = (BlockDisplay) player.getWorld().spawnEntity(player.getLocation().add(0.5, 0, 0.5), EntityType.BLOCK_DISPLAY);
		// display.setBillboard(Billboard.CENTER);
		// display.setGlowing(true);
		// display.setBlock(Material.RESPAWN_ANCHOR.createBlockData());

		//TextDisplay text = (TextDisplay) player.getWorld().spawnEntity(player.getLocation().add(0.5, 1, 0.5), EntityType.TEXT_DISPLAY);
		//text.setBillboard(Billboard.CENTER);

		// float scale = 0.2f;

		// Vector3f trans = new Vector3f(scale, scale, scale);
		// Vector3f trans1 = new Vector3f(-(scale / 2), -(scale / 2), -(scale / 2));
		// AxisAngle4f axis = new AxisAngle4f(0, 0, 3, 0);

		// Transformation transformation = new Transformation(trans1, axis, trans, axis);

		// display.setTransformation(transformation);
		// displays.add(display);
		// displays.add(text);

		double radius = 2;

		List<BlockEntityRelation> arrayList = new ArrayList<>();

		new BukkitRunnable()
		{
			public void run()
			{
				List<Block> blocks = getBlocksInRadius(player.getLocation().getBlock(), 16, Material.RESPAWN_ANCHOR);
				
				for(Block b : blocks)
				{

					Location v = b.getLocation().add(0.5, 0 ,0.5).subtract(player.getEyeLocation());
					double dist = b.getLocation().distance(player.getLocation());

					boolean included = false;

					for(BlockEntityRelation blockEntityRelation : arrayList)
					{
						Block b1 = blockEntityRelation.block;
						
						Location v1 = b1.getLocation().add(0.5, 0 ,0.5).subtract(player.getEyeLocation());
						double dist1 = b1.getLocation().distance(player.getLocation());
						
						blockEntityRelation.entity.teleport(player.getEyeLocation().add(v1.getX() / dist1, v1.getY() / dist1 ,v1.getZ() / dist1));

						if(blockEntityRelation.block.equals(b))
						{
							included = true;
							break;
						}
					}

					player.getInventory().setContents(player.getInventory().getContents());

					for(int i = 0; i < arrayList.size(); i++)
					{
						BlockEntityRelation ber = arrayList.get(i);

						for(Block t : blocks)
						{
							if(!ber.block.equals(t))
							{
								ber.entity.remove();
								arrayList.remove(i);
							}
						}
					}

					if(!included)
					{
						BlockDisplay display = (BlockDisplay) player.getWorld().spawnEntity(player.getEyeLocation().add(v.getX() / dist, v.getY() / dist ,v.getZ() / dist), EntityType.BLOCK_DISPLAY);
						display.setBillboard(Billboard.CENTER);
						display.setGlowing(false);
						display.setBlock(Material.RESPAWN_ANCHOR.createBlockData());
						
						float distf = (float) dist;

						float test = 1.0f;
						float scale = test / distf;

						Vector3f trans = new Vector3f(scale, scale, scale);
						Vector3f trans1 = new Vector3f(-(scale / 2), -(scale / 2), -(scale / 2));
						AxisAngle4f axis = new AxisAngle4f(0, 0, 3, 0);

						Transformation transformation = new Transformation(trans1, axis, trans, axis);

						display.setTransformation(transformation);
						displays.add(display);

						arrayList.add(new BlockEntityRelation(b, display));
					}
				}

				if(!toggle)
				{
					arrayList.clear();
					this.cancel();
				}
			}
		}.runTaskTimer(plugin, 0, 1);
		
		//List<Block> blocks = getBlocksInRadius(player.getLocation().getBlock(), 6, Material.RESPAWN_ANCHOR)

		if(block.getType() == Material.RESPAWN_ANCHOR)
		{
			player.teleport(block.getLocation().add(0.5, 1, 0.5).setDirection(player.getLocation().getDirection()));
		}
		
		return true;
	}

	public List<Block> getBlocksInRadius(Block start, int radius, Material material)
	{
		if (radius < 0) {
			return new ArrayList<Block>(0);
		}
		int iterations = (radius * 2) + 1;
		List<Block> blocks = new ArrayList<Block>(iterations * iterations * iterations);
		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				for (int z = -radius; z <= radius; z++) {
					if(material != null && start.getRelative(x, y, z).getType() == material) {
						blocks.add(start.getRelative(x, y, z));
					}
				}
			}
		}
		return blocks;
	}
}

class BlockEntityRelation
{
	public Block block;
	public Entity entity;

	public BlockEntityRelation(Block block, Entity entity)
	{
		this.block = block;
		this.entity = entity;
	}
}