package com.example.myapplication;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.app.Application;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppViewModel extends AndroidViewModel {
    private final UserDao userDao;
    private final PlaneTicketDao planeTicketDao;
    private final Executor executor;
    private final MutableLiveData<PlaneTicket> currentTicket;

    public AppViewModel(Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        userDao = db.userDao();
        planeTicketDao = db.planeTicketDao();
        executor = Executors.newSingleThreadExecutor();
        currentTicket = new MutableLiveData<>();
    }

    // User operations
    public void insertUser(User user) {
        executor.execute(() -> userDao.insert(user));
    }

    public LiveData<User> getUserByEmail(String email) {
        MutableLiveData<User> userLiveData = new MutableLiveData<>();
        executor.execute(() -> userLiveData.postValue(userDao.getUserByEmail(email)));
        return userLiveData;
    }

    // PlaneTicket operations
    public void insertTicket(PlaneTicket ticket) {
        executor.execute(() -> planeTicketDao.insert(ticket));
    }

    public LiveData<List<PlaneTicket>> getTicketsByDepartureDate(String date) {
        MutableLiveData<List<PlaneTicket>> ticketsLiveData = new MutableLiveData<>();
        executor.execute(() -> ticketsLiveData.postValue(planeTicketDao.getTicketsByDepartureDate(date)));
        return ticketsLiveData;
    }

    public LiveData<List<PlaneTicket>> getAllTickets() {
        MutableLiveData<List<PlaneTicket>> ticketsLiveData = new MutableLiveData<>();
        executor.execute(() -> {
            List<PlaneTicket> tickets = planeTicketDao.getAllTickets();
            ticketsLiveData.postValue(tickets);
        });
        return ticketsLiveData;
    }

    public LiveData<List<PlaneTicket>> getUserFutureTickets(String email, String currentDate) {
        MutableLiveData<List<PlaneTicket>> ticketsLiveData = new MutableLiveData<>();
        executor.execute(() -> ticketsLiveData.postValue(planeTicketDao.getUserFutureTickets(email, currentDate)));
        return ticketsLiveData;
    }

    public LiveData<PlaneTicket> getTicketById(int ticketId) {
        MutableLiveData<PlaneTicket> ticketLiveData = new MutableLiveData<>();
        executor.execute(() -> ticketLiveData.postValue(planeTicketDao.getTicketById(ticketId)));
        return ticketLiveData;
    }

    public void updateTicket(PlaneTicket ticket) {
        executor.execute(() -> planeTicketDao.update(ticket));
    }

    public void deleteTicket(PlaneTicket ticket) {
        executor.execute(() -> planeTicketDao.delete(ticket));
    }

    // Current ticket management
    public LiveData<PlaneTicket> getCurrentTicket() {
        return currentTicket;
    }

    public void setCurrentTicket(PlaneTicket ticket) {
        currentTicket.setValue(ticket);
    }
}
