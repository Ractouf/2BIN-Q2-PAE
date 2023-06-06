DROP SCHEMA IF EXISTS projet_pae CASCADE;
CREATE SCHEMA projet_pae;

-- Tables

CREATE TABLE projet_pae.types
(
    id_type   SERIAL PRIMARY KEY,
    type_name VARCHAR(30) NOT NULL,

    version INTEGER NOT NULL
);

CREATE TABLE projet_pae.availabilities
(
    id_availability   SERIAL PRIMARY KEY,
    availability_date DATE NOT NULL,
    starting_hour     TIME NOT NULL,
    ending_hour       TIME NOT NULL,

    version INTEGER NOT NULL
);

CREATE TABLE projet_pae.users
(
    id_user       SERIAL PRIMARY KEY,
    email         VARCHAR(100) NOT NULL,
    password      VARCHAR(100) NOT NULL,
    register_date DATE         NOT NULL,
    lastname      VARCHAR(50)  NOT NULL,
    firstname     VARCHAR(50)  NOT NULL,
    photo         VARCHAR(100) NOT NULL,
    phone_number  VARCHAR(13)  NOT NULL,
    role          CHAR(1)      NOT NULL,

    version INTEGER NOT NULL
);

CREATE TABLE projet_pae.objects
(
    id_object                  SERIAL PRIMARY KEY,
    proposal_date              DATE         NOT NULL,
    interest_confirmation_date DATE,
    store_deposit_date         DATE,
    market_withdrawal_date     DATE,
    selling_date               DATE,
    description                VARCHAR(120) NOT NULL,
    photo                      VARCHAR(100) NOT NULL,
    selling_price              DOUBLE PRECISION,
    status                     CHAR(2)      NOT NULL,
    reason_for_refusal         VARCHAR(100),
    fk_object_type             INTEGER      NOT NULL REFERENCES projet_pae.types (id_type),
    fk_availability            INTEGER      NOT NULL REFERENCES projet_pae.availabilities (id_availability),
    fk_offering_member         INTEGER REFERENCES projet_pae.users (id_user),
    unknown_user_phone_number  VARCHAR(13),

    version INTEGER NOT NULL
);

CREATE TABLE projet_pae.notifications
(
    id_notification     SERIAL PRIMARY KEY,
    text_notification   VARCHAR(100) NOT NULL,
    fk_concerned_object INTEGER      NOT NULL REFERENCES projet_pae.objects (id_object),

    version INTEGER NOT NULL
);

CREATE TABLE projet_pae.notifications_users
(
    is_read           BOOLEAN NOT NULL,
    fk_concerned_user INTEGER NOT NULL REFERENCES projet_pae.users (id_user),
    fk_notification   INTEGER NOT NULL REFERENCES projet_pae.notifications (id_notification),
    PRIMARY KEY (fk_concerned_user, fk_notification),

    version INTEGER NOT NULL
);

-- Demo

-- Object types
INSERT INTO projet_pae.types (type_name, version) VALUES ('Meuble', 1);
INSERT INTO projet_pae.types (type_name, version) VALUES ('Table', 1);
INSERT INTO projet_pae.types (type_name, version) VALUES ('Chaise', 1);
INSERT INTO projet_pae.types (type_name, version) VALUES ('Fauteuil', 1);
INSERT INTO projet_pae.types (type_name, version) VALUES ('Lit/Sommier', 1);
INSERT INTO projet_pae.types (type_name, version) VALUES ('Matelas', 1);
INSERT INTO projet_pae.types (type_name, version) VALUES ('Couvertures', 1);
INSERT INTO projet_pae.types (type_name, version) VALUES ('Matériel de cuisine', 1);
INSERT INTO projet_pae.types (type_name, version) VALUES ('Vaisselle', 1);

-- Managers
INSERT INTO projet_pae.users (email, password, register_date, lastname, firstname, photo, phone_number, role, version) VALUES ('bert.riez@gmail.be', '$2a$10$4q5xhF.gc9ZCfPT7TqpyT..QWUZMulb65mS87BojSzf9OLPnB9Pfq', '2023-03-10', 'Riez', 'Robert', 'MrRiez.png', '0477/96.85.47', 'M', 1);

-- Helpers
INSERT INTO projet_pae.users (email, password, register_date, lastname, firstname, photo, phone_number, role, version) VALUES ('fred.muise@gmail.be', '$2a$10$4MHnD8sVa7jRpd2FA/1jxuvGjqhTdIdSY7x..vYeTG93izkeuqfFO', '2023-03-10', 'Muise', 'Alfred', 'fred.png', '0476/96.36.26', 'H', 1);
INSERT INTO projet_pae.users (email, password, register_date, lastname, firstname, photo, phone_number, role, version) VALUES ('charline@proximus.be', '$2a$10$0NIcnM2kNTyJLjP.Z8XjyeP./O64pCZPpdtA3X9PeIj1s1KNXp80K', '2023-05-06', 'Line', 'Charles', 'Charline.png', '0481/35.62.49', 'H', 1);

-- Users
INSERT INTO projet_pae.users (email, password, register_date, lastname, firstname, photo, phone_number, role, version) VALUES ('caro.line@hotmail.com', '$2a$10$GmYkZS1q9m78nzrcVxRV9..Nvl9klOFLnTuhB7QEdNb3i81jPT51y', '2023-03-10', 'Line', 'Caroline', 'caro.png', '0487/45.23.79', 'U', 1);
INSERT INTO projet_pae.users (email, password, register_date, lastname, firstname, photo, phone_number, role, version) VALUES ('ach.ile@gmail.com', '$2a$10$GmYkZS1q9m78nzrcVxRV9..Nvl9klOFLnTuhB7QEdNb3i81jPT51y', '2023-03-10', 'Ile', 'Achille', 'achil.png', '0477/65.32.24', 'U', 1);
INSERT INTO projet_pae.users (email, password, register_date, lastname, firstname, photo, phone_number, role, version) VALUES ('bas.ile@gmail.be', '$2a$10$GmYkZS1q9m78nzrcVxRV9..Nvl9klOFLnTuhB7QEdNb3i81jPT51y', '2023-03-10', 'Ile', 'Basile', 'bazz.jpg', '0485/98.86.42', 'U', 1);
INSERT INTO projet_pae.users (email, password, register_date, lastname, firstname, photo, phone_number, role, version) VALUES ('theo.phile@proximus.be', '$2a$10$GmYkZS1q9m78nzrcVxRV9..Nvl9klOFLnTuhB7QEdNb3i81jPT51y', '2023-05-06', 'Ile', 'Théophile', 'theo.png', '0488/35.33.89', 'U', 1);

-- Availabilities
INSERT INTO projet_pae.availabilities (availability_date, starting_hour, ending_hour, version) VALUES ('2023-03-04', '11:00:00', '13:00:00', 1);
INSERT INTO projet_pae.availabilities (availability_date, starting_hour, ending_hour, version) VALUES ('2023-03-04', '14:00:00', '16:00:00', 1);
INSERT INTO projet_pae.availabilities (availability_date, starting_hour, ending_hour, version) VALUES ('2023-03-11', '11:00:00', '13:00:00', 1);
INSERT INTO projet_pae.availabilities (availability_date, starting_hour, ending_hour, version) VALUES ('2023-03-11', '14:00:00', '16:00:00', 1);
INSERT INTO projet_pae.availabilities (availability_date, starting_hour, ending_hour, version) VALUES ('2023-03-18', '11:00:00', '13:00:00', 1);
INSERT INTO projet_pae.availabilities (availability_date, starting_hour, ending_hour, version) VALUES ('2023-03-18', '14:00:00', '16:00:00', 1);
INSERT INTO projet_pae.availabilities (availability_date, starting_hour, ending_hour, version) VALUES ('2023-03-25', '11:00:00', '13:00:00', 1);
INSERT INTO projet_pae.availabilities (availability_date, starting_hour, ending_hour, version) VALUES ('2023-03-25', '14:00:00', '16:00:00', 1);
INSERT INTO projet_pae.availabilities (availability_date, starting_hour, ending_hour, version) VALUES ('2023-04-01', '11:00:00', '13:00:00', 1);
INSERT INTO projet_pae.availabilities (availability_date, starting_hour, ending_hour, version) VALUES ('2023-04-01', '14:00:00', '16:00:00', 1);
INSERT INTO projet_pae.availabilities (availability_date, starting_hour, ending_hour, version) VALUES ('2023-04-15', '11:00:00', '13:00:00', 1);
INSERT INTO projet_pae.availabilities (availability_date, starting_hour, ending_hour, version) VALUES ('2023-04-15', '14:00:00', '16:00:00', 1);
INSERT INTO projet_pae.availabilities (availability_date, starting_hour, ending_hour, version) VALUES ('2023-04-22', '11:00:00', '13:00:00', 1);
INSERT INTO projet_pae.availabilities (availability_date, starting_hour, ending_hour, version) VALUES ('2023-04-22', '14:00:00', '16:00:00', 1);
INSERT INTO projet_pae.availabilities (availability_date, starting_hour, ending_hour, version) VALUES ('2023-04-29', '11:00:00', '13:00:00', 1);
INSERT INTO projet_pae.availabilities (availability_date, starting_hour, ending_hour, version) VALUES ('2023-04-29', '14:00:00', '16:00:00', 1);
INSERT INTO projet_pae.availabilities (availability_date, starting_hour, ending_hour, version) VALUES ('2023-05-13', '11:00:00', '13:00:00', 1);
INSERT INTO projet_pae.availabilities (availability_date, starting_hour, ending_hour, version) VALUES ('2023-05-13', '14:00:00', '16:00:00', 1);
INSERT INTO projet_pae.availabilities (availability_date, starting_hour, ending_hour, version) VALUES ('2023-05-27', '11:00:00', '13:00:00', 1);
INSERT INTO projet_pae.availabilities (availability_date, starting_hour, ending_hour, version) VALUES ('2023-05-27', '14:00:00', '16:00:00', 1);
INSERT INTO projet_pae.availabilities (availability_date, starting_hour, ending_hour, version) VALUES ('2023-06-03', '11:00:00', '13:00:00', 1);
INSERT INTO projet_pae.availabilities (availability_date, starting_hour, ending_hour, version) VALUES ('2023-06-03', '14:00:00', '16:00:00', 1);
INSERT INTO projet_pae.availabilities (availability_date, starting_hour, ending_hour, version) VALUES ('2023-06-17', '11:00:00', '13:00:00', 1);
INSERT INTO projet_pae.availabilities (availability_date, starting_hour, ending_hour, version) VALUES ('2023-06-17', '14:00:00', '16:00:00', 1);

-- Objects
INSERT INTO projet_pae.objects (proposal_date, interest_confirmation_date, store_deposit_date, description, photo, selling_price, status, fk_object_type, fk_availability, fk_offering_member, version) VALUES ('2023-01-01', '2023-03-15', '2023-03-23', 'Chaise en bois brut avec cousin beige', 'Chaise-wooden-gbe3bb4b3a_1280.png', 2.00, 'SA', 3, 5, 6, 1);
INSERT INTO projet_pae.objects (proposal_date, interest_confirmation_date, store_deposit_date, selling_date, description, photo, selling_price, status, fk_object_type, fk_availability, fk_offering_member, version) VALUES ('2023-02-01', '2023-03-15', '2023-03-23', '2023-03-23', 'Canapé 3 places blanc', 'Fauteuil-sofa-g99f90fab2_1280.jpg', 3.00, 'SO', 4, 5, 6, 1);
INSERT INTO projet_pae.objects (proposal_date, interest_confirmation_date, description, photo, status, reason_for_refusal, fk_object_type, fk_availability, unknown_user_phone_number, version) VALUES ('2023-02-02', '2023-03-15', 'Secrétaire', 'Secretaire.png', 'RF', 'Ce meuble est magnifique mais fragile pour l’usage qui en sera fait.', 1, 8, '0496/32.16.54', 1);
INSERT INTO projet_pae.objects (proposal_date, interest_confirmation_date, store_deposit_date, description, photo, status, fk_object_type, fk_availability, fk_offering_member, version) VALUES ('2023-03-01', '2023-03-20', '2023-03-29', '100 assiettes blanches', 'Vaisselle-plate-629970_1280.jpg', 'SH', 9, 8, 5, 1);
INSERT INTO projet_pae.objects (proposal_date, interest_confirmation_date, store_deposit_date, selling_date, description, photo, selling_price, status, fk_object_type, fk_availability, fk_offering_member, version) VALUES ('2023-03-08', '2023-03-20', '2023-03-29', '2023-03-29', 'Grand canapé 4 places bleu usé', 'Fauteuil-couch-g0f519ec38_1280.png', 3.50, 'SO', 4, 8, 5, 1);
INSERT INTO projet_pae.objects (proposal_date, interest_confirmation_date, store_deposit_date, market_withdrawal_date, description, photo, selling_price, status, fk_object_type, fk_availability, fk_offering_member, version) VALUES ('2022-09-05', '2023-03-15', '2023-03-18', '2023-04-29', 'Fauteuil design très confortable', 'Fauteuil-design-gee14e1707_1280.jpg', 5.20, 'RM', 4, 6, 5, 1);
INSERT INTO projet_pae.objects (proposal_date, interest_confirmation_date, description, photo, status, reason_for_refusal, fk_object_type, fk_availability, fk_offering_member, version) VALUES ('2022-12-03', '2023-03-28', 'Tabouret de bar en cuir', 'bar-890375_1920.jpg', 'RF', 'Ceci n''est pas une chaise', 3, 10, 5, 1);
INSERT INTO projet_pae.objects (proposal_date, interest_confirmation_date, description, photo, status, fk_object_type, fk_availability, fk_offering_member, version)  VALUES ('2023-04-01', '2023-04-11', 'Fauteuil ancien, pieds et accoudoir en bois', 'Fauteuil-chair-g6374c21b8_1280.jpg', 'WK', 4, 13, 6, 1);
INSERT INTO projet_pae.objects (proposal_date, interest_confirmation_date, store_deposit_date, description, photo, status, fk_object_type, fk_availability, fk_offering_member, version) VALUES ('2023-04-02', '2023-04-11', '2023-04-25', '6 bols à soupe', 'Vaisselle-Bol-bowl-469295_1280.jpg', 'SH', 9, 13, 6, 1);
INSERT INTO projet_pae.objects (proposal_date, interest_confirmation_date, store_deposit_date, description, photo, status, fk_object_type, fk_availability, fk_offering_member, version) VALUES ('2023-04-03', '2023-04-11', '2023-04-25', 'Lit cage blanc', 'LitEnfant-nursery-g9913b3b19_1280.jpg', 'SH', 1, 14, 7, 1);
INSERT INTO projet_pae.objects (proposal_date, interest_confirmation_date, store_deposit_date, description, photo, status, fk_object_type, fk_availability, fk_offering_member, version) VALUES ('2023-04-04', '2023-04-18', '2023-05-05', '30 pots à épices', 'PotEpices-pharmacy-g01563afff_1280.jpg', 'SH', 9, 15, 7, 1);
INSERT INTO projet_pae.objects (proposal_date, interest_confirmation_date, store_deposit_date, description, photo, status, fk_object_type, fk_availability, fk_offering_member, version) VALUES ('2023-04-05', '2023-04-18', '2023-05-05', '4 tasses à café et leurs sous-tasses', 'Vaisselle-Tassescup-1320578_1280.jpg', 'SH', 9, 15, 4, 1);

-- Comptage du nombre d’objets dans chacun des états
SELECT
    CASE
        WHEN all_statuses.status = 'PR' THEN 'Proposé'
        WHEN all_statuses.status = 'AC' THEN 'Accepté'
        WHEN all_statuses.status = 'RF' THEN 'Refusé'
        WHEN all_statuses.status = 'WK' THEN 'En atelier'
        WHEN all_statuses.status = 'SH' THEN 'En magasin'
        WHEN all_statuses.status = 'SA' THEN 'En vente'
        WHEN all_statuses.status = 'SO' THEN 'Vendu'
        WHEN all_statuses.status = 'RM' THEN 'Retiré'
        END AS status_description,
    COUNT(o.id_object) AS status_count
FROM
    (VALUES ('PR'), ('AC'), ('RF'), ('WK'), ('SH'), ('SA'), ('SO'), ('RM'))
        AS all_statuses(status)
        LEFT JOIN projet_pae.objects o ON all_statuses.status = o.status
GROUP BY all_statuses.status;

-- Types d’objets et comptage du nombre d’objets par type
SELECT ty.type_name, COUNT(ob.id_object)
FROM projet_pae.types ty
         LEFT OUTER JOIN projet_pae.objects ob ON ty.id_type = ob.fk_object_type
GROUP BY ty.type_name;

-- Utilisateurs qui ont des objets et comptage du nombre d’objets par utilisateur
SELECT us.lastname, us.firstname, COUNT(ob.id_object)
FROM projet_pae.objects ob,
     projet_pae.users us
WHERE us.id_user = ob.fk_offering_member
GROUP BY us.lastname, us.firstname;

-- Objets : description, état, date réception, date acceptation proposition, date dépôt en magasin, date vente, date retrait
SELECT ob.description,
       CASE ob.status
           WHEN 'PR' THEN 'Proposé'
           WHEN 'AC' THEN 'Accepté'
           WHEN 'RF' THEN 'Refusé'
           WHEN 'WK' THEN 'En atelier'
           WHEN 'SH' THEN 'En magasin'
           WHEN 'SA' THEN 'En vente'
           WHEN 'SO' THEN 'Vendu'
           WHEN 'RM' THEN 'Retiré'
           ELSE ob.status
           END AS status,
       av.availability_date,
       ob.interest_confirmation_date,
       ob.store_deposit_date,
       ob.selling_date,
       ob.market_withdrawal_date,
       ob.proposal_date
FROM projet_pae.objects ob,
     projet_pae.availabilities av
WHERE av.id_availability = ob.fk_availability;

-- Objets : comptage nombre de chaque date
SELECT COUNT(proposal_date)              AS "date de proposition",
       COUNT(interest_confirmation_date) AS "date acceptation proposition",
       COUNT(store_deposit_date)         AS "date dépôt en magasin",
       COUNT(market_withdrawal_date)     AS "date retrait",
       COUNT(selling_date)               AS "date vente"
FROM projet_pae.objects;

-- Rôles et comptage du nombre d’utilisateurs par rôle
SELECT COUNT(us.id_user),
       CASE us.role
           WHEN 'M' THEN 'Responsable'
           WHEN 'H' THEN 'Aidant'
           WHEN 'U' THEN 'Utilisateur'
           ELSE us.role
           END AS role
FROM projet_pae.users us
GROUP BY us.role;

-- Comptage du nombre d’utilisateurs
SELECT COUNT(*) as nombre_utilisateurs
FROM projet_pae.users;

-- Comptage du nombre de types d’objets
SELECT COUNT(*) as nombre_types
FROM projet_pae.types;

-- Comptage des dates de présences
SELECT COUNT(DISTINCT availability_date) as nombre_dates_presence
FROM projet_pae.availabilities;
