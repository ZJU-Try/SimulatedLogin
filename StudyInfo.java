package jsoup;
import java.io.IOException;
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

public class StudyInfo {

	public List<Map<String, String>> getCookiesAndDatas(String url,String url2, String username, String pwd) throws IOException{
		Map<String, String> cookies = new HashMap<String, String>();
		Map<String, String> headers = new HashMap<String, String>();
		Map<String, String> datas = new HashMap<String, String>();
		List<Map<String, String>> maps = new ArrayList<Map<String,String>>();
		headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		headers.put("Accept-Encoding", "gzip, deflate, sdch");
		headers.put("Accept-Language", "zh-CN,zh;q=0.8");
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
		
		Connection connection = Jsoup.connect(url);
		
		connection.headers(headers);
		
		Response response = connection.execute();//获取响应  
	    Document d1=Jsoup.parse(response.body());//转换为Dom树  
	    List<Element> et= d1.select(".form-signin");//获取form表单，可以通过查看页面源码代码得知  
	          
	       //获取，cooking和表单属性，下面map存放post时的数据   
	        
	    for(Element e:et.get(0).getAllElements()){  
	    	if(e.attr("name").equals("j_username")){  
	    		e.attr("value", username);//设置用户名  
	    	}  
	             
	    	if(e.attr("name").equals("j_password")){  
	    		e.attr("value",pwd); //设置用户密码  
	    	}  
	             
	    	if(e.attr("name").length()>0){//排除空值表单属性  
	    		datas.put(e.attr("name"), e.attr("value"));    
	    	}  
	    }  
	    
		Connection con2 = Jsoup.connect(url2).ignoreContentType(true).followRedirects(true).method(Method.POST);
		con2.headers(headers);
		con2.data(datas);
		Response res2 = con2.execute();//获取响应  
		
		cookies = res2.cookies();
		System.out.println(datas);
		System.out.println(cookies);
		maps.add(cookies);
		maps.add(datas);
		return maps;
	}
	
	public void showView(String url, List<Map<String, String>> maps) throws IOException {
		//Map<String, String> cookies = new HashMap<String, String>();
		//Map<String, String> datas = new HashMap<String, String>();
		Map<String, String> headers = new HashMap<String, String>();
		//String gradeQueryUrl = "http://urp.cup.edu.cn/student/integratedQuery/scoreQuery/allPassingScores/index";
		//cookies = maps.get(0);
		//datas = maps.get(1);
		
		Map<String, String> cook2 = new HashMap<String, String>();
		cook2.put("JSESSIONID", "abc7h7KDrdkzV6sEdKtuw");
		cook2.put("insert_cookie", "49078854");
		cook2.put("selectionBar", "1183426");
		
		headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		headers.put("Accept-Encoding", "gzip, deflate, sdch");
		headers.put("Accept-Language", "zh-CN,zh;q=0.8");
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
		
		
	    Document document = Jsoup.connect(url).ignoreContentType(true).followRedirects(true).method(Method.POST).headers(headers).cookies(cook2).post();
	   
	    //Document doc = Jsoup.connect(gradeQueryUrl).ignoreContentType(true).followRedirects(true).method(Method.POST).cookies(cookies).post();
	    System.out.println(document.body());
	   // System.out.println(doc.body());
	      
	}
	
	public static void main(String[] args) throws IOException{
	
		String url = "http://urp.cup.edu.cn/login";
		String url2 = "http://urp.cup.edu.cn/j_spring_security_check";
		String username = "2015011425";
		String pwd = "wangyong,,";
		//Map<String, String> cookies = new HashMap<String, String>();
		//Map<String, String> datas = new HashMap<String, String>();
		List<Map<String, String>> maps = new ArrayList<Map<String,String>>();
		StudyInfo test = new StudyInfo();
		maps = test.getCookiesAndDatas(url, url2, username, pwd);

		test.showView(url2, maps);
	}
}
