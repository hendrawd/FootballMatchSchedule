package ganteng.hendrawd.footballmatchschedule.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * @author hendrawd on 08/08/18
 */
class Util {
    companion object {

        fun toMillis(strDate: String, strTime: String): Long {
            // date will be like 2018-08-10
            // time will be like 19:00:00+00:00
            // since the time contains timezone,
            // so it will be converted to local time directly when added to the device calendar
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ssXXX", Locale.US)
            val date = simpleDateFormat.parse("$strDate $strTime")
            val calendar = Calendar.getInstance()
            calendar.time = date
            return calendar.timeInMillis
        }
    }
}