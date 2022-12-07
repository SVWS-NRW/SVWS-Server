package de.nrw.schule.svws.core.abschluss.gost;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.nrw.schule.svws.core.abschluss.gost.belegpruefung.AbiFaecher;
import de.nrw.schule.svws.core.abschluss.gost.belegpruefung.Allgemeines;
import de.nrw.schule.svws.core.abschluss.gost.belegpruefung.Deutsch;
import de.nrw.schule.svws.core.abschluss.gost.belegpruefung.Fremdsprachen;
import de.nrw.schule.svws.core.abschluss.gost.belegpruefung.GesellschaftswissenschaftenUndReligion;
import de.nrw.schule.svws.core.abschluss.gost.belegpruefung.KurszahlenUndWochenstunden;
import de.nrw.schule.svws.core.abschluss.gost.belegpruefung.Latinum;
import de.nrw.schule.svws.core.abschluss.gost.belegpruefung.LiterarischKuenstlerisch;
import de.nrw.schule.svws.core.abschluss.gost.belegpruefung.Mathematik;
import de.nrw.schule.svws.core.abschluss.gost.belegpruefung.Naturwissenschaften;
import de.nrw.schule.svws.core.abschluss.gost.belegpruefung.Projektkurse;
import de.nrw.schule.svws.core.abschluss.gost.belegpruefung.Schwerpunkt;
import de.nrw.schule.svws.core.abschluss.gost.belegpruefung.Sport;
import de.nrw.schule.svws.core.data.gost.AbiturFachbelegung;
import de.nrw.schule.svws.core.data.gost.AbiturFachbelegungHalbjahr;
import de.nrw.schule.svws.core.data.gost.Abiturdaten;
import de.nrw.schule.svws.core.data.gost.GostFach;
import de.nrw.schule.svws.core.data.schueler.Sprachendaten;
import de.nrw.schule.svws.core.types.fach.ZulaessigesFach;
import de.nrw.schule.svws.core.types.gost.GostAbiturFach;
import de.nrw.schule.svws.core.types.gost.GostBesondereLernleistung;
import de.nrw.schule.svws.core.types.gost.GostFachbereich;
import de.nrw.schule.svws.core.types.gost.GostHalbjahr;
import de.nrw.schule.svws.core.types.gost.GostKursart;
import de.nrw.schule.svws.core.types.gost.GostSchriftlichkeit;
import de.nrw.schule.svws.core.utils.schueler.SprachendatenUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt Methoden zur Verfügung um die angegebenen Abiturdaten zu 
 * bearbeiten und Auswertungen durchzuführen. 
 */
public class AbiturdatenManager {
	
	/** Das Abiturdaten-Objekt, welches mithilfe dieses Managers bearbeitet wird */
	private final @NotNull Abiturdaten abidaten;
	
	/** Eine Map mit der Zuordnung der zulässigen Fächer der gymnasialen Oberstufe für diesen Abiturjahrgang */ 
	private final @NotNull HashMap<@NotNull Long, @NotNull GostFach> gostFaecher;

	/** Die Art der durchzuführenden Belegprüfung */
	private final @NotNull GostBelegpruefungsArt pruefungsArt;
	
	/** Eine HashMap, welche den schnellen Zugriff auf die Fachbelegungen über den Fachbereich ermöglicht */
	private final @NotNull HashMap<@NotNull GostFachbereich, @NotNull Vector<@NotNull AbiturFachbelegung>> mapFachbereiche = new HashMap<>(); 

	
	/** Die Prüfungsergebnisse der einzelnen Teilprüfungen der Belegprüfung */
	private @NotNull Vector<@NotNull GostBelegpruefung> belegpruefungen = new Vector<>();
	
	/** Die Menge der Belegprüfungsfehler, die bei den durchgeführten Belegprüfungen aufgetreten sind. */
	private @NotNull Vector<@NotNull GostBelegungsfehler> belegpruefungsfehler = new Vector<>();
	
	/** Gibt an, ob die Belegprüfung insgesamt erfolgreich war oder nicht. */
	private boolean belegpruefungErfolgreich = false;



	/**
	 * Erstellt ein neues Manager-Objekt, welches mit den übergebenen Abiturdaten verknüpft wird.
	 * 
	 * @param abidaten       die Abiturdaten
	 * @param gostFaecher    die Fächer der Gymnasialen Oberstufe, die bei dem Abiturjahrgang zur Verfügung stehen. 
	 * @param pruefungsArt   die Art der Belegpruefung (z.B. EF1 oder GESAMT)
	 */
	public AbiturdatenManager(@NotNull Abiturdaten abidaten, final @NotNull List<@NotNull GostFach> gostFaecher, final @NotNull GostBelegpruefungsArt pruefungsArt) {
		this.abidaten = abidaten;
		this.gostFaecher = new HashMap<>();
		for (int i = 0; i < gostFaecher.size(); i++) {
			GostFach fach = gostFaecher.get(i);
			if (fach != null)
				this.gostFaecher.put(fach.id, fach);
		}
		this.pruefungsArt = pruefungsArt;
		init();
	}
	
	
	/**
	 * Führt die Belegprüfung der Art pruefungs_art für einen Schüler durch, dessen Abiturdaten mit dem angegebenen 
	 * Manager verwaltet werden.
	 * 
	 * @param pruefungs_art    die Art der Prüfung, die durchgeführt wird
	 * 
	 * @return eine Liste mit den durchgefuehrten Belegpruefungen
	 */
	public @NotNull Vector<@NotNull GostBelegpruefung> getPruefungen(final @NotNull GostBelegpruefungsArt pruefungs_art) {
		@NotNull Vector<@NotNull GostBelegpruefung> pruefungen = new Vector<>();
		pruefungen.add(new Deutsch(this, pruefungs_art));
		@NotNull Fremdsprachen pruefungFremdsprachen = new Fremdsprachen(this, pruefungs_art);
		pruefungen.add(pruefungFremdsprachen);
		pruefungen.add(new Latinum(this, pruefungs_art));
		pruefungen.add(new LiterarischKuenstlerisch(this, pruefungs_art));
		pruefungen.add(new GesellschaftswissenschaftenUndReligion(this, pruefungs_art));
		pruefungen.add(new Mathematik(this, pruefungs_art));
		@NotNull Naturwissenschaften pruefungNaturwissenschaften = new Naturwissenschaften(this, pruefungs_art);
		pruefungen.add(pruefungNaturwissenschaften);
		pruefungen.add(new Sport(this, pruefungs_art));
		@NotNull Projektkurse pruefungProjektkurse = new Projektkurse(this, pruefungs_art); 
		pruefungen.add(pruefungProjektkurse);
		// Die Prüfung zu dem Schwerpunkt muss nach den Prüfungen des naturwissenschaftlichen und der Fremdsprachen durchgeführt werden, da hier eine Abhängigkeit besteht.
		pruefungen.add(new Schwerpunkt(this, pruefungs_art, pruefungFremdsprachen, pruefungNaturwissenschaften));
		pruefungen.add(new AbiFaecher(this, pruefungs_art));
		// Die Prüfung der Kurszahlen und Wochenstunden ist abhängig von den Projektkursergebnissen - sie muss nach den Projektkursergebnissen durchgeführt werden!!!
		pruefungen.add(new KurszahlenUndWochenstunden(this, pruefungs_art, pruefungProjektkurse));
		pruefungen.add(new Allgemeines(this, pruefungs_art));
		return pruefungen;
	}
	
	
	/**
	 * Initialisiert bzw. reinitialisert die Datenstrukturen, die für den schnellen Zugriff auf die Daten
	 * eingerichtet werden. 
	 */
	public void init() {
		if (abidaten == null)
			return;
		initMapFachbereiche();
		belegpruefungen = getPruefungen(pruefungsArt);
		for (int i = 0; i < belegpruefungen.size(); i++) {
			@NotNull GostBelegpruefung belegpruefung = belegpruefungen.get(i);
			belegpruefung.pruefe();
		}
		belegpruefungsfehler = GostBelegpruefung.getBelegungsfehlerAlle(belegpruefungen);
		belegpruefungErfolgreich = GostBelegpruefung.istErfolgreich(belegpruefungsfehler);
	}

	
	
	/**
	 * Initialisiert bzw. reinitialisiert die Map für den schnellen Zugriff auf Fachbelegungen
	 * anhand des Fachbereichs. 
	 */
	private void initMapFachbereiche() {
		// Leere die HashMap und erstelle neue Vektoren für die Zuordnung von Abitur-Fachbelegungen 
		mapFachbereiche.clear();
		for (@NotNull GostFachbereich fachbereich : GostFachbereich.values())
			mapFachbereiche.put(fachbereich, new Vector<@NotNull AbiturFachbelegung>());

		// Durchwandere alle belegten Fächer und weise diese den Fachbereichen zu
		@NotNull Vector<@NotNull AbiturFachbelegung> fachbelegungen = abidaten.fachbelegungen;
		for (AbiturFachbelegung fachbelegung : fachbelegungen) {
			if (zaehleBelegung(fachbelegung) > 0) { // Filtere ggf. leere Belegungen
				GostFach fach = getFach(fachbelegung);
				@NotNull List<@NotNull GostFachbereich> fachbereiche = GostFachbereich.getBereiche(fach);
				for (@NotNull GostFachbereich fachbereich : fachbereiche) {
					Vector<@NotNull AbiturFachbelegung> listFachbelegungen = mapFachbereiche.get(fachbereich);
					if (listFachbelegungen == null)
						continue;
					listFachbelegungen.add(fachbelegung);
					// TODO erstelle ggf. extra Maps für die Halbjahre
				}
			}
		}
	}


    /**
     * Liefert die in den Abiturdaten enthaltenen Sprachendaten.
     *
     * @return Die Sprachendaten (siehe {@link Sprachendaten})
     */
    public Sprachendaten getSprachendaten() {
        return abidaten.sprachendaten;
    }
    
    
    /**
     * Berechnet die Wochenstunden, welche von dem Schüler in den einzelnen 
     * Halbjahren der gymnasialen Oberstufe für das Abitur relevant belegt wurden.
     * 
     * @return ein Array mit den Wochenstunden für die sechs Halbjahre 
     */
    public int[] getWochenstunden() {
		int[] stunden = new int[] {0, 0, 0, 0, 0, 0};
		for (int i = 0; i < 6; i++) {
			for (AbiturFachbelegung fb : abidaten.fachbelegungen) {
				AbiturFachbelegungHalbjahr hjb = fb.belegungen[i];
				if ((hjb == null) || ("AT".equals(hjb.kursartKuerzel)))
					continue;
				stunden[i] += hjb.wochenstunden;
			}
		}
		return stunden;
    }

    /**
     * Berechnet die Anzahl der anrechenbaren Kurse, welche von dem Schüler in den einzelnen 
     * Halbjahren der gymnasialen Oberstufe für das Abitur belegt wurden.
     * 
     * @return ein Array mit den anrechenbaren Kursen für die sechs Halbjahre 
     */
    public int[] getAnrechenbareKurse() {
		int[] anzahl = new int[] {0, 0, 0, 0, 0, 0};
		GostBesondereLernleistung bll = GostBesondereLernleistung.fromKuerzel(abidaten.besondereLernleistung);
		for (int i = 0; i < 6; i++) {
			for (AbiturFachbelegung fb : abidaten.fachbelegungen) {
				AbiturFachbelegungHalbjahr hjb = fb.belegungen[i];
				if ((hjb == null) || ("AT".equals(hjb.kursartKuerzel)))
					continue;
				GostKursart kursart = GostKursart.fromKuerzel(hjb.kursartKuerzel);
				if ((kursart != GostKursart.VTF) && (!((kursart == GostKursart.PJK) && (bll == GostBesondereLernleistung.PROJEKTKURS))))
					anzahl[i]++;
			}
		}
		return anzahl;
    }
    

    /**
	 * Liefert das Fach der gymnasialen Oberstufe für die angegeben Abiturfachbelegung.
	 * 
	 * @param belegung   die Fachbelegung (siehe {@link AbiturFachbelegung})
	 * 
	 * @return das Fach der gymnasialen Oberstufe (siehe {@link GostFach}) 
	 */
	public GostFach getFach(AbiturFachbelegung belegung) {
		if (belegung == null)
			return null;
		return gostFaecher.get(belegung.fachID);
	}
	
	
	/**
	 * Prüft, ob das Faches in allen angegebenen Halbjahren belegt wurde.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 * 
	 * @param fachbelegung      die zu prüfende Fachbelegung
	 * @param halbjahre         die zu prüfenden Halbjahre 
	 * 
	 * @return true, falls das Fach in den Halbjahren belegt wurde, sonst false 
	 */
	public boolean pruefeBelegung(AbiturFachbelegung fachbelegung, @NotNull GostHalbjahr... halbjahre) {
		if (fachbelegung == null)
			return false;
		if ((halbjahre == null) || (halbjahre.length == 0))
			return true;
		for (GostHalbjahr halbjahr : halbjahre) {
			AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr == null) || (belegungHalbjahr.kursartKuerzel == null))
				return false;
		}
		return true;
	}
	
	
	/**
	 * Bestimmt die Anzahl der Fachbelegungen, die dem Fach zugeordnet sind.
	 * Wird keine gültige Fachbelegung übergeben, so wird 0 zurückgegeben.
	 *  
	 * @param fachbelegung   die Fachbelegung 
	 * 
	 * @return die Anzahl der Belegungen des Faches
	 */
	public int zaehleBelegung(AbiturFachbelegung fachbelegung) {
		if (fachbelegung == null)
			return 0;
		int anzahl = 0;
		for (int i = 0; i < GostHalbjahr.maxHalbjahre; i++) {
			if (fachbelegung.belegungen[i] != null)
				anzahl++;
		}
		return anzahl;
	}
		
	
	/**
	 * Zählt die Anzahl der Belegungen für die angegebenen Fachbelegungen in den angegeben Halbjahren.
	 * Ist die Fachbelegung null, so wird 0 zurückgegeben. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so wird ebenfalls 0 zurückgegeben.
	 * 
	 * @param fachbelegungen      die Fachbelegungen
	 * @param halbjahre           die Halbjahre 
	 * 
	 * @return die Anzahl der Belegungen in den Halbjahren und den Fächern 
	 */
	public int zaehleBelegungInHalbjahren(List<@NotNull AbiturFachbelegung> fachbelegungen, @NotNull GostHalbjahr... halbjahre) {
		if (fachbelegungen == null)
			return 0;
		if ((halbjahre == null) || (halbjahre.length == 0))
			return 0;
		int anzahl = 0;
		for (@NotNull AbiturFachbelegung fachbelegung : fachbelegungen)
			for (@NotNull GostHalbjahr halbjahr : halbjahre)
				if (fachbelegung.belegungen[halbjahr.id] != null)
					anzahl++;
		return anzahl;		
	}

	
	/**
	 * Prüft, ob die Belegung des Faches in den angegebenen Halbjahren der angegebenen Kursart entspricht.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 * 
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param kursart           die zu prüfende Kursart 
	 * @param halbjahre         die zu prüfenden Halbjahre 
	 * 
	 * @return true, falls die Schriftlichkeit in den Halbjahren gegeben ist, sonst false 
	 */
	public boolean pruefeBelegungMitKursart(AbiturFachbelegung fachbelegung, @NotNull GostKursart kursart, @NotNull GostHalbjahr... halbjahre) {
		if (fachbelegung == null)
			return false;
		if ((halbjahre == null) || (halbjahre.length == 0))
			return true;
		for (GostHalbjahr halbjahr : halbjahre) {
			AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr == null) || (kursart != GostKursart.fromKuerzel(belegungHalbjahr.kursartKuerzel)))
				return false;
		}
		return true;
	}

	
	/**
	 * Prüft, ob eine Fachbelegung existiert, welche in den angegebenen Halbjahren der angegebenen Kursart 
	 * entspricht.
	 * Ist keine Fachbelegung angegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen 
	 * Fachbelegung kein Halbjahr angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 * 
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param kursart           die zu prüfende Kursart 
	 * @param halbjahre         die zu prüfenden Halbjahre 
	 * 
	 * @return true, falls die Schriftlichkeit in den Halbjahren gegeben ist, sonst false 
	 */
	public boolean pruefeBelegungExistiertMitKursart(List<@NotNull AbiturFachbelegung> fachbelegungen, @NotNull GostKursart kursart, @NotNull GostHalbjahr... halbjahre) {
		if ((fachbelegungen == null) || (fachbelegungen.size() <= 0))
			return false;
		if ((halbjahre == null) || (halbjahre.length == 0))
			return true;
		for (AbiturFachbelegung fachbelegung : fachbelegungen) {
			if (pruefeBelegungMitKursart(fachbelegung, kursart, halbjahre))
				return true;
		}
		return false;
	}

	
	/**
	 * Prüft, ob die Belegung des Faches in den angegebenen Halbjahren mindestens einmal die angegebenen Kursart hat.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung nicht erfolgreich, da kein Halbjahr geprüft werden muss und somit die Kursart nicht
	 * einmal existiert.
	 * 
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param kursart           die zu prüfende Kursart 
	 * @param halbjahre         die zu prüfenden Halbjahre 
	 * 
	 * @return true, falls die Kursart mindestens einmal in den Halbjahren gegeben ist, sonst false 
	 */
	public boolean pruefeBelegungHatMindestensEinmalKursart(AbiturFachbelegung fachbelegung, @NotNull GostKursart kursart, @NotNull GostHalbjahr... halbjahre) {
		if (fachbelegung == null)
			return false;
		if ((halbjahre == null) || (halbjahre.length == 0))
			return false;
		for (GostHalbjahr halbjahr : halbjahre) {
			AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
			if (belegungHalbjahr == null)
				continue;
			if (kursart == GostKursart.fromKuerzel(belegungHalbjahr.kursartKuerzel))
				return true;
		}
		return false;
	}


	
	/**
	 * Prüft, ob die Belegung des Faches in dem angegebenen Halbjahr der angegebenen Schriftlichkeit entspricht.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. 
	 * 
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit 
	 * @param halbjahr          das zu prüfende Halbjahr 
	 * 
	 * @return true, falls die Schriftlichkeit in dem Halbjahr gegeben ist, sonst false 
	 */
	public boolean pruefeBelegungMitSchriftlichkeitEinzeln(AbiturFachbelegung fachbelegung, @NotNull GostSchriftlichkeit schriftlichkeit, @NotNull GostHalbjahr halbjahr) {
		if (fachbelegung == null)
			return false;
		AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
		if ((belegungHalbjahr == null) || (belegungHalbjahr.schriftlich == null) || 
				((schriftlichkeit != GostSchriftlichkeit.BELIEBIG) &&
				(((schriftlichkeit == GostSchriftlichkeit.SCHRIFTLICH) && (!belegungHalbjahr.schriftlich)) ||
				 ((schriftlichkeit == GostSchriftlichkeit.MUENDLICH) && (belegungHalbjahr.schriftlich)))))
			return false;
		return true;
	}
	
	
	/**
	 * Prüft, ob die Belegung des Faches in den angegebenen Halbjahren der angegebenen Schriftlichkeit entspricht.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 * 
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit 
	 * @param halbjahre         die zu prüfenden Halbjahre 
	 * 
	 * @return true, falls die Schriftlichkeit in den Halbjahren gegeben ist, sonst false 
	 */
	public boolean pruefeBelegungMitSchriftlichkeit(AbiturFachbelegung fachbelegung, @NotNull GostSchriftlichkeit schriftlichkeit, @NotNull GostHalbjahr... halbjahre) {
		if (fachbelegung == null)
			return false;
		if ((halbjahre == null) || (halbjahre.length == 0))
			return true;
		for (GostHalbjahr halbjahr : halbjahre)
			if (!pruefeBelegungMitSchriftlichkeitEinzeln(fachbelegung, schriftlichkeit, halbjahr))
				return false;
		return true;
	}

	
	/**
	 * Prüft, ob eine Belegung des Faches in den angegebenen Halbjahren nicht der angegebenen Schriftlichkeit entspricht.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 * 
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit 
	 * @param halbjahre         die zu prüfenden Halbjahre 
	 * 
	 * @return true, falls die Schriftlichkeit in den Halbjahren nicht gegeben ist, sonst false 
	 */
	public boolean pruefeBelegungErfuelltNicht(AbiturFachbelegung fachbelegung, @NotNull GostSchriftlichkeit schriftlichkeit, @NotNull GostHalbjahr... halbjahre) {
		if (fachbelegung == null)
			return false;
		if ((halbjahre == null) || (halbjahre.length == 0))
			return true;
		for (GostHalbjahr halbjahr : halbjahre) {
			AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
			if ((belegungHalbjahr == null) || ((schriftlichkeit != GostSchriftlichkeit.BELIEBIG) &&
						(((schriftlichkeit == GostSchriftlichkeit.SCHRIFTLICH) && (!belegungHalbjahr.schriftlich)) ||
						 ((schriftlichkeit == GostSchriftlichkeit.MUENDLICH) && (belegungHalbjahr.schriftlich)))))
				return true;
		}
		return false;
	}

	
	/**
	 * Prüft, ob eine Belegung des Faches in den angegebenen Halbjahren nicht der angegebenen Schriftlichkeit entspricht,
	 * sofern es in dem Halbjahr überhaupt belegt wurde..
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 * 
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit 
	 * @param halbjahre         die zu prüfenden Halbjahre 
	 * 
	 * @return true, falls die Schriftlichkeit in den Halbjahren nicht gegeben ist, sonst false 
	 */
	public boolean pruefeBelegungErfuelltNichtFallsBelegt(AbiturFachbelegung fachbelegung, @NotNull GostSchriftlichkeit schriftlichkeit, @NotNull GostHalbjahr... halbjahre) {
		if (fachbelegung == null)
			return false;
		if ((halbjahre == null) || (halbjahre.length == 0))
			return true;
		for (GostHalbjahr halbjahr : halbjahre) {
			AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
			if (belegungHalbjahr == null)
				continue;
			@NotNull Boolean schriftlich = belegungHalbjahr.schriftlich == null ? false : belegungHalbjahr.schriftlich;
			if (((schriftlichkeit != GostSchriftlichkeit.BELIEBIG) &&
						(((schriftlichkeit == GostSchriftlichkeit.SCHRIFTLICH) && (!schriftlich)) ||
						 ((schriftlichkeit == GostSchriftlichkeit.MUENDLICH) && (schriftlich)))))
				return true;
		}
		return false;
	}

	
	/**
	 * Prüft, ob die Belegung des Faches in den angegebenen Halbjahren mindestens einmal die angegebene Schritflichkeit hat.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung nicht erfolgreich, da kein Halbjahr geprüft werden muss und somit die Schriftlichkeit nicht
	 * einmal existiert.
	 * 
	 * @param fachbelegung      die zu prüfende Fachbelegung
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit 
	 * @param halbjahre         die zu prüfenden Halbjahre 
	 * 
	 * @return true, falls die angegebene Schriftlichkeit mindestens einmal in den Halbjahren gegeben ist, sonst false 
	 */
	public boolean pruefeBelegungHatMindestensEinmalSchriftlichkeit(AbiturFachbelegung fachbelegung, @NotNull GostSchriftlichkeit schriftlichkeit, @NotNull GostHalbjahr... halbjahre) {
		if (fachbelegung == null)
			return false;
		if ((halbjahre == null) || (halbjahre.length == 0))
			return false;
		for (GostHalbjahr halbjahr : halbjahre) {
			AbiturFachbelegungHalbjahr belegungHalbjahr = fachbelegung.belegungen[halbjahr.id];
			if (belegungHalbjahr == null)
				continue;
			if ((schriftlichkeit.istSchriftlich == null) || (schriftlichkeit.istSchriftlich == belegungHalbjahr.schriftlich))
				return true;
		}
		return false;
	}

	
	
	
	/**
	 * Prüft, ob eine Fachbelegung existiert, welche in den angegebenen Halbjahren mindestens einmal die angegebene 
	 * Schritflichkeit hat.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung nicht erfolgreich, da kein Halbjahr geprüft werden muss und somit die Schriftlichkeit nicht
	 * einmal existiert.
	 * 
	 * @param fachbelegungen    die zu prüfenden Fachbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit 
	 * @param halbjahre         die zu prüfenden Halbjahre 
	 * 
	 * @return true, falls die angegebene Schriftlichkeit bei einer Fachbelegung mindestens einmal in den Halbjahren gegeben ist, sonst false 
	 */
	public boolean pruefeBelegungExistiertHatMindestensEinmalSchriftlichkeit(List<@NotNull AbiturFachbelegung> fachbelegungen, @NotNull GostSchriftlichkeit schriftlichkeit, @NotNull GostHalbjahr... halbjahre) {
		if ((fachbelegungen == null) || (fachbelegungen.size() <= 0))
			return false;
		if ((halbjahre == null) || (halbjahre.length == 0))
			return false;
		for (AbiturFachbelegung fachbelegung : fachbelegungen)
			if (pruefeBelegungHatMindestensEinmalSchriftlichkeit(fachbelegung, schriftlichkeit, halbjahre))
				return true;
		return false;
	}

	
	
	
	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer mit den angegebenen Halbjahren existiert.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 * In dieser Methode wird ggf. auch geprüft, ob weitere Fachbelegungen existieren, welche das gleiche 
	 * Statistik-Kürzel haben und Ersatzweise eine Halbjahres-Belegung ersetzen können. Dies ist z.B. bei bilingualen
	 * Fächern nötig oder bei der Unterscheidung von Sport-Profilen.
	 * 
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param halbjahre         die zu prüfenden Halbjahre 
	 * 
	 * @return true, falls eine Fachbelegung mit den Halbjahren existiert, sonst false 
	 */
	public boolean pruefeBelegungExistiert(List<@NotNull AbiturFachbelegung> fachbelegungen, @NotNull GostHalbjahr... halbjahre) {
		if (fachbelegungen == null)
			return false;
		if ((halbjahre == null) || (halbjahre.length == 0))
			return true;
		for (AbiturFachbelegung fachbelegung : fachbelegungen) {
			// Beachte alle Fachbelegungen von Fächern des gleichen Statistik-Faches - dies kann bei bilingualen Fächern wichtig sein
			GostFach fach = gostFaecher.get(fachbelegung.fachID);
			if (fach == null)
				continue;
			List<@NotNull AbiturFachbelegung> alleBelegungen = getFachbelegungByFachkuerzel(fach.kuerzel);
			if ((alleBelegungen == null) || (alleBelegungen.size() == 0))
				continue;
			boolean hatBelegung = true;
			for (GostHalbjahr halbjahr : halbjahre) {
				boolean hatHalbjahresBelegung = false;
				for (AbiturFachbelegung aktFachbelegung : alleBelegungen) {
					if (aktFachbelegung.belegungen[halbjahr.id] != null) {
						hatHalbjahresBelegung = true;
						break;
					}
				}
				if (!hatHalbjahresBelegung) {
					hatBelegung = false;
					break;
				}
			}
			if (hatBelegung)
				return true;
		}
		return false;
	}

	
	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer mit dem angegebenen Halbjahr existiert.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. 
	 * In dieser Methode wird ggf. auch geprüft, ob weitere Fachbelegungen existieren, welche das gleiche 
	 * Statistik-Kürzel haben und Ersatzweise eine Halbjahres-Belegung ersetzen können. Dies ist z.B. bei bilingualen
	 * Fächern nötig oder bei der Unterscheidung von Sport-Profilen.
	 * 
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param halbjahr          das zu prüfende Halbjahr 
	 * 
	 * @return true, falls eine Fachbelegung mit dem Halbjahr existiert, sonst false 
	 */
	public boolean pruefeBelegungExistiertEinzeln(List<@NotNull AbiturFachbelegung> fachbelegungen, @NotNull GostHalbjahr halbjahr) {
		if (fachbelegungen == null)
			return false;
		for (AbiturFachbelegung fachbelegung : fachbelegungen) {
			// Beachte alle Fachbelegungen von Fächern des gleichen Statistik-Faches - dies kann bei bilingualen Fächern wichtig sein
			GostFach fach = gostFaecher.get(fachbelegung.fachID);
			if (fach == null)
				continue;
			List<@NotNull AbiturFachbelegung> alleBelegungen = getFachbelegungByFachkuerzel(fach.kuerzel);
			if ((alleBelegungen == null) || (alleBelegungen.size() == 0))
				continue;
			for (AbiturFachbelegung aktFachbelegung : alleBelegungen)
				if (aktFachbelegung.belegungen[halbjahr.id] != null)
					return true;
		}
		return false;
	}
	
	
	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer mit einer durchgängigen Belegung existiert,
	 * die zumindest in der Q1 und der Q2.1 schriftlich ist.
	 * In dieser Methode wird ggf. auch geprüft, ob weitere Fachbelegungen existieren, welche das gleiche 
	 * Statistik-Kürzel haben und Ersatzweise eine Halbjahres-Belegung ersetzen können. Dies ist bei bilingualen
	 * Fächern nötig.   
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl.
	 * 
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * 
	 * @return true, falls eine durchgehend schriftliche Fachbelegung existiert, sonst false 
	 */
	public boolean pruefeBelegungExistiertDurchgehendSchriftlich(List<@NotNull AbiturFachbelegung> fachbelegungen) {
		if (fachbelegungen == null)
			return false;
		for (AbiturFachbelegung fachbelegung : fachbelegungen) {
			// Beachte alle Fachbelegungen von Fächern des gleichen Statistik-Faches - dies kann bei bilingualen Fächern wichtig sein
			GostFach fach = gostFaecher.get(fachbelegung.fachID);
			if (fach == null)
				continue;
			List<@NotNull AbiturFachbelegung> alleBelegungen = getFachbelegungByFachkuerzel(fach.kuerzel);
			if ((alleBelegungen == null) || (alleBelegungen.size() == 0))
				continue;
			boolean hatBelegung = true;
			for (GostHalbjahr halbjahr : GostHalbjahr.values()) {
				boolean hatHalbjahresBelegung = false;
				for (AbiturFachbelegung aktFachbelegung : alleBelegungen) {
					if (aktFachbelegung.belegungen[halbjahr.id] != null) {
						AbiturFachbelegungHalbjahr belegungHalbjahr = aktFachbelegung.belegungen[halbjahr.id];
						if (((halbjahr != GostHalbjahr.Q11) && (halbjahr != GostHalbjahr.Q12) && (halbjahr != GostHalbjahr.Q21)) ||
							((belegungHalbjahr != null) && (belegungHalbjahr.schriftlich != null) && (belegungHalbjahr.schriftlich)))
						hatHalbjahresBelegung = true;
						// "break;" wird hier nicht verwendet, da bei der fehlerhaften Belegung von inhaltsgleichen Fächern (gleiches Statistik-Kürzel) im selben Halbjahr diese Prüfung dennoch korrekt ablaufen sollte   
					}
				}
				if (!hatHalbjahresBelegung) {
					hatBelegung = false;
					break;
				}
			}
			if (hatBelegung)
				return true;
		}
		return false;
	}

	
	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in dem angegebenen Halbjahr der angegebenen Schriftlichkeit entspricht.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. 
	 * 
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit 
	 * @param halbjahr          das zu prüfende Halbjahr 
	 * 
	 * @return true, falls bei einer Fachbelegung die Schriftlichkeit in dem Halbjahr gegeben ist, sonst false 
	 */
	public boolean pruefeBelegungExistiertMitSchriftlichkeitEinzeln(List<AbiturFachbelegung> fachbelegungen, @NotNull GostSchriftlichkeit schriftlichkeit, @NotNull GostHalbjahr halbjahr) {
		if (fachbelegungen == null)
			return false;
		for (AbiturFachbelegung fachbelegung : fachbelegungen) {
			if (pruefeBelegungMitSchriftlichkeitEinzeln(fachbelegung, schriftlichkeit, halbjahr))
				return true;
		}
		return false;
	}


	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in den angegebenen Halbjahren der angegebenen Schriftlichkeit entspricht.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 * 
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit 
	 * @param halbjahre         die zu prüfenden Halbjahre 
	 * 
	 * @return true, falls bei einer Fachbelegung die Schriftlichkeit in den Halbjahren gegeben ist, sonst false 
	 */
	public boolean pruefeBelegungExistiertMitSchriftlichkeit(List<@NotNull AbiturFachbelegung> fachbelegungen, @NotNull GostSchriftlichkeit schriftlichkeit, GostHalbjahr... halbjahre) {
		if (fachbelegungen == null)
			return false;
		for (AbiturFachbelegung fachbelegung : fachbelegungen) {
			if (pruefeBelegungMitSchriftlichkeit(fachbelegung, schriftlichkeit, halbjahre))
				return true;
		}
		return false;
	}


	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in den angegebenen Halbjahren mindestens einmal die angegebene Kursart 
	 * hat. Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 * 
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param kursart           die zu prüfende Kursart 
	 * @param halbjahre         die zu prüfenden Halbjahre 
	 * 
	 * @return true, falls die Kursart bei einer Fachbelegung mindestens einmal in den Halbjahren gegeben ist, sonst false 
	 */
	public boolean pruefeBelegungExistiertHatMindestensEinmalKursart(List<@NotNull AbiturFachbelegung> fachbelegungen, @NotNull GostKursart kursart, GostHalbjahr... halbjahre) {
		if (fachbelegungen == null)
			return false;
		for (AbiturFachbelegung fachbelegung : fachbelegungen) {
			if (pruefeBelegungHatMindestensEinmalKursart(fachbelegung, kursart, halbjahre))
				return true;
		}
		return false;
	}


	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in den angegebenen Halbjahren existiert,
	 * bei welchem in mind. einem der Halbjahren die angebene Schriftlichkeit nicht gegeben ist.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 * 
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit 
	 * @param halbjahre         die zu prüfenden Halbjahre 
	 * 
	 * @return true oder false (siehe oben) 
	 */
	public boolean pruefeBelegungExistiertErfuelltNicht(List<@NotNull AbiturFachbelegung> fachbelegungen, @NotNull GostSchriftlichkeit schriftlichkeit, GostHalbjahr... halbjahre) {
		if (fachbelegungen == null)
			return false;
		for (AbiturFachbelegung fachbelegung : fachbelegungen) {
			if (pruefeBelegungErfuelltNicht(fachbelegung, schriftlichkeit, halbjahre))
				return true;
		}
		return false;
	}

	
	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in den angegebenen Halbjahren existiert,
	 * bei welchem in mind. einem der Halbjahren die angebene Schriftlichkeit nicht gegeben ist, sofern
	 * das Fach überhaupt belegt wurde.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 * 
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit 
	 * @param halbjahre         die zu prüfenden Halbjahre 
	 * 
	 * @return true oder false (siehe oben) 
	 */
	public boolean pruefeBelegungExistiertErfuelltNichtFallsBelegt(List<@NotNull AbiturFachbelegung> fachbelegungen, @NotNull GostSchriftlichkeit schriftlichkeit, GostHalbjahr... halbjahre) {
		if (fachbelegungen == null)
			return false;
		for (AbiturFachbelegung fachbelegung : fachbelegungen) {
			if (pruefeBelegungErfuelltNichtFallsBelegt(fachbelegung, schriftlichkeit, halbjahre))
				return true;
		}
		return false;
	}
	
	
	
	/**
	 * Prüft, ob die Belegung des Faches in den angegebenen Halbjahren der angegebenen Schriftlichkeit entspricht
	 * und das Fach durchgehend belegbar ist.
	 * Ist die Fachbelegung null, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 * 
	 * @param fachbelegung      die zu prüfende Fachnbelegung
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit 
	 * @param halbjahre         die zu prüfenden Halbjahre 
	 * 
	 * @return true, falls die Schriftlichkeit in den Halbjahren gegeben ist, sonst false 
	 */
	public boolean pruefeBelegungDurchgehendBelegbar(AbiturFachbelegung fachbelegung, @NotNull GostSchriftlichkeit schriftlichkeit, GostHalbjahr... halbjahre) {
		if (fachbelegung == null)
			return false;
		if (!GostFachManager.istDurchgehendBelegbarBisQ22(getFach(fachbelegung)))
			return false;
		return pruefeBelegungMitSchriftlichkeit(fachbelegung, schriftlichkeit, halbjahre); 
	}

	
	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in den angegebenen Halbjahren der angegebenen Schriftlichkeit entspricht
	 * und das Fach durchgängig belegbar ist.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich, da kein Halbjahr geprüft werden muss.
	 * 
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit 
	 * @param halbjahre         die zu prüfenden Halbjahre 
	 * 
	 * @return true, falls bei einer Fachbelegung die Schriftlichkeit in den Halbjahren gegeben ist, sonst false 
	 */
	public boolean pruefeBelegungDurchgehendBelegbarExistiert(List<@NotNull AbiturFachbelegung> fachbelegungen, @NotNull GostSchriftlichkeit schriftlichkeit, GostHalbjahr... halbjahre) {
		if (fachbelegungen == null)
			return false;
		for (AbiturFachbelegung fachbelegung : fachbelegungen) {
			if (pruefeBelegungDurchgehendBelegbar(fachbelegung, schriftlichkeit, halbjahre))
				return true;
		}
		return false;		
	}

	
	
	/**
	 * Prüft, ob die Belegung eines der angegebenen Fächer in den angegebenen Halbjahren der angegebenen Schriftlichkeit entspricht
	 * und das Fach durchgängig belegt ist.
	 * Ist keine Fachbelegung gegeben, so schlägt die Prüfung fehl. Wird bei einer gültigen Fachbelegung kein Halbjahr
	 * angegeben, so ist die Prüfung erfolgreich sofern das Fach durchgängig belegt wurde, da kein Halbjahr auf die 
	 * Schriftlichkeit geprüft werden muss.
	 * 
	 * @param fachbelegungen    die zu prüfenden Fachnbelegungen
	 * @param schriftlichkeit   die zu prüfende Schriftlichkeit 
	 * @param halbjahre         die zu prüfenden Halbjahre 
	 * 
	 * @return true, falls bei eine Fachbelegung durchgängig belegt wurde und die Schriftlichkeit in den Halbjahren gegeben ist, sonst false 
	 */
	public boolean pruefeBelegungDurchgehendBelegtExistiert(List<@NotNull AbiturFachbelegung> fachbelegungen, @NotNull GostSchriftlichkeit schriftlichkeit, GostHalbjahr... halbjahre) {
		if (fachbelegungen == null)
			return false;
		for (AbiturFachbelegung fachbelegung : fachbelegungen) {
			if (pruefeBelegung(fachbelegung, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22))
				if (pruefeBelegungMitSchriftlichkeit(fachbelegung, schriftlichkeit, halbjahre))
					return true;
		}
		return false;		
	}
	
	
	
	
	/**
	 * Prüft, ob die Fachbelegung in mindestens einem der Halbjahre die angegebene Kursart aufweist.
	 * Existiert die Fachbelegung nicht (null), so kommt die Kursart auch nicht vor.
	 *  
	 * @param fachbelegung   die Fachbelegung
	 * @param kursart        die Kursart
	 * 
	 * @return true, falls mindestens einmal die Kursart belegt wurde, sonst false
	 */
	public boolean pruefeAufKursart(AbiturFachbelegung fachbelegung, @NotNull GostKursart kursart) {
		if (fachbelegung == null)
			return false;
		for (AbiturFachbelegungHalbjahr belegunghalbjahr : fachbelegung.belegungen) {
			if ((belegunghalbjahr != null) && GostKursart.fromKuerzel(belegunghalbjahr.kursartKuerzel) == kursart)
				return true;
		}
		return false;
	}
	
	
	/**
	 * Filtert die Fachbelegungen und gibt nur die Fachbelegungen zurück, bei denen die 
	 * Kursart existiert.
	 * Wird keine Fachbelegung übergeben (null oder leere Liste), so kommt auch keine 
	 * Belegung mit der Kursart vor.
	 *  
	 * @param fachbelegungen   die Fachbelegungen
	 * @param kursart          die Kursart
	 * 
	 * @return eine Liste mit den Fachbelegungen, welche die kursart haben
	 */
	public @NotNull List<@NotNull AbiturFachbelegung> filterBelegungKursartExistiert(List<@NotNull AbiturFachbelegung> fachbelegungen, @NotNull GostKursart kursart) {
		@NotNull Vector<@NotNull AbiturFachbelegung> result = new Vector<>();
		if ((fachbelegungen == null) || (fachbelegungen.size() <= 0))
			return result;
		for (AbiturFachbelegung fachbelegung : fachbelegungen) {
			if (pruefeAufKursart(fachbelegung, kursart))
				result.add(fachbelegung);
		}
		return result;				
	}
	
	
	/**
	 * Prüft, ob die Fachbelegung eine durchgängige Belegung hat. Zusatzkurse können nicht für eine
	 * durchgängige Belegung zählen.
	 * 
	 * @param fachbelegung   die zu prüfende Fachbelegung
	 * 
	 * @return true, wenn die Belegung durchgängig ist.
	 */
	public boolean pruefeDurchgaengigkeit(AbiturFachbelegung fachbelegung) {
		if ((fachbelegung == null) || (pruefeAufKursart(fachbelegung, GostKursart.ZK)))
			return false;
		return pruefeBelegung(fachbelegung, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22);
	}
	
	
	/**
	 * Zählt die Fachbelegungen, welche eine durchgängige Belegung aufweisen. Zusatzkurse zählen
	 * nicht für eine durchgängige Belegung.
	 * In dieser Methode wird ggf. auch geprüft, ob weitere Fachbelegungen existieren, welche das gleiche 
	 * Statistik-Kürzel haben und Ersatzweise eine Halbjahres-Belegung ersetzen können. Dies ist bei bilingualen
	 * Fächern nötig.   
	 * 
	 * @param fachbelegungen   die zu überprüfenden Fachbelegungen
	 * 
	 * @return die Anzahl der durchgängigen Belegungen
	 */	
	public int zaehleDurchgaengigeBelegungen(List<@NotNull AbiturFachbelegung> fachbelegungen) {
		if (fachbelegungen == null)
			return 0;
		int anzahl = 0;
		for (AbiturFachbelegung fachbelegung : fachbelegungen) {
			GostFach fach = gostFaecher.get(fachbelegung.fachID);
			if (fach == null)
				continue;
			if (fachbelegung.belegungen[GostHalbjahr.EF1.id] == null)
				continue;
			List<@NotNull AbiturFachbelegung> alleBelegungen = getFachbelegungByFachkuerzel(fach.kuerzel);
			if ((alleBelegungen == null) || (alleBelegungen.size() == 0))
				continue;
			boolean hatBelegung = true;
			// Beachte zur Fortsetzung alle Fachbelegungen von Fächern des gleichen Statistik-Faches - dies kann bei bilingualen Fächern wichtig sein
			@NotNull GostHalbjahr@NotNull[] halbjahre = { GostHalbjahr.EF1, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22 };
			for (GostHalbjahr halbjahr : halbjahre) {
				boolean hatHalbjahresBelegung = false;
				for (AbiturFachbelegung aktFachbelegung : alleBelegungen) {
					if (aktFachbelegung.belegungen[halbjahr.id] != null) {
						hatHalbjahresBelegung = true;
						break;
					}
				}
				if (!hatHalbjahresBelegung) {
					hatBelegung = false;
					break;
				}
			}
			if (hatBelegung)
				anzahl++;
		}		
		return anzahl;
	}

	
	/**
	 * Prüft, ob die Fachbelegung eine durchgängige Belegung hat und prüft die Schriftlichkeit
	 * in der Qualifikationsphase. Ein Fach in der Qualifikationsphase gilt als Schriftlich belegt,
	 * sofern die ersten 3 Halbjahre der Qualifikationsphase schriftlich belegt wurden. 
	 * - Zusatzkurse können nicht für eine durchgängige Belegung zählen.
	 * 
	 * @param fachbelegung   die zu prüfende die zu überprüfenden Fachbelegung
	 * 
	 * @return true, wenn die Belegung durchgängig ist und die Schriftlichkeit den Anforderungen genügt.
	 */
	public boolean pruefeDurchgaengigkeitSchriftlich(AbiturFachbelegung fachbelegung) {
		if (!pruefeDurchgaengigkeit(fachbelegung))
			return false;
		return pruefeBelegungMitSchriftlichkeit(fachbelegung, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21 );
	}
	
	
	/**
	 * Prüft, ob unter den angegebenen Fachbelegungen ein Fach als Abiturfach von einem der angegebenen Arten 
	 * gewählt wurde. Wird keine Art angebeben, so wird jede Fachbelegung akzeptiert und true zurückgegeben.
	 * 
	 * @param fachbelegungen   die Fachbelegungen
	 * @param arten            die Arten der Abiturfächer
	 *  
	 * @return true, falls unter den Fachbelegungen mindestens ein Fach als Abiturfach von einem der 
	 *         angegebenen Arten gewählt wurde und false sonst
	 */
	public boolean pruefeExistiertAbiFach(List<@NotNull AbiturFachbelegung> fachbelegungen, GostAbiturFach... arten) {
		if ((arten == null) || (arten.length == 0))
			return true;
		if (fachbelegungen == null)
			return false;
		for (AbiturFachbelegung fachbelegung : fachbelegungen)
			for (GostAbiturFach art : arten) {
				GostAbiturFach abiturFach = GostAbiturFach.fromID(fachbelegung.abiturFach);
				if (abiturFach == art)
					return true;
			}
		return false;
	}
	

	
	/**
	 * Prüft anhand des Statistik-Kürzels, ob in dem angegebenen Halbjahr eine doppelte Fachbelegung 
	 * vorliegt oder nicht. Bei den Fremdsprachen werden nur unterschiedliche Fremdsprachen in einem Halbjahr 
	 * akzeptiert und es dürfen mehrere Vertiefungsfächer (VX) in einem Halbjahr vorkommen.   
	 * 
	 * @param halbjahr   das zu prüfende Halbjahr
	 * 
	 * @return true, falls eine doppelte Belegung vorliegt, sonst false
	 */
	public boolean hatDoppelteFachbelegungInHalbjahr(@NotNull GostHalbjahr halbjahr) {
		@NotNull HashSet<@NotNull String> set = new HashSet<>();
		@NotNull Vector<@NotNull AbiturFachbelegung> fachbelegungen = abidaten.fachbelegungen;
		for (AbiturFachbelegung fb : fachbelegungen) {
			GostFach fach = getFach(fb);
			if (fach == null)
				continue;
			AbiturFachbelegungHalbjahr belegung = getBelegungHalbjahr(fb, halbjahr, GostSchriftlichkeit.BELIEBIG);
			if (belegung == null)
				continue;
			String kuerzel = GostFachManager.getFremdsprache(fach);
			if (kuerzel == null)
				kuerzel = fach.kuerzel == null ? "" : fach.kuerzel;
			if (!set.add(kuerzel) && (!"VX".equals(kuerzel)))
				return true;
		}
		return false;
	}
	
	
	/**
	 * Prüft anhand des Statistik-Kürzels, ob in einem der angegebenen Halbjahre eine doppelte Fachbelegung 
	 * vorliegt oder nicht. Bei den Fremdsprachen werden nur unterschiedliche Fremdsprachen in einem Halbjahr 
	 * akzeptiert und es dürfen mehrere Vertiefungsfächer (VX) in einem Halbjahr vorkommen.   
	 * 
	 * @param halbjahre   die zu prüfenden Halbjahre
	 * 
	 * @return true, falls eine doppelte Belegung vorliegt, sonst false
	 */
	public boolean hatDoppelteFachbelegung(@NotNull GostHalbjahr... halbjahre) {
		if ((halbjahre == null) || (halbjahre.length == 0))
			return false;
		for (GostHalbjahr halbjahr : halbjahre)
			if (hatDoppelteFachbelegungInHalbjahr(halbjahr))
				return true;
		return false;
	}
	
	
	/**
	 * Gibt zurück, ob der Projektkurs als besondere Lernleistung verwendet wird.
	 * 
	 * @return true, falls der Projektkurs als besondere Lernleistung verwendet wird
	 */
	public boolean istProjektKursBesondereLernleistung() {
		return (GostBesondereLernleistung.PROJEKTKURS.is(abidaten.besondereLernleistung));
	}
	
	
	
	/**
	 * Bestimmt die erste Fachbelegung mit dem angegebenen Statistik-Kürzel
	 * 
	 * @param kuerzel          das Kürzel des Faches, kann null sein (dann wird auch null zurückgegeben)
	 * 
	 * @return die Fachbelegung oder null, falls keine vorhanden ist
	 */
	public AbiturFachbelegung getFachbelegungByKuerzel(String kuerzel) {
		if ((kuerzel == null) || ("".equals(kuerzel)))
			return null;
		@NotNull Vector<@NotNull AbiturFachbelegung> fachbelegungen = abidaten.fachbelegungen;
		for (AbiturFachbelegung fb : fachbelegungen) {
			GostFach fach = getFach(fb);
			if ((fach != null) && (kuerzel.equals(fach.kuerzel)))
				return fb;
		}
		return null;
	}
	
	
	/**
	 * Liefert alle Fachbelegungen der Abiturdaten, welche den angegebenen Fachbereichen zuzuordnen sind.
	 * Wird kein Fachbereich angegeben, so werden alle Fachbelegungen der Abiturdaten zurückgegeben.
	 * 
	 * @param fachbereiche   die Fachbereiche 
	 * 
	 * @return eine Liste der Fachbelegungen aus den Fachbereichen
	 */
	public @NotNull List<@NotNull AbiturFachbelegung> getFachbelegungen(GostFachbereich... fachbereiche) {
		if ((fachbereiche == null) || (fachbereiche.length == 0))
			return abidaten.fachbelegungen;
		@NotNull Vector<@NotNull AbiturFachbelegung> result = new Vector<>();
		for (GostFachbereich fachbereich : fachbereiche) {
			List<@NotNull AbiturFachbelegung> fachbelegungen = mapFachbereiche.get(fachbereich);
			if (fachbelegungen == null)
				continue;
			result.addAll(fachbelegungen);
		}
		return result;
	}
	
	
	/**
	 * Liefert alle Fachbelegungen, die bilingual unterrichtet wurden.
	 * 
	 * @return eine Liste der Fachbelegungen
	 */
	public @NotNull List<@NotNull AbiturFachbelegung> getFachbelegungenBilingual() {
		@NotNull Vector<@NotNull AbiturFachbelegung> result = new Vector<>();
		@NotNull Vector<@NotNull AbiturFachbelegung> fachbelegungen = abidaten.fachbelegungen;
		for (AbiturFachbelegung fb : fachbelegungen) {
			if (zaehleBelegung(fb) <= 0)
				continue;
			GostFach fach = getFach(fb);
			if ((fach != null) && (!GostFachbereich.FREMDSPRACHE.hat(fach)) && (!GostFachbereich.DEUTSCH.hat(fach))
					&& (fach.biliSprache != null) && (!"D".equals(fach.biliSprache)))
				result.add(fb);
		}
		return result;		
	}

	

	/**
	 * Filtert die Fachbelegungen auf neu einsetzende Fremdsprachen.
	 * 
	 * @param fachbelegungen   die zu filternden Fachbelegungen
	 * 
	 * @return die gefilterten Fachbelegungen
	 */
	public @NotNull List<@NotNull AbiturFachbelegung> filterFremdspracheNeuEinsetzend(List<@NotNull AbiturFachbelegung> fachbelegungen) {
		if (fachbelegungen == null)
			return Collections.emptyList();
		@NotNull Vector<@NotNull AbiturFachbelegung> result = new Vector<>();
		for (AbiturFachbelegung fb : fachbelegungen) {
			GostFach fach = getFach(fb);
			if ((fach != null) && fach.istFremdsprache && fach.istFremdSpracheNeuEinsetzend)
				result.add(fb);
		}
		return result;
	}
	
	
	/**
	 * Filtert die Fachbelegungen auf fortgeführte Fremdsprachen.
	 * 
	 * @param fachbelegungen   die zu filternden Fachbelegungen
	 * 
	 * @return die gefilterten Fachbelegungen
	 */
	public @NotNull List<@NotNull AbiturFachbelegung> filterFremdspracheFortgefuehrt(List<@NotNull AbiturFachbelegung> fachbelegungen) {
		if (fachbelegungen == null)
			return Collections.emptyList();
		@NotNull Vector<@NotNull AbiturFachbelegung> result = new Vector<>();
		for (AbiturFachbelegung fb : fachbelegungen) {
			GostFach fach = getFach(fb);
			if ((fach != null) && fach.istFremdsprache && !fach.istFremdSpracheNeuEinsetzend)
				result.add(fb);
		}
		return result;
	}
	
	
	/**
	 * Filtert die Fachbelegungen danach, ob sie durchgehend belegbar sind
	 * 
	 * @param fachbelegungen   die zu filternden Fachbelegungen
	 * 
	 * @return die gefilterten Fachbelegungen
	 */
	public @NotNull List<@NotNull AbiturFachbelegung> filterDurchgehendBelegbar(List<@NotNull AbiturFachbelegung> fachbelegungen) {
		@NotNull Vector<@NotNull AbiturFachbelegung> result = new Vector<>();
		if (fachbelegungen == null)
			return result;
		for (AbiturFachbelegung fb : fachbelegungen) {
			GostFach fach = getFach(fb);
			if (GostFachManager.istDurchgehendBelegbarBisQ22(fach))
				result.add(fb);
		}
		return result;
	}
	
	
	/**
	 * Filtert die Fachbelegungen. Es werden nur Fachbelegungen behalten, die in den angegebenen Halbjahren eine Belegung aufweisen.
	 * Wird kein Halbjahr angegeben, so wird nichts gefiltert, da kein Halbjahr geprüft werden muss.
	 * 
	 * @param fachbelegungen    die zu filternden Fachbelegungen 
	 * @param halbjahre         die Halbjahre, die belegt sein müssen
	 * 
	 * @return die gefilterten Fachbelegungen
	 */
	public @NotNull List<@NotNull AbiturFachbelegung> filterBelegungen(List<@NotNull AbiturFachbelegung> fachbelegungen, GostHalbjahr... halbjahre) {
		if (fachbelegungen == null)
			return Collections.emptyList();
		@NotNull Vector<@NotNull AbiturFachbelegung> result = new Vector<>();
		for (AbiturFachbelegung fb : fachbelegungen) {
			if (pruefeBelegung(fb, halbjahre))
				result.add(fb);
		}
		return result;
	}
	

	/**
	 * Diese Methode zählt die Anzahl der angegebenen Fachbelegungen, welche in allen 
	 * Halbjahren belegt sind. Dabei werden Fachbelegungen, welche dem gleichem Statistik-Fach
	 * zuzuordnen sind zusammengefasst. Dies ist bei der Abwahl von bilingualen Sachfächern
	 * relevant.
	 *   
	 * @param fachbelegungen   die zu zählenden Fachbelegungen
	 * 
	 * @return die Anzahl der Fachbelegungen
	 */
	public int zaehleBelegungenDurchgaengig(List<@NotNull AbiturFachbelegung> fachbelegungen) {
		if (fachbelegungen == null)
			return 0;
		// Bestimme zunächst die Menge der unterschiedlichen Statistik-Fächer
		@NotNull HashSet<ZulaessigesFach> faecher = new HashSet<>();
		for (AbiturFachbelegung fb : fachbelegungen) {
			GostFach fach = gostFaecher.get(fb.fachID);
			if (fach == null)
				continue;
			@NotNull ZulaessigesFach zulFach = ZulaessigesFach.getByKuerzelASD(fach.kuerzel);
			if (zulFach != ZulaessigesFach.DEFAULT)
				faecher.add(zulFach);
		}
		// Bestimme nun die Anzahl der Fachbelegungen, die in den Halbjahren existieren.
		int count = 0;
		for (ZulaessigesFach zulFach : faecher) {
			boolean vorhanden = true;
			for (GostHalbjahr halbjahr : GostHalbjahr.values()) {
				boolean belegung_vorhanden = false;
				for (AbiturFachbelegung fb : fachbelegungen) {
					GostFach fbFach = gostFaecher.get(fb.fachID);
					if (fbFach == null)
						continue;
					@NotNull ZulaessigesFach fbZulFach = ZulaessigesFach.getByKuerzelASD(fbFach.kuerzel);
					if ((zulFach == fbZulFach) && (fb.belegungen[halbjahr.id] != null)) {
						belegung_vorhanden = true;
						break;
					}
				}
				if (!belegung_vorhanden) {
					vorhanden = false;
					break;
				}
			}
			if (vorhanden)
				count++;
		}
		return count;
	}

	
	/**
	 * Diese Methode zählt die Anzahl der angegebenen Fachbelegungen, welche in allen 
	 * Halbjahren belegt sind. Dabei werden Fachbelegungen, welche dem gleichem Statistik-Fach
	 * zuzuordnen sind zusammengefasst. Dies ist bei der Abwahl von bilingualen Sachfächern
	 * relevant.
	 *   
	 * @param fachbelegungen   die zu zählenden Fachbelegungen
	 * 
	 * @return die Anzahl der Fachbelegungen
	 */
	public int zaehleBelegungenDurchgaengigSchriftlichInQPhase(List<@NotNull AbiturFachbelegung> fachbelegungen) {
		if (fachbelegungen == null)
			return 0;
		// Bestimme zunächst die Menge der unterschiedlichen Statistik-Fächer
		@NotNull HashSet<ZulaessigesFach> faecher = new HashSet<>();
		for (AbiturFachbelegung fb : fachbelegungen) {
			GostFach fach = gostFaecher.get(fb.fachID);
			if (fach == null)
				continue;
			@NotNull ZulaessigesFach zulFach = ZulaessigesFach.getByKuerzelASD(fach.kuerzel);
			if (zulFach != ZulaessigesFach.DEFAULT)
				faecher.add(zulFach);
		}
		// Bestimme nun die Anzahl der Fachbelegungen, die in den Halbjahren existieren.
		int count = 0;
		for (ZulaessigesFach zulFach : faecher) {
			boolean vorhanden = true;
			for (GostHalbjahr halbjahr : GostHalbjahr.values()) {
				boolean belegung_vorhanden = false;
				for (AbiturFachbelegung fb : fachbelegungen) {
					GostFach fbFach = gostFaecher.get(fb.fachID);
					if (fbFach == null)
						continue;
					@NotNull ZulaessigesFach fbZulFach = ZulaessigesFach.getByKuerzelASD(fbFach.kuerzel);
					if (zulFach == fbZulFach) {
						AbiturFachbelegungHalbjahr belegung = fb.belegungen[halbjahr.id];
						if (belegung != null) {
							boolean istSchriftlichkeitOK = true;
							if (((halbjahr == GostHalbjahr.Q11) || (halbjahr == GostHalbjahr.Q12) || (halbjahr == GostHalbjahr.Q21)) && 
									((belegung.schriftlich == null) || (!belegung.schriftlich)))
								istSchriftlichkeitOK = false;
							if (istSchriftlichkeitOK) {
								belegung_vorhanden = true;
								break;
							}
						}
					}
				}
				if (!belegung_vorhanden) {
					vorhanden = false;
					break;
				}
			}
			if (vorhanden)
				count++;
		}
		return count;
	}
	
	
	/**
	 * Filtert die Fachbelegungen. Es werden nur Belegungen behalten, die in den angegebenen Halbjahren die geforderte Schriftlichkeit aufweisen.
	 * Wird kein Halbjahr angegeben, so wird nichts gefiltert, da kein Halbjahr geprüft werden muss.
	 * 
	 * @param fachbelegungen    die zu filternden Fachbelegungen 
	 * @param schriftlichkeit   die geforderte Schriftlichkeit
	 * @param halbjahre         die Halbjahre, die belegt sein müssen
	 * 
	 * @return die gefilterten Fachbelegungen
	 */
	public @NotNull List<@NotNull AbiturFachbelegung> filterBelegungenMitSchriftlichkeit(List<@NotNull AbiturFachbelegung> fachbelegungen, @NotNull GostSchriftlichkeit schriftlichkeit, GostHalbjahr... halbjahre) {
		if (fachbelegungen == null)
			return Collections.emptyList();
		@NotNull Vector<@NotNull AbiturFachbelegung> result = new Vector<>();
		for (AbiturFachbelegung fb : fachbelegungen) {
			if (pruefeBelegungMitSchriftlichkeit(fb, schriftlichkeit, halbjahre))
				result.add(fb);
		}
		return result;
	}
	

	/**
	 * Liefert die erste Fachbelegung für den Fachbereich - sofern eine existiert
	 * 
	 * @param fachbereich   der Fachbereich
	 * 
	 * @return die Fachbelegung oder null
	 */
	public AbiturFachbelegung getFachbelegung(@NotNull GostFachbereich fachbereich) {
		Vector<AbiturFachbelegung> faecher = mapFachbereiche.get(fachbereich);
		if ((faecher == null) || (faecher.size() == 0))
			return null;
		return faecher.get(0);
	}
	
	
	
	/**
	 * Liefert alle Fachbelegungen mit dem angegebenen Statistk-Kürzel des Faches 
	 * 
	 * @param kuerzel   das Kürzel des Faches
	 * 
	 * @return eine Liste mit den Fachbelegungen
	 */
	public @NotNull List<@NotNull AbiturFachbelegung> getFachbelegungByFachkuerzel(String kuerzel) {
		@NotNull Vector<@NotNull AbiturFachbelegung> fachbelegungen = new Vector<>();
		if (kuerzel == null)
			return fachbelegungen;
		@NotNull Vector<@NotNull AbiturFachbelegung> tmpFachbelegungen = abidaten.fachbelegungen;
		for (AbiturFachbelegung fachbelegung : tmpFachbelegungen) {
			GostFach fach = gostFaecher.get(fachbelegung.fachID);
			if ((fach == null) || (!kuerzel.equals(fach.kuerzel)))
				continue;
			fachbelegungen.add(fachbelegung);
		}
		return fachbelegungen;
	}
	
	
	/**
	 * Prüft, ob der Kurs in dem angegebenen Halbjahr mit der angegebenen Schriftlichkeit belegt ist 
	 * und gibt ggf. die Belegung zurück.
	 *  
	 * @param fachbelegung   die Abiturfachbelegung aus welcher die Belegungsinformationen für das Halbjahr entnommen wird 
	 * @param halbjahr       das Halbjahr, in welchem die Belegung gesucht wird.
	 * @param schriftlich    gibt an, ob das Fach schriftlich oder mündlich belegt sein muss
	 * 
	 * @return die Belegungsinformationen zu dem Fach
	 */
	public AbiturFachbelegungHalbjahr getBelegungHalbjahr(@NotNull AbiturFachbelegung fachbelegung, @NotNull GostHalbjahr halbjahr, @NotNull GostSchriftlichkeit schriftlich) {
		AbiturFachbelegungHalbjahr belegung = fachbelegung.belegungen[halbjahr.id];
		return ((belegung != null) && 
			    ((schriftlich == GostSchriftlichkeit.BELIEBIG) ||
					     ((schriftlich == GostSchriftlichkeit.SCHRIFTLICH) && (belegung.schriftlich)) ||
					     ((schriftlich == GostSchriftlichkeit.MUENDLICH) && (!belegung.schriftlich))))
				? belegung : null;
	}
	
	
	/**
	 * Liefert die erste Sprachbelegung - sofern eine existiert
	 * 
	 * @param sprache   das einstellige Kürzel der Sprache
	 * 
	 * @return die Fachbelegung für die Sprache
	 */
	public AbiturFachbelegung getSprachbelegung(String sprache) {
		if (sprache == null)
			return null;
		@NotNull Vector<@NotNull AbiturFachbelegung> fachbelegungen = abidaten.fachbelegungen;
		for (AbiturFachbelegung fb : fachbelegungen) {
			GostFach fach = getFach(fb);
			if ((fach == null) || (!GostFachManager.istFremdsprachenfach(fach, sprache)))
				continue;
			if (sprache.equals(GostFachManager.getFremdsprache(fach)))
				return fb;
		}
		return null;
	}
	
	
	/**
	 * Liefert für die übergebene Fachbelegung die Halbjahre, in denen das Fach mit einer der angebenen 
	 * Kursarten belegt wurde. Ist keine Kursart angegeben, so werden die Halbjahre aller Belegungen
	 * zurückgegeben. Ist keine Fachbelegung angegeben, so wird eine leere Liste zurückgegeben.
	 *  
	 * @param fachbelegung   die Fachbelegung
	 * @param kursarten      die Kursarten
	 * 
	 * @return eine Liste der Halbjahre in den das Fach mit einer der Kursarten belegt wurde
	 */
	public @NotNull Vector<@NotNull GostHalbjahr> getHalbjahreKursart(AbiturFachbelegung fachbelegung, GostKursart... kursarten) {
		@NotNull Vector<@NotNull GostHalbjahr> halbjahre = new Vector<>();  
		if (fachbelegung != null) {
			for (AbiturFachbelegungHalbjahr belegungHalbjahr : fachbelegung.belegungen) {
				if (belegungHalbjahr == null)
					continue;
				GostHalbjahr halbjahr = GostHalbjahr.fromKuerzel(belegungHalbjahr.halbjahrKuerzel);
				if (halbjahr == null)
					continue;
				if ((kursarten == null) || (kursarten.length == 0)) {
					halbjahre.add(halbjahr);
					continue;
				}
				for (GostKursart kursart : kursarten) {
					if (kursart.equals(GostKursart.fromKuerzel(belegungHalbjahr.kursartKuerzel))) {
						halbjahre.add(halbjahr);
						break;
					}
				}
			}
		}
		return halbjahre;
	}

	
	
	/**
	 * Gibt die Sprache des bilingualen Bildungsgang zurück oder null, falls keiner gewählt wurde.
	 * 
	 * @return die Sprache des bilingualen Bildungsgang oder null
	 */
	public String getBiligualenBildungsgang() {
		return abidaten.bilingualeSprache;
	}


	/**
	 * Prüft bei der Sprachenfolge, ob eine laut Sprachenfolge fortgeführte
	 * Fremdsprache fehlerhafterweise als neu einsetzende Fremdsprache belegt wurde.
	 * Übergebene Fachbelegungen, die keine Fremdsprachen sind werden ignoriert.  
	 * 
	 * @param fremdsprachen   die zu prüfenden Fachbelegungen
	 *  
	 * @return true, falls eine fortgeführte Fremdsprache bei den übergebenen 
	 *         Fachbelegungen existiert, ansonsten false 
	 */
	public boolean hatFortgefuehrteFremdspracheInSprachendaten(List<@NotNull AbiturFachbelegung> fremdsprachen) {
		if (fremdsprachen == null)
			return false;
		if (abidaten.sprachendaten == null)
			return false;
		for (AbiturFachbelegung fs: fremdsprachen) {
			GostFach fach = getFach(fs);
			if ((fach == null) || (!fach.istFremdsprache))
				continue;
			if (SprachendatenUtils.istFortfuehrbareSpracheInGOSt(abidaten.sprachendaten, GostFachManager.getFremdsprache(fach))) {
                return true;
            }
		}
		return false;
	}
	
	
	/**
	 * Prüft bei der Sprachenfolge, ob für eine laut Sprachenfolge neu einsetzende
	 * Fremdsprache fehlerhafterweise ein Kurs in einer fortgeführten Fremdsprache belegt wurde.
	 * Übergebene Fachbelegungen, die keine Fremdsprachen sind werden ignoriert.  
	 * 
	 * @param fremdsprachen   die zu prüfenden Fachbelegungen
	 *  
	 * @return true, falls eine neu einsetzende Fremdsprache bei den übergebenen 
	 *         Fachbelegungen existiert, ansonsten false 
	 */
	public boolean hatNeuEinsetzendeFremdspracheInSprachendaten(List<@NotNull AbiturFachbelegung> fremdsprachen) {
		if (fremdsprachen == null)
			return false;
		if (abidaten.sprachendaten == null)
			return false;
		for (AbiturFachbelegung fs: fremdsprachen) {
			GostFach fach = getFach(fs);
			if ((fach == null) || (!fach.istFremdsprache))
				continue;
            if (!SprachendatenUtils.istFortfuehrbareSpracheInGOSt(abidaten.sprachendaten, GostFachManager.getFremdsprache(fach))) {
                return true;
            }
		}
		return false;
	}


	/**
	 * Prüft, ob die Belegung seit der EF1 vorhanden ist. Hierbei werden
	 * Zusatz-, Vertiefungs- und Projektkurse auch als später einsetzend akzeptiert.
	 * Dies gilt auch für Literatur, instrumental- und vokalpraktische Kurse sowie 
	 * für Religion und Philosophie.
	 * 
	 * @param fachbelegung   die Abiturfachbelegungen, die geprüft werden
	 * 
	 * @return true, falls das Fach seit EF1 durchgängig belegt wurde oder eine der Ausnahmen zutrifft, sonsta false 
	 */
	public boolean istBelegtSeitEF(@NotNull AbiturFachbelegung fachbelegung) {
		GostFach fach = getFach(fachbelegung);
		if (fach == null)
			return false;
		if (GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(fach))
			return true;
		if (GostFachbereich.RELIGION.hat(fach))
			return true;
		if ("PL".equals(fach.kuerzel))
			return true;
		for (AbiturFachbelegungHalbjahr belegung : fachbelegung.belegungen) {
			if (belegung == null)
				continue;
			GostHalbjahr halbjahr = GostHalbjahr.fromKuerzel(belegung.halbjahrKuerzel);
			GostKursart kursart = GostKursart.fromKuerzel(belegung.kursartKuerzel);
			if ((halbjahr == null) || (kursart == null))
				continue;
			if ((kursart == GostKursart.ZK) || (kursart == GostKursart.PJK) || (kursart == GostKursart.VTF))
				continue;
			// Prüfe, ob das vorige Halbjahr auch belegt wurde
			GostHalbjahr prevHalbjahr = halbjahr.previous();
			if (prevHalbjahr == null)
				continue;
			if (fachbelegung.belegungen[prevHalbjahr.id] == null) {
				// Prüfe, ob eine Belegung des gleichen Statistik-Faches im Halbjahr zuvor existiert - dies kann bei bilingualen Fächern auftreten
				List<@NotNull AbiturFachbelegung> alleBelegungen = getFachbelegungByFachkuerzel(fach.kuerzel);
				if ((alleBelegungen == null) || (alleBelegungen.size() <= 1))
					return false;
				if (!pruefeBelegungExistiert(alleBelegungen, prevHalbjahr))
					return false;
			}
		}
		return true;
	}
	
	
	
	
	/**
	 * Gibt das Ergebnis der Belegprüfung zurück. Dieses enthält eine Liste der Fehler, die bei der Belegprüfung 
	 * festgestellt wurden und ob diese erfolgreich gewesen ist oder nicht.
	 * 
	 * @return das Ergebnis der Belegprüfung
	 */
	@JsonIgnore
	public @NotNull GostBelegpruefungErgebnis getBelegpruefungErgebnis() {
		@NotNull GostBelegpruefungErgebnis ergebnis = new GostBelegpruefungErgebnis();
		ergebnis.erfolgreich = belegpruefungErfolgreich;
		for (int i = 0; i < belegpruefungsfehler.size(); i++) {
			@NotNull GostBelegungsfehler fehler = belegpruefungsfehler.get(i);
			ergebnis.fehlercodes.add(new GostBelegpruefungErgebnisFehler(fehler, pruefungsArt));
		}
		// TODO Ergänze das Ergebnis um einen Log der Belegprüfung
		return ergebnis;
	}
	
}
