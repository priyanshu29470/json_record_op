package com.assignment.json_operator;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JsonOperatorApplicationTests {

	@Test
    void mainMethodTest() {
        try (MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {
            JsonOperatorApplication.main(new String[]{});
            mocked.verify(() -> SpringApplication.run(JsonOperatorApplication.class, new String[]{}));
        }
    }

}
