package space.app.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import space.app.Database.Entity.UserEntity;

@Dao
public interface UserDAO {
    @Insert
    void InsertUser(UserEntity user);
    @Delete
    void DeleteUser(UserEntity user);
    @Query("Delete from users")
    void DeleteAllUser();
    @Query("Select * from users where idUser = :idUser")
    LiveData<UserEntity> getUserById(String idUser);

    @Query("Select * from users where email = :email")
    LiveData<UserEntity> getUserByEmail(String email);
}
