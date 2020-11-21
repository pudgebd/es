package pers.pudgebd.java.elasticSearch.bigDataTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AddDataJTest {

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        System.out.println("version: 2");
        //println(firstNames.length) = 71 //println(lastNames.length) = 54 //println(jobs.length) = 44
        int offset = 50000000;

        int num = 1;
        while (num <= 20) {
            int start = offset * (num - 1);
            int end = offset * num;
            if (start == 0) start = 1;
            executorService.submit(new EsJThread(start, end));
            num = num + 1;
        }
    }


}
