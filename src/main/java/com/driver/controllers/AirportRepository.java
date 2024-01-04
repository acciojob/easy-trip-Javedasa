package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;

import java.util.*;

public class AirportRepository {

    private Map<String, Airport> airports = new HashMap<>();
    private Map<Integer, Flight> flights = new HashMap<>();
    private Map<Integer, Passenger> passengers = new HashMap<>();
    private Map<Integer, List<Integer>> flightWithPassengerData = new HashMap<>();
    private Map<Integer,Integer> revenueMap= new HashMap<>();
    private Map<Integer,Integer> paymentMap= new HashMap<>();

    public void addAirport(Airport airport) {
        airports.put(airport.getAirportName(), airport);
    }

    public List<Airport> getAllAirports() {
        return new ArrayList<>(airports.values());
    }

    public void addFlight(Flight flight) {
        flights.put(flight.getFlightId(), flight);
    }

    public void addPassenger(Passenger passenger) {
        passengers.put(passenger.getPassengerId(), passenger);
    }

    public List<Flight> getAllFlights() {
        return new ArrayList<>(flights.values());
    }

    public Flight getFlightById(Integer flightId) {
        return flights.get(flightId);
    }

    public boolean isValid(Integer flightId) {
        return flightWithPassengerData.containsKey(flightId);
    }

    public List<Integer> getPassengers(Integer flightId) {
        return flightWithPassengerData.get(flightId);
    }

    public void addPayment(Integer passengerId, int fare) {
        paymentMap.put(passengerId,fare);
    }

    public void updateRevenue(Integer flightId, Integer fare) {
        fare += revenueMap.getOrDefault(flightId,0);
        revenueMap.put(flightId,fare);
    }

    public void addflightWithPassenger(Integer flightId, List<Integer> list) {
        flightWithPassengerData.put(flightId,list);
    }


    public int getFareFromPayment(Integer passengerId) {
        return paymentMap.getOrDefault(passengerId,0);
    }

    public void removePassengerFromPayment(Integer passengerId) {
        paymentMap.remove(passengerId);
    }

    public int getRevenueFromRevanue(Integer flightId) {
        return revenueMap.getOrDefault(flightId,0);
    }

    public void addRevenue(Integer flightId, int revenue, int fare) {
        revenueMap.put(flightId, revenue-fare);
    }

    public Airport getAirport(String airportName) {
        return airports.get(airportName);
    }

    public List<Integer> getAllBookedFlights() {
        return new ArrayList<>(flightWithPassengerData.keySet());
    }

    public int getSizeByFlightId(Integer flightId) {
        return flightWithPassengerData.get(flightId).size();
    }

    public List<String> getAllAirportsName() {
        return new ArrayList<>(airports.keySet());
    }

    public List<Integer> getAllFlightsId() {
        return new ArrayList<>(flights.keySet());
    }
}
