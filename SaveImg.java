package jsoup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

public class SaveImg implements Runnable{
	
	private String url;
	private String picName;
	
	public SaveImg(String url, String picName) {
		this.url = url;
		this.picName = picName;
	}

	public void uploadImg(String url,String picName) throws IOException {
		String path = "D:/workspace/CodeJavaNetWork/img/" + picName+".jpg";
        Response response = Jsoup.connect(url).ignoreContentType(true) // 获取图片需设置忽略内容类型
                .userAgent("Mozilla").method(Method.GET).timeout(10000).execute();
     
        byte[] bytes = response.bodyAsBytes();  //获取图片的byte[]
        saveFile(path, bytes);
        System.out.println("图片保存到：" + path);
    }

	 public void saveFile(String filename, byte[] data) {

	        if (data != null) {
	            String filepath = filename;
	            File file = new File(filepath);
	            if (file.exists()) {
	                file.delete();
	            }
	            try {
	                FileOutputStream fos = new FileOutputStream(file);
	                fos.write(data, 0, data.length);
	                fos.flush();
	                fos.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }

	        }
	    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			uploadImg(url,picName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
