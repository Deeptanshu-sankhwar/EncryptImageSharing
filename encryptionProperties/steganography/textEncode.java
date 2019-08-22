//basic stegano encoding

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.Scanner;
import java.lang.Math;

class BasicSteganoEncode	{
	public static void main(String args[])	{
		int height, width;
		BufferedImage img = null;
		File f = null;
		Scanner sc = new Scanner(System.in);
		System.out.println("Type in message to embedd");
		String msg = sc.nextLine();
		// System.out.print(code);
		try{
			f = new File("teraSurror.png");
			img = ImageIO.read(f);
		}
		catch(IOException e)	{
			System.out.print(e);
		}
		height = img.getHeight();
		width = img.getWidth();
		int key = 0;
		for (int y = 0; y < height; y++)	{
			for (int x = 0; x < width; x++)	{
				int p = img.getRGB(x,y);
				int alpha = (p >> 24) & 0xff;
				int shade = (p >> 16) & 0xff;
				if (key == 8*msg.length())
					break;
				int code = (int)(msg.charAt(key/8));
				if ( (code & ((int)Math.pow(2,key%8))) != 0)	{
					if (shade % 2 == 0)
						shade = shade - 1;
				}
				else if ( (code & ((int)Math.pow(2,key%8)))  == 0)	{
					if (shade % 2 != 0)
						shade = shade - 1;
				}
				key++;
				p = (alpha<<24) | (shade<<16) | (shade<<8) | shade;
				img.setRGB(x,y,p);
			}
			if (key == 8*msg.length())
				break;
		}
		try{
			f = new File("stegoTextImage.png");
			ImageIO.write(img,"png",f);
		}
		catch(IOException e)	{
			System.out.print(e);
		}

	}
}