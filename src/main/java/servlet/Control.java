package servlet;

import com.mysql.cj.jdbc.MysqlDataSource;
import mapper.InvoiceMapper;
import mapper.UserMapper;
import entity.Bottom;
import entity.Cake;
import entity.Invoice;
import entity.Top;
import entity.User;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mapper.CakeMapperNEW;

@WebServlet(name = "Control", urlPatterns =
{
    "/Control"
})
public class Control extends HttpServlet
{
    UserMapper um = new UserMapper();
    InvoiceMapper im = new InvoiceMapper();
    CakeMapperNEW cmn;
    
    List<Bottom> bottoms;
    List<Top> tops;
    
    public Control()
    {
        MysqlDataSource datasource = new MysqlDataSource();
        datasource.setURL("jdbc:mysql://localhost:3306/cupcakeshoptest");
        datasource.setUser("root");
        datasource.setPassword("root");
        
        cmn = new CakeMapperNEW(datasource);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        String origin = request.getParameter("origin");
        HttpSession session = request.getSession();
        User user = null;
        Invoice invoice = null;

        switch (origin)
        {
            case "login":
                if (request.getParameter("username") == null)
                {
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
                login(request, response);
                break;
            case "addProduct":
                Object i = session.getAttribute("invoice");
                if (i != null)
                {
                    invoice = (Invoice) i;
                } else
                {
                    Object u = session.getAttribute("user");
                    if (u != null)
                    {
                        user = (User) u;
                        int invoiceId = im.createInvoice(null, user.getIdUser());
                        invoice = im.getInvoiceById(invoiceId);
                    }
                }
                addProduct(invoice, request);
                request.getRequestDispatcher("products.jsp").forward(request, response);
                break;
            case "checkout":
                i = session.getAttribute("invoice");
                if (i != null)
                {
                    invoice = (Invoice) i;
                    im.checkInvoice(invoice.getId());
                    session.setAttribute("invoice", null);
                }
                break;
            default:
                throw new AssertionError();
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        HttpSession session = request.getSession();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = um.validateUser(username, password);
        session.setAttribute("user", user);
        populateProdLists(session);
        response.sendRedirect("products.jsp");
    }

    private void populateProdLists(HttpSession session)
    {
        tops = cmn.getAllTops();
        bottoms = cmn.getAllBottoms();
        session.setAttribute("toppings", tops);
        session.setAttribute("bottoms", bottoms);
    }

    private void addProduct(Invoice invoice, HttpServletRequest request) throws NumberFormatException
    {
        HttpSession session = request.getSession();
        int toppingId = Integer.parseInt(request.getParameter("topping"));
        int bottomId = Integer.parseInt(request.getParameter("bottom"));
        int qty = Integer.parseInt(request.getParameter("qty"));

        Bottom b = cmn.getBottom(bottomId);
        Top t = cmn.getTop(toppingId);
        Cake cc = cmn.createCake(b, t);
        im.addInvoiceLine(invoice.getId(), cc.getId(), qty);
        //reload the invoice
        invoice = im.getInvoiceById(invoice.getId());
        session.setAttribute("invoice", invoice);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo()
    {
        return "CupCakeShopServlet";
    }
}