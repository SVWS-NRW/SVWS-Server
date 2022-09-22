package de.nrw.schule.svws.core.types.schule;

import java.util.Vector;

import de.nrw.schule.svws.core.data.schule.BerufskollegFachklassenKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die Fachklassen eines Berufskollegs zur Verfügung.
 *  
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public class BerufskollegFachklassen {

	// GENERIERTER CODE ANFANG


	/** Ein Vektor mit allen Fachklassen */
	private static final @NotNull Vector<@NotNull BerufskollegFachklassen> _values = initFachklassen();

	/**
	 * Initialisiert die Liste aller Fachklassen des Berufkollegs.
	 *
	 * @return   die Liste aller Fachklassen
	 */
	private static @NotNull Vector<@NotNull BerufskollegFachklassen> initFachklassen() {
		@NotNull Vector<@NotNull BerufskollegFachklassen> alle = new Vector<>();
		initFachklassenIndex0(alle);
		initFachklassenIndex10_Teil1(alle);
		initFachklassenIndex10_Teil2(alle);
		initFachklassenIndex10_Teil3(alle);
		initFachklassenIndex10_Teil4(alle);
		initFachklassenIndex10_Teil5(alle);
		initFachklassenIndex10_Teil6(alle);
		initFachklassenIndex10_Teil7(alle);
		initFachklassenIndex10_Teil8(alle);
		initFachklassenIndex10_Teil9(alle);
		initFachklassenIndex10_Teil10(alle);
		initFachklassenIndex10_Teil11(alle);
		initFachklassenIndex20(alle);
		initFachklassenIndex30(alle);
		initFachklassenIndex40(alle);
		initFachklassenIndex50(alle);
		initFachklassenIndex55(alle);
		initFachklassenIndex56(alle);
		initFachklassenIndex57(alle);
		initFachklassenIndex58(alle);
		initFachklassenIndex60(alle);
		initFachklassenIndex61(alle);
		initFachklassenIndex62(alle);
		initFachklassenIndex63(alle);
		initFachklassenIndex70(alle);
		initFachklassenIndex80(alle);
		initFachklassenIndex90(alle);
		initFachklassenIndex91(alle);
		initFachklassenIndex92(alle);
		initFachklassenIndex93(alle);
		initFachklassenIndex94(alle);
		initFachklassenIndex100(alle);
		initFachklassenIndex110(alle);
		initFachklassenIndex120(alle);
		initFachklassenIndex130(alle);
		initFachklassenIndex140(alle);
		initFachklassenIndex141(alle);
		initFachklassenIndex145(alle);
		initFachklassenIndex146(alle);
		initFachklassenIndex150(alle);
		initFachklassenIndex160(alle);
		initFachklassenIndex170(alle);
		initFachklassenIndex180(alle);
		initFachklassenIndex190(alle);
		initFachklassenIndex200(alle);
		initFachklassenIndex210_Teil1(alle);
		initFachklassenIndex210_Teil2(alle);
		initFachklassenIndex220(alle);
		initFachklassenIndex230(alle);
		initFachklassenIndex240(alle);
		initFachklassenIndex250(alle);
		initFachklassenIndex260(alle);
		initFachklassenIndex270(alle);
		initFachklassenIndex280(alle);
		initFachklassenIndex290(alle);
		initFachklassenIndex300(alle);
		initFachklassenIndex310(alle);
		initFachklassenIndex320(alle);
		initFachklassenIndex330(alle);
		initFachklassenIndex340(alle);
		initFachklassenIndex350(alle);
		initFachklassenIndex370(alle);
		initFachklassenIndex940(alle);
		initFachklassenIndex960(alle);
		initFachklassenIndex970(alle);
		initFachklassenIndex980(alle);
		return alle;
	}


	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 0
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex0(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Präzisionswerkzeugmechaniker/-in - Schneidwerkzeuge */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_0_620_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(0, 0, "620", "01", false, null, null, "TN", null, "A4", "Präzisionswerkzeugmechaniker/-in - Schneidwerkzeuge", "Präzisionswerkzeugmechaniker - Schneidwerkzeuge", "Präzisionswerkzeugmechanikerin - Schneidwerkzeuge", null, null)
		}));

		/** Fachklasse Präzisionswerkzeugmechaniker/-in - Zerspanwerkzeuge */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_0_620_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1000, 0, "620", "02", false, null, null, "TN", null, "A4", "Präzisionswerkzeugmechaniker/-in - Zerspanwerkzeuge", "Präzisionswerkzeugmechaniker - Zerspanwerkzeuge", "Präzisionswerkzeugmechanikerin - Zerspanwerkzeuge", null, null)
		}));

		/** Fachklasse Prüftechnologe/-in (Keramik) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_0_622_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2000, 0, "622", "00", false, null, null, "OZ", null, "A3", "Prüftechnologe/-in (Keramik)", "Prüftechnologe (Keramik)", "Prüftechnologin (Keramik)", null, null)
		}));

		/** Fachklasse Fachpraktiker/-in für Kreislauf- und Abfallwirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_0_898_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(3000, 0, "898", "00", false, null, null, "OZ", null, "A3", "Fachpraktiker/-in für Kreislauf- und Abfallwirtschaft", "Fachpraktiker für Kreislauf- und Abfallwirtschaft", "Fachpraktikerin für Kreislauf- und Abfallwirtschaft", null, null)
		}));

		/** Fachklasse Fachpraktiker/-in für Informationstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_0_899_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(4000, 0, "899", "00", false, null, null, "OZ", null, "A3", "Fachpraktiker/-in für Informationstechnik", "Fachpraktiker für Informationstechnik", "Fachpraktikerin für Informationstechnik", null, null)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 10 (Teil 1)
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex10_Teil1(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Anlagenmechaniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(5000, 10, "101", "00", false, "T", "MT", "TN", null, "A4", "Anlagenmechaniker/-in", "Anlagenmechaniker", "Anlagenmechanikerin", null, null)
		}));

		/** Fachklasse Anlagenmechaniker/-in - Apparatetechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_101_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(6000, 10, "101", "01", true, "T", "MT", null, null, null, "Anlagenmechaniker/-in - Apparatetechnik", "Anlagenmechaniker - Apparatetechnik", "Anlagenmechanikerin - Apparatetechnik", null, 2020)
		}));

		/** Fachklasse Anlagenmechaniker/-in - Schweißtechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_101_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(7000, 10, "101", "02", true, "T", "MT", null, null, null, "Anlagenmechaniker/-in - Schweißtechnik", "Anlagenmechaniker - Schweißtechnik", "Anlagenmechanikerin - Schweißtechnik", null, 2014)
		}));

		/** Fachklasse Anlagenmechaniker/-in - Versorgungstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_101_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(8000, 10, "101", "03", true, "T", "MT", null, null, null, "Anlagenmechaniker/-in - Versorgungstechnik", "Anlagenmechaniker - Versorgungstechnik", "Anlagenmechanikerin - Versorgungstechnik", null, 2014)
		}));

		/** Fachklasse Anlagenmechaniker/-in - Sanitär-, Heizungs- und Klimatechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_101_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(9000, 10, "101", "04", false, "T", "MT", "TN", null, "A4", "Anlagenmechaniker/-in - Sanitär-, Heizungs- und Klimatechnik", "Anlagenmechaniker - Sanitär-, Heizungs- und Klimatechnik", "Anlagenmechanikerin - Sanitär-, Heizungs- und Klimatechnik", null, null)
		}));

		/** Fachklasse Arzthelfer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_102_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(10000, 10, "102", "00", true, "O", "OH", null, null, null, "Arzthelfer/-in", "Arzthelfer", "Arzthelferin", null, 2014)
		}));

		/** Fachklasse Asphaltbauer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_103_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(11000, 10, "103", "00", false, "O", "OH", "TN", null, "A3", "Asphaltbauer/-in", "Asphaltbauer", "Asphaltbauerin", null, null)
		}));

		/** Fachklasse Assistent/-in an Bibliotheken (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_104_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(12000, 10, "104", "00", true, "W", "WV", null, null, null, "Assistent/-in an Bibliotheken", "Assistent an Bibliotheken", "Assistentin an Bibliotheken", null, 2014)
		}));

		/** Fachklasse Aufbereitungsmechaniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_105_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(13000, 10, "105", "00", true, "O", "OH", null, null, null, "Aufbereitungsmechaniker/-in", "Aufbereitungsmechaniker", "Aufbereitungsmechanikerin", null, 2012)
		}));

		/** Fachklasse Aufbereitungsmechaniker/-in - Naturstein */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_105_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(14000, 10, "105", "01", false, "O", "OH", "TN", null, "A3", "Aufbereitungsmechaniker/-in - Naturstein", "Aufbereitungsmechaniker - Naturstein", "Aufbereitungsmechanikerin - Naturstein", null, null)
		}));

		/** Fachklasse Aufbereitungsmechaniker/-in - feuerfeste und keramische Rohstoffe */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_105_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(15000, 10, "105", "02", false, "O", "OH", "TN", null, "A3", "Aufbereitungsmechaniker/-in - feuerfeste und keramische Rohstoffe", "Aufbereitungsmechaniker - feuerfeste und keramische Rohstoffe", "Aufbereitungsmechanikerin - feuerfeste und keramische Rohstoffe", null, null)
		}));

		/** Fachklasse Aufbereitungsmechaniker/-in - Sand und Kies */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_105_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(16000, 10, "105", "03", false, "O", "OH", "TN", null, "A3", "Aufbereitungsmechaniker/-in - Sand und Kies", "Aufbereitungsmechaniker - Sand und Kies", "Aufbereitungsmechanikerin - Sand und Kies", null, null)
		}));

		/** Fachklasse Aufbereitungsmechaniker/-in - Steinkohle */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_105_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(17000, 10, "105", "04", false, "O", "OH", "TN", null, "A3", "Aufbereitungsmechaniker/-in - Steinkohle", "Aufbereitungsmechaniker - Steinkohle", "Aufbereitungsmechanikerin - Steinkohle", null, null)
		}));

		/** Fachklasse Aufbereitungsmechaniker/-in - Braunkohle */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_105_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(18000, 10, "105", "05", false, "O", "OH", "TN", null, "A3", "Aufbereitungsmechaniker/-in - Braunkohle", "Aufbereitungsmechaniker - Braunkohle", "Aufbereitungsmechanikerin - Braunkohle", null, null)
		}));

		/** Fachklasse Augenoptiker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_106_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(19000, 10, "106", "00", false, "O", "OH", "TN", null, "A3", "Augenoptiker/-in", "Augenoptiker", "Augenoptikerin", null, null)
		}));

		/** Fachklasse Ausbaufacharbeiter/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_107_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(20000, 10, "107", "00", true, "T", "BT", null, null, null, "Ausbaufacharbeiter/-in", "Ausbaufacharbeiter", "Ausbaufacharbeiterin", null, 2016)
		}));

		/** Fachklasse Ausbaufacharbeiter/-in - Estricharbeiten */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_107_50", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(21000, 10, "107", "50", false, "T", "BT", "TN", null, "A2", "Ausbaufacharbeiter/-in - Estricharbeiten", "Ausbaufacharbeiter - Estricharbeiten", "Ausbaufacharbeiterin - Estricharbeiten", null, null)
		}));

		/** Fachklasse Ausbaufacharbeiter/-in - Fliesen-/Platten-/Mosaikarbeiten */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_107_51", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(22000, 10, "107", "51", false, "T", "BT", "TN", null, "A2", "Ausbaufacharbeiter/-in - Fliesen-/Platten-/Mosaikarbeiten", "Ausbaufacharbeiter - Fliesen-/Platten-/Mosaikarbeiten", "Ausbaufacharbeiterin - Fliesen-/Platten-/Mosaikarbeiten", null, null)
		}));

		/** Fachklasse Ausbaufacharbeiter/-in - Stuckateurarbeiten */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_107_52", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(23000, 10, "107", "52", false, "T", "BT", "TN", null, "A2", "Ausbaufacharbeiter/-in - Stuckateurarbeiten", "Ausbaufacharbeiter - Stuckateurarbeiten", "Ausbaufacharbeiterin - Stuckateurarbeiten", null, null)
		}));

		/** Fachklasse Ausbaufacharbeiter/-in - Trockenbauarbeiten */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_107_53", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(24000, 10, "107", "53", false, "T", "BT", "TN", null, "A2", "Ausbaufacharbeiter/-in - Trockenbauarbeiten", "Ausbaufacharbeiter - Trockenbauarbeiten", "Ausbaufacharbeiterin - Trockenbauarbeiten", null, null)
		}));

		/** Fachklasse Ausbaufacharbeiter/-in- Wärme-/Kälte-/Schallschutzarbeiten */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_107_54", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(25000, 10, "107", "54", false, "T", "BT", "TN", null, "A2", "Ausbaufacharbeiter/-in- Wärme-/Kälte-/Schallschutzarbeiten", "Ausbaufacharbeiter - Wärme-/Kälte-/Schallschutzarbeiten", "Ausbaufacharbeiterin - Wärme-/Kälte-/Schallschutzarbeiten", null, null)
		}));

		/** Fachklasse Ausbaufacharbeiter/-in - Zimmererarbeiten */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_107_55", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(26000, 10, "107", "55", false, "T", "BT", "TN", null, "A2", "Ausbaufacharbeiter/-in - Zimmererarbeiten", "Ausbaufacharbeiter - Zimmererarbeiten", "Ausbaufacharbeiterin - Zimmererarbeiten", null, null)
		}));

		/** Fachklasse Automobilkaufmann/-frau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_108_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(27000, 10, "108", "00", false, "O", "OH", "WV", null, "A3", "Automobilkaufmann/-frau", "Automobilkaufmann", "Automobilkauffrau", null, null)
		}));

		/** Fachklasse Automobilmechaniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_109_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(28000, 10, "109", "00", true, "T", "MT", null, null, null, "Automobilmechaniker/-in", "Automobilmechaniker", "Automobilmechanikerin", null, 2014)
		}));

		/** Fachklasse Bäcker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_110_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(29000, 10, "110", "00", false, "E", "EH", "EH", null, "A3", "Bäcker/-in", "Bäcker", "Bäckerin", null, null)
		}));

		/** Fachklasse Backofenbauer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_111_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(30000, 10, "111", "00", true, "T", "BT", null, null, null, "Backofenbauer/-in", "Backofenbauer", "Backofenbauerin", null, 2014)
		}));

		/** Fachklasse Bandagist/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_112_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(31000, 10, "112", "00", true, "T", "TB", null, null, null, "Bandagist/-in", "Bandagist", "Bandagistin", null, 2012)
		}));

		/** Fachklasse Bankkaufmann/-frau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_113_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(32000, 10, "113", "00", false, "W", "WV", "WV", null, "A3", "Bankkaufmann/-frau", "Bankkaufmann", "Bankkauffrau", null, null)
		}));

		/** Fachklasse Baugeräteführer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_114_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(33000, 10, "114", "00", false, "O", "OH", "TN", null, "A3", "Baugeräteführer/-in", "Baugeräteführer", "Baugeräteführerin", null, null)
		}));

		/** Fachklasse Baustoffprüfer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_115_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(34000, 10, "115", "00", true, "T", "BT", "TN", null, "A3", "Baustoffprüfer/-in", "Baustoffprüfer", "Baustoffprüferin", null, 2021)
		}));

		/** Fachklasse Baustoffprüfer/-in - Boden (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_115_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(35000, 10, "115", "01", true, "T", "BT", null, null, null, "Baustoffprüfer/-in - Boden", "Baustoffprüfer - Boden", "Baustoffprüferin - Boden", null, 2012)
		}));

		/** Fachklasse Baustoffprüfer/-in - Mörtel und Boden (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_115_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(36000, 10, "115", "02", true, "T", "BT", null, null, null, "Baustoffprüfer/-in - Mörtel und Boden", "Baustoffprüfer - Mörtel und Boden", "Baustoffprüferin - Mörtel und Boden", null, 2012)
		}));

		/** Fachklasse Baustoffprüfer/-in - Bituminöse Massen (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_115_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(37000, 10, "115", "03", true, "T", "BT", null, null, null, "Baustoffprüfer/-in - Bituminöse Massen", "Baustoffprüfer - Bituminöse Massen", "Baustoffprüferin - Bituminöse Massen", null, 2012)
		}));

		/** Fachklasse Baustoffprüfer/-in - Asphalttechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_115_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(38000, 10, "115", "04", false, "T", "BT", "TN", null, "A3", "Baustoffprüfer/-in - Asphalttechnik", "Baustoffprüfer - Asphalttechnik", "Baustoffprüferin - Asphalttechnik", null, null)
		}));

		/** Fachklasse Baustoffprüfer/-in - Geotechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_115_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(39000, 10, "115", "05", false, "T", "BT", "TN", null, "A3", "Baustoffprüfer/-in - Geotechnik", "Baustoffprüfer - Geotechnik", "Baustoffprüferin - Geotechnik", null, null)
		}));

		/** Fachklasse Baustoffprüfer/-in - Mörtel- und Betontechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_115_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(40000, 10, "115", "06", false, "T", "BT", "TN", null, "A3", "Baustoffprüfer/-in - Mörtel- und Betontechnik", "Baustoffprüfer - Mörtel- und Betontechnik", "Baustoffprüferin - Mörtel- und Betontechnik", null, null)
		}));

		/** Fachklasse Bauwerksabdichter/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_116_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(41000, 10, "116", "00", false, "T", "BT", "TN", null, "A3", "Bauwerksabdichter/-in", "Bauwerksabdichter", "Bauwerksabdichterin", null, null)
		}));

		/** Fachklasse Bauzeichner/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_117_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(42000, 10, "117", "00", false, "T", "BT", "TN", null, "A3", "Bauzeichner/-in", "Bauzeichner", "Bauzeichnerin", null, null)
		}));

		/** Fachklasse Bauzeichner/-in - Architektur (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_117_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(43000, 10, "117", "01", true, "T", "BT", null, null, null, "Bauzeichner/-in - Architektur", "Bauzeichner - Architektur", "Bauzeichnerin - Architektur", null, 2012)
		}));

		/** Fachklasse Bauzeichner/-in - Ingenieurbau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_117_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(44000, 10, "117", "02", true, "T", "BT", null, null, null, "Bauzeichner/-in - Ingenieurbau", "Bauzeichner - Ingenieurbau", "Bauzeichnerin - Ingenieurbau", null, 2012)
		}));

		/** Fachklasse Bauzeichner/-in - Tief-, Straßen- u. Landschaftsbau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_117_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(45000, 10, "117", "03", true, "T", "BT", null, null, null, "Bauzeichner/-in - Tief-, Straßen- u. Landschaftsbau", "Bauzeichner - Tief-, Straßen- u. Landschaftsbau", "Bauzeichnerin Tief-, Straßen- u. Landschaftsbau", null, 2012)
		}));

		/** Fachklasse Behälter- und Apparatebauer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_118_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(46000, 10, "118", "00", false, "T", "MT", "TN", null, "A4", "Behälter- und Apparatebauer/-in", "Behälter- und Apparatebauer", "Behälter- und Apparatebauerin", null, null)
		}));

		/** Fachklasse Bekleidungsfertiger/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_119_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(47000, 10, "119", "00", true, "T", "TB", null, null, null, "Bekleidungsfertiger/-in", "Bekleidungsfertiger", "Bekleidungsfertigerin", null, 2014)
		}));

		/** Fachklasse Bekleidungsnäher/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_120_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(48000, 10, "120", "00", true, "T", "TB", null, null, null, "Bekleidungsnäher/-in", "Bekleidungsnäher", "Bekleidungsnäherin", null, 2014)
		}));

		/** Fachklasse Bekleidungsschneider/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_121_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(49000, 10, "121", "00", true, "T", "TB", null, null, null, "Bekleidungsschneider/-in", "Bekleidungsschneider", "Bekleidungsschneiderin", null, 2014)
		}));

		/** Fachklasse Berg- und Maschinenmann/-frau - Transport- und Instandhaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_122_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(50000, 10, "122", "01", false, "O", "OH", "OZ", null, "A2", "Berg- und Maschinenmann/-frau - Transport- und Instandhaltung", "Berg- und Maschinenmann - Transport und Instandhaltung", "Berg- und Maschinenfrau - Transport und Instandhaltung", null, null)
		}));

		/** Fachklasse Berg- und Maschinenmann/-frau - Vortrieb und Gewinnung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_122_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(51000, 10, "122", "02", false, "O", "OH", "OZ", null, "A2", "Berg- und Maschinenmann/-frau - Vortrieb und Gewinnung", "Berg- und Maschinenmann - Vortrieb und Gewinnung", "Berg- und Maschinenfrau - Vortrieb und Gewinnung", null, null)
		}));

		/** Fachklasse Bergmechaniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_123_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(52000, 10, "123", "00", true, "T", "MT", null, null, null, "Bergmechaniker/-in", "Bergmechaniker", "Bergmechanikerin", null, 2014)
		}));

		/** Fachklasse Bergvermessungstechniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_124_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(53000, 10, "124", "00", true, "O", "OH", null, null, null, "Bergvermessungstechniker/-in", "Bergvermessungstechniker", "Bergvermessungstechnikerin", null, 2014)
		}));

		/** Fachklasse Berufskraftfahrer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_125_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(54000, 10, "125", "00", false, "O", "OH", "TN", null, "A3", "Berufskraftfahrer/-in", "Berufskraftfahrer", "Berufskraftfahrerin", null, null)
		}));

		/** Fachklasse Berufskraftfahrer/-in - Güterverkehr (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_125_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(55000, 10, "125", "01", true, "O", "OH", null, null, null, "Berufskraftfahrer/-in - Güterverkehr", "Berufskraftfahrer - Güterverkehr", "Berufskraftfahrerin - Güterverkehr", null, 2012)
		}));

		/** Fachklasse Berufskraftfahrer/-in - Personenverkehr (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_125_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(56000, 10, "125", "02", true, "O", "OH", null, null, null, "Berufskraftfahrer/-in - Personenverkehr", "Berufskraftfahrer - Personenverkehr", "Berufskraftfahrerin - Personenverkehr", null, 2012)
		}));

		/** Fachklasse Beton- und Stahlbetonbauer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_126_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(57000, 10, "126", "00", false, "T", "BT", "TN", null, "A3", "Beton- und Stahlbetonbauer/-in", "Beton- und Stahlbetonbauer", "Beton- und Stahlbetonbauerin", null, null)
		}));

		/** Fachklasse Betonfertigteilbauer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_127_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(58000, 10, "127", "00", false, "O", "OH", "TN", null, "A3", "Betonfertigteilbauer/-in", "Betonfertigteilbauer", "Betonfertigteilbauerin", null, null)
		}));

		/** Fachklasse Betonstein- u. Terrazzohersteller/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_128_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(59000, 10, "128", "00", true, "T", "BT", null, null, null, "Betonstein- u. Terrazzohersteller/-in", "Betonstein- u. Terrazzohersteller", "Betonstein- u. Terrazzoherstellerin", null, 2012)
		}));

		/** Fachklasse Binnenschiffer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_129_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(60000, 10, "129", "00", false, "O", "OH", "TN", null, "A3", "Binnenschiffer/-in", "Binnenschiffer", "Binnenschifferin", null, null)
		}));

		/** Fachklasse Biologielaborant/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_130_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(61000, 10, "130", "00", false, "T", "CP", "TN", null, "A4", "Biologielaborant/-in", "Biologielaborant", "Biologielaborantin", null, null)
		}));

		/** Fachklasse Biologiemodellmacher/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_131_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(62000, 10, "131", "00", false, "O", "OH", "TN", null, "A3", "Biologiemodellmacher/-in", "Biologiemodellmacher", "Biologiemodellmacherin", null, null)
		}));

		/** Fachklasse Bogenmacher/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_132_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(63000, 10, "132", "00", false, "O", "OH", "TN", null, "A3", "Bogenmacher/-in", "Bogenmacher", "Bogenmacherin", null, null)
		}));

		/** Fachklasse Bohrer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_133_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(64000, 10, "133", "00", true, "T", "MT", null, null, null, "Bohrer/-in", "Bohrer", "Bohrerin", null, 2012)
		}));

		/** Fachklasse Bootsbauer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_134_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(65000, 10, "134", "00", true, "O", "OH", null, null, null, "Bootsbauer/-in", "Bootsbauer", "Bootsbauerin", null, 2015)
		}));

		/** Fachklasse Bootsbauer/-in - Technik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_134_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(66000, 10, "134", "01", false, "O", "OH", "TN", null, "A4", "Bootsbauer/-in - Technik", "Bootsbauer - Technik", "Bootsbauerin - Technik", null, null)
		}));

		/** Fachklasse Bootsbauer/-in - Neu-, Aus- und Umbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_134_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(67000, 10, "134", "02", false, "O", "OH", "TN", null, "A4", "Bootsbauer/-in - Neu-, Aus- und Umbau", "Bootsbauer - Neu-, Aus- und Umbau", "Bootsbauerin - Neu-, Aus- und Umbau", null, null)
		}));

		/** Fachklasse Böttcher/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_135_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(68000, 10, "135", "00", false, "T", "HT", "OZ", null, "A3", "Böttcher/-in", "Böttcher", "Böttcherin", null, null)
		}));

		/** Fachklasse Brauer/-in und Mälzer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_136_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(69000, 10, "136", "00", false, "O", "OH", "EV", null, "A3", "Brauer/-in und Mälzer/-in", "Brauer und Mälzer", "Brauerin und Mälzerin", null, null)
		}));

		/** Fachklasse Brenner/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_137_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(70000, 10, "137", "00", false, "O", "OH", "TN", null, "A3", "Brenner/-in", "Brenner", "Brennerin", null, null)
		}));

		/** Fachklasse Brillenoptikschleifer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_138_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(71000, 10, "138", "00", true, "O", "OH", null, null, null, "Brillenoptikschleifer/-in", "Brillenoptikschleifer", "Brillenoptikschleiferin", null, 2014)
		}));

		/** Fachklasse Brunnenbauer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_139_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(72000, 10, "139", "00", false, "T", "BT", "TN", null, "A3", "Brunnenbauer/-in", "Brunnenbauer", "Brunnenbauerin", null, null)
		}));

		/** Fachklasse Buchbinder/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_140_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(73000, 10, "140", "00", false, "T", "DT", "TN", null, "A3", "Buchbinder/-in", "Buchbinder", "Buchbinderin", null, null)
		}));

		/** Fachklasse Buchbinder/-in - Einzel- und Sonderanfertigung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_140_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(74000, 10, "140", "01", true, "T", "DT", null, null, null, "Buchbinder/-in - Einzel- und Sonderanfertigung", "Buchbinder - Einzel- und Sonderanfertigung", "Buchbinderin - Einzel- und Sonderanfertigung", null, 2014)
		}));

		/** Fachklasse Buchbinder/-in - Buchfertigung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_140_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(75000, 10, "140", "02", true, "T", "DT", null, null, null, "Buchbinder/-in - Buchfertigung", "Buchbinder - Buchfertigung", "Buchbinderin - Buchfertigung", null, 2014)
		}));

		/** Fachklasse Buchbinder/-in - Druckweiterverarbeitung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_140_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(76000, 10, "140", "03", true, "T", "DT", null, null, null, "Buchbinder/-in - Druckweiterverarbeitung", "Buchbinder - Druckweiterverarbeitung", "Buchbinderin - Druckweiterverarbeitung", null, 2014)
		}));

		/** Fachklasse Buchhändler/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_141_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(77000, 10, "141", "00", false, "W", "WV", "WV", null, "A3", "Buchhändler/-in", "Buchhändler", "Buchhändlerin", null, null)
		}));

		/** Fachklasse Büchsenmacher/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_142_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(78000, 10, "142", "00", false, "T", "MT", "OZ", null, "A3", "Büchsenmacher/-in", "Büchsenmacher", "Büchsenmacherin", null, null)
		}));

		/** Fachklasse Büroinformationselektroniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_143_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(79000, 10, "143", "00", true, "T", "ET", null, null, null, "Büroinformationselektroniker/-in", "Büroinformationselektroniker", "Büroinformationselektronikerin", null, 2014)
		}));

		/** Fachklasse Bürokaufmann/-frau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_144_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(80000, 10, "144", "00", true, "W", "WV", null, null, null, "Bürokaufmann/-frau", "Bürokaufmann", "Bürokauffrau", null, 2017)
		}));

		/** Fachklasse Bürsten- u. Pinselmacher/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_145_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(81000, 10, "145", "00", true, "O", "OH", null, null, null, "Bürsten- u. Pinselmacher/-in", "Bürsten- u. Pinselmacher", "Bürsten- u. Pinselmacherin", null, 2012)
		}));

		/** Fachklasse Bürsten- und Pinselmacher/-in - Bürstenherstellung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_145_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(82000, 10, "145", "01", false, "O", "OH", "OZ", null, "A3", "Bürsten- und Pinselmacher/-in - Bürstenherstellung", "Bürsten- und Pinselmacher - Bürstenherstellung", "Bürsten- und Pinselmacherin - Bürstenherstellung", null, null)
		}));

		/** Fachklasse Bürsten- und Pinselmacher/-in - Pinselherstellung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_145_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(83000, 10, "145", "02", false, "O", "OH", "OZ", null, "A3", "Bürsten- und Pinselmacher/-in - Pinselherstellung", "Bürsten- und Pinselmacher - Pinselherstellung", "Bürsten- und Pinselmacherin - Pinselherstellung", null, null)
		}));

		/** Fachklasse Chemiebetriebsjungwerker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_146_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(84000, 10, "146", "00", true, "T", "CP", null, null, null, "Chemiebetriebsjungwerker/-in", "Chemiebetriebsjungwerker", "Chemiebetriebsjungwerkerin", null, 2014)
		}));

		/** Fachklasse Chemielaborant/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_147_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(85000, 10, "147", "00", false, "T", "CP", "TN", null, "A4", "Chemielaborant/-in", "Chemielaborant", "Chemielaborantin", null, null)
		}));

		/** Fachklasse Chemielaborant/-in - Chemie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_147_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(86000, 10, "147", "01", true, "T", "CP", null, null, null, "Chemielaborant/-in - Chemie", "Chemielaborant - Chemie", "Chemielaborantin - Chemie", null, 2012)
		}));

		/** Fachklasse Chemielaborant/-in - Kohle (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_147_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(87000, 10, "147", "02", true, "T", "CP", null, null, null, "Chemielaborant/-in - Kohle", "Chemielaborant - Kohle", "Chemielaborantin - Kohle", null, 2012)
		}));

		/** Fachklasse Chemielaborant/-in - Metalle (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_147_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(88000, 10, "147", "03", true, "T", "CP", null, null, null, "Chemielaborant/-in - Metalle", "Chemielaborant - Metalle", "Chemielaborantin - Metalle", null, 2012)
		}));

		/** Fachklasse Chemielaborant/-in - Silikat (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_147_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(89000, 10, "147", "04", true, "T", "CP", null, null, null, "Chemielaborant/-in - Silikat", "Chemielaborant - Silikat", "Chemielaborantin - Silikat", null, 2012)
		}));

		/** Fachklasse Chemielaborjungwerker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_148_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(90000, 10, "148", "00", false, "T", "CP", "TN", null, "A2", "Chemielaborjungwerker/-in", "Chemielaborjungwerker", "Chemielaborjungwerkerin", null, null)
		}));

		/** Fachklasse Chemigraf/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_149_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(91000, 10, "149", "00", true, "T", "CP", null, null, null, "Chemigraf/-in", "Chemigraf", "Chemigrafin", null, 2014)
		}));

		/** Fachklasse Chemikant/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_150_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(92000, 10, "150", "00", false, "T", "CP", "TN", null, "A4", "Chemikant/-in", "Chemikant", "Chemikantin", null, null)
		}));

		/** Fachklasse Chirurgiemechaniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_151_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(93000, 10, "151", "00", false, "T", "MT", "TN", null, "A4", "Chirurgiemechaniker/-in", "Chirurgiemechaniker", "Chirurgiemechanikerin", null, null)
		}));

		/** Fachklasse Dachdecker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_152_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(94000, 10, "152", "00", true, "T", "BT", "TN", null, "A3", "Dachdecker/-in", "Dachdecker", "Dachdeckerin", null, 2016)
		}));

		/** Fachklasse Dachdecker/-in - Dach-, Wand- und Abdichtungstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_152_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(95000, 10, "152", "01", true, "T", "BT", "TN", null, "A3", "Dachdecker/-in - Dach-, Wand- und Abdichtungstechnik", "Dachdecker - Dach-, Wand- und Abdichtungstechnik", "Dachdeckerin - Dach-, Wand- und Abdichtungstechnik", null, 2021)
		}));

		/** Fachklasse Dachdecker/-in - Reetdachtechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_152_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(96000, 10, "152", "02", false, "T", "BT", "TN", null, "A3", "Dachdecker/-in - Reetdachtechnik", "Dachdecker - Reetdachtechnik", "Dachdeckerin - Reetdachtechnik", null, null)
		}));

		/** Fachklasse Dachdecker/-in - Dachdeckungstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_152_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(97000, 10, "152", "03", false, "T", "BT", "TN", null, "A3", "Dachdecker/-in - Dachdeckungstechnik", "Dachdecker - Dachdeckungstechnik", "Dachdeckerin - Dachdeckungstechnik", null, null)
		}));

		/** Fachklasse Dachdecker/-in - Abdichtungstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_152_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(98000, 10, "152", "04", false, "T", "BT", "TN", null, "A3", "Dachdecker/-in - Abdichtungstechnik", "Dachdecker - Abdichtungstechnik", "Dachdeckerin - Abdichtungstechnik", null, null)
		}));

		/** Fachklasse Dachdecker/-in - Außenwandbekleidungstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_152_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(99000, 10, "152", "05", false, "T", "BT", "TN", null, "A3", "Dachdecker/-in - Außenwandbekleidungstechnik", "Dachdecker - Außenwandbekleidungstechnik", "Dachdeckerin - Außenwandbekleidungstechnik", null, null)
		}));

		/** Fachklasse Dachdecker/-in - Energietechnik an Dach und Wand */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_152_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(100000, 10, "152", "06", false, "T", "BT", "TN", null, "A3", "Dachdecker/-in - Energietechnik an Dach und Wand", "Dachdecker - Energietechnik an Dach und Wand", "Dachdeckerin - Energietechnik an Dach und Wand", null, null)
		}));

		/** Fachklasse Damenschneider/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_153_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(101000, 10, "153", "00", true, "T", "TB", null, null, null, "Damenschneider/-in", "Damenschneider", "Damenschneiderin", null, 2012)
		}));

		/** Fachklasse Datenverarbeitungskaufmann/-frau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_154_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(102000, 10, "154", "00", true, "W", "WV", null, null, null, "Datenverarbeitungskaufmann/-frau", "Datenverarbeitungskaufmann", "Datenverarbeitungskauffrau", null, 2014)
		}));

		/** Fachklasse Dekorvorlagenhersteller/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_155_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(103000, 10, "155", "00", true, "O", "OH", null, null, null, "Dekorvorlagenhersteller/-in", "Dekorvorlagenhersteller", "Dekorvorlagenherstellerin", null, 2016)
		}));

		/** Fachklasse Destillateur/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_156_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(104000, 10, "156", "00", false, "O", "OH", "EH", null, "A3", "Destillateur/-in", "Destillateur", "Destillateurin", null, null)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 10 (Teil 2)
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex10_Teil2(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Diamantschleifer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_157_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(105000, 10, "157", "00", true, "O", "OH", "OZ", null, "A3", "Diamantschleifer/-in", "Diamantschleifer", "Diamantschleiferin", null, 2021)
		}));

		/** Fachklasse Edelsteinschleifer/-in - Edelsteingravieren */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_157_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(106000, 10, "157", "01", false, null, null, "OZ", null, "A3", "Edelsteinschleifer/-in - Edelsteingravieren", "Edelsteinschleifer - Edelsteingravieren", "Edelsteinschleiferin - Edelsteingravieren", null, null)
		}));

		/** Fachklasse Edelsteinschleifer/-in - Edelsteinschleifen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_157_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(107000, 10, "157", "02", false, null, null, "OZ", null, "A3", "Edelsteinschleifer/-in - Edelsteinschleifen", "Edelsteinschleifer - Edelsteinschleifen", "Edelsteinschleiferin - Edelsteinschleifen", null, null)
		}));

		/** Fachklasse Edelsteinschleifer/-in - Industriediamantschleifen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_157_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(108000, 10, "157", "03", false, null, null, "OZ", null, "A3", "Edelsteinschleifer/-in - Industriediamantschleifen", "Edelsteinschleifer - Industriediamantschleifen", "Edelsteinschleiferin - Industriediamantschleifen", null, null)
		}));

		/** Fachklasse Edelsteinschleifer/-in - Schmuckdiamantschleifen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_157_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(109000, 10, "157", "04", false, null, null, "OZ", null, "A3", "Edelsteinschleifer/-in - Schmuckdiamantschleifen", "Edelsteinschleifer - Schmuckdiamantschleifen", "Edelsteinschleiferin - Schmuckdiamantschleifen", null, null)
		}));

		/** Fachklasse Dienstleistungsfachkraft im Postbetrieb (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_158_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(110000, 10, "158", "00", true, "W", "WV", null, null, null, "Dienstleistungsfachkraft im Postbetrieb", "Dienstleistungsfachkraft im Postbetrieb", "Dienstleistungsfachkraft im Postbetrieb", null, 2014)
		}));

		/** Fachklasse Drahtwarenmacher/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_159_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(111000, 10, "159", "00", true, "T", "MT", null, null, null, "Drahtwarenmacher/-in", "Drahtwarenmacher", "Drahtwarenmacherin", null, 2015)
		}));

		/** Fachklasse Drahtzieher/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_160_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(112000, 10, "160", "00", true, "T", "MT", null, null, null, "Drahtzieher/-in", "Drahtzieher", "Drahtzieherin", null, 2015)
		}));

		/** Fachklasse Drechsler/-in (Elfenbeinschnitzer) (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_161_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(113000, 10, "161", "00", true, "O", "OH", null, null, null, "Drechsler/-in (Elfenbeinschnitzer)", "Drechsler (Elfenbeinschnitzer)", "Drechslerin (Elfenbeinschnitzerin)", null, 2012)
		}));

		/** Fachklasse Drechsler/-in - Drechseln */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_161_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(114000, 10, "161", "01", false, "O", "OH", "OZ", null, "A3", "Drechsler/-in - Drechseln", "Drechsler - Drechseln", "Drechslerin - Drechseln", null, null)
		}));

		/** Fachklasse Drechsler/-in - Elfenbeinschnitzen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_161_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(115000, 10, "161", "02", false, "O", "OH", "OZ", null, "A3", "Drechsler/-in - Elfenbeinschnitzen", "Drechsler - Elfenbeinschnitzen", "Drechslerin - Elfenbeinschnitzen", null, null)
		}));

		/** Fachklasse Dreher/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_162_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(116000, 10, "162", "00", true, "T", "MT", null, null, null, "Dreher/-in", "Dreher", "Dreherin", null, 2014)
		}));

		/** Fachklasse Drogist/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_163_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(117000, 10, "163", "00", false, "O", "OH", "WV", null, "A3", "Drogist/-in", "Drogist", "Drogistin", null, null)
		}));

		/** Fachklasse Drucker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_164_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(118000, 10, "164", "00", true, "T", "DT", null, null, null, "Drucker/-in", "Drucker", "Druckerin", null, 2014)
		}));

		/** Fachklasse Drucker/-in - Digitaldruck (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_164_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(119000, 10, "164", "01", true, "T", "DT", null, null, null, "Drucker/-in - Digitaldruck", "Drucker - Digitaldruck", "Druckerin - Digitaldruck", null, 2014)
		}));

		/** Fachklasse Drucker/-in - Tiefdruck (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_164_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(120000, 10, "164", "02", true, "T", "DT", null, null, null, "Drucker/-in - Tiefdruck", "Drucker - Tiefdruck", "Druckerin - Tiefdruck", null, 2014)
		}));

		/** Fachklasse Drucker/-in - Hochdruck (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_164_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(121000, 10, "164", "03", true, "T", "DT", null, null, null, "Drucker/-in - Hochdruck", "Drucker - Hochdruck", "Druckerin - Hochdruck", null, 2014)
		}));

		/** Fachklasse Drucker/-in - Flachdruck (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_164_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(122000, 10, "164", "04", true, "T", "DT", null, null, null, "Drucker/-in - Flachdruck", "Drucker - Flachdruck", "Druckerin - Flachdruck", null, 2014)
		}));

		/** Fachklasse Druckformhersteller/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_165_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(123000, 10, "165", "00", true, "T", "DT", null, null, null, "Druckformhersteller/-in", "Druckformhersteller", "Druckformherstellerin", null, 2014)
		}));

		/** Fachklasse Druckvorlagenhersteller/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_166_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(124000, 10, "166", "00", true, "T", "DT", null, null, null, "Druckvorlagenhersteller/-in", "Druckvorlagenhersteller", "Druckvorlagenherstellerin", null, 2014)
		}));

		/** Fachklasse Edelmetallprüfer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_167_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(125000, 10, "167", "00", false, "T", "CP", "OZ", null, "A3", "Edelmetallprüfer/-in", "Edelmetallprüfer", "Edelmetallprüferin", null, null)
		}));

		/** Fachklasse Edelsteinfasser/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_168_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(126000, 10, "168", "00", false, "O", "OH", "OZ", null, "A4", "Edelsteinfasser/-in", "Edelsteinfasser", "Edelsteinfasserin", null, null)
		}));

		/** Fachklasse Edelsteingraveur/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_169_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(127000, 10, "169", "00", true, "O", "OH", "OZ", null, "A3", "Edelsteingraveur/-in", "Edelsteingraveur", "Edelsteingraveurin", null, 2021)
		}));

		/** Fachklasse Edelsteinschleifer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_170_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(128000, 10, "170", "00", true, "O", "OH", "OZ", null, "A3", "Edelsteinschleifer/-in", "Edelsteinschleifer", "Edelsteinschleiferin", null, 2021)
		}));

		/** Fachklasse Eisenbahner/-in im Betriebsdienst */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_171_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(129000, 10, "171", "00", false, "O", "OH", "TN", null, "A3", "Eisenbahner/-in im Betriebsdienst", "Eisenbahner im Betriebsdienst", "Eisenbahnerin im Betriebsdienst", null, null)
		}));

		/** Fachklasse Eisenbahner/-in im Betriebsdienst - Fahrweg (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_171_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(130000, 10, "171", "01", true, "O", "OH", null, null, null, "Eisenbahner/-in im Betriebsdienst - Fahrweg", "Eisenbahner im Betriebsdienst - Fahrweg", "Eisenbahnerin im Betriebsdienst - Fahrweg", null, 2016)
		}));

		/** Fachklasse Eisenbahner/-in im Betriebsdienst - Lokführer und Transport (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_171_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(131000, 10, "171", "02", true, "O", "OH", null, null, null, "Eisenbahner/-in im Betriebsdienst - Lokführer und Transport", "Eisenbahner im Betriebsdienst - Lokführer und Transport", "Eisenbahnerin im Betriebsdienst - Lokführer und Transport", null, 2016)
		}));

		/** Fachklasse Elektroanlagenmonteur/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_172_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(132000, 10, "172", "00", false, "T", "ET", "TN", null, "A3", "Elektroanlagenmonteur/-in", "Elektroanlagenmonteur", "Elektroanlagenmonteurin", null, null)
		}));

		/** Fachklasse Elektroinstallateur/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_173_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(133000, 10, "173", "00", true, "T", "ET", null, null, null, "Elektroinstallateur/-in", "Elektroinstallateur", "Elektroinstallateurin", null, 2014)
		}));

		/** Fachklasse Elektromaschinenbauer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_174_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(134000, 10, "174", "00", true, "T", "ET", null, null, null, "Elektromaschinenbauer/-in", "Elektromaschinenbauer", "Elektromaschinenbauerin", null, 2014)
		}));

		/** Fachklasse Elektromaschinenmonteur/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_175_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(135000, 10, "175", "00", true, "T", "ET", null, null, null, "Elektromaschinenmonteur/-in", "Elektromaschinenmonteur", "Elektromaschinenmonteurin", null, 2014)
		}));

		/** Fachklasse Elektromechaniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_176_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(136000, 10, "176", "00", true, "T", "ET", null, null, null, "Elektromechaniker/-in", "Elektromechaniker", "Elektromechanikerin", null, 2014)
		}));

		/** Fachklasse Elektroniker/-in für informationstechnische Systeme (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_177_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(137000, 10, "177", "00", true, "T", "ET", null, null, null, "Elektroniker/-in für informationstechnische Systeme", "Elektroniker für informationstechnische Systeme", "Elektronikerin für informationstechnische Systeme", null, 2012)
		}));

		/** Fachklasse Elektroniker/-in für Automatisierungstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_177_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(138000, 10, "177", "01", false, "T", "ET", "TN", null, "A4", "Elektroniker/-in für Automatisierungstechnik", "Elektroniker für Automatisierungstechnik", "Elektronikerin für Automatisierungstechnik", null, null)
		}));

		/** Fachklasse Elektroniker/-in für Betriebstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_177_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(139000, 10, "177", "02", false, "T", "ET", "TN", null, "A4", "Elektroniker/-in für Betriebstechnik", "Elektroniker für Betriebstechnik", "Elektronikerin für Betriebstechnik", null, null)
		}));

		/** Fachklasse Elektroniker/-in - Energie- und Gebäudetechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_177_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(140000, 10, "177", "03", false, "T", "ET", "TN", null, "A4", "Elektroniker/-in - Energie- und Gebäudetechnik", "Elektroniker - Energie- und Gebäudetechnik", "Elektronikerin - Energie- und Gebäudetechnik", null, null)
		}));

		/** Fachklasse Elektroniker/-in für Gebäude- und Infrastruktursysteme */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_177_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(141000, 10, "177", "04", false, "T", "ET", "TN", null, "A4", "Elektroniker/-in für Gebäude- und Infrastruktursysteme", "Elektroniker für Gebäude- und Infrastruktursysteme", "Elektronikerin für Gebäude- und Infrastruktursysteme", null, null)
		}));

		/** Fachklasse Elektroniker/-in für Geräte und Systeme */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_177_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(142000, 10, "177", "05", false, "T", "ET", "TN", null, "A4", "Elektroniker/-in für Geräte und Systeme", "Elektroniker für Geräte und Systeme", "Elektronikerin für Geräte und Systeme", null, null)
		}));

		/** Fachklasse Elektroniker/-in - Informations- und Telekommunikationstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_177_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(143000, 10, "177", "06", false, "T", "ET", "TN", null, "A4", "Elektroniker/-in - Informations- und Telekommunikationstechnik", "Elektroniker - Informations- und Telekommunikationstechnik", "Elektronikerin - Informations- und Telekommunikationstechnik", null, null)
		}));

		/** Fachklasse Elektroniker/-in für Maschinen und Antriebstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_177_07", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(144000, 10, "177", "07", false, "T", "ET", "TN", null, "A4", "Elektroniker/-in für Maschinen und Antriebstechnik", "Elektroniker für Maschinen und Antriebstechnik", "Elektronikerin für Maschinen und Antriebstechnik", null, null)
		}));

		/** Fachklasse Elektroniker/-in für luftfahrttechnische Systeme (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_177_08", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(145000, 10, "177", "08", true, "T", "ET", null, null, null, "Elektroniker/-in für luftfahrttechnische Systeme", "Elektroniker für luftfahrttechnische Systeme", "Elektronikerin für luftfahrttechnische Systeme", null, 2017)
		}));

		/** Fachklasse Elektroniker/-in - FR Automatisierungstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_177_09", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(146000, 10, "177", "09", false, "T", "ET", "TN", null, "A4", "Elektroniker/-in - FR Automatisierungstechnik", "Elektroniker - FR Automatisierungstechnik", "Elektronikerin - FR Automatisierungstechnik", null, null)
		}));

		/** Fachklasse Elektroniker/-in für Gebäudesystemintegration */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_177_10", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(147000, 10, "177", "10", false, null, null, "TN", null, "A4", "Elektroniker/-in für Gebäudesystemintegration", "Elektroniker für Gebäudesystemintegration", "Elektronikerin für Gebäudesystemintegration", null, null)
		}));

		/** Fachklasse Elektroniker/-in FR Automatisierungs- und Systemtechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_177_11", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(148000, 10, "177", "11", false, null, null, "TN", null, "A4", "Elektroniker/-in FR Automatisierungs- und Systemtechnik", "Elektroniker  FR Automatisierungs- und Systemtechnik", "Elektronikerin  FR Automatisierungs- und Systemtechnik", null, null)
		}));

		/** Fachklasse Emailschriftenmaler/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_178_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(149000, 10, "178", "00", true, "O", "OH", null, null, null, "Emailschriftenmaler/-in", "Emailschriftenmaler", "Emailschriftenmalerin", null, 2012)
		}));

		/** Fachklasse Energieelektroniker/-in - Anlagentechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_179_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(150000, 10, "179", "01", true, "T", "ET", null, null, null, "Energieelektroniker/-in - Anlagentechnik", "Energieelektroniker - Anlagentechnik", "Energieelektronikerin - Anlagentechnik", null, 2014)
		}));

		/** Fachklasse Energieelektroniker/-in - Betriebstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_179_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(151000, 10, "179", "02", true, "T", "ET", null, null, null, "Energieelektroniker/-in - Betriebstechnik", "Energieelektroniker - Betriebstechnik", "Energieelektronikerin - Betriebstechnik", null, 2014)
		}));

		/** Fachklasse Estrichleger/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_180_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(152000, 10, "180", "00", false, "T", "BT", "TN", null, "A3", "Estrichleger/-in", "Estrichleger", "Estrichlegerin", null, null)
		}));

		/** Fachklasse Fachangestellte/-r für Arbeitsförderung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_181_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(153000, 10, "181", "00", true, "W", "WV", null, null, null, "Fachangestellte/-r für Arbeitsförderung", "Fachangestellter für Arbeitsförderung", "Fachangestellte für Arbeitsförderung", null, 2015)
		}));

		/** Fachklasse Fachangestellte/-r für Bäderbetriebe */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_182_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(154000, 10, "182", "00", false, "O", "OH", "GS", null, "A3", "Fachangestellte/-r für Bäderbetriebe", "Fachangestellter für Bäderbetriebe", "Fachangestellte für Bäderbetriebe", null, null)
		}));

		/** Fachklasse Fachangestellte/-r für Medien- und Informationsdienste - Archiv */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_183_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(155000, 10, "183", "01", false, "O", "OH", "WV", null, "A3", "Fachangestellte/-r für Medien- und Informationsdienste - Archiv", "Fachangestellter für Medien- und Informationsdienste - Archiv", "Fachangestellte für Medien- und Informationsdienste - Archiv", null, null)
		}));

		/** Fachklasse Fachangestellte/-r für Medien- und Informationsdienste - Bibliothek */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_183_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(156000, 10, "183", "02", false, "O", "OH", "WV", null, "A3", "Fachangestellte/-r für Medien- und Informationsdienste - Bibliothek", "Fachangestellter für Medien- und Informationsdienste - Bibliothek", "Fachangestellte für Medien- und Informationsdienste - Bibliothek", null, null)
		}));

		/** Fachklasse Fachangestellte/-r für Medien- und Informationsdienste - Bildagentur */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_183_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(157000, 10, "183", "03", false, "O", "OH", "WV", null, "A3", "Fachangestellte/-r für Medien- und Informationsdienste - Bildagentur", "Fachangestellter für Medien- und Informationsdienste - Bildagentur", "Fachangestellte für Medien- und Informationsdienste - Bildagentur", null, null)
		}));

		/** Fachklasse Fachangestellte/-r für Medien- und Informationsdienste - Information und Dokumentation */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_183_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(158000, 10, "183", "04", false, "O", "OH", "WV", null, "A3", "Fachangestellte/-r für Medien- und Informationsdienste - Information und Dokumentation", "Fachangestellter für Medien- und Informationsdienste - Information und Dokumentation", "Fachangestellte für Medien- und Informationsdienste - Information und Dokumentation", null, null)
		}));

		/** Fachklasse Fachangestellte/-r für Medien- und Informationsdienste - Medizinische Dokumentation */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_183_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(159000, 10, "183", "05", false, "O", "OH", "WV", null, "A3", "Fachangestellte/-r für Medien- und Informationsdienste - Medizinische Dokumentation", "Fachangestellter für Medien- und Informationsdienste - Medizinische Dokumentation", "Fachangestellte für Medien- und Informationsdienste - Medizinische Dokumentation", null, null)
		}));

		/** Fachklasse Fachangestellte/-r für Bürokommunikation (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_184_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(160000, 10, "184", "00", true, "W", "WV", null, null, null, "Fachangestellte/-r für Bürokommunikation", "Fachangestellter für Bürokommunikation", "Fachangestellte für Bürokommunikation", null, 2017)
		}));

		/** Fachklasse Fachgehilfe/-gehilfin im Gastgewerbe (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_185_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(161000, 10, "185", "00", true, "W", "WV", null, null, null, "Fachgehilfe/-gehilfin im Gastgewerbe", "Fachgehilfe im Gastgewerbe", "Fachgehilfin im Gastgewerbe", null, 2012)
		}));

		/** Fachklasse Fachgehilfe/-gehilfin in steuer- u. wirtschaftsberatenden Berufen (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_186_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(162000, 10, "186", "00", true, "W", "WV", null, null, null, "Fachgehilfe/-gehilfin in steuer- u. wirtschaftsberatenden Berufen", "Fachgehilfe in steuer- u. wirtschaftsberatenden Berufen", "Fachgehilfin in steuer- u. wirtschaftsberatenden Berufen", null, 2014)
		}));

		/** Fachklasse Fachinformatiker/-in - Anwendungsentwicklung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_187_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(163000, 10, "187", "01", false, "O", "OH", "IF", null, "A3", "Fachinformatiker/-in - Anwendungsentwicklung", "Fachinformatiker - Anwendungsentwicklung", "Fachinformatikerin - Anwendungsentwicklung", null, null)
		}));

		/** Fachklasse Fachinformatiker/-in - Systemintegration */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_187_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(164000, 10, "187", "02", false, "O", "OH", "IF", null, "A3", "Fachinformatiker/-in - Systemintegration", "Fachinformatiker - Systemintegration", "Fachinformatikerin - Systemintegration", null, null)
		}));

		/** Fachklasse Fachinformatiker/-in - Daten- und Prozessanalyse */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_187_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(165000, 10, "187", "03", false, "O", "OH", "IF", null, "A3", "Fachinformatiker/-in - Daten- und Prozessanalyse", "Fachinformatiker - Daten- und Prozessanalyse", "Fachinformatikerin - Daten- und Prozessanalyse", null, null)
		}));

		/** Fachklasse Fachinformatiker/-in - Digitale Vernetzung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_187_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(166000, 10, "187", "04", false, "O", "OH", "IF", null, "A3", "Fachinformatiker/-in - Digitale Vernetzung", "Fachinformatiker - Digitale Vernetzung", "Fachinformatikerin - Digitale Vernetzung", null, null)
		}));

		/** Fachklasse Fachkraft für Brief- u. Frachtverkehr (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_188_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(167000, 10, "188", "00", true, "W", "WV", null, null, null, "Fachkraft für Brief- u. Frachtverkehr", "Fachkraft für Brief- u. Frachtverkehr", "Fachkraft für Brief- u. Frachtverkehr", null, 2014)
		}));

		/** Fachklasse Fachkraft für Fruchtsafttechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_189_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(168000, 10, "189", "00", false, "O", "OH", "OZ", null, "A3", "Fachkraft für Fruchtsafttechnik", "Fachkraft für Fruchtsafttechnik", "Fachkraft für Fruchtsafttechnik", null, null)
		}));

		/** Fachklasse Fachkraft für Lebensmitteltechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_190_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(169000, 10, "190", "00", false, "O", "OH", "EH", null, "A3", "Fachkraft für Lebensmitteltechnik", "Fachkraft für Lebensmitteltechnik", "Fachkraft für Lebensmitteltechnik", null, null)
		}));

		/** Fachklasse Fachkraft für Süßwarentechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_191_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(170000, 10, "191", "00", true, "O", "OH", null, null, null, "Fachkraft für Süßwarentechnik", "Fachkraft für Süßwarentechnik", "Fachkraft für Süßwarentechnik", null, 2005)
		}));

		/** Fachklasse Fachkraft für Süßwarentechnik - Konfekt (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_191_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(171000, 10, "191", "01", true, "O", "OH", null, null, null, "Fachkraft für Süßwarentechnik - Konfekt", "Fachkraft für Süßwarentechnik - Konfekt", "Fachkraft für Süßwarentechnik - Konfekt", null, 2017)
		}));

		/** Fachklasse Fachkraft für Süßwarentechnik - Schokolade (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_191_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(172000, 10, "191", "02", true, "O", "OH", null, null, null, "Fachkraft für Süßwarentechnik - Schokolade", "Fachkraft für Süßwarentechnik - Schokolade", "Fachkraft für Süßwarentechnik - Schokolade", null, 2017)
		}));

		/** Fachklasse Fachkraft für Süßwarentechnik - Zuckerwaren (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_191_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(173000, 10, "191", "03", true, "O", "OH", null, null, null, "Fachkraft für Süßwarentechnik - Zuckerwaren", "Fachkraft für Süßwarentechnik - Zuckerwaren", "Fachkraft für Süßwarentechnik - Zuckerwaren", null, 2017)
		}));

		/** Fachklasse Fachkraft für Süßwarentechnik - Dauerbackwaren (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_191_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(174000, 10, "191", "04", true, "O", "OH", null, null, null, "Fachkraft für Süßwarentechnik - Dauerbackwaren", "Fachkraft für Süßwarentechnik - Dauerbackwaren", "Fachkraft für Süßwarentechnik - Dauerbackwaren", null, 2017)
		}));

		/** Fachklasse Fachkraft für Lagerwirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_192_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(175000, 10, "192", "00", true, "O", "OH", null, null, null, "Fachkraft für Lagerwirtschaft", "Fachkraft für Lagerwirtschaft", "Fachkraft für Lagerwirtschaft", null, 2014)
		}));

		/** Fachklasse Fachmann/-frau für Systemgastronomie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_193_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(176000, 10, "193", "00", false, "E", "EH", "EH", null, "A3", "Fachmann/-frau für Systemgastronomie", "Fachmann für Systemgastronomie", "Fachfrau für Systemgastronomie", null, null)
		}));

		/** Fachklasse Fachkraft für Veranstaltungstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_194_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(177000, 10, "194", "00", false, "O", "OH", "TN", null, "A3", "Fachkraft für Veranstaltungstechnik", "Fachkraft für Veranstaltungstechnik", "Fachkraft für Veranstaltungstechnik", null, null)
		}));

		/** Fachklasse Fachkraft im Gastgewerbe */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_195_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(178000, 10, "195", "00", false, "E", "EH", "EH", null, "A2", "Fachkraft im Gastgewerbe", "Fachkraft im Gastgewerbe", "Fachkraft im Gastgewerbe", null, null)
		}));

		/** Fachklasse Fachmann/-frau für Luftfahrtservice (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_196_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(179000, 10, "196", "00", true, "W", "WV", null, null, null, "Fachmann/-frau für Luftfahrtservice", "Fachmann für Luftfahrtservice", "Fachfrau für Luftfahrtservice", null, 2012)
		}));

		/** Fachklasse Fachverkäufer/-in im Nahrungsmittelhandwerk (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_197_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(180000, 10, "197", "00", true, "E", "EH", null, null, null, "Fachverkäufer/-in im Nahrungsmittelhandwerk", "Fachverkäufer im Nahrungsmittelhandwerk", "Fachverkäuferin im Nahrungsmittelhandwerk", null, 2014)
		}));

		/** Fachklasse Fahrzeugpolsterer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_198_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(181000, 10, "198", "00", true, "G", "FR", null, null, null, "Fahrzeugpolsterer/-in", "Fahrzeugpolsterer", "Fahrzeugpolsterin", null, 2014)
		}));

		/** Fachklasse Fahrzeugstellmacher/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_199_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(182000, 10, "199", "00", true, "T", "HT", null, null, null, "Fahrzeugstellmacher/-in", "Fahrzeugstellmacher", "Fahrzeugstellmacherin", null, 2014)
		}));

		/** Fachklasse Fassadenmonteur/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_200_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(183000, 10, "200", "00", false, "T", "BT", "TN", null, "A3", "Fassadenmonteur/-in", "Fassadenmonteur", "Fassadenmonteurin", null, null)
		}));

		/** Fachklasse Federmacher/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_201_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(184000, 10, "201", "00", true, "T", "MT", null, null, null, "Federmacher/-in", "Federmacher", "Federmacherin", null, 2015)
		}));

		/** Fachklasse Feinmechaniker/-in - Feingerätebau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_202_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(185000, 10, "202", "01", true, "T", "MT", null, null, null, "Feinmechaniker/-in - Feingerätebau", "Feinmechaniker - Feingerätebau", "Feinmechanikerin - Feingerätebau", null, 2012)
		}));

		/** Fachklasse Feinmechaniker/-in - Nähmaschineninstandhaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_202_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(186000, 10, "202", "02", true, "T", "MT", null, null, null, "Feinmechaniker/-in - Nähmaschineninstandhaltung", "Feinmechaniker - Nähmaschineninstandhaltung", "Feinmechanikerin - Nähmaschineninstandhaltung", null, 2012)
		}));

		/** Fachklasse Feinoptiker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_203_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(187000, 10, "203", "00", false, "T", "MT", "TN", null, "A4", "Feinoptiker/-in", "Feinoptiker", "Feinoptikerin", null, null)
		}));

		/** Fachklasse Feinsattler/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_204_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(188000, 10, "204", "00", true, "O", "OH", null, null, null, "Feinsattler/-in", "Feinsattler", "Feinsattlerin", null, 2014)
		}));

		/** Fachklasse Feintäschner/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_205_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(189000, 10, "205", "00", true, "O", "OH", null, null, null, "Feintäschner/-in", "Feintäschner", "Feintäschnerin", null, 2014)
		}));

		/** Fachklasse Fernmeldeanlagenelektroniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_206_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(190000, 10, "206", "00", true, "T", "ET", null, null, null, "Fernmeldeanlagenelektroniker/-in", "Fernmeldeanlagenelektroniker", "Fernmeldeanlagenelektronikerin", null, 2014)
		}));

		/** Fachklasse Fertigungsmechaniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_207_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(191000, 10, "207", "00", false, "T", "MT", "TN", null, "A3", "Fertigungsmechaniker/-in", "Fertigungsmechaniker", "Fertigungsmechanikerin", null, null)
		}));

		/** Fachklasse Feuerungs- und Schornsteinbauer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_208_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(192000, 10, "208", "00", false, "T", "BT", "TN", null, "A3", "Feuerungs- und Schornsteinbauer/-in", "Feuerungs- und Schornsteinbauer", "Feuerungs- und Schornsteinbauerin", null, null)
		}));

		/** Fachklasse Figurenkeramformer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_209_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(193000, 10, "209", "00", false, "O", "OH", "OZ", null, "A3", "Figurenkeramformer/-in", "Figurenkeramformer", "Figurenkeramformerin", null, null)
		}));

		/** Fachklasse Film- und Videolaborant/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_210_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(194000, 10, "210", "00", true, "O", "OH", "OZ", null, "A3", "Film- und Videolaborant/-in", "Film- und Videolaborant", "Film- und Videolaborantin", null, 2019)
		}));

		/** Fachklasse Film- und Videoeditor/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_211_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(195000, 10, "211", "00", false, "O", "OH", "OZ", null, "A3", "Film- und Videoeditor/-in", "Film- und Videoeditor", "Film- und Videoeditorin", null, null)
		}));

		/** Fachklasse Fischwirt/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_212_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(196000, 10, "212", "00", true, "A", "AW", "OZ", null, "A3", "Fischwirt/-in", "Fischwirt", "Fischwirtin", null, 2020)
		}));

		/** Fachklasse Fischwirt/-in - Aquakultur und Binnenfischerei */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_212_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(197000, 10, "212", "01", false, "A", "AW", "OZ", null, "A3", "Fischwirt/-in - Aquakultur und Binnenfischerei", "Fischwirt - Aquakultur und Binnenfischerei", "Fischwirtin - Aquakultur und Binnenfischerei", null, null)
		}));

		/** Fachklasse Fischwirt/-in - Küstenfischerei und Kleine Hochseefischerei */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_212_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(198000, 10, "212", "02", false, "A", "AW", "OZ", null, "A3", "Fischwirt/-in - Küstenfischerei und Kleine Hochseefischerei", "Fischwirt - Küstenfischerei und Kleine Hochseefischerei", "Fischwirtin - Küstenfischerei und Kleine Hochseefischerei", null, null)
		}));

		/** Fachklasse Flachglasmechaniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_213_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(199000, 10, "213", "00", true, "O", "OH", "TN", null, "A3", "Flachglasmechaniker/-in", "Flachglasmechaniker", "Flachglasmechanikerin", null, 2021)
		}));

		/** Fachklasse Fleischer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_214_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(200000, 10, "214", "00", false, "E", "EH", "EH", null, "A3", "Fleischer/-in", "Fleischer", "Fleischerin", null, null)
		}));

		/** Fachklasse Fleischer/-in - Schlachten (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_214_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(201000, 10, "214", "01", true, "E", "EH", null, null, null, "Fleischer/-in - Schlachten", "Fleischer - Schlachten", "Fleischerin - Schlachten", null, 2012)
		}));

		/** Fachklasse Fleischer/-in - Herstellen von Feinkost und Konserven (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_214_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(202000, 10, "214", "02", true, "E", "EH", null, null, null, "Fleischer/-in - Herstellen von Feinkost und Konserven", "Fleischer - Herstellen von Feinkost und Konserven", "Fleischerin - Herstellen von Feinkost und Konserven", null, 2012)
		}));

		/** Fachklasse Fleischer/-in - Verkauf (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_214_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(203000, 10, "214", "03", true, "E", "EH", null, null, null, "Fleischer/-in - Verkauf", "Fleischer - Verkauf", "Fleischerin - Verkauf", null, 2012)
		}));

		/** Fachklasse Flexograf/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_215_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(204000, 10, "215", "00", true, "T", "DT", null, null, null, "Flexograf/-in", "Flexograf", "Flexografin", null, 2014)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 10 (Teil 3)
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex10_Teil3(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Fliesen-, Platten-, Mosaikleger/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_216_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(205000, 10, "216", "00", false, "T", "BT", "TN", null, "A3", "Fliesen-, Platten-, Mosaikleger/-in", "Fliesen-, Platten-, Mosaikleger", "Fliesen-, Platten-, Mosaiklegerin", null, null)
		}));

		/** Fachklasse Florist/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_217_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(206000, 10, "217", "00", false, "O", "OH", "GT", null, "A3", "Florist/-in", "Florist", "Floristin", null, null)
		}));

		/** Fachklasse Fluggerätebauer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_218_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(207000, 10, "218", "00", true, "T", "MT", null, null, null, "Fluggerätebauer/-in", "Fluggerätebauer", "Fluggerätebauerin", null, 2014)
		}));

		/** Fachklasse Fluggeräteelektroniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_219_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(208000, 10, "219", "00", false, "T", "MT", "TN", null, "A4", "Fluggeräteelektroniker/-in", "Fluggeräteelektroniker", "Fluggeräteelektronikerin", null, null)
		}));

		/** Fachklasse Fluggerätmechaniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_220_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(209000, 10, "220", "00", true, "T", "MT", null, null, null, "Fluggerätmechaniker/-in", "Fluggerätmechaniker", "Fluggerätmechanikerin", null, 2012)
		}));

		/** Fachklasse Fluggerätmechaniker/-in - Fertigungstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_220_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(210000, 10, "220", "01", false, "T", "MT", "TN", null, "A4", "Fluggerätmechaniker/-in - Fertigungstechnik", "Fluggerätmechaniker - Fertigungstechnik", "Fluggerätmechanikerin- Fertigungstechnik", null, null)
		}));

		/** Fachklasse Fluggerätmechaniker/-in - Instandhaltungstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_220_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(211000, 10, "220", "02", false, "T", "MT", "TN", null, "A4", "Fluggerätmechaniker/-in - Instandhaltungstechnik", "Fluggerätmechaniker - Instandhaltungstechnik", "Fluggerätmechanikerin - Instandhaltungstechnik", null, null)
		}));

		/** Fachklasse Fluggerätmechaniker/-in - Triebwerkstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_220_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(212000, 10, "220", "03", false, "T", "MT", "TN", null, "A4", "Fluggerätmechaniker/-in - Triebwerkstechnik", "Fluggerätmechaniker - Triebwerkstechnik", "Fluggerätmechanikerin - Triebwerkstechnik", null, null)
		}));

		/** Fachklasse Formstecher/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_221_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(213000, 10, "221", "00", true, "T", "MT", null, null, null, "Formstecher/-in", "Formstecher", "Formstecherin", null, 2014)
		}));

		/** Fachklasse Forstwirt/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_222_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(214000, 10, "222", "00", false, "A", "AW", "AW", null, "A3", "Forstwirt/-in", "Forstwirt", "Forstwirtin", null, null)
		}));

		/** Fachklasse Fotograf/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_223_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(215000, 10, "223", "00", false, "O", "OH", "GT", null, "A3", "Fotograf/-in", "Fotograf", "Fotografin", null, null)
		}));

		/** Fachklasse Fotogravurzeichner/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_224_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(216000, 10, "224", "00", true, "T", "DT", null, null, null, "Fotogravurzeichner/-in", "Fotogravurzeichner", "Fotogravurzeichnerin", null, 2014)
		}));

		/** Fachklasse Fotolaborant/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_225_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(217000, 10, "225", "00", true, "O", "OH", null, null, null, "Fotolaborant/-in", "Fotolaborant", "Fotolaborantin", null, 2015)
		}));

		/** Fachklasse Fotomedienlaborant/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_226_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(218000, 10, "226", "00", true, "O", "OH", null, null, null, "Fotomedienlaborant/-in", "Fotomedienlaborant", "Fotomedienlaborantin", null, 2016)
		}));

		/** Fachklasse Fräser/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_227_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(219000, 10, "227", "00", true, "T", "MT", null, null, null, "Fräser/-in", "Fräser", "Fräserin", null, 2015)
		}));

		/** Fachklasse Friseur/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_228_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(220000, 10, "228", "00", false, "S", "KP", "GS", null, "A3", "Friseur/-in", "Friseur", "Friseurin", null, null)
		}));

		/** Fachklasse Galvaniseur/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_229_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(221000, 10, "229", "00", true, "T", "CP", null, null, null, "Galvaniseur/-in", "Galvaniseur", "Galvaniseurin", null, 2014)
		}));

		/** Fachklasse Galvaniseur/-in u. Metallschleifer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_230_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(222000, 10, "230", "00", true, "T", "CP", null, null, null, "Galvaniseur/-in u. Metallschleifer/-in", "Galvaniseur u. Metallschleifer", "Galvaniseurin u. Metallschleiferin", null, 2014)
		}));

		/** Fachklasse Galvanoplastiker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_231_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(223000, 10, "231", "00", true, "T", "CP", null, null, null, "Galvanoplastiker/-in", "Galvanoplastiker", "Galvanoplastikerin", null, 2014)
		}));

		/** Fachklasse Gärtner/-in - Garten- und Landschaftsbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_232_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(224000, 10, "232", "01", false, "A", "AW", "AW", null, "A3", "Gärtner/-in - Garten- und Landschaftsbau", "Gärtner - Garten- und Landschaftsbau", "Gärtnerin - Garten- und Landschaftsbau", null, null)
		}));

		/** Fachklasse Gärtner/-in - übrige Fachrichtungen (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_232_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(225000, 10, "232", "02", true, "A", "AW", null, null, null, "Gärtner/-in - übrige Fachrichtungen", "Gärtner - übrige Fachrichtungen", "Gärtnerin - übrige Fachrichtungen", null, 2012)
		}));

		/** Fachklasse Gärtner/-in - Baumschule */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_232_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(226000, 10, "232", "03", false, "A", "AW", "AW", null, "A3", "Gärtner/-in - Baumschule", "Gärtner - Baumschule", "Gärtnerin - Baumschule", null, null)
		}));

		/** Fachklasse Gärtner/-in - Friedhofsgärtnerei */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_232_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(227000, 10, "232", "04", false, "A", "AW", "AW", null, "A3", "Gärtner/-in - Friedhofsgärtnerei", "Gärtner - Friedhofsgärtnerei", "Gärtnerin - Friedhofsgärtnerei", null, null)
		}));

		/** Fachklasse Gärtner/-in - Gemüsebau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_232_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(228000, 10, "232", "05", false, "A", "AW", "AW", null, "A3", "Gärtner/-in - Gemüsebau", "Gärtner - Gemüsebau", "Gärtnerin - Gemüsebau", null, null)
		}));

		/** Fachklasse Gärtner/-in - Obstbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_232_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(229000, 10, "232", "06", false, "A", "AW", "AW", null, "A3", "Gärtner/-in - Obstbau", "Gärtner - Obstbau", "Gärtnerin - Obstbau", null, null)
		}));

		/** Fachklasse Gärtner/-in - Staudengärtnerei */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_232_07", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(230000, 10, "232", "07", false, "A", "AW", "AW", null, "A3", "Gärtner/-in - Staudengärtnerei", "Gärtner - Staudengärtnerei", "Gärtnerin - Staudengärtnerei", null, null)
		}));

		/** Fachklasse Gärtner/-in - Zierpflanzenbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_232_08", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(231000, 10, "232", "08", false, "A", "AW", "AW", null, "A3", "Gärtner/-in - Zierpflanzenbau", "Gärtner - Zierpflanzenbau", "Gärtnerin - Zierpflanzenbau", null, null)
		}));

		/** Fachklasse Gas- u. Wasserinstallateur/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_233_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(232000, 10, "233", "00", true, "T", "MT", null, null, null, "Gas- u. Wasserinstallateur/-in", "Gas- u. Wasserinstallateur", "Gas- u. Wasserinstallateurin", null, 2014)
		}));

		/** Fachklasse Gebäudereiniger/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_234_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(233000, 10, "234", "00", false, "O", "OH", "TN", null, "A3", "Gebäudereiniger/-in", "Gebäudereiniger", "Gebäudereinigerin", null, null)
		}));

		/** Fachklasse Geigenbauer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_235_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(234000, 10, "235", "00", false, "O", "OH", "OZ", null, "A3", "Geigenbauer/-in", "Geigenbauer", "Geigenbauerin", null, null)
		}));

		/** Fachklasse Gerätezusammensetzer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_236_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(235000, 10, "236", "00", true, "O", "OH", null, null, null, "Gerätezusammensetzer/-in", "Gerätezusammensetzer", "Gerätezusammensetzerin", null, 2015)
		}));

		/** Fachklasse Gerüstbauer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_237_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(236000, 10, "237", "00", false, "O", "OH", "TN", null, "A3", "Gerüstbauer/-in", "Gerüstbauer", "Gerüstbauerin", null, null)
		}));

		/** Fachklasse Gießereimechaniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_238_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(237000, 10, "238", "00", false, null, null, "TN", null, "A4", "Gießereimechaniker/-in", "Gießereimechaniker", "Gießereimechanikerin", null, null)
		}));

		/** Fachklasse Gießereimechaniker/-in - Druck- und Kokillenguss (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_238_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(238000, 10, "238", "01", true, "T", "MT", "TN", null, "A4", "Gießereimechaniker/-in - Druck- und Kokillenguss", "Gießereimechaniker - Druck- und Kokillenguss", "Gießereimechanikerin - Druck- und Kokillenguss", null, 2020)
		}));

		/** Fachklasse Gießereimechaniker/-in - Handformguss (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_238_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(239000, 10, "238", "02", true, "T", "MT", "TN", null, "A3", "Gießereimechaniker/-in - Handformguss", "Gießereimechaniker - Handformguss", "Gießereimechanikerin - Handformguss", null, 2020)
		}));

		/** Fachklasse Gießereimechaniker/-in - Maschinenformguss (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_238_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(240000, 10, "238", "03", true, "T", "MT", "TN", null, "A3", "Gießereimechaniker/-in - Maschinenformguss", "Gießereimechaniker - Maschinenformguss", "Gießereimechanikerin - Maschinenformguss", null, 2020)
		}));

		/** Fachklasse Gipsformengießer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_239_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(241000, 10, "239", "00", true, "G", "FR", null, null, null, "Gipsformengießer/-in", "Gipsformengießer", "Gipsformengießerin", null, 2012)
		}));

		/** Fachklasse Glasapparatebauer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_240_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(242000, 10, "240", "00", false, "O", "OH", "TN", null, "A3", "Glasapparatebauer/-in", "Glasapparatebauer", "Glasapparatebauerin", null, null)
		}));

		/** Fachklasse Glasbläser/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_241_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(243000, 10, "241", "00", true, "O", "OH", null, null, null, "Glasbläser/-in", "Glasbläser", "Glasbläserin", null, 2012)
		}));

		/** Fachklasse Glasbläser/-in - Kunstaugen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_241_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(244000, 10, "241", "01", false, "O", "OH", "OZ", null, "A3", "Glasbläser/-in - Kunstaugen", "Glasbläser - Kunstaugen", "Glasbläserin - Kunstaugen", null, null)
		}));

		/** Fachklasse Glasbläser/-in - Glasgestaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_241_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(245000, 10, "241", "02", false, "O", "OH", "OZ", null, "A3", "Glasbläser/-in - Glasgestaltung", "Glasbläser - Glasgestaltung", "Glasbläserin - Glasgestaltung", null, null)
		}));

		/** Fachklasse Glasbläser/-in - Christbaumschmuck */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_241_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(246000, 10, "241", "03", false, "O", "OH", "OZ", null, "A3", "Glasbläser/-in - Christbaumschmuck", "Glasbläser - Christbaumschmuck", "Glasbläserin - Christbaumschmuck", null, null)
		}));

		/** Fachklasse Glaser/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_242_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(247000, 10, "242", "00", true, "O", "OH", null, null, null, "Glaser/-in", "Glaser", "Glaserin", null, 2012)
		}));

		/** Fachklasse Glaser/-in - Fenster- und Glasfassadenbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_242_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(248000, 10, "242", "01", false, "O", "OH", "TN", null, "A3", "Glaser/-in - Fenster- und Glasfassadenbau", "Glaser - Fenster- und Glasfassadenbau", "Glaserin - Fenster- und Glasfassadenbau", null, null)
		}));

		/** Fachklasse Glaser/-in - Verglasung und Glasbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_242_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(249000, 10, "242", "02", false, "O", "OH", "TN", null, "A3", "Glaser/-in - Verglasung und Glasbau", "Glaser - Verglasung und Glasbau", "Glaserin - Verglasung und Glasbau", null, null)
		}));

		/** Fachklasse Glasgraveur/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_243_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(250000, 10, "243", "00", true, "G", "FR", null, null, null, "Glasgraveur/-in", "Glasgraveur", "Glasgraveurin", null, 2012)
		}));

		/** Fachklasse Glasmacher/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_244_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(251000, 10, "244", "00", false, "O", "OH", "TN", null, "A3", "Glasmacher/-in", "Glasmacher", "Glasmacherin", null, null)
		}));

		/** Fachklasse Glasschleifer/-in u. Glasätzer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_245_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(252000, 10, "245", "00", true, "G", "FR", null, null, null, "Glasschleifer/-in u. Glasätzer/-in", "Glasschleifer u. Glasätzer", "Glasschleiferin u. Glasätzerin", null, 2012)
		}));

		/** Fachklasse Glas- u. Kerammaler/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_246_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(253000, 10, "246", "00", true, "O", "OH", null, null, null, "Glas- u. Kerammaler/-in", "Glas- u. Kerammaler", "Glas- u. Kerammalerin", null, 2014)
		}));

		/** Fachklasse Glas- u. Kerammaler/-in - Glasmalerei (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_246_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(254000, 10, "246", "01", true, "O", "OH", null, null, null, "Glas- u. Kerammaler/-in - Glasmalerei", "Glas- u. Kerammaler - Glasmalerei", "Glas- u. Kerammalerin - Glasmalerei", null, 2014)
		}));

		/** Fachklasse Glas- u. Kerammaler/-in - Kerammalerei (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_246_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(255000, 10, "246", "02", true, "O", "OH", null, null, null, "Glas- u. Kerammaler/-in - Kerammalerei", "Glas- u. Kerammaler - Kerammalerei", "Glas- u. Kerammalerin - Kerammalerei", null, 2014)
		}));

		/** Fachklasse Glas- und Porzellanmaler/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_247_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(256000, 10, "247", "00", false, "O", "OH", "GT", null, "A3", "Glas- und Porzellanmaler/-in", "Glas- und Porzellanmaler", "Glas- und Porzellanmalerin", null, null)
		}));

		/** Fachklasse Glasveredler/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_248_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(257000, 10, "248", "00", true, "O", "OH", null, null, null, "Glasveredler/-in", "Glasveredler", "Glasveredlerin", null, 2012)
		}));

		/** Fachklasse Glasveredler/-in - Schliff und Gravur */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_248_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(258000, 10, "248", "01", false, "O", "OH", "TN", null, "A3", "Glasveredler/-in - Schliff und Gravur", "Glasveredler - Schliff und Gravur", "Glasveredlerin - Schliff und Gravur", null, null)
		}));

		/** Fachklasse Glasveredler/-in - Glasmalerei und Kunstverglasung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_248_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(259000, 10, "248", "02", false, "O", "OH", "TN", null, "A3", "Glasveredler/-in - Glasmalerei und Kunstverglasung", "Glasveredler - Glasmalerei und Kunstverglasung", "Glasveredlerin - Glasmalerei und Kunstverglasung", null, null)
		}));

		/** Fachklasse Glasveredler/-in - Kanten- und Flächenveredlung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_248_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(260000, 10, "248", "03", false, "O", "OH", "TN", null, "A3", "Glasveredler/-in - Kanten- und Flächenveredlung", "Glasveredler - Kanten- und Flächenveredlung", "Glasveredlerin - Kanten- und Flächenveredlung", null, null)
		}));

		/** Fachklasse Gleisbauer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_249_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(261000, 10, "249", "00", false, "T", "BT", "TN", null, "A3", "Gleisbauer/-in", "Gleisbauer", "Gleisbauerin", null, null)
		}));

		/** Fachklasse Goldschmied/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_250_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(262000, 10, "250", "00", true, "O", "OH", null, null, null, "Goldschmied/-in", "Goldschmied", "Goldschmiedin", null, 2012)
		}));

		/** Fachklasse Goldschmied/-in - Schmuck */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_250_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(263000, 10, "250", "01", false, "O", "OH", "GT", null, "A4", "Goldschmied/-in - Schmuck", "Goldschmied - Schmuck", "Goldschmiedin - Schmuck", null, null)
		}));

		/** Fachklasse Goldschmied/-in - Juwelen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_250_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(264000, 10, "250", "02", false, "O", "OH", "GT", null, "A4", "Goldschmied/-in - Juwelen", "Goldschmied - Juwelen", "Goldschmiedin - Juwelen", null, null)
		}));

		/** Fachklasse Goldschmied/-in - Ketten */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_250_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(265000, 10, "250", "03", false, "O", "OH", "GT", null, "A4", "Goldschmied/-in - Ketten", "Goldschmied - Ketten", "Goldschmiedin - Ketten", null, null)
		}));

		/** Fachklasse Graveur/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_251_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(266000, 10, "251", "00", false, "O", "OH", "GT", null, "A3", "Graveur/-in", "Graveur", "Graveurin", null, null)
		}));

		/** Fachklasse Gürtler/-in und Metalldrücker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_252_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(267000, 10, "252", "00", true, "T", "MT", null, null, null, "Gürtler/-in und Metalldrücker/-in", "Gürtler und Metalldrücker", "Gürtlerin und Metalldrückerin", null, 2014)
		}));

		/** Fachklasse Handelsassistent/-in im Einzelhandel (nur ZQ) (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_253_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(268000, 10, "253", "00", true, "W", "WV", null, null, null, "Handelsassistent/-in im Einzelhandel (nur ZQ)", "Handelsassistent im Einzelhandel (nur ZQ)", "Handelsassistentin im Einzelhandel (nur ZQ)", null, 2012)
		}));

		/** Fachklasse Handelsfachpacker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_254_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(269000, 10, "254", "00", true, "O", "OH", null, null, null, "Handelsfachpacker/-in", "Handelsfachpacker", "Handelsfachpackerin", null, 2014)
		}));

		/** Fachklasse Handschuhmacher/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_255_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(270000, 10, "255", "00", true, "T", "TB", null, null, null, "Handschuhmacher/-in", "Handschuhmacher", "Handschuhmacherin", null, 2012)
		}));

		/** Fachklasse Handzuginstrumentenmacher/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_256_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(271000, 10, "256", "00", false, "O", "OH", "OZ", null, "A3", "Handzuginstrumentenmacher/-in", "Handzuginstrumentenmacher", "Handzuginstrumentenmacherin", null, null)
		}));

		/** Fachklasse Hauswirtschafter/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_257_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(272000, 10, "257", "00", false, "E", "EH", "EH", null, "A3", "Hauswirtschafter/-in", "Hauswirtschafter", "Hauswirtschafterin", null, null)
		}));

		/** Fachklasse Hauswirtschafter/-in - ländlicher Bereich (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_257_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(273000, 10, "257", "01", true, "E", "EH", null, null, null, "Hauswirtschafter/-in - ländlicher Bereich", "Hauswirtschafter - ländlicher Bereich", "Hauswirtschafterin - ländlicher Bereich", null, 2019)
		}));

		/** Fachklasse Hauswirtschafter/-in - städtischer Bereich (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_257_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(274000, 10, "257", "02", true, "E", "EH", null, null, null, "Hauswirtschafter/-in - städtischer Bereich", "Hauswirtschafter - städtischer Bereich", "Hauswirtschafterin - städtischer Bereich", null, 2012)
		}));

		/** Fachklasse Herrenschneider/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_258_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(275000, 10, "258", "00", true, "T", "TB", null, null, null, "Herrenschneider/-in", "Herrenschneider", "Herrenschneiderin", null, 2014)
		}));

		/** Fachklasse Hobler/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_259_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(276000, 10, "259", "00", true, "T", "MT", null, null, null, "Hobler/-in", "Hobler", "Hoblerin", null, 2012)
		}));

		/** Fachklasse Hochbaufacharbeiter/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_260_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(277000, 10, "260", "00", true, "T", "BT", null, null, null, "Hochbaufacharbeiter/-in", "Hochbaufacharbeiter", "Hochbaufacharbeiterin", null, 2015)
		}));

		/** Fachklasse Hochbaufacharbeiter/-in - Beton- und Stahlbetonarbeiten */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_260_50", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(278000, 10, "260", "50", false, "T", "BT", "TN", null, "A2", "Hochbaufacharbeiter/-in - Beton- und Stahlbetonarbeiten", "Hochbaufacharbeiter - Beton- und Stahlbetonarbeiten", "Hochbaufacharbeiterin - Beton- und Stahlbetonarbeiten", null, null)
		}));

		/** Fachklasse Hochbaufacharbeiter/-in - Feuerung und Schornsteinarbeiten */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_260_51", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(279000, 10, "260", "51", false, "T", "BT", "TN", null, "A2", "Hochbaufacharbeiter/-in - Feuerung und Schornsteinarbeiten", "Hochbaufacharbeiter - Feuerung und Schornsteinarbeiten", "Hochbaufacharbeiterin - Feuerung und Schornsteinarbeiten", null, null)
		}));

		/** Fachklasse Hochbaufacharbeiter/-in - Maurerarbeiten */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_260_52", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(280000, 10, "260", "52", false, "T", "BT", "TN", null, "A2", "Hochbaufacharbeiter/-in - Maurerarbeiten", "Hochbaufacharbeiter - Maurerarbeiten", "Hochbaufacharbeiterin - Maurerarbeiten", null, null)
		}));

		/** Fachklasse Hohlglasfeinschleifer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_261_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(281000, 10, "261", "00", true, "G", "FR", null, null, null, "Hohlglasfeinschleifer/-in", "Hohlglasfeinschleifer", "Hohlglasfeinschleiferin", null, 2012)
		}));

		/** Fachklasse Holzbearbeitungsmechaniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_262_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(282000, 10, "262", "00", false, "O", "OH", "OZ", null, "A3", "Holzbearbeitungsmechaniker/-in", "Holzbearbeitungsmechaniker", "Holzbearbeitungsmechanikerin", null, null)
		}));

		/** Fachklasse Holzbearbeitungsmechaniker/-in - Sägeindustrie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_262_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(283000, 10, "262", "01", true, "O", "OH", null, null, null, "Holzbearbeitungsmechaniker/-in - Sägeindustrie", "Holzbearbeitungsmechaniker - Sägeindustrie", "Holzbearbeitungsmechanikerin - Sägeindustrie", null, 2012)
		}));

		/** Fachklasse Holzbearbeitungsmechaniker/-in - Hobelindustrie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_262_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(284000, 10, "262", "02", true, "O", "OH", null, null, null, "Holzbearbeitungsmechaniker/-in - Hobelindustrie", "Holzbearbeitungsmechaniker - Hobelindustrie", "Holzbearbeitungsmechanikerin - Hobelindustrie", null, 2012)
		}));

		/** Fachklasse Holzbearbeitungsmechaniker/-in - Holzwerkstoffindustrie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_262_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(285000, 10, "262", "03", true, "O", "OH", null, null, null, "Holzbearbeitungsmechaniker/-in - Holzwerkstoffindustrie", "Holzbearbeitungsmechaniker - Holzwerkstoffindustrie", "Holzbearbeitungsmechanikerin - Holzwerkstoffindustrie", null, 2012)
		}));

		/** Fachklasse Holzbearbeitungsmechaniker/-in - Holzleimbauindustrie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_262_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(286000, 10, "262", "04", true, "O", "OH", null, null, null, "Holzbearbeitungsmechaniker/-in - Holzleimbauindustrie", "Holzbearbeitungsmechaniker - Holzleimbauindustrie", "Holzbearbeitungsmechanikerin - Holzleimbauindustrie", null, 2012)
		}));

		/** Fachklasse Holzbildhauer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_263_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(287000, 10, "263", "00", false, "O", "OH", "OZ", null, "A3", "Holzbildhauer/-in", "Holzbildhauer", "Holzbildhauerin", null, null)
		}));

		/** Fachklasse Holzblasinstrumentenmacher/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_264_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(288000, 10, "264", "00", false, "O", "OH", "OZ", null, "A3", "Holzblasinstrumentenmacher/-in", "Holzblasinstrumentenmacher", "Holzblasinstrumentenmacherin", null, null)
		}));

		/** Fachklasse Holzmechaniker/-in - Industrien des Innenausbaus und des Ladenbaus (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_265_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(289000, 10, "265", "01", true, "T", "HT", null, null, null, "Holzmechaniker/-in - Industrien des Innenausbaus und des Ladenbaus", "Holzmechaniker - Industrien des Innenausbaus und des Ladenbaus", "Holzmechanikerin - Industrien des Innenausbaus und des Ladenbaus", null, 2013)
		}));

		/** Fachklasse Holzmechaniker/-in - Holzpackmittel und Palettenindustrie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_265_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(290000, 10, "265", "02", true, "T", "HT", null, null, null, "Holzmechaniker/-in - Holzpackmittel und Palettenindustrie", "Holzmechaniker - Holzpackmittel und Palettenindustrie", "Holzmechanikerin - Holzpackmittel und Palettenindustrie", null, 2013)
		}));

		/** Fachklasse Holzmechaniker/-in - Leisten- und Rahmenindustrie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_265_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(291000, 10, "265", "03", true, "T", "HT", null, null, null, "Holzmechaniker/-in - Leisten- und Rahmenindustrie", "Holzmechaniker - Leisten- und Rahmenindustrie", "Holzmechanikerin - Leisten- und Rahmenindustrie", null, 2013)
		}));

		/** Fachklasse Holzmechaniker/-in - Möbel- und Gehäuseindustrie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_265_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(292000, 10, "265", "04", true, "T", "HT", null, null, null, "Holzmechaniker/-in - Möbel- und Gehäuseindustrie", "Holzmechaniker - Möbel- und Gehäuseindustrie", "Holzmechanikerin - Möbel- und Gehäuseindustrie", null, 2013)
		}));

		/** Fachklasse Holzmechaniker/-in - Sitzmöbel- und Gestellindustrie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_265_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(293000, 10, "265", "05", true, "T", "HT", null, null, null, "Holzmechaniker/-in - Sitzmöbel- und Gestellindustrie", "Holzmechaniker - Sitzmöbel- und Gestellindustrie", "Holzmechanikerin - Sitzmöbel- und Gestellindustrie", null, 2013)
		}));

		/** Fachklasse Holzmechaniker/-in - Bauzubehörindustrie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_265_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(294000, 10, "265", "06", true, "T", "HT", null, null, null, "Holzmechaniker/-in - Bauzubehörindustrie", "Holzmechaniker - Bauzubehörindustrie", "Holzmechanikerin - Bauzubehörindustrie", null, 2012)
		}));

		/** Fachklasse Holzmechaniker/-in - Parkettindustrie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_265_07", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(295000, 10, "265", "07", true, "T", "HT", null, null, null, "Holzmechaniker/-in - Parkettindustrie", "Holzmechaniker - Parkettindustrie", "Holzmechanikerin - Parkettindustrie", null, 2012)
		}));

		/** Fachklasse Holzmechaniker/-in - Bauteile- und Bauzubehörindustrie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_265_08", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(296000, 10, "265", "08", true, "T", "HT", null, null, null, "Holzmechaniker/-in - Bauteile- und Bauzubehörindustrie", "Holzmechaniker - Bauteile- und Bauzubehörindustrie", "Holzmechanikerin - Bauteile- und Bauzubehörindustrie", null, 2012)
		}));

		/** Fachklasse Holzmechaniker/-in - Möbel- und Innenausbauindustrie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_265_09", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(297000, 10, "265", "09", true, "T", "HT", null, null, null, "Holzmechaniker/-in - Möbel- und Innenausbauindustrie", "Holzmechaniker - Möbel- und Innenausbauindustrie", "Holzmechanikerin - Möbel- und Innenausbauindustrie", null, 2013)
		}));

		/** Fachklasse Holzmechaniker/-in - Bauelemente, Holzpackmittel und Rahmen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_265_10", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(298000, 10, "265", "10", false, "T", "HT", "TN", null, "A3", "Holzmechaniker/-in - Bauelemente, Holzpackmittel und Rahmen", "Holzmechaniker - Bauelemente, Holzpackmittel und Rahmen", "Holzmechanikerin - Bauelemente, Holzpackmittel und Rahmen", null, null)
		}));

		/** Fachklasse Holzmechaniker/-in - Möbelbau und Innenausbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_265_11", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(299000, 10, "265", "11", false, "T", "HT", "TN", null, "A3", "Holzmechaniker/-in - Möbelbau und Innenausbau", "Holzmechaniker - Möbelbau und Innenausbau", "Holzmechanikerin - Möbelbau und Innenausbau", null, null)
		}));

		/** Fachklasse Holzmechaniker/-in - Montieren von Innenausbauten und Bauelementen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_265_12", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(300000, 10, "265", "12", false, null, null, "TN", null, "A3", "Holzmechaniker/-in - Montieren von Innenausbauten und Bauelementen", "Holzmechaniker - Montieren von Innenausbauten und Bauelementen", "Holzmechanikerin - Montieren von Innenausbauten und Bauelementen", null, null)
		}));

		/** Fachklasse Holzspielzeugmacher/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_266_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(301000, 10, "266", "00", false, "O", "OH", "OZ", null, "A3", "Holzspielzeugmacher/-in", "Holzspielzeugmacher", "Holzspielzeugmacherin", null, null)
		}));

		/** Fachklasse Hörakustiker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_267_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(302000, 10, "267", "00", false, "T", "TC", "TN", null, "A3", "Hörakustiker/-in", "Hörakustiker", "Hörakustikerin", null, null)
		}));

		/** Fachklasse Hotelfachmann/-frau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_268_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(303000, 10, "268", "00", false, "E", "EH", "EH", null, "A3", "Hotelfachmann/-frau", "Hotelfachmann", "Hotelfachfrau", null, null)
		}));

		/** Fachklasse Hotelkaufmann/-frau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_269_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(304000, 10, "269", "00", false, "E", "EH", "EH", null, "A3", "Hotelkaufmann/-frau", "Hotelkaufmann", "Hotelkauffrau", null, null)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 10 (Teil 4)
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex10_Teil4(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Hut- u. Mützenmacher/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_270_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(305000, 10, "270", "00", true, "O", "OH", null, null, null, "Hut- u. Mützenmacher/-in", "Hut- u. Mützenmacher", "Hut- u. Mützenmacherin", null, 2014)
		}));

		/** Fachklasse Industrieelektroniker/-in - Gerätetechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_271_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(306000, 10, "271", "01", true, "T", "ET", null, null, null, "Industrieelektroniker/-in - Gerätetechnik", "Industrieelektroniker - Gerätetechnik", "Industrieelektronikerin - Gerätetechnik", null, 2014)
		}));

		/** Fachklasse Industrieelektroniker/-in - Produktionstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_271_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(307000, 10, "271", "02", true, "T", "ET", null, null, null, "Industrieelektroniker/-in - Produktionstechnik", "Industrieelektroniker - Produktionstechnik", "Industrieelektronikerin - Produktionstechnik", null, 2014)
		}));

		/** Fachklasse Industriekeramiker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_272_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(308000, 10, "272", "00", true, "O", "OH", null, null, null, "Industriekeramiker/-in", "Industriekeramiker", "Industriekeramikerin", null, 2014)
		}));

		/** Fachklasse Industriekeramiker/-in - Formgebung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_272_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(309000, 10, "272", "01", true, "O", "OH", null, null, null, "Industriekeramiker/-in - Formgebung", "Industriekeramiker - Formgebung", "Industriekeramikerin - Formgebung", null, 2014)
		}));

		/** Fachklasse Industriekeramiker/-in - Mechanik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_272_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(310000, 10, "272", "02", true, "O", "OH", null, null, null, "Industriekeramiker/-in - Mechanik", "Industriekeramiker - Mechanik", "Industriekeramikerin - Mechanik", null, 2014)
		}));

		/** Fachklasse Industriekeramiker/-in - Anlagentechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_272_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(311000, 10, "272", "03", false, "O", "OH", "TN", null, "A3", "Industriekeramiker/-in - Anlagentechnik", "Industriekeramiker - Anlagentechnik", "Industriekeramikerin - Anlagentechnik", null, null)
		}));

		/** Fachklasse Industriekeramiker/-in - Dekorationstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_272_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(312000, 10, "272", "04", false, "O", "OH", "TN", null, "A3", "Industriekeramiker/-in - Dekorationstechnik", "Industriekeramiker - Dekorationstechnik", "Industriekeramikerin - Dekorationstechnik", null, null)
		}));

		/** Fachklasse Industriekeramiker/-in - Modelltechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_272_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(313000, 10, "272", "05", false, "O", "OH", "TN", null, "A3", "Industriekeramiker/-in - Modelltechnik", "Industriekeramiker - Modelltechnik", "Industriekeramikerin - Modelltechnik", null, null)
		}));

		/** Fachklasse Industriekeramiker/-in - Verfahrenstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_272_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(314000, 10, "272", "06", false, "O", "OH", "TN", null, "A3", "Industriekeramiker/-in - Verfahrenstechnik", "Industriekeramiker - Verfahrenstechnik", "Industriekeramikerin - Verfahrenstechnik", null, null)
		}));

		/** Fachklasse Industrieglasfertiger/-in - Maschinenbautechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_273_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(315000, 10, "273", "01", true, "T", "MT", null, null, null, "Industrieglasfertiger/-in - Maschinenbautechnik", "Industrieglasfertiger - Maschinenbautechnik", "Industrieglasfertigerin - Maschinenbautechnik", null, 2012)
		}));

		/** Fachklasse Industrieglasfertiger/-in - Meß-, Steuer-, Regeltechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_273_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(316000, 10, "273", "02", true, "T", "MT", null, null, null, "Industrieglasfertiger/-in - Meß-, Steuer-, Regeltechnik", "Industrieglasfertiger - Meß-, Steuer-, Regeltechnik", "Industrieglasfertigerin - Meß-, Steuer-, Regeltechnik", null, 2012)
		}));

		/** Fachklasse Industrieglasfertiger/-in - Weiterverarbeitung u. Veredlung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_273_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(317000, 10, "273", "03", true, "T", "MT", null, null, null, "Industrieglasfertiger/-in - Weiterverarbeitung u. Veredlung", "Industrieglasfertiger - Weiterverarbeitung u. Veredlung", "Industrieglasfertigerin - Weiterverarbeitung u. Veredlung", null, 2012)
		}));

		/** Fachklasse Industrie-Isolierer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_274_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(318000, 10, "274", "00", false, "O", "OH", "TN", null, "A3", "Industrie-Isolierer/-in", "Industrie-Isolierer", "Industrie-Isoliererin", null, null)
		}));

		/** Fachklasse Industriekaufmann/-frau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_275_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(319000, 10, "275", "00", false, "W", "WV", "WV", null, "A3", "Industriekaufmann/-frau", "Industriekaufmann", "Industriekauffrau", null, null)
		}));

		/** Fachklasse Industriemechaniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_276_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(320000, 10, "276", "00", false, "T", "MT", "TN", null, "A4", "Industriemechaniker/-in", "Industriemechaniker", "Industriemechanikerin", null, null)
		}));

		/** Fachklasse Industriemechaniker/-in - Betriebstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_276_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(321000, 10, "276", "01", true, "T", "MT", null, null, null, "Industriemechaniker/-in - Betriebstechnik", "Industriemechaniker - Betriebstechnik", "Industriemechanikerin - Betriebstechnik", null, 2014)
		}));

		/** Fachklasse Industriemechaniker/-in - Geräte- u. Feinwerktechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_276_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(322000, 10, "276", "02", true, "T", "MT", null, null, null, "Industriemechaniker/-in - Geräte- u. Feinwerktechnik", "Industriemechaniker - Geräte- u. Feinwerktechnik", "Industriemechanikerin - Geräte- u. Feinwerktechnik", null, 2014)
		}));

		/** Fachklasse Industriemechaniker/-in - Maschinen- u. Systemtechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_276_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(323000, 10, "276", "03", true, "T", "MT", null, null, null, "Industriemechaniker/-in - Maschinen- u. Systemtechnik", "Industriemechaniker - Maschinen- u. Systemtechnik", "Industriemechanikerin - Maschinen- u. Systemtechnik", null, 2014)
		}));

		/** Fachklasse Industriemechaniker/-in - Produktionstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_276_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(324000, 10, "276", "04", true, "T", "MT", null, null, null, "Industriemechaniker/-in - Produktionstechnik", "Industriemechaniker - Produktionstechnik", "Industriemechanikerin - Produktionstechnik", null, 2014)
		}));

		/** Fachklasse Informatikkaufmann/-frau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_277_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(325000, 10, "277", "00", true, "O", "OH", "WV", null, "A3", "Informatikkaufmann/-frau", "Informatikkaufmann", "Informatikkauffrau", null, 2021)
		}));

		/** Fachklasse Kaufmann/-frau für Digitalisierungsmanagement */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_278_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(326000, 10, "278", "00", false, null, null, "WV", null, "A3", "Kaufmann/-frau für Digitalisierungsmanagement", "Kaufmann für Digitalisierungsmanagement", "Kauffrau für Digitalisierungsmanagement", null, null)
		}));

		/** Fachklasse Informations- und Telekommunikationssystem - Elektroniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_279_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(327000, 10, "279", "00", true, "O", "OH", "TN", null, "A3", "Informations- und Telekommunikationssystem - Elektroniker/-in", "Informations- und Telekommunikationssystem - Elektroniker", "Informations- und Telekommunikationssystem - Elektronikerin", null, 2021)
		}));

		/** Fachklasse Informations- und Telekommunikationssystem - Kaufmann/-frau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_280_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(328000, 10, "280", "00", true, "O", "OH", "WV", null, "A3", "Informations- und Telekommunikationssystem - Kaufmann/-frau", "Informations- und Telekommunikationssystem - Kaufmann", "Informations- und Telekommunikationssystem - Kauffrau", null, 2021)
		}));

		/** Fachklasse Informationstechniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_281_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(329000, 10, "281", "00", true, "O", "OH", null, null, null, "Informationstechniker/-in", "Informationstechniker", "Informationstechnikerin", null, 2012)
		}));

		/** Fachklasse Isolierer/-in im Bereich der Industrie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_282_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(330000, 10, "282", "00", true, "T", "BT", null, null, null, "Isolierer/-in im Bereich der Industrie", "Isolierer im Bereich der Industrie", "Isoliererin im Bereich der Industrie", null, 2014)
		}));

		/** Fachklasse Isolierfacharbeiter/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_283_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(331000, 10, "283", "00", false, "O", "OH", "TN", null, "A2", "Isolierfacharbeiter/-in", "Isolierfacharbeiter", "Isolierfacharbeiterin", null, null)
		}));

		/** Fachklasse Isoliermonteur/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_284_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(332000, 10, "284", "00", true, "T", "BT", null, null, null, "Isoliermonteur/-in", "Isoliermonteur", "Isoliermonteurin", null, 2014)
		}));

		/** Fachklasse Justizangestellte/-r (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_285_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(333000, 10, "285", "00", true, "W", "WV", null, null, null, "Justizangestellte/-r", "Justizangestellter", "Justizangestellte", null, 2014)
		}));

		/** Fachklasse Justizfachangestellte/-r */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_286_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(334000, 10, "286", "00", false, "O", "OH", "WV", null, "A3", "Justizfachangestellte/-r", "Justizfachangestellter", "Justizfachangestellte", null, null)
		}));

		/** Fachklasse Kabeljungwerker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_287_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(335000, 10, "287", "00", true, "O", "OH", null, null, null, "Kabeljungwerker/-in", "Kabeljungwerker", "Kabeljungwerkerin", null, 2015)
		}));

		/** Fachklasse Kachelofen- u. Luftheizungsbauer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_288_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(336000, 10, "288", "00", true, "O", "OH", null, null, null, "Kachelofen- u. Luftheizungsbauer/-in", "Kachelofen- u. Luftheizungsbauer", "Kachelofen- u. Luftheizungsbauerin", null, 2014)
		}));

		/** Fachklasse Kälteanlagenbauer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_289_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(337000, 10, "289", "00", true, "T", "MT", null, null, null, "Kälteanlagenbauer/-in", "Kälteanlagenbauer", "Kälteanlagenbauerin", null, 2014)
		}));

		/** Fachklasse Kanalbauer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_290_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(338000, 10, "290", "00", false, "T", "BT", "TN", null, "A3", "Kanalbauer/-in", "Kanalbauer", "Kanalbauerin", null, null)
		}));

		/** Fachklasse Karosserie- u. Fahrzeugbauer/-in - Fahrzeugbau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_291_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(339000, 10, "291", "01", true, "T", "MT", null, null, null, "Karosserie- u. Fahrzeugbauer/-in - Fahrzeugbau", "Karosserie- u. Fahrzeugbauer - Fahrzeugbau", "Karosserie- u. Fahrzeugbauerin - Fahrzeugbau", null, 2014)
		}));

		/** Fachklasse Karosserie- u. Fahrzeugbauer/-in - Karosseriebau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_291_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(340000, 10, "291", "02", true, "T", "MT", null, null, null, "Karosserie- u. Fahrzeugbauer/-in - Karosseriebau", "Karosserie- u. Fahrzeugbauer - Karosseriebau", "Karosserie- u. Fahrzeugbauerin - Karosseriebau", null, 2014)
		}));

		/** Fachklasse Kartograf/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_292_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(341000, 10, "292", "00", true, "O", "OH", null, null, null, "Kartograf/-in", "Kartograf", "Kartografin", null, 2014)
		}));

		/** Fachklasse Kaufmann/-frau für audiovisuelle Medien */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_293_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(342000, 10, "293", "00", false, "O", "OH", "WV", null, "A3", "Kaufmann/-frau für audiovisuelle Medien", "Kaufmann für audiovisuelle Medien", "Kauffrau für audiovisuelle Medien", null, null)
		}));

		/** Fachklasse Kaufmann/-frau für Bürokommunikation (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_294_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(343000, 10, "294", "00", true, "O", "OH", null, null, null, "Kaufmann/-frau für Bürokommunikation", "Kaufmann für Bürokommunikation", "Kauffrau für Bürokommunikation", null, 2017)
		}));

		/** Fachklasse Kaufmann/-frau für informationstechnische Systeme (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_295_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(344000, 10, "295", "00", true, "W", "WV", null, null, null, "Kaufmann/-frau für informationstechnische Systeme", "Kaufmann für informationstechnische Systeme", "Kauffrau für informationstechnische Systeme", null, 2012)
		}));

		/** Fachklasse Kaufmann/-frau für Verkehrsservice */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_296_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(345000, 10, "296", "00", false, "O", "OH", "WV", null, "A3", "Kaufmann/-frau für Verkehrsservice", "Kaufmann für Verkehrsservice", "Kauffrau für Verkehrsservice", null, null)
		}));

		/** Fachklasse Kaufmann/-frau im Einzelhandel */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_297_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(346000, 10, "297", "00", false, "W", "WV", "WV", null, "A3", "Kaufmann/-frau im Einzelhandel", "Kaufmann im Einzelhandel", "Kauffrau im Einzelhandel", null, null)
		}));

		/** Fachklasse Kaufmann/-frau im Eisenbahn- und Straßenverkehr */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_298_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(347000, 10, "298", "00", false, "W", "WV", "WV", null, "A3", "Kaufmann/-frau im Eisenbahn- und Straßenverkehr", "Kaufmann im Eisenbahn- und Straßenverkehr", "Kauffrau im Eisenbahn- und Straßenverkehr", null, null)
		}));

		/** Fachklasse Kaufmann/-frau im Groß- u. Außenhandel (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_299_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(348000, 10, "299", "00", true, "W", "WV", null, null, null, "Kaufmann/-frau im Groß- u. Außenhandel", "Kaufmann im Groß- u. Außenhandel", "Kauffrau im Groß- u. Außenhandel", null, 2012)
		}));

		/** Fachklasse Kaufmann/-frau im Groß- und Außenhandel - Außenhandel (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_299_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(349000, 10, "299", "01", true, "W", "WV", "WV", null, "A3", "Kaufmann/-frau im Groß- und Außenhandel - Außenhandel", "Kaufmann im Groß- und Außenhandel - Außenhandel", "Kauffrau im Groß- und Außenhandel - Außenhandel", null, 2021)
		}));

		/** Fachklasse Kaufmann/-frau im Groß- und Außenhandel - Großhandel (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_299_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(350000, 10, "299", "02", true, "W", "WV", "WV", null, "A3", "Kaufmann/-frau im Groß- und Außenhandel - Großhandel", "Kaufmann im Groß- und Außenhandel - Großhandel", "Kauffrau im Groß- und Außenhandel - Großhandel", null, 2021)
		}));

		/** Fachklasse Kaufmann/-frau für Groß- und Außenhandelsmanagement - Großhandel */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_299_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(351000, 10, "299", "03", false, "W", "WV", "WV", null, "A3", "Kaufmann/-frau für Groß- und Außenhandelsmanagement - Großhandel", "Kaufmann für Groß- und Außenhandelsmanagement - Großhandel", "Kauffrau für Groß- und Außenhandelsmanagement - Großhandel", null, null)
		}));

		/** Fachklasse Kaufmann/-frau für Groß- und Außenhandelsmanagement - Außenhandel */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_299_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(352000, 10, "299", "04", false, "W", "WV", "WV", null, "A3", "Kaufmann/-frau für Groß- und Außenhandelsmanagement - Außenhandel", "Kaufmann für Groß- und Außenhandelsmanagement - Außenhandel", "Kauffrau für Groß- und Außenhandelsmanagement - Außenhandel", null, null)
		}));

		/** Fachklasse Kaufmann/-frau in der Grundstücks- u. Wohnungswirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_300_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(353000, 10, "300", "00", true, "W", "WV", null, null, null, "Kaufmann/-frau in der Grundstücks- u. Wohnungswirtschaft", "Kaufmann in der Grundstücks- u. Wohnungswirtschaft", "Kauffrau in der Grundstücks- u. Wohnungswirtschaft", null, 2014)
		}));

		/** Fachklasse Kaufmannsgehilfe/-gehilfin im Hotel- u. Gaststättengewerbe (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_301_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(354000, 10, "301", "00", true, "W", "WV", null, null, null, "Kaufmannsgehilfe/-gehilfin im Hotel- u. Gaststättengewerbe", "Kaufmannsgehilfe im Hotel- u. Gaststättengewerbe", "Kaufmannsgehilfin im Hotel- u. Gaststättengewerbe", null, 2014)
		}));

		/** Fachklasse Keramiker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_302_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(355000, 10, "302", "00", false, "O", "OH", "TN", null, "A3", "Keramiker/-in", "Keramiker", "Keramikerin", null, null)
		}));

		/** Fachklasse Keramiker/-in - Scheibentöpferei (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_302_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(356000, 10, "302", "01", true, "O", "OH", null, null, null, "Keramiker/-in - Scheibentöpferei", "Keramiker - Scheibentöpferei", "Keramikerin - Scheibentöpferei", null, 2014)
		}));

		/** Fachklasse Keramiker/-in - Baukeramik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_302_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(357000, 10, "302", "02", true, "O", "OH", null, null, null, "Keramiker/-in - Baukeramik", "Keramiker - Baukeramik", "Keramikerin - Baukeramik", null, 2014)
		}));

		/** Fachklasse Keramiker/-in - Dekoration (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_302_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(358000, 10, "302", "03", true, "O", "OH", null, null, null, "Keramiker/-in - Dekoration", "Keramiker - Dekoration", "Keramikerin- Dekoration", null, 2014)
		}));

		/** Fachklasse Kerammodelleinrichter/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_303_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(359000, 10, "303", "00", true, "O", "OH", null, null, null, "Kerammodelleinrichter/-in", "Kerammodelleinrichter", "Kerammodelleinrichterin", null, 2014)
		}));

		/** Fachklasse Kerammodelleur/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_304_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(360000, 10, "304", "00", true, "O", "OH", null, null, null, "Kerammodelleur/-in", "Kerammodelleur", "Kerammodelleurin", null, 2014)
		}));

		/** Fachklasse Klavier- u. Cembalobauer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_305_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(361000, 10, "305", "00", true, "O", "OH", null, null, null, "Klavier- u. Cembalobauer/-in", "Klavier- u. Cembalobauer", "Klavier- u. Cembalobauerin", null, 2012)
		}));

		/** Fachklasse Klavier- und Cembalobauer/-in - Klavierbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_305_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(362000, 10, "305", "01", false, "O", "OH", "OZ", null, "A4", "Klavier- und Cembalobauer/-in - Klavierbau", "Klavier- und Cembalobauer - Klavierbau", "Klavier- und Cembalobauerin - Klavierbau", null, null)
		}));

		/** Fachklasse Klavier- und Cembalobauer/-in - Cembalobau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_305_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(363000, 10, "305", "02", false, "O", "OH", "OZ", null, "A4", "Klavier- und Cembalobauer/-in - Cembalobau", "Klavier- und Cembalobauer - Cembalobau", "Klavier- und Cembalobauerin - Cembalobau", null, null)
		}));

		/** Fachklasse Klebeabdichter/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_306_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(364000, 10, "306", "00", true, "T", "BT", null, null, null, "Klebeabdichter/-in", "Klebeabdichter", "Klebeabdichterin", null, 2014)
		}));

		/** Fachklasse Klempner/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_307_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(365000, 10, "307", "00", false, "T", "MT", "TN", null, "A4", "Klempner/-in", "Klempner", "Klempnerin", null, null)
		}));

		/** Fachklasse Koch/Köchin */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_308_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(366000, 10, "308", "00", false, "E", "EH", "EH", null, "A3", "Koch/Köchin", "Koch", "Köchin", null, null)
		}));

		/** Fachklasse Kommunikationselektroniker/-in - Funktechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_309_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(367000, 10, "309", "01", true, "T", "ET", null, null, null, "Kommunikationselektroniker/-in - Funktechnik", "Kommunikationselektroniker - Funktechnik", "Kommunikationselektronikerin - Funktechnik", null, 2014)
		}));

		/** Fachklasse Kommunikationselektroniker/-in - Informationstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_309_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(368000, 10, "309", "02", true, "T", "ET", null, null, null, "Kommunikationselektroniker/-in - Informationstechnik", "Kommunikationselektroniker - Informationstechnik", "Kommunikationselektronikerin - Informationstechnik", null, 2014)
		}));

		/** Fachklasse Kommunikationselektroniker/-in - Telekommunikationstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_309_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(369000, 10, "309", "03", true, "T", "ET", null, null, null, "Kommunikationselektroniker/-in - Telekommunikationstechnik", "Kommunikationselektroniker - Telekommunikationstechnik", "Kommunikationselektronikerin - Telekommunikationstechnik", null, 2014)
		}));

		/** Fachklasse Konditor/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_310_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(370000, 10, "310", "00", false, "E", "EH", "EH", null, "A3", "Konditor/-in", "Konditor", "Konditorin", null, null)
		}));

		/** Fachklasse Konstruktionsmechaniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_311_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(371000, 10, "311", "00", false, "T", "MT", "TN", null, "A4", "Konstruktionsmechaniker/-in", "Konstruktionsmechaniker", "Konstruktionsmechanikerin", null, null)
		}));

		/** Fachklasse Konstruktionsmechaniker/-in - Ausrüstungstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_311_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(372000, 10, "311", "01", true, "T", "MT", null, null, null, "Konstruktionsmechaniker/-in - Ausrüstungstechnik", "Konstruktionsmechaniker - Ausrüstungstechnik", "Konstruktionsmechanikerin - Ausrüstungstechnik", null, 2014)
		}));

		/** Fachklasse Konstruktionsmechaniker/-in - Feinblechbautechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_311_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(373000, 10, "311", "02", true, "T", "MT", null, null, null, "Konstruktionsmechaniker/-in - Feinblechbautechnik", "Konstruktionsmechaniker - Feinblechbautechnik", "Konstruktionsmechanikerin - Feinblechbautechnik", null, 2014)
		}));

		/** Fachklasse Konstruktionsmechaniker/-in - Metall- u. Schiffbautechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_311_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(374000, 10, "311", "03", true, "T", "MT", null, null, null, "Konstruktionsmechaniker/-in - Metall- u. Schiffbautechnik", "Konstruktionsmechaniker - Metall- u. Schiffbautechnik", "Konstruktionsmechanikerin - Metall- u. Schiffbautechnik", null, 2014)
		}));

		/** Fachklasse Konstruktionsmechaniker/-in - Schweißtechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_311_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(375000, 10, "311", "04", true, "T", "MT", null, null, null, "Konstruktionsmechaniker/-in - Schweißtechnik", "Konstruktionsmechaniker - Schweißtechnik", "Konstruktionsmechanikerin - Schweißtechnik", null, 2014)
		}));

		/** Fachklasse Korbmacher/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_312_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(376000, 10, "312", "00", true, "O", "OH", null, null, null, "Korbmacher/-in", "Korbmacher", "Korbmacherin", null, 2014)
		}));

		/** Fachklasse Korbmacher/-in - Korbwarenherstellung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_312_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(377000, 10, "312", "01", true, "O", "OH", null, null, null, "Korbmacher/-in - Korbwarenherstellung", "Korbmacher - Korbwarenherstellung", "Korbmacherin - Korbwarenherstellung", null, 2014)
		}));

		/** Fachklasse Korbmacher/-in - Korbmöbelbau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_312_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(378000, 10, "312", "02", true, "O", "OH", null, null, null, "Korbmacher/-in - Korbmöbelbau", "Korbmacher - Korbmöbelbau", "Korbmacherin - Korbmöbelbau", null, 2014)
		}));

		/** Fachklasse Kraftfahrzeugelektriker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_313_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(379000, 10, "313", "00", true, "T", "MT", null, null, null, "Kraftfahrzeugelektriker/-in", "Kraftfahrzeugelektriker", "Kraftfahrzeugelektrikerin", null, 2014)
		}));

		/** Fachklasse Kraftfahrzeugmechaniker/-in - Kraftradinstandhaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_314_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(380000, 10, "314", "01", true, "T", "MT", null, null, null, "Kraftfahrzeugmechaniker/-in - Kraftradinstandhaltung", "Kraftfahrzeugmechaniker - Kraftradinstandhaltung", "Kraftfahrzeugmechanikerin - Kraftradinstandhaltung", null, 2012)
		}));

		/** Fachklasse Kraftfahrzeugmechaniker/-in - Nutzkraftwageninstandhaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_314_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(381000, 10, "314", "02", true, "T", "MT", null, null, null, "Kraftfahrzeugmechaniker/-in - Nutzkraftwageninstandhaltung", "Kraftfahrzeugmechaniker - Nutzkraftwageninstandhaltung", "Kraftfahrzeugmechanikerin - Nutzkraftwageninstandhaltung", null, 2012)
		}));

		/** Fachklasse Kraftfahrzeugmechaniker/-in - Personenkraftwageninstandhaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_314_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(382000, 10, "314", "03", true, "T", "MT", null, null, null, "Kraftfahrzeugmechaniker/-in - Personenkraftwageninstandhaltung", "Kraftfahrzeugmechaniker - Personenkraftwageninstandhaltung", "Kraftfahrzeugmechanikerin - Personenkraftwageninstandhaltung", null, 2012)
		}));

		/** Fachklasse Krawattennäher/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_315_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(383000, 10, "315", "00", true, "T", "TB", null, null, null, "Krawattennäher/-in", "Krawattennäher", "Krawattennäherin", null, 2014)
		}));

		/** Fachklasse Kunststoff- u. Schwergewebekonfektionär/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_316_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(384000, 10, "316", "00", true, "T", "TB", null, null, null, "Kunststoff- u. Schwergewebekonfektionär/-in", "Kunststoff- u. Schwergewebekonfektionär", "Kunststoff- u. Schwergewebekonfektionärin", null, 2014)
		}));

		/** Fachklasse Kunststoff-Formgeber/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_317_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(385000, 10, "317", "00", true, "T", "CP", null, null, null, "Kunststoff-Formgeber/-in", "Kunststoff-Formgeber", "Kunststoff-Formgeberin", null, 2014)
		}));

		/** Fachklasse Kunststoffschlosser/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_318_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(386000, 10, "318", "00", true, "T", "MT", null, null, null, "Kunststoffschlosser/-in", "Kunststoffschlosser", "Kunststoffschlosserin", null, 2014)
		}));

		/** Fachklasse Kupferschmied/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_319_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(387000, 10, "319", "00", true, "T", "MT", null, null, null, "Kupferschmied/-in", "Kupferschmied", "Kupferschmiedin", null, 2014)
		}));

		/** Fachklasse Kürschner/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_320_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(388000, 10, "320", "00", false, "O", "OH", "OZ", null, "A3", "Kürschner/-in", "Kürschner", "Kürschnerin", null, null)
		}));

		/** Fachklasse Lackierer/-in - Holz und Metall (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_321_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(389000, 10, "321", "01", true, "G", "FR", null, null, null, "Lackierer/-in - Holz und Metall", "Lackierer - Holz und Metall", "Lackiererin - Holz und Metall", null, 2014)
		}));

		/** Fachklasse Lackierer/-in - Oberflächentechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_321_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(390000, 10, "321", "02", true, "G", "FR", null, null, null, "Lackierer/-in - Oberflächentechnik", "Lackierer - Oberflächentechnik", "Lackiererin - Oberflächentechnik", null, 2012)
		}));

		/** Fachklasse Lacklaborant/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_322_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(391000, 10, "322", "00", false, "T", "CP", "TN", null, "A4", "Lacklaborant/-in", "Lacklaborant", "Lacklaborantin", null, null)
		}));

		/** Fachklasse Landmaschinenmechaniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_323_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(392000, 10, "323", "00", true, "T", "MT", null, null, null, "Landmaschinenmechaniker/-in", "Landmaschinenmechaniker", "Landmaschinenmechanikerin", null, 2014)
		}));

		/** Fachklasse Landwirt/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_324_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(393000, 10, "324", "00", false, "A", "AW", "AW", null, "A3", "Landwirt/-in", "Landwirt", "Landwirtin", null, null)
		}));

		/** Fachklasse Leichtflugzeugbauer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_325_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(394000, 10, "325", "00", false, "T", "HT", "TN", null, "A3", "Leichtflugzeugbauer/-in", "Leichtflugzeugbauer", "Leichtflugzeugbauerin", null, null)
		}));

		/** Fachklasse Leuchtröhrenglasbläser/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_326_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(395000, 10, "326", "00", false, "O", "OH", "TN", null, "A3", "Leuchtröhrenglasbläser/-in", "Leuchtröhrenglasbläser", "Leuchtröhrenglasbläserin", null, null)
		}));

		/** Fachklasse Lichtdruckretuscheur/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_327_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(396000, 10, "327", "00", true, "T", "DT", null, null, null, "Lichtdruckretuscheur/-in", "Lichtdruckretuscheur", "Lichtdruckretuscheurin", null, 2012)
		}));

		/** Fachklasse Luftverkehrskaufmann/-frau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_328_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(397000, 10, "328", "00", false, "W", "WV", "TN", null, "A3", "Luftverkehrskaufmann/-frau", "Luftverkehrskaufmann", "Luftverkehrskauffrau", null, null)
		}));

		/** Fachklasse Maler/-in u. Lackierer/-in - Bau- u. Metallmaler (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_329_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(398000, 10, "329", "01", true, "G", "FR", null, null, null, "Maler/-in u. Lackierer/-in - Bau- u. Metallmaler", "Maler u. Lackierer - Bau- u. Metallmaler", "Malerin u. Lackiererin - Bau- u. Metallmaler", null, 2012)
		}));

		/** Fachklasse Maler/-in u. Lackierer/-in - Fahrzeuglackierer (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_329_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(399000, 10, "329", "02", true, "G", "FR", null, null, null, "Maler/-in u. Lackierer/-in - Fahrzeuglackierer", "Maler u. Lackierer - Fahrzeuglackierer", "Malerin u. Lackiererin - Fahrzeuglackierer", null, 2012)
		}));

		/** Fachklasse Maler/-in u. Lackierer/-in - Industrielackierer (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_329_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(400000, 10, "329", "03", true, "G", "FR", null, null, null, "Maler/-in u. Lackierer/-in - Industrielackierer", "Maler u. Lackierer - Industrielackierer", "Malerin u. Lackiererin - Industrielackierer", null, 2012)
		}));

		/** Fachklasse Maler/-in u. Lackierer/-in - Maler (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_329_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(401000, 10, "329", "04", true, "G", "FR", null, null, null, "Maler/-in u. Lackierer/-in - Maler", "Maler u. Lackierer - Maler", "Malerin u. Lackiererin - Maler", null, 2012)
		}));

		/** Fachklasse Maler/-in und Lackierer/-in - Gestaltung und Instandhaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_329_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(402000, 10, "329", "05", false, "G", "FR", "TN", null, "A3", "Maler/-in und Lackierer/-in - Gestaltung und Instandhaltung", "Maler und Lackierer - Gestaltung und Instandhaltung", "Malerin und Lackiererin - Gestaltung und Instandhaltung", null, null)
		}));

		/** Fachklasse Maler/-in und Lackierer/-in - Kirchenmalerei und Denkmalpflege */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_329_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(403000, 10, "329", "06", false, "G", "FR", "TN", null, "A3", "Maler/-in und Lackierer/-in - Kirchenmalerei und Denkmalpflege", "Maler und Lackierer - Kirchenmalerei und Denkmalpflege", "Malerin und Lackiererin - Kirchenmalerei und Denkmalpflege", null, null)
		}));

		/** Fachklasse Maler/-in und Lackierer/-in - Bauten- und Korrosionsschutz */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_329_07", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(404000, 10, "329", "07", false, "G", "FR", "TN", null, "A3", "Maler/-in und Lackierer/-in - Bauten- und Korrosionsschutz", "Maler und Lackierer - Bauten- und Korrosionsschutz", "Malerin und Lackiererin - Bauten- und Korrosionsschutz", null, null)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 10 (Teil 5)
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex10_Teil5(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Maler/-in und Lackierer/-in  - Ausbautechnik und Oberflächengestaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_329_08", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(405000, 10, "329", "08", false, null, null, "TN", null, "A3", "Maler/-in und Lackierer/-in  - Ausbautechnik und Oberflächengestaltung", "Maler und Lackierer - Ausbautechnik und Oberflächengestaltung", "Malerin und Lackiererin - Ausbautechnik und Oberflächengestaltung", null, null)
		}));

		/** Fachklasse Maler/-in und Lackierer/-in  - Energieeffizienz- und Gestaltungstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_329_09", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(406000, 10, "329", "09", false, null, null, "TN", null, "A3", "Maler/-in und Lackierer/-in  - Energieeffizienz- und Gestaltungstechnik", "Maler und Lackierer - Energieeffizienz- und Gestaltungstechnik", "Malerin und Lackiererin - Energieeffizienz- und Gestaltungstechnik", null, null)
		}));

		/** Fachklasse Manufakturporzellanmaler/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_330_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(407000, 10, "330", "00", false, "O", "OH", "OZ", null, "A4", "Manufakturporzellanmaler/-in", "Manufakturporzellanmaler", "Manufakturporzellanmalerin", null, null)
		}));

		/** Fachklasse Maschinenbaumechaniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_331_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(408000, 10, "331", "00", true, "T", "MT", null, null, null, "Maschinenbaumechaniker/-in", "Maschinenbaumechaniker", "Maschinenbaumechanikerin", null, 2014)
		}));

		/** Fachklasse Maschinenzusammensetzer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_332_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(409000, 10, "332", "00", true, "O", "OH", null, null, null, "Maschinenzusammensetzer/-in", "Maschinenzusammensetzer", "Maschinenzusammensetzerin", null, 2015)
		}));

		/** Fachklasse Mathematisch-technische/-r Assistent/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_333_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(410000, 10, "333", "00", true, "T", "CP", null, null, null, "Mathematisch-technische/-r Assistent/-in", "Mathematisch-technischer Assistent", "Mathematisch-technische Assistentin", null, 2014)
		}));

		/** Fachklasse Maurer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_334_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(411000, 10, "334", "00", false, "T", "BT", "TN", null, "A3", "Maurer/-in", "Maurer", "Maurerin", null, null)
		}));

		/** Fachklasse Mechatroniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_335_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(412000, 10, "335", "00", false, "O", "OH", "TN", null, "A4", "Mechatroniker/-in", "Mechatroniker", "Mechatronikerin", null, null)
		}));

		/** Fachklasse Mediengestalter/-in Bild und Ton */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_336_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(413000, 10, "336", "00", false, "O", "OH", "GT", null, "A3", "Mediengestalter/-in Bild und Ton", "Mediengestalter Bild und Ton", "Mediengestalterin Bild und Ton", null, null)
		}));

		/** Fachklasse Mediengestalter/-in für Digital- und Printmedien (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_337_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(414000, 10, "337", "00", true, "O", "OH", null, null, null, "Mediengestalter/-in für Digital- und Printmedien", "Mediengestalter für Digital- und Printmedien", "Mediengestalterin für Digital- und Printmedien", null, 2014)
		}));

		/** Fachklasse Mediengestalter/-in für Digital und Print - Medienberatung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_337_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(415000, 10, "337", "01", true, "O", "OH", null, null, null, "Mediengestalter/-in für Digital und Print - Medienberatung", "Mediengestalter für Digital und Print - Medienberatung", "Mediengestalterin für Digital und Print - Medienberatung", null, 2014)
		}));

		/** Fachklasse Mediengestalter/-in für Digital und Print - Mediendesign (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_337_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(416000, 10, "337", "02", true, "O", "OH", null, null, null, "Mediengestalter/-in für Digital und Print - Mediendesign", "Mediengestalter für Digital und Print - Mediendesign", "Mediengestalterin für Digital und Print - Mediendesign", null, 2014)
		}));

		/** Fachklasse Mediengestalter/-in für Digital und Print - Medienoperating (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_337_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(417000, 10, "337", "03", true, "O", "OH", null, null, null, "Mediengestalter/-in für Digital und Print - Medienoperating", "Mediengestalter für Digital und Print - Medienoperating", "Mediengestalterin für Digital und Print - Medienoperating", null, 2014)
		}));

		/** Fachklasse Mediengestalter/-in für Digital und Print - Medientechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_337_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(418000, 10, "337", "04", true, "O", "OH", null, null, null, "Mediengestalter/-in für Digital und Print - Medientechnik", "Mediengestalter für Digital und Print - Medientechnik", "Mediengestalterin für Digital und Print - Medientechnik", null, 2014)
		}));

		/** Fachklasse Mediengestalter/-in Digital und Print - Beratung und Planung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_337_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(419000, 10, "337", "05", false, "O", "OH", "GT", null, "A3", "Mediengestalter/-in Digital und Print - Beratung und Planung", "Mediengestalter Digital und Print - Beratung und Planung", "Mediengestalterin Digital und Print - Beratung und Planung", null, null)
		}));

		/** Fachklasse Mediengestalter/-in Digital und Print - Gestaltung und Technik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_337_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(420000, 10, "337", "06", false, "O", "OH", "GT", null, "A3", "Mediengestalter/-in Digital und Print - Gestaltung und Technik", "Mediengestalter Digital und Print - Gestaltung und Technik", "Mediengestalterin Digital und Print - Gestaltung und Technik", null, null)
		}));

		/** Fachklasse Mediengestalter/-in Digital und Print - Konzeption und Visualisierung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_337_07", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(421000, 10, "337", "07", false, "O", "OH", "GT", null, "A3", "Mediengestalter/-in Digital und Print - Konzeption und Visualisierung", "Mediengestalter Digital und Print - Konzeption und Visualisierung", "Mediengestalterin Digital und Print - Konzeption und Visualisierung", null, null)
		}));

		/** Fachklasse Metall- und Glockengießer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_338_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(422000, 10, "338", "00", true, "O", "OH", null, null, null, "Metall- und Glockengießer/-in", "Metall- und Glockengießer", "Metall- und Glockengießerin", null, 2012)
		}));

		/** Fachklasse Metall- und Glockengießer/-in - Zinngusstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_338_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(423000, 10, "338", "01", false, "O", "OH", "OZ", null, "A3", "Metall- und Glockengießer/-in - Zinngusstechnik", "Metall- und Glockengießer - Zinngusstechnik", "Metall- und Glockengießerin - Zinngusstechnik", null, null)
		}));

		/** Fachklasse Metall- und Glockengießer/-in - Metallgusstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_338_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(424000, 10, "338", "02", false, "O", "OH", "OZ", null, "A3", "Metall- und Glockengießer/-in - Metallgusstechnik", "Metall- und Glockengießer - Metallgusstechnik", "Metall- und Glockengießerin - Metallgusstechnik", null, null)
		}));

		/** Fachklasse Metall- und Glockengießer/-in - Kunst- und Glockengusstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_338_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(425000, 10, "338", "03", false, "O", "OH", "OZ", null, "A3", "Metall- und Glockengießer/-in - Kunst- und Glockengusstechnik", "Metall- und Glockengießer - Kunst- und Glockengusstechnik", "Metall- und Glockengießerin - Kunst- und Glockengusstechnik", null, null)
		}));

		/** Fachklasse Metallbauer/-in - Anlagen- u. Fördertechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_339_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(426000, 10, "339", "01", true, "T", "MT", null, null, null, "Metallbauer/-in - Anlagen- u. Fördertechnik", "Metallbauer - Anlagen- u. Fördertechnik", "Metallbauerin - Anlagen- u. Fördertechnik", null, 2012)
		}));

		/** Fachklasse Metallbauer/-in - Nutzfahrzeugbau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_339_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(427000, 10, "339", "02", true, "T", "MT", null, null, null, "Metallbauer/-in - Nutzfahrzeugbau", "Metallbauer - Nutzfahrzeugbau", "Metallbauerin - Nutzfahrzeugbau", null, 2012)
		}));

		/** Fachklasse Metallbauer/-in - Konstruktionstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_339_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(428000, 10, "339", "03", false, "T", "MT", "TN", null, "A4", "Metallbauer/-in - Konstruktionstechnik", "Metallbauer - Konstruktionstechnik", "Metallbauerin - Konstruktionstechnik", null, null)
		}));

		/** Fachklasse Metallbauer/-in - Landtechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_339_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(429000, 10, "339", "04", true, "T", "MT", null, null, null, "Metallbauer/-in - Landtechnik", "Metallbauer - Landtechnik", "Metallbauerin - Landtechnik", null, 2012)
		}));

		/** Fachklasse Metallbauer/-in - Metallgestaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_339_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(430000, 10, "339", "05", false, "T", "MT", "TN", null, "A4", "Metallbauer/-in - Metallgestaltung", "Metallbauer - Metallgestaltung", "Metallbauerin - Metallgestaltung", null, null)
		}));

		/** Fachklasse Metallbauer/-in - Nutzfahrzeugbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_339_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(431000, 10, "339", "06", false, "T", "MT", "TN", null, "A4", "Metallbauer/-in - Nutzfahrzeugbau", "Metallbauer - Nutzfahrzeugbau", "Metallbauerin - Nutzfahrzeugbau", null, null)
		}));

		/** Fachklasse Metallbildner/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_340_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(432000, 10, "340", "00", true, "T", "MT", null, null, null, "Metallbildner/-in", "Metallbildner", "Metallbildnerin", null, 2012)
		}));

		/** Fachklasse Metallbildner/-in - Goldschlagtechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_340_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(433000, 10, "340", "01", true, "O", "OH", "GT", null, "A3", "Metallbildner/-in - Goldschlagtechnik", "Metallbildner - Goldschlagtechnik", "Metallbildnerin - Goldschlagtechnik", null, 2019)
		}));

		/** Fachklasse Metallbildner/-in - Gürtler und Metalldrücktechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_340_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(434000, 10, "340", "02", true, "O", "OH", "GT", null, "A3", "Metallbildner/-in - Gürtler und Metalldrücktechnik", "Metallbildner - Gürtler- und Metalldrücktechnik", "Metallbildnerin - Gürtler- und Metalldrücktechnik", null, 2019)
		}));

		/** Fachklasse Metallbildner/-in - Ziseliertechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_340_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(435000, 10, "340", "03", false, "O", "OH", "GT", null, "A3", "Metallbildner/-in - Ziseliertechnik", "Metallbildner - Ziseliertechnik", "Metallbildnerin - Ziseliertechnik", null, null)
		}));

		/** Fachklasse Metallbildner/-in - Gürtlertechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_340_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(436000, 10, "340", "04", false, "O", "OH", "GT", null, "A3", "Metallbildner/-in - Gürtlertechnik", "Metallbildner - Gürtlertechnik", "Metallbildnerin - Gürtlertechnik", null, null)
		}));

		/** Fachklasse Metallbildner/-in - Metalldrücktechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_340_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(437000, 10, "340", "05", false, "O", "OH", "GT", null, "A3", "Metallbildner/-in - Metalldrücktechnik", "Metallbildner - Metalldrücktechnik", "Metallbildnerin - Metalldrücktechnik", null, null)
		}));

		/** Fachklasse Metallblasinstrumentenmacher/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_341_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(438000, 10, "341", "00", false, "O", "OH", "OZ", null, "A3", "Metallblasinstrumentenmacher/-in", "Metallblasinstrumentenmacher", "Metallblasinstrumentenmacherin", null, null)
		}));

		/** Fachklasse Metallformer/-in u. -gießer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_342_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(439000, 10, "342", "00", true, "T", "MT", null, null, null, "Metallformer/-in u. -gießer/-in", "Metallformer u. -gießer", "Metallformerin u. -gießerin", null, 2014)
		}));

		/** Fachklasse Metallschleifer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_343_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(440000, 10, "343", "00", true, "T", "MT", null, null, null, "Metallschleifer/-in", "Metallschleifer", "Metallschleiferin", null, 2015)
		}));

		/** Fachklasse Mikrotechnologe/-technologin (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_344_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(441000, 10, "344", "00", true, "O", "OH", "TN", null, "A3", "Mikrotechnologe/-technologin", "Mikrotechnologe", "Mikrotechnologin", null, 2021)
		}));

		/** Fachklasse Mikrotechnologe/-technologin - Halbleitertechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_344_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(442000, 10, "344", "01", false, "O", "OH", "TN", null, "A3", "Mikrotechnologe/-technologin - Halbleitertechnik", "Mikrotechnologe - Halbleitertechnik", "Mikrotechnologin - Halbleitertechnik", null, null)
		}));

		/** Fachklasse Mikrotechnologe/-technologin - Mikrosystemtechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_344_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(443000, 10, "344", "02", false, "O", "OH", "TN", null, "A3", "Mikrotechnologe/-technologin - Mikrosystemtechnik", "Mikrotechnologe - Mikrosystemtechnik", "Mikrotechnologin - Mikrosystemtechnik", null, null)
		}));

		/** Fachklasse Milchwirtschaftliche/-r Laborant/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_345_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(444000, 10, "345", "00", false, "T", "CP", "OZ", null, "A3", "Milchwirtschaftliche/-r Laborant/-in", "Milchwirtschaftlicher Laborant", "Milchwirtschaftliche Laborantin", null, null)
		}));

		/** Fachklasse Modellbauer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_346_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(445000, 10, "346", "00", true, "T", "HT", null, null, null, "Modellbauer/-in", "Modellbauer", "Modellbauerin", null, 2014)
		}));

		/** Fachklasse Modellbauer/-in - Anschauungsmodellbau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_346_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(446000, 10, "346", "01", true, "T", "HT", null, null, null, "Modellbauer/-in - Anschauungsmodellbau", "Modellbauer - Anschauungsmodellbau", "Modellbauerin - Anschauungsmodellbau", null, 2014)
		}));

		/** Fachklasse Modellbauer/-in - Produktionsmodellbau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_346_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(447000, 10, "346", "02", true, "T", "HT", null, null, null, "Modellbauer/-in - Produktionsmodellbau", "Modellbauer - Produktionsmodellbau", "Modellbauerin - Produktionsmodellbau", null, 2014)
		}));

		/** Fachklasse Modellbaumechaniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_347_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(448000, 10, "347", "00", true, "O", "OH", null, null, null, "Modellbaumechaniker/-in", "Modellbaumechaniker", "Modellbaumechanikerin", null, 2014)
		}));

		/** Fachklasse Modellbaumechaniker/-in - Gießereimodellbau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_347_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(449000, 10, "347", "01", true, "O", "OH", null, null, null, "Modellbaumechaniker/-in - Gießereimodellbau", "Modellbaumechaniker - Gießereimodellbau", "Modellbaumechanikerin - Gießereimodellbau", null, 2014)
		}));

		/** Fachklasse Modellbaumechaniker/-in - Karosseriemodellbau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_347_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(450000, 10, "347", "02", true, "O", "OH", null, null, null, "Modellbaumechaniker/-in - Karosseriemodellbau", "Modellbaumechaniker - Karosseriemodellbau", "Modellbaumechanikerin - Karosseriemodellbau", null, 2014)
		}));

		/** Fachklasse Modellschlosser/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_348_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(451000, 10, "348", "00", true, "T", "MT", null, null, null, "Modellschlosser/-in", "Modellschlosser", "Modellschlosserin", null, 2014)
		}));

		/** Fachklasse Modelltischler/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_349_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(452000, 10, "349", "00", true, "T", "HT", null, null, null, "Modelltischler/-in", "Modelltischler", "Modelltischlerin", null, 2014)
		}));

		/** Fachklasse Modenäher/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_350_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(453000, 10, "350", "00", true, "T", "TB", "TN", null, "A2", "Modenäher/-in", "Modenäher", "Modenäherin", null, 2017)
		}));

		/** Fachklasse Modeschneider/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_351_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(454000, 10, "351", "00", true, "T", "TB", "TN", null, "A3", "Modeschneider/-in", "Modeschneider", "Modeschneiderin", null, 2019)
		}));

		/** Fachklasse Modist/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_352_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(455000, 10, "352", "00", false, "T", "TB", "TN", null, "A3", "Modist/-in", "Modist", "Modistin", null, null)
		}));

		/** Fachklasse Molkereifachmann/-frau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_353_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(456000, 10, "353", "00", true, "O", "OH", null, null, null, "Molkereifachmann/-frau", "Molkereifachmann", "Molkereifachfrau", null, 2014)
		}));

		/** Fachklasse Müller/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_354_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(457000, 10, "354", "00", true, "O", "OH", null, null, null, "Müller/-in", "Müller", "Müllerin", null, 2014)
		}));

		/** Fachklasse Musikalienhändler/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_355_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(458000, 10, "355", "00", true, "W", "WV", null, null, null, "Musikalienhändler/-in", "Musikalienhändler", "Musikalienhändlerin", null, 2014)
		}));

		/** Fachklasse Mützenmacher/-in, Mützennäher/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_356_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(459000, 10, "356", "00", true, "T", "TB", null, null, null, "Mützenmacher/-in, Mützennäher/-in", "Mützenmacher, Mützennäher", "Mützenmacherin, Mützennäherin", null, 2012)
		}));

		/** Fachklasse Naturwerksteinmechaniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_357_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(460000, 10, "357", "00", true, "O", "OH", null, null, null, "Naturwerksteinmechaniker/-in", "Naturwerksteinmechaniker", "Naturwerksteinmechanikerin", null, 2012)
		}));

		/** Fachklasse Naturwerksteinmechaniker/-in - Maschinenbearbeitungstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_357_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(461000, 10, "357", "01", false, "O", "OH", "TN", null, "A3", "Naturwerksteinmechaniker/-in - Maschinenbearbeitungstechnik", "Naturwerksteinmechaniker - Maschinenbearbeitungstechnik", "Naturwerksteinmechanikerin - Maschinenbearbeitungstechnik", null, null)
		}));

		/** Fachklasse Naturwerksteinmechaniker/-in - Schleiftechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_357_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(462000, 10, "357", "02", false, "O", "OH", "TN", null, "A3", "Naturwerksteinmechaniker/-in - Schleiftechnik", "Naturwerksteinmechaniker - Schleiftechnik", "Naturwerksteinmechanikerin - Schleiftechnik", null, null)
		}));

		/** Fachklasse Naturwerksteinmechaniker/-in - Steinmetztechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_357_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(463000, 10, "357", "03", false, "O", "OH", "TN", null, "A3", "Naturwerksteinmechaniker/-in - Steinmetztechnik", "Naturwerksteinmechaniker - Steinmetztechnik", "Naturwerksteinmechanikerin - Steinmetztechnik", null, null)
		}));

		/** Fachklasse Notarfachangestellte/-r */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_358_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(464000, 10, "358", "00", false, "O", "OH", "WV", null, "A3", "Notarfachangestellte/-r", "Notarfachangestellter", "Notarfachangestellte", null, null)
		}));

		/** Fachklasse Orgel- u. Harmoniumbauer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_359_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(465000, 10, "359", "00", true, "O", "OH", null, null, null, "Orgel- u. Harmoniumbauer/-in", "Orgel- u. Harmoniumbauer", "Orgel- u. Harmoniumbauerin", null, 2012)
		}));

		/** Fachklasse Orgelbauer/-in - Orgelbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_359_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(466000, 10, "359", "01", false, "O", "OH", "OZ", null, "A4", "Orgelbauer/-in - Orgelbau", "Orgelbauer - Orgelbau", "Orgelbauerin - Orgelbau", null, null)
		}));

		/** Fachklasse Orgelbauer/-in - Pfeifenbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_359_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(467000, 10, "359", "02", false, "O", "OH", "OZ", null, "A4", "Orgelbauer/-in - Pfeifenbau", "Orgelbauer - Pfeifenbau", "Orgelbauerin - Pfeifenbau", null, null)
		}));

		/** Fachklasse Orthopädiemechaniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_360_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(468000, 10, "360", "00", true, "T", "CP", null, null, null, "Orthopädiemechaniker/-in", "Orthopädiemechaniker", "Orthopädiemechanikerin", null, 2012)
		}));

		/** Fachklasse Orthopädiemechaniker/-in/Bandagist/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_361_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(469000, 10, "361", "00", true, "O", "OH", null, null, null, "Orthopädiemechaniker/-in/Bandagist/-in", "Orthopädiemechaniker/Bandagist", "Orthopädiemechanikerin/Bandagistin", null, 2017)
		}));

		/** Fachklasse Orthopädieschuhmacher/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_362_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(470000, 10, "362", "00", false, "O", "OH", "GS", null, "A4", "Orthopädieschuhmacher/-in", "Orthopädieschuhmacher", "Orthopädieschuhmacherin", null, null)
		}));

		/** Fachklasse Orthopädietechniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_363_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(471000, 10, "363", "00", true, "T", "CP", null, null, null, "Orthopädietechniker/-in", "Orthopädietechniker", "Orthopädietechnikerin", null, 2012)
		}));

		/** Fachklasse Papiermacher/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_364_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(472000, 10, "364", "00", true, "O", "OH", null, null, null, "Papiermacher/-in", "Papiermacher", "Papiermacherin", null, 2014)
		}));

		/** Fachklasse Papiermacher/-in - Papier-Karton-Pappe (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_364_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(473000, 10, "364", "01", true, "O", "OH", null, null, null, "Papiermacher/-in - Papier-Karton-Pappe", "Papiermacher - Papier-Karton-Pappe", "Papiermacherin - Papier-Karton-Pappe", null, 2012)
		}));

		/** Fachklasse Papiermacher/-in - Zellstoff (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_364_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(474000, 10, "364", "02", true, "O", "OH", null, null, null, "Papiermacher/-in - Zellstoff", "Papiermacher - Zellstoff", "Papiermacherin - Zellstoff", null, 2012)
		}));

		/** Fachklasse Parkettleger/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_365_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(475000, 10, "365", "00", false, "G", "FR", "TN", null, "A3", "Parkettleger/-in", "Parkettleger", "Parkettlegerin", null, null)
		}));

		/** Fachklasse Patentanwaltsfachangestellte/-r */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_366_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(476000, 10, "366", "00", false, "O", "OH", "WV", null, "A3", "Patentanwaltsfachangestellte/-r", "Patentanwaltsfachangestellter", "Patentanwaltsfachangestellte", null, null)
		}));

		/** Fachklasse Pelzveredler/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_367_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(477000, 10, "367", "00", false, "O", "OH", "OZ", null, "A3", "Pelzveredler/-in", "Pelzveredler", "Pelzveredlerin", null, null)
		}));

		/** Fachklasse Pferdewirt/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_368_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(478000, 10, "368", "00", true, "A", "AW", null, null, null, "Pferdewirt/-in", "Pferdewirt", "Pferdewirtin", null, 2013)
		}));

		/** Fachklasse Pferdewirt/-in - (1.u. 2. Ausbildungsjahr) (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_368_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(479000, 10, "368", "01", true, "A", "AW", null, null, null, "Pferdewirt/-in - (1.u. 2. Ausbildungsjahr)", "Pferdewirt - (1.u. 2. Ausbildungsjahr)", "Pferdewirtin - (1.u. 2. Ausbildungsjahr)", null, 2012)
		}));

		/** Fachklasse Pferdewirt/-in - Pferdezucht u. Pferdehaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_368_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(480000, 10, "368", "02", true, "A", "AW", null, null, null, "Pferdewirt/-in - Pferdezucht u. Pferdehaltung", "Pferdewirt - Pferdezucht u. Pferdehaltung", "Pferdewirtin - Pferdezucht u. Pferdehaltung", null, 2012)
		}));

		/** Fachklasse Pferdewirt/-in - Reiten (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_368_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(481000, 10, "368", "03", true, "A", "AW", null, null, null, "Pferdewirt/-in - Reiten", "Pferdewirt - Reiten", "Pferdewirtin - Reiten", null, 2012)
		}));

		/** Fachklasse Pferdewirt/-in - Rennreiten (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_368_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(482000, 10, "368", "04", true, "A", "AW", null, null, null, "Pferdewirt/-in - Rennreiten", "Pferdewirt - Rennreiten", "Pferdewirtin - Rennreiten", null, 2012)
		}));

		/** Fachklasse Pferdewirt/-in - Trabrennfahren (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_368_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(483000, 10, "368", "05", true, "A", "AW", null, null, null, "Pferdewirt/-in - Trabrennfahren", "Pferdewirt - Trabrennfahren", "Pferdewirtin - Trabrennfahren", null, 2012)
		}));

		/** Fachklasse Pferdewirt/-in - Klassische Reitausbildung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_368_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(484000, 10, "368", "06", false, "A", "AW", "AW", null, "A3", "Pferdewirt/-in - Klassische Reitausbildung", "Pferdewirt - Klassische Reitausbildung", "Pferdewirtin - Klassische Reitausbildung", null, null)
		}));

		/** Fachklasse Pferdewirt/-in - Pferdehaltung und Service */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_368_07", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(485000, 10, "368", "07", false, "A", "AW", "AW", null, "A3", "Pferdewirt/-in - Pferdehaltung und Service", "Pferdewirt - Pferdehaltung und Service", "Pferdewirtin - Pferdehaltung und Service", null, null)
		}));

		/** Fachklasse Pferdewirt/-in - Pferderennen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_368_08", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(486000, 10, "368", "08", false, "A", "AW", "AW", null, "A3", "Pferdewirt/-in - Pferderennen", "Pferdewirt - Pferderennen", "Pferdewirtin - Pferderennen", null, null)
		}));

		/** Fachklasse Pferdewirt/-in - Pferdezucht */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_368_09", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(487000, 10, "368", "09", false, "A", "AW", "AW", null, "A3", "Pferdewirt/-in - Pferdezucht", "Pferdewirt - Pferdezucht", "Pferdewirtin - Pferdezucht", null, null)
		}));

		/** Fachklasse Pferdewirt/-in - Spezialreitwesen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_368_10", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(488000, 10, "368", "10", false, "A", "AW", "AW", null, "A3", "Pferdewirt/-in - Spezialreitwesen", "Pferdewirt - Spezialreitwesen", "Pferdewirtin - Spezialreitwesen", null, null)
		}));

		/** Fachklasse Pflegevorschule (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_369_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(489000, 10, "369", "00", true, "S", "GH", null, null, null, "Pflegevorschule", "Pflegevorschule", "Pflegevorschule", null, 2005)
		}));

		/** Fachklasse Pharmakant/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_370_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(490000, 10, "370", "00", false, "T", "CP", "TN", null, "A4", "Pharmakant/-in", "Pharmakant", "Pharmakantin", null, null)
		}));

		/** Fachklasse Pharmazeutisch-kaufmännische/-r Angestellte/-r */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_371_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(491000, 10, "371", "00", false, "O", "OH", "WV", null, "A3", "Pharmazeutisch-kaufmännische/-r Angestellte/-r", "Pharmazeutisch-kaufmännischer Angestellter", "Pharmazeutisch-kaufmännischer Angestellte", null, null)
		}));

		/** Fachklasse Physiklaborant/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_372_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(492000, 10, "372", "00", false, "T", "CP", "TN", null, "A4", "Physiklaborant/-in", "Physiklaborant", "Physiklaborantin", null, null)
		}));

		/** Fachklasse Polster- und Dekorationsnäher/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_373_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(493000, 10, "373", "00", false, "O", "OH", "TN", null, "A2", "Polster- und Dekorationsnäher/-in", "Polster- und Dekorationsnäher", "Polster- und Dekorationsnäherin", null, null)
		}));

		/** Fachklasse Polsterer/Polsterin */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_374_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(494000, 10, "374", "00", false, "G", "FR", "TN", null, "A3", "Polsterer/Polsterin", "Polsterer", "Polsterin", null, null)
		}));

		/** Fachklasse Postverkehrskaufmann/-frau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_375_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(495000, 10, "375", "00", true, "W", "WV", null, null, null, "Postverkehrskaufmann/-frau", "Postverkehrskaufmann", "Postverkehrskauffrau", null, 2014)
		}));

		/** Fachklasse Produktgestalter/-in - Textil */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_376_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(496000, 10, "376", "00", false, "O", "OH", "GT", null, "A3", "Produktgestalter/-in - Textil", "Produktgestalter - Textil", "Produktgestalterin - Textil", null, null)
		}));

		/** Fachklasse Prozessleitelektroniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_377_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(497000, 10, "377", "00", true, "T", "ET", null, null, null, "Prozessleitelektroniker/-in", "Prozessleitelektroniker", "Prozessleitelektronikerin", null, 2014)
		}));

		/** Fachklasse Prozessleitelektroniker/-in f. Meß- u. Regelmechanik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_378_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(498000, 10, "378", "00", true, "T", "CP", null, null, null, "Prozessleitelektroniker/-in f. Meß- u. Regelmechanik", "Prozessleitelektroniker f. Meß- u. Regelmechanik", "Prozessleitelektronikerin f. Meß- u. Regelmechanik", null, 2012)
		}));

		/** Fachklasse Radio- u. Fernsehtechniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_379_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(499000, 10, "379", "00", true, "T", "ET", null, null, null, "Radio- u. Fernsehtechniker/-in", "Radio- u. Fernsehtechniker", "Radio- u. Fernsehtechnikerin", null, 2014)
		}));

		/** Fachklasse Raumausstatter/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_380_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(500000, 10, "380", "00", false, "G", "FR", "GT", null, "A3", "Raumausstatter/-in", "Raumausstatter", "Raumausstatterin", null, null)
		}));

		/** Fachklasse Rechtsanwalts- und Notarfachangestellte/-r */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_381_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(501000, 10, "381", "00", false, "O", "OH", "WV", null, "A3", "Rechtsanwalts- und Notarfachangestellte/-r", "Rechtsanwalts- und Notarfachangestellter", "Rechtsanwalts- und Notarfachangestellte", null, null)
		}));

		/** Fachklasse Rechtsanwalts- u. Notargehilfe/-gehilfin (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_382_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(502000, 10, "382", "00", true, "W", "WV", null, null, null, "Rechtsanwalts- u. Notargehilfe/-gehilfin", "Rechtsanwalts- u. Notargehilfe", "Rechtsanwalts- u. Notargehilfin", null, 2014)
		}));

		/** Fachklasse Rechtsanwaltsfachangestellte/-r */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_383_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(503000, 10, "383", "00", false, "O", "OH", "WV", null, "A3", "Rechtsanwaltsfachangestellte/-r", "Rechtsanwaltsfachangestellter", "Rechtsanwaltsfachangestellte", null, null)
		}));

		/** Fachklasse Rechtsanwaltsgehilfe/-gehilfin (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_384_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(504000, 10, "384", "00", true, "W", "WV", null, null, null, "Rechtsanwaltsgehilfe/-gehilfin", "Rechtsanwaltsgehilfe", "Rechtsanwaltsgehilfin", null, 2014)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 10 (Teil 6)
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex10_Teil6(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Reiseverkehrskaufmann/-frau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_385_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(505000, 10, "385", "00", true, "W", "WV", null, null, null, "Reiseverkehrskaufmann/-frau", "Reiseverkehrskaufmann", "Reiseverkehrskauffrau", null, 2014)
		}));

		/** Fachklasse Reiseverkehrskaufmann/-frau - Touristik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_385_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(506000, 10, "385", "01", true, "W", "WV", null, null, null, "Reiseverkehrskaufmann/-frau - Touristik", "Reiseverkehrskaufmann - Touristik", "Reiseverkehrskauffrau - Touristik", null, 2012)
		}));

		/** Fachklasse Reiseverkehrskaufmann/-frau - Kur- und Fremdenverkehr (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_385_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(507000, 10, "385", "02", true, "W", "WV", null, null, null, "Reiseverkehrskaufmann/-frau - Kur- und Fremdenverkehr", "Reiseverkehrskaufmann - Kur- und Fremdenverkehr", "Reiseverkehrskauffrau - Kur- und Fremdenverkehr", null, 2012)
		}));

		/** Fachklasse Reprograf/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_386_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(508000, 10, "386", "00", true, "T", "DT", null, null, null, "Reprograf/-in", "Reprograf", "Reprografin", null, 2014)
		}));

		/** Fachklasse Reprohersteller/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_387_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(509000, 10, "387", "00", true, "T", "DT", null, null, null, "Reprohersteller/-in", "Reprohersteller", "Reproherstellerin", null, 2014)
		}));

		/** Fachklasse Restaurantfachmann/-frau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_388_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(510000, 10, "388", "00", false, "E", "EH", "EH", null, "A3", "Restaurantfachmann/-frau", "Restaurantfachmann", "Restaurantfachfrau", null, null)
		}));

		/** Fachklasse Revierjäger/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_389_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(511000, 10, "389", "00", false, "A", "AW", "AW", null, "A3", "Revierjäger/-in", "Revierjäger", "Revierjägerin", null, null)
		}));

		/** Fachklasse Revolverdreher/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_390_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(512000, 10, "390", "00", true, "T", "MT", null, null, null, "Revolverdreher/-in", "Revolverdreher", "Revolverdreherin", null, 2015)
		}));

		/** Fachklasse Rohrleitungsbauer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_391_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(513000, 10, "391", "00", false, "T", "BT", "TN", null, "A3", "Rohrleitungsbauer/-in", "Rohrleitungsbauer", "Rohrleitungsbauerin", null, null)
		}));

		/** Fachklasse Rolladen- u. Jalousiebauer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_392_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(514000, 10, "392", "00", true, "O", "OH", null, null, null, "Rolladen- u. Jalousiebauer/-in", "Rolladen- u. Jalousiebauer", "Rolladen- u. Jalousiebauerin", null, 2014)
		}));

		/** Fachklasse Sattler/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_393_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(515000, 10, "393", "00", true, "O", "OH", null, null, null, "Sattler/-in", "Sattler", "Sattlerin", null, 2012)
		}));

		/** Fachklasse Sattler/-in - Fahrzeugsattlerei */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_393_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(516000, 10, "393", "01", false, "O", "OH", "TN", null, "A3", "Sattler/-in - Fahrzeugsattlerei", "Sattler - Fahrzeugsattlerei", "Sattlerin - Fahrzeugsattlerei", null, null)
		}));

		/** Fachklasse Sattler/-in - Feintäschnerei */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_393_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(517000, 10, "393", "02", false, "O", "OH", "TN", null, "A3", "Sattler/-in - Feintäschnerei", "Sattler - Feintäschnerei", "Sattlerin - Feintäschnerei", null, null)
		}));

		/** Fachklasse Sattler/-in - Reitsportsattlerei */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_393_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(518000, 10, "393", "03", false, "O", "OH", "TN", null, "A3", "Sattler/-in - Reitsportsattlerei", "Sattler - Reitsportsattlerei", "Sattlerin - Reitsportsattlerei", null, null)
		}));

		/** Fachklasse Schauwerbegestalter/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_394_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(519000, 10, "394", "00", true, "G", "FR", null, null, null, "Schauwerbegestalter/-in", "Schauwerbegestalter", "Schauwerbegestalterin", null, 2014)
		}));

		/** Fachklasse Schiffbauer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_395_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(520000, 10, "395", "00", true, "T", "MT", null, null, null, "Schiffbauer/-in", "Schiffbauer", "Schiffbauerin", null, 2014)
		}));

		/** Fachklasse Schifffahrtskaufmann/-frau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_396_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(521000, 10, "396", "00", true, "W", "WV", null, null, null, "Schifffahrtskaufmann/-frau", "Schifffahrtskaufmann", "Schifffahrtskauffrau", null, 2012)
		}));

		/** Fachklasse Schifffahrtskaufmann/-frau - Linienfahrt */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_396_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(522000, 10, "396", "01", false, "W", "WV", "WV", null, "A3", "Schifffahrtskaufmann/-frau - Linienfahrt", "Schifffahrtskaufmann - Linienfahrt", "Schifffahrtskauffrau- Linienfahrt", null, null)
		}));

		/** Fachklasse Schifffahrtskaufmann/-frau - Trampfahrt */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_396_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(523000, 10, "396", "02", false, "W", "WV", "WV", null, "A3", "Schifffahrtskaufmann/-frau - Trampfahrt", "Schifffahrtskaufmann - Trampfahrt", "Schifffahrtskauffrau - Trampfahrt", null, null)
		}));

		/** Fachklasse Schiffszimmerer/-zimmerin (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_397_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(524000, 10, "397", "00", true, "T", "HT", null, null, null, "Schiffszimmerer/-zimmerin", "Schiffszimmerer", "Schiffszimmerin", null, 2012)
		}));

		/** Fachklasse Schilder- und Lichtreklamehersteller/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_398_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(525000, 10, "398", "00", false, "G", "FR", "TN", null, "A3", "Schilder- und Lichtreklamehersteller/-in", "Schilder- und Lichtreklamehersteller", "Schilder- und Lichtreklameherstellerin", null, null)
		}));

		/** Fachklasse Schirmmacher/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_399_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(526000, 10, "399", "00", true, "O", "OH", null, null, null, "Schirmmacher/-in", "Schirmmacher", "Schirmmacherin", null, 2012)
		}));

		/** Fachklasse Schleifer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_400_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(527000, 10, "400", "00", true, "T", "MT", null, null, null, "Schleifer/-in", "Schleifer", "Schleiferin", null, 2015)
		}));

		/** Fachklasse Schmelzschweißer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_401_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(528000, 10, "401", "00", true, "T", "MT", null, null, null, "Schmelzschweißer/-in", "Schmelzschweißer", "Schmelzschweißerin", null, 2014)
		}));

		/** Fachklasse Schmucktextilienhersteller/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_402_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(529000, 10, "402", "00", true, "O", "OH", null, null, null, "Schmucktextilienhersteller/-in", "Schmucktextilienhersteller", "Schmucktextilienherstellerin", null, 2014)
		}));

		/** Fachklasse Schneidwerkzeugmechaniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_403_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(530000, 10, "403", "00", true, "T", "MT", "TN", null, "A4", "Schneidwerkzeugmechaniker/-in", "Schneidwerkzeugmechaniker", "Schneidwerkzeugmechanikerin", null, 2021)
		}));

		/** Fachklasse Schornsteinfeger/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_404_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(531000, 10, "404", "00", false, "O", "OH", "TN", null, "A3", "Schornsteinfeger/-in", "Schornsteinfeger", "Schornsteinfegerin", null, null)
		}));

		/** Fachklasse Schriftsetzer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_405_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(532000, 10, "405", "00", true, "T", "DT", null, null, null, "Schriftsetzer/-in", "Schriftsetzer", "Schriftsetzerin", null, 2014)
		}));

		/** Fachklasse Schuh- und Lederwarenstepper/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_406_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(533000, 10, "406", "00", true, "O", "OH", null, null, null, "Schuh- und Lederwarenstepper/-in", "Schuh- und Lederwarenstepper", "Schuh- und Lederwarenstepperin", null, 2014)
		}));

		/** Fachklasse Schuhfertiger/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_407_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(534000, 10, "407", "00", false, "O", "OH", "TN", null, "A3", "Schuhfertiger/-in", "Schuhfertiger", "Schuhfertigerin", null, null)
		}));

		/** Fachklasse Schuhmacher/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_408_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(535000, 10, "408", "00", true, "O", "OH", "TN", null, "A3", "Schuhmacher/-in", "Schuhmacher", "Schuhmacherin", null, 2021)
		}));

		/** Fachklasse Schweißer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_409_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(536000, 10, "409", "00", true, "T", "MT", null, null, null, "Schweißer/-in", "Schweißer", "Schweißerin", null, 2012)
		}));

		/** Fachklasse Schweißwerker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_410_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(537000, 10, "410", "00", true, "T", "MT", null, null, null, "Schweißwerker/-in", "Schweißwerker", "Schweißwerkerin", null, 2012)
		}));

		/** Fachklasse Schwimmmeistergehilfe/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_411_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(538000, 10, "411", "00", true, "S", "GH", null, null, null, "Schwimmmeistergehilfe/-in", "Schwimmmeistergehilfe", "Schwimmmeistergehilfin", null, 2014)
		}));

		/** Fachklasse Seiler/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_412_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(539000, 10, "412", "00", false, "O", "OH", "TN", null, "A3", "Seiler/-in", "Seiler", "Seilerin", null, null)
		}));

		/** Fachklasse Servicekaufmann/-frau im Luftverkehr */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_413_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(540000, 10, "413", "00", false, "O", "OH", "WV", null, "A3", "Servicekaufmann/-frau im Luftverkehr", "Servicekaufmann im Luftverkehr", "Servicekauffrau im Luftverkehr", null, null)
		}));

		/** Fachklasse Siebdrucker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_414_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(541000, 10, "414", "00", true, "T", "DT", null, null, null, "Siebdrucker/-in", "Siebdrucker", "Siebdruckerin", null, 2014)
		}));

		/** Fachklasse Silberschmied/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_415_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(542000, 10, "415", "00", false, "O", "OH", "GT", null, "A4", "Silberschmied/-in", "Silberschmied", "Silberschmiedin", null, null)
		}));

		/** Fachklasse Sozialversicherungsfachangestellte/-r (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_416_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(543000, 10, "416", "00", true, "O", "OH", null, null, null, "Sozialversicherungsfachangestellte/-r", "Sozialversicherungsfachangestellter", "Sozialversicherungsfachangestellter", null, 2012)
		}));

		/** Fachklasse Sozialversicherungsfachangestellte/-r - allgemeine Krankenversicherung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_416_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(544000, 10, "416", "01", false, "O", "OH", "WV", null, "A3", "Sozialversicherungsfachangestellte/-r - allgemeine Krankenversicherung", "Sozialversicherungsfachangestellter - allgemeine Krankenversicherung", "Sozialversicherungsfachangestellte - allgemeine Krankenversicherung", null, null)
		}));

		/** Fachklasse Sozialversicherungsfachangestellte/-r - gesetzliche Unfallversicherung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_416_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(545000, 10, "416", "02", false, "O", "OH", "WV", null, "A3", "Sozialversicherungsfachangestellte/-r - gesetzliche Unfallversicherung", "Sozialversicherungsfachangestellter - gesetzliche Unfallversicherung", "Sozialversicherungsfachangestellte - gesetzliche Unfallversicherung", null, null)
		}));

		/** Fachklasse Sozialversicherungsfachangestellte/-r - gesetzliche Rentenversicherung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_416_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(546000, 10, "416", "03", false, "O", "OH", "WV", null, "A3", "Sozialversicherungsfachangestellte/-r - gesetzliche Rentenversicherung", "Sozialversicherungsfachangestellter - gesetzliche Rentenversicherung", "Sozialversicherungsfachangestellte - gesetzliche Rentenversicherung", null, null)
		}));

		/** Fachklasse Sozialversicherungsfachangestellte/-r - knappschaftliche Sozialversicherung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_416_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(547000, 10, "416", "04", false, "O", "OH", "WV", null, "A3", "Sozialversicherungsfachangestellte/-r - knappschaftliche Sozialversicherung", "Sozialversicherungsfachangestellter - knappschaftliche Sozialversicherung", "Sozialversicherungsfachangestellte - knappschaftliche Sozialversicherung", null, null)
		}));

		/** Fachklasse Sozialversicherungsfachangestellte/-r - landwirtschaftliche Sozialversicherung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_416_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(548000, 10, "416", "05", false, "O", "OH", "WV", null, "A3", "Sozialversicherungsfachangestellte/-r - landwirtschaftliche Sozialversicherung", "Sozialversicherungsfachangestellter - landwirtschaftliche Sozialversicherung", "Sozialversicherungsfachangestellte - landwirtschaftliche Sozialversicherung", null, null)
		}));

		/** Fachklasse Speditionskaufmann/-frau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_417_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(549000, 10, "417", "00", true, "W", "WV", null, null, null, "Speditionskaufmann/-frau", "Speditionskaufmann", "Speditionskauffrau", null, 2014)
		}));

		/** Fachklasse Spezialtiefbauer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_418_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(550000, 10, "418", "00", false, "T", "BT", "TN", null, "A3", "Spezialtiefbauer/-in", "Spezialtiefbauer", "Spezialtiefbauerin", null, null)
		}));

		/** Fachklasse Spielzeughersteller/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_419_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(551000, 10, "419", "00", false, "O", "OH", "OZ", null, "A3", "Spielzeughersteller/-in", "Spielzeughersteller", "Spielzeugherstellerin", null, null)
		}));

		/** Fachklasse Stahlstichpräger/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_420_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(552000, 10, "420", "00", true, "O", "OH", null, null, null, "Stahlstichpräger/-in", "Stahlstichpräger", "Stahlstichprägerin", null, 2012)
		}));

		/** Fachklasse Steindrucker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_421_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(553000, 10, "421", "00", true, "T", "DT", null, null, null, "Steindrucker/-in", "Steindrucker", "Steindruckerin", null, 2012)
		}));

		/** Fachklasse Steinmetz/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_422_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(554000, 10, "422", "00", true, "O", "OH", null, null, null, "Steinmetz/-in", "Steinmetz", "Steinmetzin", null, 2014)
		}));

		/** Fachklasse Steinmetz/-in und Steinbildhauer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_423_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(555000, 10, "423", "00", false, "O", "OH", "GT", null, "A3", "Steinmetz/-in und Steinbildhauer/-in", "Steinmetz und Steinbildhauer", "Steinmetzin und Steinbildhauerin", null, null)
		}));

		/** Fachklasse Stereotypeur/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_424_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(556000, 10, "424", "00", true, "T", "DT", null, null, null, "Stereotypeur/-in", "Stereotypeur", "Stereotypeurin", null, 2014)
		}));

		/** Fachklasse Steuerfachangestellte/-r */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_425_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(557000, 10, "425", "00", false, "O", "OH", "WV", null, "A3", "Steuerfachangestellte/-r", "Steuerfachangestellter", "Steuerfachangestellte", null, null)
		}));

		/** Fachklasse Sticker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_426_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(558000, 10, "426", "00", true, "O", "OH", null, null, null, "Sticker/-in", "Sticker", "Stickerin", null, 2014)
		}));

		/** Fachklasse Stoffprüfer/-in (Chemie) (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_427_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(559000, 10, "427", "00", true, "T", "CP", "TN", null, "A3", "Stoffprüfer/-in (Chemie)", "Stoffprüfer (Chemie)", "Stoffprüferin (Chemie)", null, 2021)
		}));

		/** Fachklasse Straßenbauer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_428_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(560000, 10, "428", "00", false, "T", "BT", "TN", null, "A3", "Straßenbauer/-in", "Straßenbauer", "Straßenbauerin", null, null)
		}));

		/** Fachklasse Straßenwärter/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_429_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(561000, 10, "429", "00", false, "T", "BT", "TN", null, "A3", "Straßenwärter/-in", "Straßenwärter", "Straßenwärterin", null, null)
		}));

		/** Fachklasse Stricker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_430_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(562000, 10, "430", "00", true, "O", "OH", null, null, null, "Stricker/-in", "Stricker", "Strickerin", null, 2014)
		}));

		/** Fachklasse Stuckateur/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_431_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(563000, 10, "431", "00", false, "T", "BT", "TN", null, "A3", "Stuckateur/-in", "Stuckateur", "Stuckateurin", null, null)
		}));

		/** Fachklasse Systemelektroniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_432_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(564000, 10, "432", "00", false, "O", "OH", "TN", null, "A4", "Systemelektroniker/-in", "Systemelektroniker", "Systemelektronikerin", null, null)
		}));

		/** Fachklasse Systemkaufmann/-frau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_433_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(565000, 10, "433", "00", true, "O", "OH", null, null, null, "Systemkaufmann/-frau", "Systemkaufmann", "Systemkauffrau", null, 2012)
		}));

		/** Fachklasse Tankwart/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_434_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(566000, 10, "434", "00", false, "W", "WV", "TN", null, "A3", "Tankwart/-in", "Tankwart", "Tankwartin", null, null)
		}));

		/** Fachklasse Tapetendrucker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_435_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(567000, 10, "435", "00", true, "T", "DT", null, null, null, "Tapetendrucker/-in", "Tapetendrucker", "Tapetendruckerin", null, 2014)
		}));

		/** Fachklasse Tapisserist/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_436_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(568000, 10, "436", "00", true, "O", "OH", null, null, null, "Tapisserist/-in", "Tapisserist", "Tapisseristin", null, 2014)
		}));

		/** Fachklasse Täschner/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_437_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(569000, 10, "437", "00", true, "O", "OH", null, null, null, "Täschner/-in", "Täschner", "Täschnerin", null, 2014)
		}));

		/** Fachklasse Technische/-r Konfektionär/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_438_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(570000, 10, "438", "00", false, "O", "OH", "TN", null, "A3", "Technische/-r Konfektionär/-in", "Technischer Konfektionär", "Technische Konfektionärin", null, null)
		}));

		/** Fachklasse Technische/-r Zeichner/-in - Elektrotechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_439_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(571000, 10, "439", "01", true, "T", "ET", null, null, null, "Technische/-r Zeichner/-in - Elektrotechnik", "Technischer Zeichner - Elektrotechnik", "Technische Zeichnerin - Elektrotechnik", null, 2015)
		}));

		/** Fachklasse Technische/-r Zeichner/-in - Heizungs-, Klima-, Sanitärtechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_439_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(572000, 10, "439", "02", true, "T", "MT", null, null, null, "Technische/-r Zeichner/-in - Heizungs-, Klima-, Sanitärtechnik", "Technischer Zeichner - Heizungs-, Klima-, Sanitärtechnik", "Technische Zeichnerin - Heizungs-, Klima-, Sanitärtechnik", null, 2015)
		}));

		/** Fachklasse Technische/-r Zeichner/-in - Heizungs-, Lüftungs- u. Klimatechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_439_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(573000, 10, "439", "03", true, "O", "OH", null, null, null, "Technische/-r Zeichner/-in - Heizungs-, Lüftungs- u. Klimatechnik", "Technischer Zeichner - Heizungs-, Lüftungs- u. Klimatechnik", "Technische Zeichnerin - Heizungs-, Lüftungs- u. Klimatechnik", null, 2012)
		}));

		/** Fachklasse Technische/-r Zeichner/-in - Holztechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_439_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(574000, 10, "439", "04", true, "T", "HT", null, null, null, "Technische/-r Zeichner/-in - Holztechnik", "Technischer Zeichner - Holztechnik", "Technische Zeichnerin - Holztechnik", null, 2015)
		}));

		/** Fachklasse Technische/-r Zeichner/-in - Maschinen- und Anlagentechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_439_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(575000, 10, "439", "05", true, "T", "MT", null, null, null, "Technische/-r Zeichner/-in - Maschinen- und Anlagentechnik", "Technischer Zeichner - Maschinen- und Anlagentechnik", "Technische Zeichnerin - Maschinen- und Anlagentechnik", null, 2015)
		}));

		/** Fachklasse Technischer Zeichner/-in - Metallgewerbe (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_439_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(576000, 10, "439", "06", true, "O", "OH", null, null, null, "Technischer Zeichner/-in - Metallgewerbe", "Technischer Zeichner - Metallgewerbe", "Technische Zeichnerin - Metallgewerbe", null, 2012)
		}));

		/** Fachklasse Technische/-r Zeichner/-in - Stahl- und Metallbautechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_439_07", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(577000, 10, "439", "07", true, "T", "MT", null, null, null, "Technische/-r Zeichner/-in - Stahl- und Metallbautechnik", "Technischer Zeichner - Stahl- und Metallbautechnik", "Technische Zeichnerin - Stahl- und Metallbautechnik", null, 2015)
		}));

		/** Fachklasse Technischer Zeichner/-in - Stahlbau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_439_08", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(578000, 10, "439", "08", true, "O", "OH", null, null, null, "Technischer Zeichner/-in - Stahlbau", "Technischer Zeichner - Stahlbau", "Technische Zeichnerin - Stahlbau", null, 2012)
		}));

		/** Fachklasse Teilezurichter/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_440_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(579000, 10, "440", "00", true, "O", "OH", null, null, null, "Teilezurichter/-in", "Teilezurichter", "Teilezurichterin", null, 2015)
		}));

		/** Fachklasse Teilzeichner/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_441_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(580000, 10, "441", "00", true, "T", "MT", null, null, null, "Teilzeichner/-in", "Teilzeichner", "Teilzeichnerin", null, 2014)
		}));

		/** Fachklasse Textillaborant/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_442_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(581000, 10, "442", "00", false, "O", "OH", "TN", null, "A4", "Textillaborant/-in", "Textillaborant", "Textillaborantin", null, null)
		}));

		/** Fachklasse Textillaborant/-in - chemisch-technisch (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_442_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(582000, 10, "442", "01", true, "O", "OH", null, null, null, "Textillaborant/-in - chemisch-technisch", "Textillaborant - chemisch-technisch", "Textillaborantin - chemisch-technisch", null, 2014)
		}));

		/** Fachklasse Textillaborant/-in - physikalisch-technisch (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_442_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(583000, 10, "442", "02", true, "O", "OH", null, null, null, "Textillaborant/-in - physikalisch-technisch", "Textillaborant - physikalisch-technisch", "Textillaborantin - physikalisch-technisch", null, 2014)
		}));

		/** Fachklasse Textilmaschinenführer/-in - Maschenindustrie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_443_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(584000, 10, "443", "01", true, "O", "OH", null, null, null, "Textilmaschinenführer/-in - Maschenindustrie", "Textilmaschinenführer - Maschenindustrie", "Textilmaschinenführerin - Maschenindustrie", null, 2014)
		}));

		/** Fachklasse Textilmaschinenführer/-in - Spinnerei (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_443_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(585000, 10, "443", "02", true, "O", "OH", null, null, null, "Textilmaschinenführer/-in - Spinnerei", "Textilmaschinenführer - Spinnerei", "Textilmaschinenführerin - Spinnerei", null, 2014)
		}));

		/** Fachklasse Textilmaschinenführer/-in - Tufting (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_443_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(586000, 10, "443", "03", true, "O", "OH", null, null, null, "Textilmaschinenführer/-in - Tufting", "Textilmaschinenführer - Tufting", "Textilmaschinenführerin - Tufting", null, 2014)
		}));

		/** Fachklasse Textilmaschinenführer/-in - Veredlung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_443_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(587000, 10, "443", "04", true, "O", "OH", null, null, null, "Textilmaschinenführer/-in - Veredlung", "Textilmaschinenführer - Veredlung", "Textilmaschinenführerin - Veredlung", null, 2014)
		}));

		/** Fachklasse Textilmaschinenführer/-in - Vliesstoff (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_443_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(588000, 10, "443", "05", true, "O", "OH", null, null, null, "Textilmaschinenführer/-in - Vliesstoff", "Textilmaschinenführer - Vliesstoff", "Textilmaschinenführerin - Vliesstoff", null, 2014)
		}));

		/** Fachklasse Textilmaschinenführer/-in - Weberei (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_443_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(589000, 10, "443", "06", true, "O", "OH", null, null, null, "Textilmaschinenführer/-in - Weberei", "Textilmaschinenführer - Weberei", "Textilmaschinenführerin - Weberei", null, 2014)
		}));

		/** Fachklasse Textilmechaniker/-in - Bandweberei (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_444_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(590000, 10, "444", "01", true, "O", "OH", null, null, null, "Textilmechaniker/-in - Bandweberei", "Textilmechaniker - Bandweberei", "Textilmechanikerin - Bandweberei", null, 2014)
		}));

		/** Fachklasse Textilmechaniker/-in - Ketten- u. Raschelwirkerei (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_444_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(591000, 10, "444", "02", true, "O", "OH", null, null, null, "Textilmechaniker/-in - Ketten- u. Raschelwirkerei", "Textilmechaniker - Ketten- u. Raschelwirkerei", "Textilmechanikerin - Ketten- u. Raschelwirkerei", null, 2014)
		}));

		/** Fachklasse Textilmechaniker/-in - Maschenindustrie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_444_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(592000, 10, "444", "03", true, "O", "OH", null, null, null, "Textilmechaniker/-in - Maschenindustrie", "Textilmechaniker - Maschenindustrie", "Textilmechanikerin - Maschenindustrie", null, 2014)
		}));

		/** Fachklasse Textilmechaniker/-in - Spinnerei (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_444_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(593000, 10, "444", "04", true, "O", "OH", null, null, null, "Textilmechaniker/-in - Spinnerei", "Textilmechaniker - Spinnerei", "Textilmechanikerin - Spinnerei", null, 2014)
		}));

		/** Fachklasse Textilmechaniker/-in - Strickerei u. Wirkerei (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_444_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(594000, 10, "444", "05", true, "O", "OH", null, null, null, "Textilmechaniker/-in - Strickerei u. Wirkerei", "Textilmechaniker - Strickerei u. Wirkerei", "Textilmechanikerin - Strickerei u. Wirkerei", null, 2012)
		}));

		/** Fachklasse Textilmechaniker/-in - Strumpf- u. Feinstrumpfrundstrickerei (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_444_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(595000, 10, "444", "06", true, "O", "OH", null, null, null, "Textilmechaniker/-in - Strumpf- u. Feinstrumpfrundstrickerei", "Textilmechaniker - Strumpf- u. Feinstrumpfrundstrickerei", "Textilmechanikerin - Strumpf- u. Feinstrumpfrundstrickerei", null, 2014)
		}));

		/** Fachklasse Textilmechaniker/-in - Tufting (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_444_07", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(596000, 10, "444", "07", true, "O", "OH", null, null, null, "Textilmechaniker/-in - Tufting", "Textilmechaniker - Tufting", "Textilmechanikerin - Tufting", null, 2014)
		}));

		/** Fachklasse Textilmechaniker/-in - Vliesstoff (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_444_08", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(597000, 10, "444", "08", true, "O", "OH", null, null, null, "Textilmechaniker/-in - Vliesstoff", "Textilmechaniker - Vliesstoff", "Textilmechanikerin - Vliesstoff", null, 2014)
		}));

		/** Fachklasse Textilmechaniker/-in - Weberei (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_444_09", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(598000, 10, "444", "09", true, "O", "OH", null, null, null, "Textilmechaniker/-in - Weberei", "Textilmechaniker - Weberei", "Textilmechanikerin - Weberei", null, 2014)
		}));

		/** Fachklasse Textilmustergestalter/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_445_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(599000, 10, "445", "00", true, "T", "TB", null, null, null, "Textilmustergestalter/-in", "Textilmustergestalter", "Textilmustergestalterin", null, 2014)
		}));

		/** Fachklasse Textilreiniger/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_446_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(600000, 10, "446", "00", false, "O", "OH", "EH", null, "A3", "Textilreiniger/-in", "Textilreiniger", "Textilreinigerin", null, null)
		}));

		/** Fachklasse Textilstopfer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_447_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(601000, 10, "447", "00", true, "O", "OH", null, null, null, "Textilstopfer/-in", "Textilstopfer", "Textilstopferin", null, 2014)
		}));

		/** Fachklasse Textilveredler/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_448_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(602000, 10, "448", "00", true, "O", "OH", null, null, null, "Textilveredler/-in", "Textilveredler", "Textilveredlerin", null, 2014)
		}));

		/** Fachklasse Textilveredler/-in - Appretur (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_448_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(603000, 10, "448", "01", true, "O", "OH", null, null, null, "Textilveredler/-in - Appretur", "Textilveredler - Appretur", "Textilveredlerin - Appretur", null, 2014)
		}));

		/** Fachklasse Textilveredler/-in - Beschichtung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_448_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(604000, 10, "448", "02", true, "O", "OH", null, null, null, "Textilveredler/-in - Beschichtung", "Textilveredler - Beschichtung", "Textilveredlerin - Beschichtung", null, 2014)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 10 (Teil 7)
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex10_Teil7(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Textilveredler/-in - Druckerei (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_448_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(605000, 10, "448", "03", true, "O", "OH", null, null, null, "Textilveredler/-in - Druckerei", "Textilveredler - Druckerei", "Textilveredlerin - Druckerei", null, 2014)
		}));

		/** Fachklasse Textilveredler/-in - Färberei (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_448_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(606000, 10, "448", "04", true, "O", "OH", null, null, null, "Textilveredler/-in - Färberei", "Textilveredler - Färberei", "Textilveredlerin - Färberei", null, 2014)
		}));

		/** Fachklasse Thermometermacher/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_449_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(607000, 10, "449", "00", true, "O", "OH", null, null, null, "Thermometermacher/-in", "Thermometermacher", "Thermometermacherin", null, 2012)
		}));

		/** Fachklasse Thermometermacher/-in - Thermometerblasen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_449_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(608000, 10, "449", "01", false, "O", "OH", "OZ", null, "A3", "Thermometermacher/-in - Thermometerblasen", "Thermometermacher - Thermometerblasen", "Thermometermacherin - Thermometerblasen", null, null)
		}));

		/** Fachklasse Thermometermacher/-in - Thermometerjustieren */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_449_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(609000, 10, "449", "02", false, "O", "OH", "OZ", null, "A3", "Thermometermacher/-in - Thermometerjustieren", "Thermometermacher - Thermometerjustieren", "Thermometermacherin - Thermometerjustieren", null, null)
		}));

		/** Fachklasse Tiefbaufacharbeiter/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_450_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(610000, 10, "450", "00", true, "T", "BT", null, null, null, "Tiefbaufacharbeiter/-in", "Tiefbaufacharbeiter", "Tiefbaufacharbeiterin", null, 2015)
		}));

		/** Fachklasse Tiefbaufacharbeiter/-in - Gleisbauer */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_450_50", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(611000, 10, "450", "50", false, "T", "BT", "TN", null, "A2", "Tiefbaufacharbeiter/-in - Gleisbauer", "Tiefbaufacharbeiter - Gleisbauer", "Tiefbaufacharbeiterin - Gleisbauer", null, null)
		}));

		/** Fachklasse Tiefbaufacharbeiter/-in - Kanalbauer */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_450_51", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(612000, 10, "450", "51", false, "T", "BT", "TN", null, "A2", "Tiefbaufacharbeiter/-in - Kanalbauer", "Tiefbaufacharbeiter - Kanalbauer", "Tiefbaufacharbeiterin - Kanalbauer", null, null)
		}));

		/** Fachklasse Tiefbaufacharbeiter/-in - Rohrleitungsbauer */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_450_52", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(613000, 10, "450", "52", false, "T", "BT", "TN", null, "A2", "Tiefbaufacharbeiter/-in - Rohrleitungsbauer", "Tiefbaufacharbeiter - Rohrleitungsbauer", "Tiefbaufacharbeiterin - Rohrleitungsbauer", null, null)
		}));

		/** Fachklasse Tiefbaufacharbeiter/-in - Straßenbauer */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_450_53", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(614000, 10, "450", "53", false, "T", "BT", "TN", null, "A2", "Tiefbaufacharbeiter/-in - Straßenbauer", "Tiefbaufacharbeiter - Straßenbauer", "Tiefbaufacharbeiterin - Straßenbauer", null, null)
		}));

		/** Fachklasse Tiefbaufacharbeiter/-in - Brunnen- und Spezialtiefbauarbeiten */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_450_54", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(615000, 10, "450", "54", false, "T", "BT", "TN", null, "A2", "Tiefbaufacharbeiter/-in - Brunnen- und Spezialtiefbauarbeiten", "Tiefbaufacharbeiter - Brunnen- und Spezialtiefbauarbeiten", "Tiefbaufacharbeiterin - Brunnen- und Spezialtiefbauarbeiten", null, null)
		}));

		/** Fachklasse Tierarzthelfer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_451_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(616000, 10, "451", "00", true, "O", "OH", null, null, null, "Tierarzthelfer/-in", "Tierarzthelfer", "Tierarzthelferin", null, 2014)
		}));

		/** Fachklasse Tierpfleger/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_452_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(617000, 10, "452", "00", true, "O", "OH", null, null, null, "Tierpfleger/-in", "Tierpfleger", "Tierpflegerin", null, 2012)
		}));

		/** Fachklasse Tierpfleger/-in - Forschung und Klinik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_452_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(618000, 10, "452", "01", false, "O", "OH", "AW", null, "A3", "Tierpfleger/-in - Forschung und Klinik", "Tierpfleger - Forschung und Klinik", "Tierpflegerin - Forschung und Klinik", null, null)
		}));

		/** Fachklasse Tierpfleger/-in - Tierheim und Tierpension */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_452_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(619000, 10, "452", "02", false, "O", "OH", "AW", null, "A3", "Tierpfleger/-in - Tierheim und Tierpension", "Tierpfleger - Tierheim und Tierpension", "Tierpflegerin - Tierheim und Tierpension", null, null)
		}));

		/** Fachklasse Tierpfleger/-in - Zoo */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_452_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(620000, 10, "452", "03", false, "O", "OH", "AW", null, "A3", "Tierpfleger/-in - Zoo", "Tierpfleger - Zoo", "Tierpflegerin - Zoo", null, null)
		}));

		/** Fachklasse Tierwirt/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_453_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(621000, 10, "453", "00", true, "A", "AW", null, null, null, "Tierwirt/-in", "Tierwirt", "Tierwirtin", null, 2012)
		}));

		/** Fachklasse Tierwirt/-in - Geflügelhaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_453_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(622000, 10, "453", "01", false, "A", "AW", "OZ", null, "A3", "Tierwirt/-in - Geflügelhaltung", "Tierwirt - Geflügelhaltung", "Tierwirtin - Geflügelhaltung", null, null)
		}));

		/** Fachklasse Tierwirt/-in - Imkerei */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_453_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(623000, 10, "453", "02", false, "A", "AW", "OZ", null, "A3", "Tierwirt/-in - Imkerei", "Tierwirt - Imkerei", "Tierwirtin - Imkerei", null, null)
		}));

		/** Fachklasse Tierwirt/-in - Rinderhaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_453_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(624000, 10, "453", "03", false, "A", "AW", "OZ", null, "A3", "Tierwirt/-in - Rinderhaltung", "Tierwirt - Rinderhaltung", "Tierwirtin - Rinderhaltung", null, null)
		}));

		/** Fachklasse Tierwirt/-in - Schäferei */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_453_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(625000, 10, "453", "04", false, "A", "AW", "OZ", null, "A3", "Tierwirt/-in - Schäferei", "Tierwirt - Schäferei", "Tierwirtin - Schäferei", null, null)
		}));

		/** Fachklasse Tierwirt/-in - Schweinehaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_453_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(626000, 10, "453", "05", false, "A", "AW", "OZ", null, "A3", "Tierwirt/-in - Schweinehaltung", "Tierwirt - Schweinehaltung", "Tierwirtin - Schweinehaltung", null, null)
		}));

		/** Fachklasse Tischler/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_454_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(627000, 10, "454", "00", false, "T", "HT", "TN", null, "A3", "Tischler/-in", "Tischler", "Tischlerin", null, null)
		}));

		/** Fachklasse Trockenbaumonteur/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_455_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(628000, 10, "455", "00", false, "T", "BT", "TN", null, "A3", "Trockenbaumonteur/-in", "Trockenbaumonteur", "Trockenbaumonteurin", null, null)
		}));

		/** Fachklasse Uhrmacher/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_456_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(629000, 10, "456", "00", false, "O", "OH", "TN", null, "A3", "Uhrmacher/-in", "Uhrmacher", "Uhrmacherin", null, null)
		}));

		/** Fachklasse Universalhärter/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_457_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(630000, 10, "457", "00", true, "T", "MT", null, null, null, "Universalhärter/-in", "Universalhärter", "Universalhärterin", null, 2014)
		}));

		/** Fachklasse Ver-/Entsorger/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_458_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(631000, 10, "458", "00", true, "T", "CP", null, null, null, "Ver-/Entsorger/-in", "Ver-/Entsorger", "Ver-/Entsorgerin", null, 2014)
		}));

		/** Fachklasse Ver-/Entsorger/-in - Abfall (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_458_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(632000, 10, "458", "01", true, "T", "CP", null, null, null, "Ver-/Entsorger/-in - Abfall", "Ver-/Entsorger - Abfall", "Ver-/Entsorgerin - Abfall", null, 2012)
		}));

		/** Fachklasse Ver-/Entsorger/-in - Abwasser (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_458_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(633000, 10, "458", "02", true, "T", "CP", null, null, null, "Ver-/Entsorger/-in - Abwasser", "Ver-/Entsorger - Abwasser", "Ver-/Entsorgerin - Abwasser", null, 2012)
		}));

		/** Fachklasse Ver-/Entsorger/-in - Wasserversorgung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_458_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(634000, 10, "458", "03", true, "T", "CP", null, null, null, "Ver-/Entsorger/-in - Wasserversorgung", "Ver-/Entsorger - Wasserversorgung", "Ver-/Entsorgerin - Wasserversorgung", null, 2012)
		}));

		/** Fachklasse Verfahrensmechaniker/-in - Hütten-/Halbzeugindustrie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_459_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(635000, 10, "459", "01", true, "O", "OH", null, null, null, "Verfahrensmechaniker/-in - Hütten-/Halbzeugindustrie", "Verfahrensmechaniker - Hütten-/Halbzeugindustrie", "Verfahrensmechanikerin - Hütten-/Halbzeugindustrie", null, 2012)
		}));

		/** Fachklasse Verfahrensmechaniker/-in - Steine- u. Erdenindustrie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_459_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(636000, 10, "459", "02", true, "O", "OH", null, null, null, "Verfahrensmechaniker/-in - Steine- u. Erdenindustrie", "Verfahrensmechaniker - Steine- u. Erdenindustrie", "Verfahrensmechanikerin - Steine- u. Erdenindustrie", null, 2012)
		}));

		/** Fachklasse Verfahrensmechaniker/-in - Glastechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_459_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(637000, 10, "459", "03", false, "O", "OH", "TN", null, "A3", "Verfahrensmechaniker/-in - Glastechnik", "Verfahrensmechaniker - Glastechnik", "Verfahrensmechanikerin - Glastechnik", null, null)
		}));

		/** Fachklasse Verfahrensmechaniker/-in für Beschichtungstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_460_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(638000, 10, "460", "00", false, "O", "OH", "TN", null, "A3", "Verfahrensmechaniker/-in für Beschichtungstechnik", "Verfahrensmechaniker für Beschichtungstechnik", "Verfahrensmechanikerin für Beschichtungstechnik", null, null)
		}));

		/** Fachklasse Verfahrensmechaniker/-in für Kunststoff- und Kautschuktechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_461_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(639000, 10, "461", "00", true, "O", "OH", null, null, null, "Verfahrensmechaniker/-in für Kunststoff- und Kautschuktechnik", "Verfahrensmechaniker für Kunststoff- und Kautschuktechnik", "Verfahrensmechanikerin für Kunststoff- und Kautschuktechnik", null, 2015)
		}));

		/** Fachklasse Verfahrensmechaniker/-in für Kunststoff- und Kautschuktechnik - Formteile */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_461_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(640000, 10, "461", "01", false, "O", "OH", "TN", null, "A3", "Verfahrensmechaniker/-in für Kunststoff- und Kautschuktechnik - Formteile", "Verfahrensmechaniker für Kunststoff- und Kautschuktechnik - Formteile", "Verfahrensmechanikerin für Kunststoff- und Kautschuktechnik - Formteile", null, null)
		}));

		/** Fachklasse Verfahrensmechaniker/-in für Kunststoff- und Kautschuktechnik - Halbzeuge */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_461_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(641000, 10, "461", "02", false, "O", "OH", "TN", null, "A3", "Verfahrensmechaniker/-in für Kunststoff- und Kautschuktechnik - Halbzeuge", "Verfahrensmechaniker für Kunststoff- und Kautschuktechnik - Halbzeuge", "Verfahrensmechanikerin für Kunststoff- und Kautschuktechnik - Halbzeuge", null, null)
		}));

		/** Fachklasse Verfahrensmechaniker/-in für Kunststoff- und Kautschuktechnik - Mehrschichtkautschukteile */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_461_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(642000, 10, "461", "03", false, "O", "OH", "TN", null, "A3", "Verfahrensmechaniker/-in für Kunststoff- und Kautschuktechnik - Mehrschichtkautschukteile", "Verfahrensmechaniker für Kunststoff- und Kautschuktechnik - Mehrschichtkautschukteile", "Verfahrensmechanikerin für Kunststoff- und Kautschuktechnik - Mehrschichtkautschukteile", null, null)
		}));

		/** Fachklasse Verfahrensmechaniker/-in für Kunststoff- und Kautschuktechnik - Compound- und Masterbatchherstellung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_461_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(643000, 10, "461", "04", false, "O", "OH", "TN", null, "A3", "Verfahrensmechaniker/-in für Kunststoff- und Kautschuktechnik - Compound- und Masterbatchherstellung", "Verfahrensmechaniker für Kunststoff- und Kautschuktechnik - Compound- und Masterbatchherstellung", "Verfahrensmechanikerin für Kunststoff- und Kautschuktechnik - Compound- und Masterbatchherstellung", null, null)
		}));

		/** Fachklasse Verfahrensmechaniker/-in für Kunststoff- und Kautschuktechnik - Bauteile */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_461_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(644000, 10, "461", "05", false, "O", "OH", "TN", null, "A3", "Verfahrensmechaniker/-in für Kunststoff- und Kautschuktechnik - Bauteile", "Verfahrensmechaniker für Kunststoff- und Kautschuktechnik - Bauteile", "Verfahrensmechanikerin für Kunststoff- und Kautschuktechnik - Bauteile", null, null)
		}));

		/** Fachklasse Verfahrensmechaniker/-in für Kunststoff- und Kautschuktechnik - Faserverbundtechnologie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_461_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(645000, 10, "461", "06", false, "O", "OH", "TN", null, "A3", "Verfahrensmechaniker/-in für Kunststoff- und Kautschuktechnik - Faserverbundtechnologie", "Verfahrensmechaniker für Kunststoff- und Kautschuktechnik - Faserverbundtechnologie", "Verfahrensmechanikerin für Kunststoff- und Kautschuktechnik - Faserverbundtechnologie", null, null)
		}));

		/** Fachklasse Verfahrensmechaniker/-in für Kunststoff- und Kautschuktechnik - Kunststofffenster */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_461_07", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(646000, 10, "461", "07", false, "O", "OH", "TN", null, "A3", "Verfahrensmechaniker/-in für Kunststoff- und Kautschuktechnik - Kunststofffenster", "Verfahrensmechaniker für Kunststoff- und Kautschuktechnik - Kunststofffenster", "Verfahrensmechanikerin für Kunststoff- und Kautschuktechnik - Kunststofffenster", null, null)
		}));

		/** Fachklasse Vergolder/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_462_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(647000, 10, "462", "00", false, "G", "FR", "OZ", null, "A3", "Vergolder/-in", "Vergolder", "Vergolderin", null, null)
		}));

		/** Fachklasse Verkäufer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_463_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(648000, 10, "463", "00", false, "W", "WV", "WV", null, "A2", "Verkäufer/-in", "Verkäufer", "Verkäuferin", null, null)
		}));

		/** Fachklasse Verlagskaufmann/-frau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_464_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(649000, 10, "464", "00", true, "O", "OH", null, null, null, "Verlagskaufmann/-frau", "Verlagskaufmann", "Verlagskauffrau", null, 2014)
		}));

		/** Fachklasse Vermessungstechniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_465_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(650000, 10, "465", "00", true, "O", "OH", null, null, null, "Vermessungstechniker/-in", "Vermessungstechniker", "Vermessungstechnikerin", null, 2013)
		}));

		/** Fachklasse Vermessungstechniker/-in - Bergvermessung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_465_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(651000, 10, "465", "01", false, "O", "OH", "TN", null, "A3", "Vermessungstechniker/-in - Bergvermessung", "Vermessungstechniker - Bergvermessung", "Vermessungstechnikerin - Bergvermessung", null, null)
		}));

		/** Fachklasse Vermessungstechniker/-in - Vermessung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_465_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(652000, 10, "465", "02", false, "O", "OH", "TN", null, "A3", "Vermessungstechniker/-in - Vermessung", "Vermessungstechniker - Vermessung", "Vermessungstechnikerin - Vermessung", null, null)
		}));

		/** Fachklasse Verpackungsmittelmechaniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_466_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(653000, 10, "466", "00", true, "O", "OH", null, null, null, "Verpackungsmittelmechaniker/-in", "Verpackungsmittelmechaniker", "Verpackungsmittelmechanikerin", null, 2014)
		}));

		/** Fachklasse Versicherungskaufmann/-frau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_467_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(654000, 10, "467", "00", true, "W", "WV", null, null, null, "Versicherungskaufmann/-frau", "Versicherungskaufmann", "Versicherungskauffrau", null, 2014)
		}));

		/** Fachklasse Verwaltungsfachangestellte/-r - Allgemeine Verwaltung des Bundes */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_468_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(655000, 10, "468", "01", false, "W", "WV", "WV", null, "A3", "Verwaltungsfachangestellte/-r - Allgemeine Verwaltung des Bundes", "Verwaltungsfachangestellter - Allgemeine Verwaltung des Bundes", "Verwaltungsfachangestellte - Allgemeine Verwaltung des Bundes", null, null)
		}));

		/** Fachklasse Verwaltungsfachangestellte/-r - Allgemeine Verwaltung des Landes NRW */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_468_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(656000, 10, "468", "02", false, "W", "WV", "WV", null, "A3", "Verwaltungsfachangestellte/-r - Allgemeine Verwaltung des Landes NRW", "Verwaltungsfachangestellter - Allgemeine Verwaltung des Landes NRW", "Verwaltungsfachangestellte - Allgemeine Verwaltung des Landes NRW", null, null)
		}));

		/** Fachklasse Verwaltungsfachangestellte/-r - Bundesverkehrsverwaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_468_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(657000, 10, "468", "03", true, "W", "WV", "WV", null, "A3", "Verwaltungsfachangestellte/-r - Bundesverkehrsverwaltung", "Verwaltungsfachangestellter - Bundesverkehrsverwaltung", "Verwaltungsfachangestellte - Bundesverkehrsverwaltung", null, 2021)
		}));

		/** Fachklasse Verwaltungsfachangestellte/-r - Bundeswehrverwaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_468_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(658000, 10, "468", "04", true, "W", "WV", "WV", null, "A3", "Verwaltungsfachangestellte/-r - Bundeswehrverwaltung", "Verwaltungsfachangestellter - Bundeswehrverwaltung", "Verwaltungsfachangestellte - Bundeswehrverwaltung", null, 2021)
		}));

		/** Fachklasse Verwaltungsfachangestellte/-r - Gliederkirchen der Evangelischen Kirchen in Deutschland öD */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_468_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(659000, 10, "468", "05", false, "W", "WV", "WV", null, "A3", "Verwaltungsfachangestellte/-r - Gliederkirchen der Evangelischen Kirchen in Deutschland öD", "Verwaltungsfachangestellter - Gliederkirchen der Evangelischen Kirchen in Deutschland öD", "Verwaltungsfachangestellte - Gliederkirchen der Evangelischen Kirchen in Deutschland öD", null, null)
		}));

		/** Fachklasse Verwaltungsfachangestellte/-r - Handwerksorganisation/IHK */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_468_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(660000, 10, "468", "06", false, "W", "WV", "WV", null, "A3", "Verwaltungsfachangestellte/-r - Handwerksorganisation/IHK", "Verwaltungsfachangestellter - Handwerksorganisation/IHK", "Verwaltungsfachangestellte - Handwerksorganisation/IHK", null, null)
		}));

		/** Fachklasse Verwaltungsfachangestellte/-r - Kommunalverwaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_468_07", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(661000, 10, "468", "07", false, "W", "WV", "WV", null, "A3", "Verwaltungsfachangestellte/-r - Kommunalverwaltung", "Verwaltungsfachangestellter - Kommunalverwaltung", "Verwaltungsfachangestellte - Kommunalverwaltung", null, null)
		}));

		/** Fachklasse Verwaltungsfachangestellte/-r - Römisch-katholische Kirche (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_468_08", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(662000, 10, "468", "08", true, "W", "WV", "WV", null, "A3", "Verwaltungsfachangestellte/-r - Römisch-katholische Kirche", "Verwaltungsfachangestellter - Römisch-katholische Kirche", "Verwaltungsfachangestellte - Römisch-katholische Kirche", null, 2021)
		}));

		/** Fachklasse Verwaltungsfachangestellte/-r - Versorgungsverwaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_468_09", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(663000, 10, "468", "09", true, "W", "WV", "WV", null, "A3", "Verwaltungsfachangestellte/-r - Versorgungsverwaltung", "Verwaltungsfachangestellter - Versorgungsverwaltung", "Verwaltungsfachangestellte - Versorgungsverwaltung", null, 2021)
		}));

		/** Fachklasse Vulkaniseur/-in u. Reifenmechaniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_469_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(664000, 10, "469", "00", true, "T", "MT", null, null, null, "Vulkaniseur/-in u. Reifenmechaniker/-in", "Vulkaniseur u. Reifenmechaniker", "Vulkaniseurin u. Reifenmechanikerin", null, 2014)
		}));

		/** Fachklasse Wachszieher/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_470_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(665000, 10, "470", "00", true, "O", "OH", null, null, null, "Wachszieher/-in", "Wachszieher", "Wachszieherin", null, 2012)
		}));

		/** Fachklasse Wachszieher/-in - Kerzenherstellung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_470_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(666000, 10, "470", "01", true, "O", "OH", "OZ", null, "A3", "Wachszieher/-in - Kerzenherstellung", "Wachszieher - Kerzenherstellung", "Wachszieherin - Kerzenherstellung", null, 2019)
		}));

		/** Fachklasse Wachszieher/-in - Wachsbildnerei (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_470_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(667000, 10, "470", "02", true, "O", "OH", "OZ", null, "A3", "Wachszieher/-in - Wachsbildnerei", "Wachszieher - Wachsbildnerei", "Wachszieherin - Wachsbildnerei", null, 2019)
		}));

		/** Fachklasse Wagner/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_471_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(668000, 10, "471", "00", true, "T", "HT", null, null, null, "Wagner/-in", "Wagner", "Wagnerin", null, 2012)
		}));

		/** Fachklasse Wärme-, Kälte-, Schallschutzisolierer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_472_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(669000, 10, "472", "00", false, "T", "BT", "TN", null, "A3", "Wärme-, Kälte-, Schallschutzisolierer/-in", "Wärme-, Kälte-, Schallschutzisolierer", "Wärme-, Kälte-, Schallschutzisoliererin", null, null)
		}));

		/** Fachklasse Wärmestellengehilfe/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_473_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(670000, 10, "473", "00", true, "T", "ET", null, null, null, "Wärmestellengehilfe/-in", "Wärmestellengehilfe", "Wärmestellengehilfin", null, 2012)
		}));

		/** Fachklasse Wäscheschneider/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_474_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(671000, 10, "474", "00", true, "T", "TB", null, null, null, "Wäscheschneider/-in", "Wäscheschneider", "Wäscheschneiderin", null, 2014)
		}));

		/** Fachklasse Wasserbauwerker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_475_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(672000, 10, "475", "00", true, "T", "BT", null, null, null, "Wasserbauwerker/-in", "Wasserbauwerker", "Wasserbauwerkerin", null, 2012)
		}));

		/** Fachklasse Weber/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_476_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(673000, 10, "476", "00", true, "O", "OH", null, null, null, "Weber/-in", "Weber", "Weberin", null, 2014)
		}));

		/** Fachklasse Weinküfer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_477_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(674000, 10, "477", "00", true, "O", "OH", null, null, null, "Weinküfer/-in", "Weinküfer", "Weinküferin", null, 2016)
		}));

		/** Fachklasse Werbekaufmann/-frau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_478_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(675000, 10, "478", "00", true, "W", "WV", null, null, null, "Werbekaufmann/-frau", "Werbekaufmann", "Werbekauffrau", null, 2014)
		}));

		/** Fachklasse Werbevorlagenhersteller/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_479_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(676000, 10, "479", "00", true, "T", "DT", null, null, null, "Werbevorlagenhersteller/-in", "Werbevorlagenhersteller", "Werbevorlagenherstellerin", null, 2014)
		}));

		/** Fachklasse Werkstoffprüfer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_480_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(677000, 10, "480", "00", true, "T", "CP", null, null, null, "Werkstoffprüfer/-in", "Werkstoffprüfer", "Werkstoffprüferin", null, 2017)
		}));

		/** Fachklasse Werkstoffprüfer/-in - Metalltechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_480_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(678000, 10, "480", "01", false, "T", "CP", "TN", null, "A4", "Werkstoffprüfer/-in - Metalltechnik", "Werkstoffprüfer - Metalltechnik", "Werkstoffprüferin - Metalltechnik", null, null)
		}));

		/** Fachklasse Werkstoffprüfer/-in - Kunststofftechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_480_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(679000, 10, "480", "02", false, "T", "CP", "TN", null, "A4", "Werkstoffprüfer/-in - Kunststofftechnik", "Werkstoffprüfer - Kunststofftechnik", "Werkstoffprüferin - Kunststofftechnik", null, null)
		}));

		/** Fachklasse Werkstoffprüfer/-in - Wärmebehandlungstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_480_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(680000, 10, "480", "03", false, "T", "CP", "TN", null, "A4", "Werkstoffprüfer/-in - Wärmebehandlungstechnik", "Werkstoffprüfer - Wärmebehandlungstechnik", "Werkstoffprüferin - Wärmebehandlungstechnik", null, null)
		}));

		/** Fachklasse Werkstoffprüfer/-in - Systemtechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_480_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(681000, 10, "480", "04", false, "T", "CP", "TN", null, "A4", "Werkstoffprüfer/-in - Systemtechnik", "Werkstoffprüfer - Systemtechnik", "Werkstoffprüferin - Systemtechnik", null, null)
		}));

		/** Fachklasse Werkzeugmacher/-in - Formenbau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_481_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(682000, 10, "481", "01", true, "T", "MT", null, null, null, "Werkzeugmacher/-in - Formenbau", "Werkzeugmacher - Formenbau", "Werkzeugmacherin - Formenbau", null, 2012)
		}));

		/** Fachklasse Werkzeugmacher/-in - Stanz- u. Vorrichtungsbau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_481_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(683000, 10, "481", "02", true, "T", "MT", null, null, null, "Werkzeugmacher/-in - Stanz- u. Vorrichtungsbau", "Werkzeugmacher - Stanz- u. Vorrichtungsbau", "Werkzeugmacherin - Stanz- u. Vorrichtungsbau", null, 2012)
		}));

		/** Fachklasse Werkzeugmechaniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_482_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(684000, 10, "482", "00", false, "T", "MT", "TN", null, "A4", "Werkzeugmechaniker/-in", "Werkzeugmechaniker", "Werkzeugmechanikerin", null, null)
		}));

		/** Fachklasse Werkzeugmechaniker/-in - Formentechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_482_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(685000, 10, "482", "01", true, "T", "MT", null, null, null, "Werkzeugmechaniker/-in - Formentechnik", "Werkzeugmechaniker - Formentechnik", "Werkzeugmechanikerin - Formentechnik", null, 2014)
		}));

		/** Fachklasse Werkzeugmechaniker/-in - Instrumententechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_482_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(686000, 10, "482", "02", true, "T", "MT", null, null, null, "Werkzeugmechaniker/-in - Instrumententechnik", "Werkzeugmechaniker - Instrumententechnik", "Werkzeugmechanikerin - Instrumententechnik", null, 2014)
		}));

		/** Fachklasse Werkzeugmechaniker/-in - Stanz- u. Umformtechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_482_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(687000, 10, "482", "03", true, "T", "MT", null, null, null, "Werkzeugmechaniker/-in - Stanz- u. Umformtechnik", "Werkzeugmechaniker - Stanz- u. Umformtechnik", "Werkzeugmechanikerin - Stanz- u. Umformtechnik", null, 2014)
		}));

		/** Fachklasse Winzer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_483_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(688000, 10, "483", "00", false, "A", "AW", "OZ", null, "A3", "Winzer/-in", "Winzer", "Winzerin", null, null)
		}));

		/** Fachklasse Zahnarzthelfer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_484_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(689000, 10, "484", "00", true, "O", "OH", null, null, null, "Zahnarzthelfer/-in", "Zahnarzthelfer", "Zahnarzthelferin", null, 2014)
		}));

		/** Fachklasse Zahntechniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_485_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(690000, 10, "485", "00", false, "O", "OH", "GS", null, "A4", "Zahntechniker/-in", "Zahntechniker", "Zahntechnikerin", null, null)
		}));

		/** Fachklasse Zentralheizungs- u. Lüftungsbauer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_486_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(691000, 10, "486", "00", true, "T", "MT", null, null, null, "Zentralheizungs- u. Lüftungsbauer/-in", "Zentralheizungs- u. Lüftungsbauer", "Zentralheizungs- u. Lüftungsbauerin", null, 2014)
		}));

		/** Fachklasse Zerspanungsmechaniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_487_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(692000, 10, "487", "00", false, "T", "MT", "TN", null, "A4", "Zerspanungsmechaniker/-in", "Zerspanungsmechaniker", "Zerspanungsmechanikerin", null, null)
		}));

		/** Fachklasse Zerspanungsmechaniker/-in - Automaten-Drehtechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_487_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(693000, 10, "487", "01", true, "T", "MT", null, null, null, "Zerspanungsmechaniker/-in - Automaten-Drehtechnik", "Zerspanungsmechaniker - Automaten-Drehtechnik", "Zerspanungsmechanikerin - Automaten-Drehtechnik", null, 2014)
		}));

		/** Fachklasse Zerspanungsmechaniker/-in - Drehtechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_487_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(694000, 10, "487", "02", true, "T", "MT", null, null, null, "Zerspanungsmechaniker/-in - Drehtechnik", "Zerspanungsmechaniker - Drehtechnik", "Zerspanungsmechanikerin - Drehtechnik", null, 2014)
		}));

		/** Fachklasse Zerspanungsmechaniker/-in - Frästechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_487_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(695000, 10, "487", "03", true, "T", "MT", null, null, null, "Zerspanungsmechaniker/-in - Frästechnik", "Zerspanungsmechaniker - Frästechnik", "Zerspanungsmechanikerin - Frästechnik", null, 2014)
		}));

		/** Fachklasse Zerspanungsmechaniker/-in - Schleiftechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_487_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(696000, 10, "487", "04", true, "T", "MT", null, null, null, "Zerspanungsmechaniker/-in - Schleiftechnik", "Zerspanungsmechaniker - Schleiftechnik", "Zerspanungsmechanikerin - Schleiftechnik", null, 2014)
		}));

		/** Fachklasse Zimmerer/Zimmerin */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_488_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(697000, 10, "488", "00", false, "T", "BT", "TN", null, "A3", "Zimmerer/Zimmerin", "Zimmerer", "Zimmerin", null, null)
		}));

		/** Fachklasse Zinngießer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_489_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(698000, 10, "489", "00", true, "T", "MT", null, null, null, "Zinngießer/-in", "Zinngießer", "Zinngießerin", null, 2014)
		}));

		/** Fachklasse Ziseleur/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_490_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(699000, 10, "490", "00", true, "T", "MT", null, null, null, "Ziseleur/-in", "Ziseleur", "Ziseleurin", null, 2012)
		}));

		/** Fachklasse Zupfinstrumentenmacher/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_491_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(700000, 10, "491", "00", true, "O", "OH", null, null, null, "Zupfinstrumentenmacher/-in", "Zupfinstrumentenmacher", "Zupfinstrumentenmacherin", null, 2017)
		}));

		/** Fachklasse Zupfinstrumentenmacher/-in - Gitarrenbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_491_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(701000, 10, "491", "01", false, "O", "OH", "OZ", null, "A3", "Zupfinstrumentenmacher/-in - Gitarrenbau", "Zupfinstrumentenmacher - Gitarrenbau", "Zupfinstrumentenmacherin - Gitarrenbau", null, null)
		}));

		/** Fachklasse Zupfinstrumentenmacher/-in - Harfenbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_491_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(702000, 10, "491", "02", false, "O", "OH", "OZ", null, "A3", "Zupfinstrumentenmacher/-in - Harfenbau", "Zupfinstrumentenmacher - Harfenbau", "Zupfinstrumentenmacherin - Harfenbau", null, null)
		}));

		/** Fachklasse Zweiradmechaniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_492_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(703000, 10, "492", "00", true, "T", "MT", null, null, null, "Zweiradmechaniker/-in", "Zweiradmechaniker", "Zweiradmechanikerin", null, 2012)
		}));

		/** Fachklasse Zweiradmechaniker/-in - Fahrradtechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_492_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(704000, 10, "492", "01", true, "T", "FT", null, null, null, "Zweiradmechaniker/-in - Fahrradtechnik", "Zweiradmechaniker - Fahrradtechnik", "Zweiradmechanikerin - Zweiradtechnik", null, 2019)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 10 (Teil 8)
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex10_Teil8(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Zweiradmechaniker/-in - Motorradtechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_492_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(705000, 10, "492", "02", true, "T", "FT", null, null, null, "Zweiradmechaniker/-in - Motorradtechnik", "Zweiradmechaniker - Motorradtechnik", "Zweiradmechanikerin - Motorradtechnik", null, 2019)
		}));

		/** Fachklasse Bühnenmaler/-in und -plastiker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_493_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(706000, 10, "493", "00", true, "O", "OH", null, null, null, "Bühnenmaler/-in und -plastiker/-in", "Bühnenmaler und -plastiker", "Bühnenmalerin und -plastikerin", null, 2012)
		}));

		/** Fachklasse Bühnenmaler/-in und -plastiker/-in - Plastik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_493_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(707000, 10, "493", "01", false, "O", "OH", "GT", null, "A3", "Bühnenmaler/-in und -plastiker/-in - Plastik", "Bühnenmaler und -plastiker - Plastik", "Bühnenmalerin und -plastikerin - Plastik", null, null)
		}));

		/** Fachklasse Bühnenmaler/-in und -plastiker/-in - Malerei */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_493_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(708000, 10, "493", "02", false, "O", "OH", "GT", null, "A3", "Bühnenmaler/-in und -plastiker/-in - Malerei", "Bühnenmaler und -plastiker - Malerei", "Bühnenmalerin und -plastikerin - Malerei", null, null)
		}));

		/** Fachklasse Fachkraft für Straßen- und Verkehrstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_494_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(709000, 10, "494", "00", false, "O", "OH", "TN", null, "A3", "Fachkraft für Straßen- und Verkehrstechnik", "Fachkraft für Straßen- und Verkehrstechnik", "Fachkraft für Straßen- und Verkehrstechnik", null, null)
		}));

		/** Fachklasse Fachkraft für Wasserwirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_495_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(710000, 10, "495", "00", false, "O", "OH", "TN", null, "A3", "Fachkraft für Wasserwirtschaft", "Fachkraft für Wasserwirtschaft", "Fachkraft für Wasserwirtschaft", null, null)
		}));

		/** Fachklasse Informationselektroniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_496_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(711000, 10, "496", "00", false, "T", "ET", "TN", null, "A4", "Informationselektroniker/-in", "Informationselektroniker", "Informationselektronikerin", null, null)
		}));

		/** Fachklasse Metallbearbeiter/-in und Fertigungsmechaniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_497_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(712000, 10, "497", "00", true, "T", "MT", null, null, null, "Metallbearbeiter/-in und Fertigungsmechaniker/-in", "Metallbearbeiter und Fertigungsmechaniker", "Metallbearbeiterin und Fertigungsmechanikerin", null, 2012)
		}));

		/** Fachklasse Bodenleger/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_498_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(713000, 10, "498", "00", false, "O", "OH", "TN", null, "A3", "Bodenleger/-in", "Bodenleger", "Bodenlegerin", null, null)
		}));

		/** Fachklasse Fachkraft für Abwassertechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_499_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(714000, 10, "499", "00", false, "O", "OH", "TN", null, "A3", "Fachkraft für Abwassertechnik", "Fachkraft für Abwassertechnik", "Fachkraft für Abwassertechnik", null, null)
		}));

		/** Fachklasse Kaufmann/-frau im Gesundheitswesen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_500_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(715000, 10, "500", "00", false, "O", "OH", "WV", null, "A3", "Kaufmann/-frau im Gesundheitswesen", "Kaufmann im Gesundheitswesen", "Kauffrau im Gesundheitswesen", null, null)
		}));

		/** Fachklasse Sport- und Fitnesskaufmann/-frau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_501_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(716000, 10, "501", "00", false, "O", "OH", "WV", null, "A3", "Sport- und Fitnesskaufmann/-frau", "Sport- und Fitnesskaufmann", "Sport- und Fitnesskauffrau", null, null)
		}));

		/** Fachklasse Veranstaltungskaufmann/-frau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_502_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(717000, 10, "502", "00", false, "O", "OH", "WV", null, "A3", "Veranstaltungskaufmann/-frau", "Veranstaltungskaufmann", "Veranstaltungskauffrau", null, null)
		}));

		/** Fachklasse Zahnmedizinische/-r Fachangestellte/-r */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_503_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(718000, 10, "503", "00", false, "O", "OH", "GE", null, "A3", "Zahnmedizinische/-r Fachangestellte/-r", "Zahnmedizinischer Fachangestellter", "Zahnmedizinische Fachangestellte", null, null)
		}));

		/** Fachklasse Fachkraft für Kreislauf- und Abfallwirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_504_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(719000, 10, "504", "00", false, "O", "OH", "TN", null, "A3", "Fachkraft für Kreislauf- und Abfallwirtschaft", "Fachkraft für Kreislauf- und Abfallwirtschaft", "Fachkraft für Kreislauf- und Abfallwirtschaft", null, null)
		}));

		/** Fachklasse Fachkraft für Rohr-, Kanal- und Industrieservice */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_505_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(720000, 10, "505", "00", false, "O", "OH", "TN", null, "A3", "Fachkraft für Rohr-, Kanal- und Industrieservice", "Fachkraft für Rohr-, Kanal- und Industrieservice", "Fachkraft für Rohr-, Kanal- und Industrieservice", null, null)
		}));

		/** Fachklasse Fachkraft für Schutz und Sicherheit */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_506_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(721000, 10, "506", "00", false, "O", "OH", "WV", null, "A3", "Fachkraft für Schutz und Sicherheit", "Fachkraft für Schutz und Sicherheit", "Fachkraft für Schutz und Sicherheit", null, null)
		}));

		/** Fachklasse Fachkraft für Wasserversorgungstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_507_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(722000, 10, "507", "00", false, "O", "OH", "TN", null, "A3", "Fachkraft für Wasserversorgungstechnik", "Fachkraft für Wasserversorgungstechnik", "Fachkraft für Wasserversorgungstechnik", null, null)
		}));

		/** Fachklasse Fachkraft im Fahrbetrieb */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_508_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(723000, 10, "508", "00", false, "O", "OH", "WV", null, "A3", "Fachkraft im Fahrbetrieb", "Fachkraft im Fahrbetrieb", "Fachkraft im Fahrbetrieb", null, null)
		}));

		/** Fachklasse Feinwerkmechaniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_509_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(724000, 10, "509", "00", false, "O", "OH", "TN", null, "A4", "Feinwerkmechaniker/-in", "Feinwerkmechaniker", "Feinwerkmechanikerin", null, null)
		}));

		/** Fachklasse Feinwerkmechaniker/-in - Feinmechanik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_509_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(725000, 10, "509", "01", true, "O", "OH", null, null, null, "Feinwerkmechaniker/-in - Feinmechanik", "Feinwerkmechaniker - Feinmechanik", "Feinwerkmechanikerin - Feinmechanik", null, 2012)
		}));

		/** Fachklasse Feinwerkmechaniker/-in - Maschinenbau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_509_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(726000, 10, "509", "02", true, "O", "OH", null, null, null, "Feinwerkmechaniker/-in - Maschinenbau", "Feinwerkmechaniker - Maschinenbau", "Feinwerkmechanikerin - Maschinenbau", null, 2012)
		}));

		/** Fachklasse Feinwerkmechaniker/-in - Werkzeugbau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_509_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(727000, 10, "509", "03", true, "O", "OH", null, null, null, "Feinwerkmechaniker/-in - Werkzeugbau", "Feinwerkmechaniker - Werkzeugbau", "Feinwerkmechanikerin - Werkzeugbau", null, 2012)
		}));

		/** Fachklasse Feinwerkmechaniker/-in - Formenbau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_509_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(728000, 10, "509", "04", true, "O", "OH", null, null, null, "Feinwerkmechaniker/-in - Formenbau", "Feinwerkmechaniker - Formenbau", "Feinwerkmechanikerin - Formenbau", null, 2012)
		}));

		/** Fachklasse Maskenbildner/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_510_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(729000, 10, "510", "00", false, "O", "OH", "GT", null, "A3", "Maskenbildner/-in", "Maskenbildner", "Maskenbildnerin", null, null)
		}));

		/** Fachklasse Verfahrensmechaniker/-in für Brillenoptik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_511_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(730000, 10, "511", "00", false, "O", "OH", "TN", null, "A3", "Verfahrensmechaniker/-in für Brillenoptik", "Verfahrensmechaniker für Brillenoptik", "Verfahrensmechanikerin für Brillenoptik", null, null)
		}));

		/** Fachklasse Bestattungsfachkraft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_512_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(731000, 10, "512", "00", false, "O", "OH", "WV", null, "A3", "Bestattungsfachkraft", "Bestattungsfachkraft", "Bestattungsfachkraft", null, null)
		}));

		/** Fachklasse Fahrzeuginnenausstatter/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_513_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(732000, 10, "513", "00", false, "G", "FR", "TN", null, "A3", "Fahrzeuginnenausstatter/-in", "Fahrzeuginnenausstatter", "Fahrzeuginnenausstatterin", null, null)
		}));

		/** Fachklasse Investmentfondskaufmann/-frau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_514_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(733000, 10, "514", "00", false, "O", "OH", "WV", null, "A3", "Investmentfondskaufmann/-frau", "Investmentfondskaufmann", "Investmentfondskauffrau", null, null)
		}));

		/** Fachklasse Kosmetiker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_515_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(734000, 10, "515", "00", false, "O", "OH", "GS", null, "A3", "Kosmetiker/-in", "Kosmetiker", "Kosmetikerin", null, null)
		}));

		/** Fachklasse Kraftfahrzeugmechatroniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_516_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(735000, 10, "516", "00", false, "T", "FT", "TN", null, "A4", "Kraftfahrzeugmechatroniker/-in", "Kraftfahrzeugmechatroniker", "Kraftfahrzeugmechatronikerin", null, null)
		}));

		/** Fachklasse Kraftfahrzeugmechatroniker/-in - Karosserietechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_516_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(736000, 10, "516", "01", false, "T", "FT", "TN", null, "A4", "Kraftfahrzeugmechatroniker/-in - Karosserietechnik", "Kraftfahrzeugmechatroniker - Karosserietechnik", "Kraftfahrzeugmechatronikerin - Karosserietechnik", null, null)
		}));

		/** Fachklasse Kraftfahrzeugmechatroniker/-in - Motorradtechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_516_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(737000, 10, "516", "02", false, "T", "FT", "TN", null, "A4", "Kraftfahrzeugmechatroniker/-in - Motorradtechnik", "Kraftfahrzeugmechatroniker - Motorradtechnik", "Kraftfahrzeugmechatronikerin - Motorradtechnik", null, null)
		}));

		/** Fachklasse Kraftfahrzeugmechatroniker/-in - Nutzfahrzeugtechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_516_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(738000, 10, "516", "03", false, "T", "FT", "TN", null, "A4", "Kraftfahrzeugmechatroniker/-in - Nutzfahrzeugtechnik", "Kraftfahrzeugmechatroniker - Nutzfahrzeugtechnik", "Kraftfahrzeugmechatronikerin - Nutzfahrzeugtechnik", null, null)
		}));

		/** Fachklasse Kraftfahrzeugmechatroniker/-in - Personenkraftwagentechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_516_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(739000, 10, "516", "04", false, "T", "FT", "TN", null, "A4", "Kraftfahrzeugmechatroniker/-in - Personenkraftwagentechnik", "Kraftfahrzeugmechatroniker - Personenkraftwagentechnik", "Kraftfahrzeugmechatronikerin - Personenkraftwagentechnik", null, null)
		}));

		/** Fachklasse Kraftfahrzeugmechatroniker/-in - System- und Hochvolttechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_516_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(740000, 10, "516", "05", false, "T", "FT", "TN", null, "A4", "Kraftfahrzeugmechatroniker/-in - System- und Hochvolttechnik", "Kraftfahrzeugmechatroniker - System- und Hochvolttechnik", "Kraftfahrzeugmechatronikerin - System- und Hochvolttechnik", null, null)
		}));

		/** Fachklasse Systeminformatiker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_517_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(741000, 10, "517", "00", true, "O", "OH", null, null, null, "Systeminformatiker/-in", "Systeminformatiker", "Systeminformatikerin", null, 2017)
		}));

		/** Fachklasse Verfahrensmechaniker/-in in der Hütten- und Halbzeugindustrie - Eisen- und Stahl-Metallurgie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_518_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(742000, 10, "518", "01", true, "O", "OH", "TN", null, "A4", "Verfahrensmechaniker/-in in der Hütten- und Halbzeugindustrie - Eisen- und Stahl-Metallurgie", "Verfahrensmechaniker in der Hütten- und Halbzeugindustrie - Eisen- und Stahl-Metallurgie", "Verfahrensmechanikerin in der Hütten- und Halbzeugindustrie - Eisen- und Stahl-Metallurgie", null, 2021)
		}));

		/** Fachklasse Verfahrensmechaniker/-in in der Hütten- und Halbzeugindustrie - Stahl-Umformung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_518_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(743000, 10, "518", "02", true, "O", "OH", "TN", null, "A4", "Verfahrensmechaniker/-in in der Hütten- und Halbzeugindustrie - Stahl-Umformung", "Verfahrensmechaniker in der Hütten- und Halbzeugindustrie - Stahl-Umformung", "Verfahrensmechanikerin in der Hütten- und Halbzeugindustrie - Stahl-Umformung", null, 2021)
		}));

		/** Fachklasse Verfahrensmechaniker/-in in der Hütten- und Halbzeugindustrie - Nichteisen-Metallurgie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_518_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(744000, 10, "518", "03", true, "O", "OH", "TN", null, "A4", "Verfahrensmechaniker/-in in der Hütten- und Halbzeugindustrie - Nichteisen-Metallurgie", "Verfahrensmechaniker in der Hütten- und Halbzeugindustrie - Nichteisen-Metallurgie", "Verfahrensmechanikerin in der Hütten- und Halbzeugindustrie - Nichteisen-Metallurgie", null, 2021)
		}));

		/** Fachklasse Verfahrensmechaniker/-in in der Hütten- und Halbzeugindustrie - Nichteisenmetall-Umformung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_518_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(745000, 10, "518", "04", true, "O", "OH", "TN", null, "A4", "Verfahrensmechaniker/-in in der Hütten- und Halbzeugindustrie - Nichteisenmetall-Umformung", "Verfahrensmechaniker in der Hütten- und Halbzeugindustrie - Nichteisenmetall-Umformung", "Verfahrensmechanikerin in der Hütten- und Halbzeugindustrie - Nichteisenmetall-Umformung", null, 2021)
		}));

		/** Fachklasse Verfahrensmechaniker/-in in der Steine- und Erdenindustrie - Baustoffe */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_519_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(746000, 10, "519", "01", false, "O", "OH", "TN", null, "A4", "Verfahrensmechaniker/-in in der Steine- und Erdenindustrie - Baustoffe", "Verfahrensmechaniker in der Steine- und Erdenindustrie - Baustoffe", "Verfahrensmechanikerin in der Steine- und Erdenindustrie - Baustoffe", null, null)
		}));

		/** Fachklasse Verfahrensmechaniker/-in in der Steine- und Erdenindustrie - Gipsplatten oder Faserzement */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_519_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(747000, 10, "519", "02", false, "O", "OH", "TN", null, "A4", "Verfahrensmechaniker/-in in der Steine- und Erdenindustrie - Gipsplatten oder Faserzement", "Verfahrensmechaniker in der Steine- und Erdenindustrie - Gipsplatten oder Faserzement", "Verfahrensmechanikerin in der Steine- und Erdenindustrie - Gipsplatten oder Faserzement", null, null)
		}));

		/** Fachklasse Verfahrensmechaniker/-in in der Steine- und Erdenindustrie - Kalksandsteine oder Porenbeton */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_519_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(748000, 10, "519", "03", false, "O", "OH", "TN", null, "A4", "Verfahrensmechaniker/-in in der Steine- und Erdenindustrie - Kalksandsteine oder Porenbeton", "Verfahrensmechaniker in der Steine- und Erdenindustrie - Kalksandsteine oder Porenbeton", "Verfahrensmechanikerin in der Steine- und Erdenindustrie - Kalksandsteine oder Porenbeton", null, null)
		}));

		/** Fachklasse Verfahrensmechaniker/-in in der Steine- und Erdenindustrie - Transportbeton */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_519_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(749000, 10, "519", "04", false, "O", "OH", "TN", null, "A4", "Verfahrensmechaniker/-in in der Steine- und Erdenindustrie - Transportbeton", "Verfahrensmechaniker in der Steine- und Erdenindustrie - Transportbeton", "Verfahrensmechanikerin in der Steine- und Erdenindustrie - Transportbeton", null, null)
		}));

		/** Fachklasse Verfahrensmechaniker/-in in der Steine- und Erdenindustrie - vorgefertigte Betonteile */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_519_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(750000, 10, "519", "05", false, "O", "OH", "TN", null, "A4", "Verfahrensmechaniker/-in in der Steine- und Erdenindustrie - vorgefertigte Betonteile", "Verfahrensmechaniker in der Steine- und Erdenindustrie - vorgefertigte Betonteile", "Verfahrensmechanikerin in der Steine- und Erdenindustrie - vorgefertigte Betonteile", null, null)
		}));

		/** Fachklasse Verfahrensmechaniker/-in in der Steine- und Erdenindustrie - Asphalttechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_519_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(751000, 10, "519", "06", false, "O", "OH", "TN", null, "A4", "Verfahrensmechaniker/-in in der Steine- und Erdenindustrie - Asphalttechnik", "Verfahrensmechaniker in der Steine- und Erdenindustrie - Asphalttechnik", "Verfahrensmechanikerin in der Steine- und Erdenindustrie - Asphalttechnik", null, null)
		}));

		/** Fachklasse Wasserbauer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_520_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(752000, 10, "520", "00", false, "T", "BT", "OZ", null, "A3", "Wasserbauer/-in", "Wasserbauer", "Wasserbauerin", null, null)
		}));

		/** Fachklasse Karosserie- und Fahrzeugbaumechaniker/-in - Karosserieinstandhaltungstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_521_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(753000, 10, "521", "01", false, "T", "MT", "TN", null, "A4", "Karosserie- und Fahrzeugbaumechaniker/-in - Karosserieinstandhaltungstechnik", "Karosserie- und Fahrzeugbaumechaniker - Karosserieinstandhaltungstechnik", "Karosserie- und Fahrzeugbaumechanikerin - Karosserieinstandhaltungstechnik", null, null)
		}));

		/** Fachklasse Karosserie- und Fahrzeugbaumechaniker/-in - Karosseriebautechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_521_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(754000, 10, "521", "02", true, "T", "MT", null, null, null, "Karosserie- und Fahrzeugbaumechaniker/-in - Karosseriebautechnik", "Karosserie- und Fahrzeugbaumechaniker - Karosseriebautechnik", "Karosserie- und Fahrzeugbaumechanikerin - Karosseriebautechnik", null, 2019)
		}));

		/** Fachklasse Karosserie- und Fahrzeugbaumechaniker/-in - Fahrzeugbautechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_521_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(755000, 10, "521", "03", true, "T", "MT", null, null, null, "Karosserie- und Fahrzeugbaumechaniker/-in - Fahrzeugbautechnik", "Karosserie- und Fahrzeugbaumechaniker - Fahrzeugbautechnik", "Karosserie- und Fahrzeugbaumechanikerin - Fahrzeugbautechnik", null, 2019)
		}));

		/** Fachklasse Karosserie- und Fahrzeugbaumechaniker/-in - Karosserie- und Fahrzeugbautechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_521_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(756000, 10, "521", "04", false, "T", "MT", "TN", null, "A4", "Karosserie- und Fahrzeugbaumechaniker/-in - Karosserie- und Fahrzeugbautechnik", "Karosserie- und Fahrzeugbaumechaniker - Karosserie- und Fahrzeugbautechnik", "Karosserie- und Fahrzeugbaumechanikerin - Karosserie- und Fahrzeugbautechnik", null, null)
		}));

		/** Fachklasse Fahrzeuglackierer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_522_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(757000, 10, "522", "00", false, "G", "FR", "TN", null, "A3", "Fahrzeuglackierer/-in", "Fahrzeuglackierer", "Fahrzeuglackiererin", null, null)
		}));

		/** Fachklasse Mechaniker/-in für Karosserieinstandhaltungstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_523_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(758000, 10, "523", "00", true, "T", "FT", null, null, null, "Mechaniker/-in für Karosserieinstandhaltungstechnik", "Mechaniker für Karosserieinstandhaltungstechnik", "Mechanikerin für Karosserieinstandhaltungstechnik", null, 2017)
		}));

		/** Fachklasse Mechaniker/-in für Karosserie- und Fahrzeugbautechnik - Fahrzeugbautechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_523_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(759000, 10, "523", "01", true, "T", "FT", null, null, null, "Mechaniker/-in für Karosserie- und Fahrzeugbautechnik - Fahrzeugbautechnik", "Mechaniker für Karosserie- und Fahrzeugbautechnik - Fahrzeugbautechnik", "Mechanikerin für Karosserie- und Fahrzeugbautechnik - Fahrzeugbautechnik", null, 2012)
		}));

		/** Fachklasse Mechaniker/-in für Karosserie- und Fahrzeugbautechnik - Karosseriebautechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_523_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(760000, 10, "523", "02", true, "T", "FT", null, null, null, "Mechaniker/-in für Karosserie- und Fahrzeugbautechnik - Karosseriebautechnik", "Mechaniker für Karosserie- und Fahrzeugbautechnik - Karosseriebautechnik", "Mechanikerin für Karosserie- und Fahrzeugbautechnik - Karosseriebautechnik", null, 2012)
		}));

		/** Fachklasse Mechaniker/-in für Land- und Baumaschinentechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_524_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(761000, 10, "524", "00", true, "T", "FT", null, null, null, "Mechaniker/-in für Land- und Baumaschinentechnik", "Mechaniker für Land- und Baumaschinentechnik", "Mechanikerin für Land- und Baumaschinentechnik", null, 2019)
		}));

		/** Fachklasse Bauten- und Objektbeschichter/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_525_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(762000, 10, "525", "00", false, "G", "FR", "TN", null, "A2", "Bauten- und Objektbeschichter/-in", "Bauten- und Objektbeschichter", "Bauten- und Objektbeschichterin", null, null)
		}));

		/** Fachklasse Bauwerksmechaniker/-in für Abbruch und Betontrenntechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_526_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(763000, 10, "526", "00", false, "T", "BT", "TN", null, "A3", "Bauwerksmechaniker/-in für Abbruch und Betontrenntechnik", "Bauwerksmechaniker für Abbruch und Betontrenntechnik", "Bauwerksmechanikerin für Abbruch und Betontrenntechnik", null, null)
		}));

		/** Fachklasse Fachkraft für Lagerlogistik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_527_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(764000, 10, "527", "00", false, "O", "OH", "WV", null, "A3", "Fachkraft für Lagerlogistik", "Fachkraft für Lagerlogistik", "Fachkraft für Lagerlogistik", null, null)
		}));

		/** Fachklasse Fachlagerist/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_528_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(765000, 10, "528", "00", false, "O", "OH", "WV", null, "A2", "Fachlagerist/-in", "Fachlagerist", "Fachlageristin", null, null)
		}));

		/** Fachklasse Fahrradmonteur/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_529_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(766000, 10, "529", "00", false, "T", "FT", "TN", null, "A2", "Fahrradmonteur/-in", "Fahrradmonteur", "Fahrradmonteurin", null, null)
		}));

		/** Fachklasse Gestalter/-in für visuelles Marketing */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_530_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(767000, 10, "530", "00", false, "G", "FR", "GT", null, "A3", "Gestalter/-in für visuelles Marketing", "Gestalter für visuelles Marketing", "Gestalterin für visuelles Marketing", null, null)
		}));

		/** Fachklasse Maschinen- und Anlagenführer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_531_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(768000, 10, "531", "00", true, "O", "OH", null, null, null, "Maschinen- und Anlagenführer/-in", "Maschinen- und Anlagenführer", "Maschinen- und Anlagenführerin", null, 2019)
		}));

		/** Fachklasse Maschinen- und Anlagenführer/-in - Druckweiter- und Papierverarbeitung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_531_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(769000, 10, "531", "01", true, "O", "OH", null, null, null, "Maschinen- und Anlagenführer/-in - Druckweiter- und Papierverarbeitung", "Maschinen- und Anlagenführer - Druckweiter- und Papierverarbeitung", "Maschinen- und Anlagenführerin - Druckweiter- und Papierverarbeitung", null, 2012)
		}));

		/** Fachklasse Maschinen- und Anlagenführer/-in - Lebensmitteltechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_531_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(770000, 10, "531", "02", true, "O", "OH", null, null, null, "Maschinen- und Anlagenführer/-in - Lebensmitteltechnik", "Maschinen- und Anlagenführer - Lebensmitteltechnik", "Maschinen- und Anlagenführerin - Lebensmitteltechnik", null, 2012)
		}));

		/** Fachklasse Maschinen- und Anlagenführer/-in - Metalltechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_531_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(771000, 10, "531", "03", true, "O", "OH", null, null, null, "Maschinen- und Anlagenführer/-in - Metalltechnik", "Maschinen- und Anlagenführer - Metalltechnik", "Maschinen- und Anlagenführerin - Metalltechnik", null, 2012)
		}));

		/** Fachklasse Maschinen- und Anlagenführer/-in - Textiltechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_531_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(772000, 10, "531", "04", true, "O", "OH", null, null, null, "Maschinen- und Anlagenführer/-in - Textiltechnik", "Maschinen- und Anlagenführer - Textiltechnik", "Maschinen- und Anlagenführerin - Textiltechnik", null, 2012)
		}));

		/** Fachklasse Maschinen- und Anlagenführer/-in - Textilveredlung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_531_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(773000, 10, "531", "05", true, "O", "OH", null, null, null, "Maschinen- und Anlagenführer/-in - Textilveredlung", "Maschinen- und Anlagenführer - Textilveredlung", "Maschinen- und Anlagenführerin - Textilveredlung", null, 2012)
		}));

		/** Fachklasse Maschinen- und Anlagenführer/-in - Druckweiter- und Papierverarbeitung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_531_50", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(774000, 10, "531", "50", false, "O", "OH", "TN", null, "A2", "Maschinen- und Anlagenführer/-in - Druckweiter- und Papierverarbeitung", "Maschinen- und Anlagenführer - Druckweiter- und Papierverarbeitung", "Maschinen- und Anlagenführerin - Druckweiter- und Papierverarbeitung", null, null)
		}));

		/** Fachklasse Maschinen- und Anlagenführer/-in - Lebensmitteltechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_531_51", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(775000, 10, "531", "51", false, "O", "OH", "TN", null, "A2", "Maschinen- und Anlagenführer/-in - Lebensmitteltechnik", "Maschinen- und Anlagenführer - Lebensmitteltechnik", "Maschinen- und Anlagenführerin - Lebensmitteltechnik", null, null)
		}));

		/** Fachklasse Maschinen- und Anlagenführer/-in - Metall- und Kunststofftechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_531_52", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(776000, 10, "531", "52", false, "O", "OH", "TN", null, "A2", "Maschinen- und Anlagenführer/-in - Metall- und Kunststofftechnik", "Maschinen- und Anlagenführer - Metall- und Kunststofftechnik", "Maschinen- und Anlagenführerin - Metall- und Kunststofftechnik", null, null)
		}));

		/** Fachklasse Maschinen- und Anlagenführer/-in - Textiltechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_531_53", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(777000, 10, "531", "53", false, "O", "OH", "TN", null, "A2", "Maschinen- und Anlagenführer/-in - Textiltechnik", "Maschinen- und Anlagenführer - Textiltechnik", "Maschinen- und Anlagenführerin - Textiltechnik", null, null)
		}));

		/** Fachklasse Maschinen- und Anlagenführer/-in - Textilveredelung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_531_54", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(778000, 10, "531", "54", false, "O", "OH", "TN", null, "A2", "Maschinen- und Anlagenführer/-in - Textilveredelung", "Maschinen- und Anlagenführer - Textilveredelung", "Maschinen- und Anlagenführerin - Textilveredelung", null, null)
		}));

		/** Fachklasse Maßschneider/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_532_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(779000, 10, "532", "00", false, "T", "TB", "TN", null, "A3", "Maßschneider/-in", "Maßschneider", "Maßschneiderin", null, null)
		}));

		/** Fachklasse Mechaniker/-in für Reifen- und Vulkanisationstechnik - Reifen- und Fahrwerktechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_533_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(780000, 10, "533", "01", false, "T", "FT", "TN", null, "A3", "Mechaniker/-in für Reifen- und Vulkanisationstechnik - Reifen- und Fahrwerktechnik", "Mechaniker für Reifen- und Vulkanisationstechnik - Reifen- und Fahrwerktechnik", "Mechanikerin für Reifen- und Vulkanisationstechnik - Reifen- und Fahrwerktechnik", null, null)
		}));

		/** Fachklasse Mechaniker/-in für Reifen- und Vulkanisationstechnik - Vulkanisationstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_533_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(781000, 10, "533", "02", false, "T", "FT", "TN", null, "A3", "Mechaniker/-in für Reifen- und Vulkanisationstechnik - Vulkanisationstechnik", "Mechaniker für Reifen- und Vulkanisationstechnik - Vulkanisationstechnik", "Mechanikerin für Reifen- und Vulkanisationstechnik - Vulkanisationstechnik", null, null)
		}));

		/** Fachklasse Rollladen- und Sonnenschutzmechatroniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_534_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(782000, 10, "534", "00", false, "O", "OH", "TN", null, "A3", "Rollladen- und Sonnenschutzmechatroniker/-in", "Rollladen- und Sonnenschutzmechatroniker", "Rollladen- und Sonnenschutzmechatronikerin", null, null)
		}));

		/** Fachklasse Schädlingsbekämpfer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_535_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(783000, 10, "535", "00", false, "O", "OH", "GE", null, "A3", "Schädlingsbekämpfer/-in", "Schädlingsbekämpfer", "Schädlingsbekämpferin", null, null)
		}));

		/** Fachklasse Kraftfahrzeugservicemechaniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_536_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(784000, 10, "536", "00", true, "T", "FT", null, null, null, "Kraftfahrzeugservicemechaniker/-in", "Kraftfahrzeugservicemechaniker", "Kraftfahrzeugservicemechanikerin", null, 2015)
		}));

		/** Fachklasse Kaufmann/-frau für Spedition und Logistikdienstleistung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_537_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(785000, 10, "537", "00", false, "W", "WV", "WV", null, "A3", "Kaufmann/-frau für Spedition und Logistikdienstleistung", "Kaufmann für Spedition und Logistikdienstleistung", "Kauffrau für Spedition und Logistikdienstleistung", null, null)
		}));

		/** Fachklasse Änderungsschneider/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_538_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(786000, 10, "538", "00", false, "T", "TB", "GT", null, "A2", "Änderungsschneider/-in", "Änderungsschneider", "Änderungsschneiderin", null, null)
		}));

		/** Fachklasse Fachkraft Agrarservice */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_539_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(787000, 10, "539", "00", false, "A", "AW", "AW", null, "A3", "Fachkraft Agrarservice", "Fachkraft Agrarservice", "Fachkraft Agrarservice", null, null)
		}));

		/** Fachklasse Fachkraft für Kurier-, Express- und Postdienstleistungen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_540_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(788000, 10, "540", "00", false, "W", "WV", "WV", null, "A2", "Fachkraft für Kurier-, Express- und Postdienstleistungen", "Fachkraft für Kurier-, Express- und Postdienstleistungen", "Fachkraft für Kurier-, Express- und Postdienstleistungen", null, null)
		}));

		/** Fachklasse Oberflächenbeschichter/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_541_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(789000, 10, "541", "00", false, "T", "CP", "TN", null, "A3", "Oberflächenbeschichter/-in", "Oberflächenbeschichter", "Oberflächenbeschichterin", null, null)
		}));

		/** Fachklasse Kaufmann/-frau für Kurier-, Express- und Postdienstleistungen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_542_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(790000, 10, "542", "00", false, "W", "WV", "WV", null, "A3", "Kaufmann/-frau für Kurier-, Express- und Postdienstleistungen", "Kaufmann für Kurier-, Express- und Postdienstleistungen", "Kauffrau für Kurier-, Express- und Postdienstleistungen", null, null)
		}));

		/** Fachklasse Kaufmann/-frau für Tourismus und Freizeit */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_543_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(791000, 10, "543", "00", false, "W", "WV", "WV", null, "A3", "Kaufmann/-frau für Tourismus und Freizeit", "Kaufmann für Tourismus und Freizeit", "Kauffrau für Tourismus und Freizeit", null, null)
		}));

		/** Fachklasse Medizinische/-r Fachangestellte/-r */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_544_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(792000, 10, "544", "00", false, "O", "OH", "WV", null, "A3", "Medizinische/-r Fachangestellte/-r", "Medizinischer Fachangestellter", "Medizinische Fachangestellte", null, null)
		}));

		/** Fachklasse Papiertechnologe/-technologin */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_545_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(793000, 10, "545", "00", false, "O", "OH", "OZ", null, "A3", "Papiertechnologe/-technologin", "Papiertechnologe", "Papiertechnologin", null, null)
		}));

		/** Fachklasse Papiertechnologe/-technologin - Papier- Karton und Pappe (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_545_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(794000, 10, "545", "01", true, "O", "OH", null, null, null, "Papiertechnologe/-technologin - Papier- Karton und Pappe", "Papiertechnologe - Papier- Karton und Pappe", "Papiertechnologin - Papier- Karton und Pappe", null, 2014)
		}));

		/** Fachklasse Papiertechnologe/-technologin - Zellstoff (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_545_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(795000, 10, "545", "02", true, "O", "OH", null, null, null, "Papiertechnologe/-technologin - Zellstoff", "Papiertechnologe - Zellstoff", "Papiertechnologin - Zellstoff", null, 2014)
		}));

		/** Fachklasse Produktionsfachkraft Chemie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_546_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(796000, 10, "546", "00", false, "T", "CP", "TN", null, "A2", "Produktionsfachkraft Chemie", "Produktionsfachkraft Chemie", "Produktionsfachkraft Chemie", null, null)
		}));

		/** Fachklasse Produktionsmechaniker/-in Textil */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_547_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(797000, 10, "547", "00", false, "O", "OH", "TN", null, "A3", "Produktionsmechaniker/-in Textil", "Produktionsmechaniker Textil", "Produktionsmechanikerin Textil", null, null)
		}));

		/** Fachklasse Produktveredler/-in Textil */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_548_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(798000, 10, "548", "00", false, "O", "OH", "TN", null, "A3", "Produktveredler/-in Textil", "Produktveredler Textil", "Produktveredlerin Textil", null, null)
		}));

		/** Fachklasse Servicefahrer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_549_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(799000, 10, "549", "00", false, "O", "OH", "WV", null, "A2", "Servicefahrer/-in", "Servicefahrer", "Servicefahrerin", null, null)
		}));

		/** Fachklasse Technische/-r Produktdesigner/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_550_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(800000, 10, "550", "00", true, "O", "OH", null, null, null, "Technische/-r Produktdesigner/-in", "Technischer Produktdesigner", "Technische Produktdesignerin", null, 2013)
		}));

		/** Fachklasse Technische/-r Produktdesigner/-in - Produktgestaltung und -konstruktion */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_550_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(801000, 10, "550", "01", false, "O", "OH", "TN", null, "A4", "Technische/-r Produktdesigner/-in - Produktgestaltung und -konstruktion", "Technischer Produktdesigner - Produktgestaltung und -konstruktion", "Technische Produktdesignerin - Produktgestaltung und -konstruktion", null, null)
		}));

		/** Fachklasse Technische/-r Produktdesigner/-in - Maschinen- und Anlagenkonstruktion */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_550_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(802000, 10, "550", "02", false, "O", "OH", "TN", null, "A4", "Technische/-r Produktdesigner/-in - Maschinen- und Anlagenkonstruktion", "Technischer Produktdesigner - Maschinen- und Anlagenkonstruktion", "Technische Produktdesignerin - Maschinen- und Anlagenkonstruktion", null, null)
		}));

		/** Fachklasse Fachangestellte/-r für Markt- und Sozialforschung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_551_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(803000, 10, "551", "00", false, "W", "WV", "WV", null, "A3", "Fachangestellte/-r für Markt- und Sozialforschung", "Fachangestellter für Markt- und Sozialforschung", "Fachangestellte für Markt- und Sozialforschung", null, null)
		}));

		/** Fachklasse Fachkraft für Möbel-, Küchen- und Umzugsservice */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_552_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(804000, 10, "552", "00", false, "O", "OH", "TN", null, "A3", "Fachkraft für Möbel-, Küchen- und Umzugsservice", "Fachkraft für Möbel-, Küchen- und Umzugsservice", "Fachkraft für Möbel-, Küchen- und Umzugsservice", null, null)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 10 (Teil 9)
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex10_Teil9(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Fachverkäufer/-in im Lebensmittelhandwerk (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_553_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(805000, 10, "553", "00", true, "E", "EH", null, null, null, "Fachverkäufer/-in im Lebensmittelhandwerk", "Fachverkäufer im Lebensmittelhandwerk", "Fachverkäuferin im Lebensmittelhandwerk", null, 2012)
		}));

		/** Fachklasse Fachverkäufer/-in im Lebensmittelhandwerk - Bäckerei (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_553_50", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(806000, 10, "553", "50", true, "E", "EH", "EH", null, "A3", "Fachverkäufer/-in im Lebensmittelhandwerk - Bäckerei", "Fachverkäufer im Lebensmittelhandwerk - Bäckerei", "Fachverkäuferin im Lebensmittelhandwerk - Bäckerei", null, 2021)
		}));

		/** Fachklasse Fachverkäufer/-in im Lebensmittelhandwerk - Fleischerei */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_553_51", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(807000, 10, "553", "51", false, "E", "EH", "EH", null, "A3", "Fachverkäufer/-in im Lebensmittelhandwerk - Fleischerei", "Fachverkäufer im Lebensmittelhandwerk - Fleischerei", "Fachverkäuferin im Lebensmittelhandwerk - Fleischerei", null, null)
		}));

		/** Fachklasse Fachverkäufer/-in im Lebensmittelhandwerk - Konditorei (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_553_52", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(808000, 10, "553", "52", true, "E", "EH", "EH", null, "A3", "Fachverkäufer/-in im Lebensmittelhandwerk - Konditorei", "Fachverkäufer im Lebensmittelhandwerk - Konditorei", "Fachverkäuferin im Lebensmittelhandwerk - Konditorei", null, 2021)
		}));

		/** Fachklasse Fachverkäufer/-in im Lebensmittelhandwerk - Bäckerei und Konditorei */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_553_53", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(809000, 10, "553", "53", false, null, null, "EH", null, "A3", "Fachverkäufer/-in im Lebensmittelhandwerk - Bäckerei und Konditorei", "Fachverkäufer im Lebensmittelhandwerk - Bäckerei und Konditorei", "Fachverkäuferin im Lebensmittelhandwerk - Bäckerei und Konditorei", null, null)
		}));

		/** Fachklasse Immobilienkaufmann/-frau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_554_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(810000, 10, "554", "00", false, "W", "WV", "WV", null, "A3", "Immobilienkaufmann/-frau", "Immobilienkaufmann", "Immobilienkauffrau", null, null)
		}));

		/** Fachklasse Kaufmann/-frau für Dialogmarketing */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_555_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(811000, 10, "555", "00", false, "W", "WV", "WV", null, "A3", "Kaufmann/-frau für Dialogmarketing", "Kaufmann für Dialogmarketing", "Kauffrau für Dialogmarketing", null, null)
		}));

		/** Fachklasse Kaufmann/-frau für Versicherungen und Finanzen - Versicherung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_556_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(812000, 10, "556", "01", false, "W", "WV", "WV", null, "A3", "Kaufmann/-frau für Versicherungen und Finanzen - Versicherung", "Kaufmann für Versicherungen und Finanzen - Versicherung", "Kauffrau für Versicherungen und Finanzen - Versicherung", null, null)
		}));

		/** Fachklasse Kaufmann/-frau für Versicherungen und Finanzen - Finanzberatung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_556_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(813000, 10, "556", "02", false, "W", "WV", "WV", null, "A3", "Kaufmann/-frau für Versicherungen und Finanzen - Finanzberatung", "Kaufmann für Versicherungen und Finanzen - Finanzberatung", "Kauffrau für Versicherungen und Finanzen - Finanzberatung", null, null)
		}));

		/** Fachklasse Ofen- und Luftheizungsbauer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_557_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(814000, 10, "557", "00", false, "O", "OH", "TN", null, "A3", "Ofen- und Luftheizungsbauer/-in", "Ofen- und Luftheizungsbauer", "Ofen- und Luftheizungsbauerin", null, null)
		}));

		/** Fachklasse Servicefachkraft für Dialogmarketing */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_558_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(815000, 10, "558", "00", false, "O", "OH", "WV", null, "A2", "Servicefachkraft für Dialogmarketing", "Servicefachkraft für Dialogmarketing", "Servicefachkraft für Dialogmarketing", null, null)
		}));

		/** Fachklasse Fachkraft für Hafenlogistik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_559_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(816000, 10, "559", "00", false, "O", "OH", "WV", null, "A3", "Fachkraft für Hafenlogistik", "Fachkraft für Hafenlogistik", "Fachkraft für Hafenlogistik", null, null)
		}));

		/** Fachklasse Medienkaufmann/-frau Digital und Print */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_560_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(817000, 10, "560", "00", false, "O", "OH", "WV", null, "A3", "Medienkaufmann/-frau Digital und Print", "Medienkaufmann Digital und Print", "Medienkauffrau Digital und Print", null, null)
		}));

		/** Fachklasse Flechtwerkgestalter/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_561_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(818000, 10, "561", "00", false, "O", "OH", "GT", null, "A3", "Flechtwerkgestalter/-in", "Flechtwerkgestalter", "Flechtwerkgestalterin", null, null)
		}));

		/** Fachklasse Hafenschiffer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_562_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(819000, 10, "562", "00", false, "O", "OH", "TN", null, "A3", "Hafenschiffer/-in", "Hafenschiffer", "Hafenschifferin", null, null)
		}));

		/** Fachklasse Kaufmann/-frau für Marketingkommunikation */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_563_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(820000, 10, "563", "00", false, "W", "WV", "WV", null, "A3", "Kaufmann/-frau für Marketingkommunikation", "Kaufmann für Marketingkommunikation", "Kauffrau für Marketingkommunikation", null, null)
		}));

		/** Fachklasse Tiermedizinische/-r Fachangestellte/-r */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_564_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(821000, 10, "564", "00", false, "O", "OH", "WV", null, "A3", "Tiermedizinische/-r Fachangestellte/-r", "Tiermedizinischer Fachangestellter", "Tiermedizinische Fachangestellte", null, null)
		}));

		/** Fachklasse Müller/-in (Verfahrenstechn. für Mühlen- und Futtermittelwirtschaft) (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_565_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(822000, 10, "565", "00", true, "O", "OH", "OZ", null, "A3", "Müller/-in (Verfahrenstechn. für Mühlen- und Futtermittelwirtschaft)", "Müller (Verfahrenstechn. für Mühlen- und Futtermittelwirtschaft)", "Müllerin (Verfahrenstechn. für Mühlen- und Futtermittelwirtschaft)", null, 2021)
		}));

		/** Fachklasse Verfahrenstechnologe/-technologin für Mühlen- und Getreidewirtschaft - Agrarlager */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_565_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(823000, 10, "565", "01", false, null, null, "OZ", null, "A3", "Verfahrenstechnologe/-technologin für Mühlen- und Getreidewirtschaft - Agrarlager", "Verfahrenstechnologe für Mühlen- und Getreidewirtschaft - Agrarlager", "Verfahrenstechnologin für Mühlen- und Getreidewirtschaft - Agrarlager", null, null)
		}));

		/** Fachklasse Verfahrenstechnologe/-technologin für Mühlen- und Getreidewirtschaft - Müllerei */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_565_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(824000, 10, "565", "02", false, null, null, "OZ", null, "A3", "Verfahrenstechnologe/-technologin für Mühlen- und Getreidewirtschaft - Müllerei", "Verfahrenstechnologe für Mühlen- und Getreidewirtschaft - Müllerei", "Verfahrenstechnologin für Mühlen- und Getreidewirtschaft - Müllerei", null, null)
		}));

		/** Fachklasse Mathematisch-technischer Softwareentwickler/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_566_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(825000, 10, "566", "00", false, "O", "OH", "IF", null, "A3", "Mathematisch-technischer Softwareentwickler/-in", "Mathematisch-technischer Softwareentwickler", "Mathematisch-technische Softwareentwicklerin", null, null)
		}));

		/** Fachklasse Produktprüfer/-in Textil */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_567_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(826000, 10, "567", "00", false, "O", "OH", "TN", null, "A2", "Produktprüfer/-in Textil", "Produktprüfer Textil", "Produktprüferin Textil", null, null)
		}));

		/** Fachklasse Fachkraft für Holz- und Bautenschutzarbeiten - Holzschutz */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_568_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(827000, 10, "568", "01", false, "O", "OH", "TN", null, "A2", "Fachkraft für Holz- und Bautenschutzarbeiten - Holzschutz", "Fachkraft für Holz- und Bautenschutzarbeiten - Holzschutz", "Fachkraft für Holz- und Bautenschutzarbeiten - Holzschutz", null, null)
		}));

		/** Fachklasse Fachkraft für Holz- und Bautenschutzarbeiten - Bautenschutz */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_568_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(828000, 10, "568", "02", false, "O", "OH", "TN", null, "A2", "Fachkraft für Holz- und Bautenschutzarbeiten - Bautenschutz", "Fachkraft für Holz- und Bautenschutzarbeiten - Bautenschutz", "Fachkraft für Holz- und Bautenschutzarbeiten - Bautenschutz", null, null)
		}));

		/** Fachklasse Holz- und Bautenschützer/-in - Holzschutz */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_569_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(829000, 10, "569", "01", false, "O", "OH", "TN", null, "A3", "Holz- und Bautenschützer/-in - Holzschutz", "Holz- und Bautenschützer - Holzschutz", "Holz- und Bautenschützerin - Holzschutz", null, null)
		}));

		/** Fachklasse Holz- und Bautenschützer/-in - Bautenschutz */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_569_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(830000, 10, "569", "02", false, "O", "OH", "TN", null, "A3", "Holz- und Bautenschützer/-in - Bautenschutz", "Holz- und Bautenschützer - Bautenschutz", "Holz- und Bautenschützerin - Bautenschutz", null, null)
		}));

		/** Fachklasse Personaldienstleistungskaufmann/-frau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_570_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(831000, 10, "570", "00", false, "W", "WV", "WV", null, "A3", "Personaldienstleistungskaufmann/-frau", "Personaldienstleistungskaufmann", "Personaldienstleistungskauffrau", null, null)
		}));

		/** Fachklasse Mechatroniker/-in für Kältetechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_571_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(832000, 10, "571", "00", false, "O", "OH", "TN", null, "A4", "Mechatroniker/-in für Kältetechnik", "Mechatroniker für Kältetechnik", "Mechatronikerin für Kältetechnik", null, null)
		}));

		/** Fachklasse Sportfachmann/-frau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_572_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(833000, 10, "572", "00", false, "O", "OH", "WV", null, "A3", "Sportfachmann/-frau", "Sportfachmann", "Sportfachfrau", null, null)
		}));

		/** Fachklasse Automatenfachmann/-frau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_573_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(834000, 10, "573", "00", true, "O", "OH", "TN", null, "A3", "Automatenfachmann/-frau", "Automatenfachmann", "Automatenfachfrau", null, 2019)
		}));

		/** Fachklasse Automatenfachmann/-frau - Automatenmechatronik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_573_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(835000, 10, "573", "01", false, null, null, "TN", null, "A3", "Automatenfachmann/-frau - Automatenmechatronik", "Automatenfachmann - Automatenmechatronik", "Automatenfachmannfrau - Automatenmechatronik", null, null)
		}));

		/** Fachklasse Automatenfachmann/-frau - Automatendienstleistung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_573_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(836000, 10, "573", "02", false, null, null, "TN", null, "A3", "Automatenfachmann/-frau - Automatendienstleistung", "Automatenfachmann - Automatendienstleistung", "Automatenfachmannfrau - Automatendienstleistung", null, null)
		}));

		/** Fachklasse Fachkraft für Automatenservice (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_574_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(837000, 10, "574", "00", true, "O", "OH", "TN", null, "A2", "Fachkraft für Automatenservice", "Fachkraft für Automatenservice", "Fachkraft für Automatenservice", null, 2017)
		}));

		/** Fachklasse Fotomedienfachmann/-frau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_575_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(838000, 10, "575", "00", false, "O", "OH", "GT", null, "A3", "Fotomedienfachmann/-frau", "Fotomedienfachmann", "Fotomedienfachfrau", null, null)
		}));

		/** Fachklasse Produktionstechnologe/-technologin */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_576_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(839000, 10, "576", "00", false, "O", "OH", "TN", null, "A3", "Produktionstechnologe/-technologin", "Produktionstechnologe", "Produktionstechnologin", null, null)
		}));

		/** Fachklasse Servicekraft für Schutz und Sicherheit */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_577_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(840000, 10, "577", "00", false, "O", "OH", "WV", null, "A2", "Servicekraft für Schutz und Sicherheit", "Servicekraft für Schutz und Sicherheit", "Servicekraft für Schutz und Sicherheit", null, null)
		}));

		/** Fachklasse Speiseeishersteller/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_578_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(841000, 10, "578", "00", true, "E", "EH", null, null, null, "Speiseeishersteller/-in", "Speiseeishersteller", "Speiseeisherstellerin", null, 2016)
		}));

		/** Fachklasse Feinpolierer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_579_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(842000, 10, "579", "00", false, "T", "MT", "OZ", null, "A3", "Feinpolierer/-in", "Feinpolierer", "Feinpoliererin", null, null)
		}));

		/** Fachklasse Gerber/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_580_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(843000, 10, "580", "00", true, "O", "OH", "OZ", null, "A3", "Gerber/-in", "Gerber", "Gerberin", null, 2019)
		}));

		/** Fachklasse Segelmacher/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_581_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(844000, 10, "581", "00", false, "O", "OH", "OZ", null, "A3", "Segelmacher/-in", "Segelmacher", "Segelmacherin", null, null)
		}));

		/** Fachklasse Werkgehilfe/-gehilfin (Schmuckwarenindustrie, Taschen- und Armbanduhren) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_582_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(845000, 10, "582", "00", false, "O", "OH", "OZ", null, "A3", "Werkgehilfe/-gehilfin (Schmuckwarenindustrie, Taschen- und Armbanduhren)", "Werkgehilfe (Schmuckwarenindustrie, Taschen- und Armbanduhren)", "Werkgehilfin (Schmuckwarenindustrie, Taschen- und Armbanduhren)", null, null)
		}));

		/** Fachklasse Musikfachhändler/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_583_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(846000, 10, "583", "00", false, "W", "WV", "OZ", null, "A3", "Musikfachhändler/-in", "Musikfachhändler", "Musikfachhändlerin", null, null)
		}));

		/** Fachklasse Werkfeuerwehrmann/-frau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_584_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(847000, 10, "584", "00", false, "O", "OH", "TN", null, "A3", "Werkfeuerwehrmann/-frau", "Werkfeuerwehrmann", "Werkfeuerwehrfrau", null, null)
		}));

		/** Fachklasse Bergbautechnologe/-technologin - Tiefbautechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_585_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(848000, 10, "585", "01", false, "T", "MT", "TN", null, "A3", "Bergbautechnologe/-technologin - Tiefbautechnik", "Bergbautechnologe - Tiefbautechnik", "Bergbautechnologin -Tiefbautechnik", null, null)
		}));

		/** Fachklasse Bergbautechnologe/-technologin - Tiefbohrtechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_585_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(849000, 10, "585", "02", false, "T", "MT", "TN", null, "A3", "Bergbautechnologe/-technologin - Tiefbohrtechnik", "Bergbautechnologe - Tiefbohrtechnik", "Bergbautechnologin -Tiefbohrtechnik", null, null)
		}));

		/** Fachklasse Industrieelektriker/-in - Betriebstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_586_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(850000, 10, "586", "01", false, "T", "ET", "TN", null, "A2", "Industrieelektriker/-in - Betriebstechnik", "Industrieelektriker - Betriebstechnik", "Industrieelektrikerin - Betriebstechnik", null, null)
		}));

		/** Fachklasse Industrieelektriker/-in - Geräte und Systeme */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_586_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(851000, 10, "586", "02", false, "T", "ET", "TN", null, "A2", "Industrieelektriker/-in - Geräte und Systeme", "Industrieelektriker - Geräte und Systeme", "Industrieelektrikerin - Geräte und Systeme", null, null)
		}));

		/** Fachklasse Technische/-r Modellbauer/-in - Anschauung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_587_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(852000, 10, "587", "01", false, "O", "OH", "TN", null, "A4", "Technische/-r Modellbauer/-in - Anschauung", "Technischer Modellbauer - Anschauung", "Technische Modellbauerin - Anschauung", null, null)
		}));

		/** Fachklasse Technische/-r Modellbauer/-in - Gießerei */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_587_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(853000, 10, "587", "02", false, "O", "OH", "TN", null, "A4", "Technische/-r Modellbauer/-in - Gießerei", "Technischer Modellbauer - Gießerei", "Technische Modellbauerin - Gießerei", null, null)
		}));

		/** Fachklasse Technische/-r Modellbauer/-in - Karosserie und Produktion */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_587_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(854000, 10, "587", "03", false, "O", "OH", "TN", null, "A4", "Technische/-r Modellbauer/-in - Karosserie und Produktion", "Technischer Modellbauer - Karosserie und Produktion", "Technische Modellbauerin - Karosserie und Produktion", null, null)
		}));

		/** Fachklasse Geomatiker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_588_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(855000, 10, "588", "00", false, "O", "OH", "TN", null, "A3", "Geomatiker/-in", "Geomatiker", "Geomatikerin", null, null)
		}));

		/** Fachklasse Milchtechnologe/-technologin */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_589_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(856000, 10, "589", "00", false, "T", "CP", "OZ", null, "A3", "Milchtechnologe/-technologin", "Milchtechnologe", "Milchtechnologin", null, null)
		}));

		/** Fachklasse Vorpolierer/-in - Schmuck- und Kleingeräteherstellung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_590_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(857000, 10, "590", "00", false, "O", "OH", "OZ", null, "A2", "Vorpolierer/-in - Schmuck- und Kleingeräteherstellung", "Vorpolierer - Schmuck- und Kleingeräteherstellung", "Vorpoliererin - Schmuck- und Kleingeräteherstellung", null, null)
		}));

		/** Fachklasse Medientechnologe/-technologin Druckverarbeitung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_591_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(858000, 10, "591", "00", false, "O", "OH", "GT", null, "A3", "Medientechnologe/-technologin Druckverarbeitung", "Medientechnologe Druckverarbeitung", "Medientechnologin Druckverarbeitung", null, null)
		}));

		/** Fachklasse Medientechnologe/-technologin Druck */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_592_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(859000, 10, "592", "00", false, "O", "OH", "GT", null, "A3", "Medientechnologe/-technologin Druck", "Medientechnologe Druck", "Medientechnologin Druck", null, null)
		}));

		/** Fachklasse Mediengestalter/-in Flexografie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_593_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(860000, 10, "593", "00", true, "O", "OH", "GT", null, "A3", "Mediengestalter/-in Flexografie", "Mediengestalter Flexografie", "Mediengestalterin Flexografie", null, 2019)
		}));

		/** Fachklasse Tourismuskaufmann/-frau (Kaufmann/-frau für Privat- und Geschäftsreisen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_594_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(861000, 10, "594", "00", false, "O", "OH", "WV", null, "A3", "Tourismuskaufmann/-frau (Kaufmann/-frau für Privat- und Geschäftsreisen)", "Tourismuskaufmann (Kaufmann für Privat- und Geschäftsreisen)", "Tourismuskauffrau (Kauffrau für Privat- und Geschäftsreisen)", null, null)
		}));

		/** Fachklasse Fachkraft für Lederverarbeitung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_595_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(862000, 10, "595", "00", false, "O", "OH", "TN", null, "A2", "Fachkraft für Lederverarbeitung", "Fachkraft für Lederverarbeitung", "Fachkraft für Lederverarbeitung", null, null)
		}));

		/** Fachklasse Medientechnologe/-technologin Siebdruck */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_596_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(863000, 10, "596", "00", false, "O", "OH", "GT", null, "A3", "Medientechnologe/-technologin Siebdruck", "Medientechnologe Siebdruck", "Medientechnologin Siebdruck", null, null)
		}));

		/** Fachklasse Textilgestalter/-in im Handwerk - Filzen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_597_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(864000, 10, "597", "01", false, "O", "OH", "GT", null, "A3", "Textilgestalter/-in im Handwerk - Filzen", "Textilgestalter im Handwerk - Filzen", "Textilgestalterin im Handwerk - Filzen", null, null)
		}));

		/** Fachklasse Textilgestalter/-in im Handwerk - Klöppeln */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_597_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(865000, 10, "597", "02", false, "O", "OH", "GT", null, "A3", "Textilgestalter/-in im Handwerk - Klöppeln", "Textilgestalter im Handwerk - Klöppeln", "Textilgestalterin im Handwerk - Klöppeln", null, null)
		}));

		/** Fachklasse Textilgestalter/-in im Handwerk - Posamentieren */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_597_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(866000, 10, "597", "03", false, "O", "OH", "GT", null, "A3", "Textilgestalter/-in im Handwerk - Posamentieren", "Textilgestalter im Handwerk - Posamentieren", "Textilgestalterin im Handwerk - Posamentieren", null, null)
		}));

		/** Fachklasse Textilgestalter/-in im Handwerk - Sticken */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_597_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(867000, 10, "597", "04", false, "O", "OH", "GT", null, "A3", "Textilgestalter/-in im Handwerk - Sticken", "Textilgestalter im Handwerk - Sticken", "Textilgestalterin im Handwerk - Sticken", null, null)
		}));

		/** Fachklasse Textilgestalter/-in im Handwerk - Stricken */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_597_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(868000, 10, "597", "05", false, "O", "OH", "GT", null, "A3", "Textilgestalter/-in im Handwerk - Stricken", "Textilgestalter im Handwerk - Stricken", "Textilgestalterin im Handwerk - Stricken", null, null)
		}));

		/** Fachklasse Textilgestalter/-in im Handwerk - Weben */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_597_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(869000, 10, "597", "06", false, "O", "OH", "GT", null, "A3", "Textilgestalter/-in im Handwerk - Weben", "Textilgestalter im Handwerk - Weben", "Textilgestalterin im Handwerk - Weben", null, null)
		}));

		/** Fachklasse Technische/-r Systemplaner/-in - Elektrotechnische Systeme */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_598_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(870000, 10, "598", "01", false, "O", "OH", "TN", null, "A4", "Technische/-r Systemplaner/-in - Elektrotechnische Systeme", "Technischer Systemplaner - Elektrotechnische Systeme", "Technische Systemplanerin - Elektrotechnische Systeme", null, null)
		}));

		/** Fachklasse Technische/-r Systemplaner/-in - Stahl- und Metallbautechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_598_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(871000, 10, "598", "02", false, "O", "OH", "TN", null, "A4", "Technische/-r Systemplaner/-in - Stahl- und Metallbautechnik", "Technischer Systemplaner - Stahl- und Metallbautechnik", "Technische Systemplanerin - Stahl- und Metallbautechnik", null, null)
		}));

		/** Fachklasse Technische/-r Systemplaner/-in - Versorgungs- und Ausrüstungstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_598_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(872000, 10, "598", "03", false, "O", "OH", "TN", null, "A4", "Technische/-r Systemplaner/-in - Versorgungs- und Ausrüstungstechnik", "Technischer Systemplaner - Versorgungs- und Ausrüstungstechnik", "Technische Systemplanerin - Versorgungs- und Ausrüstungstechnik", null, null)
		}));

		/** Fachklasse Packmitteltechnologe/-technologin */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_599_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(873000, 10, "599", "00", false, "O", "OH", "TN", null, "A3", "Packmitteltechnologe/-technologin", "Packmitteltechnologe", "Packmitteltechnologin", null, null)
		}));

		/** Fachklasse Fachangestellte/-r für Arbeitsmarktdienstleistungen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_600_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(874000, 10, "600", "00", false, "W", "WV", "WV", null, "A3", "Fachangestellte/-r für Arbeitsmarktdienstleistungen", "Fachangestellter für Arbeitsmarktdienstleistungen", "Fachangestellte für Arbeitsmarktdienstleistungen", null, null)
		}));

		/** Fachklasse Fachkraft für Metalltechnik - Montagetechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_601_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(875000, 10, "601", "01", false, "T", "MT", "TN", null, "A2", "Fachkraft für Metalltechnik - Montagetechnik", "Fachkraft für Metalltechnik - Montagetechnik", "Fachkraft für Metalltechnik - Montagetechnik", null, null)
		}));

		/** Fachklasse Fachkraft für Metalltechnik - Konstruktionstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_601_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(876000, 10, "601", "02", false, "T", "MT", "TN", null, "A2", "Fachkraft für Metalltechnik - Konstruktionstechnik", "Fachkraft für Metalltechnik - Konstruktionstechnik", "Fachkraft für Metalltechnik - Konstruktionstechnik", null, null)
		}));

		/** Fachklasse Fachkraft für Metalltechnik - Umform- und Drahttechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_601_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(877000, 10, "601", "03", false, "T", "MT", "TN", null, "A2", "Fachkraft für Metalltechnik - Umform- und Drahttechnik", "Fachkraft für Metalltechnik - Umform- und Drahttechnik", "Fachkraft für Metalltechnik - Umform- und Drahttechnik", null, null)
		}));

		/** Fachklasse Fachkraft für Metalltechnik - Zerspanungstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_601_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(878000, 10, "601", "04", false, "T", "MT", "TN", null, "A2", "Fachkraft für Metalltechnik - Zerspanungstechnik", "Fachkraft für Metalltechnik - Zerspanungstechnik", "Fachkraft für Metalltechnik - Zerspanungstechnik", null, null)
		}));

		/** Fachklasse Stanz- und Umformmechaniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_602_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(879000, 10, "602", "00", false, "T", "MT", "TN", null, "A3", "Stanz- und Umformmechaniker/-in", "Stanz- und Umformmechaniker", "Stanz- und Umformmechanikerin", null, null)
		}));

		/** Fachklasse Pflanzentechnologe/-technologin */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_603_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(880000, 10, "603", "00", false, "A", "AW", "AW", null, "A3", "Pflanzentechnologe/-technologin", "Pflanzentechnologe", "Pflanzentechnologin", null, null)
		}));

		/** Fachklasse Weintechnologe/-technologin */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_604_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(881000, 10, "604", "00", false, "O", "OH", "OZ", null, "A3", "Weintechnologe/-technologin", "Weintechnologe", "Weintechnologin", null, null)
		}));

		/** Fachklasse Elektroniker/-in für Informations- und Systemtechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_605_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(882000, 10, "605", "00", false, "O", "OH", "TN", null, "A4", "Elektroniker/-in für Informations- und Systemtechnik", "Elektroniker für Informations- und Systemtechnik", "Elektronikerin für Informations- und Systemtechnik", null, null)
		}));

		/** Fachklasse Orthopädietechnik-Mechaniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_606_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(883000, 10, "606", "00", false, "O", "OH", "GS", null, "A3", "Orthopädietechnik-Mechaniker/-in", "Orthopädietechnik-Mechaniker", "Orthopädietechnik-Mechanikerin", null, null)
		}));

		/** Fachklasse Kaufmann/-frau für Büromanagement */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_607_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(884000, 10, "607", "00", false, "W", "WV", "WV", null, "A3", "Kaufmann/-frau für Büromanagement", "Kaufmann für Büromanagement", "Kauffrau für Büromanagement", null, null)
		}));

		/** Fachklasse Süßwarentechnologe/-technologin */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_608_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(885000, 10, "608", "00", false, "O", "OH", "EH", null, "A3", "Süßwarentechnologe/-technologin", "Süßwarentechnologe", "Süßwarentechnologin", null, null)
		}));

		/** Fachklasse Fachkraft für Speiseeis (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_609_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(886000, 10, "609", "00", true, "E", "EH", "EH", null, "A3", "Fachkraft für Speiseeis", "Fachkraft für Speiseeis", "Fachkraft für Speiseeis", null, 2021)
		}));

		/** Fachklasse Land- und Baumaschinenmechatroniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_610_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(887000, 10, "610", "00", false, "T", "FT", "TN", null, "A4", "Land- und Baumaschinenmechatroniker/-in", "Land- und Baumaschinenmechatroniker", "Land- und Baumaschinenmechatronikerin", null, null)
		}));

		/** Fachklasse Zweiradmechatroniker/-in - Fahrradtechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_611_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(888000, 10, "611", "01", false, "T", "FT", "TN", null, "A4", "Zweiradmechatroniker/-in - Fahrradtechnik", "Zweiradmechatroniker - Fahrradtechnik", "Zweiradmechatronikerin - Fahrradtechnik", null, null)
		}));

		/** Fachklasse Zweiradmechatroniker/-in - Motorradtechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_611_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(889000, 10, "611", "02", false, "T", "FT", "TN", null, "A4", "Zweiradmechatroniker/-in - Motorradtechnik", "Zweiradmechatroniker - Motorradtechnik", "Zweiradmechatronikerin - Motorradtechnik", null, null)
		}));

		/** Fachklasse Kerzenhersteller/-in und Wachsbildner/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_612_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(890000, 10, "612", "00", false, null, null, "OZ", null, "A3", "Kerzenhersteller/-in und Wachsbildner/-in", "Kerzenhersteller und Wachsbildner", "Kerzenherstellerin und Wachsbildnerin", null, null)
		}));

		/** Fachklasse Werksteinhersteller/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_613_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(891000, 10, "613", "00", false, null, null, "TN", null, "A3", "Werksteinhersteller/-in", "Werksteinhersteller", "Werksteinherstellerin", null, null)
		}));

		/** Fachklasse Fachkraft für Lederherstellung und Gerbereitechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_614_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(892000, 10, "614", "00", false, null, null, "TN", null, "A3", "Fachkraft für Lederherstellung und Gerbereitechnik", "Fachkraft für Lederherstellung und Gerbereitechnik", "Fachkraft für Lederherstellung und Gerbereitechnik", null, null)
		}));

		/** Fachklasse Textil- und Modenäher/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_615_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(893000, 10, "615", "00", false, null, null, "TN", null, "A2", "Textil- und Modenäher/-in", "Textil- und Modenäher", "Textil- und Modenäherin", null, null)
		}));

		/** Fachklasse Textil- und Modeschneider/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_616_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(894000, 10, "616", "00", false, null, null, "TN", null, "A3", "Textil- und Modeschneider/-in", "Textil- und Modeschneider", "Textil- und Modeschneiderin", null, null)
		}));

		/** Fachklasse Kaufmann/-frau im E-Commerce */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_617_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(895000, 10, "617", "00", false, "W", "WV", "WV", null, "A3", "Kaufmann/-frau im E-Commerce", "Kaufmann im E-Commerce", "Kauffrau im E-Commerce", null, null)
		}));

		/** Fachklasse Verfahrenstechnolog(e/in) Metall - Eisen- und Stahlmetallurgie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_618_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(896000, 10, "618", "01", false, null, null, "TN", null, "A4", "Verfahrenstechnolog(e/in) Metall - Eisen- und Stahlmetallurgie", "Verfahrenstechnologe Metall - Eisen- und Stahlmetallurgie", "Verfahrenstechnologin Metall - Eisen- und Stahlmetallurgie", null, null)
		}));

		/** Fachklasse Verfahrenstechnolog(e/in) Metall - Stahlumformung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_618_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(897000, 10, "618", "02", false, null, null, "TN", null, "A4", "Verfahrenstechnolog(e/in) Metall - Stahlumformung", "Verfahrenstechnologe Metall - Stahlumformung", "Verfahrenstechnologin Metall - Stahlumformung", null, null)
		}));

		/** Fachklasse Verfahrenstechnolog(e/in) Metall - Nichteisenmetallurgie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_618_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(898000, 10, "618", "03", false, null, null, "TN", null, "A4", "Verfahrenstechnolog(e/in) Metall - Nichteisenmetallurgie", "Verfahrenstechnologe Metall - Nichteisenmetallurgie", "Verfahrenstechnologin Metall - Nichteisenmetallurgie", null, null)
		}));

		/** Fachklasse Verfahrenstechnolog(e/in) Metall - Nichteisenmetallumformung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_618_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(899000, 10, "618", "04", false, null, null, "TN", null, "A4", "Verfahrenstechnolog(e/in) Metall - Nichteisenmetallumformung", "Verfahrenstechnologe Metall - Nichteisenmetallumformung", "Verfahrenstechnologin Metall - Nichteisenmetallumformung", null, null)
		}));

		/** Fachklasse Flachglastechnologe/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_619_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(900000, 10, "619", "00", false, "O", "OH", "TN", null, "A3", "Flachglastechnologe/-in", "Flachglastechnologe", "Flachglastechnologin", null, null)
		}));

		/** Fachklasse Präzisionswerkzeugmechaniker/-in - Schneidwerkzeuge */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_620_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(901000, 10, "620", "01", false, "T", "MT", "TN", null, "A4", "Präzisionswerkzeugmechaniker/-in - Schneidwerkzeuge", "Präzisionswerkzeugmechaniker - Schneidwerkzeuge", "Präzisionswerkzeugmechanikerin - Schneidwerkzeuge", null, null)
		}));

		/** Fachklasse Präzisionswerkzeugmechaniker/-in - Zerspanwerkzeuge */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_620_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(902000, 10, "620", "02", false, "T", "MT", "TN", null, "A4", "Präzisionswerkzeugmechaniker/-in - Zerspanwerkzeuge", "Präzisionswerkzeugmechaniker - Zerspanwerkzeuge", "Präzisionswerkzeugmechanikerin - Zerspanwerkzeuge", null, null)
		}));

		/** Fachklasse Maßschuhmacher/-in - Maßschuhe */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_621_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(903000, 10, "621", "01", false, "O", "OH", "TN", null, "A3", "Maßschuhmacher/-in - Maßschuhe", "Maßschuhmacher - Maßschuhe", "Maßschuhmacherin - Maßschuhe", null, null)
		}));

		/** Fachklasse Maßschuhmacher/-in - Schaftbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_621_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(904000, 10, "621", "02", false, "O", "OH", "TN", null, "A3", "Maßschuhmacher/-in - Schaftbau", "Maßschuhmacher - Schaftbau", "Maßschuhmacherin - Schaftbau", null, null)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 10 (Teil 10)
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex10_Teil10(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Prüftechnologe/-in Keramik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_622_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(905000, 10, "622", "00", false, "T", "CP", "TN", null, "A3", "Prüftechnologe/-in Keramik", "Prüftechnologe Keramik", "Prüftechnologin Keramik", null, null)
		}));

		/** Fachklasse Kaufmann/-frau für IT-System-Management */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_623_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(906000, 10, "623", "00", false, null, null, "WV", null, "A3", "Kaufmann/-frau für IT-System-Management", "Kaufmann für IT-System-Management", "Kauffrau für IT-System-Management", null, null)
		}));

		/** Fachklasse IT-System-Elektroniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_624_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(907000, 10, "624", "00", false, "O", "OH", "TN", null, "A3", "IT-System-Elektroniker/-in", "IT-System-Elektroniker", "IT-System-Elektronikerin", null, null)
		}));

		/** Fachklasse Gold- und Silberschmied/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_625_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(908000, 10, "625", "00", false, "O", "OH", "GT", null, "A4", "Gold- und Silberschmied/-in", "Gold- und Silberschmied", "Gold- und Silberschmiedin", null, null)
		}));

		/** Fachklasse Fahrzeuginterieur-Mechaniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_626_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(909000, 10, "626", "00", false, "G", "FR", "TN", null, "A3", "Fahrzeuginterieur-Mechaniker/-in", "Fahrzeuginterieur-Mechaniker", "Fahrzeuginterieur-Mechanikerin", null, null)
		}));

		/** Fachklasse Binnenschifffahrtskapitän/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_627_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(910000, 10, "627", "00", false, null, null, "TN", null, "A4", "Binnenschifffahrtskapitän/-in", "Binnenschifffahrtskapitän", "Binnenschifffahrtskapitänin", 2022, null)
		}));

		/** Fachklasse Fachkraft Küche */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_628_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(911000, 10, "628", "00", false, null, null, "EH", null, "A2", "Fachkraft Küche", "Fachkraft Küche", "Fachkraft Küche", 2022, null)
		}));

		/** Fachklasse Eisenbahner/-in im Betriebsdienst Lokführer und Transport */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_629_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(912000, 10, "629", "01", false, null, null, "TN", null, "A3", "Eisenbahner/-in im Betriebsdienst Lokführer und Transport", "Eisenbahner im Betriebsdienst Lokführer und Transport", "Eisenbahnerin im Betriebsdienst Lokführer und Transport", 2022, null)
		}));

		/** Fachklasse Eisenbahner/-in in der Zugverkehrssteuerung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_629_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(913000, 10, "629", "02", false, null, null, "TN", null, "A3", "Eisenbahner/-in in der Zugverkehrssteuerung", "Eisenbahner in der Zugverkehrssteuerung", "Eisenbahnerin in der Zugverkehrssteuerung", 2022, null)
		}));

		/** Fachklasse Fachkraft für Gastronomie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_630_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(914000, 10, "630", "00", false, null, null, "EH", null, "A2", "Fachkraft für Gastronomie", "Fachkraft für Gastronomie", "Fachkraft für Gastronomie", 2022, null)
		}));

		/** Fachklasse Fachmann/-frau für Restaurants und Veranstaltungsgastronomie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_631_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(915000, 10, "631", "00", false, null, null, "EH", null, "A3", "Fachmann/-frau für Restaurants und Veranstaltungsgastronomie", "Fachmann für Restaurants und Veranstaltungsgastronomie", "Fachfrau für Restaurants und Veranstaltungsgastronomie", 2022, null)
		}));

		/** Fachklasse Kaufmann/-frau für Hotelmanagement */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_632_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(916000, 10, "632", "00", false, null, null, "EH", null, "A3", "Kaufmann/-frau für Hotelmanagement", "Kaufmann für Hotelmanagement", "Kauffrau für Hotelmanagement", 2022, null)
		}));

		/** Fachklasse Kaufmann/-frau für Versicherungen und Finanzanlagen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_633_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(917000, 10, "633", "00", false, null, null, "WV", null, "A3", "Kaufmann/-frau für Versicherungen und Finanzanlagen", "Kaufmann für Versicherungen und Finanzanlagen", "Kauffrau für Versicherungen und Finanzanlagen", 2022, null)
		}));

		/** Fachklasse Bäckerfachwerker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_801_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(918000, 10, "801", "00", false, "E", "EH", "EH", null, "MB", "Bäckerfachwerker/-in", "Bäckerfachwerker", "Bäckerfachwerkerin", null, null)
		}));

		/** Fachklasse Bäckerwerker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_802_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(919000, 10, "802", "00", false, "E", "EH", "EH", null, "MB", "Bäckerwerker/-in", "Bäckerwerker", "Bäckerwerkerin", null, null)
		}));

		/** Fachklasse Bau- und Metallmaler/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_803_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(920000, 10, "803", "00", false, "G", "FR", "TN", null, "MB", "Bau- und Metallmaler/-in", "Bau- und Metallmaler", "Bau- und Metallmalerin", null, null)
		}));

		/** Fachklasse Beikoch/-köchin */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_804_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(921000, 10, "804", "00", false, "E", "EH", "EH", null, "MB", "Beikoch/-köchin", "Beikoch", "Beiköchin", null, null)
		}));

		/** Fachklasse Bekleidungsteilenäher/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_805_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(922000, 10, "805", "00", false, "T", "TB", "TN", null, "MB", "Bekleidungsteilenäher/-in", "Bekleidungsteilenäher", "Bekleidungsteilenäherin", null, null)
		}));

		/** Fachklasse Blinde/-r Fernschreiber/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_806_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(923000, 10, "806", "00", true, "W", "WV", null, null, null, "Blinde/-r Fernschreiber/-in", "Blinder Fernschreiber", "Blinde Fernschreiberin", null, 2012)
		}));

		/** Fachklasse Blinde/-r Telefonist/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_807_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(924000, 10, "807", "00", true, "W", "WV", null, null, null, "Blinde/-r Telefonist/-in", "Blinder Telefonist", "Blinde Telefonistin", null, 2012)
		}));

		/** Fachklasse Broschürer/-in im Buchbinderhandwerk */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_808_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(925000, 10, "808", "00", false, "T", "DT", "TN", null, "MB", "Broschürer/-in im Buchbinderhandwerk", "Broschürer im Buchbinderhandwerk", "Broschürerin im Buchbinderhandwerk", null, null)
		}));

		/** Fachklasse Bürofachkraft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_809_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(926000, 10, "809", "00", false, "W", "WV", "WV", null, "MB", "Bürofachkraft", "Bürofachkraft", "Bürofachkraft", null, null)
		}));

		/** Fachklasse Bürokraft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_810_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(927000, 10, "810", "00", false, "W", "WV", "WV", null, "MB", "Bürokraft", "Bürokraft", "Bürokraft", null, null)
		}));

		/** Fachklasse Büropraktiker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_811_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(928000, 10, "811", "00", false, "W", "WV", "WV", null, "MB", "Büropraktiker/-in", "Büropraktiker", "Büropraktikerin", null, null)
		}));

		/** Fachklasse Deckenmacher/-in im Buchbinderhandwerk */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_812_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(929000, 10, "812", "00", false, "T", "DT", "TN", null, "MB", "Deckenmacher/-in im Buchbinderhandwerk", "Deckenmacher im Buchbinderhandwerk", "Deckenmacherin im Buchbinderhandwerk", null, null)
		}));

		/** Fachklasse Elektroanlagenfachkraft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_813_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(930000, 10, "813", "00", false, "T", "ET", "TN", null, "MB", "Elektroanlagenfachkraft", "Elektroanlagenfachkraft", "Elektroanlagenfachkraft", null, null)
		}));

		/** Fachklasse Elektrogerätefachkraft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_814_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(931000, 10, "814", "00", false, "T", "ET", "TN", null, "MB", "Elektrogerätefachkraft", "Elektrogerätefachkraft", "Elektrogerätefachkraft", null, null)
		}));

		/** Fachklasse Elektrogerätezusammenbauer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_815_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(932000, 10, "815", "00", false, "T", "ET", "TN", null, "MB", "Elektrogerätezusammenbauer/-in", "Elektrogerätezusammenbauer", "Elektrogerätezusammenbauerin", null, null)
		}));

		/** Fachklasse Elektroinstallationspraktiker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_816_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(933000, 10, "816", "00", false, "T", "ET", "TN", null, "MB", "Elektroinstallationspraktiker/-in", "Elektroinstallationspraktiker", "Elektroinstallationspraktikerin", null, null)
		}));

		/** Fachklasse Elektronikgerätemechaniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_817_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(934000, 10, "817", "00", false, "T", "ET", "TN", null, "MB", "Elektronikgerätemechaniker/-in", "Elektronikgerätemechaniker", "Elektronikgerätemechanikerin", null, null)
		}));

		/** Fachklasse Fachwerker/-in für Recycling */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_818_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(935000, 10, "818", "00", false, "T", "CP", "TN", null, "MB", "Fachwerker/-in für Recycling", "Fachwerker für Recycling", "Fachwerkerin für Recycling", null, null)
		}));

		/** Fachklasse Fachwerker/-in im Gartenbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_819_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(936000, 10, "819", "00", false, "A", "AW", "AW", null, "MB", "Fachwerker/-in im Gartenbau", "Fachwerker im Gartenbau", "Fachwerkerin im Gartenbau", null, null)
		}));

		/** Fachklasse Falzmaschineneinrichter/-in im Buchbinderhandwerk */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_820_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(937000, 10, "820", "00", false, "T", "DT", "TN", null, "MB", "Falzmaschineneinrichter/-in im Buchbinderhandwerk", "Falzmaschineneinrichter im Buchbinderhandwerk", "Falzmaschineneinrichterin im Buchbinderhandwerk", null, null)
		}));

		/** Fachklasse Fernmeldeinstallationspraktiker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_821_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(938000, 10, "821", "00", true, "T", "ET", null, null, null, "Fernmeldeinstallationspraktiker/-in", "Fernmeldeinstallationspraktiker", "Fernmeldeinstallationspraktikerin", null, 2012)
		}));

		/** Fachklasse Fertigmacher/-in im Buchbinderhandwerk */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_822_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(939000, 10, "822", "00", false, "T", "DT", "TN", null, "MB", "Fertigmacher/-in im Buchbinderhandwerk", "Fertigmacher im Buchbinderhandwerk", "Fertigmacherin im Buchbinderhandwerk", null, null)
		}));

		/** Fachklasse Fertigmacher/-in von Akzidenzen im Buchbinderhandwerk */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_823_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(940000, 10, "823", "00", false, "T", "DT", "TN", null, "MB", "Fertigmacher/-in von Akzidenzen im Buchbinderhandwerk", "Fertigmacher von Akzidenzen im Buchbinderhandwerk", "Fertigmacherin von Akzidenzen im Buchbinderhandwerk", null, null)
		}));

		/** Fachklasse Gießereiwerker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_824_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(941000, 10, "824", "00", false, "T", "MT", "TN", null, "MB", "Gießereiwerker/-in", "Gießereiwerker", "Gießereiwerkerin", null, null)
		}));

		/** Fachklasse Hauswirtschaftshelfer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_825_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(942000, 10, "825", "00", false, "A", "AW", "EH", null, "MB", "Hauswirtschaftshelfer/-in", "Hauswirtschaftshelfer", "Hauswirtschaftshelferin", null, null)
		}));

		/** Fachklasse Helfer/-in im Hotel- und Gaststättengewerbe */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_826_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(943000, 10, "826", "00", false, "E", "EH", "EH", null, "MB", "Helfer/-in im Hotel- und Gaststättengewerbe", "Helfer im Hotel- und Gaststättengewerbe", "Helferin im Hotel- und Gaststättengewerbe", null, null)
		}));

		/** Fachklasse Hochbaufachwerker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_827_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(944000, 10, "827", "00", false, "T", "BT", "TN", null, "MB", "Hochbaufachwerker/-in", "Hochbaufachwerker", "Hochbaufachwerkerin", null, null)
		}));

		/** Fachklasse Holzbearbeiter/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_828_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(945000, 10, "828", "00", false, "T", "HT", "TN", null, "MB", "Holzbearbeiter/-in", "Holzbearbeiter", "Holzbearbeiterin", null, null)
		}));

		/** Fachklasse Holzleimwerker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_829_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(946000, 10, "829", "00", true, "T", "HT", null, null, null, "Holzleimwerker/-in", "Holzleimwerker", "Holzleimwerkerin", null, 2012)
		}));

		/** Fachklasse Holzverarbeiter/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_830_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(947000, 10, "830", "00", false, "T", "HT", "TN", null, "MB", "Holzverarbeiter/-in", "Holzverarbeiter", "Holzverarbeiterin", null, null)
		}));

		/** Fachklasse Holzwerker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_831_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(948000, 10, "831", "00", false, "T", "HT", "TN", null, "MB", "Holzwerker/-in", "Holzwerker", "Holzwerkerin", null, null)
		}));

		/** Fachklasse Hüttenwerker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_832_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(949000, 10, "832", "00", false, "T", "MT", "TN", null, "MB", "Hüttenwerker/-in", "Hüttenwerker", "Hüttenwerkerin", null, null)
		}));

		/** Fachklasse Industriewerker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_833_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(950000, 10, "833", "00", false, "T", "MT", "TN", null, "MB", "Industriewerker/-in", "Industriewerker", "Industriewerkerin", null, null)
		}));

		/** Fachklasse Lagerfachhelfer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_834_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(951000, 10, "834", "00", false, "W", "WV", "WV", null, "MB", "Lagerfachhelfer/-in", "Lagerfachhelfer", "Lagerfachhelferin", null, null)
		}));

		/** Fachklasse Landwirtschaftsfachwerker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_835_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(952000, 10, "835", "00", false, "A", "AW", "AW", null, "MB", "Landwirtschaftsfachwerker/-in", "Landwirtschaftsfachwerker", "Landwirtschaftsfachwerkerin", null, null)
		}));

		/** Fachklasse Maschinenwerker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_836_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(953000, 10, "836", "00", false, "T", "MT", "TN", null, "MB", "Maschinenwerker/-in", "Maschinenwerker", "Maschinenwerkerin", null, null)
		}));

		/** Fachklasse Metallbearbeiter/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_837_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(954000, 10, "837", "00", false, "T", "MT", "TN", null, "MB", "Metallbearbeiter/-in", "Metallbearbeiter", "Metallbearbeiterin", null, null)
		}));

		/** Fachklasse Metallfeinbearbeiter/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_838_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(955000, 10, "838", "00", false, "T", "MT", "TN", null, "MB", "Metallfeinbearbeiter/-in", "Metallfeinbearbeiter", "Metallfeinbearbeiterin", null, null)
		}));

		/** Fachklasse Metallwerker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_839_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(956000, 10, "839", "00", false, "T", "MT", "TN", null, "MB", "Metallwerker/-in", "Metallwerker", "Metallwerkerin", null, null)
		}));

		/** Fachklasse Näher/-in im Damenschneiderhandwerk */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_840_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(957000, 10, "840", "00", false, "T", "TB", "TN", null, "MB", "Näher/-in im Damenschneiderhandwerk", "Näher im Damenschneiderhandwerk", "Näherin im Damenschneiderhandwerk", null, null)
		}));

		/** Fachklasse Sägewerksgehilfe/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_841_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(958000, 10, "841", "00", true, "T", "HT", null, null, null, "Sägewerksgehilfe/-in", "Sägewerksgehilfe", "Sägewerksgehilfin", null, 2012)
		}));

		/** Fachklasse Schäftemacher/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_842_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(959000, 10, "842", "00", false, "T", "TB", "TN", null, "MB", "Schäftemacher/-in", "Schäftemacher", "Schäftemacherin", null, null)
		}));

		/** Fachklasse Schlosserwerker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_843_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(960000, 10, "843", "00", false, "T", "MT", "TN", null, "MB", "Schlosserwerker/-in", "Schlosserwerker", "Schlosserwerkerin", null, null)
		}));

		/** Fachklasse Schreibfachkraft für Blinde und hochgradig Sehbehinderte */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_844_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(961000, 10, "844", "00", false, "W", "WV", "WV", null, "MB", "Schreibfachkraft für Blinde und hochgradig Sehbehinderte", "Schreibfachkraft für Blinde und hochgradig Sehbehinderte", "Schreibfachkraft für Blinde und hochgradig Sehbehinderte", null, null)
		}));

		/** Fachklasse Teilkoch/-köchin */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_845_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(962000, 10, "845", "00", false, "E", "EH", "EH", null, "MB", "Teilkoch/-köchin", "Teilkoch", "Teilköchin", null, null)
		}));

		/** Fachklasse Telefonist/-in (Blinde und wesentlich Sehbehinderte) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_846_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(963000, 10, "846", "00", false, "W", "WV", "WV", null, "MB", "Telefonist/-in (Blinde und wesentlich Sehbehinderte)", "Telefonist (Blinde und wesentlich Sehbehinderte)", "Telefonistin (Blinde und wesentlich Sehbehinderte)", null, null)
		}));

		/** Fachklasse Textilwerker/-in - Veredlung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_847_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(964000, 10, "847", "01", false, "T", "TB", "TN", null, "MB", "Textilwerker/-in - Veredlung", "Textilwerker - Veredlung", "Textilwerkerin - Veredlung", null, null)
		}));

		/** Fachklasse Textilwerker/-in - Weberei */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_847_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(965000, 10, "847", "02", false, "T", "TB", "TN", null, "MB", "Textilwerker/-in - Weberei", "Textilwerker - Weberei", "Textilwerkerin - Weberei", null, null)
		}));

		/** Fachklasse Verkaufshilfe */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_848_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(966000, 10, "848", "00", false, "W", "WV", "WV", null, "MB", "Verkaufshilfe", "Verkaufshilfe", "Verkaufshilfe", null, null)
		}));

		/** Fachklasse Verkaufshilfe im Lebensmittel - Einzelhandel */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_849_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(967000, 10, "849", "00", false, "W", "WV", "WV", null, "MB", "Verkaufshilfe im Lebensmittel - Einzelhandel", "Verkaufshilfe im Lebensmittel - Einzelhandel", "Verkaufshilfe im Lebensmittel - Einzelhandel", null, null)
		}));

		/** Fachklasse Vorrichter/-in im Buchbinderhandwerk */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_850_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(968000, 10, "850", "00", false, "T", "DT", "TN", null, "MB", "Vorrichter/-in im Buchbinderhandwerk", "Vorrichter im Buchbinderhandwerk", "Vorrichterin im Buchbinderhandwerk", null, null)
		}));

		/** Fachklasse Werker/-in im Gartenbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_851_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(969000, 10, "851", "00", false, "A", "AW", "AW", null, "MB", "Werker/-in im Gartenbau", "Werker im Gartenbau", "Werkerin im Gartenbau", null, null)
		}));

		/** Fachklasse Werkzeugmaschinenspaner/-in - Drehen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_852_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(970000, 10, "852", "01", false, "T", "MT", "TN", null, "MB", "Werkzeugmaschinenspaner/-in - Drehen", "Werkzeugmaschinenspaner - Drehen", "Werkzeugmaschinenspanerin - Drehen", null, null)
		}));

		/** Fachklasse Werkzeugmaschinenspaner/-in - Fräsen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_852_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(971000, 10, "852", "02", false, "T", "MT", "TN", null, "MB", "Werkzeugmaschinenspaner/-in - Fräsen", "Werkzeugmaschinenspaner - Fräsen", "Werkzeugmaschinenspanerin - Fräsen", null, null)
		}));

		/** Fachklasse Werkzeugmaschinenwerker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_853_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(972000, 10, "853", "00", false, "T", "MT", "TN", null, "MB", "Werkzeugmaschinenwerker/-in", "Werkzeugmaschinenwerker", "Werkzeugmaschinenwerkerin", null, null)
		}));

		/** Fachklasse Werkzeugmaschinenwerker/-in- Bohren */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_854_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(973000, 10, "854", "01", false, "T", "MT", "TN", null, "MB", "Werkzeugmaschinenwerker/-in- Bohren", "Werkzeugmaschinenwerker - Bohren", "Werkzeugmaschinenwerkerin - Bohren", null, null)
		}));

		/** Fachklasse Werkzeugmaschinenwerker/-in- Drehen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_854_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(974000, 10, "854", "02", false, "T", "MT", "TN", null, "MB", "Werkzeugmaschinenwerker/-in- Drehen", "Werkzeugmaschinenwerker - Drehen", "Werkzeugmaschinenwerkerin - Drehen", null, null)
		}));

		/** Fachklasse Werkzeugmaschinenwerker/-in- Fräsen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_854_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(975000, 10, "854", "03", false, "T", "MT", "TN", null, "MB", "Werkzeugmaschinenwerker/-in- Fräsen", "Werkzeugmaschinenwerker - Fräsen", "Werkzeugmaschinenwerkerin - Fräsen", null, null)
		}));

		/** Fachklasse Werkzeugmaschinenwerker/-in - Schleifen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_854_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(976000, 10, "854", "04", false, "T", "MT", "TN", null, "MB", "Werkzeugmaschinenwerker/-in - Schleifen", "Werkzeugmaschinenwerker - Schleifen", "Werkzeugmaschinenwerkerin - Schleifen", null, null)
		}));

		/** Fachklasse Zweiradmechanikerwerker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_855_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(977000, 10, "855", "00", false, "T", "MT", "TN", null, "MB", "Zweiradmechanikerwerker/-in", "Zweiradmechanikerwerker", "Zweiradmechanikerwerkerin", null, null)
		}));

		/** Fachklasse Textilreinigungswerker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_856_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(978000, 10, "856", "00", false, "T", "TB", "TN", null, "MB", "Textilreinigungswerker/-in", "Textilreinigungswerker", "Textilreinigungswerkerin", null, null)
		}));

		/** Fachklasse Recyclingwerker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_857_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(979000, 10, "857", "00", false, "T", "CP", "TN", null, "MB", "Recyclingwerker/-in", "Recyclingwerker", "Recyclingwerkerin", null, null)
		}));

		/** Fachklasse Gartenbaufachwerker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_858_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(980000, 10, "858", "00", true, "A", "AW", null, null, null, "Gartenbaufachwerker/-in", "Gartenbaufachwerker", "Gartenbaufachwerkerin", null, 2014)
		}));

		/** Fachklasse Malerfachwerker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_859_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(981000, 10, "859", "00", false, "G", "FR", "TN", null, "MB", "Malerfachwerker/-in", "Malerfachwerker", "Malerfachwerkerin", null, null)
		}));

		/** Fachklasse Mechaniker/-in für Umformtechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_860_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(982000, 10, "860", "00", true, "T", "MT", null, null, null, "Mechaniker/-in für Umformtechnik", "Mechaniker für Umformtechnik", "Mechanikerin für Umformtechnik", null, 2012)
		}));

		/** Fachklasse Nachrichtengerätemechaniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_861_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(983000, 10, "861", "00", false, "T", "MT", "TN", null, "MB", "Nachrichtengerätemechaniker/-in", "Nachrichtengerätemechaniker", "Nachrichtengerätemechanikerin", null, null)
		}));

		/** Fachklasse Hauswartsgehilfe/-gehilfin */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_862_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(984000, 10, "862", "00", false, "O", "OH", "EH", null, "MB", "Hauswartsgehilfe/-gehilfin", "Hauswartsgehilfe", "Hauswartsgehilfin", null, null)
		}));

		/** Fachklasse Ausbaufachwerker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_863_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(985000, 10, "863", "00", false, "T", "BT", "TN", null, "MB", "Ausbaufachwerker/-in", "Ausbaufachwerker", "Ausbaufachwerkerin", null, null)
		}));

		/** Fachklasse Flechtwerker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_864_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(986000, 10, "864", "00", false, "T", "HT", "TN", null, "MB", "Flechtwerker/-in", "Flechtwerker", "Flechtwerkerin", null, null)
		}));

		/** Fachklasse Fachkraft für Fahrzeugpflege */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_865_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(987000, 10, "865", "00", false, "A", "AW", "TN", null, "MB", "Fachkraft für Fahrzeugpflege", "Fachkraft für Fahrzeugpflege", "Fachkraft für Fahrzeugpflege", null, null)
		}));

		/** Fachklasse Autofachwerker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_866_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(988000, 10, "866", "00", false, "T", "MT", "TN", null, "MB", "Autofachwerker/-in", "Autofachwerker", "Autofachwerkerin", null, null)
		}));

		/** Fachklasse Fachgehilfe/-gehilfin im Nahrungsmittelverkauf */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_867_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(989000, 10, "867", "00", false, "E", "EH", "WV", null, "MB", "Fachgehilfe/-gehilfin im Nahrungsmittelverkauf", "Fachgehilfe im Nahrungsmittelverkauf", "Fachgehilfin im Nahrungsmittelverkauf", null, null)
		}));

		/** Fachklasse Fräser-Fachwerker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_868_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(990000, 10, "868", "00", false, "T", "MT", "TN", null, "MB", "Fräser-Fachwerker/-in", "Fräser-Fachwerker", "Fräserin-Fachwerkerin", null, null)
		}));

		/** Fachklasse Gebäudereinigerwerker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_869_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(991000, 10, "869", "00", false, "O", "OH", "TN", null, "MB", "Gebäudereinigerwerker/-in", "Gebäudereinigerwerker", "Gebäudereinigerwerkerin", null, null)
		}));

		/** Fachklasse Schweißwerker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_870_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(992000, 10, "870", "00", false, "T", "MT", "TN", null, "MB", "Schweißwerker/-in", "Schweißwerker", "Schweißwerkerin", null, null)
		}));

		/** Fachklasse Tiefbaufachwerker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_871_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(993000, 10, "871", "00", false, "T", "BT", "TN", null, "MB", "Tiefbaufachwerker/-in", "Tiefbaufachwerker", "Tiefbaufachwerkerin", null, null)
		}));

		/** Fachklasse Werker/-in in der Forstwirtschaft, Wald- und Landschaftspflege */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_872_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(994000, 10, "872", "00", false, "A", "AW", "AW", null, "MB", "Werker/-in in der Forstwirtschaft, Wald- und Landschaftspflege", "Werker in der Forstwirtschaft, Wald- und Landschaftspflege", "Werkerin in der Forstwirtschaft, Wald- und Landschaftspflege", null, null)
		}));

		/** Fachklasse Fahrzeugpfleger/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_873_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(995000, 10, "873", "00", false, "T", "FT", "TN", null, "MB", "Fahrzeugpfleger/-in", "Fahrzeugpfleger", "Fahrzeugpflegerin", null, null)
		}));

		/** Fachklasse Fachkraft für Sanitär-, Heizungs- und Klimatechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_874_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(996000, 10, "874", "00", false, "T", "MT", "TN", null, "MB", "Fachkraft für Sanitär-, Heizungs- und Klimatechnik", "Fachkraft für Sanitär-, Heizungs- und Klimatechnik", "Fachkraft für Sanitär-, Heizungs- und Klimatechnik", null, null)
		}));

		/** Fachklasse Helfer/-in im Gastgewerbe */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_875_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(997000, 10, "875", "00", false, "E", "EH", "EH", null, "MB", "Helfer/-in im Gastgewerbe", "Helfer im Gastgewerbe", "Helferin im Gastgewerbe", null, null)
		}));

		/** Fachklasse IT-Werker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_876_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(998000, 10, "876", "00", false, "O", "OH", "WV", null, "MB", "IT-Werker/-in", "IT-Werker", "IT-Werkerin", null, null)
		}));

		/** Fachklasse Fachpraktiker/-in im Verkauf */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_877_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(999000, 10, "877", "00", false, "W", "WV", "WV", null, "MB", "Fachpraktiker/-in im Verkauf", "Fachpraktiker im Verkauf", "Fachpraktikerin im Verkauf", null, null)
		}));

		/** Fachklasse Fachpraktiker/-in Küche (Beikoch) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_878_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1000000, 10, "878", "00", false, "E", "EH", "EH", null, "MB", "Fachpraktiker/-in Küche (Beikoch)", "Fachpraktiker Küche (Beikoch)", "Fachpraktikerin Küche (Beikoch)", null, null)
		}));

		/** Fachklasse Fachpraktiker/-in für personale Dienstleistungen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_879_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1001000, 10, "879", "00", false, "O", "OH", "WV", null, "MB", "Fachpraktiker/-in für personale Dienstleistungen", "Fachpraktiker für personale Dienstleistungen", "Fachpraktikerin für personale Dienstleistungen", null, null)
		}));

		/** Fachklasse Fachpraktiker/-in für Industrieelektrik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_880_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1002000, 10, "880", "00", false, "O", "OH", "TN", null, "MB", "Fachpraktiker/-in für Industrieelektrik", "Fachpraktiker für Industrieelektrik", "Fachpraktikerin für Industrieelektrik", null, null)
		}));

		/** Fachklasse Elektrogerätemechaniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_881_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1003000, 10, "881", "00", false, "T", "ET", "TN", null, "MB", "Elektrogerätemechaniker/-in", "Elektrogerätemechaniker", "Elektrogerätemechanikerin", null, null)
		}));

		/** Fachklasse Fachpraktiker/-in für Baugruppenmechanik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_882_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1004000, 10, "882", "00", false, "O", "OH", "TN", null, "MB", "Fachpraktiker/-in für Baugruppenmechanik", "Fachpraktiker für Baugruppenmechanik", "Fachpraktikerin für Baugruppenmechanik", null, null)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 10 (Teil 11)
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex10_Teil11(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Fachpraktiker/-in für Bürokommunikation */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_883_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1005000, 10, "883", "00", false, "O", "OH", "WV", null, "MB", "Fachpraktiker/-in für Bürokommunikation", "Fachpraktiker für Bürokommunikation", "Fachpraktikerin für Bürokommunikation", null, null)
		}));

		/** Fachklasse Fachpraktiker/-in für Metallbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_884_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1006000, 10, "884", "00", false, "O", "OH", "TN", null, "MB", "Fachpraktiker/-in für Metallbau", "Fachpraktiker für Metallbau", "Fachpraktikerin für Metallbau", null, null)
		}));

		/** Fachklasse Fachpraktiker/-in für Holzverarbeitung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_885_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1007000, 10, "885", "00", false, "T", "HT", "TN", null, "MB", "Fachpraktiker/-in für Holzverarbeitung", "Fachpraktiker für Holzverarbeitung", "Fachpraktikerin für Holzverarbeitung", null, null)
		}));

		/** Fachklasse Fachpraktiker/-in für Zerspanungsmechanik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_886_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1008000, 10, "886", "00", false, "T", "MT", "TN", null, "MB", "Fachpraktiker/-in für Zerspanungsmechanik", "Fachpraktiker für Zerspanungsmechanik", "Fachpraktikerin für Zerspanungsmechanik", null, null)
		}));

		/** Fachklasse Fachpraktiker/-in für Tiefbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_887_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1009000, 10, "887", "00", false, "T", "BT", "TN", null, "MB", "Fachpraktiker/-in für Tiefbau", "Fachpraktiker für Tiefbau", "Fachpraktikerin für Tiefbau", null, null)
		}));

		/** Fachklasse Fachpraktiker/-in im Hochbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_888_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1010000, 10, "888", "00", false, "T", "BT", "TN", null, "MB", "Fachpraktiker/-in im Hochbau", "Fachpraktiker im Hochbau", "Fachpraktikerin im Hochbau", null, null)
		}));

		/** Fachklasse Fachpraktiker/-in Service in sozialen Einrichtungen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_889_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1011000, 10, "889", "00", false, "O", "OH", "TN", null, "MB", "Fachpraktiker/-in Service in sozialen Einrichtungen", "Fachpraktiker Service in sozialen Einrichtungen", "Fachpraktikerin Service in sozialen Einrichtungen", null, null)
		}));

		/** Fachklasse Fachpraktiker/in im Lagerbereich */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_890_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1012000, 10, "890", "00", false, "O", "OH", "WV", null, "MB", "Fachpraktiker/in im Lagerbereich", "Fachpraktiker im Lagerbereich", "Fachpraktikerin im Lagerbereich", null, null)
		}));

		/** Fachklasse Fachpraktiker/-in Hauswirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_891_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1013000, 10, "891", "00", false, "O", "OH", "EH", null, "MB", "Fachpraktiker/-in Hauswirtschaft", "Fachpraktiker Hauswirtschaft", "Fachpraktikerin Hauswirtschaft", null, null)
		}));

		/** Fachklasse Fachpraktiker/-in für Möbel-, Küchen- und Umzugsservice */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_892_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1014000, 10, "892", "00", false, null, null, "TN", null, "MB", "Fachpraktiker/-in für Möbel-, Küchen- und Umzugsservice", "Fachpraktiker für Möbel-, Küchen- und Umzugsservice", "Fachpraktikerin für Möbel-, Küchen- und Umzugsservice", null, null)
		}));

		/** Fachklasse Fachpraktiker/-in im Lebensmittelverkauf */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_893_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1015000, 10, "893", "00", false, null, null, "WV", null, "MB", "Fachpraktiker/-in im Lebensmittelverkauf", "Fachpraktiker im Lebensmittelverkauf", "Fachpraktikerin im Lebensmittelverkauf", null, null)
		}));

		/** Fachklasse Fachpraktiker/-in für Zerspanungstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_894_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1016000, 10, "894", "00", false, "T", "MT", "TN", null, "MB", "Fachpraktiker/-in für Zerspanungstechnik", "Fachpraktiker für Zerspanungstechnik", "Fachpraktikerin für Zerspanungstechnik", null, null)
		}));

		/** Fachklasse Fachpraktiker/-in für technische Produktkonstruktion und  Dokumentation */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_895_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1017000, 10, "895", "00", false, null, null, "TN", null, "MB", "Fachpraktiker/-in für technische Produktkonstruktion und  Dokumentation", "Fachpraktiker für technische Produktkonstruktion und  Dokumentation", "Fachpraktikerin für technische Produktkonstruktion und  Dokumentation", null, null)
		}));

		/** Fachklasse Fachpraktikerin/Fachpraktiker für personenbezogene Serviceleistungen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_896_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1018000, 10, "896", "00", false, null, null, "WV", null, "MB", "Fachpraktikerin/Fachpraktiker für personenbezogene Serviceleistungen", "Fachpraktiker für personenbezogene Serviceleistungen", "Fachpraktikerin für personenbezogene Serviceleistungen", null, null)
		}));

		/** Fachklasse Fachpraktiker/-in für Gebäudereiniger */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_897_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1019000, 10, "897", "00", false, null, null, "TN", null, "MB", "Fachpraktiker/-in für Gebäudereiniger", "Fachpraktiker für Gebäudereiniger", "Fachpraktikerin für Gebäudereiniger", null, null)
		}));

		/** Fachklasse Fachpraktiker/-in für Kreislauf- und Abfallwirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_898_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1020000, 10, "898", "00", false, null, null, "WV", null, "MB", "Fachpraktiker/-in für Kreislauf- und Abfallwirtschaft", "Fachpraktiker für Kreislauf- und Abfallwirtschaft", "Fachpraktikerin für Kreislauf- und Abfallwirtschaft", null, null)
		}));

		/** Fachklasse Fachpraktiker/-in für Informationstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_899_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1021000, 10, "899", "00", false, null, null, "TN", null, "MB", "Fachpraktiker/-in für Informationstechnik", "Fachpraktiker für Informationstechnik", "Fachpraktikerin für Informationstechnik", null, null)
		}));

		/** Fachklasse Fachpraktiker/-in im Gebäudeservice */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_900_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1022000, 10, "900", "00", false, null, null, "TN", null, "MB", "Fachpraktiker/-in im Gebäudeservice", "Fachpraktiker im Gebäudeservice", "Fachpraktikerin im Gebäudeservice", null, null)
		}));

		/** Fachklasse Sonstiger Ausbildungsberuf Agrarwirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_999_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1023000, 10, "999", "01", false, "A", "AW", "XX", "XX", "XX", "Sonstiger Ausbildungsberuf Agrarwirtschaft", "Sonstiger Ausbildungsberuf Agrarwirtschaft", "Sonstiger Ausbildungsberuf Agrarwirtschaft", 2022, null)
		}));

		/** Fachklasse Sonstiger Ausbildungsberuf Bautechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_999_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1024000, 10, "999", "02", false, "T", "BT", "XX", "XX", "XX", "Sonstiger Ausbildungsberuf Bautechnik", "Sonstiger Ausbildungsberuf Bautechnik", "Sonstiger Ausbildungsberuf Bautechnik", 2022, null)
		}));

		/** Fachklasse Sonstiger Ausbildungsberuf Chemie, Physik und Biologie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_999_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1025000, 10, "999", "03", false, "T", "CP", "XX", "XX", "XX", "Sonstiger Ausbildungsberuf Chemie, Physik und Biologie", "Sonstiger Ausbildungsberuf Chemie, Physik und Biologie", "Sonstiger Ausbildungsberuf Chemie, Physik und Biologie", 2022, null)
		}));

		/** Fachklasse Sonstiger Ausbildungsberuf Drucktechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_999_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1026000, 10, "999", "04", false, "T", "DT", "XX", "XX", "XX", "Sonstiger Ausbildungsberuf Drucktechnik", "Sonstiger Ausbildungsberuf Drucktechnik", "Sonstiger Ausbildungsberuf Drucktechnik", 2022, null)
		}));

		/** Fachklasse Sonstiger Ausbildungsberuf Elektrotechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_999_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1027000, 10, "999", "05", false, "T", "ET", "XX", "XX", "XX", "Sonstiger Ausbildungsberuf Elektrotechnik", "Sonstiger Ausbildungsberuf Elektrotechnik", "Sonstiger Ausbildungsberuf Elektrotechnik", 2022, null)
		}));

		/** Fachklasse Sonstiger Ausbildungsberuf Ernährung und Hauswirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_999_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1028000, 10, "999", "06", false, "E", "EH", "XX", "XX", "XX", "Sonstiger Ausbildungsberuf Ernährung und Hauswirtschaft", "Sonstiger Ausbildungsberuf Ernährung und Hauswirtschaft", "Sonstiger Ausbildungsberuf Ernährung und Hauswirtschaft", 2022, null)
		}));

		/** Fachklasse Sonstiger Ausbildungsberuf Farbtechnik und Raumgestaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_999_07", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1029000, 10, "999", "07", false, "G", "FR", "XX", "XX", "XX", "Sonstiger Ausbildungsberuf Farbtechnik und Raumgestaltung", "Sonstiger Ausbildungsberuf Farbtechnik und Raumgestaltung", "Sonstiger Ausbildungsberuf Farbtechnik und Raumgestaltung", 2022, null)
		}));

		/** Fachklasse Sonstiger Ausbildungsberuf Gesundheit */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_999_08", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1030000, 10, "999", "08", false, "S", "GH", "XX", "XX", "XX", "Sonstiger Ausbildungsberuf Gesundheit", "Sonstiger Ausbildungsberuf Gesundheit", "Sonstiger Ausbildungsberuf Gesundheit", 2022, null)
		}));

		/** Fachklasse Sonstiger Ausbildungsberuf Holztechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_999_09", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1031000, 10, "999", "09", false, "T", "HT", "XX", "XX", "XX", "Sonstiger Ausbildungsberuf Holztechnik", "Sonstiger Ausbildungsberuf Holztechnik", "Sonstiger Ausbildungsberuf Holztechnik", 2022, null)
		}));

		/** Fachklasse Sonstiger Ausbildungsberuf Metalltechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_999_10", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1032000, 10, "999", "10", false, "T", "MT", "XX", "XX", "XX", "Sonstiger Ausbildungsberuf Metalltechnik", "Sonstiger Ausbildungsberuf Metalltechnik", "Sonstiger Ausbildungsberuf Metalltechnik", 2022, null)
		}));

		/** Fachklasse Sonstiger Ausbildungsberuf ohne Berufsfeld */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_999_11", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1033000, 10, "999", "11", false, "X", "XX", "XX", "XX", "XX", "Sonstiger Ausbildungsberuf ohne Berufsfeld", "Sonstiger Ausbildungsberuf ohne Berufsfeld", "Sonstiger Ausbildungsberuf ohne Berufsfeld", 2022, null)
		}));

		/** Fachklasse Sonstiger Ausbildungsberuf Textiltechnik und Bekleidung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_999_12", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1034000, 10, "999", "12", false, "T", "TB", "XX", "XX", "XX", "Sonstiger Ausbildungsberuf Textiltechnik und Bekleidung", "Sonstiger Ausbildungsberuf Textiltechnik und Bekleidung", "Sonstiger Ausbildungsberuf Textiltechnik und Bekleidung", 2022, null)
		}));

		/** Fachklasse Sonstiger Ausbildungsberuf Wirtschaft und Verwaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_10_999_13", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1035000, 10, "999", "13", false, "W", "WV", "XX", "XX", "XX", "Sonstiger Ausbildungsberuf Wirtschaft und Verwaltung", "Sonstiger Ausbildungsberuf Wirtschaft und Verwaltung", "Sonstiger Ausbildungsberuf Wirtschaft und Verwaltung", 2022, null)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 100
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex100(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Assistent/-in für Betriebsinformatik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1036000, 100, "101", "00", true, "W", "WV", null, null, null, "Assistent/-in für Betriebsinformatik", "Assistent für Betriebsinformatik", "Assistentin für Betriebsinformatik", null, 2019)
		}));

		/** Fachklasse Bautechnische/-r Assistent/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_102_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1037000, 100, "102", "00", true, "T", "BH", null, null, null, "Bautechnische/-r Assistent/-in", "Bautechnischer Assistent", "Bautechnische Assistentin", null, 2016)
		}));

		/** Fachklasse Bautechnische/-r Assistent/-in - Denkmalpflege */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_102_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1038000, 100, "102", "01", false, "T", "BH", "TN", null, null, "Bautechnische/-r Assistent/-in - Denkmalpflege", "Bautechnischer Assistent - Denkmalpflege", "Bautechnische Assistentin - Denkmalpflege", null, null)
		}));

		/** Fachklasse Bautechnische/-r Assistent/-in - Hoch-/Tiefbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_102_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1039000, 100, "102", "02", false, "T", "BH", "TN", null, null, "Bautechnische/-r Assistent/-in - Hoch-/Tiefbau", "Bautechnischer Assistent - Hoch-/Tiefbau", "Bautechnische Assistentin - Hoch-/Tiefbau", null, null)
		}));

		/** Fachklasse Bekleidungstechnische/-r Assistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_103_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1040000, 100, "103", "00", false, "T", "TB", "TN", null, null, "Bekleidungstechnische/-r Assistent/-in", "Bekleidungstechnischer Assistent", "Bekleidungstechnische Assistentin", null, null)
		}));

		/** Fachklasse Biologisch-technische/-r Assistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_104_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1041000, 100, "104", "00", false, "T", "CP", "TN", null, null, "Biologisch-technische/-r Assistent/-in", "Biologisch-technischer Assistent", "Biologisch-technische Assistentin", null, null)
		}));

		/** Fachklasse Chemisch-technische/-r Assistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_105_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1042000, 100, "105", "00", false, "T", "CP", "TN", null, null, "Chemisch-technische/-r Assistent/-in", "Chemisch-technischer Assistent", "Chemisch-technische Assistentin", null, null)
		}));

		/** Fachklasse Denkmaltechnische/-r Assistent/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_106_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1043000, 100, "106", "00", true, "T", "BH", null, null, null, "Denkmaltechnische/-r Assistent/-in", "Denkmaltechnischer Assistent", "Denkmaltechnische Assistentin", null, 2016)
		}));

		/** Fachklasse Diätassistent/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_107_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1044000, 100, "107", "00", true, "E", "EH", null, null, null, "Diätassistent/-in", "Diätassistent", "Diätassistentin", null, 2012)
		}));

		/** Fachklasse Elektronikassistent/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_108_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1045000, 100, "108", "00", true, "T", "ET", null, null, null, "Elektronikassistent/-in", "Elektronikassistent", "Elektronikassistentin", null, 2012)
		}));

		/** Fachklasse Elektrotechnische/-r Assistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_109_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1046000, 100, "109", "00", false, "T", "ET", "TN", null, null, "Elektrotechnische/-r Assistent/-in", "Elektrotechnischer Assistent", "Elektrotechnische Assistentin", null, null)
		}));

		/** Fachklasse Erzieher/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_110_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1047000, 100, "110", "00", true, "S", "SG", null, null, null, "Erzieher/-in", "Erzieher", "Erzieherin", null, 2012)
		}));

		/** Fachklasse Freizeitsportleiter/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_111_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1048000, 100, "111", "00", true, "S", "SG", null, null, null, "Freizeitsportleiter/-in", "Freizeitsportleiter", "Freizeitsportleiterin", null, 2012)
		}));

		/** Fachklasse Fremdsprachensekretär/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_112_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1049000, 100, "112", "00", true, "W", "WV", null, null, null, "Fremdsprachensekretär/-in", "Fremdsprachensekretär", "Fremdsprachensekretärin", null, 2012)
		}));

		/** Fachklasse Gestaltungstechnische/-r Assistent/-in - Grafik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_113_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1050000, 100, "113", "01", true, "G", "GS", null, null, null, "Gestaltungstechnische/-r Assistent/-in - Grafik", "Gestaltungstechnischer Assistent - Grafik", "Gestaltungstechnische Assistentin - Grafik", null, 2012)
		}));

		/** Fachklasse Gestaltungstechnische/-r Assistent/-in - Medien/Kommunikation */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_113_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1051000, 100, "113", "02", false, "G", "GS", "GT", null, null, "Gestaltungstechnische/-r Assistent/-in - Medien/Kommunikation", "Gestaltungstechnischer Assistent - Medien/Kommunikation", "Gestaltungstechnische Assistentin - Medien/Kommunikation", null, null)
		}));

		/** Fachklasse Gestaltungstechnische/-r Assistent/-in - Produkt/Design (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_113_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1052000, 100, "113", "03", true, "G", "GS", null, null, null, "Gestaltungstechnische/-r Assistent/-in - Produkt/Design", "Gestaltungstechnischer Assistent - Produkt/Design", "Gestaltungstechnische Assistentin - Produkt/Design", null, 2012)
		}));

		/** Fachklasse Gestaltungstechnische/-r Assistent/-in - Digital-, Print- und audiovisuelle Medien (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_113_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1053000, 100, "113", "04", true, "G", "GS", null, null, null, "Gestaltungstechnische/-r Assistent/-in - Digital-, Print- und audiovisuelle Medien", "Gestaltungstechnischer Assistent - Digital-, Print- und audiovisuelle Medien", "Gestaltungstechnische Assistentin - Digital-, Print- und audiovisuelle Medien", null, 2012)
		}));

		/** Fachklasse Gestaltungstechnische/-r Assistent/-in - Grafikdesign und Objektdesign */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_113_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1054000, 100, "113", "05", false, "G", "GS", "GT", null, null, "Gestaltungstechnische/-r Assistent/-in - Grafikdesign und Objektdesign", "Gestaltungstechnischer Assistent - Grafikdesign und Objektdesign", "Gestaltungstechnische Assistentin - Grafikdesign und Objektdesign", null, null)
		}));

		/** Fachklasse Gymnastiklehrer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_114_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1055000, 100, "114", "00", false, "S", "SG", "GS", null, null, "Gymnastiklehrer/-in", "Gymnastiklehrer", "Gymnastiklehrerin", null, null)
		}));

		/** Fachklasse Hauswirtschaftlich-technische/-r Assistent/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_115_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1056000, 100, "115", "00", true, "E", "EH", null, null, null, "Hauswirtschaftlich-technische/-r Assistent/-in", "Hauswirtschaftlich-technischer Assistent", "Hauswirtschaftlich-technische Assistentin", null, 2016)
		}));

		/** Fachklasse Informationstechnische/-r Assistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_116_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1057000, 100, "116", "00", false, "O", "OH", "IF", null, null, "Informationstechnische/-r Assistent/-in", "Informationstechnischer Assistent", "Informationstechnische Assistentin", null, null)
		}));

		/** Fachklasse Kaufmännische/-r Assistent/-in - Betriebswirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_117_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1058000, 100, "117", "01", false, "W", "WV", "WV", null, null, "Kaufmännische/-r Assistent/-in - Betriebswirtschaft", "Kaufmännischer Assistent - Betriebswirtschaft", "Kaufmännische Assistentin - Betriebswirtschaft", null, null)
		}));

		/** Fachklasse Kaufmännische/-r Assistent/-in f. Bürowirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_117_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1059000, 100, "117", "02", true, "W", "WV", null, null, null, "Kaufmännische/-r Assistent/-in f. Bürowirtschaft", "Kaufmännischer Assistent f. Bürowirtschaft", "Kaufmännische Assistentin f. Bürowirtschaft", null, 2012)
		}));

		/** Fachklasse Kaufmännische/-r Assistent/-in - Fremdsprachen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_117_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1060000, 100, "117", "03", false, "W", "WV", "WV", null, null, "Kaufmännische/-r Assistent/-in - Fremdsprachen", "Kaufmännischer Assistent - Fremdsprachen", "Kaufmännische Assistentin - Fremdsprachen", null, null)
		}));

		/** Fachklasse Kaufmännische/-r Assistent/-in - Informationsverarbeitung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_117_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1061000, 100, "117", "04", false, "W", "WV", "WV", null, null, "Kaufmännische/-r Assistent/-in - Informationsverarbeitung", "Kaufmännischer Assistent - Informationsverarbeitung", "Kaufmännische Assistentin - Informationsverarbeitung", null, null)
		}));

		/** Fachklasse Kaufmännische/-r Assistent/-in - Betriebsinformatik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_117_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1062000, 100, "117", "05", false, "W", "WV", "WV", null, null, "Kaufmännische/-r Assistent/-in - Betriebsinformatik", "Kaufmännischer Assistent - Betriebsinformatik", "Kaufmännische Assistentin - Betriebsinformatik", null, null)
		}));

		/** Fachklasse Kinderpfleger/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_118_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1063000, 100, "118", "00", true, "S", "SG", null, null, null, "Kinderpfleger/-in", "Kinderpfleger", "Kinderpflegerin", null, 2012)
		}));

		/** Fachklasse Konstruktions- und Fertigungstechnische/-r Assistent/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_119_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1064000, 100, "119", "00", true, "T", "MB", null, null, null, "Konstruktions- und Fertigungstechnische/-r Assistent/-in", "Konstruktions- und Fertigungstechnischer Assistent", "Konstruktions- und Fertigungstechnische Assistentin", null, 2017)
		}));

		/** Fachklasse Kosmetiker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_120_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1065000, 100, "120", "00", false, "S", "KP", "GS", null, null, "Kosmetiker/-in", "Kosmetiker", "Kosmetikerin", null, null)
		}));

		/** Fachklasse Lebensmitteltechnische/-r Assistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_121_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1066000, 100, "121", "00", false, "E", "EH", "EH", null, null, "Lebensmitteltechnische/-r Assistent/-in", "Lebensmitteltechnischer Assistent", "Lebensmitteltechnische Assistentin", null, null)
		}));

		/** Fachklasse Maschinenbautechnische/-r Assistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_122_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1067000, 100, "122", "00", false, "T", "MT", "TN", null, null, "Maschinenbautechnische/-r Assistent/-in", "Maschinenbautechnischer Assistent", "Maschinenbautechnische Assistentin", null, null)
		}));

		/** Fachklasse Medizinisch-technische/-r Assistent/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_123_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1068000, 100, "123", "00", true, "T", "MZ", null, null, null, "Medizinisch-technische/-r Assistent/-in", "Medizinisch-technischer Assistent", "Medizinisch-technische Assistentin", null, 2012)
		}));

		/** Fachklasse Physikalisch-technische/-r Assistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_124_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1069000, 100, "124", "00", false, "T", "CP", "TN", null, null, "Physikalisch-technische/-r Assistent/-in", "Physikalisch-technischer Assistent", "Physikalisch-technische Assistentin", null, null)
		}));

		/** Fachklasse Physikalisch-technische/-r Assistent/-in - Metallografie und Werkstoffkunde */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_124_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1070000, 100, "124", "01", false, "T", "MT", "TN", null, null, "Physikalisch-technische/-r Assistent/-in - Metallografie und Werkstoffkunde", "Physikalisch-technischer Assistent - Metallografie und Werkstoffkunde", "Physikalisch-technische Assistentin - Metallografie und Werkstoffkunde", null, null)
		}));

		/** Fachklasse Präparationstechnische/-r Assistent/-in - Biologie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_125_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1071000, 100, "125", "01", false, "T", "CP", "TN", null, null, "Präparationstechnische/-r Assistent/-in - Biologie", "Präparationstechnischer Assistent - Biologie", "Präparationstechnische Assistentin - Biologie", null, null)
		}));

		/** Fachklasse Präparationstechnische/-r Assistent/-in - Geowissenschaften */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_125_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1072000, 100, "125", "02", false, "T", "CP", "TN", null, null, "Präparationstechnische/-r Assistent/-in - Geowissenschaften", "Präparationstechnischer Assistent - Geowissenschaften", "Präparationstechnische Assistentin - Geowissenschaften", null, null)
		}));

		/** Fachklasse Präparationstechnische/-r Assistent/-in - Medizin */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_125_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1073000, 100, "125", "03", false, "T", "CP", "TN", null, null, "Präparationstechnische/-r Assistent/-in - Medizin", "Präparationstechnischer Assistent - Medizin", "Präparationstechnische Assistentin - Medizin", null, null)
		}));

		/** Fachklasse Technische/-r Assistent/-in für Metallografie und Werkstoffkunde (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_126_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1074000, 100, "126", "00", true, "T", "MT", null, null, null, "Technische/-r Assistent/-in für Metallografie und Werkstoffkunde", "Technischer Assistent für Metallografie und Werkstoffkunde", "Technische Assistentin für Metallografie und Werkstoffkunde", null, 2016)
		}));

		/** Fachklasse Technische/-r Assistent/-in / Kosmetik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_127_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1075000, 100, "127", "00", true, "S", "KP", null, null, null, "Technische/-r Assistent/-in / Kosmetik", "Technischer Assistent / Kosmetik", "Technische Assistentin / Kosmetik", null, 2012)
		}));

		/** Fachklasse Textiltechnische/-r Assistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_128_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1076000, 100, "128", "00", false, "T", "TB", "TN", null, null, "Textiltechnische/-r Assistent/-in", "Textiltechnischer Assistent", "Textiltechnische Assistentin", null, null)
		}));

		/** Fachklasse Umweltschutztechnische/-r Assistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_129_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1077000, 100, "129", "00", false, "T", "CP", "TN", null, null, "Umweltschutztechnische/-r Assistent/-in", "Umweltschutztechnischer Assistent", "Umweltschutztechnische Assistentin", null, null)
		}));

		/** Fachklasse Wirtschaftassistent/-in (kaufmännischer Assistent/-in) (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_130_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1078000, 100, "130", "00", true, "W", "WV", null, null, null, "Wirtschaftassistent/-in (kaufmännischer Assistent/-in)", "Wirtschaftassistent (kaufmännischer Assistent)", "Wirtschaftassistentin (kaufmännischer Assistentin)", null, 2012)
		}));

		/** Fachklasse Informatiker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_131_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1079000, 100, "131", "00", true, "O", "IF", null, null, null, "Informatiker/-in", "Informatiker", "Informatikerin", null, 2017)
		}));

		/** Fachklasse Informatiker/-in - Medizinökonomie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_131_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1080000, 100, "131", "01", false, "O", "OH", "IF", null, null, "Informatiker/-in - Medizinökonomie", "Informatiker - Medizinökonomie", "Informatikerin - Medizinökonomie", null, null)
		}));

		/** Fachklasse Informatiker/-in - Multimedia */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_131_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1081000, 100, "131", "02", false, "O", "OH", "IF", null, null, "Informatiker/-in - Multimedia", "Informatiker - Multimedia", "Informatikerin - Multimedia", null, null)
		}));

		/** Fachklasse Informatiker/-in - Softwareentwicklung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_131_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1082000, 100, "131", "03", false, "O", "OH", "IF", null, null, "Informatiker/-in - Softwareentwicklung", "Informatiker - Softwareentwicklung", "Informatikerin - Softwareentwicklung", null, null)
		}));

		/** Fachklasse Informatiker/-in - Wirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_131_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1083000, 100, "131", "04", false, "O", "OH", "IF", null, null, "Informatiker/-in - Wirtschaft", "Informatiker - Wirtschaft", "Informatikerin - Wirtschaft", null, null)
		}));

		/** Fachklasse Technische/-r Assistent/-in für regenerative Energietechnik und Energiemanagement */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_132_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1084000, 100, "132", "00", false, "T", "TC", "TN", null, null, "Technische/-r Assistent/-in für regenerative Energietechnik und Energiemanagement", "Technischer Assistent für regenerative Energietechnik und Energiemanagement", "Technische Assistentin für regenerative Energietechnik und Energiemanagement", null, null)
		}));

		/** Fachklasse Assistent/-in für Ernährung und Versorgung - Technik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_133_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1085000, 100, "133", "01", false, "E", "EH", "EH", null, null, "Assistent/-in für Ernährung und Versorgung - Technik", "Assistent für Ernährung und Versorgung - Technik", "Assistentin für Ernährung und Versorgung - Technik", null, null)
		}));

		/** Fachklasse Sonstiger Bildungsgang */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1086000, 100, "999", "00", false, "X", "XX", "XX", "XX", "XX", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", 2022, null)
		}));

		/** Fachklasse Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_100_999_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1087000, 100, "999", "01", true, "X", "XX", null, null, null, "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, 2014)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 110
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex110(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Assistent/-in für Betriebsinformatik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1088000, 110, "101", "00", true, "W", "WV", null, null, null, "Assistent/-in für Betriebsinformatik", "Assistent für Betriebsinformatik", "Assistentin für Betriebsinformatik", null, 2015)
		}));

		/** Fachklasse Bautechnische/-r Assistent/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_102_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1089000, 110, "102", "00", true, "T", "BH", null, null, null, "Bautechnische/-r Assistent/-in", "Bautechnischer Assistent", "Bautechnische Assistentin", null, 2012)
		}));

		/** Fachklasse Bautechnische/-r Assistent/-in - Denkmalpflege */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_102_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1090000, 110, "102", "01", false, "T", "BH", "TN", null, null, "Bautechnische/-r Assistent/-in - Denkmalpflege", "Bautechnischer Assistent - Denkmalpflege", "Bautechnische Assistentin - Denkmalpflege", null, null)
		}));

		/** Fachklasse Bautechnische/-r Assistent/-in - Hoch-/Tiefbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_102_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1091000, 110, "102", "02", false, "T", "BH", "TN", null, null, "Bautechnische/-r Assistent/-in - Hoch-/Tiefbau", "Bautechnischer Assistent - Hoch-/Tiefbau", "Bautechnische Assistentin - Hoch-/Tiefbau", null, null)
		}));

		/** Fachklasse Bekleidungstechnische/-r Assistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_103_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1092000, 110, "103", "00", false, "T", "TB", "TN", null, null, "Bekleidungstechnische/-r Assistent/-in", "Bekleidungstechnischer Assistent", "Bekleidungstechnische Assistentin", null, null)
		}));

		/** Fachklasse Biologisch-technische/-r Assistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_104_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1093000, 110, "104", "00", false, "T", "CP", "TN", null, null, "Biologisch-technische/-r Assistent/-in", "Biologisch-technischer Assistent", "Biologisch-technische Assistentin", null, null)
		}));

		/** Fachklasse Chemisch-technische/-r Assistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_105_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1094000, 110, "105", "00", false, "T", "CP", "TN", null, null, "Chemisch-technische/-r Assistent/-in", "Chemisch-technischer Assistent", "Chemisch-technische Assistentin", null, null)
		}));

		/** Fachklasse Denkmaltechnische/-r Assistent/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_106_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1095000, 110, "106", "00", true, "T", "BH", null, null, null, "Denkmaltechnische/-r Assistent/-in", "Denkmaltechnischer Assistent", "Denkmaltechnische Assistentin", null, 2012)
		}));

		/** Fachklasse Elektrotechnische/-r Assistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_107_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1096000, 110, "107", "00", false, "T", "ET", "TN", null, null, "Elektrotechnische/-r Assistent/-in", "Elektrotechnischer Assistent", "Elektrotechnische Assistentin", null, null)
		}));

		/** Fachklasse Erzieher/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_108_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1097000, 110, "108", "00", true, "S", "SG", null, null, null, "Erzieher/-in", "Erzieher", "Erzieherin", null, 2012)
		}));

		/** Fachklasse Eurowirtschaftsassistent/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_109_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1098000, 110, "109", "00", true, "W", "WV", null, null, null, "Eurowirtschaftsassistent/-in", "Eurowirtschaftsassistent", "Eurowirtschaftsassistentin", null, 2012)
		}));

		/** Fachklasse Gestaltungstechnische/-r Assistent/-in - Grafik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_110_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1099000, 110, "110", "01", true, "G", "GS", null, null, null, "Gestaltungstechnische/-r Assistent/-in - Grafik", "Gestaltungstechnischer Assistent - Grafik", "Gestaltungstechnische Assistentin - Grafik", null, 2012)
		}));

		/** Fachklasse Gestaltungstechnische/-r Assistent/-in - Medien/Kommunikation */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_110_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1100000, 110, "110", "02", false, "G", "GS", "GT", null, null, "Gestaltungstechnische/-r Assistent/-in - Medien/Kommunikation", "Gestaltungstechnischer Assistent - Medien/Kommunikation", "Gestaltungstechnische Assistentin - Medien/Kommunikation", null, null)
		}));

		/** Fachklasse Gestaltungstechnische/-r Assistent/-in - Produkt/Design (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_110_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1101000, 110, "110", "03", true, "G", "GS", null, null, null, "Gestaltungstechnische/-r Assistent/-in - Produkt/Design", "Gestaltungstechnischer Assistent - Produkt/Design", "Gestaltungstechnische Assistentin - Produkt/Design", null, 2012)
		}));

		/** Fachklasse Gestaltungstechnische/-r Assistent/-in - Digital-, Print- und audiovisuelle Medien (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_110_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1102000, 110, "110", "04", true, "G", "GS", null, null, null, "Gestaltungstechnische/-r Assistent/-in - Digital-, Print- und audiovisuelle Medien", "Gestaltungstechnischer Assistent - Digital-, Print- und audiovisuelle Medien", "Gestaltungstechnische Assistentin - Digital-, Print- und audiovisuelle Medien", null, 2012)
		}));

		/** Fachklasse Gestaltungstechnische/-r Assistent/-in - Grafikdesign und Objektdesign */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_110_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1103000, 110, "110", "05", false, "G", "GS", "GT", null, null, "Gestaltungstechnische/-r Assistent/-in - Grafikdesign und Objektdesign", "Gestaltungstechnischer Assistent - Grafikdesign und Objektdesign", "Gestaltungstechnische Assistentin - Grafikdesign und Objektdesign", null, null)
		}));

		/** Fachklasse Hauswirtschaftlich-technische/-r Assistent/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_111_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1104000, 110, "111", "00", true, "E", "EH", null, null, null, "Hauswirtschaftlich-technische/-r Assistent/-in", "Hauswirtschaftlich-technischer Assistent", "Hauswirtschaftlich-technische Assistentin", null, 2015)
		}));

		/** Fachklasse Hotel- u. Gaststättengewerbe (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_112_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1105000, 110, "112", "00", true, "W", "WV", null, null, null, "Hotel- u. Gaststättengewerbe", "Hotel- u. Gaststättengewerbe", "Hotel- u. Gaststättengewerbe", null, 2005)
		}));

		/** Fachklasse Industrietechnologe/-technologin */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_113_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1106000, 110, "113", "00", false, "O", "OH", "TN", null, null, "Industrietechnologe/-technologin", "Industrietechnologe", "Industrietechnologin", null, null)
		}));

		/** Fachklasse Informatikassistent/-in - Büromanagement (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_114_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1107000, 110, "114", "01", true, "O", "OH", null, null, null, "Informatikassistent/-in - Büromanagement", "Informatikassistent - Büromanagement", "Informatikassistentin - Büromanagement", null, 2012)
		}));

		/** Fachklasse Informatiker/-in - Medizinökonomie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_114_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1108000, 110, "114", "02", false, "O", "OH", "IF", null, null, "Informatiker/-in - Medizinökonomie", "Informatiker - Medizinökonomie", "Informatikerin - Medizinökonomie", null, null)
		}));

		/** Fachklasse Informatiker/-in - Multimedia */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_114_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1109000, 110, "114", "03", false, "O", "OH", "IF", null, null, "Informatiker/-in - Multimedia", "Informatiker - Multimedia", "Informatikerin - Multimedia", null, null)
		}));

		/** Fachklasse Informatiker/-in - Softwareentwicklung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_114_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1110000, 110, "114", "04", false, "O", "OH", "IF", null, null, "Informatiker/-in - Softwareentwicklung", "Informatiker - Softwareentwicklung", "Informatikerin - Softwareentwicklung", null, null)
		}));

		/** Fachklasse Informatiker/-in - Wirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_114_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1111000, 110, "114", "05", false, "O", "OH", "IF", null, null, "Informatiker/-in - Wirtschaft", "Informatiker - Wirtschaft", "Informatikerin - Wirtschaft", null, null)
		}));

		/** Fachklasse Informationsassistent/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_115_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1112000, 110, "115", "00", true, "O", "OH", null, null, null, "Informationsassistent/-in", "Informationsassistent", "Informationsassistentin", null, 2012)
		}));

		/** Fachklasse Informationstechnische/-r Assistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_116_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1113000, 110, "116", "00", false, "O", "OH", "IF", null, null, "Informationstechnische/-r Assistent/-in", "Informationstechnischer Assistent", "Informationstechnische Assistentin", null, null)
		}));

		/** Fachklasse Kaufmännische/-r Assistent/-in - Betriebswirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_117_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1114000, 110, "117", "01", false, "W", "WV", "WV", null, null, "Kaufmännische/-r Assistent/-in - Betriebswirtschaft", "Kaufmännischer Assistent - Betriebswirtschaft", "Kaufmännische Assistentin - Betriebswirtschaft", null, null)
		}));

		/** Fachklasse Kaufmännische/-r Assistent/-in f. Bürowirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_117_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1115000, 110, "117", "02", true, "W", "WV", null, null, null, "Kaufmännische/-r Assistent/-in f. Bürowirtschaft", "Kaufmännischer Assistent f. Bürowirtschaft", "Kaufmännische Assistentin f. Bürowirtschaft", null, 2012)
		}));

		/** Fachklasse Kaufmännische/-r Assistent/-in - Fremdsprachen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_117_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1116000, 110, "117", "03", false, "W", "WV", "WV", null, null, "Kaufmännische/-r Assistent/-in - Fremdsprachen", "Kaufmännischer Assistent - Fremdsprachen", "Kaufmännische Assistentin - Fremdsprachen", null, null)
		}));

		/** Fachklasse Kaufmännische/-r Assistent/-in - Informationsverarbeitung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_117_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1117000, 110, "117", "04", false, "W", "WV", "WV", null, null, "Kaufmännische/-r Assistent/-in - Informationsverarbeitung", "Kaufmännischer Assistent - Informationsverarbeitung", "Kaufmännische Assistentin - Informationsverarbeitung", null, null)
		}));

		/** Fachklasse Kaufmännische/-r Assistent/-in - Betriebsinformatik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_117_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1118000, 110, "117", "05", false, "W", "WV", "WV", null, null, "Kaufmännische/-r Assistent/-in - Betriebsinformatik", "Kaufmännischer Assistent - Betriebsinformatik", "Kaufmännische Assistentin - Betriebsinformatik", null, null)
		}));

		/** Fachklasse Konstruktions- und Fertigungstechnische/-r Assistent/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_118_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1119000, 110, "118", "00", true, "T", "MB", null, null, null, "Konstruktions- und Fertigungstechnische/-r Assistent/-in", "Konstruktions- und Fertigungstechnischer Assistent", "Konstruktions- und Fertigungstechnische Assistentin", null, 2012)
		}));

		/** Fachklasse Kosmetiker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_119_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1120000, 110, "119", "00", false, "S", "KP", "GS", null, null, "Kosmetiker/-in", "Kosmetiker", "Kosmetikerin", null, null)
		}));

		/** Fachklasse Lebensmitteltechnische/-r Assistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_120_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1121000, 110, "120", "00", false, "E", "EH", "EH", null, null, "Lebensmitteltechnische/-r Assistent/-in", "Lebensmitteltechnischer Assistent", "Lebensmitteltechnische Assistentin", null, null)
		}));

		/** Fachklasse Maschinenbautechnische/-r Assistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_121_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1122000, 110, "121", "00", false, "T", "MT", "TN", null, null, "Maschinenbautechnische/-r Assistent/-in", "Maschinenbautechnischer Assistent", "Maschinenbautechnische Assistentin", null, null)
		}));

		/** Fachklasse Medizinisch-technische/r Assistent/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_122_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1123000, 110, "122", "00", true, "T", "MZ", null, null, null, "Medizinisch-technische/r Assistent/-in", "Medizinisch-technischer Assistent", "Medizinisch-technische Assistentin", null, 2012)
		}));

		/** Fachklasse Physikalisch-technische/-r Assistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_123_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1124000, 110, "123", "00", false, "T", "CP", "TN", null, null, "Physikalisch-technische/-r Assistent/-in", "Physikalisch-technischer Assistent", "Physikalisch-technische Assistentin", null, null)
		}));

		/** Fachklasse Physikalisch-technische/-r Assistent/-in - Metallografie und Werkstoffkunde */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_123_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1125000, 110, "123", "01", false, "T", "MT", "TN", null, null, "Physikalisch-technische/-r Assistent/-in - Metallografie und Werkstoffkunde", "Physikalisch-technischer Assistent - Metallografie und Werkstoffkunde", "Physikalisch-technische Assistentin - Metallografie und Werkstoffkunde", null, null)
		}));

		/** Fachklasse Präparationstechnische/-r Assistent/-in - Biologie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_124_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1126000, 110, "124", "01", false, "T", "CP", "TN", null, null, "Präparationstechnische/-r Assistent/-in - Biologie", "Präparationstechnischer Assistent - Biologie", "Präparationstechnische Assistentin - Biologie", null, null)
		}));

		/** Fachklasse Präparationstechnische/-r Assistent/-in - Geowissenschaften */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_124_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1127000, 110, "124", "02", false, "T", "CP", "TN", null, null, "Präparationstechnische/-r Assistent/-in - Geowissenschaften", "Präparationstechnischer Assistent - Geowissenschaften", "Präparationstechnische Assistentin - Geowissenschaften", null, null)
		}));

		/** Fachklasse Präparationstechnische/-r Assistent/-in - Medizin */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_124_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1128000, 110, "124", "03", false, "T", "CP", "TN", null, null, "Präparationstechnische/-r Assistent/-in - Medizin", "Präparationstechnischer Assistent - Medizin", "Präparationstechnische Assistentin - Medizin", null, null)
		}));

		/** Fachklasse Technische/-r Assistent/-in für Metallografie und Werkstoffkunde (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_125_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1129000, 110, "125", "00", true, "T", "MT", null, null, null, "Technische/-r Assistent/-in für Metallografie und Werkstoffkunde", "Technischer Assistent für Metallografie und Werkstoffkunde", "Technische Assistentin für Metallografie und Werkstoffkunde", null, 2012)
		}));

		/** Fachklasse Textiltechnische/-r Assistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_126_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1130000, 110, "126", "00", false, "T", "TB", "TN", null, null, "Textiltechnische/-r Assistent/-in", "Textiltechnischer Assistent", "Textiltechnische Assistentin", null, null)
		}));

		/** Fachklasse Umweltschutztechnische/-r Assistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_127_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1131000, 110, "127", "00", false, "T", "CP", "TN", null, null, "Umweltschutztechnische/-r Assistent/-in", "Umweltschutztechnischer Assistent", "Umweltschutztechnische Assistentin", null, null)
		}));

		/** Fachklasse Wirtschaftsassistent/-in Informatik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_128_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1132000, 110, "128", "00", true, "W", "WV", null, null, null, "Wirtschaftsassistent/-in Informatik", "Wirtschaftsassistent Informatik", "Wirtschaftsassistentin Informatik", null, 2012)
		}));

		/** Fachklasse Assistent/-in für Ernährung und Versorgung - Technik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_129_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1133000, 110, "129", "01", false, "E", "EH", "EH", null, null, "Assistent/-in für Ernährung und Versorgung - Technik", "Assistent für Ernährung und Versorgung - Technik", "Assistentin für Ernährung und Versorgung - Technik", null, null)
		}));

		/** Fachklasse Sonstiger Bildungsgang */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1134000, 110, "999", "00", false, "X", "XX", "XX", "XX", "XX", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", 2022, null)
		}));

		/** Fachklasse Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_110_999_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1135000, 110, "999", "01", true, "X", "XX", null, null, null, "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, 2014)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 120
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex120(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Agrarwirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_120_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1136000, 120, "101", "00", false, "A", "AW", "AW", null, null, "Agrarwirtschaft", "Agrarwirtschaft", "Agrarwirtschaft", null, null)
		}));

		/** Fachklasse Bau- und Holztechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_120_102_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1137000, 120, "102", "00", false, "T", "BH", "TN", "BH", null, "Bau- und Holztechnik", "Bau- und Holztechnik", "Bau- und Holztechnik", null, null)
		}));

		/** Fachklasse Berufliche Kenntnisse/Fachhochschulreife (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_120_103_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1138000, 120, "103", "00", true, "W", "WV", null, null, null, "Berufliche Kenntnisse/Fachhochschulreife", "Berufliche Kenntnisse/Fachhochschulreife", "Berufliche Kenntnisse/Fachhochschulreife", null, 2010)
		}));

		/** Fachklasse Drucktechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_120_104_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1139000, 120, "104", "00", false, "T", "DT", "TN", "DT", null, "Drucktechnik", "Drucktechnik", "Drucktechnik", null, null)
		}));

		/** Fachklasse Elektrotechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_120_105_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1140000, 120, "105", "00", false, "T", "ET", "TN", "ET", null, "Elektrotechnik", "Elektrotechnik", "Elektrotechnik", null, null)
		}));

		/** Fachklasse Ernährung/Hauswirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_120_106_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1141000, 120, "106", "00", false, "E", "EH", "EH", null, null, "Ernährung/Hauswirtschaft", "Ernährung/Hauswirtschaft", "Ernährung/Hauswirtschaft", null, null)
		}));

		/** Fachklasse Gestaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_120_107_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1142000, 120, "107", "00", false, "G", "GS", "GT", null, null, "Gestaltung", "Gestaltung", "Gestaltung", null, null)
		}));

		/** Fachklasse Metalltechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_120_108_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1143000, 120, "108", "00", false, "T", "MT", "TN", "ME", null, "Metalltechnik", "Metalltechnik", "Metalltechnik", null, null)
		}));

		/** Fachklasse Physik, Chemie, Biologie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_120_109_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1144000, 120, "109", "00", true, "T", "CP", null, null, null, "Physik, Chemie, Biologie", "Physik, Chemie, Biologie", "Physik, Chemie, Biologie", null, 2010)
		}));

		/** Fachklasse Gesundheit und Soziales */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_120_110_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1145000, 120, "110", "00", false, "S", "SG", "GS", null, null, "Gesundheit und Soziales", "Gesundheit und Soziales", "Gesundheit und Soziales", null, null)
		}));

		/** Fachklasse Textiltechnik und Bekleidung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_120_111_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1146000, 120, "111", "00", false, "T", "TB", "TN", "TB", null, "Textiltechnik und Bekleidung", "Textiltechnik und Bekleidung", "Textiltechnik und Bekleidung", null, null)
		}));

		/** Fachklasse Wirtschaft und Verwaltung (Höhere Handelsschule) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_120_112_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1147000, 120, "112", "00", false, "W", "WV", "WV", null, null, "Wirtschaft und Verwaltung (Höhere Handelsschule)", "Wirtschaft und Verwaltung (Höhere Handelsschule)", "Wirtschaft und Verwaltung (Höhere Handelsschule)", null, null)
		}));

		/** Fachklasse Labor- und Verfahrenstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_120_113_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1148000, 120, "113", "00", false, "T", "CP", "TN", "LV", null, "Labor- und Verfahrenstechnik", "Labor- und Verfahrenstechnik", "Labor- und Verfahrenstechnik", null, null)
		}));

		/** Fachklasse Energie-/Automatisierungstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_120_114_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1149000, 120, "114", "00", false, null, null, "TN", "ET", null, "Energie-/Automatisierungstechnik", "Energie-/Automatisierungstechnik", "Energie-/Automatisierungstechnik", null, null)
		}));

		/** Fachklasse Informations- und Kommunikationstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_120_115_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1150000, 120, "115", "00", false, null, null, "TN", "ET", null, "Informations- und Kommunikationstechnik", "Informations- und Kommunikationstechnik", "Informations- und Kommunikationstechnik", null, null)
		}));

		/** Fachklasse Sonstiger Bildungsgang */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_120_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1151000, 120, "999", "00", false, "X", "XX", "XX", "XX", "XX", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", 2022, null)
		}));

		/** Fachklasse Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_120_999_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1152000, 120, "999", "01", true, "X", "XX", null, null, null, "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, 2014)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 130
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex130(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Agrarwirtschaft, Bio- und Umwelttechnologie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_130_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1153000, 130, "101", "00", true, "A", "AW", null, null, null, "Agrarwirtschaft, Bio- und Umwelttechnologie", "Agrarwirtschaft, Bio- und Umwelttechnologie", "Agrarwirtschaft, Bio- und Umwelttechnologie", null, 2015)
		}));

		/** Fachklasse Bau- und Holztechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_130_102_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1154000, 130, "102", "00", true, "T", "BH", null, null, null, "Bau- und Holztechnik", "Bau- und Holztechnik", "Bau- und Holztechnik", null, 2015)
		}));

		/** Fachklasse Drucktechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_130_103_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1155000, 130, "103", "00", true, "T", "DT", null, null, null, "Drucktechnik", "Drucktechnik", "Drucktechnik", null, 2015)
		}));

		/** Fachklasse Elektrotechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_130_104_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1156000, 130, "104", "00", true, "T", "ET", null, null, null, "Elektrotechnik", "Elektrotechnik", "Elektrotechnik", null, 2015)
		}));

		/** Fachklasse Ernährung und Hauswirschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_130_105_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1157000, 130, "105", "00", true, "E", "EH", null, null, null, "Ernährung und Hauswirschaft", "Ernährung und Hauswirschaft", "Ernährung und Hauswirschaft", null, 2015)
		}));

		/** Fachklasse Fremdsprachenkorrespondent/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_130_106_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1158000, 130, "106", "00", true, "W", "WV", null, null, null, "Fremdsprachenkorrespondent/-in", "Fremdsprachenkorrespondent", "Fremdsprachenkorrespondentin", null, 2015)
		}));

		/** Fachklasse Gestaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_130_107_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1159000, 130, "107", "00", true, "G", "GS", null, null, null, "Gestaltung", "Gestaltung", "Gestaltung", null, 2015)
		}));

		/** Fachklasse Metalltechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_130_108_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1160000, 130, "108", "00", true, "T", "MT", null, null, null, "Metalltechnik", "Metalltechnik", "Metalltechnik", null, 2015)
		}));

		/** Fachklasse Physik, Chemie, Biologie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_130_109_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1161000, 130, "109", "00", true, "T", "CP", null, null, null, "Physik, Chemie, Biologie", "Physik, Chemie, Biologie", "Physik, Chemie, Biologie", null, 2014)
		}));

		/** Fachklasse Gesundheit und Soziales (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_130_110_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1162000, 130, "110", "00", true, "S", "SG", null, null, null, "Gesundheit und Soziales", "Gesundheit und Soziales", "Gesundheit und Soziales", null, 2015)
		}));

		/** Fachklasse Textiltechnik und Bekleidung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_130_111_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1163000, 130, "111", "00", true, "T", "TB", null, null, null, "Textiltechnik und Bekleidung", "Textiltechnik und Bekleidung", "Textiltechnik und Bekleidung", null, 2015)
		}));

		/** Fachklasse Wirtschaft und Verwaltung (Höhere Handelsschule) (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_130_112_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1164000, 130, "112", "00", true, "W", "WV", null, null, null, "Wirtschaft und Verwaltung (Höhere Handelsschule)", "Wirtschaft und Verwaltung (Höhere Handelsschule)", "Wirtschaft und Verwaltung (Höhere Handelsschule)", null, 2015)
		}));

		/** Fachklasse Labor- und Verfahrenstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_130_113_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1165000, 130, "113", "00", true, "T", "CP", null, null, null, "Labor- und Verfahrenstechnik", "Labor- und Verfahrenstechnik", "Labor- und Verfahrenstechnik", null, 2015)
		}));

		/** Fachklasse Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_130_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1166000, 130, "999", "00", true, "X", "XX", null, null, null, "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, 2014)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 140
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex140(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Agrarwirtschaft, Bio- und Umwelttechnologie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_140_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1167000, 140, "101", "00", false, "A", "AW", "AB", null, null, "Agrarwirtschaft, Bio- und Umwelttechnologie", "Agrarwirtschaft, Bio- und Umwelttechnologie", "Agrarwirtschaft, Bio- und Umwelttechnologie", null, null)
		}));

		/** Fachklasse Bau- und Holztechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_140_102_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1168000, 140, "102", "00", false, "T", "BH", "TE", "BH", null, "Bau- und Holztechnik", "Bau- und Holztechnik", "Bau- und Holztechnik", null, null)
		}));

		/** Fachklasse Drucktechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_140_103_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1169000, 140, "103", "00", false, "T", "DT", "TE", "DT", null, "Drucktechnik", "Drucktechnik", "Drucktechnik", null, null)
		}));

		/** Fachklasse Elektrotechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_140_104_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1170000, 140, "104", "00", false, "T", "ET", "TE", "ET", null, "Elektrotechnik", "Elektrotechnik", "Elektrotechnik", null, null)
		}));

		/** Fachklasse Ernährung und Hauswirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_140_105_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1171000, 140, "105", "00", false, "E", "EH", "EU", null, null, "Ernährung und Hauswirtschaft", "Ernährung und Hauswirtschaft", "Ernährung und Hauswirtschaft", null, null)
		}));

		/** Fachklasse Gestaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_140_106_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1172000, 140, "106", "00", false, "G", "GS", "GT", null, null, "Gestaltung", "Gestaltung", "Gestaltung", null, null)
		}));

		/** Fachklasse Metalltechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_140_107_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1173000, 140, "107", "00", false, "T", "MT", "TE", "ME", null, "Metalltechnik", "Metalltechnik", "Metalltechnik", null, null)
		}));

		/** Fachklasse Metalltechnik - Fahrzeugtechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_140_107_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1174000, 140, "107", "01", true, "T", "MT", null, null, null, "Metalltechnik - Fahrzeugtechnik", "Metalltechnik - Fahrzeugtechnik", "Metalltechnik - Fahrzeugtechnik", null, 2021)
		}));

		/** Fachklasse Physik, Chemie, Biologie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_140_108_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1175000, 140, "108", "00", false, "T", "CP", "TE", "PU", null, "Physik, Chemie, Biologie", "Physik, Chemie, Biologie", "Physik, Chemie, Biologie", null, null)
		}));

		/** Fachklasse Gesundheit und Soziales */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_140_109_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1176000, 140, "109", "00", false, "S", "SG", "GU", null, null, "Gesundheit und Soziales", "Gesundheit und Soziales", "Gesundheit und Soziales", null, null)
		}));

		/** Fachklasse Sozialpädagogik u. Sozialarbeit (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_140_110_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1177000, 140, "110", "00", true, "S", "SG", null, null, null, "Sozialpädagogik u. Sozialarbeit", "Sozialpädagogik u. Sozialarbeit", "Sozialpädagogik u. Sozialarbeit", null, 2005)
		}));

		/** Fachklasse Textiltechnik und Bekleidung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_140_111_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1178000, 140, "111", "00", false, "T", "TB", "TE", "TB", null, "Textiltechnik und Bekleidung", "Textiltechnik und Bekleidung", "Textiltechnik und Bekleidung", null, null)
		}));

		/** Fachklasse Wirtschaft und Verwaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_140_112_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1179000, 140, "112", "00", false, "W", "WV", "WV", null, null, "Wirtschaft und Verwaltung", "Wirtschaft und Verwaltung", "Wirtschaft und Verwaltung", null, null)
		}));

		/** Fachklasse Wirtschaft und Verwaltung - Polizeivollzugsdienst */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_140_112_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1180000, 140, "112", "01", false, null, null, "WV", null, null, "Wirtschaft und Verwaltung - Polizeivollzugsdienst", "Wirtschaft und Verwaltung - Polizeivollzugsdienst", "Wirtschaft und Verwaltung - Polizeivollzugsdienst", 2022, null)
		}));

		/** Fachklasse Vermessungstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_140_113_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1181000, 140, "113", "00", true, "T", "VT", null, null, null, "Vermessungstechnik", "Vermessungstechnik", "Vermessungstechnik", null, 2012)
		}));

		/** Fachklasse Labor- und Verfahrenstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_140_114_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1182000, 140, "114", "00", true, "T", "CP", null, null, null, "Labor- und Verfahrenstechnik", "Labor- und Verfahrenstechnik", "Labor- und Verfahrenstechnik", null, 2013)
		}));

		/** Fachklasse Informatik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_140_115_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1183000, 140, "115", "00", false, null, null, "WV", null, null, "Informatik", "Informatik", "Informatik", null, null)
		}));

		/** Fachklasse Sonstiger Bildungsgang */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_140_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1184000, 140, "999", "00", false, "X", "XX", "XX", "XX", "XX", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", 2022, null)
		}));

		/** Fachklasse Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_140_999_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1185000, 140, "999", "01", true, "X", "XX", null, null, null, "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, 2014)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 141
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex141(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Agrarwirtschaft, Bio- und Umwelttechnologie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_141_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1186000, 141, "101", "00", false, null, null, "AB", null, null, "Agrarwirtschaft, Bio- und Umwelttechnologie", "Agrarwirtschaft, Bio- und Umwelttechnologie", "Agrarwirtschaft, Bio- und Umwelttechnologie", null, null)
		}));

		/** Fachklasse Bau- und Holztechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_141_102_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1187000, 141, "102", "00", false, null, null, "TE", "BH", null, "Bau- und Holztechnik", "Bau- und Holztechnik", "Bau- und Holztechnik", null, null)
		}));

		/** Fachklasse Drucktechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_141_103_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1188000, 141, "103", "00", false, null, null, "TE", "DT", null, "Drucktechnik", "Drucktechnik", "Drucktechnik", null, null)
		}));

		/** Fachklasse Elektrotechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_141_104_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1189000, 141, "104", "00", false, null, null, "TE", "ET", null, "Elektrotechnik", "Elektrotechnik", "Elektrotechnik", null, null)
		}));

		/** Fachklasse Ernährung und Hauswirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_141_105_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1190000, 141, "105", "00", false, null, null, "EU", null, null, "Ernährung und Hauswirtschaft", "Ernährung und Hauswirtschaft", "Ernährung und Hauswirtschaft", null, null)
		}));

		/** Fachklasse Gestaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_141_106_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1191000, 141, "106", "00", false, null, null, "GT", null, null, "Gestaltung", "Gestaltung", "Gestaltung", null, null)
		}));

		/** Fachklasse Metalltechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_141_107_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1192000, 141, "107", "00", false, null, null, "TE", "ME", null, "Metalltechnik", "Metalltechnik", "Metalltechnik", null, null)
		}));

		/** Fachklasse Physik, Chemie, Biologie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_141_108_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1193000, 141, "108", "00", false, null, null, "TE", "PU", null, "Physik, Chemie, Biologie", "Physik, Chemie, Biologie", "Physik, Chemie, Biologie", null, null)
		}));

		/** Fachklasse Gesundheit und Soziales */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_141_109_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1194000, 141, "109", "00", false, null, null, "GU", null, null, "Gesundheit und Soziales", "Gesundheit und Soziales", "Gesundheit und Soziales", null, null)
		}));

		/** Fachklasse Textiltechnik und Bekleidung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_141_111_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1195000, 141, "111", "00", false, null, null, "TE", "TB", null, "Textiltechnik und Bekleidung", "Textiltechnik und Bekleidung", "Textiltechnik und Bekleidung", null, null)
		}));

		/** Fachklasse Wirtschaft und Verwaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_141_112_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1196000, 141, "112", "00", false, null, null, "WV", null, null, "Wirtschaft und Verwaltung", "Wirtschaft und Verwaltung", "Wirtschaft und Verwaltung", null, null)
		}));

		/** Fachklasse Sonstiger Bildungsgang */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_141_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1197000, 141, "999", "00", false, null, null, "XX", "XX", "XX", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", 2022, null)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 145
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex145(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Bautechnische/-r Assistent/-in - Denkmalpflege */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_145_102_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1198000, 145, "102", "01", false, null, null, "TN", null, null, "Bautechnische/-r Assistent/-in - Denkmalpflege", "Bautechnischer Assistent - Denkmalpflege", "Bautechnische Assistentin - Denkmalpflege", null, null)
		}));

		/** Fachklasse Bautechnische/-r Assistent/-in - Hoch-/Tiefbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_145_102_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1199000, 145, "102", "02", false, null, null, "TN", null, null, "Bautechnische/-r Assistent/-in - Hoch-/Tiefbau", "Bautechnischer Assistent - Hoch-/Tiefbau", "Bautechnische Assistentin - Hoch-/Tiefbau", null, null)
		}));

		/** Fachklasse Bekleidungstechnische/-r Assistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_145_103_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1200000, 145, "103", "00", false, null, null, "TN", null, null, "Bekleidungstechnische/-r Assistent/-in", "Bekleidungstechnischer Assistent", "Bekleidungstechnische Assistentin", null, null)
		}));

		/** Fachklasse Biologisch-technische/-r Assistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_145_104_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1201000, 145, "104", "00", false, null, null, "TN", null, null, "Biologisch-technische/-r Assistent/-in", "Biologisch-technischer Assistent", "Biologisch-technische Assistentin", null, null)
		}));

		/** Fachklasse Chemisch-technische/-r Assistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_145_105_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1202000, 145, "105", "00", false, null, null, "TN", null, null, "Chemisch-technische/-r Assistent/-in", "Chemisch-technischer Assistent", "Chemisch-technische Assistentin", null, null)
		}));

		/** Fachklasse Elektrotechnische/-r Assistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_145_109_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1203000, 145, "109", "00", false, null, null, "TN", null, null, "Elektrotechnische/-r Assistent/-in", "Elektrotechnischer Assistent", "Elektrotechnische Assistentin", null, null)
		}));

		/** Fachklasse Gestaltungstechnische/-r Assistent/-in - Medien/Kommunikation */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_145_113_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1204000, 145, "113", "02", false, null, null, "GT", null, null, "Gestaltungstechnische/-r Assistent/-in - Medien/Kommunikation", "Gestaltungstechnischer Assistent - Medien/Kommunikation", "Gestaltungstechnische Assistentin - Medien/Kommunikation", null, null)
		}));

		/** Fachklasse Gestaltungstechnische/-r Assistent/-in - Grafikdesign und Objektdesign */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_145_113_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1205000, 145, "113", "05", false, null, null, "GT", null, null, "Gestaltungstechnische/-r Assistent/-in - Grafikdesign und Objektdesign", "Gestaltungstechnischer Assistent - Grafikdesign und Objektdesign", "Gestaltungstechnische Assistentin - Grafikdesign und Objektdesign", null, null)
		}));

		/** Fachklasse Gymnastiklehrer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_145_114_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1206000, 145, "114", "00", false, null, null, "GS", null, null, "Gymnastiklehrer/-in", "Gymnastiklehrer", "Gymnastiklehrerin", null, null)
		}));

		/** Fachklasse Informationstechnische/-r Assistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_145_116_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1207000, 145, "116", "00", false, null, null, "IF", null, null, "Informationstechnische/-r Assistent/-in", "Informationstechnischer Assistent", "Informationstechnischer Assistentin", null, null)
		}));

		/** Fachklasse Kaufmännische/-r Assistent/-in - Betriebswirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_145_117_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1208000, 145, "117", "01", false, null, null, "WV", null, null, "Kaufmännische/-r Assistent/-in - Betriebswirtschaft", "Kaufmännischer Assistent - Betriebswirtschaft", "Kaufmännische Assistentin - Betriebswirtschaft", null, null)
		}));

		/** Fachklasse Kaufmännische/-r Assistent/-in - Fremdsprachen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_145_117_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1209000, 145, "117", "03", false, null, null, "WV", null, null, "Kaufmännische/-r Assistent/-in - Fremdsprachen", "Kaufmännischer Assistent - Fremdsprachen", "Kaufmännische Assistentin - Fremdsprachen", null, null)
		}));

		/** Fachklasse Kaufmännische/-r Assistent/-in - Informationsverarbeitung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_145_117_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1210000, 145, "117", "04", false, null, null, "WV", null, null, "Kaufmännische/-r Assistent/-in - Informationsverarbeitung", "Kaufmännischer Assistent - Informationsverarbeitung", "Kaufmännische Assistentin - Informationsverarbeitung", null, null)
		}));

		/** Fachklasse Kaufmännische/-r Assistent/-in - Betriebsinformatik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_145_117_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1211000, 145, "117", "05", false, null, null, "WV", null, null, "Kaufmännische/-r Assistent/-in - Betriebsinformatik", "Kaufmännischer Assistent - Betriebsinformatik", "Kaufmännische Assistentin - Betriebsinformatik", null, null)
		}));

		/** Fachklasse Kosmetiker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_145_120_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1212000, 145, "120", "00", false, null, null, "GS", null, null, "Kosmetiker/-in", "Kosmetiker", "Kosmetikerin", null, null)
		}));

		/** Fachklasse Lebensmitteltechnische/-r Assistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_145_121_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1213000, 145, "121", "00", false, null, null, "EH", null, null, "Lebensmitteltechnische/-r Assistent/-in", "Lebensmitteltechnischer Assistent", "Lebensmitteltechnische Assistentin", null, null)
		}));

		/** Fachklasse Maschinenbautechnische/-r Assistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_145_122_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1214000, 145, "122", "00", false, null, null, "TN", null, null, "Maschinenbautechnische/-r Assistent/-in", "Maschinenbautechnischer Assistent", "Maschinenbautechnische Assistentin", null, null)
		}));

		/** Fachklasse Physikalisch-technische/-r Assistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_145_124_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1215000, 145, "124", "00", false, null, null, "TN", null, null, "Physikalisch-technische/-r Assistent/-in", "Physikalisch-technischer Assistent", "Physikalisch-technische Assistentin", null, null)
		}));

		/** Fachklasse Physikalisch-technische/-r Assistent/-in - Metallografie und Werkstoffkunde */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_145_124_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1216000, 145, "124", "01", false, null, null, "TN", null, null, "Physikalisch-technische/-r Assistent/-in - Metallografie und Werkstoffkunde", "Physikalisch-technischer Assistent - Metallografie und Werkstoffkunde", "Physikalisch-technische Assistentin - Metallografie und Werkstoffkunde", null, null)
		}));

		/** Fachklasse Präparationstechnische/-r Assistent/-in - Biologie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_145_125_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1217000, 145, "125", "01", false, null, null, "TN", null, null, "Präparationstechnische/-r Assistent/-in - Biologie", "Präparationstechnischer Assistent - Biologie", "Präparationstechnische Assistentin - Biologie", null, null)
		}));

		/** Fachklasse Präparationstechnische/-r Assistent/-in - Geowissenschaften */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_145_125_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1218000, 145, "125", "02", false, null, null, "TN", null, null, "Präparationstechnische/-r Assistent/-in - Geowissenschaften", "Präparationstechnischer Assistent - Geowissenschaften", "Präparationstechnische Assistentin - Geowissenschaften", null, null)
		}));

		/** Fachklasse Präparationstechnische/-r Assistent/-in - Medizin */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_145_125_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1219000, 145, "125", "03", false, null, null, "TN", null, null, "Präparationstechnische/-r Assistent/-in - Medizin", "Präparationstechnischer Assistent - Medizin", "Präparationstechnische Assistentin - Medizin", null, null)
		}));

		/** Fachklasse Textiltechnische/-r Assistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_145_128_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1220000, 145, "128", "00", false, null, null, "TN", null, null, "Textiltechnische/-r Assistent/-in", "Textiltechnischer Assistent", "Textiltechnische Assistentin", null, null)
		}));

		/** Fachklasse Umweltschutztechnische/-r Assistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_145_129_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1221000, 145, "129", "00", false, null, null, "TN", null, null, "Umweltschutztechnische/-r Assistent/-in", "Umweltschutztechnischer Assistent", "Umweltschutztechnische Assistentin", null, null)
		}));

		/** Fachklasse Informatiker/-in - Multimedia */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_145_131_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1222000, 145, "131", "02", false, null, null, "IF", null, null, "Informatiker/-in - Multimedia", "Informatiker - Multimedia", "Informatikerin - Multimedia", null, null)
		}));

		/** Fachklasse Technische/-r Assistent/-in für regenerative Energietechnik und Energiemanagement */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_145_132_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1223000, 145, "132", "00", false, null, null, "SV", null, null, "Technische/-r Assistent/-in für regenerative Energietechnik und Energiemanagement", "Technischer Assistent für regenerative Energietechnik und Energiemanagement", "Technische Assistentin für regenerative Energietechnik und Energiemanagement", null, null)
		}));

		/** Fachklasse Assistent/-in für Ernährung und Versorgung - Technik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_145_133_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1224000, 145, "133", "01", false, null, null, "EH", null, null, "Assistent/-in für Ernährung und Versorgung - Technik", "Assistent für Ernährung und Versorgung - Technik", "Assistentin für Ernährung und Versorgung - Technik", null, null)
		}));

		/** Fachklasse Sonstiger Bildungsgang (noch ohne Schlüssel) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_145_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1225000, 145, "999", "00", false, null, null, "XX", "XX", "XX", "Sonstiger Bildungsgang (noch ohne Schlüssel)", "Sonstiger Bildungsgang (noch ohne Schlüssel)", "Sonstiger Bildungsgang (noch ohne Schlüssel)", 2022, null)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 146
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex146(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Ingenieurtechnik (Klasse 11 und 12) (Jahrgangsstufe 01 und 02) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_146_110_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1226000, 146, "110", "00", false, "T", "TC", "TN", null, null, "Ingenieurtechnik (Klasse 11 und 12) (Jahrgangsstufe 01 und 02)", "Ingenieurtechnik (Klasse 11 und 12) (Jahrgangsstufe 01 und 02)", "Ingenieurtechnik (Klasse 11 und 12) (Jahrgangsstufe 01 und 02)", null, null)
		}));

		/** Fachklasse Ingenieurtechnik: Berufsabschluss Bautechnische/-r Assistent/-in (Klasse 13) (Jg 03) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_146_110_10", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1227000, 146, "110", "10", false, "T", "TC", "TN", "BT", "BA", "Ingenieurtechnik: Berufsabschluss Bautechnische/-r Assistent/-in (Klasse 13) (Jg 03)", "Ingenieurtechnik: Berufsabschluss Bautechnische/-r Assistent/-in (Klasse 13) (Jg 03)", "Ingenieurtechnik: Berufsabschluss Bautechnische/-r Assistent/-in (Klasse 13) (Jg 03)", null, null)
		}));

		/** Fachklasse Ingenieurtechnik: Berufsabschluss Elektrotechnische/-r Assistent/-in (Klasse 13) (Jg 03) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_146_110_11", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1228000, 146, "110", "11", false, "T", "TC", "TN", "ET", "EA", "Ingenieurtechnik: Berufsabschluss Elektrotechnische/-r Assistent/-in (Klasse 13) (Jg 03)", "Ingenieurtechnik: Berufsabschluss Elektrotechnische/-r Assistent/-in (Klasse 13) (Jg 03)", "Ingenieurtechnik: Berufsabschluss Elektrotechnische/-r Assistent/-in (Klasse 13) (Jg 03)", null, null)
		}));

		/** Fachklasse Ingenieurtechnik: Berufsabschluss Maschinenbautechnische/-r Assistent/-in (Klasse 13) (Jg 03) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_146_110_12", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1229000, 146, "110", "12", false, "T", "TC", "TN", "MB", null, "Ingenieurtechnik: Berufsabschluss Maschinenbautechnische/-r Assistent/-in (Klasse 13) (Jg 03)", "Ingenieurtechnik: Berufsabschluss Maschinenbautechnische/-r Assistent/-in (Klasse 13) (Jg 03)", "Ingenieurtechnik: Berufsabschluss Maschinenbautechnische/-r Assistent/-in (Klasse 13) (Jg 03)", null, null)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 150
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex150(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Berufspraktikum für Erzieher/-innen (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_150_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1230000, 150, "101", "00", true, "S", "SG", null, null, null, "Berufspraktikum für Erzieher/-innen", "Berufspraktikum für Erzieher", "Berufspraktikum für Erzieherinnen", null, 2014)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 160
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex160(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Assistent/-in für Konstruktions- und Fertigungstechnik / AHR */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_160_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1231000, 160, "101", "00", false, "T", "MB", "TE", "MB", "AK", "Assistent/-in für Konstruktions- und Fertigungstechnik / AHR", "Assistent für Konstruktions- und Fertigungstechnik / AHR", "Assistentin für Konstruktions- und Fertigungstechnik / AHR", null, null)
		}));

		/** Fachklasse Bautechnische/-r Assistent/-in / AHR */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_160_102_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1232000, 160, "102", "00", false, "T", "BT", "TE", "BT", "BA", "Bautechnische/-r Assistent/-in / AHR", "Bautechnischer Assistent / AHR", "Bautechnische Assistentin / AHR", null, null)
		}));

		/** Fachklasse Biologisch-technische/-r Assistent/-in / AHR */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_160_103_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1233000, 160, "103", "00", false, "T", "NW", "TE", "BI", "BS", "Biologisch-technische/-r Assistent/-in / AHR", "Biologisch-technischer Assistent / AHR", "Biologisch-technische Assistentin / AHR", null, null)
		}));

		/** Fachklasse Chemisch-technische/-r Assistent/-in / AHR */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_160_104_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1234000, 160, "104", "00", false, "T", "NW", "TE", "CT", "CA", "Chemisch-technische/-r Assistent/-in / AHR", "Chemisch-technischer Assistent / AHR", "Chemisch-technische Assistentin / AHR", null, null)
		}));

		/** Fachklasse Elektrotechnische/-r Assistent/-in / AHR */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_160_105_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1235000, 160, "105", "00", false, "T", "ET", "TE", "ET", "EA", "Elektrotechnische/-r Assistent/-in / AHR", "Elektrotechnischer Assistent / AHR", "Elektrotechnische Assistentin / AHR", null, null)
		}));

		/** Fachklasse Erzieher/-in / AHR */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_160_106_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1236000, 160, "106", "00", false, "S", "ES", "GU", "PG", "EZ", "Erzieher/-in / AHR", "Erzieher / AHR", "Erzieherin / AHR", null, null)
		}));

		/** Fachklasse Gestaltungstechnische/-r Assistent/-in / AHR */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_160_107_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1237000, 160, "107", "00", false, "G", "KM", "GT", null, "GA", "Gestaltungstechnische/-r Assistent/-in / AHR", "Gestaltungstechnischer Assistent / AHR", "Gestaltungstechnische Assistentin / AHR", null, null)
		}));

		/** Fachklasse Hauswirtschaftliche/-r Assistent/-in / AHR (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_160_108_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1238000, 160, "108", "00", true, "A", "LH", null, null, null, "Hauswirtschaftliche/-r Assistent/-in / AHR", "Hauswirtschaftlicher Assistent / AHR", "Hauswirtschaftliche Assistentin / AHR", null, 2012)
		}));

		/** Fachklasse Kaufmännische/-r Assistent/-in / AHR */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_160_109_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1239000, 160, "109", "00", false, "W", "WW", "WV", null, "KA", "Kaufmännische/-r Assistent/-in / AHR", "Kaufmännischer Assistent / AHR", "Kaufmännische Assistentin / AHR", null, null)
		}));

		/** Fachklasse Maschinenbautechnische/-r Assistent/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_160_110_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1240000, 160, "110", "00", true, "T", "MB", null, null, null, "Maschinenbautechnische/-r Assistent/-in", "Maschinenbautechnischer Assistent", "Maschinenbautechnische Assistentin", null, 2012)
		}));

		/** Fachklasse Physikalisch-technische/-r Assistent/-in / AHR */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_160_111_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1241000, 160, "111", "00", false, "T", "NW", "TE", "PT", "PT", "Physikalisch-technische/-r Assistent/-in / AHR", "Physikalisch-technischer Assistent / AHR", "Physikalisch-technische Assistentin / AHR", null, null)
		}));

		/** Fachklasse Technische/-r Assistent/-in für Betriebsinformatik / AHR */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_160_112_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1242000, 160, "112", "00", false, "W", "WW", "WV", null, "TA", "Technische/-r Assistent/-in für Betriebsinformatik / AHR", "Technischer Assistent für Betriebsinformatik / AHR", "Technischer Assistentin für Betriebsinformatik / AHR", null, null)
		}));

		/** Fachklasse Textiltechnische/-r Assistent/-in / AHR (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_160_113_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1243000, 160, "113", "00", true, "T", "TB", null, null, null, "Textiltechnische/-r Assistent/-in / AHR", "Textiltechnischer Assistent / AHR", "Textiltechnische Assistentin / AHR", null, 2012)
		}));

		/** Fachklasse Umwelttechnische/-r Assistent/-in / AHR */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_160_114_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1244000, 160, "114", "00", false, "T", "NW", "TE", "UT", "UA", "Umwelttechnische/-r Assistent/-in / AHR", "Umwelttechnischer Assistent / AHR", "Umwelttechnische Assistentin / AHR", null, null)
		}));

		/** Fachklasse Informationstechnische/-r Assistent/-in / AHR */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_160_115_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1245000, 160, "115", "00", false, "T", "IF", "IF", "TI", "IA", "Informationstechnische/-r Assistent/-in / AHR", "Informationstechnischer Assistent / AHR", "Informationstechnische Assistentin / AHR", null, null)
		}));

		/** Fachklasse Sonstiger Bildungsgang */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_160_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1246000, 160, "999", "00", false, "X", "XX", "XX", "XX", "XX", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", 2022, null)
		}));

		/** Fachklasse Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_160_999_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1247000, 160, "999", "01", true, "X", "XX", null, null, null, "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, 2014)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 170
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex170(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Allgemeine Hochschulreife / Bautechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_170_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1248000, 170, "101", "00", false, "T", "BT", "TE", "BT", "BH", "Allgemeine Hochschulreife / Bautechnik", "Allgemeine Hochschulreife / Bautechnik", "Allgemeine Hochschulreife / Bautechnik", null, null)
		}));

		/** Fachklasse Allgemeine Hochschulreife / Betriebswirtschaftslehre */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_170_102_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1249000, 170, "102", "00", false, "W", "WW", "WV", null, "BW", "Allgemeine Hochschulreife / Betriebswirtschaftslehre", "Allgemeine Hochschulreife / Betriebswirtschaftslehre", "Allgemeine Hochschulreife / Betriebswirtschaftslehre", null, null)
		}));

		/** Fachklasse Allgemeine Hochschulreife / Biologie, Chemie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_170_103_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1250000, 170, "103", "00", false, "T", "NW", "TE", "BI", "BC", "Allgemeine Hochschulreife / Biologie, Chemie", "Allgemeine Hochschulreife / Biologie, Chemie", "Allgemeine Hochschulreife / Biologie, Chemie", null, null)
		}));

		/** Fachklasse Allgemeine Hochschulreife / Chemie, Chemietechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_170_104_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1251000, 170, "104", "00", false, "T", "NW", "TE", "CT", "CC", "Allgemeine Hochschulreife / Chemie, Chemietechnik", "Allgemeine Hochschulreife / Chemie, Chemietechnik", "Allgemeine Hochschulreife / Chemie, Chemietechnik", null, null)
		}));

		/** Fachklasse Allgemeine Hochschulreife / Chemietechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_170_105_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1252000, 170, "105", "00", true, "T", "CP", null, null, null, "Allgemeine Hochschulreife / Chemietechnik", "Allgemeine Hochschulreife / Chemietechnik", "Allgemeine Hochschulreife / Chemietechnik", null, 2005)
		}));

		/** Fachklasse Allgemeine Hochschulreife / Deutsch, Englisch */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_170_106_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1253000, 170, "106", "00", false, "S", "SL", "GT", null, "DE", "Allgemeine Hochschulreife / Deutsch, Englisch", "Allgemeine Hochschulreife / Deutsch, Englisch", "Allgemeine Hochschulreife / Deutsch, Englisch", null, null)
		}));

		/** Fachklasse Allgemeine Hochschulreife / Elektrotechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_170_107_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1254000, 170, "107", "00", false, "T", "ET", "TE", "ET", "ET", "Allgemeine Hochschulreife / Elektrotechnik", "Allgemeine Hochschulreife / Elektrotechnik", "Allgemeine Hochschulreife / Elektrotechnik", null, null)
		}));

		/** Fachklasse Allgemeine Hochschulreife / Ernährung u. Hauswirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_170_108_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1255000, 170, "108", "00", true, "E", "EH", null, null, null, "Allgemeine Hochschulreife / Ernährung u. Hauswirtschaft", "Allgemeine Hochschulreife / Ernährung u. Hauswirtschaft", "Allgemeine Hochschulreife / Ernährung u. Hauswirtschaft", null, 2005)
		}));

		/** Fachklasse Allgemeine Hochschulreife / Ernährung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_170_109_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1256000, 170, "109", "00", false, "E", "EW", "ER", null, "ER", "Allgemeine Hochschulreife / Ernährung", "Allgemeine Hochschulreife / Ernährung", "Allgemeine Hochschulreife / Ernährung", null, null)
		}));

		/** Fachklasse Allgemeine Hochschulreife / Erziehungswissenschaften */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_170_110_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1257000, 170, "110", "00", false, "S", "ES", "GU", "PG", "EW", "Allgemeine Hochschulreife / Erziehungswissenschaften", "Allgemeine Hochschulreife / Erziehungswissenschaften", "Allgemeine Hochschulreife / Erziehungswissenschaften", null, null)
		}));

		/** Fachklasse Allgemeine Hochschulreife / Freizeitsportleiter/-in (Sport, Gesundheitsförderung, Biologie) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_170_111_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1258000, 170, "111", "00", false, "S", "ES", "GU", "GS", "FL", "Allgemeine Hochschulreife / Freizeitsportleiter/-in (Sport, Gesundheitsförderung, Biologie)", "Allgemeine Hochschulreife / Freizeitsportleiter (Sport, Gesundheitsförderung, Biologie)", "Allgemeine Hochschulreife / Freizeitsportleiterin (Sport, Gesundheitsförderung, Biologie)", null, null)
		}));

		/** Fachklasse Allgemeine Hochschulreife / Fremdsprachenkorrespondent/-in (Betriebswirtschaftslehre, Sprachen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_170_112_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1259000, 170, "112", "00", false, "W", "WW", "WV", null, "FK", "Allgemeine Hochschulreife / Fremdsprachenkorrespondent/-in (Betriebswirtschaftslehre, Sprachen)", "Allgemeine Hochschulreife / Fremdsprachenkorrespondent (Betriebswirtschaftslehre, Sprachen)", "Allgemeine Hochschulreife / Fremdsprachenkorrespondentin (Betriebswirtschaftslehre, Sprachen)", null, null)
		}));

		/** Fachklasse Allgemeine Hochschulreife / Kunst, Englisch */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_170_113_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1260000, 170, "113", "00", false, "G", "KM", "GT", null, "KE", "Allgemeine Hochschulreife / Kunst, Englisch", "Allgemeine Hochschulreife / Kunst, Englisch", "Allgemeine Hochschulreife / Kunst, Englisch", null, null)
		}));

		/** Fachklasse Allgemeine Hochschulreife / Maschinenbautechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_170_114_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1261000, 170, "114", "00", false, "T", "MB", "TE", "MB", "MT", "Allgemeine Hochschulreife / Maschinenbautechnik", "Allgemeine Hochschulreife / Maschinenbautechnik", "Allgemeine Hochschulreife / Maschinenbautechnik", null, null)
		}));

		/** Fachklasse Allgemeine Hochschulreife / Mathematik, Informatik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_170_115_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1262000, 170, "115", "00", false, "T", "MP", "IF", "IF", "MI", "Allgemeine Hochschulreife / Mathematik, Informatik", "Allgemeine Hochschulreife / Mathematik, Informatik", "Allgemeine Hochschulreife / Mathematik, Informatik", null, null)
		}));

		/** Fachklasse Allgemeine Hochschulreife / Sozial- u. Gesundheitswesen (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_170_116_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1263000, 170, "116", "00", true, "S", "SG", null, null, null, "Allgemeine Hochschulreife / Sozial- u. Gesundheitswesen", "Allgemeine Hochschulreife / Sozial- u. Gesundheitswesen", "Allgemeine Hochschulreife / Sozial- u. Gesundheitswesen", null, 2005)
		}));

		/** Fachklasse Allgemeine Hochschulreife / Textiltechnik, Physik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_170_117_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1264000, 170, "117", "00", true, "T", "TB", null, null, null, "Allgemeine Hochschulreife / Textiltechnik, Physik", "Allgemeine Hochschulreife / Textiltechnik, Physik", "Allgemeine Hochschulreife / Textiltechnik, Physik", null, 2005)
		}));

		/** Fachklasse Allgemeine Hochschulreife / Werkstofftechnik, Physik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_170_118_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1265000, 170, "118", "00", true, "T", "RW", null, null, null, "Allgemeine Hochschulreife / Werkstofftechnik, Physik", "Allgemeine Hochschulreife / Werkstofftechnik, Physik", "Allgemeine Hochschulreife / Werkstofftechnik, Physik", null, 2008)
		}));

		/** Fachklasse Allgemeine Hochschulreife / Wirtschaft u. Verwaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_170_119_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1266000, 170, "119", "00", true, "W", "WW", null, null, null, "Allgemeine Hochschulreife / Wirtschaft u. Verwaltung", "Allgemeine Hochschulreife / Wirtschaft u. Verwaltung", "Allgemeine Hochschulreife / Wirtschaft u. Verwaltung", null, 2006)
		}));

		/** Fachklasse Allgemeine Hochschulreife / Gesundheit */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_170_120_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1267000, 170, "120", "00", false, "S", "SG", "GS", "GS", "GE", "Allgemeine Hochschulreife / Gesundheit", "Allgemeine Hochschulreife / Gesundheit", "Allgemeine Hochschulreife / Gesundheit", null, null)
		}));

		/** Fachklasse Allgemeine Hochschulreife / Ingenieurwissenschaften */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_170_121_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1268000, 170, "121", "00", false, "T", "TC", "TE", null, "IW", "Allgemeine Hochschulreife / Ingenieurwissenschaften", "Allgemeine Hochschulreife / Ingenieurwissenschaften", "Allgemeine Hochschulreife / Ingenieurwissenschaften", null, null)
		}));

		/** Fachklasse Sonstiger Bildungsgang */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_170_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1269000, 170, "999", "00", false, "X", "XX", "XX", "XX", "XX", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", 2022, null)
		}));

		/** Fachklasse Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_170_999_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1270000, 170, "999", "01", true, "X", "XX", null, null, null, "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, 2014)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 180
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex180(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Assistent/-in für Konstruktions- und Fertigungstechnik / AHR (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_180_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1271000, 180, "101", "00", true, "T", "MB", null, null, null, "Assistent/-in für Konstruktions- und Fertigungstechnik / AHR", "Assistent für Konstruktions- und Fertigungstechnik / AHR", "Assistentin für Konstruktions- und Fertigungstechnik / AHR", null, 2014)
		}));

		/** Fachklasse Bautechnische/-r Assistent/-in / AHR (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_180_102_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1272000, 180, "102", "00", true, "T", "BT", null, null, null, "Bautechnische/-r Assistent/-in / AHR", "Bautechnischer Assistent / AHR", "Bautechnische Assistentin / AHR", null, 2014)
		}));

		/** Fachklasse Biologisch-technische/-r Assistent/-in / AHR (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_180_103_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1273000, 180, "103", "00", true, "T", "NW", null, null, null, "Biologisch-technische/-r Assistent/-in / AHR", "Biologisch-technischer Assistent / AHR", "Biologisch-technische Assistentin / AHR", null, 2014)
		}));

		/** Fachklasse Chemisch-technische/-r Assistent/-in / AHR (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_180_104_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1274000, 180, "104", "00", true, "T", "NW", null, null, null, "Chemisch-technische/-r Assistent/-in / AHR", "Chemisch-technischer Assistent / AHR", "Chemisch-technische Assistentin / AHR", null, 2014)
		}));

		/** Fachklasse Elektrotechnische/-r Assistent/-in / AHR (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_180_105_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1275000, 180, "105", "00", true, "E", "ET", null, null, null, "Elektrotechnische/-r Assistent/-in / AHR", "Elektrotechnischer Assistent / AHR", "Elektrotechnische Assistentin / AHR", null, 2014)
		}));

		/** Fachklasse Erzieher/-in / AHR (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_180_106_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1276000, 180, "106", "00", true, "S", "ES", null, null, null, "Erzieher/-in / AHR", "Erzieher / AHR", "Erzieherin / AHR", null, 2014)
		}));

		/** Fachklasse Gestaltungstechnische/-r Assistent/-in / AHR (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_180_107_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1277000, 180, "107", "00", true, "G", "KM", null, null, null, "Gestaltungstechnische/-r Assistent/-in / AHR", "Gestaltungstechnischer Assistent / AHR", "Gestaltungstechnische Assistentin / AHR", null, 2014)
		}));

		/** Fachklasse Hauswirtschaftliche/-r Assistent/-in / AHR (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_180_108_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1278000, 180, "108", "00", true, "A", "LH", null, null, null, "Hauswirtschaftliche/-r Assistent/-in / AHR", "Hauswirtschaftlicher Assistent / AHR", "Hauswirtschaftliche Assistentin / AHR", null, 2014)
		}));

		/** Fachklasse Kaufmännische/-r Assistent/-in / AHR (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_180_109_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1279000, 180, "109", "00", true, "W", "WW", null, null, null, "Kaufmännische/-r Assistent/-in / AHR", "Kaufmännischer Assistent / AHR", "Kaufmännische Assistentin / AHR", null, 2014)
		}));

		/** Fachklasse Physikalisch-technische/-r Assistent/-in / AHR (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_180_110_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1280000, 180, "110", "00", true, "T", "NW", null, null, null, "Physikalisch-technische/-r Assistent/-in / AHR", "Physikalisch-technischer Assistent / AHR", "Physikalisch-technische Assistentin / AHR", null, 2014)
		}));

		/** Fachklasse Technische/-r Assistent/-in für Betriebsinformatik / AHR (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_180_111_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1281000, 180, "111", "00", true, "W", "WW", null, null, null, "Technische/-r Assistent/-in für Betriebsinformatik / AHR", "Technischer Assistent für Betriebsinformatik / AHR", "Technische Assistentin für Betriebsinformatik / AHR", null, 2014)
		}));

		/** Fachklasse Textiltechnische/-r Assistent/-in / AHR (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_180_112_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1282000, 180, "112", "00", true, "T", "TB", null, null, null, "Textiltechnische/-r Assistent/-in / AHR", "Textiltechnischer Assistent / AHR", "Textiltechnische Assistentin / AHR", null, 2014)
		}));

		/** Fachklasse Umwelttechnische/-r Assistent/-in / AHR (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_180_113_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1283000, 180, "113", "00", true, "T", "NW", null, null, null, "Umwelttechnische/-r Assistent/-in / AHR", "Umwelttechnischer Assistent / AHR", "Umwelttechnische Assistentin / AHR", null, 2014)
		}));

		/** Fachklasse Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_180_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1284000, 180, "999", "00", true, "X", "XX", null, null, null, "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, 2014)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 190
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex190(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Berufspraktikum f. Erzieher/-innen / AHR (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_190_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1285000, 190, "101", "00", true, "S", "ES", null, null, null, "Berufspraktikum f. Erzieher/-innen / AHR", "Berufspraktikum f. Erzieher / AHR", "Berufspraktikum f. Erzieherinnen / AHR", null, 2014)
		}));

		/** Fachklasse Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_190_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1286000, 190, "999", "00", true, "X", "XX", null, null, null, "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, 2014)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 20
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex20(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse BV/Agrarwirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_20_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1287000, 20, "101", "00", true, "A", "AW", null, null, null, "BV/Agrarwirtschaft", "BV/Agrarwirtschaft", "BV/Agrarwirtschaft", null, 2015)
		}));

		/** Fachklasse BV/Ausgesiedelte Jugendliche als Förderklasse (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_20_102_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1288000, 20, "102", "00", true, "O", "OH", null, null, null, "BV/Ausgesiedelte Jugendliche als Förderklasse", "BV/Ausgesiedelte Jugendliche als Förderklasse", "BV/Ausgesiedelte Jugendliche als Förderklasse", null, 2015)
		}));

		/** Fachklasse BV/Bautechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_20_103_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1289000, 20, "103", "00", true, "T", "BT", null, null, null, "BV/Bautechnik", "BV/Bautechnik", "BV/Bautechnik", null, 2015)
		}));

		/** Fachklasse BV/Drucktechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_20_104_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1290000, 20, "104", "00", true, "T", "DT", null, null, null, "BV/Drucktechnik", "BV/Drucktechnik", "BV/Drucktechnik", null, 2015)
		}));

		/** Fachklasse BV/Elektrotechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_20_105_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1291000, 20, "105", "00", true, "T", "ET", null, null, null, "BV/Elektrotechnik", "BV/Elektrotechnik", "BV/Elektrotechnik", null, 2015)
		}));

		/** Fachklasse BV/Ernährung und Hauswirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_20_106_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1292000, 20, "106", "00", true, "E", "EH", null, null, null, "BV/Ernährung und Hauswirtschaft", "BV/Ernährung und Hauswirtschaft", "BV/Ernährung und Hauswirtschaft", null, 2015)
		}));

		/** Fachklasse BV/Farbtechnik und Raumgestaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_20_107_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1293000, 20, "107", "00", true, "G", "FR", null, null, null, "BV/Farbtechnik und Raumgestaltung", "BV/Farbtechnik und Raumgestaltung", "BV/Farbtechnik und Raumgestaltung", null, 2015)
		}));

		/** Fachklasse BV/Holztechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_20_108_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1294000, 20, "108", "00", true, "T", "HT", null, null, null, "BV/Holztechnik", "BV/Holztechnik", "BV/Holztechnik", null, 2015)
		}));

		/** Fachklasse BV/Informationstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_20_109_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1295000, 20, "109", "00", true, "T", "IT", null, null, null, "BV/Informationstechnik", "BV/Informationstechnik", "BV/Informationstechnik", null, 2015)
		}));

		/** Fachklasse BV/Körperpflege (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_20_110_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1296000, 20, "110", "00", true, "S", "KP", null, null, null, "BV/Körperpflege", "BV/Körperpflege", "BV/Körperpflege", null, 2015)
		}));

		/** Fachklasse BV/Land- u. Hauswirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_20_111_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1297000, 20, "111", "00", true, "A", "LH", null, null, null, "BV/Land- u. Hauswirtschaft", "BV/Land- u. Hauswirtschaft", "BV/Land- u. Hauswirtschaft", null, 2014)
		}));

		/** Fachklasse BV/Medien (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_20_112_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1298000, 20, "112", "00", true, "T", "MM", null, null, null, "BV/Medien", "BV/Medien", "BV/Medien", null, 2015)
		}));

		/** Fachklasse BV/Medizintechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_20_113_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1299000, 20, "113", "00", true, "T", "MZ", null, null, null, "BV/Medizintechnik", "BV/Medizintechnik", "BV/Medizintechnik", null, 2015)
		}));

		/** Fachklasse BV/Metalltechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_20_114_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1300000, 20, "114", "00", true, "T", "MT", null, null, null, "BV/Metalltechnik", "BV/Metalltechnik", "BV/Metalltechnik", null, 2015)
		}));

		/** Fachklasse BV/ohne Schwerpunkt (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_20_115_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1301000, 20, "115", "00", true, "O", "OH", null, null, null, "BV/ohne Schwerpunkt", "BV/ohne Schwerpunkt", "BV/ohne Schwerpunkt", null, 2014)
		}));

		/** Fachklasse BV/Physik,Chemie,Biologie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_20_116_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1302000, 20, "116", "00", true, "T", "CP", null, null, null, "BV/Physik,Chemie,Biologie", "BV/Physik,Chemie,Biologie", "BV/Physik,Chemie,Biologie", null, 2015)
		}));

		/** Fachklasse BV/Sozial- und Gesundheitswesen (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_20_117_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1303000, 20, "117", "00", true, "S", "SG", null, null, null, "BV/Sozial- und Gesundheitswesen", "BV/Sozial- und Gesundheitswesen", "BV/Sozial- und Gesundheitswesen", null, 2015)
		}));

		/** Fachklasse BV/Textiltechnik und Bekleidung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_20_118_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1304000, 20, "118", "00", true, "T", "TB", null, null, null, "BV/Textiltechnik und Bekleidung", "BV/Textiltechnik und Bekleidung", "BV/Textiltechnik und Bekleidung", null, 2015)
		}));

		/** Fachklasse BV/Vermessungstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_20_119_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1305000, 20, "119", "00", true, "T", "VT", null, null, null, "BV/Vermessungstechnik", "BV/Vermessungstechnik", "BV/Vermessungstechnik", null, 2015)
		}));

		/** Fachklasse BV/Wirtschaft und Verwaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_20_120_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1306000, 20, "120", "00", true, "W", "WV", null, null, null, "BV/Wirtschaft und Verwaltung", "BV/Wirtschaft und Verwaltung", "BV/Wirtschaft und Verwaltung", null, 2015)
		}));

		/** Fachklasse BV/Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_20_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1307000, 20, "999", "00", true, "X", "XX", null, null, null, "BV/Sonstiger Bildungsgang", "BV/Sonstiger Bildungsgang", "BV/Sonstiger Bildungsgang", null, 2014)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 200
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex200(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Agrarwirtschaft, Bio und Umwelttechnologie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_200_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1308000, 200, "101", "00", false, "A", "AW", "AB", null, null, "Agrarwirtschaft, Bio und Umwelttechnologie", "Agrarwirtschaft, Bio und Umwelttechnologie", "Agrarwirtschaft, Bio und Umwelttechnologie", null, null)
		}));

		/** Fachklasse Bau- und Holztechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_200_102_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1309000, 200, "102", "00", false, "T", "BH", "TE", "BH", null, "Bau- und Holztechnik", "Bau- und Holztechnik", "Bau- und Holztechnik", null, null)
		}));

		/** Fachklasse Drucktechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_200_103_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1310000, 200, "103", "00", false, "T", "DT", "TE", "DT", null, "Drucktechnik", "Drucktechnik", "Drucktechnik", null, null)
		}));

		/** Fachklasse Elektrotechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_200_104_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1311000, 200, "104", "00", false, "T", "ET", "TE", "ET", null, "Elektrotechnik", "Elektrotechnik", "Elektrotechnik", null, null)
		}));

		/** Fachklasse Ernährung und Hauswirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_200_105_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1312000, 200, "105", "00", false, "E", "EH", "EU", null, null, "Ernährung und Hauswirtschaft", "Ernährung und Hauswirtschaft", "Ernährung und Hauswirtschaft", null, null)
		}));

		/** Fachklasse Gestaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_200_106_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1313000, 200, "106", "00", false, "G", "GS", "GT", null, null, "Gestaltung", "Gestaltung", "Gestaltung", null, null)
		}));

		/** Fachklasse Metalltechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_200_107_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1314000, 200, "107", "00", false, "T", "MT", "TE", "ME", null, "Metalltechnik", "Metalltechnik", "Metalltechnik", null, null)
		}));

		/** Fachklasse Physik, Chemie, Biologie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_200_108_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1315000, 200, "108", "00", false, "T", "CP", "TE", "PB", null, "Physik, Chemie, Biologie", "Physik, Chemie, Biologie", "Physik, Chemie, Biologie", null, null)
		}));

		/** Fachklasse Gesundheit und Soziales */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_200_109_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1316000, 200, "109", "00", false, "S", "SG", "GU", null, null, "Gesundheit und Soziales", "Gesundheit und Soziales", "Gesundheit und Soziales", null, null)
		}));

		/** Fachklasse Textiltechnik und Bekleidung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_200_110_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1317000, 200, "110", "00", false, "T", "TB", "TE", "TB", null, "Textiltechnik und Bekleidung", "Textiltechnik und Bekleidung", "Textiltechnik und Bekleidung", null, null)
		}));

		/** Fachklasse Wirtschaft und Verwaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_200_111_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1318000, 200, "111", "00", false, "W", "WV", "WV", null, null, "Wirtschaft und Verwaltung", "Wirtschaft und Verwaltung", "Wirtschaft und Verwaltung", null, null)
		}));

		/** Fachklasse Sonstiger Bildungsgang */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_200_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1319000, 200, "999", "00", false, "X", "XX", "XX", "XX", "XX", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", 2022, null)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 210 (Teil 1)
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex210_Teil1(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse FS Agrarwirtschaft Gartenbau - Friedhofsgärtnerei (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_101_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1320000, 210, "101", "01", true, "A", "AW", null, null, null, "FS Agrarwirtschaft Gartenbau - Friedhofsgärtnerei", "FS Agrarwirtschaft Gartenbau - Friedhofsgärtnerei", "FS Agrarwirtschaft Gartenbau - Friedhofsgärtnerei", null, 2013)
		}));

		/** Fachklasse FS Agrarwirtschaft Gartenbau - Garten- und Landschaftsbau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_101_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1321000, 210, "101", "02", true, "A", "AW", null, null, null, "FS Agrarwirtschaft Gartenbau - Garten- und Landschaftsbau", "FS Agrarwirtschaft Gartenbau - Garten- und Landschaftsbau", "FS Agrarwirtschaft Gartenbau - Garten- und Landschaftsbau", null, 2013)
		}));

		/** Fachklasse FS Agrarwirtschaft/Gartenbau - Produktion und Vermarktung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_101_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1322000, 210, "101", "03", false, null, "AW", "AW", "GB", "PV", "FS Agrarwirtschaft/Gartenbau - Produktion und Vermarktung", "FS Agrarwirtschaft/Gartenbau - Produktion und Vermarktung", "FS Agrarwirtschaft/Gartenbau - Produktion und Vermarktung", null, null)
		}));

		/** Fachklasse FS Agrarwirtschaft/Gartenbau - Dienstleistungsgartenbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_101_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1323000, 210, "101", "04", false, "A", "AW", "AW", "GB", "DG", "FS Agrarwirtschaft/Gartenbau - Dienstleistungsgartenbau", "FS Agrarwirtschaft/Gartenbau - Dienstleistungsgartenbau", "FS Agrarwirtschaft/Gartenbau - Dienstleistungsgartenbau", null, null)
		}));

		/** Fachklasse FS Agrarwirtschaft Milch- und Molkereiwirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_102_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1324000, 210, "102", "00", true, "A", "AW", null, null, null, "FS Agrarwirtschaft Milch- und Molkereiwirtschaft", "FS Agrarwirtschaft Milch- und Molkereiwirtschaft", "FS Agrarwirtschaft Milch- und Molkereiwirtschaft", null, 2012)
		}));

		/** Fachklasse FS Ernährung u. Hauswirtschaft/Ernährung u. Hauswirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_103_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1325000, 210, "103", "00", true, "E", "EH", null, null, null, "FS Ernährung u. Hauswirtschaft/Ernährung u. Hauswirtschaft", "FS Ernährung u. Hauswirtschaft/Ernährung u. Hauswirtschaft", "FS Ernährung u. Hauswirtschaft/Ernährung u. Hauswirtschaft", null, 2005)
		}));

		/** Fachklasse FS Ernährung u. Hauswirtschaft/Hauswirtschaft (2-jährig) - ambulante hauswirtschaftliche Dienstl. (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_104_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1326000, 210, "104", "01", true, "E", "EH", null, null, null, "FS Ernährung u. Hauswirtschaft/Hauswirtschaft (2-jährig) - ambulante hauswirtschaftliche Dienstl.", "FS Ernährung u. Hauswirtschaft/Hauswirtschaft (2-jährig) - ambulante hauswirtschaftliche Dienstl.", "FS Ernährung u. Hauswirtschaft/Hauswirtschaft (2-jährig) - ambulante hauswirtschaftliche Dienstl.", null, 2005)
		}));

		/** Fachklasse FS Ernährungs- und Versorgungsmanagement/Großhaushalt */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_104_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1327000, 210, "104", "02", false, "E", "EH", "EV", "GO", null, "FS Ernährungs- und Versorgungsmanagement/Großhaushalt", "FS Ernährungs- und Versorgungsmanagement/Großhaushalt", "FS Ernährungs- und Versorgungsmanagement/Großhaushalt", null, null)
		}));

		/** Fachklasse FS Ernährung u. Hauswirtschaft/Hauswirtschaftsmeister/-innen (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_105_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1328000, 210, "105", "00", true, "E", "EH", null, null, null, "FS Ernährung u. Hauswirtschaft/Hauswirtschaftsmeister/-innen", "FS Ernährung u. Hauswirtschaft/Hauswirtschaftsmeister", "FS Ernährung u. Hauswirtschaft/Hauswirtschaftsmeisterinnen", null, 2012)
		}));

		/** Fachklasse FS Ernährungs- und Versorgungsmanagement/Hotel und Gaststätten */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_106_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1329000, 210, "106", "00", false, "E", "EH", "EV", "HG", null, "FS Ernährungs- und Versorgungsmanagement/Hotel und Gaststätten", "FS Ernährungs- und Versorgungsmanagement/Hotel und Gaststätten", "FS Ernährungs- und Versorgungsmanagement/Hotel und Gaststätten", null, null)
		}));

		/** Fachklasse FS Ernährungs- und Versorgungsmanagement/Lehrgänge für Hauswirtschaftsmeisterinnen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_107_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1330000, 210, "107", "00", false, "E", "EH", "OZ", null, null, "FS Ernährungs- und Versorgungsmanagement/Lehrgänge für Hauswirtschaftsmeisterinnen", "FS Ernährungs- und Versorgungsmanagement/Lehrgänge für Hauswirtschaftsmeisterinnen", "FS Ernährungs- und Versorgungsmanagement/Lehrgänge für Hauswirtschaftsmeisterinnen", null, null)
		}));

		/** Fachklasse FS Familienpflege (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_108_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1331000, 210, "108", "00", true, "S", "SG", null, null, null, "FS Familienpflege", "FS Familienpflege", "FS Familienpflege", null, 2005)
		}));

		/** Fachklasse FS Gestaltung/Edelmetallgestaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_109_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1332000, 210, "109", "00", false, "G", "GS", "GT", "EM", null, "FS Gestaltung/Edelmetallgestaltung", "FS Gestaltung/Edelmetallgestaltung", "FS Gestaltung/Edelmetallgestaltung", null, null)
		}));

		/** Fachklasse FS Gestaltung - Farbe, Gestaltung, Werbung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_110_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1333000, 210, "110", "00", false, "G", "GS", null, null, null, "FS Gestaltung - Farbe, Gestaltung, Werbung", "FS Gestaltung - Farbe, Gestaltung, Werbung", "FS Gestaltung - Farbe, Gestaltung, Werbung", null, null)
		}));

		/** Fachklasse FS Gestaltung/Farbe, Gestaltung, Werbung - Mediendesign (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_110_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1334000, 210, "110", "01", true, "G", "GS", "GT", "FG", "GD", "FS Gestaltung/Farbe, Gestaltung, Werbung - Mediendesign", "FS Gestaltung/Farbe, Gestaltung, Werbung - Mediendesign", "FS Gestaltung/Farbe, Gestaltung, Werbung - Mediendesign", null, 2021)
		}));

		/** Fachklasse FS Gestaltung - Farbe, Gestaltung, Werbung - Industrie-, Objekt-, Raumdesign (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_110_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1335000, 210, "110", "02", true, "G", "GS", "GT", "FG", "GD", "FS Gestaltung - Farbe, Gestaltung, Werbung - Industrie-, Objekt-, Raumdesign", "FS Gestaltung - Farbe, Gestaltung, Werbung - Industrie-, Objekt-, Raumdesign", "FS Gestaltung - Farbe, Gestaltung, Werbung - Industrie-, Objekt-, Raumdesign", null, 2018)
		}));

		/** Fachklasse FS Gestaltung - Farbe, Gestaltung, Werbung - Medien-Design (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_110_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1336000, 210, "110", "03", true, "G", "GS", null, null, null, "FS Gestaltung - Farbe, Gestaltung, Werbung - Medien-Design", "FS Gestaltung - Farbe, Gestaltung, Werbung - Medien-Design", "FS Gestaltung - Farbe, Gestaltung, Werbung - Medien-Design", null, 2018)
		}));

		/** Fachklasse FS Sozial- u. Gesundheitswesen - Heilerziehungspflege (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_111_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1337000, 210, "111", "01", true, "S", "SG", null, null, null, "FS Sozial- u. Gesundheitswesen - Heilerziehungspflege", "FS Sozial- u. Gesundheitswesen - Heilerziehungspflege", "FS Sozial- u. Gesundheitswesen - Heilerziehungspflege", null, 2005)
		}));

		/** Fachklasse FS Sozialwesen/Heilpädagogik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_111_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1338000, 210, "111", "02", false, "S", "SG", "SW", "HD", null, "FS Sozialwesen/Heilpädagogik", "FS Sozialwesen/Heilpädagogik", "FS Sozialwesen/Heilpädagogik", null, null)
		}));

		/** Fachklasse FS Technik/Abfalltechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_112_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1339000, 210, "112", "00", true, "T", "TC", null, null, null, "FS Technik/Abfalltechnik", "FS Technik/Abfalltechnik", "FS Technik/Abfalltechnik", null, 2005)
		}));

		/** Fachklasse FS Technik/Abwassertechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_113_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1340000, 210, "113", "00", true, "T", "TC", null, null, null, "FS Technik/Abwassertechnik", "FS Technik/Abwassertechnik", "FS Technik/Abwassertechnik", null, 2005)
		}));

		/** Fachklasse FS Technik/Baudenkmalpflege und Altbauerhaltung - Farbe/Stuck (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_114_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1341000, 210, "114", "01", true, "T", "TC", null, null, null, "FS Technik/Baudenkmalpflege und Altbauerhaltung - Farbe/Stuck", "FS Technik/Baudenkmalpflege und Altbauerhaltung - Farbe/Stuck", "FS Technik/Baudenkmalpflege und Altbauerhaltung - Farbe/Stuck", null, 2016)
		}));

		/** Fachklasse FS Technik/Baudenkmalpflege und Altbauerhaltung - Holz (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_114_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1342000, 210, "114", "02", true, "T", "TC", null, null, null, "FS Technik/Baudenkmalpflege und Altbauerhaltung - Holz", "FS Technik/Baudenkmalpflege und Altbauerhaltung - Holz", "FS Technik/Baudenkmalpflege und Altbauerhaltung - Holz", null, 2016)
		}));

		/** Fachklasse FS Technik/Baudenkmalpflege u. Altbauerhaltung - Metall (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_114_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1343000, 210, "114", "03", true, "T", "TC", null, null, null, "FS Technik/Baudenkmalpflege u. Altbauerhaltung - Metall", "FS Technik/Baudenkmalpflege u. Altbauerhaltung - Metall", "FS Technik/Baudenkmalpflege u. Altbauerhaltung - Metall", null, 2011)
		}));

		/** Fachklasse FS Technik/Baudenkmalpflege und Altbauerhaltung - Stein (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_114_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1344000, 210, "114", "04", true, "T", "TC", null, null, null, "FS Technik/Baudenkmalpflege und Altbauerhaltung - Stein", "FS Technik/Baudenkmalpflege und Altbauerhaltung - Stein", "FS Technik/Baudenkmalpflege und Altbauerhaltung - Stein", null, 2016)
		}));

		/** Fachklasse FS Technik/Baudenkmalpflege und Altbauerneuerung - angewandte Baudenkmalpflege */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_114_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1345000, 210, "114", "05", false, "T", "TC", "TE", "BP", "BP", "FS Technik/Baudenkmalpflege und Altbauerneuerung - angewandte Baudenkmalpflege", "FS Technik/Baudenkmalpflege und Altbauerneuerung - angewandte Baudenkmalpflege", "FS Technik/Baudenkmalpflege und Altbauerneuerung - angewandte Baudenkmalpflege", null, null)
		}));

		/** Fachklasse FS Technik/Baudenkmalpflege und Altbauerneuerung - energieeffiziente ökologische Altbauerneuerung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_114_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1346000, 210, "114", "06", false, "T", "TC", "TE", "BP", "AE", "FS Technik/Baudenkmalpflege und Altbauerneuerung - energieeffiziente ökologische Altbauerneuerung", "FS Technik/Baudenkmalpflege und Altbauerneuerung - energieeffiziente ökologische Altbauerneuerung", "FS Technik/Baudenkmalpflege und Altbauerneuerung - energieeffiziente ökologische Altbauerneuerung", null, null)
		}));

		/** Fachklasse FS Technik/Bautechnik - Hochbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_115_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1347000, 210, "115", "01", false, "T", "TC", "TE", "BT", "HB", "FS Technik/Bautechnik - Hochbau", "FS Technik/Bautechnik - Hochbau", "FS Technik/Bautechnik - Hochbau", null, null)
		}));

		/** Fachklasse FS Technik/Bautechnik - Tiefbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_115_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1348000, 210, "115", "02", false, "T", "TC", "TE", "BT", "TB", "FS Technik/Bautechnik - Tiefbau", "FS Technik/Bautechnik - Tiefbau", "FS Technik/Bautechnik - Tiefbau", null, null)
		}));

		/** Fachklasse FS Technik/Bautechnik - Ausbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_115_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1349000, 210, "115", "03", false, "T", "TC", "TE", "BT", "AU", "FS Technik/Bautechnik - Ausbau", "FS Technik/Bautechnik - Ausbau", "FS Technik/Bautechnik - Ausbau", null, null)
		}));

		/** Fachklasse FS Technik/Bekleidungstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_116_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1350000, 210, "116", "00", false, "T", "TC", "TE", "BK", null, "FS Technik/Bekleidungstechnik", "FS Technik/Bekleidungstechnik", "FS Technik/Bekleidungstechnik", null, null)
		}));

		/** Fachklasse FS Technik/Bekleidungstechnik - Bekleidungsfertigung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_116_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1351000, 210, "116", "01", true, "T", "TC", null, null, null, "FS Technik/Bekleidungstechnik - Bekleidungsfertigung", "FS Technik/Bekleidungstechnik - Bekleidungsfertigung", "FS Technik/Bekleidungstechnik - Bekleidungsfertigung", null, 2008)
		}));

		/** Fachklasse FS Technik/Bekleidungstechnik - Bekleidungsgestaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_116_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1352000, 210, "116", "02", true, "T", "TC", null, null, null, "FS Technik/Bekleidungstechnik - Bekleidungsgestaltung", "FS Technik/Bekleidungstechnik - Bekleidungsgestaltung", "FS Technik/Bekleidungstechnik - Bekleidungsgestaltung", null, 2008)
		}));

		/** Fachklasse FS Technik/Bergbautechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_117_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1353000, 210, "117", "00", true, "T", "TC", null, null, null, "FS Technik/Bergbautechnik", "FS Technik/Bergbautechnik", "FS Technik/Bergbautechnik", null, 2012)
		}));

		/** Fachklasse FS Technik/Bergbautechnik - Tagebautechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_117_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1354000, 210, "117", "01", true, "T", "TC", null, null, null, "FS Technik/Bergbautechnik - Tagebautechnik", "FS Technik/Bergbautechnik - Tagebautechnik", "FS Technik/Bergbautechnik - Tagebautechnik", null, 2005)
		}));

		/** Fachklasse FS Technik/Bergbautechnik - Tiefbautechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_117_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1355000, 210, "117", "02", true, "T", "TC", null, null, null, "FS Technik/Bergbautechnik - Tiefbautechnik", "FS Technik/Bergbautechnik - Tiefbautechnik", "FS Technik/Bergbautechnik - Tiefbautechnik", null, 2005)
		}));

		/** Fachklasse FS Technik/Bergbautechnik - Verfahrenstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_117_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1356000, 210, "117", "03", true, "T", "TC", null, null, null, "FS Technik/Bergbautechnik - Verfahrenstechnik", "FS Technik/Bergbautechnik - Verfahrenstechnik", "FS Technik/Bergbautechnik - Verfahrenstechnik", null, 2005)
		}));

		/** Fachklasse FS Technik/Bergbautechnik - Kokerei/Aufbereitungstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_117_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1357000, 210, "117", "04", false, "T", "TC", "TE", "BB", "KO", "FS Technik/Bergbautechnik - Kokerei/Aufbereitungstechnik", "FS Technik/Bergbautechnik - Kokerei/Aufbereitungstechnik", "FS Technik/Bergbautechnik - Kokerei/Aufbereitungstechnik", null, null)
		}));

		/** Fachklasse FS Technik/Bergbautechnik - Tagebautechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_117_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1358000, 210, "117", "05", false, "T", "TC", "TE", "BB", "TG", "FS Technik/Bergbautechnik - Tagebautechnik", "FS Technik/Bergbautechnik - Tagebautechnik", "FS Technik/Bergbautechnik - Tagebautechnik", null, null)
		}));

		/** Fachklasse FS Technik/Bergbautechnik - Tiefbautechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_117_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1359000, 210, "117", "06", false, "T", "TC", "TE", "BB", "TT", "FS Technik/Bergbautechnik - Tiefbautechnik", "FS Technik/Bergbautechnik - Tiefbautechnik", "FS Technik/Bergbautechnik - Tiefbautechnik", null, null)
		}));

		/** Fachklasse FS Technik/Biogentechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_118_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1360000, 210, "118", "00", false, "T", "TC", "TE", "BO", null, "FS Technik/Biogentechnik", "FS Technik/Biogentechnik", "FS Technik/Biogentechnik", null, null)
		}));

		/** Fachklasse FS Technik/Chemietechnik - Betriebstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_119_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1361000, 210, "119", "01", false, "T", "TC", "TE", "CT", "BT", "FS Technik/Chemietechnik - Betriebstechnik", "FS Technik/Chemietechnik - Betriebstechnik", "FS Technik/Chemietechnik - Betriebstechnik", null, null)
		}));

		/** Fachklasse FS Technik/Chemietechnik - Labortechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_119_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1362000, 210, "119", "02", false, "T", "TC", "TE", "CT", "LT", "FS Technik/Chemietechnik - Labortechnik", "FS Technik/Chemietechnik - Labortechnik", "FS Technik/Chemietechnik - Labortechnik", null, null)
		}));

		/** Fachklasse FS Technik/Chemietechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_120_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1363000, 210, "120", "00", true, "T", "TC", null, null, null, "FS Technik/Chemietechnik", "FS Technik/Chemietechnik", "FS Technik/Chemietechnik", null, 2005)
		}));

		/** Fachklasse FS Technik/Druck- und Medientechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_121_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1364000, 210, "121", "00", false, "T", "TC", "TE", "DM", null, "FS Technik/Druck- und Medientechnik", "FS Technik/Druck- und Medientechnik", "FS Technik/Druck- und Medientechnik", null, null)
		}));

		/** Fachklasse FS Technik/Drucktechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_122_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1365000, 210, "122", "00", true, "T", "TC", null, null, null, "FS Technik/Drucktechnik", "FS Technik/Drucktechnik", "FS Technik/Drucktechnik", null, 2005)
		}));

		/** Fachklasse FS Technik/Elektrotechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_123_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1366000, 210, "123", "00", false, "T", "TC", "TE", "ET", null, "FS Technik/Elektrotechnik", "FS Technik/Elektrotechnik", "FS Technik/Elektrotechnik", null, null)
		}));

		/** Fachklasse FS Technik/Elektrotechnik - Datenverarbeitungstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_123_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1367000, 210, "123", "01", true, "T", "TC", null, null, null, "FS Technik/Elektrotechnik - Datenverarbeitungstechnik", "FS Technik/Elektrotechnik - Datenverarbeitungstechnik", "FS Technik/Elektrotechnik - Datenverarbeitungstechnik", null, 2008)
		}));

		/** Fachklasse FS Technik/Elektrotechnik - Elektronik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_123_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1368000, 210, "123", "02", true, "T", "TC", null, null, null, "FS Technik/Elektrotechnik - Elektronik", "FS Technik/Elektrotechnik - Elektronik", "FS Technik/Elektrotechnik - Elektronik", null, 2005)
		}));

		/** Fachklasse FS Technik/Elektrotechnik - Energietechnik u. Prozeßautomatisierung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_123_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1369000, 210, "123", "03", true, "T", "TC", null, null, null, "FS Technik/Elektrotechnik - Energietechnik u. Prozeßautomatisierung", "FS Technik/Elektrotechnik - Energietechnik u. Prozeßautomatisierung", "FS Technik/Elektrotechnik - Energietechnik u. Prozeßautomatisierung", null, 2008)
		}));

		/** Fachklasse FS Technik/Elektrotechnik - Gebäudetechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_123_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1370000, 210, "123", "04", true, "T", "TC", null, null, null, "FS Technik/Elektrotechnik - Gebäudetechnik", "FS Technik/Elektrotechnik - Gebäudetechnik", "FS Technik/Elektrotechnik - Gebäudetechnik", null, 2005)
		}));

		/** Fachklasse FS Technik/Elektrotechnik - Kommunikationstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_123_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1371000, 210, "123", "05", true, "T", "TC", null, null, null, "FS Technik/Elektrotechnik - Kommunikationstechnik", "FS Technik/Elektrotechnik - Kommunikationstechnik", "FS Technik/Elektrotechnik - Kommunikationstechnik", null, 2007)
		}));

		/** Fachklasse FS Technik/Farb- und Lacktechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_124_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1372000, 210, "124", "00", false, "T", "TC", "TE", "FL", null, "FS Technik/Farb- und Lacktechnik", "FS Technik/Farb- und Lacktechnik", "FS Technik/Farb- und Lacktechnik", null, null)
		}));

		/** Fachklasse FS Technik/Feinwerktechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_125_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1373000, 210, "125", "00", true, "T", "TC", null, null, null, "FS Technik/Feinwerktechnik", "FS Technik/Feinwerktechnik", "FS Technik/Feinwerktechnik", null, 2017)
		}));

		/** Fachklasse FS Technik/Galvanotechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_126_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1374000, 210, "126", "00", false, "T", "TC", "TE", "GT", null, "FS Technik/Galvanotechnik", "FS Technik/Galvanotechnik", "FS Technik/Galvanotechnik", null, null)
		}));

		/** Fachklasse FS Technik/Galvanotechnik - Leiterplattentechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_126_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1375000, 210, "126", "01", true, "T", "TC", null, null, null, "FS Technik/Galvanotechnik - Leiterplattentechnik", "FS Technik/Galvanotechnik - Leiterplattentechnik", "FS Technik/Galvanotechnik - Leiterplattentechnik", null, 2016)
		}));

		/** Fachklasse FS Technik/Galvanotechnik - Oberflächentechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_126_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1376000, 210, "126", "02", true, "T", "TC", null, null, null, "FS Technik/Galvanotechnik - Oberflächentechnik", "FS Technik/Galvanotechnik - Oberflächentechnik", "FS Technik/Galvanotechnik - Oberflächentechnik", null, 2016)
		}));

		/** Fachklasse FS Technik/Glastechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_127_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1377000, 210, "127", "00", false, "T", "TC", "TE", "GL", null, "FS Technik/Glastechnik", "FS Technik/Glastechnik", "FS Technik/Glastechnik", null, null)
		}));

		/** Fachklasse FS Technik/Heizungs-, Lüftungs-, Klimatechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_128_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1378000, 210, "128", "00", false, "T", "TC", "TE", "HL", null, "FS Technik/Heizungs-, Lüftungs-, Klimatechnik", "FS Technik/Heizungs-, Lüftungs-, Klimatechnik", "FS Technik/Heizungs-, Lüftungs-, Klimatechnik", null, null)
		}));

		/** Fachklasse FS Technik/Holztechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_129_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1379000, 210, "129", "00", false, "T", "TC", "TE", "HT", null, "FS Technik/Holztechnik", "FS Technik/Holztechnik", "FS Technik/Holztechnik", null, null)
		}));

		/** Fachklasse FS Technik/Informatik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_130_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1380000, 210, "130", "00", true, "T", "TC", null, null, null, "FS Technik/Informatik", "FS Technik/Informatik", "FS Technik/Informatik", null, 2016)
		}));

		/** Fachklasse FS Technik/Informatik - Computer- und Kommunikationstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_130_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1381000, 210, "130", "01", true, "T", "TC", null, null, null, "FS Technik/Informatik - Computer- und Kommunikationstechnik", "FS Technik/Informatik - Computer- und Kommunikationstechnik", "FS Technik/Informatik - Computer- und Kommunikationstechnik", null, 2012)
		}));

		/** Fachklasse FS Technik/Informatik - CNC-Systemtechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_130_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1382000, 210, "130", "02", true, "T", "TC", null, null, null, "FS Technik/Informatik - CNC-Systemtechnik", "FS Technik/Informatik - CNC-Systemtechnik", "FS Technik/Informatik - CNC-Systemtechnik", null, 2012)
		}));

		/** Fachklasse FS Technik/Informatik - Technische Informatik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_130_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1383000, 210, "130", "03", true, "T", "TC", null, null, null, "FS Technik/Informatik - Technische Informatik", "FS Technik/Informatik - Technische Informatik", "FS Technik/Informatik - Technische Informatik", null, 2012)
		}));

		/** Fachklasse FS Technik/Kältetechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_131_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1384000, 210, "131", "00", false, "T", "TC", "TE", "KT", null, "FS Technik/Kältetechnik", "FS Technik/Kältetechnik", "FS Technik/Kältetechnik", null, null)
		}));

		/** Fachklasse FS Technik/Karosserie- und Fahrzeugbautechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_132_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1385000, 210, "132", "00", false, "T", "TC", "TE", "KF", null, "FS Technik/Karosserie- und Fahrzeugbautechnik", "FS Technik/Karosserie- und Fahrzeugbautechnik", "FS Technik/Karosserie- und Fahrzeugbautechnik", null, null)
		}));

		/** Fachklasse FS Technik/Korrosionsschutztechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_133_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1386000, 210, "133", "00", false, "T", "TC", "TE", "KO", null, "FS Technik/Korrosionsschutztechnik", "FS Technik/Korrosionsschutztechnik", "FS Technik/Korrosionsschutztechnik", null, null)
		}));

		/** Fachklasse FS Technik/Kraftfahrzeugtechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_134_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1387000, 210, "134", "00", true, "T", "TC", null, null, null, "FS Technik/Kraftfahrzeugtechnik", "FS Technik/Kraftfahrzeugtechnik", "FS Technik/Kraftfahrzeugtechnik", null, 2017)
		}));

		/** Fachklasse FS Technik/Kunststoff- und Kautschuktechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_135_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1388000, 210, "135", "00", false, "T", "TC", "TE", "KK", null, "FS Technik/Kunststoff- und Kautschuktechnik", "FS Technik/Kunststoff- und Kautschuktechnik", "FS Technik/Kunststoff- und Kautschuktechnik", null, null)
		}));

		/** Fachklasse FS Technik/Lebensmitteltechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_136_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1389000, 210, "136", "00", false, "T", "TC", "TE", "LT", null, "FS Technik/Lebensmitteltechnik", "FS Technik/Lebensmitteltechnik", "FS Technik/Lebensmitteltechnik", null, null)
		}));

		/** Fachklasse FS Technik/Lebensmitteltechnik - Bäckereitechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_136_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1390000, 210, "136", "01", true, "T", "TC", null, null, null, "FS Technik/Lebensmitteltechnik - Bäckereitechnik", "FS Technik/Lebensmitteltechnik - Bäckereitechnik", "FS Technik/Lebensmitteltechnik - Bäckereitechnik", null, 2005)
		}));

		/** Fachklasse FS Technik/Lebensmitteltechnik - Fleischereitechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_136_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1391000, 210, "136", "02", true, "T", "TC", null, null, null, "FS Technik/Lebensmitteltechnik - Fleischereitechnik", "FS Technik/Lebensmitteltechnik - Fleischereitechnik", "FS Technik/Lebensmitteltechnik - Fleischereitechnik", null, 2005)
		}));

		/** Fachklasse FS Technik/Lebensmitteltechnik - Konservierungstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_136_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1392000, 210, "136", "03", true, "T", "TC", null, null, null, "FS Technik/Lebensmitteltechnik - Konservierungstechnik", "FS Technik/Lebensmitteltechnik - Konservierungstechnik", "FS Technik/Lebensmitteltechnik - Konservierungstechnik", null, 2005)
		}));

		/** Fachklasse FS Technik/Lebensmitteltechnik - Lebensmittelverarbeitungstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_136_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1393000, 210, "136", "04", true, "T", "TC", null, null, null, "FS Technik/Lebensmitteltechnik - Lebensmittelverarbeitungstechnik", "FS Technik/Lebensmitteltechnik - Lebensmittelverarbeitungstechnik", "FS Technik/Lebensmitteltechnik - Lebensmittelverarbeitungstechnik", null, 2005)
		}));

		/** Fachklasse FS Technik/Maschinenbautechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_137_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1394000, 210, "137", "00", false, "T", "TC", "TE", "MB", null, "FS Technik/Maschinenbautechnik", "FS Technik/Maschinenbautechnik", "FS Technik/Maschinenbautechnik", null, null)
		}));

		/** Fachklasse FS Technik/Maschinenbautechnik - Betriebstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_137_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1395000, 210, "137", "01", true, "T", "TC", null, null, null, "FS Technik/Maschinenbautechnik - Betriebstechnik", "FS Technik/Maschinenbautechnik - Betriebstechnik", "FS Technik/Maschinenbautechnik - Betriebstechnik", null, 2007)
		}));

		/** Fachklasse FS Technik/Maschinenbautechnik - Entwicklungstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_137_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1396000, 210, "137", "02", true, "T", "TC", null, null, null, "FS Technik/Maschinenbautechnik - Entwicklungstechnik", "FS Technik/Maschinenbautechnik - Entwicklungstechnik", "FS Technik/Maschinenbautechnik - Entwicklungstechnik", null, 2008)
		}));

		/** Fachklasse FS Technik/Maschinenbautechnik - Fertigungstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_137_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1397000, 210, "137", "03", true, "T", "TC", null, null, null, "FS Technik/Maschinenbautechnik - Fertigungstechnik", "FS Technik/Maschinenbautechnik - Fertigungstechnik", "FS Technik/Maschinenbautechnik - Fertigungstechnik", null, 2008)
		}));

		/** Fachklasse FS Technik/Maschinenbautechnik - Luftfahrzeugtechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_137_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1398000, 210, "137", "04", true, "T", "TC", null, null, null, "FS Technik/Maschinenbautechnik - Luftfahrzeugtechnik", "FS Technik/Maschinenbautechnik - Luftfahrzeugtechnik", "FS Technik/Maschinenbautechnik - Luftfahrzeugtechnik", null, 2005)
		}));

		/** Fachklasse FS Technik/Maschinenbautechnik - System- u. Automatisierungstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_137_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1399000, 210, "137", "05", true, "T", "TC", null, null, null, "FS Technik/Maschinenbautechnik - System- u. Automatisierungstechnik", "FS Technik/Maschinenbautechnik - System- u. Automatisierungstechnik", "FS Technik/Maschinenbautechnik - System- u. Automatisierungstechnik", null, 2008)
		}));

		/** Fachklasse FS Technik/Medizintechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_138_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1400000, 210, "138", "00", false, "T", "TC", "TE", "MD", null, "FS Technik/Medizintechnik", "FS Technik/Medizintechnik", "FS Technik/Medizintechnik", null, null)
		}));

		/** Fachklasse FS Technik/Metallbautechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_139_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1401000, 210, "139", "00", false, "T", "TC", "TE", "ML", null, "FS Technik/Metallbautechnik", "FS Technik/Metallbautechnik", "FS Technik/Metallbautechnik", null, null)
		}));

		/** Fachklasse FS Technik/Museums- und Ausstellungstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_140_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1402000, 210, "140", "00", true, "T", "TC", null, null, null, "FS Technik/Museums- und Ausstellungstechnik", "FS Technik/Museums- und Ausstellungstechnik", "FS Technik/Museums- und Ausstellungstechnik", null, 2012)
		}));

		/** Fachklasse FS Technik/Sanitärtechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_141_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1403000, 210, "141", "00", true, "T", "TC", null, null, null, "FS Technik/Sanitärtechnik", "FS Technik/Sanitärtechnik", "FS Technik/Sanitärtechnik", null, 2012)
		}));

		/** Fachklasse FS Technik/Spreng- und Sicherheitstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_142_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1404000, 210, "142", "00", false, "T", "TC", "TE", "SI", null, "FS Technik/Spreng- und Sicherheitstechnik", "FS Technik/Spreng- und Sicherheitstechnik", "FS Technik/Spreng- und Sicherheitstechnik", null, null)
		}));

		/** Fachklasse FS Technik/Textiltechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_143_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1405000, 210, "143", "00", false, "T", "TC", "TE", "TT", null, "FS Technik/Textiltechnik", "FS Technik/Textiltechnik", "FS Technik/Textiltechnik", null, null)
		}));

		/** Fachklasse FS Technik/Textiltechnik - Textilerzeugung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_143_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1406000, 210, "143", "01", true, "T", "TC", null, null, null, "FS Technik/Textiltechnik - Textilerzeugung", "FS Technik/Textiltechnik - Textilerzeugung", "FS Technik/Textiltechnik - Textilerzeugung", null, 2005)
		}));

		/** Fachklasse FS Technik/Textiltechnik - Textilveredlung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_143_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1407000, 210, "143", "02", true, "T", "TC", null, null, null, "FS Technik/Textiltechnik - Textilveredlung", "FS Technik/Textiltechnik - Textilveredlung", "FS Technik/Textiltechnik - Textilveredlung", null, 2005)
		}));

		/** Fachklasse FS Technik/Vermessungstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_144_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1408000, 210, "144", "00", false, "T", "TC", "TE", "VT", null, "FS Technik/Vermessungstechnik", "FS Technik/Vermessungstechnik", "FS Technik/Vermessungstechnik", null, null)
		}));

		/** Fachklasse FS Technik/Wasserversorgungstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_145_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1409000, 210, "145", "00", true, "T", "TC", null, null, null, "FS Technik/Wasserversorgungstechnik", "FS Technik/Wasserversorgungstechnik", "FS Technik/Wasserversorgungstechnik", null, 2005)
		}));

		/** Fachklasse FS Technik/Werkstofftechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_146_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1410000, 210, "146", "00", false, "T", "TC", "TE", "WT", null, "FS Technik/Werkstofftechnik", "FS Technik/Werkstofftechnik", "FS Technik/Werkstofftechnik", null, null)
		}));

		/** Fachklasse FS Wirtschaft/Außenhandel */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_147_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1411000, 210, "147", "00", false, "W", "WI", "WI", "BW", "AH", "FS Wirtschaft/Außenhandel", "FS Wirtschaft/Außenhandel", "FS Wirtschaft/Außenhandel", null, null)
		}));

		/** Fachklasse FS Wirtschaft/Betriebswirtschaft - Absatzwirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_148_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1412000, 210, "148", "01", false, "W", "WI", "WI", "BW", "AB", "FS Wirtschaft/Betriebswirtschaft - Absatzwirtschaft", "FS Wirtschaft/Betriebswirtschaft - Absatzwirtschaft", "FS Wirtschaft/Betriebswirtschaft - Absatzwirtschaft", null, null)
		}));

		/** Fachklasse FS Wirtschaft/Betriebswirtschaft - Allg. Betriebswirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_148_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1413000, 210, "148", "02", true, "W", "WI", null, null, null, "FS Wirtschaft/Betriebswirtschaft - Allg. Betriebswirtschaft", "FS Wirtschaft/Betriebswirtschaft - Allg. Betriebswirtschaft", "FS Wirtschaft/Betriebswirtschaft - Allg. Betriebswirtschaft", null, 2005)
		}));

		/** Fachklasse FS Wirtschaft/Betriebswirtschaft - Finanzwirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_148_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1414000, 210, "148", "03", false, "W", "WI", "WI", "BW", "FW", "FS Wirtschaft/Betriebswirtschaft - Finanzwirtschaft", "FS Wirtschaft/Betriebswirtschaft - Finanzwirtschaft", "FS Wirtschaft/Betriebswirtschaft - Finanzwirtschaft", null, null)
		}));

		/** Fachklasse FS Wirtschaft/Betriebswirtschaft - Fremdsprachen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_148_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1415000, 210, "148", "04", false, "W", "WI", "WI", "BW", "FS", "FS Wirtschaft/Betriebswirtschaft - Fremdsprachen", "FS Wirtschaft/Betriebswirtschaft - Fremdsprachen", "FS Wirtschaft/Betriebswirtschaft - Fremdsprachen", null, null)
		}));

		/** Fachklasse FS Wirtschaft/Betriebswirtschaft - Medizinische Verwaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_148_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1416000, 210, "148", "05", false, "W", "WI", "WI", "BW", "MV", "FS Wirtschaft/Betriebswirtschaft - Medizinische Verwaltung", "FS Wirtschaft/Betriebswirtschaft - Medizinische Verwaltung", "FS Wirtschaft/Betriebswirtschaft - Medizinische Verwaltung", null, null)
		}));

		/** Fachklasse FS Wirtschaft/Betriebswirtschaft - Personalwirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_148_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1417000, 210, "148", "06", false, "W", "WI", "WI", "BW", "PE", "FS Wirtschaft/Betriebswirtschaft - Personalwirtschaft", "FS Wirtschaft/Betriebswirtschaft - Personalwirtschaft", "FS Wirtschaft/Betriebswirtschaft - Personalwirtschaft", null, null)
		}));

		/** Fachklasse FS Wirtschaft/Betriebswirtschaft - Produktionswirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_148_07", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1418000, 210, "148", "07", false, "W", "WI", "WI", "BW", "PW", "FS Wirtschaft/Betriebswirtschaft - Produktionswirtschaft", "FS Wirtschaft/Betriebswirtschaft - Produktionswirtschaft", "FS Wirtschaft/Betriebswirtschaft - Produktionswirtschaft", null, null)
		}));

		/** Fachklasse FS Wirtschaft/Betriebswirtschaft - Rechnungswesen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_148_08", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1419000, 210, "148", "08", false, "W", "WI", "WI", "BW", "RW", "FS Wirtschaft/Betriebswirtschaft - Rechnungswesen", "FS Wirtschaft/Betriebswirtschaft - Rechnungswesen", "FS Wirtschaft/Betriebswirtschaft - Rechnungswesen", null, null)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 210 (Teil 2)
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex210_Teil2(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse FS Wirtschaft/Betriebswirtschaft - Recht */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_148_09", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1420000, 210, "148", "09", false, "W", "WI", "WI", "BW", "RE", "FS Wirtschaft/Betriebswirtschaft - Recht", "FS Wirtschaft/Betriebswirtschaft - Recht", "FS Wirtschaft/Betriebswirtschaft - Recht", null, null)
		}));

		/** Fachklasse FS Wirtschaft/Betriebswirtschaft - Reiseverkehr / Touristik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_148_10", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1421000, 210, "148", "10", false, "W", "WI", "WI", "BW", "RT", "FS Wirtschaft/Betriebswirtschaft - Reiseverkehr / Touristik", "FS Wirtschaft/Betriebswirtschaft - Reiseverkehr / Touristik", "FS Wirtschaft/Betriebswirtschaft - Reiseverkehr / Touristik", null, null)
		}));

		/** Fachklasse FS Wirtschaft/Betriebswirtschaft - Sekretariat (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_148_11", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1422000, 210, "148", "11", true, "W", "WI", null, null, null, "FS Wirtschaft/Betriebswirtschaft - Sekretariat", "FS Wirtschaft/Betriebswirtschaft - Sekretariat", "FS Wirtschaft/Betriebswirtschaft - Sekretariat", null, 2011)
		}));

		/** Fachklasse FS Wirtschaft/Betriebswirtschaft - Steuern */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_148_12", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1423000, 210, "148", "12", false, "W", "WI", "WI", "BW", "ST", "FS Wirtschaft/Betriebswirtschaft - Steuern", "FS Wirtschaft/Betriebswirtschaft - Steuern", "FS Wirtschaft/Betriebswirtschaft - Steuern", null, null)
		}));

		/** Fachklasse FS Wirtschaft/Betriebswirtschaft - Umweltökonomie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_148_13", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1424000, 210, "148", "13", true, "W", "WI", null, null, null, "FS Wirtschaft/Betriebswirtschaft - Umweltökonomie", "FS Wirtschaft/Betriebswirtschaft - Umweltökonomie", "FS Wirtschaft/Betriebswirtschaft - Umweltökonomie", null, 2011)
		}));

		/** Fachklasse FS Wirtschaft/Betriebswirtschaft - Wirtschaftsinformatik/Organisation */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_148_14", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1425000, 210, "148", "14", false, "W", "WI", "WI", "BW", "WI", "FS Wirtschaft/Betriebswirtschaft - Wirtschaftsinformatik/Organisation", "FS Wirtschaft/Betriebswirtschaft - Wirtschaftsinformatik/Organisation", "FS Wirtschaft/Betriebswirtschaft - Wirtschaftsinformatik/Organisation", null, null)
		}));

		/** Fachklasse FS Wirtschaft/Betriebswirtschaft - Logistik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_148_15", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1426000, 210, "148", "15", false, "W", "WI", "WI", "BW", "LO", "FS Wirtschaft/Betriebswirtschaft - Logistik", "FS Wirtschaft/Betriebswirtschaft - Logistik", "FS Wirtschaft/Betriebswirtschaft - Logistik", null, null)
		}));

		/** Fachklasse FS Wirtschaft/Betriebswirtschaft - Sport und Freizeit */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_148_16", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1427000, 210, "148", "16", false, "W", "WI", "WI", "BW", "SF", "FS Wirtschaft/Betriebswirtschaft - Sport und Freizeit", "FS Wirtschaft/Betriebswirtschaft - Sport und Freizeit", "FS Wirtschaft/Betriebswirtschaft - Sport und Freizeit", null, null)
		}));

		/** Fachklasse FS Wirtschaft/Betriebswirtschaft - Finanzdienstleistungen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_148_17", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1428000, 210, "148", "17", false, "W", "WI", "WI", "BW", "FD", "FS Wirtschaft/Betriebswirtschaft - Finanzdienstleistungen", "FS Wirtschaft/Betriebswirtschaft - Finanzdienstleistungen", "FS Wirtschaft/Betriebswirtschaft - Finanzdienstleistungen", null, null)
		}));

		/** Fachklasse FS Wirtschaft/Betriebswirtschaft - Handelsmanagement */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_148_18", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1429000, 210, "148", "18", false, "O", "OH", "WI", "BW", "HM", "FS Wirtschaft/Betriebswirtschaft - Handelsmanagement", "FS Wirtschaft/Betriebswirtschaft - Handelsmanagement", "FS Wirtschaft/Betriebswirtschaft - Handelsmanagement", null, null)
		}));

		/** Fachklasse FS Wirtschaft/Betriebswirtschaft - Marketing-Kommunikation */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_148_19", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1430000, 210, "148", "19", false, "W", "WI", "WI", "BW", "MK", "FS Wirtschaft/Betriebswirtschaft - Marketing-Kommunikation", "FS Wirtschaft/Betriebswirtschaft - Marketing-Kommunikation", "FS Wirtschaft/Betriebswirtschaft - Marketing-Kommunikation", null, null)
		}));

		/** Fachklasse FS Wirtschaft/Betriebswirtschaft - Gesundheitsökonomie und -management */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_148_20", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1431000, 210, "148", "20", false, "W", "WI", "WI", "BW", "GM", "FS Wirtschaft/Betriebswirtschaft - Gesundheitsökonomie und -management", "FS Wirtschaft/Betriebswirtschaft - Gesundheitsökonomie und -management", "FS Wirtschaft/Betriebswirtschaft - Gesundheitsökonomie und -management", null, null)
		}));

		/** Fachklasse FS Wirtschaft/Betriebswirtschaft - Medien- und Eventmanagement (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_148_21", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1432000, 210, "148", "21", true, "W", "WI", "WI", "BW", "ME", "FS Wirtschaft/Betriebswirtschaft - Medien- und Eventmanagement", "FS Wirtschaft/Betriebswirtschaft - Medien- und Eventmanagement", "FS Wirtschaft/Betriebswirtschaft - Medien- und Eventmanagement", null, 2021)
		}));

		/** Fachklasse FS Wirtschaft/Betriebswirtschaft - Kulturmanagement */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_148_22", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1433000, 210, "148", "22", false, "W", "WI", "WI", "BW", null, "FS Wirtschaft/Betriebswirtschaft - Kulturmanagement", "FS Wirtschaft/Betriebswirtschaft - Kulturmanagement", "FS Wirtschaft/Betriebswirtschaft - Kulturmanagement", null, null)
		}));

		/** Fachklasse FS Wirtschaft/Betriebswirtschaft - Internationale Wirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_148_23", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1434000, 210, "148", "23", false, null, null, "WI", null, null, "FS Wirtschaft/Betriebswirtschaft - Internationale Wirtschaft", "FS Wirtschaft/Betriebswirtschaft - Internationale Wirtschaft", "FS Wirtschaft/Betriebswirtschaft - Internationale Wirtschaft", 2022, null)
		}));

		/** Fachklasse FS Wirtschaft/Eisenwaren- u. Hausratshandel (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_149_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1435000, 210, "149", "00", true, "W", "WI", null, null, null, "FS Wirtschaft/Eisenwaren- u. Hausratshandel", "FS Wirtschaft/Eisenwaren- u. Hausratshandel", "FS Wirtschaft/Eisenwaren- u. Hausratshandel", null, 2005)
		}));

		/** Fachklasse FS Wirtschaft/Tourismus */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_150_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1436000, 210, "150", "00", false, "W", "WI", "WI", "TO", null, "FS Wirtschaft/Tourismus", "FS Wirtschaft/Tourismus", "FS Wirtschaft/Tourismus", null, null)
		}));

		/** Fachklasse FS Wirtschaft/Hotel- und Gaststättengewerbe */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_151_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1437000, 210, "151", "00", false, "W", "WI", "WI", "HB", null, "FS Wirtschaft/Hotel- und Gaststättengewerbe", "FS Wirtschaft/Hotel- und Gaststättengewerbe", "FS Wirtschaft/Hotel- und Gaststättengewerbe", null, null)
		}));

		/** Fachklasse FS Wirtschaft/Marketing */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_152_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1438000, 210, "152", "00", false, "W", "WI", "WI", "MG", null, "FS Wirtschaft/Marketing", "FS Wirtschaft/Marketing", "FS Wirtschaft/Marketing", null, null)
		}));

		/** Fachklasse FS Wirtschaft/Möbelhandel */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_153_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1439000, 210, "153", "00", false, "W", "WI", "WI", "MH", null, "FS Wirtschaft/Möbelhandel", "FS Wirtschaft/Möbelhandel", "FS Wirtschaft/Möbelhandel", null, null)
		}));

		/** Fachklasse FS Wirtschaft/Informatik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_154_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1440000, 210, "154", "00", true, "W", "WI", null, null, null, "FS Wirtschaft/Informatik", "FS Wirtschaft/Informatik", "FS Wirtschaft/Informatik", null, 2011)
		}));

		/** Fachklasse FS Wirtschaft/Informatik - Wirtschaftsinformatik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_154_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1441000, 210, "154", "01", true, "W", "WI", null, null, null, "FS Wirtschaft/Informatik - Wirtschaftsinformatik", "FS Wirtschaft/Informatik - Wirtschaftsinformatik", "FS Wirtschaft/Informatik - Wirtschaftsinformatik", null, 2012)
		}));

		/** Fachklasse FS Wirtschaft/Wohnungswirtschaft und Realkredit */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_155_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1442000, 210, "155", "00", false, "W", "WI", "WI", "WR", null, "FS Wirtschaft/Wohnungswirtschaft und Realkredit", "FS Wirtschaft/Wohnungswirtschaft und Realkredit", "FS Wirtschaft/Wohnungswirtschaft und Realkredit", null, null)
		}));

		/** Fachklasse Höhere FS für Sozialarbeit (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_156_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1443000, 210, "156", "00", true, "S", "SG", null, null, null, "Höhere FS für Sozialarbeit", "Höhere FS für Sozialarbeit", "Höhere FS für Sozialarbeit", null, 2005)
		}));

		/** Fachklasse FS Technik/Denkmalpflege - Möbel und Raumausstattung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_157_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1444000, 210, "157", "00", true, "T", "TC", null, null, null, "FS Technik/Denkmalpflege - Möbel und Raumausstattung", "FS Technik/Denkmalpflege - Möbel und Raumausstattung", "FS Technik/Denkmalpflege - Möbel und Raumausstattung", null, 2005)
		}));

		/** Fachklasse FS Technik/Mechatronik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_158_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1445000, 210, "158", "00", false, "T", "TC", "TE", "MT", null, "FS Technik/Mechatronik", "FS Technik/Mechatronik", "FS Technik/Mechatronik", null, null)
		}));

		/** Fachklasse FS Wirtschaft/Logistik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_159_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1446000, 210, "159", "00", true, "W", "WI", null, null, null, "FS Wirtschaft/Logistik", "FS Wirtschaft/Logistik", "FS Wirtschaft/Logistik", null, 2016)
		}));

		/** Fachklasse FS Technik/Medien */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_160_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1447000, 210, "160", "00", false, "T", "TC", "TE", "MN", null, "FS Technik/Medien", "FS Technik/Medien", "FS Technik/Medien", null, null)
		}));

		/** Fachklasse FS Agrarwirtschaft Forstwirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_161_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1448000, 210, "161", "00", true, "A", "AW", null, null, null, "FS Agrarwirtschaft Forstwirtschaft", "FS Agrarwirtschaft Forstwirtschaft", "FS Agrarwirtschaft Forstwirtschaft", null, 2012)
		}));

		/** Fachklasse FS Gestaltung - Metallgestaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_162_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1449000, 210, "162", "00", true, "G", "GS", null, null, null, "FS Gestaltung - Metallgestaltung", "FS Gestaltung - Metallgestaltung", "FS Gestaltung -Metallgestaltung", null, 2012)
		}));

		/** Fachklasse FS Gestaltung - Werbegestaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_163_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1450000, 210, "163", "00", true, "G", "GS", null, null, null, "FS Gestaltung - Werbegestaltung", "FS Gestaltung - Werbegestaltung", "FS Gestaltung - Werbegestaltung", null, 2012)
		}));

		/** Fachklasse FS Technik/Agrartechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_164_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1451000, 210, "164", "00", true, "T", "TC", null, null, null, "FS Technik/Agrartechnik", "FS Technik/Agrartechnik", "FS Technik/Agrartechnik", null, 2012)
		}));

		/** Fachklasse FS Technik/Gebäudesystemtechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_165_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1452000, 210, "165", "00", false, "T", "TC", "TE", "GD", null, "FS Technik/Gebäudesystemtechnik", "FS Technik/Gebäudesystemtechnik", "FS Technik/Gebäudesystemtechnik", null, null)
		}));

		/** Fachklasse FS Technik/Gießereitechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_166_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1453000, 210, "166", "00", false, "T", "TC", "TE", "GR", null, "FS Technik/Gießereitechnik", "FS Technik/Gießereitechnik", "FS Technik/Gießereitechnik", null, null)
		}));

		/** Fachklasse FS Technik/Luftfahrttechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_167_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1454000, 210, "167", "00", true, "T", "TC", null, null, null, "FS Technik/Luftfahrttechnik", "FS Technik/Luftfahrttechnik", "FS Technik/Luftfahrttechnik", null, 2016)
		}));

		/** Fachklasse FS Technik/Luftfahrttechnik - Avionik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_167_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1455000, 210, "167", "01", false, "T", "TC", "TE", "LF", "AI", "FS Technik/Luftfahrttechnik - Avionik", "FS Technik/Luftfahrttechnik - Avionik", "FS Technik/Luftfahrttechnik - Avionik", null, null)
		}));

		/** Fachklasse FS Technik/Luftfahrttechnik - Flugwerk/Triebwerk */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_167_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1456000, 210, "167", "02", false, "T", "TC", "TE", "LF", "FT", "FS Technik/Luftfahrttechnik - Flugwerk/Triebwerk", "FS Technik/Luftfahrttechnik - Flugwerk/Triebwerk", "FS Technik/Luftfahrttechnik - Flugwerk/Triebwerk", null, null)
		}));

		/** Fachklasse FS Technik/Umweltschutztechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_168_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1457000, 210, "168", "00", false, "T", "TC", "TE", "US", null, "FS Technik/Umweltschutztechnik", "FS Technik/Umweltschutztechnik", "FS Technik/Umweltschutztechnik", null, null)
		}));

		/** Fachklasse FS Technik/Umweltschutztechnik - Abfalltechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_168_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1458000, 210, "168", "01", true, "T", "TC", null, null, null, "FS Technik/Umweltschutztechnik - Abfalltechnik", "FS Technik/Umweltschutztechnik - Abfalltechnik", "FS Technik/Umweltschutztechnik - Abfalltechnik", null, 2016)
		}));

		/** Fachklasse FS Technik/Umweltschutztechnik - Abwassertechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_168_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1459000, 210, "168", "02", true, "T", "TC", null, null, null, "FS Technik/Umweltschutztechnik - Abwassertechnik", "FS Technik/Umweltschutztechnik - Abwassertechnik", "FS Technik/Umweltschutztechnik - Abwassertechnik", null, 2016)
		}));

		/** Fachklasse FS Technik/Umweltschutztechnik - Wasserversorgungstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_168_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1460000, 210, "168", "03", true, "T", "TC", null, null, null, "FS Technik/Umweltschutztechnik - Wasserversorgungstechnik", "FS Technik/Umweltschutztechnik - Wasserversorgungstechnik", "FS Technik/Umweltschutztechnik - Wasserversorgungstechnik", null, 2011)
		}));

		/** Fachklasse FS Agrarwirtschaft/Landwirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_169_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1461000, 210, "169", "00", false, "A", "AW", "AW", "LW", null, "FS Agrarwirtschaft/Landwirtschaft", "FS Agrarwirtschaft/Landwirtschaft", "FS Agrarwirtschaft/Landwirtschaft", null, null)
		}));

		/** Fachklasse FS Gestaltung/Mode */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_170_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1462000, 210, "170", "00", false, "G", "GS", "GT", "MO", null, "FS Gestaltung/Mode", "FS Gestaltung/Mode", "FS Gestaltung/Mode", null, null)
		}));

		/** Fachklasse FS Wirtschaft/Bank- und Finanzdienstleistungen (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_171_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1463000, 210, "171", "00", true, "W", "WI", null, null, null, "FS Wirtschaft/Bank- und Finanzdienstleistungen", "FS Wirtschaft/Bank- und Finanzdienstleistungen", "FS Wirtschaft/Bank- und Finanzdienstleistungen", null, 2011)
		}));

		/** Fachklasse FS Technik/Augenoptik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_172_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1464000, 210, "172", "00", false, "T", "TC", "TE", "AU", null, "FS Technik/Augenoptik", "FS Technik/Augenoptik", "FS Technik/Augenoptik", null, null)
		}));

		/** Fachklasse FS Informatik - Technische Informatik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_173_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1465000, 210, "173", "01", true, "T", "TC", null, null, null, "FS Informatik - Technische Informatik", "FS Informatik - Technische Informatik", "FS Informatik - Technische Informatik", null, 2021)
		}));

		/** Fachklasse FS Informatik/Wirtschaftsinformatik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_173_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1466000, 210, "173", "02", false, "W", "WI", "IF", "WI", null, "FS Informatik/Wirtschaftsinformatik", "FS Informatik/Wirtschaftsinformatik", "FS Informatik/Wirtschaftsinformatik", null, null)
		}));

		/** Fachklasse FS Informatik/Technische Informatik - Computer- und Kommunikationstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_173_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1467000, 210, "173", "03", false, null, null, "IF", "TI", "CK", "FS Informatik/Technische Informatik - Computer- und Kommunikationstechnik", "FS Informatik/Technische Informatik - Computer- und Kommunikationstechnik", "FS Informatik/Technische Informatik - Computer- und Kommunikationstechnik", null, null)
		}));

		/** Fachklasse FS Informatik/Technische Informatik - CNC-Systemtechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_173_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1468000, 210, "173", "04", false, null, null, "IF", "TI", "CS", "FS Informatik/Technische Informatik - CNC-Systemtechnik", "FS Informatik/Technische Informatik - CNC-Systemtechnik", "FS Informatik/Technische Informatik - CNC-Systemtechnik", null, null)
		}));

		/** Fachklasse FS Technik/Fahrzeugtechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_174_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1469000, 210, "174", "00", false, "T", "TC", "TE", "FT", null, "FS Technik/Fahrzeugtechnik", "FS Technik/Fahrzeugtechnik", "FS Technik/Fahrzeugtechnik", null, null)
		}));

		/** Fachklasse FS Technik/Fahrzeugtechnik - Elektromobilität */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_174_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1470000, 210, "174", "01", false, null, null, "TE", "FT", null, "FS Technik/Fahrzeugtechnik - Elektromobilität", "FS Technik/Fahrzeugtechnik - Elektromobilität", "FS Technik/Fahrzeugtechnik - Elektromobilität", 2022, null)
		}));

		/** Fachklasse FS Technik/Automatisierungstechnik mit Schwerpunkt digitale Produktionstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_175_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1471000, 210, "175", "00", false, null, null, "TE", "PL", null, "FS Technik/Automatisierungstechnik mit Schwerpunkt digitale Produktionstechnik", "FS Technik/Automatisierungstechnik mit Schwerpunkt digitale Produktionstechnik", "FS Technik/Automatisierungstechnik mit Schwerpunkt digitale Produktionstechnik", null, null)
		}));

		/** Fachklasse FS Gestaltung/Werbe- und Mediendesign */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_176_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1472000, 210, "176", "00", false, null, null, "GT", "MM", null, "FS Gestaltung/Werbe- und Mediendesign", "FS Gestaltung/Werbe- und Mediendesign", "FS Gestaltung/Werbe- und Mediendesign", 2022, null)
		}));

		/** Fachklasse Sonstiger Bildungsgang */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1473000, 210, "999", "00", false, "X", "XX", "XX", "XX", "XX", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", 2022, null)
		}));

		/** Fachklasse Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_999_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1474000, 210, "999", "01", true, "X", "XX", null, null, null, "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, 2014)
		}));

		/** Fachklasse Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_210_999_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1475000, 210, "999", "02", true, "X", "XX", null, null, null, "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, 2017)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 220
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex220(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse FS Agrarwirtschaft Gartenbau - Baumschule (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_101_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1476000, 220, "101", "01", true, "A", "AW", null, null, null, "FS Agrarwirtschaft Gartenbau - Baumschule", "FS Agrarwirtschaft Gartenbau - Baumschule", "FS Agrarwirtschaft Gartenbau - Baumschule", null, 2012)
		}));

		/** Fachklasse FS Agrarwirtschaft Gartenbau - Floristik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_101_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1477000, 220, "101", "02", true, "A", "AW", null, null, null, "FS Agrarwirtschaft Gartenbau - Floristik", "FS Agrarwirtschaft Gartenbau - Floristik", "FS Agrarwirtschaft Gartenbau - Floristik", null, 2011)
		}));

		/** Fachklasse FS Agrarwirtschaft Gartenbau - Friedhofsgärtnerei (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_101_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1478000, 220, "101", "03", true, "A", "AW", null, null, null, "FS Agrarwirtschaft Gartenbau - Friedhofsgärtnerei", "FS Agrarwirtschaft Gartenbau - Friedhofsgärtnerei", "FS Agrarwirtschaft Gartenbau - Friedhofsgärtnerei", null, 2012)
		}));

		/** Fachklasse FS Agrarwirtschaft Gartenbau - Garten- und Landschaftsbau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_101_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1479000, 220, "101", "04", true, "A", "AW", null, null, null, "FS Agrarwirtschaft Gartenbau - Garten- und Landschaftsbau", "FS Agrarwirtschaft Gartenbau - Garten- und Landschaftsbau", "FS Agrarwirtschaft Gartenbau - Garten- und Landschaftsbau", null, 2012)
		}));

		/** Fachklasse FS Agrarwirtschaft Gartenbau - Gemüsebau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_101_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1480000, 220, "101", "05", true, "A", "AW", null, null, null, "FS Agrarwirtschaft Gartenbau - Gemüsebau", "FS Agrarwirtschaft Gartenbau - Gemüsebau", "FS Agrarwirtschaft Gartenbau - Gemüsebau", null, 2011)
		}));

		/** Fachklasse FS Agrarwirtschaft Gartenbau - Obstbau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_101_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1481000, 220, "101", "06", true, "A", "AW", null, null, null, "FS Agrarwirtschaft Gartenbau - Obstbau", "FS Agrarwirtschaft Gartenbau - Obstbau", "FS Agrarwirtschaft Gartenbau - Obstbau", null, 2011)
		}));

		/** Fachklasse FS Agrarwirtschaft Gartenbau - Staudengärtnerei (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_101_07", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1482000, 220, "101", "07", true, "A", "AW", null, null, null, "FS Agrarwirtschaft Gartenbau - Staudengärtnerei", "FS Agrarwirtschaft Gartenbau - Staudengärtnerei", "FS Agrarwirtschaft Gartenbau - Staudengärtnerei", null, 2011)
		}));

		/** Fachklasse FS Agrarwirtschaft Gartenbau - Zierpflanzenbau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_101_08", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1483000, 220, "101", "08", true, "A", "AW", null, null, null, "FS Agrarwirtschaft Gartenbau - Zierpflanzenbau", "FS Agrarwirtschaft Gartenbau - Zierpflanzenbau", "FS Agrarwirtschaft Gartenbau - Zierpflanzenbau", null, 2012)
		}));

		/** Fachklasse FS Agrarwirtschaft Gartenbau - Produktion und Vermarktung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_101_09", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1484000, 220, "101", "09", true, "A", "AW", null, null, null, "FS Agrarwirtschaft Gartenbau - Produktion und Vermarktung", "FS Agrarwirtschaft Gartenbau - Produktion und Vermarktung", "FS Agrarwirtschaft Gartenbau - Produktion und Vermarktung", null, 2011)
		}));

		/** Fachklasse FS Agrarwirtschaft/Gartenbau Stufe I - Dienstleistungsgartenbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_101_10", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1485000, 220, "101", "10", false, "A", "AW", "AW", "GB", "DG", "FS Agrarwirtschaft/Gartenbau Stufe I - Dienstleistungsgartenbau", "FS Agrarwirtschaft/Gartenbau Stufe I - Dienstleistungsgartenbau", "FS Agrarwirtschaft/Gartenbau Stufe I - Dienstleistungsgartenbau", null, null)
		}));

		/** Fachklasse FS Agrarwirtschaft/Gartenbau Stufe II - Dienstleistungsgartenbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_101_11", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1486000, 220, "101", "11", false, "A", "AW", "AW", "GB", "DG", "FS Agrarwirtschaft/Gartenbau Stufe II - Dienstleistungsgartenbau", "FS Agrarwirtschaft/Gartenbau Stufe II - Dienstleistungsgartenbau", "FS Agrarwirtschaft/Gartenbau Stufe II - Dienstleistungsgartenbau", null, null)
		}));

		/** Fachklasse FS Agrarwirtschaft/Gartenbau Stufe I - Produktion und Vermarktung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_101_12", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1487000, 220, "101", "12", false, "A", "AW", "AW", "GB", "PV", "FS Agrarwirtschaft/Gartenbau Stufe I - Produktion und Vermarktung", "FS Agrarwirtschaft/Gartenbau Stufe I - Produktion und Vermarktung", "FS Agrarwirtschaft/Gartenbau Stufe I - Produktion und Vermarktung", null, null)
		}));

		/** Fachklasse FS Agrarwirtschaft/Gartenbau Stufe II - Produktion und Vermarktung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_101_13", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1488000, 220, "101", "13", false, "A", "AW", "AW", "GB", "PV", "FS Agrarwirtschaft/Gartenbau Stufe II - Produktion und Vermarktung", "FS Agrarwirtschaft/Gartenbau Stufe II - Produktion und Vermarktung", "FS Agrarwirtschaft/Gartenbau Stufe II - Produktion und Vermarktung", null, null)
		}));

		/** Fachklasse FS Agrarwirtschaft Ländliche Hauswirtschaft Stufe I (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_102_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1489000, 220, "102", "01", true, "A", "AW", null, null, null, "FS Agrarwirtschaft Ländliche Hauswirtschaft Stufe I", "FS Agrarwirtschaft Ländliche Hauswirtschaft Stufe I", "FS Agrarwirtschaft Ländliche Hauswirtschaft Stufe I", null, 2011)
		}));

		/** Fachklasse FS Agrarwirtschaft Ländliche Hauswirtschaft Stufe II (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_102_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1490000, 220, "102", "02", true, "A", "AW", null, null, null, "FS Agrarwirtschaft Ländliche Hauswirtschaft Stufe II", "FS Agrarwirtschaft Ländliche Hauswirtschaft Stufe II", "FS Agrarwirtschaft Ländliche Hauswirtschaft Stufe II", null, 2005)
		}));

		/** Fachklasse FS Agrarwirtschaft/Landwirtschaft Stufe I (Landwirtschaftsschule) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_103_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1491000, 220, "103", "01", false, "A", "AW", "AW", "LW", null, "FS Agrarwirtschaft/Landwirtschaft Stufe I (Landwirtschaftsschule)", "FS Agrarwirtschaft/Landwirtschaft Stufe I (Landwirtschaftsschule)", "FS Agrarwirtschaft/Landwirtschaft Stufe I (Landwirtschaftsschule)", null, null)
		}));

		/** Fachklasse FS Agrarwirtschaft Landwirtschaft Stufe II (Höhere Landbauschule) (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_103_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1492000, 220, "103", "02", true, "A", "AW", null, null, null, "FS Agrarwirtschaft Landwirtschaft Stufe II (Höhere Landbauschule)", "FS Agrarwirtschaft Landwirtschaft Stufe II (Höhere Landbauschule)", "FS Agrarwirtschaft Landwirtschaft Stufe II (Höhere Landbauschule)", null, 2011)
		}));

		/** Fachklasse FS Agrarwirtschaft/Landwirtschaft Stufe I (Landwirtschaftsschule) - Ökologischer Landbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_104_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1493000, 220, "104", "01", false, "A", "AW", "AW", "LW", "OL", "FS Agrarwirtschaft/Landwirtschaft Stufe I (Landwirtschaftsschule) - Ökologischer Landbau", "FS Agrarwirtschaft/Landwirtschaft Stufe I (Landwirtschaftsschule) - Ökologischer Landbau", "FS Agrarwirtschaft/Landwirtschaft Stufe I (Landwirtschaftsschule) - Ökologischer Landbau", null, null)
		}));

		/** Fachklasse FS Agrarwirtschaft/Landwirtschaft Stufe II (Höhere Landbauschule) - Ökologischer Landbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_104_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1494000, 220, "104", "02", false, "A", "AW", "AW", "LW", "OL", "FS Agrarwirtschaft/Landwirtschaft Stufe II (Höhere Landbauschule) - Ökologischer Landbau", "FS Agrarwirtschaft/Landwirtschaft Stufe II (Höhere Landbauschule) - Ökologischer Landbau", "FS Agrarwirtschaft/Landwirtschaft Stufe II (Höhere Landbauschule) - Ökologischer Landbau", null, null)
		}));

		/** Fachklasse FS Agrarwirtschaft/Landwirtschaft Stufe I (Landwirtschaftsschule) - Agrarservice */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_104_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1495000, 220, "104", "03", false, "A", "AW", "AW", "LW", "AS", "FS Agrarwirtschaft/Landwirtschaft Stufe I (Landwirtschaftsschule) - Agrarservice", "FS Agrarwirtschaft/Landwirtschaft Stufe I (Landwirtschaftsschule) - Agrarservice", "FS Agrarwirtschaft/Landwirtschaft Stufe I (Landwirtschaftsschule) - Agrarservice", null, null)
		}));

		/** Fachklasse FS Ernährungs- und Versorgungsmanagement/Großhaushalt Stufe I */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_105_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1496000, 220, "105", "00", false, "E", "EH", "EV", "GO", null, "FS Ernährungs- und Versorgungsmanagement/Großhaushalt Stufe I", "FS Ernährungs- und Versorgungsmanagement/Großhaushalt Stufe I", "FS Ernährungs- und Versorgungsmanagement/Großhaushalt Stufe I", null, null)
		}));

		/** Fachklasse FS Sozialwesen/Aufb.gang f. Abs. v. FS - Sozialmanagement */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_106_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1497000, 220, "106", "00", false, "S", "SG", "SW", "SM", null, "FS Sozialwesen/Aufb.gang f. Abs. v. FS - Sozialmanagement", "FS Sozialwesen/Aufb.gang f. Abs. v. FS - Sozialmanagement", "FS Sozialwesen/Aufb.gang f. Abs. v. FS - Sozialmanagement", null, null)
		}));

		/** Fachklasse FS Sozialwesen - Heilerziehungshilfe (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_107_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1498000, 220, "107", "00", true, "S", "SG", null, null, null, "FS Sozialwesen - Heilerziehungshilfe", "FS Sozialwesen - Heilerziehungshilfe", "FS Sozialwesen - Heilerziehungshilfe", null, 2005)
		}));

		/** Fachklasse FS Sozialwesen/Motopädie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_108_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1499000, 220, "108", "00", false, "S", "MO", "SW", "MI", null, "FS Sozialwesen/Motopädie", "FS Sozialwesen/Motopädie", "FS Sozialwesen/Motopädie", null, null)
		}));

		/** Fachklasse FS Technik/Aufbaubildungsgang Energiemanagement f. staatl. gepr. Techniker/-innen (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_109_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1500000, 220, "109", "00", true, "T", "TC", null, null, null, "FS Technik/Aufbaubildungsgang Energiemanagement f. staatl. gepr. Techniker/-innen", "FS Technik/Aufbaubildungsgang Energiemanagement f. staatl. gepr. Techniker", "FS Technik/Aufbaubildungsgang Energiemanagement f. staatl. gepr. Technikerinnen", null, 2012)
		}));

		/** Fachklasse FS Technik/Aufbaubildungsgang Existenzgründung f. staatl. gepr. Techniker/-innen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_110_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1501000, 220, "110", "00", false, "T", "TC", "TE", "XT", null, "FS Technik/Aufbaubildungsgang Existenzgründung f. staatl. gepr. Techniker/-innen", "FS Technik/Aufbaubildungsgang Existenzgründung f. staatl. gepr. Techniker", "FS Technik/Aufbaubildungsgang Existenzgründung f. staatl. gepr. Technikerinnen", null, null)
		}));

		/** Fachklasse FS Technik/Aufbaubildungsgang Gebäudemanagement f. staatl. gepr. Techniker/-innen (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_111_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1502000, 220, "111", "00", true, "T", "TC", null, null, null, "FS Technik/Aufbaubildungsgang Gebäudemanagement f. staatl. gepr. Techniker/-innen", "FS Technik/Aufbaubildungsgang Gebäudemanagement f. staatl. gepr. Techniker", "FS Technik/Aufbaubildungsgang Gebäudemanagement f. staatl. gepr. Technikerinnen", null, 2012)
		}));

		/** Fachklasse FS Sozialwesen/Aufbaubildungsgang f. Absolventen/Absolventinnen v. FS - Heilpädagogik (Schulversuch) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_111_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1503000, 220, "111", "02", false, null, null, "SW", "HD", null, "FS Sozialwesen/Aufbaubildungsgang f. Absolventen/Absolventinnen v. FS - Heilpädagogik (Schulversuch)", "FS Sozialwesen/Aufbaubildungsgang f. Absolventen v. FS - Heilpädagogik (Schulversuch)", "FS Sozialwesen/Aufbaubildungsgang f. Absolventinnen v. FS - Heilpädagogik (Schulversuch)", null, null)
		}));

		/** Fachklasse FS Technik/Aufbaubildungsgang Lichttechnik f. staatl. gepr. Techniker/-innen (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_112_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1504000, 220, "112", "00", true, "T", "TC", null, null, null, "FS Technik/Aufbaubildungsgang Lichttechnik f. staatl. gepr. Techniker/-innen", "FS Technik/Aufbaubildungsgang Lichttechnik f. staatl. gepr. Techniker", "FS Technik/Aufbaubildungsgang Lichttechnik f. staatl. gepr. Technikerinnen", null, 2012)
		}));

		/** Fachklasse FS Technik/Aufbaubildungsgang Ökologisches Bauen f. staatl. gepr. Techniker/-innen (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_113_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1505000, 220, "113", "00", true, "T", "TC", null, null, null, "FS Technik/Aufbaubildungsgang Ökologisches Bauen f. staatl. gepr. Techniker/-innen", "FS Technik/Aufbaubildungsgang Ökologisches Bauen f. staatl. gepr. Techniker", "FS Technik/Aufbaubildungsgang Ökologisches Bauen f. staatl. gepr. Technikerinnen", null, 2012)
		}));

		/** Fachklasse FS Technik/Aufbaubildungsgang Qualitätsmanagement f. staatl. gepr. Techniker/-innen (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_114_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1506000, 220, "114", "00", true, "T", "TC", null, null, null, "FS Technik/Aufbaubildungsgang Qualitätsmanagement f. staatl. gepr. Techniker/-innen", "FS Technik/Aufbaubildungsgang Qualitätsmanagement f. staatl. gepr. Techniker", "FS Technik/Aufbaubildungsgang Qualitätsmanagement f. staatl. gepr. Technikerinnen", null, 2012)
		}));

		/** Fachklasse FS Technik/Aufbaubildungsgang Technischer Umweltschutz f. staatl. gepr. Techniker/-innen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_115_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1507000, 220, "115", "00", false, "T", "TC", "TE", "TU", null, "FS Technik/Aufbaubildungsgang Technischer Umweltschutz f. staatl. gepr. Techniker/-innen", "FS Technik/Aufbaubildungsgang Technischer Umweltschutz f. staatl. gepr. Techniker", "FS Technik/Aufbaubildungsgang Technischer Umweltschutz f. staatl. gepr. Technikerinnen", null, null)
		}));

		/** Fachklasse FS Wirtschaft/Aufbaubildungsgang f. Absolv. v. FS - Technik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_116_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1508000, 220, "116", "00", true, "W", "WI", null, null, null, "FS Wirtschaft/Aufbaubildungsgang f. Absolv. v. FS - Technik", "FS Wirtschaft/Aufbaubildungsgang f. Absolv. v. FS - Technik", "FS Wirtschaft/Aufbaubildungsgang f. Absolv. v. FS - Technik", null, 2005)
		}));

		/** Fachklasse FS Wirtschaft/Aufbaubildungsgang f. staatl. gepr. Techn. - Betriebswirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_117_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1509000, 220, "117", "00", false, "W", "WI", "WI", "AB", null, "FS Wirtschaft/Aufbaubildungsgang f. staatl. gepr. Techn. - Betriebswirtschaft", "FS Wirtschaft/Aufbaubildungsgang f. staatl. gepr. Techn. - Betriebswirtschaft", "FS Wirtschaft/Aufbaubildungsgang f. staatl. gepr. Techn. - Betriebswirtschaft", null, null)
		}));

		/** Fachklasse FS Wirtschaft/Aufbaulehrgang f. staatl. gepr. Techn. - Qualitätsmanagement (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_118_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1510000, 220, "118", "00", true, "W", "WI", null, null, null, "FS Wirtschaft/Aufbaulehrgang f. staatl. gepr. Techn. - Qualitätsmanagement", "FS Wirtschaft/Aufbaulehrgang f. staatl. gepr. Techn. - Qualitätsmanagement", "FS Wirtschaft/Aufbaulehrgang f. staatl. gepr. Techn. - Qualitätsmanagement", null, 2011)
		}));

		/** Fachklasse FS Wirtschaft/Aufbaubildungsgang Unternehmensmanagement */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_119_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1511000, 220, "119", "00", false, "W", "WI", "WI", "UM", null, "FS Wirtschaft/Aufbaubildungsgang Unternehmensmanagement", "FS Wirtschaft/Aufbaubildungsgang Unternehmensmanagement", "FS Wirtschaft/Aufbaubildungsgang Unternehmensmanagement", null, null)
		}));

		/** Fachklasse FS Wirtschaft/Aufbaubildungsgang Informations- u. Telekommunikationswirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_120_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1512000, 220, "120", "00", true, "W", "WI", null, null, null, "FS Wirtschaft/Aufbaubildungsgang Informations- u. Telekommunikationswirtschaft", "FS Wirtschaft/Aufbaubildungsgang Informations- u. Telekommunikationswirtschaft", "FS Wirtschaft/Aufbaubildungsgang Informations- u. Telekommunikationswirtschaft", null, 2011)
		}));

		/** Fachklasse FS Wirtschaft/Möbelhandel */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_121_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1513000, 220, "121", "00", false, "W", "WI", "WI", "MH", null, "FS Wirtschaft/Möbelhandel", "FS Wirtschaft/Möbelhandel", "FS Wirtschaft/Möbelhandel", null, null)
		}));

		/** Fachklasse FS Wirtschaft/Aufbaubildungsgang Controlling */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_122_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1514000, 220, "122", "00", false, "W", "WI", "WI", "CO", null, "FS Wirtschaft/Aufbaubildungsgang Controlling", "FS Wirtschaft/Aufbaubildungsgang Controlling", "FS Wirtschaft/Aufbaubildungsgang Controlling", null, null)
		}));

		/** Fachklasse FS Agrarwirtschaft/Städtische Hauswirtschaft - Großhaushalt (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_123_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1515000, 220, "123", "00", true, "A", "AW", null, null, null, "FS Agrarwirtschaft/Städtische Hauswirtschaft - Großhaushalt", "FS Agrarwirtschaft/Städtische Hauswirtschaft - Großhaushalt", "FS Agrarwirtschaft/Städtische Hauswirtschaft - Großhaushalt", null, 2005)
		}));

		/** Fachklasse FS Technik/Aufbaubildungsgang \"Gießereitechnik\" f. staatl. gepr. Techniker/-innen (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_124_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1516000, 220, "124", "00", true, "T", "TC", null, null, null, "FS Technik/Aufbaubildungsgang \"Gießereitechnik\" f. staatl. gepr. Techniker/-innen", "FS Technik/Aufbaubildungsgang \"Gießereitechnik\" f. staatl. gepr. Techniker", "FS Technik/Aufbaubildungsgang \"Gießereitechnik\" f. staatl. gepr. Technikerinnen", null, 2012)
		}));

		/** Fachklasse FS Sozialwesen/Aufbaubildungsgang f. Abs. v. FS - Sprachförderung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_125_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1517000, 220, "125", "00", false, "S", "SG", "SW", "SF", null, "FS Sozialwesen/Aufbaubildungsgang f. Abs. v. FS - Sprachförderung", "FS Sozialwesen/Aufbaubildungsgang f. Abs. v. FS - Sprachförderung", "FS Sozialwesen/Aufbaubildungsgang f. Abs. v. FS - Sprachförderung", null, null)
		}));

		/** Fachklasse FS Agrarwirtschaft Forstwirtschaft Stufe I (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_126_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1518000, 220, "126", "00", true, "A", "AW", null, null, null, "FS Agrarwirtschaft Forstwirtschaft Stufe I", "FS Agrarwirtschaft Forstwirtschaft Stufe I", "FS Agrarwirtschaft Forstwirtschaft Stufe I", null, 2012)
		}));

		/** Fachklasse FS Sozialwesen/Aufbaubildungsgang f. Abs. v. FS - Medienkompetenz in der Kinder- u. Jugendhilfe */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_127_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1519000, 220, "127", "00", false, "S", "SG", "SW", "MK", null, "FS Sozialwesen/Aufbaubildungsgang f. Abs. v. FS - Medienkompetenz in der Kinder- u. Jugendhilfe", "FS Sozialwesen/Aufbaubildungsgang f. Abs. v. FS - Medienkompetenz in der Kinder- u. Jugendhilfe", "FS Sozialwesen/Aufbaubildungsgang f. Abs. v. FS - Medienkompetenz in der Kinder- u. Jugendhilfe", null, null)
		}));

		/** Fachklasse FS Technik/Aufbaubildungsgang \"Technisches Wirtschaftsmanagement\" f. staatl. gepr. Techniker/-innen (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_128_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1520000, 220, "128", "00", true, "T", "TC", null, null, null, "FS Technik/Aufbaubildungsgang \"Technisches Wirtschaftsmanagement\" f. staatl. gepr. Techniker/-innen", "FS Technik/Aufbaubildungsgang \"Technisches Wirtschaftsmanagement\" f. staatl. gepr. Techniker", "FS Technik/Aufbaubildungsgang \"Technisches Wirtschaftsmanagement\" f. staatl. gepr. Technikerinnen", null, 2012)
		}));

		/** Fachklasse FS Sozialwesen - Aufbaubildungsgang f. staatlich geprüfte Erzieher/-innen (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_129_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1521000, 220, "129", "00", true, "S", "SG", null, null, null, "FS Sozialwesen - Aufbaubildungsgang f. staatlich geprüfte Erzieher/-innen", "FS Sozialwesen - Aufbaubildungsgang f. staatlich geprüfte Erzieher", "FS Sozialwesen - Aufbaubildungsgang f. staatlich geprüfte Erzieherinnen", null, 2013)
		}));

		/** Fachklasse FS Sozialwesen/Aufbaubg.  f. Abs. v. FS - Musikalische Förderung im sozialpädag. Arbeitsfeld */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_130_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1522000, 220, "130", "00", false, "S", "SG", "SW", "MF", null, "FS Sozialwesen/Aufbaubg.  f. Abs. v. FS - Musikalische Förderung im sozialpädag. Arbeitsfeld", "FS Sozialwesen/Aufbaubg.  f. Abs. v. FS - Musikalische Förderung im sozialpädag. Arbeitsfeld", "FS Sozialwesen/Aufbaubg.  f. Abs. v. FS - Musikalische Förderung im sozialpädag. Arbeitsfeld", null, null)
		}));

		/** Fachklasse FS Sozialwesen/Aufbaubildungsgang Bewegung und Gesundheit (Bewegungsförderung) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_131_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1523000, 220, "131", "00", false, "S", "SG", "SW", "BG", null, "FS Sozialwesen/Aufbaubildungsgang Bewegung und Gesundheit (Bewegungsförderung)", "FS Sozialwesen/Aufbaubildungsgang Bewegung und Gesundheit (Bewegungsförderung)", "FS Sozialwesen/Aufbaubildungsgang Bewegung und Gesundheit (Bewegungsförderung)", null, null)
		}));

		/** Fachklasse FS Sozialwesen/Aufbaubildungsgang f. Abs. v. FS - Naturwissenschaftlich-techn. Früherziehung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_132_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1524000, 220, "132", "00", false, "S", "SG", "SW", "NF", null, "FS Sozialwesen/Aufbaubildungsgang f. Abs. v. FS - Naturwissenschaftlich-techn. Früherziehung", "FS Sozialwesen/Aufbaubildungsgang f. Abs. v. FS - Naturwissenschaftlich-techn. Früherziehung", "FS Sozialwesen/Aufbaubildungsgang f. Abs. v. FS - Naturwissenschaftlich-techn. Früherziehung", null, null)
		}));

		/** Fachklasse FS Sozialwesen/Aufbaubildungsgang f. Abs. v. FS - Praxisanleitung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_133_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1525000, 220, "133", "00", false, "S", "SG", "SW", "PA", null, "FS Sozialwesen/Aufbaubildungsgang f. Abs. v. FS - Praxisanleitung", "FS Sozialwesen/Aufbaubildungsgang f. Abs. v. FS - Praxisanleitung", "FS Sozialwesen/Aufbaubildungsgang f. Abs. v. FS - Praxisanleitung", null, null)
		}));

		/** Fachklasse FS Sozialwesen/Aufbaubildungsgang f. Abs. v. FS - Offene Ganztagsschule */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_134_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1526000, 220, "134", "00", false, "S", "SG", "SW", "OG", null, "FS Sozialwesen/Aufbaubildungsgang f. Abs. v. FS - Offene Ganztagsschule", "FS Sozialwesen/Aufbaubildungsgang f. Abs. v. FS - Offene Ganztagsschule", "FS Sozialwesen/Aufbaubildungsgang f. Abs. v. FS - Offene Ganztagsschule", null, null)
		}));

		/** Fachklasse FS Sozialwesen - Aufbaubildungsgang f. Abs. v. FS - Religiöse Erziehung im Kindes- und Jugendalter (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_135_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1527000, 220, "135", "00", true, "S", "SG", null, null, null, "FS Sozialwesen - Aufbaubildungsgang f. Abs. v. FS - Religiöse Erziehung im Kindes- und Jugendalter", "FS Sozialwesen - Aufbaubildungsgang f. Abs. v. FS - Religiöse Erziehung im Kindes- und Jugendalter", "FS Sozialwesen - Aufbaubildungsgang f. Abs. v. FS - Religiöse Erziehung im Kindes- und Jugendalter", null, 2011)
		}));

		/** Fachklasse FS Wirtschaft/Aufbaubildungsgang Internationale Rechnungslegung und Besteuerung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_136_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1528000, 220, "136", "00", true, "W", "WI", null, null, null, "FS Wirtschaft/Aufbaubildungsgang Internationale Rechnungslegung und Besteuerung", "FS Wirtschaft/Aufbaubildungsgang Internationale Rechnungslegung und Besteuerung", "FS Wirtschaft/Aufbaubildungsgang Internationale Rechnungslegung und Besteuerung", null, 2011)
		}));

		/** Fachklasse FS Technik/Aufbaubildungsgang Augenoptik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_137_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1529000, 220, "137", "00", false, "T", "TC", "TE", "AA", null, "FS Technik/Aufbaubildungsgang Augenoptik", "FS Technik/Aufbaubildungsgang Augenoptik", "FS Technik/Aufbaubildungsgang Augenoptik", null, null)
		}));

		/** Fachklasse FS Sozialwesen/Aufbaubg.  f. Abs. v. FS - Bildung, Erziehung u. Betreuung in Tageseinr. f. Kinder */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_138_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1530000, 220, "138", "00", false, "S", "SG", "SW", "BE", null, "FS Sozialwesen/Aufbaubg.  f. Abs. v. FS - Bildung, Erziehung u. Betreuung in Tageseinr. f. Kinder", "FS Sozialwesen/Aufbaubg.  f. Abs. v. FS - Bildung, Erziehung u. Betreuung in Tageseinr. f. Kinder", "FS Sozialwesen/Aufbaubg.  f. Abs. v. FS - Bildung, Erziehung u. Betreuung in Tageseinr. f. Kinder", null, null)
		}));

		/** Fachklasse FS Wirtschaft/Aufbaubildungsgang International Business Communication (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_139_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1531000, 220, "139", "00", true, "W", "WI", null, null, null, "FS Wirtschaft/Aufbaubildungsgang International Business Communication", "FS Wirtschaft/Aufbaubildungsgang International Business Communication", "FS Wirtschaft/Aufbaubildungsgang International Business Communication", null, 2011)
		}));

		/** Fachklasse FS Sozialwesen/Aufbaubildungsgang Fachkraft für heilpäd. Förderung mit dem Pferd */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_140_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1532000, 220, "140", "00", false, "S", "SG", "SW", "HF", null, "FS Sozialwesen/Aufbaubildungsgang Fachkraft für heilpäd. Förderung mit dem Pferd", "FS Sozialwesen/Aufbaubildungsgang Fachkraft für heilpäd. Förderung mit dem Pferd", "FS Sozialwesen/Aufbaubildungsgang Fachkraft für heilpäd. Förderung mit dem Pferd", null, null)
		}));

		/** Fachklasse FS Sozialwesen/Aufbaubildungsgang zur Fachkraft für Beratung und Anleitung in der Pflege */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_141_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1533000, 220, "141", "00", false, "S", "SG", "SW", "BA", null, "FS Sozialwesen/Aufbaubildungsgang zur Fachkraft für Beratung und Anleitung in der Pflege", "FS Sozialwesen/Aufbaubildungsgang zur Fachkraft für Beratung und Anleitung in der Pflege", "FS Sozialwesen/Aufbaubildungsgang zur Fachkraft für Beratung und Anleitung in der Pflege", null, null)
		}));

		/** Fachklasse FS Agrarwirtschaft Floristik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_142_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1534000, 220, "142", "00", true, "A", "AW", null, null, null, "FS Agrarwirtschaft Floristik", "FS Agrarwirtschaft Floristik", "FS Agrarwirtschaft Floristik", null, 2012)
		}));

		/** Fachklasse FS Sozialwesen/Aufbaubg.  f. Abs. v. FS - Bildung und Schulvorbereitung in Tageseinr. für Kinder */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_143_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1535000, 220, "143", "00", false, "S", "SG", "SW", "BS", null, "FS Sozialwesen/Aufbaubg.  f. Abs. v. FS - Bildung und Schulvorbereitung in Tageseinr. für Kinder", "FS Sozialwesen/Aufbaubg.  f. Abs. v. FS - Bildung und Schulvorbereitung in Tageseinr. für Kinder", "FS Sozialwesen/Aufbaubg.  f. Abs. v. FS - Bildung und Schulvorbereitung in Tageseinr. für Kinder", null, null)
		}));

		/** Fachklasse FS Sozialwesen/Aufbaubildungsgang Inklusive Pädagogik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_144_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1536000, 220, "144", "00", false, "S", "SG", "SW", "IP", null, "FS Sozialwesen/Aufbaubildungsgang Inklusive Pädagogik", "FS Sozialwesen/Aufbaubildungsgang Inklusive Pädagogik", "FS Sozialwesen/Aufbaubildungsgang Inklusive Pädagogik", null, null)
		}));

		/** Fachklasse FS Technik/Aufbaubildungsgang Produktionslogistik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_145_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1537000, 220, "145", "00", false, "O", "OH", "TE", "PL", null, "FS Technik/Aufbaubildungsgang Produktionslogistik", "FS Technik/Aufbaubildungsgang Produktionslogistik", "FS Technik/Aufbaubildungsgang Produktionslogistik", null, null)
		}));

		/** Fachklasse FS Sozialwesen/Aufbaubildungsgang Existenzgründung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_146_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1538000, 220, "146", "00", false, "O", "OH", "SW", "XS", null, "FS Sozialwesen/Aufbaubildungsgang Existenzgründung", "FS Sozialwesen/Aufbaubildungsgang Existenzgründung", "FS Sozialwesen/Aufbaubildungsgang Existenzgründung", null, null)
		}));

		/** Fachklasse FS Sozialwesen/Aufbaubildungsgang Fachkraft für inklusive Bildungs- und Erziehungsarbeit */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_147_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1539000, 220, "147", "00", false, "S", "SG", "SW", "IP", null, "FS Sozialwesen/Aufbaubildungsgang Fachkraft für inklusive Bildungs- und Erziehungsarbeit", "FS Sozialwesen/Aufbaubildungsgang Fachkraft für inklusive Bildungs- und Erziehungsarbeit", "FS Sozialwesen/Aufbaubildungsgang Fachkraft für inklusive Bildungs- und Erziehungsarbeit", null, null)
		}));

		/** Fachklasse FS Technik/Aufbaubildungsgang Digitale Produktionstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_148_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1540000, 220, "148", "00", false, null, null, "TE", "PL", null, "FS Technik/Aufbaubildungsgang Digitale Produktionstechnik", "FS Technik/Aufbaubildungsgang Digitale Produktionstechnik", "FS Technik/Aufbaubildungsgang Digitale Produktionstechnik", null, null)
		}));

		/** Fachklasse FS Technik/Automatisierungstechnik mit Schwerpunkt digitale Produktionstechnik (Schulversuch) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_175_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1541000, 220, "175", "00", false, null, null, "TE", "PL", null, "FS Technik/Automatisierungstechnik mit Schwerpunkt digitale Produktionstechnik (Schulversuch)", "FS Technik/Automatisierungstechnik mit Schwerpunkt digitale Produktionstechnik (Schulversuch)", "FS Technik/Automatisierungstechnik mit Schwerpunkt digitale Produktionstechnik (Schulversuch)", null, null)
		}));

		/** Fachklasse Sonstiger Bildungsgang */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1542000, 220, "999", "00", false, "X", "XX", "XX", "XX", "XX", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", 2022, null)
		}));

		/** Fachklasse Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_220_999_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1543000, 220, "999", "01", true, "X", "XX", null, null, null, "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, 2010)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 230
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex230(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse FS Sozialwesen - Heilerziehungspflege (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_230_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1544000, 230, "101", "00", true, "S", "SG", null, null, null, "FS Sozialwesen - Heilerziehungspflege", "FS Sozialwesen - Heilerziehungspflege", "FS Sozialwesen - Heilerziehungspflege", null, 2005)
		}));

		/** Fachklasse FS Sozialwesen/Heilerziehungspflege (Theorie; Jahrgang 1 und 2 bei VZ, Jahrgang 1 bis 4 bei TZ) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_230_101_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1545000, 230, "101", "01", false, "S", "SG", "SW", "HP", null, "FS Sozialwesen/Heilerziehungspflege (Theorie; Jahrgang 1 und 2 bei VZ, Jahrgang 1 bis 4 bei TZ)", "FS Sozialwesen/Heilerziehungspflege (Theorie; Jahrgang 1 und 2 bei VZ, Jahrgang 1 bis 4 bei TZ)", "FS Sozialwesen/Heilerziehungspflege (Theorie; Jahrgang 1 und 2 bei VZ, Jahrgang 1 bis 4 bei TZ)", null, null)
		}));

		/** Fachklasse FS Sozialwesen/Heilerziehungspflege (Praxis; Jahrgang 3 bei VZ, Jahrgang 5 bis 6 bei TZ) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_230_101_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1546000, 230, "101", "02", false, "S", "SG", "SW", "HP", null, "FS Sozialwesen/Heilerziehungspflege (Praxis; Jahrgang 3 bei VZ, Jahrgang 5 bis 6 bei TZ)", "FS Sozialwesen/Heilerziehungspflege (Praxis; Jahrgang 3 bei VZ, Jahrgang 5 bis 6 bei TZ)", "FS Sozialwesen/Heilerziehungspflege (Praxis; Jahrgang 3 bei VZ, Jahrgang 5 bis 6 bei TZ)", null, null)
		}));

		/** Fachklasse FS Sozialwesen/Heilerziehungspflege (integrierte Form) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_230_101_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1547000, 230, "101", "05", false, "S", "SG", "SW", "HP", null, "FS Sozialwesen/Heilerziehungspflege (integrierte Form)", "FS Sozialwesen/Heilerziehungspflege (integrierte Form)", "FS Sozialwesen/Heilerziehungspflege (integrierte Form)", null, null)
		}));

		/** Fachklasse FS Sozialwesen - Sozialpädagogik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_230_102_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1548000, 230, "102", "00", true, "S", "SG", null, null, null, "FS Sozialwesen - Sozialpädagogik", "FS Sozialwesen - Sozialpädagogik", "FS Sozialwesen - Sozialpädagogik", null, 2005)
		}));

		/** Fachklasse FS Sozialwesen/Sozialpädagogik (Theorie; Jahrgang 1 und 2 bei VZ, Jahrgang 1 bis 4 bei TZ) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_230_102_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1549000, 230, "102", "01", false, "S", "SG", "SW", "SP", null, "FS Sozialwesen/Sozialpädagogik (Theorie; Jahrgang 1 und 2 bei VZ, Jahrgang 1 bis 4 bei TZ)", "FS Sozialwesen/Sozialpädagogik (Theorie; Jahrgang 1 und 2 bei VZ, Jahrgang 1 bis 4 bei TZ)", "FS Sozialwesen/Sozialpädagogik (Theorie; Jahrgang 1 und 2 bei VZ, Jahrgang 1 bis 4 bei TZ)", null, null)
		}));

		/** Fachklasse FS Sozialwesen/Sozialpädagogik (Praxis; Jahrgang 3 bei VZ, Jahrgang 5 bis 6 bei TZ) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_230_102_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1550000, 230, "102", "02", false, "S", "SG", "SW", "SP", null, "FS Sozialwesen/Sozialpädagogik (Praxis; Jahrgang 3 bei VZ, Jahrgang 5 bis 6 bei TZ)", "FS Sozialwesen/Sozialpädagogik (Praxis; Jahrgang 3 bei VZ, Jahrgang 5 bis 6 bei TZ)", "FS Sozialwesen/Sozialpädagogik (Praxis; Jahrgang 3 bei VZ, Jahrgang 5 bis 6 bei TZ)", null, null)
		}));

		/** Fachklasse FS Sozialwesen/Sozialpädagogik (integrierte Form) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_230_102_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1551000, 230, "102", "05", false, "S", "SG", "SW", "SP", null, "FS Sozialwesen/Sozialpädagogik (integrierte Form)", "FS Sozialwesen/Sozialpädagogik (integrierte Form)", "FS Sozialwesen/Sozialpädagogik (integrierte Form)", null, null)
		}));

		/** Fachklasse FS Sozialwesen - Familienpflege (Theorie; Jahrgang 1 und 2 bei VZ, Jahrgang 1 bis 4 bei TZ) (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_230_103_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1552000, 230, "103", "01", true, "S", "SG", null, null, null, "FS Sozialwesen - Familienpflege (Theorie; Jahrgang 1 und 2 bei VZ, Jahrgang 1 bis 4 bei TZ)", "FS Sozialwesen - Familienpflege (Theorie; Jahrgang 1 und 2 bei VZ, Jahrgang 1 bis 4 bei TZ)", "FS Sozialwesen - Familienpflege (Theorie; Jahrgang 1 und 2 bei VZ, Jahrgang 1 bis 4 bei TZ)", null, 2014)
		}));

		/** Fachklasse FS Sozialwesen - Familienpflege (Praxis; Jahrgang 3 bei VZ, Jahrgang 5 bis 6 bei TZ) (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_230_103_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1553000, 230, "103", "02", true, "S", "SG", null, null, null, "FS Sozialwesen - Familienpflege (Praxis; Jahrgang 3 bei VZ, Jahrgang 5 bis 6 bei TZ)", "FS Sozialwesen - Familienpflege (Praxis; Jahrgang 3 bei VZ, Jahrgang 5 bis 6 bei TZ)", "FS Sozialwesen - Familienpflege (Praxis; Jahrgang 3 bei VZ, Jahrgang 5 bis 6 bei TZ)", null, 2016)
		}));

		/** Fachklasse Sonstiger Bildungsgang */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_230_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1554000, 230, "999", "00", false, "S", "SG", "XX", "XX", "XX", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", 2022, null)
		}));

		/** Fachklasse Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_230_999_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1555000, 230, "999", "01", true, "S", "SG", null, null, null, "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, 2010)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 240
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex240(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse FS Sozialwesen - Heilerziehungspflege (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_240_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1556000, 240, "101", "00", true, "S", "SG", null, null, null, "FS Sozialwesen - Heilerziehungspflege", "FS Sozialwesen - Heilerziehungspflege", "FS Sozialwesen - Heilerziehungspflege", null, 2005)
		}));

		/** Fachklasse FS Sozialwesen - Sozialpädagogik (berufsbegleitend) (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_240_102_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1557000, 240, "102", "00", true, "S", "SG", null, null, null, "FS Sozialwesen - Sozialpädagogik (berufsbegleitend)", "FS Sozialwesen - Sozialpädagogik (berufsbegleitend)", "FS Sozialwesen - Sozialpädagogik (berufsbegleitend)", null, 2005)
		}));

		/** Fachklasse Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_240_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1558000, 240, "999", "00", true, "S", "SG", null, null, null, "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, 2010)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 250
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex250(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse FS Augenoptik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_250_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1559000, 250, "101", "00", true, "T", "AO", null, null, null, "FS Augenoptik", "FS Augenoptik", "FS Augenoptik", null, 2006)
		}));

		/** Fachklasse FS Informatik - CNC-Systemtechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_250_102_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1560000, 250, "102", "01", true, "T", "IF", null, null, null, "FS Informatik - CNC-Systemtechnik", "FS Informatik - CNC-Systemtechnik", "FS Informatik - CNC-Systemtechnik", null, 2006)
		}));

		/** Fachklasse FS Informatik - Computer- u. Kommunikationstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_250_102_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1561000, 250, "102", "02", true, "T", "IF", null, null, null, "FS Informatik - Computer- u. Kommunikationstechnik", "FS Informatik - Computer- u. Kommunikationstechnik", "FS Informatik - Computer- u. Kommunikationstechnik", null, 2006)
		}));

		/** Fachklasse FS Informatik - Wirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_250_102_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1562000, 250, "102", "03", true, "T", "IF", null, null, null, "FS Informatik - Wirtschaft", "FS Informatik - Wirtschaft", "FS Informatik - Wirtschaft", null, 2006)
		}));

		/** Fachklasse FS Mode (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_250_103_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1563000, 250, "103", "00", true, "G", "MD", null, null, null, "FS Mode", "FS Mode", "FS Mode", null, 2005)
		}));

		/** Fachklasse Höhere FS für Sozialarbeit (Theorie) (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_250_104_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1564000, 250, "104", "00", true, "S", "SG", null, null, null, "Höhere FS für Sozialarbeit (Theorie)", "Höhere FS für Sozialarbeit (Theorie)", "Höhere FS für Sozialarbeit (Theorie)", null, 2007)
		}));

		/** Fachklasse Höhere FS für Sozialarbeit (Praxis) (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_250_104_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1565000, 250, "104", "01", true, "S", "SG", null, null, null, "Höhere FS für Sozialarbeit (Praxis)", "Höhere FS für Sozialarbeit (Praxis)", "Höhere FS für Sozialarbeit (Praxis)", null, 2008)
		}));

		/** Fachklasse FS Sozialswesen - Heilerziehungspflege (Integrierte Ausbildung) (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_250_105_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1566000, 250, "105", "00", true, "S", "SG", null, null, null, "FS Sozialswesen - Heilerziehungspflege (Integrierte Ausbildung)", "FS Sozialswesen - Heilerziehungspflege (Integrierte Ausbildung)", "FS Sozialwesen - Heilerziehungspflege (Integrierte Ausbildung)", null, 2006)
		}));

		/** Fachklasse Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_250_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1567000, 250, "999", "00", true, "X", "XX", null, null, null, "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, 2010)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 260
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex260(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse FS Augenoptik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_260_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1568000, 260, "101", "00", true, "T", "AO", null, null, null, "FS Augenoptik", "FS Augenoptik", "FS Augenoptik", null, 2005)
		}));

		/** Fachklasse Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_260_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1569000, 260, "999", "00", true, "X", "XX", null, null, null, "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, 2010)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 270
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex270(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse FS - Berufspraktikum für Erzieher/-innen (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_270_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1570000, 270, "101", "00", true, "S", "SG", null, null, null, "FS - Berufspraktikum für Erzieher/-innen", "FS - Berufspraktikum für Erzieher", "FS - Berufspraktikum für Erzieherinnen", null, 2012)
		}));

		/** Fachklasse FS - Berufspraktikum für Sozialarbeiter/-innen (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_270_102_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1571000, 270, "102", "00", true, "S", "SG", null, null, null, "FS - Berufspraktikum für Sozialarbeiter/-innen", "FS - Berufspraktikum für Sozialarbeiter", "FS - Berufspraktikum für Sozialarbeiterinnen", null, 2012)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 280
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex280(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Allgemeine Hochschulreife (gymn. Oberstufe) - Kunst/Musik/Gestaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_280_086_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1572000, 280, "086", "00", true, "G", "KG", null, null, null, "Allgemeine Hochschulreife (gymn. Oberstufe) - Kunst/Musik/Gestaltung", "Allgemeine Hochschulreife (gymn. Oberstufe) - Kunst/Musik/Gestaltung", "Allgemeine Hochschulreife (gymn. Oberstufe) - Kunst/Musik/Gestaltung", null, 2010)
		}));

		/** Fachklasse Allgemeine Hochschulreife (gymn. Oberstufe) - Sprache u. Literatur (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_280_087_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1573000, 280, "087", "00", true, "L", "SL", null, null, null, "Allgemeine Hochschulreife (gymn. Oberstufe) - Sprache u. Literatur", "Allgemeine Hochschulreife (gymn. Oberstufe) - Sprache u. Literatur", "Allgemeine Hochschulreife (gymn. Oberstufe) - Sprache u. Literatur", null, 2010)
		}));

		/** Fachklasse Allgemeine Hochschulreife (gymn. Oberstufe) - Erziehung u. Soziales (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_280_088_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1574000, 280, "088", "00", true, "S", "ES", null, null, null, "Allgemeine Hochschulreife (gymn. Oberstufe) - Erziehung u. Soziales", "Allgemeine Hochschulreife (gymn. Oberstufe) - Erziehung u. Soziales", "Allgemeine Hochschulreife (gymn. Oberstufe) - Erziehung u. Soziales", null, 2010)
		}));

		/** Fachklasse Allgemeine Hochschulreife (gymn. Oberstufe) - Elektrotechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_280_089_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1575000, 280, "089", "00", true, "T", "ET", null, null, null, "Allgemeine Hochschulreife (gymn. Oberstufe) - Elektrotechnik", "Allgemeine Hochschulreife (gymn. Oberstufe) - Elektrotechnik", "Allgemeine Hochschulreife (gymn. Oberstufe) - Elektrotechnik", null, 2010)
		}));

		/** Fachklasse Allgemeine Hochschulreife (gymn. Oberstufe) - Mathematik/Philosophie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_280_090_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1576000, 280, "090", "00", true, "T", "MP", null, null, null, "Allgemeine Hochschulreife (gymn. Oberstufe) - Mathematik/Philosophie", "Allgemeine Hochschulreife (gymn. Oberstufe) - Mathematik/Philosophie", "Allgemeine Hochschulreife (gymn. Oberstufe) - Mathematik/Philosophie", null, 2010)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 290
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex290(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Fachhochschulreife 2j. (VZ) - Kunst/Musik/Gestaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_290_081_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1577000, 290, "081", "00", true, "G", "KG", null, null, null, "Fachhochschulreife 2j. (VZ) - Kunst/Musik/Gestaltung", "Fachhochschulreife 2j. (VZ) - Kunst/Musik/Gestaltung", "Fachhochschulreife 2j. (VZ) - Kunst/Musik/Gestaltung", null, 2010)
		}));

		/** Fachklasse Fachhochschulreife 2j. (VZ) - Erziehung u. Soziales (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_290_082_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1578000, 290, "082", "00", true, "S", "ES", null, null, null, "Fachhochschulreife 2j. (VZ) - Erziehung u. Soziales", "Fachhochschulreife 2j. (VZ) - Erziehung u. Soziales", "Fachhochschulreife 2j. (VZ) - Erziehung u. Soziales", null, 2010)
		}));

		/** Fachklasse Fachhochschulreife 2j. (VZ) - Wirtschaftswissenschaften (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_290_083_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1579000, 290, "083", "00", true, "W", "WW", null, null, null, "Fachhochschulreife 2j. (VZ) - Wirtschaftswissenschaften", "Fachhochschulreife 2j. (VZ) - Wirtschaftswissenschaften", "Fachhochschulreife 2j. (VZ) - Wirtschaftswissenschaften", null, 2010)
		}));

		/** Fachklasse Fachhochschulreife 2j. (VZ) - Land- u. Hauswirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_290_084_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1580000, 290, "084", "00", true, "A", "LH", null, null, null, "Fachhochschulreife 2j. (VZ) - Land- u. Hauswirtschaft", "Fachhochschulreife 2j. (VZ) - Land- u. Hauswirtschaft", "Fachhochschulreife 2j. (VZ) - Land- u. Hauswirtschaft", null, 2010)
		}));

		/** Fachklasse Fachhochschulreife 2j. (VZ) - Bautechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_290_085_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1581000, 290, "085", "00", true, "T", "BT", null, null, null, "Fachhochschulreife 2j. (VZ) - Bautechnik", "Fachhochschulreife 2j. (VZ) - Bautechnik", "Fachhochschulreife 2j. (VZ) - Bautechnik", null, 2010)
		}));

		/** Fachklasse Fachhochschulreife 2j. (VZ) - Verkehrstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_290_086_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1582000, 290, "086", "00", true, "T", "VT", null, null, null, "Fachhochschulreife 2j. (VZ) - Verkehrstechnik", "Fachhochschulreife 2j. (VZ) - Verkehrstechnik", "Fachhochschulreife 2j. (VZ) - Verkehrstechnik", null, 2010)
		}));

		/** Fachklasse Fachhochschulreife 2j. (VZ) - Maschinenbautechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_290_087_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1583000, 290, "087", "00", true, "T", "MT", null, null, null, "Fachhochschulreife 2j. (VZ) - Maschinenbautechnik", "Fachhochschulreife 2j. (VZ) - Maschinenbautechnik", "Fachhochschulreife 2j. (VZ) - Maschinenbautechnik", null, 2010)
		}));

		/** Fachklasse Fachhochschulreife 2j. (VZ) - Elektrotechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_290_088_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1584000, 290, "088", "00", true, "T", "ET", null, null, null, "Fachhochschulreife 2j. (VZ) - Elektrotechnik", "Fachhochschulreife 2j. (VZ) - Elektrotechnik", "Fachhochschulreife 2j. (VZ) - Elektrotechnik", null, 2010)
		}));

		/** Fachklasse Fachhochschulreife 2j. (VZ) - Rohstoffe/Werkstoffe (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_290_089_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1585000, 290, "089", "00", true, "T", "RO", null, null, null, "Fachhochschulreife 2j. (VZ) - Rohstoffe/Werkstoffe", "Fachhochschulreife 2j. (VZ) - Rohstoffe/Werkstoffe", "Fachhochschulreife 2j. (VZ) - Rohstoffe/Werkstoffe", null, 2010)
		}));

		/** Fachklasse Fachhochschulreife 2j. (VZ) - Mathematik/Philosophie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_290_090_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1586000, 290, "090", "00", true, "T", "MP", null, null, null, "Fachhochschulreife 2j. (VZ) - Mathematik/Philosophie", "Fachhochschulreife 2j. (VZ) - Mathematik/Philosophie", "Fachhochschulreife 2j. (VZ) - Mathematik/Philosophie", null, 2010)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 30
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex30(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse BG/Agrarwirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_30_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1587000, 30, "101", "00", true, "A", "AW", null, null, null, "BG/Agrarwirtschaft", "BG/Agrarwirtschaft", "BG/Agrarwirtschaft", null, 2015)
		}));

		/** Fachklasse BG/Bautechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_30_102_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1588000, 30, "102", "00", true, "T", "BT", null, null, null, "BG/Bautechnik", "BG/Bautechnik", "BG/Bautechnik", null, 2015)
		}));

		/** Fachklasse BG/Chemie,Physik,Biologie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_30_103_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1589000, 30, "103", "00", true, "T", "CP", null, null, null, "BG/Chemie,Physik,Biologie", "BG/Chemie,Physik,Biologie", "BG/Chemie,Physik,Biologie", null, 2014)
		}));

		/** Fachklasse BG/Drucktechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_30_104_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1590000, 30, "104", "00", true, "T", "DT", null, null, null, "BG/Drucktechnik", "BG/Drucktechnik", "BG/Drucktechnik", null, 2015)
		}));

		/** Fachklasse BG/Elektrotechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_30_105_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1591000, 30, "105", "00", true, "T", "ET", null, null, null, "BG/Elektrotechnik", "BG/Elektrotechnik", "BG/Elektrotechnik", null, 2015)
		}));

		/** Fachklasse BG/Ernährung und Hauswirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_30_106_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1592000, 30, "106", "00", true, "E", "EH", null, null, null, "BG/Ernährung und Hauswirtschaft", "BG/Ernährung und Hauswirtschaft", "BG/Ernährung und Hauswirtschaft", null, 2015)
		}));

		/** Fachklasse BG/Farbtechnik und Raumgestaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_30_107_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1593000, 30, "107", "00", true, "G", "FR", null, null, null, "BG/Farbtechnik und Raumgestaltung", "BG/Farbtechnik und Raumgestaltung", "BG/Farbtechnik und Raumgestaltung", null, 2015)
		}));

		/** Fachklasse BG/Holztechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_30_108_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1594000, 30, "108", "00", true, "T", "HT", null, null, null, "BG/Holztechnik", "BG/Holztechnik", "BG/Holztechnik", null, 2015)
		}));

		/** Fachklasse BG/Informationstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_30_109_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1595000, 30, "109", "00", true, "T", "IT", null, null, null, "BG/Informationstechnik", "BG/Informationstechnik", "BG/Informationstechnik", null, 2015)
		}));

		/** Fachklasse BG/Körperpflege (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_30_110_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1596000, 30, "110", "00", true, "S", "KP", null, null, null, "BG/Körperpflege", "BG/Körperpflege", "BG/Körperpflege", null, 2015)
		}));

		/** Fachklasse BG/Land- u. Hauswirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_30_111_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1597000, 30, "111", "00", true, "A", "LH", null, null, null, "BG/Land- u. Hauswirtschaft", "BG/Land- u. Hauswirtschaft", "BG/Land- u. Hauswirtschaft", null, 2014)
		}));

		/** Fachklasse BG/Medien (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_30_112_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1598000, 30, "112", "00", true, "T", "MM", null, null, null, "BG/Medien", "BG/Medien", "BG/Medien", null, 2015)
		}));

		/** Fachklasse BG/Medizintechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_30_113_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1599000, 30, "113", "00", true, "T", "MZ", null, null, null, "BG/Medizintechnik", "BG/Medizintechnik", "BG/Medizintechnik", null, 2015)
		}));

		/** Fachklasse BG/Metalltechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_30_114_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1600000, 30, "114", "00", true, "T", "MT", null, null, null, "BG/Metalltechnik", "BG/Metalltechnik", "BG/Metalltechnik", null, 2015)
		}));

		/** Fachklasse BG/ohne Schwerpunkt (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_30_115_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1601000, 30, "115", "00", true, "O", "OH", null, null, null, "BG/ohne Schwerpunkt", "BG/ohne Schwerpunkt", "BG/ohne Schwerpunkt", null, 2014)
		}));

		/** Fachklasse BG/Physik,Chemie,Biologie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_30_116_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1602000, 30, "116", "00", true, "T", "CP", null, null, null, "BG/Physik,Chemie,Biologie", "BG/Physik,Chemie,Biologie", "BG/Physik,Chemie,Biologie", null, 2015)
		}));

		/** Fachklasse BG/Sozial- und Gesundheitswesen (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_30_117_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1603000, 30, "117", "00", true, "S", "SG", null, null, null, "BG/Sozial- und Gesundheitswesen", "BG/Sozial- und Gesundheitswesen", "BG/Sozial- und Gesundheitswesen", null, 2015)
		}));

		/** Fachklasse BG/Textiltechnik und Bekleidung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_30_118_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1604000, 30, "118", "00", true, "T", "TB", null, null, null, "BG/Textiltechnik und Bekleidung", "BG/Textiltechnik und Bekleidung", "BG/Textiltechnik und Bekleidung", null, 2015)
		}));

		/** Fachklasse BG/Verkehrstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_30_119_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1605000, 30, "119", "00", true, "T", "VK", null, null, null, "BG/Verkehrstechnik", "BG/Verkehrstechnik", "BG/Verkehrstechnik", null, 2014)
		}));

		/** Fachklasse BG/Vermessungstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_30_120_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1606000, 30, "120", "00", true, "T", "VT", null, null, null, "BG/Vermessungstechnik", "BG/Vermessungstechnik", "BG/Vermessungstechnik", null, 2015)
		}));

		/** Fachklasse BG/Wirtschaft und Verwaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_30_121_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1607000, 30, "121", "00", true, "W", "WV", null, null, null, "BG/Wirtschaft und Verwaltung", "BG/Wirtschaft und Verwaltung", "BG/Wirtschaft und Verwaltung", null, 2015)
		}));

		/** Fachklasse BG/Fahrzeugtechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_30_122_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1608000, 30, "122", "00", true, "T", "FT", null, null, null, "BG/Fahrzeugtechnik", "BG/Fahrzeugtechnik", "BG/Fahrzeugtechnik", null, 2015)
		}));

		/** Fachklasse BG/Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_30_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1609000, 30, "999", "00", true, "X", "XX", null, null, null, "BG/Sonstiger Bildungsgang", "BG/Sonstiger Bildungsgang", "BG/Sonstiger Bildungsgang", null, 2014)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 300
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex300(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Fachhochschulreife 3j. (TZ) - Kunst/Musik/Gestaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_300_083_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1610000, 300, "083", "00", true, "G", "KG", null, null, null, "Fachhochschulreife 3j. (TZ) - Kunst/Musik/Gestaltung", "Fachhochschulreife 3j. (TZ) - Kunst/Musik/Gestaltung", "Fachhochschulreife 3j. (TZ) - Kunst/Musik/Gestaltung", null, 2010)
		}));

		/** Fachklasse Fachhochschulreife 3j. (TZ) - Erziehung u. Soziales (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_300_084_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1611000, 300, "084", "00", true, "S", "ES", null, null, null, "Fachhochschulreife 3j. (TZ) - Erziehung u. Soziales", "Fachhochschulreife 3j. (TZ) - Erziehung u. Soziales", "Fachhochschulreife 3j. (TZ) - Erziehung u. Soziales", null, 2010)
		}));

		/** Fachklasse Fachhochschulreife 3j. (TZ) - Wirtschaftswissenschaften (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_300_085_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1612000, 300, "085", "00", true, "W", "WW", null, null, null, "Fachhochschulreife 3j. (TZ) - Wirtschaftswissenschaften", "Fachhochschulreife 3j. (TZ) - Wirtschaftswissenschaften", "Fachhochschulreife 3j. (TZ) - Wirtschaftswissenschaften", null, 2010)
		}));

		/** Fachklasse Fachhochschulreife 3j. (TZ) - Bautechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_300_086_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1613000, 300, "086", "00", true, "T", "BT", null, null, null, "Fachhochschulreife 3j. (TZ) - Bautechnik", "Fachhochschulreife 3j. (TZ) - Bautechnik", "Fachhochschulreife 3j. (TZ) - Bautechnik", null, 2010)
		}));

		/** Fachklasse Fachhochschulreife 3j. (TZ) - Verkehrstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_300_087_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1614000, 300, "087", "00", true, "T", "VT", null, null, null, "Fachhochschulreife 3j. (TZ) - Verkehrstechnik", "Fachhochschulreife 3j. (TZ) - Verkehrstechnik", "Fachhochschulreife 3j. (TZ) - Verkehrstechnik", null, 2010)
		}));

		/** Fachklasse Fachhochschulreife 3j. (TZ) - Maschinenbautechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_300_088_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1615000, 300, "088", "00", true, "T", "MT", null, null, null, "Fachhochschulreife 3j. (TZ) - Maschinenbautechnik", "Fachhochschulreife 3j. (TZ) - Maschinenbautechnik", "Fachhochschulreife 3j. (TZ) - Maschinenbautechnik", null, 2010)
		}));

		/** Fachklasse Fachhochschulreife 3j. (TZ) - Elektrotechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_300_089_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1616000, 300, "089", "00", true, "T", "ET", null, null, null, "Fachhochschulreife 3j. (TZ) - Elektrotechnik", "Fachhochschulreife 3j. (TZ) - Elektrotechnik", "Fachhochschulreife 3j. (TZ) - Elektrotechnik", null, 2010)
		}));

		/** Fachklasse Fachhochschulreife 3j. (TZ) - Naturwissenschaften (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_300_090_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1617000, 300, "090", "00", true, "T", "NW", null, null, null, "Fachhochschulreife 3j. (TZ) - Naturwissenschaften", "Fachhochschulreife 3j. (TZ) - Naturwissenschaften", "Fachhochschulreife 3j. (TZ) - Naturwissenschaften", null, 2010)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 310
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex310(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Berufspraktikum Fremdsprachenassistent/-in/Wirtschaftswissenschaften (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_310_090_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1618000, 310, "090", "00", true, "W", "WW", null, null, null, "Berufspraktikum Fremdsprachenassistent/-in/Wirtschaftswissenschaften", "Berufspraktikum Fremdsprachenassistent/Wirtschaftswissenschaften", "Berufspraktikum Fremdsprachenassistentin/Wirtschaftswissenschaften", null, 2012)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 320
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex320(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Berufsabschluss/Assistent 3j. (TZ) - Wirtschaftswiss./Eurowirtschaftsassistent/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_320_090_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1619000, 320, "090", "00", true, "W", "WW", null, null, null, "Berufsabschluss/Assistent 3j. (TZ) - Wirtschaftswiss./Eurowirtschaftsassistent/-in", "Berufsabschluss/Assistent 3j. (TZ) - Wirtschaftswiss./Eurowirtschaftsassistent", "Berufsabschluss/Assistent 3j. (TZ) - Wirtschaftswiss./Eurowirtschaftsassistentin", null, 2012)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 330
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex330(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Berufsabschluss/Ass. 3j. (VZ) - Sprache u. Literatur / Fremdspr.korrespondent/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_330_088_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1620000, 330, "088", "00", true, "L", "SL", null, null, null, "Berufsabschluss/Ass. 3j. (VZ) - Sprache u. Literatur / Fremdspr.korrespondent/-in", "Berufsabschluss/Ass. 3j. (VZ) - Sprache u. Literatur / Fremdspr.korrespondent", "Berufsabschluss/Ass. 3j. (VZ) - Sprache u. Literatur / Fremdspr.korrespondentin", null, 2012)
		}));

		/** Fachklasse Berufsabschluss/Ass. 3j. (VZ) - Erziehung u. Soziales / Freizeitsportleiter/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_330_089_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1621000, 330, "089", "00", true, "S", "ES", null, null, null, "Berufsabschluss/Ass. 3j. (VZ) - Erziehung u. Soziales / Freizeitsportleiter/-in", "Berufsabschluss/Ass. 3j. (VZ) - Erziehung u. Soziales / Freizeitsportleiter", "Berufsabschluss/Ass. 3j. (VZ) - Erziehung u. Soziales / Freizeitsportleiterin", null, 2012)
		}));

		/** Fachklasse Berufsabschluss/Ass. 3j. (VZ) - Naturwissenschaften / Informationstechn. Ass. (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_330_090_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1622000, 330, "090", "00", true, "T", "NW", null, null, null, "Berufsabschluss/Ass. 3j. (VZ) - Naturwissenschaften / Informationstechn. Ass.", "Berufsabschluss/Ass. 3j. (VZ) - Naturwissenschaften / Informationstechn. Ass.", "Berufsabschluss/Ass. 3j. (VZ) - Naturwissenschaften / Informationstechn. Ass.", null, 2005)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 340
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex340(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Berufsabschluss/Ass. 3j. (VZ) - Erziehung u. Soziales / Sozialhelfer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_340_089_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1623000, 340, "089", "00", true, "S", "ES", null, null, null, "Berufsabschluss/Ass. 3j. (VZ) - Erziehung u. Soziales / Sozialhelfer/-in", "Berufsabschluss/Ass. 3j. (VZ) - Erziehung u. Soziales / Sozialhelfer", "Berufsabschluss/Ass. 3j. (VZ) - Erziehung u. Soziales / Sozialhelferin", null, 2012)
		}));

		/** Fachklasse Berufsabschluss/Ass. 3j. (VZ) - Erziehung u. Soziales / Kinderpfleger/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_340_090_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1624000, 340, "090", "00", true, "S", "ES", null, null, null, "Berufsabschluss/Ass. 3j. (VZ) - Erziehung u. Soziales / Kinderpfleger/-in", "Berufsabschluss/Ass. 3j. (VZ) - Erziehung u. Soziales / Kinderpfleger", "Berufsabschluss/Ass. 3j. (VZ) - Erziehung u. Soziales / Kinderpflegerin", null, 2012)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 350
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex350(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Berufsabschluss/ZQ. 3j. (VZ) - Kunst/Musik/Gestaltung/Techn. Assistent  - Kosm. (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_350_087_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1625000, 350, "087", "00", true, "G", "KG", null, null, null, "Berufsabschluss/ZQ. 3j. (VZ) - Kunst/Musik/Gestaltung/Techn. Assistent  - Kosm.", "Berufsabschluss/ZQ. 3j. (VZ) - Kunst/Musik/Gestaltung/Techn. Assistent  - Kosm.", "Berufsabschluss/ZQ. 3j. (VZ) - Kunst/Musik/Gestaltung/Techn. Assistent  - Kosm.", null, 2005)
		}));

		/** Fachklasse Berufsabschluss/ZQ. 3j. (VZ) - Sprache u. Literatur/Fremdsprachenkorrespondent/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_350_088_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1626000, 350, "088", "00", true, "L", "SL", null, null, null, "Berufsabschluss/ZQ. 3j. (VZ) - Sprache u. Literatur/Fremdsprachenkorrespondent/-in", "Berufsabschluss/ZQ. 3j. (VZ) - Sprache u. Literatur/Fremdsprachenkorrespondent", "Berufsabschluss/ZQ. 3j. (VZ) - Sprache u. Literatur/Fremdsprachenkorrespondentin", null, 2012)
		}));

		/** Fachklasse Berufsabschluss/ZQ. 3j. (VZ) - Erziehung u. Soziales/Erzieher/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_350_089_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1627000, 350, "089", "00", true, "S", "ES", null, null, null, "Berufsabschluss/ZQ. 3j. (VZ) - Erziehung u. Soziales/Erzieher/-in", "Berufsabschluss/ZQ. 3j. (VZ) - Erziehung u. Soziales/Erzieher", "Berufsabschluss/ZQ. 3j. (VZ) - Erziehung u. Soziales/Erzieherin", null, 2012)
		}));

		/** Fachklasse Berufsabschluss/ZQ. 3j. (VZ) - Textil- u. Bekleidungstechnik/Damenschneider/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_350_090_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1628000, 350, "090", "00", true, "T", "TB", null, null, null, "Berufsabschluss/ZQ. 3j. (VZ) - Textil- u. Bekleidungstechnik/Damenschneider/-in", "Berufsabschluss/ZQ. 3j. (VZ) - Textil- u. Bekleidungstechnik/Damenschneider", "Berufsabschluss/ZQ. 3j. (VZ) - Textil- u. Bekleidungstechnik/Damenschneiderin", null, 2012)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 370
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex370(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Kooperationsklasse Hauptschule */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_370_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1629000, 370, "101", "00", false, null, null, null, null, null, "Kooperationsklasse Hauptschule", "Kooperationsklasse Hauptschule", "Kooperationsklasse Hauptschule", null, null)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 40
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex40(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Agrarwirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_101_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1630000, 40, "101", "01", true, "A", "AW", null, null, null, "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Agrarwirtschaft", "Schüler mit Arbeitsverhältnis und Praktikanten - Agrarwirtschaft", "Schülerinnen mit Arbeitsverhältnis und Praktikantinnen - Agrarwirtschaft", null, 2015)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Bautechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_101_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1631000, 40, "101", "02", true, "T", "BT", null, null, null, "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Bautechnik", "Schüler mit Arbeitsverhältnis und Praktikanten - Bautechnik", "Schülerinnen mit Arbeitsverhältnis und Praktikantinnen - Bautechnik", null, 2015)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Drucktechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_101_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1632000, 40, "101", "03", true, "T", "DT", null, null, null, "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Drucktechnik", "Schüler mit Arbeitsverhältnis und Praktikanten - Drucktechnik", "Schülerinnen mit Arbeitsverhältnis und Praktikantinnen - Drucktechnik", null, 2015)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Elektrotechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_101_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1633000, 40, "101", "04", true, "T", "ET", null, null, null, "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Elektrotechnik", "Schüler mit Arbeitsverhältnis und Praktikanten - Elektrotechnik", "Schülerinnen mit Arbeitsverhältnis und Praktikantinnen - Elektrotechnik", null, 2015)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Ernährung u. Hauswirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_101_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1634000, 40, "101", "05", true, "E", "EH", null, null, null, "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Ernährung u. Hauswirtschaft", "Schüler mit Arbeitsverhältnis und Praktikanten - Ernährung und Hauswirtschaft", "Schülerinnen mit Arbeitsverhältnis und Praktikantinnen - Ernährung und Hauswirtschaft", null, 2015)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Farbtechnik u. Raumgestaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_101_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1635000, 40, "101", "06", true, "G", "FR", null, null, null, "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Farbtechnik u. Raumgestaltung", "Schüler mit Arbeitsverhältnis und Praktikanten - Farbtechnik und Raumgestaltung", "Schülerinnen mit Arbeitsverhältnis und Praktikantinnen - Farbtechnik und Raumgestaltung", null, 2015)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Holztechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_101_07", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1636000, 40, "101", "07", true, "T", "HT", null, null, null, "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Holztechnik", "Schüler mit Arbeitsverhältnis und Praktikanten - Holztechnik", "Schülerinnen mit Arbeitsverhältnis und Praktikantinnen - Holztechnik", null, 2015)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Informations- u. Telekomm.technik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_101_08", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1637000, 40, "101", "08", true, "T", "IT", null, null, null, "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Informations- u. Telekomm.technik", "Schüler mit Arbeitsverhältnis und Praktikanten - Informations- u. Telekomm.technik", "Schülerinnen mit Arbeitsverhältnis und Praktikantinnen - Informations- u. Telekomm.technik", null, 2015)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Körperpflege (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_101_09", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1638000, 40, "101", "09", true, "S", "KP", null, null, null, "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Körperpflege", "Schüler mit Arbeitsverhältnis und Praktikanten - Körperpflege", "Schülerinnen mit Arbeitsverhältnis und Praktikantinnen - Körperpflege", null, 2015)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Medien/Medientechnologie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_101_10", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1639000, 40, "101", "10", true, "T", "MM", null, null, null, "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Medien/Medientechnologie", "Schüler mit Arbeitsverhältnis und Praktikanten - Medien/Medientechnologie", "Schülerinnen mit Arbeitsverhältnis und Praktikantinnen - Medien/Medientechnologie", null, 2015)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Medizintechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_101_11", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1640000, 40, "101", "11", true, "T", "MZ", null, null, null, "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Medizintechnik", "Schüler mit Arbeitsverhältnis und Praktikanten - Medizintechnik", "Schülerinnen mit Arbeitsverhältnis und Praktikantinnen - Medizintechnik", null, 2015)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Metalltechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_101_12", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1641000, 40, "101", "12", true, "T", "MT", null, null, null, "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Metalltechnik", "Schüler mit Arbeitsverhältnis und Praktikanten - Metalltechnik", "Schülerinnen mit Arbeitsverhältnis und Praktikantinnen - Metalltechnik", null, 2015)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Physik, Chemie, Biologie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_101_13", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1642000, 40, "101", "13", true, "T", "CP", null, null, null, "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Physik, Chemie, Biologie", "Schüler mit Arbeitsverhältnis und Praktikanten - Physik, Chemie, Biologie", "Schülerinnen mit Arbeitsverhältnis und Praktikantinnen - Physik, Chemie, Biologie", null, 2015)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Sozial- u. Gesundheitswesen (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_101_14", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1643000, 40, "101", "14", true, "S", "SG", null, null, null, "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Sozial- u. Gesundheitswesen", "Schüler mit Arbeitsverhältnis und Praktikanten - Sozial- u. Gesundheitswesen", "Schülerinnen mit Arbeitsverhältnis und Praktikantinnen - Sozial- u. Gesundheitswesen", null, 2015)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Textiltechnik u. Bekleidung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_101_15", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1644000, 40, "101", "15", true, "T", "TB", null, null, null, "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Textiltechnik u. Bekleidung", "Schüler mit Arbeitsverhältnis und Praktikanten - Textiltechnik u. Bekleidung", "Schülerinnen mit Arbeitsverhältnis und Praktikantinnen - Textiltechnik u. Bekleidung", null, 2015)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Vermessungstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_101_16", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1645000, 40, "101", "16", true, "T", "VT", null, null, null, "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Vermessungstechnik", "Schüler mit Arbeitsverhältnis und Praktikanten - Vermessungstechnik", "Schülerinnen mit Arbeitsverhältnis und Praktikantinnen - Vermessungstechnik", null, 2015)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Wirtschaft u. Verwaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_101_17", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1646000, 40, "101", "17", true, "W", "WV", null, null, null, "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Wirtschaft u. Verwaltung", "Schüler mit Arbeitsverhältnis und Praktikanten - Wirtschaft und Verwaltung", "Schülerinnen mit Arbeitsverhältnis und Praktikantinnen - Wirtschaft und Verwaltung", null, 2015)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Fahrzeugtechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_101_18", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1647000, 40, "101", "18", true, "T", "FT", null, null, null, "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Fahrzeugtechnik", "Schüler mit Arbeitsverhältnis und Praktikanten - Fahrzeugtechnik", "Schülerinnen mit Arbeitsverhältnis und Praktikantinnen - Fahrzeugtechnik", null, 2015)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverhältnis und Praktikanten/Praktikantinnen - Kunst, Musik, Gestaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_101_21", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1648000, 40, "101", "21", true, "G", "FR", null, null, null, "Schüler/-innen mit Arbeitsverhältnis und Praktikanten/Praktikantinnen - Kunst, Musik, Gestaltung", "Schüler mit Arbeitsverhältnis und Praktikanten - Kunst, Musik, Gestaltung", "Schülerinnen mit Arbeitsverhältnis und Praktikanten - Kunst, Musik, Gestaltung", null, 2014)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverhältnis und Praktikanten/Praktikantinnen - Land- u. Hauswirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_101_22", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1649000, 40, "101", "22", true, "A", "LH", null, null, null, "Schüler/-innen mit Arbeitsverhältnis und Praktikanten/Praktikantinnen - Land- u. Hauswirtschaft", "Schüler mit Arbeitsverhältnis und Praktikanten - Land- u. Hauswirtschaft", "Schülerinnen mit Arbeitsverhältnis und Praktikanten - Land- u. Hauswirtschaft", null, 2014)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverhältnis und Praktikanten/Praktikantinnen - ohne Schwerpunkt (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_101_23", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1650000, 40, "101", "23", true, "O", "OH", null, null, null, "Schüler/-innen mit Arbeitsverhältnis und Praktikanten/Praktikantinnen - ohne Schwerpunkt", "Schüler mit Arbeitsverhältnis und Praktikanten - ohne Schwerpunkt", "Schülerinnen mit Arbeitsverhältnis und Praktikanten - ohne Schwerpunkt", null, 2014)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Agrarwirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_102_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1651000, 40, "102", "01", true, "A", "AW", null, null, null, "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Agrarwirtschaft", "Schüler in berufsvorb. Maßnahmen der AV u. freier Träger - Agrarwirtschaft", "Schülerinnen in berufsvorb. Maßnahmen der AV u. freier Träger - Agrarwirtschaft", null, 2015)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Bautechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_102_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1652000, 40, "102", "02", true, "T", "BT", null, null, null, "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Bautechnik", "Schüler in berufsvorb. Maßnahmen der AV u. freier Träger - Bautechnik", "Schülerinnen in berufsvorb. Maßnahmen der AV u. freier Träger - Bautechnik", null, 2015)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Drucktechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_102_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1653000, 40, "102", "03", true, "T", "DT", null, null, null, "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Drucktechnik", "Schüler in berufsvorb. Maßnahmen der AV u. freier Träger - Drucktechnik", "Schülerinnen in berufsvorb. Maßnahmen der AV u. freier Träger - Drucktechnik", null, 2015)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Elektrotechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_102_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1654000, 40, "102", "04", true, "T", "ET", null, null, null, "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Elektrotechnik", "Schüler in berufsvorb. Maßnahmen der AV u. freier Träger - Elektrotechnik", "Schülerinnen in berufsvorb. Maßnahmen der AV u. freier Träger - Elektrotechnik", null, 2015)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Ernährung und Hauswirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_102_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1655000, 40, "102", "05", true, "E", "EH", null, null, null, "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Ernährung und Hauswirtschaft", "Schüler in berufsvorb. Maßnahmen der AV u. freier Träger - Ernährung und Hauswirtschaft", "Schülerinnen in berufsvorb. Maßnahmen der AV u. freier Träger - Ernährung und Hauswirtschaft", null, 2015)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Farbtechnik u.Raumgestaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_102_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1656000, 40, "102", "06", true, "G", "FR", null, null, null, "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Farbtechnik u.Raumgestaltung", "Schüler in berufsvorb. Maßnahmen der AV u. freier Träger - Farbtechnik u.Raumgestaltung", "Schülerinnen in berufsvorb. Maßnahmen der AV u. freier Träger - Farbtechnik u.Raumgestaltung", null, 2015)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Holztechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_102_07", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1657000, 40, "102", "07", true, "T", "HT", null, null, null, "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Holztechnik", "Schüler in berufsvorb. Maßnahmen der AV u. freier Träger - Holztechnik", "Schülerinnen in berufsvorb. Maßnahmen der AV u. freier Träger - Holztechnik", null, 2015)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Informations- u. Telekomm.technik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_102_08", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1658000, 40, "102", "08", true, "T", "IT", null, null, null, "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Informations- u. Telekomm.technik", "Schüler in berufsvorb. Maßnahmen der AV u. freier Träger - Informations- u. Telekomm.technik", "Schülerinnen in berufsvorb. Maßnahmen der AV u. freier Träger - Informations- u. Telekomm.technik", null, 2015)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Körperpflege (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_102_09", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1659000, 40, "102", "09", true, "S", "KP", null, null, null, "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Körperpflege", "Schüler in berufsvorb. Maßnahmen der AV u. freier Träger - Körperpflege", "Schülerinnen in berufsvorb. Maßnahmen der AV u. freier Träger - Körperpflege", null, 2015)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Medien/Medientechnologie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_102_10", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1660000, 40, "102", "10", true, "T", "MM", null, null, null, "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Medien/Medientechnologie", "Schüler in berufsvorb. Maßnahmen der AV u. freier Träger - Medien/Medientechnologie", "Schülerinnen in berufsvorb. Maßnahmen der AV u. freier Träger - Medien/Medientechnologie", null, 2015)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Medizintechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_102_11", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1661000, 40, "102", "11", true, "T", "MZ", null, null, null, "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Medizintechnik", "Schüler in berufsvorb. Maßnahmen der AV u. freier Träger - Medizintechnik", "Schülerinnen in berufsvorb. Maßnahmen der AV u. freier Träger - Medizintechnik", null, 2015)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Metalltechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_102_12", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1662000, 40, "102", "12", true, "T", "MT", null, null, null, "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Metalltechnik", "Schüler in berufsvorb. Maßnahmen der AV u. freier Träger - Metalltechnik", "Schülerinnen in berufsvorb. Maßnahmen der AV u. freier Träger - Metalltechnik", null, 2015)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Physik, Chemie, Biologie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_102_13", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1663000, 40, "102", "13", true, "T", "CP", null, null, null, "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Physik, Chemie, Biologie", "Schüler in berufsvorb. Maßnahmen der AV u. freier Träger - Physik, Chemie, Biologie", "Schülerinnen in berufsvorb. Maßnahmen der AV u. freier Träger - Physik, Chemie, Biologie", null, 2015)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Sozial- u. Gesundheitswesen (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_102_14", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1664000, 40, "102", "14", true, "S", "SG", null, null, null, "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Sozial- u. Gesundheitswesen", "Schüler in berufsvorb. Maßnahmen der AV u. freier Träger - Sozial- u. Gesundheitswesen", "Schülerinnen in berufsvorb. Maßnahmen der AV u. freier Träger - Sozial- u. Gesundheitswesen", null, 2015)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Textiltechnik und Bekleidung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_102_15", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1665000, 40, "102", "15", true, "T", "TB", null, null, null, "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Textiltechnik und Bekleidung", "Schüler in berufsvorb. Maßnahmen der AV u. freier Träger - Textiltechnik und Bekleidung", "Schülerinnen in berufsvorb. Maßnahmen der AV u. freier Träger - Textiltechnik und Bekleidung", null, 2015)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Vermessungstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_102_16", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1666000, 40, "102", "16", true, "T", "VT", null, null, null, "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Vermessungstechnik", "Schüler in berufsvorb. Maßnahmen der AV u. freier Träger - Vermessungstechnik", "Schülerinnen in berufsvorb. Maßnahmen der AV u. freier Träger - Vermessungstechnik", null, 2015)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Wirtschaft und Verwaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_102_17", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1667000, 40, "102", "17", true, "W", "WV", null, null, null, "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Wirtschaft und Verwaltung", "Schüler in berufsvorb. Maßnahmen der AV u. freier Träger - Wirtschaft und Verwaltung", "Schülerinnen in berufsvorb. Maßnahmen der AV u. freier Träger - Wirtschaft und Verwaltung", null, 2015)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Fahrzeugtechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_102_18", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1668000, 40, "102", "18", true, "T", "FT", null, null, null, "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Fahrzeugtechnik", "Schüler in berufsvorb. Maßnahmen der AV u. freier Träger - Fahrzeugtechnik", "Schülerinnen in berufsvorb. Maßnahmen der AV u. freier Träger - Fahrzeugtechnik", null, 2015)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Agrarwirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_103_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1669000, 40, "103", "01", true, "A", "AW", null, null, null, "Schüler/-innen ohne Arbeitsverhältnis - Agrarwirtschaft", "Schüler ohne Arbeitsverhältnis - Agrarwirtschaft", "Schülerinnen ohne Arbeitsverhältnis - Agrarwirtschaft", null, 2015)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Bautechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_103_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1670000, 40, "103", "02", true, "T", "BT", null, null, null, "Schüler/-innen ohne Arbeitsverhältnis - Bautechnik", "Schüler ohne Arbeitsverhältnis - Bautechnik", "Schülerinnen ohne Arbeitsverhältnis - Bautechnik", null, 2015)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Drucktechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_103_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1671000, 40, "103", "03", true, "T", "DT", null, null, null, "Schüler/-innen ohne Arbeitsverhältnis - Drucktechnik", "Schüler ohne Arbeitsverhältnis - Drucktechnik", "Schülerinnen ohne Arbeitsverhältnis - Drucktechnik", null, 2015)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Elektrotechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_103_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1672000, 40, "103", "04", true, "T", "ET", null, null, null, "Schüler/-innen ohne Arbeitsverhältnis - Elektrotechnik", "Schüler ohne Arbeitsverhältnis - Elektrotechnik", "Schülerinnen ohne Arbeitsverhältnis - Elektrotechnik", null, 2015)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Ernährung und Hauswirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_103_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1673000, 40, "103", "05", true, "E", "EH", null, null, null, "Schüler/-innen ohne Arbeitsverhältnis - Ernährung und Hauswirtschaft", "Schüler ohne Arbeitsverhältnis - Ernährung und Hauswirtschaft", "Schülerinnen ohne Arbeitsverhältnis - Ernährung und Hauswirtschaft", null, 2015)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Farbtechnik und Raumgestaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_103_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1674000, 40, "103", "06", true, "G", "FR", null, null, null, "Schüler/-innen ohne Arbeitsverhältnis - Farbtechnik und Raumgestaltung", "Schüler ohne Arbeitsverhältnis - Farbtechnik und Raumgestaltung", "Schülerinnen ohne Arbeitsverhältnis - Farbtechnik und Raumgestaltung", null, 2015)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Holztechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_103_07", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1675000, 40, "103", "07", true, "T", "HT", null, null, null, "Schüler/-innen ohne Arbeitsverhältnis - Holztechnik", "Schüler ohne Arbeitsverhältnis - Holztechnik", "Schülerinnen ohne Arbeitsverhältnis - Holztechnik", null, 2015)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Informations- und Telekommunikationstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_103_08", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1676000, 40, "103", "08", true, "T", "IT", null, null, null, "Schüler/-innen ohne Arbeitsverhältnis - Informations- und Telekommunikationstechnik", "Schüler ohne Arbeitsverhältnis - Informations- und Telekommunikationstechnik", "Schülerinnen ohne Arbeitsverhältnis - Informations- und Telekommunikationstechnik", null, 2015)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Körperpflege (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_103_09", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1677000, 40, "103", "09", true, "S", "KP", null, null, null, "Schüler/-innen ohne Arbeitsverhältnis - Körperpflege", "Schüler ohne Arbeitsverhältnis - Körperpflege", "Schülerinnen ohne Arbeitsverhältnis - Körperpflege", null, 2015)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Medien/Medientechnologie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_103_10", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1678000, 40, "103", "10", true, "T", "MM", null, null, null, "Schüler/-innen ohne Arbeitsverhältnis - Medien/Medientechnologie", "Schüler ohne Arbeitsverhältnis - Medien/Medientechnologie", "Schülerinnen ohne Arbeitsverhältnis - Medien/Medientechnologie", null, 2015)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Medizintechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_103_11", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1679000, 40, "103", "11", true, "T", "MZ", null, null, null, "Schüler/-innen ohne Arbeitsverhältnis - Medizintechnik", "Schüler ohne Arbeitsverhältnis - Medizintechnik", "Schülerinnen ohne Arbeitsverhältnis - Medizintechnik", null, 2015)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Metalltechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_103_12", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1680000, 40, "103", "12", true, "T", "MT", null, null, null, "Schüler/-innen ohne Arbeitsverhältnis - Metalltechnik", "Schüler ohne Arbeitsverhältnis - Metalltechnik", "Schülerinnen ohne Arbeitsverhältnis - Metalltechnik", null, 2015)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Physik, Chemie, Biologie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_103_13", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1681000, 40, "103", "13", true, "T", "CP", null, null, null, "Schüler/-innen ohne Arbeitsverhältnis - Physik, Chemie, Biologie", "Schüler ohne Arbeitsverhältnis - Physik, Chemie, Biologie", "Schülerinnen ohne Arbeitsverhältnis - Physik, Chemie, Biologie", null, 2015)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Sozial- und Gesundheitswesen (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_103_14", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1682000, 40, "103", "14", true, "S", "SG", null, null, null, "Schüler/-innen ohne Arbeitsverhältnis - Sozial- und Gesundheitswesen", "Schüler ohne Arbeitsverhältnis - Sozial- und Gesundheitswesen", "Schülerinnen ohne Arbeitsverhältnis - Sozial- und Gesundheitswesen", null, 2015)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Textiltechnik und Bekleidung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_103_15", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1683000, 40, "103", "15", true, "T", "TB", null, null, null, "Schüler/-innen ohne Arbeitsverhältnis - Textiltechnik und Bekleidung", "Schüler ohne Arbeitsverhältnis - Textiltechnik und Bekleidung", "Schülerinnen ohne Arbeitsverhältnis - Textiltechnik und Bekleidung", null, 2015)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Vermessungstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_103_16", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1684000, 40, "103", "16", true, "T", "VT", null, null, null, "Schüler/-innen ohne Arbeitsverhältnis - Vermessungstechnik", "Schüler ohne Arbeitsverhältnis - Vermessungstechnik", "Schülerinnen ohne Arbeitsverhältnis - Vermessungstechnik", null, 2015)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Wirtschaft und Verwaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_103_17", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1685000, 40, "103", "17", true, "W", "WV", null, null, null, "Schüler/-innen ohne Arbeitsverhältnis - Wirtschaft und Verwaltung", "Schüler ohne Arbeitsverhältnis - Wirtschaft und Verwaltung", "Schülerinnen ohne Arbeitsverhältnis - Wirtschaft und Verwaltung", null, 2015)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Fahrzeugtechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_103_18", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1686000, 40, "103", "18", true, "T", "FT", null, null, null, "Schüler/-innen ohne Arbeitsverhältnis - Fahrzeugtechnik", "Schüler ohne Arbeitsverhältnis - Fahrzeugtechnik", "Schülerinnen ohne Arbeitsverhältnis - Fahrzeugtechnik", null, 2015)
		}));

		/** Fachklasse Werkstattjahr - Agrarwirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_104_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1687000, 40, "104", "01", true, "A", "AW", null, null, null, "Werkstattjahr - Agrarwirtschaft", "Werkstattjahr - Agrarwirtschaft", "Werkstattjahr - Agrarwirtschaft", null, 2015)
		}));

		/** Fachklasse Werkstattjahr - Bautechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_104_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1688000, 40, "104", "02", true, "T", "BT", null, null, null, "Werkstattjahr - Bautechnik", "Werkstattjahr - Bautechnik", "Werkstattjahr - Bautechnik", null, 2015)
		}));

		/** Fachklasse Werkstattjahr - Drucktechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_104_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1689000, 40, "104", "03", true, "T", "DT", null, null, null, "Werkstattjahr - Drucktechnik", "Werkstattjahr - Drucktechnik", "Werkstattjahr - Drucktechnik", null, 2015)
		}));

		/** Fachklasse Werkstattjahr - Elektrotechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_104_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1690000, 40, "104", "04", true, "T", "ET", null, null, null, "Werkstattjahr - Elektrotechnik", "Werkstattjahr - Elektrotechnik", "Werkstattjahr - Elektrotechnik", null, 2015)
		}));

		/** Fachklasse Werkstattjahr - Ernährung und Hauswirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_104_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1691000, 40, "104", "05", true, "E", "EH", null, null, null, "Werkstattjahr - Ernährung und Hauswirtschaft", "Werkstattjahr - Ernährung und Hauswirtschaft", "Werkstattjahr - Ernährung und Hauswirtschaft", null, 2015)
		}));

		/** Fachklasse Werkstattjahr - Farbtechnik und Raumgestaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_104_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1692000, 40, "104", "06", true, "G", "FR", null, null, null, "Werkstattjahr - Farbtechnik und Raumgestaltung", "Werkstattjahr - Farbtechnik und Raumgestaltung", "Werkstattjahr - Farbtechnik und Raumgestaltung", null, 2015)
		}));

		/** Fachklasse Werkstattjahr - Holztechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_104_07", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1693000, 40, "104", "07", true, "T", "HT", null, null, null, "Werkstattjahr - Holztechnik", "Werkstattjahr - Holztechnik", "Werkstattjahr - Holztechnik", null, 2015)
		}));

		/** Fachklasse Werkstattjahr - Informations- und Telekommunikationstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_104_08", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1694000, 40, "104", "08", true, "T", "IT", null, null, null, "Werkstattjahr - Informations- und Telekommunikationstechnik", "Werkstattjahr - Informations- und Telekommunikationstechnik", "Werkstattjahr - Informations- und Telekommunikationstechnik", null, 2015)
		}));

		/** Fachklasse Werkstattjahr - Körperpflege (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_104_09", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1695000, 40, "104", "09", true, "S", "KP", null, null, null, "Werkstattjahr - Körperpflege", "Werkstattjahr - Körperpflege", "Werkstattjahr - Körperpflege", null, 2015)
		}));

		/** Fachklasse Werkstattjahr - Medien/Medientechnologie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_104_10", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1696000, 40, "104", "10", true, "T", "MM", null, null, null, "Werkstattjahr - Medien/Medientechnologie", "Werkstattjahr - Medien/Medientechnologie", "Werkstattjahr - Medien/Medientechnologie", null, 2015)
		}));

		/** Fachklasse Werkstattjahr - Medizintechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_104_11", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1697000, 40, "104", "11", true, "T", "MZ", null, null, null, "Werkstattjahr - Medizintechnik", "Werkstattjahr - Medizintechnik", "Werkstattjahr - Medizintechnik", null, 2015)
		}));

		/** Fachklasse Werkstattjahr - Metalltechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_104_12", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1698000, 40, "104", "12", true, "T", "MT", null, null, null, "Werkstattjahr - Metalltechnik", "Werkstattjahr - Metalltechnik", "Werkstattjahr - Metalltechnik", null, 2015)
		}));

		/** Fachklasse Werkstattjahr - Physik, Chemie, Biologie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_104_13", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1699000, 40, "104", "13", true, "T", "CP", null, null, null, "Werkstattjahr - Physik, Chemie, Biologie", "Werkstattjahr - Physik, Chemie, Biologie", "Werkstattjahr - Physik, Chemie, Biologie", null, 2015)
		}));

		/** Fachklasse Werkstattjahr - Sozial- und Gesundheitswesen (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_104_14", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1700000, 40, "104", "14", true, "S", "SG", null, null, null, "Werkstattjahr - Sozial- und Gesundheitswesen", "Werkstattjahr - Sozial- und Gesundheitswesen", "Werkstattjahr - Sozial- und Gesundheitswesen", null, 2015)
		}));

		/** Fachklasse Werkstattjahr - Textiltechnik und Bekleidung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_104_15", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1701000, 40, "104", "15", true, "T", "TB", null, null, null, "Werkstattjahr - Textiltechnik und Bekleidung", "Werkstattjahr - Textiltechnik und Bekleidung", "Werkstattjahr - Textiltechnik und Bekleidung", null, 2015)
		}));

		/** Fachklasse Werkstattjahr - Vermessungstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_104_16", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1702000, 40, "104", "16", true, "T", "VT", null, null, null, "Werkstattjahr - Vermessungstechnik", "Werkstattjahr - Vermessungstechnik", "Werkstattjahr - Vermessungstechnik", null, 2015)
		}));

		/** Fachklasse Werkstattjahr - Wirtschaft und Verwaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_104_17", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1703000, 40, "104", "17", true, "W", "WV", null, null, null, "Werkstattjahr - Wirtschaft und Verwaltung", "Werkstattjahr - Wirtschaft und Verwaltung", "Werkstattjahr - Wirtschaft und Verwaltung", null, 2015)
		}));

		/** Fachklasse Werkstattjahr - Fahrzeugtechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_104_18", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1704000, 40, "104", "18", true, "T", "FT", null, null, null, "Werkstattjahr - Fahrzeugtechnik", "Werkstattjahr - Fahrzeugtechnik", "Werkstattjahr - Fahrzeugtechnik", null, 2015)
		}));

		/** Fachklasse Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1705000, 40, "999", "00", true, "X", "XX", null, null, null, "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, 2014)
		}));

		/** Fachklasse Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_999_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1706000, 40, "999", "01", true, "X", "XX", null, null, null, "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, 2014)
		}));

		/** Fachklasse Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_40_999_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1707000, 40, "999", "02", true, "X", "XX", null, null, null, "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, 2014)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 50
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex50(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Praktikanten/Praktikantinnen mit FO-Reife für FS der Sozialpädagogik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_50_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1708000, 50, "101", "00", true, "S", "SG", null, null, null, "Praktikanten/Praktikantinnen mit FO-Reife für FS der Sozialpädagogik", "Praktikanten mit FO-Reife für FS der Sozialpädagogik", "Praktikantinnen mit FO-Reife für FS der Sozialpädagogik", null, 2014)
		}));

		/** Fachklasse Praktikanten/Praktikantinnen mit FO-Reife für FS der Ernährung u. Hauswirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_50_102_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1709000, 50, "102", "00", true, "E", "EH", null, null, null, "Praktikanten/Praktikantinnen mit FO-Reife für FS der Ernährung u. Hauswirtschaft", "Praktikanten mit FO-Reife für FS der Ernährung u. Hauswirtschaft", "Praktikantinnen mit FO-Reife für FS der Ernährung u. Hauswirtschaft", null, 2014)
		}));

		/** Fachklasse Praktikanten/Praktikantinnen: Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_50_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1710000, 50, "999", "00", true, "X", "XX", null, null, null, "Praktikanten/Praktikantinnen: Sonstiger Bildungsgang", "Praktikanten: Sonstiger Bildungsgang", "Praktikanten: Sonstiger Bildungsgang", null, 2014)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 55
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex55(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Agrarwirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_55_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1711000, 55, "101", "00", true, "A", "AW", null, null, null, "Agrarwirtschaft", "Agrarwirtschaft", "Agrarwirtschaft", null, 2017)
		}));

		/** Fachklasse Bautechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_55_102_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1712000, 55, "102", "00", true, "T", "BT", null, null, null, "Bautechnik", "Bautechnik", "Bautechnik", null, 2015)
		}));

		/** Fachklasse Drucktechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_55_104_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1713000, 55, "104", "00", true, "T", "DT", null, null, null, "Drucktechnik", "Drucktechnik", "Drucktechnik", null, 2015)
		}));

		/** Fachklasse Elektrotechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_55_105_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1714000, 55, "105", "00", true, "T", "ET", null, null, null, "Elektrotechnik", "Elektrotechnik", "Elektrotechnik", null, 2015)
		}));

		/** Fachklasse Ernährung und Hauswirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_55_106_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1715000, 55, "106", "00", true, "E", "EH", null, null, null, "Ernährung und Hauswirtschaft", "Ernährung und Hauswirtschaft", "Ernährung und Hauswirtschaft", null, 2015)
		}));

		/** Fachklasse Farbtechnik und Raumgestaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_55_107_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1716000, 55, "107", "00", true, "G", "FR", null, null, null, "Farbtechnik und Raumgestaltung", "Farbtechnik und Raumgestaltung", "Farbtechnik und Raumgestaltung", null, 2015)
		}));

		/** Fachklasse Holztechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_55_108_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1717000, 55, "108", "00", true, "T", "HT", null, null, null, "Holztechnik", "Holztechnik", "Holztechnik", null, 2015)
		}));

		/** Fachklasse Informations- und Telekommunikationstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_55_109_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1718000, 55, "109", "00", true, "T", "IT", null, null, null, "Informations- und Telekommunikationstechnik", "Informations- und Telekommunikationstechnik", "Informations- und Telekommunikationstechnik", null, 2015)
		}));

		/** Fachklasse Körperpflege (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_55_110_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1719000, 55, "110", "00", true, "S", "KP", null, null, null, "Körperpflege", "Körperpflege", "Körperpflege", null, 2015)
		}));

		/** Fachklasse Medien/Medientechnologie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_55_112_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1720000, 55, "112", "00", true, "T", "MM", null, null, null, "Medien/Medientechnologie", "Medien/Medientechnologie", "Medien/Medientechnologie", null, 2015)
		}));

		/** Fachklasse Medizintechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_55_113_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1721000, 55, "113", "00", true, "T", "MZ", null, null, null, "Medizintechnik", "Medizintechnik", "Medizintechnik", null, 2015)
		}));

		/** Fachklasse Metalltechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_55_114_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1722000, 55, "114", "00", true, "T", "MT", null, null, null, "Metalltechnik", "Metalltechnik", "Metalltechnik", null, 2015)
		}));

		/** Fachklasse Physik,Chemie,Biologie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_55_116_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1723000, 55, "116", "00", true, "T", "CP", null, null, null, "Physik,Chemie,Biologie", "Physik,Chemie,Biologie", "Physik,Chemie,Biologie", null, 2015)
		}));

		/** Fachklasse Sozial- und Gesundheitswesen (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_55_117_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1724000, 55, "117", "00", true, "S", "SG", null, null, null, "Sozial- und Gesundheitswesen", "Sozial- und Gesundheitswesen", "Sozial- und Gesundheitswesen", null, 2015)
		}));

		/** Fachklasse Textiltechnik und Bekleidung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_55_118_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1725000, 55, "118", "00", true, "T", "TB", null, null, null, "Textiltechnik und Bekleidung", "Textiltechnik und Bekleidung", "Textiltechnik und Bekleidung", null, 2015)
		}));

		/** Fachklasse Vermessungstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_55_120_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1726000, 55, "120", "00", true, "T", "VT", null, null, null, "Vermessungstechnik", "Vermessungstechnik", "Vermessungstechnik", null, 2015)
		}));

		/** Fachklasse Wirtschaft und Verwaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_55_121_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1727000, 55, "121", "00", true, "W", "WV", null, null, null, "Wirtschaft und Verwaltung", "Wirtschaft und Verwaltung", "Wirtschaft und Verwaltung", null, 2015)
		}));

		/** Fachklasse Fahrzeugtechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_55_122_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1728000, 55, "122", "00", true, "T", "FT", null, null, null, "Fahrzeugtechnik", "Fahrzeugtechnik", "Fahrzeugtechnik", null, 2015)
		}));

		/** Fachklasse Praktikanten/Praktikantinnen mit Förderverträgen - Elektrotechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_55_123_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1729000, 55, "123", "04", true, "T", "ET", null, null, null, "Praktikanten/Praktikantinnen mit Förderverträgen - Elektrotechnik", "Praktikanten mit Förderverträgen - Elektrotechnik", "Praktikantinnen mit Förderverträgen - Elektrotechnik", null, 2015)
		}));

		/** Fachklasse Praktikanten/Praktikantinnen mit Förderverträgen - Metalltechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_55_123_12", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1730000, 55, "123", "12", true, "T", "MT", null, null, null, "Praktikanten/Praktikantinnen mit Förderverträgen - Metalltechnik", "Praktikanten mit Förderverträgen - Metalltechnik", "Praktikantinnen mit Förderverträgen - Metalltechnik", null, 2015)
		}));

		/** Fachklasse Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_55_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1731000, 55, "999", "00", true, "X", "XX", null, null, null, "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, 2014)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 56
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex56(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Elektroniker/-in für Betriebstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_56_177_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1732000, 56, "177", "02", true, null, null, "TN", null, null, "Elektroniker/-in für Betriebstechnik", "Elektroniker für Betriebstechnik", "Elektronikerin für Betriebstechnik", null, 2019)
		}));

		/** Fachklasse Elektroniker/-in für Geräte und Systeme (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_56_177_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1733000, 56, "177", "05", true, null, null, "TN", null, null, "Elektroniker/-in für Geräte und Systeme", "Elektroniker für Geräte und Systeme", "Elektronikerin für Geräte und Systeme", null, 2019)
		}));

		/** Fachklasse Florist/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_56_217_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1734000, 56, "217", "00", true, null, null, "GT", null, null, "Florist/-in", "Florist", "Floristin", null, 2019)
		}));

		/** Fachklasse Friseur/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_56_228_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1735000, 56, "228", "00", true, null, null, "GE", null, null, "Friseur/-in", "Friseur", "Friseurin", null, 2019)
		}));

		/** Fachklasse Glaser/-in - Verglasung und Glasbau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_56_242_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1736000, 56, "242", "02", true, null, null, "TN", null, null, "Glaser/-in - Verglasung und Glasbau", "Glaser - Verglasung und Glasbau", "Glaserin - Verglasung und Glasbau", null, 2019)
		}));

		/** Fachklasse Glasveredler/-in - Schliff und Gravur (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_56_248_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1737000, 56, "248", "01", true, null, null, "TN", null, null, "Glasveredler/-in - Schliff und Gravur", "Glasveredler - Schliff und Gravur", "Glasveredlerin - Schliff und Gravur", null, 2019)
		}));

		/** Fachklasse Glasveredler/-in - Glasmalerei und Kunstverglasung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_56_248_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1738000, 56, "248", "02", true, null, null, "TN", null, null, "Glasveredler/-in - Glasmalerei und Kunstverglasung", "Glasveredler - Glasmalerei und Kunstverglasung", "Glasveredlerin - Glasmalerei und Kunstverglasung", null, 2019)
		}));

		/** Fachklasse Glasveredler/-in - Kanten- und Flächenveredlung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_56_248_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1739000, 56, "248", "03", true, null, null, "TN", null, null, "Glasveredler/-in - Kanten- und Flächenveredlung", "Glasveredler - Kanten- und Flächenveredlung", "Glasveredlerin - Kanten- und Flächenveredlung", null, 2019)
		}));

		/** Fachklasse Hauswirtschafter/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_56_257_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1740000, 56, "257", "00", true, null, null, "EV", null, null, "Hauswirtschafter/-in", "Hauswirtschafter", "Hauswirtschafterin", null, 2019)
		}));

		/** Fachklasse Industriemechaniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_56_276_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1741000, 56, "276", "00", true, null, null, "TN", null, null, "Industriemechaniker/-in", "Industriemechaniker", "Industriemechanikerin", null, 2019)
		}));

		/** Fachklasse Informations- und Telekommunikationssystemelektroniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_56_279_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1742000, 56, "279", "00", true, null, null, "TN", null, null, "Informations- und Telekommunikationssystemelektroniker/-in", "Informations- und Telekommunikationssystemelektroniker", "Informations- und Telekommunikationssystemelektronikerin", null, 2019)
		}));

		/** Fachklasse Mechatroniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_56_335_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1743000, 56, "335", "00", true, null, null, "TN", null, null, "Mechatroniker/-in", "Mechatroniker", "Mechatronikerin", null, 2019)
		}));

		/** Fachklasse Metallbauer/-in - Konstruktionstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_56_339_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1744000, 56, "339", "03", true, null, null, "TN", null, null, "Metallbauer/-in - Konstruktionstechnik", "Metallbauer - Konstruktionstechnik", "Metallbauerin - Konstruktionstechnik", null, 2019)
		}));

		/** Fachklasse Uhrmacher/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_56_456_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1745000, 56, "456", "00", true, null, null, "TN", null, null, "Uhrmacher/-in", "Uhrmacher", "Uhrmacherin", null, 2019)
		}));

		/** Fachklasse Verkäufer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_56_463_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1746000, 56, "463", "00", true, null, null, "WV", null, null, "Verkäufer/-in", "Verkäufer", "Verkäuferin", null, 2019)
		}));

		/** Fachklasse Werkzeugmechaniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_56_482_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1747000, 56, "482", "00", true, null, null, "TN", null, null, "Werkzeugmechaniker/-in", "Werkzeugmechaniker", "Werkzeugmechanikerin", null, 2019)
		}));

		/** Fachklasse Zerspanungsmechaniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_56_487_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1748000, 56, "487", "00", true, null, null, "TN", null, null, "Zerspanungsmechaniker/-in", "Zerspanungsmechaniker", "Zerspanungsmechanikerin", null, 2019)
		}));

		/** Fachklasse Kosmetiker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_56_515_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1749000, 56, "515", "00", true, null, null, "GE", null, null, "Kosmetiker/-in", "Kosmetiker", "Kosmetikerin", null, 2019)
		}));

		/** Fachklasse Fachkraft für Lagerlogistik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_56_527_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1750000, 56, "527", "00", true, null, null, "WV", null, null, "Fachkraft für Lagerlogistik", "Fachkraft für Lagerlogistik", "Fachkraft für Lagerlogistik", null, 2019)
		}));

		/** Fachklasse Fachlagerist/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_56_528_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1751000, 56, "528", "00", true, null, null, "WV", null, null, "Fachlagerist/-in", "Fachlagerist", "Fachlageristin", null, 2019)
		}));

		/** Fachklasse Maschinen- und Anlagenführer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_56_531_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1752000, 56, "531", "00", true, null, null, "TN", null, null, "Maschinen- und Anlagenführer/-in", "Maschinen- und Anlagenführer", "Maschinen- und Anlagenführerin", null, 2019)
		}));

		/** Fachklasse Maßschneider/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_56_532_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1753000, 56, "532", "00", true, null, null, "TN", null, null, "Maßschneider/-in", "Maßschneider", "Maßschneiderin", null, 2019)
		}));

		/** Fachklasse Änderungsschneider/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_56_538_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1754000, 56, "538", "00", true, null, null, "TN", null, null, "Änderungsschneider/-in", "Änderungsschneider", "Änderungsschneiderin", null, 2019)
		}));

		/** Fachklasse Oberflächenbeschichter/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_56_541_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1755000, 56, "541", "00", true, null, null, "TN", null, null, "Oberflächenbeschichter/-in", "Oberflächenbeschichter", "Oberflächenbeschichterin", null, 2019)
		}));

		/** Fachklasse Kaufmann/-frau für Büromanagement (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_56_607_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1756000, 56, "607", "00", true, null, null, "WV", null, null, "Kaufmann/-frau für Büromanagement", "Kaufmann für Büromanagement", "Kauffrau für Büromanagement", null, 2019)
		}));

		/** Fachklasse Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_56_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1757000, 56, "999", "00", true, null, null, "XX", "XX", "XX", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, 2015)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 57
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex57(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Agrarwirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_57_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1758000, 57, "101", "00", false, null, null, "AW", null, null, "Agrarwirtschaft", "Agrarwirtschaft", "Agrarwirtschaft", null, null)
		}));

		/** Fachklasse Ernährungs- und Versorgungsmanagement */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_57_102_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1759000, 57, "102", "00", false, null, null, "EV", null, null, "Ernährungs- und Versorgungsmanagement", "Ernährungs- und Versorgungsmanagement", "Ernährungs- und Versorgungsmanagement", null, null)
		}));

		/** Fachklasse Farbtechnik und Raumgestaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_57_103_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1760000, 57, "103", "01", false, null, null, "GT", "FR", null, "Farbtechnik und Raumgestaltung", "Farbtechnik und Raumgestaltung", "Farbtechnik und Raumgestaltung", null, null)
		}));

		/** Fachklasse Medien/Medientechnologie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_57_103_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1761000, 57, "103", "02", false, null, null, "GT", "MM", null, "Medien/Medientechnologie", "Medien/Medientechnologie", "Medien/Medientechnologie", null, null)
		}));

		/** Fachklasse Gesundheitswesen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_57_104_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1762000, 57, "104", "01", false, null, null, "GE", "GW", null, "Gesundheitswesen", "Gesundheitswesen", "Gesundheitswesen", null, null)
		}));

		/** Fachklasse Sozialwesen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_57_104_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1763000, 57, "104", "02", false, null, null, "GE", "SW", null, "Sozialwesen", "Sozialwesen", "Sozialwesen", null, null)
		}));

		/** Fachklasse Körperpflege */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_57_104_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1764000, 57, "104", "03", false, null, null, "GE", "KP", null, "Körperpflege", "Körperpflege", "Körperpflege", null, null)
		}));

		/** Fachklasse Informatik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_57_105_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1765000, 57, "105", "00", false, null, null, "IF", null, null, "Informatik", "Informatik", "Informatik", null, null)
		}));

		/** Fachklasse Bau und Holztechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_57_106_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1766000, 57, "106", "01", false, null, null, "TN", "BH", null, "Bau und Holztechnik", "Bau und Holztechnik", "Bau und Holztechnik", null, null)
		}));

		/** Fachklasse Drucktechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_57_106_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1767000, 57, "106", "02", false, null, null, "TN", "DT", null, "Drucktechnik", "Drucktechnik", "Drucktechnik", null, null)
		}));

		/** Fachklasse Elektrotechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_57_106_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1768000, 57, "106", "03", false, null, null, "TN", "ET", null, "Elektrotechnik", "Elektrotechnik", "Elektrotechnik", null, null)
		}));

		/** Fachklasse Fahrzeugtechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_57_106_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1769000, 57, "106", "04", false, null, null, "TN", "FT", null, "Fahrzeugtechnik", "Fahrzeugtechnik", "Fahrzeugtechnik", null, null)
		}));

		/** Fachklasse Medizintechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_57_106_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1770000, 57, "106", "05", false, null, null, "TN", "MD", null, "Medizintechnik", "Medizintechnik", "Medizintechnik", null, null)
		}));

		/** Fachklasse Metalltechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_57_106_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1771000, 57, "106", "06", false, null, null, "TN", "ME", null, "Metalltechnik", "Metalltechnik", "Metalltechnik", null, null)
		}));

		/** Fachklasse Physik/Chemie/Biologie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_57_106_07", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1772000, 57, "106", "07", false, null, null, "TN", "PC", null, "Physik/Chemie/Biologie", "Physik/Chemie/Biologie", "Physik/Chemie/Biologie", null, null)
		}));

		/** Fachklasse Textiltechnik und Bekleidung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_57_106_08", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1773000, 57, "106", "08", false, null, null, "TN", "TB", null, "Textiltechnik und Bekleidung", "Textiltechnik und Bekleidung", "Textiltechnik und Bekleidung", null, null)
		}));

		/** Fachklasse Wirtschaft und Verwaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_57_107_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1774000, 57, "107", "00", false, null, null, "WV", null, null, "Wirtschaft und Verwaltung", "Wirtschaft und Verwaltung", "Wirtschaft und Verwaltung", null, null)
		}));

		/** Fachklasse Praktikanten/ Praktikantinnen mit Förderverträgen - Elektrotechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_57_108_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1775000, 57, "108", "01", false, null, null, "TN", "ET", "PF", "Praktikanten/ Praktikantinnen mit Förderverträgen - Elektrotechnik", "Praktikanten/ Praktikantinnen mit Förderverträgen - Elektrotechnik", "Praktikantinnen mit Förderverträgen - Elektrotechnik", null, null)
		}));

		/** Fachklasse Praktikanten/ Praktikantinnen mit Förderverträgen - Metalltechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_57_108_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1776000, 57, "108", "02", false, null, null, "TN", "ME", "PF", "Praktikanten/ Praktikantinnen mit Förderverträgen - Metalltechnik", "Praktikanten/ Praktikantinnen mit Förderverträgen - Metalltechnik", "Praktikantinnen mit Förderverträgen - Metalltechnik", null, null)
		}));

		/** Fachklasse Internationale Förderklasse */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_57_109_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1777000, 57, "109", "00", false, null, null, "OZ", null, null, "Internationale Förderklasse", "Internationale Förderklasse", "Internationale Förderklasse", null, null)
		}));

		/** Fachklasse Fit für mehr */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_57_110_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1778000, 57, "110", "00", false, null, null, "OZ", null, null, "Fit für mehr", "Fit für mehr", "Fit für mehr", null, null)
		}));

		/** Fachklasse Sonstiger Bildungsgang */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_57_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1779000, 57, "999", "00", false, null, null, "XX", "XX", "XX", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", 2022, null)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 58
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex58(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Agrarwirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1780000, 58, "101", "00", false, null, null, "AW", null, "AP", "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Agrarwirtschaft", "Schüler mit Arbeitsverh. u. Praktikanten - Agrarwirtschaft", "Schülerinnen mit Arbeitsverh. u. Praktikantinnen - Agrarwirtschaft", null, null)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Ernährungs- u. Versorgungsmanagem. */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_102_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1781000, 58, "102", "00", false, null, null, "EV", null, "AP", "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Ernährungs- u. Versorgungsmanagem.", "Schüler mit Arbeitsverh. u. Praktikanten - Ernährungs- u. Versorgungsmanagem.", "Schülerinnen mit Arbeitsverh. u. Praktikantinnen - Ernährungs- u. Versorgungsmanagem.", null, null)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Farbtechnik und Raumgestaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_103_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1782000, 58, "103", "01", false, null, null, "GT", "FR", "AP", "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Farbtechnik und Raumgestaltung", "Schüler mit Arbeitsverh. u. Praktikanten - Farbtechnik und Raumgestaltung", "Schülerinnen mit Arbeitsverh. u. Praktikantinnen - Farbtechnik und Raumgestaltung", null, null)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Medien/Medientechnologie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_103_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1783000, 58, "103", "02", false, null, null, "GT", "MM", "AP", "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Medien/Medientechnologie", "Schüler mit Arbeitsverh. u. Praktikanten - Medien/Medientechnologie", "Schülerinnen mit Arbeitsverh. u. Praktikantinnen - Medien/Medientechnologie", null, null)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Gesundheitswesen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_104_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1784000, 58, "104", "01", false, null, null, "GE", "GW", "AP", "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Gesundheitswesen", "Schüler mit Arbeitsverh. u. Praktikanten - Gesundheitswesen", "Schülerinnen mit Arbeitsverh. u. Praktikantinnen - Gesundheitswesen", null, null)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Sozialwesen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_104_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1785000, 58, "104", "02", false, null, null, "GE", "SW", "AP", "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Sozialwesen", "Schüler mit Arbeitsverh. u. Praktikanten - Sozialwesen", "Schülerinnen mit Arbeitsverh. u. Praktikantinnen - Sozialwesen", null, null)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Körperpflege */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_104_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1786000, 58, "104", "03", false, null, null, "GE", "KP", "AP", "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Körperpflege", "Schüler mit Arbeitsverh. u. Praktikanten - Körperpflege", "Schülerinnen mit Arbeitsverh. u. Praktikantinnen - Körperpflege", null, null)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Informatik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_105_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1787000, 58, "105", "00", false, null, null, "IF", null, "AP", "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Informatik", "Schüler mit Arbeitsverh. u. Praktikanten - Informatik", "Schülerinnen mit Arbeitsverh. u. Praktikantinnen - Informatik", null, null)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Bau und Holztechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_106_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1788000, 58, "106", "01", false, null, null, "TN", "BH", "AP", "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Bau und Holztechnik", "Schüler mit Arbeitsverh. u. Praktikanten - Bau und Holztechnik", "Schülerinnen mit Arbeitsverh. u. Praktikantinnen - Bau und Holztechnik", null, null)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Drucktechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_106_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1789000, 58, "106", "02", false, null, null, "TN", "DT", "AP", "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Drucktechnik", "Schüler mit Arbeitsverh. u. Praktikanten - Drucktechnik", "Schülerinnen mit Arbeitsverh. u. Praktikantinnen - Drucktechnik", null, null)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Elektrotechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_106_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1790000, 58, "106", "03", false, null, null, "TN", "ET", "AP", "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Elektrotechnik", "Schüler mit Arbeitsverh. u. Praktikanten - Elektrotechnik", "Schülerinnen mit Arbeitsverh. u. Praktikantinnen - Elektrotechnik", null, null)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Fahrzeugtechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_106_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1791000, 58, "106", "04", false, null, null, "TN", "FT", "AP", "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Fahrzeugtechnik", "Schüler mit Arbeitsverh. u. Praktikanten - Fahrzeugtechnik", "Schülerinnen mit Arbeitsverh. u. Praktikantinnen - Fahrzeugtechnik", null, null)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Medizintechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_106_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1792000, 58, "106", "05", false, null, null, "TN", "MD", "AP", "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Medizintechnik", "Schüler mit Arbeitsverh. u. Praktikanten - Medizintechnik", "Schülerinnen mit Arbeitsverh. u. Praktikantinnen - Medizintechnik", null, null)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Metalltechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_106_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1793000, 58, "106", "06", false, null, null, "TN", "ME", "AP", "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Metalltechnik", "Schüler mit Arbeitsverh. u. Praktikanten - Metalltechnik", "Schülerinnen mit Arbeitsverh. u. Praktikantinnen - Metalltechnik", null, null)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Physik/Chemie/Biologie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_106_07", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1794000, 58, "106", "07", false, null, null, "TN", "PC", "AP", "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Physik/Chemie/Biologie", "Schüler mit Arbeitsverh. u. Praktikanten - Physik/Chemie/Biologie", "Schülerinnen mit Arbeitsverh. u. Praktikantinnen - Physik/Chemie/Biologie", null, null)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Textiltechnik und Bekleidung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_106_08", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1795000, 58, "106", "08", false, null, null, "TN", "TB", "AP", "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Textiltechnik und Bekleidung", "Schüler mit Arbeitsverh. u. Praktikanten - Textiltechnik und Bekleidung", "Schülerinnen mit Arbeitsverh. u. Praktikantinnen - Textiltechnik und Bekleidung", null, null)
		}));

		/** Fachklasse Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Wirtschaft und Verwaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_107_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1796000, 58, "107", "00", false, null, null, "WV", null, "AP", "Schüler/-innen mit Arbeitsverh. u. Praktikanten/Praktikantinnen - Wirtschaft und Verwaltung", "Schüler mit Arbeitsverh. u. Praktikanten - Wirtschaft und Verwaltung", "Schülerinnen mit Arbeitsverh. u. Praktikantinnen - Wirtschaft und Verwaltung", null, null)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Agrarwirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_201_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1797000, 58, "201", "00", false, null, null, "AW", null, "AV", "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Agrarwirtschaft", "Schüler in berufsvorb. Maßn. der AV u. freier Träger - Agrarwirtschaft", "Schülerinnen in berufsvorb. Maßn. der AV u. freier Träger - Agrarwirtschaft", null, null)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Ernährungs- und Versorgungsmanagement */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_202_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1798000, 58, "202", "00", false, null, null, "EV", null, "AV", "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Ernährungs- und Versorgungsmanagement", "Schüler in berufsvorb. Maßn. der AV u. freier Träger - Ernährungs- und Versorgungsmanagement", "Schülerinnen in berufsvorb. Maßn. der AV u. freier Träger - Ernährungs- und Versorgungsmanagement", null, null)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Farbtechnik und Raumgestaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_203_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1799000, 58, "203", "01", false, null, null, "GT", "FR", "AV", "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Farbtechnik und Raumgestaltung", "Schüler in berufsvorb. Maßn. der AV u. freier Träger - Farbtechnik und Raumgestaltung", "Schülerinnen in berufsvorb. Maßn. der AV u. freier Träger - Farbtechnik und Raumgestaltung", null, null)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Medien/Medientechnologie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_203_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1800000, 58, "203", "02", false, null, null, "GT", "MM", "AV", "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Medien/Medientechnologie", "Schüler in berufsvorb. Maßn. der AV u. freier Träger - Medien/Medientechnologie", "Schülerinnen in berufsvorb. Maßn. der AV u. freier Träger - Medien/Medientechnologie", null, null)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Gesundheitswesen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_204_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1801000, 58, "204", "01", false, null, null, "GE", "GW", "AV", "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Gesundheitswesen", "Schüler in berufsvorb. Maßn. der AV u. freier Träger - Gesundheitswesen", "Schülerinnen in berufsvorb. Maßn. der AV u. freier Träger - Gesundheitswesen", null, null)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Sozialwesen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_204_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1802000, 58, "204", "02", false, null, null, "GE", "SW", "AV", "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Sozialwesen", "Schüler in berufsvorb. Maßn. der AV u. freier Träger - Sozialwesen", "Schülerinnen in berufsvorb. Maßn. der AV u. freier Träger - Sozialwesen", null, null)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Körperpflege */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_204_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1803000, 58, "204", "03", false, null, null, "GE", "KP", "AV", "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Körperpflege", "Schüler in berufsvorb. Maßn. der AV u. freier Träger - Körperpflege", "Schülerinnen in berufsvorb. Maßn. der AV u. freier Träger - Körperpflege", null, null)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Informatik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_205_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1804000, 58, "205", "00", false, null, null, "IF", null, "AV", "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Informatik", "Schüler in berufsvorb. Maßn. der AV u. freier Träger - Informatik", "Schülerinnen in berufsvorb. Maßn. der AV u. freier Träger - Informatik", null, null)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Bau und Holztechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_206_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1805000, 58, "206", "01", false, null, null, "TN", "BH", "AV", "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Bau und Holztechnik", "Schüler in berufsvorb. Maßn. der AV u. freier Träger - Bau und Holztechnik", "Schülerinnen in berufsvorb. Maßn. der AV u. freier Träger - Bau und Holztechnik", null, null)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Drucktechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_206_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1806000, 58, "206", "02", false, null, null, "TN", "DT", "AV", "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Drucktechnik", "Schüler in berufsvorb. Maßn. der AV u. freier Träger - Drucktechnik", "Schülerinnen in berufsvorb. Maßn. der AV u. freier Träger - Drucktechnik", null, null)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Elektrotechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_206_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1807000, 58, "206", "03", false, null, null, "TN", "ET", "AV", "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Elektrotechnik", "Schüler in berufsvorb. Maßn. der AV u. freier Träger - Elektrotechnik", "Schülerinnen in berufsvorb. Maßn. der AV u. freier Träger - Elektrotechnik", null, null)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Fahrzeugtechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_206_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1808000, 58, "206", "04", false, null, null, "TN", "FT", "AV", "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Fahrzeugtechnik", "Schüler in berufsvorb. Maßn. der AV u. freier Träger - Fahrzeugtechnik", "Schülerinnen in berufsvorb. Maßn. der AV u. freier Träger - Fahrzeugtechnik", null, null)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Medizintechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_206_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1809000, 58, "206", "05", false, null, null, "TN", "MD", "AV", "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Medizintechnik", "Schüler in berufsvorb. Maßn. der AV u. freier Träger - Medizintechnik", "Schülerinnen in berufsvorb. Maßn. der AV u. freier Träger - Medizintechnik", null, null)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Metalltechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_206_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1810000, 58, "206", "06", false, null, null, "TN", "ME", "AV", "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Metalltechnik", "Schüler in berufsvorb. Maßn. der AV u. freier Träger - Metalltechnik", "Schülerinnen in berufsvorb. Maßn. der AV u. freier Träger - Metalltechnik", null, null)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Physik/Chemie/Biologie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_206_07", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1811000, 58, "206", "07", false, null, null, "TN", "PC", "AV", "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Physik/Chemie/Biologie", "Schüler in berufsvorb. Maßn. der AV u. freier Träger - Physik/Chemie/Biologie", "Schülerinnen in berufsvorb. Maßn. der AV u. freier Träger - Physik/Chemie/Biologie", null, null)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Textiltechnik und Bekleidung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_206_08", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1812000, 58, "206", "08", false, null, null, "TN", "TB", "AV", "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Textiltechnik und Bekleidung", "Schüler in berufsvorb. Maßn. der AV u. freier Träger - Textiltechnik und Bekleidung", "Schülerinnen in berufsvorb. Maßn. der AV u. freier Träger - Textiltechnik und Bekleidung", null, null)
		}));

		/** Fachklasse Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Wirtschaft und Verwaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_207_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1813000, 58, "207", "00", false, null, null, "WV", null, "AV", "Schüler/-innen in berufsvorb. Maßn. der AV u. freier Träger - Wirtschaft und Verwaltung", "Schüler in berufsvorb. Maßn. der AV u. freier Träger - Wirtschaft und Verwaltung", "Schülerinnen in berufsvorb. Maßn. der AV u. freier Träger - Wirtschaft und Verwaltung", null, null)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Agrarwirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_301_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1814000, 58, "301", "00", false, null, null, "AW", null, "OA", "Schüler/-innen ohne Arbeitsverhältnis - Agrarwirtschaft", "Schüler ohne Arbeitsverhältnis - Agrarwirtschaft", "Schülerinnen ohne Arbeitsverhältnis - Agrarwirtschaft", null, null)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Ernährungs- und Versorgungsmanagement */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_302_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1815000, 58, "302", "00", false, null, null, "EV", null, "OA", "Schüler/-innen ohne Arbeitsverhältnis - Ernährungs- und Versorgungsmanagement", "Schüler ohne Arbeitsverhältnis - Ernährungs- und Versorgungsmanagement", "Schülerinnen ohne Arbeitsverhältnis - Ernährungs- und Versorgungsmanagement", null, null)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Farbtechnik und Raumgestaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_303_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1816000, 58, "303", "01", false, null, null, "GT", "FR", "OA", "Schüler/-innen ohne Arbeitsverhältnis - Farbtechnik und Raumgestaltung", "Schüler ohne Arbeitsverhältnis - Farbtechnik und Raumgestaltung", "Schülerinnen ohne Arbeitsverhältnis - Farbtechnik und Raumgestaltung", null, null)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Medien/Medientechnologie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_303_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1817000, 58, "303", "02", false, null, null, "GT", "MM", "OA", "Schüler/-innen ohne Arbeitsverhältnis - Medien/Medientechnologie", "Schüler ohne Arbeitsverhältnis - Medien/Medientechnologie", "Schülerinnen ohne Arbeitsverhältnis - Medien/Medientechnologie", null, null)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Gesundheitswesen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_304_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1818000, 58, "304", "01", false, null, null, "GE", "GW", "OA", "Schüler/-innen ohne Arbeitsverhältnis - Gesundheitswesen", "Schüler ohne Arbeitsverhältnis - Gesundheitswesen", "Schülerinnen ohne Arbeitsverhältnis - Gesundheitswesen", null, null)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Sozialwesen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_304_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1819000, 58, "304", "02", false, null, null, "GE", "SW", "OA", "Schüler/-innen ohne Arbeitsverhältnis - Sozialwesen", "Schüler ohne Arbeitsverhältnis - Sozialwesen", "Schülerinnen ohne Arbeitsverhältnis - Sozialwesen", null, null)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis -Körperpflege */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_304_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1820000, 58, "304", "03", false, null, null, "GE", "KP", "OA", "Schüler/-innen ohne Arbeitsverhältnis -Körperpflege", "Schüler ohne Arbeitsverhältnis - Körperpflege", "Schülerinnen ohne Arbeitsverhältnis -Körperpflege", null, null)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Informatik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_305_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1821000, 58, "305", "00", false, null, null, "IF", null, "OA", "Schüler/-innen ohne Arbeitsverhältnis - Informatik", "Schüler ohne Arbeitsverhältnis - Informatik", "Schülerinnen ohne Arbeitsverhältnis - Informatik", null, null)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Bau und Holztechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_306_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1822000, 58, "306", "01", false, null, null, "TN", "BH", "OA", "Schüler/-innen ohne Arbeitsverhältnis - Bau und Holztechnik", "Schüler ohne Arbeitsverhältnis - Bau und Holztechnik", "Schülerinnen ohne Arbeitsverhältnis - Bau und Holztechnik", null, null)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Drucktechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_306_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1823000, 58, "306", "02", false, null, null, "TN", "DT", "OA", "Schüler/-innen ohne Arbeitsverhältnis - Drucktechnik", "Schüler ohne Arbeitsverhältnis - Drucktechnik", "Schülerinnen ohne Arbeitsverhältnis - Drucktechnik", null, null)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Elektrotechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_306_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1824000, 58, "306", "03", false, null, null, "TN", "ET", "OA", "Schüler/-innen ohne Arbeitsverhältnis - Elektrotechnik", "Schüler ohne Arbeitsverhältnis - Elektrotechnik", "Schülerinnen ohne Arbeitsverhältnis - Elektrotechnik", null, null)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Fahrzeugtechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_306_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1825000, 58, "306", "04", false, null, null, "TN", "FT", "OA", "Schüler/-innen ohne Arbeitsverhältnis - Fahrzeugtechnik", "Schüler ohne Arbeitsverhältnis - Fahrzeugtechnik", "Schülerinnen ohne Arbeitsverhältnis - Fahrzeugtechnik", null, null)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Medizintechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_306_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1826000, 58, "306", "05", false, null, null, "TN", "MD", "OA", "Schüler/-innen ohne Arbeitsverhältnis - Medizintechnik", "Schüler ohne Arbeitsverhältnis - Medizintechnik", "Schülerinnen ohne Arbeitsverhältnis - Medizintechnik", null, null)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Metalltechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_306_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1827000, 58, "306", "06", false, null, null, "TN", "ME", "OA", "Schüler/-innen ohne Arbeitsverhältnis - Metalltechnik", "Schüler ohne Arbeitsverhältnis - Metalltechnik", "Schülerinnen ohne Arbeitsverhältnis - Metalltechnik", null, null)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Physik/Chemie/Biologie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_306_07", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1828000, 58, "306", "07", false, null, null, "TN", "PC", "OA", "Schüler/-innen ohne Arbeitsverhältnis - Physik/Chemie/Biologie", "Schüler ohne Arbeitsverhältnis - Physik/Chemie/Biologie", "Schülerinnen ohne Arbeitsverhältnis - Physik/Chemie/Biologie", null, null)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Textiltechnik und Bekleidung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_306_08", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1829000, 58, "306", "08", false, null, null, "TN", "TB", "OA", "Schüler/-innen ohne Arbeitsverhältnis - Textiltechnik und Bekleidung", "Schüler ohne Arbeitsverhältnis - Textiltechnik und Bekleidung", "Schülerinnen ohne Arbeitsverhältnis - Textiltechnik und Bekleidung", null, null)
		}));

		/** Fachklasse Schüler/-innen ohne Arbeitsverhältnis - Wirtschaft und Verwaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_307_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1830000, 58, "307", "00", false, null, null, "WV", null, "OA", "Schüler/-innen ohne Arbeitsverhältnis - Wirtschaft und Verwaltung", "Schüler ohne Arbeitsverhältnis - Wirtschaft und Verwaltung", "Schülerinnen ohne Arbeitsverhältnis - Wirtschaft und Verwaltung", null, null)
		}));

		/** Fachklasse Werkstattjahr - Agrarwirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_401_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1831000, 58, "401", "00", false, null, null, "AW", null, "WJ", "Werkstattjahr - Agrarwirtschaft", "Werkstattjahr - Agrarwirtschaft", "Werkstattjahr - Agrarwirtschaft", null, null)
		}));

		/** Fachklasse Werkstattjahr - Ernährungs- und Versorgungsmanagement */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_402_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1832000, 58, "402", "00", false, null, null, "EV", null, "WJ", "Werkstattjahr - Ernährungs- und Versorgungsmanagement", "Werkstattjahr - Ernährungs- und Versorgungsmanagement", "Werkstattjahr - Ernährungs- und Versorgungsmanagement", null, null)
		}));

		/** Fachklasse Werkstattjahr - Farbtechnik und Raumgestaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_403_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1833000, 58, "403", "01", false, null, null, "GT", "FR", "WJ", "Werkstattjahr - Farbtechnik und Raumgestaltung", "Werkstattjahr - Farbtechnik und Raumgestaltung", "Werkstattjahr - Farbtechnik und Raumgestaltung", null, null)
		}));

		/** Fachklasse Werkstattjahr - Medien/Medientechnologie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_403_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1834000, 58, "403", "02", false, null, null, "GT", "MM", "WJ", "Werkstattjahr - Medien/Medientechnologie", "Werkstattjahr - Medien/Medientechnologie", "Werkstattjahr - Medien/Medientechnologie", null, null)
		}));

		/** Fachklasse Werkstattjahr - Gesundheitswesen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_404_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1835000, 58, "404", "01", false, null, null, "GE", "GW", "WJ", "Werkstattjahr - Gesundheitswesen", "Werkstattjahr - Gesundheitswesen", "Werkstattjahr - Gesundheitswesen", null, null)
		}));

		/** Fachklasse Werkstattjahr - Sozialwesen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_404_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1836000, 58, "404", "02", false, null, null, "GE", "SW", "WJ", "Werkstattjahr - Sozialwesen", "Werkstattjahr - Sozialwesen", "Werkstattjahr - Sozialwesen", null, null)
		}));

		/** Fachklasse Werkstattjahr - Körperpflege */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_404_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1837000, 58, "404", "03", false, null, null, "GE", "KP", "WJ", "Werkstattjahr - Körperpflege", "Werkstattjahr - Körperpflege", "Werkstattjahr - Körperpflege", null, null)
		}));

		/** Fachklasse Werkstattjahr - Informatik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_405_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1838000, 58, "405", "00", false, null, null, "IF", null, "WJ", "Werkstattjahr - Informatik", "Werkstattjahr - Informatik", "Werkstattjahr - Informatik", null, null)
		}));

		/** Fachklasse Werkstattjahr - Bau und Holztechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_406_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1839000, 58, "406", "01", false, null, null, "TN", "BH", "WJ", "Werkstattjahr - Bau und Holztechnik", "Werkstattjahr - Bau und Holztechnik", "Werkstattjahr - Bau und Holztechnik", null, null)
		}));

		/** Fachklasse Werkstattjahr - Drucktechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_406_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1840000, 58, "406", "02", false, null, null, "TN", "DT", "WJ", "Werkstattjahr - Drucktechnik", "Werkstattjahr - Drucktechnik", "Werkstattjahr - Drucktechnik", null, null)
		}));

		/** Fachklasse Werkstattjahr - Elektrotechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_406_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1841000, 58, "406", "03", false, null, null, "TN", "ET", "WJ", "Werkstattjahr - Elektrotechnik", "Werkstattjahr - Elektrotechnik", "Werkstattjahr - Elektrotechnik", null, null)
		}));

		/** Fachklasse Werkstattjahr - Fahrzeugtechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_406_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1842000, 58, "406", "04", false, null, null, "TN", "FT", "WJ", "Werkstattjahr - Fahrzeugtechnik", "Werkstattjahr - Fahrzeugtechnik", "Werkstattjahr - Fahrzeugtechnik", null, null)
		}));

		/** Fachklasse Werkstattjahr - Medizintechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_406_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1843000, 58, "406", "05", false, null, null, "TN", "MD", "WJ", "Werkstattjahr - Medizintechnik", "Werkstattjahr - Medizintechnik", "Werkstattjahr - Medizintechnik", null, null)
		}));

		/** Fachklasse Werkstattjahr - Metalltechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_406_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1844000, 58, "406", "06", false, null, null, "TN", "ME", "WJ", "Werkstattjahr - Metalltechnik", "Werkstattjahr - Metalltechnik", "Werkstattjahr - Metalltechnik", null, null)
		}));

		/** Fachklasse Werkstattjahr - Physik/Chemie/Biologie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_406_07", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1845000, 58, "406", "07", false, null, null, "TN", "PC", "WJ", "Werkstattjahr - Physik/Chemie/Biologie", "Werkstattjahr - Physik/Chemie/Biologie", "Werkstattjahr - Physik/Chemie/Biologie", null, null)
		}));

		/** Fachklasse Werkstattjahr - Textiltechnik und Bekleidung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_406_08", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1846000, 58, "406", "08", false, null, null, "TN", "TB", "WJ", "Werkstattjahr - Textiltechnik und Bekleidung", "Werkstattjahr - Textiltechnik und Bekleidung", "Werkstattjahr - Textiltechnik und Bekleidung", null, null)
		}));

		/** Fachklasse Werkstattjahr - Wirtschaft und Verwaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_407_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1847000, 58, "407", "00", false, null, null, "WV", null, "WJ", "Werkstattjahr - Wirtschaft und Verwaltung", "Werkstattjahr - Wirtschaft und Verwaltung", "Werkstattjahr - Wirtschaft und Verwaltung", null, null)
		}));

		/** Fachklasse Förderzentrum für Flüchtlinge */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_500_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1848000, 58, "500", "00", false, null, null, "OZ", null, null, "Förderzentrum für Flüchtlinge", "Förderzentrum für Flüchtlinge", "Förderzentrum für Flüchtlinge", null, null)
		}));

		/** Fachklasse Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Agrarwirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_601_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1849000, 58, "601", "00", false, null, null, "AW", null, "AV", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Agrarwirtschaft", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Agrarwirtschaft", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Agrarwirtschaft", null, null)
		}));

		/** Fachklasse Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Ernährungs- und Versorgungsmanagement */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_602_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1850000, 58, "602", "00", false, null, null, "EV", null, "AV", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Ernährungs- und Versorgungsmanagement", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Ernährungs- und Versorgungsmanagement", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Ernährungs- und Versorgungsmanagement", null, null)
		}));

		/** Fachklasse Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Farbtechnik und Raumgestaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_603_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1851000, 58, "603", "01", false, null, null, "GT", "FR", "AV", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Farbtechnik und Raumgestaltung", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Farbtechnik und Raumgestaltung", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Farbtechnik und Raumgestaltung", null, null)
		}));

		/** Fachklasse Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Medien/Medientechnologie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_603_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1852000, 58, "603", "02", false, null, null, "GT", "MM", "AV", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Medien/Medientechnologie", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Medien/Medientechnologie", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Medien/Medientechnologie", null, null)
		}));

		/** Fachklasse Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Gesundheitswesen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_604_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1853000, 58, "604", "01", false, null, null, "GE", "GW", "AV", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Gesundheitswesen", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Gesundheitswesen", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Gesundheitswesen", null, null)
		}));

		/** Fachklasse Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Sozialwesen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_604_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1854000, 58, "604", "02", false, null, null, "GE", "SW", "AV", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Sozialwesen", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Sozialwesen", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Sozialwesen", null, null)
		}));

		/** Fachklasse Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Körperpflege */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_604_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1855000, 58, "604", "03", false, null, null, "GE", "KP", "AV", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Körperpflege", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Körperpflege", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Körperpflege", null, null)
		}));

		/** Fachklasse Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Informatik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_605_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1856000, 58, "605", "00", false, null, null, "IF", null, "AV", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Informatik", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Informatik", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Informatik", null, null)
		}));

		/** Fachklasse Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Bau und Holztechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_606_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1857000, 58, "606", "01", false, null, null, "TN", "BH", "AV", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Bau und Holztechnik", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Bau und Holztechnik", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Bau und Holztechnik", null, null)
		}));

		/** Fachklasse Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Drucktechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_606_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1858000, 58, "606", "02", false, null, null, "TN", "DT", "AV", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Drucktechnik", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Drucktechnik", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Drucktechnik", null, null)
		}));

		/** Fachklasse Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Elektrotechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_606_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1859000, 58, "606", "03", false, null, null, "TN", "ET", "AV", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Elektrotechnik", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Elektrotechnik", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Elektrotechnik", null, null)
		}));

		/** Fachklasse Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Fahrzeugtechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_606_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1860000, 58, "606", "04", false, null, null, "TN", "FT", "AV", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Fahrzeugtechnik", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Fahrzeugtechnik", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Fahrzeugtechnik", null, null)
		}));

		/** Fachklasse Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Medizintechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_606_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1861000, 58, "606", "05", false, null, null, "TN", "MD", "AV", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Medizintechnik", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Medizintechnik", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Medizintechnik", null, null)
		}));

		/** Fachklasse Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Metalltechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_606_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1862000, 58, "606", "06", false, null, null, "TN", "ME", "AV", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Metalltechnik", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Metalltechnik", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Metalltechnik", null, null)
		}));

		/** Fachklasse Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Physik/Chemie/Biologie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_606_07", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1863000, 58, "606", "07", false, null, null, "TN", "PC", "AV", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Physik/Chemie/Biologie", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Physik/Chemie/Biologie", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Physik/Chemie/Biologie", null, null)
		}));

		/** Fachklasse Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Textiltechnik und Bekleidung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_606_08", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1864000, 58, "606", "08", false, null, null, "TN", "TB", "AV", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Textiltechnik und Bekleidung", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Textiltechnik und Bekleidung", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Textiltechnik und Bekleidung", null, null)
		}));

		/** Fachklasse Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Wirtschaft und Verwaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_607_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1865000, 58, "607", "00", false, null, null, "WV", null, "AV", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Wirtschaft und Verwaltung", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Wirtschaft und Verwaltung", "Berufsvorb. Bildungsmaßn. Rehabilitation (BvB Reha) - Wirtschaft und Verwaltung", null, null)
		}));

		/** Fachklasse Sonstiger Bildungsgang */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_58_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1866000, 58, "999", "00", false, null, null, "XX", "XX", "XX", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, null)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 60
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex60(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Bautechnische/-r Assistent/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_60_102_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1867000, 60, "102", "00", true, "T", "BT", null, null, null, "Bautechnische/-r Assistent/-in", "Bautechnischer Assistent", "Bautechnische Assistentin", null, 2019)
		}));

		/** Fachklasse Bekleidungstechnische/-r Assistent/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_60_105_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1868000, 60, "105", "00", true, "T", "TB", null, null, null, "Bekleidungstechnische/-r Assistent/-in", "Bekleidungstechnischer Assistent", "Bekleidungstechnische Assistentin", null, 2019)
		}));

		/** Fachklasse Biologisch-technische/-r Assistent/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_60_107_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1869000, 60, "107", "00", true, "T", "CP", null, null, null, "Biologisch-technische/-r Assistent/-in", "Biologisch-technischer Assistent", "Biologisch-technische Assistentin", null, 2019)
		}));

		/** Fachklasse Chemisch-technische/-r Assistent/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_60_110_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1870000, 60, "110", "00", true, "T", "CP", null, null, null, "Chemisch-technische/-r Assistent/-in", "Chemisch-technischer Assistent", "Chemisch-technische Assistentin", null, 2019)
		}));

		/** Fachklasse Elektrotechnische/-r Assistent/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_60_117_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1871000, 60, "117", "00", true, "T", "ET", null, null, null, "Elektrotechnische/-r Assistent/-in", "Elektrotechnischer Assistent", "Elektrotechnische Assistentin", null, 2019)
		}));

		/** Fachklasse Erzieher/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_60_120_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1872000, 60, "120", "00", true, "S", "SG", null, null, null, "Erzieher/-in", "Erzieher", "Erzieherin", null, 2019)
		}));

		/** Fachklasse Heilerziehungshelfer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_60_121_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1873000, 60, "121", "00", true, "S", "SG", null, null, null, "Heilerziehungshelfer/-in", "Heilerziehungshelfer", "Heilerziehungshelferin", null, 2019)
		}));

		/** Fachklasse Fremdsprachenkorrespondent/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_60_122_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1874000, 60, "122", "00", true, "W", "WV", null, null, null, "Fremdsprachenkorrespondent/-in", "Fremdsprachenkorrespondent", "Fremdsprachenkorrespondentin", null, 2019)
		}));

		/** Fachklasse Gestaltungstechnische/-r Assistent/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_60_125_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1875000, 60, "125", "00", true, "G", "FR", null, null, null, "Gestaltungstechnische/-r Assistent/-in", "Gestaltungstechnischer Assistent", "Gestaltungstechnische Assistentin", null, 2019)
		}));

		/** Fachklasse Informationstechnische/-r Assistent/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_60_133_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1876000, 60, "133", "00", true, "T", "IT", null, null, null, "Informationstechnische/-r Assistent/-in", "Informationstechnischer Assistent", "Informationstechnische Assistentin", null, 2019)
		}));

		/** Fachklasse Kinderpfleger/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_60_134_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1877000, 60, "134", "00", true, "S", "SG", null, null, null, "Kinderpfleger/-in", "Kinderpfleger", "Kinderpflegerin", null, 2019)
		}));

		/** Fachklasse Lebensmitteltechnische/-r Assistent/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_60_135_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1878000, 60, "135", "00", true, "E", "EH", null, null, null, "Lebensmitteltechnische/-r Assistent/-in", "Lebensmitteltechnischer Assistent", "Lebensmitteltechnische Assistentin", null, 2019)
		}));

		/** Fachklasse Servicekraft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_60_146_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1879000, 60, "146", "00", true, "E", "EH", null, null, null, "Servicekraft", "Servicekraft", "Servicekraft", null, 2019)
		}));

		/** Fachklasse Sozialhelfer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_60_147_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1880000, 60, "147", "00", true, "S", "SG", null, null, null, "Sozialhelfer/-in", "Sozialhelfer", "Sozialhelferin", null, 2019)
		}));

		/** Fachklasse Technische/-r Assistent/-in / Kosmetik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_60_148_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1881000, 60, "148", "00", true, "S", "KP", null, null, null, "Technische/-r Assistent/-in / Kosmetik", "Technischer Assistent/Kosmetik", "Technische Assistentin/Kosmetik", null, 2019)
		}));

		/** Fachklasse Änderungsschneider/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_60_149_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1882000, 60, "149", "00", true, "T", "TB", null, null, null, "Änderungsschneider/-in", "Änderungsschneider", "Änderungsschneiderin", null, 2019)
		}));

		/** Fachklasse Assistent/-in für Ernährung und Versorgung - Service (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_60_150_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1883000, 60, "150", "01", true, "E", "EH", null, null, null, "Assistent/-in für Ernährung und Versorgung - Service", "Assistent für Ernährung und Versorgung - Service", "Assistentin für Ernährung und Versorgung - Service", null, 2019)
		}));

		/** Fachklasse Sozialassistent/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_60_151_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1884000, 60, "151", "00", true, "S", "SG", null, null, null, "Sozialassistent/-in", "Sozialassistent", "Sozialassistentin", null, 2019)
		}));

		/** Fachklasse Sozialassistent/-in - Heilerziehung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_60_151_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1885000, 60, "151", "01", true, "S", "SG", null, null, null, "Sozialassistent/-in - Heilerziehung", "Sozialassistent - Heilerziehung", "Sozialassistentin - Heilerziehung", null, 2019)
		}));

		/** Fachklasse Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_60_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1886000, 60, "999", "00", true, "X", "XX", null, null, null, "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, 2019)
		}));

		/** Fachklasse Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_60_999_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1887000, 60, "999", "01", true, "X", "XX", null, null, null, "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, 2019)
		}));

		/** Fachklasse Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_60_999_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1888000, 60, "999", "02", true, "X", "XX", null, null, null, "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, 2019)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 61
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex61(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Glaser/-in - Verglasung und Glasbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_61_242_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1889000, 61, "242", "02", false, null, null, "TN", null, null, "Glaser/-in - Verglasung und Glasbau", "Glaser - Verglasung und Glasbau", "Glaserin - Verglasung und Glasbau", null, null)
		}));

		/** Fachklasse Glasveredler/-in - Schliff und Gravur */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_61_248_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1890000, 61, "248", "01", false, null, null, "TN", null, null, "Glasveredler/-in - Schliff und Gravur", "Glasveredler - Schliff und Gravur", "Glasveredlerin - Schliff und Gravur", null, null)
		}));

		/** Fachklasse Glasveredler/-in - Glasmalerei und Kunstverglasung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_61_248_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1891000, 61, "248", "02", false, null, null, "TN", null, null, "Glasveredler/-in - Glasmalerei und Kunstverglasung", "Glasveredler - Glasmalerei und Kunstverglasung", "Glasveredlerin - Glasmalerei und Kunstverglasung", null, null)
		}));

		/** Fachklasse Glasveredler/-in - Kanten- und Flächenveredlung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_61_248_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1892000, 61, "248", "03", false, null, null, "TN", null, null, "Glasveredler/-in - Kanten- und Flächenveredlung", "Glasveredler - Kanten- und Flächenveredlung", "Glasveredlerin - Kanten- und Flächenveredlung", null, null)
		}));

		/** Fachklasse Industriemechaniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_61_276_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1893000, 61, "276", "00", false, null, null, "TN", null, null, "Industriemechaniker/-in", "Industriemechaniker", "Industriemechanikerin", null, null)
		}));

		/** Fachklasse Uhrmacher/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_61_456_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1894000, 61, "456", "00", true, null, null, "TN", null, null, "Uhrmacher/-in", "Uhrmacher", "Uhrmacherin", null, 2019)
		}));

		/** Fachklasse Fachkraft für Lagerlogistik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_61_527_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1895000, 61, "527", "00", false, null, null, "WV", null, null, "Fachkraft für Lagerlogistik", "Fachkraft für Lagerlogistik", "Fachkraft für Lagerlogistik", null, null)
		}));

		/** Fachklasse Maschinen- und Anlagenführer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_61_531_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1896000, 61, "531", "00", false, null, null, "TN", null, null, "Maschinen- und Anlagenführer/-in", "Maschinen- und Anlagenführer", "Maschinen- und Anlagenführerin", null, null)
		}));

		/** Fachklasse Änderungsschneider/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_61_538_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1897000, 61, "538", "00", true, null, null, "TN", null, null, "Änderungsschneider/-in", "Änderungsschneider", "Änderungsschneiderin", null, 2019)
		}));

		/** Fachklasse Kaufmann/-frau für Büromanagement */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_61_607_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1898000, 61, "607", "00", false, null, null, "WV", null, null, "Kaufmann/-frau für Büromanagement", "Kaufmann für Büromanagement", "Kauffrau für Büromanagement", null, null)
		}));

		/** Fachklasse Sonstiger Bildungsgang */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_61_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1899000, 61, "999", "00", false, null, null, "XX", "XX", "XX", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, null)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 62
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex62(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Elektroniker/-in für Betriebstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_62_177_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1900000, 62, "177", "02", true, null, null, "TN", null, null, "Elektroniker/-in für Betriebstechnik", "Elektroniker für Betriebstechnik", "Elektronikerin für Betriebstechnik", null, 2019)
		}));

		/** Fachklasse Glaser/-in - Verglasung und Glasbau */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_62_242_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1901000, 62, "242", "02", false, null, null, "TN", null, null, "Glaser/-in - Verglasung und Glasbau", "Glaser - Verglasung und Glasbau", "Glaserin - Verglasung und Glasbau", null, null)
		}));

		/** Fachklasse Glasveredler/-in - Schliff und Gravur */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_62_248_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1902000, 62, "248", "01", false, null, null, "TN", null, null, "Glasveredler/-in - Schliff und Gravur", "Glasveredler - Schliff und Gravur", "Glasveredlerin - Schliff und Gravur", null, null)
		}));

		/** Fachklasse Glasveredler/-in - Glasmalerei und Kunstverglasung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_62_248_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1903000, 62, "248", "02", false, null, null, "TN", null, null, "Glasveredler/-in - Glasmalerei und Kunstverglasung", "Glasveredler - Glasmalerei und Kunstverglasung", "Glasveredlerin - Glasmalerei und Kunstverglasung", null, null)
		}));

		/** Fachklasse Glasveredler/-in - Kanten- und Flächenveredlung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_62_248_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1904000, 62, "248", "03", false, null, null, "TN", null, null, "Glasveredler/-in - Kanten- und Flächenveredlung", "Glasveredler - Kanten- und Flächenveredlung", "Glasveredlerin - Kanten- und Flächenveredlung", null, null)
		}));

		/** Fachklasse Industriemechaniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_62_276_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1905000, 62, "276", "00", false, null, null, "TN", null, null, "Industriemechaniker/-in", "Industriemechaniker", "Industriemechanikerin", null, null)
		}));

		/** Fachklasse Informations- und Telekommunikationssystemelektroniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_62_279_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1906000, 62, "279", "00", true, null, null, "TN", null, null, "Informations- und Telekommunikationssystemelektroniker/-in", "Informations- und Telekommunikationssystemelektroniker", "Informations- und Telekommunikationssystemelektronikerin", null, 2019)
		}));

		/** Fachklasse Mechatroniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_62_335_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1907000, 62, "335", "00", true, null, null, "TN", null, null, "Mechatroniker/-in", "Mechatroniker", "Mechatronikerin", null, 2019)
		}));

		/** Fachklasse Maschinen- und Anlagenführer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_62_531_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1908000, 62, "531", "00", true, null, null, "TN", null, null, "Maschinen- und Anlagenführer/-in", "Maschinen- und Anlagenführer", "Maschinen- und Anlagenführerin", null, 2019)
		}));

		/** Fachklasse Sonstiger Bildungsgang */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_62_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1909000, 62, "999", "00", false, null, null, "XX", "XX", "XX", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, null)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 63
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex63(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Elektroniker/-in für Automatisierungstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_63_177_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1910000, 63, "177", "01", false, null, null, "TN", null, null, "Elektroniker/-in für Automatisierungstechnik", "Elektroniker für Automatisierungstechnik", "Elektronikerin für Automatisierungstechnik", null, null)
		}));

		/** Fachklasse Elektroniker/-in für Betriebstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_63_177_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1911000, 63, "177", "02", false, null, null, "TN", null, null, "Elektroniker/-in für Betriebstechnik", "Elektroniker für Betriebstechnik", "Elektronikerin für Betriebstechnik", null, null)
		}));

		/** Fachklasse Elektroniker/-in für Geräte und Systeme */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_63_177_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1912000, 63, "177", "05", false, null, null, "TN", null, null, "Elektroniker/-in für Geräte und Systeme", "Elektroniker für Geräte und Systeme", "Elektronikerin für Geräte und Systeme", null, null)
		}));

		/** Fachklasse Fachinformatiker/-in für Systemintegration (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_63_180_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1913000, 63, "180", "00", true, null, null, "TN", null, null, "Fachinformatiker/-in für Systemintegration", "Fachinformatiker für Systemintegration", "Fachinformatikerin für Systemintegration", null, 2019)
		}));

		/** Fachklasse Fachinformatiker/-in - Systemintegration */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_63_187_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1914000, 63, "187", "02", false, null, null, "IF", null, "A3", "Fachinformatiker/-in - Systemintegration", "Fachinformatiker - Systemintegration", "Fachinformatikerin - Systemintegration", null, null)
		}));

		/** Fachklasse Florist/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_63_217_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1915000, 63, "217", "00", false, null, null, "GT", null, null, "Florist/-in", "Florist", "Floristin", null, null)
		}));

		/** Fachklasse Friseur/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_63_228_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1916000, 63, "228", "00", false, null, null, "GE", null, null, "Friseur/-in", "Friseur", "Friseurin", null, null)
		}));

		/** Fachklasse Hauswirtschafter/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_63_257_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1917000, 63, "257", "00", false, null, null, "EV", null, null, "Hauswirtschafter/-in", "Hauswirtschafter", "Hauswirtschafterin", null, null)
		}));

		/** Fachklasse Industriemechaniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_63_276_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1918000, 63, "276", "00", false, null, null, "TN", null, null, "Industriemechaniker/-in", "Industriemechaniker", "Industriemechanikerin", null, null)
		}));

		/** Fachklasse Informations- und Telekommunikationssystemelektroniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_63_279_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1919000, 63, "279", "00", false, null, null, "TN", null, null, "Informations- und Telekommunikationssystemelektroniker/-in", "Informations- und Telekommunikationssystemelektroniker", "Informations- und Telekommunikationssystemelektronikerin", null, null)
		}));

		/** Fachklasse Koch/Köchin */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_63_308_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1920000, 63, "308", "00", false, null, null, "EH", null, null, "Koch/Köchin", "Koch", "Köchin", null, null)
		}));

		/** Fachklasse Mechatroniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_63_335_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1921000, 63, "335", "00", false, null, null, "TN", null, null, "Mechatroniker/-in", "Mechatroniker", "Mechatronikerin", null, null)
		}));

		/** Fachklasse Metallbauer/-in - Konstruktionstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_63_339_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1922000, 63, "339", "03", false, null, null, "TN", null, null, "Metallbauer/-in - Konstruktionstechnik", "Metallbauer - Konstruktionstechnik", "Metallbauerin - Konstruktionstechnik", null, null)
		}));

		/** Fachklasse Uhrmacher/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_63_456_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1923000, 63, "456", "00", false, null, null, "TN", null, null, "Uhrmacher/-in", "Uhrmacher", "Uhrmacherin", null, null)
		}));

		/** Fachklasse Verkäufer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_63_463_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1924000, 63, "463", "00", false, null, null, "WV", null, null, "Verkäufer/-in", "Verkäufer", "Verkäuferin", null, null)
		}));

		/** Fachklasse Werkzeugmechaniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_63_482_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1925000, 63, "482", "00", false, null, null, "TN", null, null, "Werkzeugmechaniker/-in", "Werkzeugmechaniker", "Werkzeugmechanikerin", null, null)
		}));

		/** Fachklasse Zerspanungsmechaniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_63_487_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1926000, 63, "487", "00", false, null, null, "TN", null, null, "Zerspanungsmechaniker/-in", "Zerspanungsmechaniker", "Zerspanungsmechanikerin", null, null)
		}));

		/** Fachklasse Kosmetiker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_63_515_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1927000, 63, "515", "00", false, null, null, "GE", null, null, "Kosmetiker/-in", "Kosmetiker", "Kosmetikerin", null, null)
		}));

		/** Fachklasse Fachkraft für Lagerlogistik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_63_527_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1928000, 63, "527", "00", false, null, null, "WV", null, null, "Fachkraft für Lagerlogistik", "Fachkraft für Lagerlogistik", "Fachkraft für Lagerlogistik", null, null)
		}));

		/** Fachklasse Fachlagerist/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_63_528_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1929000, 63, "528", "00", false, null, null, "WV", null, null, "Fachlagerist/-in", "Fachlagerist", "Fachlageristin", null, null)
		}));

		/** Fachklasse Maschinen- und Anlagenführer/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_63_531_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1930000, 63, "531", "00", false, null, null, "TN", null, null, "Maschinen- und Anlagenführer/-in", "Maschinen- und Anlagenführer", "Maschinen- und Anlagenführerin", null, null)
		}));

		/** Fachklasse Maßschneider/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_63_532_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1931000, 63, "532", "00", false, null, null, "TN", null, null, "Maßschneider/-in", "Maßschneider", "Maßschneiderin", null, null)
		}));

		/** Fachklasse Änderungsschneider/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_63_538_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1932000, 63, "538", "00", false, null, null, "TN", null, null, "Änderungsschneider/-in", "Änderungsschneider", "Änderungsschneiderin", null, null)
		}));

		/** Fachklasse Oberflächenbeschichter/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_63_541_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1933000, 63, "541", "00", false, null, null, "TN", null, null, "Oberflächenbeschichter/-in", "Oberflächenbeschichter", "Oberflächenbeschichterin", null, null)
		}));

		/** Fachklasse Medizinische/-r Fachangestellte/-r */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_63_544_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1934000, 63, "544", "00", false, null, null, "WV", null, "A3", "Medizinische/-r Fachangestellte/-r", "Medizinischer Fachangestellter", "Medizinische Fachangestellte", null, null)
		}));

		/** Fachklasse Kaufmann/-frau für Büromanagement */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_63_607_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1935000, 63, "607", "00", false, null, null, "WV", null, null, "Kaufmann/-frau für Büromanagement", "Kaufmann für Büromanagement", "Kauffrau für Büromanagement", null, null)
		}));

		/** Fachklasse Sonstiger Bildungsgang */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_63_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1936000, 63, "999", "00", false, null, null, "XX", "XX", "XX", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, null)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 70
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex70(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Bautechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_70_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1937000, 70, "101", "00", true, "T", "BT", null, null, null, "Bautechnik", "Bautechnik", "Bautechnik", null, 2016)
		}));

		/** Fachklasse Agrarwirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_70_102_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1938000, 70, "102", "00", true, "A", "AW", null, null, null, "Agrarwirtschaft", "Agrarwirtschaft", "Agrarwirtschaft", null, 2017)
		}));

		/** Fachklasse Drucktechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_70_103_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1939000, 70, "103", "00", true, "T", "DT", null, null, null, "Drucktechnik", "Drucktechnik", "Drucktechnik", null, 2016)
		}));

		/** Fachklasse Elektrotechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_70_104_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1940000, 70, "104", "00", true, "T", "ET", null, null, null, "Elektrotechnik", "Elektrotechnik", "Elektrotechnik", null, 2017)
		}));

		/** Fachklasse Ernährung und Hauswirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_70_105_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1941000, 70, "105", "00", true, "E", "EH", null, null, null, "Ernährung und Hauswirtschaft", "Ernährung und Hauswirtschaft", "Ernährung und Hauswirtschaft", null, 2017)
		}));

		/** Fachklasse Farbtechnik und Raumgestaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_70_107_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1942000, 70, "107", "00", true, "G", "FR", null, null, null, "Farbtechnik und Raumgestaltung", "Farbtechnik und Raumgestaltung", "Farbtechnik und Raumgestaltung", null, 2017)
		}));

		/** Fachklasse Holztechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_70_108_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1943000, 70, "108", "00", true, "T", "HT", null, null, null, "Holztechnik", "Holztechnik", "Holztechnik", null, 2017)
		}));

		/** Fachklasse Körperpflege (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_70_109_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1944000, 70, "109", "00", true, "S", "KP", null, null, null, "Körperpflege", "Körperpflege", "Körperpflege", null, 2017)
		}));

		/** Fachklasse Land- u. Hauswirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_70_110_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1945000, 70, "110", "00", true, "A", "AW", null, null, null, "Land- u. Hauswirtschaft", "Land- u. Hauswirtschaft", "Land- u. Hauswirtschaft", null, 2014)
		}));

		/** Fachklasse Lebensmitteltechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_70_111_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1946000, 70, "111", "00", true, "E", "EH", null, null, null, "Lebensmitteltechnik", "Lebensmitteltechnik", "Lebensmitteltechnik", null, 2014)
		}));

		/** Fachklasse Medizintechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_70_112_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1947000, 70, "112", "00", true, "T", "MZ", null, null, null, "Medizintechnik", "Medizintechnik", "Medizintechnik", null, 2016)
		}));

		/** Fachklasse Metalltechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_70_113_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1948000, 70, "113", "00", true, "T", "MT", null, null, null, "Metalltechnik", "Metalltechnik", "Metalltechnik", null, 2017)
		}));

		/** Fachklasse Metalltechnik - Allgemeine Maschinentechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_70_113_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1949000, 70, "113", "01", true, "T", "MT", null, null, null, "Metalltechnik - Allgemeine Maschinentechnik", "Metalltechnik - Allgemeine Maschinentechnik", "Metalltechnik - Allgemeine Maschinentechnik", null, 2015)
		}));

		/** Fachklasse Metalltechnik - Heizung, Lüftung, Sanitär (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_70_113_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1950000, 70, "113", "02", true, "T", "MT", null, null, null, "Metalltechnik - Heizung, Lüftung, Sanitär", "Metalltechnik - Heizung, Lüftung, Sanitär", "Metalltechnik - Heizung, Lüftung, Sanitär", null, 2014)
		}));

		/** Fachklasse Metalltechnik - KFZ-Technik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_70_113_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1951000, 70, "113", "03", true, "T", "MT", null, null, null, "Metalltechnik - KFZ-Technik", "Metalltechnik - KFZ-Technik", "Metalltechnik - KFZ-Technik", null, 2015)
		}));

		/** Fachklasse Physik, Chemie, Biologie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_70_114_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1952000, 70, "114", "00", true, "T", "CP", null, null, null, "Physik, Chemie, Biologie", "Physik, Chemie, Biologie", "Physik, Chemie, Biologie", null, 2016)
		}));

		/** Fachklasse Sozial- u. Gesundheitswesen (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_70_115_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1953000, 70, "115", "00", true, "S", "SG", null, null, null, "Sozial- u. Gesundheitswesen", "Sozial- u. Gesundheitswesen", "Sozial- u. Gesundheitswesen", null, 2014)
		}));

		/** Fachklasse Sozialwesen (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_70_115_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1954000, 70, "115", "01", true, "S", "SG", null, null, null, "Sozialwesen", "Sozialwesen", "Sozialwesen", null, 2017)
		}));

		/** Fachklasse Gesundheitswesen (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_70_115_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1955000, 70, "115", "02", true, "S", "SG", null, null, null, "Gesundheitswesen", "Gesundheitswesen", "Gesundheitswesen", null, 2017)
		}));

		/** Fachklasse Textiltechnik und Bekleidung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_70_116_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1956000, 70, "116", "00", true, "T", "TB", null, null, null, "Textiltechnik und Bekleidung", "Textiltechnik und Bekleidung", "Textiltechnik und Bekleidung", null, 2016)
		}));

		/** Fachklasse Vermessungstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_70_117_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1957000, 70, "117", "00", true, "T", "VT", null, null, null, "Vermessungstechnik", "Vermessungstechnik", "Vermessungstechnik", null, 2016)
		}));

		/** Fachklasse Wirtschaft und Verwaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_70_118_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1958000, 70, "118", "00", true, "W", "WV", null, null, null, "Wirtschaft und Verwaltung", "Wirtschaft und Verwaltung", "Wirtschaft und Verwaltung", null, 2017)
		}));

		/** Fachklasse Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_70_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1959000, 70, "999", "00", true, "X", "XX", null, null, null, "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, 2014)
		}));

		/** Fachklasse Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_70_999_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1960000, 70, "999", "01", true, "X", "XX", null, null, null, "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, 2014)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 80
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex80(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Agrarwirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_80_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1961000, 80, "101", "00", true, "A", "AW", null, null, null, "Agrarwirtschaft", "Agrarwirtschaft", "Agrarwirtschaft", null, 2018)
		}));

		/** Fachklasse Bautechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_80_102_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1962000, 80, "102", "00", true, "T", "BT", null, null, null, "Bautechnik", "Bautechnik", "Bautechnik", null, 2015)
		}));

		/** Fachklasse Drucktechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_80_103_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1963000, 80, "103", "00", true, "T", "DT", null, null, null, "Drucktechnik", "Drucktechnik", "Drucktechnik", null, 2015)
		}));

		/** Fachklasse Elektrotechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_80_104_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1964000, 80, "104", "00", true, "T", "ET", null, null, null, "Elektrotechnik", "Elektrotechnik", "Elektrotechnik", null, 2015)
		}));

		/** Fachklasse Ernährung und Hauswirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_80_105_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1965000, 80, "105", "00", true, "E", "EH", null, null, null, "Ernährung und Hauswirtschaft", "Ernährung und Hauswirtschaft", "Ernährung und Hauswirtschaft", null, 2015)
		}));

		/** Fachklasse Farbtechnik und Raumgestaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_80_106_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1966000, 80, "106", "00", true, "G", "FR", null, null, null, "Farbtechnik und Raumgestaltung", "Farbtechnik und Raumgestaltung", "Farbtechnik und Raumgestaltung", null, 2015)
		}));

		/** Fachklasse Holztechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_80_107_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1967000, 80, "107", "00", true, "T", "HT", null, null, null, "Holztechnik", "Holztechnik", "Holztechnik", null, 2015)
		}));

		/** Fachklasse Informations- und Telekommunikationstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_80_108_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1968000, 80, "108", "00", true, "T", "IT", null, null, null, "Informations- und Telekommunikationstechnik", "Informations- und Telekommunikationstechnik", "Informations- und Telekommunikationstechnik", null, 2015)
		}));

		/** Fachklasse Körperpflege (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_80_109_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1969000, 80, "109", "00", true, "S", "KP", null, null, null, "Körperpflege", "Körperpflege", "Körperpflege", null, 2015)
		}));

		/** Fachklasse Land- u. Hauswirtschaft (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_80_110_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1970000, 80, "110", "00", true, "A", "AW", null, null, null, "Land- u. Hauswirtschaft", "Land- u. Hauswirtschaft", "Land- u. Hauswirtschaft", null, 2014)
		}));

		/** Fachklasse Medien/Medientechnologie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_80_111_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1971000, 80, "111", "00", true, "T", "MM", null, null, null, "Medien/Medientechnologie", "Medien/Medientechnologie", "Medien/Medientechnologie", null, 2015)
		}));

		/** Fachklasse Medizintechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_80_112_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1972000, 80, "112", "00", true, "T", "MZ", null, null, null, "Medizintechnik", "Medizintechnik", "Medizintechnik", null, 2015)
		}));

		/** Fachklasse Metalltechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_80_113_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1973000, 80, "113", "00", true, "T", "MT", null, null, null, "Metalltechnik", "Metalltechnik", "Metalltechnik", null, 2015)
		}));

		/** Fachklasse Metalltechnik - Allgemeine Maschinentechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_80_113_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1974000, 80, "113", "01", true, "T", "MT", null, null, null, "Metalltechnik - Allgemeine Maschinentechnik", "Metalltechnik - Allgemeine Maschinentechnik", "Metalltechnik - Allgemeine Maschinentechnik", null, 2014)
		}));

		/** Fachklasse Metalltechnik - Heizung, Lüftung, Sanitär (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_80_113_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1975000, 80, "113", "02", true, "T", "MT", null, null, null, "Metalltechnik - Heizung, Lüftung, Sanitär", "Metalltechnik - Heizung, Lüftung, Sanitär", "Metalltechnik - Heizung, Lüftung, Sanitär", null, 2014)
		}));

		/** Fachklasse Metalltechnik - KFZ-Technik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_80_113_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1976000, 80, "113", "03", true, "T", "MT", null, null, null, "Metalltechnik - KFZ-Technik", "Metalltechnik - KFZ-Technik", "Metalltechnik - KFZ-Technik", null, 2014)
		}));

		/** Fachklasse Physik, Chemie, Biologie (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_80_114_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1977000, 80, "114", "00", true, "T", "CP", null, null, null, "Physik, Chemie, Biologie", "Physik, Chemie, Biologie", "Physik, Chemie, Biologie", null, 2015)
		}));

		/** Fachklasse Sozial- u. Gesundheitswesen (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_80_115_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1978000, 80, "115", "00", true, "S", "SG", null, null, null, "Sozial- u. Gesundheitswesen", "Sozial- u. Gesundheitswesen", "Sozial- u. Gesundheitswesen", null, 2014)
		}));

		/** Fachklasse Sozialwesen (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_80_115_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1979000, 80, "115", "01", true, "S", "SG", null, null, null, "Sozialwesen", "Sozialwesen", "Sozialwesen", null, 2015)
		}));

		/** Fachklasse Gesundheitswesen (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_80_115_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1980000, 80, "115", "02", true, "S", "SG", null, null, null, "Gesundheitswesen", "Gesundheitswesen", "Gesundheitswesen", null, 2015)
		}));

		/** Fachklasse Sozialpflege/Pflegevorschule (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_80_116_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1981000, 80, "116", "00", true, "S", "SG", null, null, null, "Sozialpflege/Pflegevorschule", "Sozialpflege/Pflegevorschule", "Sozialpflege/Pflegevorschule", null, 2014)
		}));

		/** Fachklasse Textiltechnik und Bekleidung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_80_117_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1982000, 80, "117", "00", true, "T", "TB", null, null, null, "Textiltechnik und Bekleidung", "Textiltechnik und Bekleidung", "Textiltechnik und Bekleidung", null, 2015)
		}));

		/** Fachklasse Vermessungstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_80_118_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1983000, 80, "118", "00", true, "T", "VT", null, null, null, "Vermessungstechnik", "Vermessungstechnik", "Vermessungstechnik", null, 2015)
		}));

		/** Fachklasse Wirtschaft und Verwaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_80_119_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1984000, 80, "119", "00", true, "W", "WV", null, null, null, "Wirtschaft und Verwaltung", "Wirtschaft und Verwaltung", "Wirtschaft und Verwaltung", null, 2015)
		}));

		/** Fachklasse Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_80_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1985000, 80, "999", "00", true, "X", "XX", null, null, null, "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, 2014)
		}));

		/** Fachklasse Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_80_999_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1986000, 80, "999", "01", true, "X", "XX", null, null, null, "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, 2014)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 90
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex90(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Energiegeräteelektroniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1987000, 90, "101", "00", true, "T", "IT", null, null, null, "Energiegeräteelektroniker/-in", "Energiegeräteelektroniker", "Energiegeräteelektronikerin", null, 2014)
		}));

		/** Fachklasse Funkelektroniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_102_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1988000, 90, "102", "00", true, "T", "IT", null, null, null, "Funkelektroniker/-in", "Funkelektroniker", "Funkelektronikerin", null, 2014)
		}));

		/** Fachklasse Galvaniseur/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_103_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1989000, 90, "103", "00", true, "T", "CP", null, null, null, "Galvaniseur/-in", "Galvaniseur", "Galvaniseurin", null, 2014)
		}));

		/** Fachklasse Glas- und Porzellanmaler/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_104_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1990000, 90, "104", "00", true, "G", "FR", null, null, null, "Glas- und Porzellanmaler/-in", "Glas- und Porzellanmaler", "Glas- und Porzellanmalerin", null, 2014)
		}));

		/** Fachklasse Glaser/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_105_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1991000, 90, "105", "00", true, "T", "BT", null, null, null, "Glaser/-in", "Glaser", "Glaserin", null, 2014)
		}));

		/** Fachklasse Glaser/-in - Fenster und Glasfassadenbau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_105_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1992000, 90, "105", "01", true, "T", "BT", null, null, null, "Glaser/-in - Fenster und Glasfassadenbau", "Glaser - Fenster und Glasfassadenbau", "Glaserin - Fenster und Glasfassadenbau", null, 2014)
		}));

		/** Fachklasse Glaser/-in - Verglasung und Glasbau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_105_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1993000, 90, "105", "02", true, "T", "BT", null, null, null, "Glaser/-in - Verglasung und Glasbau", "Glaser - Verglasung und Glasbau", "Glaserin - Verglasung und Glasbau", null, 2019)
		}));

		/** Fachklasse Glasveredler/-in - Flächenveredler (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_106_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1994000, 90, "106", "01", true, "G", "FR", null, null, null, "Glasveredler/-in - Flächenveredler", "Glasveredler - Flächenveredler", "Glasveredlerin - Flächenveredler", null, 2014)
		}));

		/** Fachklasse Glasveredler/-in - Glasgraveur (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_106_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1995000, 90, "106", "02", true, "G", "FR", null, null, null, "Glasveredler/-in - Glasgraveur", "Glasveredler - Glasgraveur", "Glasveredlerin- Glasgraveur", null, 2014)
		}));

		/** Fachklasse Glasveredler/-in - Glasschleifer (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_106_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1996000, 90, "106", "03", true, "G", "FR", null, null, null, "Glasveredler/-in - Glasschleifer", "Glasveredler - Glasschleifer", "Glasveredlerin - Glasschleifer", null, 2014)
		}));

		/** Fachklasse Glasveredler/-in - Glasmalerei und Kunstverglasung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_106_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1997000, 90, "106", "04", true, "G", "FR", null, null, null, "Glasveredler/-in - Glasmalerei und Kunstverglasung", "Glasveredler - Glasmalerei und Kunstverglasung", "Glasveredlerin - Glasmalerei und Kunstverglasung", null, 2019)
		}));

		/** Fachklasse Glasveredler/-in - Kanten- und Flächenveredlung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_106_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1998000, 90, "106", "05", true, "G", "FR", null, null, null, "Glasveredler/-in - Kanten- und Flächenveredlung", "Glasveredler - Kanten- und Flächenveredlung", "Glasveredlerin - Kanten- und Flächenveredlung", null, 2019)
		}));

		/** Fachklasse Glasveredler/-in - Schliff und Gravur (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_106_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(1999000, 90, "106", "06", true, "G", "FR", null, null, null, "Glasveredler/-in - Schliff und Gravur", "Glasveredler - Schliff und Gravur", "Glasveredlerin - Schliff und Gravur", null, 2019)
		}));

		/** Fachklasse Informationselektroniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_107_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2000000, 90, "107", "00", true, "T", "ET", null, null, null, "Informationselektroniker/-in", "Informationselektroniker", "Informationselektronikerin", null, 2014)
		}));

		/** Fachklasse Maschinenschlosser/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_108_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2001000, 90, "108", "00", true, "T", "MT", null, null, null, "Maschinenschlosser/-in", "Maschinenschlosser", "Maschinenschlosserin", null, 2014)
		}));

		/** Fachklasse Weber/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_109_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2002000, 90, "109", "00", true, "T", "TB", null, null, null, "Weber/-in", "Weber", "Weberin", null, 2014)
		}));

		/** Fachklasse Werkzeugmacher/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_110_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2003000, 90, "110", "00", true, "T", "MT", null, null, null, "Werkzeugmacher/-in", "Werkzeugmacher", "Werkzeugmacherin", null, 2014)
		}));

		/** Fachklasse Damenschneider/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_111_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2004000, 90, "111", "00", true, "T", "TB", null, null, null, "Damenschneider/-in", "Damenschneider", "Damenschneiderin", null, 2014)
		}));

		/** Fachklasse Elektroniker/-in für Automatisierungstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_112_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2005000, 90, "112", "01", true, "T", "ET", null, null, null, "Elektroniker/-in für Automatisierungstechnik", "Elektroniker für Automatisierungstechnik", "Elektronikerin für Automatisierungstechnik", null, 2014)
		}));

		/** Fachklasse Elektroniker/-in für Betriebstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_112_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2006000, 90, "112", "02", true, "T", "ET", null, null, null, "Elektroniker/-in für Betriebstechnik", "Elektroniker für Betriebstechnik", "Elektronikerin für Betriebstechnik", null, 2019)
		}));

		/** Fachklasse Elektroniker/-in für Energie- und Gebäudetechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_112_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2007000, 90, "112", "03", true, "T", "ET", null, null, null, "Elektroniker/-in für Energie- und Gebäudetechnik", "Elektroniker für Energie- und Gebäudetechnik", "Elektronikerin für Energie- und Gebäudetechnik", null, 2014)
		}));

		/** Fachklasse Elektroniker/-in für Gebäude- und Infrastruktursysteme (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_112_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2008000, 90, "112", "04", true, "T", "ET", null, null, null, "Elektroniker/-in für Gebäude- und Infrastruktursysteme", "Elektroniker für Gebäude- und Infrastruktursysteme", "Elektronikerin für Gebäude- und Infrastruktursysteme", null, 2014)
		}));

		/** Fachklasse Elektroniker/-in für Geräte und Systeme (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_112_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2009000, 90, "112", "05", true, "T", "ET", null, null, null, "Elektroniker/-in für Geräte und Systeme", "Elektroniker für Geräte und Systeme", "Elektronikerin für Geräte und Systeme", null, 2019)
		}));

		/** Fachklasse Elektroniker/-in für Informations- und Telekommunikationstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_112_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2010000, 90, "112", "06", true, "T", "ET", null, null, null, "Elektroniker/-in für Informations- und Telekommunikationstechnik", "Elektroniker für Informations- und Telekommunikationstechnik", "Elektronikerin für Informations- und Telekommunikationstechnik", null, 2014)
		}));

		/** Fachklasse Elektroniker/-in für Maschinen und Antriebstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_112_07", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2011000, 90, "112", "07", true, "T", "ET", null, null, null, "Elektroniker/-in für Maschinen und Antriebstechnik", "Elektroniker für Maschinen und Antriebstechnik", "Elektronikerin für Maschinen und Antriebstechnik", null, 2014)
		}));

		/** Fachklasse Industriemechaniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_113_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2012000, 90, "113", "00", true, "T", "MT", null, null, null, "Industriemechaniker/-in", "Industriemechaniker", "Industriemechanikerin", null, 2019)
		}));

		/** Fachklasse Industriemechaniker/-in - Betriebstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_113_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2013000, 90, "113", "01", true, "T", "MT", null, null, null, "Industriemechaniker/-in - Betriebstechnik", "Industriemechaniker - Betriebstechnik", "Industriemechanikerin - Betriebstechnik", null, 2014)
		}));

		/** Fachklasse Industriemechaniker/-in - Geräte- und Feinwerktechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_113_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2014000, 90, "113", "02", true, "T", "MT", null, null, null, "Industriemechaniker/-in - Geräte- und Feinwerktechnik", "Industriemechaniker - Geräte- und Feinwerktechnik", "Industriemechanikerin - Geräte- und Feinwerktechnik", null, 2014)
		}));

		/** Fachklasse Industriemechaniker/-in - Maschinen- und Systemtechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_113_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2015000, 90, "113", "03", true, "T", "MT", null, null, null, "Industriemechaniker/-in - Maschinen- und Systemtechnik", "Industriemechaniker - Maschinen- und Systemtechnik", "Industriemechanikerin - Maschinen- und Systemtechnik", null, 2014)
		}));

		/** Fachklasse Industriemechaniker/-in - Produktionstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_113_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2016000, 90, "113", "04", true, "T", "MT", null, null, null, "Industriemechaniker/-in - Produktionstechnik", "Industriemechaniker - Produktionstechnik", "Industriemechanikerin - Produktionstechnik", null, 2014)
		}));

		/** Fachklasse Werkzeugmechaniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_114_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2017000, 90, "114", "00", true, "T", "MT", null, null, null, "Werkzeugmechaniker/-in", "Werkzeugmechaniker", "Werkzeugmechanikerin", null, 2019)
		}));

		/** Fachklasse Werkzeugmechaniker/-in - Formentechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_114_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2018000, 90, "114", "01", true, "T", "MT", null, null, null, "Werkzeugmechaniker/-in - Formentechnik", "Werkzeugmechaniker - Formentechnik", "Werkzeugmechanikerin - Formentechnik", null, 2014)
		}));

		/** Fachklasse Werkzeugmechaniker/-in - Instrumententechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_114_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2019000, 90, "114", "02", true, "T", "MT", null, null, null, "Werkzeugmechaniker/-in - Instrumententechnik", "Werkzeugmechaniker - Instrumententechnik", "Werkzeugmechanikerin - Instrumententechnik", null, 2014)
		}));

		/** Fachklasse Werkzeugmechaniker/-in - Stanz- und Umformtechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_114_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2020000, 90, "114", "03", true, "T", "MT", null, null, null, "Werkzeugmechaniker/-in - Stanz- und Umformtechnik", "Werkzeugmechaniker - Stanz- und Umformtechnik", "Werkzeugmechanikerin- Stanz- und Umformtechnik", null, 2014)
		}));

		/** Fachklasse Energieelektroniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_115_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2021000, 90, "115", "00", true, "T", "ET", null, null, null, "Energieelektroniker/-in", "Energieelektroniker", "Energieelektronikerin", null, 2014)
		}));

		/** Fachklasse Kommunikationselektroniker/-in - Funktechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_116_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2022000, 90, "116", "00", true, "T", "ET", null, null, null, "Kommunikationselektroniker/-in - Funktechnik", "Kommunikationselektroniker - Funktechnik", "Kommunikationselektronikerin - Funktechnik", null, 2014)
		}));

		/** Fachklasse Mechatroniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_117_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2023000, 90, "117", "00", true, "O", "OH", null, null, null, "Mechatroniker/-in", "Mechatroniker", "Mechatronikerin", null, 2019)
		}));

		/** Fachklasse Maßschneider/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_118_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2024000, 90, "118", "00", true, "T", "TB", null, null, null, "Maßschneider/-in", "Maßschneider", "Maßschneiderin", null, 2019)
		}));

		/** Fachklasse Informations- und Telekommunikationssystemelektroniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_119_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2025000, 90, "119", "00", true, "O", "OH", null, null, null, "Informations- und Telekommunikationssystemelektroniker/-in", "Informations- und Telekommunikationssystemelektroniker", "Informations- und Telekommunikationssystemelektronikerin", null, 2019)
		}));

		/** Fachklasse Kommunikationselektroniker/-in - Informationstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_120_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2026000, 90, "120", "00", true, "T", "ET", null, null, null, "Kommunikationselektroniker/-in - Informationstechnik", "Kommunikationselektroniker - Informationstechnik", "Kommunikationselektronikerin - Informationstechnik", null, 2014)
		}));

		/** Fachklasse Oberflächenbeschichter/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_121_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2027000, 90, "121", "00", true, "T", "CP", null, null, null, "Oberflächenbeschichter/-in", "Oberflächenbeschichter", "Oberflächenbeschichterin", null, 2019)
		}));

		/** Fachklasse Kaufmann/-frau für Bürokommunikation (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_122_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2028000, 90, "122", "00", true, "O", "OH", null, null, null, "Kaufmann/-frau für Bürokommunikation", "Kaufmann für Bürokommunikation", "Kauffrau für Bürokommunikation", null, 2014)
		}));

		/** Fachklasse Bauten- und Objektbeschichter/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_123_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2029000, 90, "123", "00", true, "G", "FR", null, null, null, "Bauten- und Objektbeschichter/-in", "Bauten- und Objektbeschichter", "Bauten- und Objektbeschichterin", null, 2014)
		}));

		/** Fachklasse Bürokaufmann/-frau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_124_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2030000, 90, "124", "00", true, "W", "WV", null, null, null, "Bürokaufmann/-frau", "Bürokaufmann", "Bürokauffrau", null, 2019)
		}));

		/** Fachklasse Friseur/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_125_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2031000, 90, "125", "00", true, "S", "KP", null, null, null, "Friseur/-in", "Friseur", "Friseurin", null, 2019)
		}));

		/** Fachklasse Konstruktionsmechaniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_126_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2032000, 90, "126", "00", true, "T", "MT", null, null, null, "Konstruktionsmechaniker/-in", "Konstruktionsmechaniker", "Konstruktionsmechanikerin", null, 2014)
		}));

		/** Fachklasse Hauswirtschafter/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_127_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2033000, 90, "127", "00", true, "E", "EH", null, null, null, "Hauswirtschafter/-in", "Hauswirtschafter", "Hauswirtschafterin", null, 2019)
		}));

		/** Fachklasse Maschinen- und Anlagenführer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_128_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2034000, 90, "128", "00", true, "O", "OH", null, null, null, "Maschinen- und Anlagenführer/-in", "Maschinen- und Anlagenführer", "Maschinen- und Anlagenführerin", null, 2019)
		}));

		/** Fachklasse Verfahrensmechaniker/-in im Kunststoff- und Kautschukbereich (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_129_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2035000, 90, "129", "00", true, "O", "OH", null, null, null, "Verfahrensmechaniker/-in im Kunststoff- und Kautschukbereich", "Verfahrensmechaniker im Kunststoff- und Kautschukbereich", "Verfahrensmechanikerin im Kunststoff- und Kautschukbereich", null, 2014)
		}));

		/** Fachklasse Fachkraft für Lagerlogistik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_130_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2036000, 90, "130", "00", true, "O", "OH", null, null, null, "Fachkraft für Lagerlogistik", "Fachkraft für Lagerlogistik", "Fachkraft für Lagerlogistik", null, 2014)
		}));

		/** Fachklasse Fachlagerist/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_131_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2037000, 90, "131", "00", true, "O", "OH", null, null, null, "Fachlagerist/-in", "Fachlagerist", "Fachlageristin", null, 2019)
		}));

		/** Fachklasse Holzmechaniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_132_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2038000, 90, "132", "00", true, "T", "HT", null, null, null, "Holzmechaniker/-in", "Holzmechaniker", "Holzmechanikerin", null, 2014)
		}));

		/** Fachklasse Kaufmann/-frau im Einzelhandel (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_133_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2039000, 90, "133", "00", true, "W", "WV", null, null, null, "Kaufmann/-frau im Einzelhandel", "Kaufmann im Einzelhandel", "Kauffrau im Einzelhandel", null, 2014)
		}));

		/** Fachklasse Verkäufer/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_134_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2040000, 90, "134", "00", true, "W", "WV", null, null, null, "Verkäufer/-in", "Verkäufer", "Verkäuferin", null, 2019)
		}));

		/** Fachklasse Zerspanungsmechaniker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_135_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2041000, 90, "135", "00", true, "T", "MT", null, null, null, "Zerspanungsmechaniker/-in", "Zerspanungsmechaniker", "Zerspanungsmechanikerin", null, 2019)
		}));

		/** Fachklasse Fachverkäufer/-in im Lebensmittelhandwerk (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_136_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2042000, 90, "136", "00", true, "E", "EH", null, null, null, "Fachverkäufer/-in im Lebensmittelhandwerk", "Fachverkäufer im Lebensmittelhandwerk", "Fachverkäuferin im Lebensmittelhandwerk", null, 2014)
		}));

		/** Fachklasse Metallbauer/-in - Konstruktionstechnik (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_137_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2043000, 90, "137", "01", true, "T", "MT", null, null, null, "Metallbauer/-in - Konstruktionstechnik", "Metallbauer - Konstruktionstechnik", "Metallbauerin - Konstruktionstechnik", null, 2019)
		}));

		/** Fachklasse Metallbauer/-in - Metallgestaltung (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_137_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2044000, 90, "137", "02", true, "T", "MT", null, null, null, "Metallbauer/-in - Metallgestaltung", "Metallbauer - Metallgestaltung", "Metallbauerin - Metallgestaltung", null, 2014)
		}));

		/** Fachklasse Metallbauer/-in - Nutzfahrzeugbau (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_137_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2045000, 90, "137", "03", true, "T", "MT", null, null, null, "Metallbauer/-in - Nutzfahrzeugbau", "Metallbauer - Nutzfahrzeugbau", "Metallbauerin - Nutzfahrzeugbau", null, 2014)
		}));

		/** Fachklasse Florist/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_138_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2046000, 90, "138", "00", true, "O", "OH", null, null, null, "Florist/-in", "Florist", "Floristin", null, 2019)
		}));

		/** Fachklasse Änderungsschneider/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_139_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2047000, 90, "139", "00", true, "T", "TB", null, null, null, "Änderungsschneider/-in", "Änderungsschneider", "Änderungsschneiderin", null, 2019)
		}));

		/** Fachklasse Kosmetiker/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_140_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2048000, 90, "140", "00", true, "O", "OH", null, null, null, "Kosmetiker/-in", "Kosmetiker", "Kosmetikerin", null, 2019)
		}));

		/** Fachklasse Uhrmacher/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_141_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2049000, 90, "141", "00", true, "O", "OH", null, null, null, "Uhrmacher/-in", "Uhrmacher", "Uhrmacherin", null, 2019)
		}));

		/** Fachklasse Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2050000, 90, "999", "00", true, "X", "XX", null, null, null, "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, 2014)
		}));

		/** Fachklasse Sonstiger Bildungsgang (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_90_999_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2051000, 90, "999", "01", true, "X", "XX", null, null, null, "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", null, 2014)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 91
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex91(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Agrarwirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_91_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2052000, 91, "101", "00", false, null, null, "AW", null, null, "Agrarwirtschaft", "Agrarwirtschaft", "Agrarwirtschaft", null, null)
		}));

		/** Fachklasse Ernährungs- und Versorgungsmanagement */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_91_102_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2053000, 91, "102", "00", false, null, null, "EV", null, null, "Ernährungs- und Versorgungsmanagement", "Ernährungs- und Versorgungsmanagement", "Ernährungs- und Versorgungsmanagement", null, null)
		}));

		/** Fachklasse Farbtechnik und Raumgestaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_91_103_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2054000, 91, "103", "01", false, null, null, "GT", "FR", null, "Farbtechnik und Raumgestaltung", "Farbtechnik und Raumgestaltung", "Farbtechnik und Raumgestaltung", null, null)
		}));

		/** Fachklasse Medien/Medientechnologie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_91_103_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2055000, 91, "103", "02", false, null, null, "GT", "MM", null, "Medien/Medientechnologie", "Medien/Medientechnologie", "Medien/Medientechnologie", null, null)
		}));

		/** Fachklasse Gesundheitswesen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_91_104_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2056000, 91, "104", "01", false, null, null, "GE", "GW", null, "Gesundheitswesen", "Gesundheitswesen", "Gesundheitswesen", null, null)
		}));

		/** Fachklasse Sozialwesen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_91_104_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2057000, 91, "104", "02", false, null, null, "GE", "SW", null, "Sozialwesen", "Sozialwesen", "Sozialwesen", null, null)
		}));

		/** Fachklasse Körperpflege */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_91_104_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2058000, 91, "104", "03", false, null, null, "GE", "KP", null, "Körperpflege", "Körperpflege", "Körperpflege", null, null)
		}));

		/** Fachklasse Informatik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_91_105_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2059000, 91, "105", "00", false, null, null, "IF", null, null, "Informatik", "Informatik", "Informatik", null, null)
		}));

		/** Fachklasse Bau und Holztechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_91_106_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2060000, 91, "106", "01", false, null, null, "TN", "BH", null, "Bau und Holztechnik", "Bau und Holztechnik", "Bau und Holztechnik", null, null)
		}));

		/** Fachklasse Drucktechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_91_106_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2061000, 91, "106", "02", false, null, null, "TN", "DT", null, "Drucktechnik", "Drucktechnik", "Drucktechnik", null, null)
		}));

		/** Fachklasse Elektrotechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_91_106_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2062000, 91, "106", "03", false, null, null, "TN", "ET", null, "Elektrotechnik", "Elektrotechnik", "Elektrotechnik", null, null)
		}));

		/** Fachklasse Fahrzeugtechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_91_106_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2063000, 91, "106", "04", false, null, null, "TN", "FT", null, "Fahrzeugtechnik", "Fahrzeugtechnik", "Fahrzeugtechnik", null, null)
		}));

		/** Fachklasse Medizintechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_91_106_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2064000, 91, "106", "05", false, null, null, "TN", "MD", null, "Medizintechnik", "Medizintechnik", "Medizintechnik", null, null)
		}));

		/** Fachklasse Metalltechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_91_106_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2065000, 91, "106", "06", false, null, null, "TN", "ME", null, "Metalltechnik", "Metalltechnik", "Metalltechnik", null, null)
		}));

		/** Fachklasse Physik/Chemie/Biologie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_91_106_07", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2066000, 91, "106", "07", false, null, null, "TN", "PC", null, "Physik/Chemie/Biologie", "Physik/Chemie/Biologie", "Physik/Chemie/Biologie", null, null)
		}));

		/** Fachklasse Textiltechnik und Bekleidung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_91_106_08", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2067000, 91, "106", "08", false, null, null, "TN", "TB", null, "Textiltechnik und Bekleidung", "Textiltechnik und Bekleidung", "Textiltechnik und Bekleidung", null, null)
		}));

		/** Fachklasse Informations- und Telekommunikationstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_91_106_09", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2068000, 91, "106", "09", false, null, null, "TN", "IT", null, "Informations- und Telekommunikationstechnik", "Informations- und Telekommunikationstechnik", "Informations- und Telekommunikationstechnik", null, null)
		}));

		/** Fachklasse Wirtschaft und Verwaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_91_107_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2069000, 91, "107", "00", false, null, null, "WV", null, null, "Wirtschaft und Verwaltung", "Wirtschaft und Verwaltung", "Wirtschaft und Verwaltung", null, null)
		}));

		/** Fachklasse Sonstiger Bildungsgang */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_91_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2070000, 91, "999", "00", false, null, null, "XX", "XX", "XX", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", 2022, null)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 92
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex92(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Agrarwirtschaft */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_92_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2071000, 92, "101", "00", false, null, null, "AW", null, null, "Agrarwirtschaft", "Agrarwirtschaft", "Agrarwirtschaft", null, null)
		}));

		/** Fachklasse Ernährungs- und Versorgungsmanagement */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_92_102_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2072000, 92, "102", "00", false, null, null, "EV", null, null, "Ernährungs- und Versorgungsmanagement", "Ernährungs- und Versorgungsmanagement", "Ernährungs- und Versorgungsmanagement", null, null)
		}));

		/** Fachklasse Farbtechnik und Raumgestaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_92_103_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2073000, 92, "103", "01", false, null, null, "GT", "FR", null, "Farbtechnik und Raumgestaltung", "Farbtechnik und Raumgestaltung", "Farbtechnik und Raumgestaltung", null, null)
		}));

		/** Fachklasse Medien/Medientechnologie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_92_103_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2074000, 92, "103", "02", false, null, null, "GT", "MM", null, "Medien/Medientechnologie", "Medien/Medientechnologie", "Medien/Medientechnologie", null, null)
		}));

		/** Fachklasse Gesundheitswesen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_92_104_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2075000, 92, "104", "01", false, null, null, "GE", "GW", null, "Gesundheitswesen", "Gesundheitswesen", "Gesundheitswesen", null, null)
		}));

		/** Fachklasse Sozialwesen */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_92_104_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2076000, 92, "104", "02", false, null, null, "GE", "SW", null, "Sozialwesen", "Sozialwesen", "Sozialwesen", null, null)
		}));

		/** Fachklasse Körperpflege */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_92_104_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2077000, 92, "104", "03", false, null, null, "GE", "KP", null, "Körperpflege", "Körperpflege", "Körperpflege", null, null)
		}));

		/** Fachklasse Informatik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_92_105_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2078000, 92, "105", "00", false, null, null, "IF", null, null, "Informatik", "Informatik", "Informatik", null, null)
		}));

		/** Fachklasse Bau und Holztechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_92_106_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2079000, 92, "106", "01", false, null, null, "TN", "BH", null, "Bau und Holztechnik", "Bau und Holztechnik", "Bau und Holztechnik", null, null)
		}));

		/** Fachklasse Drucktechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_92_106_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2080000, 92, "106", "02", false, null, null, "TN", "DT", null, "Drucktechnik", "Drucktechnik", "Drucktechnik", null, null)
		}));

		/** Fachklasse Elektrotechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_92_106_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2081000, 92, "106", "03", false, null, null, "TN", "ET", null, "Elektrotechnik", "Elektrotechnik", "Elektrotechnik", null, null)
		}));

		/** Fachklasse Fahrzeugtechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_92_106_04", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2082000, 92, "106", "04", false, null, null, "TN", "FT", null, "Fahrzeugtechnik", "Fahrzeugtechnik", "Fahrzeugtechnik", null, null)
		}));

		/** Fachklasse Medizintechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_92_106_05", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2083000, 92, "106", "05", false, null, null, "TN", "MD", null, "Medizintechnik", "Medizintechnik", "Medizintechnik", null, null)
		}));

		/** Fachklasse Metalltechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_92_106_06", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2084000, 92, "106", "06", false, null, null, "TN", "ME", null, "Metalltechnik", "Metalltechnik", "Metalltechnik", null, null)
		}));

		/** Fachklasse Physik/Chemie/Biologie */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_92_106_07", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2085000, 92, "106", "07", false, null, null, "TN", "PC", null, "Physik/Chemie/Biologie", "Physik/Chemie/Biologie", "Physik/Chemie/Biologie", null, null)
		}));

		/** Fachklasse Textiltechnik und Bekleidung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_92_106_08", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2086000, 92, "106", "08", false, null, null, "TN", "TB", null, "Textiltechnik und Bekleidung", "Textiltechnik und Bekleidung", "Textiltechnik und Bekleidung", null, null)
		}));

		/** Fachklasse Informations- und Telekommunikationstechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_92_106_09", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2087000, 92, "106", "09", false, null, null, "TN", "IT", null, "Informations- und Telekommunikationstechnik", "Informations- und Telekommunikationstechnik", "Informations- und Telekommunikationstechnik", null, null)
		}));

		/** Fachklasse Wirtschaft und Verwaltung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_92_107_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2088000, 92, "107", "00", false, null, null, "WV", null, null, "Wirtschaft und Verwaltung", "Wirtschaft und Verwaltung", "Wirtschaft und Verwaltung", null, null)
		}));

		/** Fachklasse Sonstiger Bildungsgang */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_92_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2089000, 92, "999", "00", false, null, null, "XX", "XX", "XX", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", 2022, null)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 93
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex93(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Assistent/-in für Ernährung und Versorgung - Service */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_93_101_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2090000, 93, "101", "00", false, null, null, "EV", "EV", "SV", "Assistent/-in für Ernährung und Versorgung - Service", "Assistent für Ernährung und Versorgung - Service", "Assistentin für Ernährung und Versorgung - Service", null, null)
		}));

		/** Fachklasse Kinderpfleger/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_93_102_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2091000, 93, "102", "00", false, null, null, "GE", "KI", null, "Kinderpfleger/-in", "Kinderpfleger", "Kinderpflegerin", null, null)
		}));

		/** Fachklasse Kinderpfleger/-in (praxisintegriert) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_93_102_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2092000, 93, "102", "01", false, null, null, "GE", "KI", null, "Kinderpfleger/-in (praxisintegriert)", "Kinderpfleger (praxisintegriert)", "Kinderpflegerin (praxisintegriert)", 2022, null)
		}));

		/** Fachklasse Sozialassistent/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_93_103_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2093000, 93, "103", "00", false, null, null, "GE", "SA", null, "Sozialassistent/-in", "Sozialassistent", "Sozialassistentin", null, null)
		}));

		/** Fachklasse Sozialassistent/-in - Heilerziehung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_93_103_01", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2094000, 93, "103", "01", false, null, null, "GE", "SH", "HE", "Sozialassistent/-in - Heilerziehung", "Sozialassistent - Heilerziehung", "Sozialassistentin - Heilerziehung", null, null)
		}));

		/** Fachklasse Sozialassistent/-in (praxisintegriert) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_93_103_02", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2095000, 93, "103", "02", false, null, null, "GE", "SA", null, "Sozialassistent/-in (praxisintegriert)", "Sozialassistent (praxisintegriert)", "Sozialassistentin (praxisintegriert)", 2022, null)
		}));

		/** Fachklasse Sozialassistent/-in - Heilerziehung (praxisintegriert) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_93_103_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2096000, 93, "103", "03", false, null, null, "GE", "SH", "HE", "Sozialassistent/-in - Heilerziehung (praxisintegriert)", "Sozialassistent - Heilerziehung (praxisintegriert)", "Sozialassistentin - Heilerziehung (praxisintegriert)", 2022, null)
		}));

		/** Fachklasse Sonstiger Bildungsgang */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_93_999_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2097000, 93, "999", "00", false, null, null, "XX", "XX", "XX", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", "Sonstiger Bildungsgang", 2022, null)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 94
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex94(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Berufsbegleitende/-r Kinderpfleger/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_94_102_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2098000, 94, "102", "00", true, null, null, "GE", "KI", null, "Berufsbegleitende/-r Kinderpfleger/-in", "Berufsbegleitender Kinderpfleger", "Berufsbegleitende Kinderpflegerin", null, 2021)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 940
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex940(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Berufsausbildung - Kinderpfleger/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_940_134_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2099000, 940, "134", "00", false, "S", "SG", null, null, null, "Berufsausbildung - Kinderpfleger/-in", "Berufsausbildung - Kinderpfleger", "Berufsausbildung - Kinderpflegerin", null, null)
		}));

		/** Fachklasse Berufsausbildung - Elektroniker/-in für Energie- und Gebäudetechnik */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_940_177_03", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2100000, 940, "177", "03", false, "T", "ET", null, null, null, "Berufsausbildung - Elektroniker/-in für Energie- und Gebäudetechnik", "Berufsausbildung - Elektroniker für Energie- und Gebäudetechnik", "Berufsausbildung - Elektronikerin für Energie- und Gebäudetechnik", null, null)
		}));

		/** Fachklasse Berufsausbildung - Tischler/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_940_454_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2101000, 940, "454", "00", false, "T", "HT", null, null, null, "Berufsausbildung - Tischler/-in", "Berufsausbildung - Tischler", "Berufsausbildung - Tischlerin", null, null)
		}));

		/** Fachklasse Berufsausbildung - Feinwerkmechaniker/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_940_509_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2102000, 940, "509", "00", false, "T", "MT", null, null, null, "Berufsausbildung - Feinwerkmechaniker/-in", "Berufsausbildung - Feinwerkmechaniker", "Berufsausbildung - Feinwerkmechanikerin", null, null)
		}));

		/** Fachklasse Berufsausbildung - Maßschneider/-in */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_940_532_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2103000, 940, "532", "00", false, "T", "TB", null, null, null, "Berufsausbildung - Maßschneider/-in", "Berufsausbildung - Maßschneider", "Berufsausbildung - Maßschneiderin", null, null)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 960
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex960(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Erzieher/-in / AHR (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_960_106_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2104000, 960, "106", "00", true, "S", "ES", null, null, null, "Erzieher/-in / AHR", "Erzieher/ AHR", "Erzieherin/ AHR", null, 2012)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 970
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex970(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Erzieher/-in (ausgelaufen) */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_970_108_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2105000, 970, "108", "00", true, "S", "SG", null, null, null, "Erzieher/-in", "Erzieher", "Erzieherin", null, 2012)
		}));
	}

	/**
	 * Initialisiert den Vektor mit den Fachklassen des Berufkollegs
	 * mit den Einträgen für den Index 980
	 *
	 * @param fachklassen   die Liste, in dem die Fachklassen ergänzt werden
	 */
	private static void initFachklassenIndex980(@NotNull Vector<@NotNull BerufskollegFachklassen> fachklassen) {
		/** Fachklasse Berufsgrundbildung - ohne weitere Zuordnung */
		fachklassen.add(new BerufskollegFachklassen("Fachklasse_980_998_00", new BerufskollegFachklassenKatalogEintrag[] {
			new BerufskollegFachklassenKatalogEintrag(2106000, 980, "998", "00", false, "O", "OH", null, null, null, "Berufsgrundbildung - ohne weitere Zuordnung", "Berufsgrundbildung - ohne weitere Zuordnung", "Berufsgrundbildung - ohne weitere Zuordnung", null, null)
		}));
	}

	// GENERIERTER CODE ENDE

	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	

	/** Der aktuellen Daten der Fachklasse, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull BerufskollegFachklassenKatalogEintrag daten;
	
	/** Die Historie mit den Einträgen der Fachklasse */
	public final @NotNull BerufskollegFachklassenKatalogEintrag@NotNull[] historie;	

	/** Der eindeutige Name des Core-Types (enum Emulation) */
	private final @NotNull String __name;
	
	
	/**
	 * Gibt alle Fachklassen als Array zurück. 
	 * 
	 * @return alle Fachklassen
	 */
	public static BerufskollegFachklassen[] values() {
		return _values.toArray(new BerufskollegFachklassen[0]);
	}

	/**
	 * Erzeugt eine neue Fachklasse in der Aufzählung.
	 * 
	 * @param historie   die Historie der Fachklasse, welches ein Array von {@link BerufskollegFachklassenKatalogEintrag} ist  
	 */
	private BerufskollegFachklassen(@NotNull String name, @NotNull BerufskollegFachklassenKatalogEintrag@NotNull[] historie) {
		this.__name = name;
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist 
		this.daten = historie[historie.length - 1];
	}


	@Override
	public @NotNull String toString() {
		return __name;
	}
	
	
}
