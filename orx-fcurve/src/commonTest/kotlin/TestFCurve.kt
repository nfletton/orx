import org.openrndr.extra.fcurve.fcurve
import kotlin.test.Test
import kotlin.test.assertEquals

class TestFCurve {
    @Test
    fun testConstantExpression() {
        val text = "10.5"
        val fc = fcurve(text)
        assertEquals(10.5, fc.value(0.0))
        assertEquals(10.5, fc.value(1.0))
        assertEquals(10.5, fc.value(-1.0))

        val normalizedSampler = fc.sampler(true)
        assertEquals(10.5, normalizedSampler(0.0))
        assertEquals(10.5, normalizedSampler(1.0))
        assertEquals(10.5, normalizedSampler(-1.0))
    }
}