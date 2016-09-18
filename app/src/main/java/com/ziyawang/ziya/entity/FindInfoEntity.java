package com.ziyawang.ziya.entity;

/**
 * Created by 牛海丰 on 2016/7/23.
 */
public class FindInfoEntity {

    private String ProjectID ;
    private String ProArea ;
    private String FromWhere ;
    private String AssetType ;
    private String TotalMoney ;
    private String TransferMoney ;
    private String Status ;
    private String Rate ;
    private String Requirement ;
    private String BuyerNature ;
    private String Informant ;
    private String Buyer ;
    private String TypeName ;
    private String ProjectNumber ;
    private String Member ;
    private String CertifyState ;
    private String PublishState ;
    private String Corpore ;
    private String WordDes ;
    private String InvestType ;
    private String Year ;

    public FindInfoEntity(){}

    public FindInfoEntity(String projectID , String proArea, String fromWhere, String assetType, String totalMoney, String transferMoney, String status, String rate, String requirement, String buyerNature, String informant, String buyer,String typeName,String projectNumber , String member , String certifyState , String publishState , String corpore , String wordDes ,String investType , String year) {
        super();
        ProjectID = projectID ;
        ProArea = proArea;
        FromWhere = fromWhere;
        AssetType = assetType;
        TotalMoney = totalMoney;
        TransferMoney = transferMoney;
        Status = status;
        Rate = rate;
        Requirement = requirement;
        BuyerNature = buyerNature;
        Informant = informant;
        Buyer = buyer;
        TypeName = typeName ;
        ProjectNumber = projectNumber ;
        Member = member ;
        CertifyState = certifyState ;
        PublishState = publishState ;
        Corpore = corpore ;
        WordDes = wordDes ;
        InvestType = investType ;
        Year = year ;
    }

    public String getWordDes() {
        return WordDes;
    }

    public String getInvestType() {
        return InvestType;
    }

    public void setInvestType(String investType) {
        InvestType = investType;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public void setWordDes(String wordDes) {
        WordDes = wordDes;
    }

    public String getProjectNumber() {
        return ProjectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        ProjectNumber = projectNumber;
    }

    public String getMember() {
        return Member;
    }

    public void setMember(String member) {
        Member = member;
    }

    public String getCorpore() {
        return Corpore;
    }

    public void setCorpore(String corpore) {
        Corpore = corpore;
    }

    public String getCertifyState() {
        return CertifyState;
    }

    public String getPublishState() {
        return PublishState;
    }

    public void setPublishState(String publishState) {
        PublishState = publishState;
    }

    public void setCertifyState(String certifyState) {
        CertifyState = certifyState;
    }

    public String getProjectID() {
        return ProjectID;
    }

    public void setProjectID(String projectID) {
        ProjectID = projectID;
    }

    public String getProArea() {
        return ProArea;
    }

    public void setProArea(String proArea) {
        ProArea = proArea;
    }

    public String getFromWhere() {
        return FromWhere;
    }

    public void setFromWhere(String fromWhere) {
        FromWhere = fromWhere;
    }

    public String getAssetType() {
        return AssetType;
    }

    public void setAssetType(String assetType) {
        AssetType = assetType;
    }

    public String getTotalMoney() {
        return TotalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        TotalMoney = totalMoney;
    }

    public String getTransferMoney() {
        return TransferMoney;
    }

    public void setTransferMoney(String transferMoney) {
        TransferMoney = transferMoney;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }

    public String getRequirement() {
        return Requirement;
    }

    public void setRequirement(String requirement) {
        Requirement = requirement;
    }

    public String getBuyerNature() {
        return BuyerNature;
    }

    public void setBuyerNature(String buyerNature) {
        BuyerNature = buyerNature;
    }

    public String getInformant() {
        return Informant;
    }

    public void setInformant(String informant) {
        Informant = informant;
    }

    public String getBuyer() {
        return Buyer;
    }

    public void setBuyer(String buyer) {
        Buyer = buyer;
    }

    public String getTypeName() {
        return TypeName;
    }

    public void setTypeName(String typeName) {
        TypeName = typeName;
    }
}
