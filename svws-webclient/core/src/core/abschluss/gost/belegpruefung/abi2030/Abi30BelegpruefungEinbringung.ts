import { JavaObject } from '../../../../../java/lang/JavaObject';
import { Fach } from '../../../../../asd/types/fach/Fach';
import { GostFach } from '../../../../../core/data/gost/GostFach';
import { Abi30BelegpruefungProjektkurse, cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2030_Abi30BelegpruefungProjektkurse } from '../../../../../core/abschluss/gost/belegpruefung/abi2030/Abi30BelegpruefungProjektkurse';
import { AbiturFachbelegung } from '../../../../../core/data/gost/AbiturFachbelegung';
import { ArrayList } from '../../../../../java/util/ArrayList';
import { GostBelegpruefungsArt } from '../../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { GostBelegpruefung } from '../../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager } from '../../../../../core/abschluss/gost/AbiturdatenManager';
import { GostFachbereich } from '../../../../../core/types/gost/GostFachbereich';
import { GostHalbjahr } from '../../../../../core/types/gost/GostHalbjahr';
import type { List } from '../../../../../java/util/List';
import { Class } from '../../../../../java/lang/Class';
import { GostBelegungsfehler } from '../../../../../core/abschluss/gost/GostBelegungsfehler';

export class Abi30BelegpruefungEinbringung extends GostBelegpruefung {

	/**
	 * Die Belegungen für das Fach Sport.
	 */
	private sport: List<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();

	/**
	 * Die Belegung des Faches Geschichte - oder null, falls es nicht belegt wurde.
	 */
	private geschichte: List<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();

	/**
	 * Die Belegung des Faches Sozialwissenschaften - oder null, falls es nicht belegt wurde.
	 */
	private sozialwissenschaften: List<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();

	/**
	 * Die Belegungen für das Fach Kunst.
	 */
	private kunst: List<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();

	/**
	 * Die Belegungen für das Fach Musik.
	 */
	private musik: List<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();

	/**
	 * Die Belegungen für das Fach Philosophie.
	 */
	private philosophie: List<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();

	/**
	 * Die Belegung im Gesellschaftwissenschaftlichen Bereich
	 */
	private gesellschaftswissenschaftenReligion: List<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();


	/**
	 * Erstellt eine neue Belegprüfung für Anzahl der einzubringenden Kurse. Die kann ggf. zu hoch sein.
	 *
	 * @param manager                 der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt           die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 * @param pruefungProjektkurse    das Ergebnis für die Belegprüfung der Projektkurse
	 */
	public constructor(manager: AbiturdatenManager, pruefungsArt: GostBelegpruefungsArt, pruefungProjektkurse: GostBelegpruefung) {
		super(manager, pruefungsArt, pruefungProjektkurse);
	}

	protected init(): void {
		this.sport = this.manager.getRelevanteFachbelegungen(GostFachbereich.SPORT);
		this.geschichte = this.manager.getRelevanteFachbelegungen(GostFachbereich.GESCHICHTE);
		this.sozialwissenschaften = this.manager.getRelevanteFachbelegungen(GostFachbereich.SOZIALWISSENSCHAFTEN);
		this.kunst = this.manager.getRelevanteFachbelegungenByFach(Fach.KU);
		this.musik = this.manager.getRelevanteFachbelegungenByFach(Fach.MU);
		this.philosophie = this.manager.getRelevanteFachbelegungen(GostFachbereich.PHILOSOPHIE);
		this.gesellschaftswissenschaftenReligion = this.manager.getRelevanteFachbelegungen(GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH_MIT_RELIGION);
	}

	protected pruefeEF1(): void {
		// empty block
	}

	protected pruefeGesamt(): void {
		const hatSportAbi: boolean = (this.sport.size() === 1) && (this.sport.get(0).abiturFach !== null);
		const anzahlGE: number = this.manager.zaehleBelegungInHalbjahren(this.geschichte, ...GostHalbjahr.getQualifikationsphase());
		const anzahlSW: number = this.manager.zaehleBelegungInHalbjahren(this.sozialwissenschaften, ...GostHalbjahr.getQualifikationsphase());
		if (hatSportAbi && (anzahlGE === 2) && (anzahlSW === 2)) {
			const hatKunstAbi: boolean = (this.kunst.size() === 1) && (this.kunst.get(0).abiturFach !== null);
			if (hatKunstAbi) {
				this.addFehler(GostBelegungsfehler.GOST30_EINBR_2);
				return;
			}
			const hatMusikAbi: boolean = (this.musik.size() === 1) && (this.musik.get(0).abiturFach !== null);
			if (hatMusikAbi) {
				this.addFehler(GostBelegungsfehler.GOST30_EINBR_3);
				return;
			}
			const pruefungProjektkurse: Abi30BelegpruefungProjektkurse = (cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2030_Abi30BelegpruefungProjektkurse(this.pruefungen_vorher[0]));
			const projektkurs: AbiturFachbelegung | null = pruefungProjektkurse.getProjektkurs();
			const hatPjkAbi: boolean = (projektkurs !== null) && (projektkurs.abiturFach !== null);
			const leitfach: AbiturFachbelegung | null = (projektkurs === null) || (projektkurs.idReferenzfach === null) ? null : this.manager.getFachbelegungByID(projektkurs.idReferenzfach);
			const anzahlPjkLeitfach: number = (leitfach === null) ? 0 : this.manager.zaehleHalbjahresbelegungen(leitfach, ...GostHalbjahr.getQualifikationsphase());
			if ((hatPjkAbi) && (anzahlPjkLeitfach === 2)) {
				this.addFehler(GostBelegungsfehler.GOST30_EINBR_4);
				return;
			}
			let anzahlGWAbi: number = 0;
			let anzahlREAbi: number = 0;
			for (const bel of this.gesellschaftswissenschaftenReligion) {
				const fach: GostFach | null = this.manager.faecher().get(bel.fachID);
				if ((fach === null) || JavaObject.equalsTranspiler("GE", (fach.kuerzel)) || JavaObject.equalsTranspiler("SW", (fach.kuerzel))) {
					continue;
				}
				if (bel.abiturFach !== null) {
					if (GostFachbereich.RELIGION.hat(fach)) {
						anzahlREAbi++;
					} else {
						anzahlGWAbi++;
					}
				}
			}
			if (anzahlGWAbi === 2) {
				this.addFehler(GostBelegungsfehler.GOST30_EINBR_5);
			}
			if ((anzahlGWAbi === 1) && (anzahlREAbi === 1)) {
				this.addFehler(GostBelegungsfehler.GOST30_EINBR_6);
			}
		}
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
