JTCLASS	 = JTerm.class JTDisp.class JTDispAction.class \
	   JTHelpContent.class \
	   JTKeyListener.class \
	   JTInput.class JTList.class \
	   JTModel.class JTMouseListener.class \
	   JTReader.class \
	   JTScreen.class \
	   JTSerialAction.class JTSerialConnect.class \
	   JTServerAction.class JTServerConnect.class \
	   JTStringParser.class \
	   JTView.class
DOCS	 = ../docs
JTDOCS	 = $(DOCS)/jterm
JTDOCIDX = $(JTDOCS)/index.html

all:	$(JTCLASS) 

clean:	
	rm *.class
	rm -f $(JTDOCS)/*.html $(JTDOCS)/*.css $(JTDOCS)/package-list
	rm -rf $(JTDOCS)/resources

docs:	$(JTDOCIDX)

$(JTDOCIDX): *.java
	cd $(JTDOCS); javadoc ../../jterm/*.java

JTerm.class: JTerm.java JTDisp.class JTModel.class JTView.class
	javac JTerm.java

JTDisp.class: JTDisp.java JTDispAction.class \
	      JTInput.class \
	      JTKeyListener.class \
	      JTModel.class \
	      JTScreen.class
	javac JTDisp.java

JTDispAction.class: JTDispAction.java JTHelpContent.class JTModel.class \
		    JTSerialConnect.class JTServerConnect.class 
	javac JTDispAction.java

JTHelpContent.class: JTHelpContent.java
	javac JTHelpContent.java

JTKeyListener.class: JTKeyListener.java
	javac JTKeyListener.java

JTInput.class: JTInput.java
	javac JTInput.java

JTList.class: JTList.java JTStringParser.class
	javac JTList.java

JTModel.class: JTModel.java JTStringParser.class JTReader.class
	javac JTModel.java

JTMouseListener.class: JTMouseListener.java
	javac JTMouseListener.java

JTReader.class: JTReader.java
	javac JTReader.java

JTScreen.class: JTScreen.java
	javac JTScreen.java

JTSerialAction.class: JTSerialAction.java JTInput.class JTList.class \
	JTMouseListener.class
	javac JTSerialAction.java

JTSerialConnect.class: JTSerialConnect.java JTSerialAction.class \
        JTInput.class JTList.class JTMouseListener.class
	javac JTSerialConnect.java

JTServerAction.class: JTServerAction.java JTInput.class
	javac JTServerAction.java

JTServerConnect.class: JTServerConnect.java JTServerAction.class JTInput.class
	javac JTServerConnect.java

JTStringParser.class: JTStringParser.java
	javac JTStringParser.java

JTView.class: JTView.java JTDisp.class JTModel.class
	javac JTView.java


