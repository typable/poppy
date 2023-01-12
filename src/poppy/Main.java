package poppy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import poppy.modules.AutoWorkbenchModule;
import poppy.modules.CauldronModule;
import poppy.modules.ChairModule;
import poppy.modules.DoubleDoorModule;
import poppy.modules.CommonModule;
import poppy.modules.LeavesDecayModule;


public class Main extends JavaPlugin
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

	@Override
	public void onEnable()
	{
		plugin = this;
		config = new Config("config/minecraft-hub.yml");
		
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
	}

	@Override
	public void onDisable()
	{
		chairModule.onDisable();
	}

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
					return addWarppoint(player, args);
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
		
		final String name = args[0];
		final Location location = config.getWarp(name);

		if(location == null)
		{
			player.sendMessage(ChatColor.RED + "Warp point " + name + " doesn't exist!");
			return true;
		}
		
		if(Utils.travelTo(plugin, player, location))
		{
			player.sendMessage(ChatColor.GRAY + "You've been teleported to warp point " + name + ".");
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

	private boolean addWarppoint(final Player player, final String[] args)
	{
		if(args.length != 1)
		{
			return false;
		}
		
		final String name = args[0];

		if(!Utils.payFee(player, Material.COMPASS, 1))
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

		return true;
	}
	
}
