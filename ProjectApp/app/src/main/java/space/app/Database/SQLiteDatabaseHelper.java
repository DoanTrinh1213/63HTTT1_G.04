package space.app.Database;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;


public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "my_app";
    private static final int DATABASE_VERSION = 1;
    public SQLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {
        db.execSQL("Create table searchResult(id INTEGER primary key autoincrement,searchQuery TEXT)");
        db.execSQL("Create table cafe(idCafe TEXT primary key,resName TEXT,address TEXT,describe TEXT,price FLOAT,menu TEXT,timeOpen TEXT,contact TEXT,images TEXT,evaluate TEXT,link TEXT,purpose TEXT)");
        db.execSQL("Create table user(idUser TEXT PRIMARY KEY,Email TEXT,image TEXT,describe TEXT)");
    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS searchResult");
        db.execSQL("DROP TABLE IF EXISTS cafe");
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }
}
