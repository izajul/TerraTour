
package com.monordevelopers.tt.terratour.weatherPojo.dayforcastpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QpfDay {

    @SerializedName("in")
    @Expose
    private Double in;
    @SerializedName("mm")
    @Expose
    private Integer mm;

    public Double getIn() {
        return in;
    }

    public void setIn(Double in) {
        this.in = in;
    }

    public Integer getMm() {
        return mm;
    }

    public void setMm(Integer mm) {
        this.mm = mm;
    }

}
