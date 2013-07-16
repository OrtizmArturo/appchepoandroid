package com.example.test;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;

import com.loopj.android.http.*;
import org.json.*;

public class MainActivity extends Activity {

	float alphaVal = (float) 0.5;
	private String choiceString;
	EditText textEdit;
	private static final String BASE_URL = "http://api.twitter.com/1/";
    private static AsyncHttpClient client = new AsyncHttpClient();
	RelativeLayout buttonsLayout;
	ActivityInfo activityInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
//		activityInfo= (ActivityInfo)findv
//		setRequestedOrientation();
		buttonsLayout = (RelativeLayout) findViewById(R.id.editTextLayout);
	}
		  
	@Override
	public void onPause(){
		super.onPause();
		System.out.println("ON PAUSE");
	}
	
	@Override
	public void onResume(){
		super.onResume();
		System.out.println("ON RESUME");
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
		if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
			System.out.println("Landscape");
		}
		else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
			System.out.println("Portratit");
		}
	}
	
	
	public static void connect(String url)
	{

	    HttpClient httpclient = new DefaultHttpClient();

	    // Prepare a request object
	    HttpGet httpget = new HttpGet(url); 

	    // Execute the request
	    HttpResponse response;
	    try {
	        response = httpclient.execute(httpget);
	        // Examine the response status
	        Log.i("Praeda",response.getStatusLine().toString());

	        // Get hold of the response entity
	        HttpEntity entity = response.getEntity();
	        // If the response does not enclose an entity, there is no need
	        // to worry about connection release

	        if (entity != null) {

	            // A Simple JSON Response Read
	            InputStream instream = entity.getContent();
	            String result= convertStreamToString(instream);
	            // now you have the string representation of the HTML request
	            instream.close();
	        }


	    } catch (Exception e) {}
	}
	
    private static String convertStreamToString(InputStream is) {
    /*
     * To convert the InputStream to String we use the BufferedReader.readLine()
     * method. We iterate until the BufferedReader return null which means
     * there's no more data to read. Each line will appended to a StringBuilder
     * and returned as String.
     */
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    StringBuilder sb = new StringBuilder();

    String line = null;
    try {
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    return sb.toString();
}

    
    
public void buttonYesPressed(View view){
	System.out.println("YES PRESSED!");
	choiceString = "1";
    buttonsLayout.setVisibility(View.VISIBLE);
}
	
public void buttonNoPressed(View view){
	System.out.println("NO PRESSED!");
//	Log.v("t", "LOG");
	choiceString= "0";
	buttonsLayout.setVisibility(View.VISIBLE);

}

public void buttonEnviarPressed(View view){
	System.out.println("ENVIAR!");
	
	textEdit = (EditText)findViewById(R.id.textField);
	String URLst = "http://www.barronegro.net/api/chepo.php?decision="+choiceString+"&comentario="+textEdit.getText().toString()+" -enviado desde Android-";
	String URLfull = URLst.replace(" ", "%20");
	System.out.println(URLfull);
	
	AsyncHttpClient client = new AsyncHttpClient();
	
	client.get(URLfull, new AsyncHttpResponseHandler() {
		
		
	    @Override
	    public void onSuccess(String response) {
	        System.out.println(response);
	        
	        JSONObject jObject;
	        JSONArray jArray;
	        
			try {
				jObject = new JSONObject(response);
		        System.out.println("json: "+jObject);
//		        JSONObject menuObject = jObject.getJSONObject("success");
		        System.out.println("json success: "+jObject.getString("comentarios"));
		        jArray = new JSONArray(jObject.getString("comentarios"));
		        System.out.println("array: "+jArray.get(0));
		        
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	    }
	    
	    public void onFailure(Throwable e, String response) {
	         // Response failed :(
	        System.out.println(response);

	        Toast toast = Toast.makeText(getApplicationContext(), "Verifica tu conexion!", Toast.LENGTH_SHORT);
	        toast.show();
	        
//	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//	        builder.setMessage("Ejemplo de Mensaje Popup para Android OS desde Devtroce.com")
//	                .setTitle("Atenci—n!!")
//	                .setCancelable(false)
//	                .setNeutralButton("Aceptar",
//	                        new DialogInterface.OnClickListener() {
//	                            public void onClick(DialogInterface dialog, int id) {
//	                                dialog.cancel();
//	                            }
//	                        });
//	        AlertDialog alert = builder.create();
//	        alert.show();
	        
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

	        builder.setTitle("Error");
	        builder.setMessage("Verifica tu Conexion");

	        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

	            public void onClick(DialogInterface dialog, int which) {
	                // Do nothing but close the dialog

	                dialog.dismiss();
	            }

	        });
	        AlertDialog alert = builder.create();
	        alert.show();
	     }

		public void onFinish() {
		System.out.println("stop spinner");
}
	
	});		
	
	

//	connect(URLst);

}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
