package de.typable.minecrafthub;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;


public class Config
{
	private static final Gson gson = new Gson();

	public static Config open(String path)
	{
		File file = new File(path);

		try
		{
			if(!file.exists())
			{
				file.createNewFile();
			}

			String content = Files.readString(Paths.get(path));

			if(content != null)
			{
				// TODO: convert string to json object
			}
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}

		return new Config();
	}
}
