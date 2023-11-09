package service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Class definition for Results Service
public class ResultSetService {

    private List<String> getHeaderColumns(final ResultSet resultSet) throws SQLException {
        List<String> headerColumns = new ArrayList<>();
        ResultSetMetaData rsMetaData = resultSet.getMetaData();
        int count = rsMetaData.getColumnCount();
        for(int i=1; i<=count; i++) {
            headerColumns.add(rsMetaData.getColumnLabel(i));
        }
        return headerColumns;
    }

    //Function to view From Result set
    public void viewFromResultSet(ResultSet resultSet) {
        try {
            List<String> headerColumns = getHeaderColumns(resultSet);

            if (!resultSet.isBeforeFirst() ) {
                //If there is no data found, print an informative message
                System.out.println("-----------------");
                System.out.println("NO DATA");
                System.out.println("-----------------");
            } else {
                while(resultSet.next()) {
                    System.out.println("-----------------");
                    for(int i=1; i<=headerColumns.size(); i++) {
                        String columnValue = resultSet.getString(i);
                        System.out.println(headerColumns.get(i-1) + ": " + columnValue);
                    }
                }
                System.out.println("-----------------");
            }

        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
            return;
        }
    }

    //Function to run Query and Print the output
    public void runQueryAndPrintOutput(final Connection connection, final String sqlQuery) {

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            List<String> headerColumns = getHeaderColumns(resultSet);

            if (!resultSet.isBeforeFirst() ) {
                System.out.println("-----------------");
                System.out.println("NO DATA");
                System.out.println("-----------------");

                return;
            }

            System.out.println("-------------------------------------");

            System.out.print("|");
            for(int i=0; i<headerColumns.size(); i++) {
                System.out.print("\t" + headerColumns.get(i) + "\t|");
            }
            System.out.println();
            while(resultSet.next()) {
                System.out.print("|");
                for(int i=1; i<=headerColumns.size(); i++) {
                    String columnValue = resultSet.getString(i);
                    System.out.print("\t" + columnValue + "\t|");
                }
                System.out.println();
            }

            System.out.println("-------------------------------------");

        } catch (Exception e) {
            System.out.println("Exception Occurred: " + e.getMessage());
        }

    }

    //Function to run Query and Print output in row format
    public void runQueryAndPrintOutputRowFormat(final Connection connection, final String sqlQuery) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            viewFromResultSet(resultSet);
        } catch (Exception e) {
            System.out.println("Exception Occurred: " + e.getMessage());
        }
    }

}
