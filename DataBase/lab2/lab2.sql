#lab2 - var 423534

SELECT 
	Н_ОЦЕНКИ.ПРИМЕЧАНИЕ,
	Н_ВЕДОМОСТИ.ДАТА 
FROM 
	Н_ВЕДОМОСТИ
RIGHT JOIN
	Н_ОЦЕНКИ
	ON Н_ОЦЕНКИ.КОД = Н_ВЕДОМОСТИ.ОЦЕНКА
WHERE 
	Н_ОЦЕНКИ.КОД > '4' AND
	Н_ВЕДОМОСТИ.ИД > 1250972 AND
	Н_ВЕДОМОСТИ.ИД = 1250981;

SELECT 
	Н_ЛЮДИ.ФАМИЛИЯ, 
	Н_ОБУЧЕНИЯ.НЗК,
	Н_УЧЕНИКИ.НАЧАЛО
FROM 
	Н_ОБУЧЕНИЯ
RIGHT JOIN
	Н_ЛЮДИ
	ON Н_ОБУЧЕНИЯ.ЧЛВК_ИД = Н_ЛЮДИ.ИД
RIGHT JOIN
	Н_УЧЕНИКИ
	ON Н_ОБУЧЕНИЯ.ЧЛВК_ИД = Н_УЧЕНИКИ.ЧЛВК_ИД
WHERE 
	Н_ЛЮДИ.ИМЯ > 'Александр' AND
	Н_ОБУЧЕНИЯ.ЧЛВК_ИД < 113409;

SELECT EXISTS (
    SELECT 1
    FROM Н_ЛЮДИ
    JOIN Н_УЧЕНИКИ s ON s.ЧЛВК_ИД = Н_ЛЮДИ.ИД
    JOIN Н_ГРУППЫ_ПЛАНОВ gr ON gr.ГРУППА = s.ГРУППА
    JOIN Н_ПЛАНЫ pl ON pl.ПЛАН_ИД = gr.ПЛАН_ИД
    JOIN Н_ОТДЕЛЫ ON Н_ОТДЕЛЫ.ИД = pl.ОТД_ИД
    WHERE Н_ОТДЕЛЫ.ИД = 703
      AND Н_ЛЮДИ.ИНН IS NULL
) AS есть_студенты_без_инн;




SELECT 
    s.ГРУППА,
    COUNT(DISTINCT s.ЧЛВК_ИД) AS КОЛИЧЕСТВО_СТУДЕНТОВ
FROM 
	Н_УЧЕНИКИ AS s
JOIN Н_ПЛАНЫ p ON p.ПЛАН_ИД = s.ПЛАН_ИД
JOIN Н_УЧЕБНЫЕ_ГОДА uch ON uch.УЧЕБНЫЙ_ГОД = p.УЧЕБНЫЙ_ГОД
WHERE p.ОТД_ИД_ЗАКРЕПЛЕН_ЗА = 102 
  AND uch.УЧЕБНЫЙ_ГОД = '2011/2012'
GROUP BY s.ГРУППА
HAVING COUNT(DISTINCT s.ЧЛВК_ИД) > 10
ORDER BY КОЛИЧЕСТВО_СТУДЕНТОВ;


SELECT 
	s.ГРУППА,
	ROUND(AVG(DATE_PART('year', AGE(CURRENT_DATE, p.ДАТА_РОЖДЕНИЯ)))) AS ВОЗРАСТ
FROM
	Н_ЛЮДИ AS p
JOIN Н_УЧЕНИКИ AS s
	ON p.ИД = s.ЧЛВК_ИД
GROUP BY s.ГРУППА
HAVING ((AVG(DATE_PART('year', AGE(CURRENT_DATE, p.ДАТА_РОЖДЕНИЯ))))) < (SELECT MIN(DATE_PART('year', AGE(CURRENT_DATE, p.ДАТА_РОЖДЕНИЯ))) FROM Н_ЛЮДИ AS p JOIN Н_УЧЕНИКИ AS s ON p.ИД = s.ЧЛВК_ИД WHERE s.ГРУППА = '1101')
ORDER BY ВОЗРАСТ;



SELECT 
	s.ГРУППА AS номер_группы,
	s.ИД AS номер_ученика,
	l.ФАМИЛИЯ AS фамилия_студента,
	l.ИМЯ AS имя_студента,
	l.ОТЧЕСТВО AS отчество_студента,
	s.ПЛАН_ИД AS номер_приказа,
	s.СОСТОЯНИЕ AS состояние_приказа
FROM Н_ЛЮДИ AS l
INNER JOIN 
	Н_УЧЕНИКИ AS s
	ON s.ЧЛВК_ИД = l.ИД
INNER JOIN
	Н_ПЛАНЫ AS p
	ON p.ИД = s.ПЛАН_ИД
INNER JOIN 
	Н_УЧЕБНЫЕ_ГОДА AS y
	ON p.УЧЕБНЫЙ_ГОД = y.УЧЕБНЫЙ_ГОД
WHERE p.ФО_ИД = 1 AND p.КУРС = 1 AND y.УЧЕБНЫЙ_ГОД = '2012/2013' AND s.НАЧАЛО > '2012-09-01 00:00:00';




SELECT 
	p1.ДАТА_РОЖДЕНИЯ AS ДР_1_ГРУППА,
	p2.ДАТА_РОЖДЕНИЯ AS ДР_2_ГРУППА,
	p1.ИМЯ AS ИМЯ,
	p1.ФАМИЛИЯ AS ФАМИЛИЯ,
	p1.ОТЧЕСТВО AS ОТЧЕСТВО
FROM
	Н_ЛЮДИ p1
JOIN 
	Н_ЛЮДИ p2 ON
	p1.ИМЯ = p2.ИМЯ
	AND p1.ФАМИЛИЯ = p2.ФАМИЛИЯ
	AND p1.ОТЧЕСТВО = p2.ОТЧЕСТВО
	AND p2.ДАТА_РОЖДЕНИЯ <> p1.ДАТА_РОЖДЕНИЯ;



#lab2 - var 7535



SELECT                            
        Н_ОЦЕНКИ.КОД,
        Н_ВЕДОМОСТИ.ДАТА 
FROM 
        Н_ВЕДОМОСТИ
INNER JOIN
        Н_ОЦЕНКИ
        ON Н_ОЦЕНКИ.КОД = Н_ВЕДОМОСТИ.ОЦЕНКА
WHERE 
        Н_ОЦЕНКИ.КОД < '2' AND
        Н_ВЕДОМОСТИ.ИД < 1250981;



	


SELECT 
        Н_ЛЮДИ.ОТЧЕСТВО, 
        Н_ВЕДОМОСТИ.ИД,
        Н_СЕССИЯ.ИД
FROM 
        Н_ЛЮДИ
LEFT JOIN
        Н_ВЕДОМОСТИ
        ON Н_ВЕДОМОСТИ.ЧЛВК_ИД = Н_ЛЮДИ.ИД
LEFT JOIN
        Н_СЕССИЯ
        ON Н_ВЕДОМОСТИ.СЭС_ИД = Н_СЕССИЯ.СЭС_ИД
WHERE 
        Н_ЛЮДИ.ИМЯ > 'Ярослав' AND
        Н_ВЕДОМОСТИ.ДАТА < '1998-01-05' AND
        Н_СЕССИЯ.УЧГОД < '2003/2004';






SELECT 
	COUNT(*)
FROM 	
	(SELECT ФАМИЛИЯ, ИМЯ FROM Н_ЛЮДИ GROUP BY ФАМИЛИЯ, ИМЯ);





SELECT 
	ПЛАН_ИД
FROM(SELECT 
	gr.ПЛАН_ИД,
	COUNT(gr.ГРУППА) AS КОЛИЧЕСТВО_ГРУПП
FROM
	Н_ГРУППЫ_ПЛАНОВ gr
FULL OUTER JOIN 
	Н_ПЛАНЫ AS p
	ON p.ПЛАН_ИД = gr.ПЛАН_ИД
WHERE p.ФО_ИД = 3
GROUP BY gr.ПЛАН_ИД)
WHERE КОЛИЧЕСТВО_ГРУПП = 2
ORDER BY КОЛИЧЕСТВО_ГРУПП;



SELECT 
    s.ГРУППА,
    ROUND(AVG(DATE_PART('year', AGE(CURRENT_DATE, p.ДАТА_РОЖДЕНИЯ)))) AS СРЕД_ВОЗРАСТ
FROM
    Н_ЛЮДИ AS p
JOIN Н_УЧЕНИКИ AS s
    ON p.ИД = s.ЧЛВК_ИД
GROUP BY 
	s.ГРУППА
HAVING 
	AVG(DATE_PART('year', AGE(CURRENT_DATE, p.ДАТА_РОЖДЕНИЯ))) > (SELECT MIN(DATE_PART('year', AGE(CURRENT_DATE, p.ДАТА_РОЖДЕНИЯ))) FROM Н_ЛЮДИ AS p JOIN Н_УЧЕНИКИ AS s ON p.ИД = s.ЧЛВК_ИД WHERE s.ГРУППА = '1101')
ORDER BY 
	СРЕД_ВОЗРАСТ;


SELECT 
    s.ГРУППА AS номер_группы,
    s.ИД AS номер_ученика,
    l.ФАМИЛИЯ AS фамилия_студента,
    l.ИМЯ AS имя_студента,
    l.ОТЧЕСТВО AS отчество_студента,
    s.ПЛАН_ИД AS номер_приказа,
    s.СОСТОЯНИЕ AS состояние_приказа
FROM Н_ЛЮДИ AS l
JOIN Н_УЧЕНИКИ AS s
	ON l.ИД = s.ЧЛВК_ИД
WHERE EXISTS(SELECT 1 FROM Н_ПЛАНЫ AS p  
JOIN 
    Н_УЧЕБНЫЕ_ГОДА AS y
    ON p.УЧЕБНЫЙ_ГОД = y.УЧЕБНЫЙ_ГОД
WHERE
    p.ФО_ИД IN (1, 2)
	AND p.КУРС = 1
	AND y.УЧЕБНЫЙ_ГОД = '2011/2012' 
	AND s.НАЧАЛО > '2011-09-01 00:00:00')
ORDER BY 
	s.ГРУППА, 
    l.ФАМИЛИЯ, 
    l.ИМЯ;


SELECT
	COUNT(s.ИД)
FROM 
	Н_УЧЕНИКИ AS s
LEFT JOIN 
	Н_ВЕДОМОСТИ AS ved
	ON ved.ЧЛВК_ИД = s.ЧЛВК_ИД
WHERE s.ГРУППА = '3101' AND ved.ОЦЕНКА = '3';













