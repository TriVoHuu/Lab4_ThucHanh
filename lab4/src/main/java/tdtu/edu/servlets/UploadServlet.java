package tdtu.edu.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

@WebServlet(name = "UploadServlet", urlPatterns = "/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        getServletContext().getRequestDispatcher("/upload.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String new_name = req.getParameter("file_name");
        String override = req.getParameter("override");
        String name = null;
        Part filePart = req.getPart("document");
        for (String content : filePart.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                name = content.substring(content.indexOf("=") + 1).trim().replace("\"", "");
                break;
            }
        }

        String extension = name.substring(name.lastIndexOf('.') + 1).toLowerCase();

        if(!check_extension(extension)) {
            req.setAttribute("error","Unsupported file extension");
            getServletContext().getRequestDispatcher("/upload.jsp").forward(req,resp);
        } else {
            String dir_path = req.getServletContext().getRealPath("/uploads");
            File file = new File(dir_path+"/"+new_name+"."+extension);
            if(override == null && file.exists()) {
                req.setAttribute("error","File already exists");
                getServletContext().getRequestDispatcher("/upload.jsp").forward(req,resp);
            } else if(override == "on" && file.exists()) {
                file.delete();
                for (Part part : req.getParts()) {
                    part.write(dir_path+"/"+new_name+"."+extension);
                    req.setAttribute("error","File has been overriden");
                    String tag = "<p>Click <a href=\""+dir_path+"/"+new_name+"."+extension+"\">here</a> to visit file</p>";
                    req.setAttribute("success", tag);
                    getServletContext().getRequestDispatcher("/upload.jsp").forward(req,resp);
                }
            } else {
                for (Part part : req.getParts()) {
                    part.write(dir_path+"/"+new_name+"."+extension);
                }
                req.setAttribute("error","File has been uploaded");
                String tag = "<p>Click <a href=\""+dir_path+"/"+new_name+"."+extension+"\">here</a> to visit file</p>";
                req.setAttribute("success", tag);
                getServletContext().getRequestDispatcher("/upload.jsp").forward(req,resp);
            }
        }
    }

    private static boolean check_extension(String extension) {
        List<String> supported_ex = new ArrayList<>();
        supported_ex.add("txt");
        supported_ex.add("doc");
        supported_ex.add("docx");
        supported_ex.add("img");
        supported_ex.add("pdf");
        supported_ex.add("rar");
        supported_ex.add("zip");

        if(supported_ex.contains(extension)) return true;
        return false;
    }
}
