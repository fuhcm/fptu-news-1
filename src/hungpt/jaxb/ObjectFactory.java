
package hungpt.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the hungpt.jaxb package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _News_QNAME = new QName("", "news");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: hungpt.jaxb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link News }
     * 
     */
    public News createNews() {
        return new News();
    }

    /**
     * Create an instance of {@link Article }
     * 
     */
    public Article createArticle() {
        return new Article();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link News }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "news")
    public JAXBElement<News> createNews(News value) {
        return new JAXBElement<News>(_News_QNAME, News.class, null, value);
    }

}
