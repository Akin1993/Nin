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
package net.sf.odinms.client.Buffs;

import java.io.Serializable;
import net.sf.odinms.net.LongValueHolder;

public enum MapleBuffStat implements LongValueHolder, Serializable {

    ELEMENT_RESET       (0x200000000L, true),

    HOMING_BEACON       (0x1),
    MORPH               (0x2),
    RECOVERY            (0x4),
    MAPLE_WARRIOR       (0x8),

    STANCE              (0x10),
    SHARP_EYES          (0x20),
    MANA_REFLECTION     (0x40),
    DRAGON_ROAR         (0x80), // Stuns the user

    SPIRIT_CLAW         (0x100),
    INFINITY            (0x200),
    HOLY_SHIELD         (0x400),
    HAMSTRING           (0x800),

    BLIND               (0x1000),
    CONCENTRATE         (0x2000),
    ECHO_OF_HERO        (0x8000),

    GHOST_MORPH         (0x20000),
    CONFUSE             (0x80000),

    GM_HIDE             (0x1000000),
    BERSERK_FURY        (0x8000000),

    DIVINE_BODY         (0x10000000),
    FINALATTACK         (0x80000000),

    WATK                (0x100000000L),
    ENERGY_CHARGE       (0x800000000L),
    WDEF                (0x200000000L),
    MATK                (0x400000000L),
    MDEF                (0x800000000L),
    
    ACC                 (0x1000000000L),
    DASH                (0x1000000000L),
    DASH2               (0x2000000000L),
    SHOWDASH            (0x4000000000L),
    MONSTER_RIDING      (0x4000000000L),
    SPEED_INFUSION      (0x8000000000L),
    AVOID               (0x2000000000L),
    HANDS               (0x4000000000L),
    SPEED               (0x8000000000L),

    JUMP                (0x10000000000L),
    MAGIC_GUARD         (0x20000000000L),
    DARKSIGHT           (0x40000000000L),
    DARKSIGHT_2         (0x40000000000L),
    BOOSTER             (0x80000000000L),

    POWERGUARD          (0x100000000000L),
    MAXHP               (0x200000000000L),
    MAXMP               (0x400000000000L),
    INVINCIBLE          (0x800000000000L),

    SOULARROW           (0x1000000000000L),
    STUN                (0x2000000000000L),
    POISON              (0x4000000000000L),
    SEAL                (0x8000000000000L),

    DARKNESS            (0x10000000000000L),
    COMBO               (0x20000000000000L),
    SUMMON              (0x20000000000000L),
    WK_CHARGE           (0x40000000000000L),
    DRAGONBLOOD         (0x80000000000000L),

    HOLY_SYMBOL         (0x100000000000000L),
    MESOUP              (0x200000000000000L),
    SHADOWPARTNER       (0x400000000000000L),
    PICKPOCKET          (0x800000000000000L),
    PUPPET              (0x800000000000000L),

    MESOGUARD           (0x1000000000000000L),
    WEAKEN              (0x4000000000000000L),
    
   ;
    static final long serialVersionUID = 0L;
    private final long buffstat;
    private final boolean first;

    private MapleBuffStat(long buffstat) {
        this.buffstat = buffstat;
        first = false;
    }

    private MapleBuffStat(long buffstat, boolean first) {
        this.buffstat = buffstat;
        this.first = first;
    }

    public final boolean isFirst() {
        return first;
    }

    public final long getValue() {
        return buffstat;
    }
}
