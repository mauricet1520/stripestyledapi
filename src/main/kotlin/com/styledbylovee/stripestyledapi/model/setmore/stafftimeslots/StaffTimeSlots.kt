package com.styledbylovee.stripestyledapi.model.setmore.stafftimeslots

import com.styledbylovee.stripestyledapi.model.setmore.staff.Staff

data class StaffTimeSlots(val staff: Staff,
                          val slots: List<String>,
                          var sevenToNineTimeSlotsAvailable: Boolean = false,
                          var eightToTenTimeSlotsAvailable: Boolean = false,
                          var nineToElevenTimeSlotsAvailable: Boolean= false,
                          var tenToTwelveTimeSlotsAvailable: Boolean= false,
                          var elevenToThirteenTimeSlotsAvailable: Boolean= false,
                          var twelveToFourteenTimeSlotsAvailable: Boolean= false,
                          var thirteenToFifteenTimeSlotsAvailable: Boolean= false,
                          var fourteenToSixteenTimeSlotsAvailable: Boolean= false
)