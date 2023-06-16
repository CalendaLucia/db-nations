package org.learning.database.nations;

import java.sql.*;
import java.util.Scanner;

public class Bonus {
    public static void main(String[] args) {

        //definiamo i parametri di connessione
        String url = "jdbc:mysql://localhost:8889/db_nations";
        String user = "root";
        String password = "root";

        //PROVO AD APRIRE UNA CONNESSIONE AL DATABASE
        try (Connection connection = DriverManager.getConnection(url,user,password)){
            //stampo il catalogo dei database
            connection.getCatalog();

            //preparo lo statment sql per ottenere tutte le nazioni
            String allCountriesSql = """
        SELECT countries.name AS Nome, `country_id` AS ID, regions.name AS Regione, continents.name AS Continente
        FROM `countries` JOIN regions ON country_id = regions.region_id
        JOIN continents ON country_id = continents.continent_id
        ORDER BY Nome ASC;
    """;

            //chiedo alla connection di preparare lo statement per ottenere tutte le nazioni
            try (PreparedStatement allCountriesStatement = connection.prepareStatement(allCountriesSql)){
                //eseguo lo statement che restituisce un resultSet
                try (ResultSet allCountriesResult = allCountriesStatement.executeQuery()){
                    //itero sulle righe del resultSet e stampo le nazioni
                    while (allCountriesResult.next()){
                        String name = allCountriesResult.getString("Nome");
                        int countryId = allCountriesResult.getInt("ID");
                        String region = allCountriesResult.getString("Regione");
                        String continent = allCountriesResult.getString("Continente");

                        System.out.println("Nome: " + name);
                        System.out.println("ID: " + countryId);
                        System.out.println("Regione: " + region);
                        System.out.println("Continente: " + continent);
                        System.out.println("----------------------");
                    }
                }
            }

            //chiedo all'utente di inserire l'ID del paese per la ricerca
            Scanner scanner = new Scanner(System.in);
            System.out.print("Inserisci l'ID del paese: ");
            int searchCountryId = scanner.nextInt();

            //preparo lo statment sql per la ricerca basata sull'ID del paese
            String searchCountrySql = """
             SELECT countries.name AS Nome, `country_id` AS ID, regions.name AS Regione, continents.name AS Continente, languages.language AS Lingua
                        FROM `countries`
                        JOIN regions ON country_id = regions.region_id\s
                        JOIN continents ON country_id = continents.continent_id\s
                        JOIN languages ON country_id = languages.language_id
                       \s
                        WHERE country_id = ?
                        ORDER BY Nome ASC;
                    """;

            //chiedo alla connection di preparare lo statement per la ricerca
            try (PreparedStatement searchCountryStatement = connection.prepareStatement(searchCountrySql)){
                //imposto il parametro con l'ID del paese
                searchCountryStatement.setInt(1, searchCountryId);

                //eseguo lo statement che restituisce un resultSet
                try (ResultSet searchCountryResult = searchCountryStatement.executeQuery()){
                    //itero sulle righe del resultSet e stampo i risultati della ricerca
                    while (searchCountryResult.next()){
                        String name = searchCountryResult.getString("Nome");
                        int countryId = searchCountryResult.getInt("ID");
                        String region = searchCountryResult.getString("Regione");
                        String continent = searchCountryResult.getString("Continente");
                        String lenguage = searchCountryResult.getString("Lingua");

                        System.out.println("Risultato della ricerca:");
                        System.out.println("Nome: " + name);
                        System.out.println("ID: " + countryId);
                        System.out.println("Regione: " + region);
                        System.out.println("Continente: " + continent);
                        System.out.println("Lingua: " + lenguage);
                        System.out.println("----------------------");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
