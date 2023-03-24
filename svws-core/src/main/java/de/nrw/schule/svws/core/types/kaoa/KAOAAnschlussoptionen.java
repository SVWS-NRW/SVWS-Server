package de.nrw.schule.svws.core.types.kaoa;

import java.util.Arrays;
import java.util.HashMap;

import de.nrw.schule.svws.core.data.kaoa.KAOAAnschlussoptionEintrag;
import de.nrw.schule.svws.core.types.schule.Schulstufe;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die KAoA-Anschlussoptionen für Anschlussvereinbarungen nach SBO 10.7.
 */
public enum KAOAAnschlussoptionen {

    /** KAoA-Anschlussoption: Berufspraxisstufe einer Förderschule für Geistige Entwicklung */
    BE(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(22, "BE", "Berufspraxisstufe einer Förderschule für Geistige Entwicklung", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_3
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Gymnasiale Oberstufe der Gesamtschule oder des Gymnasiums */
    GY(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(23, "GY", "Gymnasiale Oberstufe der Gesamtschule oder des Gymnasiums", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_8
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Berufskolleg - Einjährige Ausbildungsvorbereitung in Vollzeit (AV) - Anlage A */
    AV(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(24, "AV", "Berufskolleg - Einjährige Ausbildungsvorbereitung in Vollzeit (AV) - Anlage A", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_3, 
                KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Berufskolleg - Einjährige Berufsfachschule Typ I (BFS I) - Anlage B */
    BFSI(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(25, "BFSI", "Berufskolleg - Einjährige Berufsfachschule Typ I (BFS I) - Anlage B", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_5
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Berufskolleg - Einjährige Berufsfachschule Typ II (BFS II) - Anlage B */
    BFSII(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(26, "BFSII", "Berufskolleg - Einjährige Berufsfachschule Typ II (BFS II) - Anlage B", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_6, KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, 
                KAOAZusatzmerkmal.SBO_10_7_9, KAOAZusatzmerkmal.SBO_10_7_10
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Berufskolleg - Zweijährige [Höhere] Berufsfachschule - Anlage C */
    HOEBFS(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(27, "HÖBFS", "Berufskolleg - Zweijährige [Höhere] Berufsfachschule - Anlage C", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, 
                KAOAZusatzmerkmal.SBO_10_7_10
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Berufskolleg - Zweijährige Fachoberschule (FOS 11/12) - Anlage C */
    FOS(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(28, "FOS", "Berufskolleg - Zweijährige Fachoberschule (FOS 11/12) - Anlage C", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, 
                KAOAZusatzmerkmal.SBO_10_7_10
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Berufskolleg - Berufliches Gymnasium - Anlage D */
    BERUFGY(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(29, "BERUFGY", "Berufskolleg - Berufliches Gymnasium - Anlage D", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, KAOAZusatzmerkmal.SBO_10_7_10, 
                KAOAZusatzmerkmal.SBO_10_7_11, KAOAZusatzmerkmal.SBO_10_7_12
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Duale Berufsausbildung (inkl. Beamtenlaufbahn im mittleren Dienst) */
    DUAL(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(30, "DUAL", "Duale Berufsausbildung (inkl. Beamtenlaufbahn im mittleren Dienst)", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_3, 
                KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, 
                KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, 
                KAOAZusatzmerkmal.SBO_10_7_10, KAOAZusatzmerkmal.SBO_10_7_11, KAOAZusatzmerkmal.SBO_10_7_12, 
                KAOAZusatzmerkmal.SBO_10_7_13
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Berufskolleg - schulische Ausbildung zwei- oder dreijährig in der Berufsfachschule Anlage B oder C oder am Beruflichen Gymnasium */
    SCHUAUS(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(31, "SCHUAUS", "Berufskolleg - schulische Ausbildung zwei- oder dreijährig in der Berufsfachschule Anlage B oder C oder am Beruflichen Gymnasium", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, KAOAZusatzmerkmal.SBO_10_7_7, 
                KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, KAOAZusatzmerkmal.SBO_10_7_10, 
                KAOAZusatzmerkmal.SBO_10_7_11, KAOAZusatzmerkmal.SBO_10_7_12, KAOAZusatzmerkmal.SBO_10_7_13
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Schulische Ausbildung in Berufen des Gesundheitswesens und der Altenpflege */
    SCHUGA(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(32, "SCHUGA", "Schulische Ausbildung in Berufen des Gesundheitswesens und der Altenpflege", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, KAOAZusatzmerkmal.SBO_10_7_7, 
                KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, KAOAZusatzmerkmal.SBO_10_7_10, 
                KAOAZusatzmerkmal.SBO_10_7_11, KAOAZusatzmerkmal.SBO_10_7_12, KAOAZusatzmerkmal.SBO_10_7_13
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Berufsausbildung in einer außerbetrieblichen Einrichtung (BaE kooperativ/integrativ) */
    BAE(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(33, "BAE", "Berufsausbildung in einer außerbetrieblichen Einrichtung (BaE kooperativ/integrativ)", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Betriebliche Ausbildung in gesondert geregelten Berufen (Fachpraktikerausbildung/Werkerausbildung) für Jugendliche mit Behinderung */
    FACHAUS(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(34, "FACHAUS", "Betriebliche Ausbildung in gesondert geregelten Berufen (Fachpraktikerausbildung/Werkerausbildung) für Jugendliche mit Behinderung", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_3, KAOAZusatzmerkmal.SBO_10_7_4
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Berufsvorbereitende Bildungsmaßnahme der Agentur für Arbeit (BvB), auch rehaspezifisch */
    BVB(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(35, "BVB", "Berufsvorbereitende Bildungsmaßnahme der Agentur für Arbeit (BvB), auch rehaspezifisch", Arrays.asList(
            Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
        ), Arrays.asList(
            KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_3, 
            KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, 
            KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, 
            KAOAZusatzmerkmal.SBO_10_7_10
        ), null, null)
    }),

    /** KAoA-Anschlussoption: Einstiegsqualifizierung (EQ und EQ plus) */
    EQ(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(36, "EQ", "Einstiegsqualifizierung (EQ und EQ plus)", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_3, 
                KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, 
                KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, 
                KAOAZusatzmerkmal.SBO_10_7_10, KAOAZusatzmerkmal.SBO_10_7_11
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Werkstattjahr.NRW */
    WERK(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(37, "WERK", "Werkstattjahr.NRW", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_3, 
                KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, 
                KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, 
                KAOAZusatzmerkmal.SBO_10_7_10
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Jugendwerkstatt */
    JUWERK(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(38, "JUWERK", "Jugendwerkstatt", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_3, 
                KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, 
                KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, 
                KAOAZusatzmerkmal.SBO_10_7_10
            ), null, null)
    }),

    /** KAoA-Anschlussoption: weitere Maßnahmen gemäß SGB II/III/VIII (z. B. Aktivierungshilfen, Projekte der Jugendberufshilfe)  */
    MASS(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(39, "MASS", "weitere Maßnahmen gemäß SGB II/III/VIII (z. B. Aktivierungshilfen, Projekte der Jugendberufshilfe) ", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_3, 
                KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, 
                KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, 
                KAOAZusatzmerkmal.SBO_10_7_10
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Abendrealschule oder VHS zum Nachholen des Schulabschlusses */
    VHS(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(40, "VHS", "Abendrealschule oder VHS zum Nachholen des Schulabschlusses", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_4, 
                KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6
            ), null, null)
    }),

    /** KAoA-Anschlussoption: betriebliche Langzeitpraktika (ohne EQ) (z.B. zum Erwerb der vollen FHR) */
    PRAKT(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(41, "PRAKT", "betriebliche Langzeitpraktika (ohne EQ) (z.B. zum Erwerb der vollen FHR)", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_3, 
                KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, 
                KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, 
                KAOAZusatzmerkmal.SBO_10_7_10, KAOAZusatzmerkmal.SBO_10_7_11, KAOAZusatzmerkmal.SBO_10_7_12, 
                KAOAZusatzmerkmal.SBO_10_7_13
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Freiwilligendienste, Freiwilliger Wehrdienst/Laufbahn Bundeswehr und ähnliche Anschlussoptionen */
    FREI(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(42, "FREI", "Freiwilligendienste, Freiwilliger Wehrdienst/Laufbahn Bundeswehr und ähnliche Anschlussoptionen", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_3, 
                KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, 
                KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, 
                KAOAZusatzmerkmal.SBO_10_7_10, KAOAZusatzmerkmal.SBO_10_7_11, KAOAZusatzmerkmal.SBO_10_7_12, 
                KAOAZusatzmerkmal.SBO_10_7_13
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Sozialversicherungspflichtige Beschäftigung als ungelernte Kraft */
    SVP(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(43, "SVP", "Sozialversicherungspflichtige Beschäftigung als ungelernte Kraft", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_3, 
                KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, 
                KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, 
                KAOAZusatzmerkmal.SBO_10_7_10, KAOAZusatzmerkmal.SBO_10_7_11, KAOAZusatzmerkmal.SBO_10_7_12, 
                KAOAZusatzmerkmal.SBO_10_7_13
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Außerbetriebliche Ausbildung für Menschen mit Behinderung im Berufsbildungswerk (BBW) */
    BBW(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(44, "BBW", "Außerbetriebliche Ausbildung für Menschen mit Behinderung im Berufsbildungswerk (BBW)", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_3, KAOAZusatzmerkmal.SBO_10_7_4
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Begleitete betriebliche Ausbildung (bbA) für Menschen mit Behinderung */
    BBA(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(45, "BBA", "Begleitete betriebliche Ausbildung (bbA) für Menschen mit Behinderung", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_3, KAOAZusatzmerkmal.SBO_10_7_4
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Unterstützte Beschäftigung (UB) für Menschen mit Behinderung */
    UB(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(46, "UB", "Unterstützte Beschäftigung (UB) für Menschen mit Behinderung", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_3, KAOAZusatzmerkmal.SBO_10_7_4
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Diagnose der Arbeitsmarktfähigkeit besonders betroffener behinderter Menschen (DIA-AM) */
    DIAAM(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(47, "DIAAM", "Diagnose der Arbeitsmarktfähigkeit besonders betroffener behinderter Menschen (DIA-AM)", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_3, KAOAZusatzmerkmal.SBO_10_7_4
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Maßnahmen im Eingangsverfahren und im Berufsbildungsbereich in Werkstätten für behinderte Menschen (WfbM) */
    WFBM(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(48, "WFBM", "Maßnahmen im Eingangsverfahren und im Berufsbildungsbereich in Werkstätten für behinderte Menschen (WfbM)", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_3, KAOAZusatzmerkmal.SBO_10_7_4
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Betreuung in Tagesförderstätten für schwerst- und schwermehrfachbehinderte Menschen */
    TAG(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(49, "TAG", "Betreuung in Tagesförderstätten für schwerst- und schwermehrfachbehinderte Menschen", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_3
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Verbleib zu Hause bei einer Schwerst- und Schwermehrfachbehinderung */
    HAUSE(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(50, "HAUSE", "Verbleib zu Hause bei einer Schwerst- und Schwermehrfachbehinderung", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_3
            ), null, null)
    }),

    /** KAoA-Anschlussoption: weitere rehaspezifische Maßnahmen für Menschen mit Behinderung */
    REHA(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(51, "REHA", "weitere rehaspezifische Maßnahmen für Menschen mit Behinderung", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_3
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Minijob */
    MINI(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(52, "MINI", "Minijob", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_3, 
                KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, 
                KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, 
                KAOAZusatzmerkmal.SBO_10_7_10, KAOAZusatzmerkmal.SBO_10_7_11
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Schwangerschaft/Elternzeit */
    SCHWAN(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(53, "SCHWAN", "Schwangerschaft/Elternzeit", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_3, 
                KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, 
                KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, 
                KAOAZusatzmerkmal.SBO_10_7_10, KAOAZusatzmerkmal.SBO_10_7_11
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Schulabsenz, daher Verbleib unbekannt */
    ABSENZ(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(54, "ABSENZ", "Schulabsenz, daher Verbleib unbekannt", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_3, 
                KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, 
                KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, 
                KAOAZusatzmerkmal.SBO_10_7_10, KAOAZusatzmerkmal.SBO_10_7_11
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Verbleib unbekannt - andere Gründe */
    UNBE(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(55, "UNBE", "Verbleib unbekannt - andere Gründe", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_3, 
                KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, 
                KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, 
                KAOAZusatzmerkmal.SBO_10_7_10, KAOAZusatzmerkmal.SBO_10_7_11, KAOAZusatzmerkmal.SBO_10_7_12, 
                KAOAZusatzmerkmal.SBO_10_7_13
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Noch kein Anschluss */
    NOKEAN(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(56, "NOKEAN", "Noch kein Anschluss", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_I, Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_1, KAOAZusatzmerkmal.SBO_10_7_2, KAOAZusatzmerkmal.SBO_10_7_3, 
                KAOAZusatzmerkmal.SBO_10_7_4, KAOAZusatzmerkmal.SBO_10_7_5, KAOAZusatzmerkmal.SBO_10_7_6, 
                KAOAZusatzmerkmal.SBO_10_7_7, KAOAZusatzmerkmal.SBO_10_7_8, KAOAZusatzmerkmal.SBO_10_7_9, 
                KAOAZusatzmerkmal.SBO_10_7_10, KAOAZusatzmerkmal.SBO_10_7_11, KAOAZusatzmerkmal.SBO_10_7_12, 
                KAOAZusatzmerkmal.SBO_10_7_13
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Duales Studium (inkl. Beamtenausbildung im gehobenen Dienst) */
    DUASTUD(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(57, "DUASTUD", "Duales Studium (inkl. Beamtenausbildung im gehobenen Dienst)", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_12, KAOAZusatzmerkmal.SBO_10_7_13
            ), null, null)
    }),

    /** KAoA-Anschlussoption: Hochschulstudium */
    STUD(new KAOAAnschlussoptionEintrag[]{
        new KAOAAnschlussoptionEintrag(58, "STUD", "Hochschulstudium", Arrays.asList(
                Schulstufe.SEKUNDARSTUFE_II
            ), Arrays.asList(
                KAOAZusatzmerkmal.SBO_10_7_12, KAOAZusatzmerkmal.SBO_10_7_13
            ), null, null)
    });


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	
	
	/** Die aktuellsten Daten der KAoA-Anschlussoption */
	public final @NotNull KAOAAnschlussoptionEintrag daten;
	
	/** Die Historie mit den Einträgen der KAoA-Anschlussoption */
	public final @NotNull KAOAAnschlussoptionEintrag@NotNull[] historie;	

	/** Eine Hashmap mit allen Einträgen, welche ihrer ID zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull Long, @NotNull KAOAAnschlussoptionen> _statusByID = new HashMap<>();

	/** Eine Hashmap mit allen Einträgen, welche dem Kürzel zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull String, @NotNull KAOAAnschlussoptionen> _statusByKuerzel = new HashMap<>();


	/**
	 * Erzeugt ein neues Element in der Aufzählung.
	 * 
	 * @param historie   die Historie der Einträge, welche ein Array von {@link KAOAAnschlussoptionEintrag} ist  
	 */
	private KAOAAnschlussoptionen(final @NotNull KAOAAnschlussoptionEintrag@NotNull[] historie) {
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}

	
	/**
	 * Gibt eine Map von der ID auf die zugehörige Anschlussoption zurück. 
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von der ID auf die zugehörige Anschlussoption
	 */
	private static @NotNull HashMap<@NotNull Long, @NotNull KAOAAnschlussoptionen> getMapStatusByID() {
		if (_statusByID.size() == 0)
			for (final KAOAAnschlussoptionen g : KAOAAnschlussoptionen.values())
				_statusByID.put(g.daten.id, g);				
		return _statusByID;
	}

	
	/**
	 * Gibt eine Map von dem Kürzel auf die zugehörige Anschlussoption zurück. 
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von dem Kürzel auf die zugehörige Anschlussoption
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull KAOAAnschlussoptionen> getMapStatusByKuerzel() {
		if (_statusByKuerzel.size() == 0)
			for (final KAOAAnschlussoptionen g : KAOAAnschlussoptionen.values())
				_statusByKuerzel.put(g.daten.kuerzel, g);				
		return _statusByKuerzel;
	}
		

	/**
	 * Gibt die Anschlussoption anhand der angegebenen ID zurück.
	 * 
	 * @param id   die ID der Anschlussoption
	 * 
	 * @return die Anschlussoption oder null, falls die ID ungültig ist
	 */
	public static KAOAAnschlussoptionen getByID(final long id) {
		return getMapStatusByID().get(id);
	}


	/**
	 * Gibt die Anschlussoption anhand des angegebenen Kürzels zurück.
	 * 
	 * @param kuerzel   das Kürzel der Anschlussoption
	 * 
	 * @return die Anschlussoption oder null, falls das Kürzel ungültig ist
	 */
	public static KAOAAnschlussoptionen getByKuerzel(final String kuerzel) {
		return getMapStatusByKuerzel().get(kuerzel);
	}

}
