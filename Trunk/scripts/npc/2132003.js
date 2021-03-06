/*
 * @NPC : Shadrion
 * @id  2132003
 * @location : Henesys
 * @Author : System of NinjaMS
 * @function: Selects Clan for now *
 */
var status = 0;
var fee = 0;
importPackage (net.sf.odinms.client);
var tao = 4032016;

function start() {
    if(cm.getPlayer().getEventInstance() != null){
        cm.sendOk("You cannot do this from inside Event Maps");
        cm.dispose();
    } else {
        status = -1;
        action(1, 0, 0);
    }
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.voteMSG();
        cm.dispose();
    } else {
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            var tex = "I'm Shadrion, the awesome #bClan and Path Manager#k of NinjaMs.";
            tex += "Choose what you need help with :";
            tex += "\r\n#L1#Change to KOC#l\r\n#L2#Change To Adventurer#l";
            tex +=  "\r\n#L3#Rate Management#l \r\n#L4#Mode Management#l";
            cm.sendSimple(tex);
        } else if (status == 1){
            if (selection == 1){
                cm.changeJobById(1000);
                cm.getPlayer().wipeKB();
                KeyMapShit.loadKeymap(cm.getPlayer(), 2);
                cm.sendOk("Your job path has been changed to be Cygnus");
                cm.dispose();
            } else if (selection == 2) {
                cm.changeJobById(0);
                cm.getPlayer().wipeKB();
                KeyMapShit.loadKeymap(cm.getPlayer(), 1);
                cm.sendOk("Your job path has been changed to be Adventurer");
                cm.dispose();
            } else if (selection == 3){
                var txt1 = " Choose Which rate you want to manage\r\n#b";
                txt1 += "\r\n #L1# Exp Rate#l";
                txt1 += "\r\n #L2# Meso Rate#l";
                txt1 += "\r\n #L3# Drop Rate#l";
                txt1 += "\r\n #L4# Boss Drop Rate#l";
                cm.sendSimple(txt1);
                status = 9;
            } else if (selection == 4){
                if(cm.getPlayer().getReborns() < 100){
                    fee = 3;
                } else if(cm.getPlayer().getReborns() < 500){
                    fee = 6;
                } else {
                    fee = 10;
                }
                var txtt = "#rChoose What you want : \r\n#b";
                txtt += "\r\n#L1#Kyubi - Exp rate boosting mode#r(5 dragon heart #v4031449# and "+fee+" Tao Of Sight)#b#l";
                txtt += "\r\n#L2#Sage - Meso rate boosting mode#r(5 dragon heart #v4031449# and "+fee+" Tao Of Sight)#b#l";
                txtt += "\r\n#L3#Hachibi - Drop rate boosting mode#r(5 dragon heart #v4031449# and "+fee+" Tao of Sight)#b#l";
                txtt += "\r\n#L4#Shakaku - Boss drop rate boosting mode#r(5 dragon heart #v4031449# and "+fee+" Tao Of Sight)#b#l";
                txtt += "\r\n#L5#All 4 modes - Turns on all the modes#r(25 dragon heart #v4031449# and "+(fee * 5)+" Tao Of Sight)#b#l"
                txtt += "\r\n#L6#No thanks. I'm fine cya later#l\r\n";
                cm.sendSimple(txtt);
                status = 99;
            } else {
                cm.sendOk("under construction");
            }
        } else if (status == 2){
            var text = "";
            if (cm.getClan() == 0){
                text += "There are the clans you can choose from. It will cost you 5 Tao Of Sight \r\n";
                text += "#rChoose Which Clan you want : #d *Changing clans is not easy. It will cost you loads. So choose your clan wisely*";
                text += "#b\r\n\r\n#L1# Earth Clan = 400x exp rates 10x meso rate and 2x drop rate 4x boss drop rate#l";
                text += "\r\n\r\n#L2# Wind Clan = 200x exp rate 20x meso rate and 2x drop rate 4x boss drop rate#l";
                text += "#b\r\n\r\n#L3# Water Clan = 200x exp rates 10x meso rate and 6x drop rate 4x boss drop rate#l";
                text += "\r\n\r\n#L4# Fire Clan = 200x exp rate 10x meso rate and 2x drop rate 8x boss drop rate#l";
                text += "#b\r\n\r\n#L5# Naruto Clan = 300x exp rates 15x meso rate and 4x drop rate 5x boss drop rate#l";
            } else {
                text += "There are the clans you can choose from. It will cost you ";
                text += " 2000 Tao of Sight, 5 dark crystal #v4005004#, 5 refined Emerald #v4021003# and 5 refined garnet #v4021000# and 5 refined Sapphire #v4021005#\r\n";
                text += "#rChoose Which Clan you want : ";
                text += "#b\r\n\r\n#L1# Earth Clan = 400x exp rates 10x meso rate and 2x drop rate 4x boss drop rate#l";
                text += "\r\n\r\n#L2# Wind Clan = 200x exp rate 20x meso rate and 2x drop rate 4x boss drop rate#l";
                text += "#b\r\n\r\n#L3# Water Clan = 200x exp rates 10x meso rate and 6x drop rate 4x boss drop rate#l";
                text += "\r\n\r\n#L4# Fire Clan = 200x exp rate 10x meso rate and 2x drop rate 8x boss drop rate#l";
                text += "#b\r\n\r\n#L5# Naruto Clan = 300x exp rates 15x meso rate and 4x drop rate 5x boss drop rate#l";

            }
            cm.sendSimple(text);
        } else if (status == 3){
            if(cm.getClan() == 0){
                if (cm.getPlayer().haveSight(5)){
                    cm.getPlayer().gainItem(tao, -5);
                    cm.setClan(selection);
                    var texte = "Now officially " + cm.showClan();
                    cm.sendOk(texte);
                } else {
                    cm.sendOk("You do not have enough chakra to choose clans");
                    cm.dispose();
                }
            } else {
                if (cm.getPlayer().haveSight(2000) && cm.haveItem(4005004, 5) && cm.haveItem(4021005, 5) && cm.haveItem(4021000, 5) && cm.haveItem(4021003, 5)){
                    cm.gainItem(tao, -2000);
                    cm.gainItem(4005004, -5);
                    cm.gainItem(4021005, -5);
                    cm.gainItem(4021000, -5);
                    cm.gainItem(4021003, -5);
                    cm.setClan(selection);
                    var textet = "Now officially " + cm.showClan();
                    cm.sendOk(textet);
                } else {
                    cm.sendOk("You do not have enough chakra to change clans :P");
                    cm.dispose();
                }
            }
        } else if (status == 10){
            var plates = Array(4011000, // bronze
                4011001, // steel
                4011002, // mithril
                4011003, // adamantium
                4011004, // silver
                4011005, // Orihalcon
                4011006, // gold
                4011007, // moon rock
                4011008); // lidium
            if (selection == 1){
                var curexp = cm.getPlayer().getExpBoost();
                if (curexp <= 15){
                    if(cm.haveItem(plates[0], 1) && cm.getPlayer().haveSight(25)){
                        cm.gainItem(plates[0], -1);
                        cm.gainItem(tao, -25);
                        cm.getPlayer().addExpBoost();
                        cm.getPlayer().addExpBoost();
                        cm.getPlayer().addExpBoost();
                        cm.getPlayer().addExpBoost();
                        cm.getPlayer().addExpBoost();
                        cm.sendOk("You have gained 5% exp boost");
                    } else {
                        cm.sendNext("Bring me 1 #v"+plates[0]+"# and 25 tao of sight");
                        cm.dispose();
                    }
                } else if (curexp <= 30){
                    if(cm.haveItem(plates[1], 2) && cm.getPlayer().haveSight(50)){
                        cm.gainItem(plates[1], -2);
                        cm.gainItem(tao, -50);
                        cm.getPlayer().addExpBoost();
                        cm.getPlayer().addExpBoost();
                        cm.getPlayer().addExpBoost();
                        cm.getPlayer().addExpBoost();
                        cm.getPlayer().addExpBoost();
                        cm.sendOk("You have gained 5% exp boost");
                    }else {
                        cm.sendNext("Bring me 2 #v"+plates[1]+"# and 50 tao of sight");
                        cm.dispose();
                    }
                } else if (curexp <= 50){
                    if(cm.haveItem(plates[2], 3) && cm.getPlayer().haveSight(75)){
                        cm.gainItem(plates[2], -3);
                        cm.gainItem(tao, -75);
                        cm.getPlayer().addExpBoost();
                        cm.getPlayer().addExpBoost();
                        cm.getPlayer().addExpBoost();
                        cm.getPlayer().addExpBoost();
                        cm.getPlayer().addExpBoost();
                        cm.sendOk("You have gained 5% exp boost");
                    }else {
                        cm.sendNext("Bring me 3 #v"+plates[2]+"# and 75 tao of sight");
                        cm.dispose();
                    }
                } else if (curexp <= 75){
                    if(cm.haveItem(plates[3], 3) && cm.haveItem(plates[4], 3) && cm.getPlayer().haveSight(100)){
                        cm.gainItem(plates[3], -3);
                        cm.gainItem(plates[4], -3);
                        cm.gainItem(tao, -100);
                        cm.getPlayer().addExpBoost();
                        cm.getPlayer().addExpBoost();
                        cm.getPlayer().addExpBoost();
                        cm.getPlayer().addExpBoost();
                        cm.getPlayer().addExpBoost();
                        cm.sendOk("You have gained 5% exp boost");
                    }else {
                        cm.sendNext("Bring me 3 #v"+plates[3]+"# and 3 #v"+plates[4]+"# and 100 Tao Of Sight");
                        cm.dispose();
                    }
                } else if(curexp < 100){
                    if(cm.haveItem(plates[5], 5) && cm.getPlayer().haveSight(1000)){
                        cm.gainItem(plates[5], -5);
                        cm.gainItem(tao, -1000);
                        cm.getPlayer().addExpBoost();
                        cm.getPlayer().addExpBoost();
                        cm.getPlayer().addExpBoost();
                        cm.getPlayer().addExpBoost();
                        cm.getPlayer().addExpBoost();
                        cm.sendOk("You have gained 5% exp boost");
                    } else {
                        cm.sendNext("Bring me 5 #v"+plates[8]+"# and 1000 Tao Of Sight");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("You already have maxxed your EXP boost");
                }
                cm.getPlayer().dropMessage("Your EXP boost rate now is : "+ cm.getPlayer().getExpBoost() + "% " );
                cm.dispose();
            } else if (selection == 2){
                var drops = new Array(4000136, //coconut
                    2022116, // peach
                    4031150, // plant sample
                    4000027, // wild cargo eye
                    4000120, // mateon tentacle
                    4000120
                    );
                var curmeso = cm.getPlayer().getMesoBoost();
                var ii;
                if(curmeso < 20){
                    ii = 0
                } else if (curmeso < 40){
                    ii = 1;
                } else if (curmeso < 60){
                    ii = 2;
                } else if (curmeso < 80) {
                    ii = 3;
                } else {
                    ii = 4;
                }
                var reqitem = drops[ii];
                var reqitemamt = 1;
                reqitemamt += curmeso;
                if (curmeso < 100){
                    if(cm.haveItem(reqitem, reqitemamt) && cm.getPlayer().haveSight(reqitemamt)){
                        cm.gainItem(reqitem, - reqitemamt);
                        cm.gainItem(tao, - reqitemamt);
                        cm.getPlayer().addMesoBoost();
                        cm.getPlayer().addMesoBoost();
                        cm.getPlayer().addMesoBoost();
                        cm.sendOk("You have gained 3% meso boost");
                        cm.getPlayer().dropMessage("Your Meso boost rate now is : "+ cm.getPlayer().getMesoBoost() + "% " );
                    } else {
                        cm.sendNext("Bring me "+reqitemamt+" #v"+reqitem+"# And "+reqitemamt+" Tao Of Sight");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("You already have maxxed your Meso boost");
                }
                cm.dispose();
            } else if (selection == 3){
                var drop = new Array(4000051, //Hector tail
                    4000159, // poisonous poopa spike
                    4000003, // tree branch
                    4000236, // beetle's horn
                    4000169, // pounder
                    4000169
                    );
                var curdrop = cm.getPlayer().getDropBoost();
                var iii;
                if(curdrop < 20){
                    iii = 0
                } else if (curdrop < 40){
                    iii = 1;
                } else if (curdrop < 60){
                    iii = 2;
                } else if (curdrop < 80) {
                    iii = 3;
                } else {
                    iii = 4;
                }
                var reqitemm = drop[iii];
                var amt = 1;
                amt += curdrop;
                amt *= 3;
                if (curdrop < 100){
                    if(cm.haveItem(reqitemm, amt) && cm.getPlayer().haveSight(amt)){
                        cm.gainItem(reqitemm, -amt);
                        cm.gainItem(tao, -amt);
                        cm.getPlayer().addDropBoost();
                        cm.sendOk("You have gained 1% Drop boost");
                        cm.getPlayer().dropMessage("Your Drop boost rate now is : "+ cm.getPlayer().getDropBoost() + "% " );
                    } else {
                        cm.sendNext("Bring me "+amt+" #v"+reqitemm+"# And "+amt+" Tao Of Sight");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("You already have maxxed your drop boost");
                }
                cm.dispose();
            } else if (selection == 4){
                var core = new Array(4020000, //garnet
                    4020001, // amethyst
                    4020002, // aquamarine
                    4020003, // Emerald
                    4020004, // opal
                    4020005, // sapphire
                    4020006, // topaz
                    4020007, // diamond
                    4020008 // black crystal ore
                    );
                var curbdrop = cm.getPlayer().getBossDropBoost();
                var iiii;
                if(curdrop < 20){
                    iiii = 0
                } else if (curbdrop < 30){
                    iiii = 1;
                } else if (curbdrop < 40){
                    iiii = 2;
                } else if (curbdrop < 50) {
                    iiii = 3;
                }else if (curbdrop < 60) {
                    iiii = 4;
                }else if (curbdrop < 70) {
                    iiii = 5;
                }else if (curbdrop < 80) {
                    iiii = 6;
                }else if (curbdrop < 90) {
                    iiii = 7;
                } else {
                    iiii = 8;
                }
                var reqitemmm = core[iiii];
                var amnt = 1;
                amnt += curbdrop / 2 ;

                if (curbdrop < 100){
                    if(cm.haveItem(reqitemmm, amnt) && cm.getPlayer().haveSight(amnt)){
                        cm.gainItem(reqitemmm, -amnt);
                        cm.gainItem(tao, -amnt);
                        cm.getPlayer().addBossDropBoost();
                        cm.sendOk("You have gained 1% Boss Drop boost");
                        cm.getPlayer().dropMessage("Your Boss Drop boost rate now is : "+ cm.getPlayer().getBossDropBoost() + "% " );
                    } else {
                        cm.sendNext("Bring me "+amnt+" #v"+reqitemmm+"# And "+amnt+" Tao Of Sight");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("You already have maxxed your Boss drop boost");
                }
                cm.dispose();
            } else {
                cm.sendOk("Under Construction");
                cm.dispose();
            }
        } else if (status == 100){
            if (selection > 0 && selection < 5){
                if (cm.haveItem(4031449, 5) && cm.getPlayer().haveSight(fee)){
                    cm.gainItem(4031449, -5);
                    cm.gainItem(tao, -fee);
                    cm.setModeOn(selection);
                    cm.sendOk("Remember : The modes will be cancelled if you change channel or log off");
                    cm.dispose();
                } else {
                    cm.sendOk("You either do not have the meso or dragon heart. It might also be that you are completely retarded");
                    cm.dispose();
                }
            } else if(selection == 5){
                if (cm.haveItem(4031449, 25) && cm.getPlayer().haveSight(fee*4)){
                    cm.gainItem(4031449, -25);
                    cm.gainItem(tao, -(fee*4));
                    cm.setModeOn(selection);
                    cm.sendOk("Remember : The modes will be cancelled if you change channel or log off");
                    cm.dispose();
                } else {
                    cm.sendOk("You either do not have the meso or dragon heart. It might also be that you are completely retarded");
                    cm.dispose();
                }
            } else {
                cm.voteMSG();
                cm.dispose();
            }
        } else {
            cm.dispose();
        }
    }
}
