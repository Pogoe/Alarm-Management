package plc;

public interface IMessage
{
    // direction defination
    final byte TOPLC = 0;
    final byte FROMPLC = 1;

    //Protocol, Seriel starts with 0xff before Commmand
    final int COMMAND = 0;
    final int DIRECTION = 1;
    final int SERIAL_NO = 2;
    final int SIZE = 3;  // Size of data 
    // Timestamp to sec. precision
    final int YEAR = 4;
    final int MONTH = 5;
    final int DAY = 6;
    final int HOUR = 7;
    final int MINUTE = 8;
    final int SECOND = 9;
    // Data
    final int DATA_START = 10;
    final int MAX_DATA = 100 + DATA_START;
}
