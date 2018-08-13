package ganteng.hendrawd.footballmatchschedule.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class MyDatabaseOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "Favorite.db", null, 4) {
    companion object {
        private var instance: MyDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): MyDatabaseOpenHelper {
            if (instance == null) {
                instance = MyDatabaseOpenHelper(ctx.applicationContext)
            }
            return instance as MyDatabaseOpenHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(FavoriteMatch.TABLE_NAME, true,
                FavoriteMatch.Column.ID to TEXT + PRIMARY_KEY,
                FavoriteMatch.Column.TEAM_1_STATISTICS to TEXT,
                FavoriteMatch.Column.TEAM_2_STATISTICS to TEXT,
                FavoriteMatch.Column.DATE to TEXT,
                FavoriteMatch.Column.TIME to TEXT
        )
        db.createTable(FavoriteTeam.TABLE_NAME, true,
                FavoriteTeam.Column.ID to TEXT + PRIMARY_KEY,
                FavoriteTeam.Column.LOGO_URL to TEXT,
                FavoriteTeam.Column.BACKDROP_URL to TEXT,
                FavoriteTeam.Column.NAME to TEXT,
                FavoriteTeam.Column.DESCRIPTION to TEXT,
                FavoriteTeam.Column.FORMED_YEAR to TEXT,
                FavoriteTeam.Column.STADIUM to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(FavoriteMatch.TABLE_NAME, true)
        db.dropTable(FavoriteTeam.TABLE_NAME, true)
    }
}

val Context.database: MyDatabaseOpenHelper
    get() = MyDatabaseOpenHelper.getInstance(applicationContext)