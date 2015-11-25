#!/bin/bash
BASEDIR=`pwd`
echo $BASEDIR
cd src/test/java
find . -name "*Test.java" \
    | sed -e "s/\.java//" \
          -e "s=./==" \
          -e "s=/=.=g" \
    | xargs java -classpath $BASEDIR/target/test-classes/:$BASEDIR/target/classes/:$BASEDIR/lib/hamcrest-core-1.3.jar:$BASEDIR/lib/junit-4.12.jar:$BASEDIR/lib/mockito-all-1.8.4.jar org.junit.runner.JUnitCore
