package connector;

import java.sql.Connection;
import javax.sql.DataSource;

public class DBConnectorNEW
{
    private DataSource datasource;
    private Connection connection;
        
    public void setSource(DataSource ds)
    {
        datasource = ds;
    }
    
    public Connection getConnection()
    {
        return connection;
    }
    
    public void open()
    {
        try
        {
            if (connection == null || connection.isClosed())
            {
                connection = datasource.getConnection();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    public void close()
    {
        try
        {
            if(connection != null && !connection.isClosed())
            {
                connection.close();
                connection = null;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}