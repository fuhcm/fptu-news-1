package hungpt.crawler;

import hungpt.resolver.URIResolverFU;


import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;

public class FUCrawler {
    public static ByteArrayOutputStream crawl(String url) throws Exception {
        InputStream is = new FileInputStream(url);
        TransformerFactory factory = TransformerFactory.newInstance();
        URIResolverFU resolverFU = new URIResolverFU();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        StreamResult rs = new StreamResult(os);
        factory.setURIResolver(resolverFU);
        Transformer transformer = factory.newTransformer();
        transformer.transform(new StreamSource(is), rs);
        System.out.println(url);
        return os;
    }
}
