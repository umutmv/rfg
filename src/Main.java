

public class Main {
    public static void main(String[] args) {
        try {
            RandomFileGenerator.main(args);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("RandomFileGenerator sınıfı çalıştırılırken bir hata oluştu: " + e.getMessage());
        }
    }
}
