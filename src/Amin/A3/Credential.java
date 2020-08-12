package Amin.A3;

public class Credential {

    private String url;
    private String user;
    private String password;

    public Credential() {
        this.url = "jdbc:mysql://localhost:3306/employees" +
                "?useUnicode=true&useJDBCCompliantTimezoneShift=" +
                "true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        this.user = "root";
        this.password = "123456";

    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
