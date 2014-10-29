package altarix.supermarket;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

public class PriceListProvider extends ContentProvider {

    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    SQLiteQueryBuilder queryBuilder;

    public static final String PROVIDER = "altarix.supermarket.provider";
    public static final String PRICE_PATH = "priceList";

    public static final Uri PRICE_CONTENT_URI = Uri.parse("content://"
            + PROVIDER + "/" + PRICE_PATH);

    public static final int URI_PRICE = 1;

    public static final int URI_PRICE_ID = 2;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER, PRICE_PATH, URI_PRICE);
        uriMatcher.addURI(PROVIDER, PRICE_PATH + "/#", URI_PRICE_ID);
    }

    @Override
    public boolean onCreate() {
        dbHelper = DBHelper.getInstance(getContext());
        try {
            sqLiteDatabase = dbHelper.getWritableDatabase();
        }catch (SQLiteException e){
            sqLiteDatabase = dbHelper.getReadableDatabase();
        }
        return true;
    }

    @Override
    public  Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        queryBuilder = new SQLiteQueryBuilder();
        checkColumns(projection);
        queryBuilder.setTables(DBHelper.TABLE_PRICE);
        switch (uriMatcher.match(uri)) {
            case URI_PRICE:
                break;
            case URI_PRICE_ID:
                queryBuilder.appendWhere(DBHelper.ID +"="+ uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        cursor = queryBuilder.query(sqLiteDatabase,projection,selection,selectionArgs,null,null,sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),PRICE_CONTENT_URI);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long id;
        switch (uriMatcher.match(uri)) {
            case URI_PRICE:
                id = sqLiteDatabase.insert(DBHelper.TABLE_PRICE, null, contentValues);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(PRICE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted;
        switch (uriMatcher.match(uri)) {
            case URI_PRICE:
                rowsDeleted = sqLiteDatabase.delete(DBHelper.TABLE_PRICE, selection,selectionArgs);
                break;
            case URI_PRICE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqLiteDatabase
                            .delete(DBHelper.TABLE_PRICE,DBHelper.ID + "=" + id,null);
                } else {
                    rowsDeleted = sqLiteDatabase.delete(DBHelper.TABLE_PRICE,
                            DBHelper.ID + "=" + id+ " and " + selection,selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int rowsUpdated;
        switch (uriMatcher.match(uri)) {
            case URI_PRICE:
                rowsUpdated = sqLiteDatabase
                        .update(DBHelper.TABLE_PRICE,contentValues,selection,selectionArgs);
                break;
            case URI_PRICE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqLiteDatabase
                        .update(DBHelper.TABLE_PRICE,contentValues,DBHelper.ID + "=" + id,null);
                } else {
                    rowsUpdated = sqLiteDatabase
                        .update(DBHelper.TABLE_PRICE,contentValues,DBHelper.ID + "=" + id+ " and "+ selection,selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = {DBHelper.NAME,DBHelper.TYPE,DBHelper.COUNT, DBHelper.COST,DBHelper.ID};
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }

    public  ContentValues createContentValues(String name, int type, String cost, int count) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.NAME,name);
        values.put(DBHelper.TYPE,type);
        values.put(DBHelper.COST,cost);
        values.put(DBHelper.COUNT,count);
        return values;
    }
}
