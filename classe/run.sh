# Capture the output of the above commands and save it to 'result.txt'
{ 
  rm ./dump -r
  mkdir ./dump
  javac Main.java -d ./dump
  jar -cf ./dump/Main.jar ./dump/Main.class
  cd ./dump
  for i in {2..10}; do
    cp Main.class "Main${i}.class"
  done
  sleep 3
  java Main Main ./ -p
  cd ..
} > result.txt 2>&1
