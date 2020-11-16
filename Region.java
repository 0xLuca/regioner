import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class Region {
    private final double x1, z1;
    private final double x2, z2;

    private final double xLower, xHigher;
    private final double zLower, zHigher;

    public Region(double x1, double z1, double x2, double z2) {
        this.x1 = x1;
        this.z1 = z1;
        this.x2 = x2;
        this.z2 = z2;
        xLower = Math.min(x1, x2);
        xHigher = Math.max(x1, x2);
        zLower = Math.min(z1, z2);
        zHigher = Math.max(z1, z2);
    }

    public boolean isInRegion(double x, double z) {
        return x > xLower && x < xHigher && z > zLower && z < zHigher;
    }

    public boolean isInRegion(Location location) {
        return isInRegion(location.getX(), location.getZ());
    }

    public boolean isInRegion(Entity entity) {
        return isInRegion(entity.getLocation());
    }

    public Region(Location one, Location two) {
        this(one.getX(), one.getZ(), two.getX(), two.getZ());
    }

    @Override
    public String toString() {
        return "Region{" +
                "x1=" + x1 +
                ", z1=" + z1 +
                ", x2=" + x2 +
                ", z2=" + z2 +
                '}';
    }
}
