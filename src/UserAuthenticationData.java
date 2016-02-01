import java.io.Serializable;

public class UserAuthenticationData implements Serializable{
    private String name;
    private String password;

    public UserAuthenticationData(String name, String password){
        this.name = name;
        this.password = password;
    }

    public String getName(){
        return name;
    }

    public String getPassword(){
        return password;
    }

}
