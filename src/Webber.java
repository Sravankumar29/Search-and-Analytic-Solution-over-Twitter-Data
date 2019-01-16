

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Webber extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html");
        PrintWriter pw=resp.getWriter();

        String query=req.getParameter("txt");
        pw.write(query);
    }

  /*  public static void main(String args[]){

        String query="";


        try {
            //Desktop desktop = java.awt.Desktop.getDesktop();
            URL obj = new URL("http://localhost:8983/solr/DFR_CORE_IR3/select?q=%23Syria%20%23SALMA%20%23LATAKIA%5E6&fl=id%2Cscore&wt=json&indent=true&rows=20&defType=dismax");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " );
            System.out.println("Response Code : " + responseCode);
            BufferedReader in =new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            } in .close();
            //print in String
            // System.out.println(response.toString());

            JSONObject myresponse = new JSONObject(response.toString());
            System.out.print(myresponse);
            //desktop.browse(oURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
