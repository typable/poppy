package de.typable.minecrafthub.game;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerState
{
	private UUID uuid;
	private Double health;
	private Integer foodLevel;
	private Integer level;
	private Float exp;
	private Location location;
	private GameMode gameMode;
	private ItemStack[] equipment;

	public static PlayerState of(Player player)
	{
		PlayerState playerState = new PlayerState();

		playerState.setUUID(player.getUniqueId());
		playerState.setHealth(player.getHealth());
		playerState.setFoodLevel(player.getFoodLevel());
		playerState.setLevel(player.getLevel());
		playerState.setExp(player.getExp());
		playerState.setLocation(player.getLocation());
		playerState.setGameMode(player.getGameMode());
		playerState.setEquipment(player.getInventory().getContents());

		return playerState;
	}

	public void apply(Player player)
	{
		if(!player.getUniqueId().equals(uuid))
		{
			throw new IllegalArgumentException("Only the player with the same UUID as the state can recieve it!");
		}

		player.setHealth(health);
		player.setFoodLevel(foodLevel);
		player.setLevel(level);
		player.setExp(exp);
		player.teleport(location);
		player.setGameMode(gameMode);
		player.getInventory().setContents(equipment);
		// equipment
	}

	public UUID getUUID()
	{
		return uuid;
	}

	public void setUUID(UUID uuid)
	{
		this.uuid = uuid;
	}

	public Double getHealth()
	{
		return health;
	}

	public void setHealth(Double health)
	{
		this.health = health;
	}

	public Integer getFoodLevel()
	{
		return foodLevel;
	}

	public void setFoodLevel(Integer foodLevel)
	{
		this.foodLevel = foodLevel;
	}

	public Integer getLevel()
	{
		return level;
	}

	public void setLevel(Integer level)
	{
		this.level = level;
	}

	public Float getExp()
	{
		return exp;
	}

	public void setExp(Float exp)
	{
		this.exp = exp;
	}

	public Location getLocation()
	{
		return location;
	}

	public void setLocation(Location location)
	{
		this.location = location;
	}

	public GameMode getGameMode()
	{
		return gameMode;
	}

	public void setGameMode(GameMode gameMode)
	{
		this.gameMode = gameMode;
	}

	public ItemStack[] getEquipment()
	{
		return equipment;
	}

	public void setEquipment(ItemStack[] equipment)
	{
		this.equipment = equipment;
	}
}
