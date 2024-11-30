#!/usr/bin/bash
#1 Рекурсивно подсчитать количество символов содержимого файлов из директории lab0, имя которых начинается на 'g', результат записать в файл в директории /tmp, ошибки доступа не подавлять и не перенаправлять
cd lab0; mkdir tmp
cd tmp; touch res.log
cd ..

find . -type f -name "g*" -exec cat -n {} + | wc -m > ./tmp/res.log
echo "\n#1 successfully\n"

#2 Вывести три первых элемента рекурсивного списка имен и атрибутов файлов в директории lab0, начинающихся на символ 'g', список отсортировать по убыванию количества жестких ссылок, ошибки доступа перенаправить в файл в директории /tmp

find . -type f -name "g*" -exec ls -lR {} \; | sort -k 8nr | head -n 3 2>./tmp/err.log
echo "\n#2 successfully\n"

#3 Вывести содержимое файлов в директории golduck5, строки отсортировать по имени a->z, ошибки доступа не подавлять и не перенаправлять
echo "\n#3"
find golduck5 -type f -exec cat {} + | sort
echo "\n#3 successfully\n"
#4 Вывести три первых элемента рекурсивного списка имен и атрибутов файлов в директории lab0, заканчивающихся на символ 'g', список отсортировать по возрастанию даты изменения записи о файле, ошибки доступа не подавлять и не перенаправлять
echo "\n#4"
find . -type f -name "*g" -exec ls -lt {} + | sort -k 6,7M -k 8n | head -n 3
echo "#4 successfully\n"

#5 Подсчитать количество строк содержимого файлов: buizel, klinklang, отсортировать вывод по уменьшению количества, подавить вывод ошибок доступа
echo "#5\n"
wc -l golduck5/buizel yanma0/klinklang yanma0/gastrodon/yanma0/klinklang 2>/dev/null | sort -nr | tail -n 3
echo "\n#5 successfully"

#6 Подсчитать количество символов содержимого файлов: deerling, buizel, результат записать в файл в директории /tmp, ошибки доступа перенаправить в файл в директории /tmp

 wc -m ./ekans2/deerling ./golduck5/buizel | head -n 2 > ./tmp/res6.log 2>./tmp/error6.log
