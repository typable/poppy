package de.typable.minecrafthub;

import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import de.typable.minecrafthub.constant.DefaultConstants;
import de.typable.minecrafthub.event.AutoWorkbenchListener;
import de.typable.minecrafthub.event.ChairListener;
import de.typable.minecrafthub.event.DoubleDoorListener;
import de.typable.minecrafthub.event.EventListener;
import de.typable.minecrafthub.event.LeavesDecayListener;
import de.typable.minecrafthub.event.StandbyListener;
import de.typable.minecrafthub.game.kit.KitGame;
import de.typable.minecrafthub.util.Util;


public class Main extends JavaPlugin
{
	private PluginManager pluginManager;
	private StandbyListener standbyListener;
	private DoubleDoorListener doubleDoorListener;
	private ChairListener chairListener;
	private AutoWorkbenchListener autoWorkbenchListener;
	private LeavesDecayListener leavesDecayListener;
	private EventListener eventListener;

	private KitGame kitGame;

	private Plugin plugin;
	private BukkitTask task;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		
		pluginManager = Bukkit.getPluginManager();

		standbyListener = new StandbyListener(this);
		pluginManager.registerEvents(standbyListener, this);

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

		kitGame = new KitGame();
		this.getCommand("kit").setExecutor(kitGame);
		pluginManager.registerEvents(kitGame, this);
		
		task = Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable()
		{
			@Override
			public void run()
			{
				try(ServerSocket serverSocket = new ServerSocket(25560))
				{
					while(!serverSocket.isClosed())
					{
						Socket socket = serverSocket.accept();
						
						Util.sendCountdown(plugin, "Server restarts in", 5, new Runnable()
						{
							@Override
							public void run()
							{
								for(Player target : Bukkit.getOnlinePlayers())
								{
									target.kickPlayer("Server restarting...");
								}
								
								Bukkit.getServer().shutdown();
							}
						});
						
						socket.close();
					}
				}
				catch(BindException ex)
				{
					// ignore
				}
				catch(IOException ex)
				{
					ex.printStackTrace();
				}
			}
		});

        new WorldCreator("creative")
            .type(WorldType.FLAT)
            .generateStructures(false)
            .createWorld();
	}

	@Override
	public void onDisable()
	{
		if(task != null && task.isCancelled())
		{
			task.cancel();
		}

		chairListener.onDisable();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player player = (Player) sender;

			if(label.equals("shutdown"))
			{
				if(!player.isOp())
				{
					player.sendMessage(DefaultConstants.Messages.NOT_ENOUGH_PERMISSION);
					return true;
				}
				
				String path = new File(".").getAbsolutePath();
				
				File file = new File(path + "/.shutdown");
				
				try
				{
					file.createNewFile();
					
					Util.sendCountdown(this, "Server shuts down in", 5, new Runnable()
					{
						@Override
						public void run()
						{
							for(Player target : Bukkit.getOnlinePlayers())
							{
								target.kickPlayer("Server stopped");
							}
							
							Bukkit.getServer().shutdown();
						}
					});
				}
				catch(IOException ex)
				{
					ex.printStackTrace();
				}
			}
			
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

			if(label.equals("world"))
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
				
				World world = Bukkit.getWorld(args[0]);

				if(world == null)
				{
					player.sendMessage(DefaultConstants.Messages.WORLD_NOT_EXIST);
					return true;
				}

				player.teleport(world.getSpawnLocation());
			}
		}

		return true;
	}
}
