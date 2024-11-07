-- Active: 1712233503625@@127.0.0.1@3306@beatdrive
-- Suppression des tables si elles existent déjà pour éviter des erreurs lors de la création
DROP TABLE IF EXISTS Track_Commande;
DROP TABLE IF EXISTS Detail_Commande;
DROP TABLE IF EXISTS Licence_Track;
DROP TABLE IF EXISTS Track;
DROP TABLE IF EXISTS User;

-- Création de la table User
CREATE TABLE User (
    id_user INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255),
    prenom VARCHAR(255),
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    pseudo VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    adresse_facturation VARCHAR(255),
    adresse_livraison VARCHAR(255),
    avatar VARCHAR(255),
    telephone VARCHAR(255)
);

-- Création de la table Track
CREATE TABLE Track (
    id_track INT AUTO_INCREMENT PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    date DATETIME,
    bpm VARCHAR(50) NOT NULL,
    description TEXT,
    cle VARCHAR(50) NOT NULL,
    genre VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL,
    audio VARCHAR(255) NOT NULL,
    statut VARCHAR(50) NOT NULL,
    cover VARCHAR(255) NOT NULL,
    id_user INT,
    FOREIGN KEY (id_user) REFERENCES User(id_user)
);

-- Création de la table Licence_Track
CREATE TABLE Licence_Track (
    id_licence_track INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    prix VARCHAR(255),
    id_track INT,
    FOREIGN KEY (id_track) REFERENCES Track(id_track)
);

-- Création de la table Detail_Commande
CREATE TABLE Detail_Commande (
    id_detail_commande INT AUTO_INCREMENT PRIMARY KEY,
    prix_total VARCHAR(255),
    nombre_total VARCHAR(255),
    date DATETIME,
    id_user INT,
    FOREIGN KEY (id_user) REFERENCES User(id_user)
);

-- Création de la table Track_Commande
CREATE TABLE Track_Commande (
    id_track_commande INT AUTO_INCREMENT PRIMARY KEY,
    id_licence_track INT,
    id_detail_commande INT,
    FOREIGN KEY (id_licence_track) REFERENCES Licence_Track(id_licence_track),
    FOREIGN KEY (id_detail_commande) REFERENCES Detail_Commande(id_detail_commande)
);

-- Insertion de multiples utilisateurs
INSERT INTO User (nom, prenom, email, password, pseudo, type, adresse_facturation, adresse_livraison, avatar, telephone)
VALUES ('Doe', 'John', 'john.doe@example.com', 'securepass123', 'johndoe', 'regular', '1234 Billing St', '1234 Shipping St', 'http://example.com/avatar1.jpg', '1234567890'),
       ('Smith', 'Jane', 'jane.smith@example.com', 'password456', 'janesmith', 'premium', '2345 Billing St', '2345 Shipping St', 'http://example.com/avatar2.jpg', '2345678901'),
       ('Brown', 'Charlie', 'charlie.brown@example.com', 'changepass789', 'charlieb', 'regular', '3456 Billing St', '3456 Shipping St', 'http://example.com/avatar3.jpg', '3456789012');

-- Insertion de multiples pistes
INSERT INTO Track (titre, date, bpm, description, cle, genre, type, audio, statut, cover, id_user)
VALUES ('Summer Vibes', NOW(), '110', 'Perfect track for the summer', 'A Minor', 'Dance', 'Original', 'http://example.com/audio1.mp3', 'Active', 'http://example.com/cover1.jpg', 1),
       ('Winter Whisper', NOW(), '90', 'Smooth and calming', 'E Major', 'Chill', 'Cover', 'http://example.com/audio2.mp3', 'Active', 'http://example.com/cover2.jpg', 2),
       ('Spring Awakening', NOW(), '128', 'Energetic spring beats', 'G Major', 'Electronic', 'Remix', 'http://example.com/audio3.mp3', 'Active', 'http://example.com/cover3.jpg', 3);

-- Insertion de multiples licences
INSERT INTO Licence_Track (type, prix, id_track)
VALUES ('Exclusive', '200.00', 1),
       ('Non-exclusive', '75.00', 2),
       ('Lease', '50.00', 3);

-- Insertion de détails de commandes
INSERT INTO Detail_Commande (prix_total, nombre_total, date, id_user)
VALUES ('300.00', '1', NOW(), 1),
       ('150.00', '2', NOW(), 2),
       ('250.00', '3', NOW(), 3);

-- Insertion de commandes de pistes
INSERT INTO Track_Commande (id_licence_track, id_detail_commande)
VALUES (1, 1),
       (2, 2),
       (3, 3);
