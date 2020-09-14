package com.droidlogix.dbflare.client.test.models;

import java.io.Serializable;
import java.util.Date;

public class Jobsheet implements Serializable {
    private long idJobsheet;
    private String documentReference;
    private Date applicationDate;

    public long getIdJobsheet() {
        return idJobsheet;
    }

    public void setIdJobsheet(long idJobsheet) {
        this.idJobsheet = idJobsheet;
    }

    public String getDocumentReference() {
        return documentReference;
    }

    public void setDocumentReference(String documentReference) {
        this.documentReference = documentReference;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }
}
