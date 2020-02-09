package hungpt.resolver;

import hungpt.utils.HttpHelper;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import java.io.IOException;

public class URIResolverFU implements URIResolver {
    @Override
    public Source resolve(String href, String base) throws TransformerException {
        if (href != null && href.indexOf("https://hcmuni.fpt.edu.vn/") == 0) {
            try {
                String content = HttpHelper.getContent(href);
                System.out.println(content);
            } catch (IOException ex){

            }
        }
        return null;
    }
}
