package com.example.csvparser;

import com.example.reader.Station;
import com.sleepycat.je.utilint.Stat;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CSVParser {
    public List<Station> processInputFile(String inputFile){
        List<Station> inputList = new ArrayList<>();
        try{
            File inputF = new File(inputFile);
            InputStream inputFS = new FileInputStream(inputF);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));

            inputList = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());
            br.close();
        }catch(IOException e){
            System.out.println(e);
        }
        return inputList;
    }

    private static Function<String, Station> mapToItem = (line) -> {
        String[] p = line.split(",");
        Station station = new Station();

        station.setId(p[0]);
        station.setName(p[1]);
        station.setMesonet(p[2]);

        return station;
    };
}
