package de.typable.minecrafthub;

import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.material.Tree;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.typable.minecrafthub.constant.Constants;
import de.typable.minecrafthub.event.AutoWorkbenchListener;
import de.typable.minecrafthub.event.ChairListener;
import de.typable.minecrafthub.event.DoubleDoorListener;
import de.typable.minecrafthub.event.EventListener;
import de.typable.minecrafthub.event.LeavesDecayListener;
import de.typable.minecrafthub.util.Util;
import de.typable.minecrafthub.config.Config;


public class Main extends JavaPlugin
{
	private PluginManager pluginManager;
	private DoubleDoorListener doubleDoorListener;
	private ChairListener chairListener;
	private AutoWorkbenchListener autoWorkbenchListener;
	private LeavesDecayListener leavesDecayListener;
	private EventListener eventListener;

	private Plugin plugin;
	private Config config;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		config = new Config("config/minecraft-hub.yml");
		
		pluginManager = Bukkit.getPluginManager();

		doubleDoorListener = new DoubleDoorListener();
		pluginManager.registerEvents(doubleDoorListener, this);

		chairListener = new ChairListener();
		pluginManager.registerEvents(chairListener, this);
		
		autoWorkbenchListener = new AutoWorkbenchListener();
		pluginManager.registerEvents(autoWorkbenchListener, this);

		leavesDecayListener = new LeavesDecayListener(this);
		pluginManager.registerEvents(leavesDecayListener, this);

		eventListener = new EventListener();
		pluginManager.registerEvents(eventListener, this);
	}

	@Override
	public void onDisable()
	{
		chairListener.onDisable();
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
					player.sendMessage(Constants.Messages.NOT_ENOUGH_PERMISSION);
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

			if(label.equals("spawn"))
			{
				final Location location = Bukkit.getWorld("world").getSpawnLocation();

				if(travelTo(player, location))
				{
					player.sendMessage(ChatColor.GRAY + "You've been teleported to spawn.");
				}
			}

			if(label.equals("invsee"))
			{
				if(args.length == 0) {
					return false;
				}

				if(Bukkit.getPlayer(args[0]) == null)
				{
					player.sendMessage(ChatColor.RED + "Player not found!");
					return true;
				}

				if(!player.isOp()) 
				{
					player.sendMessage(Constants.Messages.NOT_ENOUGH_PERMISSION);
					return true;
				}

				Player target = Bukkit.getPlayer(args[0]);

				player.openInventory(target.getInventory());

				if(args.length == 1 && args[1].toLowerCase() == "enderchest")
				{
					player.openInventory(target.getEnderChest());
				}
			}

			if(label.equals("sethome"))
			{
				try
				{
					config.setHome(player);
					player.sendMessage(ChatColor.YELLOW + "Home point set.");
				}
				catch(Exception ex)
				{
					player.sendMessage(Constants.Messages.FAILED_TO_SAVE_CONFIG_FILE);
				}
			}

			if(label.equals("home"))
			{
				final Location location = config.getHome(player);

				if(location == null)
				{
					player.sendMessage(ChatColor.RED + "You've don't have a home point.");
					return true;
				}
				
				if(travelTo(player, location))
				{
					player.sendMessage(ChatColor.GRAY + "You've been teleported to your home.");
				}
			}

			if(label.equals("setwarp"))
			{
				if(args.length != 1)
				{
					return false;
				}
				
				final String name = args[0];

				if(!payFee(player, Material.COMPASS, 1))
				{
					player.sendMessage(ChatColor.RED + "The fee for creating a warp point is 1 compass!");
					return true;
				}

				try
				{
					if(!config.setWarp(name, player.getLocation()))
					{
						player.sendMessage(ChatColor.RED + "Warp point " + name + " already exists!");
						return true;
					}
					
					player.sendMessage(ChatColor.YELLOW + "Warp point " + name + " set.");
				}
				catch(Exception ex)
				{
					player.sendMessage(Constants.Messages.FAILED_TO_SAVE_CONFIG_FILE);
				}
			}

			if(label.equals("warp"))
			{
				if(args.length != 1)
				{
					return false;
				}
				
				final String name = args[0];
				final Location location = config.getWarp(name);

				if(location == null)
				{
					player.sendMessage(ChatColor.RED + "Warp point " + name + " doesn't exist!");
					return true;
				}
				
				if(travelTo(player, location))
				{
					player.sendMessage(ChatColor.GRAY + "You've been teleported to warp point " + name + ".");
				}
			}

			if(label.equals("info"))
			{
				final ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
				final BookMeta meta = (BookMeta) book.getItemMeta();
				meta.setTitle("Info");
				meta.setAuthor("Server");
				meta.setPages(config.getInfoPages());
				book.setItemMeta(meta);
		
				player.openBook(book);
			}
		}

		return true;
	}

	private boolean travelTo(final Player player, final Location location)
	{
		final int fee = Util.calcTravelFee(player.getLocation(), location);
		final String unit = fee == 1 ? "emerald" : "emeralds";

		if(!payFee(player, Material.EMERALD, fee) && player.getGameMode() == GameMode.SURVIVAL)
		{
			player.sendMessage(ChatColor.RED + "Not enough emeralds to teleport! Travel fee: " + fee + " " + unit);
			return false;
		}

		if(player.getVehicle() != null)
		{
			Entity vehicle = player.getVehicle();
			vehicle.eject();
			player.teleport(location);

			Bukkit.getScheduler().runTaskLater(plugin, () -> {
				vehicle.teleport(location);
			}, 3);

			Bukkit.getScheduler().runTaskLater(plugin, () -> {
				vehicle.addPassenger(player);
			}, 6);
		}
		else
		{
			player.teleport(location);
		}

		return true;
	}

	private boolean payFee(final Player player, final Material unit, final int amount)
	{
		final ItemStack item = player.getInventory().getItemInMainHand();

		if(item.getType() != unit || item.getAmount() < amount)
		{
			return false;
		}

		item.setAmount(item.getAmount() - amount);
		
		return true;
	}
}
