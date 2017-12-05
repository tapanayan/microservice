package com.msn.poc.cart.utility;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;


public class Utility {
	private static final Logger log = LoggerFactory.getLogger(Utility.class);
	public static String makePostCall(String urlParameters,String url,String contentType,String accept) throws IOException{
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
//		con.setRequestProperty("User-Agent", USER_AGENT);
//		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Content-Type", contentType);
	    con.setRequestProperty("Accept", accept);


		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + obj.toString());
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());
		return response.toString();		
	}
	
	public static String makeSecureGetCall(String url) throws IOException{
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + obj.toString());
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());
		return response.toString();		
	}
	
	public static String getRestServiceDataByGET(final String urlStr) throws IOException {
        InputStreamReader inStreamReader = null;
        HttpURLConnection conn = null;
        BufferedReader inReader = null;
        final StringBuilder responseStr = new StringBuilder();
        
        try {
                URL url = new URL(urlStr);
                System.out.println("URL:"+url.toString());
               // Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.cognizant.com", 6050));
               // conn = (HttpURLConnection) url.openConnection(proxy);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                Integer rescode = conn.getResponseCode() ;
                if (rescode != 200) {
                	System.out.println("Call Failed");
                }
                inStreamReader = new InputStreamReader(conn.getInputStream());
                inReader = new BufferedReader(inStreamReader);
                String output;
               
                while ( (output = inReader.readLine()) != null) {
                    responseStr.append(output);
                }
                conn.disconnect();

        }finally {
            if (inReader != null) {
                try {
                    inReader.close();
                }catch (IOException ex) {
                	ex.printStackTrace();
                }
            }
            if (inStreamReader != null) {
                try {
                    inStreamReader.close();
                }catch (IOException ex) {
                	ex.printStackTrace();
                }
            }
            if (null != conn) {
                conn.disconnect();
            }
        }
        
        return responseStr.toString();
    }
	
	public static String objectToJSON(final Object obj) {
        String json = null;
        final ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.writeValueAsString(obj);
        }catch (IOException ex) {
        	log.error("Error while converting to Json"+ex);
        }
        return json;
    }

}
