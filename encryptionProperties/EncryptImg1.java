import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class EncryptImg1
{
	//function to detect top 4 populated shades of grey
	static int[] top4(int shade[])	{
		int[] max = new int[4];
		int[] maxI = new int[4];
		for (int i = 0; i < 256; i++)	{
			if (max[0] < shade[i])	{
				max[0] = shade[i];
				maxI[0] = i;
			}
		}
		for (int i = 0; i < 256; i++)	{
			if(shade[i] < max[0] && max[1] < shade[i] )	{
				max[1] = shade[i];
				maxI[1] = i;
			}

		}
		for (int i = 0; i < 256; i++)	{
			if(shade[i] < max[1] && max[2] < shade[i])	{
				max[2] = shade[i];
				maxI[2] = i;
			}
		}
		for (int i = 0; i < 256; i++)	{
			if(shade[i] < max[2] && max[3] < shade[i])	{
				max[3] = shade[i];
				maxI[3] = i;
			}
		}
		return maxI;
	}

	public static void main(String args[]) throws IOException{
		int height,width;
		BufferedImage img = null;
		File fkey,f,fx,fy,fz,f0 = null;
		try
		{
			f = new File("babbage.jpg");
			img = ImageIO.read(f);
		}
		catch(IOException e)
		{
			System.out.print(e);
		}
		width = img.getWidth();
		height = img.getHeight();
		int [][]position = new int[width][height];
		int []shade = new int[256];
		for (int i = 0; i < height; i++)	{
			for(int j = 0; j < width; j++)	{
				position[j][i] = img.getRGB(j,i) & 0xff;
				shade[img.getRGB(j,i)&0xff]++;
			}
		}

		//creation of required images by initalising them to default image
		f = new File("gX.jpg");
		ImageIO.write(img,"jpg",f);
		f = new File("gY.jpg");
		ImageIO.write(img,"jpg",f);
		f = new File("gZ.jpg");
		ImageIO.write(img,"jpg",f);
		f = new File("gMisc.jpg");
		ImageIO.write(img,"jpg",f);
		f = new File("gKey.jpg");
		ImageIO.write(img,"jpg",f);

		//declaring image pointers
		BufferedImage imgx,key = null;
		BufferedImage imgy = null;
		BufferedImage imgz = null;
		BufferedImage img0 = null;
		fx = new File("gX.jpg");
		imgx = ImageIO.read(fx);
		fy = new File("gY.jpg");
		imgy = ImageIO.read(fy);
		fz = new File("gZ.jpg");
		imgz = ImageIO.read(fz);
		fkey = new File("gKey.jpg");
		key = ImageIO.read(fkey);
		f0 = new File("gMisc.jpg");
		img0 = ImageIO.read(f0);

		int []maxI = top4(shade);
		
		//setting images to a default value
		for(int i = 0; i < height; i++)	{
			for (int j = 0; j < width; j++)	{
				int p = (1<<24) | (1<<16) | (1<<8) | 1;
				imgx.setRGB(j,i,p);
				imgy.setRGB(j,i,p);
				imgz.setRGB(j,i,p);
				img0.setRGB(j,i,p);
				key.setRGB(j,i,p);
			}
		}

		//plotting the equivalued tones to seperate images
		for (int i = 0; i < height; i++)	{
			for(int j = 0; j < width; j++)	{
				
				if(position[j][i] == maxI[0]){
					int p = img.getRGB(j,i);
					int a = (p>>24) & 0xff;
					int r = (p>>16) & 0xff;
					p = (a<<24) | (r<<16) | (r<<8) | r;
					imgx.setRGB(j,i,p);
				}
				
				else if(position[j][i] == maxI[1]){
					int p = img.getRGB(j,i);
					int a = (p>>24) & 0xff;
					int r = (p>>16) & 0xff;
					p = (a<<24) | (r<<16) | (r<<8) | r;
					imgy.setRGB(j,i,p);
				}
				
				else if(position[j][i] == maxI[2])	{
					int p = img.getRGB(j,i);
					int a = (p>>24) & 0xff;
					int r = (p>>16) & 0xff;
					p = (a<<24) | (r<<16) | (r<<8) | r;
					imgz.setRGB(j,i,p);
				}
				
				else if(position[j][i] == maxI[3])	{
					int p = img.getRGB(j,i);
					int a = (p>>24) & 0xff;
					int r = (p>>16) & 0xff;
					p = (a<<24) | (r<<16) | (r<<8) | r;
					key.setRGB(j,i,p);
				}
				
				else{
					int p = img.getRGB(j,i);
					int a = (p>>24) & 0xff;
					int r = (p>>16) & 0xff;
					p = (a<<24) | (r<<16) | (r<<8) | r;
					img0.setRGB(j,i,p);
				}
			}
		}

		// xor of every bit of key with every bit of imgx, imgy, imgz and img0
		for (int i = 0; i< height; i++)	{
			for(int j = 0; j < width; j++)	{
				
				int p = key.getRGB(j,i);
				int a = (p>>24) & 0xff;
				int r = (p>>16) & 0xff;
				
				int px = imgx.getRGB(j,i);
				int ax = (px>>24) & 0xff;
				int rx = (px>>16) & 0xff;
				px = ((a^ax)<<24) | ((r^rx)<<16) | ((r^rx)<<8) | (r^rx);
				imgx.setRGB(j,i,px);
				
				int py = imgy.getRGB(j,i);
				int ay = (py>>24) & 0xff;
				int ry = (py>>16) & 0xff;
				py = ((a^ay)<<24) | ((r^ry)<<16) | ((r^ry)<<8) | (r^ry);
				imgy.setRGB(j,i,py);
				
				int pz = imgz.getRGB(j,i);
				int az = (pz>>24) & 0xff;
				int rz = (pz>>16) & 0xff;
				pz = ((a^az)<<24) | ((r^rz)<<16) | ((r^rz)<<8) | (r^rz);
				imgz.setRGB(j,i,pz);
				
				int p0 = img0.getRGB(j,i);
				int a0 = (p0>>24) & 0xff;
				int r0 = (p0>>16) & 0xff;
				p0 = ((a^a0)<<24) | ((r^r0)<<16) | ((r^r0)<<8) | (r^r0);
				img0.setRGB(j,i,p0);

			}
		}

		//writing the output to the image files
		f = new File("gX.jpg");
		ImageIO.write(imgx,"jpg",f);
		f = new File("gY.jpg");
		ImageIO.write(imgy,"jpg",f);
		f = new File("gZ.jpg");
		ImageIO.write(imgz,"jpg",f);
		f = new File("gMisc.jpg");
		ImageIO.write(img0,"jpg",f);
		f = new File("gKey.jpg");
		ImageIO.write(key,"jpg",f);
	}
}