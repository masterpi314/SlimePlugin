package org.masterpi.mc.slimeplugin;

/*
    This file is part of SlimePlugin

    SlimePlugin is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    SlimePlugin is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with SlimePlugin.  If not, see <http://www.gnu.org/licenses/>.
 */

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.ChatColor;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.logging.Logger;

public class SlimePluginCompassCommandExecutor implements CommandExecutor {

    private SlimePlugin plugin;
    Logger log = Logger.getLogger("Minecraft");//Define your logger

    public SlimePluginCompassCommandExecutor(SlimePlugin plugin) {
        this.plugin = plugin;
    }

    private static String METADATA_KEY="SlimePluginCompassPrevLoc";

    @Override
    public boolean onCommand(CommandSender sender, Command command,
            String label, String[] args) {

        if (command.getName().equalsIgnoreCase("slimecompass")) {
            if(sender instanceof Player ) {
                Player player = (Player) sender;
                if (args.length==1 && args[0].equalsIgnoreCase("off")) {
                    if (hasPrevLoc(player)) {
                        Location prevLocation = getPrevLoc(player);
                        player.setCompassTarget(prevLocation);
                        removePrevLoc(player);
                        player.sendMessage("Slime Compass turned off");
                    } else {
                        player.sendMessage("Slime Compass was not on");
                    }
                } else {
                    Location location = player.getLocation();
                    Location nearestSlimeChunk = chunkCenter(
                        location.getWorld(),
                        SlimeChunk.nearestSlimeChunk(
                            location.getWorld().getSeed(),
                            location.getX(),
                            location.getZ()));

                    // If we haven't stored the compass location before, do so
                    if(!hasPrevLoc(player)) {
                        setPrevLoc(player,player.getCompassTarget());
                    }

                    player.setCompassTarget(nearestSlimeChunk);

                    player.sendMessage(ChatColor.GREEN
                        +"Compass set to center of nearest slime chunk"
                        +ChatColor.RESET);
                }
            }
            else {
                sender.sendMessage("Only Players can use this command.");
            }
            return true;
        }
        return false;
    }

    private static Location chunkCenter(World world, SlimeChunk.
            ChunkCoords coords) {

        long x = coords.getX();
        long z = coords.getZ();

        return new Location(world,x*16+8,0,z*16+8);

    }

    private boolean hasPrevLoc(Player player) {
        if (!player.hasMetadata(METADATA_KEY)) return false;
        for (MetadataValue v: player.getMetadata(METADATA_KEY)) {
            if (v.getOwningPlugin().equals(plugin)) {
                return true;
            }
        }
        return false;
    }

    private void setPrevLoc(Player player, Location location) {
        player.setMetadata(METADATA_KEY,
                new FixedMetadataValue(plugin,location));
    }

    private Location getPrevLoc(Player player) {
        for (MetadataValue v: player.getMetadata(METADATA_KEY)) {
            if (v.getOwningPlugin().equals(plugin)) {
                return (Location)v.value();
            }
        }
        return null;
    }

    private void removePrevLoc(Player player) {
        player.removeMetadata(METADATA_KEY, plugin);
    }
}
