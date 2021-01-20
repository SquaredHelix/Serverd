package me.kristoffer.serverd;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Serverd {

	private ServerSocket server;
	private ThreadPoolExecutor executor;
	private boolean running = true;

	private ArrayList<Runnable<Socket>> interceptList = new ArrayList<Runnable<Socket>>();

	public void start(int port) {
		try {
			server = new ServerSocket(port);
			executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1024);
			executor.submit(() -> {
				while (running) {
					try {
						Socket socket = server.accept();
						executor.submit(() -> {
							interceptList.forEach(runnable -> {
								runnable.run(socket);
							});
						});
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addIntercept(Runnable<Socket> socketRunnable) {
		interceptList.add(socketRunnable);
	}

	public int getThreadCount() {
		return executor.getActiveCount();
	}

}
