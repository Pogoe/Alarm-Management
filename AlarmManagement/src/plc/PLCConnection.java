package plc;

/**
 * Methods to PLC communication
 *
 * @author Steffen Skov
 */
abstract public class PLCConnection
{
    protected Message mess;

    public abstract boolean send();

    /**
     * Add message
     *
     * @param m the message
     */
    public void addMessage(Message m)
    {
        mess = m;
    }
    
    public abstract String getIp();
}
