#!/bin/bash

cd ~/Projet_GL/

resultat=" "
echo "Chargement des tests ..."
#mvn clean > resultat
#mvn test-compile > resultat

#rm resultat

for file in `ls src/test/deca/context/invalid/unprovided/*.deca`
do
    decac -v $file >/dev/null 2>&1; resultat=$?
    if [ $resultat -eq 0 ]; then #il faut qu'une erreur soit levée
    	echo "FAILED : Test du fichier $file"
    elif [ $resultat -eq 1 ]; then
    	echo "PASSED : Test du fichier $file"
    else		echo "TEST ERROR : contactez le chef d'équipe"
    fi

done

for file in `ls src/test/deca/context/valid/unprovided/*.deca`
do
    decac -v $file >/dev/null 2>&1; resultat=$?
    if [ $resultat -eq 0 ]; then #il ne faut pas qu'une erreur soit levée
    	echo "PASSED : Test du fichier $file"
    elif [ $resultat -eq 1 ]; then
    	echo "FAILED : Test du fichier $file"
    else		echo "TEST ERROR : contactez le chef d'équipe sur le fichier $file"
    fi

done
