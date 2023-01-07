package com.example.demo;

import io.r2dbc.spi.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ServerManager {

    final ServerRepository repository;
    final ConnectionFactory factory;

    private final Logger log = LoggerFactory.getLogger(ServerManager.class);

    @Autowired
    public ServerManager(ServerRepository _repository, ConnectionFactory _factory) {
        this.repository = _repository;
        this.factory = _factory;
    }

    public Mono<ServerDAO> findServerById(long serverId) {
        return this.repository.findById(serverId);
    }

    public Flux<ServerDAO> findServersInRegion(final String regionName) {
        Flux<Long> region = Mono.from(factory.create())
                .flatMapMany(it ->
                    it.createStatement("select region_id from system_regions where region_name = $1").bind("$1", regionName).execute()
                )
                .log()
                .flatMap(result -> result
                        .map( (row, rowMetadata) -> row.get("region_id", Long.class)))
                .doOnNext(val -> log.info("Region Id for region {} is {}", regionName, val));

        Flux<Long> serverIds = region.flatMap(regionId ->
            Mono.from(factory.create())
                    .flatMapMany(it ->
                            it.createStatement("select server_id from server_regions where region_id = $1").bind("$1", regionId).execute())
                    .flatMap(result -> result.map((row, rowMetaData) -> row.get("server_id", Long.class)))
        );

        return serverIds.flatMap(serverId ->
                Mono.from(factory.create())
                        .flatMapMany(conn ->
                                Flux.from(conn.createStatement("select * from servers where server_id = $1").bind("$1", serverId).execute()))
                        .flatMap(result -> result.map((row, rowMetadata) -> {
                            var serverName = row.get("server_name", String.class);
                            var serverCores = row.get("server_cores", Integer.class);
                            var serverCpus = row.get("server_cpus", Integer.class);
                            var serverOs = row.get("server_os", String.class);
                            var server = new ServerDAO();

                            server.setServerId(serverId);
                            server.setServerName(serverName);
                            server.setServerCores(serverCores);
                            server.setServerCpus(serverCpus);
                            server.setServerOs(serverOs);
                            return server;
                        }))
        );
    }
}
