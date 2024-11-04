-- Désactiver la vérification des clés étrangères pour permettre les suppressions de tables sans contraintes
SET FOREIGN_KEY_CHECKS = 0;

-- Suppression des tables dans l'ordre correct
DROP TABLE IF EXISTS tracks_commande;
DROP TABLE IF EXISTS details_commande;
DROP TABLE IF EXISTS licence_tracks;
DROP TABLE IF EXISTS tracks;
DROP TABLE IF EXISTS user;

-- Création des tables

CREATE TABLE user (
    id_user INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    pseudo VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    adresse_facturation TEXT,
    adresse_livraison TEXT,
    avatar VARCHAR(255),
    telephone VARCHAR(20)
);

CREATE TABLE tracks (
    id_tracks INT PRIMARY KEY AUTO_INCREMENT,
    titre VARCHAR(255) NOT NULL,
    date DATETIME NOT NULL,
    bpm VARCHAR(10) NOT NULL,
    description TEXT,
    cle VARCHAR(10) NOT NULL,
    genre VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    audio VARCHAR(255) NOT NULL,
    statut VARCHAR(50),
    jaime VARCHAR(255),
    cover VARCHAR(255) NOT NULL,
    id_user INT NOT NULL,
    FOREIGN KEY (id_user) REFERENCES user(id_user) ON DELETE CASCADE
);

CREATE TABLE licence_tracks (
    id_licence_tracks INT PRIMARY KEY AUTO_INCREMENT,
    type VARCHAR(255) NOT NULL,
    prix VARCHAR(20) NOT NULL,
    id_tracks INT NOT NULL,
    FOREIGN KEY (id_tracks) REFERENCES tracks(id_tracks) ON DELETE CASCADE
);

CREATE TABLE details_commande (
    id_details_commande INT PRIMARY KEY AUTO_INCREMENT,
    prix_total VARCHAR(20) NOT NULL,
    nombre_total VARCHAR(20) NOT NULL,
    id_user INT NOT NULL,
    FOREIGN KEY (id_user) REFERENCES user(id_user) ON DELETE CASCADE
);

CREATE TABLE tracks_commande (
    id_tracks_commande INT PRIMARY KEY AUTO_INCREMENT,
    id_tracks INT NOT NULL,
    id_details_commande INT NOT NULL,
    FOREIGN KEY (id_tracks) REFERENCES tracks(id_tracks) ON DELETE CASCADE,
    FOREIGN KEY (id_details_commande) REFERENCES details_commande(id_details_commande) ON DELETE CASCADE
);

-- Réactivation des clés étrangères
SET FOREIGN_KEY_CHECKS = 1;

-- Insertion de plusieurs utilisateurs (account) avec mot de passe haché en MD5
INSERT INTO user (nom, prenom, email, password, pseudo, type, adresse_facturation, adresse_livraison, avatar, telephone)
VALUES
('Doe', 'John', 'john.doe@example.com', '81dc9bdb52d04dc20036dbd8313ed055', 'johndoe', 'acheteur', '123 Rue Principale', '456 Rue Secondaire', 'avatar1.png', '555-1234'),
('Smith', 'Jane', 'jane.smith@example.com', '81dc9bdb52d04dc20036dbd8313ed055', 'janesmith', 'vendeur', '789 Rue Principale', '987 Rue Tertiaire', 'avatar2.png', '555-5678'),
('Brown', 'Michael', 'michael.brown@example.com', '81dc9bdb52d04dc20036dbd8313ed055', 'mikebrown', 'acheteur', '111 Rue Colline', '222 Rue Vallée', 'avatar3.png', '555-9988'),
('Davis', 'Laura', 'laura.davis@example.com', '81dc9bdb52d04dc20036dbd8313ed055', 'lauradavis', 'vendeur', '333 Rue Foret', '444 Rue Mer', 'avatar4.png', '555-7766'),
('Johnson', 'Chris', 'chris.johnson@example.com', '81dc9bdb52d04dc20036dbd8313ed055', 'chrisjohn', 'acheteur', '555 Rue Montagne', '666 Rue Lac', 'avatar5.png', '555-1122');

-- Insertion de plusieurs pistes musicales (track)
INSERT INTO tracks (titre, date, bpm, description, cle, genre, type, audio, statut, jaime, cover, id_user)
VALUES
('Hip-hop Beat 1', NOW(), '90', 'Un beat hip-hop smooth', 'C', 'Hip-hop', 'Instrumentale', 'audio1.mp3', 'disponible', '100', 'cover1.png', 2),
('Trap Beat 2', NOW(), '120', 'Un beat trap puissant', 'G', 'Trap', 'Instrumentale', 'audio2.mp3', 'disponible', '200', 'cover2.png', 2),
('Jazz Vibes', NOW(), '80', 'Douces vibrations jazz', 'F', 'Jazz', 'Instrumentale', 'audio3.mp3', 'disponible', '150', 'cover3.png', 4),
('Electronic Groove', NOW(), '130', 'Un groove électronique accrocheur', 'A', 'Electronic', 'Instrumentale', 'audio4.mp3', 'disponible', '250', 'cover4.png', 4),
('Classical Symphony', NOW(), '60', 'Une symphonie classique en do majeur', 'C', 'Classique', 'Instrumentale', 'audio5.mp3', 'disponible', '300', 'cover5.png', 2);

-- Insertion de plusieurs licences pour les pistes (licence_tracks)
INSERT INTO licence_tracks (type, prix, id_tracks)
VALUES
('Exclusif', '49.99', 1),
('Non-exclusif', '49.99', 1),
('Exclusif', '49.99', 2),
('Non-exclusif', '49.99', 2),
('Exclusif', '49.99', 3),
('Non-exclusif', '49.99', 3),
('Exclusif', '49.99', 4),
('Non-exclusif', '49.99', 4),
('Exclusif', '49.99', 5),
('Non-exclusif', '49.99', 5);

-- Insertion de plusieurs commandes (details_commande)
INSERT INTO details_commande (prix_total, nombre_total, id_user)
VALUES
('44.98', '2', 1),  -- John Doe achète 2 pistes
('19.99', '1', 3),  -- Michael Brown achète 1 piste
('59.98', '2', 5),  -- Chris Johnson achète 2 pistes
('29.99', '1', 1),  -- John Doe achète 1 piste supplémentaire
('49.99', '1', 3);  -- Michael Brown achète 1 piste supplémentaire

-- Insertion des relations entre les commandes et les pistes (tracks_commande)
INSERT INTO tracks_commande (id_tracks, id_details_commande)
VALUES
(1, 1),  -- John Doe a acheté "Hip-hop Beat 1"
(2, 1),  -- John Doe a acheté "Trap Beat 2"
(3, 2),  -- Michael Brown a acheté "Jazz Vibes"
(4, 3),  -- Chris Johnson a acheté "Electronic Groove"
(5, 3),  -- Chris Johnson a acheté "Classical Symphony"
(1, 4),  -- John Doe a racheté "Hip-hop Beat 1"
(5, 5);  -- Michael Brown a acheté "Classical Symphony"
