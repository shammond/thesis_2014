# Define a variable for classpath
#CLASS_PATH = ../bin/thesis

# Define a virtual path for .class in the bin directory
#vpath %.class $(CLASS_PATH)

# $* matches the target filename without the extension
#thesis.ConnectionJNI.h : ConnectionJNI.class
#-sourcepath C:\Users\shammond\git\thesis_2014\thesis_workspace\thesis\src\thesis
#ConnectionJNI.class : ../src/thesis/ConnectionJNI.java
#	javac -g ../src/thesis/ConnectionJNI.java

all : connection.dll

# $@ matches the target, $< matches the first dependancy
connection.dll : ConnectionJNI.o ./lib/ABM_Datastreaming.lib ./lib/ABM_ThirdPartyCommunication.lib
	gcc -Wl,--add-stdcall-alias -shared -o $@  $< -D_STDCALL_SUPPORTED -D_M_IX86 -L./lib -lstdc++ -lABM_Datastreaming -lABM_ThirdPartyCommunication

# $@ matches the target, $< matches the first dependancy
ConnectionJNI.o : ConnectionJNI.cpp ConnectionJNI.h ./lib/ABM_Datastreaming.lib ./lib/ABM_ThirdPartyCommunication.lib
	gcc -I"C:\Program Files\Java\jdk1.7.0_25\include" -I"C:\Program Files\Java\jdk1.7.0_25\include\win32" -c $< -o $@ -L./lib -lABM_Datastreaming -lABM_ThirdPartyCommunication

ConnectionJNI.h : ../bin/thesis/ConnectionJNI.class
	javah -classpath ../bin thesis.ConnectionJNI
	
TestConnection.o : TestConnection.cpp
	gcc TestConnection.cpp -L./lib -lstdc++ 
	
clean :
	rm thesis_ConnectionJNI.h ConnectionJNI.o connection.dll