package com.i2s.flawlog;

/**
 * Created by wxy on 2017/11/26.
 */

import com.i2s.flawlog.domain.bo.MetalBO;
import com.i2s.flawlog.service.MetalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.util.List;

@Component
@Configuration
public class FileRead {
    public static String LINE_DATA_BEGIN_MARKER = "&&& ";
    public static String LINE_DATA_TITLR_BEGIN_MARKER = "PosX\tPosY\t";
    public static int LINE_DATA_BEGIN_INDEX = 4;
    public static String LINE_DATA_FIRST_DELIMETER = " ";
    public static String LINE_DATA_DELIMETER = "\t";
    @Autowired
    public MetalService  metalService;

    @Value("${logdir}")
    public String baseDIR;
    //public String baseDIR="D:\\work\\flawlog";
    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     *
     * @param fileName
     *            文件名
     */
    public  void readFileByLines(String fileName) throws Exception {
        System.out.println("parsing file  " +fileName);
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                if(tempString.startsWith(LINE_DATA_BEGIN_MARKER) && !tempString.contains(LINE_DATA_TITLR_BEGIN_MARKER)){
                    // 匹配成功，将文件名添加到结果集

                    MetalBO met = new MetalBO();
                    int pos = tempString.indexOf(LINE_DATA_FIRST_DELIMETER,0);

                    int iDeli = 0;
                    while (pos != -1) {
                        int pos1 = tempString.indexOf(LINE_DATA_DELIMETER, pos + 1);
                        if(iDeli ==0)
                        {
                            met.setGaugeclass(tempString.substring(pos+1,pos1));
                        }
                        else if(iDeli ==1)
                        {
                            met.setImagette(tempString.substring(pos+1,pos1));
                        }
                        else if(iDeli ==2)
                        {
                            met.setPosx(Double.valueOf(tempString.substring(pos+1,pos1)));
                        }
                        else if(iDeli ==3)
                        {
                            met.setPosy(Double.valueOf(tempString.substring(pos+1,pos1)));
                        }
                        else if(iDeli ==4)
                        {
                            met.setLength(Double.valueOf(tempString.substring(pos+1,pos1)));
                        }
                        else if(iDeli ==5)
                        {
                            met.setWidth(Double.valueOf(tempString.substring(pos+1,pos1)));
                        }
                        else if(iDeli ==6)
                        {
                            met.setArea(Double.valueOf(tempString.substring(pos+1,pos1)));
                        }
                        else if(iDeli ==7)
                        {
                            met.setTime(tempString.substring(pos+1,pos1));
                        }
                        else if(iDeli ==8)
                        {
                            met.setLeftedge(tempString.substring(pos+1,pos1));
                        }
                        else if(iDeli ==9)
                        {
                            met.setRightedge(tempString.substring(pos+1,pos1));
                        }
                        else if(iDeli ==10)
                        {
                            met.setRefpos(tempString.substring(pos+1,tempString.length()-1));
                        }
                        iDeli++;
                        pos = pos1;
                    }
                    System.out.println("[info]inserting  " +met.toString());
                    metalService.insertMetal(met);
                    System.out.println("[info]insert finished  " );
                }
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 通配符匹配
     * @param pattern    通配符模式
     * @param str    待匹配的字符串
     * @return    匹配成功则返回true，否则返回false
     */
    private static boolean wildcardMatch(String pattern, String str) {
        int patternLength = pattern.length();
        int strLength = str.length();
        int strIndex = 0;
        char ch;
        for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {
            ch = pattern.charAt(patternIndex);
            if (ch == '*') {
                //通配符星号*表示可以匹配任意多个字符
                while (strIndex < strLength) {
                    if (wildcardMatch(pattern.substring(patternIndex + 1),
                            str.substring(strIndex))) {
                        return true;
                    }
                    strIndex++;
                }
            } else if (ch == '?') {
                //通配符问号?表示匹配任意一个字符
                strIndex++;
                if (strIndex > strLength) {
                    //表示str中已经没有字符匹配?了。
                    return false;
                }
            } else {
                if ((strIndex >= strLength) || (ch != str.charAt(strIndex))) {
                    return false;
                }
                strIndex++;
            }
        }
        return (strIndex == strLength);
    }

    public void findFiles(String targetFileName, List fileList) {
        File baseDir = new File(baseDIR.trim());       // 创建一个File对象
        if (!baseDir.exists() || !baseDir.isDirectory()) {  // 判断目录是否存在
            System.out.println("[error]find file failed:" + baseDIR + " not exist or it is not a dorectory");
        }
        String tempName = null;
        //判断目录是否存在
        File tempFile;
        File[] files = baseDir.listFiles();
        if(files == null) return;
        for (int i = 0; i < files.length; i++) {
            tempFile = files[i];
            if(tempFile.isDirectory()){
                continue;
            }
            tempName = tempFile.getName();
            if(wildcardMatch(targetFileName, tempName)){
                // 匹配成功，将文件名添加到结果集
                fileList.add(tempFile.getAbsolutePath());
            }

        }
    }
/*
    public static void main(String[] args) {
        String fileName = "C:/temp/newTemp.txt";
        FileRead.readFileByBytes(fileName);
        FileRead.readFileByChars(fileName);
        FileRead.readFileByLines(fileName);
        FileRead.readFileByRandomAccess(fileName);
    }
    */
}
