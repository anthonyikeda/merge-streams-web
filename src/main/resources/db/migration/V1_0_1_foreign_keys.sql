alter table server_regions add constraint fk_region_id foreign key (region_id) references system_regions(region_id);
alter table server_regions add constraint fk_server_id foreign key (server_id) references servers(server_id);
