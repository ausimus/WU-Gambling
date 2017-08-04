package org.ausimus.wurmunlimited.mods.gambling.actions.roulette;

/*
     ___          ___          ___                     ___          ___          ___
    /\  \        /\__\        /\  \         ___       /\__\        /\__\        /\  \
   /::\  \      /:/  /       /::\  \       /\  \     /::|  |      /:/  /       /::\  \
  /:/\:\  \    /:/  /       /:/\ \  \      \:\  \   /:|:|  |     /:/  /       /:/\ \  \
 /::\~\:\  \  /:/  /  ___  _\:\~\ \  \     /::\__\ /:/|:|__|__  /:/  /  ___  _\:\~\ \  \
/:/\:\ \:\__\/:/__/  /\__\/\ \:\ \ \__\ __/:/\/__//:/ |::::\__\/:/__/  /\__\/\ \:\ \ \__\
\/__\:\/:/  /\:\  \ /:/  /\:\ \:\ \/__//\/:/  /   \/__/~~/:/  /\:\  \ /:/  /\:\ \:\ \/__/
     \::/  /  \:\  /:/  /  \:\ \:\__\  \::/__/          /:/  /  \:\  /:/  /  \:\ \:\__\
     /:/  /    \:\/:/  /    \:\/:/  /   \:\__\         /:/  /    \:\/:/  /    \:\/:/  /
    /:/  /      \::/  /      \::/  /     \/__/        /:/  /      \::/  /      \::/  /
    \/__/        \/__/        \/__/                   \/__/        \/__/        \/__/

*/

import com.wurmonline.server.*;
import com.wurmonline.server.economy.Change;
import com.wurmonline.server.items.*;
import org.ausimus.wurmunlimited.mods.gambling.config.AusConstants;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.creatures.Creature;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class RouletteSpin implements WurmServerMod, ItemTypes, MiscConstants, ModAction, BehaviourProvider, ActionPerformer {
    private static short PTID;
    private static ActionEntry actionEntryPT;

    public RouletteSpin() {
        PTID = (short) ModActions.getNextActionId();
        actionEntryPT = ActionEntry.createEntry(PTID, "Spin", "Spinning", new int[]{});
        ModActions.registerAction(actionEntryPT);
    }

    @Override
    public BehaviourProvider getBehaviourProvider() {
        return this;
    }

    @Override
    public ActionPerformer getActionPerformer() {
        return this;
    }

    @Override
    public short getActionId() {
        return PTID;
    }

    /**
     * @param performer performer representing the instantiation of Creature.
     * @param source    The Item source.
     * @param target    The Item target.
     * @return Fuck Warnings.
     **/
    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
        if (source == target && target.getTemplateId() == AusConstants.GamblingTokenTemplateID && target.getAuxData() == AusConstants.GameModeRoulette) {
            return Collections.singletonList(actionEntryPT);
        } else {
            return null;
        }
    }

    /**
     * @param act       the act.
     * @param performer performer representing the instantiation of Creature.
     * @param source    The Item source.
     * @param target    The Item target.
     * @param action    Action ID number.
     * @param counter   Timer shit.
     * @return Fuck Warnings.
     **/
    @Override
    public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
        Random rand = new Random();
        int randomNumberPick = rand.nextInt(37);
        int randomColor = rand.nextInt(2);
        String logFile = "mods/GambleMod/Roulette/log.txt";

        if (target.getData2() == -1 && target.getColor() == -1) {
            performer.getCommunicator().sendNormalServerMessage("The token has no bet data.");
            return true;
        }

            // Single
        if (source == target
                && source.getTemplateId() == AusConstants.GamblingTokenTemplateID
                && target.getAuxData() == AusConstants.GameModeRoulette) {
            if (target.getColor() == AusConstants.Black || target.getColor() == AusConstants.White) {
                switch (randomColor) {
                    case 0:
                        if (target.getColor() == AusConstants.Black) {
                            performer.getCommunicator().sendNormalServerMessage("You Win you rolled black.");
                            try {
                                Change c = new Change(performer.getMoney() + target.getData1() * 2);
                                performer.setMoney(performer.getMoney() + target.getData1() * 2);
                                performer.getCommunicator().sendNormalServerMessage("New Bank balance: " + c.getChangeString() + ".");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    case 1:
                        if (target.getColor() == AusConstants.White && randomColor == 1) {
                            performer.getCommunicator().sendNormalServerMessage("You Win you rolled white.");
                            try {
                                Change c = new Change(performer.getMoney() + target.getData1() * 4);
                                performer.setMoney(performer.getMoney() + target.getData1() * 4);
                                performer.getCommunicator().sendNormalServerMessage("New Bank balance: " + c.getChangeString() + ".");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    default:
                        if (target.getColor() == AusConstants.White && randomColor == 0) {
                            performer.getCommunicator().sendNormalServerMessage("You Lose you rolled black.");
                        }
                        if (target.getColor() == AusConstants.Black && randomColor == 1) {
                            performer.getCommunicator().sendNormalServerMessage("You Lose you rolled white.");
                        }
                }
            }

            // Double
            if (target.getData2() >= 0) {
                if (randomNumberPick == target.getData2()) {
                    performer.getCommunicator().sendNormalServerMessage("You win, you rolled " + randomNumberPick + ".");
                    try {
                        Change c = new Change(performer.getMoney() + target.getData1() * 8);
                        performer.setMoney(performer.getMoney() + target.getData1() * 8);
                        performer.getCommunicator().sendNormalServerMessage("New Bank balance: " + c.getChangeString() + ".");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    performer.getCommunicator().sendNormalServerMessage("You did not win the number, the random # was " + randomNumberPick + ".");
                }
            }

            // Give em the pot
            if (target.getColor() != -1 && target.getData2() >= 0) {
                if (randomNumberPick == target.getData2() && target.getColor() == AusConstants.Black && randomColor == 0) {
                    performer.getCommunicator().sendNormalServerMessage("You won the color and the number.");
                    try {
                        Change c = new Change(performer.getMoney() + target.getData1() * 16);
                        performer.setMoney(performer.getMoney() + target.getData1() * 16);
                        performer.getCommunicator().sendNormalServerMessage("New Bank balance: " + c.getChangeString() + ".");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (randomNumberPick == target.getData2() && target.getColor() == AusConstants.White && randomColor == 1) {
                    performer.getCommunicator().sendNormalServerMessage("You won the color and the number.");
                    try {
                        Change c = new Change(performer.getMoney() + target.getData1() * 16);
                        performer.setMoney(performer.getMoney() + target.getData1() * 16);
                        performer.getCommunicator().sendNormalServerMessage("New Bank balance: " + c.getChangeString() + ".");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                // Log Writer
                FileWriter writeLog = new FileWriter(logFile, true);
                BufferedWriter bufferedLogWriter = new BufferedWriter(writeLog);
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date timeStamp = new Date();
                bufferedLogWriter.write("==========================================================\n");
                bufferedLogWriter.write(dateFormat.format(timeStamp) + "\n");
                bufferedLogWriter.write(performer.getName() + " rolled " + randomColor + ".\n");
                bufferedLogWriter.write("==========================================================\n");
                bufferedLogWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            performer.getCommunicator().sendNormalServerMessage("Cant do that");
        }
        Items.destroyItem(target.getWurmId());
        return true;
    }
}