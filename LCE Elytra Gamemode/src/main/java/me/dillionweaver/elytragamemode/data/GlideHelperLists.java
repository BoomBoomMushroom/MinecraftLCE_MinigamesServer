package me.dillionweaver.elytragamemode.data;

public class GlideHelperLists {
    public static boolean bracketListIncludes(Object[] data, Object contains){
        for(Object d1 : data){
            if(d1 == contains){ return true; }
        }
        return false;
    }
}
