package com.i2s.flawlog;

/**
 * Created by Administrator on 2017/11/27.
 */
import com.i2s.flawlog.service.MetalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduleTask {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleTask.class);

    @Autowired
    FileRead reader;

    @Scheduled(cron="0/1 * * * * ?")
    public void parseFile() {

        //    找扩展名为txt的文件
        String fileName = "*.log";
        List<String> resultList = new ArrayList<String>();
        reader.findFiles(fileName,resultList);
        if (resultList.size() == 0) {
            return;
        }
        for (int i = 0; i < resultList.size(); i++) {
            try {
                reader.readFileByLines(resultList.get(i));
            }
            catch (Exception ex){
                System.out.println("store data exception...\n");//显示查找结果。
            }

            File file = new File(resultList.get(i));
            file.renameTo(new File(resultList.get(i)+"_bak"));
        }

    }
}
