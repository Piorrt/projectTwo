package pl.sages.javadevpro.projecttwo.api.user.dto;


// TODO czy to leży w dobrym pakiecie - done
public class CredentialsDto {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
