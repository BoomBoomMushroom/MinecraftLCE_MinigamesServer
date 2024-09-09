package me.dillionweaver.battlegamemode.data;

public class BattleHelperLists {
    public static boolean bracketListIncludes(Object[] data, Object contains){
        for(Object d1 : data){
            if(d1 == contains){ return true; }
        }
        return false;
    }
}
