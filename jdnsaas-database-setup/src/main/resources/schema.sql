use jdnsaas;

create table transaction_signature_algorithm(
    id                                  int unsigned not null auto_increment,
    name                                varchar(255) not null,
    primary key(id)
) engine = innodb;

create table transaction_signature(
    id                                  int unsigned not null auto_increment,
    name                                varchar(255) not null,
    secret                              varchar(255) not null,
    transaction_signature_algorithm_id  int unsigned not null,
    primary key(id),
    foreign key(transaction_signature_algorithm_id) references transaction_signature_algorithm(id)
) engine = innodb;

create table dns_server(
    id                                  int unsigned not null auto_increment,
    name                                varchar(255) not null,
    port                                smallint unsigned not null,
    transaction_signature_id            int unsigned not null,
    primary key(id),
    foreign key(transaction_signature_id) references transaction_signature(id),
    unique key(name)
) engine = innodb;

create table zone(
    id                                  int unsigned not null auto_increment,
    name                                varchar(255) not null,
    dns_server_id                       int unsigned not null,
    primary key(id),
    foreign key(dns_server_id) references dns_server(id),
    unique key(name, dns_server_id)
) engine = innodb;

