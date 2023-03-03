package tdtu.edu.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String errorMessage = null;

        // Check if username and password are provided
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            errorMessage = "Username and password are required.";

        } else {
            // Check if username and password are correct
            boolean authenticated;
            try {
                authenticated = authenticateUser(username, password);
                if (!authenticated) {
                    errorMessage = "Name/Password does not match";
                } else {
                    errorMessage = "Name/Password match";
                }
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        // Redirect to login page
        req.setAttribute("error", errorMessage);
        getServletContext().getRequestDispatcher("/index.jsp").forward(req,resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/index.jsp").forward(req,resp);
    }

    private boolean authenticateUser(String username, String password)throws ClassNotFoundException {
        HashMap<String, String> accounts = new HashMap<>();
        accounts.put("admin","admin");
        accounts.put("trico", "0966188549");
        if(accounts.containsKey(username)) {
            if (password.equals(accounts.get(username))) return true;
        }
        return false;
    }
}
