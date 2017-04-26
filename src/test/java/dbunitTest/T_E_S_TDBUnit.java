package dbunitTest;

import mapper.CakeMapperNEW;
import com.mysql.cj.jdbc.MysqlDataSource;
import entity.Top;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import org.dbunit.Assertion;
import org.junit.Before;
import org.junit.Test;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;

public class T_E_S_TDBUnit
{
    CakeMapperNEW cmn;

    MysqlDataSource datasource;
    Connection connection;

    IDatabaseConnection dbConnection;
    IDataSet xmlDataSet;
    QueryDataSet databaseDataSet;
    ITable xmlTable, databaseTable;

    public T_E_S_TDBUnit()
    {
        datasource = new MysqlDataSource();
        datasource.setURL("jdbc:mysql://localhost:3306/cupcakeshoptest");
        datasource.setUser("tester");
        datasource.setPassword("tester");
        datasource.setDatabaseName("cupcakeshoptest");

        cmn = new CakeMapperNEW(datasource);
    }

    @Before
    public void setUp() throws Exception
    {
        try
        {
            connection = datasource.getConnection();

            dbConnection = new DatabaseConnection(connection, "cupcakeshoptest");

            xmlDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("dataset_Init-Topping.xml"));
            //DatabaseOperation.CLEAN_INSERT.execute(dbConnection, xmlDataSet);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        finally
        {
            //dbConnection.close();
        }
    }

    @Test
    public void testDBUnit1() throws Exception
    {
        System.out.println("TESTDBUnit1");

        cmn.createTop(new Top(6, "Coconut", 8));
        
        databaseDataSet = new QueryDataSet(dbConnection);
        databaseDataSet.addTable("cupcake");
        databaseDataSet.addTable("cupcaketopping");
        databaseDataSet.addTable("cupcakebottom");
        databaseDataSet.addTable("invoice");
        databaseDataSet.addTable("invoicedetails");
        databaseDataSet.addTable("user");
        databaseTable = databaseDataSet.getTable("cupcaketopping");

        //FlatXmlDataSet.write(databaseDataSet, new FileOutputStream("dataset_Init-Topping.xml"));
        //FlatDtdDataSet.write(databaseDataSet, new FileOutputStream("dataset_Init-Topping.dtd"));

        xmlDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("dataset_Created-Topping.xml"));
        xmlTable = xmlDataSet.getTable("cupcaketopping");

        Assertion.assertEquals(xmlTable, databaseTable);
    }
}
