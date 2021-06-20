package kr.hs.mirim.doing;

import java.util.ArrayList;
import java.util.Comparator;

class Descending extends ArrayList<MyFriendList> implements Comparator<MyFriendList>{
    public int compare(MyFriendList a, MyFriendList b)
    {
        Integer c1 =a.getLevel();
        Integer c2 =b.getLevel();
        return c1.compareTo(c2);
    }
}