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
package net.sf.odinms.server.maps;

import java.awt.Point;
import java.awt.Rectangle;

import net.sf.odinms.client.Skills.ISkill;
import net.sf.odinms.client.MapleCharacter;
import net.sf.odinms.client.MapleClient;
import net.sf.odinms.client.Skills.SkillFactory;
import net.sf.odinms.net.MaplePacket;
import net.sf.odinms.client.Buffs.MapleStatEffect;
import net.sf.odinms.server.life.MapleMonster;
import net.sf.odinms.server.life.MobSkill;
import net.sf.odinms.tools.MaplePacketCreator;

public class MapleMist extends AbstractMapleMapObject {


    private Rectangle mistPosition;
    private MapleCharacter owner = null; // Assume this is removed, else weakref will be required
    private MapleMonster mob = null;
    private MapleStatEffect source;
    private MobSkill skill;
    private boolean isMobMist, isPoisonMist;
    private int skillDelay, skilllevel;

    public MapleMist(Rectangle mistPosition, MapleMonster mob, MobSkill skill) {
	this.mistPosition = mistPosition;
	this.mob = mob;
	this.skill = skill;
	skilllevel = skill.getSkillId();

	isMobMist = true;
	isPoisonMist = true;
	skillDelay = 0;
    }

    public MapleMist(Rectangle mistPosition, MapleCharacter owner, MapleStatEffect source) {
	this.mistPosition = mistPosition;
	this.owner = owner;
	this.source = source;
	this.skilllevel = owner.getSkillLevel(SkillFactory.getSkill(source.getSourceId()));

	switch (source.getSourceId()) {
	    case 4221006: // Smoke Screen
		isMobMist = false;
		isPoisonMist = false;
		skillDelay = 8;
		break;
	    case 2111003: // FP mist
	    case 12111005: // Flame wizard, [Flame Gear]
		isMobMist = false;
		isPoisonMist = true;
		skillDelay = 8;
		break;
	}
    }

    @Override
    public MapleMapObjectType getType() {
	return MapleMapObjectType.MIST;
    }

    @Override
    public Point getPosition() {
	return mistPosition.getLocation();
    }

    public ISkill getSourceSkill() {
	return SkillFactory.getSkill(source.getSourceId());
    }

    public boolean isMobMist() {
	return isMobMist;
    }

    public boolean isPoisonMist() {
	return isPoisonMist;
    }

    public int getSkillDelay() {
	return skillDelay;
    }

    public int getSkillLevel() {
	return skilllevel;
    }

    public MapleMonster getMobOwner() {
	return mob;
    }

    public MapleCharacter getOwner() {
	return owner;
    }

    public MobSkill getMobSkill() {
	return this.skill;
    }

    public Rectangle getBox() {
	return mistPosition;
    }

    @Override
    public void setPosition(Point position) {
    }

    public MaplePacket fakeSpawnData(int level) {
	if (owner != null) {
	    return MaplePacketCreator.spawnMist(this);
	}
	return MaplePacketCreator.spawnMist(this);
    }

    @Override
    public void sendSpawnData(final MapleClient c) {
	c.getSession().write(MaplePacketCreator.spawnMist(this));
    }

    @Override
    public void sendDestroyData(final MapleClient c) {
	c.getSession().write(MaplePacketCreator.removeMist(getObjectId()));
    }

    public boolean makeChanceResult() {
	return source.makeChanceResult();
    }
}