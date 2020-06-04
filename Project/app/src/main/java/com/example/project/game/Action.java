package com.example.project.game;

import java.util.StringTokenizer;

public abstract class Action {

    private interface ActionCreate {
        Action create();
    }

    private static class ActionDefine {
        String mLongName;
        String mShortName;
        ActionCreate mCreator;

        public ActionDefine(String longName, String shortName, ActionCreate creator) {
            mLongName = longName;
            mShortName = shortName;
            mCreator = creator;
        }

        public Action create() {
            return mCreator.create();
        }

        public String getLongName() {
            return mLongName;
        }

        public String getShortName() {
            return mShortName;
        }
    }

    private static final ActionDefine[] actions = {
            new ActionDefine(SetCellValueAction.class.getSimpleName(), "c4",
                    SetCellValueAction::new)
    };

    public static Action stringToAction(StringTokenizer data) {
        String actionShortName = data.nextToken();
        for (ActionDefine actDef : actions) {
            if (actDef.getShortName().equals(actDef)) {
                Action action = actDef.create();
                return action;
            }
        }
        throw new IllegalArgumentException(String.format("Invalid action name: '%s'.", actionShortName));
    }

    protected void _stringToAction(StringTokenizer data) {

    }

    public void actionToString(StringBuilder data) {
        String actionLongName = getActionClass();
        for (ActionDefine actDef : actions) {
            if (actDef.getLongName().equals(actionLongName)) {
                data.append(actDef.getShortName()).append("|");
                return;
            }
        }
        throw new IllegalArgumentException(String.format("Invalid action name '%s'.", actionLongName));
    }

    public String getActionClass() {
        return getClass().getSimpleName();
    }

    abstract void execute();

    abstract void undo();
}
