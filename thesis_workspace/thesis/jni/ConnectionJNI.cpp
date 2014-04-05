/*
 * ConnectionJNI.cpp
 *
 *  Created on: Apr 2, 2014
 *      Author: shammond
 */


#pragma comment(lib, "ABM_Datastreaming.lib")
#include <jni.h>
#include <stdio.h>
#include "thesis_ConnectionJNI.h"
#include "string.h"

#include "include/TypeDefDataTypes.h"
#include "include/ThirdPartyCommunication.h"
#include "AbmTngrmDll.h"
#include <windows.h>

//#include "stdafx.h"
#include "Connection.h"
#include <math.h>
#include <iostream>

using namespace std;

int m_nHandle;
string m_sIP;



JNIEXPORT jint JNICALL Java_thesis_ConnectionJNI_openConnection(JNIEnv *env, jobject thisObj, jstring sIPAddress, jstring sPort, jboolean bTCP) {
	printf("Hello World!\n");

	HINSTANCE datastreaming = LoadLibrary("C:\\Users\\shammond\\git\\thesis_2014\\thesis_workspace\\thesis\\dllfiles\\ABM_Datastreaming.dll");

	 if (!datastreaming) {
	    cout << "could not load the dynamic library" << endl;
	    return EXIT_FAILURE;
	  }

   	char* sConnectionString;
   	sprintf(sConnectionString, "%s %s", sIPAddress, sPort);
   	m_nHandle = -1;
   	//int nConnected = 1;
    int nConnected = OpenConnection(sConnectionString, m_nHandle, bTCP);
   	if(nConnected)
   	{
   		const char *nativeString = env->GetStringUTFChars(sIPAddress, 0);
   		m_sIP = nativeString;
   		//GetDeviceInfo();
   	}

   	return nConnected;
   }

JNIEXPORT jfloatArray JNICALL Java_thesis_ConnectionJNI_getClassData(JNIEnv *env, jobject thisObj)
{

	jfloatArray brainData;
	brainData = env->NewFloatArray(26);
	if (brainData == NULL)
		return NULL; /* out of memory error thrown */

	int nCount = 2;
	float pFloatData[26];
	for (int i = 0; i < 26 ; i++)
		pFloatData[i] = (float) i;
	/*int nCount = -1;
	float* pFloatData = AgetCWPC(nCount, CConnection::m_nHandle);

	if (pFloatData == NULL || nCount <= 0)
		return NULL;

	_ABM_DATA_BRAIN_STATE* pData = NULL; */
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

	size_t stateSize = sizeof(_ABM_DATA_BRAIN_STATE);
	/*brainData = new float*[nCount];

	for (int i = 0; i < nCount; i++) {
		brainData[i] = new float[stateSize];
		for (int j = 0; j < stateSize; j++) {
			brainData[i][j] = pFloatData[i * stateSize + j];
		}
	}*/

	env->SetFloatArrayRegion(brainData,0,26,pFloatData);

	return brainData;


	/*if (pData == NULL)
	{
		return;
	}
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


