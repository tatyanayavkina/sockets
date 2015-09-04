import java.io.Serializable;

/**
 * Created by ������� on 04.09.2015.
 */
public class UtilityMessage implements Serializable{
    private StatusCodes code;

    public enum StatusCodes{
        AUTHORIZED(200, "You are welcome!"),
        NONAUTHORIZED(401, "Login/password error");

        private int code;
        private String description;

        StatusCodes(int code, String description){
            this.code = code;
            this.description = description;
        }

        public int getCode(){
            return code;
        }

        public String getDescription(){
            return description;
        }

    }

    public UtilityMessage(StatusCodes code){
        this.code = code;
    }

    public StatusCodes getCode(){
        return code;
    }
}
