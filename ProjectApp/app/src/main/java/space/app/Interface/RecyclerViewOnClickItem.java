package space.app.Interface;

import android.net.Uri;

import space.app.Database.Entity.SearchResultEntity;
import space.app.Model.Cafe;

public interface RecyclerViewOnClickItem {
    default void onItemClickCafe(Cafe cafe) {
    }

    default void onItemClickImage(Uri uri) {
    }
}
