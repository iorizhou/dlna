package com.xiaojiutech.dlna.webserver;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

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
            Log.i("iorizhou","mime = "+getMimeTypeForFile(file.getName()));
            Response response = newChunkedResponse(Response.Status.OK,getMimeTypeForFile(file.getName()),new BufferedInputStream(fis));
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
