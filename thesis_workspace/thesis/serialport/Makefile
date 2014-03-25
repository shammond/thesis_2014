BIN 	 = paramdecode.o paramcheck.o \
           rxdelay.o \
           serialport.o spd_message.o sysdependent.o

CMDS	 = spd spc
CONF	 = spd.conf
TESTS	 = testspd sptarget

ETC	 = /usr/local/etc
EXEC	 = /usr/local/bin
EXT      = /usr/java/sdk/jre/lib/ext
INITD	 = /etc/init.d
INITL	 = init.d
INICMD	 = serialport
MAN      = /usr/local/man/man1
PKG      = SerialPort
JAR	 = $(PKG).jar
JCLASS	 = TestSPD.class
DOCS	 = docs
CDOCS    = $(DOCS)/c
CDOCIDX  = $(CDOCS)/index.html
JDOCS	 = $(DOCS)/java
JDOCIDX  = $(JDOCS)/index.html
JTDOCS	 = $(DOCS)/jterm
JTDOCIDX = $(JTDOCS)/index.html
LIB	 = libsp.a
LIBT	 = /usr/local/lib
LIBD	 = -L/$(LIBT) -L.
SPCOBJS  = spc.o
SPDOBJS  = spd.o spd_message.o spd_cmd_handler.o spd_error.o \
	   port.o session.o sysdependent.o

CFLAGS 	 = -c -I.
LDFLAGS  = $(LIBD) -lsp -lenviron


all:	dirs $(LIB) $(CMDS) $(TESTS) jinterface $(JCLASS) jterminal

clean:	
	rm -f *.o *.class *.log $(CMDS) $(LIB) port[0-9].txt 
	rm -f $(CDOCS)/*.html
	rm -f $(JDOCS)/*.html $(JDOCS)/*.css $(JDOCS)/package-list
	rm -rf $(JDOCS)/resources $(JDOCS)/$(PKG)/*.html
	cd $(PKG); make clean
	cd jterm; make clean

dirs:	
	setup

docs:	$(CDOCIDX) $(JDOCIDX) $(JTDOCIDX)

install:	
	cp $(JAR) $(EXT)
	cp $(LIB) $(LIBT)
	chmod uga+x $(CMDS)
	cp $(CMDS) $(EXEC)
	chmod uga+r $(CONF)
	cp $(CONF) $(ETC)
	chmod uga+r $(DOCS)/*.1
	cp $(DOCS)/*.1 $(MAN)
	cp $(INITL)/$(INICMD) $(INITD)


jinterface:	
	cd $(PKG); make all

jterminal:	
	cd jterm; make all

$(CDOCIDX): *.[ch]
	cd $(CDOCS); java cdoc -DLINUX ../../*.h

$(JDOCIDX): $(PKG)/*.java
	cd $(JDOCS); javadoc ../../$(PKG)/*.java

$(JTDOCIDX): jterm/*.java
	cd $(JTDOCS); javadoc ../../jterm/*.java

$(JAR):	$(PKG)/*.java
	rm -f $(JAR)
	jar -cvf $(JAR) $(PKG)/*.class

$(LIB): $(BIN)
	ar -r $(LIB) $(BIN)

TestSPD.class:		TestSPD.java $(JAR)
	javac TestSPD.java

port.o: port.c spd.h
	cc port.c $(CFLAGS) 

paramcheck.o: paramcheck.c paramcheck.h
	cc paramcheck.c $(CFLAGS) 

paramdecode.o: paramdecode.c paramdecode.h
	cc paramdecode.c $(CFLAGS) 

rxdelay.o: rxdelay.c rxdelay.h
	cc rxdelay.c $(CFLAGS) 

serialport.o:	serialport.c serialport.h spd_message.h spd_codes.h
	cc $(CFLAGS) serialport.c

session.o: session.c spd.h
	cc session.c $(CFLAGS) 

spc: $(SPCOBJS)
	cc $(SPCOBJS) $(LDFLAGS) -o spc

spc.o: spc.c spc.h
	cc spc.c $(CFLAGS) 

spd: $(SPDOBJS) $(LIB)
	cc $(SPDOBJS) $(LDFLAGS) -o spd

spd.o: spd.c spd.h
	cc spd.c $(CFLAGS) 

spd_cmd_handler.o: spd_cmd_handler.c spd.h spd_message.h spd_codes.h
	cc spd_cmd_handler.c $(CFLAGS)

spd_error.o: spd_error.c spd.h
	cc spd_error.c $(CFLAGS) 

spd_message.o:	spd_message.c spd_message.h
	cc $(CFLAGS) spd_message.c

sptarget: sptarget.o $(LIB)
	cc sptarget.o $(LDFLAGS) -o sptarget

sptarget.o: sptarget.c sptarget.h
	cc $(CFLAGS) sptarget.c

sysdependent.o: sysdependent.c sysdependent.h
	cc $(CFLAGS) sysdependent.c

testspd: testspd.o $(LIB) 
	cc testspd.o $(LDFLAGS) -o testspd

testspd.o: testspd.c testspd.h serialport.h spd_message.h
	cc $(CFLAGS) testspd.c


