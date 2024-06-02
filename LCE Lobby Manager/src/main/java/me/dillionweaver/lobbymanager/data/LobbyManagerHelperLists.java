package me.dillionweaver.lobbymanager.data;

public class LobbyManagerHelperLists {
    public static boolean bracketListIncludes(Object[] data, Object contains){
        for(Object d1 : data){
            if(d1 == contains){ return true; }
        }
        return false;
    }
}
