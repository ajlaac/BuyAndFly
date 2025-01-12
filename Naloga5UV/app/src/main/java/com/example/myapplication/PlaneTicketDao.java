package com.example.myapplication;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;
import java.util.List;


// DAO: PlaneTicketDao
@Dao
public interface PlaneTicketDao {
    @Insert
    void insert(PlaneTicket ticket);

    @Update
    void update(PlaneTicket ticket);

    @Delete
    void delete(PlaneTicket ticket);

    @Query("SELECT * FROM plane_tickets WHERE id = :id")
    PlaneTicket getTicketById(int id);

    @Query("SELECT * FROM plane_tickets")
    List<PlaneTicket> getAllTickets();

    @Query("SELECT * FROM plane_tickets WHERE departureDate = :departureDate")
    List<PlaneTicket> getTicketsByDepartureDate(String departureDate);
}
