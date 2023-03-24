package de.nrw.schule.svws.core.types.kaoa;

import java.util.HashMap;

import de.nrw.schule.svws.core.data.kaoa.KAOAZusatzmerkmalEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die KAoA-Zusatzmerkmal.
 */
public enum KAOAZusatzmerkmal {

    /** KAoA-Zusatzmerkmal: Schulisches individuelles Beratungsgespräch durchgeführt */
    SBO_2_1_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(13, "SBO 2.1.1", "Schulisches individuelles Beratungsgespräch durchgeführt", KAOAMerkmal.SBO_2_1, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Schulisches individuelles Beratungsgespräch nicht durchgeführt */
    SBO_2_1_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(14, "SBO 2.1.2", "Schulisches individuelles Beratungsgespräch nicht durchgeführt", KAOAMerkmal.SBO_2_1, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Teilnahme an einem berufsorientierenden Angebote der Berufsberatung */
    SBO_2_2_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(15, "SBO 2.2.1", "Teilnahme an einem berufsorientierenden Angebote der Berufsberatung", KAOAMerkmal.SBO_2_2, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Keine Teilnahme an einem berufsorientierenden Angebote der Berufsberatung */
    SBO_2_2_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(16, "SBO 2.2.2", "Keine Teilnahme an einem berufsorientierenden Angebote der Berufsberatung", KAOAMerkmal.SBO_2_2, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Berufsberatung der Agentur für Arbeit (BA) */
    SBO_2_3_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(17, "SBO 2.3.1", "Berufsberatung der Agentur für Arbeit (BA)", KAOAMerkmal.SBO_2_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Beratungsgespräch der Jugendberufsagentur */
    SBO_2_3_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(18, "SBO 2.3.2", "Beratungsgespräch der Jugendberufsagentur", KAOAMerkmal.SBO_2_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Beratungsgespräch des Jobcenters */
    SBO_2_3_3(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(19, "SBO 2.3.3", "Beratungsgespräch des Jobcenters", KAOAMerkmal.SBO_2_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Beratungsgespräch Jugendsozialarbeit */
    SBO_2_3_4(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(20, "SBO 2.3.4", "Beratungsgespräch Jugendsozialarbeit", KAOAMerkmal.SBO_2_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Beratungsgespräch eines anderen außerschulischen Partners */
    SBO_2_3_5(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(21, "SBO 2.3.5", "Beratungsgespräch eines anderen außerschulischen Partners", KAOAMerkmal.SBO_2_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Kein Beratungskontakt zu einem außerschulischen Partner */
    SBO_2_3_6(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(22, "SBO 2.3.6", "Kein Beratungskontakt zu einem außerschulischen Partner", KAOAMerkmal.SBO_2_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: STAR - Berufswegekonferenz durchgeführt */
    SBO_2_4_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(23, "SBO 2.4.1", "STAR - Berufswegekonferenz durchgeführt", KAOAMerkmal.SBO_2_4, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: STAR - Berufswegekonferenz nicht durchgeführt */
    SBO_2_4_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(24, "SBO 2.4.2", "STAR - Berufswegekonferenz nicht durchgeführt", KAOAMerkmal.SBO_2_4, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Teilnahme der Eltern bzw. Erziehungsberechtigten an Beratungs- und Informationsveranstaltungen */
    SBO_2_5_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(25, "SBO 2.5.1", "Teilnahme der Eltern bzw. Erziehungsberechtigten an Beratungs- und Informationsveranstaltungen", KAOAMerkmal.SBO_2_5, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Keine Teilnahme der Eltern bzw. Erziehungsberechtigten an Beratungs- und Informationsveranstaltungen */
    SBO_2_5_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(26, "SBO 2.5.2", "Keine Teilnahme der Eltern bzw. Erziehungsberechtigten an Beratungs- und Informationsveranstaltungen", KAOAMerkmal.SBO_2_5, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Teilnahme der Eltern bzw. Erziehungsberechtigten an STAR - Beratungs- und Informationsveranstaltungen */
    SBO_2_6_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(27, "SBO 2.6.1", "Teilnahme der Eltern bzw. Erziehungsberechtigten an STAR - Beratungs- und Informationsveranstaltungen", KAOAMerkmal.SBO_2_6, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Keine Teilnahme der Eltern bzw. Erziehungsberechtigten an STAR - Beratungs- und Informationsveranstaltungen */
    SBO_2_6_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(28, "SBO 2.6.2", "Keine Teilnahme der Eltern bzw. Erziehungsberechtigten an STAR - Beratungs- und Informationsveranstaltungen", KAOAMerkmal.SBO_2_6, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Berufwahlpass NRW SekI/II erhalten */
    SBO_3_4_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(29, "SBO 3.4.1", "Berufwahlpass NRW SekI/II erhalten", KAOAMerkmal.SBO_3_4, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Berufwahlpass NRW Leichte Sprache erhalten */
    SBO_3_4_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(30, "SBO 3.4.2", "Berufwahlpass NRW Leichte Sprache erhalten", KAOAMerkmal.SBO_3_4, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Berufswahlpass NRW kompakt erhalten */
    SBO_3_4_3(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(31, "SBO 3.4.3", "Berufswahlpass NRW kompakt erhalten", KAOAMerkmal.SBO_3_4, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)
    }),

    /** KAoA-Zusatzmerkmal: anderes Portfolioinstrument  erhalten */
    SBO_3_4_4(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(32, "SBO 3.4.4", "anderes Portfolioinstrument  erhalten", KAOAMerkmal.SBO_3_4, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: kein Portfolioinstrument erhalten */
    SBO_3_4_5(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(33, "SBO 3.4.5", "kein Portfolioinstrument erhalten", KAOAMerkmal.SBO_3_4, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: An der Potenzialanalyse teilgenommen */
    SBO_4_1_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(34, "SBO 4.1.1", "An der Potenzialanalyse teilgenommen", KAOAMerkmal.SBO_4_1, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Keine Teilnahme an der Potenzialanalyse */
    SBO_4_1_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(35, "SBO 4.1.2", "Keine Teilnahme an der Potenzialanalyse", KAOAMerkmal.SBO_4_1, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: An der Potenzialanalyse teilgenommen */
    SBO_4_2_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(36, "SBO 4.2.1", "An der Potenzialanalyse teilgenommen", KAOAMerkmal.SBO_4_2, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Keine Teilnahme an der Potenzialanalyse */
    SBO_4_2_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(37, "SBO 4.2.2", "Keine Teilnahme an der Potenzialanalyse", KAOAMerkmal.SBO_4_2, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: An der STAR-Potenzialanalyse teilgenommen */
    SBO_4_3_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(38, "SBO 4.3.1", "An der STAR-Potenzialanalyse teilgenommen", KAOAMerkmal.SBO_4_3, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Keine Teilnahme an der Potenzialanalyse */
    SBO_4_3_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(39, "SBO 4.3.2", "Keine Teilnahme an der Potenzialanalyse", KAOAMerkmal.SBO_4_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: An der Festellung des funktionalen Sehvermögens teilgenommen */
    SBO_4_4_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(40, "SBO 4.4.1", "An der Festellung des funktionalen Sehvermögens teilgenommen", KAOAMerkmal.SBO_4_4, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Keine Teilnahme an der Feststellung des funktionalen Sehrvermögens */
    SBO_4_4_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(41, "SBO 4.4.2", "Keine Teilnahme an der Feststellung des funktionalen Sehrvermögens", KAOAMerkmal.SBO_4_4, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: An der STAR-Potenzialanalyse teilgenommen */
    SBO_4_5_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(42, "SBO 4.5.1", "An der STAR-Potenzialanalyse teilgenommen", KAOAMerkmal.SBO_4_5, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Keine Teilnahme an der Potenzialanalyse */
    SBO_4_5_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(43, "SBO 4.5.2", "Keine Teilnahme an der Potenzialanalyse", KAOAMerkmal.SBO_4_5, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: An der betrieblichen Berufsfelderkundung teilgenommen - 1. Tag */
    SBO_5_1_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(44, "SBO 5.1.1", "An der betrieblichen Berufsfelderkundung teilgenommen - 1. Tag", KAOAMerkmal.SBO_5_1, KAOAZusatzmerkmaleOptionsarten.BERUFSFELD, null, null)
    }),

    /** KAoA-Zusatzmerkmal: An der betrieblichen Berufsfelderkundung teilgenommen - 2. Tag */
    SBO_5_1_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(45, "SBO 5.1.2", "An der betrieblichen Berufsfelderkundung teilgenommen - 2. Tag", KAOAMerkmal.SBO_5_1, KAOAZusatzmerkmaleOptionsarten.BERUFSFELD, null, null)
    }),

    /** KAoA-Zusatzmerkmal: An der betrieblichen Berufsfelderkundung teilgenommen - 3. Tag */
    SBO_5_1_3(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(46, "SBO 5.1.3", "An der betrieblichen Berufsfelderkundung teilgenommen - 3. Tag", KAOAMerkmal.SBO_5_1, KAOAZusatzmerkmaleOptionsarten.BERUFSFELD, null, null)
    }),

    /** KAoA-Zusatzmerkmal: An der betrieblichen Berufsfelderkundung teilgenommen - mehr als drei Tage */
    SBO_5_1_4(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(47, "SBO 5.1.4", "An der betrieblichen Berufsfelderkundung teilgenommen - mehr als drei Tage", KAOAMerkmal.SBO_5_1, KAOAZusatzmerkmaleOptionsarten.BERUFSFELD, null, null)
    }),

    /** KAoA-Zusatzmerkmal: An der trägergestützten Berufsfelderkundung teilgenommen - 1. Tag */
    SBO_5_1_5(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(48, "SBO 5.1.5", "An der trägergestützten Berufsfelderkundung teilgenommen - 1. Tag", KAOAMerkmal.SBO_5_1, KAOAZusatzmerkmaleOptionsarten.BERUFSFELD, null, null)
    }),

    /** KAoA-Zusatzmerkmal: An der trägergestützten Berufsfelderkundung teilgenommen - 2. Tag */
    SBO_5_1_6(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(49, "SBO 5.1.6", "An der trägergestützten Berufsfelderkundung teilgenommen - 2. Tag", KAOAMerkmal.SBO_5_1, KAOAZusatzmerkmaleOptionsarten.BERUFSFELD, null, null)
    }),

    /** KAoA-Zusatzmerkmal: An der trägergestützten Berufsfelderkundung teilgenommen - 3. Tag */
    SBO_5_1_7(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(50, "SBO 5.1.7", "An der trägergestützten Berufsfelderkundung teilgenommen - 3. Tag", KAOAMerkmal.SBO_5_1, KAOAZusatzmerkmaleOptionsarten.BERUFSFELD, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Keine Teilnahme an den betrieblichen oder trägergestütztenBerufsfelderkundungen */
    SBO_5_1_8(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(51, "SBO 5.1.8", "Keine Teilnahme an den betrieblichen oder trägergestütztenBerufsfelderkundungen", KAOAMerkmal.SBO_5_1, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: An der betrieblichen STAR - Berufsfelderkundung teilgenommen - 1. Tag */
    SBO_5_2_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(52, "SBO 5.2.1", "An der betrieblichen STAR - Berufsfelderkundung teilgenommen - 1. Tag", KAOAMerkmal.SBO_5_2, KAOAZusatzmerkmaleOptionsarten.BERUFSFELD, null, null)
    }),

    /** KAoA-Zusatzmerkmal: An der betrieblichen STAR - Berufsfelderkundung teilgenommen - 2. Tag */
    SBO_5_2_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(53, "SBO 5.2.2", "An der betrieblichen STAR - Berufsfelderkundung teilgenommen - 2. Tag", KAOAMerkmal.SBO_5_2, KAOAZusatzmerkmaleOptionsarten.BERUFSFELD, null, null)
    }),

    /** KAoA-Zusatzmerkmal: An der betrieblichen STAR - Berufsfelderkundung teilgenommen - 3. Tag */
    SBO_5_2_3(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(54, "SBO 5.2.3", "An der betrieblichen STAR - Berufsfelderkundung teilgenommen - 3. Tag", KAOAMerkmal.SBO_5_2, KAOAZusatzmerkmaleOptionsarten.BERUFSFELD, null, null)
    }),

    /** KAoA-Zusatzmerkmal: An der betrieblichen STAR - Berufsfelderkundung teilgenommen - weitere Tage */
    SBO_5_2_4(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(55, "SBO 5.2.4", "An der betrieblichen STAR - Berufsfelderkundung teilgenommen - weitere Tage", KAOAMerkmal.SBO_5_2, KAOAZusatzmerkmaleOptionsarten.BERUFSFELD, null, null)
    }),

    /** KAoA-Zusatzmerkmal: An der trägergestützten STAR - Berufsfelderkundung teilgenommen - 1. Tag */
    SBO_5_2_5(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(56, "SBO 5.2.5", "An der trägergestützten STAR - Berufsfelderkundung teilgenommen - 1. Tag", KAOAMerkmal.SBO_5_2, KAOAZusatzmerkmaleOptionsarten.BERUFSFELD, null, null)
    }),

    /** KAoA-Zusatzmerkmal: An der trägergestützten STAR - Berufsfelderkundung teilgenommen - 2. Tag */
    SBO_5_2_6(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(57, "SBO 5.2.6", "An der trägergestützten STAR - Berufsfelderkundung teilgenommen - 2. Tag", KAOAMerkmal.SBO_5_2, KAOAZusatzmerkmaleOptionsarten.BERUFSFELD, null, null)
    }),

    /** KAoA-Zusatzmerkmal: An der trägergestützten STAR - Berufsfelderkundung teilgenommen - 3. Tag */
    SBO_5_2_7(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(58, "SBO 5.2.7", "An der trägergestützten STAR - Berufsfelderkundung teilgenommen - 3. Tag", KAOAMerkmal.SBO_5_2, KAOAZusatzmerkmaleOptionsarten.BERUFSFELD, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Keine Teilnahme an den betrieblichen oder trägergestützten STAR - Berufsfelderkundungen */
    SBO_5_2_8(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(59, "SBO 5.2.8", "Keine Teilnahme an den betrieblichen oder trägergestützten STAR - Berufsfelderkundungen", KAOAMerkmal.SBO_5_2, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Am STAR - Arbeitsplatzbezogenes Kommunikationstraining I teilgenommen */
    SBO_5_3_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(60, "SBO 5.3.1", "Am STAR - Arbeitsplatzbezogenes Kommunikationstraining I teilgenommen", KAOAMerkmal.SBO_5_3, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Keine Teilnahme am STAR - Arbeitsplatzbezogenes Kommunikationstraining I */
    SBO_5_3_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(61, "SBO 5.3.2", "Keine Teilnahme am STAR - Arbeitsplatzbezogenes Kommunikationstraining I", KAOAMerkmal.SBO_5_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Am STAR - Berufsorientierungsseminar teilgenommen */
    SBO_5_4_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(62, "SBO 5.4.1", "Am STAR - Berufsorientierungsseminar teilgenommen", KAOAMerkmal.SBO_5_4, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Keine Teilnahme am STAR - Berufsorientierungsseminar */
    SBO_5_4_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(63, "SBO 5.4.2", "Keine Teilnahme am STAR - Berufsorientierungsseminar", KAOAMerkmal.SBO_5_4, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Am Betriebspraktikum teilgenommen - 1 Woche */
    SBO_6_1_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(64, "SBO 6.1.1", "Am Betriebspraktikum teilgenommen - 1 Woche", KAOAMerkmal.SBO_6_1, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Am Betriebspraktikum teilgenommen - 2 Wochen */
    SBO_6_1_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(65, "SBO 6.1.2", "Am Betriebspraktikum teilgenommen - 2 Wochen", KAOAMerkmal.SBO_6_1, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Am Betriebspraktikum Sek I teilgenommen - mehr als zwei Wochen */
    SBO_6_1_3(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(66, "SBO 6.1.3", "Am Betriebspraktikum Sek I teilgenommen - mehr als zwei Wochen", KAOAMerkmal.SBO_6_1, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Zusätzliche Praktika wie z. B. Schnupperpraktika */
    SBO_6_1_4(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(67, "SBO 6.1.4", "Zusätzliche Praktika wie z. B. Schnupperpraktika", KAOAMerkmal.SBO_6_1, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Keine Teilnahme am Betriebspraktikum */
    SBO_6_1_5(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(68, "SBO 6.1.5", "Keine Teilnahme am Betriebspraktikum", KAOAMerkmal.SBO_6_1, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Keine Teilnahme am Intensivtraining TASK */
    SBO_6_2_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(69, "SBO 6.2.2", "Keine Teilnahme am Intensivtraining TASK", KAOAMerkmal.SBO_6_2, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum im Block - eine Woche teilgenommen */
    SBO_6_3_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(70, "SBO 6.3.1", "Am STAR - Betriebspraktikum im Block - eine Woche teilgenommen", KAOAMerkmal.SBO_6_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum im Block - zwei Wochen teilgenommen */
    SBO_6_3_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(71, "SBO 6.3.2", "Am STAR - Betriebspraktikum im Block - zwei Wochen teilgenommen", KAOAMerkmal.SBO_6_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum im Block - drei Wochen teilgenommen */
    SBO_6_3_3(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(72, "SBO 6.3.3", "Am STAR - Betriebspraktikum im Block - drei Wochen teilgenommen", KAOAMerkmal.SBO_6_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum im Block - vier Wochen teilgenommen */
    SBO_6_3_4(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(73, "SBO 6.3.4", "Am STAR - Betriebspraktikum im Block - vier Wochen teilgenommen", KAOAMerkmal.SBO_6_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum im Block - fünf Wochen teilgenommen */
    SBO_6_3_5(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(74, "SBO 6.3.5", "Am STAR - Betriebspraktikum im Block - fünf Wochen teilgenommen", KAOAMerkmal.SBO_6_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum im Block - sechs Wochen teilgenommen */
    SBO_6_3_6(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(75, "SBO 6.3.6", "Am STAR - Betriebspraktikum im Block - sechs Wochen teilgenommen", KAOAMerkmal.SBO_6_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Keine Teilnahme am STAR - Betriebspraktikum im Block */
    SBO_6_3_7(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(76, "SBO 6.3.7", "Keine Teilnahme am STAR - Betriebspraktikum im Block", KAOAMerkmal.SBO_6_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: An trägergestützten Praxiskursen teilgenommen */
    SBO_6_4_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(77, "SBO 6.4.1", "An trägergestützten Praxiskursen teilgenommen", KAOAMerkmal.SBO_6_4, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)
    }),

    /** KAoA-Zusatzmerkmal: An betrieblichen Praxiskursen teilgenommen */
    SBO_6_4_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(78, "SBO 6.4.2", "An betrieblichen Praxiskursen teilgenommen", KAOAMerkmal.SBO_6_4, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Keine Teilnahme an Praxiskursen */
    SBO_6_4_3(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(79, "SBO 6.4.3", "Keine Teilnahme an Praxiskursen", KAOAMerkmal.SBO_6_4, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Am Langzeitpraktikum teilgenommen */
    SBO_6_5_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(80, "SBO 6.5.1", "Am Langzeitpraktikum teilgenommen", KAOAMerkmal.SBO_6_5, KAOAZusatzmerkmaleOptionsarten.SBO_EBENE_4, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Langzeitpraktikum abgebrochen */
    SBO_6_5_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(81, "SBO 6.5.2", "Langzeitpraktikum abgebrochen", KAOAMerkmal.SBO_6_5, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum in Langzeit 1-tägig teilgenommen */
    SBO_6_6_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(82, "SBO 6.6.1", "Am STAR - Betriebspraktikum in Langzeit 1-tägig teilgenommen", KAOAMerkmal.SBO_6_6, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Am STAR - Betriebspraktikum in Langzeit 2-tägig teilgenommen */
    SBO_6_6_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(83, "SBO 6.6.2", "Am STAR - Betriebspraktikum in Langzeit 2-tägig teilgenommen", KAOAMerkmal.SBO_6_6, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)
    }),

    /** KAoA-Zusatzmerkmal: STAR - Betriebspraktikum in Langzeit abgebrochen */
    SBO_6_6_3(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(84, "SBO 6.6.3", "STAR - Betriebspraktikum in Langzeit abgebrochen", KAOAMerkmal.SBO_6_6, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: An KAoA-kompakt teilgenommen */
    SBO_7_1_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(85, "SBO 7.1.1", "An KAoA-kompakt teilgenommen", KAOAMerkmal.SBO_7_1, KAOAZusatzmerkmaleOptionsarten.SBO_EBENE_4, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Keine Teilnahme an KAoA-kompakt */
    SBO_7_1_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(86, "SBO 7.1.2", "Keine Teilnahme an KAoA-kompakt", KAOAMerkmal.SBO_7_1, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Am Workshop "Standortbestimmung Reflexionsworkshop Sek. II" teilgenommen */
    SBO_8_1_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(87, "SBO 8.1.1", "Am Workshop \"Standortbestimmung Reflexionsworkshop Sek. II\" teilgenommen", KAOAMerkmal.SBO_8_1, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Keine Teilnahme am Workshop "Standortbestimmung Reflexionsworkshop Sek. II" */
    SBO_8_1_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(88, "SBO 8.1.2", "Keine Teilnahme am Workshop \"Standortbestimmung Reflexionsworkshop Sek. II\"", KAOAMerkmal.SBO_8_1, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Am Workshop "Stärkung der Entscheidungskompetenz I - Sek. II" teilgenommen */
    SBO_8_2_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(89, "SBO 8.2.1", "Am Workshop \"Stärkung der Entscheidungskompetenz I - Sek. II\" teilgenommen", KAOAMerkmal.SBO_8_2, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Keine Teilnahme am Workshop "Stärkung der Entscheidungskompetenz I - Sek. II" */
    SBO_8_2_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(90, "SBO 8.2.2", "Keine Teilnahme am Workshop \"Stärkung der Entscheidungskompetenz I - Sek. II\"", KAOAMerkmal.SBO_8_2, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: An Praxiselementen in Betrieben, Hochschulen, Institutionen teilgenommen */
    SBO_9_1_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(91, "SBO 9.1.1", "An Praxiselementen in Betrieben, Hochschulen, Institutionen teilgenommen", KAOAMerkmal.SBO_9_1, KAOAZusatzmerkmaleOptionsarten.SBO_EBENE_4, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Keine Teilnahme an den Praxiselementen */
    SBO_9_1_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(92, "SBO 9.1.2", "Keine Teilnahme an den Praxiselementen", KAOAMerkmal.SBO_9_1, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Am Betriebspraktikum teilgenommen - 1 Woche (Sek II)  */
    SBO_9_1_3(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(93, "SBO 9.1.3", "Am Betriebspraktikum teilgenommen - 1 Woche (Sek II) ", KAOAMerkmal.SBO_9_1, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Am Betriebspraktikum teilgenommen - 2 Wochen (Sek II)  */
    SBO_9_1_4(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(94, "SBO 9.1.4", "Am Betriebspraktikum teilgenommen - 2 Wochen (Sek II) ", KAOAMerkmal.SBO_9_1, KAOAZusatzmerkmaleOptionsarten.FREITEXT_BERUF, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Teilnahme an den Veranstaltungen zur Studienorientierung */
    SBO_9_2_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(95, "SBO 9.2.1", "Teilnahme an den Veranstaltungen zur Studienorientierung", KAOAMerkmal.SBO_9_2, KAOAZusatzmerkmaleOptionsarten.SBO_EBENE_4, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Keine Teilnahme an den Veranstaltungen zur Studienorientierung */
    SBO_9_2_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(96, "SBO 9.2.2", "Keine Teilnahme an den Veranstaltungen zur Studienorientierung", KAOAMerkmal.SBO_9_2, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Am Workshop "Stärkung der Entscheidungskompetenz II - Sek II" teilgenommen */
    SBO_9_3_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(97, "SBO 9.3.1", "Am Workshop \"Stärkung der Entscheidungskompetenz II - Sek II\" teilgenommen", KAOAMerkmal.SBO_9_3, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Keine Teilnahme am Workshop "Stärkung der Entscheidungskompetenz II - Sek II" */
    SBO_9_3_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(98, "SBO 9.3.2", "Keine Teilnahme am Workshop \"Stärkung der Entscheidungskompetenz II - Sek II\"", KAOAMerkmal.SBO_9_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Bewerbungstraining wurde durchgeführt */
    SBO_10_1_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(99, "SBO 10.1.1", "Bewerbungstraining wurde durchgeführt", KAOAMerkmal.SBO_10_1, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Keine Teilnahme an einem Bewerbungstraining */
    SBO_10_1_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(100, "SBO 10.1.2", "Keine Teilnahme an einem Bewerbungstraining", KAOAMerkmal.SBO_10_1, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Am STAR - Arbeitsplatzbezogenen Kommunikationstraining II teilgenommen */
    SBO_10_2_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(101, "SBO 10.2.1", "Am STAR - Arbeitsplatzbezogenen Kommunikationstraining II teilgenommen", KAOAMerkmal.SBO_10_2, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Keine Teilnahme am STAR - Arbeitsplatzbezogenen Kommunikationstraining II */
    SBO_10_2_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(102, "SBO 10.2.2", "Keine Teilnahme am STAR - Arbeitsplatzbezogenen Kommunikationstraining II", KAOAMerkmal.SBO_10_2, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Am STAR - Bewerbungstraining teilgenommen */
    SBO_10_3_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(103, "SBO 10.3.1", "Am STAR - Bewerbungstraining teilgenommen", KAOAMerkmal.SBO_10_3, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Keine Teilnahme am STAR - Bewerbungstraining */
    SBO_10_3_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(104, "SBO 10.3.2", "Keine Teilnahme am STAR - Bewerbungstraining", KAOAMerkmal.SBO_10_3, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: durch die Jugendhilfe */
    SBO_10_4_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(105, "SBO 10.4.1", "durch die Jugendhilfe", KAOAMerkmal.SBO_10_4, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: durch die Schulsozialarbeit */
    SBO_10_4_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(106, "SBO 10.4.2", "durch die Schulsozialarbeit", KAOAMerkmal.SBO_10_4, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: durch die Berufseinstiegsbegleitung */
    SBO_10_4_3(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(107, "SBO 10.4.3", "durch die Berufseinstiegsbegleitung", KAOAMerkmal.SBO_10_4, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: durch die Einstiegsbegleitung über die Kommune finanziert */
    SBO_10_4_4(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(108, "SBO 10.4.4", "durch die Einstiegsbegleitung über die Kommune finanziert", KAOAMerkmal.SBO_10_4, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: durch eine ehrenamtlich tätige Person */
    SBO_10_4_5(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(109, "SBO 10.4.5", "durch eine ehrenamtlich tätige Person", KAOAMerkmal.SBO_10_4, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: durch andere Institution */
    SBO_10_4_6(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(110, "SBO 10.4.6", "durch andere Institution", KAOAMerkmal.SBO_10_4, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Eine STAR - Übergangsbegleitung durch den Integrationsfachdienst (IFD) findet statt */
    SBO_10_5_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(111, "SBO 10.5.1", "Eine STAR - Übergangsbegleitung durch den Integrationsfachdienst (IFD) findet statt", KAOAMerkmal.SBO_10_5, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Keine STAR - Übergangsbegleitung durch den Integrationsfachdienst (IFD) */
    SBO_10_5_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(112, "SBO 10.5.2", "Keine STAR - Übergangsbegleitung durch den Integrationsfachdienst (IFD)", KAOAMerkmal.SBO_10_5, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Die Anschlussvereinbarung Sek. I ist ausgefüllt worden */
    SBO_10_6_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(113, "SBO 10.6.1", "Die Anschlussvereinbarung Sek. I ist ausgefüllt worden", KAOAMerkmal.SBO_10_6, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Die Anschlussvereinbarung Sek. I ist nicht ausgefüllt worden */
    SBO_10_6_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(114, "SBO 10.6.2", "Die Anschlussvereinbarung Sek. I ist nicht ausgefüllt worden", KAOAMerkmal.SBO_10_6, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Die Anschlussvereinbarung Sek. II ist ausgefüllt worden */
    SBO_10_6_3(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(115, "SBO 10.6.3", "Die Anschlussvereinbarung Sek. II ist ausgefüllt worden", KAOAMerkmal.SBO_10_6, KAOAZusatzmerkmaleOptionsarten.KEINE, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Die Anschlussvereinbarung Sek. II ist nicht ausgefüllt worden */
    SBO_10_6_4(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(116, "SBO 10.6.4", "Die Anschlussvereinbarung Sek. II ist nicht ausgefüllt worden", KAOAMerkmal.SBO_10_6, KAOAZusatzmerkmaleOptionsarten.FREITEXT, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Anschlüsse bei einem Abgangszeugnis für Schüler/innen mit sonderpädagogischem Unterstützungsbedarf */
    SBO_10_7_1(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(117, "SBO 10.7.1", "Anschlüsse bei einem Abgangszeugnis für Schüler/innen mit sonderpädagogischem Unterstützungsbedarf", KAOAMerkmal.SBO_10_7, KAOAZusatzmerkmaleOptionsarten.ANSCHLUSSOPTION, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Anschlüsse bei einem Abgangszeugnis */
    SBO_10_7_2(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(118, "SBO 10.7.2", "Anschlüsse bei einem Abgangszeugnis", KAOAMerkmal.SBO_10_7, KAOAZusatzmerkmaleOptionsarten.ANSCHLUSSOPTION, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Anschlüsse bei einem Abschlusszeugnis im Bildungsgang Geistige Entwicklung */
    SBO_10_7_3(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(119, "SBO 10.7.3", "Anschlüsse bei einem Abschlusszeugnis im Bildungsgang Geistige Entwicklung", KAOAMerkmal.SBO_10_7, KAOAZusatzmerkmaleOptionsarten.ANSCHLUSSOPTION, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Anschlüsse bei einem Abschlusszeugnis im Bildungsgang Lernen */
    SBO_10_7_4(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(120, "SBO 10.7.4", "Anschlüsse bei einem Abschlusszeugnis im Bildungsgang Lernen", KAOAMerkmal.SBO_10_7, KAOAZusatzmerkmaleOptionsarten.ANSCHLUSSOPTION, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Anschlüsse bei einem Hauptschulabschluss (HA9) oder diesem gleichwertig */
    SBO_10_7_5(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(121, "SBO 10.7.5", "Anschlüsse bei einem Hauptschulabschluss (HA9) oder diesem gleichwertig", KAOAMerkmal.SBO_10_7, KAOAZusatzmerkmaleOptionsarten.ANSCHLUSSOPTION, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Anschlüsse bei einem Hauptschulabschluss nach Klasse 10 oder diesem gleichwertig */
    SBO_10_7_6(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(122, "SBO 10.7.6", "Anschlüsse bei einem Hauptschulabschluss nach Klasse 10 oder diesem gleichwertig", KAOAMerkmal.SBO_10_7, KAOAZusatzmerkmaleOptionsarten.ANSCHLUSSOPTION, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Anschlüsse bei einem Mittlerem Schulabschluss (FOR) */
    SBO_10_7_7(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(123, "SBO 10.7.7", "Anschlüsse bei einem Mittlerem Schulabschluss (FOR)", KAOAMerkmal.SBO_10_7, KAOAZusatzmerkmaleOptionsarten.ANSCHLUSSOPTION, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Anschlüsse bei einem Mittlerem Schulabschluss (FOR) mit der Qualifikation für die Oberstufe */
    SBO_10_7_8(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(124, "SBO 10.7.8", "Anschlüsse bei einem Mittlerem Schulabschluss (FOR) mit der Qualifikation für die Oberstufe", KAOAMerkmal.SBO_10_7, KAOAZusatzmerkmaleOptionsarten.ANSCHLUSSOPTION, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Anschlüsse bei einem Hauptschulabschluss (HA9) mit der Qualifikation für die Oberstufe */
    SBO_10_7_9(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(125, "SBO 10.7.9", "Anschlüsse bei einem Hauptschulabschluss (HA9) mit der Qualifikation für die Oberstufe", KAOAMerkmal.SBO_10_7, KAOAZusatzmerkmaleOptionsarten.ANSCHLUSSOPTION, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Anschlüsse bei einem Hauptschulabschluss nach Klasse 10 mit der Qualifikation für die Oberstufe */
    SBO_10_7_10(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(126, "SBO 10.7.10", "Anschlüsse bei einem Hauptschulabschluss nach Klasse 10 mit der Qualifikation für die Oberstufe", KAOAMerkmal.SBO_10_7, KAOAZusatzmerkmaleOptionsarten.ANSCHLUSSOPTION, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Anschlüsse bei dem schulischen Teil der Fachhochschulreife */
    SBO_10_7_11(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(127, "SBO 10.7.11", "Anschlüsse bei dem schulischen Teil der Fachhochschulreife", KAOAMerkmal.SBO_10_7, KAOAZusatzmerkmaleOptionsarten.ANSCHLUSSOPTION, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Anschlüsse mit der Fachhochschulreife */
    SBO_10_7_12(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(128, "SBO 10.7.12", "Anschlüsse mit der Fachhochschulreife", KAOAMerkmal.SBO_10_7, KAOAZusatzmerkmaleOptionsarten.ANSCHLUSSOPTION, null, null)
    }),

    /** KAoA-Zusatzmerkmal: Anschlüsse mit der allgemeinen Hochschulreife */
    SBO_10_7_13(new KAOAZusatzmerkmalEintrag[]{
        new KAOAZusatzmerkmalEintrag(129, "SBO 10.7.13", "Anschlüsse mit der allgemeinen Hochschulreife", KAOAMerkmal.SBO_10_7, KAOAZusatzmerkmaleOptionsarten.ANSCHLUSSOPTION, null, null)
    });


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	
	
	/** Die aktuellsten Daten des KAoA-Merkmals */
	public final @NotNull KAOAZusatzmerkmalEintrag daten;
	
	/** Die Historie mit den Einträgen des KAoA-Merkmals */
	public final @NotNull KAOAZusatzmerkmalEintrag@NotNull[] historie;	

	/** Eine Hashmap mit allen Einträgen, welche ihrer ID zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull Long, @NotNull KAOAZusatzmerkmal> _statusByID = new HashMap<>();

	/** Eine Hashmap mit allen Einträgen, welche dem Kürzel zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull String, @NotNull KAOAZusatzmerkmal> _statusByKuerzel = new HashMap<>();


	/**
	 * Erzeugt ein neues Element in der Aufzählung.
	 * 
	 * @param historie   die Historie der Einträge, welche ein Array von {@link KAOAZusatzmerkmalEintrag} ist  
	 */
	private KAOAZusatzmerkmal(final @NotNull KAOAZusatzmerkmalEintrag@NotNull[] historie) {
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	
	/**
	 * Gibt eine Map von der ID auf das zugehörige Merkmal zurück. 
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von der ID auf das zugehörige Merkmal
	 */
	private static @NotNull HashMap<@NotNull Long, @NotNull KAOAZusatzmerkmal> getMapStatusByID() {
		if (_statusByID.size() == 0)
			for (final KAOAZusatzmerkmal g : KAOAZusatzmerkmal.values())
				_statusByID.put(g.daten.id, g);				
		return _statusByID;
	}

	
	/**
	 * Gibt eine Map von dem Kürzel auf das zugehörige Merkmal zurück. 
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von dem Kürzel auf das zugehörige Merkmal
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull KAOAZusatzmerkmal> getMapStatusByKuerzel() {
		if (_statusByKuerzel.size() == 0)
			for (final KAOAZusatzmerkmal g : KAOAZusatzmerkmal.values())
				_statusByKuerzel.put(g.daten.kuerzel, g);				
		return _statusByKuerzel;
	}
		

	/**
	 * Gibt das Merkmal anhand der angegebenen ID zurück.
	 * 
	 * @param id   die ID des Merkmals
	 * 
	 * @return das Merkmal oder null, falls die ID ungültig ist
	 */
	public static KAOAZusatzmerkmal getByID(final long id) {
		return getMapStatusByID().get(id);
	}


	/**
	 * Gibt das Merkmal anhand des angegebenen Kürzels zurück.
	 * 
	 * @param kuerzel   das Kürzel des Merkmals
	 * 
	 * @return das Merkmal oder null, falls das Kürzel ungültig ist
	 */
	public static KAOAZusatzmerkmal getByKuerzel(final String kuerzel) {
		return getMapStatusByKuerzel().get(kuerzel);
	}

}
