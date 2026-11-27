package de.svws_nrw.core.utils.uvblockung;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.uv.UvKlassenLehrer;
import de.svws_nrw.core.data.uv.UvLerngruppenLehrer;
import de.svws_nrw.core.data.uv.UvPlanungsabschnitt;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegel;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelPrioritaet;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelTyp;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.utils.uv.UvManager;
import de.svws_nrw.core.utils.uv.UvRegelManager;
import jakarta.validation.constraints.NotNull;

/**
 * Testet die Konflikt-Erkennung des {@link UvRegelManager}.
 * <br>Widersprüchliche Regeln (z. B. eine Lehrkraft ist in einer Lerngruppe sowohl verboten als auch fixiert)
 * werden als fehlerhaft markiert und dadurch automatisch entfernt.
 *
 * @author Benjamin A. Bartsch
 */
@DisplayName("Testet den UvRegelManager.")
class TestUvRegelManager {

	/** Durch einen eigenen Logger werden die absichtlichen ERROR-Meldungen konsumiert. */
	private static final Logger LOG = new Logger();

	private static final Random RND = new Random(1L);

	/** Die Standard-Priorität der Test-Regeln. */
	private static final int PRIO = UvBlockungRegelPrioritaet.MITTEL.nr;

	/**
	 * Initialisierung der ASD-Core-Types vor allen Tests.
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}


	private static UvBlockungRegel erzeugeRegel(final long id, final @NotNull UvBlockungRegelTyp typ, final int prioritaet, final long... parameter) {
		final UvBlockungRegel regel = new UvBlockungRegel();
		regel.id = id;
		regel.typ = typ.nr;
		regel.prioritaet = prioritaet;
		for (final long wert : parameter) {
			regel.parameter.add(wert);
		}
		return regel;
	}


	@Test
	@DisplayName("testKonfliktVerbotenUndFixiertInLerngruppe")
	void testKonfliktVerbotenUndFixiertInLerngruppe() {
		final UvManager man = UvDatensatz1.erzeugeUvManager();
		final List<UvBlockungRegel> regeln = new ArrayList<>();
		regeln.add(erzeugeRegel(1, UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_LERNGRUPPE_B, PRIO, UvDatensatz1.ID_LEHRER, UvDatensatz1.ID_LERNGRUPPE));
		regeln.add(erzeugeRegel(2, UvBlockungRegelTyp.LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B, PRIO, UvDatensatz1.ID_LEHRER, UvDatensatz1.ID_LERNGRUPPE));

		final UvRegelManager manRegeln = new UvRegelManager(man, regeln);
		// Die spätere Regel (ID=2) wird entfernt, die frühere (ID=1) bleibt aktiv.
		assertEquals(1, manRegeln.regelnGetMengeFehlerhaftAsList().size());
		assertEquals(2, manRegeln.regelnGetMengeFehlerhaftAsList().get(0).id);
		assertEquals(1, manRegeln.regelnGetMengeAktiviertUndFehlerfreiAsList().size());
		assertEquals(1, manRegeln.regelnGetMengeAktiviertUndFehlerfreiAsList().get(0).id);

		// Der Algorithmus darf nun nicht mehr werfen.
		final UvPlanungsabschnitt pla = man.planungsabschnittGetByIdOrException(UvDatensatz1.ID_PLANUNGSABSCHNITT);
		assertDoesNotThrow(() -> new UvAlgorithmusDynDaten(LOG, RND, man, manRegeln, pla));
	}


	@Test
	@DisplayName("testKonfliktUmgekehrteReihenfolge")
	void testKonfliktUmgekehrteReihenfolge() {
		// Fixierung zuerst, Verbot danach -> das Verbot (ID=2) wird entfernt.
		final UvManager man1 = UvDatensatz1.erzeugeUvManager();
		final List<UvBlockungRegel> regeln1 = new ArrayList<>();
		regeln1.add(erzeugeRegel(1, UvBlockungRegelTyp.LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B, PRIO, UvDatensatz1.ID_LEHRER, UvDatensatz1.ID_LERNGRUPPE));
		regeln1.add(erzeugeRegel(2, UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_LERNGRUPPE_B, PRIO, UvDatensatz1.ID_LEHRER, UvDatensatz1.ID_LERNGRUPPE));

		final UvRegelManager manRegeln1 = new UvRegelManager(man1, regeln1);
		assertEquals(1, manRegeln1.regelnGetMengeFehlerhaftAsList().size());
		assertEquals(2, manRegeln1.regelnGetMengeFehlerhaftAsList().get(0).id);
		assertEquals(1, manRegeln1.regelnGetMengeAktiviertUndFehlerfreiAsList().size());
		assertEquals(1, manRegeln1.regelnGetMengeAktiviertUndFehlerfreiAsList().get(0).id);

		// Fixierung zuerst, Verbot in der Klasse danach -> das Verbot (ID=2) wird entfernt.
		final UvManager man2 = UvDatensatz1.erzeugeUvManager();
		final List<UvBlockungRegel> regeln2 = new ArrayList<>();
		regeln2.add(erzeugeRegel(1, UvBlockungRegelTyp.LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B, PRIO, UvDatensatz1.ID_LEHRER, UvDatensatz1.ID_LERNGRUPPE));
		regeln2.add(erzeugeRegel(2, UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_KLASSE_B, PRIO, UvDatensatz1.ID_LEHRER, UvDatensatz1.ID_KLASSE));

		final UvRegelManager manRegeln2 = new UvRegelManager(man2, regeln2);
		assertEquals(1, manRegeln2.regelnGetMengeFehlerhaftAsList().size());
		assertEquals(2, manRegeln2.regelnGetMengeFehlerhaftAsList().get(0).id);
		assertEquals(1, manRegeln2.regelnGetMengeAktiviertUndFehlerfreiAsList().size());
		assertEquals(1, manRegeln2.regelnGetMengeAktiviertUndFehlerfreiAsList().get(0).id);
	}


	@Test
	@DisplayName("testKeinKonfliktBeiVerschiedenenLehrkraeften")
	void testKeinKonfliktBeiVerschiedenenLehrkraeften() {
		final UvManager man = UvDatensatz2.erzeugeUvManager();
		final List<UvBlockungRegel> regeln = new ArrayList<>();
		regeln.add(erzeugeRegel(1, UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_LERNGRUPPE_B, PRIO, UvDatensatz2.ID_LEHRER1, UvDatensatz2.ID_LERNGRUPPE1));
		regeln.add(erzeugeRegel(2, UvBlockungRegelTyp.LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B, PRIO, UvDatensatz2.ID_LEHRER2, UvDatensatz2.ID_LERNGRUPPE1));

		final UvRegelManager manRegeln = new UvRegelManager(man, regeln);
		assertEquals(0, manRegeln.regelnGetMengeFehlerhaftAsList().size());
		assertEquals(2, manRegeln.regelnGetMengeAktiviertUndFehlerfreiAsList().size());
	}


	@Test
	@DisplayName("testKonfliktVerbotenUndFixiertAlsLeitung1")
	void testKonfliktVerbotenUndFixiertAlsLeitung1() {
		final UvManager man = UvDatensatz1.erzeugeUvManager();
		final List<UvBlockungRegel> regeln = new ArrayList<>();
		regeln.add(erzeugeRegel(1, UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_ALS_KLASSENLEHRER_IN_KLASSE_B, PRIO, UvDatensatz1.ID_LEHRER,
				UvDatensatz1.ID_KLASSE));
		regeln.add(erzeugeRegel(2, UvBlockungRegelTyp.LEHRKRAFT_A_IST_KLASSENLEHRER_IN_KLASSE_B, PRIO, UvDatensatz1.ID_LEHRER, UvDatensatz1.ID_KLASSE));

		final UvRegelManager manRegeln = new UvRegelManager(man, regeln);
		assertEquals(1, manRegeln.regelnGetMengeFehlerhaftAsList().size());
		assertEquals(2, manRegeln.regelnGetMengeFehlerhaftAsList().get(0).id);
		assertEquals(1, manRegeln.regelnGetMengeAktiviertUndFehlerfreiAsList().size());
		assertEquals(1, manRegeln.regelnGetMengeAktiviertUndFehlerfreiAsList().get(0).id);
	}


	@Test
	@DisplayName("testKonfliktVerbotenUndFixiertAlsLeitung2")
	void testKonfliktVerbotenUndFixiertAlsLeitung2() {
		final UvManager man = UvDatensatz1.erzeugeUvManager();
		final List<UvBlockungRegel> regeln = new ArrayList<>();
		regeln.add(erzeugeRegel(1, UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_ALS_STELLV_KLASSENLEHRER_IN_KLASSE_B, PRIO, UvDatensatz1.ID_LEHRER,
				UvDatensatz1.ID_KLASSE));
		regeln.add(erzeugeRegel(2, UvBlockungRegelTyp.LEHRKRAFT_A_IST_STELLV_KLASSENLEHRER_IN_KLASSE_B, PRIO, UvDatensatz1.ID_LEHRER,
				UvDatensatz1.ID_KLASSE));

		final UvRegelManager manRegeln = new UvRegelManager(man, regeln);
		assertEquals(1, manRegeln.regelnGetMengeFehlerhaftAsList().size());
		assertEquals(2, manRegeln.regelnGetMengeFehlerhaftAsList().get(0).id);
		assertEquals(1, manRegeln.regelnGetMengeAktiviertUndFehlerfreiAsList().size());
		assertEquals(1, manRegeln.regelnGetMengeAktiviertUndFehlerfreiAsList().get(0).id);
	}


	@Test
	@DisplayName("testKonfliktLeitung1UndLeitung2Fixiert")
	void testKonfliktLeitung1UndLeitung2Fixiert() {
		final UvManager man = UvDatensatz1.erzeugeUvManager();
		final List<UvBlockungRegel> regeln = new ArrayList<>();
		regeln.add(erzeugeRegel(1, UvBlockungRegelTyp.LEHRKRAFT_A_IST_KLASSENLEHRER_IN_KLASSE_B, PRIO, UvDatensatz1.ID_LEHRER, UvDatensatz1.ID_KLASSE));
		regeln.add(erzeugeRegel(2, UvBlockungRegelTyp.LEHRKRAFT_A_IST_STELLV_KLASSENLEHRER_IN_KLASSE_B, PRIO, UvDatensatz1.ID_LEHRER,
				UvDatensatz1.ID_KLASSE));

		final UvRegelManager manRegeln = new UvRegelManager(man, regeln);
		assertEquals(1, manRegeln.regelnGetMengeFehlerhaftAsList().size());
		assertEquals(2, manRegeln.regelnGetMengeFehlerhaftAsList().get(0).id);
		assertEquals(1, manRegeln.regelnGetMengeAktiviertUndFehlerfreiAsList().size());
		assertEquals(1, manRegeln.regelnGetMengeAktiviertUndFehlerfreiAsList().get(0).id);
	}


	@Test
	@DisplayName("testKonfliktVerbotenInKlasseUndFixiertInLerngruppe")
	void testKonfliktVerbotenInKlasseUndFixiertInLerngruppe() {
		final UvManager man = UvDatensatz1.erzeugeUvManager();
		final List<UvBlockungRegel> regeln = new ArrayList<>();
		regeln.add(erzeugeRegel(1, UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_KLASSE_B, PRIO, UvDatensatz1.ID_LEHRER, UvDatensatz1.ID_KLASSE));
		regeln.add(erzeugeRegel(2, UvBlockungRegelTyp.LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B, PRIO, UvDatensatz1.ID_LEHRER, UvDatensatz1.ID_LERNGRUPPE));

		final UvRegelManager manRegeln = new UvRegelManager(man, regeln);
		assertEquals(1, manRegeln.regelnGetMengeFehlerhaftAsList().size());
		assertEquals(2, manRegeln.regelnGetMengeFehlerhaftAsList().get(0).id);
		assertEquals(1, manRegeln.regelnGetMengeAktiviertUndFehlerfreiAsList().size());
		assertEquals(1, manRegeln.regelnGetMengeAktiviertUndFehlerfreiAsList().get(0).id);
	}


	@Test
	@DisplayName("testKonfliktVerbotenInJahrgangUndFixiertInLerngruppe")
	void testKonfliktVerbotenInJahrgangUndFixiertInLerngruppe() {
		final UvManager man = UvDatensatz1.erzeugeUvManager();
		final List<UvBlockungRegel> regeln = new ArrayList<>();
		regeln.add(erzeugeRegel(1, UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_JAHRGANG_B, PRIO, UvDatensatz1.ID_LEHRER, UvDatensatz1.JAHRGANG));
		regeln.add(erzeugeRegel(2, UvBlockungRegelTyp.LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B, PRIO, UvDatensatz1.ID_LEHRER, UvDatensatz1.ID_LERNGRUPPE));

		final UvRegelManager manRegeln = new UvRegelManager(man, regeln);
		assertEquals(1, manRegeln.regelnGetMengeFehlerhaftAsList().size());
		assertEquals(2, manRegeln.regelnGetMengeFehlerhaftAsList().get(0).id);
		assertEquals(1, manRegeln.regelnGetMengeAktiviertUndFehlerfreiAsList().size());
		assertEquals(1, manRegeln.regelnGetMengeAktiviertUndFehlerfreiAsList().get(0).id);
	}


	@Test
	@DisplayName("testDeaktivierteRegelErzeugtKeinenKonflikt")
	void testDeaktivierteRegelErzeugtKeinenKonflikt() {
		final UvManager man = UvDatensatz1.erzeugeUvManager();
		final List<UvBlockungRegel> regeln = new ArrayList<>();
		regeln.add(erzeugeRegel(1, UvBlockungRegelTyp.LEHRKRAFT_A_VERBOTEN_IN_LERNGRUPPE_B, UvBlockungRegelPrioritaet.DEAKTIVIERT.nr, UvDatensatz1.ID_LEHRER,
				UvDatensatz1.ID_LERNGRUPPE));
		regeln.add(erzeugeRegel(2, UvBlockungRegelTyp.LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B, PRIO, UvDatensatz1.ID_LEHRER, UvDatensatz1.ID_LERNGRUPPE));

		final UvRegelManager manRegeln = new UvRegelManager(man, regeln);
		assertEquals(0, manRegeln.regelnGetMengeFehlerhaftAsList().size());
		assertEquals(1, manRegeln.regelnGetMengeAktiviertUndFehlerfreiAsList().size());

		// Regression: Die Liste der deaktivierten Regeln darf nicht die fehlerhaften Regeln zurückgeben.
		final List<UvBlockungRegel> deaktiviert = manRegeln.regelnGetMengeDeaktiviertAsList();
		assertEquals(1, deaktiviert.size());
		assertEquals(1, deaktiviert.get(0).id);
		assertTrue(deaktiviert.contains(regeln.get(0)));
	}

	@Test
	@DisplayName("Fixierung löst alle aktiven Duplikate ohne den Regelbestand zu verändern")
	void testFixierungUmschaltenKopiertRegeln() {
		final UvManager manager = UvDatensatz1.erzeugeUvManager();
		final UvPlanungsabschnitt plan = manager.planungsabschnittGetByIdOrException(UvDatensatz1.ID_PLANUNGSABSCHNITT);
		final UvBlockungRegel erste = erzeugeRegel(101, UvBlockungRegelTyp.LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B, PRIO, UvDatensatz1.ID_LEHRER, UvDatensatz1.ID_LERNGRUPPE);
		final UvBlockungRegel zweite = erzeugeRegel(102, UvBlockungRegelTyp.LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B, PRIO, UvDatensatz1.ID_LEHRER, UvDatensatz1.ID_LERNGRUPPE);
		final List<UvBlockungRegel> aenderungen = UvRegelManager.lehrerFixierungUmschaltenRegeln(manager, List.of(erste, zweite), plan, false, UvDatensatz1.ID_LERNGRUPPE, UvDatensatz1.ID_LEHRER, 0);
		assertEquals(2, aenderungen.size());
		assertTrue(aenderungen.stream().allMatch(regel -> regel.prioritaet == UvBlockungRegelPrioritaet.DEAKTIVIERT.nr));
		assertEquals(PRIO, erste.prioritaet);
		assertEquals(PRIO, zweite.prioritaet);
		aenderungen.get(0).parameter.set(0, 999L);
		assertEquals(UvDatensatz1.ID_LEHRER, erste.parameter.get(0).longValue());
	}

	@Test
	@DisplayName("Eine deaktivierte Fixierung wird als unabhängiger Entwurf reaktiviert")
	void testFixierungsEntwurfReaktiviertVorhandeneRegel() {
		final UvManager manager = UvDatensatz1.erzeugeUvManager();
		final UvPlanungsabschnitt plan = manager.planungsabschnittGetByIdOrException(UvDatensatz1.ID_PLANUNGSABSCHNITT);
		final UvLerngruppenLehrer zuordnung = new UvLerngruppenLehrer();
		zuordnung.id = 901;
		zuordnung.idPlanungsabschnitt = plan.id;
		zuordnung.idLerngruppe = UvDatensatz1.ID_LERNGRUPPE;
		zuordnung.idLehrer = UvDatensatz1.ID_LEHRER;
		manager.lerngruppenLehrerAdd(zuordnung);
		final UvBlockungRegel deaktiviert = erzeugeRegel(103, UvBlockungRegelTyp.LEHRKRAFT_A_FIXIERT_IN_LERNGRUPPE_B,
				UvBlockungRegelPrioritaet.DEAKTIVIERT.nr, UvDatensatz1.ID_LEHRER, UvDatensatz1.ID_LERNGRUPPE);
		final UvRegelManager regeln = new UvRegelManager(manager, List.of(deaktiviert));
		final UvBlockungRegel entwurf = regeln.lehrerFixierungsEntwurf(plan, UvDatensatz1.ID_LERNGRUPPE, UvDatensatz1.ID_LEHRER);
		assertEquals(deaktiviert.id, entwurf.id);
		assertEquals(UvBlockungRegelPrioritaet.SEHR_HOCH.nr, entwurf.prioritaet);
		assertEquals(UvBlockungRegelPrioritaet.DEAKTIVIERT.nr, deaktiviert.prioritaet);
		assertTrue(regeln.regelIstGueltig(entwurf));
	}

	@Test
	@DisplayName("Klassenleitungs-Fixierung verwendet die bestehende Reihenfolge")
	void testKlassenleitungsFixierungsEntwurf() {
		final UvManager manager = UvDatensatz1.erzeugeUvManager();
		final UvPlanungsabschnitt plan = manager.planungsabschnittGetByIdOrException(UvDatensatz1.ID_PLANUNGSABSCHNITT);
		final UvKlassenLehrer zuordnung = new UvKlassenLehrer();
		zuordnung.id = 902;
		zuordnung.idPlanungsabschnitt = plan.id;
		zuordnung.idKlasse = UvDatensatz1.ID_KLASSE;
		zuordnung.idLehrer = UvDatensatz1.ID_LEHRER;
		zuordnung.reihenfolge = 2;
		manager.klassenLehrerAdd(zuordnung);
		final UvRegelManager regeln = new UvRegelManager(manager, List.of());
		final UvBlockungRegel entwurf = regeln.klassenlehrerFixierungsEntwurf(plan, zuordnung.idKlasse, zuordnung.idLehrer, 2);
		assertEquals(UvBlockungRegelTyp.LEHRKRAFT_A_IST_STELLV_KLASSENLEHRER_IN_KLASSE_B.nr, entwurf.typ);
		assertEquals(List.of(zuordnung.idLehrer, zuordnung.idKlasse), entwurf.parameter);
	}

}
