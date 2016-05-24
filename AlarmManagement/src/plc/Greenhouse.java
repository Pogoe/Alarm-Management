package plc;

import java.util.Objects;

public class Greenhouse implements IGreenhouse, ICommands
{
    private PLCConnection conn;
    private Message mess;

    /**
     * Create greenhouse API
     *
     * @param c connection
     */
    public Greenhouse(PLCConnection c)
    {
        this.conn = c;
    }

    public PLCConnection getConnection()
    {
        return conn;
    }
    
    /**
     * Setpoint for temperature inside Greenhouse CMD: 1
     *
     * @param kelvin : temperature in Kelvin (273 > T > 303)
     * @return true if processed
     */
    @Override
    public boolean SetTemperature(int kelvin)
    {
        mess = new Message(TEMP_SETPOINT);
        if (kelvin > 273 && kelvin < 303) // 0 - 30 grader celcius
        {
            System.out.println("Set temperatur setpoint to " + kelvin);
            mess.setData(kelvin - 273);
            return conn.send();
        }
        return false;
    }

    /**
     * Setpoint for moisture inside Greenhouse CMD:2
     *
     * @param moist in % ( 10 > M > 90 )
     * @return true if processed
     */
    @Override
    public boolean SetMoisture(int moist)
    {
        mess = new Message(TEMP_SETPOINT);
        if (moist > 10 && moist < 90)
        {
            mess.setData(moist);
            return conn.send();
        }
        return false;
    }

    /**
     * Setpoint for red light inside Greenhouse CMD:3
     *
     * @param level in percent
     * @return true if processed
     */
    @Override
    public boolean SetRedLight(int level)
    {
        System.out.println("Set red light to " + level);
        mess = new Message(REDLIGHT_SETPOINT);
        if (level >= 0 && level < 100)
        {
            mess.setData(level);
            conn.addMessage(mess);
            return conn.send();
        }
        return false;
    }

    /**
     * Setpoint for red light inside Greenhouse CMD: 4
     *
     * @param level in percent
     * @return true if processed
     */
    @Override
    public boolean SetBlueLight(int level)
    {
        mess = new Message(BLUELIGHT_SETPOINT);
        if (level >= 0 && level < 100)
        {
            mess.setData(level);
            return conn.send();
        }
        return false;
    }

    /**
     * Add water for some seconds. Pump is stopped if height of water is
     * exceeded CMD: 5
     *
     * @param sec : Second to turn on the pump
     * @return true if processed
     */
    @Override
    public boolean AddWater(int sec)
    {
        if (sec > 0 && sec < 120)
        {
            mess = new Message(ADDWATER);
            mess.setData(sec);
            return conn.send();
        }
        return false;
    }

    /**
     * Read temperature inside the Greenhouse CMD:8
     *
     * @return Temperature in Kelvin
     */
    @Override
    public double ReadTemp1()
    {
        System.out.println("Read greenhouse temperatur ");
        mess = new Message(READ_GREENHOUSE_TEMP);
        double temp = 0.0;
        mess.setData(); //None data
        conn.addMessage(mess);
        if (conn.send())
        {
            if (mess.getResultData() != null)
            {
                temp = (double) mess.getResultData()[0];
            }
            else
            {
                temp = 19.99; // return a dummy value
            }
        }
        System.out.println("Temperature is: " + temp);
        return temp + 273.0;
    }

    /**
     * Read temperature outside the Greenhouse CMD: 9
     *
     * @return Temperature in Kelvin
     */
    @Override
    public double ReadTemp2()
    {
        System.out.println("Read outdoor temperatur ");
        mess = new Message(READ_OUTDOOR_TEMP);
        double temp2 = 0.0;
        mess.setData(); //None data
        conn.addMessage(mess);
        if (conn.send())
        {
            if (mess.getResultData() != null)
            {
                temp2 = (double) (mess.getResultData())[0];
            }
            else
            {
                temp2 = 19.99; // return a dummy value
            }
        }
        System.out.println("Temperature is: " + temp2);
        return temp2 + 273.0;

    }

    /**
     * Read relative moisture inside the Greenhouse CMD: 10
     *
     * @return Moisture in %
     */
    @Override
    public double ReadMoist()
    {
        System.out.println("Read outdoor temperatur ");
        mess = new Message(READ_MOISTURE);
        double moist = 0.0;
        mess.setData(); //None data
        conn.addMessage(mess);
        if (conn.send())
        {
            if (mess.getResultData() != null)
            {
                moist = (double) mess.getResultData()[0];
            }
            else
            {
                moist = 1.0; // return a dummy value
            }                               
        }
        System.out.println("Moisture is: " + moist + " %");
        return moist;
    }

    /**
     * Read level of water in the Greenhouse CMD: 11
     *
     * @return Level in millimeter [0 < level < 250]
     */
    @Override
    public double ReadWaterLevel()
    {
        System.out.println("Read water level ");
        mess = new Message(READ_WATER_LEVEL);
        double level = 0.0; // level
        mess.setData(); //None data
        conn.addMessage(mess);
        if (conn.send())
        {
            if (mess.getResultData() != null)
            {
                level = (mess.getResultData())[0] * 10.0;
            }
            else
            {
                level = 1000.0; // return a dummy value
            }
        }
        System.out.println("Water level is: " + level);
        return level;
    }

    /**
     * NOT IMPLEMENTED Read highs of the plants CMD: 12
     *
     * @return Highs (cm?)
     */
    @Override
    public double ReadPlantHeight()
    {
        System.out.println("Read height of plants");
        mess = new Message(READ_PLANT_HEIGHT);
        double level = 0.0; // level
        mess.setData(); //None data
        conn.addMessage(mess);
        if (conn.send())
        {
            if (mess.getResultData() != null)
            {
                level = (mess.getResultData())[0];
            }
            else
            {
                level = 1000.0; // return a dummy value
            }
        }
        System.out.println("Plant height is: " + level);
        return level;
    }

    /**
     * Read all alarms one bits per alarm. CMD: 13
     *
     * @return Alarms as BitSet
     */
    @Override
    public byte[] ReadErrors()
    {
        System.out.println("Get all alarms ");
        mess = new Message(READ_ALL_ALARMS);
        mess.setData(); //None data
        conn.addMessage(mess);
        if (conn.send())
        {
            return mess.getResultData();
        }
        return mess.getResultData();
    }

    /**
     * Set Fane speed
     *
     * @param speed : {OFF (0), LOW (1), HIGH(2)};
     * @return Done
     */
    @Override
    public boolean SetFanSpeed(int speed)
    {
        System.out.println("Set fan speed " + speed);
        mess = new Message(SET_FAN_SPEED);
        if (speed >= 0 && speed <= 2)
        {
            mess.setData(speed);
            conn.addMessage(mess);
            return conn.send();
        }
        return false;
    }
    
    @Override
    public String toString()
    {
        return "Greenhouse at " + conn.getIp();
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.conn);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Greenhouse other = (Greenhouse) obj;
        if (!Objects.equals(this.conn, other.conn))
        {
            return false;
        }
        return true;
    }
    
}