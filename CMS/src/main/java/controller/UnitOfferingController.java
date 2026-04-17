package controller;

import model.UnitOffering;
import util.FileManager;

import java.util.ArrayList;

public class UnitOfferingController {
    private ArrayList<UnitOffering> offerings;
    private final String filePath;

    public UnitOfferingController() {
        this("data/offerings.csv");
    }

    public UnitOfferingController(String filePath) {
        this.filePath = filePath;
        offerings = FileManager.loadOfferings(filePath);
    }

    public boolean addOffering(UnitOffering offering) {
        for (UnitOffering o : offerings) {
            if (o.getOfferingId().equalsIgnoreCase(offering.getOfferingId())) return false;
        }
        offerings.add(offering);
        FileManager.saveOfferings(offerings, filePath);
        return true;
    }

    public ArrayList<UnitOffering> getAllOfferings() {
        return offerings;
    }

    public UnitOffering findById(String id) {
        for (UnitOffering o : offerings) {
            if (o.getOfferingId().equalsIgnoreCase(id)) return o;
        }
        return null;
    }

    public void updateOffering(UnitOffering updated) {
        for (int i = 0; i < offerings.size(); i++) {
            if (offerings.get(i).getOfferingId().equalsIgnoreCase(updated.getOfferingId())) {
                offerings.set(i, updated);
                break;
            }
        }
        FileManager.saveOfferings(offerings, filePath);
    }

    public ArrayList<UnitOffering> getOfferingsByUnit(String unitCode) {
        ArrayList<UnitOffering> result = new ArrayList<>();
        for (UnitOffering o : offerings) {
            if (o.getUnitCode().equalsIgnoreCase(unitCode)) result.add(o);
        }
        return result;
    }
}
