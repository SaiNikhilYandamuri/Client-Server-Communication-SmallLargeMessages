package com.example.CMPE275;

import com.example.grpc.HelloServiceI;
import io.grpc.*;
import com.example.reader.Reader;
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
        System.out.println("Server started");
        // Don't exit the main thread. Wait until server is terminated.
        server.awaitTermination();
    }
}
