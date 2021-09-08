package org.geetingservice;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.geetingservice.GreetingServiceOuterClass.HelloRequest;
import org.geetingservice.GreetingServiceOuterClass.HelloResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.grpc.internal.testing.StreamRecorder;

public class GreetingServiceImplTest {

    GreetingServiceImpl greetingServiceGrpc;

    @Before
    public void setup() {
        this.greetingServiceGrpc = new GreetingServiceImpl();
    }
    
    @Test
    public void testGreeting() throws Exception {
        HelloRequest request = HelloRequest.newBuilder().setName("test").build();
        StreamRecorder<HelloResponse> recorder = StreamRecorder.create();
        this.greetingServiceGrpc.greeting(request, recorder);
        if (!recorder.awaitCompletion(3, TimeUnit.SECONDS)) {
            Assert.fail("Enlapsed time without response");
        }
        List<HelloResponse> values = recorder.getValues();
        Assert.assertNotNull(values);
        Assert.assertEquals(1, values.size());
        HelloResponse response = values.get(0);
        Assert.assertEquals("Hello test!", response.getGreeting());
    }

}
