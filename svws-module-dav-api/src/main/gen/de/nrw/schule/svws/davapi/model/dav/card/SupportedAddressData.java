package de.nrw.schule.svws.davapi.model.dav.card;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
