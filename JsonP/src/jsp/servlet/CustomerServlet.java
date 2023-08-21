package jsp.servlet;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

//http://localhost:8080/pos_one/customer
//http://localhost:8080/pos_one/pages/customer? 404
//http://localhost:8080/customer? 404

//http://localhost:8080/pos_one/pages/customer//
//http:://localhost:8080/pos_one/pages/customer
//http:://localhost:8080/pos_one/pages/customer

@WebServlet(urlPatterns = {"/pages/customer"})
public class CustomerServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("select * from Customer");
            ResultSet rst = pstm.executeQuery();
            resp.addHeader("Content-Type", "application/json");

//            ArrayList<CustomerDTO> allCustomers = new ArrayList<>();
//            String json = "[";
//            while (rst.next()) {
//                String customer = "{";
//                String id = rst.getString(1);
//                String name = rst.getString(2);
//                String address = rst.getString(3);
////                allCustomers.add(new CustomerDTO(id, name, address));
//                customer+="\"id\":\""+id+"\",";
//                customer+="\"name\":\""+name+"\",";
//                customer+="\"address\":\""+address+"\"";
//                customer+="},";
//                json += customer;
//            }
//            json += "]";
//            req.setAttribute("keyOne", allCustomers);
//
//            req.getRequestDispatcher("customer.html").forward(req, resp);
            //create json response including customer data
            // String s= "[{id:""}]";
            //send the output through the ajax response
            // resp.setContentType("application/json");
            //  resp.getWriter().print(json.substring(0,json.length()-2)+"]");
            JsonArrayBuilder allCustomers = Json.createArrayBuilder();
            while (rst.next()) {
                String customer = "{";
                String id = rst.getString(1);
                String name = rst.getString(2);
                String address = rst.getString(3);
//                String salary = rst.getString(4);

                JsonObjectBuilder customerObject = Json.createObjectBuilder();
                customerObject.add("id", id);
                customerObject.add("name", name);
                customerObject.add("address", address);
//                customerObject.add("salary",salary);
                allCustomers.add(customerObject.build());
            }

            resp.getWriter().print(allCustomers.build());

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cusID = req.getParameter("cusID");
        String cusName = req.getParameter("cusName");
        String cusAddress = req.getParameter("cusAddress");
//        String cusSalary = req.getParameter("cusSalary");
//        String option = req.getParameter("option");
        resp.addHeader("Content-Type", "application/json");


        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos", "root", "1234");

//            switch (option) {
//                case "add":
            PreparedStatement pstm = connection.prepareStatement("insert into customer values(?,?,?)");
            pstm.setObject(1, cusID);
            pstm.setObject(2, cusName);
            pstm.setObject(3, cusAddress);

            if (pstm.executeUpdate() > 0) {
                JsonObjectBuilder customerObj = Json.createObjectBuilder();
                customerObj.add("state", "Ok");
                customerObj.add("message", "Suucessfully Added");
                customerObj.add("data", " ");
                //resp.getWriter().println("Customer Added..!");
                resp.getWriter().print(customerObj.build());

            }

//                    break;
//                case "delete":
//                    PreparedStatement pstm2 = connection.prepareStatement("delete from Customer where id=?");
//                    pstm2.setObject(1, cusID);
//                    resp.addHeader("Content-Type", "application/json");
//                    if (pstm2.executeUpdate() > 0) {
////                        resp.getWriter().println("Customer Deleted..!");
//                        JsonObjectBuilder customerObj = Json.createObjectBuilder();
//                        customerObj.add("state", "Ok");
//                        customerObj.add("message", "Sucessfully Deleted Customer");
//                        customerObj.add("data", " ");
//                        //resp.getWriter().println("Customer Added..!");
//                        resp.getWriter().print(customerObj.build());
//                    }
//                    break;
//                case "update":
//                    PreparedStatement pstm3 = connection.prepareStatement("update Customer set name=?,address=? where id=?");
//                    pstm3.setObject(3, cusID);
//                    pstm3.setObject(1, cusName);
//                    pstm3.setObject(2, cusAddress);
//                    resp.addHeader("Content-Type", "application/json");
//
//                    if (pstm3.executeUpdate() > 0) {
//                        //resp.getWriter().println("Customer Updated..!");
//                        JsonObjectBuilder customerObj = Json.createObjectBuilder();
//                        customerObj.add("state", "Ok");
//                        customerObj.add("message", "Sucessfully Updated Customer");
//                        customerObj.add("data", " ");
//                        //resp.getWriter().println("Customer Added..!");
//                        resp.getWriter().print(customerObj.build());
//                    }
//                    break;
//            }

//            resp.sendRedirect("/jsonp/pages/customer");

        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);


        } catch (SQLException e) {
            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("state", "Error");
            response.add("message", e.getMessage());
            response.add("data", "");
            resp.setStatus(400);
            resp.getWriter().print(response.build());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Content-Type","application/json");
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject customerOB = reader.readObject();
        String cusID = customerOB.getString("id");
        String cusName = customerOB.getString("name");
        String cusAddress = customerOB.getString("address");
        String cusSalary = customerOB.getString("salary");



        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos", "root", "1234");
            PreparedStatement pstm3 = connection.prepareStatement("update customer set name=?,address=? where id=?");
            pstm3.setObject(3, cusID);
            pstm3.setObject(1, cusName);
            pstm3.setObject(2, cusAddress);

            resp.addHeader("Content-Type", "application/json");

            if (pstm3.executeUpdate() > 0) {
                //resp.getWriter().println("Customer Updated..!");
                JsonObjectBuilder customerObj = Json.createObjectBuilder();
                resp.addHeader("Content-Type", "application/json");

                customerObj.add("state", "Ok");
                customerObj.add("message", "Sucessfully Updated Customer");
                customerObj.add("data", " ");
                //resp.getWriter().println("Customer Added..!");
                resp.getWriter().print(customerObj.build());
            }

        } catch (SQLException e) {
            JsonObjectBuilder response = Json.createObjectBuilder();
            resp.addHeader("Content-Type", "application/json");

            response.add("state", "Error");
            response.add("message", e.getMessage());
            response.add("data", "");
            resp.setStatus(400);
            resp.getWriter().print(response.build());
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }

//            resp.sendRedirect("/jsonp/pages/customer");


    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cusID = req.getParameter("cusID");
        resp.addHeader("Content-Type", "application/json");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos", "root", "1234");
            PreparedStatement pstm2 = connection.prepareStatement("delete from  customer where id=?");
            pstm2.setObject(1, cusID);

            if (pstm2.executeUpdate() > 0) {
//                        resp.getWriter().println("Customer Deleted..!");
                JsonObjectBuilder customerObj = Json.createObjectBuilder();
                resp.addHeader("Content-Type", "application/json");
                customerObj.add("state", "Ok");
                customerObj.add("message", "Sucessfully Deleted Customer");
                customerObj.add("data", " ");
                //resp.getWriter().println("Customer Added..!");
                resp.getWriter().print(customerObj.build());
            }
        } catch (ClassNotFoundException e) {
            JsonObjectBuilder response = Json.createObjectBuilder();
            resp.addHeader("Content-Type", "application/json");

            response.add("state", "Error");
            response.add("message", e.getMessage());

            resp.getWriter().print(response.build());
        } catch (SQLException e) {
            JsonObjectBuilder response = Json.createObjectBuilder();
            resp.addHeader("Content-Type", "application/json");

            response.add("state", "Error");
            response.add("message", e.getMessage());
            response.add("data", "");

            resp.getWriter().print(response.build());
        }
    }
}
