package de.svws_nrw.core.utils.uvblockung;

import java.util.List;

import de.svws_nrw.core.data.fach.FachDaten;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.lehrer.LehrerUnterrichtsfach;
import de.svws_nrw.core.data.uv.UvFach;
import de.svws_nrw.core.data.uv.UvKlasse;
import de.svws_nrw.core.data.uv.UvLehrer;
import de.svws_nrw.core.data.uv.UvLehrerPflichtstundensoll;
import de.svws_nrw.core.data.uv.UvLerngruppe;
import de.svws_nrw.core.data.uv.UvPlanungsabschnitt;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittLehrer;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittSchueler;
import de.svws_nrw.core.data.uv.UvSchuelergruppe;
import de.svws_nrw.core.data.uv.UvSchuelergruppeSchueler;
import de.svws_nrw.core.utils.uv.UvManager;

/**
 * Hilfsklasse zum Erzeugen von UV-Testdaten.
 */
public final class UvDatensatz {

	private UvDatensatz() {
		// Util-Klasse
	}

	/**
	 * Erzeugt einen Planungsabschnitt.
	 *
	 * @param id           Die UV-ID.
	 * @param gueltigVon   Der Beginnzeitpunkt.
	 * @param gueltigBis   Der Endzeitpunkt.
	 *
	 * @return der erzeugte Planungsabschnitt.
	 */
	public static UvPlanungsabschnitt erzeugePlanungsabschnitt(final long id, final String gueltigVon, final String gueltigBis) {
		final UvPlanungsabschnitt pl = new UvPlanungsabschnitt();
		pl.id = id;
		pl.gueltigVon = gueltigVon;
		pl.gueltigBis = gueltigBis;
		return pl;
	}

	/**
	 * Erzeugt Fachdaten.
	 *
	 * @param id                 Die UV-ID.
	 * @param kuerzel            Das Kürzel.
	 * @param kuerzelStatistik   Das Statistik-Kürzel.
	 * @param bezeichnung        Die Bezeichnung.
	 * @param istSichtbar        {@code true}, falls sichtbar.
	 * @param sortierung         Die Sortierung.
	 *
	 * @return die erzeugten Fachdaten.
	 */
	public static FachDaten erzeugeFachDaten(final long id, final String kuerzel, final String kuerzelStatistik, final String bezeichnung,
			final boolean istSichtbar, final int sortierung) {
		final FachDaten fd = new FachDaten();
		fd.id = id;
		fd.kuerzel = kuerzel;
		fd.kuerzelStatistik = kuerzelStatistik;
		fd.bezeichnung = bezeichnung;
		fd.istSichtbar = istSichtbar;
		fd.sortierung = sortierung;
		return fd;
	}

	/**
	 * Erzeugt ein UV-Fach.
	 *
	 * @param id           Die UV-ID.
	 * @param idFach       Die Fachdaten-ID.
	 * @param gueltigVon   Der Beginnzeitpunkt.
	 * @param gueltigBis   Der Endzeitpunkt.
	 *
	 * @return das erzeugte UV-Fach.
	 */
	public static UvFach erzeugeUvFach(final long id, final long idFach, final String gueltigVon, final String gueltigBis) {
		final UvFach fa = new UvFach();
		fa.id = id;
		fa.idFach = idFach;
		fa.gueltigVon = gueltigVon;
		fa.gueltigBis = gueltigBis;
		return fa;
	}

	/**
	 * Erzeugt einen Jahrgang.
	 *
	 * @param id                       Die UV-ID.
	 * @param kuerzel                  Das Kürzel.
	 * @param kurzbezeichnung          Die Kurzbezeichnung.
	 * @param kuerzelStatistik         Das Statistik-Kürzel.
	 * @param bezeichnung              Die Bezeichnung.
	 * @param sortierung               Die Sortierung.
	 * @param kuerzelSchulgliederung   Das Kürzel der Schulgliederung.
	 * @param istSichtbar              {@code true}, falls sichtbar.
	 *
	 * @return der erzeugte Jahrgang.
	 */
	public static JahrgangsDaten erzeugeJahrgang(final long id, final String kuerzel, final String kurzbezeichnung, final String kuerzelStatistik,
			final String bezeichnung, final int sortierung, final String kuerzelSchulgliederung, final boolean istSichtbar) {
		final JahrgangsDaten jg = new JahrgangsDaten();
		jg.id = id;
		jg.kuerzel = kuerzel;
		jg.kurzbezeichnung = kurzbezeichnung;
		jg.bezeichnung = bezeichnung;
		jg.sortierung = sortierung;
		jg.istSichtbar = istSichtbar;
		return jg;
	}

	/**
	 * Erzeugt eine Schülergruppe.
	 *
	 * @param id                     Die UV-ID.
	 * @param idPlanungsabschnitt    Die Planungsabschnitt-ID.
	 * @param bezeichnung            Die Bezeichnung.
	 * @param idsJahrgaengeErlaubt   Die erlaubten Jahrgänge.
	 *
	 * @return die erzeugte Schülergruppe.
	 */
	public static UvSchuelergruppe erzeugeSchuelergruppe(final long id, final long idPlanungsabschnitt, final String bezeichnung,
			final Long... idsJahrgaengeErlaubt) {
		final UvSchuelergruppe sg = new UvSchuelergruppe();
		sg.id = id;
		sg.idPlanungsabschnitt = idPlanungsabschnitt;
		sg.bezeichnung = bezeichnung;
		for (final Long idJahrgang : idsJahrgaengeErlaubt) {
			sg.idsJahrgaengeErlaubt.add(idJahrgang);
		}
		return sg;
	}

	/**
	 * Erzeugt eine Klasse.
	 *
	 * @param id                    Die UV-ID.
	 * @param idPlanungsabschnitt   Die Planungsabschnitt-ID.
	 * @param kuerzel               Das Kürzel.
	 * @param idSchuelergruppe      Die Schülergruppen-ID.
	 *
	 * @return die erzeugte Klasse.
	 */
	public static UvKlasse erzeugeKlasse(final long id, final long idPlanungsabschnitt, final String kuerzel, final long idSchuelergruppe) {
		final UvKlasse kl = new UvKlasse();
		kl.id = id;
		kl.idPlanungsabschnitt = idPlanungsabschnitt;
		kl.kuerzel = kuerzel;
		kl.idSchuelergruppe = idSchuelergruppe;
		return kl;
	}

	/**
	 * Erzeugt einen Schüler im Planungsabschnitt.
	 *
	 * @param idPlanungsabschnitt   Die Planungsabschnitt-ID.
	 * @param idSchueler            Die Schüler-ID.
	 * @param idJahrgang            Die Jahrgangs-ID.
	 * @param idKlasse              Die Klassen-ID.
	 *
	 * @return der erzeugte Schüler.
	 */
	public static UvPlanungsabschnittSchueler erzeugePlanungsabschnittSchueler(final long idPlanungsabschnitt, final long idSchueler,
			final long idJahrgang, final long idKlasse) {
		final UvPlanungsabschnittSchueler psch = new UvPlanungsabschnittSchueler();
		psch.idPlanungsabschnitt = idPlanungsabschnitt;
		psch.idSchueler = idSchueler;
		psch.idJahrgang = idJahrgang;
		psch.idKlasse = idKlasse;
		return psch;
	}

	/**
	 * Erzeugt die Zuordnung eines Schülers zu einer Schülergruppe.
	 *
	 * @param idPlanungsabschnitt   Die Planungsabschnitt-ID.
	 * @param idSchuelergruppe      Die Schülergruppen-ID.
	 * @param idSchueler            Die Schüler-ID.
	 *
	 * @return die erzeugte Zuordnung.
	 */
	public static UvSchuelergruppeSchueler erzeugeSchuelergruppeSchueler(final long idPlanungsabschnitt, final long idSchuelergruppe,
			final long idSchueler) {
		final UvSchuelergruppeSchueler sgs = new UvSchuelergruppeSchueler();
		sgs.idPlanungsabschnitt = idPlanungsabschnitt;
		sgs.idSchuelergruppe = idSchuelergruppe;
		sgs.idSchueler = idSchueler;
		return sgs;
	}

	/**
	 * Erzeugt einen Lehrer.
	 *
	 * @param id            Die UV-ID.
	 * @param kuerzel       Das Kürzel.
	 * @param nachname      Der Nachname.
	 * @param vorname       Der Vorname.
	 * @param datumZugang   Das Zugangsdatum.
	 * @param datumAbgang   Das Abgangsdatum.
	 *
	 * @return der erzeugte Lehrer.
	 */
	public static UvLehrer erzeugeLehrer(final long id, final String kuerzel, final String nachname, final String vorname,
			final String datumZugang, final String datumAbgang) {
		final UvLehrer le = new UvLehrer();
		le.id = id;
		le.idKLehrer = null;
		le.kuerzel = kuerzel;
		le.nachname = nachname;
		le.vorname = vorname;
		le.datumZugang = datumZugang;
		le.datumAbgang = datumAbgang;
		return le;
	}

	/**
	 * Erzeugt ein Pflichtstundensoll.
	 *
	 * @param id               Die UV-ID.
	 * @param idLehrer         Die Lehrer-ID.
	 * @param gueltigVon       Der Beginnzeitpunkt.
	 * @param gueltigBis       Der Endzeitpunkt.
	 * @param pflichtstdSoll   Das Pflichtstundensoll.
	 *
	 * @return das erzeugte Pflichtstundensoll.
	 */
	public static UvLehrerPflichtstundensoll erzeugePflichtstundensoll(final long id, final long idLehrer, final String gueltigVon,
			final String gueltigBis, final double pflichtstdSoll) {
		final UvLehrerPflichtstundensoll ps = new UvLehrerPflichtstundensoll();
		ps.id = id;
		ps.idLehrer = idLehrer;
		ps.gueltigVon = gueltigVon;
		ps.gueltigBis = gueltigBis;
		ps.pflichtstdSoll = pflichtstdSoll;
		return ps;
	}

	/**
	 * Erzeugt eine Planungsabschnitt-Lehrer-Zuordnung.
	 *
	 * @param idPlanungsabschnitt   Die Planungsabschnitt-ID.
	 * @param idLehrer              Die Lehrer-ID.
	 *
	 * @return die erzeugte Zuordnung.
	 */
	public static UvPlanungsabschnittLehrer erzeugePlanungsabschnittLehrer(final long idPlanungsabschnitt, final long idLehrer) {
		final UvPlanungsabschnittLehrer pal = new UvPlanungsabschnittLehrer();
		pal.idPlanungsabschnitt = idPlanungsabschnitt;
		pal.idLehrer = idLehrer;
		return pal;
	}

	/**
	 * Erzeugt ein Unterrichtsfach für einen Lehrer.
	 *
	 * @param id           Die UV-ID.
	 * @param idLehrer     Die Lehrer-ID.
	 * @param idFach       Die Fach-ID.
	 * @param istSek1      {@code true}, falls Sekundarstufe I.
	 * @param istSek2      {@code true}, falls Sekundarstufe II.
	 * @param istKLehrer   {@code true}, falls Klassenlehrer.
	 *
	 * @return das erzeugte Unterrichtsfach.
	 */
	public static LehrerUnterrichtsfach erzeugeLehrerUnterrichtsfach(final long id, final long idLehrer, final long idFach,
			final boolean istSek1, final boolean istSek2, final boolean istKLehrer) {
		final LehrerUnterrichtsfach uf = new LehrerUnterrichtsfach();
		uf.id = id;
		uf.idLehrer = idLehrer;
		uf.idFach = idFach;
		uf.istSek1 = istSek1;
		uf.istSek2 = istSek2;
		uf.istKLehrer = istKLehrer;
		return uf;
	}

	/**
	 * Erzeugt eine Lerngruppe.
	 *
	 * @param id                    Die UV-ID.
	 * @param idPlanungsabschnitt   Die Planungsabschnitt-ID.
	 * @param idKlasse              Die Klassen-ID.
	 * @param idFach                Die Fach-ID.
	 * @param idKurs                Die Kurs-ID.
	 * @param wochenstunden         Die Wochenstunden.
	 *
	 * @return die erzeugte Lerngruppe.
	 */
	public static UvLerngruppe erzeugeLerngruppe(final long id, final long idPlanungsabschnitt, final long idKlasse, final long idFach, final Long idKurs,
			final int wochenstunden) {
		final UvLerngruppe lg = new UvLerngruppe();
		lg.id = id;
		lg.idPlanungsabschnitt = idPlanungsabschnitt;
		lg.idKlasse = idKlasse;
		lg.idFach = idFach;
		lg.idKurs = idKurs;
		lg.wochenstunden = wochenstunden;
		return lg;
	}

	/**
	 * Erzeugt einen UvManager.
	 *
	 * @param jahraenge   Die Jahrgänge.
	 * @param faecher     Die Fächer.
	 *
	 * @return der erzeugte Manager.
	 */
	public static UvManager erzeugeUvManager(final List<JahrgangsDaten> jahraenge, final List<FachDaten> faecher) {
		return new UvManager(jahraenge, faecher);
	}
}
