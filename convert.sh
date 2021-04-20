#!/bin/bash
#folders=('DistrGenAlg' 'DistrGenProg' 'GenAlg' 'GenProg')
folders=('examples/DistributedGeneticAlgorithm')
for folder in "${folders[@]}"; do
  for filename in ./"$folder"/*.java; do
    file=${filename%.java}
    ./build/java2python-0.5.1/bin/j2py "$filename" > "$file.py"
  done
done