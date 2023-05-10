package de.svws_nrw.core.types.schueler;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import de.svws_nrw.core.data.schule.HerkunftsartKatalogEintrag;
import de.svws_nrw.core.data.schule.HerkunftsartKatalogEintragBezeichnung;
import de.svws_nrw.core.types.schule.Schulform;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die Herkunftsarten von Schülern zur Verfügung.
 *
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum Herkunftsarten {

	/**
	 * Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)
	 */
	NICHTVERSETZUNG(new HerkunftsartKatalogEintrag[] {
		new HerkunftsartKatalogEintrag(0L, "00", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.G, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.GE, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.GM, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SG, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SK, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SR, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)")
		), null, 2022),
        new HerkunftsartKatalogEintrag(1L, "00", Arrays.asList(
                new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.G, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.GE, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.SG, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.SK, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.SR, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Nichtversetzung", "Gleiche oder niedrigere Jahrgangsstufe gegenüber dem Vorjahr wegen Nichtversetzung (§ 50 Abs. 5 SchulG)")
            ), 2023, null)
	}),


	/**
	 * Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung
	 */
	FREIWILLIGE_WIEDERHOLUNG(new HerkunftsartKatalogEintrag[] {
		new HerkunftsartKatalogEintrag(3000L, "03", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.G, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.GE, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.GM, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SG, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SK, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SR, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung")
		), null, 2022),
        new HerkunftsartKatalogEintrag(3001L, "03", Arrays.asList(
                new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.G, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.GE, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.SG, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.SK, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.SR, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Freiwillige Wiederholung", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen freiwilliger Wiederholung")
            ), 2023, null)
	}),


	/**
	 * Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt
	 */
	RUECKTRITT(new HerkunftsartKatalogEintrag[] {
		new HerkunftsartKatalogEintrag(3100L, "03", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.G, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.GE, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.GM, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SG, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SK, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SR, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt")
		), null, 2022),
        new HerkunftsartKatalogEintrag(3101L, "03", Arrays.asList(
                new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.G, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.GE, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.SG, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.SK, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.SR, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Rücktritt", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Rücktritt")
            ), 2023, null)
	}),


	/**
	 * Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)
	 */
	VERBLEIB_IN_SCHULSTUFE(new HerkunftsartKatalogEintrag[] {
		new HerkunftsartKatalogEintrag(3200L, "03", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.G, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.GE, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.GM, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SG, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SK, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SR, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)")
		), null, 2022),
        new HerkunftsartKatalogEintrag(3201L, "03", Arrays.asList(
                new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.G, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.GE, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.SG, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.SK, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.SR, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Verbleib in der Schulstufe", "Gleiche Jahrgangsstufe gegenüber dem Vorjahr wegen Verbleib in der Schulstufe (Förderschwerpunkt geistige Entwicklung)")
            ), 2023, null)
	}),


	/**
	 * Verbleib in der Schuleingangsphase
	 */
	VERBLEIB_IN_SCHULEINGANGSPHASE(new HerkunftsartKatalogEintrag[] {
		new HerkunftsartKatalogEintrag(4000L, "04", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Verbleib in der Schuleingangsphase", "Verbleib in der Schuleingangsphase"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Verbleib in der Schuleingangsphase", "Verbleib in der Schuleingangsphase"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Verbleib in der Schuleingangsphase", "Verbleib in der Schuleingangsphase"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.G, "Verbleib in der Schuleingangsphase", "Verbleib in der Schuleingangsphase"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Verbleib in der Schuleingangsphase", "Verbleib in der Schuleingangsphase"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Verbleib in der Schuleingangsphase", "Verbleib in der Schuleingangsphase"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Verbleib in der Schuleingangsphase", "Verbleib in der Schuleingangsphase"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Verbleib in der Schuleingangsphase", "Verbleib in der Schuleingangsphase")
		), null, null)
	}),


	/**
	 * Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung
	 */
	VERSETZUNG(new HerkunftsartKatalogEintrag[] {
		new HerkunftsartKatalogEintrag(11000L, "11", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.G, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.GE, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.GM, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SG, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SK, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SR, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung")
		), null, 2022),
        new HerkunftsartKatalogEintrag(11001L, "11", Arrays.asList(
                new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.G, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.GE, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.SG, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.SK, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.SR, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Versetzung", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Versetzung")
            ), 2023, null)
	}),


	/**
	 * Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform
	 */
	UEBERGANG(new HerkunftsartKatalogEintrag[] {
		new HerkunftsartKatalogEintrag(11100L, "11", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.G, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.GE, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.GM, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SG, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SK, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SR, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform")
		), null, 2022),
        new HerkunftsartKatalogEintrag(11101L, "11", Arrays.asList(
                new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.G, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.GE, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.SG, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.SK, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.SR, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Übergang (analog zu Versetzung)", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen versetzungsanalogem Übergang innerhalb der Schulform")
            ), 2023, null)
	}),


	/**
	 * Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg
	 */
	SCHULFORMAUFSTIEG(new HerkunftsartKatalogEintrag[] {
		new HerkunftsartKatalogEintrag(11200L, "11", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.G, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.GE, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.GM, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SG, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SK, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SR, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg")
		), null, 2022),
        new HerkunftsartKatalogEintrag(11201L, "11", Arrays.asList(
                new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.G, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.GE, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.SG, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.SK, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.SR, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Schulformaufstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Schulformaufstieg")
            ), 2023, null)
	}),


	/**
	 * Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Wechsel zur Gesamtschule
	 */
	WECHSEL_ZUR_GESAMTSCHULE(new HerkunftsartKatalogEintrag[] {
		new HerkunftsartKatalogEintrag(11300L, "11", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Wechsel zur Gesamtschule", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Wechsel zur Gesamtschule"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Wechsel zur Gesamtschule", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Wechsel zur Gesamtschule"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Wechsel zur Gesamtschule", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Wechsel zur Gesamtschule"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Wechsel zur Gesamtschule", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Wechsel zur Gesamtschule"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Wechsel zur Gesamtschule", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Wechsel zur Gesamtschule"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Wechsel zur Gesamtschule", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Wechsel zur Gesamtschule"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Wechsel zur Gesamtschule", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Wechsel zur Gesamtschule")
		), null, null)
	}),


	/**
	 * Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung
	 */
	VORVERSETZUNG(new HerkunftsartKatalogEintrag[] {
		new HerkunftsartKatalogEintrag(12000L, "12", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.G, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.GE, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.GM, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SG, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SK, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SR, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung")
		), null, 2022),
        new HerkunftsartKatalogEintrag(12001L, "12", Arrays.asList(
                new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.G, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.GE, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.SG, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.SK, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.SR, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Vorversetzung in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr wegen Vorversetzung")
            ), 2023, null)
	}),


	/**
	 * Höhere Jahrgangsstufe gegenüber dem Vorjahr in Verbindung mit einem Schulformabstieg (§ 12 Abs. 3 Satz 2 und 3, § 13 Abs.6 APO-SI)
	 */
	SCHULFORMABSTIEG(new HerkunftsartKatalogEintrag[] {
		new HerkunftsartKatalogEintrag(13000L, "13", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.GE, "Schulformabstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr in Verbindung mit einem Schulformabstieg (§ 12 Abs. 3 Satz 2 und 3, § 13 Abs.6 APO-SI)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Schulformabstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr in Verbindung mit einem Schulformabstieg (§ 12 Abs. 3 Satz 2 und 3, § 13 Abs.6 APO-SI)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Schulformabstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr in Verbindung mit einem Schulformabstieg (§ 12 Abs. 3 Satz 2 und 3, § 13 Abs.6 APO-SI)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Schulformabstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr in Verbindung mit einem Schulformabstieg (§ 12 Abs. 3 Satz 2 und 3, § 13 Abs.6 APO-SI)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Schulformabstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr in Verbindung mit einem Schulformabstieg (§ 12 Abs. 3 Satz 2 und 3, § 13 Abs.6 APO-SI)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SK, "Schulformabstieg in höheren Jahrgang", "Höhere Jahrgangsstufe gegenüber dem Vorjahr in Verbindung mit einem Schulformabstieg (§ 12 Abs. 3 Satz 2 und 3, § 13 Abs.6 APO-SI)")
		), null, null)
	}),


	/**
	 * Schüler, die aus dem Ausland zugezogen sind
	 */
	ZUZUG_AUSLAND(new HerkunftsartKatalogEintrag[] {
		new HerkunftsartKatalogEintrag(99000L, "99", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.G, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.GE, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.GM, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SG, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SK, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SR, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind")
		), null, 2022),
        new HerkunftsartKatalogEintrag(99001L, "99", Arrays.asList(
                new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.G, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.GE, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.GY, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.H, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.PS, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.R, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.KS, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.S, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.SG, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.SK, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.SR, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind"),
                new HerkunftsartKatalogEintragBezeichnung(Schulform.V, "Zuzug aus dem Ausland", "Schüler, die aus dem Ausland zugezogen sind")
            ), 2023, null)
	}),


	/**
	 * Kein Abschluss
	 */
	KEIN_ABSCHLUSS(new HerkunftsartKatalogEintrag[]{
		new HerkunftsartKatalogEintrag(1000000L, "A", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "Kein Abschluss", "Kein Abschluss"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "Kein Abschluss", "Kein Abschluss"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Kein Abschluss", "Abgangszeugnis ohne Abschluss"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Kein Abschluss", "Abgangszeugnis ohne Abschluss"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Kein Abschluss", "Abgangszeugnis ohne Abschluss"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "Kein Abschluss", "Abgangszeugnis ohne Abschluss / Abgangszeugnis ohne Zuerkennung eines Abschlusses / Kooperationsklasse Hauptschule")
		), null, null)
	}),


	/**
	 * Hauptschulabschluss
	 */
	HA9A(new HerkunftsartKatalogEintrag[]{
		new HerkunftsartKatalogEintrag(2000000L, "B", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "HA9", "Hauptschulabschluss, auch Hauptschulabschluss der Förderschule für Lernen"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "HA9", "Hauptschulabschluss, auch Hauptschulabschluss der Förderschule für Lernen"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "HA9", "Hauptschulabschluss"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "HA9", "Hauptschulabschluss"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "HA9", "Hauptschulabschluss"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "HA9", "Hauptschulabschluss / Abgangszeugnis mit Gleichstellung zum Hauptschulabschluss / Hauptschulabschluss der Schule für Lernbehinderte")
		), null, null)
	}),


	/**
	 * nach Klasse 9 (mit Berechtigung zum Besuch der Klasse 10 Typ B)
	 */
	HA9(new HerkunftsartKatalogEintrag[]{
		new HerkunftsartKatalogEintrag(3000000L, "C", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "HA9-10B", "Hauptschulabschluss mit Berechtigung zum Besuch der Klasse 10, Typ B / Abgangszeugnis aus Klasse 10 ohne Mittlerer Abschluss"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "HA9-10B", "Hauptschulabschluss mit Berechtigung zum Besuch der Klasse 10, Typ B / Abgangszeugnis aus Klasse 10 ohne Mittlerer Abschluss"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "HA9-10B", "Hauptschulabschluss mit Berechtigung zum Besuch der Klasse 10, Typ B / Abgangszeugnis aus Klasse 10 ohne Gleichstellung zum Hauptschulabschluss nach Klasse 10")
		), null, null)
	}),


	/**
	 * Hauptschulabschluss nach Klasse 10
	 */
	HA10(new HerkunftsartKatalogEintrag[]{
		new HerkunftsartKatalogEintrag(4000000L, "D", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "HA10", "Hauptschulabschluss nach Klasse 10"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "HA10", "Hauptschulabschluss nach Klasse 10"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "HA10", "Sekundarabschluss I (Hauptschulabschluss nach Klasse 10)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "HA10", "Sekundarabschluss I (Hauptschulabschluss nach Klasse 10)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "HA10", "Sekundarabschluss I (Hauptschulabschluss nach Klasse 10)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "HA10", "Hauptschulabschluss nach Klasse 10 / Abgangszeugnis mit Gleichstellung zum Hauptschulabschluss nach Klasse 10")
		), null, null)
	}),


	/**
	 * Mittlerer Schulabschluss
	 */
	MSA(new HerkunftsartKatalogEintrag[]{
		new HerkunftsartKatalogEintrag(6000000L, "F", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "MSA", "Mittlerer Abschluss, Fachoberschulreife ohne Berechtigung zum Besuch der gymnasialen Oberstufe"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "MSA", "Mittlerer Abschluss, Fachoberschulreife ohne Berechtigung zum Besuch der gymnasialen Oberstufe"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "MSA", "Sekundarabschluss I ohne Versetzungsvermerk"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "MSA", "Sekundarabschluss I ohne Versetzungsvermerk"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "MSA", "Sekundarabschluss I ohne Versetzungsvermerk"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "MSA", "Fachoberschulreife ohne Versetzungsvermerk")
		), null, null)
	}),


	/**
	 * Mittlerer Schulabschluss mit Berechtigung zum Besuch der gymnasialen Oberstufe (Einführungsphase)
	 */
	MSA_Q(new HerkunftsartKatalogEintrag[]{
		new HerkunftsartKatalogEintrag(7000000L, "G", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "MSA-Q", "Mittlerer Abschluss, Fachoberschulreife mit  Berechtigung zum Besuch der gymnasialen Oberstufe (Einführungsphase)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "MSA-Q", "Mittlerer Abschluss, Fachoberschulreife mit  Berechtigung zum Besuch der gymnasialen Oberstufe (Einführungsphase)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "MSA-Q", "Sekundarabschluss I mit Versetzungsvermerk"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "MSA-Q", "Sekundarabschluss I mit Versetzungsvermerk"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "MSA-Q", "Sekundarabschluss I mit Versetzungsvermerk"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "MSA-Q", "Fachoberschulreife mit  Versetzungsvermerk")
		), null, null)
	}),


	/**
	 * Fachhochschulreife (schulischer Teil)
	 */
	FHR_S(new HerkunftsartKatalogEintrag[]{
		new HerkunftsartKatalogEintrag(8000000L, "H", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "FHR (schulischer Teil)", "Fachhochschulreife (schulischer Teil)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "FHR (schulischer Teil)", "Fachhochschulreife (schulischer Teil)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "FHR (schulischer Teil)", "Fachhochschulreife (schulischer Teil)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "FHR (schulischer Teil)", "Fachhochschulreife (schulischer Teil)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "FHR (schulischer Teil)", "Fachhochschulreife (schulischer Teil)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "FHR (schulischer Teil)", "Fachhochschulreife (schulischer Teil)")
		), null, null)
	}),


	/**
	 * Mittlerer Schulabschluss mit Berechtigung zum Besuch der gymnasialen Oberstufe (Qualifikationsphase 1)
	 */
	MSA_Q1(new HerkunftsartKatalogEintrag[]{
		new HerkunftsartKatalogEintrag(9000000L, "I", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "MSA-Q1", "Mittlerer Abschluss, Fachoberschulreife mit  Berechtigung zum Besuch der Qualifikationsphase 1"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "MSA-Q1", "Mittlerer Abschluss, Fachoberschulreife mit  Berechtigung zum Besuch der Qualifikationsphase 1"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "MSA-Q1", "Fachoberschulreife mit Berechtigung zum Besuch der Qualifikationsphase")
		), null, null)
	}),


	/**
	 * Fachhochschulreife
	 */
	FHR(new HerkunftsartKatalogEintrag[]{
		new HerkunftsartKatalogEintrag(10000000L, "J", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "FHR", "Fachhochschulreife"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "FHR", "Fachhochschulreife"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "FHR", "Fachhochschulreife"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "FHR", "Fachhochschulreife"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "FHR", "Fachhochschulreife"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "FHR", "Fachhochschulreife")
		), null, null)
	}),


	/**
	 * Allgemeine Hochschulreife
	 */
	AHR(new HerkunftsartKatalogEintrag[]{
		new HerkunftsartKatalogEintrag(11000000L, "K", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "AHR", "Allgemeine Hochschulreife"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "AHR", "Allgemeine Hochschulreife"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "AHR", "Allgemeine Hochschulreife")
		), null, null)
	}),


	/**
	 * Hochschulreife für das Land NRW
	 */
	HR_NRW(new HerkunftsartKatalogEintrag[]{
		new HerkunftsartKatalogEintrag(12000000L, "L", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "Hochschulreife NRW", "Hochschulreife für das Land NRW"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "Hochschulreife NRW", "Hochschulreife für das Land NRW"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "Hochschulreife NRW", "Hochschulreife für das Land NRW")
		), null, null)
	}),


	/**
	 * Abschlusszeugnis (Förderschule, Förderschwerpunkt geistige Entwicklung)
	 */
	ABSCHLUSS_FOEG(new HerkunftsartKatalogEintrag[]{
		new HerkunftsartKatalogEintrag(13000000L, "M", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "Abschlusszeugnis Förderschule (geistige Entwicklung)", "Abschlusszeugnis (Förderschule, Förderschwerpunkt geistige Entwicklung)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "Abschlusszeugnis Förderschule (geistige Entwicklung)", "Abschlusszeugnis (Förderschule, Förderschwerpunkt geistige Entwicklung)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "Abschlusszeugnis Förderschule (geistige Entwicklung)", "Abschlusszeugnis (Förderschule, Förderschwerpunkt geistige Entwicklung)")
		), null, null)
	}),


	/**
	 * Abschlusszeugnis (Förderschule, Förderschwerpunkt Lernen)
	 */
	ABSCHLUSS_FOEL(new HerkunftsartKatalogEintrag[]{
		new HerkunftsartKatalogEintrag(14000000L, "N", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "Abschlusszeugnis Förderschule (Lernen)", "Abschlusszeugnis (Förderschule, Förderschwerpunkt Lernen)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "Abschlusszeugnis Förderschule (Lernen)", "Abschlusszeugnis (Förderschule, Förderschwerpunkt Lernen)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "Abschlusszeugnis Förderschule (Lernen)", "Abschlusszeugnis (Förderschule, Förderschwerpunkt Lernen)")
		), null, null)
	}),


	/**
	 * Hauptschulabschluss mit Berechtigung zum Besuch der gymnasialen Oberstufe
	 */
	HA9_Q(new HerkunftsartKatalogEintrag[]{
		new HerkunftsartKatalogEintrag(15000000L, "O", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "HA-Q", "Hauptschulabschluss mit Berechtigung zum Besuch der gymnasialen Oberstufe"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "HA-Q", "Hauptschulabschluss mit Berechtigung zum Besuch der gymnasialen Oberstufe"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "HA-Q", "Hauptschulabschluss mit Berechtigung zum Besuch der gymnasialen Oberstufe (auch Versetzungszeugnis der Kl. 9 des Gymnasiums)")
		), null, null)
	}),


	/**
	 * Versetzung nach Klasse 11 FO
	 */
	VS_11(new HerkunftsartKatalogEintrag[]{
		new HerkunftsartKatalogEintrag(16000000L, "P", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "Versetzung Kl. 11", "Versetzung nach Klasse 11 FO"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "Versetzung Kl. 11", "Versetzung nach Klasse 11 FO"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "Versetzung Kl. 11", "Versetzung nach Klasse 11 FO")
		), null, null)
	}),


	/**
	 * Fachgebundene Hochschulreife
	 */
	FGHR(new HerkunftsartKatalogEintrag[]{
		new HerkunftsartKatalogEintrag(17000000L, "Q", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "FGHR", "Fachgebundene Hochschulreife"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "FGHR", "Fachgebundene Hochschulreife"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "FGHR", "Fachgebundene Hochschulreife")
		), null, null)
	}),


	/**
	 * Ein dem Hauptschulabschluss vergleichbarer Abschluss mit Berechtigung zum Besuch eines weiterführenden Bildungsgangs im Berufskolleg (nur Internationale Förderklasse)
	 */
	HA9_FOE(new HerkunftsartKatalogEintrag[]{
		new HerkunftsartKatalogEintrag(19000000L, "S", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "HA-IntFö", "Ein dem Hauptschulabschluss vergleichbarer Abschluss mit Berechtigung zum Besuch eines weiterführenden Bildungsgangs im Berufskolleg (nur Internationale Förderklasse)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "HA-IntFö", "Ein dem Hauptschulabschluss vergleichbarer Abschluss mit Berechtigung zum Besuch eines weiterführenden Bildungsgangs im Berufskolleg (nur Internationale Förderklasse)")
		), null, null)
	}),


	/**
	 * Herkunft noch unbekannt (nur Gliederung A12, A13)
	 */
	UNBEKANNT(new HerkunftsartKatalogEintrag[]{
		new HerkunftsartKatalogEintrag(21000000L, "U", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "Unbekannt", "Herkunft noch unbekannt (nur Gliederung A12, A13)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "Unbekannt", "Herkunft noch unbekannt (nur Gliederung A12, A13)")
		), null, null)
	}),


	/**
	 * Versetzte bzw. vorgerückte Schüler/-innen / Höheres Semester
	 */
	VERSETZT(new HerkunftsartKatalogEintrag[]{
		new HerkunftsartKatalogEintrag(22000000L, "V", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "Versetzt", "Versetzte bzw. vorgerückte Schüler/-innen"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "Versetzt", "Versetzte bzw. vorgerückte Schüler/-innen"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Versetzt", "Höheres Semester"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Versetzt", "Höheres Semester"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Versetzt", "Höheres Semester"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "Versetzt", "Höheres Semester")
		), null, null)
	}),


	/**
	 * Wiederholer / Gleiches oder niedrigeres Semester
	 */
	WIEDERHOLER(new HerkunftsartKatalogEintrag[]{
		new HerkunftsartKatalogEintrag(23000000L, "W", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "Wiederholer", "Wiederholer"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "Wiederholer", "Wiederholer"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Wiederholer", "Gleiches oder niedrigeres Semester"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Wiederholer", "Gleiches oder niedrigeres Semester"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Wiederholer", "Gleiches oder niedrigeres Semester"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "Wiederholer", "Gleiches oder niedrigeres Semester")
		), null, null)
	}),


	/**
	 * Sonstige Qualifikation (bei Schüler/-innen, die seit den letzten amtlichen Schuldaten aus dem Ausland zugezogen sind)
	 */
	SONSTIGE_QUALIFIKATION(new HerkunftsartKatalogEintrag[]{
		new HerkunftsartKatalogEintrag(24000000L, "X", Arrays.asList(
			new HerkunftsartKatalogEintragBezeichnung(Schulform.BK, "Sonstige Qualifikation", "Sonstige Qualifikation (bei Schüler/-innen, die seit den letzten amtlichen Schuldaten aus dem Ausland zugezogen sind)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.SB, "Sonstige Qualifikation", "Sonstige Qualifikation (bei Schüler/-innen, die seit den letzten amtlichen Schuldaten aus dem Ausland zugezogen sind)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.FW, "Sonstige Qualifikation", "Sonstige Qualifikation (bei Schüler/-innen, die seit den letzten amtlichen Schuldaten aus dem Ausland zugezogen sind)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.HI, "Sonstige Qualifikation", "Sonstige Qualifikation (bei Schüler/-innen, die seit den letzten amtlichen Schuldaten aus dem Ausland zugezogen sind)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WF, "Sonstige Qualifikation", "Sonstige Qualifikation (bei Schüler/-innen, die seit den letzten amtlichen Schuldaten aus dem Ausland zugezogen sind)"),
			new HerkunftsartKatalogEintragBezeichnung(Schulform.WB, "Sonstige Qualifikation", "Sonstige Qualifikation (bei Schüler/-innen, die seit den letzten amtlichen Schuldaten aus dem Ausland zugezogen sind)")
		), null, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static final long VERSION = 2;

	/** Der aktuellen Daten der Herkunftsart, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull HerkunftsartKatalogEintrag daten;

	/** Die Historie mit den Einträgen der Herkunftsarten */
	public final @NotNull HerkunftsartKatalogEintrag@NotNull[] historie;

	/** Eine Map mit der Zuordnung der Herkunftsart zu dem Kürzel der Herkunftsart */
	private static final @NotNull HashMap<@NotNull String, Herkunftsarten> _mapKuerzel = new HashMap<>();

	/** Eine Map mit der Zuordnung der Herkunftsart zu der ID der Herkunftsart */
	private static final @NotNull HashMap<@NotNull Long, Herkunftsarten> _mapID = new HashMap<>();

	/** Die Schulformen, bei welchen die Herkunftsart vorkommt, für die einzelnen Historieneinträge */
	private @NotNull ArrayList<Schulform> @NotNull[] schulformen;

	/** Die Bezeichnungen bei den Schulformen, bei welchen die Herkunftsart vorkommt, für die einzelnen Historieneinträge */
	private @NotNull ArrayList<@NotNull String> @NotNull[] bezeichnungen;


	/**
	 * Erzeugt eine neue Herkunftsart in der Aufzählung.
	 *
	 * @param historie   die Historie der Herkunftsart, welches ein Array von {@link HerkunftsartKatalogEintrag} ist
	 */
	@SuppressWarnings("unchecked")
	Herkunftsarten(final @NotNull HerkunftsartKatalogEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist
		this.daten = historie[historie.length - 1];
		// Erzeuge zwei weitere Arrays mit der Schulformzuordnung und den Bezeichnungen für die Historie
		this.schulformen = (@NotNull ArrayList<Schulform> @NotNull[]) Array.newInstance(ArrayList.class, historie.length);
		this.bezeichnungen = (@NotNull ArrayList<@NotNull String> @NotNull[]) Array.newInstance(ArrayList.class, historie.length);
		for (int i = 0; i < historie.length; i++) {
			this.schulformen[i] = new ArrayList<>();
			this.bezeichnungen[i] = new ArrayList<>();
			for (final @NotNull HerkunftsartKatalogEintragBezeichnung bez : historie[i].bezeichnungen) {
				final Schulform sf = Schulform.getByKuerzel(bez.schulform);
				if (sf != null)
					this.schulformen[i].add(sf);
				this.bezeichnungen[i].add(bez.bezeichnung);
			}
		}
	}


	/**
	 * Gibt eine Map von den Kürzeln der Herkunftsarten auf die zugehörigen Herkunftsarten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Herkunftsarten auf die zugehörigen Herkunftsarten
	 */
	private static @NotNull HashMap<@NotNull String, Herkunftsarten> getMapHerkunftsartByKuerzel() {
		if (_mapKuerzel.size() == 0)
			for (final Herkunftsarten j : Herkunftsarten.values())
				if (!_mapKuerzel.containsKey(j.daten.kuerzel))
					_mapKuerzel.put(j.daten.kuerzel, j);
		return _mapKuerzel;
	}


	/**
	 * Gibt eine Map von den IDs der Herkunftsarten auf die zugehörigen Herkunftsarten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs der Herkunftsarten auf die zugehörigen Herkunftsarten
	 */
	private static @NotNull HashMap<@NotNull Long, Herkunftsarten> getMapHerkunftsartByID() {
		if (_mapID.size() == 0)
			for (final Herkunftsarten j : Herkunftsarten.values()) {
				for (final HerkunftsartKatalogEintrag k : j.historie)
					_mapID.put(k.id, j);
			}
		return _mapID;
	}


	/**
	 * Liefert die Herkunftsart anhand des übergebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel der Herkunftsart
	 *
	 * @return die Herkunftsart oder null, falls das Kürzel ungültig ist
	 */
	public static Herkunftsarten getByKuerzel(final String kuerzel) {
		return getMapHerkunftsartByKuerzel().get(kuerzel);
	}


	/**
	 * Liefert die Herkunftsart anhand der übergebenen ID zurück.
	 *
	 * @param id   die ID der Herkunftsart
	 *
	 * @return die Herkunftsart oder null, falls die ID ungültig ist
	 */
	public static Herkunftsarten getByID(final Long id) {
		return getMapHerkunftsartByID().get(id);
	}


	/**
	 * Liefert die Bezeichnung der Herkunftsart für die angebenene Schulform.
	 *
	 * @param schulform   die Schulform
	 *
	 * @return die Bezeichung der Herkunftsart oder null, falls die Schulform nicht zulässig ist
	 */
	public String getBezeichnung(final Schulform schulform) {
		if ((schulform == null) || (schulform.daten == null))
			return null;
		if (daten.bezeichnungen != null) {
			for (int i = 0; i < daten.bezeichnungen.size(); i++) {
				final HerkunftsartKatalogEintragBezeichnung bez = daten.bezeichnungen.get(i);
				final String sfKuerzel = bez.schulform;
				if (sfKuerzel.equals(schulform.daten.kuerzel))
					return bez.bezeichnung;
			}
		}
		return null;
	}



	/**
	 * Liefert alle Schulformen zurück, bei welchen die Herkunftsart vorkommt.
	 *
	 * @return eine Liste der Schulformen
	 */
	public @NotNull List<Schulform> getSchulformen() {
		return schulformen[historie.length - 1];
	}


	/**
	 * Liefert alle zulässigen Herkunftsarten für die angegebene Schulform.
	 *
	 * @param schulform   die Schulform
	 *
	 * @return die bei der Schulform zulässigen Herkunftsarten
	 */
	public static @NotNull List<Herkunftsarten> get(final Schulform schulform) {
		final @NotNull ArrayList<Herkunftsarten> result = new ArrayList<>();
		if (schulform == null)
			return result;
		final @NotNull Herkunftsarten@NotNull[] herkunftsarten = Herkunftsarten.values();
		for (int i = 0; i < herkunftsarten.length; i++) {
			final @NotNull Herkunftsarten herkunftsart = herkunftsarten[i];
			if (herkunftsart.hasSchulform(schulform))
				result.add(herkunftsart);
		}
		return result;
	}


	/**
	 * Prüft anhand des Schulform-Kürzels, ob die Schulform diese Herkunftsart
	 * hat oder nicht.
	 *
	 * @param kuerzel   das Kürzel der Schulform
	 *
	 * @return true, falls die Herkunftsart bei der Schulform existiert und ansonsten false
	 */
	public boolean hasSchulformByKuerzel(final String kuerzel) {
		if ((kuerzel == null) || "".equals(kuerzel))
			return false;
		if (daten.bezeichnungen != null) {
			for (int i = 0; i < daten.bezeichnungen.size(); i++) {
				final String sfKuerzel = daten.bezeichnungen.get(i).schulform;
				if (sfKuerzel.equals(kuerzel))
					return true;
			}
		}
		return false;
	}


	/**
	 * Prüft, ob die Schulform diese Herkunftsart hat oder nicht.
	 *
	 * @param schulform   die Schulform
	 *
	 * @return true, falls die Herkunftsart bei der Schulform existiert und ansonsten false
	 */
	public boolean hasSchulform(final Schulform schulform) {
		if ((schulform == null) || (schulform.daten == null))
			return false;
		if (daten.bezeichnungen != null) {
			for (int i = 0; i < daten.bezeichnungen.size(); i++) {
				final String sfKuerzel = daten.bezeichnungen.get(i).schulform;
				if (sfKuerzel.equals(schulform.daten.kuerzel))
					return true;
			}
		}
		return false;
	}

}
