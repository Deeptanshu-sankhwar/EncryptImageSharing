// basic image stegno decoding

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

class BasicImageDecode	{

	//not in use as of now
	static void randomImage(BufferedImage imgTemp, int height, int width)	{
		for (int y = 0; y < height; y++)	{
			for (int x = 0; x < width; x++)	{
				int alpha = (int)(Math.random()*256);
				int shade = (int)(Math.random()*256);
				int p = (alpha << 24) | (shade << 16) | (shade << 8) | shade;
				imgTemp.setRGB(x,y,p);
			}
		}
	}

	public static void main(String args[])	{
		BufferedImage img = null;
		File f = null;
		try{
			f = new File("stegoImage.png");
			img = ImageIO.read(f);
		}
		catch (IOException e){
			System.out.print(e);
		}
		int height, width;
		height = img.getHeight();
		width = img.getWidth();
		BufferedImage imgTemp = new BufferedImage(width/8, height/8, BufferedImage.TYPE_INT_ARGB);
		// BasicImageEncode dummy = new BasicImageEncode();
		// dummy.randomImage(imgTemp, 8*height, 8*width);
	
		int counter = 0;
		int temp = 0;

		for (int y = 0; y < height; y++)	{
			for (int x = 0; x < width; x++)	{
				int p = img.getRGB(x,y);
				int alpha = (p >> 24) & 0xff;
				int shade = p & 0xff;
				int pTemp = img.getRGB(x/8,y/8);
				if (shade != 0 ){
					temp = temp + ((int)Math.pow(2,counter%8));
				}
				
				counter++;
				if (counter % 8 == 0)	{
					pTemp = (alpha << 24) | (temp << 16) | (temp << 8) | (temp);
					temp = 0;
					imgTemp.setRGB(x/8,y/8,pTemp);
				}
			}
		}
		
		try{
			f = new File("decryptedStegoImage.png");
			ImageIO.write(imgTemp, "png", f);
		}
		catch (IOException e) {
			System.out.print(e);
		}

	}
}