package com.stcm.sync_access;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.stcm.sync_access.data.MySqlCon;
import com.stcm.sync_access.entity.Access;
import com.stcm.sync_access.entity.AccessResponse;
import com.stcm.sync_access.entity.AccessWS;
import com.stcm.sync_access.entity.Config;
import com.stcm.sync_access.entity.LoginToken;

public class RegistroAccesos {

	/**
	 * @param args
	 * @throws Exception
	 */
	static int version = 104;
	
	public static void main(String[] args) throws Exception {
		
		// Obtener la fecha y hora actual
        LocalDateTime ahora = LocalDateTime.now();

        // Crear un DateTimeFormatter para el formato deseado (YYYY-MM-DD HH:mm:ss.SSS)
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        // Formatear la fecha y hora a un String
        String fechaInicial = ahora.format(formato);

        // Imprimir la fecha y hora formateada
        //System.out.println("Fecha inicial"+fechaInicial);
		
		AccessResponse accessResponse = new AccessResponse();
		MySqlCon localDB = new MySqlCon();
		int limit = 0;
		
		System.out.println("Programa para sincronizar Registro de transacciones de los Validadores Embarcados");

		System.out.println("Version: " + version);

		/*if (args.length != 5) {
			System.out.println("Error en los argumentos de entrada.");
			return;
		} else {
			System.out.println("Configuracion correcta.");
		}
		*/
		String server = String.format("http://%s", "localhost:9093");//El server del WS
		localDB.setServer_name(String.format("jdbc:mysql://%s", "192.168.24.71:3306/mdi_configs?zeroDateTimeBehavior=convertToNull"));
		localDB.setUser_name("hsm_mng");
		localDB.setUser_pass("DDT.metrorrey.2024");
		limit = Integer.valueOf("1000");
		
		//String authJson;
		//LoginToken loginToken = new LoginToken();

		//Config config = new Config();

		boolean backoffice_status = false;
		
		int ms = 1000;
		int min = 60;
		long delay_sync 	=  1 * min * ms; //  1 minuto, dato que se actualiza con el valor de la base de datos
		long delay_config 	=  1 * min * ms; //  1 minuto
		//long delay_token 	=  1 * min * ms; //  1 minuto, dato que se actualiza con el valor de la base de datos
		

		Date time_now = new Date();
		Date time_sync = time_now;
		//Date time_token = time_now;
		Date time_config = time_now;
		int count=0;
		do {
			
			/*
			 * Validación del temporizador para renovar el token del backoffice
			 * */
			/*if (time_now.getTime() >= time_token.getTime()) {
				authJson = AuthHttp(server);
				if (authJson != null) {
					loginToken = AuthJson(authJson);
					backoffice_status = true;
					//System.out.println(authJson);
					time_token = new Date(time_now.getTime() + delay_token);

					System.out.println("Correcta conexion con BackOffice: " + server);
					//System.out.println("Tiempo para renovar token en ms " + time_token.getTime());
				} else {
					backoffice_status = false;
					System.out.println("Error de conexion con BackOffice " + server + " .");
				}
			} else {
				time_now = new Date();
			}

			/*
			 * Sincronizacion de datos desde la base de datos a Backoffice cada N minutos.
			 */

			backoffice_status = true;
			
			if (time_now.getTime() >= time_sync.getTime()) {
				
				try
				{
					localDB.Connect();
					System.out.println("Conexion a base de datos exitosa");
					
					if (time_now.getTime() >= time_config.getTime()) 
					{

						//config = localDB.SelectConfig();
						delay_sync 	= 60 * ms;
						//delay_token = 60 * ms;
						//server 		= config.getServer_url();
						//server 		= "172.30.0.9:9093";
						//version 	= config.getVersion();

						time_config = new Date(time_now.getTime() + delay_config);
					}

					if (backoffice_status == true) {
					// Cuenta la cantidad de transacciones con estatus 0
						//long trans_counters = localDB.CountTransaction();
						//long cdeId = localDB.GetLastCounter() + 1;
						System.out.println("Inicia envio de transacciones a BackOffice");
						
						long cdeId = localDB.getIdCde() + 1;
						List<Access> accessList = localDB.SelectTransaction(cdeId,limit);
						System.out.println("Numero de transacciones a enviar: " + accessList.size());
						
					//	authJson = AuthHttp(server);
						if (true) 
						{
							//loginToken = AuthJson(authJson);
							backoffice_status = true;
							System.out.println("Correcta conexion con BackOffice: " + server);
							
							if (accessList.size() > 0) 
							{
								for(Access access : accessList)
								{
									if (access.getCde() != 0) {

										accessResponse = addAccessPost(server,  access);
										
										if (accessResponse == null) {
											System.out.println("Respuesta nula obtenida del BackOffice.");
											accessResponse = new AccessResponse();
											accessResponse.setStatus(500);
											accessResponse.setIdCde((int)access.getCde()); // long
											accessResponse.setIdCC(0);
											accessResponse.setTransaction(null);
											accessResponse.setFileName(null);
											accessResponse.setCode(500);
											accessResponse.setMessage("ERROR");
											accessResponse.setDescription("Error al enviar la transaccion al BackOffice. No se recibio respuesta.");

										}
										
										localDB.addAccessResponce(accessResponse);
										//trans_counters--;

										/*
										 * Validar la respuesta del accessResponse y cambiar el estado de la
										 * transaccion. Status = 0: estado inicial. Status = 200: transaccion de acceso
										 * posteado correctamente
										 */
										
										localDB.updateIdCde((int)access.getCde());
										//System.out.println("status: " + accessResponse.getStatus());
										switch (accessResponse.getStatus()) {
										
										case 200:
											System.out.println("status: " + accessResponse.getStatus() 
											+ ", idCde = " + access.getCde() 
											+ ", meid = " + access.getMeid()
											+ ", uid_card = " + access.getUidCard()
											+ ", uid_sam = " + access.getUidSam()
											+ ", payment_seq = " + access.getPaymentSeq()
											+ ", date = " + access.getDate()
											+ ".");
											localDB.UpdateTransactionStatus(access, accessResponse.getStatus());
											break;
										/*case 409:
											localDB.UpdateTransactionStatus(access, accessResponse.getStatus());
											break;
										case 400:
											localDB.UpdateTransactionStatus(access, accessResponse.getStatus());*/
										default:
											System.out.println("Error: " + accessResponse.getStatus() 
											+ ", idCde = " + access.getCde()
											+ ", meid = " + access.getMeid()
											+ ", uid_card = " + access.getUidCard()
											+ ", uid_sam = " + access.getUidSam()
											+ ", payment_seq = " + access.getPaymentSeq()
											+ ", date = " + access.getDate()
											+ ".");
											localDB.UpdateTransactionStatus(access, accessResponse.getStatus());
											break;
										}
									}
								}
								System.out.println("Fin de envio de "+ accessList.size() + " transacciones");
							}
						} else {
							backoffice_status = false;
							System.out.println("Error de conexion con BackOffice " + server + " .");
						}
							
						
					}
				}catch(SQLException e)
				{
					System.out.println("Error al conectar a la base de datos: " + e.toString());
				}
				finally
				{
					if (localDB.isConnect()) {
						localDB.Close();
					}
				}
				
				time_sync = new Date(time_now.getTime() + delay_sync);
			} 
			else {
				time_now = new Date();
			}
			++count;
			
			// Obtener la fecha y hora actual
	        LocalDateTime ahora2 = LocalDateTime.now();

	        // Crear un DateTimeFormatter para el formato deseado (YYYY-MM-DD HH:mm:ss.SSS)
	        DateTimeFormatter formato2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

	        // Formatear la fecha y hora a un String
	        String fechaFinal = ahora2.format(formato2);

	        System.out.println("fechaInicial: "+fechaInicial );
	        System.out.println("fechaFinal: "+fechaFinal );

	        
		} while (count==0);
	}

	public static String AuthHttp(String server) throws Exception {
		String url = server + "/api/v1/login";

		URL uri = new URL(url);
		// Abrir la conexion e indicar que sera de tipo GET
		HttpURLConnection conexion = (HttpURLConnection) uri.openConnection();
		conexion.setRequestMethod("POST");
		conexion.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		conexion.setRequestProperty("Accept", "application/json");

		conexion.setDoOutput(true);

		String login = "{\r\n    \"username\":\"APIUSER\",\r\n    \"password\":\"1q2w3e4r5T\"\r\n}";

		try (OutputStream os = conexion.getOutputStream()) {
			byte[] input = login.getBytes("utf-8");
			os.write(input, 0, input.length);
		} catch (Exception e) {
			// Manejar excepcion
			e.printStackTrace();
		}

		try (BufferedReader br = new BufferedReader(new InputStreamReader(conexion.getInputStream(), "utf-8"))) {
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			// System.out.println(response.toString());
			return response.toString();
		} catch (Exception e) {
			// Manejar excepcion
			e.printStackTrace();
		}

		return null;
	}

	public static LoginToken AuthJson(String authJson) {
		Gson gson = new Gson();
		LoginToken loginToken = new LoginToken();

		loginToken = gson.fromJson(authJson, LoginToken.class);

		return loginToken;
	}

	public static AccessResponse addAccessPost(String server, LoginToken loginToken, Access access) throws Exception {
		Gson gson = new Gson();
		AccessWS accessWs = new AccessWS(access);
		AccessResponse accessResponse = new AccessResponse();
		// Esta es la URI/Endpoint que se usa para obtener los request
		String url = server + "/api/v1/coa/add";
		String token = "Bearer " + loginToken.getToken();
		// Crear un objeto de tipo URL
		URL uri = new URL(url);

		// Abrir la conexion e indicar que sera de tipo POST
		HttpURLConnection conexion = (HttpURLConnection) uri.openConnection();
		conexion.setRequestMethod("POST");
		conexion.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		conexion.setRequestProperty("Accept", "application/json");
		conexion.setRequestProperty("Authorization", token);
		// metodo que puede ser utilizado para enviar datos a servidores remotos
		conexion.setDoOutput(true);

		try (OutputStream os = conexion.getOutputStream()) {
			byte[] input = gson.toJson(accessWs).getBytes("utf-8");
			os.write(input, 0, input.length);
		} catch (Exception e) {
			// Manejar excepcion
			e.printStackTrace();
		}

		try (BufferedReader br = new BufferedReader(new InputStreamReader(conexion.getInputStream(), "utf-8"))) {
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			//System.out.println(response.toString());
			// return response.toString();
			accessResponse = gson.fromJson(response.toString(), AccessResponse.class);
			return accessResponse;
		} catch (Exception e) {
			// Manejar excepcion
			e.printStackTrace();
		}
		return null;
	}
	
	public static AccessResponse addAccessPost(String server,  Access access) throws Exception {
		Gson gson = new Gson();
		AccessWS accessWs = new AccessWS(access);
		AccessResponse accessResponse = new AccessResponse();
		// Esta es la URI/Endpoint que se usa para obtener los request
		String url = server + "/api/v1/coa/add2";
		//String token = "Bearer " + loginToken.getToken();
		// Crear un objeto de tipo URL
		URL uri = new URL(url);

		// Abrir la conexion e indicar que sera de tipo POST
		HttpURLConnection conexion = (HttpURLConnection) uri.openConnection();
		conexion.setRequestMethod("POST");
		conexion.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		conexion.setRequestProperty("Accept", "application/json");
		//conexion.setRequestProperty("Authorization", token);
		// metodo que puede ser utilizado para enviar datos a servidores remotos
		conexion.setDoOutput(true);

		try (OutputStream os = conexion.getOutputStream()) {
			byte[] input = gson.toJson(accessWs).getBytes("utf-8");
			os.write(input, 0, input.length);
		} catch (Exception e) {
			// Manejar excepcion
			e.printStackTrace();
		}

		try (BufferedReader br = new BufferedReader(new InputStreamReader(conexion.getInputStream(), "utf-8"))) {
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
		    System.out.println(response.toString());
			// return response.toString();
			accessResponse = gson.fromJson(response.toString(), AccessResponse.class);
			return accessResponse;
		} catch (Exception e) {
			// Manejar excepcion
			e.printStackTrace();
		}
		return null;
	}

}
