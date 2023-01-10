package poppy;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.Location;

import poppy.Utils;

public class Config
{
    private File file;
    private FileConfiguration configuration;

    public Config(final String path)
    {
        this.file = new File(path);
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }

    public void setHome(final Player player) throws IOException
    {
        final String path = "home." + player.getDisplayName();
        final Location location = Utils.calcBlockCenter(player.getLocation());
    
        this.configuration.set(path, location);
        this.configuration.save(this.file);
    }

    public Location getHome(final Player player)
    {
        final String path = "home." + player.getDisplayName();
    
        return this.configuration.getLocation(path);
    }

    public boolean setWarp(final String name, final Location location) throws IOException
    {
        final String path = "warp." + name;

        if(this.configuration.getLocation(path) != null)
        {
            return false;
        }
    
        this.configuration.set(path, Utils.calcBlockCenter(location));
        this.configuration.save(this.file);

        return true;
    }

    public Location getWarp(final String name)
    {
        final String path = "warp." + name;
    
        return this.configuration.getLocation(path);
    }

    public List<String> getInfoPages()
    {
        final String path = "info";
        List<String> pages = null;

        try
        {
            this.configuration.load(this.file);
            pages = (List<String>) this.configuration.getList(path);
        }
        catch(Exception ex)
        {
            // ignore
        }

        if(pages == null)
        {
            pages = new ArrayList<>();
        }

        return pages;
    }
}
