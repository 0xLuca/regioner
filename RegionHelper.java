import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RegionHelper {
    private static final List<Region> regions = new ArrayList<>();

    public static List<Region> getRegions() {
        return regions;
    }

    public static Region addRegion(Region region) {
        regions.add(region);
        return region;
    }

    public static Region addRegion(double x1, double z1, double x2, double z2) {
        return addRegion(new Region(x1, z1, x2, z2));
    }

    public static void setup(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onMove(PlayerMoveEvent event) {
                Player player = event.getPlayer();
                Location from = event.getFrom();
                Location to = event.getTo();
                List<Region> regionsInBefore = getRegionsIn(from);
                List<Region> regionsInAfter = getRegionsIn(to);
                List<Region> regionsLeft = regionsInBefore.stream().filter(region -> !regionsInAfter.contains(region)).collect(Collectors.toList());
                List<Region> regionsEntered = regionsInAfter.stream().filter(region -> !regionsInBefore.contains(region)).collect(Collectors.toList());
                boolean cancelled = false;
                for (Region region : regionsLeft) {
                    RegionLeaveEvent regionLeaveEvent = new RegionLeaveEvent(player, region);
                    Bukkit.getPluginManager().callEvent(regionLeaveEvent);
                    if (regionLeaveEvent.isCancelled()) {
                        cancelled = true;
                    }
                }
                for (Region region : regionsEntered) {
                    RegionEnterEvent regionEnterEvent = new RegionEnterEvent(player, region);
                    Bukkit.getPluginManager().callEvent(regionEnterEvent);
                    if (regionEnterEvent.isCancelled()) {
                        cancelled = true;
                    }
                }
                if (cancelled) {
                    event.setCancelled(true);
                    Vector direction = player.getLocation().getDirection();
                    player.setVelocity(direction.multiply(-0.75).setY(0.5F));
                    player.sendMessage("§cDu darfst diesen Bereich hier nicht verlassen!");
                    player.playSound(player.getLocation(), Sound.ENTITY_ARMORSTAND_HIT, 1.0f, 1.0f);
                }
            }
        }, plugin);
    }

    public static List<Region> getRegionsIn(Location location) {
        return regions.stream().filter(region -> region.isInRegion(location)).collect(Collectors.toList());
    }

    public static List<Region> getRegionsIn(Entity entity) {
        return getRegionsIn(entity.getLocation());
    }
}
