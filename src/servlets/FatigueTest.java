package servlets;

import com.oreilly.servlet.MultipartRequest;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

@WebServlet("/fatigueTest")
public class FatigueTest extends HttpServlet {

    private Process fatigueProcess;
    private Writer writeToFatigue;
    private Scanner fatigueOutput;
    private Scanner fatigueErrors;

    @Override
    public void init() throws ServletException {
        try {
            fatigueProcess = startFatigueProcess();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (fatigueProcess != null) {
            System.out.println("Started successfully");
        } else {
            System.out.println("Start failed");
            throw new ServletException();
        }

        writeToFatigue = new PrintWriter(fatigueProcess.getOutputStream());
        fatigueOutput = new Scanner(fatigueProcess.getInputStream());
        fatigueErrors = new Scanner(fatigueProcess.getErrorStream());
        System.out.println(fatigueOutput.nextLine());
        System.out.println(fatigueOutput.nextLine());
    }


    private boolean getFatigueTest() throws IOException {
        writeToFatigue.write("Start\n");
        writeToFatigue.flush();
        String output = "";
        while (!(output.equals("0") || output.equals("1"))) {
            try {
                output = fatigueOutput.nextLine();
                System.out.println(output);
            }catch (NoSuchElementException e){
                while (fatigueErrors.hasNext()){
                    System.out.println(fatigueErrors.next());
                }
            }
        }
        return output.equals("1");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("start uploading");
        MultipartRequest request = new MultipartRequest(req, "Z:\\code\\java\\FunnotesJServer\\photos", 5_000_000);
        System.out.println("pic uploaded");
        JSONObject object = new JSONObject();
        object.put("isTired", getFatigueTest());
        resp.getWriter().write(object.toString());
    }

    private Process startFatigueProcess() throws IOException {

        List<String> command = new ArrayList<>();

        command.add("C:\\python\\python.exe");
        command.add("Z:\\code\\java\\FunnotesJServer\\fatigue_detection\\main_script.py");
        command.add("-p");
        command.add("Z:\\code\\java\\FunnotesJServer\\photos");

        File workingDir = new File("Z:\\code\\java\\FunnotesJServer\\fatigue_detection");

        ProcessBuilder processBuilder = new ProcessBuilder(command);

        processBuilder.redirectErrorStream(true);


        processBuilder.directory(workingDir);
        Process process;
        process = processBuilder.start();
        return process;
    }

}
