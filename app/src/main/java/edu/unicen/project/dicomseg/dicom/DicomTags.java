package edu.unicen.project.dicomseg.dicom;

public class DicomTags {

    // Group numbers

    public static final int PATIENT_INFO_GROUP = 0x0010;
    public static final int STUDY_INFO_GROUP = 0x0020;
    public static final int FRAME_INFO_GROUP = 0x0028;

    // Element numbers

    // Value Representation (VR)
    // Value Multiplicity (VM)

    // (0010,0010) Patient's Name VR=PN VM=1
    public static final int PATIENT_NAME = 0x0010;
    // (0010,0020) Patient ID VR=LO VM=1
    public static final int PATIENT_ID = 0x0020;
    // (0010,0030) Patient's Birth Date VR=DA VM=1
    public static final int PATIENT_BIRTH_DATE = 0x0030;
    // (0010,0040) Patient Sex VR=CS VM=1
    public static final int PATIENT_SEX = 0x0040;
    // (0010,1010) Patient's Age VR=AS VM=1
    public static final int PATIENT_AGE = 0x1010;
    // (0010,1030) Patient's Weight VR=DS VM=1
    public static final int PATIENT_WEIGHT = 0x1030;
    // (0010,1040) Patient's Address VR=LO VM=1
    public static final int PATIENT_ADDRESS = 0x1040;

    // (0020,000D) Unique identifier for the Study.
    public static final int STUDY_INSTANCE_UID = 0x000D;
    // (0020,000E) Unique identifier of the Series.
    public static final int SERIES_INSTANCE_UID = 0x000E;

    // (0028,0008) Number of Frames VR=IS VM=1
    public static final int NUMBER_OF_FRAMES = 0x0008;

}
