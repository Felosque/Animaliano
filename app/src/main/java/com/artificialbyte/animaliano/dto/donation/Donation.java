package com.artificialbyte.animaliano.dto.donation;

public class Donation {

    private String userid;
    private String refPayco;
    private String facture;
    private String foundationName;
    private String idFoundation;
    private String status;
    private String mount;

    public Donation(String userid, String refPayco, String foundationName, String mount, String idFoundation, String status) {
        this.userid = userid;
        this.refPayco = refPayco;
        this.foundationName = foundationName;
        this.mount = mount;
        this.idFoundation = idFoundation;
        this.status = status;
    }

    public Donation() {
    }

    public String getFacture() {
        return facture;
    }

    public void setFacture(String facture) {
        this.facture = facture;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRefPayco() {
        return refPayco;
    }

    public void setRefPayco(String refPayco) {
        this.refPayco = refPayco;
    }

    public String getFoundationName() {
        return foundationName;
    }

    public void setFoundationName(String foundationName) {
        this.foundationName = foundationName;
    }

    public String getMount() {
        return mount;
    }

    public void setMount(String mount) {
        this.mount = mount;
    }

    public String getIdFoundation() {
        return idFoundation;
    }

    public void setIdFoundation(String idFoundation) {
        this.idFoundation = idFoundation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Donation{" +
                "userid='" + userid + '\'' +
                ", refPayco='" + refPayco + '\'' +
                ", facture='" + facture + '\'' +
                ", foundationName='" + foundationName + '\'' +
                ", idFoundation='" + idFoundation + '\'' +
                ", status='" + status + '\'' +
                ", mount='" + mount + '\'' +
                '}';
    }
}
