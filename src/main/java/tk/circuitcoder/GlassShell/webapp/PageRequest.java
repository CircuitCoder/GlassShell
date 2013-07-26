package tk.circuitcoder.GlassShell.webapp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * Servlet used to deal with page GET request
 * This Servlet is called when page requests come (all requests that fits pattern "/page/*")<br/>
 * It will check if the requested page is exist/resisted(PageDB) and available for the specific user(UserDB and GroupDB)<br/>
 * Then it will forward requests to the selected page, base on the user's permissions and the current server status.
 * @see tk.circuitcoder.GlassShell.server.ServerContainer
 * @author circuitcoder
 *
 */
@WebServlet(urlPatterns="/page/*")
public class PageRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * The GET method.. still a "HelloWorld" now
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		try {
			PrintWriter output=resp.getWriter();
			
			output.println("<!DOCTYPE html><head><title>Hello GlassShell!</title></head>"
					+ "<body><p style=\"color:gray; text-align:center; font-size:2em;\">Word In Progress...</p></body></html>");
			
			output.flush();
			
		} catch (IOException e) {
			System.out.println("Exceptions occured when try to get pages. Shouldn't Happen...");
			e.printStackTrace();
		}
	}
}
