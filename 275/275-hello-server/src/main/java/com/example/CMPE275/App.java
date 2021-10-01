package com.example.CMPE275;

import com.example.grpc.HelloServiceI;
import com.example.reader.Station;
import io.grpc.*;
import com.example.reader.Reader;
import com.example.csvparser.CSVParser;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * Hello world!
 *
 */

public class App 
{
    public static void main( String[] args ) throws Exception
    {
        // Create a new server to listen on port 8080
        System.out.println("Server starting at port "+ args[0]);
        Server server = ServerBuilder.forPort(Integer.parseInt(args[0])).executor(Executors.newFixedThreadPool(100))
                .addService(new HelloServiceI())
                .build();

        // Start the server
        server.start();
        Reader reader = new Reader();
        String[] args1 = {args[0]};
        reader.readFile(args1);



        server.awaitTermination();
    }


}
