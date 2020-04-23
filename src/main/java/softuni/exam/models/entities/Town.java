package softuni.exam.models.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import java.util.List;

@Entity
@Table(name = "towns")
public class Town extends BaseEntity {
    private String name;
    private int population;
    private String guide;
    private List<Ticket> fromTickets;
    private List<Ticket> toTickets;

    @OneToMany(mappedBy = "fromTown")
    public List<Ticket> getFromTickets() {
        return fromTickets;
    }

    public void setFromTickets(List<Ticket> fromTickets) {
        this.fromTickets = fromTickets;
    }

    @OneToMany(mappedBy = "toTown")
    public List<Ticket> getToTickets() {
        return toTickets;
    }

    public void setToTickets(List<Ticket> toTickets) {
        this.toTickets = toTickets;
    }

    public Town() {
    }

    @Column(unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Min(value = 0)
    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }
}
