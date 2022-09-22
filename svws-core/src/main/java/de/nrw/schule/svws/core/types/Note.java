package de.nrw.schule.svws.core.types;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt die Core-Types als Enumeration für 
 * die zulässigen Noteneinträge zur Verfügung.
 */
public enum Note {

	/** leerer Noteneintrag */
	KEINE(-1, -1, null, "", "", "--------------------", null, null),

	/** Die Note "ungenügend" mit 0 Punkten.*/
	UNGENUEGEND(0, 600, 0, "6", "ungenuegend", "ungenuegend", null, null),

	/** Die Note "mangelhaft minus" mit 1 Punkt.*/
	MANGELHAFT_MINUS(1, 501, 1, "5-", "mangelhaft (minus)", "mangelhaft", null, null),

	/** Die Note "mangelhaft" mit 2 Punkten.*/
	MANGELHAFT(2, 500, 2, "5", "mangelhaft", "mangelhaft", null, null),

	/** Die Note "mangelhaft plus" mit 3 Punkten.*/
	MANGELHAFT_PLUS(3, 499, 3, "5+", "mangelhaft (plus)", "mangelhaft", null, null),

	/** Die Note "ausreichend minus" mit 4 Punkten.*/
	AUSREICHEND_MINUS(4, 401, 4, "4-", "ausreichend (minus)", "ausreichend", null, null),

	/** Die Note "ausreichend " mit 5 Punkten.*/
	AUSREICHEND(5, 400, 5, "4", "ausreichend", "ausreichend", null, null),

	/** Die Note "ausreichend plus" mit 6 Punkten.*/
	AUSREICHEND_PLUS(6, 399, 6, "4+", "ausreichend (plus)", "ausreichend", null, null),

	/** Die Note "befriedigend minus" mit 7 Punkten.*/
	BEFRIEDIGEND_MINUS(7, 301, 7, "3-", "befriedigend (minus)", "befriedigend", null, null),

	/** Die Note "befriedigend" mit 8 Punkten.*/
	BEFRIEDIGEND(8, 300, 8, "3", "befriedigend", "befriedigend", null, null),

	/** Die Note "befriedigend plus" mit 9 Punkten.*/
	BEFRIEDIGEND_PLUS(9, 299, 9, "3+", "befriedigend (plus)", "befriedigend", null, null),

	/** Die Note "gut minus" mit 10 Punkten.*/
	GUT_MINUS(10, 201, 10, "2-", "gut (minus)", "gut", null, null),

	/** Die Note "gut" mit 111 Punkten.*/
	GUT(11, 200, 11, "2", "gut", "gut", null, null),

	/** Die Note "gut plus" mit 12 Punkten.*/
	GUT_PLUS(12, 199, 12, "2+", "gut (plus)", "gut", null, null),

	/** Die Note "sehr gut mnus" mit 13 Punkten.*/
	SEHR_GUT_MINUS(13, 101, 13, "1-", "sehr gut (minus)", "sehr gut", null, null),

	/** Die Note "sehr gut" mit 14 Punkten.*/
	SEHR_GUT(14, 100, 14, "1", "sehr gut", "sehr gut", null, null),

	/** Die Note "sehr gut plus" mit 15 Punkten.*/
	SEHR_GUT_PLUS(15, 99, 15, "1+", "sehr gut (plus)", "sehr gut", null, null),

	/** Der Noteneintrag "ärtzliches Attest" mit dem Wert AT.*/
	ATTEST(16, 2500, null, "AT", "(ärtzliches Attest)", "--------------------", null, null),

	/** Der Noteneintrag "mit besonderem Erfolg teilgenommen" mit dem Wert E1.*/
	E1_MIT_BESONDEREM_ERFOLG_TEILGENOMMEN(17, 1000, null, "E1", "mit besonderem Erfolg teilgenommen", "mit besonderem Erfolg teilgenommen", null, null),

	/** Der Noteneintrag "mit Erfolg teilgenommen" mit dem Wert E2.*/
	E2_MIT_ERFOLG_TEILGENOMMEN(18, 1001, null, "E2", "mit Erfolg teilgenommen", "mit Erfolg teilgenommen", null, null),

	/** Der Noteneintrag "teilgenommen" mit dem Wert E3.*/
	E3_TEILGENOMMEN(19, 1002, null, "E3", "teilgenommen", "teilgenommen", null, null),

	/** Der Noteneintrag "nicht teilgenommen" mit dem Wert NT.*/
	NICHT_TEILGENOMMEN(20, 4010, null, "NT", "(nicht teilgenommen)", "--------------------", null, null),

	/** Der Noteneintrag "kann nicht beurteilt werdent" mit dem Wert NB.*/
	NICHT_BEURTEILT(21, 4000, null, "NB", "(kann nicht beurteilt werden)", "--------------------", null, null),

	/** Der Noteneintrag "nicht erteilt" mit dem Wert NE.*/
	NICHT_ERTEILT(22, 4020, null, "NE", "(nicht erteilt)", "--------------------", null, null),

	/** Der Noteneintrag "kein Unterricht wegen Lehrermangel" mit dem Wert LM.*/
	LEHRERMANGEL(23, 9000, null, "LM", "(kein Unterricht wegen Lehrermangel)", "--------------------", null, null),

	/** Der Noteneintrag "abgemeldet" mit dem Wert AM.*/
	ABGEMELDET(24, 2000, null, "AM", "(abgemeldet)", "--------------------", null, null);


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;


	/** Die eindeutige ID der Note */
	public final int id;

	/** Eine ID, die der Sortierung der Noteneinträge in einer Anwendung vorgibt */
	public final int sortierung;

	/** Die Notenpunkte, die dieser Note zugeordnet sind */
	public final Integer notenpunkte;

	/** Die Kurzschreibweise der Note als Zahl ggf. mit Tendenz (+/-) */
	public final @NotNull String kuerzel;

	/** Die Note in ausführlicher Textform ggf. mit Tendenz (plus/minus) */
	public final @NotNull String text;

	/** Die Note in ausführlicher Textform, wie sie auf einem Zeugnis dargestellt wird. */
	public final @NotNull String textZeugnis;

	/** Gibt an, in welchem Schuljahr die Note einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	public final Integer gueltigVon;

	/** Gibt an, bis zu welchem Schuljahr die Note gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	public final Integer gueltigBis;



	/**
	 * Erzeugt ein neues Element der Aufzählung
	 *  
	 * @param id              die eindeutige ID der Note
	 * @param sortierung      eine ID, die der Sortierung der Noteneinträge in einer Anwendung vorgibt  
	 * @param notenpunkte     die Notenpunkte, die dieser Note zugeordnet sind
	 * @param kuerzel         die Kurzschreibweise der Note als Zahl ggf. mit Tendenz (+/-)
	 * @param text            die Note in ausführlicher Textform ggf. mit Tendenz (plus/minus)
	 * @param textZeugnis     die Note in ausführlicher Textform, wie sie auf einem Zeugnis dargestellt wird
	 * @param gueltigVon      gibt an, in welchem Schuljahr die Note einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 * @param gueltigBis      gibt an, bis zu welchem Schuljahr die Note gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	private Note(int id, int sortierung, Integer notenpunkte, @NotNull String kuerzel, @NotNull String text, @NotNull String textZeugnis, Integer gueltigVon, Integer gueltigBis) {
    	this.id = id;
    	this.sortierung = sortierung;
		this.notenpunkte = notenpunkte;
		this.kuerzel = kuerzel;
		this.text = text;
		this.textZeugnis = textZeugnis;
        this.gueltigVon = gueltigVon;
        this.gueltigBis = gueltigBis;
	}


	/**
	 * Gibt zurück, ob es sich um eine echte Note oder nur um eine "Pseudonote" handelt
	 * 
	 * @return true, wenn es sich bei der Note um eine echte Note handelt
	 */
	@JsonIgnore
	public boolean istNote() {
		return notenpunkte != null;
	}


	/**
	 * Bestimmt die Note anhand des übergebenen Integer-Wert, welcher eine
	 * Note ohne Tendenz darstellt.
	 *  
	 * @param noteSekI    die Note ohne Tendenz
	 * 
	 * @return die Note
	 */
	public static Note fromNoteSekI(Integer noteSekI) {
		if (noteSekI == null)
			return null;
		switch (noteSekI) {
			case 1: return SEHR_GUT;
			case 2: return GUT;
			case 3: return BEFRIEDIGEND;
			case 4: return AUSREICHEND;
			case 5: return MANGELHAFT;
			case 6: return UNGENUEGEND;
			default: return null;
		}
	}


	/**
	 * Gibt die Note anhand der angebenen Notenpunkte zurück.
	 * 
	 * @param notenpunkte   die Notenpunkte anhand derer die Note ermittelt wird
	 * 
	 * @return die Note aus dieser Aufzählung oder Note.KEINE im Fehlerfall
	 */
	public static @NotNull Note fromNotenpunkte(Integer notenpunkte) {
		if (notenpunkte == null)
			return KEINE;
		switch (notenpunkte) {
			case 0: return UNGENUEGEND;
			case 1: return MANGELHAFT_MINUS;
			case 2: return MANGELHAFT;
			case 3: return MANGELHAFT_PLUS;
			case 4: return AUSREICHEND_MINUS;		
			case 5: return AUSREICHEND;		
			case 6: return AUSREICHEND_PLUS;		
			case 7: return BEFRIEDIGEND_MINUS;		
			case 8: return BEFRIEDIGEND;		
			case 9: return BEFRIEDIGEND_PLUS;		
			case 10: return GUT_MINUS;		
			case 11: return GUT;		
			case 12: return GUT_PLUS;		
			case 13: return SEHR_GUT_MINUS;		
			case 14: return SEHR_GUT;		
			case 15: return SEHR_GUT_PLUS;	
		}
		return KEINE;
	}

	
	
	/**
	 * Gibt die Note bzw. Pseudonote anhand des angebenen Kürzels zurück.
	 * 
	 * @param kuerzel   das Kürzel anhand derer die Note ermittelt wird
	 * 
	 * @return die Note aus dieser Aufzählung oder Note.KEINE im Fehlerfall
	 */
	public static @NotNull Note fromKuerzel(String kuerzel) {
		if (kuerzel == null)
			return KEINE;
		kuerzel = kuerzel.toUpperCase();
		switch (kuerzel) {
			case "6": return UNGENUEGEND;
			case "5-": return MANGELHAFT_MINUS;
			case "5": return MANGELHAFT;
			case "5+": return MANGELHAFT_PLUS;
			case "4-": return AUSREICHEND_MINUS;		
			case "4": return AUSREICHEND;		
			case "4+": return AUSREICHEND_PLUS;		
			case "3-": return BEFRIEDIGEND_MINUS;		
			case "3": return BEFRIEDIGEND;		
			case "3+": return BEFRIEDIGEND_PLUS;		
			case "2-": return GUT_MINUS;		
			case "2": return GUT;		
			case "2+": return GUT_PLUS;		
			case "1-": return SEHR_GUT_MINUS;		
			case "1": return SEHR_GUT;		
			case "1+": return SEHR_GUT_PLUS;
			case "E1": return E1_MIT_BESONDEREM_ERFOLG_TEILGENOMMEN;
			case "E2": return E2_MIT_ERFOLG_TEILGENOMMEN;
			case "E3": return E3_TEILGENOMMEN;
			case "AT": return ATTEST;
			case "AM": return ABGEMELDET;
			case "NB": return NICHT_BEURTEILT;
			case "NT": return NICHT_TEILGENOMMEN;
			case "NE": return NICHT_ERTEILT;
			case "LM": return LEHRERMANGEL;
		}
		return KEINE;
	}
	
	
	/**
	 * Gibt die Note anhand der angebenen Notenpunkte zurück, welche als String 
	 * übergeben werden.
	 * 
	 * @param notenpunkte   die Notenpunkte anhand derer die Note ermittelt wird als String
	 * 
	 * @return die Note aus dieser Aufzählung oder Note.KEINE im Fehlerfall
	 */
	public static @NotNull Note fromNotenpunkteString(String notenpunkte) {
		if (notenpunkte == null)
			return KEINE;
		switch (notenpunkte) {
			case "0": return UNGENUEGEND;
			case "1": return MANGELHAFT_MINUS;
			case "2": return MANGELHAFT;
			case "3": return MANGELHAFT_PLUS;
			case "4": return AUSREICHEND_MINUS;		
			case "5": return AUSREICHEND;		
			case "6": return AUSREICHEND_PLUS;		
			case "7": return BEFRIEDIGEND_MINUS;		
			case "8": return BEFRIEDIGEND;		
			case "9": return BEFRIEDIGEND_PLUS;		
			case "10": return GUT_MINUS;		
			case "11": return GUT;		
			case "12": return GUT_PLUS;		
			case "13": return SEHR_GUT_MINUS;		
			case "14": return SEHR_GUT;		
			case "15": return SEHR_GUT_PLUS;		
		}
		return KEINE;
	}
	
	
	/**
	 * Gibt an, ob es sich um eine Note mit Tendenz handelt oder nicht.
	 * 
	 * @return true, falls die Note eine Tendenz hat
	 */
	public boolean hatTendenz() {
		if (notenpunkte == null)
			return false;
		switch (notenpunkte) {
			case 0: 
			case 2: 
			case 5:
			case 8: 		
			case 11: 		
			case 14: 		
				return false;				
		}
		return true;
	}
	
	
	/**
	 * Ermittelt die zu der Note gehörende Note ohne Tendenz (z.B. 3+ wird zu 3)
	 * 
	 * @return die entsprechende Note ohne Tendenz
	 */
	@JsonIgnore
	public @NotNull Note ohneTendenz() {
		if (notenpunkte == null)
			return KEINE;
		switch (notenpunkte) {
			case 0: 
				return UNGENUEGEND;
			case 1: 
			case 2: 
			case 3: 
				return MANGELHAFT;
			case 4: 		
			case 5:
			case 6: 
				return AUSREICHEND;
			case 7: 		
			case 8: 		
			case 9: 
				return BEFRIEDIGEND;
			case 10: 		
			case 11: 		
			case 12: 
				return GUT;		
			case 13: 		
			case 14: 		
			case 15: 
				return SEHR_GUT;				
		}
		return KEINE;
	}


	@Override
	@JsonIgnore
	public @NotNull String toString() {
		return kuerzel;
	}


	/**
	 * Gibt die Note als Note der Sekundarstufe I ohne Tendenz als Zahl zurück.
	 * 
	 * @return die Noten 1-6 oder im Fehlerfall null  
	 */
	@JsonIgnore
	public Integer getNoteSekI() {
		switch (notenpunkte) {
			case 15:
			case 14:
			case 13:
				return 1;
			case 12:
			case 11:
			case 10:
				return 2;
			case 9:
			case 8:
			case 7:
				return 3;
			case 6:
			case 5:
			case 4:
				return 4;
			case 3:
			case 2:
			case 1:
				return 5;
			case 0:
				return 6;
			default:
				return null;
		}
	}

}
