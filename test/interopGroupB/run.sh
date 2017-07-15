#java -cp ~/wingfoot_development/extern/j2se_kxml.jar:.:/home/ksiyer/wingfoot_development/soap/build/lib/j2sewsoap_1.01.jar interopGroupB.InteropClient ./interopGroupB.xml
java -Djava.compiler=NONE -cp ${WINGFOOT_HOME}/wingfoot_soap/build/lib/j2sewsoap_1.04.jar:. interopGroupB.InteropClient ./interopGroupB.xml
#java -cp ~/wsoap1.02/wsoap/lib/j2sewsoap_1.02.jar:. interopGroupB.InteropClient ./interopGroupB.xml
#javac -classpath ${WINGFOOT_HOME}/wingfoot_soap/build/lib/j2sewsoap_1.03.jar:.  *.java
