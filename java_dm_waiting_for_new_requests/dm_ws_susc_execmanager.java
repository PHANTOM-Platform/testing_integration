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
import java.net.URL;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.exceptions.WebsocketNotConnectedException;

import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class dm_ws_susc_execmanager extends WebSocketClient {
	static String[] labels ;
	static String[] values ;
	static String[] ids ;

	static String[] addStringElement(String[] a, String e){
		if(a==null){
			a= new String[1];
		}else{
			a  = Arrays.copyOf(a, a.length + 1);
		}
		a[a.length - 1] = e;
		return a;
	}

	public static void print_all_docs(int count_fields){
		System.out.print(" Number_of_fields:" +count_fields+"\n");
		for(int l=0; l<count_fields; l++){
			System.out.print("\t"+labels[l]+":"+values[l]+"\n");
		}
		System.out.print("\n\n");
	}
	
	public static int collect_data(JSONObject myjson){
		Iterator iterator= myjson.keys();
		String key = null;
		int count=0;
		try{
			while(iterator.hasNext()){
				key =(String)iterator.next();
				String keyStr =(String)key;
				Object keyvalue = myjson.get(keyStr);
				if(keyvalue instanceof JSONObject){
					//nothing to do
				}else if(keyvalue instanceof JSONArray){
					//nothing to do
				}else{//imadiate value
					count=count+1;
					labels = addStringElement(labels,(String)keyStr);
					if(keyvalue instanceof String){
						values = addStringElement(values,(String)keyvalue);
					}else if(keyvalue instanceof Integer){
						values = addStringElement(values,(String)Integer.toString((Integer)keyvalue));
					}
				}
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
		return count;
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
		//we look if it is defined the field app, then it a new request, in other case is the reply message of acceptance of subscription
		values= null;
		labels= null;
		try{
			int count_fields =collect_data((JSONObject) new JSONObject(message));
			int it_is_new_exec =0;
			for(int l=0; l<count_fields; l++){
				if(labels[l].equals("app")){
					it_is_new_exec = 1;
				}
			}
			if(it_is_new_exec==1){
				System.out.println("\n REQUESTED NEW EXECUTION !!!");
				print_all_docs(count_fields );
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
	}

	@Override
	public void onClose( int code, String reason, boolean remote) {
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
		System.out.println("\nSubscribe for PENDING execs:");
		try {
			ws_client.send("{\"user\":\"alice@abc.com\", \"execution_id\":\"any\", \"type\":\"pending\"}"); //user is for debugging purposes, it may help to find lost connections
		} catch (WebsocketNotConnectedException ex) {
			System.out.println("Websocket not connected: " + ex);
			System.exit(6);
		}
	}
}
