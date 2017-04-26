package mapperTest;

import com.mysql.cj.jdbc.MysqlDataSource;
import mapper.CakeMapperNEW;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.After;
import static junit.framework.TestCase.assertEquals;

public class T_E_S_TCakeMapperNEW
{
    CakeMapperNEW cmn;
    
    public T_E_S_TCakeMapperNEW()
    {
        MysqlDataSource datasource = new MysqlDataSource();
        datasource.setURL("jdbc:mysql://localhost:3306/cupcakeshoptest");
        datasource.setUser("root");
        datasource.setPassword("root");
        
        cmn = new CakeMapperNEW(datasource);
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
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    @Test
    public void testCakeMapperNEW() throws Exception
    {
        System.out.println("testCakeMapperNEW");

        assertEquals(5, cmn.getAllTops().size());
        assertEquals(9, cmn.getAllBottoms().size());
        assertEquals(9, cmn.getCakeById(1).getName().length());
        assertEquals("ChoccoNut", cmn.getCakeById(1).getName());
    }
}