package src.framework.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import src.framework.io.FileHelper;

public class CompileHelper {
    private static List<String> javaFiles = new ArrayList<String>();

    // 用于生成编译和运行的bat文件：将生成内容保存为bat文件即可。
    public static String GenerateRunCode() {
        String rootPath = System.getProperty("user.dir") + "\\";
        String drive = rootPath.substring(0, 2);
        String txt = "@echo off\n";
        txt += drive + "\n";
        txt += "cd " + rootPath + "\n";
        txt += "javac -encoding UTF-8 -d ";
        String bin = new File(rootPath, "bin").getPath();
        txt += bin;// 输出路径

        // 包引用
        txt += " -cp ";
        javaFiles.clear();
        listFile(new File(rootPath, "lib"));
        for (String str : javaFiles) {
            if (str.endsWith(".jar")) {
                txt += str + ";";
            }
        }
        // java文件
        javaFiles.clear();
        listFile(new File(rootPath, "src"));
        for (String str : javaFiles) {
            if (str.endsWith(".java")) {
                txt += str + " ";
            }
        }

        txt += "\n";
        txt += "cd " + bin + "\n";
        txt += "java -cp ";
        javaFiles.clear();
        listFile(new File(rootPath, "lib"));
        for (String str : javaFiles) {
            if (str.endsWith(".jar")) {
                txt += str + ";";
            }
        }
        txt += " src.Main\n";
        txt += "pause";
        FileHelper.WriteFile(new File(rootPath, "run.bat").getPath(), txt, "GBK");
        return txt;
    }

    public static void listFile(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    javaFiles.add(file.getPath());
                } else if (file.isDirectory()) {
                    listFile(file);// 递归遍历
                }
            }
        }
    }

}