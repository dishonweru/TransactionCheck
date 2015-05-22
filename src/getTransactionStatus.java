
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class getTransactionStatus
 */
@WebServlet("/getTransactionStatus")
public class getTransactionStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public int[] failure_gen = {23,56,77,20,23,50,7,19,43};

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getTransactionStatus() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		String tran_ref, tran_origin;
		try {
			Random randomGenerator = new Random();
			int random_no = randomGenerator.nextInt(100);
			System.out.println("Failure Number: " + random_no);
			
			if (request.getParameter("txn_ref") != null && request.getParameter("txn_origin") != null && !Arrays.asList(failure_gen).contains(random_no)) {
				response.getOutputStream().println("Ok");
			} else {
				response.getOutputStream().println("Error 500");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
