#pragma once

//#include "tcp\TypeDef.h"
#include "include/TypeDefDataTypes.h"
#include "include/ThirdPartyCommunication.h"
#include "include/AbmTngrmDll.h"

#define INVALID_HANDLE -1
#define MAXNUM_BandOverallPSDDATA     60   // maximum number of BandOverallPSD 

#define     NOT_USE_METHOD					0
#define     SUCCESS							1

#define     ERROR_INIT_PARAMETER			-1
#define     ERROR_INIT_WINSOCK2				-2
#define     ERROR_CREATE_SOCKET				-3
#define     ERROR_GET_HOST					-4
#define     ERROR_CONNECT					-5
#define     ERROR_NON_BLOCK_MODE			-6

#define		DS_ERROR_BAD_HANDLE				-7
#define     DS_ERROR_TO_MANY_CONNECTIONS		-8
#define     DS_ERROR_WRONG_INPUT_PARAMETER		-9
#define     DS_ERROR_NONEXISTANT_CONNECTION		-10

#define     DS_ERROR_CHANNELMAP_NOT_SET			-11


#define SEP _T(",")		// field separator in output files

#include <fstream>
#include <string>

using namespace std;

string GetStr_h_m_s_ms(float h, float m, float s, float ms);

class CConnection
{
public:
	CConnection(void);
	~CConnection(void);

	void GetDeviceInfo();
	int Open(string sIPAddress, string sPort, bool bTCP);
	int Close();
	int CreateFiles(string sPath);
	void CloseFiles();
	void GetData();
	string GetTimeStampStr();

	void GetRaw();
	void GetClass();
	void GetEKG();
	void GetBandOverAllPSD();
	void GetDeconData();
	void GetZScore();
	void GetPsdRefRaw();

	int  GetCurrentDataPoint(){return m_nCurrentDataPoint;}

	int CreateOutputFiles();
	void CloseOutputFiles();

	// Get/set gui control index for connection
	void SetGUIControlRowIndex(int nIndex){ m_nGUIControlRowIndex = nIndex;}
	int GetGUIControlRowIndex(){return m_nGUIControlRowIndex; }
	int m_nGUIControlRowIndex;

	int m_nHandle;
	//	long connected = 0;
	int m_nEKGPackageSize;
	int m_nPSDPackageSize;
	int numberchannels;
	int numberPSDchannels ;
	int m_nEKG ;
	int m_nDeconPackageSize ;
	int m_nRawPackageSize ;
	int m_nQualityPackageSize ;
	int m_nQualityChannelPackageSize ;
	int m_nMovementPackageSize ;
	int m_nACCPackageSize ;
	int m_nZScorePackageSize ;
	int m_nBandOverallPSDPackageSize ;
	int m_nPSDBandwidthPackageSize ;
	int m_nPSDBandwidthRawPackageSize ;
	int m_nNumberPSDBands ;
	int m_nNumberPSDBandsOverall ;
	string m_sSessionId ;
	bool m_bOutputFilesCreated;

	string m_sIP;
	string m_sLastTime;
	int m_nCurrentDataPoint;

	// output files
	ofstream m_ofs_raw;
	ofstream m_ofs_class;
	ofstream m_ofs_ekg;
	ofstream m_ofs_bandOverallPSDData;
	ofstream m_ofs_decon;
	ofstream m_ofs_zscore;
	ofstream m_ofs_psd_ref_raw;

};
