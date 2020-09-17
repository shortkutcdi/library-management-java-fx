-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3308
-- Généré le :  jeu. 06 fév. 2020 à 22:31
-- Version du serveur :  8.0.18
-- Version de PHP :  7.3.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `library_assistant`
--

-- --------------------------------------------------------

--
-- Structure de la table `book`
--

DROP TABLE IF EXISTS `book`;
CREATE TABLE IF NOT EXISTS `book` (
  `book_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `author` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `publisher` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `intcode` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `is_avail` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`book_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

--
-- Déchargement des données de la table `book`
--

INSERT INTO `book` (`book_id`, `title`, `author`, `publisher`, `intcode`, `is_avail`) VALUES
('AA', 'Tempête', 'Giles Burett', 'Ilot', '', 1),
('AA2', 'Book 2', 'Author 2', 'Publi', '', 0),
('B100', 'Harry Potter', 'JK R', 'Publish Books', '', 1);

-- --------------------------------------------------------

--
-- Structure de la table `issue`
--

DROP TABLE IF EXISTS `issue`;
CREATE TABLE IF NOT EXISTS `issue` (
  `issue_id` int(11) NOT NULL AUTO_INCREMENT,
  `book_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `member_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `issue_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `renew_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`issue_id`),
  KEY `book_id_idx` (`book_id`),
  KEY `member_id_idx` (`member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

--
-- Déchargement des données de la table `issue`
--

INSERT INTO `issue` (`issue_id`, `book_id`, `member_id`, `issue_time`, `renew_count`) VALUES
(15, 'aa2', 'bobdoe', '2020-02-03 13:31:12', 0);

-- --------------------------------------------------------

--
-- Structure de la table `membre`
--

DROP TABLE IF EXISTS `membre`;
CREATE TABLE IF NOT EXISTS `membre` (
  `member_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

--
-- Déchargement des données de la table `membre`
--

INSERT INTO `membre` (`member_id`, `name`, `mobile`, `email`) VALUES
('bobdoe', 'Bob Doe', '064545464', 'bobdoe@gmail.com'),
('delphsido', 'Delphine Sidoni', '02546464', 'delphinesidoni@gmail.com'),
('gueco', 'Guenuine Coder', '12354345345', 'guenuinecoder@gamil.com'),
('holf', 'Holly Frank', '2434354', 'hollyf@gamail.com'),
('joshsto', 'Josh Stone', '0654545454', 'josh.stone@gmail.com'),
('wiildigger', 'William Digger', '06454542', 'williamdigger@gmail.com'),
('zoe', 'Zoe Philo', '0644554545', 'zoephilo@gmail.com');

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `issue`
--
ALTER TABLE `issue`
  ADD CONSTRAINT `book_id` FOREIGN KEY (`book_id`) REFERENCES `book` (`book_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `member_id` FOREIGN KEY (`member_id`) REFERENCES `membre` (`member_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
