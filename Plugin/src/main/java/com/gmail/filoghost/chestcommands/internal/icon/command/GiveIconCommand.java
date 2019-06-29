/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.gmail.filoghost.chestcommands.internal.icon.command;

import co.aikar.taskchain.TaskChain;
import com.gmail.filoghost.chestcommands.exception.FormatException;
import com.gmail.filoghost.chestcommands.internal.icon.IconCommand;
import com.gmail.filoghost.chestcommands.util.ItemStackReader;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveIconCommand extends IconCommand {

  private ItemStack itemToGive;
  private String errorMessage;

  public GiveIconCommand(String command) {
    super(command);
    if (!hasVariables) {
      parseItem(super.command);
    }
  }

  private void parseItem(String command) {
    try {
      ItemStackReader reader = new ItemStackReader(command, true);
      itemToGive = reader.createStack();
      errorMessage = null;
    } catch (FormatException e) {
      errorMessage = ChatColor.RED + "Invalid item to give: " + e.getMessage();
    }
  }

  @Override
  public void execute(Player player, TaskChain taskChain) {
    taskChain.sync(() -> {
      if (hasVariables) {
        parseItem(getParsedCommand(player));
      }
      if (errorMessage != null) {
        player.sendMessage(errorMessage);
        TaskChain.abort();
      }

      player.getInventory().addItem(itemToGive.clone());
    });
  }

}
