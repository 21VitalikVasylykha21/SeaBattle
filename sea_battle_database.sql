-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Хост: 127.0.0.1
-- Время создания: Июн 10 2021 г., 00:00
-- Версия сервера: 10.4.17-MariaDB
-- Версия PHP: 8.0.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `sea_battle_database`
--

-- --------------------------------------------------------

--
-- Структура таблицы `instructions_for_game`
--

CREATE TABLE `instructions_for_game` (
  `information` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `instructions_for_game`
--

INSERT INTO `instructions_for_game` (`information`) VALUES
('Свістаи всіх на палубу, адмірал іде. КАРРР!!! Вітаю тебе салага на моєму кораблі. Від сьогодні й до кінця твоєї служби на моєму флоті ти підкоряєся мені. До того моменту як ти став на мою палубу ти був сухопутною крисою, але ти вже на моїй палубі, тому САЛАГА, ти підвищений! Щоб далі піднятися в флоті, й отримати свою флотилію тобі необхідно боротися за свою честь і бали, тим замим отримуючи нові звання. За кожний виіграш ти будеш отримувати 20 балів,а за проіграш втрачати їх. Щоб ти не був, як корабель на морі без паруса, то можеш подивитися в рейтингу гравців хто твій найближчий ворог і наздоганяти його. Хочеш дізнатися скільки очків тобі залишилося до наступного звання заходи в свою каюту і вивчай. Все Салага, попутного вітру тобі в бою!');

-- --------------------------------------------------------

--
-- Структура таблицы `rank_player`
--

CREATE TABLE `rank_player` (
  `rank` varchar(500) NOT NULL,
  `counter_rank` int(11) NOT NULL,
  `info` varchar(1000) NOT NULL,
  `image_rank` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `rank_player`
--

INSERT INTO `rank_player` (`rank`, `counter_rank`, `info`, `image_rank`) VALUES
('Адмірал', 300, 'Найвище військове звання вищого офіцерського складу', 'admiral'),
('Головний корабельний старшина', 80, 'Військове звання сержантського і старшинського складу складу ', 'sergeant'),
('Капітан', 160, 'Військове звання старшого офіцерського складу, відповідає званню полковника в сухопутних військах', 'captain'),
('Командор', 200, 'Військове звання вищого офіцерського складу.', 'commander'),
('Лейтенант', 130, 'Військове звання молодшого офіцерського складу', 'lieutenant'),
('Матрос', 10, 'Військове звання у військово-морських силах', 'sailor'),
('Салага', 0, 'Найменше звання в морському флоті', 'salaga'),
('Старшина', 50, 'Військове звання у військово-морських силах', 'first sergeant');

-- --------------------------------------------------------

--
-- Структура таблицы `user`
--

CREATE TABLE `user` (
  `name` varchar(500) NOT NULL,
  `password` varchar(1000) NOT NULL,
  `rank` varchar(300) NOT NULL,
  `counter_poins` int(255) NOT NULL,
  `counter_battle` int(255) NOT NULL,
  `counter_wins` int(255) NOT NULL,
  `counter_lose` int(255) NOT NULL,
  `image` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `user`
--

INSERT INTO `user` (`name`, `password`, `rank`, `counter_poins`, `counter_battle`, `counter_wins`, `counter_lose`, `image`) VALUES
('VVVV', '6eebb9de075c027566b48283ffc2b8a8b04d39adb1e6c52040010348b1d05445', 'Командор', 200, 12, 10, 2, 'wallhaven-73w26e.png');

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `rank_player`
--
ALTER TABLE `rank_player`
  ADD PRIMARY KEY (`rank`);

--
-- Индексы таблицы `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`name`),
  ADD KEY `rank` (`rank`);

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `user_ibfk_1` FOREIGN KEY (`rank`) REFERENCES `rank_player` (`rank`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
