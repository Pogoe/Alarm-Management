package plc;

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
    double ReadTemp1();
    double ReadTemp2();
    double ReadMoist();
    double ReadWaterLevel();
    double ReadPlantHeight();
    byte[] ReadErrors();
    boolean SetFanSpeed(int speed);
}
