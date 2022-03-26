package project.investmentservice.controller;

import org.junit.Test;

import java.util.*;


public class GameStartTimerTest {

    @Test
    public void TimerTest() {
        // given
        HashSet<Long> companyIds = new HashSet<>();
        companyIds.add(1L);
        companyIds.add(2L);

        List<List<Double>> stockLists = new ArrayList<>();
        for(Long cid: companyIds){
            List<Double> tempArray = new ArrayList<>();
            for(int i = 0; i < 10; i++){
                tempArray.add(1.1 + i);
            }
            stockLists.add(tempArray);
        }

        Map<Long, Double> closeValue = new HashMap<>();
        

        // when
        
        // 타이머 종료를 위한 lock 객체 설정
        Object lock = new Object();
        // 10초에 한 번씩 주가 정보를 전송한다.
        // 주요 게임 로직을 담당한다.
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            int idx = 0;
            @Override
            public void run() {
                if(idx < 10) {
                    int k = 0;
                    Iterator<Long> it = companyIds.iterator();
                    while (it.hasNext()) {
                        System.out.println("it.next() = " + it.next());
                        System.out.println("k = " + k);
                        Double aDouble = stockLists.get(k++).get(idx);
                        System.out.println("aDouble = " + aDouble);
                        closeValue.put(1L, aDouble);
                    }
                    idx++;
                }
                else {
                    // 타이머가 종료되면 lock에 시그널을 보낸다.
                    synchronized (lock) {
                        lock.notifyAll();
                    }
                    timer.cancel();
                }
            }
        };
        timer.schedule(timerTask, 0, 1000);

//         타이머가 종료된 이후 다음 로직을 수행해야 하므로
//         lock이 시그널을 받을 때까지 기다린다.
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException ex) {
            }
        }

        
        // then
        
        System.out.println("성공");
    }
    
}
