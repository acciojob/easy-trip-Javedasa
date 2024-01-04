package com.driver.controllers;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;

import java.util.*;

public class AirportService {
    private AirportRepository airportRepository = new AirportRepository();

    public void addAirport(Airport airport) {
        airportRepository.addAirport(airport);
    }

    public String getLargestAirportName() {
        List<Airport> airportList = airportRepository.getAllAirports();
        int maxTerminals = Integer.MIN_VALUE;
        String airportName = "";
        for(Airport airport : airportList) {
            if(airport.getNoOfTerminals() > maxTerminals) {
                maxTerminals = airport.getNoOfTerminals();
                airportName = airport.getAirportName();
            } else if(airport.getNoOfTerminals() == maxTerminals && airport.getAirportName().compareTo(airportName) < 0) {
                airportName = airport.getAirportName();
            }
        }
        return airportName;
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity) {
        List<Flight> flightList = airportRepository.getAllFlights();
        double shortestFlightDuration = Double.MAX_VALUE;
        for(Flight flight : flightList) {
            if(flight.getFromCity().equals(fromCity) && flight.getToCity().equals(toCity) && shortestFlightDuration > flight.getDuration()) {
                shortestFlightDuration = flight.getDuration();
            }
        }
        return shortestFlightDuration == Double.MAX_VALUE ? -1 : shortestFlightDuration;
    }

    public void addFlight(Flight flight) {
        airportRepository.addFlight(flight);
    }

    public void addPassenger(Passenger passenger) {
        airportRepository.addPassenger(passenger);
    }

    public String bookATicket(Integer flightId, Integer passengerId) {
        Flight flight = airportRepository.getFlightById(flightId);
        int maxCapacity = flight.getMaxCapacity();
        List<Integer> list= new ArrayList<>();
        if(airportRepository.isValid(flightId)){
            list = airportRepository.getPassengers(flightId);
        }
        int capacity=list.size();
        if(capacity==maxCapacity) return "FAILURE";
        else if(list.contains(passengerId)) return "FAILURE";
        int fare=calculateFare(flightId);
        airportRepository.addPayment(passengerId,fare);
        airportRepository.updateRevenue(flightId, fare);
        list.add(passengerId);

        airportRepository.addflightWithPassenger(flightId, list);
        return "SUCCESS";
    }

    public String cancelATicket(Integer flightId, Integer passengerId) {
        List<Integer> list = airportRepository.getPassengers(flightId);
        if(list.contains(passengerId)){
            list.remove(passengerId);
            int fare = airportRepository.getFareFromPayment(passengerId);
            airportRepository.removePassengerFromPayment(passengerId);
            int revenue = airportRepository.getRevenueFromRevanue(flightId);
            airportRepository.addRevenue(flightId, revenue, fare);
            return "SUCCESS";
        }
        return "FAILURE";
    }

    public int getNumberOfPeopleOn(Date date, String airportName) {
        Airport airport = airportRepository.getAirport(airportName);
        List<Flight> flightList = airportRepository.getAllFlights();
        int count = 0;
        if(airport != null){
            City city = airport.getCity();
            for(Flight flight : flightList){
                if(date.equals(flight.getFlightDate())){
                    if(city.equals(flight.getToCity()) || city.equals(flight.getFromCity())){
                        Integer flightId = flight.getFlightId();
                        List<Integer> list = airportRepository.getPassengers(flightId);
                        if(list != null){
                            count += list.size();
                        }
                    }
                }
            }
        }
        return count;
    }

    public int calculateFare(Integer flightId) {
        int fare = 3000;
        int alreadyBooked = 0;
        List<Integer> flightList = airportRepository.getAllBookedFlights();
        if(flightList.contains(flightId))
            alreadyBooked = airportRepository.getSizeByFlightId(flightId);
        return (fare + (alreadyBooked * 50));
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId) {
        List<Integer> flightList = airportRepository.getAllBookedFlights();
        int count=0;
        for(Integer flightId : flightList){
            List<Integer> list = airportRepository.getPassengers(flightId);
            if(list.contains(passengerId)){
                count++;
            }
        }
        return count;
    }

    public String getAirportName(Integer flightId) {
        List<Integer> flightList = airportRepository.getAllFlightsId();
        if(!flightList.contains((flightId))) return null;
        List<String> airports = airportRepository.getAllAirportsName();
        Flight flight = airportRepository.getFlightById(flightId);
        City city = flight.getFromCity();
        for (String airportName : airports){
            Airport airport = airportRepository.getAirport(airportName);
            if(city.equals(airport.getCity())){
                return airportName;
            }
        }
        return null;
    }

    public int calculateRevenueOfAFlight(Integer flightId) {
        return airportRepository.getRevenueFromRevanue(flightId);
    }
}