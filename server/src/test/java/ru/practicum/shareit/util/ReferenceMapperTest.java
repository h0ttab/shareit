package ru.practicum.shareit.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.model.User;

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