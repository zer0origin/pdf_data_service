package com.w.callum.pdf_service_data.model;

import java.util.ArrayList;
import java.util.List;

public record Selection(String selectionUUID, String documentUUID, Coordinate coordinates, String pageKey) { //FIXME: Why is selectionUUID & documentUUID a string???
    public static Coordinate[] CoordinatesToSelection(Selection... selections){
        Coordinate[] arr = new Coordinate[selections.length];

        for (int x = 0; x < selections.length; x++){
            arr[x] = selections[x].coordinates;
        }

        return arr;
    }

    public static List<Coordinate> CoordinatesToSelection(List<Selection> selections){
        List<Coordinate> arr = new ArrayList<>(selections.size());

        for (Selection selection : selections) {
            arr.add(selection.coordinates);
        }

        return arr;
    }
}
