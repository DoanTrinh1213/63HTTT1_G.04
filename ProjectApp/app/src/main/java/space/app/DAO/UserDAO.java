package space.app.DAO;

public interface UserDAO {
    public default int User1(){
        return 1;
    }
    public String Check();
}
