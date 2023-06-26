package sample.cafekiosk.unit.beverage;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AmericanoTest {

    @Test
    void getName() {
        // given
        Americano americano = new Americano();

        // when
        String name = americano.getName();

        // then
        assertThat(americano.getName()).isEqualTo("아메리카노");
    }

    @Test
    void getPrice() {
        // given
        Americano americano = new Americano();

        // when
        int price = americano.getPrice();

        // then
        assertThat(americano.getPrice()).isEqualTo(4000);
    }
}