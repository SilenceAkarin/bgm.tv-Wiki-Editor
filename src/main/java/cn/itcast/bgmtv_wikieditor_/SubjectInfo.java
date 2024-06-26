package cn.itcast.bgmtv_wikieditor_;

public class SubjectInfo {
    public SubjectInfo(int subject_id, String albumtitle, String key_value, String value) {
        this.subject_id = subject_id;
        this.albumtitle = albumtitle;
        this.key_value = key_value;
        this.value = value;
    }

    private int subject_id;
    private String albumtitle;
    private String key_value;
    private String value;

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public String getAlbumtitle() {
        return albumtitle;
    }

    public void setAlbumtitle(String albumtitle) {
        this.albumtitle = albumtitle;
    }

    public String getKey_value() {
        return key_value;
    }

    public void setKey_value(String key_value) {
        this.key_value = key_value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
