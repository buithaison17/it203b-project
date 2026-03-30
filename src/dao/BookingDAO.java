package dao;

import models.dto.BookingDTO;
import models.dto.ComputerDTO;
import models.entity.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingDAO {
    public List<ComputerDTO> getListComputerCanBook(int currentPage, LocalDateTime startTime, LocalDateTime endTime);

    public List<BookingDTO> viewListBooking(int currentPage);

    public boolean bookingComputer(int computerId, LocalDateTime startTime, LocalDateTime endTime, double totalPrice);

    public int getTotalPage();
}
