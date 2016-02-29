package plc;

public class Solution
{
    private int id;
    private int errorCode;
    private String description;
    
    public Solution(int id, int errorCode, String description)
    {
        this.id = id;
        this.errorCode = errorCode;
        this.description = description;
    }

    public int getId()
    {
        return id;
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    @Override
    public String toString()
    {
        return description;
    }
}
