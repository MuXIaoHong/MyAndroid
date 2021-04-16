package com.allfun.doublenetwork;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * author：93289
 * date:2020/4/23
 * dsc:
 */
public class OSSTokenBean {

    /**
     * data : {"AccessKeySecret":"DYeRKDoFefPB9XGYHzrvLzy9KCGPZrVeav4nuRfR7hN5","SecurityToken":"CAIShgJ1q6Ft5B2yfSjIr5bhHtXxpZlZ4/GgYGregTknYtVngZHvkzz2IH9IfXVsBe4Zs/0+lGpV6v4TlqVoRoReREvCKM1565kPf6gioXiH6aKP9rUhpMCPOwr6UmzWvqL7Z+H+U6muGJOEYEzFkSle2KbzcS7YMXWuLZyOj+wMDL1VJH7aCwBLH9BLPABvhdYHPH/KT5aXPwXtn3DbATgD2GM+qxsmtv/mmJLAu0aF3AamlbVOnemrfMj4NfsLFYxkTtK40NZxcqf8yyNK43BIjvwp0PUYpWuZ4YzEUgkLvkneYvCn+9luPRJ/YbMhB6lHof7zmPt1oOXPkJ7tzBJALWrpn6xq/mHLGoABlubtI6fd0BBSWs7fIMB2EyyjxOEqpwWdto0WcWZGUyhfj7flz8VP8B750NiBShODm34KOSnkbPb1v+anwops2vW1xh3l7KYEffKNXaiZ1umpKgmD2wKuibplZtVBS5/wKSRbeDpXDeusId4QBgz28SOxsa6bm/Sfp1jgNP0v06U=","Expiration":"2020-04-23T04:45:28Z","AccessKeyId":"STS.NUTUoEHFxT3KbLoe9rnZKnTDq"}
     */
    @SerializedName("code")
    @Expose
    public Integer result_code;
    @SerializedName("msg")
    @Expose
    public String result_message;
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * AccessKeySecret : DYeRKDoFefPB9XGYHzrvLzy9KCGPZrVeav4nuRfR7hN5
         * SecurityToken : CAIShgJ1q6Ft5B2yfSjIr5bhHtXxpZlZ4/GgYGregTknYtVngZHvkzz2IH9IfXVsBe4Zs/0+lGpV6v4TlqVoRoReREvCKM1565kPf6gioXiH6aKP9rUhpMCPOwr6UmzWvqL7Z+H+U6muGJOEYEzFkSle2KbzcS7YMXWuLZyOj+wMDL1VJH7aCwBLH9BLPABvhdYHPH/KT5aXPwXtn3DbATgD2GM+qxsmtv/mmJLAu0aF3AamlbVOnemrfMj4NfsLFYxkTtK40NZxcqf8yyNK43BIjvwp0PUYpWuZ4YzEUgkLvkneYvCn+9luPRJ/YbMhB6lHof7zmPt1oOXPkJ7tzBJALWrpn6xq/mHLGoABlubtI6fd0BBSWs7fIMB2EyyjxOEqpwWdto0WcWZGUyhfj7flz8VP8B750NiBShODm34KOSnkbPb1v+anwops2vW1xh3l7KYEffKNXaiZ1umpKgmD2wKuibplZtVBS5/wKSRbeDpXDeusId4QBgz28SOxsa6bm/Sfp1jgNP0v06U=
         * Expiration : 2020-04-23T04:45:28Z
         * AccessKeyId : STS.NUTUoEHFxT3KbLoe9rnZKnTDq
         */

        private String AccessKeySecret;
        private String SecurityToken;
        private String Expiration;
        private String AccessKeyId;

        public String getAccessKeySecret() {
            return AccessKeySecret;
        }

        public void setAccessKeySecret(String AccessKeySecret) {
            this.AccessKeySecret = AccessKeySecret;
        }

        public String getSecurityToken() {
            return SecurityToken;
        }

        public void setSecurityToken(String SecurityToken) {
            this.SecurityToken = SecurityToken;
        }

        public String getExpiration() {
            return Expiration;
        }

        public void setExpiration(String Expiration) {
            this.Expiration = Expiration;
        }

        public String getAccessKeyId() {
            return AccessKeyId;
        }

        public void setAccessKeyId(String AccessKeyId) {
            this.AccessKeyId = AccessKeyId;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "AccessKeySecret='" + AccessKeySecret + '\'' +
                    ", SecurityToken='" + SecurityToken + '\'' +
                    ", Expiration='" + Expiration + '\'' +
                    ", AccessKeyId='" + AccessKeyId + '\'' +
                    '}';
        }
    }
}
