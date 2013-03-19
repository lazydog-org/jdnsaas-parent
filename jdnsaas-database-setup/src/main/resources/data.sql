use jdnsaas;

insert into transaction_signature
    (name, secret, algorithm)
values
    ('ddns', '5ifB05a6nUMByf7BqJn5S/wcy8gUaJIidU3TWMX3FkZ/ypLIWnwyCybp6qgn+CbxRNLZz8Xr2QoDTTWEeAJfqw==', 'HMAC_SHA512');

insert into dns_server
    (name, port, transaction_signature_id)
values
    ('ns.zone.test', 53, 1);

insert into zone
    (name, dns_server_id)
values
    ('zone.test', 1),
    ('16.172.in-addr.arpa', 1),
    ('0.0.0.0.0.0.0.0.0.0.0.0.0.0.d.f.ip6.arpa', 1);