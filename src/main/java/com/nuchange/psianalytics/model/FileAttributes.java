package com.nuchange.psianalytics.model;

public class FileAttributes {
    String fullName;
    String fileName;
    Integer instance;
    Integer fileId;

    public FileAttributes(String originalFile) {
        super();
        this.fullName = originalFile;
        String temp = originalFile;
        temp = temp.substring(temp.indexOf("^")+1, temp.indexOf("/"));
        temp = temp.replaceAll(" ", "_");
        temp = temp.replaceAll("\\.", "_");
        this.fileName = temp;
        temp = originalFile;
        temp = temp.substring(temp.indexOf("-") + 1);
        this.instance = Integer.parseInt(temp);
        temp = originalFile;
        temp = temp.substring(temp.indexOf("/")+1, temp.indexOf("-"));
        this.fileId = Integer.parseInt(temp);
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
