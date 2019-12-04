package utility;

import library.SessionManager;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class ApiUtility {

    private LogUtility logUtility = new LogUtility(ApiUtility.class);

    public ApiUtility(SessionManager sessionManager)
    {
        this.logUtility.setExtentLogger(sessionManager.getExtentReporter().getExtentlogger());
    }

    public String convertResponseToString(HttpResponse response) {
        StringBuffer result = new StringBuffer();
        try {
            BufferedReader responseReader = null;
            String line = "";
            responseReader = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));
            while ((line = responseReader.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        }
        return result.toString();
    }

    public JSONObject convertResponseToJsonObject(HttpResponse response) {
        return convertStringToJSON(convertResponseToString(response));
    }

    public HttpRequestBase getHttpObject(String method, String URL, Map<String, String> headers) {
        HttpRequestBase httpRequestBase = null;
        if (method.equalsIgnoreCase("get")) {
            httpRequestBase = new HttpGet(URL);
        } else if (method.equalsIgnoreCase("post")) {
            httpRequestBase = new HttpPost(URL);
        } else if (method.equalsIgnoreCase("put")) {
            httpRequestBase = new HttpPut(URL);
        } else if (method.equalsIgnoreCase("patch")) {
            httpRequestBase = new HttpPatch(URL);
        }
        httpRequestBase = setHeaders(httpRequestBase, headers);
        return httpRequestBase;
    }

    public HttpRequestBase setHeaders(HttpRequestBase httpRequestBase, Map<String, String> headers) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            httpRequestBase.setHeader(entry.getKey(), entry.getValue());
        }
        return httpRequestBase;
    }

    public <T extends HttpEntityEnclosingRequestBase> T setBody(T t, JSONObject body, String method) {
        try {
            String bodyString = body.toString();
            StringEntity stringEntity = new StringEntity(bodyString);
            if (method.equalsIgnoreCase("patch")) {
                stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            }
            t.setEntity(stringEntity);
        } catch (UnsupportedEncodingException e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        }
        return t;
    }

    public HttpResponse sendRequest(String method, String URL, Map<String, String> headers) {
        HttpResponse response = null;
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpRequestBase httpObject = getHttpObject(method, URL, headers);
            response = client.execute(httpObject);
        } catch (IOException e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        }
        return response;
    }

    public HttpResponse sendRequest(String method, String URL, Map<String, String> headers, JSONObject body) {
        HttpResponse response = null;
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpRequestBase httpObject = getHttpObject(method, URL, headers);
            httpObject = setBody((HttpEntityEnclosingRequestBase) httpObject, body, method);
            response = client.execute(httpObject);
        } catch (IOException e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        }
        return response;
    }

    public HttpResponse sendRequest(String method, String URL, Map<String, String> headers, HttpEntity httpEntity) {
        HttpResponse response = null;
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpRequestBase httpObject = getHttpObject(method, URL, headers);
            ((HttpEntityEnclosingRequestBase) httpObject).setEntity(httpEntity);
            response = client.execute(httpObject);
        } catch (IOException e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        }
        return response;
    }

    public Document convertStringToXML(String input) {
        Document doc = null;
        try {
            doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(new InputSource(new StringReader(input)));
        } catch (SAXException | IOException | ParserConfigurationException e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        }

        return doc;
    }

    public JSONObject convertStringToJSON(String input) {
        JSONObject json = null;
        try {
            JSONParser parser = new JSONParser();
            json = (JSONObject) parser.parse(input);
        } catch (ParseException e) {
            logUtility.logException(ExceptionUtils.getStackTrace(e));
        }
        return json;
    }

    public boolean isURLUnique(String URL, List<String> URLList) {
        boolean result = false;
        if (!URLList.contains(URL)) {
            result = true;
        }
        return result;
    }

    public boolean isURLFormatCorrect(String URL) {
        boolean result = false;
        if (!URL.contains("--")) {
            result = true;
        }
        return result;
    }

    public String getAuthorizationHeader(String username, String accessKey) {
        String auth = username + ":" + accessKey;
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(StandardCharsets.ISO_8859_1));
        return "Basic " + new String(encodedAuth);
    }

    public String getMultiPartEntityAsString(HttpEntity multipartEntity)
    {
        String multiPartEntityString = null;
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            multipartEntity.writeTo(bytes);
            multiPartEntityString = bytes.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  multiPartEntityString;
    }
}

