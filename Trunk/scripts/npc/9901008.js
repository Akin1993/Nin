var status = 0;var tao = 4032016;var text = "";var itemid = 0;var accessoryreq = new Array (350, 4000240, //- Small Flaming Feather    250, 4000224, //- Sabots    350, 4031674, //- Elpam Magnet ( Quest Item )    2500, 4000325 //- Carrot    );    var capreq = new Array (200, 4000017, //Pig Head    1500, 4000077, //Dark Cloud Foxtail    69, 4031241, //- Swallow's Lost Seed ( Quest Item )    1500, 4000150 // Ice Piece    );var capereq = new Array (200, 4031309,// - Cloud Piece ( Quest Item )    2000, 4000205,// Dirty Bandage    1000, 4000228 //Anesthetic Powder    );var coatreq = new Array (250, 4000125, //- Chief Gray's Sign    2500, 4000137, //- Subordinate D Fingernail    300, 4031458 //- Thanatos's Black Tornado ( Quest Item )    );var glovereq = new Array (1000, 4000061, // - Luster Pixie's Sunpiece    1669, 4000075, //- Triangular Bandana of the Nightghost    250, 4031098 //- All-purpose Clock Spring ( Quest Item )    );var longcoatreq = new Array (1337, 4000437, // - Black Mushroom Spore    250, 4001006,// - Flaming Feather    500, 4001075, // - Cornian's Marrow ( Quest Item )    1337, 4000041 // - Malady's Experimental Frog    );var pantsreq = new Array (2000, 4000153, //- Snorkle    150, 4031215, // - Taurospear's Spirit Rock ( Quest Item )    2000, 4000128 // - Buffy Hat    );var peteqreq = new Array (250, 4031460,// - Cold Heart of a Wolf ( Quest Item )    250, 4000415,// - Ice Tear    250, 4001000,// - Arwen's Glass Shoes    1500, 4032005 // - Typhon Feather    );var shoesreq = new Array (300, 4031253, //- Pianus's Scream ( Quest Item )    2000, 4000066,// - Cloud Foxtail    2000, 4032010 // - Elder Ashes    );var tamingreq = new Array (350, 4031195, //- Aurora Marble ( Quest Item )    350, 4000082, //- Zombie's Lost Gold Tooth    350, 4000124, //- Rombot's Memory Card    700, 4000336 //- Bible of the Corrupt    );function start() {    status = -1;    action(1, 0, 0);}function action(mode, type, selection) {    if (mode == 1)        status++;    else {        cm.voteMSG();        cm.dispose();        return;    }    if (status == 0) {        text = "Hey #h #! I'm Angy the Hokage of NinjaMS.";        text += "I can help you to get Shuriken and Shadow Shuriken";        text += "Items (Max Stat Items) if you would like some.";        text += "\r\n\r\n#rChoose What you want : #b";        text += "\r\n#L1#Shuriken Items#l";        text += "\r\n#L2#Shadow Shuriken Items#l";        text += "\r\n#L3#Shuriken Items Info#l";        text += "\r\n#L4#Shadow Shuriken Items Info#l";        cm.sendSimple(text);    } else if (status == 1) {        var msicount = cm.getPlayer().getMaxStatItems();        if(selection == 1) {            var goldscam = Math.min(((msicount) * 300), 5000);            if (!cm.getPlayer().hasAllStatMax()) {                cm.sendOk("You haven't maxxed all your stats");                return;            } else if (!cm.checkSpace(1302000)) {                cm.sendOk("I should have scammed you but nah... go clean the junk. Your inventory is full");                return;            } else if(msicount >= 9){                cm.sendOk("You already have all the Shuriken Items. Try to get Shadow Shuriken Items");                return;            } else if(cm.getPlayer().getReborns() < ((msicount + 1) * 100)){                cm.sendOk("You need atleast " + ((msicount+1) * 100 )+ " Rebirths to get the next Shuriken Item.");                return;            } else if (!cm.getPlayer().haveItem(tao, goldscam)) {                cm.sendOk("You are such a poor Fag! you need " + goldscam + " Tao Of Sight.");                return;            } else if(cm.getPlayer().getNinjaTensu() < msicount){                cm.sendOk("You need to have " +msicount+ "NinjaTensu to get the next MSI (You can get then by voting or GM events)");                return;            } else if (!cm.checkShurikenEtcitems()){                var pok = "You do not have the required Etc items. \r\n\r\nYou need :";                switch (msicount) {                    case 1:                    case 2:                        pok += "500 of #t4000020# ~ #v4000020# and 750 of #t4000069# ~ #v4000069#";// Wild Boar Tooth and Zombie tooth                        break;                    case 3:                    case 4:                        pok += "750 #t4000002# ~ #v4000002# and 1500 of #t4000168# ~ #v4000168#";//pig ribbon and sunflower seends                        break;                    case 5:                    case 6:                        pok += "1000 #t4000001# ~ #v4000001# and 1500 of #t4000078# ~ #v4000078#"; // orange mushy cap and jr cerebres tooth                        break;                    case 7:                        pok += "1500 #t4000051# ~ #v4000051# and 2500 of #t4000184# ~ #v4000184#";  // Hector Tails and Butter Toasted Squid                        break;                    case 8:                        pok += "50 #t4000040# ~ #v4000040# and 50 of #t4000176# ~ #v4000176#"; // // mushroom spore, poisonous mushroom,                        pok += "2500 #t4000074# ~ #v4000074# and 3000 of #t4000014# ~ #v4000014#";// lucida tail & drake skull.                        break;                    default:                        break;                }                cm.sendOk(pok);                return;            } else if (cm.getPlayer().getBossPoints() < (msicount * 3000)){                cm.sendNext("You do not have required boss PQ points You need :"+ msicount * 3000 + " Boss PQ Points");                return;            } else {                cm.sendOk("Seems like you have all the required Items. Want to get your shuriken Item now?");                status = 9;            }        } else if (selection == 2){            if(cm.getPlayer().getMaxStatItems() < 9 && !cm.getPlayer().isJounin()){                cm.sendOk("You have to get 9 Shuriken Items before you try to get Shadow shuriken Items. Too bad so sad!");                cm.dispose();            } else {                text = " Every Shadow shuriken Item has different requirements";                text += " according to the item you choose and the amount of MSI";                text += " you already have and your rebirths.";                text += " shadow shuriken Item can be any item except";                text += " Wizet items or magic scales";                text += " \r\n\r\n#r#ePlease enter the itemid of the Item you want ";                text += " to be made as Max stat in the box ";                cm.sendGetText(text);            }        } else if (selection == 3){            var lolz = cm.getPlayer().getMaxStatItems();            if(lolz >= 9){                cm.sendOk("You already have all the Shuriken Items. Try to get Shadow Shuriken Items");                return;            } else {                text = " Shuriken Items are very straight forward";                text += "#r\r\n Requirements for your next item : \r\n#b";                text += "\r\n 1) You should have atleast 1 Free Equipment slot";                text += "\r\n 2) You should have maxxed ALL your stats(Str, dex,";                text += "int & Luk) to 32767";                text += "\r\n 3) You should have "+(lolz * 100)+" Rebirths.";                text += "\r\n 4) You should have "+lolz+ " NinjaTensu (You can get by voting)";                text += "\r\n 5) You should have :"+ lolz * 3000 + " Boss PQ Points (Talk to Stirgeman for BossPQ)";                text += "\r\n 4) You should have : ";                switch (lolz) {                    case 1:                    case 2:                        text += "500 of #t4000020# ~ #v4000020# and 750 of #t4000069# ~ #v4000069#";// Wild Boar Tooth and Zombie tooth                        break;                    case 3:                    case 4:                        text += "750 #t4000002# ~ #v4000002# and 1500 of #t4000168# ~ #v4000168#";//pig ribbon and sunflower seends                        break;                    case 5:                    case 6:                        text += "1000 #t4000001# ~ #v4000001# and 1500 of #t4000078# ~ #v4000078#"; // orange mushy cap and jr cerebres tooth                        break;                    case 7:                        text += "1500 #t4000051# ~ #v4000051# and 2500 of #t4000184# ~ #v4000184#";  // Hector Tails and Butter Toasted Squid                        break;                    case 8:                        text += "50 #t4000040# ~ #v4000040# and 50 of #t4000176# ~ #v4000176#"; // // mushroom spore, poisonous mushroom,                        text += "2500 #t4000074# ~ #v4000074# and 3000 of #t4000014# ~ #v4000014#";// lucida tail & drake skull.                        break;                    default:                        break;                }                cm.sendNext(text);                cm.dispose();            }        } else if (selection == 4){            text = " Every Shadow shuriken Item has different requirements";            text += " according to the item you choose and the amount of MSI";            text += " you already have and your rebirths.";            text += " shadow shuriken Item can be any item except";            text += " Wizet items or magic scales";            text += " \r\n\r\n#r#ePlease enter the itemid of the Item you want ";            text += " to be made as Max stat in the box to get the list of requirements ";            cm.sendGetText(text);            status = 49;        }    } else if (status == 2){        itemid = cm.getNumber();        var x = cm.getItemType(itemid);        var taocount = cm.getPlayer().getMaxStatItems() * 200;        var gm = cm.getPlayer().isJounin();        if(cm.isCashItem(itemid)){            taocount *= 2;        }        if(cm.isBlockedItem(itemid)){            cm.sendNext("You have entered a item which is blocked from being obtained");            status = 99;        } else if (cm.nonExistantItem(itemid)){            cm.sendNext("You have entered a item which does not exist");            status = 99;        } else if (!cm.getPlayer().hasAllStatMax() && !gm) {            cm.sendOk("You haven't maxxed all your stats");            status = 99;        } else if (!cm.getPlayer().haveItem(tao, taocount) && !gm) {            cm.sendOk("You are such a poor Fag! you need " + taocount + " Tao Of Sight.");            status = 99;        } else if (!haveEtcReq(itemid) && !gm){            cm.sendNext("You do not have the required Etc Irems" + getEtcReq(itemid));            status = 99;        } else if (cm.getPlayer().getBossPoints() < (taocount * 100) && !gm){            cm.sendNext("You do not have required boss points You need :"+ taocount * 100);            status = 99;        } else {            cm.sendOk("Are you sure you want to make #v" + itemid + "# a Shadow Shuriken Item? If yes,press Ok");        }           } else if (status == 3) {        if(cm.checkSpace(1302000, 1)){            if (!cm.getPlayer().isJounin()){                removeItems(itemid);                cm.getPlayer().wipeStats();                var taocountd = cm.getPlayer().getMaxStatItems() * 200;                if(cm.isCashItem(itemid)){                    taocountd *= 2;                }                cm.gainItem(tao, -taocountd);                cm.getPlayer().setBossPoints(cm.getPlayer().getBossPoints() - (taocountd * 100));            }            cm.gainStatItem(itemid, 32767, 10, 10);            cm.getPlayer().addMaxStatItem();            cm.sendServerNotice(6, "[Anbu] " + cm.getPlayer().getName() + " - Has just got himself a Shadow Shuriken Item. Now he has a Total of " + cm.getPlayer().getMaxStatItems() + " Max stat Items!");            cm.sendServerNotice(5, "[Anbu] " + cm.getPlayer().getName() + " - Has just got himself a Shadow Shuriken Item. Now he has a Total of " + cm.getPlayer().getMaxStatItems() + " Max stat Items!");            cm.sendOk("Have Fun bish");            cm.dispose();        } else {            cm.sendNext(" You do not have enough inventory space");            cm.dispose();        }    } else if (status == 49){        itemid = cm.getNumber();        cm.dropMessage(itemid);        cm.sendOk(cm.getReqItems(itemid));        cm.dispose();    } else if (status == 100){        cm.sendOk(cm.getReqItems(itemid));        cm.dispose();    } else if (status == 10){        var msicunt = cm.getPlayer().getMaxStatItems();        var scam = Math.min(((msicunt) * 300), 5000);        var msis = new Array(1012011, // Rudolph Red Nose            1032055, // Agent c's Old Reciever            1061002, // Red Skirt            1102053, // Old Ragged Cape            1082244, // agent 0's nylon glove            1022058, // racoon mask            1122054, // Maple Awakening shit necklace            1112000, // Sparkling Ring            1002801 // Raven ninja Bandana            );        cm.getPlayer().gainItem(tao, -scam);        cm.getPlayer().wipeStats();        cm.getPlayer().setLevel(1);        cm.getPlayer().setNinjaTensu(cm.getPlayer().getNinjaTensu() - msicunt);        cm.removeShurikenEtcItems();        cm.gainStatItem(msis[msicunt], 32763, 10, 10);        cm.getPlayer().addMaxStatItem();        cm.sendServerNotice(6, "[Anbu] " + cm.getPlayer().getName() + " - Has just got himself a shuriken Item. Now he has a Total of " + player.getMaxStatItems() + " Max stat Items!");        cm.dispose();    } else {        cm.voteMSG();        cm.dispose();    }}function haveEtcReq(itemid){    var etcarray;    var x = cm.getItemType(itemid);    switch (x) {        case 1:            etcarray = accessoryreq;            break;        case 2:            etcarray = capreq;            break;        case 3:            etcarray = capereq;            break;        case 4:            etcarray = coatreq;            break;        case 5:            etcarray = glovereq;            break;        case 6:            etcarray = longcoatreq;            break;        case 7:            etcarray = pantsreq;            break;        case 8:            etcarray = peteqreq;            break;        case 9:            etcarray = shoesreq;            break;        default:            etcarray = tamingreq;            break;    }    for(var i = 0; i < etcarray.length; i++){        var item = etcarray[i+1];        var amt = etcarray[i];        if(cm.isCashItem(itemid)){            amt *= 2;        }        cm.dropMessage("req itemid : " + item + " amount : " + amt);        if(!cm.haveItem(item, amt)){            return false;        }        i++;    }    return true;}function removeItems(itemid){    var etcarray;    var x = cm.getItemType(itemid);    switch (x) {        case 1:            etcarray = accessoryreq;            break;        case 2:            etcarray = capreq;            break;        case 3:            etcarray = capereq;            break;        case 4:            etcarray = coatreq;            break;        case 5:            etcarray = glovereq;            break;        case 6:            etcarray = longcoatreq;            break;        case 7:            etcarray = pantsreq;            break;        case 8:            etcarray = peteqreq;            break;        case 9:            etcarray = shoesreq;            break;        default:            etcarray = tamingreq;            break;    }    for(var i = 0; i < etcarray.length; i++){        var item = etcarray[i+1];        var amt = etcarray[i];        if(cm.isCashItem(itemid)){            amt *= 2;        }        cm.gainItem(item, -amt);        i++;    }}function getEtcReq(itemid){    var etcarray;    var x = cm.getItemType(itemid);    switch (x) {        case 1:            etcarray = accessoryreq;            break;        case 2:            etcarray = capreq;            break;        case 3:            etcarray = capereq;            break;        case 4:            etcarray = coatreq;            break;        case 5:            etcarray = glovereq;            break;        case 6:            etcarray = longcoatreq;            break;        case 7:            etcarray = pantsreq;            break;        case 8:            etcarray = peteqreq;            break;        case 9:            etcarray = shoesreq;            break;        default:            etcarray = tamingreq;            break;    }    var omg = " You need : \r\n";    for(var i = 0; i < etcarray.length; i++){        var item = etcarray[i+1];        var amt = etcarray[i];        if(cm.isCashItem(itemid)){            amt *= 2;        }        omg += amt + " of #v"+item+"# ~ #t"+item+"# \r\n"        i++;    }    return omg;}