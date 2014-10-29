package altarix.supermarket;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper sInstance;

    // Database fields
    public static final String DB_NAME = "supermarket.db";
    public static final int DB_VERSION  = 1;
    public static final String  TABLE_PRICE  = "price";
    public static final String  ID  = "_id";
    public static final String  NAME  = "name";
    public static final String  TYPE  = "type";
    public static final String  COST = "cost";
    public static final String  COUNT = "count";

    // Database creation sql statement
    private static final String DB_CREATE =
            "create table "+TABLE_PRICE+"("+ID+" integer primary key autoincrement," +
            " "+NAME+" text not null, "+TYPE+" integer not null," +
            " "+COST+" text not null,"+COUNT+" integer not null);";

    private static final String DB_DROP = "drop table if exists "+TABLE_PRICE;

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //singleton
    public static DBHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL(DB_DROP);
        onCreate(sqLiteDatabase);
    }

}
