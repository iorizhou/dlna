package com.xiaojiutech.dlna.webserver;

import android.text.TextUtils;
import android.util.Log;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
    private Response getPartialResponse(String mimeType, String rangeHeader,String filepath) throws IOException {
        File file = new File(filepath);
        String rangeValue = rangeHeader.trim().substring("bytes=".length());
        long fileLength = file.length();
        long start, end;
        String[] range = rangeValue.split("-");
        start = Long.parseLong(range[0]);
        end = range.length > 1 ? Long.parseLong(range[1]): fileLength ;
        if (end > fileLength) {
            end = fileLength;
        }
        if (start <= end) {
            long contentLength = end - start;
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.skip(start);
            Response response = new Response(Response.Status.PARTIAL_CONTENT, mimeType, fileInputStream);
            response.addHeader("Content-Length", contentLength + "");
            response.addHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);
            Log.e("SERVER_PARTIAL", "bytes " + start + "-" + end + "/" + fileLength);
            response.addHeader("Content-Type", mimeType);
            return response;
        } else {
            return new Response(Response.Status.RANGE_NOT_SATISFIABLE, "video/mp4", rangeHeader);
        }
    }
    @Override
    public Response serve(IHTTPSession session) {
        try{
            String range = session.getHeaders().get("range");
            if (TextUtils.isEmpty(range)){
                range = "bytes=0-";
            }
            Response response = null;
            String type = session.getParms().get("type");

            if (TextUtils.isEmpty(type)||!type.equals("picture")){
                response = getPartialResponse("image/png",range,session.getUri());
            }else {
                response = new Response(Response.Status.OK,"image/png",new FileInputStream(new File(session.getUri())));
            }
            return response;
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Response(Response.Status.NOT_FOUND,"","filepath is invalid");
    }
}
