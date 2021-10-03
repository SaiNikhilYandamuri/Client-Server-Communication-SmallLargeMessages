package com.example.grpc;
import com.example.csvparser.CSVParser;
import com.example.CMPE275.*;
import io.grpc.Grpc;
import io.grpc.Server;
import io.grpc.stub.StreamObserver;
import com.example.reader.Station;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class HelloServiceI extends LargeMessageServiceGrpc.LargeMessageServiceImplBase{
    int portNumber = 8080;
    public HelloServiceI(){

    }
    public HelloServiceI(int port){
        portNumber = port;
    }
    @Override
    public void largeMessage(GreetingServiceOuterClass.HelloRequest request,
                             StreamObserver<GreetingServiceOuterClass.Responses> responseObserver) {
        System.out.println(request);
        String path = "C:\\275_lab1\\CMPE275Lab1\\275\\275-hello-server";
        String dataSource = null;
        if(portNumber==8080) {
            System.out.println("operating on port 8080");
            dataSource =path+"\\catalog.csv";
        }else if(portNumber==8081){
            System.out.println("operating on port 8081");
            dataSource =path+"\\catalog1.csv";
        }else if(portNumber==8082){
            dataSource =path+"\\catalog2.csv";
        }
        else if(portNumber==8083){
            dataSource =path+"\\catalog3.csv";
        }

            CSVParser csvParser = new CSVParser();
            //List<GreetingServiceOuterClass.HelloResponse> superList = csvParser.processInputFile("C:\\275_lab1\\CMPE275Lab1\\275\\275-hello-server\\catalog.csv");
            List<GreetingServiceOuterClass.HelloResponse> superList = csvParser.processInputFile(dataSource);
            System.out.println(superList.size());
            try {
                //System.out.println(queue.poll().getCity());
            }catch (Exception e){
                e.printStackTrace();
            }

            GreetingServiceOuterClass.Responses responses = GreetingServiceOuterClass.Responses.newBuilder().addAllResponse(superList.stream().collect(Collectors.toList())).build();

            System.out.println("Super");
            responseObserver.onNext(responses);
            responseObserver.onCompleted();
            //System.out.println("Queue Size = " + queue.size());
        //}

    }



}
