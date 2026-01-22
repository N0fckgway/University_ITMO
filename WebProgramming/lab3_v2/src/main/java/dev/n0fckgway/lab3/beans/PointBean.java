package dev.n0fckgway.lab3.beans;


import dev.n0fckgway.lab3.model.Result;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static dev.n0fckgway.lab3.model.AreaChecker.isHit;
import static javax.faces.application.FacesMessage.SEVERITY_ERROR;

public class PointBean {
    private Double x;
    private Double y;
    private Double r;
    private String xValues;
    private ResultsBean resultsBean;

    public void submit() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        boolean valid = true;
        boolean hasX = false;
        List<Double> xs = parseValuesX(facesContext);
        if (xs == null) {
            facesContext.addMessage("", new FacesMessage(SEVERITY_ERROR, "Некорректные значения X!", null));
            valid = false;
        } else if (!xs.isEmpty()) {
            hasX = true;
            for (Double value : xs) {
                if (value == null || value < -3 || value > 3) {
                    facesContext.addMessage("", new FacesMessage(SEVERITY_ERROR, "X должен быть в диапазоне [-3; 3].", null));
                    valid = false;
                    break;
                }
            }
        }

        if (!hasX && x == null) {
            facesContext.addMessage("", new FacesMessage(SEVERITY_ERROR, "Выберите хотя бы одно X.", null));
            valid = false;
        } else if (x != null) {
            if (x < -3 || x > 3) {
                facesContext.addMessage("", new FacesMessage(SEVERITY_ERROR, "X должен быть в диапазоне [-3; 3].", null));
                valid = false;
            } else xs = List.of(x);
        }

        if (y == null) {
            facesContext.addMessage("", new FacesMessage(SEVERITY_ERROR, "Введите Y!", null));
            valid = false;
        } else if (y < -5 || y > 5) {
            facesContext.addMessage("", new FacesMessage(SEVERITY_ERROR, "Введите Y от -5 до 5 (включительно).", null));
            valid = false;
        }

        if (r == null) {
            facesContext.addMessage("", new FacesMessage(SEVERITY_ERROR, "Введите R!", null));
            valid = false;
        } else if (r < 1 || r > 5) {
            facesContext.addMessage("", new FacesMessage(SEVERITY_ERROR, "R должен быть в диапазоне [1; 5].", null));
            valid = false;
        }

        if (!valid) {
            facesContext.validationFailed();
            return;
        }

        for (Double value : xs) {
            boolean hit = isHit(value, y, r);
            Result result = new Result(value, y, r, hit, LocalDateTime.now());
            resultsBean.addResult(result);
        }

    }

    private List<Double> parseValuesX(FacesContext facesContext) {
        String raw = xValues;
        if (raw == null || raw.trim().isEmpty()) {
            raw = readHiddenValuesX(facesContext);
        }
        if (raw == null || raw.trim().isEmpty()) {
            return List.of();
        }

        String[] parts = raw.split(",");
        List<Double> values = new ArrayList<>();
        for (String value : parts) {
            String trimmed = value.trim();
            try {
                values.add(Double.valueOf(trimmed));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return values;
    }

    private String readHiddenValuesX(FacesContext facesContext) {
        if (facesContext == null) {
            return null;
        }
        UIComponent component = facesContext.getViewRoot().findComponent("inputForm:xValues");
        if (component == null) {
            return null;
        }
        String clientId = component.getClientId(facesContext);
        return facesContext.getExternalContext().getRequestParameterMap().get(clientId);
    }


    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getR() {
        return r;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public void setR(Double r) {
        this.r = r;
    }

    public String getXValues() {
        return xValues;
    }

    public void setXValues(String xValues) {
        this.xValues = xValues;
    }

    public ResultsBean getResultsBean() {
        return resultsBean;
    }

    public void setResultsBean(ResultsBean resultsBean) {
        this.resultsBean = resultsBean;
    }
}
