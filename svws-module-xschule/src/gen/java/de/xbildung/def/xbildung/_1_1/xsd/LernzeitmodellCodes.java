//
// This file was generated by the Eclipse Implementation of JAXB, v3.0.0 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2025.07.15 at 12:28:12 PM CEST 
//


package de.xbildung.def.xbildung._1_1.xsd;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Lernzeitmodell-Codes.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="Lernzeitmodell-Codes"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
 *     &lt;enumeration value="http://xbildung.de/def/xbildung/2025-02-04/code/lernzeitmodell/dual-ausbildungsintegriert"/&gt;
 *     &lt;enumeration value="http://xbildung.de/def/xbildung/2025-02-04/code/lernzeitmodell/dual-praxisintegriert"/&gt;
 *     &lt;enumeration value="http://xbildung.de/def/xbildung/2025-02-04/code/lernzeitmodell/teilzeit"/&gt;
 *     &lt;enumeration value="http://xbildung.de/def/xbildung/2025-02-04/code/lernzeitmodell/vollzeit"/&gt;
 *     &lt;enumeration value="http://xbildung.de/def/xbildung/2025-02-04/code/lernzeitmodell/~wert_nicht_bekannt"/&gt;
 *     &lt;enumeration value="http://xbildung.de/def/xbildung/2025-02-04/code/lernzeitmodell/~wert_nicht_in_liste"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "Lernzeitmodell-Codes")
@XmlEnum
public enum LernzeitmodellCodes {

    @XmlEnumValue("http://xbildung.de/def/xbildung/2025-02-04/code/lernzeitmodell/dual-ausbildungsintegriert")
    HTTP_XBILDUNG_DE_DEF_XBILDUNG_2025_02_04_CODE_LERNZEITMODELL_DUAL_AUSBILDUNGSINTEGRIERT("http://xbildung.de/def/xbildung/2025-02-04/code/lernzeitmodell/dual-ausbildungsintegriert"),
    @XmlEnumValue("http://xbildung.de/def/xbildung/2025-02-04/code/lernzeitmodell/dual-praxisintegriert")
    HTTP_XBILDUNG_DE_DEF_XBILDUNG_2025_02_04_CODE_LERNZEITMODELL_DUAL_PRAXISINTEGRIERT("http://xbildung.de/def/xbildung/2025-02-04/code/lernzeitmodell/dual-praxisintegriert"),
    @XmlEnumValue("http://xbildung.de/def/xbildung/2025-02-04/code/lernzeitmodell/teilzeit")
    HTTP_XBILDUNG_DE_DEF_XBILDUNG_2025_02_04_CODE_LERNZEITMODELL_TEILZEIT("http://xbildung.de/def/xbildung/2025-02-04/code/lernzeitmodell/teilzeit"),
    @XmlEnumValue("http://xbildung.de/def/xbildung/2025-02-04/code/lernzeitmodell/vollzeit")
    HTTP_XBILDUNG_DE_DEF_XBILDUNG_2025_02_04_CODE_LERNZEITMODELL_VOLLZEIT("http://xbildung.de/def/xbildung/2025-02-04/code/lernzeitmodell/vollzeit"),
    @XmlEnumValue("http://xbildung.de/def/xbildung/2025-02-04/code/lernzeitmodell/~wert_nicht_bekannt")
    HTTP_XBILDUNG_DE_DEF_XBILDUNG_2025_02_04_CODE_LERNZEITMODELL_WERT_NICHT_BEKANNT("http://xbildung.de/def/xbildung/2025-02-04/code/lernzeitmodell/~wert_nicht_bekannt"),
    @XmlEnumValue("http://xbildung.de/def/xbildung/2025-02-04/code/lernzeitmodell/~wert_nicht_in_liste")
    HTTP_XBILDUNG_DE_DEF_XBILDUNG_2025_02_04_CODE_LERNZEITMODELL_WERT_NICHT_IN_LISTE("http://xbildung.de/def/xbildung/2025-02-04/code/lernzeitmodell/~wert_nicht_in_liste");
    private final String value;

    LernzeitmodellCodes(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LernzeitmodellCodes fromValue(String v) {
        for (LernzeitmodellCodes c: LernzeitmodellCodes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
