package models;

public class User {
    /*
    * Kept the model simple, in a real application the user would
    * have a password and more information related to it.
    */
    private Integer id;
    private String user_name;

    public User() {
    }

    public User(String name) {
        this.user_name = name;
    }

    public User(Integer id, String name) {
        this.id = id;
        this.user_name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String name) {
        this.user_name = name;
    }

}