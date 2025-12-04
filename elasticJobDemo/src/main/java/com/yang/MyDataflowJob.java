package com.yang;


import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class MyDataflowJob implements DataflowJob {
    @Override
    public List<String> fetchData(ShardingContext context) {
        List<String> foos = new ArrayList<>();


        while (true) {
            double random = Math.random();
            if (random > 0.3) {
                foos.add("处理线程："+Thread.currentThread().getName()+" random：" + random + " 分片：" + context.getShardingItem());
            }else {
                break;
            }
        }
        if (foos.size() ==0) {
            System.out.println("处理完毕！");
        }else{
            System.out.println("完成一次fetch");
        }
        return foos;
    }

    @Override
    public void processData(ShardingContext context, List list) {
        System.out.println(
                String.format("分片项 ShardingItem: %s | 运行时间: %s | 线程ID: %s | 分片参数: %s ",
                        context.getShardingItem(),
                        new SimpleDateFormat("HH:mm:ss").format(new Date()),
                        Thread.currentThread().getId(),
                        context.getShardingParameter())
        );
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }
}
