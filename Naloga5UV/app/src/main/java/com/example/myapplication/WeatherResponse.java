package com.example.myapplication;

public class WeatherResponse {
    public Main main;
    public Weather[] weather;

    public static class Main {
        public double temp; // Temperature in Kelvin
    }

    public static class Weather {
        public String description; // Weather description
    }
}
