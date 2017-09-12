package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class Daogenerator {

    public static void main(String[] args) throws Exception {
        int version = 1;
        String defaultJavaPackage = "com.oushangfeng.ounews.greendao";
        Schema schema = new Schema(version, defaultJavaPackage);
        addTable(schema);
        // / ./../
        new DaoGenerator().generateAll(schema, "./app/src/main/java-gen");
    }

    private static void addTable(Schema schema) {
        Entity note = schema.addEntity("NewsChannelTable");
        //        note.addIdProperty();
        /**
         * 频道名称
         */
        note.addStringProperty("newsChannelName").notNull().primaryKey().index();
        /**
         * 频道id
         */
        note.addStringProperty("newsChannelId").notNull();
        /**
         * 频道类型
         */
        note.addStringProperty("newsChannelType").notNull();
        /**
         *
         */
        note.addBooleanProperty("newsChannelSelect").notNull();
        /**
         *
         */
        note.addIntProperty("newsChannelIndex").notNull();
        /**
         * 频道是否是固定的
         */
        note.addBooleanProperty("newsChannelFixed");
    }

}
