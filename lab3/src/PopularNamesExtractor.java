import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class PopularNamesExtractor {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Введіть етнічну групу (наприклад, 'HISPANIC'): ");
            String ethnicGroup = scanner.nextLine();
            System.out.print("Введіть кількість найпопулярніших імен: ");
            int count = scanner.nextInt();
            scanner.nextLine(); // Очистити буфер

            // Зчитування XML-файлу та отримання заданої етнічної групи
            List<NameInfo> nameInfoList = extractNames("Popular_Baby_Names_NY.xml", ethnicGroup, count);

            // Сортування інформації за збільшенням рейтингу
            nameInfoList.sort(Comparator.comparingInt(NameInfo::getRank));

            // Створення нового XML-файлу за допомогою DOM парсера
            createXML(nameInfoList, "output.xml");

            System.out.println("Вибрана та відсортована інформація успішно збережена в файлі output.xml");
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    // Метод для витягнення інформації про імена з XML-файлу для заданої етнічної групи та кількості імен
    private static List<NameInfo> extractNames(String fileName, String ethnicGroup, int count) throws IOException, ParserConfigurationException, SAXException {
        List<NameInfo> nameInfoList = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(fileName));

        NodeList nodeList = document.getElementsByTagName("row");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String ethcty = element.getElementsByTagName("ethcty").item(0).getTextContent();
                if (ethcty.equalsIgnoreCase(ethnicGroup)) {
                    String name = element.getElementsByTagName("nm").item(0).getTextContent();
                    String gender = element.getElementsByTagName("gndr").item(0).getTextContent();
                    int cnt = Integer.parseInt(element.getElementsByTagName("cnt").item(0).getTextContent());
                    int rank = Integer.parseInt(element.getElementsByTagName("rnk").item(0).getTextContent());
                    nameInfoList.add(new NameInfo(name, gender, cnt, rank));
                }
            }
        }

        // Обмеження кількості отриманих імен, якщо кількість отриманих більше, ніж count
        if (nameInfoList.size() > count) {
            nameInfoList = nameInfoList.subList(0, count);
        }

        return nameInfoList;
    }


    // Метод для створення нового XML-файлу з відсортованою інформацією
    private static void createXML(List<NameInfo> nameInfoList, String outputFileName) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element rootElement = document.createElement("names");
        document.appendChild(rootElement);

        for (NameInfo nameInfo : nameInfoList) {
            Element nameElement = document.createElement("name");
            rootElement.appendChild(nameElement);

            Element name = document.createElement("name");
            name.appendChild(document.createTextNode(nameInfo.getName()));
            nameElement.appendChild(name);

            Element gender = document.createElement("gender");
            gender.appendChild(document.createTextNode(nameInfo.getGender()));
            nameElement.appendChild(gender);

            Element count = document.createElement("count");
            count.appendChild(document.createTextNode(String.valueOf(nameInfo.getCount())));
            nameElement.appendChild(count);

            Element rank = document.createElement("rank");
            rank.appendChild(document.createTextNode(String.valueOf(nameInfo.getRank())));
            nameElement.appendChild(rank);
        }

        // Збереження документу у вихідний файл
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(outputFileName));
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    // Клас для зберігання інформації про ім'я
    private static class NameInfo {
        private final String name;
        private final String gender;
        private final int count;
        private final int rank;

        public NameInfo(String name, String gender, int count, int rank) {
            this.name = name;
            this.gender = gender;
            this.count = count;
            this.rank = rank;
        }

        public String getName() {
            return name;
        }

        public String getGender() {
            return gender;
        }

        public int getCount() {
            return count;
        }

        public int getRank() {
            return rank;
        }
    }
}
