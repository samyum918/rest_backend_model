CREATE TABLE IF NOT EXISTS `USER` (
ID INTEGER NOT NULL AUTO_INCREMENT,
ADDRESS1 VARCHAR(255),
ADDRESS2 VARCHAR(255),
BIRTH DATE,
CREATE_TIME DATETIME,
NAME VARCHAR(255) NOT NULL,
PHONE VARCHAR(255),
UPDATE_TIME DATETIME,
PRIMARY KEY (ID));

INSERT INTO `user` (`id`, `create_time`, `update_time`, `address1`, `address2`, `birth`, `name`, `phone`) VALUES
(1, '2021-07-04 03:28:00', '2021-07-04 03:28:00', 'address1', NULL, '1995-01-02', 'abc', '12345678'),
(2, '2021-07-04 03:31:11', '2021-07-04 03:31:11', 'address1', NULL, '1995-02-01', 'def', '22345678'),
(3, '2021-07-16 15:49:58', '2021-07-16 15:49:58', 'addressAAA', NULL, '1996-02-01', 'aaa', '22345678'),
(4, '2021-07-16 15:50:34', '2021-07-16 15:50:34', 'addressBBB', 'address2', '1997-02-01', 'bbb', '11345678'),
(5, '2021-07-16 15:50:48', '2021-07-16 15:50:48', 'addressCCC', 'address2', '1998-02-01', 'ccc', '33345678'),
(6, '2021-07-16 15:51:05', '2021-07-16 15:51:05', 'addressDDD', 'address2', '1999-02-01', 'ddd', '45345678'),
(7, '2021-07-16 15:51:16', '2021-07-16 15:51:16', 'addressEEE', 'address2', '2000-02-01', 'eee', '55345678'),
(8, '2021-07-16 15:51:26', '2021-07-16 15:51:26', 'addressFFF', 'address2', '2001-03-01', 'fff', '65345678');