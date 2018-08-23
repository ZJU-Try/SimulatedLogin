package jsoup;


import org.jsoup.nodes.Element;


public class GetInfo implements Runnable{
	private Element element;
	
	public GetInfo(Element element) {
		this.element = element;
	}
	
	
	public void Info() {
		element.attr("title");

		
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
