import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;

public class ReadXML {
    public static void main(String[] args) {
        try {
            // Зчитування XML-файлу
            File inputFile = new File("output.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = null;
            try {
                document = builder.parse(inputFile);
            } catch (SAXException e) {
                throw new RuntimeException(e);
            }

            // Отримання кореневого елементу
            Element rootElement = document.getDocumentElement();

            // Обхід дочірніх елементів кореневого елементу та виведення їх на екран
            NodeList nodeList = rootElement.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String name = element.getElementsByTagName("name").item(0).getTextContent();
                    String gender = element.getElementsByTagName("gender").item(0).getTextContent();
                    int count = Integer.parseInt(element.getElementsByTagName("count").item(0).getTextContent());
                    int rank = Integer.parseInt(element.getElementsByTagName("rank").item(0).getTextContent());

                    System.out.println("Ім'я: " + name);
                    System.out.println("Гендер: " + gender);
                    System.out.println("Кількість: " + count);
                    System.out.println("Рейтинг: " + rank);
                    System.out.println();
                }
            }
        } catch (ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }
}
