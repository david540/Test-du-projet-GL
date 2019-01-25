#!/bin/bash

cd ~/Projet_GL/

echo "Chargement des tests ..."

#mvn clean > resultat
#mvn test-compile > resultat


cd src/test/deca/integration/input

for file in *.deca
do
    test_lex $file > /dev/null 2>&1; error=$?
    if [ $error -eq 1 ]; then
      echo "FAILED : $file"
      continue;
    fi
    test_synt $file > /dev/null 2>&1; error=$?
    if [ $error -eq 1 ]; then
      echo "FAILED : $file"
      continue;
    fi
    test_context $file > /dev/null 2>&1; error=$?
    if [ $error -eq 1 ]; then
      echo "FAILED : $file"
      continue;
    fi
    decac -p $file > /dev/null 2>&1; error=$?
    if [ $error -eq 1 ]; then
      echo "FAILED : $file"
      continue;
    fi
    decac -v $file > /dev/null 2>&1; error=$?
    if [ $error -eq 1 ]; then
      echo "FAILED : $file"
      continue;
    fi
    decac $file > /dev/null 2>&1; error=$?
    if [ $error -eq 1 ]; then
      echo "FAILED : $file"
      continue;
    fi
    ima ${file%.deca}.ass > ~/Projet_GL/outputfinal.txt 2>/dev/null
    rm ${file%.deca}.ass

    diff --ignore-all-space ~/Projet_GL/outputfinal.txt ../expected_output/${file%.deca}.txt 2>&1; resultat=$?
    if [ $resultat -eq 0 ]; then
    	echo "PASSED : $file"
    elif [ $resultat -eq 1 ]; then
    	echo "FAILED : $file"
    else		echo "TEST ERROR : contactez le chef d'équipe"
    fi

done

cd ~/Projet_GL/
decac src/test/deca/integration/test_manuel/comparaisons.deca > /dev/null 2>&1; error=$?
if [ $error -eq 0 ]; then
  for num in 1 2
  do
    ima src/test/deca/integration/test_manuel/comparaisons.ass < src/test/deca/integration/test_manuel/comparaisons_input$num.txt > outputfinal.txt 2>/dev/null

    diff --ignore-all-space outputfinal.txt src/test/deca/integration/test_manuel/comparaisons_output$num.txt 2>&1; resultat=$?
    if [ $resultat -eq 0 ]; then
      echo "PASSED : test_manuel/comparaisons.deca (entrée $num)"
    elif [ $resultat -eq 1 ]; then
      echo "FAILED : test_manuel/comparaisons.deca (entrée $num)"
    else		echo "TEST ERROR : contactez le chef d'équipe"
    fi
  done
fi
rm src/test/deca/integration/test_manuel/comparaisons.ass


cd ~/Projet_GL/
decac src/test/deca/integration/test_manuel/plusOuMoins.deca > /dev/null 2>&1; error=$?
if [ $error -eq 0 ]; then
  ima src/test/deca/integration/test_manuel/plusOuMoins.ass < src/test/deca/integration/test_manuel/plusOuMoins_input.txt > outputfinal.txt 2>/dev/null
  diff --ignore-all-space outputfinal.txt src/test/deca/integration/test_manuel/plusOuMoins_output.txt 2>&1; resultat=$?
  if [ $resultat -eq 0 ]; then
    echo "PASSED : test_manuel/plusOuMoins.deca"
  elif [ $resultat -eq 1 ]; then
    echo "FAILED : test_manuel/plusOuMoins.deca"
  else		echo "TEST ERROR : contactez le chef d'équipe"
  fi
fi

decac src/test/deca/integration/test_manuel/abstract_read_expr.deca > /dev/null 2>&1; error=$?
ima src/test/deca/integration/test_manuel/abstract_read_expr.ass < src/test/deca/integration/test_manuel/read_expr_input.txt > outputfinal.txt 2>/dev/null
diff --ignore-all-space outputfinal.txt src/test/deca/integration/test_manuel/read_expr_output.txt 2>&1; resultat=$?
if [ $resultat -eq 0 ]; then
  echo "PASSED : test_manuel/plusOuMoins.deca"
elif [ $resultat -eq 1 ]; then
  echo "FAILED : test_manuel/plusOuMoins.deca"
else		echo "TEST ERROR : contactez le chef d'équipe"
fi
rm src/test/deca/integration/test_manuel/abstract_read_expr.ass

rm outputfinal.txt
