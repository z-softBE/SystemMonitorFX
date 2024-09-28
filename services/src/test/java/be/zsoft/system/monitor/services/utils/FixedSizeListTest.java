package be.zsoft.system.monitor.services.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FixedSizeListTest {

    @Test
    void add() {
        FixedSizeList<Integer> resultList = new FixedSizeList<>(3, 0);

        resultList.add(1);

        assertThat(resultList.getData()).containsExactly(0,0,1);
    }
}