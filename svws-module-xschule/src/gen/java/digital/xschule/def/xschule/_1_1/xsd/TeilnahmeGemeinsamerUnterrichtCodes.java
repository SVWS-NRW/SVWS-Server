//
// This file was generated by the Eclipse Implementation of JAXB, v3.0.0 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2025.07.15 at 12:28:12 PM CEST 
//


package digital.xschule.def.xschule._1_1.xsd;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TeilnahmeGemeinsamerUnterricht-Codes.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="TeilnahmeGemeinsamerUnterricht-Codes"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/teilnahmegemeinsamerunterricht/zieldifferent"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/teilnahmegemeinsamerunterricht/zielgleich"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/teilnahmegemeinsamerunterricht/~wert_nicht_bekannt"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/teilnahmegemeinsamerunterricht/~wert_nicht_in_liste"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/teilnahmegemeinsamerunterricht/~wert_nicht_uebermittelbar_rechtliche_gruende"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "TeilnahmeGemeinsamerUnterricht-Codes")
@XmlEnum
public enum TeilnahmeGemeinsamerUnterrichtCodes {

    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/teilnahmegemeinsamerunterricht/zieldifferent")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_TEILNAHMEGEMEINSAMERUNTERRICHT_ZIELDIFFERENT("http://xschule.digital/def/xschule/1.0/code/teilnahmegemeinsamerunterricht/zieldifferent"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/teilnahmegemeinsamerunterricht/zielgleich")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_TEILNAHMEGEMEINSAMERUNTERRICHT_ZIELGLEICH("http://xschule.digital/def/xschule/1.0/code/teilnahmegemeinsamerunterricht/zielgleich"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/teilnahmegemeinsamerunterricht/~wert_nicht_bekannt")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_TEILNAHMEGEMEINSAMERUNTERRICHT_WERT_NICHT_BEKANNT("http://xschule.digital/def/xschule/1.0/code/teilnahmegemeinsamerunterricht/~wert_nicht_bekannt"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/teilnahmegemeinsamerunterricht/~wert_nicht_in_liste")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_TEILNAHMEGEMEINSAMERUNTERRICHT_WERT_NICHT_IN_LISTE("http://xschule.digital/def/xschule/1.0/code/teilnahmegemeinsamerunterricht/~wert_nicht_in_liste"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/teilnahmegemeinsamerunterricht/~wert_nicht_uebermittelbar_rechtliche_gruende")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_TEILNAHMEGEMEINSAMERUNTERRICHT_WERT_NICHT_UEBERMITTELBAR_RECHTLICHE_GRUENDE("http://xschule.digital/def/xschule/1.0/code/teilnahmegemeinsamerunterricht/~wert_nicht_uebermittelbar_rechtliche_gruende");
    private final String value;

    TeilnahmeGemeinsamerUnterrichtCodes(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TeilnahmeGemeinsamerUnterrichtCodes fromValue(String v) {
        for (TeilnahmeGemeinsamerUnterrichtCodes c: TeilnahmeGemeinsamerUnterrichtCodes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
