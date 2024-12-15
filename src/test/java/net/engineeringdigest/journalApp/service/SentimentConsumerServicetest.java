package net.engineeringdigest.journalApp.service;
import net.engineeringdigest.journalApp.model.SentimentData;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
public class SentimentConsumerServicetest {
    @Autowired
    private SentimentConsumerService sentimentConsumerService;

    @MockBean
    private EmailService emailService;

    @Test
    public void testConsume() {
        // Arrange
        SentimentData mockData = new SentimentData();
        mockData.setEmail("test@example.com");
        mockData.setSentiment("Positive");

        // Act
        sentimentConsumerService.consume(mockData);

        // Assert
        Mockito.verify(emailService, Mockito.times(1))
                .sendEmail("test@example.com", "Sentiment for previous week", "Positive");
    }
}
