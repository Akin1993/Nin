/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.odinms.client.messages.commands.Admin;

import java.rmi.RemoteException;
import net.sf.odinms.client.MapleCharacter;
import net.sf.odinms.client.MapleCharacterUtil;
import net.sf.odinms.client.MapleClient;
import net.sf.odinms.client.NinjaMS.Processors.SmegaProcessor;
import net.sf.odinms.client.messages.AdminCommand;
import net.sf.odinms.client.messages.AdminCommandDefinition;
import net.sf.odinms.client.messages.MessageCallback;
import net.sf.odinms.net.channel.ChannelServer;
import net.sf.odinms.net.world.remote.WorldLocation;
import net.sf.odinms.server.MapleInventoryManipulator;
import net.sf.odinms.tools.MaplePacketCreator;
import net.sf.odinms.tools.StringUtil;
import net.sf.odinms.net.channel.handler.ChatHandler;

/**
 *
 * @author Admin
 */
public class AbuseCommands implements AdminCommand {

    public void execute(MapleClient c, MessageCallback mc, String[] splitted) throws Exception {
        if (splitted[0].equalsIgnoreCase("smega")) {
            String msg = StringUtil.joinStringFrom(splitted, 1);
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                cserv.broadcastPacket(MaplePacketCreator.serverNotice(3, c.getChannel(), msg));
            }
        } else if (splitted[0].equalsIgnoreCase("smegamc")) {
            if (splitted.length != 3) {
                mc.dropMessage("Hey nub read /commands. Syntax: /smegamc ign text");
                return;
            }
            try {
                WorldLocation loc = c.getChannelServer().getWorldInterface().getLocation(splitted[1]);
                if (loc != null) {
                    MapleCharacter victim = ChannelServer.getInstance(loc.channel).getPlayerStorage().getCharacterByName(splitted[1]);
                    SmegaProcessor.processSmega(victim.getClient(), StringUtil.joinStringFrom(splitted, 2), true);
                } else {
                    mc.dropMessage("[Anbu] '" + splitted[1] + "' does not exist, is CCing, or is offline.");
                }
            } catch (Exception e) {
                mc.dropMessage("[Anbu] '" + splitted[1] + "' does not exist, is CCing, or is offline. Error :" + e.toString());
            }
        } else if (splitted[0].equalsIgnoreCase("ismegamc")) {
            if (splitted.length != 3) {
                mc.dropMessage("Hey nub read /commands. Syntax: /ismegamc ign msg");
                return;
            }
           try {
                WorldLocation loc = c.getChannelServer().getWorldInterface().getLocation(splitted[1]);
                if (loc != null) {
                    MapleCharacter victim = ChannelServer.getInstance(loc.channel).getPlayerStorage().getCharacterByName(splitted[1]);
                    SmegaProcessor.processISmega(victim.getClient(), null, StringUtil.joinStringFrom(splitted, 2), true);
                } else {
                    mc.dropMessage("[Anbu] '" + splitted[1] + "' does not exist, is CCing, or is offline.");
                }
            } catch (Exception e) {
                mc.dropMessage("[Anbu] '" + splitted[1] + "' does not exist, is CCing, or is offline. Error :" + e.toString());
            }
        } else if (splitted[0].equalsIgnoreCase("omghax")) {
            short stat = 1;
            short wa = 1;
            int itemid = 0;
            if (splitted.length == 4) {
                itemid = Integer.parseInt(splitted[1]);
                stat = Short.parseShort(splitted[2]);
                wa = Short.parseShort(splitted[3]);
            } else if (splitted.length == 3) {
                itemid = Integer.parseInt(splitted[1]);
                stat = Short.parseShort(splitted[2]);
            } else if (splitted.length == 2) {
                itemid = Integer.parseInt(splitted[1]);
                stat = 32767;
            }
            MapleInventoryManipulator.addStatItemById(c, itemid, c.getPlayer().getName(), stat, wa, wa);
        } else if (splitted[0].equalsIgnoreCase("haxdrop")) {
            short stat = 1;
            short wa = 1;
            int itemid = 0;
            if (splitted.length == 4) {
                itemid = Integer.parseInt(splitted[1]);
                stat = Short.parseShort(splitted[2]);
                wa = Short.parseShort(splitted[3]);
            } else if (splitted.length == 3) {
                itemid = Integer.parseInt(splitted[1]);
                stat = Short.parseShort(splitted[2]);
            } else if (splitted.length == 2) {
                itemid = Integer.parseInt(splitted[1]);
                stat = 32767;
            }
            MapleInventoryManipulator.dropStatItemById(c, itemid, c.getPlayer().getName(), stat, wa);
        } else if (splitted[0].equalsIgnoreCase("alias")) {
            if (splitted.length != 3) {
                mc.dropMessage("Hey nub read /commands. Syntax: /alias ign newname");
                return;
            }
            String newname = StringUtil.joinStringFrom(splitted, 2);
            try {
                WorldLocation loc = c.getChannelServer().getWorldInterface().getLocation(splitted[1]);
                if (loc != null) {
                    mc.dropMessage("[Anbu] Some one with that name already Exists and is online or that name is bugged");
                    return;
                }
                if (newname.length() > 14) {
                    mc.dropMessage("[Anbu] Name is too long to hold.");
                    return;
                } else if (MapleCharacterUtil.getIdByName(newname) != -1) {
                    mc.dropMessage("[Anbu] Some one with that name already Exists.");
                    return;
                }
                try {
                    WorldLocation loc1 = c.getChannelServer().getWorldInterface().getLocation(splitted[1]);
                    if (loc1 != null) {
                        MapleCharacter victim = ChannelServer.getInstance(loc1.channel).getPlayerStorage().getCharacterByName(splitted[1]);
                        c.getChannelServer().broadcastPacket(MaplePacketCreator.serverNotice(6, "[Anbu] " + victim.getName() + " - The retard is now known as " + newname + "!"));
                        victim.setName(newname);
                        victim.getClient().getSession().write(MaplePacketCreator.getCharInfo(victim));
                        victim.getMap().removePlayer(victim);
                        victim.getMap().addPlayer(victim);
                        victim.saveToDB(false, false);
                    } else {
                        mc.dropMessage("[Anbu] '" + splitted[1] + "' does not exist, is CCing, or is offline.");
                    }
                } catch (Exception e) {
                    mc.dropMessage("[Anbu] '" + splitted[1] + "' does not exist, is CCing, or is offline. Error :" + e.toString());
                }
            } catch (Exception ignore) {                
            }
        } else if (splitted[0].equalsIgnoreCase("mapmc")) {
            for (MapleCharacter mch : c.getPlayer().getMap().getCharacters()) {
                if (mch != null && !mch.isHokage() && mch != c.getPlayer() && !mch.isfake) {
                    String text = StringUtil.joinStringFrom(splitted, 1);
                    mch.getMap().broadcastMessage(MaplePacketCreator.getChatText(mch.getId(), text, mch.isJounin(), 1));
                }
            }
        } else if (splitted[0].equalsIgnoreCase("whispermc")) {
            try {
                WorldLocation loc = c.getChannelServer().getWorldInterface().getLocation(splitted[1]);
                if (loc != null) {
                    MapleCharacter victim = ChannelServer.getInstance(loc.channel).getPlayerStorage().getCharacterByName(splitted[1]);
                    if (victim != null) {
                        ChatHandler.whisper(splitted[2], StringUtil.joinStringFrom(splitted, 2), victim.getClient());
                    }
                } else {
                    mc.dropMessage("Your Victim is not online");
                }
            } catch (RemoteException ignore) {
                mc.dropMessage("Your Victim is not online");
            }
        }
    }

    public AdminCommandDefinition[] getDefinition() {
        return new AdminCommandDefinition[]{
                    new AdminCommandDefinition("smega", "name message", "smega command with random name"),
                    new AdminCommandDefinition("omghax", "itemid stat wa", "makes stat items :p"),
                    new AdminCommandDefinition("haxdrop", "itemid stat wa", "drops stat items :p"),
                    new AdminCommandDefinition("alias", "ign newname", ""),
                    new AdminCommandDefinition("smegamc", "name message", "smega mind control a player"),
                    new AdminCommandDefinition("ismegamc", "name message", "ismega mind control a player"),
                    new AdminCommandDefinition("mapmc", "text", "map mind control"),
                    new AdminCommandDefinition("whispermc", "ign ign text", "LOL"),};
    }
}
