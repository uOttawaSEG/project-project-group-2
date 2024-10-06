package ca.seg2105project.model;

import android.content.Context;
import android.content.SharedPreferences;

import ca.seg2105project.model.UserRepository;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ca.seg2105project.R;
import ca.seg2105project.model.userClasses.User;

public class LoginSessionRepository {

    public void registerLoginSession(String username, Context applicationContext){

    }



    public static Boolean loginSession(String email, String password, Context applicationContext){
        List<User> users = UserRepository.getAllRegisteredUsers();
        for(int i = 0; i < users.size(); i++){
            if( (Objects.equals(users.get(i).getEmail(), email)) && (Objects.equals(users.get(i).getPassword(), password))){
                return true;
            }
        }
        return false;
    }
}



