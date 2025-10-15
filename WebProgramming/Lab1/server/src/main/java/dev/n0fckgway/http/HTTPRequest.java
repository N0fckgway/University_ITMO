package dev.n0fckgway.http;

import java.io.InputStream;
import java.util.Map;

public class HTTPRequest {
    String method;
    String uri;
    String proto;
    Map<String, String> header;
    InputStream bodyReader;
    int contentLength;

    public static class Builder {
        HTTPRequest httpRequest = new HTTPRequest();

        public HTTPRequest build() {

            if (httpRequest.method == null || httpRequest.uri == null || httpRequest.proto == null || httpRequest.bodyReader == null || httpRequest.header == null) {
                throw new IllegalStateException("""
                        You must provide at least:
                        http method,
                        request uri,
                        proto,
                        body reader
                        """);


            }
            return httpRequest;

        }

        public void clear() {
            httpRequest = new HTTPRequest();
        }

        public Builder setMethod(String method) {
            if (method == null || !method.equals("GET") && !method.equals("POST") && !method.equals("PUT") && !method.equals("DELETE")) {
                throw new IllegalArgumentException("Invalid HTTP method:" + method);
            }
            httpRequest.method = method;
            return this;
        }

        public Builder setUri(String uri) {
            httpRequest.uri = uri;
            return this;
        }

        public Builder setProto(String proto) {
            httpRequest.proto = proto;
            return this;
        }

        public Builder header(Map<String, String> header) {
            if (header == null) {
                throw new NullPointerException("Header cannot be null");
            }
            httpRequest.header = header;
            return this;
        }

        public Builder bodyReader(InputStream bodyReader) {
            httpRequest.bodyReader = bodyReader;
            return this;
        }

        public Builder contentLength(int contentLength) {
            if (contentLength < 0) {
                throw new IllegalArgumentException("Invalid content length: " + contentLength);
            }
            httpRequest.contentLength = contentLength;
            return this;
        }
    }

}
