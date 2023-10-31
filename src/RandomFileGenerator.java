import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Random;

class RandomFileGenerator {

    public static void main(String[] args) throws IOException, InterruptedException {
        String serverUrl = "http://localhost:8080/upload";
        int dosyaSira = 0;
        String filePath = "C:\\Users\\umutm\\OneDrive\\Masaüstü\\task\\";

        while (true) {
            Thread.sleep(5000);
            dosyaSira++;
            try {
                File file = generateRandomFile(dosyaSira);

                if (file.exists()) {
                    String fileContent = readFileToString(file);
                    String fileName = "Dosya" + dosyaSira + ".txt";
                    URL url = new URL(serverUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    // Set the request method to POST
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    String combinedData = fileName + "\n" + fileContent;

                    try (OutputStream os = connection.getOutputStream()) {
                        byte[] combinedDataBytes = combinedData.getBytes(StandardCharsets.UTF_8);
                        os.write(combinedDataBytes);
                    }

                    // Get the server response
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        System.out.println("Dosya başarıyla yüklendi.");
                    } else {
                        System.err.println("Dosya yüklemesinde hata. Hata kodu:: " + responseCode);
                    }

                    connection.disconnect();
                } else {
                    System.err.println("Dosya bulunamadı : " + filePath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public static String readFileToString(File file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        }
    }

    public static File generateRandomFile(int dosyaSira) throws IOException {
        Random random = new Random();
        String dizin = "C:\\Users\\umutm\\OneDrive\\Masaüstü\\task\\";
        String dosyaAdi = "Dosya" + dosyaSira + ".txt";
        FileWriter fileWriter = new FileWriter(dizin + dosyaAdi);

        Properties properties = new Properties();

        try (FileInputStream configFile = new FileInputStream("deneme.config")) {  //yapılandırma dosyasının içeriğini yüklemek için okumayı sağlar (fileinputstream)
            properties.load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int maksimumSatirSayisi = Integer.parseInt(properties.getProperty("maksimumSatirSayisi"));
        int maksimumDosyaBoyutuKB = Integer.parseInt(properties.getProperty("maksimumDosyaBoyutuKB"));
        int satirCount = 0;
        int dosyaBoyutu = 0;

        try (fileWriter) {
            while (satirCount < maksimumSatirSayisi && dosyaBoyutu < maksimumDosyaBoyutuKB * 1024) {
                String rastgeleMetin = generateRandomString(random.nextInt(50) + 1);
                String satir = rastgeleMetin + "\n";
                int satirBoyutu = satir.getBytes().length;

                if (dosyaBoyutu + satirBoyutu <= maksimumDosyaBoyutuKB * 1024) {
                    fileWriter.write(satir);
                    dosyaBoyutu += satirBoyutu;
                    satirCount++;
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new File(dizin + dosyaAdi);
    }

    private static String generateRandomString(int uzunluk) {
        String karakterler = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder rastgeleMetin = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < uzunluk; i++) {
            int rastgeleIndex = random.nextInt(karakterler.length());
            char rastgeleKarakter = karakterler.charAt(rastgeleIndex);
            rastgeleMetin.append(rastgeleKarakter);
        }

        return rastgeleMetin.toString();
    }
}
