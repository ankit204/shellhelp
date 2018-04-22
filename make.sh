#!/bin/bash

if [ "$1" == "test" ]; then
    echo "Runnig tests..."
    java -cp .:lib/lucene-analyzers-common-6.3.0.jar:lib/lucene-backward-codecs-6.3.0.jar:lib/lucene-core-6.3.0.jar:lib/lucene-queryparser-6.3.0.jar runTests
else
    rm -f PreProcessData/*.class *.class Index/*.class Search/*.class Classes/*.class

    javac -cp lib/lucene-analyzers-common-6.3.0.jar:lib/lucene-backward-codecs-6.3.0.jar:lib/lucene-core-6.3.0.jar:lib/lucene-queryparser-6.3.0.jar Index.java Indexing/*.java Classes/*.java PreProcessRepo.java PreProcessData/*.java shellhelp.java Search/*.java runTests.java

    java PreProcessRepo

    java -cp .:lib/lucene-analyzers-common-6.3.0.jar:lib/lucene-backward-codecs-6.3.0.jar:lib/lucene-core-6.3.0.jar:lib/lucene-queryparser-6.3.0.jar Index

    java -cp .:lib/lucene-analyzers-common-6.3.0.jar:lib/lucene-backward-codecs-6.3.0.jar:lib/lucene-core-6.3.0.jar:lib/lucene-queryparser-6.3.0.jar runTests
fi

