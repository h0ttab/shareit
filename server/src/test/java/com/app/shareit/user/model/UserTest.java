package com.app.shareit.user.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void testUserEqualsAndHashCode() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("Test User");
        user1.setEmail("test@mail.com");

        User user2 = new User();
        user2.setId(1L);
        user2.setName("Test User");
        user2.setEmail("test@mail.com");

        User user3 = new User();
        user3.setId(2L);

        assertThat(user1).isEqualTo(user2);
        assertThat(user1).isNotEqualTo(user3);

        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
        assertThat(user1.hashCode()).isNotEqualTo(user3.hashCode());

        assertThat(user1.getId()).isEqualTo(1L);
        assertThat(user1.getName()).isEqualTo("Test User");
        assertThat(user1.getEmail()).isEqualTo("test@mail.com");
    }
}