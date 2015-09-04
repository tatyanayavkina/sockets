/**
 * Created by Татьяна on 04.09.2015.
 */
public class UtilityMessage {
    private StatusCodes code;

    public enum StatusCodes{
        AUTHORIZED(200, ""),
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
