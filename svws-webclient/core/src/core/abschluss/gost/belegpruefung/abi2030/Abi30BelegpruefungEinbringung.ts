import { GostFach } from '../../../../../core/data/gost/GostFach';
import { Abi30BelegpruefungProjektkurse } from '../../../../../core/abschluss/gost/belegpruefung/abi2030/Abi30BelegpruefungProjektkurse';
import { Abi30BelegpruefungSchwerpunkt } from '../../../../../core/abschluss/gost/belegpruefung/abi2030/Abi30BelegpruefungSchwerpunkt';
import { GostAbiturFach } from '../../../../../core/types/gost/GostAbiturFach';
import { Abi30BelegpruefungKurszahlenUndWochenstunden } from '../../../../../core/abschluss/gost/belegpruefung/abi2030/Abi30BelegpruefungKurszahlenUndWochenstunden';
import { AbiturFachbelegung } from '../../../../../core/data/gost/AbiturFachbelegung';
import { ArrayList } from '../../../../../java/util/ArrayList';
import { GostBelegpruefungsArt } from '../../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { GostBelegpruefung } from '../../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager } from '../../../../../core/abschluss/gost/AbiturdatenManager';
import { Abi30BelegpruefungAbiFaecher } from '../../../../../core/abschluss/gost/belegpruefung/abi2030/Abi30BelegpruefungAbiFaecher';
import { GostFachbereich } from '../../../../../core/types/gost/GostFachbereich';
import { GostHalbjahr } from '../../../../../core/types/gost/GostHalbjahr';
import type { List } from '../../../../../java/util/List';
import { Class } from '../../../../../java/lang/Class';
import { GostBelegungsfehler } from '../../../../../core/abschluss/gost/GostBelegungsfehler';

export class Abi30BelegpruefungEinbringung extends GostBelegpruefung {

	private readonly pruefungProjektkurse: Abi30BelegpruefungProjektkurse;

	private readonly pruefungSchwerpunkt: Abi30BelegpruefungSchwerpunkt;

	private readonly pruefungAbiFaecher: Abi30BelegpruefungAbiFaecher;

	private readonly pruefungKurszahlenUndWochenstunden: Abi30BelegpruefungKurszahlenUndWochenstunden;

	/**
	 * Die maximale Anzahl der Kurse die während der Berechnung noch einbringungspflichtig sein können.
	 */
	private kurseOberereGrenze: number = -1;

	/**
	 * Die minimale Anzahl der Kurse die - soweit bereits erkannt - einbringungspflichtig sind.
	 */
	private kurseUntereGrenze: number = -1;

	/**
	 * Gibt an, ob der gewählte Projektkurs Abiturfach ist oder nicht.
	 */
	private hatPjkAbi: boolean = false;

	/**
	 * Das Referenzfach des Projektkurses
	 */
	private referenzfach: GostFach = new GostFach();

	/**
	 * Gibt an, ob bereits eine durchgängige Gesellschaftwissenschaft gefunden wurde.
	 */
	private hatGWDurchgaengig: boolean = false;

	/**
	 * Die Belegung des Faches Sport
	 */
	private sport: List<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();

	/**
	 * Die anzahl der in der Q-Phase belegten Sport-Kurse
	 */
	private anzahlSportKurse: number = 0;


	/**
	 * Erstellt eine neue Belegprüfung für Anzahl der einzubringenden Kurse. Die kann ggf. zu hoch sein.
	 *
	 * @param manager                der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt           die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 * @param pruefungProjektkurse   das Ergebnis für die Belegprüfung der Projektkurse
	 * @param pruefungSchwerpunkt    das Ergebnis für die Belegprüfung zum Schwerpunkt
	 * @param pruefungAbiFaecher     das Ergebnis für die Belegprüfung zu den Abitur-Fächern
	 * @param pruefungKurszahlenUndWochenstunden   das Ergebnis für die Belegprüfung zu den Kurszahlen und Wochenstunden
	 */
	public constructor(manager: AbiturdatenManager, pruefungsArt: GostBelegpruefungsArt, pruefungProjektkurse: Abi30BelegpruefungProjektkurse, pruefungSchwerpunkt: Abi30BelegpruefungSchwerpunkt, pruefungAbiFaecher: Abi30BelegpruefungAbiFaecher, pruefungKurszahlenUndWochenstunden: Abi30BelegpruefungKurszahlenUndWochenstunden) {
		super(manager, pruefungsArt);
		this.pruefungProjektkurse = pruefungProjektkurse;
		this.pruefungSchwerpunkt = pruefungSchwerpunkt;
		this.pruefungAbiFaecher = pruefungAbiFaecher;
		this.pruefungKurszahlenUndWochenstunden = pruefungKurszahlenUndWochenstunden;
	}

	protected init(): void {
		const projektkurs: AbiturFachbelegung | null = this.pruefungProjektkurse.getProjektkurs();
		const referenzfachBelegung: AbiturFachbelegung | null = (projektkurs === null) || (projektkurs.idReferenzfach === null) ? null : this.manager.getFachbelegungByID(projektkurs.idReferenzfach);
		this.hatPjkAbi = (projektkurs !== null) && (projektkurs.abiturFach !== null);
		const refFach: GostFach | null = this.manager.getFach(referenzfachBelegung);
		if (refFach !== null) {
			this.referenzfach = refFach;
		}
		this.hatGWDurchgaengig = this.hatAbifachGWOhnePJK();
		this.sport = this.manager.getRelevanteFachbelegungen(GostFachbereich.SPORT);
		this.anzahlSportKurse = this.manager.zaehleBelegungInHalbjahrenOhneAT(this.sport, ...GostHalbjahr.getQualifikationsphase());
	}

	protected pruefeEF1(): void {
		// empty block
	}

	private hatAbifachGWOhnePJK(): boolean {
		for (const abifach of GostAbiturFach.values()) {
			const bel: AbiturFachbelegung | null = this.pruefungAbiFaecher.getAbiturfach(abifach);
			const fach: GostFach | null = this.manager.getFach(bel);
			if (fach === null) {
				continue;
			}
			if (GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH.hat(fach)) {
				return true;
			}
		}
		return false;
	}

	private pruefeSport(): void {
		const hatSportAbi: boolean = (this.sport.size() === 1) && (this.sport.get(0).abiturFach !== null);
		const hatSportAbiPJK: boolean = this.hatPjkAbi && GostFachbereich.SPORT.hat(this.referenzfach);
		if (hatSportAbi) {
			this.kurseUntereGrenze += 4;
		} else
			if (hatSportAbiPJK) {
				this.kurseUntereGrenze += 2;
				this.kurseOberereGrenze -= 2;
			} else {
				this.kurseOberereGrenze -= 4;
			}
	}

	private pruefeVertiefungskurse(): void {
		const vertiefungskurse: List<AbiturFachbelegung> = this.manager.getRelevanteFachbelegungen(GostFachbereich.VERTIEFUNGSKURSE);
		const anzahlVT: number = this.manager.zaehleBelegungInHalbjahren(vertiefungskurse, ...GostHalbjahr.getQualifikationsphase());
		this.kurseOberereGrenze -= anzahlVT;
	}

	private pruefeFremdsprachenUndNaturwissenschaften(): void {
		const listNwNeu: List<AbiturFachbelegung> = this.manager.getRelevanteFachbelegungen(GostFachbereich.NATURWISSENSCHAFTLICH_NEU_EINSETZEND);
		let hatNwNeuAbi: boolean = false;
		let anzahlKurseNwPjkAbi: number = 0;
		let anzahlKurseNw: number = 0;
		let anzahlNwAbi: number = 0;
		for (const nwNeu of listNwNeu) {
			const anzahl: number = this.manager.zaehleHalbjahresbelegungen(nwNeu, ...GostHalbjahr.getQualifikationsphase());
			anzahlKurseNw += anzahl;
			if ((anzahl === 4) && (nwNeu.abiturFach !== null)) {
				hatNwNeuAbi = true;
				anzahlNwAbi++;
			} else
				if (this.hatPjkAbi && (nwNeu.fachID === this.referenzfach.id)) {
					anzahlKurseNwPjkAbi += anzahl;
				}
		}
		const listNwKl: List<AbiturFachbelegung> = this.manager.getRelevanteFachbelegungen(GostFachbereich.NATURWISSENSCHAFTLICH_KLASSISCH);
		for (const nwKl of listNwKl) {
			const anzahl: number = this.manager.zaehleHalbjahresbelegungen(nwKl, ...GostHalbjahr.getQualifikationsphase());
			anzahlKurseNw += anzahl;
			if ((anzahl === 4) && (nwKl.abiturFach !== null)) {
				anzahlNwAbi++;
			} else
				if (this.hatPjkAbi && (nwKl.fachID === this.referenzfach.id)) {
					anzahlKurseNwPjkAbi += anzahl;
				}
		}
		const listFS: List<AbiturFachbelegung> = this.manager.getRelevanteFachbelegungen(GostFachbereich.FREMDSPRACHE);
		let anzahlKurseFs: number = 0;
		let anzahlFsAbi: number = 0;
		let anzahlKurseFsPjkAbi: number = 0;
		for (const fs of listFS) {
			const anzahl: number = this.manager.zaehleHalbjahresbelegungen(fs, ...GostHalbjahr.getQualifikationsphase());
			anzahlKurseFs += anzahl;
			if ((anzahl === 4) && (fs.abiturFach !== null)) {
				anzahlFsAbi++;
			}
			if (this.hatPjkAbi && (fs.fachID === this.referenzfach.id)) {
				anzahlKurseFsPjkAbi += anzahl;
			}
		}
		this.kurseUntereGrenze += 8;
		anzahlKurseFs -= 4;
		anzahlKurseNw -= 4;
		if (this.pruefungSchwerpunkt.hatNurSchwerpunktFS()) {
			if ((anzahlFsAbi === 2) || ((anzahlFsAbi === 1) && (anzahlKurseFsPjkAbi > 1))) {
				this.kurseUntereGrenze += 2;
				anzahlKurseFs -= 2;
			}
		} else
			if (this.pruefungSchwerpunkt.hatNurSchwerpunktNW()) {
				if (hatNwNeuAbi || (anzahlNwAbi === 2) || ((anzahlNwAbi === 1) && (anzahlKurseNwPjkAbi > 1))) {
					this.kurseUntereGrenze += 2;
					anzahlKurseNw -= 2;
				}
			} else {
				if ((anzahlFsAbi === 2) || ((anzahlFsAbi === 1) && (anzahlKurseFsPjkAbi > 1))) {
					this.kurseUntereGrenze += 2;
					anzahlKurseFs -= 2;
				}
				if (hatNwNeuAbi || (anzahlNwAbi === 2) || ((anzahlNwAbi === 1) && (anzahlKurseNwPjkAbi > 1))) {
					this.kurseUntereGrenze += 2;
					anzahlKurseNw -= 2;
				}
			}
		if ((anzahlKurseNwPjkAbi > 1) && (anzahlKurseNwPjkAbi < 4)) {
			this.kurseUntereGrenze += 2;
			anzahlKurseNw -= 2;
		}
		if ((anzahlKurseFsPjkAbi > 1) && (anzahlKurseFsPjkAbi < 4)) {
			this.kurseUntereGrenze += 2;
			anzahlKurseFs -= 2;
		}
		if (anzahlKurseFs >= 2) {
			this.kurseUntereGrenze += 2;
			anzahlKurseFs -= 2;
		} else {
			this.kurseUntereGrenze += 2;
			anzahlKurseNw -= 2;
		}
		this.kurseOberereGrenze -= anzahlKurseFs;
		this.kurseOberereGrenze -= anzahlKurseNw;
	}

	private pruefeKunstMusikUndLiteratur(): void {
		const kumuli: List<AbiturFachbelegung> = this.manager.getRelevanteFachbelegungen(GostFachbereich.KUNST_MUSIK_LITERATUR);
		let anzahlKurseKML: number = 0;
		let anzahlKMLAbi: number = 0;
		for (const kml of kumuli) {
			const anzahl: number = this.manager.zaehleHalbjahresbelegungen(kml, ...GostHalbjahr.getQualifikationsphase());
			anzahlKurseKML += anzahl;
			if ((anzahl === 4) && (kml.abiturFach !== null)) {
				anzahlKMLAbi++;
			}
		}
		if (anzahlKMLAbi > 0) {
			this.kurseUntereGrenze += anzahlKMLAbi * 4;
			this.kurseOberereGrenze -= (anzahlKurseKML - (anzahlKMLAbi * 4));
		} else {
			this.kurseUntereGrenze += 2;
			this.kurseOberereGrenze -= (anzahlKurseKML - 2);
		}
	}

	private pruefeReligion(): number {
		let anzahlErsatzfachRE: number = 0;
		const listRE: List<AbiturFachbelegung> = this.manager.getRelevanteFachbelegungen(GostFachbereich.RELIGION);
		let istREAbi: boolean = false;
		for (const religion of listRE) {
			if (religion.abiturFach !== null) {
				istREAbi = true;
			}
		}
		const anzahl: number = this.manager.zaehleBelegungInHalbjahren(listRE, ...GostHalbjahr.getQualifikationsphase());
		if (((anzahl === 4) && istREAbi)) {
			this.kurseUntereGrenze += 4;
		} else
			if (anzahl >= 2) {
				this.kurseUntereGrenze += 2;
				this.kurseOberereGrenze -= (anzahl - 2);
			} else
				if (anzahl === 1) {
					this.kurseUntereGrenze += 1;
					anzahlErsatzfachRE = 1;
				} else
					if (anzahl === 0) {
						anzahlErsatzfachRE = 2;
					}
		return anzahlErsatzfachRE;
	}

	private pruefePhilosophie(anzahlErsatzfachRE: number, istGEDurchgaengig: boolean, istSWDurchgaengig: boolean): number {
		let istErsatzfachRE: boolean = false;
		const listPL: List<AbiturFachbelegung> = this.manager.getRelevanteFachbelegungen(GostFachbereich.PHILOSOPHIE);
		const anzahlPL: number = this.manager.zaehleBelegungInHalbjahren(listPL, ...GostHalbjahr.getQualifikationsphase());
		const istPLDurchgaengig: boolean = (!this.hatGWDurchgaengig) && (anzahlPL === 4) && !istGEDurchgaengig && !istSWDurchgaengig;
		if (listPL.size() === 1) {
			const philosophie: AbiturFachbelegung = listPL.getFirst();
			if (((anzahlPL === 4) && (philosophie.abiturFach !== null))) {
				this.kurseUntereGrenze += 4;
			} else
				if (this.hatPjkAbi && (philosophie.fachID === this.referenzfach.id)) {
					this.kurseUntereGrenze += 2;
				} else
					if (anzahlPL > 0) {
						if (istPLDurchgaengig) {
							this.kurseUntereGrenze += 4;
							istErsatzfachRE = true;
							this.hatGWDurchgaengig = true;
						} else
							if (anzahlErsatzfachRE > 0) {
								this.kurseUntereGrenze += anzahlErsatzfachRE;
								this.kurseOberereGrenze -= (anzahlPL - anzahlErsatzfachRE);
								istErsatzfachRE = true;
							} else {
								this.kurseOberereGrenze -= anzahlPL;
							}
					}
		}
		return istErsatzfachRE ? 0 : anzahlErsatzfachRE;
	}

	private pruefeGeschichte(geschichte: List<AbiturFachbelegung>, anzahlGE: number, anzahlErsatzfachRE: number, istGEDurchgaengig: boolean): number {
		let istErsatzfachRE: boolean = false;
		let istGEAbi: boolean = false;
		for (const ge of geschichte) {
			if (ge.abiturFach !== null) {
				istGEAbi = true;
			}
		}
		const istGEErsatzfachRE: boolean = (anzahlErsatzfachRE > 0) && ((anzahlGE - 2) - anzahlErsatzfachRE >= 0);
		let restKurse: number = anzahlGE;
		if (istGEAbi || (!this.hatGWDurchgaengig && istGEDurchgaengig)) {
			this.kurseUntereGrenze += 4;
			restKurse -= 4;
			if (istGEErsatzfachRE) {
				istErsatzfachRE = true;
			}
		} else {
			this.kurseUntereGrenze += 2;
			restKurse -= 2;
		}
		if (istGEErsatzfachRE) {
			this.kurseUntereGrenze += anzahlErsatzfachRE;
			this.kurseOberereGrenze += anzahlErsatzfachRE;
			istErsatzfachRE = true;
		}
		this.kurseOberereGrenze -= restKurse;
		return istErsatzfachRE ? 0 : anzahlErsatzfachRE;
	}

	private pruefeSozialwissenschaften(sozialwissenschaften: List<AbiturFachbelegung>, anzahlSW: number, anzahlErsatzfachRE: number, istGEDurchgaengig: boolean, istSWDurchgaengig: boolean): number {
		let istErsatzfachRE: boolean = false;
		let istSWAbi: boolean = false;
		for (const sw of sozialwissenschaften) {
			if (sw.abiturFach !== null) {
				istSWAbi = true;
			}
		}
		const istSWErsatzfachRE: boolean = (anzahlErsatzfachRE > 0) && ((anzahlSW - 2) - anzahlErsatzfachRE >= 0);
		let restKurse: number = anzahlSW;
		if (istSWAbi || (!this.hatGWDurchgaengig && istSWDurchgaengig && !istGEDurchgaengig)) {
			this.kurseUntereGrenze += 4;
			restKurse -= 4;
			if (istSWErsatzfachRE) {
				istErsatzfachRE = true;
			}
		} else {
			this.kurseUntereGrenze += 2;
			restKurse -= 2;
		}
		if (istSWErsatzfachRE) {
			this.kurseUntereGrenze += anzahlErsatzfachRE;
			this.kurseOberereGrenze += anzahlErsatzfachRE;
			istErsatzfachRE = true;
		}
		this.kurseOberereGrenze -= restKurse;
		return istErsatzfachRE ? 0 : anzahlErsatzfachRE;
	}

	private pruefeSonstigeGesellschaftswissenschaft(gw: AbiturFachbelegung, anzahlErsatzfachRE: number): number {
		let anzahl: number = this.manager.zaehleHalbjahresbelegungen(gw, ...GostHalbjahr.getQualifikationsphase());
		const istErsatzfachRE: boolean = (anzahlErsatzfachRE > 0) && (anzahl - anzahlErsatzfachRE >= 0);
		const nimmAlsDurchgaengigeGW: boolean = !this.hatGWDurchgaengig && (anzahl === 4);
		if (nimmAlsDurchgaengigeGW) {
			this.hatGWDurchgaengig = true;
		}
		if (gw.abiturFach !== null) {
			this.kurseUntereGrenze += 4;
			anzahl -= 4;
		} else
			if (this.hatPjkAbi && (gw.fachID === this.referenzfach.id)) {
				this.kurseUntereGrenze += 2;
				anzahl -= 2;
			}
		if (nimmAlsDurchgaengigeGW) {
			this.kurseUntereGrenze += anzahl;
		} else {
			if (istErsatzfachRE) {
				this.kurseUntereGrenze += anzahlErsatzfachRE;
				this.kurseOberereGrenze += anzahlErsatzfachRE;
			}
			this.kurseOberereGrenze -= anzahl;
		}
		return istErsatzfachRE ? 0 : anzahlErsatzfachRE;
	}

	private pruefeSonstigeGesellschaftswissenschaften(anzahlErsatzfachRE: number): void {
		let kurseErsatzfachRE: number = anzahlErsatzfachRE;
		const gesellschaftswissenschaften: List<AbiturFachbelegung> = this.manager.getRelevanteFachbelegungen(GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH_SONSTIGE);
		for (const gw of gesellschaftswissenschaften) {
			kurseErsatzfachRE = this.pruefeSonstigeGesellschaftswissenschaft(gw, kurseErsatzfachRE);
		}
	}

	private pruefeGesellschaftswissenschaftenUndReligion(): void {
		let anzahlErsatzfachRE: number = this.pruefeReligion();
		const geschichte: List<AbiturFachbelegung> = this.manager.getRelevanteFachbelegungen(GostFachbereich.GESCHICHTE);
		const anzahlGE: number = this.manager.zaehleBelegungInHalbjahren(geschichte, ...GostHalbjahr.getQualifikationsphase());
		const istGEDurchgaengig: boolean = (!this.hatGWDurchgaengig) && (anzahlGE === 4);
		const sozialwissenschaften: List<AbiturFachbelegung> = this.manager.getRelevanteFachbelegungen(GostFachbereich.SOZIALWISSENSCHAFTEN);
		const anzahlSW: number = this.manager.zaehleBelegungInHalbjahren(sozialwissenschaften, ...GostHalbjahr.getQualifikationsphase());
		const istSWDurchgaengig: boolean = (!this.hatGWDurchgaengig) && (anzahlSW === 4);
		anzahlErsatzfachRE = this.pruefePhilosophie(anzahlErsatzfachRE, istGEDurchgaengig, istSWDurchgaengig);
		anzahlErsatzfachRE = this.pruefeGeschichte(geschichte, anzahlGE, anzahlErsatzfachRE, istGEDurchgaengig);
		anzahlErsatzfachRE = this.pruefeSozialwissenschaften(sozialwissenschaften, anzahlSW, anzahlErsatzfachRE, istGEDurchgaengig, istSWDurchgaengig);
		this.hatGWDurchgaengig = this.hatGWDurchgaengig || istGEDurchgaengig || istSWDurchgaengig;
		this.pruefeSonstigeGesellschaftswissenschaften(anzahlErsatzfachRE);
	}

	protected pruefeGesamt(): void {
		if ((this.pruefungKurszahlenUndWochenstunden.getBlockIAnzahlAnrechenbar() !== 40) || (this.referenzfach.id === -1) || (this.anzahlSportKurse !== 4)) {
			return;
		}
		this.kurseOberereGrenze = 40;
		this.kurseUntereGrenze = 10;
		this.pruefeSport();
		this.pruefeVertiefungskurse();
		this.pruefeFremdsprachenUndNaturwissenschaften();
		this.pruefeKunstMusikUndLiteratur();
		this.pruefeGesellschaftswissenschaftenUndReligion();
		if (this.kurseOberereGrenze > 36) {
			this.addFehler(GostBelegungsfehler.GOST30_EINBR_1);
		}
	}

	/**
	 * Gibt zurück, ob das Ergebnis dieser Prüfung ein exaktes Ergebnis geliefert hat. (Zur Fehlersuche)
	 *
	 * @return true, wenn die beiden Grenzen bei der Berechnung übereinstimmen und ansonsten false
	 */
	public istExakt(): boolean {
		return (this.kurseOberereGrenze === this.kurseUntereGrenze);
	}

	/**
	 * Gibt die Anzahl der einbringungspflichtigen Kurse zurück, sofern die Berechnung erfolgreich war
	 *
	 * @return die Anzahl der einbringungspflichtigen Kurse
	 */
	public getAnzahlEinbrungungspflichtigerKurse(): number {
		return this.kurseOberereGrenze;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungEinbringung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.GostBelegpruefung', 'de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungEinbringung'].includes(name);
	}

	public static readonly class = new Class<Abi30BelegpruefungEinbringung>('de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungEinbringung');

}

export function cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2030_Abi30BelegpruefungEinbringung(obj: unknown): Abi30BelegpruefungEinbringung {
	return obj as Abi30BelegpruefungEinbringung;
}
