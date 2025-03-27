package de.svws_nrw.schulen.v1.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse beschreibt die grundlegenden Daten einer Organisationseinheit
 * der Schuldatei. Hier wird nur ein Standort der Organisationseinheit
 * gespeichert, i.A. der Hauptstandort
 */
@XmlRootElement
@Schema(description = "die Grunddaten einer Organisationseinheit der Schuldatei.")
@TranspilerDTO
public class SchuldateiOrganisationseinheitGrunddaten extends SchuldateiEintrag {

	/** Die ID der Grunddaten. */
	@Schema(description = "die ID dieses Eintrags", example = "4711")
	public @NotNull String id = "";

	/** Die Schulnummer. */
	@Schema(description = "die Schulnummer", example = "100001")
	public @NotNull String schulnummer = "";

	/** Die Amtsbezeichnung der Organisationseinheit */
	@Schema(description = "Teil1 der amtlichen Bezeichnung der Organisationseinheit", example = "Städt. Gymnasium ....")
	public @NotNull String amtsbez1 = "";

	/** Die Amtsbezeichnung der Organisationseinheit */
	@Schema(description = "Teil2 der amtlichen Bezeichnung der Organisationseinheit", example = "Städt. Gymnasium ....")
	public @NotNull String amtsbez2 = "";

	/** Die Amtsbezeichnung der Organisationseinheit */
	@Schema(description = "Teil3 der amtlichen Bezeichnung der Organisationseinheit", example = "Städt. Gymnasium ....")
	public @NotNull String amtsbez3 = "";

	/** Die Kurzbezeichnung der Organisationseinheit */
	@Schema(description = "die Kurzbezeichnung der Organisationseinheit", example = "Düsseldorf, MSB")
	public @NotNull String kurzbezeichnung = "";

	/** Der Rechtsstatus der Organisationseinheit 1=öffentlich, 2=privat*/
	@Schema(description = "Der Rechtsstatus der Organisationseinheit", example = "1")
	public @NotNull String rechtsstatus = "";

	/** Schulträgernummer der Organisationseinheit */
	@Schema(description = "Schulträgernummer der Organisationseinheit")
	public @NotNull String schultraegernummer = "";

	/** Art der Trägerschaft der Schule */
	@Schema(description = "Art der Trägerschaft des Schulträgers", example = "00")
	public @NotNull String artdertraegerschaft = "";

	/** Betriebsschlüssel der Schule */
	@Schema(description = "Betriebsschlüssel der Schule")
	public @NotNull String schulbetriebsschluessel = "";

	/** Kapitel der Schule */
	@Schema(description = "Kapitel der Schule")
	public @NotNull String kapitel = "";

	/** Obere Schulaufsicht der Schule */
	@Schema(description = "Obere Schulaufsicht der Schule", example = "001")
	public @NotNull String obereschulaufsicht = "";

	/** Untere Schulaufsicht der Schule */
	@Schema(description = "Untere Schulaufsicht der Schule")
	public @NotNull String untereschulaufsicht = "";

	/** Zentrum für schulpraktische Lehrerausbildung ZFSL */
	@Schema(description = "Zentrum für schulpraktische Lehrerausbildung ZFSL", example = "503010")
	public @NotNull String zfsl = "";

	/** Dienststellenschlüssel bzw. Personalbereich der Organisationseinheit */
	@Schema(description = "Dienststellenschlüssel (Personalbereich) der Organisationseinheit", example = "M005")
	public @NotNull String dienststellenschluessel = "";

	/** Personalteilbereich der Organisationseinheit */
	@Schema(description = "Personalteilbereich der Organisationseinheit", example = "2160")
	public @NotNull String ptb = "";

	/** Gibt an ob die Schule Internatsbetrieb hat */
	@Schema(description = "Gibt die Art des Internatbetriebs an")
	public @NotNull String internatsbetrieb = "";

	/** Anzahl der Internatsplätze */
	@Schema(description = "Anzahl der Internatsplätze")
	public int internatsplaetze = 0;

	/**
	 * Erstellt neue Grunddaten für eine Organiationseinheit der Schuldatei
	 */
	public SchuldateiOrganisationseinheitGrunddaten() {
		// Die Initialisierung mit Defaults erfolgt direkt über die Attribute
	}

}
