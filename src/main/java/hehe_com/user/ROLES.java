package hehe_com.user;

public enum ROLES {

    USER("USER"),
    ADMIN("ADMIN");
    private String value;

    ROLES(String value) {
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ROLE-"+value;
    }
}
