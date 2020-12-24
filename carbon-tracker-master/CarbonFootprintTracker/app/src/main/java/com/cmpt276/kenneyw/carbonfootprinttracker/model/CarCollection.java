package com.cmpt276.kenneyw.carbonfootprinttracker.model;


import java.util.ArrayList;
import java.util.List;

/**
 * DESCRIPTION:
 *  - Create a list of car and functions to access the cars in the list
 *
 *  This class contains all the functions necessary to manage collections of cars.
 */

public class CarCollection extends Car {
    private List<Car> cars = new ArrayList<>();

    public List<Car> returnCarArrays(){
        return cars;
    }
    public void addCar(Car car){
        cars.add(car);
    }

    public void changeCar(Car car, int indexOfCarToEdit) {
        cars.get(indexOfCarToEdit).setName(car.getName());
        cars.get(indexOfCarToEdit).setMake(car.getMake());
        cars.get(indexOfCarToEdit).setModel(car.getModel());
        cars.get(indexOfCarToEdit).setTransmission(car.getTransmission());
        cars.get(indexOfCarToEdit).setYear(car.getYear());
        cars.get(indexOfCarToEdit).setLiterEngine(car.getLiterEngine());
        cars.get(indexOfCarToEdit).setHighwayEmissions(car.getHighwayEmissions());
        cars.get(indexOfCarToEdit).setGasType(car.getGasType());
        cars.get(indexOfCarToEdit).setCityEmissions(car.getCityEmissions());
        cars.get(indexOfCarToEdit).setIconID(car.getIconID());
    }

    public void deleteCar(int indexToDelete){
        validateIndexWithException(indexToDelete);
        cars.remove(indexToDelete);
    }

    public int countCars() {
        return cars.size();
    }

    public Car getCar(int index) {
        validateIndexWithException(index);
        return cars.get(index);
    }

    public String[] getCarsDescriptions() {
        String[] descriptions = new String[countCars()];
        for (int i = 0; i < countCars(); i++) {
            Car car = getCar(i);
            descriptions[i] = car.getMake() + " - " +
                    car.getModel() + " - " +
                    car.getYear() + " - " +
                    car.getTransmission() + " - " +
                    car.getLiterEngine() + "L";
        }
        return descriptions;
    }

    public String[] getCarsNames() {
        String[] descriptions = new String[countCars()];
        for (int i = 0; i < countCars(); i++) {
            Car car = getCar(i);
            descriptions[i] = car.getName();
        }
        return descriptions;
    }

    public String[] getCarsDescriptionsWithName() {
        String[] descriptions = new String[countCars()];
        for (int i = 0; i < countCars(); i++) {
            Car car = getCar(i);
            descriptions[i] = car.getName() + " - " +
                    car.getMake() + " - " +
                    car.getModel() + " - " +
                    car.getYear() + " - " +
                    car.getTransmission() + " - " +
                    car.getLiterEngine() + "L";
        }
        return descriptions;
    }

    public int[] getIconIDs(){
        int[] IDs = new int[countCars()];
        for (int i =0; i < countCars(); i++){
            Car car = getCar(i);
            IDs[i] = car.getIconID();
        }
        return IDs;
    }
    private void validateIndexWithException(int index) {
        if (index < 0 || index >= countCars()) {
            throw new IllegalArgumentException();
        }
    }

}
