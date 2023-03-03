package tdtu.edu.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(name = "ImageServlet2", urlPatterns = "/image2")
public class ImageServlet2 extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String imagePath = request.getServletContext().getRealPath("/images/image1.jpg");
        File file = new File(imagePath);

        if (file.exists()) {
            // Set the response content type
            response.setContentType("application/octet-stream");

            // Set the Content-Disposition header to force download
            String fileName = file.getName();
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            // Copy the file contents to the response output stream
            InputStream in = new FileInputStream(file);
            OutputStream out = response.getOutputStream();
            byte[] buffer = new byte[4096];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.flush();
            out.close();
        } else {
            // Handle file not found case
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}

