package com.library.dao;

import com.library.model.Staff;
import java.util.List;

public interface IStaffDAO {
    boolean addStaff(Staff staff);
    boolean removeStaff(int staffId);
    Staff getStaffById(int id);
    List<Staff> getAllStaff();
    boolean updateStaff(Staff staff);
}






