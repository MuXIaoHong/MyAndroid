package com.android.oss_speed_test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author：93289
 * date:2020/4/23
 * dsc:
 */
public class EndpointBean  {
    @SerializedName("code")
    @Expose
    public Integer result_code;
    @SerializedName("msg")
    @Expose
    public String result_message;
    @SerializedName("data")
    @Expose
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * endPoint : oss-cn-beijing.aliyuncs.com
         * title : 中国上传节点
         * title_en : China Upload Point
         * bucketName : nest-pjt
         * bucketUrl : nest-pjt.oss-cn-beijing.aliyuncs.com
         * id : 0
         */

        public String endPoint;
        public String title;
        public String title_en;
        public String bucketName;
        public String bucketUrl;
        public int id;
        public boolean isSelect;
        public String speed;

        @Override
        public String toString() {
            return "DataBean{" +
                    "endPoint='" + endPoint + '\'' +
                    ", title='" + title + '\'' +
                    ", title_en='" + title_en + '\'' +
                    ", bucketName='" + bucketName + '\'' +
                    ", bucketUrl='" + bucketUrl + '\'' +
                    ", id=" + id +
                    ", isSelect=" + isSelect +
                    ", speed='" + speed + '\'' +
                    '}';
        }
    }
}
