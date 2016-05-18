package plc;

import java.util.Objects;

public class ErrorType
{
    private int errorCode;
    private String description;
    
    public ErrorType(int errorCode, String description)
    {
        this.errorCode = errorCode;
        this.description = description;
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public String getDescription()
    {
        return description;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.errorCode;
        hash = 97 * hash + Objects.hashCode(this.description);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ErrorType other = (ErrorType) obj;
        if (this.errorCode != other.errorCode) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString()
    {
        return "errorCode: " + errorCode + ", description: " + description;
    }
}
