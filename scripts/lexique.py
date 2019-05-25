import sys
import csv
typologie = sys.argv[1]

lexique = csv.reader(open('lexique-dicollecte-fr-v6.4.1-simple.txt'), delimiter='\t')
dictionnary = {}

for row in lexique:
    if typologie in row[2].split(" "):
        flexion = row[0]
        lemme = row[1]
        if lemme in dictionnary:
            flexions = dictionnary[lemme]
        else:
            flexions = []
        flexions.append(flexion)
        dictionnary[lemme] = flexions

d = open('{}Dic.txt'.format(typologie), 'w')
for lemme in dictionnary:
    d.write('{}==={}\n'.format(lemme, ';'.join(dictionnary[lemme])))

