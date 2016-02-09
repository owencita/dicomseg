package edu.unicen.project.dicomseg.dicom;

public class DicomTags {

    // Group numbers
    public static final int PATIENT_INFO_GROUP = 0x0010;

    // Element numbers
    // (0010,0010) Patient's Name PN 1
    public static final int PATIENT_NAME = 0x0010;
    // (0010,0020) Patient ID LO 1
    public static final int PATIENT_ID = 0x0020;
    // (0010,0030) Patient's Birth Date DA 1
    public static final int PATIENT_BIRTH_DATE = 0x0030;
    // (0010,0040) Patient Sex CS 1
    public static final int PATIENT_SEX = 0x0040;
    // (0010,1010) Patient's Age AS 1
    public static final int PATIENT_AGE = 0x1010;
    // (0010,1030) Patient's Weight DS 1
    public static final int PATIENT_WEIGHT = 0x1030;
    // (0010,1040) Patient's Address LO 1
    public static final int PATIENT_ADDRESS = 0x1040;

}
