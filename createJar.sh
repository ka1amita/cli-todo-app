#!/bin/bash
# https://stackoverflow.com/questions/9941296/how-do-i-make-a-jar-from-a-java-file
projectRoot="/Users/kalamita/coding/projects/cli-todo-app"
if [ $(pwd) != $projectRoot ]; then
  echo "execute from the project root!"
  exit 1
fi

jarName="cli-todo-app.jar"
compDir="build/cli-todo-app/"
targetDir="build"

rm -vrf "$compDir"
mkdir -p "$compDir"

javac -d "$compDir" $(find src -name "*.java")

cd $compDir || exit
jar cmvf ../../META-INF/MANIFEST.MF $jarName *
mv $jarName ../../$targetDir
cd - || exit

chmod u+x $targetDir/$jarName

echo "run with:"
echo "java -jar $targetDir/cli-todo-app.jar <commands>"
