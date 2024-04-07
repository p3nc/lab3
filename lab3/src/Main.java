import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            // Створення фабрики та парсера
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();

            // Визначення обробника подій
            DefaultHandler handler = new DefaultHandler() {
                // Обробник події початку елементу
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    System.out.print("Початок елементу: " + qName);
                }

                // Обробник події закінчення елементу
                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    System.out.println("Кінець елементу: " + qName);
                }

                // Обробник події текстового вмісту елементу
                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    // Вивід текстового вмісту, якщо він не порожній
                    String content = new String(ch, start, length).trim();
                    if (!content.isEmpty()) {
                        System.out.println(" - Вміст: " + content);
                    }
                }
            };

            // Парсинг XML-файлу
            parser.parse(new File("Popular_Baby_Names_NY.xml"), handler);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}
