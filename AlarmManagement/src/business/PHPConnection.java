package business;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class PHPConnection
{
    private Socket socket;
    private BufferedReader input;
    private BufferedWriter output;
    
    public PHPConnection() throws IOException
    {
        socket = new Socket(InetAddress.getByName("localhost"), 12345);
        output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    
    public BufferedReader getInputStream()
    {
        return input;
    }
    
    public BufferedWriter getOutputStream()
    {
        return output;
    }
}
