package org.demo;

import org.apache.commons.lang3.RandomUtils;
import org.neo4j.graphdb.*;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.PerformsWrites;
import org.neo4j.procedure.Procedure;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */

public class Procedures
{

    @Context
    public GraphDatabaseService db;

    @Procedure
    @PerformsWrites
    public Stream loaddata() {

        List<Node> persons = createRandomList(1000, () -> randomPerson());
        List<Node> cars = createRandomList(200, () -> randomCar());
        List<Node> stations = createRandomList(100, () -> randomStation());

        connectRandomCloseStations(80, stations);

        List<Node> trips = createRandomTripList(10000, persons, cars, stations);

        return Stream.of("OK");
    }

    private <T> List<T> createRandomList(int nrOfEntities, Supplier<T> randomizer) {
        List<T> list = new ArrayList<>(nrOfEntities);
        IntStream.range(0, nrOfEntities).forEach(i -> list.add(randomizer.get()));
        return list;
    }

    private List<Node> createRandomTripList(int nrOfEntities, List<Node> persons, List<Node> cars, List<Node> stations) {
        List<Node> list = new ArrayList<>(nrOfEntities);
        IntStream.range(0, nrOfEntities).forEach(i -> {
            list.add(randomTrip(persons, cars, stations));
        });
        return list;
    }

    private <T> T getRandom(List<T> list) {
        if(list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List must contain entities");
        }
        return list.get(RandomUtils.nextInt(0, list.size()));
    }


    private void connectRandomCloseStations(int connections, List<Node> stations) {
        RelationshipType relType = RelationshipType.withName("CLOSE_STATION");
        IntStream.range(0, connections).forEach(i -> {
            Node stationA;
            Node stationB;
            do {
                stationA = getRandom(stations);
                stationB = getRandom(stations);
            } while(checkRelationshipBetweenNodes(stationA, stationB, relType));

            Relationship relation = stationA.createRelationshipTo(stationB, relType);
            relation.setProperty("distance", StationRandomizer.randomDistance());
        });
    }


    private Node randomTrip(List<Node> persons, List<Node> cars, List<Node> stations) {
        Node trip = db.createNode(Label.label("Trip"));
        trip.setProperty("uid", UUID.randomUUID());
        trip.setProperty("distance", TripRandomizer.randomDistance());

        Node person = getRandom(persons);
        Node car = getRandom(cars);
        Node fromStation = getRandom(stations);
        Node toStation = getRandom(stations);

        trip.createRelationshipTo(person, RelationshipType.withName("DRIVER"));
        trip.createRelationshipTo(car, RelationshipType.withName("CAR"));
        trip.createRelationshipTo(fromStation, RelationshipType.withName("FROM"));
        trip.createRelationshipTo(toStation, RelationshipType.withName("TO"));

        return trip;
    }

    private Node randomPerson() {
        Node person = db.createNode(Label.label("Person"));
        person.setProperty("uid", UUID.randomUUID());
        person.setProperty("name", NameRandomizer.randomName());
        return person;
    }


    private Node randomCar() {
        Label carLabel = Label.label("Car");
        Node car = db.createNode(carLabel);
        car.setProperty("reg_nr", getUnique(carLabel, "reg_nr", () -> CarRandomizer.randomRegNr()));
        car.setProperty("model", CarRandomizer.randomRegNr());
        return car;
    }

    private Node randomStation() {
        Label stationLabel = Label.label("Station");
        Node station = db.createNode();
        station.setProperty("code", getUnique(stationLabel, "code", () -> StationRandomizer.randomCode()));
        return station;
    }

    private Object getUnique(Label label, String propertyName, Supplier<Object> propertyRandomizer) {
        Object propertyValue;
        do {
            propertyValue = propertyRandomizer.get();
        } while (checkIfPropertyAlreadyExist(label, propertyName, propertyValue));

        return propertyValue;
    }

    private boolean checkIfPropertyAlreadyExist(Label label, String propertyName, Object propertyValue) {
        return db.findNode(label, propertyName, propertyValue) != null;
    }

    private boolean checkRelationshipBetweenNodes(Node stationA, Node stationB, RelationshipType rel) {
        for(Relationship r : stationA.getRelationships(rel)) {
            if (r.getOtherNode(stationA).equals(stationB)) {
                return true;
            }
        }

        return false;
    }
}
