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

@WebServlet(name = "DownloadServlet", urlPatterns = "/download")
public class DownloadServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileName = request.getParameter("file");
        if (fileName == null) {
            response.getWriter().println("File not found");
            return;
        }

        String filePath = request.getServletContext().getRealPath("/files/" + fileName);
        File file = new File(filePath);

        if (file.exists() && !file.isDirectory()) {
            // Set the response content type
            response.setContentType("application/octet-stream");

            // Set the Content-Disposition header to force download
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            // Get the speed parameter from query string
            int speed = Integer.parseInt(request.getParameter("speed"));

            // Copy the file contents to the response output stream at the specified speed
            InputStream in = new FileInputStream(file);
            OutputStream out = response.getOutputStream();
            byte[] buffer = new byte[4096];
            int length;
            long startTime = System.currentTimeMillis();
            long bytesWritten = 0;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
                bytesWritten += length;
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime > 0) {
                    int bytesPerSecond = (int) (bytesWritten / elapsedTime);
                    if (bytesPerSecond > speed * 1024) {
                        // If speed limit is exceeded, sleep for a short time
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
            in.close();
            out.flush();
            out.close();
        } else {
            response.getWriter().println("File not found");
        }
    }

}

