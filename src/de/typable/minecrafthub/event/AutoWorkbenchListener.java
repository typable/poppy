package de.typable.minecrafthub.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dropper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import de.typable.minecrafthub.util.Util;


public class AutoWorkbenchListener implements Listener
{
	@EventHandler
	public void onItemMove(InventoryMoveItemEvent event)
	{
		Inventory source = event.getSource();
		Inventory target = event.getDestination();

		if(source.getHolder() instanceof Dropper)
		{
			Dropper dropper = (Dropper) source.getHolder();

			ItemFrame frame = getItemFrame(dropper.getBlock());

			if(frame == null)
			{
				return;
			}

			ItemStack item = frame.getItem();

			Recipe recipe = getCraftingRecipe(item, source, target);

			if(recipe != null)
			{
				if(recipe instanceof ShapelessRecipe)
				{
					ShapelessRecipe shapless = (ShapelessRecipe) recipe;
					
					target.addItem(shapless.getResult());
				}

				if(recipe instanceof ShapedRecipe)
				{
					ShapedRecipe shaped = (ShapedRecipe) recipe;

					target.addItem(shaped.getResult());
				}
			}
			
			event.setCancelled(true);
		}
	}

	private Recipe getCraftingRecipe(ItemStack item, Inventory source, Inventory target)
	{
		if(Util.isEmpty(item))
		{
			return null;
		}

		Iterator<Recipe> iterator = Bukkit.recipeIterator();

		while(iterator.hasNext())
		{
			Recipe recipe = iterator.next();

			if(Util.compare(item, recipe.getResult()))
			{
				List<ItemStack> ingredients = null;

				if(recipe instanceof ShapelessRecipe)
				{
					ShapelessRecipe shapless = (ShapelessRecipe) recipe;

					ingredients = shapless.getIngredientList();
				}

				if(recipe instanceof ShapedRecipe)
				{
					ShapedRecipe shaped = (ShapedRecipe) recipe;

					ingredients = new ArrayList<>();
					
					for(ItemStack value : shaped.getIngredientMap().values())
					{
						if(ingredients.isEmpty())
						{
							if(!Util.isEmpty(value))
							{
								ingredients.add(value);
							}
						}
						else
						{
							appendUniqueIngredient(ingredients, value);
						}
					}
				}

				if(ingredients == null)
				{
					continue;
				}

				if(!hasIngredients(source, ingredients))
				{
					continue;
				}

				if(Util.isInventoryFull(target, recipe.getResult()))
				{
					return null;
				}
				
				removeIngredients(ingredients, source);
				
				return recipe;
			}
		}

		return null;
	}

	private void removeIngredients(List<ItemStack> ingredients, Inventory inventory)
	{
		for(ItemStack ingredient : ingredients)
		{
			if(Util.isEmpty(ingredient))
			{
				continue;
			}
			
			int amount = ingredient.getAmount();
			
			ListIterator<ItemStack> iterator = inventory.iterator();
			
			while(iterator.hasNext())
			{
				ItemStack item = iterator.next();
				
				if(Util.isEmpty(item))
				{
					continue;
				}
				
				if(Util.compare(item, ingredient))
				{
					if(item.getAmount() > amount)
					{
						item.setAmount(item.getAmount() - amount);
						amount = 0;
						
						break;
					}

					if(item.getAmount() == amount)
					{
						amount = 0;
						inventory.removeItem(item);
						
						break;
					}

					if(item.getAmount() < amount)
					{
						amount -= item.getAmount();
						inventory.removeItem(item);
					}
				}
			}
		}
	}
	
	private void appendUniqueIngredient(List<ItemStack> ingredients, ItemStack item)
	{
		for(ItemStack ingredient : ingredients)
		{
			if(Util.compare(item, ingredient))
			{
				ingredient.setAmount(ingredient.getAmount() + item.getAmount());
				
				return;
			}
		}
		
		if(!Util.isEmpty(item))
		{
			ingredients.add(item);
		}
	}
	
	private boolean hasIngredients(Inventory inventory, List<ItemStack> ingredients)
	{
		for(ItemStack ingredient : ingredients)
		{
			if(ingredient != null && !Util.containsAtLeast(inventory, ingredient, ingredient.getAmount()))
			{
				return false;
			}
		}
		
		return true;
	}

	private ItemFrame getItemFrame(Block block)
	{
		for(Entity entity : block.getWorld().getNearbyEntities(block.getLocation(), 2, 2, 2))
		{
			if(entity instanceof ItemFrame)
			{
				ItemFrame frame = (ItemFrame) entity;

				if(frame.getFacing() == BlockFace.UP)
				{
					if(block.getLocation().add(0.5, 1.03125, 0.5).distance(frame.getLocation()) < 0.2D)
					{
						return frame;
					}
				}
			}
		}

		return null;
	}
}
