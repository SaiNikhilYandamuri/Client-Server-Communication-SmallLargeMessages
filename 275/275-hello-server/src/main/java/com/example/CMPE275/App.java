package com.example.CMPE275;

import com.example.grpc.HelloServiceI;
import com.example.reader.Station;
import io.grpc.*;
import com.example.reader.Reader;
import com.example.csvparser.CSVParser;

import java.util.List;

/**
 * Hello world!
 *
 */

public class App 
{
    public static void main( String[] args ) throws Exception
    {
        // Create a new server to listen on port 8080
        Server server = ServerBuilder.forPort(8080)
                .addService(new HelloServiceI())
                .build();

        // Start the server
        server.start();
        Reader reader = new Reader();
        String[] args1 = {};
        reader.readFile(args1);


        // Server threads are running in the background.
//        System.out.println("Server started");
//        CSVParser csvParser = new CSVParser();
//        List<Station> superList = csvParser.processInputFile("C:\\Users\\nikhi\\OneDrive\\Desktop\\Fall'21\\275\\lab-g01\\reader\\catalog.csv");
//        System.out.println(superList.size());
        // Don't exit the main thread. Wait until server is terminated.
        server.awaitTermination();
    }
}
