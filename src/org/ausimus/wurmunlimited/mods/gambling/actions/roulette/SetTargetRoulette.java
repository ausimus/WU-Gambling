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
import com.wurmonline.server.items.*;
import com.wurmonline.server.players.Player;
import org.ausimus.wurmunlimited.mods.gambling.config.AusConstants;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.creatures.Creature;
import java.util.*;

public class SetTargetRoulette implements WurmServerMod, ItemTypes, MiscConstants, ModAction, BehaviourProvider, ActionPerformer
{

    private static short actionID;
    private static ActionEntry actionEntry;

    public SetTargetRoulette()
    {
        actionID = (short) ModActions.getNextActionId();
        actionEntry = ActionEntry.createEntry(actionID, "Set to Roulette", "setting", new int[]{});
        ModActions.registerAction(actionEntry);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public BehaviourProvider getBehaviourProvider()
    {
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ActionPerformer getActionPerformer()
    {
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public short getActionId()
    {
        return actionID;
    }

    /**
     * {@inheritDoc}
     *
     * @param performer performer representing the instantiation of Creature.
     * @param source    The Item source.
     * @param target    The Item target.
     * @return {@link Collections#singletonList(java.lang.Object) object will = {@link SetTargetRoulette#actionEntry} else is null.}.
     **/
    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target)
    {
        if (performer instanceof Player && target.getTemplateId() == AusConstants.GamblingMachineTemplateID
                && target.getAuxData() != AusConstants.GameModeRoulette)
        {
            return Collections.singletonList(actionEntry);
        }
        else
        {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param act       the act.
     * @param performer performer representing the instantiation of Creature.
     * @param source    The Item source.
     * @param target    The Item target.
     * @param action    Action ID number.
     * @param counter   Timer shit.
     * @return boolean.
     **/
    @Override
    public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter)
    {
        if (performer instanceof Player && target.getTemplateId() == AusConstants.GamblingMachineTemplateID
                && target.getAuxData() != AusConstants.GameModeRoulette)
        {
            target.setAuxData(AusConstants.GameModeRoulette);
            source.setData2(-1);
            source.setColor(-1);
            target.setName(target.getTemplate().getName() + " [Roulette]");
            performer.getCommunicator().sendNormalServerMessage("Set to roulette.");
        }
        else
        {
            performer.getCommunicator().sendNormalServerMessage("Cant do that.");
        }
        return true;
    }
}