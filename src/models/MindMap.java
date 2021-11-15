package models;

public class MindMap {
    /*
     * Each MindMap represents a node in a MindMap tree structure
     * Root nodes will have a parent_id of 0
     * All MindMaps belong to a user, which they can refer to by
     * user_id.
     */
    private Integer id;
    private Integer parent_id;
    private String topic;
    private Integer user_id;

    public MindMap() {
    }

    public MindMap(Integer id, Integer parent_id, String topic, Integer user_id) {
        this.id = id;
        this.parent_id = parent_id;
        this.topic = topic;
        this.user_id = user_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parent_id;
    }

    public void setParentId(Integer parent_id) {
        this.parent_id = parent_id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getUserId() {
        return user_id;
    }

    public void setUserId(Integer user_id) {
        this.user_id = user_id;
    }
}
