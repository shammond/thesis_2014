/*
 * ConnectionJNI.cpp
 *
 *  Created on: Apr 2, 2014
 *      Author: shammond
 */


#pragma comment(lib, "ABM_Datastreaming")
#include <jni.h>
#include <stdio.h>
#include "thesis_ConnectionJNI.h"
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


JNIEXPORT jint JNICALL Java_thesis_ConnectionJNI_openConnection(JNIEnv *env, jobject thisObj, jstring sIPAddress, jstring sPort, jboolean bTCP) {
	printf("Hello World!\n");

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

	 if(openFunction == NULL || getBrainState == NULL || closeFunction == NULL)
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

   	//int nConnected = 1;
    //long nConnected = OpenConnection(sConnectionString, m_nHandle, bTCP);
   	BOOL btCP = 1;
  	m_nHandle = -1;
   	char sconnect[] = "192.168.1.2 4505";


   	// Unload the DLL
   	//FreeLibrary(datastreaming);

   	long nConnected = 12314;

   	//try {
   	nConnected = (*openFunction)(sconnect, m_nHandle, btCP);
   //	}
   //	catch (int ex) {
   //		cout << "Exception " << ex << endl;
   	//}

    cout << nConnected << endl;
   	/*if(nConnected)
   	{
   		const char *nativeString = env->GetStringUTFChars(sIPAddress, 0);
   		m_sIP = nativeString;
   		//GetDeviceInfo();
   	}*/



   	return nConnected;
}

JNIEXPORT jint JNICALL Java_thesis_ConnectionJNI_closeConnection(JNIEnv *env, jobject thisObj)
{
	long nResult = 0;
	if( INVALID_HANDLE != m_nHandle )
	{
		//nResult = CloseConnection(m_nHandle);
		long nResult = (*closeFunction)(m_nHandle);
		if(1 == nResult)
		{
			m_nHandle = INVALID_HANDLE;
		}
	}
	return nResult;
}

JNIEXPORT jfloatArray JNICALL Java_thesis_ConnectionJNI_getClassData(JNIEnv *env, jobject thisObj)
{

	int nCount = -1;
	float* pFloatData = (*getBrainState)(nCount,m_nHandle);

	if (pFloatData == NULL || nCount <= 0) {
		cout << "it was null" << endl;
		return NULL;
	}

	jfloatArray brainData;
	brainData = env->NewFloatArray(nCount*13);

	for (int i=0;i<nCount;i++) {
		for (int j=0;j<13;j++) {
			cout << pFloatData[j+i*13] << ",";
			brainData[i*13+j] = pFloatData[j+i*13];
		}
		cout << endl;
	}
	//_ABM_DATA_BRAIN_STATE* pData = NULL;
	// bosko: TODO: copy array to _ABM_DATA_BRAIN_STATE struct

	/*typedef struct __ATIME_DATA_NO_OFFSET{
		float epoch;
		float abmHour;
		float abmMin;
		float abmSec;
		float abmMilli;
	}_ABM_DATA_TIME_NO_OFFSET;*/

	/*pData = new _ABM_DATA_BRAIN_STATE[nCount];

	for (int i = 0; i < nCount; i++) {
		pData[i].time.epoch = pFloatData + i * sizeof(_ABM_DATA_BRAIN_STATE);
		pData[i].time.abmHour = pFloatData + i * sizeof(_ABM_DATA_BRAIN_STATE) + 1;
		pData[i].time.abmMin = pFloatData + i * sizeof(_ABM_DATA_BRAIN_STATE) + 2;
		pData[i].time.abmSec = pFloatData + i * sizeof(_ABM_DATA_BRAIN_STATE) + 3;
		pData[i].time.abmMilli = pFloatData + i * sizeof(_ABM_DATA_BRAIN_STATE) + 4;

		pData[i].fClassificationEstimate = pFloatData + i * sizeof(_ABM_DATA_BRAIN_STATE) + 5;
		pData[i].fHighEngagementEstimate = pFloatData + i * sizeof(_ABM_DATA_BRAIN_STATE) + 6;
		pData[i].fLowEngagementEstimate = pFloatData + i * sizeof(_ABM_DATA_BRAIN_STATE) + 7;
		pData[i].fDistractionEstimate = pFloatData + i * sizeof(_ABM_DATA_BRAIN_STATE) + 8;
		pData[i].fDrowsyEstimate = pFloatData + i * sizeof(_ABM_DATA_BRAIN_STATE) + 9;
		pData[i].fWorkloadFBDSEstimate = pFloatData + i * sizeof(_ABM_DATA_BRAIN_STATE) + 10;
		pData[i].fWorkloadBDSEstimate = pFloatData + i * sizeof(_ABM_DATA_BRAIN_STATE) + 11;
		pData[i].fWorkloadAverageEstimate = pFloatData + i * sizeof(_ABM_DATA_BRAIN_STATE) + 12;
	}*/

	//size_t stateSize = sizeof(_ABM_DATA_BRAIN_STATE);
	/*brainData = new float*[nCount];

	for (int i = 0; i < nCount; i++) {
		brainData[i] = new float[stateSize];
		for (int j = 0; j < stateSize; j++) {
			brainData[i][j] = pFloatData[i * stateSize + j];
		}
	}*/

	//env->SetFloatArrayRegion(brainData,0,26,pFloatData);

	return brainData;


/*
	for (int ind = 0; ind < nCount; ind++)
	{
		char* sLine;
		string sTimeStamp = GetStr_h_m_s_ms(
			pData[ind].time.abmHour,
			pData[ind].time.abmMin,
			pData[ind].time.abmSec,
			pData[ind].time.abmMilli );
			sLine = sprintf(sLine, "%s,%s,%6f,%6f,%6f,%6f,%6f,%6f,%6f,%6f",
			CConnection::m_sSessionId, sTimeStamp,
			pData[ind].fDrowsyEstimate ,
			pData[ind].fDistractionEstimate,
			pData[ind].fLowEngagementEstimate ,
			pData[ind].fHighEngagementEstimate ,
			pData[ind].fClassificationEstimate,
			pData[ind].fWorkloadFBDSEstimate,
			pData[ind].fWorkloadBDSEstimate,
			pData[ind].fWorkloadAverageEstimate );
		CConnection::m_ofs_class
			<< sLine//sLine.GetBuffer(0)
			<< endl;
	}*/
}

JNIEXPORT string JNICALL Java_thesis_ConnectionJNI_getTimeStamp()
{
	char* sTime;
	if( INVALID_HANDLE == m_nHandle )
		return sTime;
	float hour, min, sec, msec;
	//GetTimeStamp(hour, min, sec, msec, m_nHandle);

	sprintf(sTime,"%02d:%02d:%02d:%03d", (int)hour, (int)min, (int)sec, (int)msec );
	return sTime;
}


