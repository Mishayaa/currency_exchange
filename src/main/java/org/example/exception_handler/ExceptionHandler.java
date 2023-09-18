package org.example.exception_handler;



import org.example.mappers.JsonMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class ExceptionHandler {
    public static void handleException(int errorCode, String message, HttpServletResponse resp) throws IOException {
        resp.setStatus(errorCode);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String json = JsonMapper.toJson(new HashMap<>(){{put("message", message);}});
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }
}
