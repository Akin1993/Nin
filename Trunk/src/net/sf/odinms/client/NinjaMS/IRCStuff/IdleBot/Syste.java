/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.odinms.client.NinjaMS.IRCStuff.IdleBot;

import java.io.IOException;
import net.sf.odinms.server.TimerManager;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

/**
 *
 * @author Admin
 */
public class Syste extends PircBot {

   /*
     * To change this template, choose Tools | Templates
     * and open the template in the editor.
     */
/*
    private String channel = "#ninjams";
    private static Syste instance = new Syste();

    public static Syste getInstance() {
        return instance;
    }

    public Syste() {
        try {
            this.setLogin("System2");
            this.setName("System2");
            this.setAutoNickChange(true);
            this.connect("irc.vbirc.com");
            this.identify("123123");
            this.joinChannel(channel);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (IrcException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onDisconnect() {
        //reconnect
        this.reconnectIRC();
    }

    public void reconnectIRC() {
        if (!isConnected()) {
            try {
                reconnect();
            } catch (Exception e) {
                e.printStackTrace();
                TimerManager.getInstance().schedule(new Runnable() {

                    @Override
                    public void run() {
                        reconnectIRC();
                    }
                }, 5000);
            }
        }
    }

    @Override
    public void onKick(String channel, String kickerNick, String kickerLogin,
            String kickerHostname, String recipientNick, String reason) {
        //Auto Rejoin if Kicked
        if (recipientNick.equalsIgnoreCase(getNick())) {
            joinChannel(channel);
        }
    }*/
}

