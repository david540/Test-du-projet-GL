cd ~/Projet_GL/
resultat=" "

decac -n -r 10 -d -P -p -b src/test/deca/integration/input/programme_vide.deca
decac -n -r 10 -d -P -v -b src/test/deca/integration/input/programme_vide.deca

cd src/test/deca/integration/input/
echo "Chargement des tests en parallèle ..."

time decac -P *.deca
for file in *.ass
do
  mv $file ../$file
done

echo "Chargement des tests pas en parallèle ..."

time decac *.deca
for file in *.ass
do
    diff $file ../$file 2>&1; resultat=$?
    if [ $resultat -eq 1 ]; then
    	echo "L'output de decac -P est différent de celui de decac pour le fichier $file"
    fi
    rm ../$file
    rm $file
done
