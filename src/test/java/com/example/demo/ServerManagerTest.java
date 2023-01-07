package com.example.demo;

import io.r2dbc.spi.ConnectionFactory;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Objects;

@SpringBootTest
public class ServerManagerTest {

    private final Logger log = LoggerFactory.getLogger(ServerManagerTest.class);

    @Autowired
    ConnectionFactory factory;

    @Autowired
    ServerManager manager;

    @Test
    public void testGetServersInRegion() {
        StepVerifier.create(manager.findServersInRegion("nz-west-1"))
                .thenConsumeWhile(val -> {
                    log.info("Server name is {}", val.getServerName());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    public void testDatabaseQuery() {
        String regionName = "nz-west-1";
        Flux<Long> test = Mono.from(factory.create())
                .flatMapMany(it ->
                        it.createStatement("select region_id from system_regions where region_name = $1").bind("$1", regionName).execute()
                )
                .log()
                .flatMap(result -> result
                        .map( (row, rowMetadata) -> {
                            long regionId = row.get("region_id", Long.class);
                            return regionId;
                        }))
                .doOnNext(val -> log.info("Region Id for region {} is {}", regionName, val));

        Flux<Long> results = test.flatMap(regionId -> {
            log.info("Looking for servers in region {}", regionId);
            return Mono.from(factory.create())
                    .flatMapMany(it ->
                        it.createStatement("select server_id from server_regions where region_id = $1").bind("$1", regionId).execute())
                    .log()
                    .flatMap(result -> result.map((row, rowMetaData) -> row.get("server_id", Long.class)))
                    .doOnNext(val -> log.info("Server id in region {} is {}", regionName, val));
            });

        StepVerifier.create(results)
                .thenConsumeWhile(x -> {
                    log.info("Recieved server id {}", x);
                    return true;
                })
                .verifyComplete();

    }
}
