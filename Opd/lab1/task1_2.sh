#!/bin/bash

#Создание древа каталогов

mkdir -p lab0/golduck5 lab0/yanma0
cd lab0
touch bellsprout4 dugtrio7 volcarona3
mkdir -p ekans2/lotad ekans2/hoppip; cd ekans2
touch deerling
cd ..
mkdir -p golduck5/klink golduck5/zubat golduck5/silcoon; cd golduck5
touch buizel
cd ..
mkdir -p yanma0/arbok yanma0/gastrodon yanma0/murkrow; cd yanma0
touch klinklang herdier
cd ..

#Наполнение файлов

echo "Развитые способности Gluttony" >> bellsprout4
echo -e "Тип покемона\nGROUND NONE" >> dugtrio7; cd ekans2
echo -e "Развитые способности Serene\nGrace" >> deerling; cd ..; cd golduck5
echo "satk=6 sdef=3 spd=9" >> buizel; cd ..
echo -e "Тип покемона BUG\nFIRE" >> volcarona3; cd yanma0
echo -e "Возможности Overland=1 Sky=9 Jump=1 Power=4\nIntelligence=5 Zapper=0 Sinker=0" >> klinklang
echo -e "Возможности Overland=7\nSurface=5 Jump=3 Power=3 Intelligence=4 Tracker=0" >> herdier
cd ..


#2 channge root


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





 
