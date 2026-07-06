package com.app.shareit.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.app.shareit.user.model.User;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class ReferenceMapperTest {

    @InjectMocks
    private ReferenceMapper referenceMapper;

    @Test
    void map_whenIdIsNull_thenReturnNull() {
        assertNull(referenceMapper.map(null, User.class));
    }
}