-- phpMyAdmin SQL Dump
-- version 5.0.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 15, 2022 at 03:29 PM
-- Server version: 10.4.14-MariaDB
-- PHP Version: 7.2.34

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `electrogrid`
--

-- --------------------------------------------------------

--
-- Table structure for table `billing`
--

CREATE TABLE `billing` (
  `ID` int(10) NOT NULL,
  `Account_No` varchar(20) NOT NULL,
  `Name` varchar(100) NOT NULL,
  `Address` varchar(100) NOT NULL,
  `From_Date` date NOT NULL,
  `Previous_Reading` int(15) NOT NULL,
  `To_Date` date NOT NULL,
  `Current_Reading` int(15) NOT NULL,
  `Units` int(11) NOT NULL,
  `Current_amount` decimal(7,2) NOT NULL,
  `Previous_amount` decimal(7,2) NOT NULL,
  `Total_amount` decimal(7,2) NOT NULL,
  `Status` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `billing`
--

INSERT INTO `billing` (`ID`, `Account_No`, `Name`, `Address`, `From_Date`, `Previous_Reading`, `To_Date`, `Current_Reading`, `Units`, `Current_amount`, `Previous_amount`, `Total_amount`, `Status`) VALUES
(201, '1234564641', 'tharu', 'watareka', '2022-04-10', 0, '2022-05-10', 100, 100, '1048.50', '0.00', '1048.50', 'Cancel'),
(202, '2147483647', 'tharaki', 'colombo', '2022-04-10', 0, '2022-05-11', 120, 120, '1603.50', '0.00', '1603.50', 'Cancel'),
(203, '2147483647', 'tharaki', 'colombo', '2022-04-11', 120, '2022-05-12', 150, 30, '235.50', '1603.50', '1839.00', 'Pending'),
(204, '4455611111', 'Anushka', 'Colombo', '2022-05-08', 0, '2022-06-10', 100, 100, '1048.50', '0.00', '1048.50', 'Pending'),
(205, '1234564641', 'tharu', 'watareka', '2022-05-02', 100, '2022-06-20', 210, 110, '1326.00', '1048.50', '2374.50', 'Cancel'),
(206, '1234564641', 'tharu', 'watareka', '2022-05-02', 210, '2022-06-20', 210, 0, '0.00', '2374.50', '2374.50', 'Done');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `userId` int(20) NOT NULL,
  `accountNo` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `address` varchar(100) NOT NULL,
  `NIC` varchar(10) NOT NULL,
  `email` varchar(30) NOT NULL,
  `phone` varchar(10) NOT NULL,
  `username` varchar(15) NOT NULL,
  `password` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`userId`, `accountNo`, `name`, `address`, `NIC`, `email`, `phone`, `username`, `password`) VALUES
(10, '2147483647', 'tharaki', 'colombo', '543454534V', 'kavi@gmail.com', '0718099026', 'kaviiiindi', 'kavi1256@'),
(11, '1234564641', 'tharu', 'watareka', '999211111V', 'tharu@gmail.com', '0718078065', 'tharu', 'tharu123'),
(12, '4455611111', 'Anushka', 'Colombo', '672343242V', 'anu@gmail.com', '0718888888', 'anushka123', 'anu@1232'),
(13, '1111111111111', 'sadi', 'matara', '77890769V', 'sadi@gmail.com', '0765569969', 'sadi', '123456');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `billing`
--
ALTER TABLE `billing`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`userId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `billing`
--
ALTER TABLE `billing`
  MODIFY `ID` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=207;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `userId` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
