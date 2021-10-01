package my.app.module2

import com.fasterxml.jackson.databind.ObjectMapper;
import my.app.module1.Module1

class Module2 {

    fun use(m1: Module1) {
        m1.use()
        println("Module2! (Jackson version ${ObjectMapper().version()})")
    }
}
