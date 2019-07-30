package br.com.icaro.agende.model;

public class JWToken {

    private String access;
    private String refresh;

    public JWToken() {}

    public JWToken(String access, String refresh) {
        this.access = access;
        this.refresh = refresh;
    }

    public String getAccess() {

        return access;
    }

    public void setAccess(String access) {

        this.access = access;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {

        this.refresh = refresh;
    }

    @Override
    public String toString() {
        return "JWToken {" +
                "\taccess='" + access + "'" +
                ",\n\trefresh='" + refresh + "'" +
                "\n}";
    }

}
