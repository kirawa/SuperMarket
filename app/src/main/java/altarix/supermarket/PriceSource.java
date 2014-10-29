package altarix.supermarket;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class PriceSource {


    PriceListProvider dataSource;
    Cursor cursor;
    List<PriceList> priceList;
    String name,cost;
    int type,count;
    long _id;

    public List<PriceList> getPriceList(Context context){
        priceList = new ArrayList<PriceList>();
        dataSource = new PriceListProvider();
        dataSource.onCreate();
        cursor = context.getContentResolver().query(PriceListProvider.PRICE_CONTENT_URI,null,null,null,null);
        if (cursor!=null){
            while (cursor.moveToNext()){
               _id = cursor.getLong(cursor.getColumnIndex(DBHelper.ID));
               name =  cursor.getString(cursor.getColumnIndex(DBHelper.NAME));
               type = cursor.getInt(cursor.getColumnIndex(DBHelper.TYPE));
               cost =  cursor.getString(cursor.getColumnIndex(DBHelper.COST));
               count = cursor.getInt(cursor.getColumnIndex(DBHelper.COUNT));
               priceList.add(new PriceList(_id,name,cost,type,count));
            }
            cursor.close();
       }
        return priceList;
    }
}
