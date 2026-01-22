package dev.n0fckgway.lab3.beans;

public class GraphBean {
    private ResultsBean resultsBean;
    public String getPointsJson() {
        return resultsBean.getPointsJson();
    }

    public ResultsBean getResultsBean() {
        return resultsBean;
    }

    public void setResultsBean(ResultsBean resultsBean) {
        this.resultsBean = resultsBean;
    }
}
