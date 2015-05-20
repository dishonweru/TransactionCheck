import java.sql.Timestamp;
import java.util.Date;
import java.util.TimerTask;

public class taskTimer extends TimerTask {

	public void run() {
		Date date_start = new Date();
		try {
					System.out
					.println("---------------------Starting Transaction Check Cycle At:"
							+ " "
							+ new Timestamp(date_start.getTime())
							+ "------------------------------------");
			transactionUpdate.dbConnect();
			transactionUpdate.transactionCheck();
			//performTillUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
