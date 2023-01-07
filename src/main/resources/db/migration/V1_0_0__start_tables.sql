create table servers (
    server_id bigint generated always as identity not null,
    server_name varchar(150) not null,
    server_cpus int,
    server_cores int,
    server_os varchar(50),
    constraint pk_server primary key (server_id)
);

create table system_regions (
    region_id bigint generated always as identity not null primary key,
    region_name varchar(50) not null
);

create table server_regions (
    region_id bigint not null,
    server_id bigint not null,
    constraint pk_server_regions primary key (region_id, server_id)
);

insert into servers (server_name, server_cpus, server_cores, server_os) values('nz_124378', 2, 4, 'WindowsNT');
insert into servers (server_name, server_cpus, server_cores, server_os) values('nz_124377', 2, 4, 'WindowsNT');
insert into servers (server_name, server_cpus, server_cores, server_os) values ('uk_54356', 4, 8, 'Linux');
insert into servers (server_name, server_cpus, server_cores, server_os) values ('uk_54357', 4, 8, 'Linux');
insert into servers (server_name, server_cpus, server_cores, server_os) values ('uk_54358', 8, 16, 'Linux');

insert into system_regions (region_name) values ('nz-central-1');
insert into system_regions (region_name) values ('nz-east-1');
insert into system_regions (region_name) values ('nz-east-2');
insert into system_regions (region_name) values ('nz-west-1');
insert into system_regions (region_name) values ('uk-north-1');
insert into system_regions (region_name) values ('uk-north-2');
insert into system_regions (region_name) values ('uk-south-1');

