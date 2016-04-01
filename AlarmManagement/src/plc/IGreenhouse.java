package plc;

import java.util.BitSet;

public interface IGreenhouse
{
    final byte ON = 1;
    final byte OFF = 0;

    final byte LOW = 1;
    final byte HIGH = 2;

    boolean SetTemperature(int kelvin);

    boolean SetMoisture(int moist);

    boolean SetRedLight(int level);

    boolean SetBlueLight(int level);

    boolean AddWater(int sec);

    boolean AddFertiliser(int sec);

    boolean AddCO2(int sec);

    double ReadTemp1();

    double ReadTemp2();

    double ReadMoist();

    double ReadWaterLevel();

    double ReadPlantHeight();

    BitSet ReadErrors();

    boolean ResetError(int errorNum);

    byte[] GetStatus();

    boolean SetFanSpeed(int speed);
}
