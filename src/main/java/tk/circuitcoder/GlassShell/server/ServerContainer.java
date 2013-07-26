package tk.circuitcoder.GlassShell.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Class who manages the servers' process life-cycle
 * Launches server instances, linking I/O Stream, and destroy the instances at exit.
 * @author circuitcoder
 *
 */
public class ServerContainer {
	/**
	 * Represent the current server status
	 * filed <code>good</code> decides whether the server is good-to-go now
	 * @author circuitcoder
	 *
	 */
	public enum ServerStatus {
		NOTINSTALLED(false),
		ERROR(false),
		BORKENJAR(false),
		SYSTEMERROR(false),
		STOPED(true),
		RUNNING(true);
		
		private boolean good;
		
		ServerStatus(boolean good) {
			this.good=good;
		}
		
		public boolean isGood() {
			return good;
		}
	}
	
	/**
	 * Redirecting a single input stream to another output stream
	 * @author circuitcoder
	 *
	 */
	public class StreamRedirector extends Thread {
		private BufferedReader is;
		private BufferedWriter os;
		public StreamRedirector(InputStream is,OutputStream os) {
			this.is=new BufferedReader(new InputStreamReader(is));
			this.os=new BufferedWriter(new OutputStreamWriter(os));
		}
		@Override
		public void run() {
			String tmp;
			try {
				while((tmp=is.readLine())!=null) os.write(tmp);
			} catch (IOException e) {
				System.out.println("Something wierd just happened. If you want to help with our development, you can send the log file to us");
				e.printStackTrace();
			} 
		}
	}
	/**
	 * There are ONLY ONE container VM-wide
	 */
	private static ServerContainer container;
	
	/**
	 * Informations & properties for each server.
	 */
	//TODO: bundle into single object & multi-server support & modifiable redirector
	private ServerStatus status;
	private ProcessBuilder serverPBuilder;
	private Process serverInstance;
	
	/**
	 * Create the ServerContainer, initialize the first server instance
	 * @param filePath the path of the server jar file
	 * @param bindir the working directory for the JVM
	 * @param args the arguments for server
	 */
	private ServerContainer(String filePath,String bindir,final String[] args) {
		File serverJarFile=new File(filePath);
		File serverDir=new File(bindir);
		if(!serverJarFile.exists()) {
			status=ServerStatus.NOTINSTALLED;
			return;
		}
		
		if(!serverDir.exists()) {
			if(!serverDir.mkdir()) {
				System.out.println("Error creating server directory... Maybe you should make sure JVM can R/W the directory?");
				status=ServerStatus.SYSTEMERROR;
				return;
			}
		}
		
		System.out.println("Now creating server instance... \nServer Jar: "+serverJarFile.getAbsolutePath());
		
		serverPBuilder=new ProcessBuilder();
		serverPBuilder.directory(serverDir.getAbsoluteFile());
		//TODO: add other arguments
		serverPBuilder.command("java", "-jar" ,serverJarFile.getAbsolutePath());
		
		try {
			serverInstance=serverPBuilder.start();
			
		} catch (IOException e) {
			System.out.println("Oops, unable to create server instance.. \nPlease check if JRE has been already installed");
			status=ServerStatus.SYSTEMERROR;
			e.printStackTrace();
		}
		
		//For testing... redirecting to console
		new StreamRedirector(System.in,serverInstance.getOutputStream()).run();
		new StreamRedirector(serverInstance.getInputStream(),System.out).run();
		new StreamRedirector(serverInstance.getErrorStream(),System.out).run();
		
		if(status==null) status=ServerStatus.RUNNING;
	}
	
	/**
	 * Returns the current status of the server
	 * @return
	 */
	public final ServerStatus status() {
		return status;
	}
	
	/**
	 * Tester.. Will be removed after adding (some kind of) web interface
	 * @param args
	 */
	public static void main(String[] args) {
		ServerContainer container=ServerContainer.container();
		System.out.println(container.status());
	}	
	
	/**
	 * Returns the VM-wide container
	 * @return
	 */
	public static final ServerContainer container() {
		if(container==null) container=new ServerContainer(
				"server"+File.separator+"server.jar",
				"server"+File.separator+"files",
				new String[]{""});
		return container;
	}
}
