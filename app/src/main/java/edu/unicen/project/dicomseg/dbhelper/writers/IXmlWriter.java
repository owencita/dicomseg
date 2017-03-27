package edu.unicen.project.dicomseg.dbhelper.writers;


import java.util.List;

import edu.unicen.project.dicomseg.dbhelper.DbHelper;
import edu.unicen.project.dicomseg.models.GenericModel;

public interface IXmlWriter {

    public String getXmlFileName();

    public List<GenericModel> getModels(DbHelper dbHelper);

    public String writeXml(List<GenericModel> models);
}
