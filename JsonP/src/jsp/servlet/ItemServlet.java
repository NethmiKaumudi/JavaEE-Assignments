package jsp.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet(urlPatterns = {"/pages/item"})
public class ItemServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("select * from item");
            ResultSet rst = pstm.executeQuery();

            // ArrayList<ItemDTO> allItems = new ArrayList<>();
            resp.addHeader("Content-Type", "application/json");
            String json = "[";
            while (rst.next()) {
                String item = "{";
                String code = rst.getString(1);
                String name = rst.getString(2);
                int qty = rst.getInt(3);
                double unitPrice = rst.getDouble(4);
                // allItems.add(new ItemDTO(code, name, qtyOnHand, unitPrice));
                item += "\"code\":\"" + code + "\",";
                item += "\"name\":\"" + name + "\",";
                item += "\"qty\":\"" + qty + "\",";
                item += "\"unitPrice\":\"" + unitPrice + "\"";
                item += "},";
                json += item;
            }
            json += "]";
            resp.getWriter().print(json.substring(0, json.length() - 2) + "]");
//            req.setAttribute("keyTwo", allItems);
//
//            req.getRequestDispatcher("item.html").forward(req, resp);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String code = req.getParameter("code");
        String itemName = req.getParameter("description");
        String qty = req.getParameter("qty");
        String unitPrice = req.getParameter("unitPrice");
        String option = req.getParameter("option");
//
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos", "root", "1234");
            switch (option) {
                case "add":
                    PreparedStatement pstm = connection.prepareStatement("insert into item values(?,?,?,?)");
                    pstm.setObject(1, code);
                    pstm.setObject(2, itemName);
                    pstm.setObject(3, qty);
                    pstm.setObject(4, unitPrice);
                    if (pstm.executeUpdate() > 0) {
                        resp.getWriter().println("Item Added..!");
                        resp.sendRedirect("item");
                    }
                    break;
                case "delete":
                    PreparedStatement pstm2 = connection.prepareStatement("delete from item where code=?");
                    pstm2.setObject(1, code);
                    if (pstm2.executeUpdate() > 0) {
                        resp.getWriter().println("Item Deleted..!");
                        resp.sendRedirect("/jsonp/pages/item.html");
                    }
                    break;
                case "update":
                    PreparedStatement pstm3 = connection.prepareStatement("update item set itemName=?,qty=?,price=? where code=?");
                    pstm3.setObject(1, itemName);
                    pstm3.setObject(2, qty);
                    pstm3.setObject(3, unitPrice);
                    pstm3.setObject(4, code);
                    if (pstm3.executeUpdate() > 0) {
                        resp.getWriter().println("Item Updated..!");
                        resp.sendRedirect("/jsonp/pages/item.html");
                    }
                    break;
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
