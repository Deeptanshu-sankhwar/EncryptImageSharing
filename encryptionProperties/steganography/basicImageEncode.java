// basic image stegno embedding

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

class BasicImageEncode	{

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
			f = new File("test.png");
			img = ImageIO.read(f);
		}
		catch (IOException e){
			System.out.print(e);
		}
		int height, width;
		height = img.getHeight();
		width = img.getWidth();
		BufferedImage imgTemp = new BufferedImage(8*width, 8*height, BufferedImage.TYPE_INT_ARGB);
		// BasicImageEncode dummy = new BasicImageEncode();
		// dummy.randomImage(imgTemp, 8*height, 8*width);
	
		int counter = 0;

		for (int y = 0; y < 8*height; y++)	{
			for (int x = 0; x < 8*width; x++)	{
				int p = img.getRGB(x/8,y/8);
				int alpha = (p >> 24) & 0xff;
				int shade = (p >> 16) & 0xff;
				int pTemp = imgTemp.getRGB(x,y);
				int shadeTemp = (pTemp >> 16) & 0xff;
				if ( (shade & (int)Math.pow(2,counter%8)) != 0 )	{
					shadeTemp = 1;
				}
				pTemp = (alpha << 24) | (0 << 16) | (0 << 8) | (shadeTemp);
				counter++;
				imgTemp.setRGB(x,y,pTemp);
			}
		}

		try{
			f = new File("stegoImage.png");
			ImageIO.write(imgTemp, "png", f);
		}
		catch (IOException e) {
			System.out.print(e);
		}

	}
}
