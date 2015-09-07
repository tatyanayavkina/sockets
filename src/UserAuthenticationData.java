import javax.jws.soap.SOAPBinding;
import java.io.Serializable;

/**
 * Created on 07.09.2015.
 */
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
