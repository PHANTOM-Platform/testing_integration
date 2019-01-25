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

import java.net.URI;
import java.net.URISyntaxException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.exceptions.WebsocketNotConnectedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;

public class dm_ws_susc_execmanager extends WebSocketClient {
// FUNCTIONS FOR GET TOKEN
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
		BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));//stream to resource
		String str;
		while ((str = in.readLine()) != null){ //reading data
			responsestring += str;//process the response and save it in some string or so
		}
		in.close();//closing stream
		c.disconnect();
		return responsestring;
	}

// FUNCTIONS FOR THE WS-SUSCRIPTIONS
	public dm_ws_susc_execmanager( URI serverURI) {
		super(serverURI);
	}

	boolean connection_stablished =false;
	@Override
	public void onOpen( ServerHandshake handshakedata) {
		System.out.println("opened connection");
		connection_stablished=true;
		// if you plan to refuse connection based on ip or httpfields overload: onWebsocketHandshakeReceivedAsClient
	}

	@Override
	public void onMessage( String message ) { // THIS FUNCTION WILL RUN FOR EACH RECEIVED MESSAGE
		System.out.println("received: " + message);
	}

	@Override
	public void onClose( int code, String reason, boolean remote ) {
		// The codecodes are documented in class org.java_websocket.framing.CloseFrame
		System.out.println("Connection closed by " + ( remote ? "remote peer" : "us" ) + " Code: " + code + " Reason: " + reason);
	}

	@Override
	public void onError( Exception ex) {
		ex.printStackTrace();
		// if the error is fatal then onClose will be called additionally
	}

	public static void main( String[] args) throws URISyntaxException {
	// GLOBAL VARIABLES AND CONSTANTS
		final String serveraddress="localhost";
		final String serverport= "8700";//execution manager
		
		final String user_id="montana@abc.com";
		final String user_pw="new";
		final String charset = "UTF-8";
		String token="";
		// ***** REQUEST FOR A TOKEN **************************************
		try{
			token = request_server(serveraddress, serverport, "/login?email="+user_id+"&pw="+user_pw);
		} catch (IOException e){
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("token is :"+token+"[]\n"); //it returns the string token.

		System.out.println("SUSCRIPTION FOR RECEPTION OF NOTIFICATIONS FROM THE EXECUTION MANAGER");
		// ***** SUSCRIPTION FOR RECEPTION OF NOTIFICATIONS FROM THE EXECUTION MANAGER
		String serverlocation = "ws://" + serveraddress + ":" + serverport + "/";
		WebSocketClient ws_client = null;
		try {
			ws_client = new dm_ws_susc_execmanager( new URI( serverlocation )); // We create a WS connection
		} catch (URISyntaxException e) {
			e.printStackTrace();
			System.exit(4);
		}
		try {
			ws_client.connectBlocking();
		} catch (InterruptedException exc) {
			exc.printStackTrace();
			System.exit(5);
		}
		System.out.println("\nSuscribe for PENDING execs:");
		try {
			ws_client.send("{\"user\":\"alice@abc.com\", \"execution_id\":\"any\", \"type\":\"pending\"}"); //user is for debugging purposes, it may help to find lost connections
		} catch (WebsocketNotConnectedException ex) {
			System.out.println("Websocket not connected: " + ex);
			System.exit(6);
		}
	}
}
