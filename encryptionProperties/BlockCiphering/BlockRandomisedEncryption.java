package encrypt;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class BlockRandomisedEncryption	{
	public String Encrypt(String location, int[] key)	{
		int height, width;
		BufferedImage img = null;
		File f = null;
		try{
			f = new File(location);
			img = ImageIO.read(f);
		}
		catch(IOException e)	{
			System.out.print(e);
		}
		int now = 0;
		width = img.getWidth();
		height = img.getHeight();
		// int[] key = new int[]{170,45,212,195,139};
		for (int y = 0; y < height; y++)	{
			for (int x = 0; x < width ; x++)	{
				int p = img.getRGB(x,y);
				int alpha = (p>>24) & 0xff;
				int shade = (p>>16) & 0xff;
				if (x == 0 && y == 0)	{
					shade = key[0] ^ shade;
				}
				else{
					shade = now ^ shade ;
				}
				now = shade ^ x ^ y ^ (x+y) ^ (x%(y+1)) ^ (key[x%5]);
				p = (alpha<<24) | (shade<<16) | (shade<<8) | (shade);
				img.setRGB(x,y,p);
			}
		}
		try{
			f = new File("BlockOutput.png");
			ImageIO.write(img,"png",f);
		}
		catch(IOException e)	{
			System.out.print(e);
		}
		return "BlockOutput.png";
	}
}