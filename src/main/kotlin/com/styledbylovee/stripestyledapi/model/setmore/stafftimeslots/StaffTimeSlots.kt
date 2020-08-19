package com.styledbylovee.stripestyledapi.model.setmore.stafftimeslots

import com.styledbylovee.stripestyledapi.model.setmore.staff.Staff

data class StaffTimeSlots(val staff: Staff, val slots: List<String>)