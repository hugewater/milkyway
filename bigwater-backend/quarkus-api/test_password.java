import com.app6768688.util.QCEncryptor;

public class test_password {
    public static void main(String[] args) {
        try {
            String password = "123456";
            String encrypted = QCEncryptor.encrypt(password);
            System.out.println("Encrypted password: " + encrypted);
            
            String storedHash = "5k13DrTlbdwTpQhkugDvWe8htCHUG71B";
            boolean matches = encrypted.equals(storedHash);
            System.out.println("Matches stored hash: " + matches);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
