package de.typable.minecrafthub.event;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.typable.minecrafthub.util.Util;


public class EventListener implements Listener
{
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{

		Player player = event.getPlayer();

		if(event.getAction() == Action.PHYSICAL)
		{
			if(event.getClickedBlock() != null)
			{
				if(event.getClickedBlock().getType() == Material.FARMLAND)
				{
					event.setCancelled(true);
				}
			}
		}

		// farming with right click

		EquipmentSlot equip = event.getHand();
		if(equip.equals(EquipmentSlot.HAND)) {

			if(event.getAction() == Action.RIGHT_CLICK_BLOCK) 
			{
				Block block = event.getClickedBlock();
	
				if(block == null) 
				{
					return;
				}

				Material material = block.getType();

				if(player.isSneaking()) 
				{
					return;
				}

				BlockData blockdata = block.getBlockData();

				if(blockdata instanceof Ageable) 
				{

					Ageable ageable = (Ageable) blockdata;
	
					if(Util.isFarmable(material) && ageable.getAge() == ageable.getMaximumAge()) 
					{

						if(blockdata instanceof Directional) 
						{
							Directional directional = (Directional) blockdata;
							BlockFace blockface = directional.getFacing();

							block.breakNaturally(player.getItemInUse());
							block.setType(material);
							directional = (Directional) blockdata;
							ageable.setAge(0);
							directional.setFacing(blockface);
							block.setBlockData(blockdata);
						} 
						else 
						{
							block.breakNaturally(player.getItemInUse());
							block.setType(material);
						}

					}

				}
			}

		}
	}

	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event)
	{
		if(event.getEntityType() == EntityType.CREEPER)
		{
			event.blockList().clear();
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event)
	{
		String message = ChatColor.GRAY + event.getMessage().replace("%", "%%");
		String format = ChatColor.WHITE + event.getPlayer().getName() + ": " + message;
		event.setFormat(format);
	}

	@EventHandler
	public void onEntityHit(EntityDamageByEntityEvent event)
	{
		if(event.getDamager() instanceof Snowball)
		{
			Snowball snowball = (Snowball) event.getDamager();

			if(snowball.getShooter() instanceof Player)
			{
				Player player = (Player) event.getEntity();

				player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 2, 1));
				event.setDamage(1);
			}
		}
	}

	@EventHandler
	public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event)
	{
		Player player = event.getPlayer();

		if(event.getRightClicked().getType().equals(EntityType.ARMOR_STAND))
		{
			ItemStack item = player.getInventory().getItemInMainHand();
			
			if(item.getType() == Material.STICK && item.getAmount() > 1 && player.isSneaking())
			{
				ArmorStand armorstand = (ArmorStand) event.getRightClicked();

				armorstand.setArms(true);
				item.setAmount(item.getAmount() - 2);
				
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onArmorStandDestroy(EntityDeathEvent event)
	{
		if(event.getEntity() instanceof ArmorStand)
		{
			ArmorStand armorstand = (ArmorStand) event.getEntity();

			if(armorstand.hasArms())
			{
				event.getDrops().add(new ItemStack(Material.STICK, 2));
			}
		}
	}
}