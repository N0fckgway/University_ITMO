#!/usr/bin/bash
chmod -R u+rw lab0
chmod 700 lab0
cd lab0


#1 скопировать содержимое файла dugtrio7 в новый файл lab0/golduck5/buizeldugtrio
cp dugtrio7 golduck5/buizeldugtrio


#2 создать символическую ссылку c именем Copy_28 на директорию ekans2 в каталоге lab0

ln -s lab0/ekans2 Copy28


#3 cоздать жесткую ссылку для файла dugtrio7 с именем lab0/yanma0/herdierdugtrio

ln dugtrio7 yanma0/herdierdugrtrio

#4 скопировать файл dugtrio7 в директорию lab0/golduck5/klink

chmod u+r dugtrio7
chmod u+rw golduck5/klink
cp dugtrio7 golduck5/klink
chmod u-r dugtrio7
chmod u-rw golduck5/klink

#5 cоздать символическую ссылку для файла volcarona3 с именем lab0/golduck5/buizelvolcarona

ln -s volcarona3 golduck5/buizelvolcarona

#6 объеденить содержимое файлов lab0/golduck5/buizel, lab0/golduck5/buizel, в новый файл lab0/volcarona3_68

cat golduck5/buizel golduck5/buizel > volcarona3_68


#7 скопировать рекурсивно директорию yanma0 в директорию lab0/yanma0/gastrodon

chmod u+r yanma0/herdierdugrtrio
cp -r yanma0 yanma0/gastrodon
chmod u-r yanma0/herdierdugrtrio


#upd chmod
chmod 400 bellsprout4
chmod 046 dugtrio7
chmod 524 ekans2; cd ekans2
chmod 711 lotad
chmod 537 hoppip
chmod 440 deerling; cd ..
chmod 333 golduck5; cd golduck5
chmod 333 klink
chmod 575 zubat
chmod 400 buizel
chmod 363 silcoon; cd ..
chmod 006 volcarona3
chmod 512 yanma0; cd yanma0
chmod 404 klinklang
chmod 751 arbok
chmod 440 herdier
chmod 571 gastrodon
chmod 753 murkrow; cd ..
