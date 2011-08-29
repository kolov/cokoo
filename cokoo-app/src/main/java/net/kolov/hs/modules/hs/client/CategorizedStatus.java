package net.kolov.hs.modules.hs.client;

import java.io.Serializable;
import java.util.List;

/**
 * User: assen
 * Date: 5/14/11
 */
public class CategorizedStatus implements Serializable {
    private String name;
    private List<ClientStatus> states = new java.util.ArrayList<ClientStatus>();

    public CategorizedStatus() {

    }

    public CategorizedStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ClientStatus> getStates() {
        return states;
    }

    public void setStates(List<ClientStatus> states) {
        this.states = states;
    }
}
