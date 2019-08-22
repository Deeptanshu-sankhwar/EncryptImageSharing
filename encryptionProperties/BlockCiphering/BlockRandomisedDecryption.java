package decrypt;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class BlockRandomisedDecryption	{
	public String Decrypt(String location, int[] publicKey)	{
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
		height = img.getHeight();
		width = img.getWidth();
		// int[] publicKey = new int[]{170,45,212,195,139};
		for (int y = height-1; y >= 0; y--)	{
			for (int x = width-1; x >= 0;x--)	{
				int p = img.getRGB(x,y);
				int alpha = (p>>24) & 0xff;
				int shade = (p>>16) & 0xff;
				if (x == 0 && y == 0)	{
					shade = shade ^ publicKey[0];
					p = (alpha<<24) | (shade<<16) | (shade<<8) | shade;
				}
				else{
					if (x != 0)	{
						int pNow = img.getRGB(x-1,y);
						int now = (pNow>>16) & 0xff;
						shade = shade ^ now ^ (x-1)  ^ y  ^ (x-1 + y) ^ ((x-1)% (y+1)) ^ publicKey[(x-1)%5];
						p = (alpha<<24) | (shade<<16) | (shade<<8) | shade;
					}
					else{
						int pNow = img.getRGB(width-1, y-1);
						int now = (pNow>>16) & 0xff;
						shade = shade ^ now ^ (width-1) ^ (y-1) ^ (width-1 + y-1) ^ ((width-1)%(y)) ^ publicKey[(width-1)%5];
						p = (alpha<<24) | (shade<<16) | (shade<<8) | shade;
					}
				}
				img.setRGB(x,y,p);
			}
		}
		try{
			f = new File("DecryptedOutput.png");
			ImageIO.write(img,"png",f);
		}
		catch(IOException e){
			System.out.print(e);
		}
		return "DecryptedOutput.png";
	}
}