package com.example.CMPE275;

import com.example.grpc.HelloServiceI;
import com.example.grpc.LoadBalancerI;
import com.example.reader.Reader;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.util.concurrent.Executors;

public class LoadBalancerApp {

    public static void main( String[] args ) throws Exception
    {
        // Create a new server to listen on port 8080
        System.out.println("Server starting at port 8083" );
        Server server = ServerBuilder.forPort(8083).executor(Executors.newFixedThreadPool(100))
                .addService(new LoadBalancerI())
                //.addService(ProtoReflectionService.newInstance())
                .build();

        // Start the server
        server.start();
//        //Reader reader = new Reader();
//        String[] args1 = {args[0]};
//        reader.readFile(args1);



        server.awaitTermination();
    }
}
