-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 05, 2023 at 03:52 PM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `studentmanagement`
--
CREATE DATABASE IF NOT EXISTS `studentmanagement` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE `studentmanagement`;

-- --------------------------------------------------------

--
-- Table structure for table `accounts`
--

CREATE TABLE `accounts` (
  `account_id` char(10) NOT NULL,
  `user_id` char(10) NOT NULL,
  `student_list_id` char(5) NOT NULL,
  `user_name` varchar(50) NOT NULL,
  `_password` varchar(50) NOT NULL,
  `role_id` char(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `accounts`
--

INSERT INTO `accounts` (`account_id`, `user_id`, `student_list_id`, `user_name`, `_password`, `role_id`) VALUES
('ACC000001', 'user_00001', 'SL001', 'admin', 'admin123', 'A001'),
('ACC000002', 'user_00002', 'SL001', 'jane_smith', '456', 'E003'),
('ACC000003', 'user_00003', 'SL001', 'bob_johnson', '123', 'M002');

-- --------------------------------------------------------

--
-- Table structure for table `certificates`
--

CREATE TABLE `certificates` (
  `certificate_id` char(10) NOT NULL,
  `classification_id` char(10) NOT NULL,
  `student_id` char(10) NOT NULL,
  `account_id` char(10) NOT NULL,
  `date_create` datetime NOT NULL,
  `certificate_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `certificates`
--

INSERT INTO `certificates` (`certificate_id`, `classification_id`, `student_id`, `account_id`, `date_create`, `certificate_name`) VALUES
('CERT000001', 'CLSF000001', 'STU000001', 'ACC000001', '2023-01-15 08:45:00', 'Certificate of Achievement'),
('CERT000002', 'CLSF000002', 'STU000002', 'ACC000001', '2023-02-28 14:30:00', 'Merit Certificate'),
('CERT000003', 'CLSF000003', 'STU000003', 'ACC000001', '2023-03-10 10:00:00', 'Exemplary Student Award'),
('CERT000004', 'CLSF000001', 'STU000004', 'ACC000001', '2023-04-05 11:20:00', 'Outstanding Performance Award'),
('CERT000005', 'CLSF000002', 'STU000005', 'ACC000001', '2023-05-20 13:45:00', 'Distinguished Achievement Certificate'),
('CERT000006', 'CLSF000003', 'STU000006', 'ACC000001', '2023-06-15 09:30:00', 'Honor Roll Certificate'),
('CERT000007', 'CLSF000001', 'STU000007', 'ACC000001', '2023-07-08 16:15:00', 'Leadership Award'),
('CERT000008', 'CLSF000002', 'STU000008', 'ACC000001', '2023-08-22 12:00:00', 'Sportsmanship Certificate'),
('CERT000009', 'CLSF000003', 'STU000009', 'ACC000001', '2023-09-10 14:45:00', 'Teamwork Recognition'),
('CERT000010', 'CLSF000001', 'STU000010', 'ACC000001', '2023-10-05 10:30:00', 'Creative Arts Certificate');

-- --------------------------------------------------------

--
-- Table structure for table `classifications`
--

CREATE TABLE `classifications` (
  `classification_id` char(10) NOT NULL,
  `classification_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `classifications`
--

INSERT INTO `classifications` (`classification_id`, `classification_name`) VALUES
('CLSF000001', 'Excellent'),
('CLSF000002', 'Good'),
('CLSF000003', 'Average');

-- --------------------------------------------------------

--
-- Table structure for table `histories`
--

CREATE TABLE `histories` (
  `history_id` char(10) NOT NULL,
  `account_id` char(10) NOT NULL,
  `date_perform` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `histories`
--

INSERT INTO `histories` (`history_id`, `account_id`, `date_perform`) VALUES
('HIST000001', 'ACC000001', '2023-01-01 10:30:00'),
('HIST000002', 'ACC000002', '2023-02-15 15:45:00'),
('HIST000003', 'ACC000003', '2023-03-20 08:00:00'),
('HIST000004', 'ACC000001', '2023-04-05 14:20:00'),
('HIST000005', 'ACC000002', '2023-05-12 11:10:00'),
('HIST000006', 'ACC000003', '2023-06-25 17:30:00');

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `role_id` char(4) NOT NULL,
  `role_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `roles`
--

INSERT INTO `roles` (`role_id`, `role_name`) VALUES
('A001', 'Admin'),
('E003', 'Employee'),
('M002', 'Manager');

-- --------------------------------------------------------

--
-- Table structure for table `studentlist`
--

CREATE TABLE `studentlist` (
  `student_list_id` char(5) NOT NULL,
  `total` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `studentlist`
--

INSERT INTO `studentlist` (`student_list_id`, `total`) VALUES
('SL001', 10);

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

CREATE TABLE `students` (
  `student_id` char(10) NOT NULL,
  `student_list_id` char(5) NOT NULL,
  `full_name` varchar(50) NOT NULL,
  `birthday` date NOT NULL,
  `gender` varchar(10) NOT NULL,
  `home_address` varchar(100) NOT NULL,
  `phone_number` varchar(15) NOT NULL,
  `gpa` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `students`
--

INSERT INTO `students` (`student_id`, `student_list_id`, `full_name`, `birthday`, `gender`, `home_address`, `phone_number`, `gpa`) VALUES
('STU000001', 'SL001', 'John Doe', '1995-05-15', 'Male', '123 Main St, Cityville', '555-1234', 3.75),
('STU000002', 'SL001', 'Jane Smith', '1998-02-28', 'Female', '456 Oak St, Townsville', '555-5678', 3.9),
('STU000003', 'SL001', 'Bob Johnson', '1997-11-10', 'Male', '789 Pine St, Villagetown', '555-9876', 3.6),
('STU000004', 'SL001', 'Emily Davis', '1999-08-20', 'Female', '101 Elm St, Hamletville', '555-4321', 3.8),
('STU000005', 'SL001', 'Alex Turner', '1996-04-03', 'Male', '202 Birch St, Countryside', '555-8765', 3.95),
('STU000006', 'SL001', 'Sara Miller', '2000-01-12', 'Female', '303 Maple St, Suburbia', '555-2109', 3.7),
('STU000007', 'SL001', 'Michael Lee', '1994-09-25', 'Male', '404 Cedar St, Ruralville', '555-1098', 3.85),
('STU000008', 'SL001', 'Ella White', '1993-06-18', 'Female', '505 Walnut St, Farmland', '555-6543', 3.55),
('STU000009', 'SL001', 'Chris Brown', '1992-03-07', 'Male', '606 Spruce St, Outskirts', '555-8769', 3.65),
('STU000010', 'SL001', 'Mia Johnson', '1991-12-30', 'Female', '707 Pine St, Countryside', '555-2345', 3.75);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` char(10) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `age` int(11) NOT NULL,
  `phone_number` varchar(15) NOT NULL,
  `image_profile` varchar(500) NOT NULL,
  `status_user` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `full_name`, `age`, `phone_number`, `image_profile`, `status_user`) VALUES
('user_00001', 'John Doe', 30, '123-456-7890', '', 'Active'),
('user_00002', 'Jane Smith', 35, '987-654-3210', '', 'Active'),
('user_00003', 'Bob Johnson', 25, '555-123-4567', '', 'Inactive');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `accounts`
--
ALTER TABLE `accounts`
  ADD PRIMARY KEY (`account_id`),
  ADD KEY `FK_Account_UserID` (`user_id`),
  ADD KEY `student_list_id` (`student_list_id`),
  ADD KEY `FK_Account_Role` (`role_id`);

--
-- Indexes for table `certificates`
--
ALTER TABLE `certificates`
  ADD PRIMARY KEY (`certificate_id`),
  ADD KEY `FK_Certificate_Account` (`account_id`),
  ADD KEY `FK_Certificate_Student` (`student_id`),
  ADD KEY `FK_Certificate_Classificate` (`classification_id`);

--
-- Indexes for table `classifications`
--
ALTER TABLE `classifications`
  ADD PRIMARY KEY (`classification_id`);

--
-- Indexes for table `histories`
--
ALTER TABLE `histories`
  ADD PRIMARY KEY (`history_id`),
  ADD KEY `FK_History_Account` (`account_id`);

--
-- Indexes for table `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`role_id`);

--
-- Indexes for table `studentlist`
--
ALTER TABLE `studentlist`
  ADD PRIMARY KEY (`student_list_id`);

--
-- Indexes for table `students`
--
ALTER TABLE `students`
  ADD PRIMARY KEY (`student_id`),
  ADD KEY `FK_Students_StudentList` (`student_list_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `accounts`
--
ALTER TABLE `accounts`
  ADD CONSTRAINT `FK_Account_Role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`),
  ADD CONSTRAINT `FK_Account_UserID` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `accounts_ibfk_1` FOREIGN KEY (`student_list_id`) REFERENCES `studentlist` (`student_list_id`);

--
-- Constraints for table `certificates`
--
ALTER TABLE `certificates`
  ADD CONSTRAINT `FK_Certificate_Account` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`account_id`),
  ADD CONSTRAINT `FK_Certificate_Classificate` FOREIGN KEY (`classification_id`) REFERENCES `classifications` (`classification_id`),
  ADD CONSTRAINT `FK_Certificate_Student` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`);

--
-- Constraints for table `histories`
--
ALTER TABLE `histories`
  ADD CONSTRAINT `FK_History_Account` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`account_id`);

--
-- Constraints for table `students`
--
ALTER TABLE `students`
  ADD CONSTRAINT `FK_Students_StudentList` FOREIGN KEY (`student_list_id`) REFERENCES `studentlist` (`student_list_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
