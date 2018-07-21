package com.mikhaellopez.androidwebserver;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by Mikhael LOPEZ on 14/12/2015.
 */
public class AndroidWebServer extends NanoHTTPD {

    public AndroidWebServer(int port) {
        super(port);
    }

    public AndroidWebServer(String hostname, int port) {
        super(hostname, port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        try{

            Log.i("webserver",session.getUri());
            File file = new File(session.getUri());
            FileInputStream fis = new FileInputStream(file);
            Response response = newFixedLengthResponse(Response.Status.OK,getMimeTypeForFile(file.getName()),fis,file.length());
            response.addHeader("Access-Control-Allow-Origin","*");
            response.addHeader("Access-Control-Expose-Headers","Content-Length");
            response.addHeader("Proxy-Connection","Proxy-Connection");
            return response;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}