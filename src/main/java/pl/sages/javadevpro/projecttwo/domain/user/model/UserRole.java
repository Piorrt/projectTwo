package pl.sages.javadevpro.projecttwo.domain.user.model;

public enum UserRole {

    ADMIN ("ADMIN"),
    STUDENT ("STUDENT");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
