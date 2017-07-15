
if test -z ${WINGFOOT_HOME}
then
     echo "Please set the WINGFOOT_HOME variable"
     exit 0
fi

BOOTCLASSPATH=${WINGFOOT_HOME}/wingfoot_extern/java/kvmclasses.jar
CLASSPATH=${WINGFOOT_HOME}/wingfoot_extern/java/midpclasses.jar:${WINGFOOT_HOME}/wingfoot_parser/build/lib/kvm_kxml.jar:${WINGFOOT_HOME}/wingfoot_soap/src
PREVERIFY=${WINGFOOT_HOME}/wingfoot_extern/java/kvm/bin/linux/preverify
JAVAC_OPTIONS="-g:none"
COMPILED_CLASSES=${WINGFOOT_HOME}/wingfoot_soap/build/classes
VERIFIED_DIR=${WINGFOOT_HOME}/wingfoot_soap/build/preverified_classes
LIB_DIR=${WINGFOOT_HOME}/wingfoot_soap/build/lib

case $1 in

   compile)
      mkdir ${WINGFOOT_HOME}/wingfoot_soap/build/classes
      mkdir ${WINGFOOT_HOME}/wingfoot_soap/build/preverified_classes
      echo Compiling java classes....
      for javafile in `find ${WINGFOOT_HOME}/wingfoot_soap/src -type f -name '*.java' -print`
      do
         if test $javafile != "${WINGFOOT_HOME}/wingfoot_soap/src/com/wingfoot/soap/transport/J2SEHTTPTransport.java"
	 then
              echo $javafile
	      javac -g:none  -bootclasspath \
               $BOOTCLASSPATH -classpath $CLASSPATH \
               -d $COMPILED_CLASSES $javafile
         else
	      echo "Skipping $javafile"
         fi
      done
      ;;

   preverify)
      echo Preverifying classes....
      $PREVERIFY -d $VERIFIED_DIR -classpath $CLASSPATH $COMPILED_CLASSES
      cd $VERIFIED_DIR
      jar -cvf kvmwsoap_1.05.jar com/*
      mv kvmwsoap_1.05.jar ${LIB_DIR}
      cd ${WINGFOOT_HOME}/wingfoot_soap
      rm -rf  ${WINGFOOT_HOME}/wingfoot_soap/build/classes
      rm -rf ${WINGFOOT_HOME}/wingfoot_soap/build/preverified_classes
      ;;
   prc)
      echo Creating PRC...
      java -cp ../extern/Converter.jar:. \
      com.sun.midp/palm.database.MakeMIDPApp -name HelloMidlet \
      -o HelloMidlet.prc -creator 1001 -jad ./HelloMidlet.jad \
      ${LIB_DIR}/HelloMidlet.jar
      ;;
   clean)
      rm -f ${WINGFOOT_HOME}/wingfoot_soap/build/lib/*.jar
      ;;
   obfuscate)
      cd $COMPILED_CLASSES
      jar -cvf classes.jar com/*
      java -classpath \
       ${WINGFOOT_HOME}/wingfoot_parser/build/lib/kvm_kxml.jar:.:${WINGFOOT_HOME}/wingfoot_extern/retroguard-v1.1/retroguard.jar \
      RetroGuard classes.jar kvmob.jar ${WINGFOOT_HOME}/wingfoot_soap/build/scriptkvm.rgs
      rm -rf com
      jar -xvf kvmob.jar
      rm classes.jar
      rm kvmob.jar
      cd ${WINGFOOT_HOME}/wingfoot_parser
      ;;
   package)
      mkdir ${WINGFOOT_HOME}/wingfoot_soap/build/tempjar
      cp ${WINGFOOT_HOME}/wingfoot_soap/build/lib/kvmwsoap_1.05.jar ${WINGFOOT_HOME}/wingfoot_soap/build/tempjar
      cp ${WINGFOOT_HOME}/wingfoot_parser/build/lib/kvm_kxml.jar ${WINGFOOT_HOME}/wingfoot_soap/build/tempjar
      cd ${WINGFOOT_HOME}/wingfoot_soap/build/tempjar
      jar -xvf kvm_kxml.jar
      jar -xvf kvmwsoap_1.05.jar
      rm -rf META-INF
      jar -cvf kvmwsoap_1.05.jar com/* org/*
      cp kvmwsoap_1.05.jar ${WINGFOOT_HOME}/wingfoot_soap/build/lib
      cd ${WINGFOOT_HOME}/wingfoot_soap
      rm -rf ${WINGFOOT_HOME}/wingfoot_soap/build/tempjar
      ;;
   *)
      echo "usage compile.sh [compile|obfuscate|preverify|package|prc|clean|usage]"
      ;;
esac
