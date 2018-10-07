package se.walkercrou.places;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DefaultRequestHandler implements RequestHandler {
    /**
     * The default and recommended character encoding.
     */
    public static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private String characterEncoding;

    /**
     * Creates a new handler with the specified character encoding.
     *
     * @param characterEncoding to use
     */
    public DefaultRequestHandler(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    /**
     * Creates a new handler with UTF-8 character encoding.
     */
    public DefaultRequestHandler() {
        this(DEFAULT_CHARACTER_ENCODING);
    }

    /**
     * Returns the character encoding used by this handler.
     *
     * @return character encoding
     */
    @Override
    public String getCharacterEncoding() {
        return characterEncoding;
    }

    /**
     * Sets the character encoding used by this handler.
     *
     * @param characterEncoding to use
     */
    @Override
    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    private final OkHttpClient client = new OkHttpClient();

    private String readString(Response response) throws IOException {
        String str = response.body().string();
        if (str == null || str.trim().length() == 0) {
            return null;
        }
        return str.trim();
    }

    @Override
    public InputStream getInputStream(String uri) throws IOException {
        Request request = new Request.Builder()
                .url(uri)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().byteStream();
        }
        catch (Exception e) {
            throw new IOException(e);
        }
        finally {
        }
    }

    @Override
    public String get(String uri) throws IOException {
        Request request = new Request.Builder()
                .url(uri)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.body() != null) {
                return response.body().string();
            }
        }
        catch (Exception e) {
            throw new IOException(e);
        }
        return null;
    }

    @Override
    public String post(String uri, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(uri)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.body() != null) {
                return response.body().string();
            }
        }
        catch (Exception e) {
            throw new IOException(e);
        }
        return null;
    }
}
