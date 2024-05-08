package space.app.Controller;
import DAO.UserDAO;
public class UserController implements UserDAO{
    public String hello(){
        return "space/app";
    }
//    @Override
//    public int User1(){
//        return 2;
//    }
    @Override
    public String Check(){
      return "Hello world";
    };
}
