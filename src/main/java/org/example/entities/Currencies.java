package org.example.entities;

public class Currencies {
    int id;
    String code;
    String full_name;
    String sign;

    public Currencies(int id, String code, String fullName, String sign) {
        this.id = id;
        this.code = code;
        this.full_name = fullName;
        this.sign = sign;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return full_name;
    }

    public void setFullName(String fullName) {
        this.full_name = fullName;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", fullName='" + full_name + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
