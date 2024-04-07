import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class NationalGroupsExtractor {
    public static void main(String[] args) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            Set<String> ethnicGroups = new HashSet<>();

            DefaultHandler handler = new DefaultHandler() {
                boolean isEthnicGroup = false;

                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    if (qName.equals("ethcty")) {
                        isEthnicGroup = true;
                    }
                }

                public void characters(char[] ch, int start, int length) throws SAXException {
                    if (isEthnicGroup) {
                        String ethnicGroup = new String(ch, start, length).trim();
                        if (!ethnicGroup.isEmpty()) {
                            ethnicGroups.add(ethnicGroup);
                            isEthnicGroup = false;
                        }
                    }
                }
            };

            saxParser.parse("Popular_Baby_Names_NY.xml", handler);

            System.out.println("Етнічні групи в документі:");
            for (String ethnicGroup : ethnicGroups) {
                System.out.println(ethnicGroup);
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}
