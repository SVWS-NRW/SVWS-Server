import { GostLaufbahnplanungGKLKlausurvorgabe } from '../../../../../core/utils/gost/GostLaufbahnplanungGKLKlausurvorgabe';
import type { JavaSet } from '../../../../../java/util/JavaSet';
import { HashMap } from '../../../../../java/util/HashMap';
import { AbiturFachbelegung } from '../../../../../core/data/gost/AbiturFachbelegung';
import { ArrayList } from '../../../../../java/util/ArrayList';
import { GostBelegpruefungsArt } from '../../../../../core/abschluss/gost/GostBelegpruefungsArt';
import { AbiturFachbelegungHalbjahr } from '../../../../../core/data/gost/AbiturFachbelegungHalbjahr';
import { GostBelegpruefung } from '../../../../../core/abschluss/gost/GostBelegpruefung';
import { AbiturdatenManager } from '../../../../../core/abschluss/gost/AbiturdatenManager';
import { GostKursart } from '../../../../../core/types/gost/GostKursart';
import { GostFachbereich } from '../../../../../core/types/gost/GostFachbereich';
import { GostHalbjahr } from '../../../../../core/types/gost/GostHalbjahr';
import { GostSchuelerGKLWahl } from '../../../../../core/data/gost/GostSchuelerGKLWahl';
import type { List } from '../../../../../java/util/List';
import { Class } from '../../../../../java/lang/Class';
import type { JavaMap } from '../../../../../java/util/JavaMap';
import { HashSet } from '../../../../../java/util/HashSet';
import { GostBelegungsfehler } from '../../../../../core/abschluss/gost/GostBelegungsfehler';

export class Abi30BelegpruefungGKL extends GostBelegpruefung {

	/**
	 * die Schülerwahlen für die gleichwertigen komplexen Leistungsnachweise (GKL)
	 */
	private readonly gklWahlen: GostSchuelerGKLWahl;

	/**
	 * eine Map mit der Klausurvorgaben zugeordnet zu ihrer ID
	 */
	private readonly mapKlausurvorgaben: JavaMap<number, GostLaufbahnplanungGKLKlausurvorgabe>;

	/**
	 * Die Belegung im Aufgabenfeld 1
	 */
	private belegungenAF1: List<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();

	/**
	 * Die Belegung im Aufgabenfeld 2 (mit Religion)
	 */
	private belegungenAF2: List<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();

	/**
	 * Die Belegung im Aufgabenfeld 3
	 */
	private belegungenAF3: List<AbiturFachbelegung> = new ArrayList<AbiturFachbelegung>();

	/**
	 * Die für GKLs möglichen Halbjahre in der Qualifikationsphase
	 */
	private static readonly gklHalbjahreQ: List<GostHalbjahr> = ArrayList.of(GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21);

	/**
	 * Eine Map, welche den Fach-IDs eine Menge von Halbjahren in der QPhase zuordnet, wo eine GKL möglich ist.
	 */
	private readonly mapGKLMoeglichQPhase: JavaMap<number, JavaSet<GostHalbjahr>> = new HashMap<number, JavaSet<GostHalbjahr>>();


	/**
	 * Erstellt eine neue Belegprüfung für die gleichwertigen komplexen Leistungsnachweise (GKL).
	 *
	 * @param manager              der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt         die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 * @param gklWahlen            die Schülerwahlen für die gleichwertigen komplexen Leistungsnachweise (GKL)
	 * @param mapKlausurvorgaben   eine Map mit der Klausurvorgaben zugeordnet zu ihrer ID
	 */
	public constructor(manager: AbiturdatenManager, pruefungsArt: GostBelegpruefungsArt, gklWahlen: GostSchuelerGKLWahl, mapKlausurvorgaben: JavaMap<number, GostLaufbahnplanungGKLKlausurvorgabe>) {
		super(manager, pruefungsArt);
		this.gklWahlen = gklWahlen;
		this.mapKlausurvorgaben = mapKlausurvorgaben;
	}

	protected init(): void {
		this.belegungenAF1 = this.manager.getRelevanteFachbelegungen(GostFachbereich.SPRACHLICH_LITERARISCH_KUENSTLERISCH);
		this.belegungenAF2 = this.manager.getRelevanteFachbelegungen(GostFachbereich.GESELLSCHAFTSWISSENSCHAFTLICH_MIT_RELIGION);
		this.belegungenAF3 = this.manager.getRelevanteFachbelegungen(GostFachbereich.MATHEMATISCH_NATURWISSENSCHAFTLICH);
		for (const vorgabe of this.mapKlausurvorgaben.values()) {
			if (vorgabe.getVorgabe().istGklMoeglich && Abi30BelegpruefungGKL.gklHalbjahreQ.contains(vorgabe.getHalbjahr())) {
				const setHalbjahre: JavaSet<GostHalbjahr> | null = this.mapGKLMoeglichQPhase.computeIfAbsent(vorgabe.getFach().id, { apply: (k: number | null) => new HashSet<GostHalbjahr>() });
				if (setHalbjahre !== null) {
					setHalbjahre.add(vorgabe.getHalbjahr());
				}
			}
		}
	}

	protected pruefeEF1(): void {
		// empty block
	}

	private static hatSchriftlicheGKBelegungInEF(belegungen: List<AbiturFachbelegung>): boolean {
		for (const bel of belegungen) {
			for (const halbjahr of GostHalbjahr.getEinfuehrungsphase()) {
				const belHj: AbiturFachbelegungHalbjahr | null = bel.belegungen[halbjahr.id];
				if ((belHj === null) || (GostKursart.fromKuerzel(belHj.kursartKuerzel) as unknown !== GostKursart.GK as unknown)) {
					continue;
				}
				if (belHj.schriftlich) {
					return true;
				}
			}
		}
		return false;
	}

	private pruefeGKLEinfuehrungsphaseAF1(): void {
		if (this.gklWahlen.idKlausurvorgabeEF_Sprachen === null) {
			if (Abi30BelegpruefungGKL.hatSchriftlicheGKBelegungInEF(this.belegungenAF1)) {
				this.addFehler(GostBelegungsfehler.GOST30_GKL_EF_AF1);
			}
		} else {
			// empty block
		}
	}

	private pruefeGKLEinfuehrungsphaseAF2(): void {
		if (this.gklWahlen.idKlausurvorgabeEF_GW === null) {
			if (Abi30BelegpruefungGKL.hatSchriftlicheGKBelegungInEF(this.belegungenAF2)) {
				this.addFehler(GostBelegungsfehler.GOST30_GKL_EF_AF2);
			}
		} else {
			// empty block
		}
	}

	private pruefeGKLEinfuehrungsphaseAF3(): void {
		if (this.gklWahlen.idKlausurvorgabeEF_NW === null) {
			if (Abi30BelegpruefungGKL.hatSchriftlicheGKBelegungInEF(this.belegungenAF3)) {
				this.addFehler(GostBelegungsfehler.GOST30_GKL_EF_AF3);
			}
		} else {
			// empty block
		}
	}

	private hatSchriftlicheGKBelegungInQMitGKLMoeglich(belegungen: List<AbiturFachbelegung>): boolean {
		for (const bel of belegungen) {
			let setHalbjahreMoeglich: JavaSet<GostHalbjahr> | null = this.mapGKLMoeglichQPhase.get(bel.fachID);
			if (setHalbjahreMoeglich === null) {
				setHalbjahreMoeglich = new HashSet<GostHalbjahr>();
			}
			for (const halbjahr of Abi30BelegpruefungGKL.gklHalbjahreQ) {
				const belHj: AbiturFachbelegungHalbjahr | null = bel.belegungen[halbjahr.id];
				if ((belHj === null) || (GostKursart.fromKuerzel(belHj.kursartKuerzel) as unknown !== GostKursart.GK as unknown)) {
					continue;
				}
				if (belHj.schriftlich && setHalbjahreMoeglich.contains(halbjahr)) {
					return true;
				}
			}
		}
		return false;
	}

	private pruefeGKLQualifikationsphaseAF1(): void {
		if (this.gklWahlen.idKlausurvorgabeQ_Sprachen === null) {
			if (this.hatSchriftlicheGKBelegungInQMitGKLMoeglich(this.belegungenAF1)) {
				this.addFehler(GostBelegungsfehler.GOST30_GKL_Q_AF1);
			}
		} else {
			// empty block
		}
	}

	private pruefeGKLQualifikationsphaseAF2(): void {
		if (this.gklWahlen.idKlausurvorgabeQ_GW === null) {
			if (this.hatSchriftlicheGKBelegungInQMitGKLMoeglich(this.belegungenAF2)) {
				this.addFehler(GostBelegungsfehler.GOST30_GKL_Q_AF2);
			}
		} else {
			// empty block
		}
	}

	private pruefeGKLQualifikationsphaseAF3(): void {
		if (this.gklWahlen.idKlausurvorgabeQ_NW === null) {
			if (this.hatSchriftlicheGKBelegungInQMitGKLMoeglich(this.belegungenAF3)) {
				this.addFehler(GostBelegungsfehler.GOST30_GKL_Q_AF3);
			}
		} else {
			// empty block
		}
	}

	protected pruefeGesamt(): void {
		this.pruefeGKLEinfuehrungsphaseAF1();
		this.pruefeGKLEinfuehrungsphaseAF2();
		this.pruefeGKLEinfuehrungsphaseAF3();
		this.pruefeGKLQualifikationsphaseAF1();
		this.pruefeGKLQualifikationsphaseAF2();
		this.pruefeGKLQualifikationsphaseAF3();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungGKL';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.GostBelegpruefung', 'de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungGKL'].includes(name);
	}

	public static readonly class = new Class<Abi30BelegpruefungGKL>('de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030.Abi30BelegpruefungGKL');

}

export function cast_de_svws_nrw_core_abschluss_gost_belegpruefung_abi2030_Abi30BelegpruefungGKL(obj: unknown): Abi30BelegpruefungGKL {
	return obj as Abi30BelegpruefungGKL;
}
