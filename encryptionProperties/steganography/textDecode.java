//basic stegano decoding

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.Scanner;
import java.lang.Math;

class BlockSteganoDecode	{
	public static void main(String args[])	{
		BufferedImage img = null;
		File f = null;
		int height, width;
		try{
			f = new File("stegoImage.png");
			img = ImageIO.read(f);
		}
		catch(IOException e)	{
			System.out.print(e);
		}
		Scanner sc = new Scanner(System.in);
		System.out.println("Input public key");
		int publicKey = sc.nextInt();
		int key = 0;
		int decode = 0;
		char output;
		height = img.getHeight();
		width = img.getWidth();
		System.out.println("Decrypted message is");
		for (int y = 0; y < height; y++)	{
			for (int x = 0; x < width; x++)	{
				int p = img.getRGB(x,y);
				int shade = (p >> 16) & 0xff;
				if (key == 8*publicKey)
					break;
				if(shade % 2 != 0)	{
					decode = decode + ((int)Math.pow(2,key%8));
				}
				key++;
				if (key % 8 == 0)	{
					output = (char)(decode);
					System.out.print(output);
					decode = 0;
				}
			}
			if (key == 8*publicKey)
				break;
		}
	}
}