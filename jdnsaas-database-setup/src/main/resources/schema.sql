use jdnsaas;

create table tsig_key(
    id                              int unsigned not null auto_increment,
    name                            varchar(255) not null,
    value                           varchar(255) not null,
    algorithm                       varchar(255) not null,
    primary key(id),
    unique key(name, value, algorithm)
) engine = innodb;

create table resolver(
    id                              int unsigned not null auto_increment,
    address                         varchar(255) not null,
    port                            smallint unsigned not null,
    local_address                   varchar(255),
    primary key(id),
    unique key(address, port, local_address)
) engine = innodb;

create table dns_view(
    id                              int unsigned not null auto_increment,
    name                            varchar(255) not null,
    primary key(id),
    unique key(name)
) engine = innodb;

create table dns_view_resolver(
    dns_view_id                     int unsigned not null,
    resolver_id                     int unsigned not null,
    primary key(dns_view_id,resolver_id),
    foreign key(dns_view_id) references dns_view(id),
    foreign key(resolver_id) references resolver(id)
) engine = innodb;

create table dns_zone(
    id                              int unsigned not null auto_increment,
    name                            varchar(255) not null,
    dns_view_id                     int unsigned not null,
    query_tsig_key_id               int unsigned,
    transfer_tsig_key_id            int unsigned,
    update_tsig_key_id              int unsigned,
    primary key(id),
    foreign key(dns_view_id) references dns_view(id),
    foreign key(query_tsig_key_id) references tsig_key(id),
    foreign key(transfer_tsig_key_id) references tsig_key(id),
    foreign key(update_tsig_key_id) references tsig_key(id),
    unique key(name, dns_view_id)
) engine = innodb;

