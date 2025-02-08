package com.orion.mdd_api.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.orion.mdd_api.dtos.TopicDto;
import com.orion.mdd_api.models.Topic;

@SpringBootTest
@ActiveProfiles("test")
class TopicMapperTest {

    @Autowired
    private TopicMapper topicMapper;

    @Test
    void shouldMapTopicListToTopicDtoList() {

        assertNotNull(topicMapper);

        Topic topic1 = new Topic();
        topic1.setId(1L);
        topic1.setTitle("Java");
        topic1.setDescription("Java is a high-level, class-based, object-oriented programming language.");

        Topic topic2 = new Topic();
        topic2.setId(2L);
        topic2.setTitle("Spring");
        topic2.setDescription("Spring is a powerful, feature-rich framework for building Java applications.");

        List<Topic> topics = List.of(topic1, topic2);
        List<TopicDto> topicDtos = topicMapper.toDtoList(topics);

        assertEquals(2, topicDtos.size());
        assertEquals(1L, topicDtos.get(0).getId());
        assertEquals("Java", topicDtos.get(0).getTitle());
        assertEquals("Java is a high-level, class-based, object-oriented programming language.",
                topicDtos.get(0).getDescription());
        assertEquals(2L, topicDtos.get(1).getId());
        assertEquals("Spring", topicDtos.get(1).getTitle());
        assertEquals("Spring is a powerful, feature-rich framework for building Java applications.",
                topicDtos.get(1).getDescription());
    }

}
