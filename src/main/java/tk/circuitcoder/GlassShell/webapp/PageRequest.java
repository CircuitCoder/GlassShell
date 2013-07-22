package tk.circuitcoder.GlassShell.webapp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Generate requested page
 * @author circuitcoder
 *
 */
@WebServlet(urlPatterns="/page/*")
public class PageRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
