package me.kristoffer.serverd.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Optional;
import java.util.stream.Stream;

import me.kristoffer.serverd.Runnable;

public class HTTPIntercept implements Runnable<Socket> {

	private Runnable<HTTPContext> contextRunnable;

	public HTTPIntercept(Runnable<HTTPContext> contextRunnable) {
		this.contextRunnable = contextRunnable;
	}

	@Override
	public void run(Socket socket) {
		HTTPContext httpContext = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			int lineNumber = 1;
			while (in.ready()) {
				String line = in.readLine();
				if (lineNumber == 1) {
					String requestLine = line;
					String[] request = requestLine.split(" ");
					Optional<HTTPMethod> maybeHttpMethod = Stream.of(HTTPMethod.values())
							.filter(method -> method.toString().equals(request[0])).findFirst();
					if (maybeHttpMethod.isEmpty()) {
						throw new HTTPProtocolException();
					}
					HTTPMethod httpMethod = maybeHttpMethod.get();
					httpContext = new HTTPContext(httpMethod, socket);
					httpContext.setPath(request[1]);
				} else if (!line.equals("")) {
					if (!line.contains(":")) {
						throw new HTTPProtocolException();
					}
					String[] header = line.split(":", 2);
					httpContext.setRequestHeader(header[0], header[1]);
				}
				// Possibly add request body
				lineNumber++;
			}
			if (httpContext == null) {
				throw new HTTPProtocolException();
			}
		} catch (IOException | HTTPProtocolException e) {
			e.printStackTrace();
		}
		contextRunnable.run(httpContext);
	}

}
