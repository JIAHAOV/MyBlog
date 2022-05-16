package com.study.reproduce.utils;

import java.net.URI;
import java.net.URISyntaxException;

public class URIUtil {
    public static URI getResponseURI(URI uri, String filename) {
        URI result = null;
        try {
            result = new URI(uri.getScheme(), null, uri.getHost(),
                    uri.getPort(), "/admin/blogs/" + filename, null, null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return result;
    }
}
