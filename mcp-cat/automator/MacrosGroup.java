package net.automator;

import java.io.Serializable;
import java.util.ArrayList;

public class MacrosGroup implements Serializable {
    ArrayList<Macros> group = new ArrayList<>();

    public void add(Macros macros){
        group.add(macros);
    }

    public Macros get(int id) {
        if (0 <= id && id < group.size()) {
            return group.get(id);
        } else {
            System.out.println("Incorrect id");
            return null;
        }
    }

    public void remove(int id){
        if (0 <= id && id < group.size()) {
           group.remove(id);
        } else {
            return;
        }
    }

    public int size(){
        return group.size();
    }

    @Override
    public String toString() {
        return "MacrosGroup{" +
                "group=" + group +
                '}';
    }
}
