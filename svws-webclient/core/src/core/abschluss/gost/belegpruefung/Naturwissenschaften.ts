import { GostFachbereich } from '../../../../core/types/gost/GostFachbereich';
import { AbiturFachbelegung } from '../../../../core/data/gost/AbiturFachbelegung';
import { GostBelegpruefungsArt } from '../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { GostSchriftlichkeit } from '../../../../core/types/gost/GostSchriftlichkeit';
import { GostHalbjahr } from '../../../../core/types/gost/GostHalbjahr';
import type { List } from '../../../../java/util/List';
import { GostBelegpruefung } from '../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager } from '../../../../core/abschluss/gost/AbiturdatenManager';
import { GostBelegungsfehler } from '../../../../core/abschluss/gost/GostBelegungsfehler';

export class Naturwissenschaften extends GostBelegpruefung {

	private _naturwissenschaften : List<AbiturFachbelegung> | null = null;

	private _naturwissenschaftenKlassisch : List<AbiturFachbelegung> | null = null;

	private _anzahlDurchgehend : number = 0;

	private _anzahlDurchgehendSchriftlich : number = 0;


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
		this._naturwissenschaften = this.manager.getRelevanteFachbelegungen(GostFachbereich.NATURWISSENSCHAFTLICH);
		this._naturwissenschaftenKlassisch = this.manager.getRelevanteFachbelegungen(GostFachbereich.NATURWISSENSCHAFTLICH_KLASSISCH);
		this._anzahlDurchgehend = 0;
		this._anzahlDurchgehendSchriftlich = 0;
	}

	protected pruefeEF1() : void {
		if (!this.manager.pruefeBelegungDurchgehendBelegbarExistiert(this._naturwissenschaftenKlassisch, GostSchriftlichkeit.BELIEBIG, GostHalbjahr.EF1))
			this.addFehler(GostBelegungsfehler.NW_10);
		if (!this.manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(this._naturwissenschaftenKlassisch, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1))
			this.addFehler(GostBelegungsfehler.NW_11);
		let fachbelegungen : List<AbiturFachbelegung> | null = this.manager.filterDurchgehendBelegbar(this._naturwissenschaften);
		fachbelegungen = this.manager.filterBelegungen(fachbelegungen, GostHalbjahr.EF1);
		this._anzahlDurchgehend = fachbelegungen === null ? 0 : fachbelegungen.size();
		fachbelegungen = this.manager.filterBelegungenMitSchriftlichkeit(fachbelegungen, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1);
		this._anzahlDurchgehendSchriftlich = fachbelegungen === null ? 0 : fachbelegungen.size();
	}

	protected pruefeGesamt() : void {
		if (!this.manager.pruefeBelegungExistiert(this._naturwissenschaftenKlassisch, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22))
			this.addFehler(GostBelegungsfehler.NW_10);
		if ((!this.manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(this._naturwissenschaftenKlassisch, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1)) || (!this.manager.pruefeBelegungExistiertMitSchriftlichkeitEinzeln(this._naturwissenschaftenKlassisch, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF2)))
			this.addFehler(GostBelegungsfehler.NW_11);
		this._anzahlDurchgehend = this.manager.zaehleBelegungenDurchgaengig(this._naturwissenschaften);
		this._anzahlDurchgehendSchriftlich = this.manager.zaehleBelegungenDurchgaengigSchriftlichInQPhase(this._naturwissenschaften);
	}

	/**
	 * Gibt die Anzahl der durchgehend belegten bzw. belegbaren Naturwissenschaften zurück.
	 *
	 * @return die Anzahl der durchgehend belegten bzw. belegbaren Naturwissenschaften zurück.
	 */
	public getAnzahlDurchgehendBelegt() : number {
		return this._anzahlDurchgehend;
	}

	/**
	 * Gibt die Anzahl der durchgehend schriftlich belegten bzw. belegbaren Naturwissenschaften zurück.
	 * Durchgehend schriftlich bedeutet, dass das Fach mind. von Q1.1 bus Q2.1 schriftlich belegt wurde.
	 *
	 * @return die Anzahl der durchgehend schriftlich belegten bzw. belegbaren Naturwissenschaften zurück.
	 */
	public getAnzahlDurchgehendSchritflichBelegt() : number {
		return this._anzahlDurchgehendSchriftlich;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.gost.belegpruefung.Naturwissenschaften';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.belegpruefung.Naturwissenschaften', 'de.svws_nrw.core.abschluss.gost.GostBelegpruefung'].includes(name);
	}

}

export function cast_de_svws_nrw_core_abschluss_gost_belegpruefung_Naturwissenschaften(obj : unknown) : Naturwissenschaften {
	return obj as Naturwissenschaften;
}
