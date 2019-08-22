import encrypt.BlockRandomisedEncryption;
import decrypt.BlockRandomisedDecryption;
import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.lang.System;

public class Client	{
	public void clientFunc(String addr, int port)	{
		Scanner sc = new Scanner(System.in);
		System.out.println("S -> send & R -> receive");
		char choice = sc.next().charAt(0);
		int[] key = new int[]{170,45,212,195,139};
		switch (choice)	{
			case 'S':
			try{
				System.out.println("Enter image name with location : ");
				String imgName = sc.next();
				BlockRandomisedEncryption enc = new BlockRandomisedEncryption();
				imgName = enc.Encrypt(imgName, key);
			
				Socket socket = new Socket(addr,port);
				System.out.print("connected with socket ID : "+ socket);
				FileInputStream f = new FileInputStream(imgName);
				DataOutputStream output = new DataOutputStream(socket.getOutputStream());
				int i;
				while((i = f.read()) > -1)	{
					output.write(i);
				}
				output.close();
				f.close();
				File file = new File(imgName);
				file.delete();
				socket.close();
			}
			catch(IOException e)	{
				System.out.print(e);
			}
			break;

			case 'R':
			try{
				Socket socket = new Socket(addr,port);
				System.out.print("connnected with socket ID : "+ socket);
				FileOutputStream f = new FileOutputStream("clientImage.png");
				DataInputStream input = new DataInputStream(socket.getInputStream());
				int i;
				while((i = input.read()) > -1)	{
					f.write(i);
				}
				Boolean flag = true;
				System.out.println("You have the image, provide the key in order to decrypt the image");
				//decrytion-to-be-added
				System.out.print("Input number of key terms : ");
				int keyLen = sc.nextInt();
				if (keyLen != key.length)	{
					System.out.print("Incorrect input");
					File file = new File("clientImage.png");
					file.delete();
					System.exit(0);
				}
				System.out.print("Enter subsequent elements : ");
				int[] keyAr = new int[keyLen];
				for (int j = 0; j < keyLen; j++)	{
					keyAr[j] = sc.nextInt();
					if (keyAr[j] != key[j])	
						flag = false;
				}
				if (flag == true)	{
					BlockRandomisedDecryption dec = new BlockRandomisedDecryption();
					dec.Decrypt("clientImage.png",keyAr);
				}
				else
				{
					System.out.print("Incorrect Input");
					File file = new File("clientImage.png");
					file.delete();
					System.exit(0);
				}

				f.close();
				File file = new File("clientImage.png");
				file.delete();
				input.close();
				socket.close();
			}
			catch(IOException e)	{
				System.out.print(e);
			} 
		}
	}

	public static void main(String args[])	{
		Client obj = new Client();
		obj.clientFunc("127.0.0.1",5000);
	}
}