package space.app.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import space.app.Database.Entity.UserEntity;
import space.app.Repository.CafeRepository;
import space.app.Repository.UserRepository;

public class UserViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }
    public LiveData<UserEntity> getUserById(String iduser){
        return userRepository.getUserById(iduser);
    }
}
