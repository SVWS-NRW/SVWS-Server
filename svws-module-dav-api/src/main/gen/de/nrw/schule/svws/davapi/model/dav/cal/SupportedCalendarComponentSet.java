package de.nrw.schule.svws.davapi.model.dav.cal;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "supported-calendar-component-set", namespace = "urn:ietf:params:xml:ns:caldav")
public class SupportedCalendarComponentSet {

    @XmlElement(required = true)
    protected List<CalendarComponent> calendarComponents;

    public List<CalendarComponent> getCalendarComponents() {
        if (calendarComponents == null) {
            calendarComponents = new ArrayList<>();
        }
        return this.calendarComponents;
    }
}
