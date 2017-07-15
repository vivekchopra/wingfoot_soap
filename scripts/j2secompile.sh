
if test -z ${WINGFOOT_HOME}
then
     echo "Please set the WINGFOOT_HOME variable"
     exit 0
fi

WINGFOOT_SOAP=${WINGFOOT_HOME}/wingfoot_soap
#CLASSPATH=../extern/j2se_kxml.jar:./src
#COMPILED_CLASSES=./build/j2se_classes
CLASSPATH=${WINGFOOT_SOAP}/src:${WINGFOOT_HOME}/wingfoot_parser/build/lib/j2se_kxml.jar
COMPILED_CLASSES=${WINGFOOT_SOAP}/build/j2se_classes


case $1 in

   compile)
      ## Create the j2se_classes directory.  This is a temp directory
      ## and is deleted at the end of the script
      mkdir ${WINGFOOT_SOAP}/build/j2se_classes 

      echo Compiling java classes....
      for javafile in `find ${WINGFOOT_SOAP}/src -type f -name '*.java' -print`
      do
          if test $javafile != "${WINGFOOT_SOAP}/src/com/wingfoot/soap/transport/HTTPTransport.java"
	  then
              echo $javafile
	      javac -g:none -classpath $CLASSPATH \
              -d $COMPILED_CLASSES $javafile
          else
	      echo "Skipping $javafile"
          fi
      done
      ;;

   obfuscate)
      cd $COMPILED_CLASSES
      jar -cvf classes.jar com/*
      java -classpath \
      ${WINGFOOT_HOME}/wingfoot_parser/build/lib/j2se_kxml.jar:${WINGFOOT_HOME}/wingfoot_extern/retroguard-v1.1/retroguard.jar \
      RetroGuard classes.jar j2sewsoap_1.06.jar ${WINGFOOT_HOME}/wingfoot_soap/build/scriptj2se.rgs
      mv j2sewsoap_1.06.jar ${WINGFOOT_HOME}/wingfoot_soap/build/lib
      rm -rf ${WINGFOOT_SOAP}/build/j2se_classes 
      cd  ${WINGFOOT_SOAP}
       ;; 

    clean)
      rm -f  ${WINGFOOT_SOAP}/build/lib/*.jar
      rm -rf ${WINGFOOT_SOAP}/build/tempjar
      ;;

   package)
      mkdir ${WINGFOOT_SOAP}/build/tempjar
      cp ${WINGFOOT_SOAP}/build/lib/j2sewsoap_1.06.jar ${WINGFOOT_SOAP}/build/tempjar
      cp ${WINGFOOT_HOME}/wingfoot_parser/build/lib/j2se_kxml.jar ${WINGFOOT_SOAP}/build/tempjar
      cd ${WINGFOOT_SOAP}/build/tempjar
      jar -xvf j2se_kxml.jar
      jar -xvf j2sewsoap_1.06.jar
      rm -rf META-INF
      jar -cvf j2sewsoap_1.06.jar com/* org/*
      cp j2sewsoap_1.06.jar ${WINGFOOT_SOAP}/build/lib
      cd ${WINGFOOT_SOAP}
      rm -rf ${WINGFOOT_SOAP}/build/tempjar
      ;;
   *)
      echo "usage compile.sh [compile|obfuscate|package|clean|usage]"
      ;;
esac
