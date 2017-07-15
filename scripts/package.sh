# use this script to package WSOAP product

echo "Preparing Wingfoot SOAP runtime at $1"
WINGFOOT_SOAP=$WINGFOOT_HOME/wingfoot_soap
mkdir $1/wsoap
mkdir $1/wsoap/demos
#mkdir $1/wsoap/demos/xmethods
#mkdir $1/wsoap/demos/xmethods/stockquote
mkdir $1/wsoap/lib
mkdir $1/wsoap/doc

cp $WINGFOOT_SOAP/doc/Readme.txt $1/wsoap
cp $WINGFOOT_SOAP/doc/LICENSE $1/wsoap
cp $WINGFOOT_SOAP/doc/WSOAP_User_Guide1_04.pdf $1/wsoap/doc
cp $WINGFOOT_SOAP/doc/javadoc.zip $1/wsoap/doc
cp $WINGFOOT_SOAP/test/interop/interop.html $1/wsoap/doc
cp $WINGFOOT_SOAP/test/interopGroupB/interopGroupB.html $1/wsoap/doc

cp $WINGFOOT_SOAP/build/lib/*.jar $1/wsoap/lib

#cp ./demos/xmethods/stockquote/StockQuote.java $1/wsoap/demos/xmethods/stockquote
cp -r $WINGFOOT_SOAP/demos/xmethods/ $1/wsoap/demos/

