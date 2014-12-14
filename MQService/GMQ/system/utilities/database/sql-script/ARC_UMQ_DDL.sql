DROP DATABASE umq;

CREATE DATABASE umq;
SET storage_engine=InnoDB;

USE umq;

CREATE TABLE `umq`.`Queue_Details` (
  `queueId` INT NOT NULL AUTO_INCREMENT,
  `exchangeName` CHAR(50) NOT NULL,
  `queueName` CHAR(50) NOT NULL,
  `maxAtmpt` INT NOT NULL,
  `nxtAtmptDly` BIGINT NOT NULL,
  `msgPriorityAtmpt` INT NOT NULL,
  `routingKey` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`queueId`),
  UNIQUE INDEX `queueName_UNIQUE` (`queueName` ASC));
  
insert into `umq`.`Queue_Details`(`exchangeName`,`queueName`,`maxAtmpt`,`nxtAtmptDly`,`msgPriorityAtmpt`,`routingKey`) values('00000','00000',0,0,0,'@@@@');
  

CREATE TABLE `umq`.`Client` (
  `clientId` VARCHAR(50) NOT NULL,
  `clientType` SMALLINT NOT NULL,
  `queueId` INT NOT NULL,
  `isActive` SMALLINT NOT NULL,
  `routingKey` VARCHAR(50) NOT NULL,
  `timeToLive` BIGINT NOT NULL,
  PRIMARY KEY (`clientId`),
  INDEX `queueId_idx` (`queueId` ASC),
  CONSTRAINT `queueId`
    FOREIGN KEY (`queueId`)
    REFERENCES `umq`.`Queue_Details` (`queueId`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE);

CREATE TABLE `umq`.`MsgChannel` (
  `clientId` VARCHAR(50) NOT NULL,
  `channelId` VARCHAR(50) NOT NULL,
  `createTime` TIMESTAMP(3) NOT NULL,
  `channelStatus` SMALLINT NOT NULL,
  `maxUpdatedTTL` BIGINT NOT NULL,
  `lastUpdatedTime` TIMESTAMP(3) NOT NULL,
  `lastMsgDlvrdTime` TIMESTAMP(3) NOT NULL,
  PRIMARY KEY (`channelId`),
  CONSTRAINT `clientId`
    FOREIGN KEY (`clientId`)
    REFERENCES `umq`.`Client` (`clientId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `umq`.`History` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `queueName` VARCHAR(50) NOT NULL,
  `clientId` VARCHAR(50) NOT NULL,
  `message` TEXT NOT NULL,
  `msgAttrName` CHAR(50) NULL,
  `msgAttrValue` CHAR(50) NULL,
  `status` SMALLINT NOT NULL,
  `parentId` VARCHAR(32) NULL,
  `loggingTime` DATETIME NOT NULL,
  `remark` TEXT NULL,
  PRIMARY KEY (`id`),
  INDEX `queueName_Namex` (`queueName` ASC),
  INDEX `clientId_idx` (`clientId` ASC));
    
 CREATE TABLE `umq`.`User` (
  `userName` VARCHAR(50) NOT NULL,
  `password` VARCHAR(50) NOT NULL,
  `status` SMALLINT NOT NULL,
  UNIQUE INDEX `userName_UNIQUE` (`userName` ASC));
  
insert into `umq`.`User`(`userName`,`password`,`status`) VALUES('admin','admin123',1);