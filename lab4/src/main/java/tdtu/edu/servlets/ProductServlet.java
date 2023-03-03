package tdtu.edu.servlets;
import tdtu.edu.classes.Product;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


@WebServlet(name = "ProductServlet", urlPatterns = "/product")
public class ProductServlet extends HttpServlet {
    private static List<Product> productList = getAllProduct();
    public static List<Product> getAllProduct() {
        List<Product> ls = new ArrayList<>();
        ls.add(new Product("1", "iphone 11", 100));
        ls.add(new Product("2", "iphone 12", 300));
        ls.add(new Product("3", "Samsung Z flip", 200));
        ls.add(new Product("4", "apple", 10));
        ls.add(new Product("5", "computer", 1000));

        return ls;
    }

    public static Product getProductById(String id) {
        for(Product p : productList) {
            if(p.getId().equals(id)) return p;
        }
        return null;
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productId = request.getParameter("id");

        if(productId == null) {
            List<Product> productList = getAllProduct();
            JsonObject responseJson = new JsonObject();
            Gson gson = new Gson();
            responseJson.addProperty("id", 0);
            responseJson.addProperty("message", "Đọc sản phẩn thành công");
            responseJson.add("data", gson.toJsonTree(productList));

            response.setContentType("application/json; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(responseJson.toString());
            out.flush();
        } else {
            Product product = getProductById(productId);

            if(product == null) {
                JsonObject responseJson = new JsonObject();
                responseJson.addProperty("id", 1);
                responseJson.addProperty("message", "Không tìm thấy sản phẩm nào với mã số "+productId);

                response.setContentType("application/json; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.print(responseJson.toString());
                out.flush();
            } else {
                JsonObject responseJson = new JsonObject();
                Gson gson = new Gson();
                responseJson.addProperty("id", 0);
                responseJson.addProperty("message", "Success");
                responseJson.add("data", gson.toJsonTree(product));

                response.setContentType("application/json; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.print(responseJson.toString());
                out.flush();
            }
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        int price = Integer.parseInt(req.getParameter("price"));

        if (name == null || name.isEmpty() || id == null || id.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            sendErrorResponse(resp, "Missing required fields", null);
        } else {
            Product product = new Product(id, name, price);
            productList.add(product);
            sendSuccessResponse(resp, product);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        Product product = getProductById(id);

        if (product == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            sendErrorResponse(resp, "Product not found", null);
        } else {
            String name = req.getParameter("name");
            String price = req.getParameter("price");

            if (name != null && !name.isEmpty()) {
                product.setName(name);
            }
            if (price != null && !price.isEmpty()) {
                product.setPrice(Integer.parseInt(price));
            }
            sendSuccessResponse(resp, product);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        Product product = getProductById(id);

        if (product == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            sendErrorResponse(resp, "Product not found", null);
        } else {
            productList.remove(product);
            sendSuccessResponse(resp, null);
        }
    }

    private void sendSuccessResponse(HttpServletResponse response, Object data) throws IOException {
        sendResponse(response, 0, "Success", data);
    }

    private void sendErrorResponse(HttpServletResponse response, String message, Object data) throws IOException {
        sendResponse(response, 1, message, data);
    }

    public void sendResponse(HttpServletResponse response, int statusCode, String message, Object data) throws IOException {
        response.setStatus(statusCode);

        response.setContentType("application/json");
        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        String jsonData = gson.toJson(data);
        String json = "{ \"id\": " + statusCode + ", \"message\": \"" + message + "\", \"data\": " + jsonData + " }";

        out.print(json);
        out.flush();
    }

}
