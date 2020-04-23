package softuni.exam.models.dtos;

import org.hibernate.validator.constraints.Length;
import softuni.exam.models.entities.Passenger;
import softuni.exam.models.entities.Plane;
import softuni.exam.models.entities.Town;

import javax.validation.constraints.DecimalMin;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement(name = "ticket")
@XmlAccessorType(XmlAccessType.FIELD)
public class TicketSeedDto {
    @XmlElement(name = "serial-number")
    private String serialNumber;
    @XmlElement(name = "price")
    private BigDecimal price;
    @XmlElement(name = "take-off")
    private String takeOff;
    @XmlElement(name = "from-town")
    private TownSeedDto fromTown;
    @XmlElement(name = "to-town")
    private TownSeedDto toTown;
    @XmlElement(name = "passenger")
    private PassengerDto passengerDto;
    @XmlElement(name = "plane")
    private PlaneDto planeDto;

    public PassengerDto getPassengerDto() {
        return passengerDto;
    }

    public void setPassengerDto(PassengerDto passengerDto) {
        this.passengerDto = passengerDto;
    }

    public PlaneDto getPlaneDto() {
        return planeDto;
    }

    public void setPlaneDto(PlaneDto planeDto) {
        this.planeDto = planeDto;
    }

    public TownSeedDto getFromTown() {
        return fromTown;
    }

    public void setFromTown(TownSeedDto fromTown) {
        this.fromTown = fromTown;
    }

    public TownSeedDto getToTown() {
        return toTown;
    }

    public void setToTown(TownSeedDto toTown) {
        this.toTown = toTown;
    }

    public TicketSeedDto() {
    }

    @Length(min = 2)
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @DecimalMin(value = "0")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTakeOff() {
        return takeOff;
    }

    public void setTakeOff(String takeOff) {
        this.takeOff = takeOff;
    }

}
