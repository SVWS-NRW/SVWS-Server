package de.svws_nrw.davapi.model.dav.card;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "supported-address-data", namespace = "urn:ietf:params:xml:ns:carddav")
public class SupportedAddressData {

    @XmlElement(required = true)
    protected List<CardAddressDataType> addressDataTypes;

    public List<CardAddressDataType> getAddressDataTypes() {
        if (addressDataTypes == null) {
            addressDataTypes = new ArrayList<>();
        }
        return this.addressDataTypes;
    }
}
