#!/bin/bash

cd ~/Projet_GL/

resultat=" "
echo "Chargement des tests ..."
#mvn clean > resultat
#mvn test-compile > resultat

#rm resultat
echo "-------------- TESTS INVALIDES --------------"
for file in `ls src/test/deca/syntax/invalid/unprovided/*.deca`
do
    decac -p $file >/dev/null 2>&1; resultat=$?
    if [ $resultat -eq 0 ]; then #il faut qu'une erreur soit levée
    	echo "FAILED : Test du fichier $file"
    elif [ $resultat -eq 1 ]; then
    	echo "PASSED : Test du fichier $file"
    else		echo "TEST ERROR : contactez le chef d'équipe"
    fi

done
echo "-------------- FIN TESTS INVALIDES --------------"
echo ""
cd src/test/deca/syntax/valid/unprovided/input/
echo "-------------- TESTS VALIDES --------------"
for file in *.deca
do
    decac -p $file > ~/Projet_GL/outputfinal.txt 2> /dev/null
    diff -w ~/Projet_GL/outputfinal.txt ../expectedOutput/$file  2>&1; resultat=$?

    if [ $resultat -eq 0 ]; then
    	echo "PASSED : Test du fichier $file"
    elif [ $resultat -eq 1 ]; then
    	echo "FAILED : Test du fichier $file"
    else		echo "TEST ERROR : contactez le chef d'équipe"
    fi

done
echo "-------------- FIN TESTS VALIDES --------------"
cd ~/Projet_GL
rm outputfinal.txt
