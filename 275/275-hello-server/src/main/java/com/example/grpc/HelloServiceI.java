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
//        System.out.println(request);
        String path = "C:\\Users\\nikhi\\OneDrive\\Desktop\\Fall'21\\275\\lab-g01\\";
        String dataSource = null;
        if(portNumber==8080) {
            dataSource =path+"reader\\catalog.csv";
        }else if(portNumber==8081){
            dataSource =path+"reader\\catalog-1.csv";
        }else if(portNumber==8082){
            dataSource =path+"reader\\catalog-2.csv";
        }

            CSVParser csvParser = new CSVParser();
            //List<GreetingServiceOuterClass.HelloResponse> superList = csvParser.processInputFile("C:\\275_lab1\\CMPE275Lab1\\275\\275-hello-server\\catalog.csv");
            List<GreetingServiceOuterClass.HelloResponse> superList = csvParser.processInputFile(dataSource);
            System.out.println(superList.size());
//        MultiThreadingReadFile mtrf = new MultiThreadingReadFile();
//        mtrf.start();
//            try {
//                //System.out.println(queue.poll().getCity());
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        try {
//            mtrf.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        //System.out.println("SUper We are");
//        for(int i=0;i<mtrf.list.size();i++){
//                System.out.println(mtrf.list.get(i));
//            }
            //GreetingServiceOuterClass.Responses responses = GreetingServiceOuterClass.Responses.newBuilder().addAllResponse(mtrf.list.stream().collect(Collectors.toList())).build();
        GreetingServiceOuterClass.Responses responses = GreetingServiceOuterClass.Responses.newBuilder().addAllResponse(superList.stream().collect(Collectors.toList())).build();

        System.out.println("Super");
            responseObserver.onNext(responses);
            responseObserver.onCompleted();
            //System.out.println("Queue Size = " + queue.size());
        //}

    }
}
class MultiThreadingReadFile extends Thread {
    public List<GreetingServiceOuterClass.HelloResponse> list = null;
    public MultiThreadingReadFile(){
        this.list = new ArrayList<>();
    }
    public void run(){
        String path = "C:\\Users\\nikhi\\OneDrive\\Desktop\\Fall'21\\275\\lab-g01\\";
        String dataSource = null;
        try{
            //System.out.println(Thread.currentThread().getId());
            if((Thread.currentThread().getId())%3==0) {
                dataSource =path+"reader\\catalog.csv";
            }else if((Thread.currentThread().getId())%3==1){
                dataSource =path+"reader\\catalog-1.csv";
            }else if((Thread.currentThread().getId())%3==2){
                dataSource =path+"reader\\catalog-2.csv";
            }

            CSVParser csvParser = new CSVParser();
            //List<GreetingServiceOuterClass.HelloResponse> superList = csvParser.processInputFile("C:\\275_lab1\\CMPE275Lab1\\275\\275-hello-server\\catalog.csv");
            this.list = csvParser.processInputFile(dataSource);
            System.out.println(this.list.size());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
