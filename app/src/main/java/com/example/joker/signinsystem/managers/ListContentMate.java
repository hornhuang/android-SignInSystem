package com.example.joker.signinsystem.managers;

import com.example.joker.signinsystem.baseclasses.User;

import java.util.ArrayList;
import java.util.List;

/**该类由于将 list 与 字符串进行配对
 * 检索出符合条件的 List
 * @author fishinwater
 */
public class ListContentMate {

    public static List<User> mate(List<User> mList, String fullName){
        List<User> newList = new ArrayList<>();
        for (int i = 0 ; i < mList.size() ; i++){
            if (mList.get(i).getFullname().contains(fullName)){
                newList.add(mList.get(i));
            }
        }
        return newList;
    }

}
