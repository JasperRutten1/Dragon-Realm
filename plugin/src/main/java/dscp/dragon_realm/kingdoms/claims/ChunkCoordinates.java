package dscp.dragon_realm.kingdoms.claims;

import java.io.Serializable;
import java.util.Objects;

public class ChunkCoordinates implements Serializable {
    private static final long serialVersionUID = 6874426757612792539L;

    private double x;
    private double z;

    public ChunkCoordinates(double x, double z){
        this.x = x;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkCoordinates that = (ChunkCoordinates) o;
        return Double.compare(that.x, x) == 0 &&
                Double.compare(that.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }
}
