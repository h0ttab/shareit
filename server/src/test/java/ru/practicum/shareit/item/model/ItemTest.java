package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ItemTest {

    @Test
    void testItemEqualsAndHashCode() {
        Item item1 = new Item();
        item1.setId(1L);

        Item item2 = new Item();
        item2.setId(1L);

        Item item3 = new Item();
        item3.setId(2L);

        assertThat(item1).isEqualTo(item2);
        assertThat(item1).isNotEqualTo(item3);
        assertThat(item1.hashCode()).isEqualTo(item2.hashCode());
    }
}