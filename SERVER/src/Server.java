
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.net.*;
import java.sql.*;
import java.io.*;

public class Server {
    public static ResultSet rs;
    public static Statement stmt;
    public static Connection con;
    public static DataOutputStream out;
    public static void main(String []args) throws SQLException, IOException {
        try{

            //Connection with client
            int port1 = 1111; //port's number
            //Creating server's socket using port
            ServerSocket Server = new ServerSocket(port1);
            System.out.println("Waiting for a client...");
            //Waiting for connection
            Socket socket = Server.accept();
            System.out.println("I heard you.\nList of all tables:");
            String message=" ";
            System.out.println();
            //Creating input and output streams
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            DataInputStream in = new DataInputStream(sin);
            out = new DataOutputStream(sout);

            String answer="";

            while(true) {
                message = in.readUTF();
                System.out.println("\nI got your message. That's  : " + message);
                System.out.println("I'm sending you my answer\n");
                if(message.equals("Disconnect"))
                    break;
                if(message.equals("Connect to DB"))
                {
                    //Connection to database
                    System.out.println("Connection started...");
                    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
                    System.out.println("Driver Loaded...");
                    String server = "localhost\\sqlexpress";
                    int port = 1433;
                    String user = "sa";
                    String password = "samsungace3";//don't look
                    String database = "Lab2TestDB";//database name

                    String jdbcUrl = "jdbc:sqlserver://"+server+":"+port+";user="+user+";password="+password+";databaseName="+database+"";

                    con = DriverManager.getConnection(jdbcUrl);
                    System.out.println("Connected!!!");
                    stmt = con.createStatement();
                    rs = stmt.executeQuery("SELECT TABLE_NAME FROM [Sales of sweets].INFORMATION_SCHEMA.Tables where NOT TABLE_NAME='sysdiagrams'");

                    while(rs.next())
                    {
                        answer+=rs.getString(1)+" ";
                    }
                    out.writeUTF(answer);
                    out.flush();
                }
                else
                {
                    answer="";
                    String divider = "`div`";
                    if(message.charAt(0) == 's')
                    {
                        rs = stmt.executeQuery(message);
                        ResultSetMetaData rsmd=rs.getMetaData();
                        int col = rsmd.getColumnCount();

                        answer +=col+divider;
                        for (int i=1;i<=col;i++)
                        {
                            answer +=rsmd.getColumnName(i)+divider;
                        }
                        while (rs.next())
                        {
                            for(int i=1;i<=col;i++) {
                                answer+=rs.getString(i)+divider;
                            }
                        }
                    }
                    else
                    {
                        int ans = 0;
                        try {
                            ans = stmt.executeUpdate(message);
                        }
                        catch (SQLServerException ex)
                        {
                            System.out.print("Error");
                        }
                        if(ans>0)
                        {
                            answer = "0";
                        }
                        else
                        {
                            answer = "-1"+divider+"Invalid Query! Try again!";
                        }
                    }
                    out.writeUTF(answer);
                    out.flush();
                }
                System.out.print("My ANSWER: "+answer);
                answer="";
                message="";
                System.out.println("\nWaiting for the next message...");
                System.out.println();
            }
            System.out.print("DB is disconnected. Have a good day!");
            out.writeUTF("-1`div`DB is disconnected. Have a good day!");
            out.flush();
        }
        catch (Exception ex) {
            System.out.println("Error : "+ex);
        } finally {
            if (rs != null) try { rs.close(); } catch(Exception e) {}
            if (stmt != null) try { stmt.close(); } catch(Exception e) {}
            if (con != null) try { con.close(); } catch(Exception e) {}
        }
    }

}
