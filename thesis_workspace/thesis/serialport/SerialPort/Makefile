JCLASS	 = SerialPort.class SPDMessage.class
DOCS	 = ../docs
JDOCS	 = $(DOCS)/java
JDOCIDX  = $(JDOCS)/index.html

all:	$(JCLASS)

clean:	
	rm -f *.o *.class
	rm -f $(JDOCS)/*.html $(JDOCS)/*.css $(JDOCS)/package-list
	rm -rf $(JDOCS)/resources

docs:	$(JDOCIDX)

$(JDOCIDX): *.java
	cd $(JDOCS); javadoc ../../SerialPort/*.java

SerialPort.class:	SerialPort.java SPDMessage.class
	javac SerialPort.java

SPDMessage.class:	SPDMessage.java
	javac SPDMessage.java

