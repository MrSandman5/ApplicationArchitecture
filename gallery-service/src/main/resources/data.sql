insert into testbase.users values (1, 'client1', '12345678', 'Ivan Petrov', 'ivan_petrov@mail.ru', false),
                                  (2, 'client2', '87654321', 'Vladimir Putin', 'vladimir_putin@mail.ru', false),
                                  (3, 'owner1', '31415926', 'Max Altman', 'max_altmanv@mail.ru', false),
                                  (4, 'artist1', 'qwert123', 'Banksy', 'benksy@mail.ru', false),
                                  (5, 'artist2', 'qwert123456', 'Lampas Pokras', 'lampas_pokras@mail.ru', false);

insert into testbase.clients values (1, '{}', '{}'),
                                    (2, '{}', '{}');

insert into testbase.owners values (3, '{}');

insert into testbase.artists values (4, '{}'),
                                    (5, '{}');
