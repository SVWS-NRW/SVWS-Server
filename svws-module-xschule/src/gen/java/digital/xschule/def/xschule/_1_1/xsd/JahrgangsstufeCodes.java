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
 * <p>Java class for Jahrgangsstufe-Codes.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <pre>
 * &lt;simpleType name="Jahrgangsstufe-Codes"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/1_jahr"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/2_jahr"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/3_jahr"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/4_jahr"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/aufbau_vorsemester"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/berufsbildungsstufe"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/einfuehrungsphase"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_0"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_1"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_10"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_11"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_12"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_13"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_2"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_3"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_4"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_5"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_6"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_7"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_8"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_9"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/nicht_zugeordnet"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/primarstufe"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/qualifikationsphase_1"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/qualifikationsphase_2"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/qualifikationsphase_3"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/qualifikationsphase_4"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/sammelklasse_umow"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/schulkindergarten"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/sekundarstufe_i"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/ubergreifend_klasse"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/uebergreifend_primar"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/uebergreifend_primar_sekundar_i"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/uebergreifend_sekundar_i"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/uebergreifend_sekundar_i_ii"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/uebergreifend_sekundar_ii"/&gt;
 *     &lt;enumeration value="http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/vorklasse"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "Jahrgangsstufe-Codes")
@XmlEnum
public enum JahrgangsstufeCodes {

    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/1_jahr")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_1_JAHR("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/1_jahr"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/2_jahr")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_2_JAHR("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/2_jahr"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/3_jahr")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_3_JAHR("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/3_jahr"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/4_jahr")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_4_JAHR("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/4_jahr"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/aufbau_vorsemester")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_AUFBAU_VORSEMESTER("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/aufbau_vorsemester"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/berufsbildungsstufe")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_BERUFSBILDUNGSSTUFE("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/berufsbildungsstufe"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/einfuehrungsphase")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_EINFUEHRUNGSPHASE("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/einfuehrungsphase"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_0")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_JAHRGANGSSTUFE_0("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_0"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_1")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_JAHRGANGSSTUFE_1("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_1"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_10")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_JAHRGANGSSTUFE_10("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_10"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_11")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_JAHRGANGSSTUFE_11("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_11"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_12")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_JAHRGANGSSTUFE_12("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_12"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_13")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_JAHRGANGSSTUFE_13("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_13"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_2")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_JAHRGANGSSTUFE_2("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_2"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_3")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_JAHRGANGSSTUFE_3("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_3"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_4")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_JAHRGANGSSTUFE_4("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_4"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_5")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_JAHRGANGSSTUFE_5("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_5"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_6")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_JAHRGANGSSTUFE_6("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_6"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_7")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_JAHRGANGSSTUFE_7("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_7"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_8")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_JAHRGANGSSTUFE_8("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_8"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_9")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_JAHRGANGSSTUFE_9("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/jahrgangsstufe_9"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/nicht_zugeordnet")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_NICHT_ZUGEORDNET("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/nicht_zugeordnet"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/primarstufe")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_PRIMARSTUFE("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/primarstufe"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/qualifikationsphase_1")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_QUALIFIKATIONSPHASE_1("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/qualifikationsphase_1"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/qualifikationsphase_2")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_QUALIFIKATIONSPHASE_2("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/qualifikationsphase_2"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/qualifikationsphase_3")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_QUALIFIKATIONSPHASE_3("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/qualifikationsphase_3"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/qualifikationsphase_4")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_QUALIFIKATIONSPHASE_4("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/qualifikationsphase_4"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/sammelklasse_umow")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_SAMMELKLASSE_UMOW("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/sammelklasse_umow"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/schulkindergarten")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_SCHULKINDERGARTEN("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/schulkindergarten"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/sekundarstufe_i")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_SEKUNDARSTUFE_I("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/sekundarstufe_i"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/ubergreifend_klasse")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_UBERGREIFEND_KLASSE("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/ubergreifend_klasse"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/uebergreifend_primar")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_UEBERGREIFEND_PRIMAR("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/uebergreifend_primar"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/uebergreifend_primar_sekundar_i")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_UEBERGREIFEND_PRIMAR_SEKUNDAR_I("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/uebergreifend_primar_sekundar_i"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/uebergreifend_sekundar_i")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_UEBERGREIFEND_SEKUNDAR_I("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/uebergreifend_sekundar_i"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/uebergreifend_sekundar_i_ii")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_UEBERGREIFEND_SEKUNDAR_I_II("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/uebergreifend_sekundar_i_ii"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/uebergreifend_sekundar_ii")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_UEBERGREIFEND_SEKUNDAR_II("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/uebergreifend_sekundar_ii"),
    @XmlEnumValue("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/vorklasse")
    HTTP_XSCHULE_DIGITAL_DEF_XSCHULE_1_0_CODE_JAHRGANGSSTUFE_VORKLASSE("http://xschule.digital/def/xschule/1.0/code/jahrgangsstufe/vorklasse");
    private final String value;

    JahrgangsstufeCodes(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static JahrgangsstufeCodes fromValue(String v) {
        for (JahrgangsstufeCodes c: JahrgangsstufeCodes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
