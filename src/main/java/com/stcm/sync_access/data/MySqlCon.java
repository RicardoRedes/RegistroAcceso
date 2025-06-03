/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stcm.sync_access.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.stcm.sync_access.entity.Access;
import com.stcm.sync_access.entity.AccessResponse;
import com.stcm.sync_access.entity.Config;

/**
 *
 * @author Isaac Vasconcelos
 */
public class MySqlCon {
	// se inicializa el objeto connect con valor nulo
	public Connection connect = null;

	/*
	 * String server_name = "jdbc:mysql://192.168.24.123:3306/hsm"; String user_name
	 * = "hsm_mng"; String user_pass = "DDT.metrorrey.2019";
	 */

	String server_name;
	String user_name;
	String user_pass;

	public String getServer_name() {
		return server_name;
	}

	public void setServer_name(String server_name) {
		this.server_name = server_name;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_pass() {
		return user_pass;
	}

	public void setUser_pass(String user_pass) {
		this.user_pass = user_pass;
	}

	public Connection Connect() throws ClassNotFoundException, SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// here sonoo is database name, root is username and password
			connect = DriverManager.getConnection(server_name, user_name, user_pass);
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println(e.toString());
		}

		/*
		 * if((connect != null)&&(connect.isValid(5))) return true; else return false;
		 */

		return connect;
	}

	public void Close() throws SQLException {
		connect.close();
	}

	public boolean isConnect() throws SQLException {
		boolean status;
		int time_secconds = 2;
		status = connect.isValid(time_secconds);
		return status;
	}

	public boolean isClosed() throws SQLException {
		boolean status;
		status = connect.isClosed();
		return status;
	}

	public Config SelectConfig() throws SQLException {
		Config config = new Config();
		String selectSql = "SELECT * FROM mdi_configs.`config`";

		PreparedStatement preparedStmt = connect.prepareStatement(selectSql);
		ResultSet rs = preparedStmt.executeQuery();
		while (rs.next()) {
			config.setVersion(rs.getInt(2));
			config.setDelay_token(rs.getLong(3));
			config.setServer_url(rs.getString(4));
			config.setDelay_sync(rs.getLong(5));
		}
		return config;
	}

	public long CountTransaction() throws SQLException {
		String countSql = "SELECT COUNT(meid) FROM mdi_configs.`transactions` as transactions WHERE transactions.status = 0";

		long records = 0;

		PreparedStatement preparedStmt = connect.prepareStatement(countSql);

		ResultSet rs = preparedStmt.executeQuery();

		while (rs.next()) {
			records = rs.getInt(1);
		}

		System.out.println("Numero de transacciones a enviar: " + records);

		return records;
	}

	public long GetLastCounter() throws SQLException {
		String getCounterSql = "SELECT counter FROM  mdi_configs.`transactions` WHERE status <> 0 ORDER BY counter DESC LIMIT 1";

		long counter = 0;

		PreparedStatement preparedStmt = connect.prepareStatement(getCounterSql);

		ResultSet rs = preparedStmt.executeQuery();

		while (rs.next()) {
			counter = rs.getInt(1);
		}

		System.out.println("Last Id: " + counter);

		return counter;
	}

	public long getIdCde() throws SQLException {
		String getCounterSql = "SELECT id_cde " + "FROM mdi_configs.ope_control_ids "
				+ "WHERE description = 'transactions'";

		long counter = 0;

		PreparedStatement preparedStmt = connect.prepareStatement(getCounterSql);

		ResultSet rs = preparedStmt.executeQuery();

		while (rs.next()) {
			counter = rs.getInt(1);
		}

		System.out.println("Last IdCde: " + counter);

		return counter;
	}

	public boolean updateIdCde(int idCde) throws SQLException {
		String updateSql = "UPDATE mdi_configs.ope_control_ids " + "SET id_cde = ? "
				+ "WHERE description = 'transactions'";

		PreparedStatement preparedStmt = connect.prepareStatement(updateSql);
		preparedStmt.setInt(1, idCde);

		int i = preparedStmt.executeUpdate();

		//System.out.println(idCde + " id cde updated");

		preparedStmt.close();

		return true;
	}

	public List<Access> SelectTransaction(long cde, int limit) throws SQLException {

		List<Access> accessItems = new ArrayList<Access>();
		String selectSql = "SELECT counter, id_route, id_station, id_unit, equipment_code, meid, type_title_code, uid_card, logicNumber, uid_sam, fare, payment_seq, balance_ewallet, final_balance_ewallet, balance_trips, final_balance_trips, ticketing_validation_code, latitud, longitud, `date`, status "
				+ "FROM mdi_configs.`transactions` AS transactions WHERE transactions.status = 0 LIMIT ?";

		PreparedStatement preparedStmt = connect.prepareStatement(selectSql);
		preparedStmt.setInt(1, limit);

		ResultSet rs = preparedStmt.executeQuery();

		while (rs.next()) {
			Access access = new Access();
			access.setCde(cde++);
			access.setRouteId(rs.getInt("id_route"));
			access.setStationId(rs.getInt("id_station"));
			access.setUnitId(rs.getInt("id_unit"));
			access.setEquipmentCode(rs.getString("equipment_code"));
			access.setMeid(rs.getString("meid"));
			access.setTypeTitleCode(rs.getString("type_title_code"));
			access.setUidCard(rs.getString("uid_card"));
			access.setLogicNumber(rs.getString("logicNumber"));
			access.setUidSam(rs.getString("uid_sam"));
			access.setFare(rs.getFloat("fare"));
			access.setPaymentSeq(rs.getInt("payment_seq"));
			access.setBalanceEWallet(rs.getFloat("balance_ewallet"));
			access.setFinalBalanceEWallet(rs.getFloat("final_balance_ewallet"));
			access.setBalanceTrips(rs.getInt("balance_trips"));
			access.setFinalBalanceTrips(rs.getInt("final_balance_trips"));
			access.setTicketingValidationCode(rs.getString("ticketing_validation_code"));
			access.setDate(rs.getString("date"));

			accessItems.add(access);
		}
		preparedStmt.close();
		rs.close();

		return accessItems;
	}

	public boolean UpdateTransactionStatus(Access access, int status)  {
		PreparedStatement preparedStmt = null;
		try {
		// UPDATE `mdi_configs`.`transactions` SET `counter` = '200', `status` = '200'
		// WHERE `transactions`.`meid` = 'E80000002D471D' AND `transactions`.`uid_card`
		// = '9203a163' AND `transactions`.`payment_seq` = 1845 AND
		// `transactions`.`date` = '2023-08-29 13:30:58';
		// String updateSql1 = "UPDATE mdi_configs.`transactions` AS transactions SET
		// status=? WHERE id=? ";
		String updateSql = "UPDATE transactions SET counter=?, status=? WHERE meid=? AND uid_card=? AND payment_seq=? AND date=?";
		preparedStmt = connect.prepareStatement(updateSql);

		preparedStmt.setInt(1, (int) access.getCde());
		preparedStmt.setLong(2, status);
		preparedStmt.setString(3, access.getMeid());
		preparedStmt.setString(4, access.getUidCard());
		preparedStmt.setLong(5, access.getPaymentSeq());
		preparedStmt.setString(6, access.getDate());

		int i = preparedStmt.executeUpdate();

		//System.out.println(i + " records updated");

		preparedStmt.close();
		return true;
		
		} catch (SQLException e) {
			
			System.out.println("La transaccion no se pudo actualizar: " + e.getMessage());
			System.out.println("Volviendo a actualizar el estado de la transaccion despues de 5 segundos.");
			try {
				preparedStmt.close();
				Thread.sleep(5000);
			} catch (InterruptedException ie) {
				System.err.println("Thread interrupted: " + ie.getMessage());
			} catch (SQLException e2) {
				System.out.println("Error " + e2.getMessage());
			}
				
			return UpdateTransactionStatus(access, status);
		}
		
		
	}

	public void addAccessResponce(AccessResponse accessResponse) throws SQLException {

		try {

			String insertSql = "INSERT INTO mdi_configs.`transaction_log`"
					+ " (idCde, idCC, `transaction`, fileName, status, code, message, description)"
					+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

			PreparedStatement preparedStmt = connect.prepareStatement(insertSql);

			preparedStmt.setInt(1, accessResponse.getIdCde());
			preparedStmt.setInt(2, accessResponse.getIdCC());
			preparedStmt.setString(3, accessResponse.getTransaction());
			preparedStmt.setString(4, accessResponse.getFileName());
			preparedStmt.setInt(5, accessResponse.getStatus());
			preparedStmt.setInt(6, accessResponse.getCode());
			preparedStmt.setString(7, accessResponse.getMessage());
			preparedStmt.setString(8, accessResponse.getDescription());

			int success = preparedStmt.executeUpdate();

			//System.out.println("The access response was successfully inserted into the database." + success);

			preparedStmt.close();

		} catch (SQLException e) {
			System.out.println("The access response could not be inserted into the database." + e.getMessage());
			System.err.println("Error" + e.getMessage());
		}

	}
}
