package de.svws_nrw.service.gost;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.data.gost.GostJahrgangFachwahlen;
import de.svws_nrw.core.data.gost.GostJahrgangFachwahlenHalbjahr;
import de.svws_nrw.core.data.gost.GostStatistikFachwahl;
import de.svws_nrw.core.data.gost.GostStatistikFachwahlHalbjahr;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostFachwahlManager;
import de.svws_nrw.data.gost.DBUtilsGost;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.benutzer.BenutzerAllgemeinRepository;
import de.svws_nrw.repo.schule.kataloge.fach.FachRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import jakarta.ws.rs.core.Response.Status;

/**
 * Ein Service für die aggregierten Informationen zu Fachwahlen von Abiturjahrgängen der Gymnasialen Oberstufe
 */
public class GostJahrgangFachwahlService {

	private final BenutzerAllgemeinRepository benutzerRepository;
	private final SchuelerRepository schuelerRepository;
	private final FachRepository fachRepository;
	private final GostAbiturdatenService gostAbiturdatenService;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param benutzerRepository                     das Repository für den Zugriff auf Benutzerdaten und den angemeldeten Benutzer
	 * @param schuelerRepository                     das Repository für den Zugriff auf Schülerdaten
	 * @param fachRepository                         das Repository für den Zugriff auf Fächerdaten
	 * @param gostAbiturdatenService                 der Service für den Zugriff auf die Abiturdaten der gymnasialen Oberstufe
	 */
	public GostJahrgangFachwahlService(final BenutzerAllgemeinRepository benutzerRepository,
			final SchuelerRepository schuelerRepository,
			final FachRepository fachRepository,
			final GostAbiturdatenService gostAbiturdatenService) {
		this.benutzerRepository = benutzerRepository;
		this.schuelerRepository = schuelerRepository;
		this.fachRepository = fachRepository;
		this.gostAbiturdatenService = gostAbiturdatenService;
	}


	/**
	 * Bestimmt alle Fachwahlen des angebebenen Abiturjahrgangs als Map von der ID des Schülers auf die jeweiligen Fachwahlen.
	 *
	 * @param abijahrgang   der Abiturjahrgang
	 *
	 * @return die Fachwahlen des Abiturjahrgangs als Map
	 */
	private Map<Long, GostJahrgangFachwahlen> getFachwahlenByAbiJahrgang(final int abijahrgang) {
		if (!benutzerRepository.getAktuellerBenutzer().schuleHatGymOb()) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Schule hat eine Schulform ohne gymnasiale Oberstufe.");
		}

		final Map<Long, Abiturdaten> mapAbiturdaten = gostAbiturdatenService.getMapByAbiturjahrgang(abijahrgang);

		// Lese die Fachliste aus der DB
		final Map<Long, DTOFach> faecher = fachRepository.getMap();
		if ((faecher == null) || (faecher.size() == 0)) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es konnten keine Fächer in der Datenbank gefunden werden.");
		}

		// Erstelle die Fachwahl-Objekte
		final Map<Long, GostJahrgangFachwahlen> result = new HashMap<>();
		for (final Abiturdaten abidaten : mapAbiturdaten.values()) {
			final GostJahrgangFachwahlen fachwahlen = new GostJahrgangFachwahlen();
			result.put(abidaten.schuelerID, fachwahlen);
			for (final AbiturFachbelegung belegung : abidaten.fachbelegungen) {
				final DTOFach fach = faecher.get(belegung.fachID);
				if (fach == null) {
					continue;
				}
				for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
					if (belegung.belegungen[halbjahr.id] != null) {
						final AbiturFachbelegungHalbjahr belegungHj = belegung.belegungen[halbjahr.id];
						final GostKursart kursart = GostKursart.fromKuerzel(belegungHj.kursartKuerzel);
						if (kursart == null) {
							continue;
						}
						final GostFachwahl fw = new GostFachwahl();
						fw.fachID = belegung.fachID;
						fw.schuelerID = abidaten.schuelerID;
						fw.kursartID = kursart.id;
						fw.istSchriftlich = belegungHj.schriftlich;
						fw.abiturfach = belegung.abiturFach;
						if (fachwahlen.halbjahr[halbjahr.id] == null) {
							fachwahlen.halbjahr[halbjahr.id] = new GostJahrgangFachwahlenHalbjahr();
						}
						fachwahlen.halbjahr[halbjahr.id].fachwahlen.add(fw);
					}
				}
				if ((belegung.abiturFach != null) && (belegung.belegungen[GostHalbjahr.Q22.id] != null)) {
					final AbiturFachbelegungHalbjahr belegungHj = belegung.belegungen[GostHalbjahr.Q22.id];
					final GostFachwahl fwAbi = new GostFachwahl();
					fwAbi.fachID = belegung.fachID;
					fwAbi.schuelerID = abidaten.schuelerID;
					fwAbi.kursartID = GostKursart.fromKuerzel(belegungHj.kursartKuerzel).id;
					fwAbi.istSchriftlich = belegungHj.schriftlich;
					fwAbi.abiturfach = belegung.abiturFach;
					fachwahlen.abitur.fachwahlen.add(fwAbi);
				}
			}
		}
		return result;
	}

	/**
	 * Ermittelt die Fachwahlen zu dem angegebenen Abiturjahrgang.
	 *
	 * @param abijahr   der Abiturjahrgang
	 *
	 * @return die Fachwahlen des Abiturjahrgangs dieses Objektes
	 */
	public GostJahrgangFachwahlen getSchuelerFachwahlen(final int abijahr) {
		return transactional(() -> {
			final Benutzer benutzer = benutzerRepository.getAktuellerBenutzer();
			final Map<Long, GostJahrgangFachwahlen> mapFachwahlen = getFachwahlenByAbiJahrgang(abijahr);
			final Map<Long, DTOSchueler> mapSchueler = schuelerRepository.findMapByIds(mapFachwahlen.keySet());
			final GostJahrgangFachwahlen result = new GostJahrgangFachwahlen();
			for (final Map.Entry<Long, GostJahrgangFachwahlen> entry : mapFachwahlen.entrySet()) {
				final DTOSchueler schueler = mapSchueler.get(entry.getKey());
				final GostJahrgangFachwahlen fachwahlen = entry.getValue();
				for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
					if (DBUtilsGost.pruefeIstAnSchule(schueler, halbjahr, abijahr, benutzer) && (fachwahlen.halbjahr[halbjahr.id] != null)) {
						if (result.halbjahr[halbjahr.id] == null) {
							result.halbjahr[halbjahr.id] = new GostJahrgangFachwahlenHalbjahr();
						}
						result.halbjahr[halbjahr.id].fachwahlen.addAll(fachwahlen.halbjahr[halbjahr.id].fachwahlen);
					}
				}
				if (DBUtilsGost.pruefeIstAnSchule(schueler, GostHalbjahr.Q22, abijahr, benutzer)) {
					result.abitur.fachwahlen.addAll(fachwahlen.abitur.fachwahlen);
				}
			}
			for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
				if (result.halbjahr[halbjahr.id] == null) {
					result.halbjahr[halbjahr.id] = new GostJahrgangFachwahlenHalbjahr();
				}
			}
			return result;
		});
	}


	/**
	 * Ermittelt die Fachwahlen zu dem angegebenen Abiturjahrgang und für das angegebene Halbjahr.
	 *
	 * @param abijahr    der Abiturjahrgang
	 * @param halbjahr   das Halbjahr der gymnasialen Oberstufe, für welches die Fachwahlen bestimmt werden sollen
	 *
	 * @return die Fachwahlen des Abiturjahrgangs dieses Objektes
	 */
	public GostJahrgangFachwahlenHalbjahr getSchuelerFachwahlenHalbjahr(final int abijahr, final GostHalbjahr halbjahr) {
		if (halbjahr == null) {
			return new GostJahrgangFachwahlenHalbjahr();
		}
		final GostJahrgangFachwahlenHalbjahr result = this.getSchuelerFachwahlen(abijahr).halbjahr[halbjahr.id];
		return (result == null) ? new GostJahrgangFachwahlenHalbjahr() : result;
	}


	/**
	 * Ermittelt die Fachwahlen zu dem angegebenen Abiturjahrgang und gibt einen Fachwahl-Manager dafür zurück
	 *
	 * @param abijahr    der Abiturjahrgang
	 * @param halbjahr   das Halbjahr der gymnasialen Oberstufe, für welches die Fachwahlen bestimmt werden sollen
	 *
	 * @return der Fachwahl-Manager für die Fachwahlen des Abiturjahrgangs
	 */
	public GostFachwahlManager getFachwahlManager(final int abijahr, final GostHalbjahr halbjahr) {
		return new GostFachwahlManager(this.getSchuelerFachwahlenHalbjahr(abijahr, halbjahr));
	}


	/**
	 * Ermittelt die Fachwahlen zu dem angegebenen Abiturjahrgang.
	 *
	 * @param abijahr    der Abiturjahrgang
	 *
	 * @return die Statistik zu den Fachwahlen des Abiturjahrgangs
	 */
	public List<GostStatistikFachwahl> getFachwahlStatistik(final int abijahr) {
		// Bestimme die Fachwahlen
		final GostJahrgangFachwahlen wahlen = getSchuelerFachwahlen(abijahr);

		// Lese die Fachliste aus der DB
		final Map<Long, DTOFach> faecher = fachRepository.getMap();
		if ((faecher == null) || (faecher.size() == 0)) {
			return new ArrayList<>();
		}

		// Erzeuge die Statistik
		final HashMap<Long, GostStatistikFachwahl> matrixFachwahlen = new HashMap<>();
		for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
			if (wahlen.halbjahr[halbjahr.id] == null) {
				continue;
			}
			for (final GostFachwahl wahl : wahlen.halbjahr[halbjahr.id].fachwahlen) {
				final DTOFach fach = faecher.get(wahl.fachID);
				if (fach == null) {
					continue;
				}
				GostStatistikFachwahl statfw = matrixFachwahlen.get(fach.ID);
				if (statfw == null) {
					statfw = new GostStatistikFachwahl();
					statfw.abiturjahr = abijahr;
					statfw.id = wahl.fachID;
					statfw.kuerzel = fach.Kuerzel;
					statfw.bezeichnung = fach.Bezeichnung;
					statfw.kuerzelStatistik = fach.StatistikKuerzel;
					for (final GostHalbjahr hj : GostHalbjahr.values()) {
						statfw.fachwahlen[hj.id] = new GostStatistikFachwahlHalbjahr();
					}
					matrixFachwahlen.put(statfw.id, statfw);
				}
				final GostKursart kursart = GostKursart.fromIDorNull(wahl.kursartID);
				if (kursart != null) {
					switch (kursart) {
						case GK -> {
							statfw.fachwahlen[halbjahr.id].wahlenGK++;
							if (wahl.istSchriftlich) {
								statfw.fachwahlen[halbjahr.id].wahlenGKSchriftlich++;
							} else {
								statfw.fachwahlen[halbjahr.id].wahlenGKMuendlich++;
							}
						}
						case LK -> statfw.fachwahlen[halbjahr.id].wahlenLK++;
						case PJK, VTF -> {
							statfw.fachwahlen[halbjahr.id].wahlenGK++;
							statfw.fachwahlen[halbjahr.id].wahlenGKMuendlich++;
						}
						case ZK -> statfw.fachwahlen[halbjahr.id].wahlenZK++;
					}
				}
			}
		}
		for (final GostFachwahl wahl : wahlen.abitur.fachwahlen) {
			final GostStatistikFachwahl statfw = matrixFachwahlen.get(wahl.fachID);
			if (statfw == null) {
				continue;
			}
			if (wahl.abiturfach != null) {
				if (wahl.abiturfach == 3) {
					statfw.wahlenAB3++;
				}
				if (wahl.abiturfach == 4) {
					statfw.wahlenAB4++;
				}
				if (wahl.abiturfach == 5) {
					statfw.wahlenAB5++;
				}
			}
		}
		return matrixFachwahlen.values().stream()
				.sorted((a, b) -> {
					final int cmp = GostFachbereich.compareFachByKuerzel(a.kuerzelStatistik, b.kuerzelStatistik);
					if (cmp != 0) {
						return cmp;
					}
					return Integer.compare(faecher.get(a.id).SortierungAllg, faecher.get(b.id).SortierungAllg);
				})
				.toList();
	}

}
