package me.kristoffer.serverd.http;

import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;

public class HTTPContext {

	private HTTPMethod httpMethod;
	private String path;
	private HashMap<String, String> requestHeaders = new HashMap<String, String>();
	private Socket socket;

	public HTTPContext(HTTPMethod httpMethod, Socket socket) {
		this.httpMethod = httpMethod;
		this.socket = socket;
	}

	public HTTPMethod getHttpMethod() {
		return httpMethod;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setRequestHeader(String key, String value) {
		requestHeaders.put(key, value);
	}

	public String getRequestHeader(String key) {
		return requestHeaders.get(key);
	}

	public Collection<String> getRequestHeaders() {
		return requestHeaders.keySet();
	}

	public Socket getSocket() {
		return socket;
	}

}
