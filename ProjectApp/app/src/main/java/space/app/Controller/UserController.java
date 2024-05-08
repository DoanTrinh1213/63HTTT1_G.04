package space.app.Controller;
import space.app.DAO.UserDAO;
public class UserController implements UserDAO{
    public String hello(){
        return "space/app";
    }
    @Override
    public String Check(){
      return "Hello world";
    };
}
