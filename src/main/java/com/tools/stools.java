package com.tools;


public abstract class stools {
    private stools() {}
    public static String[] RemoveSpace(String[] str){
        int count=0;
        for (int i = 0; i < str.length; i++) {
            if(str[i].equals(""))
            {
                count++;
                for (int j = i; j < str.length-1 ; j++) {
                    str[i]=str[i+1];
                }
            }
        }

        return str;
    }

    //对吃棋处理,RB是红方或黑方的吃棋表


}
