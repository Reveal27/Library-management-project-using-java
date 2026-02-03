package com.library.dao;

import java.util.List;

import com.library.model.BorrowRequest;
import com.library.model.RequestStatus;

public interface IBorrowRequestDAO {
    boolean createBorrowRequest(BorrowRequest request);

    BorrowRequest getBorrowRequestById(int id);

    List<BorrowRequest> getActiveRequests();

    List<BorrowRequest> getRequestsByStudentId(int studentId);

    boolean updateRequestStatus(int requestId, RequestStatus status);

    boolean approveRequest(int requestId);

    boolean returnBook(int requestId);

    List<BorrowRequest> getRequestsByBookId(int bookId);

    boolean deleteRequestsByBookId(int bookId);

    void checkOverdueRequests();
}
