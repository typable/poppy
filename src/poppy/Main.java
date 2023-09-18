package poppy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.event.Listener;

import poppy.modules.AutoBreakerModule;
import poppy.modules.AutoPlacerModule;
import poppy.modules.AutoWorkbenchModule;
import poppy.modules.BlockDetectorModule;
import poppy.modules.CauldronModule;
import poppy.modules.ChairModule;
import poppy.modules.DoubleDoorModule;
import poppy.modules.HopperSorterModule;
import poppy.modules.CommonModule;
import poppy.modules.LeavesDecayModule;
import poppy.modules.SpawnerModule;
import poppy.modules.PlayerMountModule;


public class Main extends JavaPlugin implements Listener
{
	private Plugin plugin;
	private Config config;
	private PluginManager pluginManager;
	
	private CommonModule commonModule;
	private DoubleDoorModule doubleDoorModule;
	private ChairModule chairModule;
	private AutoWorkbenchModule autoWorkbenchModule;
	private LeavesDecayModule leavesDecayModule;
	private CauldronModule cauldronModule;
	private SpawnerModule spawnerModule;
	private HopperSorterModule hopperSorterModule;
	private AutoPlacerModule autoPlacerModule;
	private AutoBreakerModule autoBreakerModule;
	private PlayerMountModule playerMountModule;
	private BlockDetectorModule blockDetectorModule;

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id)
	{
		return new CustomChunkGenerator();
	}

	@Override
	public void onEnable()
	{
		plugin = this;
		config = new Config("config/poppy.yml");
		
		pluginManager = Bukkit.getPluginManager();

		commonModule = new CommonModule();
		pluginManager.registerEvents(commonModule, this);

		doubleDoorModule = new DoubleDoorModule();
		pluginManager.registerEvents(doubleDoorModule, this);

		chairModule = new ChairModule();
		pluginManager.registerEvents(chairModule, this);
		
		autoWorkbenchModule = new AutoWorkbenchModule();
		pluginManager.registerEvents(autoWorkbenchModule, this);

		leavesDecayModule = new LeavesDecayModule(this);
		pluginManager.registerEvents(leavesDecayModule, this);

		cauldronModule = new CauldronModule(this);
		pluginManager.registerEvents(cauldronModule, this);

		spawnerModule = new SpawnerModule();
		pluginManager.registerEvents(spawnerModule, this);

		hopperSorterModule = new HopperSorterModule();
		pluginManager.registerEvents(hopperSorterModule, this);

		autoPlacerModule = new AutoPlacerModule(this);
		pluginManager.registerEvents(autoPlacerModule, this);

		autoBreakerModule = new AutoBreakerModule(this);
		pluginManager.registerEvents(autoBreakerModule, this);

		playerMountModule = new PlayerMountModule();
		pluginManager.registerEvents(playerMountModule, this);

		blockDetectorModule = new BlockDetectorModule();
		pluginManager.registerEvents(blockDetectorModule, this);

		NamespacedKey key = new NamespacedKey(this, "leather");
		FurnaceRecipe recipe = new FurnaceRecipe(key, new ItemStack(Material.LEATHER), Material.ROTTEN_FLESH, 1, 200);
		Bukkit.addRecipe(recipe);

		new WorldCreator("creative")
			.type(WorldType.FLAT)
            .generateStructures(true)
            .createWorld();
	}

	@Override
	public void onDisable()
	{
		chairModule.onDisable();

		Bukkit.unloadWorld("creative", false);

		final Path dir = Paths.get("creative");

        try
		{
	        Files
	            .walk(dir)
	            .sorted(Comparator.reverseOrder())
	            .forEach(path -> {
	                try
					{
	                    System.out.println("Deleting: " + path);
	                    Files.delete(path);
	                }
					catch (final Exception ex)
					{
	                    ex.printStackTrace();
	                }
	            });
        }
		catch (final Exception ex)
		{
            ex.printStackTrace();
        }
	}

	@EventHandler

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player player = (Player) sender;

			switch(label)
			{
				case "head":
					return setItemAsHead(player, args);
				case "skull":
					return getSkullForName(player, args);
				case "invsee":
					return openInventoryForPlayer(player, args);
				case "info":
					return openInfoBook(player, args);
				case "spawn":
					return teleportToSpawn(player, args);
				case "home":
					return teleportToHome(player, args);
				case "warp":
					return teleportToWarppoint(player, args);
				case "sethome":
					return setHome(player, args);
				case "setwarp":
					return setWarppoint(player, args);
				case "slime":
					return isSlimeChunk(player, args);
				case "r":
					return doReload(player, args);
				case "up":
					return placeBlockBelow(player, args);
				case "e":
					return openEnderChest(player, args);
				case "enderchest":
					return openEnderChest(player, args);
				default:
					return false;
			}
		}

		return true;
	}

	private boolean setItemAsHead(final Player player, final String[] args)
	{
		final ItemStack item = player.getInventory().getItemInMainHand();
		final ItemStack head = player.getInventory().getHelmet();

		if(head != null && head.getType().equals(Material.SADDLE))
		{
			player.eject();
		}

		player.getInventory().setHelmet(item);
		player.getInventory().setItemInMainHand((new ItemStack(Material.AIR)));

		if(player.getGameMode() == GameMode.SURVIVAL)
		{
			if(head != null)
			{
				player.getInventory().addItem(head);
			}
		}

		return true;
	}
	
	@SuppressWarnings("deprecation")
	private boolean getSkullForName(final Player player, final String[] args)
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

		final String name = args[0];
		final ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
		final SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setOwner(name);
		skull.setItemMeta(meta);

		player.getInventory().addItem(skull);

		return true;
	}

	private boolean openInventoryForPlayer(final Player player, final String[] args)
	{
		if(args.length == 0)
		{
			return false;
		}

		if(!player.isOp()) 
		{
			player.sendMessage(Constants.Messages.NOT_ENOUGH_PERMISSION);
			return true;
		}

		final Player target = Bukkit.getPlayer(args[0]);

		if(target == null)
		{
			player.sendMessage(ChatColor.RED + "Player not found!");
			return true;
		}

		player.openInventory(target.getInventory());
		
		return true;
	}

	private boolean openInfoBook(final Player player, final String[] args)
	{
		final ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
		final BookMeta meta = (BookMeta) book.getItemMeta();
		meta.setTitle("Info");
		meta.setAuthor("Server");
		meta.setPages(config.getInfoPages());
		book.setItemMeta(meta);

		player.openBook(book);

		return true;
	}
	
	private boolean teleportToSpawn(final Player player, final String[] args)
	{
		final Location location = Bukkit.getWorld("world").getSpawnLocation();

		if(Utils.travelTo(plugin, player, location))
		{
			player.sendMessage(ChatColor.GRAY + "You've been teleported to spawn.");
		}

		return true;
	}

	private boolean teleportToHome(final Player player, final String[] args)
	{
		final Location location = config.getHome(player);

		if(location == null)
		{
			player.sendMessage(ChatColor.RED + "You've don't have a home point.");
			return true;
		}
		
		if(Utils.travelTo(plugin, player, location))
		{
			player.sendMessage(ChatColor.GRAY + "You've been teleported to your home.");
		}

		return true;
	}

	private boolean teleportToWarppoint(final Player player, final String[] args)
	{
		if(args.length != 1)
		{
			return false;
		}
		
		final String name = args[0].toLowerCase();
		final Location location = config.getWarp(name);

		if(location == null)
		{
			player.sendMessage(ChatColor.RED + "Warppoint " + name + " doesn't exist!");
			return true;
		}
		
		if(Utils.travelTo(plugin, player, location))
		{
			player.sendMessage(ChatColor.GRAY + "You've been teleported to warppoint " + name + ".");
		}

		return true;
	}

	private boolean setHome(final Player player, final String[] args)
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

		return true;
	}

	private boolean setWarppoint(final Player player, final String[] args)
	{
		if(args.length != 1)
		{
			return false;
		}
		
		final String name = args[0].toLowerCase();

		if(!Utils.payFee(player, Material.COMPASS, 1))
		{
			player.sendMessage(ChatColor.RED + "The fee for creating a warppoint is 1 compass!");
			return true;
		}

		try
		{
			if(!config.setWarp(name, player.getLocation()))
			{
				player.sendMessage(ChatColor.RED + "Warppoint " + name + " already exists!");
				return true;
			}
			
			player.sendMessage(ChatColor.YELLOW + "Warppoint " + name + " set.");
		}
		catch(Exception ex)
		{
			player.sendMessage(Constants.Messages.FAILED_TO_SAVE_CONFIG_FILE);
		}

		return true;
	}

	private boolean isSlimeChunk(final Player player, final String[] args)
	{
		boolean isSlimeChunk = player.getLocation().getChunk().isSlimeChunk();

		if(isSlimeChunk)
		{
			player.sendMessage(ChatColor.YELLOW + "This is a slime chunk!");
			return true;
		}
		else
		{
			player.sendMessage(ChatColor.RED  + "This is not a slime chunk!");
			return true;
		}
	}

	private boolean doReload(final Player player, final String[] args)
	{
		player.chat("/reload confirm");
		return true;
	}

	private boolean placeBlockBelow(final Player player, final String[] args)
	{
		Location playerLocation = player.getLocation().clone();
		Block blockBelow = playerLocation.add(0, -1, 0).getBlock();

		if(!Utils.isAir(blockBelow.getType()))
		{
			return false;
		}

		if(!player.isOp()) 
		{
			player.sendMessage(Constants.Messages.NOT_ENOUGH_PERMISSION);
			return true;
		}

		blockBelow.setType(Material.GLASS);

		return true;
	}

	private boolean openEnderChest(final Player player, final String[] args)
	{
		if(args.length != 0)
		{
			if(!player.isOp()) 
			{
				player.sendMessage(Constants.Messages.NOT_ENOUGH_PERMISSION);
				return true;
			}

			final Player target = Bukkit.getPlayer(args[0]);

			if(target == null)
			{
				player.sendMessage(ChatColor.RED + "Player not found!");
				return true;
			}

			player.openInventory(target.getEnderChest());

		}
		else
		{
			player.openInventory(player.getEnderChest());
		}

		return true;
	}
}
