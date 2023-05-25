package src.framework.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileHelper {
    protected static boolean cut_falg = false;
    protected static boolean cope_falg = false;
    protected static boolean delete_falg = false;

    // 记录日志的通用方法
    public static void Log(String content) {

        try {
            String filepath;
            // 如果目录不存在，则创建目录
            String directory = new File("./log").getPath();
            if (!directory.endsWith(File.separator)) {
                directory = directory + File.separator;
            }
            File dir = new File(directory);
            dir.mkdirs();

            // 日志文件按日期进行命名
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            filepath = directory + File.separator + format.format(new Date()) + ".txt";

            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
            WriteFile(filepath, format2.format(new Date()) + content + System.getProperty("line.separator"), true);
        } catch (Exception ex) {

        }
    }

    /**
     * 读取文件
     * 
     * @param filepath 文件路径
     */
    public static String ReadFile(String filepath) {
        String encoding = "UTF-8";
        return ReadFile(filepath, encoding);
    }

    /**
     * 读取文件，指定编码
     * 
     * @param filepath 文件路径
     */
    public static String ReadFile(String filepath, String encoding) {
        try {
            filepath = URLDecoder.decode(filepath, "utf-8");
            File file = new File(filepath);
            Long filelength = file.length();
            byte[] filecontent = new byte[filelength.intValue()];
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
            return new String(filecontent, encoding);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将字符串写入文件
     * 
     * @param filepath
     * @param text
     * @param isAppend
     */
    public static void WriteFile(String filepath, String text, boolean isAppend) {

        try {
            filepath = URLDecoder.decode(filepath, "utf-8");
            File file = new File(filepath);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream f = new FileOutputStream(filepath, isAppend);
            f.write(text.getBytes());
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void WriteFile(String filepath, String text, String encodeing) {

        try {
            filepath = URLDecoder.decode(filepath, "utf-8");
            File file = new File(filepath);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            // FileOutputStream f = new FileOutputStream(filepath, "GBK");
            // f.write(text.getBytes());
            // f.close();

            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filepath), encodeing);
            writer.append(text);
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * 
     * @方法名称：CutFile @描述： 单文件剪切/目录文件剪切功能实现 单文件剪切操作（1）： File src = new
     *               File("F://work//s2sh.jpg"); 剪切文件路径 File desc = new
     *               File("F://AAA//"); 存放目录路径 falg = CutFile( src, desc, true ,
     *               true); 返回文件剪切成功与失败状态(测试通过) 单文件剪切操作（2）： File src = new
     *               File("F://work//s2sh.jpg"); 剪切文件路径 File src = new
     *               File("F://AAA//s2sh.jpg"); 存放后全路径 falg = CutFile( src, desc,
     *               true , true); 返回文件剪切成功与失败状态(测试通过) 文件目录剪切操作(1): File src = new
     *               File("F://testB"); 源文件所在目录 File desc = new
     *               File("F://AAA//testB"); 文件剪切到目录全路径 falg = CutFile( src, desc,
     *               true , true); 返回文件剪切成功与失败状态(测试通过) @作者： 谢泽鹏 @创建日期： 2012-7-2
     * @参数：@param src 源文件夹
     * @参数：@param desc 目标文夹
     * @参数：@param boolCover 如(源/目)文件目录同名
     * @参数：@param boolCut 如是否是剪切操作，
     * @参数：@throws Exception 异常处理
     * @参数：@return falg = true 文件剪切成功。falg = false 文件剪切失败。
     */
    public static boolean CutFile(File src, File desc, boolean boolCover, boolean boolCut) {
        try {
            if (src.isFile()) {
                if (!desc.isFile() || boolCover)
                    desc.createNewFile();
                cut_falg = CopeFile(src, desc);
                if (boolCut) {
                    src.delete();
                }
            } else if (src.isDirectory()) {
                desc.mkdirs();
                File[] list = src.listFiles();
                for (int i = 0; i < list.length; i++) {
                    String fileName = list[i].getAbsolutePath().substring(src.getAbsolutePath().length(),
                            list[i].getAbsolutePath().length());
                    File descFile = new File(desc.getAbsolutePath() + fileName);
                    CutFile(list[i], descFile, boolCover, boolCut);
                }
                if (boolCut) {
                    src.delete();
                }
            }
        } catch (Exception e) {
            cut_falg = false;
            e.printStackTrace();
            System.err.println("文件剪切操作出现异常!" + e.getMessage());
        }
        return cut_falg;
    }

    /***
     * 
     * @方法名称：CopeFile @描述： 单文件或多文件目录复制操作 单文件复制形式1： File src = new
     *                File("F://work//s2sh.jpg"); 源文件全路径 File desc = new
     *                File("F://AAA//"); 需要复制文件路径 falg = CopeFile(src, desc);
     *                返回复制成功与失败状态(测试通过) 单文件复制形式2： File src = new
     *                File("F://work//s2sh.jpg"); 源文件全路径 File desc = new
     *                File("F://AAA//s2sh.jpg"); 需要复制文件路径 falg = CopeFile(src,
     *                desc); 返回复制成功与失败状态(测试通过) 目录复制形式1： File src = new
     *                File("F://test"); 源文件目录路径 File desc = new
     *                File("F://AAA//test"); 复制目录下全路径 falg = CopeFile(src, desc);
     *                返回复制成功与失败状态(测试通过) @作者： 谢泽鹏 @创建日期： 2012-7-2
     * @参数：@param src 源文件的全路径
     * @参数：@param desc 复制文件路径
     * @参数：@throws Exception 异常处理
     * @参数：@return falg = true 复制操作成功。falg = false 复制操作失败。
     */
    public static boolean CopeFile(File src, File desc) {
        // 创建字节流对象(输入,输出)
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        // 创建文件输入流,输入流对象
        FileInputStream srcInputStream = null;
        FileOutputStream descOutputStream = null;
        // 记录同文件复制数量操作
        int count = 0;
        // 是否存在相同文件
        boolean boolCover = false;
        // 单文件复制操作实现
        if (src.isFile()) {
            try {
                // 获取需要复制下目录列表文件数组
                File[] list = desc.listFiles();
                // 获取复制文件名
                String srcname = src.toString().substring(src.toString().lastIndexOf("\\") + 1, src.toString().length())
                        .trim();
                if (null != list) {
                    if (list.length > 0) {
                        // 循环判断复制目录下是否和源文名相同
                        for (int i = 0; i < list.length; i++) {
                            // 获取复制目录下文件名
                            String descname = list[i].toString()
                                    .substring(list[i].toString().lastIndexOf("\\") + 1, list[i].toString().length())
                                    .trim();
                            // 判定复制文件名和目录文件名相同，记录重复数为1
                            if (srcname.equals(descname)) {
                                count = count + 1;
                                boolCover = true;
                            }
                            if (descname.indexOf("复件") != -1 && descname
                                    .indexOf(srcname.substring(srcname.indexOf(")") + 1, srcname.length())) != -1) {
                                count = count + 1;
                            }
                        }
                    }
                }
                // 存在重复文件信息
                if (boolCover) {
                    if (count == 1) {
                        if (desc.toString().indexOf(".") != -1) {
                            // 向磁盘中写入： 复件 + 复制文件名称
                            descOutputStream = new FileOutputStream(desc.toString() + "\\复件 ");
                        } else {
                            // 向磁盘中写入： 复件 + 复制文件名称
                            descOutputStream = new FileOutputStream(desc.toString() + "\\复件 " + srcname);
                        }
                    } else {
                        if (desc.toString().indexOf(".") != -1) {
                            // 向磁盘中写入： 复件(记录数)+ 复制文件名称
                            descOutputStream = new FileOutputStream(desc.toString() + "\\复件 (" + count + ") ");
                        } else {
                            // 向磁盘中写入： 复件(记录数)+ 复制文件名称
                            descOutputStream = new FileOutputStream(
                                    desc.toString() + "\\复件 (" + count + ") " + srcname);
                        }
                    }
                } else {
                    if (desc.toString().indexOf(".") != -1) {
                        descOutputStream = new FileOutputStream(desc.toString() + "\\");
                    } else {
                        descOutputStream = new FileOutputStream(desc.toString() + "\\" + srcname);
                    }
                }
                byte[] buf = new byte[1];
                srcInputStream = new FileInputStream(src);
                bis = new BufferedInputStream(srcInputStream);
                bos = new BufferedOutputStream(descOutputStream);
                while (bis.read(buf) != -1) {
                    bos.write(buf);
                    bos.flush();
                }
                cope_falg = true;
            } catch (Exception e) {
                cope_falg = false;
                e.printStackTrace();
                System.err.println("文件复制操作出现异常!" + e.getMessage());
            } finally {
                try {
                    if (bis != null) {
                        bis.close();
                    }
                    if (bos != null) {
                        bos.close();
                    }
                } catch (IOException e) {
                    cope_falg = false;
                    e.printStackTrace();
                    System.err.println("文件复制操作出现异常!" + e.getMessage());
                }
            }
        } else if (src.isDirectory()) {
            // 创建目录
            desc.mkdir();
            File[] list = src.listFiles();
            // 循环向目标目录写如内容
            for (int i = 0; i < list.length; i++) {
                String fileName = list[i].getAbsolutePath().substring(src.getAbsolutePath().length(),
                        list[i].getAbsolutePath().length());
                File descFile = new File(desc.getAbsolutePath() + fileName);
                CopeFile(list[i], descFile);
            }
        }
        return cope_falg;
    }

    /***
     * 
     * @方法名称：RenameFile @描述： 用于对文件进行重命名操作 1：重命名：FileHelper.RenameFile(new
     *                  File("F:\\AAA\\A.txt"),"AA") 测试通过 @作者： 谢泽鹏 @创建日期： 2012-7-6
     * @参数：@param file 重命名文件对象
     * @参数：@param name 命名文件名称
     * @参数：@return rename_falg为true重命名成功,为false重命名失败。
     */
    public static boolean RenameFile(File file, String name) {
        String path = file.getParent();
        if (!path.endsWith(File.separator)) {
            path += File.separator;
        }
        return file.renameTo(new File(path + name));
    }

    /***
     * 
     * @方法名称：DeleteFile @描述： 用于对文件或文件夹进行删除操作 1：删除文件 FileHelper.DeleteFile(new
     *                  File("F:\\AAA\\A.txt")) 测试通过 2：删除目录
     *                  FileHelper.DeleteFile(new File("F:\\AAA\\work")) 测试通过 @作者：
     *                  谢泽鹏 @创建日期： 2012-7-6
     * @参数：@param file 删除文件对象
     * @参数：@return delete_falg为true删除文件/目录成功,为false删除文件/目录失败。
     */
    public static boolean DeleteFile(File file) {
        try {
            if (file.isFile()) {
                file.delete();
                delete_falg = true;
            } else if (file.isDirectory()) {
                File[] list = file.listFiles();
                for (int i = 0; i < list.length; i++) {
                    DeleteFile(list[i]);
                }
                file.delete();
            }
        } catch (Exception e) {
            delete_falg = false;
            e.printStackTrace();
            System.err.println("文件删除出现异常" + e.getMessage());
        }
        return delete_falg;
    }

    public static boolean DeleteFile(String file) {
        return DeleteFile(new File(file));
    }
}