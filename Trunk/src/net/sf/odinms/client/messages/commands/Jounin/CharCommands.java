/*
This file is part of the OdinMS Maple Story Server
Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
Matthias Butz <matze@odinms.de>
Jan Christian Meyer <vimes@odinms.de>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License version 3
as published by the Free Software Foundation. You may not use, modify
or distribute this program under any other version of the
GNU Affero General Public License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.odinms.client.messages.commands.Jounin;

import java.util.ArrayList;
import java.util.List;
import net.sf.odinms.client.Buffs.MapleStat;
import static net.sf.odinms.client.messages.CommandProcessor.getOptionalIntArg;
import net.sf.odinms.client.MapleCharacter;
import net.sf.odinms.client.MapleClient;
import net.sf.odinms.client.messages.GMCommand;
import net.sf.odinms.client.messages.GMCommandDefinition;
import net.sf.odinms.client.messages.IllegalCommandSyntaxException;
import net.sf.odinms.client.messages.MessageCallback;
import net.sf.odinms.client.messages.ServernoticeMapleClientMessageCallback;
import net.sf.odinms.net.channel.ChannelServer;
import net.sf.odinms.net.world.remote.WorldLocation;
import net.sf.odinms.server.MapleItemInformationProvider;
import net.sf.odinms.tools.MaplePacketCreator;
import net.sf.odinms.tools.Pair;

public class CharCommands implements GMCommand {

    @Override
    public GMCommandDefinition[] getDefinition() {
        return new GMCommandDefinition[]{
                    new GMCommandDefinition("lowhp", "", ""),
                    new GMCommandDefinition("heal", "", "heals you"),
                    //  new GMCommandDefinition("skill", "", ""),
                    new GMCommandDefinition("ap", "", ""),
                    new GMCommandDefinition("job", "", ""),
                    new GMCommandDefinition("whereami", "", ""),
                    new GMCommandDefinition("levelup", "", ""),
                    new GMCommandDefinition("level", "<ign> <level>", " ign is optional. if you don't know what it does, go bang a wall"),
                    new GMCommandDefinition("statreset", "", ""),
                    new GMCommandDefinition("mesos", "", ""),
                    new GMCommandDefinition("setstat", "<str/dex/luk/int/ap> <ign> <amount>", "sets "),
                    new GMCommandDefinition("maxstat", "", " maxxes all your stats"),
                    new GMCommandDefinition("setclan", "undecided/earth/wind/naruto/fire/lightning", " sets Clan")
                };
    }
    private MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();

    @Override
    public void execute(MapleClient c, MessageCallback mc, String[] splitted) throws Exception,
            IllegalCommandSyntaxException {

        MapleCharacter player = c.getPlayer();
        if (splitted[0].equals("lowhp")) {
            player.getStat().setHp(1);
            player.getStat().setMp(500);
            player.updateSingleStat(MapleStat.HP, 1);
            player.updateSingleStat(MapleStat.MP, 500);
        } else if (splitted[0].equals("heal")) {
            player.addMPHP(player.getStat().getMaxHp() - player.getStat().getHp(), player.getStat().getMaxMp() - player.getStat().getMp());
        } else if (splitted[0].equals("ap")) {
            player.setRemainingAp(getOptionalIntArg(splitted, 1, 1));
            player.updateSingleStat(MapleStat.AVAILABLEAP, player.getRemainingAp());
        } else if (splitted[0].equals("whereami")) {
            new ServernoticeMapleClientMessageCallback(c).dropMessage("You are on map "
                    + c.getPlayer().getMap().getId() + " - " + c.getPlayer().getMap().getMapName());
        } else if (splitted[0].equals("levelup")) {
            if (splitted.length < 2) {
                c.getPlayer().levelUp();
                c.getPlayer().setExp(0);
            } else if (splitted.length == 2) {
                byte quantity = Byte.parseByte(splitted[1]);
                c.getPlayer().setLevel(quantity);
                c.getPlayer().levelUp();
                int newexp = c.getPlayer().getExp();
                if (newexp < 0) {
                    c.getPlayer().gainExp(-newexp, false, false);
                }
            } else {
                mc.dropMessage("learn to read commands la nub. Syntax : !levelup <ign (optional)>");
            }
        } else if (splitted[0].equals("level")) {
            if (splitted.length == 3) {
                MapleCharacter noob = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
                if (noob == null) {
                    mc.dropMessage("player not in your channel");
                    return;
                }
                noob.setLevel(Byte.parseByte(splitted[2]));
                noob.levelUp();
                int newexp = noob.getExp();
                if (newexp < 0) {
                    noob.gainExp(-newexp, false, false);
                }
            } else if (splitted.length == 2) {
                byte quantity = Byte.parseByte(splitted[1]);
                c.getPlayer().setLevel(quantity);
                c.getPlayer().levelUp();
                int newexp = c.getPlayer().getExp();
                if (newexp < 0) {
                    c.getPlayer().gainExp(-newexp, false, false);
                }
            } else {
                c.getPlayer().levelUp();
                c.getPlayer().setExp(0);
            }
        } else if (splitted[0].equals("statreset")) {
            c.getPlayer().getStat().setAllStats(4);
            c.getPlayer().setRemainingAp(4);
            List<Pair<MapleStat, Integer>> stats = new ArrayList<Pair<MapleStat, Integer>>();
            stats.add(new Pair<MapleStat, Integer>(MapleStat.STR, 4));
            stats.add(new Pair<MapleStat, Integer>(MapleStat.DEX, 4));
            stats.add(new Pair<MapleStat, Integer>(MapleStat.INT, 4));
            stats.add(new Pair<MapleStat, Integer>(MapleStat.LUK, 4));
            stats.add(new Pair<MapleStat, Integer>(MapleStat.AVAILABLEAP, 4));
            c.getSession().write(MaplePacketCreator.updatePlayerStats(stats));
        } else if (splitted[0].equals("maxstat")) {
            int x = Short.MAX_VALUE;
            c.getPlayer().getStat().setAllStats(x);
            c.getPlayer().setRemainingAp(x);
            List<Pair<MapleStat, Integer>> stats = new ArrayList<Pair<MapleStat, Integer>>();
            stats.add(new Pair<MapleStat, Integer>(MapleStat.STR, x));
            stats.add(new Pair<MapleStat, Integer>(MapleStat.DEX, x));
            stats.add(new Pair<MapleStat, Integer>(MapleStat.INT, x));
            stats.add(new Pair<MapleStat, Integer>(MapleStat.LUK, x));
            stats.add(new Pair<MapleStat, Integer>(MapleStat.AVAILABLEAP, x));
            c.getSession().write(MaplePacketCreator.updatePlayerStats(stats));
        } else if (splitted[0].equalsIgnoreCase("job")) {
            if (splitted.length == 2) {
                int fuck = Integer.parseInt(splitted[1]);
                player.changeJobById(fuck);
            } else if (splitted.length == 3) {
                int fuck = Integer.parseInt(splitted[2]);
                MapleCharacter noob = ChannelServer.getInstance(c.getChannel()).getPlayerStorage().getCharacterByName(splitted[1]);
                noob.changeJobById(fuck);
            } else {
                mc.dropMessage("Syntax : !job <id> or !job <name> <id>");
            }
        } else if (splitted[0].equalsIgnoreCase("mesos")) {
            player.gainMeso(Integer.MAX_VALUE - player.getMeso(), true);
        } else if (splitted[0].equalsIgnoreCase("setstat")) {
            if (splitted.length < 4) {
                mc.dropMessage("Learn to read !commands");
                return;
            }
            MapleCharacter noob = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[2]);
            if (noob == null) {
                mc.dropMessage("The Player is not in your channel");
                return;
            }
            short x;
            try {
                x = Short.parseShort(splitted[3]);
            } catch (NumberFormatException numberFormatException) {
                mc.dropMessage("You have entered a invalid number and so the stat will be set as 4");
                x = (short) 4;
            }
            if (splitted[1].equalsIgnoreCase("str")) {
                noob.getStat().setStr(x);
                noob.updateSingleStat(MapleStat.STR, x);
            } else if (splitted[1].equalsIgnoreCase("dex")) {
                noob.getStat().setDex(x);
                noob.updateSingleStat(MapleStat.DEX, x);
            } else if (splitted[1].equalsIgnoreCase("luk")) {
                noob.getStat().setLuk(x);
                noob.updateSingleStat(MapleStat.LUK, x);
            } else if (splitted[1].equalsIgnoreCase("int")) {
                noob.getStat().setInt(x);
                noob.updateSingleStat(MapleStat.INT, x);
            } else if (splitted[1].equalsIgnoreCase("ap")) {
                noob.setRemainingAp(x);
                noob.updateSingleStat(MapleStat.AVAILABLEAP, x);
            } else {
                mc.dropMessage("Learn to read !commands");
                return;
            }
            mc.dropMessage("Command Executed");
        } 
    }
}
