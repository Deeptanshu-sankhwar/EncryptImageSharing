import java.io.*;
import java.net.*;

public class Server {
    public void serverFunc(int port)    {
        try{
            ServerSocket server = new ServerSocket(port);
            System.out.println("server-began");
            int k = 2;
            DataInputStream input = null;
            FileOutputStream f = null;
            FileInputStream f1 = null;
            Socket socket = null;
            Socket socket2 = null;
            DataOutputStream output = null;

                socket = server.accept();
                System.out.println("Client accepted with socked ID : "+ socket);
                input = new DataInputStream(socket.getInputStream());   //coming image
                f= new FileOutputStream("serverImage.png");
                int i;
                while((i = input.read()) > -1)  {
                    f.write(i);
                }
                socket2 = server.accept();
                System.out.println("Client accepted with socked ID : "+ socket);
                f1 = new FileInputStream("serverImage.png");
                output = new DataOutputStream(socket2.getOutputStream());
                int j;
                while((j = f1.read()) > -1)   {
                    output.write(j);
                }
            File file = new File("serverImage.png");
            file.delete();
            output.close();
            input.close();
            f.close();
            f1.close();
            socket.close();
            socket2.close();
        }   
        catch (IOException e)   {
            System.out.print(e);
        }
    }

    public static void main(String args[])  {
        Server obj = new Server();
        obj.serverFunc(5000);
    }
}