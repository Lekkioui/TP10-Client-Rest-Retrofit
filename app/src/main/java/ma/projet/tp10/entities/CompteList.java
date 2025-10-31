package ma.projet.tp10.entities;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import java.util.List;

@Root(name = "List", strict = false)
public class CompteList {
    @ElementList(inline = true, entry = "item")
    private List<Compte> comptes;

    public List<Compte> getComptes() {
        return comptes;
    }

    public void setComptes(List<Compte> comptes) {
        this.comptes = comptes;
    }
}
