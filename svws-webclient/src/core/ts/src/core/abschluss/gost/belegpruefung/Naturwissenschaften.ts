import { GostFachbereich } from '../../../../core/types/gost/GostFachbereich';
import { AbiturFachbelegung } from '../../../../core/data/gost/AbiturFachbelegung';
import { GostBelegpruefungsArt } from '../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { GostSchriftlichkeit } from '../../../../core/types/gost/GostSchriftlichkeit';
import { GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import { List } from '../../../../java/util/List';
import { GostBelegpruefung } from '../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager } from '../../../../core/abschluss/gost/AbiturdatenManager';
import { GostBelegungsfehler } from '../../../../core/abschluss/gost/GostBelegungsfehler';

export class Naturwissenschaften extends GostBelegpruefung {

	private naturwissenschaften : List<AbiturFachbelegung> | null = null;

	private naturwissenschaften_klassisch : List<AbiturFachbelegung> | null = null;

	private anzahl_durchgehend : number = 0;

	private anzahl_schriftlich_durchgehend : number = 0;


	/**
	 * Erstellt eine neue Belegprüfung für das Fach Mathematik.
	 *
	 * @param manager        der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public constructor(manager : AbiturdatenManager, pruefungsArt : GostBelegpruefungsArt) {
		super(manager, pruefungsArt);
	}

	protected init() : void {
		this.naturwissenschaften = this.manager.getFachbelegungen(GostFachbereich.NATURWISSENSCHAFTLICH);
		this.naturwissenschaften_klassisch = this.manager.getFachbelegungen(GostFachbereich.NATURWISSENSCHAFTLICH_KLASSISCH);
		this.anzahl_durchgehend = 0;
		this.anzahl_schriftlich_durchgehend = 0;
	}

	protected pruefeEF1() : void {
		if (!this.manager.pruefeBelegungDurchgehendBelegbarExistiert(this.naturwissenschaften_klassisch, GostSchriftlichkeit.BELIEBIG, GostHalbjahr.EF1))
			this.addFehler(GostBelegungsfehler.NW_10);
		if (!this.manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(this.naturwissenschaften_klassisch, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1))
			this.addFehler(GostBelegungsfehler.NW_11);
		let fachbelegungen : List<AbiturFachbelegung> | null = this.manager.filterDurchgehendBelegbar(this.naturwissenschaften);
		fachbelegungen = this.manager.filterBelegungen(fachbelegungen, GostHalbjahr.EF1);
		this.anzahl_durchgehend = fachbelegungen === null ? 0 : fachbelegungen.size();
		fachbelegungen = this.manager.filterBelegungenMitSchriftlichkeit(fachbelegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1);
		this.anzahl_schriftlich_durchgehend = fachbelegungen === null ? 0 : fachbelegungen.size();
	}

	protected pruefeGesamt() : void {
		if (!this.manager.pruefeBelegungExistiert(this.naturwissenschaften_klassisch, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22))
			this.addFehler(GostBelegungsfehler.NW_10);
		if ((!this.manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(this.naturwissenschaften_klassisch, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1)) || (!this.manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(this.naturwissenschaften_klassisch, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF2)))
			this.addFehler(GostBelegungsfehler.NW_11);
		this.anzahl_durchgehend = this.manager.zaehleBelegungenDurchgaengig(this.naturwissenschaften);
		this.anzahl_schriftlich_durchgehend = this.manager.zaehleBelegungenDurchgaengigSchriftlichInQPhase(this.naturwissenschaften);
	}

	/**
	 * Gibt die Anzahl der durchgehend belegten bzw. belegbaren Naturwissenschaften zurück.
	 *
	 * @return die Anzahl der durchgehend belegten bzw. belegbaren Naturwissenschaften zurück.
	 */
	public getAnzahlDurchgehendBelegt() : number {
		return this.anzahl_durchgehend;
	}

	/**
	 * Gibt die Anzahl der durchgehend schriftlich belegten bzw. belegbaren Naturwissenschaften zurück.
	 * Durchgehend schriftlich bedeutet, dass das Fach mind. von Q1.1 bus Q2.1 schriftlich belegt wurde.
	 *
	 * @return die Anzahl der durchgehend schriftlich belegten bzw. belegbaren Naturwissenschaften zurück.
	 */
	public getAnzahlDurchgehendSchritflichBelegt() : number {
		return this.anzahl_schriftlich_durchgehend;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.belegpruefung.Naturwissenschaften', 'de.svws_nrw.core.abschluss.gost.GostBelegpruefung'].includes(name);
	}

}

export function cast_de_svws_nrw_core_abschluss_gost_belegpruefung_Naturwissenschaften(obj : unknown) : Naturwissenschaften {
	return obj as Naturwissenschaften;
}
