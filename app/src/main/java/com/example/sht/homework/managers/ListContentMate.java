package com.example.sht.homework.managers;

import android.util.Log;

import com.example.sht.homework.baseclasses.User;
import com.example.sht.homework.utils.MyLog;

import java.util.ArrayList;
import java.util.List;

/**该类由于将 list 与 字符串进行配对
 * 检索出符合条件的 List
 * @author fishinwater
 */
public class ListContentMate {

    public static List<User> mate(List<User> mFullList, String fullName){
        if (fullName.equals("")){
            return mFullList;
        }
        List<User> newList = new ArrayList<>();
        for (int i = 0 ; i < mFullList.size() ; i++){
            if (mFullList.get(i).getFullname() != null){
                if (mFullList.get(i).getFullname().contains(fullName)){
                    newList.add(mFullList.get(i));
                }
            }
        }
        return newList;
    }

}
