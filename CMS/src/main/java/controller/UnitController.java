package controller;

import model.Unit;
import util.FileManager;
import util.SearchSortUtil;

import java.util.ArrayList;

public class UnitController {
    private ArrayList<Unit> units;
    private final String filePath;

    public UnitController() {
        this("data/units.csv");
    }

    public UnitController(String filePath) {
        this.filePath = filePath;
        units = FileManager.loadUnits(filePath);
    }

    public boolean addUnit(Unit unit) {
        for (Unit u : units) {
            if (u.getUnitCode().equalsIgnoreCase(unit.getUnitCode())) return false;
        }
        units.add(unit);
        FileManager.saveUnits(units, filePath);
        return true;
    }

    public ArrayList<Unit> getAllUnits() {
        return units;
    }

    public Unit findByCode(String code) {
        return SearchSortUtil.linearSearchUnit(units, code);
    }

    public ArrayList<Unit> getSortedByName() {
        ArrayList<Unit> copy = new ArrayList<>(units);
        SearchSortUtil.bubbleSortUnits(copy);
        return copy;
    }

    public ArrayList<Unit> searchUnits(String keyword) {
        ArrayList<Unit> result = new ArrayList<>();
        String kw = keyword.toLowerCase();
        for (Unit u : units) {
            if (u.getUnitCode().toLowerCase().contains(kw) || u.getUnitName().toLowerCase().contains(kw)) {
                result.add(u);
            }
        }
        return result;
    }
}
