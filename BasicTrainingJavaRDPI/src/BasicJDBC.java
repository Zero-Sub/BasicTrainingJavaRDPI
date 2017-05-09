

import java.sql.*;
import java.util.Properties;


    public class BasicJDBC {

        private Connection connection = null;

        public static void main(java.lang.String[] args) {
            BasicJDBC test = new BasicJDBC();
            if (!test.rebuildTable()) {
                System.out.println("Failure occurred while setting up " +
                                           " for running the test.");
                System.out.println("Test will not continue.");
                System.exit(0);
            }

            test.runQuery();

            test.cleanup(); 
        }

        public BasicJDBC() {

            Properties properties = new Properties ();
            properties.put("user", "cujo");
            properties.put("password", "newtiger");

            try {

                Class.forName("com.ibm.db2.jdbc.app.DB2Driver");

                connection = DriverManager.getConnection("jdbc:db2:*local", properties);
            } catch (Exception e) {

                System.out.println("Caught exception: " + e.getMessage());
            }
        }

        public boolean rebuildTable() {


            try {

                Statement s = connection.createStatement();
                try {

                    s.executeUpdate("drop table qgpl.basicjdbc");
                } catch (SQLException e) {

                }

                s.executeUpdate("create table qgpl.basicjdbc(id int, name char(15))");

                s.executeUpdate("insert into qgpl.basicjdbc values(1, ’Frank Johnson’)");
                s.executeUpdate("insert into qgpl.basicjdbc values(2, ’Neil Schwartz’)");
                s.executeUpdate("insert into qgpl.basicjdbc values(3, ’Ben Rodman’)");
                s.executeUpdate("insert into qgpl.basicjdbc values(4, ’Dan Gloore’)");

                s.close();

                return true;
            } catch (SQLException sqle) {

                System.out.println("Error in rebuildTable: " + sqle.getMessage());
                return false;
            }
        }

        public void runQuery() {

            try {

                Statement s = connection.createStatement();

                ResultSet rs = s.executeQuery("select * from qgpl.basicjdbc");

                System.out.println("--------------------");
                int i = 0;

                while (rs.next()) {

                    System.out.println("| " + rs.getInt(1) + " | " + rs.getString(2) + "|");
                    i++;
                }

                System.out.println("--------------------");
                System.out.println("There were " + i + " rows returned.");
                System.out.println("Output is complete.");
            } catch (SQLException e) {

                System.out.println("SQLException exception: ");
                System.out.println("Message:....." + e.getMessage());
                System.out.println("SQLState:...." + e.getSQLState());
                System.out.println("Vendor Code:." + e.getErrorCode());
                e.printStackTrace();
            }
        }

        public void cleanup() {
            try {
                if (connection != null)
                    connection.close();
            } catch (Exception e) {
                System.out.println("Caught exception: ");
                e.printStackTrace();
            }
        }
    }