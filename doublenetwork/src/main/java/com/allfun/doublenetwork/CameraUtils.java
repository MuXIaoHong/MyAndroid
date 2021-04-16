package com.allfun.doublenetwork;

import com.arashivision.sdkcamera.camera.InstaCameraManager;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * author：93289
 * date:2020/4/15
 * dsc:
 */
public class CameraUtils {

    //{"fileUrl":"http://192.168.42.1:80/DCIM/Camera01/IMG_20200415_053928_00_670.jpg","_localFileGroup":["/DCIM/Camera01/IMG_20200415_053928_00_670.jpg","/DCIM/Camera01/IMG_20200415_053928_00_671.jpg","/DCIM/Camera01/IMG_20200415_053928_00_672.jpg"],"_fileGroup":["http://192.168.42.1:80/DCIM/Camera01/IMG_20200415_053928_00_670.jpg","http://192.168.42.1:80/DCIM/Camera01/IMG_20200415_053928_00_671.jpg","http://192.168.42.1:80/DCIM/Camera01/IMG_20200415_053928_00_672.jpg"]}
    public class OSCBean {
        @SerializedName("fileUrl")
        @Expose
        String fileUrl;
        @SerializedName("_localFileGroup")
        @Expose
        ArrayList<String> _localFileGroup;
        @SerializedName("_fileGroup")
        @Expose
        ArrayList<String> _fileGroup;

        public String getFileUrl() {
            return fileUrl;
        }

        public ArrayList<String> get_localFileGroup() {
            return _localFileGroup;
        }

        public ArrayList<String> get_fileGroup() {
            return _fileGroup;
        }

        @Override
        public String toString() {
            return "OSCBean{" +
                    "fileUrl='" + fileUrl + '\'' +
                    ", _localFileGroup='" + _localFileGroup + '\'' +
                    ", _fileGroup='" + _fileGroup + '\'' +
                    '}';
        }
    }

    public static boolean isCameraConnected() {
        return InstaCameraManager.getInstance().getCameraConnectedType() != InstaCameraManager.CONNECT_TYPE_NONE;
    }

    public static String startCapture() {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"name\":\"camera.takePicture\"}");
        Request request = new Request.Builder()
                .url(InstaCameraManager.getInstance().getCameraHttpPrefix() + "/osc/commands/execute")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("X-XSRF-Protected", "1")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response != null && response.code() == 200) {
                String jsonString = response.body().string();
                String id = JsonUtils.getString(jsonString, "id");
                return id;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }


    public static OSCBean queryResult(String id) {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n" +
                "\t\"id\": \"" + id + "\"\n" +
                "}");
        Request request = new Request.Builder()
                .url(InstaCameraManager.getInstance().getCameraHttpPrefix() + "/osc/commands/status")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("X-XSRF-Protected", "1")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response != null && response.code() == 200) {
                String jsonString = response.body().string();
                if ("done".equals(JsonUtils.getString(jsonString, "state"))) {
                    JSONObject resultJsonObject = JsonUtils.getJSONObject(jsonString, "results", null);
                    OSCBean oscBean = GsonUtils.fromJson(resultJsonObject.toString(), OSCBean.class);
                    return oscBean;
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static Boolean downFile(final String localPath, String fileUrl) throws IOException {
        if (fileUrl == null) return false;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(fileUrl)
                .addHeader("Connection", "close")
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            InputStream is = null;
            byte[] buf = new byte[2048];
            int len = 0;
            FileOutputStream fos = null;
            // 储存下载文件的目录

            try {
                is = response.body().byteStream();
                long total = response.body().contentLength();
                File file = new File(localPath);
                fos = new FileOutputStream(file);
                long sum = 0;
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                    sum += len;
                    int progress = (int) (sum * 1.0f / total * 100);
                }
                fos.flush();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    if (is != null)
                        is.close();
                } catch (IOException e) {
                }
                try {
                    if (fos != null)
                        fos.close();
                } catch (IOException e) {
                }
            }
        } else {
            return false;
        }
        return true;
    }

    public static int setHDRCaptureMode() {

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n" +
                "\t\"name\":\"camera.setOptions\",\n" +
                "\t\"parameters\":{\n" +
                "\t\t\"options\":{\n" +
                "\t\t\t\"captureMode\": \"image\",\n" +
                "\t\t\t\"hdr\": \"hdr\",\n" +
                "\t\t\t\"exposureBracket\":{\n" +
                "\t\t\t\t\"shots\": 3,\n" +
                "\t\t\t\t\"increment\": 2\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}");


        try {
            Request request = new Request.Builder()
                    .url(InstaCameraManager.getInstance().getCameraHttpPrefix() + "/osc/commands/execute")
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .addHeader("X-XSRF-Protected", "1")
                    .build();
            Response response = client.newCall(request).execute();
            if (response != null && response.code() == 200) {
                String jsonString = response.body().string();
                if ("done".equals(JsonUtils.getString(jsonString, "state"))) {
                    return 0;
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return -1;
    }

}
