package edu.exa.unicen.dicomseg.dbhelper.writers;


import java.util.List;

import edu.exa.unicen.dicomseg.dbhelper.DbHelper;
import edu.exa.unicen.dicomseg.models.GenericModel;

public interface IXmlWriter {

    public String getXmlFileName();

    public List<GenericModel> getModels(DbHelper dbHelper);

    public String writeXml(List<GenericModel> models);
}
