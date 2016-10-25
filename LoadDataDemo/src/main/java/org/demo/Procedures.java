package org.demo;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.PerformsWrites;
import org.neo4j.procedure.Procedure;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * Hello world!
 *
 */

public class Procedures
{

    @Context
    public GraphDatabaseService db;

    @Procedure("load.demodata")
    @PerformsWrites
    public void loadDemoData() {

        List<Node> persons = createRandomNodeList(1000, () -> randomPerson());
        List<Node> cars = createRandomNodeList(200, () -> randomCar());
        List<Node> stations = createRandomNodeList(100, () -> randomStation());

    }

    private List<Node> createRandomNodeList(int nrOfEntities, Supplier<Node> randomizer) {
        List<Node> list = new ArrayList<>();
        IntStream.range(0, nrOfEntities).forEach(i -> list.add(randomizer.get()));
        return list;
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

}
