package com.timemachine.database;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.timemachine.model.DBEvent;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Jeri on 13/05/2014.
 */
public class DBEventRepository {
    private DBHelper db;
    Dao<DBEvent, Integer> dbEventDao;

    public DBEventRepository(Context ctx)
    {
        try {
            DBManager dbManager = new DBManager();
            db = dbManager.getHelper(ctx);
            dbEventDao = db.getDBEventDao();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }

    }

    public int create(DBEvent obj)
    {
        try {
/*
            if(get(obj.getEventId()) == null)
                return dbEventDao.create(obj);
            else
                return update(obj);
*/
            return dbEventDao.create(obj);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }
    public int update(DBEvent obj)
    {
        try {
            return dbEventDao.update(obj);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(DBEvent obj)
    {
        try {
            return dbEventDao.delete(obj);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public void deleteAll()
    {
        try {
            //QueryBuilder qb = dbMessageDao.queryBuilder();
            //qb.where().eq("trending", true);

            //PreparedQuery<DBMessage> preparedQuery = qb.prepare();
            //dbMessageDao.delete(dbMessageDao.query(preparedQuery));
            dbEventDao.queryRaw(
                    "delete from event");

        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
    }

    public void deleteOnlyDeleted()
    {
        try {
            //QueryBuilder qb = dbMessageDao.queryBuilder();
            //qb.where().eq("trending", true);

            //PreparedQuery<DBMessage> preparedQuery = qb.prepare();
            //dbMessageDao.delete(dbMessageDao.query(preparedQuery));
            dbEventDao.queryRaw(
                    "delete from addressbook where requestStatus = 0");

        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
    }

    public DBEvent get(int id)
    {
        try {
            QueryBuilder qb = dbEventDao.queryBuilder();
            qb.where().eq("id", id);
            PreparedQuery<DBEvent> preparedQuery = qb.prepare();
            // query for all accounts that have "qwerty" as a password
            if(dbEventDao.query(preparedQuery).size() > 0)
                return dbEventDao.query(preparedQuery).get(0);

            //return dbMessageDao.queryForAll();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

    public List getAll()
    {
        try {
            return dbEventDao.queryForAll();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

    public List getAll(int start, int icon)
    {
        try {
            int diff = 1;
            if(start == 1860)
                diff = 100;

            QueryBuilder qb = dbEventDao.queryBuilder();
            qb.where().gt("Decade", start-diff).and().lt("Decade", start+10).and().eq("icon", icon);

            // prepare our sql statement
            PreparedQuery<DBEvent> preparedQuery = qb.prepare();
            return dbEventDao.query(preparedQuery);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

    public List getNext(long page, long size)
    {
        try {
            QueryBuilder qb = dbEventDao.queryBuilder();
            //qb.where().eq("requestStatus", 2);
            qb.orderBy("requestStatus", true);
            qb.orderBy("displayName", true);
            qb.offset(page*size);
            qb.limit(size);

            // prepare our sql statement
            PreparedQuery<DBEvent> preparedQuery = qb.prepare();
            return dbEventDao.query(preparedQuery);

            //return dbMessageDao.queryForAll();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

}
