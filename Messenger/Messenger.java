import java.*;
import java.io.*;
import java.net.*;

public class Messenger{

    private static String IP="182.64.74.181";
    private static int PORT=4242;
    private static InetAddress local;

    public static void main(String[] args) throws Exception{
        if(args.length<1){
            //Creates a client by default if no arguments specified
            System.out.println("Starting client");
            Client client=new Client(args.length>1?args[0]:IP);
        }else{
            if(args[0].toUpperCase().equals("SERVER")){
                System.out.println("Starting server");
                Server server=new Server();
            }else{
                System.out.println("Could not identify the call. Please write \"java Messenger SERVER\" to start a server.\nExiting... ");
            }
        }
    }

    public static class Server{
        Server() throws Exception{

            Thread t=new Thread(new Runnable(){

                @Override
                public void run(){
                    try{
                        ServerSocket server=new ServerSocket(PORT);
                        Socket s=server.accept();
                        System.out.println("Connected to "+s);

                        Thread t2=new Thread(new Runnable(){

                            @Override
                            public void run(){

                                try{
                                    DataOutputStream out=new DataOutputStream(s.getOutputStream());
                                    int s2;
                                    while(true){
                                        s2=System.in.read();
                                        out.writeUTF(Character.toString((char)(s2)));
                                    }
                                }catch(Exception e){

                                }
                            }
                                                
                        });
                        t2.start();

                        DataInputStream in = new DataInputStream(s.getInputStream());

                        while(true){
                            System.out.print(in.readUTF());
                        }

                    }catch(SocketException se){

                        System.out.println("Connection has been closed by the client");

                    }catch(Exception e){
                        System.out.println(e);
                        e.printStackTrace();
                    }
                }

            });

            t.start();
        }
    }

    public static class Client{
        Client(String address) throws Exception{
            
            local=InetAddress.getByName(address);
            System.out.println(local);

            Thread t=new Thread(new Runnable(){

                @Override
                public void run(){
                    try{
                        Socket s=new Socket(local.getHostAddress(), PORT);
                        DataOutputStream out=new DataOutputStream(s.getOutputStream());

                        DataInputStream in=new DataInputStream(s.getInputStream());

                        Thread t2=new Thread(new Runnable(){

                            @Override
                            public void run(){
                                try{
                                    while(true){
                                        System.out.print(in.readUTF());
                                    }
                                }catch(Exception e){

                                }
                            }

                        });
                        t2.start();

                        int s2;
                        while(true){
                            s2=System.in.read();
                            out.writeUTF(Character.toString((char)(s2)));
                        }
                    }catch(Exception e){
                        System.out.println(e);
                        e.printStackTrace();
                    }
                }

            });

            t.start();
        }
    }

}