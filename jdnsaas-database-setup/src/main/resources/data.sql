use jdnsaas;

insert into tsig_key
    (name, value, algorithm)
values
    ('ddns', '5ifB05a6nUMByf7BqJn5S/wcy8gUaJIidU3TWMX3FkZ/ypLIWnwyCybp6qgn+CbxRNLZz8Xr2QoDTTWEeAJfqw==', 'HMAC_SHA512');

insert into dns_server
    (name, port)
values
    ('ns.zone.test', 53);

insert into dns_view
    (name, dns_server_id)
values
    ('internal', 1);

insert into dns_zone
    (name, dns_view_id, transfer_tsig_key_id, update_tsig_key_id)
values
    ('zone.test', 1, 1, 1),
    ('16.172.in-addr.arpa', 1, 1, 1),
    ('0.0.0.0.0.0.0.0.0.0.0.0.0.0.d.f.ip6.arpa', 1, 1, 1);