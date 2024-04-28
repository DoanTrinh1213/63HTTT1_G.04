package app;
import DAO.UserDAO;
public class UserController implements UserDAO{

    @Override
    public String getAllBook() {
        return "User1";
    }
}
