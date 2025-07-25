//
// This file was generated by the Eclipse Implementation of JAXB, v3.0.0 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2025.07.15 at 12:28:12 PM CEST 
//


package digital.xschule.def.xschule._1_1.xsd;

import java.util.ArrayList;
import java.util.List;
import de.xbildung.def.xbildung._1_1.xsd.CodeGesetzlicherVertreter;
import de.xbildung.def.xbildung._1_1.xsd.TeilbekanntesDatum;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * Mit diesem Datentyp werden die Daten des gesetzlichen Vertreters der betroffenen Person abgebildet.
 * 
 * <p>Java class for GesetzlicherVertreter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GesetzlicherVertreter"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="gesetzlicherVertreterSchluessel" type="{http://xbildung.de/def/xbildung/1.1/xsd}Code.GesetzlicherVertreter"/&gt;
 *         &lt;element name="tagDerBeendigung" type="{http://xbildung.de/def/xbildung/1.1/xsd}TeilbekanntesDatum" minOccurs="0"/&gt;
 *         &lt;element name="auskunftssperreGueltigkeit" type="{http://xschule.digital/def/xschule/1.1/xsd}Zeitraum" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="anschrift" type="{http://xschule.digital/def/xschule/1.1/xsd}Anschrift" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="kontakt" type="{http://xschule.digital/def/xschule/1.1/xsd}Kommunikation" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="weitereDaten"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;choice&gt;
 *                   &lt;element name="natuerlichePerson"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="name" type="{http://xschule.digital/def/xschule/1.1/xsd}NameNatuerlichePerson"/&gt;
 *                             &lt;element name="geburt" type="{http://xbildung.de/def/xbildung/1.1/xsd}TeilbekanntesDatum" minOccurs="0"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="organisation"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="name" type="{http://xschule.digital/def/xschule/1.1/xsd}NameOrganisation"/&gt;
 *                             &lt;element name="ansprechpartner" type="{http://xschule.digital/def/xschule/1.1/xsd}NameNatuerlichePerson" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/choice&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GesetzlicherVertreter", propOrder = {
    "gesetzlicherVertreterSchluessel",
    "tagDerBeendigung",
    "auskunftssperreGueltigkeit",
    "anschrift",
    "kontakt",
    "weitereDaten"
})
@XmlSeeAlso({
    digital.xschule.def.xschule._1_1.xsd.SchuelerWechsel0001 .Erziehungsberechtigter.class
})
public class GesetzlicherVertreter {

    @XmlElement(required = true)
    protected CodeGesetzlicherVertreter gesetzlicherVertreterSchluessel;
    protected TeilbekanntesDatum tagDerBeendigung;
    protected List<Zeitraum> auskunftssperreGueltigkeit;
    protected List<Anschrift> anschrift;
    protected List<Kommunikation> kontakt;
    @XmlElement(required = true)
    protected GesetzlicherVertreter.WeitereDaten weitereDaten;

    /**
     * Gets the value of the gesetzlicherVertreterSchluessel property.
     * 
     * @return
     *     possible object is
     *     {@link CodeGesetzlicherVertreter }
     *     
     */
    public CodeGesetzlicherVertreter getGesetzlicherVertreterSchluessel() {
        return gesetzlicherVertreterSchluessel;
    }

    /**
     * Sets the value of the gesetzlicherVertreterSchluessel property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodeGesetzlicherVertreter }
     *     
     */
    public void setGesetzlicherVertreterSchluessel(CodeGesetzlicherVertreter value) {
        this.gesetzlicherVertreterSchluessel = value;
    }

    /**
     * Gets the value of the tagDerBeendigung property.
     * 
     * @return
     *     possible object is
     *     {@link TeilbekanntesDatum }
     *     
     */
    public TeilbekanntesDatum getTagDerBeendigung() {
        return tagDerBeendigung;
    }

    /**
     * Sets the value of the tagDerBeendigung property.
     * 
     * @param value
     *     allowed object is
     *     {@link TeilbekanntesDatum }
     *     
     */
    public void setTagDerBeendigung(TeilbekanntesDatum value) {
        this.tagDerBeendigung = value;
    }

    /**
     * Gets the value of the auskunftssperreGueltigkeit property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the auskunftssperreGueltigkeit property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAuskunftssperreGueltigkeit().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Zeitraum }
     * 
     * 
     */
    public List<Zeitraum> getAuskunftssperreGueltigkeit() {
        if (auskunftssperreGueltigkeit == null) {
            auskunftssperreGueltigkeit = new ArrayList<Zeitraum>();
        }
        return this.auskunftssperreGueltigkeit;
    }

    /**
     * Gets the value of the anschrift property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the anschrift property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAnschrift().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Anschrift }
     * 
     * 
     */
    public List<Anschrift> getAnschrift() {
        if (anschrift == null) {
            anschrift = new ArrayList<Anschrift>();
        }
        return this.anschrift;
    }

    /**
     * Gets the value of the kontakt property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the kontakt property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getKontakt().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Kommunikation }
     * 
     * 
     */
    public List<Kommunikation> getKontakt() {
        if (kontakt == null) {
            kontakt = new ArrayList<Kommunikation>();
        }
        return this.kontakt;
    }

    /**
     * Gets the value of the weitereDaten property.
     * 
     * @return
     *     possible object is
     *     {@link GesetzlicherVertreter.WeitereDaten }
     *     
     */
    public GesetzlicherVertreter.WeitereDaten getWeitereDaten() {
        return weitereDaten;
    }

    /**
     * Sets the value of the weitereDaten property.
     * 
     * @param value
     *     allowed object is
     *     {@link GesetzlicherVertreter.WeitereDaten }
     *     
     */
    public void setWeitereDaten(GesetzlicherVertreter.WeitereDaten value) {
        this.weitereDaten = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;choice&gt;
     *         &lt;element name="natuerlichePerson"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="name" type="{http://xschule.digital/def/xschule/1.1/xsd}NameNatuerlichePerson"/&gt;
     *                   &lt;element name="geburt" type="{http://xbildung.de/def/xbildung/1.1/xsd}TeilbekanntesDatum" minOccurs="0"/&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="organisation"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="name" type="{http://xschule.digital/def/xschule/1.1/xsd}NameOrganisation"/&gt;
     *                   &lt;element name="ansprechpartner" type="{http://xschule.digital/def/xschule/1.1/xsd}NameNatuerlichePerson" maxOccurs="unbounded" minOccurs="0"/&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *       &lt;/choice&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "natuerlichePerson",
        "organisation"
    })
    public static class WeitereDaten {

        protected GesetzlicherVertreter.WeitereDaten.NatuerlichePerson natuerlichePerson;
        protected GesetzlicherVertreter.WeitereDaten.Organisation organisation;

        /**
         * Gets the value of the natuerlichePerson property.
         * 
         * @return
         *     possible object is
         *     {@link GesetzlicherVertreter.WeitereDaten.NatuerlichePerson }
         *     
         */
        public GesetzlicherVertreter.WeitereDaten.NatuerlichePerson getNatuerlichePerson() {
            return natuerlichePerson;
        }

        /**
         * Sets the value of the natuerlichePerson property.
         * 
         * @param value
         *     allowed object is
         *     {@link GesetzlicherVertreter.WeitereDaten.NatuerlichePerson }
         *     
         */
        public void setNatuerlichePerson(GesetzlicherVertreter.WeitereDaten.NatuerlichePerson value) {
            this.natuerlichePerson = value;
        }

        /**
         * Gets the value of the organisation property.
         * 
         * @return
         *     possible object is
         *     {@link GesetzlicherVertreter.WeitereDaten.Organisation }
         *     
         */
        public GesetzlicherVertreter.WeitereDaten.Organisation getOrganisation() {
            return organisation;
        }

        /**
         * Sets the value of the organisation property.
         * 
         * @param value
         *     allowed object is
         *     {@link GesetzlicherVertreter.WeitereDaten.Organisation }
         *     
         */
        public void setOrganisation(GesetzlicherVertreter.WeitereDaten.Organisation value) {
            this.organisation = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="name" type="{http://xschule.digital/def/xschule/1.1/xsd}NameNatuerlichePerson"/&gt;
         *         &lt;element name="geburt" type="{http://xbildung.de/def/xbildung/1.1/xsd}TeilbekanntesDatum" minOccurs="0"/&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "name",
            "geburt"
        })
        public static class NatuerlichePerson {

            @XmlElement(required = true)
            protected NameNatuerlichePerson name;
            protected TeilbekanntesDatum geburt;

            /**
             * Gets the value of the name property.
             * 
             * @return
             *     possible object is
             *     {@link NameNatuerlichePerson }
             *     
             */
            public NameNatuerlichePerson getName() {
                return name;
            }

            /**
             * Sets the value of the name property.
             * 
             * @param value
             *     allowed object is
             *     {@link NameNatuerlichePerson }
             *     
             */
            public void setName(NameNatuerlichePerson value) {
                this.name = value;
            }

            /**
             * Gets the value of the geburt property.
             * 
             * @return
             *     possible object is
             *     {@link TeilbekanntesDatum }
             *     
             */
            public TeilbekanntesDatum getGeburt() {
                return geburt;
            }

            /**
             * Sets the value of the geburt property.
             * 
             * @param value
             *     allowed object is
             *     {@link TeilbekanntesDatum }
             *     
             */
            public void setGeburt(TeilbekanntesDatum value) {
                this.geburt = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="name" type="{http://xschule.digital/def/xschule/1.1/xsd}NameOrganisation"/&gt;
         *         &lt;element name="ansprechpartner" type="{http://xschule.digital/def/xschule/1.1/xsd}NameNatuerlichePerson" maxOccurs="unbounded" minOccurs="0"/&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "name",
            "ansprechpartner"
        })
        public static class Organisation {

            @XmlElement(required = true)
            protected NameOrganisation name;
            protected List<NameNatuerlichePerson> ansprechpartner;

            /**
             * Gets the value of the name property.
             * 
             * @return
             *     possible object is
             *     {@link NameOrganisation }
             *     
             */
            public NameOrganisation getName() {
                return name;
            }

            /**
             * Sets the value of the name property.
             * 
             * @param value
             *     allowed object is
             *     {@link NameOrganisation }
             *     
             */
            public void setName(NameOrganisation value) {
                this.name = value;
            }

            /**
             * Gets the value of the ansprechpartner property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the Jakarta XML Binding object.
             * This is why there is not a <CODE>set</CODE> method for the ansprechpartner property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getAnsprechpartner().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link NameNatuerlichePerson }
             * 
             * 
             */
            public List<NameNatuerlichePerson> getAnsprechpartner() {
                if (ansprechpartner == null) {
                    ansprechpartner = new ArrayList<NameNatuerlichePerson>();
                }
                return this.ansprechpartner;
            }

        }

    }

}
