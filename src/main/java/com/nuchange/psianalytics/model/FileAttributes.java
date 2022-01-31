package com.nuchange.psianalytics.model;

public class FileAttributes {
    String fullName;
    String fileName;
    String formName;
    Integer instance;
    Integer fileId;

    public FileAttributes(String originalFile) {
        super();
        this.fullName = originalFile;
        String temp = originalFile;
        temp = temp.substring(temp.indexOf("^")+1, temp.indexOf("."));
        temp = temp.replaceAll(" ", "_");
        temp = temp.replaceAll("\\.", "_");
        this.formName = temp;
        temp = originalFile;
        temp = temp.substring(temp.indexOf("^")+1, temp.indexOf("/"));
        temp = temp.replaceAll(" ", "_");
        temp = temp.replaceAll("\\.", "_");
        this.fileName = temp;
        temp = originalFile;
        temp = temp.substring(temp.indexOf("-") + 1);
        if(temp.contains("/")) {
            temp = temp.substring(0,temp.indexOf("/"));
        }
        this.instance = Integer.parseInt(temp);
        //We are not using fieldId now. Uncomment if needed.
        // Current parsing logic will fail for formNamespace like Bahmni^Programs 6475.1/1-0/9-0, where there is an additional / after 1-0
        // Add fix for this if uncommenting.
//        temp = originalFile;
//        temp = temp.substring(temp.indexOf("/")+1, temp.indexOf("-"));
//        this.fileId = Integer.parseInt(temp);
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getInstance() {
        return instance;
    }

    public void setInstance(Integer instance) {
        this.instance = instance;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }
}
