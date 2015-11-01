package com.verizon.usage.clent;


import java.net.*;
import java.io.*;

public  class SendSms
 {
	public static void sendMessageToDataUsers(String CustomerId, int limit) {
		try {
			if(true)
				return;
			String recipient = CustomerId;//CusomerNo
			String message = "Hello Customer"
					+ "Reached the "+limit+" DataUsuage";
			String username = "admin";
			String password = "admin";
			String originator = "VM-Verizon";

			String requestUrl = "http://127.0.0.1:9501/api?action=sendmessage&" + "username="
					+ URLEncoder.encode(username, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8")
					+ "&recipient=" + URLEncoder.encode(recipient, "UTF-8") + "&messagetype=SMS:TEXT" + "&messagedata="
					+ URLEncoder.encode(message, "UTF-8") + "&originator=" + URLEncoder.encode(originator, "UTF-8")
					+ "&serviceprovider=GSMModem1" + "&responseformat=html";

			URL url = new URL(requestUrl);
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			System.out.println(requestUrl);
			System.out.println(uc.getResponseMessage());

			uc.disconnect();

		} catch (Exception ex) {
			System.out.println(ex.getMessage());

		}
	}

}