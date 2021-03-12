package objects;

public class User {

    private String email;
    private String password;
    private String token;
    private String userId; 
    private String firstName;
    private String lastName;

    public User(String firstName,String lastName,String email, String password) {
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.email = email;
        this.password = password;
    }

    public User(String firstName,String lastName,String email, String password, String userId, String token) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.userId = userId;
        this.token = token;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String[] credentials) {
        this.email = credentials[0];
        this.password = credentials[1];
        if(credentials[2]!=null) {
            this.userId = credentials[2];
        }
        if(credentials[3]!=null) {
            this.token = credentials[3];
        }
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
