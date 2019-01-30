// Author: J.M.Montañana HLRS 2018
// If you find any bug, please notify to hpcjmont@hlrs.de
// 
// Copyright (C) 2018 University of Stuttgart
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// 	http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package demo_websocket_execmanager;

import java.net.URISyntaxException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
public class dm_loop_susc_execmanager {
	public static String process_response(BufferedReader is) {
		String result ="";
		try {
			String inputLine;
			while ((inputLine = is.readLine()) != null) {
				result=result+inputLine;
			}
			is.close();
		} catch (IOException e) {
			System.out.println("IOException: " + e);
		}
		return result;
	}

	public static String request_server(String serveraddress, String serverport, String table_ini) throws IOException {
		String table=table_ini.replaceAll(" ","%20");
		String retmonmetric = new String();
		String urlString = new String();
		String responsestring = new String();
		urlString = "http://"+serveraddress+":"+serverport+table;
		URL httpurl = new URL(urlString);
		HttpURLConnection c = (HttpURLConnection)httpurl.openConnection();//connecting to url
		c.setRequestProperty( "Content-type", "application/x-www-form-urlencoded");
		c.setRequestProperty( "Accept", "*/*");
		c.setRequestMethod("GET");
		BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
		String str;
		while ((str = in.readLine()) != null){ //reading data
			responsestring += str;//process the response and save it in some string or so
		}
		in.close();//closing stream
		c.disconnect();
		return responsestring;
	}

	public static void main( String[] args) throws URISyntaxException {
	// GLOBAL VARIABLES AND CONSTANTS
		final String serveraddress="localhost";
		final String serverport= "8700";//execution manager
		final String user_id="montana@abc.com";
		final String user_pw="new";
		final String boundary = "*****";
		String token="";
		String exec_id="";
		// ***** REQUEST FOR A TOKEN **************************************
		try{
			token = request_server(serveraddress, serverport, "/login?email="+user_id+"&pw="+user_pw);
		} catch (IOException e){
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("token is :"+token+"[]\n"); //it returns the string token.
	// ***** LOOP FOR FIND PENDING EXECUTIONS **************************************
		int count = 1;
		do {
			System.out.println("Count is: " + count);
			count++;
			String requestURL = "http://"+serveraddress+":"+serverport+"/older_pending_execution";
			try {
				// Open a HTTP connection to the URL
				URL url = new URL(requestURL);
				HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
				httpConn.setUseCaches(false);
				httpConn.setDoOutput(true);
				httpConn.setDoInput(true);
				httpConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
				httpConn.setRequestMethod("GET");
				httpConn.setRequestProperty("Authorization", "OAuth " + token);
				BufferedReader br;
				if (200 <= httpConn.getResponseCode() && httpConn.getResponseCode() <= 299) {// this range should be for accepted request
					br = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
					exec_id=process_response(br);
					System.out.println("Received execution_id: "+exec_id+"\n");
				} else { // we got error from the execution manager
					br = new BufferedReader(new InputStreamReader(httpConn.getErrorStream()));
					String response=process_response(br);
					System.out.println("ERROR: Not received reposnse, "+response);
					System.exit(2);
				}
			} catch (IOException ex) {
				System.err.println(ex);
				System.exit(3);
			}
			try{
				Thread.sleep(500);// 0.5 secs in milliseconds
			}
			catch(InterruptedException e){
				System.out.println("thread interrupted");
			}
		} while (count < 11);
	}
}
