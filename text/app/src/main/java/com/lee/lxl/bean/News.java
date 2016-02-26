package com.lee.lxl.bean;

import java.util.List;

/**
 * Created by lee-lxl on 2016/2/2.
 */
public class News {

    /**
     * status : true
     * errorcode : 0
     * errorinfo : query posts success
     * post : [{"username":"jason","title":"好人一生平安","content":"好人一生平安","id":"15","ctime":"2016-01-14 08:38:31","mtime":"0000-00-00 00:00:00"}]
     */

    private boolean status;
    private int errorcode;
    private String errorinfo;
    /**
     * username : jason
     * title : 好人一生平安
     * content : 好人一生平安
     * id : 15
     * ctime : 2016-01-14 08:38:31
     * mtime : 0000-00-00 00:00:00
     */

    private List<PostEntity> post;

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public void setErrorinfo(String errorinfo) {
        this.errorinfo = errorinfo;
    }

    public void setPost(List<PostEntity> post) {
        this.post = post;
    }

    public boolean isStatus() {
        return status;
    }

    public int getErrorcode() {
        return errorcode;
    }

    public String getErrorinfo() {
        return errorinfo;
    }

    public List<PostEntity> getPost() {
        return post;
    }

    public class PostEntity {
        private String username;
        private String title;
        private String content;
        private String id;
        private String ctime;
        private String mtime;

        public void setUsername(String username) {
            this.username = username;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public void setMtime(String mtime) {
            this.mtime = mtime;
        }

        public String getUsername() {
            return username;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public String getId() {
            return id;
        }

        public String getCtime() {
            return ctime;
        }

        public String getMtime() {
            return mtime;
        }
    }
}
