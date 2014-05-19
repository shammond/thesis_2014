/*
 * TestConnection.cpp
 *
 *  Created on: Apr 8, 2014
 *      Author: shammond
 */

#pragma comment(lib, "ABM_Datastreaming")
#include <stdio.h>
#include <string>

#include "include/TypeDefDataTypes.h"
#include "include/ThirdPartyCommunication.h"
#include "include/AbmTngrmDll.h"
#include <windows.h>

//#include "stdafx.h"
#include "Connection.h"
#include <math.h>
#include <iostream>

using namespace std;

typedef long (FAR PASCAL *OPEN_FUNCTION)(char*, int&, BOOL);
typedef long (FAR PASCAL *CLOSE_FUNCTION)(int);
typedef float* (FAR PASCAL *GETDATA_FUNCTION)(int&, int);

int m_nHandle;
string m_sIP;
HINSTANCE datastreaming;
OPEN_FUNCTION openFunction;
CLOSE_FUNCTION closeFunction;
GETDATA_FUNCTION getBrainState;

int main() {

	datastreaming = LoadLibrary("C:\\Users\\shammond\\git\\thesis_2014\\thesis_workspace\\thesis\\jni\\dllfiles\\ABM_Datastreaming.dll");

	if (!datastreaming) {
		cout << "could not load the dynamic library" << endl;
		return EXIT_FAILURE;
	}
	else
		cout << "loaded datastreaming lib" << endl;

	openFunction = (OPEN_FUNCTION)GetProcAddress(datastreaming, "OpenConnection");
	closeFunction = (CLOSE_FUNCTION)GetProcAddress(datastreaming, "CloseConnection");
	getBrainState = (GETDATA_FUNCTION)GetProcAddress(datastreaming, "AgetCWPC");

	if(openFunction == NULL || getBrainState == NULL)
	{
		FreeLibrary(datastreaming);
		MessageBox(NULL, "Unable to load function", "Error", MB_OK|MB_ICONERROR);
		return 0;
	}

	//const char *nativeIP = env->GetStringUTFChars(sIPAddress, 0);
	//m_sIP = nativeIP;

	//const char *nativePort = env->GetStringUTFChars(sPort, 0);
	//m_sIP = nativePort;

	//char* sConnectionString;
	//sConnectionString[sIPAddress] = ' ';
	//sprintf(sConnectionString, "%s %s", nativeIP, nativePort);
	//cout << sConnectionString << endl;
	m_nHandle = -1;
	//int nConnected = 1;
	//long nConnected = OpenConnection(sConnectionString, m_nHandle, bTCP);

	BOOL btCP = 1;

	char sconnect[] = "192.168.1.2 4505";

	// Unload the DLL
	FreeLibrary(datastreaming);

	long nConnected = 1244343;
	try {
		nConnected = (*openFunction)(sconnect, m_nHandle, btCP);
	   }
	catch (int ex) {
		cout << "Exception " << ex << endl;
	}

	cout << nConnected << endl;
	   	/*if(nConnected)
	   	{
	   		const char *nativeString = env->GetStringUTFChars(sIPAddress, 0);
	   		m_sIP = nativeString;
	   		//GetDeviceInfo();
	   	}
	*/

}

