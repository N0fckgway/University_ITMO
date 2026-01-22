package dev.n0fckgway.lab3.beans;

import dev.n0fckgway.lab3.model.Result;
import dev.n0fckgway.lab3.persistence.ResultRepository;
import com.google.gson.Gson;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.webapp.FacesServlet;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class ResultsBean implements Serializable{
    public final List<Result> resultList = new ArrayList<>();
    public ResultRepository resultRepository;
    private String sessionId;

    @PostConstruct
    public void init() {
        resultRepository = new ResultRepository();
        resultList.clear();
        String currentId = getSessionId();
        if (currentId != null) {
            List<Result> currentList = resultRepository.findBySessionId(currentId);
            resultList.addAll(currentList);
        }


    }

    public void addResult(Result result) {
        if (result == null) return;
        String currentId = getSessionId();
        result.setSessionId(currentId);
        resultRepository.save(result);
        resultList.add(0, result);

    }

    public void clear() {
        String currentId = getSessionId();
        resultRepository.deleteBySessionId(currentId);
        resultList.clear();
    }

    public List<Result> getResultList() {
        return resultList;
    }

    public List<Result> getResults() {
        return resultList;
    }

    public String getPointsJson() {
        if (resultList.isEmpty()) return "[]";

        return new Gson().toJson(
                resultList.stream()
                        .map(result -> Map.of(
                                "x", formatDouble(result.getX()),
                                "y", formatDouble(result.getY()),
                                "r", formatDouble(result.getR()),
                                "hit", result.isHit()
                        ))
                        .toList()
        );
    }

    private String formatDouble(Double value) {
        if (value == null) {
            return "0";
        }
        return String.format(Locale.US, "%.2f", value);
    }

    private String getSessionId() {
        if (sessionId == null) {
            sessionId = resolveSessionId();
        }
        return sessionId;
    }

    private String resolveSessionId() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null) {
            return null;
        }
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        return session.getId();


    }

}
