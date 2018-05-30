package com.timemachine.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.timemachine.model.DBEvent;

import java.sql.SQLException;


public class DBHelper  extends OrmLiteSqliteOpenHelper {
    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "timemachine.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the Friend datatable
    private Dao<DBEvent, Integer> dbEventDao = null;
    private RuntimeExceptionDao<DBEvent, Integer> dbFriendRuntimeDao = null;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DBHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, DBEvent.class);
        } catch (SQLException e) {
            Log.e(DBHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        
    }

    public void onUpgradeOld(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
/*
        try {
            Log.i(DBHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, DBSetting.class, true);
            TableUtils.dropTable(connectionSource, DBMessage.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DBHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }


        try {
            while(oldVersion < newVersion){
                switch(oldVersion){
                    case 1:
                        // setting table
                        // add username
                        DaoManager.createDao(connectionSource, DBSetting.class).executeRaw(
                            "ALTER TABLE `setting` ADD COLUMN username VARCHAR"
                        );

                        DaoManager.createDao(connectionSource, DBMessage.class).executeRaw(
                                "UPDATE `setting` SET username = 'me'"
                        );

                        // message table
                        // add likes
                        DaoManager.createDao(connectionSource, DBMessage.class).executeRaw(
                                "ALTER TABLE `message` ADD COLUMN likes INTEGER"
                        );

                        DaoManager.createDao(connectionSource, DBMessage.class).executeRaw(
                                "UPDATE `message` SET likes = 0"
                        );

                        // add followers
                        DaoManager.createDao(connectionSource, DBMessage.class).executeRaw(
                                "ALTER TABLE `message` ADD COLUMN followers INTEGER"
                        );

                        DaoManager.createDao(connectionSource, DBMessage.class).executeRaw(
                                "UPDATE `message` SET followers = 0"
                        );

                        // add created by
                        DaoManager.createDao(connectionSource, DBMessage.class).executeRaw(
                                "ALTER TABLE `message` ADD COLUMN createdBy VARCHAR"
                        );

                        DaoManager.createDao(connectionSource, DBMessage.class).executeRaw(
                                "UPDATE `message` SET createdBy = 'me'"
                        );


                        break;
                    case 2:
                        // message table
                        // add beforeType
                        DaoManager.createDao(connectionSource, DBMessage.class).executeRaw(
                                "ALTER TABLE `message` ADD COLUMN beforeType INTEGER"
                        );

                        DaoManager.createDao(connectionSource, DBMessage.class).executeRaw(
                                "UPDATE `message` SET beforeType = 1"
                        );

                        // add afterType
                        DaoManager.createDao(connectionSource, DBMessage.class).executeRaw(
                                "ALTER TABLE `message` ADD COLUMN afterType INTEGER"
                        );

                        DaoManager.createDao(connectionSource, DBMessage.class).executeRaw(
                                "UPDATE `message` SET afterType = 1"
                        );


                        break;
                    case 3:
                        // message table
                        // add beforeFilenameThumb
                        DaoManager.createDao(connectionSource, DBSetting.class).executeRaw(
                                "ALTER TABLE `message` ADD COLUMN beforeFilenameThumb VARCHAR"
                        );

                        DaoManager.createDao(connectionSource, DBMessage.class).executeRaw(
                                "UPDATE `message` SET beforeFilenameThumb = ''"
                        );

                        // add afterFilenameThumb
                        DaoManager.createDao(connectionSource, DBSetting.class).executeRaw(
                                "ALTER TABLE `message` ADD COLUMN afterFilenameThumb VARCHAR"
                        );

                        DaoManager.createDao(connectionSource, DBMessage.class).executeRaw(
                                "UPDATE `message` SET afterFilenameThumb = ''"
                        );
                        break;
                    case 4:
                        // message table
                        // add radius
                        DaoManager.createDao(connectionSource, DBSetting.class).executeRaw(
                                "ALTER TABLE `message` ADD COLUMN radius INTEGER"
                        );

                        DaoManager.createDao(connectionSource, DBMessage.class).executeRaw(
                                "UPDATE `message` SET radius = 0"
                        );

                    case 5:

                        break;
                    case 6:
                        // message table
                        // add isGPSUnlocked
                        DaoManager.createDao(connectionSource, DBSetting.class).executeRaw(
                                "ALTER TABLE `message` ADD COLUMN isGPSUnlocked INTEGER"
                        );

                        DaoManager.createDao(connectionSource, DBMessage.class).executeRaw(
                                "UPDATE `message` SET isGPSUnlocked = 0"
                        );

                        break;
                    case 7:
                        // settings table
                        // add latitude
                        DaoManager.createDao(connectionSource, DBSetting.class).executeRaw(
                                "ALTER TABLE `setting` ADD COLUMN latitude BIGINT"
                        );

                        DaoManager.createDao(connectionSource, DBMessage.class).executeRaw(
                                "UPDATE `setting` SET latitude = 0"
                        );
                        // add longitude
                        DaoManager.createDao(connectionSource, DBSetting.class).executeRaw(
                                "ALTER TABLE `setting` ADD COLUMN longitude BIGINT"
                        );

                        DaoManager.createDao(connectionSource, DBMessage.class).executeRaw(
                                "UPDATE `setting` SET longitude = 0"
                        );

                        break;
                    default:
                        break;
                }
                oldVersion++;
            }
        } catch (SQLException e) {
            Log.e(DBHelper.class.getName(), "Can't upgrade database", e);
            throw new RuntimeException(e);

            // worst case scenario is drop everything and reload
        }


        dao.executeRaw("ALTER TABLE `account` ADD COLUMN hasDog BOOLEAN DEFAULT false;");
        dao.updateRaw("UPDATE `account` SET hasDog = true WHERE dogCount > 0;");
*/
    }

    /**
     * Returns the Database Access Object (DAO) for our DBSetting class. It will create it or just give the cached
     * value.
     */
    public Dao<DBEvent, Integer> getDBEventDao() throws SQLException {
        if (dbEventDao == null) {
            dbEventDao = getDao(DBEvent.class);
        }
        return dbEventDao;
    }



    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our DBSetting class. It will
     * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
     */
    public RuntimeExceptionDao<DBEvent, Integer> getDBFriendDataDao() {
        if (dbFriendRuntimeDao == null) {
            dbFriendRuntimeDao = getRuntimeExceptionDao(DBEvent.class);
        }
        return dbFriendRuntimeDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        dbEventDao = null;
        super.close();
    }
}
