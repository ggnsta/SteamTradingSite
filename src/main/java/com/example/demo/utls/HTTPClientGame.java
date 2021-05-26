package com.example.demo.utls;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.openid4java.server.ServerException;
import org.springframework.web.reactive.function.client.WebClient;

public class HTTPClientGame {
    private HttpClient client;
    private List<Header> headers= new ArrayList<>();
    private String URIl;

    public HTTPClientGame(String URL) {
        headers.add(new BasicHeader("Content-Type", "application/json; charset=UTF-8"));
        client = HttpClientBuilder.create().build();
        this.URIl = URL;
    }
    public HTTPClientGame()
    {

    }

    public HTTPClientGame(String URL, List<Header> headers)
    {
        this.URIl = URL;
        this.headers = headers;
    }



    //EXCEP SLOVIT
    public String getAll(){
        try {
            client = HttpClientBuilder.create().build();
            HttpGet get = new HttpGet(URIl);
            for(int i=0;i<headers.size();i++)
            {
                get.addHeader(headers.get(i));
            }

            HttpResponse response = client.execute(get);

            throwServerException(response, 200);
            return inputStreamToString(response.getEntity().getContent());

        } catch (Exception e) {
          e.printStackTrace();
          return null;
        }
    }

    public String getAllproxy(){
        try {
            client = HttpClientBuilder.create().build();
            HttpGet get = new HttpGet(URIl);
            for(int i=0;i<headers.size();i++)
            {
                get.addHeader(headers.get(i));
            }

            HttpResponse response = client.execute(get);

            throwServerException(response, 200);
            return inputStreamToString(response.getEntity().getContent());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private String inputStreamToString(InputStream is) {

        String line = "";
        StringBuilder total = new StringBuilder();

        // Wrap a BufferedReader around the InputStream
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        try {
            // Read response until the end
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (IOException e) {

        }
        // Return full string
        return total.toString();
    }

    public static HttpRequest build(String url, Map<String, String> headers, RequestType requestType, String body) {

        HttpRequest.Builder request = HttpRequest.newBuilder();

        request.uri(URI.create(url));

        for (String header : headers.keySet()) {
            request.header(header, headers.get(header));
        }

        switch (requestType) {
            case GET:
                request.GET();
                break;
            case POST:
                if (body != null) {
                    request.POST(HttpRequest.BodyPublishers.ofString(body));
                } else request.POST(HttpRequest.BodyPublishers.noBody());
                break;
        }

        return request.build();
    }

    public enum RequestType {
        GET,
        POST,
    }

    private void test ()
    {
        WebClient client = WebClient.create();
    }

    private void throwServerException(HttpResponse response, int goodCode) throws ServerException {
        if (response.getStatusLine().getStatusCode() == goodCode) return;

        switch (response.getStatusLine().getStatusCode()) {
            case 404:
                 throw new ServerException("Item wasn't found!", 404);
            case 204:
                 throw new ServerException("There is no content", 204);
            case 409:
                 throw new ServerException("The item already exists", 409);
            case 429: return;
            default:
                  throw new ServerException("Server error occurred!", response.getStatusLine().getStatusCode());
        }
    }

}
