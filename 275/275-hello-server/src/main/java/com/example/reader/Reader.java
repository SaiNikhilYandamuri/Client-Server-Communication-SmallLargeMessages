package com.example.reader;

import java.io.File;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileFilter;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Reader {

    public Reader(){

    }

    public final void readFile(String[] args){

//        File dataSource = new File("C:\\gash\\datasets\\mesonet\\9-days\\20120201\\20120201_0100\\20120201_0100");
//        File catSource = new File("C:\\275_lab1\\CMPE275Lab1\\275\\275-hello-server\\catalog.csv");
//        File outputSource = new File("C:\\275_lab1\\CMPE275Lab1\\275\\275-hello-server\\output.txt");
        File dataSource = null;
        File catSource = null;
        if(args[0].equals("8080")) {
            dataSource = new File("C:\\Users\\nikhi\\OneDrive\\Desktop\\Fall'21\\275\\lab-g01\\data\\0201\\20120201_0100\\20120201_0100");
            catSource = new File("C:\\Users\\nikhi\\OneDrive\\Desktop\\Fall'21\\275\\lab-g01\\reader\\catalog.csv");
        }else if(args[0].equals("8081")){
            dataSource = new File("C:\\Users\\nikhi\\OneDrive\\Desktop\\Fall'21\\275\\lab-g01\\data-1\\0201\\20120201_0100\\20120201_0100");
            catSource = new File("C:\\Users\\nikhi\\OneDrive\\Desktop\\Fall'21\\275\\lab-g01\\reader\\catalog-1.csv");
        }else if(args[0].equals("8082")){
            dataSource = new File("C:\\Users\\nikhi\\OneDrive\\Desktop\\Fall'21\\275\\lab-g01\\data-2\\0201\\20120201_0100\\20120201_0100");
            catSource = new File("C:\\Users\\nikhi\\OneDrive\\Desktop\\Fall'21\\275\\lab-g01\\reader\\catalog-2.csv");
        }
        File outputSource = new File("C:\\Users\\nikhi\\OneDrive\\Desktop\\Fall'21\\275\\lab-g01\\output.txt");
        Date startDate = null;
        Date endDate = null;
        Rectangle region = null;
        Set<String> stationIDs = new HashSet<>();
        stationIDs.add("C7959");
        stationIDs.add("MEGT2");
        long startTime = System.currentTimeMillis();
        try {
            Catalog cat = new Catalog();
            if (!cat.load(catSource)) {
                System.out.println("-- new catalog file created");
            }

            MesonetReader rawReader = new MesonetReader();

            /**
             * note use readFile() to perform the same steps
             */

            if (!dataSource.exists())
                return;
            if (dataSource.isFile()) {
                List<Station> stations = rawReader.extractCatalog(dataSource);
                if (stations != null) {
                    for (Station s : stations)
                        cat.addStation(s);
                }

                List<MesonetData> data = rawReader.extract(dataSource, startDate , endDate, region, stationIDs);
                System.out.println("processed " + data.size() + " entries");

                // now do something with the data
                // 1. send to the cluster or cloud

				for (MesonetData d : data) {
					// TODO do something other than print!
					System.out.println("Obs: " + d.getStationID() + " T = " + d.getTemperature() + ", WS = "
							+ d.getWindSpeed() + ", WD = " + d.getWindDir() + ", RH = " + d.getRelHumidity());
				}
            } else {
                FileFilter filter = new FileFilter() {
                    public boolean accept(File pathname) {
                        System.out.println(pathname.toString());
                        return (pathname.isFile() && !pathname.getName().startsWith(".")
                                && !pathname.getName().endsWith(".gz"));
                    }
                };

                // TODO walk through accepted files and process
                System.out.println("TODO: process files");

            }

            // save catalog
            cat.save(catSource);

            long stopTime = System.currentTimeMillis();
            System.out.println(
                    "MADIS Mesonet - total processing time is " + ((stopTime - startTime) / 1000.0) + " seconds");
        } catch (Throwable t) {
            System.out.println(
                    "Unable to process mesowest data in " + dataSource.getAbsolutePath() + ": " + t.getMessage());
        }
    }
}
