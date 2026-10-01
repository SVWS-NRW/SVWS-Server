import type { KlassenDaten } from "@core/asd/data/klassen/KlassenDaten";
import type { SchuelerLernabschnittsdaten } from "@core/asd/data/schueler/SchuelerLernabschnittsdaten";
import type { SchuelerSchulbesuchsdaten } from "@core/asd/data/schueler/SchuelerSchulbesuchsdaten";
import type { SchuelerStammdaten } from "@core/asd/data/schueler/SchuelerStammdaten";
import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { FachDaten } from "@core/core/data/fach/FachDaten";
import type { SchulEintrag } from "@core/core/data/kataloge/SchulEintrag";
import type { SchuelerListe } from "@core/core/data/schueler/SchuelerListe";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";
import { useHerkunftschulenState } from "@ui/states/kataloge/HerkunftschulenState";

export class SchuelerSchnelleingabeManager {

	private readonly _stammdaten: SchuelerStammdaten;
	private readonly _schulbesuchsdaten: SchuelerSchulbesuchsdaten;
	private readonly _lernabschnittsdaten: SchuelerLernabschnittsdaten;
	private readonly _schuelerliste: SchuelerListe;
	private readonly _schuljahresabschnitte: List<Schuljahresabschnitt>;
	private readonly _faecherById: Map<number, FachDaten>;
	private readonly _schulenByExterneSchulnummer: Map<string, SchulEintrag> = new Map();
	private readonly _klassenAktuell: List<KlassenDaten> = new ArrayList();

	private readonly schulenState = useHerkunftschulenState();

	constructor(
		stammdaten: SchuelerStammdaten,
		schulbesuchsdaten: SchuelerSchulbesuchsdaten,
		lernabschnittsdaten: SchuelerLernabschnittsdaten,
		schuelerliste: SchuelerListe,
		schuljahresabschnitte: List<Schuljahresabschnitt>,
		faecherById: Map<number, FachDaten>
	) {
		this._stammdaten = stammdaten;
		this._schulbesuchsdaten = schulbesuchsdaten;
		this._lernabschnittsdaten = lernabschnittsdaten;
		this._schuelerliste = schuelerliste;
		this._schuljahresabschnitte = schuljahresabschnitte;
		this._faecherById = faecherById;
		this.filterKlassen();
		this.processSchulen();
	}

	private filterKlassen() {
		for (const klasse of this.schuelerliste.klassen) {
			if (klasse.idSchuljahresabschnitt === this.schuelerliste.idSchuljahresabschnitt) {
				this._klassenAktuell.add(klasse);
			}
		}
	}

	private processSchulen() {
		for (const schule of this.schulenState.herkunftschulen.list) {
			if (schule.schulnummerStatistik !== null) {
				this.schulenByExterneSchulnummer.set(schule.schulnummerStatistik, schule);
			}
		}
	}

	get stammdaten(): SchuelerStammdaten {
		return this._stammdaten;
	}

	get schulbesuchsdaten(): SchuelerSchulbesuchsdaten {
		return this._schulbesuchsdaten;
	}

	get lernabschnittsdaten(): SchuelerLernabschnittsdaten {
		return this._lernabschnittsdaten;
	}

	get schuljahresabschnitte(): List<Schuljahresabschnitt> {
		return this._schuljahresabschnitte;
	}

	get schuelerliste(): SchuelerListe {
		return this._schuelerliste;
	}

	get faecherById(): Map<number, FachDaten> {
		return this._faecherById;
	}

	get klassenAktuell(): List<KlassenDaten> {
		return this._klassenAktuell;
	}

	get schulenByExterneSchulnummer(): Map<string, SchulEintrag> {
		return this._schulenByExterneSchulnummer;
	}
}
