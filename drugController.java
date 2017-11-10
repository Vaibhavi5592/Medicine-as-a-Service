package com.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("drugController")
public class drugController {
    
     @GET
    @Path("/getdata/{disease}")
    @Produces(MediaType.APPLICATION_JSON)
     public ArrayList<testDrug> getDrugInJSON(@PathParam("disease") String dis) throws ClassNotFoundException, SQLException
     {
         ArrayList<testDrug> t = new ArrayList<>();
         System.out.println("hello");
        // String username="test";
        // String password="1234";
         StringBuilder q = new StringBuilder();
         String query = "select disease,drug,trade from druginfo where disease like '"+dis+"%'";
              Connection con=null;
//            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Class.forName("oracle.jdbc.OracleDriver");
            con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "system", "SAP12345");

         //con= DriverManager.getConnection("jdbc:derby://localhost:1527/druginfo",username,password);
         
         Statement st = con.createStatement();
         ResultSet rs = st.executeQuery(query);
         while(rs.next())
         {
             testDrug tm = new testDrug();            
             tm.setDrug(rs.getString("drug"));
             tm.setTrade(rs.getString("trade"));
             tm.setDisease(rs.getString("disease"));
             t.add(tm);
         }
         return t;
     }
}
