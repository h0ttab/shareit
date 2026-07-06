package com.app.shareit.item.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommentTest {

    @Test
    void testCommentEqualsAndHashCode() {
        Comment comment1 = new Comment();
        comment1.setId(1L);

        Comment comment2 = new Comment();
        comment2.setId(1L);

        Comment comment3 = new Comment();
        comment3.setId(2L);

        assertThat(comment1).isEqualTo(comment2);
        assertThat(comment1).isNotEqualTo(comment3);
        assertThat(comment1.hashCode()).isEqualTo(comment2.hashCode());
    }
}