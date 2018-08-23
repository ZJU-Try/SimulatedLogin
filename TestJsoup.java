package jsoup;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class TestJsoup {

	public List<Map<String, String>> getCookiesAndDatas(String url,String url2, String username, String pwd) throws IOException{
		Map<String, String> cookies = new HashMap<String, String>();
		Map<String, String> headers = new HashMap<String, String>();
		Map<String, String> datas = new HashMap<String, String>();
		List<Map<String, String>> lists = new ArrayList<Map<String,String>>();
		headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		headers.put("Accept-Encoding", "gzip, deflate, sdch");
		headers.put("Accept-Language", "zh-CN,zh;q=0.8");
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
		
		Connection connection = Jsoup.connect(url);
		
		connection.headers(headers);  //设置访问头部header
		
		Response response = connection.execute();//获取响应  
	    Document d1=Jsoup.parse(response.body());//转换为Dom树  
	    List<Element> et= d1.select("#loginForm");//获取form表单，可以通过查看页面源码代码得知  
	          
	       //获取，cooking和表单属性，下面map存放post时的数据   
	        
	    for(Element e:et.get(0).getAllElements()){  
	    	if(e.attr("name").equals("user_name")){  
	    		e.attr("value", username);//设置用户名  
	    	}  
	             
	    	if(e.attr("name").equals("password")){  
	    		e.attr("value",pwd); //设置用户密码  
	    	}  
	             
	    	if(e.attr("name").length()>0){//排除空值表单属性  
	    		datas.put(e.attr("name"), e.attr("value"));    //获取数据信息
	    	}  
	    }  
	    //访问表单提交的URL
		Connection con2 = Jsoup.connect(url2).ignoreContentType(true).followRedirects(true).method(Method.POST);
		con2.headers(headers);
		con2.data(datas);
		Response res2 = con2.execute();//获取响应  

		cookies = res2.cookies();  //获取访问返回的cookie
	
		lists.add(cookies);
		lists.add(datas);
		return lists;  //返回数据和cookie的集合
	}
	
	public void showView(String url, List<Map<String, String>> maps,String getInfoUrl) throws IOException {
		Long timeStart = System.currentTimeMillis();
		
		Map<String, String> cookies = new HashMap<String, String>();
		List<String> imgUrlList = new ArrayList<>();
		List<String> picNamelList = new ArrayList<>();
		//访问目标URL
		String url2 = getInfoUrl;
		cookies = maps.get(0);  //获取传入的cookie

		File goodsInfoFile = new File("D:/workspace/CodeJavaNetWork/src/jsoup/goodsInfo.txt");  //新建记录信息文件
		Writer writer = null;  //写入器
		OutputStream out = new FileOutputStream(goodsInfoFile);
		writer = new OutputStreamWriter(out);
		if(goodsInfoFile.exists()){
			goodsInfoFile.delete();
		}
		
		String title = "图片路径 商品名 容量 单位 价格"+"\n";
		writer.write(title);   //记录文件首行
		
	    Document doc = Jsoup.connect(url2).ignoreContentType(true).followRedirects(true).method(Method.POST).cookies(cookies).timeout(10000).post();
	    
	    Elements elements = doc.getElementsByClass("avatar");
	    for (Element element : elements) {
			Element img = element.child(0).child(0);  //获取图片路径
			String imgUrl= "http://www.youmkt.com"+img.attr("src");
			imgUrlList.add(imgUrl);
			
			Element goods = element.nextElementSibling();  //获取商品名称
			String goodsName = img.attr("title");
			picNamelList.add(goodsName);
			
			Element volumeInfo = goods.nextElementSibling(); //获取商品容量
			String volume = volumeInfo.text();
			
			Element unitInfo = volumeInfo.nextElementSibling();  //获取商品单位
			String unit = unitInfo.text();
			
			Element priceInfo = unitInfo.nextElementSibling();  //获取商品价格
			String price = priceInfo.text();
			
			String info = imgUrl+" "+goodsName+" "+volume+" "+unit+" "+price+"\n";
			System.out.println(info);
	
			writer.write(info);   //记录相关信息
			//new SaveImg(imgUrl, goodsName.substring(0,5)).run();
		}
		writer.flush();
	    writer.close();  //关闭写入器
	    
	    for (int i=0;i<imgUrlList.size();i++) { //多线程完成图片抓取
			Thread thread = new Thread(new SaveImg(imgUrlList.get(i), picNamelList.get(i).substring(0,5)));
			thread.start();
		}
	    long timeEnd = System.currentTimeMillis();
	    long during = timeEnd - timeStart;
	    
	    System.out.println("采集完成花费"+during);
	      
	}
	
	public static void main(String[] args) throws IOException{
	
		String url = "http://www.youmkt.com/user/login";
		String url2 = "http://www.youmkt.com/user/login_auth";
		String getInfoUrl = "http://www.youmkt.com/store/198611/goods_list&res_name=sale&start_price_range=price_all&end_price_range=price_all";
		String username = "luffy2";
		String pwd = "wangyong159111";
		List<Map<String, String>> lists = new ArrayList<Map<String,String>>();
		TestJsoup test = new TestJsoup();
		
		lists = test.getCookiesAndDatas(url, url2, username, pwd);
		test.showView(url2, lists, getInfoUrl);

	}
}
