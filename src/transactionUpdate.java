import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.Timer;
import java.util.Date;


public class transactionUpdate {

	static Connection conn = null;
	Timer timer;
	Toolkit toolkit;
	static PreparedStatement pstmt = null;
	Reader reader = null;
	static String[] param_values = new String[10];
	public static String USER_AGENT = "Mozilla/5.0";
	public static  int counter, success_counter;	
	static Date date;
	
	public transactionUpdate(){
		Reader reader = new Reader();
		date = new Date();
		System.out.println("--------------------------------New Cycle StartTime :"+new Timestamp(date.getTime())+"----------------------------------------------------");
		timer = new Timer();
		timer.schedule(new taskTimer(), 0, 30 * 1000); // subsequent rate
	}

	public static void dbConnect() {
		try {
			param_values = Reader.parameters[0].split("=");
			String url = "jdbc:postgresql://" + param_values[1] + ":"
					+ param_values[2] + "/" + param_values[3];
			Class.forName("org.postgresql.Driver");
			// conn = DriverManager.getConnection( url, param_values[4],
			// param_values[5]);
			conn = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/postgres", "postgres",
					"postgres");
			System.out.println("Database Connection  Created  Successfully!!");

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public static void transactionCheck() throws Exception {
		
		String sql = "Select * from transaction where tran_status ='05'";
		counter = 0;
		success_counter = 0;
		try {				
			String url = Reader.parameters[1];
			
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			//System.out.println("txn Selection  Success!!: "+rs.getFetchSize());

			while (rs.next()) {
				//System.out.println("InSide While Loop!!");
				String request_url = url
						+ "txn_ref="
						+ "\""
						+ rs.getString("tran_ref")
						+ "\""
						+ "&txn_origin="
						+ "\""
						+ rs.getString("tran_origin")
						+ "\"";
				System.out.println("Request Url Is: " + request_url);

				String rsp = sendGet(request_url);
				System.out.println("Response From Web Service: " + rsp);

				if (rsp.contentEquals("Ok")) {
					String update_query = "Update transaction set tran_status = '00' where tran_status = '05' and tran_ref ="+"'"+rs.getString("tran_ref") + "'"+" and tran_origin ="+"'"+ rs.getString("tran_origin")+"'";
					//System.out.println(update_query);
					pstmt = conn.prepareStatement(update_query);
					int update_status = pstmt.executeUpdate();
					if(update_status == 1){
						System.out.println("Successfully Fetched and Updated Transaction Ref "+rs.getString("tran_ref") + "  For user: " + rs.getString("tran_origin"));
						success_counter++;
					}
				}
				counter++;
			}
			System.out.println("Total Un-Checked Transactions Fetched: " + counter + "   Successfully Verified Transactions: "+ success_counter);
			conn.close();
			System.out.println("Database Connection  Closed  Successfully!!");
			System.out.println("--------------------------------Transaction Check Cycle Ends At:"+new Timestamp(date.getTime())+"----------------------------------------------------\n\n\n\n\n\n");
		} catch (Exception ex) {
			ex.printStackTrace();
			conn.close();
			System.out.println("Database Connection  Closed  Successfully!!");
			System.out.println("--------------------------------Transaction Check Cycle Ends At:"+new Timestamp(date.getTime())+"----------------------------------------------------\n\n\n\n\n\n");
		}
		
	}

	public static String sendGet(String url) throws Exception {

		StringBuffer response = new StringBuffer();
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			// add request header
			con.setRequestProperty("User-Agent", USER_AGENT);

			int responseCode = con.getResponseCode();
			if (responseCode == 200) {
				System.out.println("\nSending 'GET' request to URL : " + url);
				System.out.println("Response Code : " + responseCode);

				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
			} else {
				System.out
						.println("Failed To Secure A response From Ian Soft Service with http response code:"
								+ responseCode
								+ "......Check Service Availablity.....");

			}
		} catch (Exception ex) {
			System.out.println("An Error Occurred When Firing HTTP Request URL"
					+ " " + url);
			conn.close();
			System.out.println("Database Connection  Closed  Successfully!!");
			System.out.println("--------------------------------Transaction Check Cycle Ends At:"+new Timestamp(date.getTime())+"----------------------------------------------------\n\n\n\n\n\n");
		}

		// print result
		System.out.println(response.toString());
		return response.toString();

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("--------------------------------Transaction Status Check(Process Will Run At Intervals of 1 Min)----------------------------------------------------");		
		transactionUpdate tupdate = new transactionUpdate();
		}

}
