package mapper;

import connector.DBConnectorNEW;
import entity.Bottom;
import entity.Cake;
import entity.Top;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class CakeMapperNEW
{
    private DBConnectorNEW dbcn = new DBConnectorNEW();
    
    public CakeMapperNEW()
    {
    }
    
    public CakeMapperNEW(DataSource ds)
    {
        dbcn.setSource(ds);
    }
    
    public boolean createTop(Top top)
    {        
        boolean created = false;
        
        try
        {
            dbcn.open();
            
            String sql = "INSERT INTO cupcakeTopping (cupcakeToppingName, price) VALUES (?,?)";
            PreparedStatement pstmt = dbcn.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, top.getName());
            pstmt.setDouble(2, top.getPrice());
            pstmt.executeUpdate();
            pstmt.close();
            
            created = true;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            dbcn.close();
        }
        
        return created;
    }

    public Cake createCake(Bottom bottom, Top top)
    {        
        Cake cake = null;
        
        try
        {
            dbcn.open();
            
            String sql = "INSERT INTO cupcake (cupcakeName, idTopping, idBottom) VALUES (?,?,?)";
            PreparedStatement pstmt = dbcn.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            String name = bottom.getName() + " with " + top.getName();
            pstmt.setString(1, name);
            pstmt.setInt(2, top.getId());
            pstmt.setInt(3, bottom.getId());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            
            if (rs.next())
            {
                cake = new Cake(rs.getInt(1), top, bottom, name);
            }
            
            rs.close();
            pstmt.close();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            dbcn.close();
        }
        
        return cake;
    }

    public boolean deleteTopById(int id)
    {        
        boolean deleted = false;
        
        try
        {
            dbcn.open();

            String sql = "delete from cupcaketopping WHERE idTopping = ?";
            PreparedStatement pstmt = dbcn.getConnection().prepareStatement(sql);
            pstmt.setInt(1, id);
            
            pstmt.executeUpdate();          
            pstmt.close();
            
            deleted = true;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            dbcn.close();
        }
    
        return deleted;
    }
    
    public Cake getCakeById(int id)
    {        
        Cake cake = null;
                
        int idTopping = 0;
        int idBottom = 0;
        
        try
        {
            dbcn.open();

            String sql = "select * from cupcake WHERE idCupcake = ?";
            PreparedStatement pstmt = dbcn.getConnection().prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next())
            {
                cake = new Cake();
                cake.setId(id);
                cake.setName(rs.getString("cupcakeName"));
                
                idTopping = rs.getInt("idTopping");
                idBottom = rs.getInt("idBottom");
            }
            
            rs.close();
            pstmt.close();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            dbcn.close();
        }
    
        if(cake != null)
        {
            cake.setTop(getTop(idTopping));
            cake.setBottom(getBottom(idBottom));
        }
        
        return cake;
    }
    
    public List<Top> getAllTops()
    {        
        List<Top> tops = new ArrayList();
       
        try
        {
            dbcn.open();
            
            String sql = "select idTopping, cupcakeToppingName, price from cupcaketopping";
            PreparedStatement pstmt = dbcn.getConnection().prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                int id = rs.getInt("idTopping");
                String name = rs.getString("cupcakeToppingName");
                double price = rs.getDouble("price");
                tops.add(new Top(id, name, price));
            }
            
            rs.close();
            pstmt.close();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            dbcn.close();
        }
        
        return tops;
    }
    
    public List<Bottom> getAllBottoms()
    {
        List<Bottom> bottoms = new ArrayList();
        
        try
        {
            dbcn.open();
            
            String sql = "select idBottom, cupcakeBottomName, price from cupcakebottom";
            PreparedStatement pstmt = dbcn.getConnection().prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next())
            {
                int id = rs.getInt("idBottom");
                String name = rs.getString("cupcakeBottomName");
                double price = rs.getDouble("price");
                bottoms.add(new Bottom(id, name, price));
            }
            
            rs.close();
            pstmt.close();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            dbcn.close();
        }
        
        return bottoms;
    }

    public Top getTop(int id)
    {        
        Top top = null;
        
        try
        {
            dbcn.open();
        
            String sql = "select cupcakeToppingName, price from cupcaketopping WHERE idTopping = ?";
            PreparedStatement pstmt = dbcn.getConnection().prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next())
            {
                String name = rs.getString("cupcakeToppingName");
                double price = rs.getDouble("price");
                top = new Top(id, name, price);
            }
            
            rs.close();
            pstmt.close();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            dbcn.close();
        }
        
        return top;
    }
    
    public Bottom getBottom(int id)
    {        
        Bottom bottom = null;
        
        try
        {
            dbcn.open();
        
            String sql = "select cupcakeBottomName, price from cupcakebottom WHERE idBottom = ?";
            PreparedStatement pstmt = dbcn.getConnection().prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next())
            {
                String name = rs.getString("cupcakeBottomName");
                double price = rs.getDouble("price");
                bottom = new Bottom(id, name, price);
            }
            
            rs.close();
            pstmt.close();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            dbcn.close();
        }
        
        return bottom;
    }
}