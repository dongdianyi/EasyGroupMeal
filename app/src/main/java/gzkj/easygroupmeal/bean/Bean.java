package gzkj.easygroupmeal.bean;


import java.io.Serializable;
import java.util.List;

//得到消息数据
public class Bean implements Serializable {
    private String session;
    private String mode;
    private String body;
    private String digest;
    /**
     * result : 0
     * filePath : ["http://39.98.191.64/default_pic/1558415411249.jpg"]
     */

    private List<String> filePath;

    public Bean() {
    }

    public Bean(String session, String mode, String body, String digest) {
        this.session = session;
        this.mode = mode;
        this.body = body;
        this.digest = digest;
    }


    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    public List<String> getFilePath() {
        return filePath;
    }
}
