package com.example.myapplication;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.AppDatabase;
import com.example.myapplication.User;
import com.example.myapplication.PlaneTicket;
import com.example.myapplication.UserDao;
import com.example.myapplication.PlaneTicketDao;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class AppViewModel extends AndroidViewModel {
    private final UserDao userDao;
    private final PlaneTicketDao planeTicketDao;
    private final Executor executor;

    public AppViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        userDao = db.userDao();
        planeTicketDao = db.planeTicketDao();
        executor = Executors.newSingleThreadExecutor();
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
}

