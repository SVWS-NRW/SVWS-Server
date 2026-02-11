import { StateManager } from "../../../index";
import type { List } from '../../../../../core/src/java/util/List';
import type { JavaMap } from '../../../../../core/src/java/util/JavaMap';
import type { JavaSet } from '../../../../../core/src/java/util/JavaSet';
import type { SchuelerKAoADaten } from '../../../../../core/src/core/data/schueler/SchuelerKAoADaten';
import type { SchuelerLernabschnittListeEintrag } from '../../../../../core/src/core/data/schueler/SchuelerLernabschnittListeEintrag';
import type { Schuljahresabschnitt } from '../../../../../core/src/asd/data/schule/Schuljahresabschnitt';
import { SchuelerListeEintrag } from '../../../../../core/src/core/data/schueler/SchuelerListeEintrag';
import { HashSet } from '../../../../../core/src/java/util/HashSet';
import { HashMap } from '../../../../../core/src/java/util/HashMap';
import { KAOAKategorie } from '../../../../../core/src/asd/types/kaoa/KAOAKategorie';


interface SchuelerKaoaState {
	auswahl: SchuelerListeEintrag;
	kAoADatenById: JavaMap<number, SchuelerKAoADaten>;
}

const createDefaultState = (): SchuelerKaoaState => ({
	auswahl: new SchuelerListeEintrag(),
	kAoADatenById: new HashMap(),
});

export class SchuelerKAoAManager extends StateManager<SchuelerKaoaState> {

	private readonly _schuljahresabschnitteById: Map<number, Schuljahresabschnitt> = new Map();
	private readonly _lernabschnitteBySchuljahr: Map<number, SchuelerLernabschnittListeEintrag> = new Map();
	private readonly _schuljahr: number;
	private readonly _schuljahresabschnitteFiltered: JavaSet<Schuljahresabschnitt> = new HashSet<Schuljahresabschnitt>();

	/**
	 * Erstellt einen neuen Manager mit den übergebenen KAoA Daten
	 *
	 * @param kAoADaten                  	KAoA Daten des Schülers
	 * @param schuljahresabschnitte			Der Schuljahresabschnitt, in welchem sich die Schule aktuell befindet.
	 * @param lernabschnitte         		Die Lernabschnittsdaten des Schülers
	 */
	public constructor(kAoADaten: List<SchuelerKAoADaten>, schuljahresabschnitte: List<Schuljahresabschnitt>, lernabschnitte: List<SchuelerLernabschnittListeEintrag>) {
		super(createDefaultState());
		this.mapKAoADaten(kAoADaten);
		this.mapSchuljahresabschnitte(schuljahresabschnitte);
		this.mapLernabschnitte(lernabschnitte);
		this.processSchuljahresabschnitte();
		this._schuljahr = this.calcSchuljahr();
	}

	private mapKAoADaten(kAoADaten: List<SchuelerKAoADaten>) {
		for (const kaoa of kAoADaten) {
			this._state.value.kAoADatenById.put(kaoa.id, kaoa);
		}
	}

	private mapSchuljahresabschnitte(schuljahresabschnitte: List<Schuljahresabschnitt>) {
		for (const schuljahresabschnitt of schuljahresabschnitte) {
			this._schuljahresabschnitteById.set(schuljahresabschnitt.id, schuljahresabschnitt);
		}
	}

	private mapLernabschnitte(lernabschnitte: List<SchuelerLernabschnittListeEintrag>) {
		for (const lernabschnitt of lernabschnitte) {
			this._lernabschnitteBySchuljahr.set(lernabschnitt.schuljahr, lernabschnitt);
		}
	}

	private calcSchuljahr(): number {
		const abschnitt = this._schuljahresabschnitteById.get(this._state.value.auswahl.idSchuljahresabschnitt);
		return (abschnitt === undefined) ? -1 : abschnitt.schuljahr;
	}

	/**
	 * Diese Methode erzeugt die Liste derjenigen Schuljahresabschnitte, in denen es einen Lernabschnitt für den ausgewählten Schüler und
	 * entsprechend der dazugehörigen Jahrgänge KAOAKategorieEinträge gibt.
	 */
	private processSchuljahresabschnitte(): void {
		const availableJahrgaenge = this.getAvailableJahrgaenge();
		if (availableJahrgaenge.isEmpty()) {
			return;
		}

		const idsSchuljahresabschnitte = this.getIdsSchuljahresabschnitte(availableJahrgaenge);
		if (idsSchuljahresabschnitte.isEmpty()) {
			return;
		}

		this.filterSchuljahresabschnitte(idsSchuljahresabschnitte);
	}

	private getAvailableJahrgaenge(): JavaSet<string> {
		const availableJahrgaenge = new HashSet<string>();

		for (const kategorie of KAOAKategorie.data().getWerteBySchuljahr(this._schuljahr)) {
			const kategorieEintrag = kategorie.daten(this._schuljahr);
			if (kategorieEintrag === null) {
				return availableJahrgaenge;
			}
			for (const jahrgang of kategorieEintrag.jahrgaenge) {
				availableJahrgaenge.add(jahrgang.substring(jahrgang.length - 2));
			}
		}

		return availableJahrgaenge;
	}

	private getIdsSchuljahresabschnitte(availableKuerzelJahrgang: JavaSet<string>): JavaSet<number> {
		const idsSchuljahresabschnitte = new HashSet<number>();

		for (const lernabschnitt of this._lernabschnitteBySchuljahr.values()) {
			if (availableKuerzelJahrgang.contains(lernabschnitt.jahrgang)) {
				idsSchuljahresabschnitte.add(lernabschnitt.schuljahresabschnitt);
			}
		}

		return idsSchuljahresabschnitte;
	}

	private filterSchuljahresabschnitte(schuljahresabschnittIDs: JavaSet<number>): void {
		for (const schuljahresabschnitt of this._schuljahresabschnitteById.values()) {
			if (schuljahresabschnittIDs.contains(schuljahresabschnitt.id)) {
				this._schuljahresabschnitteFiltered.add(schuljahresabschnitt);
			}
		}
	}

	get schuljahresabschnitteById(): Map<number, Schuljahresabschnitt> {
		return this._schuljahresabschnitteById;
	}


	get schuljahresabschnitteFiltered(): JavaSet<Schuljahresabschnitt> {
		return this._schuljahresabschnitteFiltered;
	}

	get lernabschnitteBySchuljahr(): Map<number, SchuelerLernabschnittListeEintrag> {
		return this._lernabschnitteBySchuljahr;
	}

	/** Gibt die aktuell im Manager gespeicherten KAoADaten zurück. */
	public get kAoADatenById(): JavaMap<number, SchuelerKAoADaten> {
		return this._state.value.kAoADatenById;
	}

}
