package java10th4;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Thread 1: Read data from student.xml
        executor.execute(new Thread1());

        // Thread 2: Calculate age and encode
        executor.execute(new Thread2());

        // Thread 3: Check if the sum of digits is prime
        executor.execute(new Thread3());

        executor.shutdown();
    }

    static class Thread1 implements Runnable {
        @Override
        public void run() {
            try {
                File file = new File("students.xml");
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(file);
                doc.getDocumentElement().normalize();

                NodeList nodeList = doc.getElementsByTagName("Student");
                StringBuilder xmlContent = new StringBuilder();
            xmlContent.append("<Students>\n");

                          for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String id = element.getElementsByTagName("id").item(0).getTextContent();
                    String name = element.getElementsByTagName("name").item(0).getTextContent();
                    String address = element.getElementsByTagName("address").item(0).getTextContent();
                    String dateOfBirth = element.getElementsByTagName("dateOfBirth").item(0).getTextContent();

                    // Đọc thông tin của sinh viên và xử lý ở đây
                    System.out.println("ID: " + id);
                    System.out.println("Name: " + name);
                    System.out.println("Address: " + address);
                    System.out.println("Date of Birth: " + dateOfBirth);
                    System.out.println("---------------------------");

                    // Thêm thông tin của sinh viên vào StringBuilder
                    xmlContent.append("\t<Student>\n");
                    xmlContent.append("\t\t<id>").append(id).append("</id>\n");
                    xmlContent.append("\t\t<name>").append(name).append("</name>\n");
                    xmlContent.append("\t\t<address>").append(address).append("</address>\n");
                    xmlContent.append("\t\t<dateOfBirth>").append(dateOfBirth).append("</dateOfBirth>\n");
                    xmlContent.append("\t</Student>\n");
                }
            }

            xmlContent.append("</Students>");

            // Ghi nội dung của StringBuilder vào file kq.xml
            writeToFile(xmlContent.toString(), "kq.xml");

        } catch (Exception e) {
            e.printStackTrace();
        }
    
        }

         private static void writeToFile(String content, String fileName) {
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(content);
            writer.close();
            System.out.println("File " + fileName + " đã được tạo thành công.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }

    static class Thread2 implements Runnable {
        private static Student student;
        private int encodedAge;
        
        
         public void setStudent(Student student) {
            this.student = student;
        }
          public int getEncodedAge() {
            return encodedAge;
        }
        public static void processStudentData(Student data) {
            student = data;
        }

        @Override
       public void run() {
            if (student != null) {
                // Calculate age
                Date currentDate = new Date();
                long ageInMillis = currentDate.getTime() - student.getDateOfBirth().getTime();
                long ageInYears = ageInMillis / (1000L * 60 * 60 * 24 * 365);

                // Encode age
                encodedAge = encodeAge((int) ageInYears);
            }
        }

        private int encodeAge(int age) {
            // Simple encoding logic, you can replace with your own encoding
            return age + 5;
        }
    }

    static class Thread3 extends Thread {
        private int encodedAge;
        private boolean isPrime;

        public void setEncodedAge(int encodedAge) {
            this.encodedAge = encodedAge;
        }

        public boolean isPrime() {
            return isPrime;
        }

        @Override
        public void run() {
            // Check if the sum of digits is prime
            int sumOfDigits = sumOfDigits(encodedAge);
            isPrime = isPrime(sumOfDigits);
        }

        private int sumOfDigits(int number) {
            int sum = 0;
            while (number != 0) {
                sum += number % 10;
                number /= 10;
            }
            return sum;
        }

        private boolean isPrime(int number) {
            if (number <= 1) {
                return false;
            }
            for (int i = 2; i <= Math.sqrt(number); i++) {
                if (number % i == 0) {
                    return false;
                }
            }
            return true;
        }
    }

    static class Student {
        private String id;
        private String name;
        private String address;
        private Date dateOfBirth;

        public Student(String id, String name, String address, Date dateOfBirth) {
            this.id = id;
            this.name = name;
            this.address = address;
            this.dateOfBirth = dateOfBirth;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }

        public Date getDateOfBirth() {
            return dateOfBirth;
        }
    }
     static void createResultFile(Student student, int encodedAge, boolean isPrime) {
        try {
            File resultFile = new File("kq.xml");
            FileWriter writer = new FileWriter(resultFile);
            writer.write("<Student>\n");
            writer.write("\t<age>" + encodedAge + "</age>\n");
            writer.write("\t<sum>" + sumOfDigits(encodedAge) + "</sum>\n");
            writer.write("\t<isDigit>" + isPrime + "</isDigit>\n");
            writer.write("</Student>");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static int sumOfDigits(int number) {
        int sum = 0;
        while (number != 0) {
            sum += number % 10;
            number /= 10;
        }
        return sum;
    }

}
