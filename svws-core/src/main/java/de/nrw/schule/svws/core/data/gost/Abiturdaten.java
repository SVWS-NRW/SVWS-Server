package de.nrw.schule.svws.core.data.gost;

import java.util.Vector;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.nrw.schule.svws.core.data.Sprachendaten;
import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import de.nrw.schule.svws.core.types.gost.GostBesondereLernleistung;
import de.nrw.schule.svws.core.types.gost.GostHalbjahr;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;


/**
 * Die Klasse enthält enthält die Informationen der gymnasialen Oberstufe eines Schülers in Bezug auf das Abitur.
 */
@XmlRootElement
@Schema(description="enthält die Informationen der gymnasialen Oberstufe eines Schülers in Bezug auf das Abitur.")
@JsonPropertyOrder({ "schuelerID", "abiturjahr", "schuljahrAbitur", "fachbelegungen", "sprachenfolge", "sprachpruefungen", "bilingualeSprache",
    "sek1Fremdsprache2ManuellGeprueft", "muttersprachenpruefungEndeEF", "latinum", "kleinesLatinum", "graecum", "hebraicum",
    "block1FehlstundenGesamt", "block1FehlstundenUnentschuldigt", "projektKursThema", "projektkursLeitfach1Kuerzel", 
    "projektkursLeitfach2Kuerzel", "besondereLernleistung", "besondereLernleistungNotenKuerzel", "besondereLernleistungThema",
    "block1AnzahlKurse", "block1DefiziteGesamt", "block1DefiziteLK", "block1PunktSummeGK", "block1PunktSummeLK", "block1PunktSummeNormiert",
    "block1NotenpunkteDurchschnitt", "block1Zulassung", "freiwilligerRuecktritt", "block2DefiziteGesamt", "block2DefiziteLK", 
    "block2PunktSumme", "gesamtPunkte", "gesamtPunkteVerbesserung", "gesamtPunkteVerschlechterung", "pruefungBestanden", "note" })
@TranspilerDTO
public class Abiturdaten {
	
	/** Die eindeutige ID des Schülers */
	@Schema(required = true, description = "Die eindeutige ID des Schülers.", example="4711")
	public long schuelerID;
	
	/** Das Kalenderjahr, in dem der Schüler sein Abitur ablegt bzw. ablegen wird. */
	@Schema(required = true, description = "Das Kalenderjahr, in dem der Schüler sein Abitur ablegt bzw. ablegen wird.", example="2025")
	public int abiturjahr;
	
	/** Das Schuljahr, in dem der Schüler sein Abitur ablegt bzw. ablegen wird. */
	@Schema(required = true, description = "Das Schuljahr, in dem der Schüler sein Abitur ablegt bzw. ablegen wird.", example="2024")
	public int schuljahrAbitur;
	
	
	/** Gibt für die einzelnen {@link GostHalbjahr}-Werte an, ob gewertete Leistungsdaten vorhanden sind oder es sich um Werte der Laufbahnplanung handelt. */
	@ArraySchema(schema = @Schema(required = true, implementation = Boolean.class, description = "Gibt für die einzelnen Halbjahre der Oberstufe an, ob gewertete Leistungsdaten vorhanden sind oder es sich um Werte der Laufbahnplanung handelt."))
	public final @NotNull boolean[] bewertetesHalbjahr = new boolean[6];
	
	/** Ein Array mit den Fachbelegungen in der Oberstufe. */
	@ArraySchema(schema = @Schema(required = true, implementation = AbiturFachbelegung.class, description = "Ein Array mit den Fachbelegungen in der Oberstufe."))
	public final @NotNull Vector<@NotNull AbiturFachbelegung> fachbelegungen = new Vector<>();
	
	/** Die Sprachendaten des Schülers mit Informationen zu Sprachbelegungen (Sprachenfolge) und zu Sprachprüfungen. */
	@Schema(required = true, implementation = Sprachendaten.class, description = "Die Sprachenfolge und die Sprachprüfungen des Schülers unter Einbeziehung der Daten aus der Sekundarstufe I.")
	public @NotNull Sprachendaten sprachendaten = new Sprachendaten();

    /** Das einstellige Kürzel der Sprache des bilingualen Bildungsganges, falls der Schüler an einem solchen teilnimmt. */
	@Schema(required = false, description = "Das einstellige Kürzel der Sprache des bilingualen Bildungsganges, falls der Schüler an einem solchen teilnimmt, ansonsten null.", example="E")
	public String bilingualeSprache = null;
	
	
	// TODO IMPLEMENTATION SPRACHPRÜFUNGEN: Alte LuPO Einträge zur manuellen Prüfung von Fremdsprachen können entfernt werden.
	/** Gibt an, ob die zweite Fremdsprache in der Sekundarstufe I manuell geprüft wurde und vorhanden ist. Bei einer Belegprüfung muss diese dann nicht anhand der Sprachenfolge geprüft werden. */
	@Schema(required = false, defaultValue ="false", description = "Gibt an, ob die zweite Fremdsprache in der Sekundarstufe I manuell geprüft wurde und vorhanden ist. Bei einer Belegprüfung muss diese dann nicht anhand der Sprachenfolge geprüft werden.", example="true")
	public boolean sek1Fremdsprache2ManuellGeprueft = false;

    // TODO IMPLEMENTATION SPRACHPRÜFUNGEN: Alte LuPO Einträge zur manuellen Prüfung von Fremdsprachen können entfernt werden.
	/** Gibt an, ob am Ende der EF eine Muttersprachliche Prüfung erfolgen wird bzw. erfolgt ist. */
	@Schema(required = false, defaultValue ="false", description = "Gibt an, ob am Ende der EF eine Muttersprachliche Prüfung erfolgen wird bzw. erfolgt ist.", example="true")
	public boolean muttersprachenpruefungEndeEF = false;
    

	/** Gibt an, ob das große Latinum erworben wurde. */
	@Schema(required = false, defaultValue ="false", description = "Gibt an, ob das große Latinum erworben wurde.", example="true")
	public boolean latinum = false;
	
	/** Gibt an, ob das kleine Latinum erworben wurde. */
	@Schema(required = false, defaultValue ="false", description = "Gibt an, ob das kleine Latinum erworben wurde.", example="true")
	public boolean kleinesLatinum = false;
	
	/** Gibt an, ob das Graecum erworben wurde. */
	@Schema(required = false, defaultValue ="false", description = "Gibt an, ob das Graecum erworben wurde.", example="true")
	public boolean graecum = false;
	
	/** Gibt an, ob das Hebraicum erworben wurde. */
	@Schema(required = false, defaultValue ="false", description = "Gibt an, ob das Hebraicum erworben wurde.", example="true")
	public boolean hebraicum = false;

	
	/** Die Anzahl der Fehlstunden in der gesamten Qualifikationsphase. */
	@Schema(required = false, defaultValue ="0", description = "Die Anzahl der Fehlstunden in der gesamten Qualifikationsphase.", example="0")	
	public long block1FehlstundenGesamt = 0;
	
	/** Die Anzahl der unentschuldigten Fehlstunden in der gesamten Qualifikationsphase. */
	@Schema(required = false, defaultValue ="0", description = "Die Anzahl der unentschuldigten Fehlstunden in der gesamten Qualifikationsphase.", example="0")	
	public long block1FehlstundenUnentschuldigt = 0;

	
	/** Das Projektkursthema, sofern ein Projektkurs belegt wurde.", example="Das Abitur IN NRW im Wandel der Zeit. */
	@Schema(required = false, description = "Das Projektkursthema, sofern ein Projektkurs belegt wurde.", example="Das Abitur IN NRW im Wandel der Zeit.")	
	public String projektKursThema = null;
	
	/** Das Kürzel des ersten Leitfaches des belegten Projektkurs, sofern einer belegt wurde. */
	@Schema(required = false, description = "Das Kürzel des ersten Leitfaches des belegten Projektkurs, sofern einer belegt wurde.", example="M")	
	public String projektkursLeitfach1Kuerzel = null;
	
	/** Das Kürzel des zweiten Leitfaches des belegten Projektkurs, sofern einer belegt wurde und ein zweites Leitfach für diesen festgelegt wurde */
	@Schema(required = false, description = "Das Kürzel des zweiten Leitfaches des belegten Projektkurs, sofern einer belegt wurde und ein zweites Leitfach für diesen festgelegt wurde.", example="E")	
	public String projektkursLeitfach2Kuerzel = null;


	/** Gibt an, ob eine besondere Lernleistung vorliegt (K - keine, P - in einem Projektkurs, E - extern). */
	@Schema(required = false, defaultValue ="K", description = "Gibt an, ob eine besondere Lernleistung vorliegt (K - keine, P - in einem Projektkurs, E - extern).", example="K")
	public String besondereLernleistung = GostBesondereLernleistung.KEINE.kuerzel;
	
	/** Gibt ggf. die Note einer externen besonderen Lernleistung an. */
	@Schema(required = false, description = "Gibt ggf. die Note einer externen besonderen Lernleistung an.", example="2+")
	public String besondereLernleistungNotenKuerzel = null; // nur bei externer BLL
	
	/** Gibt das Thema der Besonderen Lernleistung an. */
	@Schema(required = false, description = "Gibt das Thema der Besonderen Lernleistung an.", example="Besondere Lernleistungen - Etwas Besonderes für Besondere.")	
	public String besondereLernleistungThema = null;
		    
	
	/** Gibt die Anzahl der Kurse in der Qualifikationsphase an. */
	@Schema(required = false, description = "Gibt die Anzahl der Kurse in der Qualifikationsphase an.", example="38")
	public Integer block1AnzahlKurse = null;
	
	/** Gibt die Anzahl der Gesamtdefizite in der Qualifikationsphase an. */
	@Schema(required = false, description = "Gibt die Anzahl der Gesamtdefizite in der Qualifikationsphase an.", example="4")
	public Integer block1DefiziteGesamt = null;
	
	/** Gibt die Anzahl der Defizite im LK-Bereich in der Qualifikationsphase an. */
	@Schema(required = false, description = "Gibt die Anzahl der Defizite im LK-Bereich in der Qualifikationsphase an.", example="1")
	public Integer block1DefiziteLK = null;
		
	/** Gibt die Punktsumme aller Grundkurse in der Qualifikationsphase an. */
	@Schema(required = false, description = "Gibt die Punktsumme aller Grundkurse in der Qualifikationsphase an.", example="157")
	public Integer block1PunktSummeGK = null;
	
	/** Gibt die Punktsumme aller Leistungskurse in der Qualifikationsphase an. */
	@Schema(required = false, description = "Gibt die Punktsumme aller Leistungskurse in der Qualifikationsphase an.", example="117")
	public Integer block1PunktSummeLK = null;
	
	/** Gibt die normierte Punktsumme aller Kurse in der Qualifikationsphase an. */
	@Schema(required = false, description = "Gibt die normierte Punktsumme aller Kurse in der Qualifikationsphase an.", example="276")
	public Integer block1PunktSummeNormiert = null;
	
	/** Gibt den Durchschnitt der Notenpunkte von allen Kursen der Qualifikationsphase an. */
	@Schema(required = false, description = "Gibt den Durchschnitt der Notenpunkte von allen Kursen der Qualifikationsphase an.", example="11.25")
	public Double block1NotenpunkteDurchschnitt = null;
	
	/** Gibt an, ob die Zulassung erreicht wurde oder nicht - sofern diese schon geprüft wurde. */
	@Schema(required = false, description = "Gibt an, ob die Zulassung erreicht wurde oder nicht - sofern diese schon geprüft wurde.", example="true")
	public Boolean block1Zulassung = null;
	

	/** Gibt an, ob freiwillig von der Abiturprüfung zurückgetreten wurde. */
	@Schema(required = true, defaultValue = "false", description = "Gibt an, ob freiwillig von der Abiturprüfung zurückgetreten wurde.", example="true")
	public boolean freiwilligerRuecktritt = false;

	

	/** Gibt die Anzahl der Gesamtdefizite im Abiturbereich (Block II) an. */
	@Schema(required = false, description = "Gibt die Anzahl der Gesamtdefizite im Abiturbereich (Block II) an.", example="2")
	public Integer block2DefiziteGesamt = null;
	
	/** Gibt die Anzahl der Leistungskurs-Defizite im Abiturbereich (Block II) an. */
	@Schema(required = false, description = "Gibt die Anzahl der Leistungskurs-Defizite im Abiturbereich (Block II) an.", example="1")
	public Integer block2DefiziteLK = null;
	
	/** Gibt die Punktsumme im Abiturbereich (Block II) an. */
	@Schema(required = false, description = "Gibt die Punktsumme im Abiturbereich (Block II) an.", example="120")
	public Integer block2PunktSumme = null;
    

	
	/** Gibt die erreichte Gesamtpunktzahl in der Qualifikation und im Abiturbereich (Block I und II) an. */
	@Schema(required = false, description = "Gibt die erreichte Gesamtpunktzahl in der Qualifikation und im Abiturbereich (Block I und II) an.", example="456")
	public Integer gesamtPunkte = null;
	
	/** Gibt die Gesamtpunktzahl an, ab der sich die Abiturnote verbessern würde */
	@Schema(required = false, description = "Gibt die Gesamtpunktzahl an, ab der sich die Abiturnote verbessern würde.", example="463")
	public Integer gesamtPunkteVerbesserung = null;
	
	/** Gibt die Gesamtpunktzahl an, ab der sich die Abiturnote verschlechtern würde. */
	@Schema(required = false, description = "Gibt die Gesamtpunktzahl an, ab der sich die Abiturnote verschlechtern würde.", example="444")
	public Integer gesamtPunkteVerschlechterung = null;
	
	
	/** Gibt an, ob die Abiturprüfung bestanden wurde oder nicht - sofern das Prüfungsverfahren schon abgeschlossen wurde. */
	@Schema(required = false, description = "Gibt an, ob die Abiturprüfung bestanden wurde oder nicht - sofern das Prüfungsverfahren schon abgeschlossen wurde.", example="true")
	public Boolean pruefungBestanden = null;
	
	/** Die Abiturnote einer bestandenen Abiturprüfung - sofern das Prüfungsverfahren schon abgeschlossen wurde. */
	@Schema(required = false, description = "Die Abiturnote einer bestandenen Abiturprüfung - sofern das Prüfungsverfahren schon abgeschlossen wurde.", example="3,1")
	public String note = null;

}
