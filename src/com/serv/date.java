package com.serv;

import jdk.internal.org.objectweb.asm.commons.SimpleRemapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.python.core.PyInstance;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

public class date {

    static PythonInterpreter interpreter = null;
    public static void main(String args[]) throws ParseException, IOException {

        String dt = "2018-11-22T17:00:00Z";


        DateFormat recievedDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:00:00'Z'");
        Date parsedRecDate = recievedDateFormat.parse(dt);
        //      System.out.print("Created: "+s.getCreatedAt().toString());
        DateFormat solrDateForm = new SimpleDateFormat("dd MMMM yyyy");
        String finalDate = solrDateForm.format(parsedRecDate);
        System.out.println(finalDate);

        String[] tweets={"I’m feeling quite down actually",
                "The news really lifted my spirits.",
                "My heart sank when the phone rang :(",
                "Things are looking good today.",
                "despair",
                "Don’t look so down in the mouth!",":/",
                "😂","\uD83D\uDE00"
        };

        Map<String,String> sentiments=new HashMap<>();
        String dir = System.getProperty("user.dir");
        System.out.println(dir);

        URL resource = date.class.getResource("senti.py");
        System.out.println("URL to resource: " + resource );
        String pythonScriptPath = dir+"/src/com/serv/senti.py";
        System.out.println(pythonScriptPath);
        String[] cmd = new String[2];
        cmd[0] = "python"; // check version of installed python: python -V
        cmd[1] = pythonScriptPath;

// create runtime to execute external command
        Runtime rt = Runtime.getRuntime();

        for(String s:tweets){
            Process pr = rt.exec(new String[]{cmd[0],cmd[1], s});

            BufferedReader bfr = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line = "";
            while ((line = bfr.readLine()) != null) {

                sentiments.put(s,line);
            }
        }


    for(String s:sentiments.keySet()){
        System.out.println(s+" "+sentiments.get(s));
    }
    }
}
