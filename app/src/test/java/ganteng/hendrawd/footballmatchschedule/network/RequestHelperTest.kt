package ganteng.hendrawd.footballmatchschedule.network

import junit.framework.Assert.assertEquals
import org.junit.Test

/**
 * Test the RequestHelper class
 * @author hendrawd on 01/08/18
 */
class RequestHelperTest {

    /**
     * Test RequestHelper.getDataAsString function by hitting an EndPoint,
     * getting the result as String, and check if the result starts with
     * the expected prefix
     */
    @Test
    fun testGetDataAsString() {
        val result = RequestHelper.getDataAsString("https://hendrawd.github.io")
        val prefix = "<!DOCTYPE html>"
        val expected = result!!.substring(0, prefix.length)
        assertEquals(expected, prefix)
    }
}