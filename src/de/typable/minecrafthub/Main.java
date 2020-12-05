package de.typable.minecrafthub;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.typable.minecrafthub.constant.DefaultConstants;
import de.typable.minecrafthub.event.ChairListener;
import de.typable.minecrafthub.event.DoubleDoorListener;
import de.typable.minecrafthub.event.EventListener;
import de.typable.minecrafthub.event.StandbyListener;


public class Main extends JavaPlugin
{
	private PluginManager pluginManager;
	private StandbyListener standbyListener;
	private DoubleDoorListener doubleDoorListener;
	private ChairListener chairListener;
	private EventListener eventListener;

	@Override
	public void onEnable()
	{
		pluginManager = Bukkit.getPluginManager();

		standbyListener = new StandbyListener(this);
		pluginManager.registerEvents(standbyListener, this);

		doubleDoorListener = new DoubleDoorListener();
		pluginManager.registerEvents(doubleDoorListener, this);

		chairListener = new ChairListener();
		pluginManager.registerEvents(chairListener, this);

		eventListener = new EventListener();
		pluginManager.registerEvents(eventListener, this);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player player = (Player) sender;

			if(label.equals("head"))
			{
				ItemStack handitem = player.getInventory().getItemInMainHand();
				ItemStack headitem = player.getInventory().getHelmet();

				player.getInventory().setHelmet(handitem);
				player.getInventory().setItemInMainHand((new ItemStack(Material.AIR)));

				if(player.getGameMode() == GameMode.SURVIVAL)
				{
					if(headitem != null)
					{
						player.getInventory().addItem(headitem);
					}
				}
			}

			if(label.equals("skull"))
			{
				if(!player.isOp())
				{
					player.sendMessage(DefaultConstants.Messages.NOT_ENOUGH_PERMISSION);
					return true;
				}

				if(args.length != 1)
				{
					return false;
				}

				ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
				SkullMeta meta = (SkullMeta) skull.getItemMeta();
				meta.setOwner(args[0]);
				skull.setItemMeta(meta);

				player.getInventory().addItem(skull);
			}

			if(label.equals("standby"))
			{
				if(!player.isOp())
				{
					player.sendMessage(DefaultConstants.Messages.NOT_ENOUGH_PERMISSION);
					return true;
				}

				if(args.length != 1)
				{
					return false;
				}

				String argument = args[0];

				if(!argument.equals("true") && !argument.equals("false"))
				{
					return false;
				}

				boolean enabled = argument.equals("true");
				standbyListener.setEnabled(enabled);

				player.sendMessage(ChatColor.GRAY + "Standby mode is now " + (enabled ? "enabled" : "disabled"));
			}
		}

		return true;
	}
}
