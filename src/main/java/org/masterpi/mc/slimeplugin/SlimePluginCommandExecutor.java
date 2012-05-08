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
import org.bukkit.ChatColor;

import java.util.logging.Logger;

public class SlimePluginCommandExecutor implements CommandExecutor {

    private SlimePlugin plugin;
    Logger log = Logger.getLogger("Minecraft");//Define your logger

    public SlimePluginCommandExecutor(SlimePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command,
            String label, String[] args) {

        if (command.getName().equalsIgnoreCase("slime")) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                Location location = player.getLocation();
                if (SlimeChunk.isSlimeChunk(
                        location.getWorld().getSeed(),
                        location.getX(),
                        location.getZ())) {
                    sender.sendMessage(ChatColor.GREEN+"You are in a slime chunk"+ChatColor.RESET);
                } else {
                    sender.sendMessage("You are not in a slime chunk");
                }
            }
            else {
                sender.sendMessage("You have no location!");
            }
            return true;
        }
        return false;
    }
}
