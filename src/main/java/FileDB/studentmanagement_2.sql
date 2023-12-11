-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 10, 2023 at 07:37 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

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
-- Table structure for table `certificate`
--

CREATE TABLE `certificates` (
  `certificate_id` INT NOT NULL AUTO_INCREMENT,
  `student_id` char(10) NOT NULL,
  `date_create` date DEFAULT curdate(),
  `certificate_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `certificate_level` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `expired_date` date DEFAULT NULL,
  PRIMARY KEY (`certificate_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `certificate`
--

INSERT INTO `certificates` (`student_id`, `date_create`, `certificate_name`, `certificate_level`, `expired_date`) VALUES
('STU0000001', '2023-12-10', 'IELTS General', '7.0', '2024-03-22'),
('STU0000002', '2023-12-10', 'IELTS Academic', '6.5', '2024-05-15'),
('STU0000003', '2023-12-10', 'TOEIC', '850', '2024-08-10'),
('STU0000004', '2023-12-10', 'IELTS General', '7.5', '2023-11-28'),
('STU0000005', '2023-12-10', 'TOEIC', '950', '2024-09-30'),
('STU0000006', '2023-12-10', 'IELTS Academic', '7.0', '2023-06-12'),
('STU0000007', '2023-12-10', 'TOEIC', '900', '2024-12-05'),
('STU0000008', '2023-12-10', 'IELTS General', '6.0', '2023-09-18'),
('STU0000009', '2023-12-10', 'TOEIC', '800', '2024-07-22'),
('STU0000010', '2023-12-10', 'IELTS Academic', '7.5', '2023-04-02'),
('STU0000010', '2023-12-10', 'TOEIC', '870', '2024-11-15');

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
('SL001', 12);

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

CREATE TABLE `students` (
  `student_id` char(10) NOT NULL,
  `student_list_id` char(5) NOT NULL DEFAULT 'SL001',
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
('STU0000001', 'SL001', 'John Doe', '2000-01-15', 'Male', '123 Main St', '1234567890', 3.5),
('STU0000002', 'SL001', 'Jane Doe', '2001-05-22', 'Female', '456 Oak St', '9876543210', 3.8),
('STU0000003', 'SL001', 'Bob Smith', '1999-11-10', 'Male', '789 Pine St', '5551112222', 3.2),
('STU0000004', 'SL001', 'Alice Johnson', '2002-03-08', 'Female', '101 Elm St', '7778889999', 3.6),
('STU0000005', 'SL001', 'Charlie Brown', '1998-07-03', 'Male', '202 Maple St', '4443335555', 3.9),
('STU0000006', 'SL001', 'Eva Martinez', '2003-09-18', 'Female', '303 Cedar St', '6669990000', 3.1),
('STU0000007', 'SL001', 'David Wilson', '2000-12-25', 'Male', '404 Birch St', '2223334444', 3.7),
('STU0000008', 'SL001', 'Grace Lee', '1997-04-30', 'Female', '505 Walnut St', '1112223333', 3.4),
('STU0000009', 'SL001', 'Sam Johnson', '2001-08-12', 'Male', '606 Pine St', '9998887777', 3),
('STU0000010', 'SL001', 'Linda Davis', '1999-02-28', 'Female', '707 Oak St', '4445556666', 3.45);

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
-- Indexes for table `certificate`
--
ALTER TABLE `certificate`
  ADD KEY `FK_Certificate_Student` (`student_id`),
  ADD KEY `FK_Certificate_Classificate` (`certificate_id`);

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
-- Constraints for table `certificate`
--
ALTER TABLE `certificate`
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
