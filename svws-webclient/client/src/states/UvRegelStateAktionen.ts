import type { UvBlockungRegel } from "@core/core/data/uv/regel/UvBlockungRegel";
import type { UvLerngruppenLehrer } from "@core/core/data/uv/UvLerngruppenLehrer";
import type { UvPlanungsabschnitt } from "@core/core/data/uv/UvPlanungsabschnitt";
import { UvRegelManager } from "@core/core/utils/uv/UvRegelManager";
import { UvAlgorithmusImpl } from "@core/core/utils/uvblockung/UvAlgorithmusImpl";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import type { UvState } from "@ui/states/UvState";

import { UvPlanungsManager } from "~/components/unterrichtsverteilung/planung/UvPlanungsManager";
import { api } from "~/router/Api";

import type { UvRequestKontext } from "./UvStateImpl";

type UvRegelAktionen = UvState['regeln'];

interface UvRegelStateKontext {
	state: Pick<UvState, 'uvManager' | 'regelliste'>;
	planungsabschnitt: () => UvPlanungsabschnitt;
	requestKontext: () => UvRequestKontext;
	istRequestKontextAktuell: (kontext: UvRequestKontext) => boolean;
	regellisteSetzen: (regeln: UvBlockungRegel[]) => void;
	ersetzeLehrkraftzuordnungen: (ergebnis: List<Partial<UvLerngruppenLehrer>>) => Promise<void>;
}

export class UvRegelStateAktionen implements UvRegelAktionen {

	private blockung: { stoppen: boolean; verwerfen: boolean } | null = null;

	public constructor(private readonly kontext: UvRegelStateKontext) {
	}

	/** Legt eine Regel im aktiven Planungsabschnitt an. */
	public async create(regel: UvBlockungRegel): Promise<UvBlockungRegel> {
		const kontext = this.kontext.requestKontext();
		const neu = await api.server.createUvRegel(regel, kontext.schema, this.kontext.planungsabschnitt().id);
		if (this.kontext.istRequestKontextAktuell(kontext)) {
			this.kontext.regellisteSetzen([...this.kontext.state.regelliste, neu]);
		}
		return neu;
	}

	/** Aktualisiert eine Regel im aktiven Planungsabschnitt. */
	public async patch(regel: UvBlockungRegel): Promise<void> {
		const kontext = this.kontext.requestKontext();
		const gespeichert = await api.server.patchUvRegel(regel, kontext.schema, this.kontext.planungsabschnitt().id, regel.id);
		if (this.kontext.istRequestKontextAktuell(kontext)) {
			this.kontext.regellisteSetzen(this.kontext.state.regelliste.map(item => item.id === gespeichert.id ? gespeichert : item));
		}
	}

	/** Speichert mehrere Regeländerungen gemeinsam. */
	public async patchMehrere(regeln: UvBlockungRegel[]): Promise<void> {
		const kontext = this.kontext.requestKontext();
		if (regeln.length === 0) {
			return;
		}
		const daten = new ArrayList<UvBlockungRegel>();
		for (const regel of regeln) {
			daten.add(regel);
		}
		const gespeichert: List<UvBlockungRegel> = await api.server.patchUvRegeln(daten, kontext.schema, this.kontext.planungsabschnitt().id);
		const byId = new Map([...gespeichert].map(regel => [regel.id, regel]));
		if (this.kontext.istRequestKontextAktuell(kontext)) {
			this.kontext.regellisteSetzen(this.kontext.state.regelliste.map(regel => byId.get(regel.id) ?? regel));
		}
	}

	/** Löscht eine Regel aus dem aktiven Planungsabschnitt. */
	public async loeschen(id: number): Promise<void> {
		const kontext = this.kontext.requestKontext();
		await api.server.deleteUvRegel(kontext.schema, this.kontext.planungsabschnitt().id, id);
		if (this.kontext.istRequestKontextAktuell(kontext)) {
			this.kontext.regellisteSetzen(this.kontext.state.regelliste.filter(item => item.id !== id));
		}
	}

	/** Aktiviert oder deaktiviert die Fixierung einer Lehrkraftzuordnung. */
	public async lehrerFixierungUmschalten(art: 'klasse' | 'lerngruppe', idZiel: number, idLehrer: number, reihenfolge: number): Promise<void> {
		const manager = new UvPlanungsManager(this.kontext.state.uvManager, this.kontext.planungsabschnitt(), this.kontext.state.regelliste);
		const aenderungen = manager.lehrerFixierungUmschaltenRegeln(art, idZiel, idLehrer, reihenfolge);
		const neu = aenderungen.find(regel => regel.id < 0);
		if (neu !== undefined) {
			await this.create(neu);
		} else {
			await this.patchMehrere(aenderungen);
		}
	}

	/** Berechnet Lehrkraftzuordnungen und meldet den Fortschritt bis zum Ende der Blockung. */
	public blockePlanungsabschnitt = async (fortschritt: (malusBeschreibung: string) => void): Promise<void> => {
		if (this.blockung !== null) {
			throw new Error('Eine UV-Blockung läuft bereits.');
		}
		const plan = this.kontext.planungsabschnitt();
		const kontext = this.kontext.requestKontext();
		const regelliste = new ArrayList<UvBlockungRegel>();
		for (const regel of this.kontext.state.regelliste) {
			regelliste.add(regel);
		}
		const regeln = new UvRegelManager(this.kontext.state.uvManager, regelliste);
		if (!regeln.regelnGetMengeFehlerhaftAsList().isEmpty()) {
			throw new Error('Die Regeln enthalten ungültige Einträge. Bitte in der Regelansicht korrigieren.');
		}
		const algorithmus = new UvAlgorithmusImpl(this.kontext.state.uvManager, regeln, plan);
		const lauf = { stoppen: false, verwerfen: false };
		this.blockung = lauf;
		try {
			const beschreibung = () => (algorithmus as UvAlgorithmusImpl & { gibMalusBeschreibung(): string }).gibMalusBeschreibung();
			while (!lauf.stoppen && !lauf.verwerfen) {
				algorithmus.berechneInnerhalb(200);
				fortschritt(beschreibung());
				await new Promise<void>(resolve => {
					setTimeout(resolve, 0);
				});
			}
			if (!lauf.verwerfen) {
				if (!this.kontext.istRequestKontextAktuell(kontext)) {
					throw new Error('Der Planungsabschnitt hat sich während der Blockung geändert.');
				}
				await this.kontext.ersetzeLehrkraftzuordnungen(algorithmus.gibBestesAktuellesErgebnis());
			}
		} finally {
			if (this.blockung === lauf) {
				this.blockung = null;
			}
		}
	};

	/** Stoppt die laufende Blockung und lässt das beste Ergebnis übernehmen. */
	public stoppeBlockung = (): void => {
		if (this.blockung !== null) {
			this.blockung.stoppen = true;
		}
	};

	/** Bricht die laufende Blockung ab und verwirft das Ergebnis. */
	public verwerfeBlockung = (): void => {
		if (this.blockung !== null) {
			this.blockung.verwerfen = true;
		}
	};

}
