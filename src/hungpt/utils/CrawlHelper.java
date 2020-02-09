package hungpt.utils;

import com.sun.tools.javadoc.Start;
import hungpt.constant.EntityCharacter;
import hungpt.constant.State;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlHelper {

    private static final String[] IGNORE_TAGS = new String[]{"script", "head", "noscript", "style"};
    private static final List<String> INLINE_TAGS = Arrays.asList("area", "base", "br", "col", "command",
            "embed", "hr", "img", "input", "keygen",
            "link", "meta", "param", "source", "track", "wbr");

    public static void parseHTML(String html) throws Exception {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        xmlInputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
        xmlInputFactory.setProperty(XMLInputFactory.IS_VALIDATING, false);

        XMLEventReader reader = null;
        String refiendHTML = refineHtml(html);

        String wellFormHTML = wellFormHtml(refiendHTML);
        System.out.println(wellFormHTML);
//        reader = xmlInputFactory.createXMLEventReader(new InputStreamReader(new ByteArrayInputStream(refiendHTML.getBytes()), "UTF-8"));
//        boolean inNewsTag = false;
//        while (reader.hasNext()) {
//            XMLEvent event = reader.nextEvent();
//            if (event.isStartElement()) {
//                StartElement startElement = (StartElement) event;
//                if (startElement.getName().toString().equals("div")) {
//                    System.out.println(startElement.getName());
//                    Attribute attribute = ((StartElement) event).getAttributeByName(new QName("class"));
//                    if (attribute != null) {
//                        if (attribute.getValue().equals("tin-tuc-2")) {
//                            inNewsTag = true;
//                        }
//                    }
//                }
//            }
//        }
    }

    private static String wellFormHtml(String html) {

        char[] reader = html.toCharArray();

        StringBuilder writer = new StringBuilder();
        boolean isOpenTag = false, isEmptyTag = false;

        Stack<String> stack = new Stack<>();

        StringBuilder content = new StringBuilder();
        StringBuilder openTagName = new StringBuilder();
        StringBuilder closeTagName = new StringBuilder();

        StringBuilder attName = new StringBuilder();

        Map<String,String> attributes = new HashMap<>();
        State state = State.CONTENT;
        for (char el : reader) {

            switch (state) {
                case CONTENT:
                    if (el == EntityCharacter.LESS_THAN.getCharacter()) {
                        state = State.OPEN_BRACKET;

                        // Ex: Bộ GD&ĐT -> Bộ GD&amp;ĐT -> Wellform XML
                        writer.append(content.toString().trim().replaceAll("&", "&amp;"));
                    } else {
                        content.append(el);
                    }
                    break;
                case OPEN_BRACKET:
                    isEmptyTag = false;

                    if (el == EntityCharacter.SLASH.getCharacter()) {
                        isOpenTag = false;
                        state = State.CLOSE_TAG_SLASH;
                    } else if (StringHelper.isStartCharacter(el)) {
                        isOpenTag = true;
                        openTagName.setLength(0);
                        openTagName.append(el);
                        state = State.OPEN_TAG_NAME;
                    }
                    break;
                case CLOSE_TAG_SLASH:
                    if (StringHelper.isStartCharacter(el)) {
                        state = State.CLOSE_TAG_NAME;
                    }
                    break;
                case CLOSE_TAG_NAME:
                    if (el == EntityCharacter.SPACE.getCharacter()) {
                        state = State.WAIT_END_TAG_CLOSE;
                    } else if (el == EntityCharacter.GREAT_THAN.getCharacter()) {
                        state = State.CLOSE_BRACKET;
                    } else {
                        closeTagName.setLength(0);
                        closeTagName.append(el);
                    }
                    break;
                case WAIT_END_TAG_CLOSE:
                    if (el == EntityCharacter.SPACE.getCharacter()) {
                        state = State.CLOSE_BRACKET;
                    }
                    break;
                case OPEN_TAG_NAME:
                    if (el == EntityCharacter.SPACE.getCharacter()) {
                        state = State.TAG_INNER;
                    } else if (el == EntityCharacter.SLASH.getCharacter()) {
                        state = State.EMPTY_SLASH;
                    } else if (el == EntityCharacter.GREAT_THAN.getCharacter()) {
                        state = State.CLOSE_BRACKET;
                    } else if (StringHelper.isNameCharacter(el)){
                        openTagName.append(el);
                    }
                    break;
                case TAG_INNER:
                    if (StringHelper.isStartCharacter(el)) {
                        state = State.ATT_NAME;
                    }
                    break;
                case ATT_NAME:
                    if (StringHelper.isNameCharacter(el)){
                        attName.append(el);
                    } else if (el == EntityCharacter.SPACE.getCharacter()) {
                        state = State.EQUAL_WAIT;
                    } else if (el == EntityCharacter.EQUAL.getCharacter()) {
                        state = State.EQUAL;
                    }
                    break;
                case EQUAL_WAIT:
                    if (el == EntityCharacter.EQUAL.getCharacter()) {
                        state = State.EQUAL;
                    }
                    break;
                case EQUAL:
                    if (el != EntityCharacter.EQUAL.getCharacter() && el != EntityCharacter.GREAT_THAN.getCharacter()) {
                        state = State.ATT_VALUE_NQ;
                    } else if (el == EntityCharacter.DOUBLE_QOUT.getCharacter() || el == EntityCharacter.SINGLE_QOUT.getCharacter()) {
                        state = State.ATT_VALUE_Q;
                    }
                    break;
                case ATT_VALUE_Q:
                    if (el == EntityCharacter.DOUBLE_QOUT.getCharacter() || el == EntityCharacter.SINGLE_QOUT.getCharacter()) {
                        state = State.TAG_INNER;
                    }
                case ATT_VALUE_NQ:
                    if (el == EntityCharacter.GREAT_THAN.getCharacter()) {
                        state = State.CLOSE_BRACKET;
                    }
                    break;
                case CLOSE_BRACKET:
                    String openTagLowerCase = openTagName.toString().toLowerCase();
                    String closeTagLowerCase = closeTagName.toString().toLowerCase();

                    if (isOpenTag) {
                        writer.append(EntityCharacter.LESS_THAN.getCharacter()).append(openTagLowerCase).append(EntityCharacter.GREAT_THAN.getCharacter());
                        if (INLINE_TAGS.contains(openTagLowerCase)) {
                            isEmptyTag = true;
                        }
                        if (isEmptyTag) {
                            writer.append(EntityCharacter.LESS_THAN.getCharacter()).append(openTagLowerCase).append(EntityCharacter.SLASH.getCharacter()).append(EntityCharacter.GREAT_THAN.getCharacter());
                        }
                    } else {
                        writer.append(EntityCharacter.LESS_THAN.getCharacter()).append(EntityCharacter.SLASH.getCharacter()).append(closeTagLowerCase).append(EntityCharacter.GREAT_THAN.getCharacter());
                    }
                    if (el == EntityCharacter.LESS_THAN.getCharacter()) {
                        state = State.OPEN_BRACKET;
                    } else {
                        content.setLength(0);
                        content.append(el);
                        state = State.CONTENT;
                    }
                    break;
                case EMPTY_SLASH:
                    if (el == EntityCharacter.GREAT_THAN.getCharacter()) {
                        state = State.CLOSE_BRACKET;
                    }
                    break;
            }

        }
        return writer.toString();
    }


    private static String refineHtml(String html) {
        html = getMainContent(html);
        html = removeNeedlessTags(html);
        return html;
    }

    private static String getMainContent(String src) {
        String result = src;
        Matcher matcher = Pattern.compile("<body.*?</body>").matcher(src);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return result;
    }

    private static String removeNeedlessTags(String src) {
        String result = src;

        String expression = "<!--.*?-->";
        result = result.replaceAll(expression, "");

        expression = "&nbsp;?";
        result = result.replaceAll(expression, "");

        for (String exp : IGNORE_TAGS) {
            expression = String.format("<%s.*?</%s>", exp, exp);
            result = result.replaceAll(expression, "");
        }

        return result;
    }
}
