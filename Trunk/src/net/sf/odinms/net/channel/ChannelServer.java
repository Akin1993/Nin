/*
This file is part of the OdinMS Maple Story Server
Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc>
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
package net.sf.odinms.net.channel;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.rmi.ssl.SslRMIClientSocketFactory;

import net.sf.odinms.client.MapleCharacter;
import net.sf.odinms.client.NinjaMS.IRCStuff.IdleBot.Pandora;
import net.sf.odinms.client.NinjaMS.IRCStuff.MainIRC;
import net.sf.odinms.client.NinjaMS.IRCStuff.RPG;
import net.sf.odinms.client.Skills.SkillFactory;
import net.sf.odinms.database.DatabaseConnection;
import net.sf.odinms.net.MaplePacket;
import net.sf.odinms.net.MapleServerHandler;
import net.sf.odinms.net.ServerConstants;
import net.sf.odinms.net.ServerType;
import net.sf.odinms.net.channel.remote.ChannelWorldInterface;
import net.sf.odinms.net.mina.MapleCodecFactory;
import net.sf.odinms.net.world.MapleParty;
import net.sf.odinms.net.world.MaplePartyCharacter;
import net.sf.odinms.net.world.guild.MapleGuild;
import net.sf.odinms.net.world.guild.MapleGuildCharacter;
import net.sf.odinms.net.world.guild.MapleGuildSummary;
import net.sf.odinms.net.world.remote.WorldChannelInterface;
import net.sf.odinms.net.world.remote.WorldRegistry;
import net.sf.odinms.scripting.event.EventScriptManager;
import net.sf.odinms.server.AutobanManager;
import net.sf.odinms.server.ItemMakerFactory;
import net.sf.odinms.server.MapleItemInformationProvider;
import net.sf.odinms.server.MapleSquad;
import net.sf.odinms.server.ShutdownServer;
import net.sf.odinms.server.TimerManager;
import net.sf.odinms.server.constants.RandomRewards;
import net.sf.odinms.server.maps.MapTimer;
import net.sf.odinms.server.maps.MapleMapFactory;
import net.sf.odinms.server.shops.HiredMerchant;
import net.sf.odinms.tools.MaplePacketCreator;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.SimpleByteBufferAllocator;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptorConfig;
import org.jibble.pircbot.Colors;

public class ChannelServer {

    private byte expRate, mesoRate, dropRate;
    private short port = 7575;
    private int channel, running_MerchantID = 0;
    private String serverMessage, key, ip;
    private Boolean worldReady = true;
    private boolean shutdown = false, finishedShutdown = false, MegaphoneMuteState = false;
    private static InetSocketAddress InetSocketadd;
    private static Properties initialProp;
    private static WorldRegistry worldRegistry;
    private PlayerStorage players;
    private Properties props = new Properties();
    private ChannelWorldInterface cwi;
    private WorldChannelInterface wci = null;
    private IoAcceptor acceptor;
    private final MapleMapFactory mapFactory = new MapleMapFactory();
    private EventScriptManager eventSM;
    private static final Map<Integer, ChannelServer> instances = new HashMap<Integer, ChannelServer>();
    private static final Map<String, ChannelServer> pendingInstances = new HashMap<String, ChannelServer>();
    private final Map<Integer, MapleGuildSummary> gsStore = new HashMap<Integer, MapleGuildSummary>();
    private final Map<String, MapleSquad> mapleSquads = new HashMap<String, MapleSquad>();
    private final Map<Integer, HiredMerchant> merchants = new HashMap<Integer, HiredMerchant>();
    private final Lock merchant_mutex = new ReentrantLock();

    private ChannelServer(final String key) {
        this.key = key;
    }

    public static final WorldRegistry getWorldRegistry() {
        return worldRegistry;
    }

    public final void reconnectWorld() {
        // check if the connection is really gone
        try {
            wci.isAvailable();
        } catch (RemoteException ex) {
            synchronized (worldReady) {
                worldReady = false;
            }
            synchronized (cwi) {
                synchronized (worldReady) {
                    if (worldReady) {
                        return;
                    }
                }
                System.out.println("Reconnecting to world server");
                synchronized (wci) {
                    // completely re-establish the rmi connection
                    try {
                        initialProp = new Properties();
                        FileReader fr = new FileReader(System.getProperty("net.sf.odinms.channel.config"));
                        initialProp.load(fr);
                        fr.close();
                        final Registry registry = LocateRegistry.getRegistry(initialProp.getProperty("net.sf.odinms.world.host"),
                                Registry.REGISTRY_PORT, new SslRMIClientSocketFactory());
                        worldRegistry = (WorldRegistry) registry.lookup("WorldRegistry");
                        cwi = new ChannelWorldInterfaceImpl(this);
                        wci = worldRegistry.registerChannelServer(key, cwi);
                        props = wci.getGameProperties();
                        expRate = Byte.parseByte(props.getProperty("net.sf.odinms.world.exp"));
                        mesoRate = Byte.parseByte(props.getProperty("net.sf.odinms.world.meso"));
                        dropRate = Byte.parseByte(props.getProperty("net.sf.odinms.world.drop"));
                        serverMessage = props.getProperty("net.sf.odinms.world.serverMessage");
                        Properties dbProp = new Properties();
                        fr = new FileReader("db.properties");
                        dbProp.load(fr);
                        fr.close();
                        DatabaseConnection.setProps(dbProp);
                        DatabaseConnection.getConnection();
                        wci.serverReady();
                    } catch (Exception e) {
                        System.err.println("Reconnecting failed" + e);
                    }
                    worldReady = true;
                }
            }
            synchronized (worldReady) {
                worldReady.notifyAll();
            }
        }
    }

    public final void run_startup_configurations() {
        try {
            cwi = new ChannelWorldInterfaceImpl(this);
            wci = worldRegistry.registerChannelServer(key, cwi);
            props = wci.getGameProperties();
            expRate = Byte.parseByte(props.getProperty("net.sf.odinms.world.exp"));
            mesoRate = Byte.parseByte(props.getProperty("net.sf.odinms.world.meso"));
            dropRate = Byte.parseByte(props.getProperty("net.sf.odinms.world.drop"));
            serverMessage = props.getProperty("net.sf.odinms.world.serverMessage");
            eventSM = new EventScriptManager(this, props.getProperty("net.sf.odinms.channel.events").split(","));
            final Properties dbProp = new Properties();
            final FileReader fileReader = new FileReader("db.properties");
            dbProp.load(fileReader);
            fileReader.close();
            DatabaseConnection.setProps(dbProp);
            DatabaseConnection.getConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        port = Short.parseShort(props.getProperty("net.sf.odinms.channel.net.port"));
        ip = props.getProperty("net.sf.odinms.channel.net.interface") + ":" + port;

        ByteBuffer.setUseDirectBuffers(false);
        ByteBuffer.setAllocator(new SimpleByteBufferAllocator());

        acceptor = new SocketAcceptor();
        final SocketAcceptorConfig acceptor_config = new SocketAcceptorConfig();
        acceptor_config.getSessionConfig().setTcpNoDelay(true);
        acceptor_config.setDisconnectOnUnbind(true);
        acceptor_config.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MapleCodecFactory()));
        TimerManager.getInstance().start();
        TimerManager.getInstance().register(AutobanManager.getInstance(), 60000);
        MapTimer.getInstance().start();
        ItemMakerFactory.getInstance();
        MapleItemInformationProvider.getInstance(); // Load\
        RandomRewards.getInstance();
        SkillFactory.getSkill(99999999); // Load
        IRCLoad();
        players = new PlayerStorage();
        try {
            final MapleServerHandler serverHandler = new MapleServerHandler(ServerType.CHANNEL, channel);
            InetSocketadd = new InetSocketAddress(port);
            acceptor.bind(InetSocketadd, serverHandler, acceptor_config);
            System.out.println("Channel " + getChannel() + ": Listening on port " + port + "");
            wci.serverReady();
            eventSM.init();
        } catch (IOException e) {
            System.out.println("Binding to port " + port + " failed (ch: " + getChannel() + ")" + e);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(new ShutDownListener()));
    }

//    public void startSpawnTask(TimerManager m) {
//	m.register(new respawnMaps(), 10000);
//    }

    /*    private class respawnMaps implements Runnable {

    @Override
    public void run() {
    for (Entry<Integer, MapleMap> map : mapFactory.getMaps().entrySet()) {
    map.getValue().respawn(false);
    }
    for (Entry<Integer, MapleMap> map : mapFactory.getInstanceMaps().entrySet()) {
    map.getValue().respawn(false);
    }
    }
    }*/
    public final void shutdown() {
        // dc all clients by hand so we get sessionClosed...
        shutdown = true;

        System.out.println("Channel " + channel + ", Saving hired merchants...");

        closeAllMerchant();

        System.out.println("Channel " + channel + ", Saving characters...");

        players.disconnectAll();

        System.out.println("Channel " + channel + ", Unbinding ports...");

        finishedShutdown = true;

        wci = null;
        cwi = null;
    }

    public final void unbind() {
        acceptor.unbindAll();
    }

    public final boolean hasFinishedShutdown() {
        return finishedShutdown;
    }

    public final MapleMapFactory getMapFactory() {
        return mapFactory;
    }

    public static final ChannelServer newInstance(final String key) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException, MalformedObjectNameException {
        final ChannelServer instance = new ChannelServer(key);
        pendingInstances.put(key, instance);
        return instance;
    }

    public static final ChannelServer getInstance(final int channel) {
        return instances.get(channel);
    }

    public final void addPlayer(final MapleCharacter chr) {
        players.registerPlayer(chr);
        chr.getClient().getSession().write(MaplePacketCreator.serverMessage(serverMessage));
    }

    public final PlayerStorage getPlayerStorage() {
        return players;
    }

    public final void removePlayer(final MapleCharacter chr) {
        players.deregisterPlayer(chr);
    }

    public final String getServerMessage() {
        return serverMessage;
    }

    public final void setServerMessage(final String newMessage) {
        serverMessage = newMessage;
        broadcastPacket(MaplePacketCreator.serverMessage(serverMessage));
    }

    public final void broadcastPacket(final MaplePacket data) {
        players.broadcastPacket(data);
    }

    public final void broadcastSmegaPacket(final MaplePacket data) {
        players.broadcastSmegaPacket(data);
    }

    public final void broadcastGMPacket(final MaplePacket data) {
        players.broadcastGMPacket(data);
    }

    public final void broadcastStaffPacket(final MaplePacket data) {
        players.broadcastStaffPacket(data);
    }

    public void broadcastASmegaPacket(MaplePacket pkt) {
        players.broadcastASmegaPacket(pkt);
    }

    public final byte getExpRate() {
        return expRate;
    }

    public final void setExpRate(final byte expRate) {
        this.expRate = expRate;
    }

    public final int getChannel() {
        return channel;
    }

    public final void setChannel(final int channel) {
        if (pendingInstances.containsKey(key)) {
            pendingInstances.remove(key);
        }

        if (instances.containsKey(channel)) {
            instances.remove(channel);
        }

        instances.put(channel, this);
        this.channel = channel;
        this.mapFactory.setChannel(channel);
    }

    public static final Collection<ChannelServer> getAllInstances() {
        return Collections.unmodifiableCollection(instances.values());
    }

    public final String getIP() {
        return ip;
    }

    public final String getIP(final int channel) {
        try {
            return getWorldInterface().getIP(channel);
        } catch (RemoteException e) {
            System.err.println("Lost connection to world server" + e);
            throw new RuntimeException("Lost connection to world server");
        }
    }

    public final WorldChannelInterface getWorldInterface() {
        synchronized (worldReady) {
            while (!worldReady) {
                try {
                    worldReady.wait();
                } catch (InterruptedException e) {
                }
            }
        }
        return wci;
    }

    public final String getProperty(final String name) {
        return props.getProperty(name);
    }

    public final boolean isShutdown() {
        return shutdown;
    }

    public final void shutdown(final int time) {
        TimerManager.getInstance().schedule(new ShutdownServer(getChannel()), time);
    }

    public final void shutdownWorld(final int time) {
        try {
            getWorldInterface().shutdown(time);
        } catch (RemoteException e) {
            reconnectWorld();
        }
    }

    public final void shutdownLogin() {
        try {
            getWorldInterface().shutdownLogin();
        } catch (RemoteException e) {
            reconnectWorld();
        }
    }

    public final int getLoadedMaps() {
        return mapFactory.getLoadedMaps();
    }

    public final EventScriptManager getEventSM() {
        return eventSM;
    }

    public final void reloadEvents() {
        eventSM.cancel();
        eventSM = new EventScriptManager(this, props.getProperty("net.sf.odinms.channel.events").split(","));
        eventSM.init();
    }

    public final byte getMesoRate() {
        return mesoRate;
    }

    public final void setMesoRate(final byte mesoRate) {
        this.mesoRate = mesoRate;
    }

    public final byte getDropRate() {
        return dropRate;
    }

    public final void setDropRate(final byte dropRate) {
        this.dropRate = dropRate;
    }

    public final MapleGuild getGuild(final MapleGuildCharacter mgc) {
        final int gid = mgc.getGuildId();
        MapleGuild g = null;
        try {
            g = getWorldInterface().getGuild(gid, mgc);
        } catch (RemoteException re) {
            System.err.println("RemoteException while fetching MapleGuild." + re);
            return null;
        }

        if (gsStore.get(gid) == null) {
            gsStore.put(gid, new MapleGuildSummary(g));
        }

        return g;
    }

    public final MapleGuildSummary getGuildSummary(final int gid) {
        if (gsStore.containsKey(gid)) {
            return gsStore.get(gid);
        }
        //this shouldn't happen much, if ever, but if we're caught
        //without the summary, we'll have to do a worldop
        try {
            final MapleGuild g = this.getWorldInterface().getGuild(gid, null);
            if (g != null) {
                gsStore.put(gid, new MapleGuildSummary(g));
            }
            return gsStore.get(gid);	//if g is null, we will end up returning null
        } catch (RemoteException re) {
            System.err.println("RemoteException while fetching GuildSummary." + re);
            return null;

        }
    }

    public final void updateGuildSummary(final int gid, final MapleGuildSummary mgs) {
        gsStore.put(gid, mgs);
    }

    public static final void startChannel_Main() {
        try {
            initialProp = new Properties();
            final FileReader channelConfig = new FileReader(System.getProperty("net.sf.odinms.channel.config"));
            initialProp.load(channelConfig);
            channelConfig.close();
            final Registry registry = LocateRegistry.getRegistry(initialProp.getProperty("net.sf.odinms.world.host"), Registry.REGISTRY_PORT, new SslRMIClientSocketFactory());
            worldRegistry = (WorldRegistry) registry.lookup("WorldRegistry");

            for (int i = 0; i < Integer.parseInt(initialProp.getProperty("net.sf.odinms.channel.count", "0")); i++) {
                newInstance(ServerConstants.Channel_Key[i]).run_startup_configurations();
            }
//	    newInstance(initialProp.getProperty("net.sf.odinms.channel."+ new File(System.getProperty("net.sf.odinms.channelToLaunch")) +".key")).run();
            DatabaseConnection.getConnection(); // touch - so we see database problems early...
        } catch (FileNotFoundException fnfe) {
            System.out.println("[EXCEPTION] Channel configuration not found.");
        } catch (IOException ioe) {
            System.out.println("[EXCEPTION] Unable to load configuration properties, please check if the file is in use");
        } catch (NotBoundException nbe) {
            System.out.println("[EXCEPTION] The host channel IPs is out of bounds.");
        } catch (InstanceAlreadyExistsException iaee) {
            System.out.println("[EXCEPTION] The channel instance is already opened!");
        } catch (MBeanRegistrationException nmre) {
            System.out.println("[EXCEPTION] Something went wrong with Java MBEAN Registration.");
        } catch (NotCompliantMBeanException ncmbe) {
            System.out.println("[EXCEPTION] Something went wrong with Java MBEAN.");
        } catch (MalformedObjectNameException mone) {
            System.out.println("[EXCEPTION] The channel IPs is invalid.");
        }
    }

    private void IRCLoad() {
        if (channel == 9) {
            MainIRC.getInstance().sendGlobalMessage(Colors.DARK_GREEN + Colors.REVERSE + "The Server Is Up. See you in game");
            MainIRC.getInstance().sendAction("#ninjams", "I have just loaded channel" + channel);
            RPG.getInstance();
            Pandora.getInstance();
        }
    }

    private final class ShutDownListener implements Runnable {

        @Override
        public void run() {
            System.out.println("Saving all Hired Merchant...");
            closeAllMerchant();
            System.out.println("Saving all connected clients...");
            /*	    synchronized (players.mutex) {
            for (final MapleCharacter chr : players.getAllCharacters()) {
            if (chr.getClient().isLoggedIn() && chr != null) {
            System.out.println("Saving character : " + chr.getName());
            try {
            chr.getClient().disconnect(false);
            } catch (Exception e) {
            chr.saveToDB(true); // At least try to save.
            }
            }
            }
            }*/
            players.disconnectAll();

            acceptor.unbindAll();
            finishedShutdown = true;
            wci = null;
            cwi = null;
        }
    }

    public final MapleSquad getMapleSquad(final String type) {
        return mapleSquads.get(type);
    }

    public final boolean addMapleSquad(final MapleSquad squad, final String type) {
        if (mapleSquads.get(type) == null) {
            mapleSquads.remove(type);
            mapleSquads.put(type, squad);
            return true;
        }
        return false;
    }

    public final boolean removeMapleSquad(final String type) {
        if (mapleSquads.containsKey(type)) {
            mapleSquads.remove(type);
            return true;
        }
        return false;
    }

    public final void closeAllMerchant() {
        merchant_mutex.lock();

        final Iterator<HiredMerchant> merchants_ = merchants.values().iterator();
        try {
            while (merchants_.hasNext()) {
                merchants_.next().closeShop(true, false);
                merchants_.remove();
            }
        } finally {
            merchant_mutex.unlock();
        }
    }

    public final int addMerchant(final HiredMerchant hMerchant) {
        merchant_mutex.lock();

        int runningmer = 0;
        try {
            runningmer = running_MerchantID;
            merchants.put(running_MerchantID, hMerchant);
            running_MerchantID++;
        } finally {
            merchant_mutex.unlock();
        }
        return runningmer;
    }

    public final void removeMerchant(final HiredMerchant hMerchant) {
        merchant_mutex.lock();

        try {
            merchants.remove(hMerchant.getStoreId());
        } finally {
            merchant_mutex.unlock();
        }
    }

    public final boolean constainsMerchant(final int accid) {
        boolean contains = false;

        merchant_mutex.lock();
        try {
            final Iterator itr = merchants.values().iterator();

            while (itr.hasNext()) {
                if (((HiredMerchant) itr.next()).getOwnerAccId() == accid) {
                    contains = true;
                    break;
                }
            }
        } finally {
            merchant_mutex.unlock();
        }
        return contains;
    }

    public final void toggleMegaponeMuteState() {
        this.MegaphoneMuteState = !this.MegaphoneMuteState;
    }

    public final boolean getMegaphoneMuteState() {
        return MegaphoneMuteState;
    }

    public final List<MapleCharacter> getPartyMembers(final MapleParty party) {
        List<MapleCharacter> partym = new LinkedList<MapleCharacter>();
        for (final MaplePartyCharacter partychar : party.getMembers()) {
            if (partychar.getChannel() == getChannel()) { // Make sure the thing doesn't get duplicate plays due to ccing bug.
                MapleCharacter chr = getPlayerStorage().getCharacterByName(partychar.getName());
                if (chr != null) {
                    partym.add(chr);
                }
            }
        }
        return partym;
    }
}
