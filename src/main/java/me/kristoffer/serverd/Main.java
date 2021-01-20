package me.kristoffer.serverd;
import me.kristoffer.serverd.http.HTTPIntercept;

public class Main {

	public static void main(String[] args) {
		Serverd httpd = new Serverd();
		HTTPIntercept httpIntercept = new HTTPIntercept(context -> {
			System.out.println(context.getHttpMethod());
			System.out.println(context.getPath());
			context.getRequestHeaders().forEach(header -> {
				System.out.println(header + " : " + context.getRequestHeader(header));
			});
		});
		httpd.addIntercept(httpIntercept);
		httpd.start(80);
	}

}
