package me.dillionweaver.tumblegamemode.data;

public class TumbleHelperLists {
    public static boolean bracketListIncludes(Object[] data, Object contains){
        for(Object d1 : data){
            if(d1 == contains){ return true; }
        }
        return false;
    }
}
