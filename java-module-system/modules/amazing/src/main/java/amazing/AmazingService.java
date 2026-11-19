package amazing;

import amazing.impl.DefaultAmazingService;

public interface AmazingService {
    AmazingService DEFAULT = new DefaultAmazingService();

    void compute();
}
