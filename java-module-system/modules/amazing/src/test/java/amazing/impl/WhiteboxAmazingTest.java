package amazing.impl;

import amazing.AmazingService;
import org.junit.jupiter.api.Test;

public class WhiteboxAmazingTest {

    @Test
    void testService() {
        AmazingService.DEFAULT.compute();
    }

    @Test
    void testServiceInternals() {
        ((DefaultAmazingService) AmazingService.DEFAULT).internalCompute(null);
    }
}
