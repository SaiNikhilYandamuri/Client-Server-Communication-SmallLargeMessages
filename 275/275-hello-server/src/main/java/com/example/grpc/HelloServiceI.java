package com.example.grpc;
import com.example.csvparser.CSVParser;
import com.example.CMPE275.*;
import com.example.reader.MesonetData;
import com.example.reader.MesonetReader;
import io.grpc.Grpc;
import io.grpc.Server;
import io.grpc.stub.StreamObserver;
import com.example.reader.Station;
import java.awt.Rectangle;

import java.io.File;
import java.util.*;
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
        File dataSourceF = null;
        if(portNumber==8080) {
            System.out.println("operating on port 8080");
            dataSource =path+"\\catalog.csv";
            dataSourceF = new File(path+"\\20120201_0100");
        }else if(portNumber==8081){
            System.out.println("operating on port 8081");
            dataSource =path+"\\catalog1.csv";
            dataSourceF = new File(path+"\\20120201_0100");
        }else if(portNumber==8082){
            dataSource =path+"\\catalog2.csv";
            dataSourceF = new File(path+"\\20120201_0100");
        }
        else if(portNumber==8083){
            dataSource =path+"\\catalog3.csv";
        }

        System.out.println(request.getCity()+"Printing city on service side");


            CSVParser csvParser = new CSVParser();
            //List<GreetingServiceOuterClass.HelloResponse> superList = csvParser.processInputFile("C:\\275_lab1\\CMPE275Lab1\\275\\275-hello-server\\catalog.csv");

            List<GreetingServiceOuterClass.HelloResponse> superList = new ArrayList<>();//csvParser.processInputFile(dataSource);

            Date startDate = null;
            Date endDate = null;
            Rectangle region = null;
            Set<String> stationIDs = new HashSet<>();
            stationIDs.add(request.getCity());
//            stationIDs.add("C7959");
//            stationIDs.add("MEGT2");
            MesonetReader rawReader = new MesonetReader();
            List<MesonetData> data = null;
            try {
                data = rawReader.extract(dataSourceF, startDate , endDate, region, stationIDs);
                System.out.println("processed " + data.size() + " entries in service");

                for (MesonetData d : data) {
                    // TODO do something other than print!
                    System.out.println("Obs: " + d.getStationID() + " T = " + d.getTemperature() + ", WS = "
                            + d.getWindSpeed() + ", WD = " + d.getWindDir() + ", RH = " + d.getRelHumidity());

                    GreetingServiceOuterClass.HelloResponse resp = GreetingServiceOuterClass.HelloResponse.newBuilder().setId(d.getStationID()).setName(""+d.getTemperature()).setMesonet(d.getStationName()).build();
                    System.out.println(d.getTemperature());
                    superList.add(resp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


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
