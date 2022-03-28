package project.investmentservice.domain;

import org.junit.Test;

import java.util.HashMap;

public class ChannelHashMapTest {
    @Test
    public void 해쉬맵사용Test() {
        // given
        HashMap<Long, Double> mp = new HashMap<>();


        // when
        mp.put(1L, 33.33);
        Double aDouble = mp.get(1L);

        // then
        System.out.println("aDouble = " + aDouble);
        System.out.println("mp.size() = " + mp.size());
    }
}
