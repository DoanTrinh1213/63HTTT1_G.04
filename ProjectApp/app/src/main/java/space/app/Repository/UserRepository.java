package space.app.Repository;
import android.app.Application;


import androidx.lifecycle.LiveData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import space.app.DAO.UserDAO;
import space.app.Database.Entity.UserEntity;
import space.app.Database.DatabaseRoom;
import space.app.Model.User;

public class UserRepository {
    private UserDAO userDao;
    private ExecutorService databaseWriteExecutor;
    public UserRepository(Application application){
        DatabaseRoom databaseRoom = DatabaseRoom.getInstance(application);
        userDao = databaseRoom.userDAO();
        databaseWriteExecutor = Executors.newSingleThreadExecutor();
    }
    public void insertUser(UserEntity user){
        databaseWriteExecutor.execute(() -> {
            userDao.InsertUser(user);
        });
    }
    public void deleteUser(UserEntity user){
        databaseWriteExecutor.execute(()->{
            userDao.DeleteUser(user);
        });
    }
    public void deleteAllUser(){
        databaseWriteExecutor.execute(()->{
            userDao.DeleteAllUser();
        });
    }
    public LiveData<UserEntity> getUserById(String iduser){
        LiveData<UserEntity> data = userDao.getUserById(iduser);
        return data;
    }
    public void convert(User user){
        // conver user nay sang UserEntity
    }

    public LiveData<UserEntity> getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }
}
