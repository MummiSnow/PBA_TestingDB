package mockitoTest;

import mapper.CakeMapperNEW;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class T_E_S_TMockito
{
    MysqlDataSource ds;
    Connection c;
    PreparedStatement stmt;
    ResultSet rs;
    
    CakeMapperNEW cmn;

    public T_E_S_TMockito()
    {
    }

    @BeforeClass
    public static void setUpClass()
    {

    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    @Before
    public void setUp() throws Exception
    {
        ds = mock(MysqlDataSource.class);
        c = mock(Connection.class);
        stmt = mock(PreparedStatement.class);
        rs = mock(ResultSet.class);
        
        cmn = new CakeMapperNEW(ds);
        
        assertNotNull(ds);
        when(c.prepareStatement(any(String.class))).thenReturn(stmt);
        when(ds.getConnection()).thenReturn(c);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("idBottom")).thenReturn(1);
        when(rs.getInt("idTopping")).thenReturn(1);
        when(rs.getString("cupcakeName")).thenReturn("Nuts with Chocolate");
        when(rs.getString("cupcakeToppingName")).thenReturn("Chocolate");
        when(rs.getDouble("price")).thenReturn(22.45);
        when(stmt.executeQuery()).thenReturn(rs);
        when(c.prepareStatement(any(String.class))).thenReturn(stmt);
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void testMockito_getCakeById() throws Exception
    {
        System.out.println("testMockito_getCakeById");
        
        cmn.getCakeById(1);

        verify(ds, times(3)).getConnection();
        verify(c, times(3)).close();
    }
    
    @Test
    public void testMockito_getTop() throws Exception
    {
        System.out.println("testMockito_getTop");
        
        assertEquals(1, cmn.getTop(1).getId());
        assertEquals("Chocolate", cmn.getTop(1).getName());
        assertEquals(22.45, cmn.getTop(1).getPrice());
    }
}