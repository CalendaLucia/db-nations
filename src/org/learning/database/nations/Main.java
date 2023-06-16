package org.learning.database.nations;

import java.sql.*;

import static java.sql.DriverManager.getConnection;

public class Main {

    public static void main(String[] args) {
        //definiamo i parametri di connessione
        String url = "jdbc:mysql://localhost:8889/db_nations";
        String user = "root";
        String password = "root";



        //provo ad aprire una connessione al database
        try(Connection connection = DriverManager.getConnection(url,user,password)) {
            //stampo il catalogo dei database
            connection.getCatalog();
            //preparo lo statment sql da eseguire
            String sql = """
                SELECT countries.name AS Nome, `country_id` AS ID, regions.name AS Regione, continents.name AS Continente 
                FROM `countries` JOIN regions ON country_id = regions.region_id 
                JOIN continents ON country_id = continents.continent_id 
                ORDER BY Nome ASC;
            """;

            //chiedo alla connection di preparare lo statement
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                //eseguo lo statmnet che restituisce un resultSet

                //sql injection per proteggere dal codice malevolo  si fa prima della executeQuery
                //inserire i punti interrogativi nelle query dove si richiedere un paramentro dall utente
                // ps.setInt(1, paramentro1input);
                // ps.setString(2, paramentro2input);

                try (ResultSet rs = ps.executeQuery()) {
                    //itero sulle righe del resultSet
                    while (rs.next()){
                        //per ogni riga prendo i valori dalle singole colonne

                        String name = rs.getString("nome");
                        String id= rs.getString("id");
                        String regione = rs.getString("regione");
                        String continente = rs.getString("continente");
                        System.out.println("Nome Nazione: " + name + " Id: " +  id + " Regione: " + regione  + " Continente: " + continente);
                    }

                }
            }

        } catch (SQLException e ) {
            System.out.println("Unable to connetc to database");
            e.printStackTrace();
        }
    }
}






