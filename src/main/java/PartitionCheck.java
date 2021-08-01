import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import io.prestosql.jdbc.PrestoConnection;

import static java.sql.DriverManager.getConnection;

public class PartitionCheck {
    public static void main (String args[]) throws Exception {

        System.setProperty("from_sql", args[0]);
        System.setProperty("where_sql", args[1]);
        System.setProperty("root_folder", args[2]);

        String from_sql = System.getProperty("from_sql");
        String where_sql = System.getProperty("where_sql");
        String root_folder = System.getProperty("root_folder");

        System.setProperty("javax.net.ssl.trustStore", root_folder + "myTrustStore");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

        String count = "";

        String encryptedPassword = "hks9ssVIPIJex3zHUGaMiA==";
        String decryptedPassword = "";

        PasswordRequest r = new PasswordRequest();
        r.Login();
        String secretKey = r.Retrieve();

        try
        {
            EncryptDecrypt ed = new EncryptDecrypt(secretKey);
            decryptedPassword = ed.decrypt(encryptedPassword);
        }
        catch (Exception e)
        {
            count = "-1";
            e.printStackTrace();
        }

        String url = "jdbc:presto://presto-datadiscovery.skumart.com:8443/hive?SSL=true"
                + "&SSLTrustStorePath=" + root_folder + "truststore.ks"
                + "&SSLTrustStorePassword="+URLEncoder.encode("kL+}H+A+.}-*^57w");

        try
        {
            Class.forName("io.prestosql.jdbc.PrestoDriver");
            Connection c = getConnection(url, "abccuewdatfs", decryptedPassword);

            PrestoConnection pc = c.unwrap(PrestoConnection.class);

            String sql = "select count(*) from " + from_sql + " where " + where_sql;

            Statement s1 = pc.createStatement();
            ResultSet rs = s1.executeQuery(sql);

            while(rs.next())
            {
                count = rs.getString(1);
            }

            pc.close();
        }
        catch (Exception e)
        {
            count = "-1";
            e.printStackTrace();
        }
        System.out.println(count);
    }
}
