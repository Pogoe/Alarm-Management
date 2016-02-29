package plc;

public class TestGreenhouse
{
    public static void main(String[] args)
    {
        //PLCConnection con = new UDPConnection(1025, "localhost");
        PLCConnection con = new UDPConnection(5000, "192.168.0.10");
        //PLCConnection con = new SerialConnection("COM4");
        //SerialConnection.getPortList("COM1");

        IGreenhouse api = new Greenhouse(con);
        api.SetBlueLight(50);
        api.SetFanSpeed(0);
        while(true)
        {
            System.out.println(api.ReadTemp1());
        }
    }
}