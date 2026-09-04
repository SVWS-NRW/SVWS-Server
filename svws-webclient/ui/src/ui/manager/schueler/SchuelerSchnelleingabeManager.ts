import type { KlassenDaten } from "@core/asd/data/klassen/KlassenDaten";
import type { EinschulungsartKatalogEintrag } from "@core/asd/data/schueler/EinschulungsartKatalogEintrag";
import type { SchuelerLernabschnittsdaten } from "@core/asd/data/schueler/SchuelerLernabschnittsdaten";
import type { SchuelerSchulbesuchsdaten } from "@core/asd/data/schueler/SchuelerSchulbesuchsdaten";
import type { SchuelerStammdaten } from "@core/asd/data/schueler/SchuelerStammdaten";
import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
import type { Erzieherart } from "@core/core/data/erzieher/Erzieherart";
import type { FachDaten } from "@core/core/data/fach/FachDaten";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import type { SchulEintrag } from "@core/core/data/kataloge/SchulEintrag";
import type { SchuelerListe } from "@core/core/data/schueler/SchuelerListe";
import type { Fahrschuelerart } from "@core/core/data/schule/Fahrschuelerart";
import type { Haltestelle } from "@core/core/data/schule/Haltestelle";
import type { Kindergarten } from "@core/core/data/schule/Kindergarten";
import type { ReligionEintrag } from "@core/core/data/schule/ReligionEintrag";
import type { Telefonart } from "@core/core/data/schule/Telefonart";
import type { VermerkartEintrag } from "@core/core/data/schule/VermerkartEintrag";
import { ArrayList } from "@core/java/util/ArrayList";
import type { List } from "@core/java/util/List";

export class SchuelerSchnelleingabeManager {

	private readonly _stammdaten: SchuelerStammdaten;
	private readonly _schulbesuchsdaten: SchuelerSchulbesuchsdaten;
	private readonly _lernabschnittsdaten: SchuelerLernabschnittsdaten;
	private readonly _schuelerliste: SchuelerListe;
	private readonly _schuljahresabschnitte: List<Schuljahresabschnitt>;
	private readonly _einschulungsartenById: Map<number, EinschulungsartKatalogEintrag>;
	private readonly _erzieherartenById: Map<number, Erzieherart>;
	private readonly _faecherById: Map<number, FachDaten>;
	private readonly _fahrschuelerartenById: Map<number, Fahrschuelerart>;
	private readonly _haltestellenById: Map<number, Haltestelle>;
	private readonly _jahrgaengeById: Map<number, JahrgangsDaten>;
	private readonly _kindergaertenById: Map<number, Kindergarten>;
	private readonly _religionenById: Map<number, ReligionEintrag>;
	private readonly _schulenById: Map<number, SchulEintrag>;
	private readonly _schulenByExterneSchulnummer: Map<string, SchulEintrag> = new Map();
	private readonly _telefonartenById: Map<number, Telefonart>;
	private readonly _vermerkartenById: Map<number, VermerkartEintrag>;
	private readonly _klassenAktuell: List<KlassenDaten> = new ArrayList();


	constructor(
		stammdaten: SchuelerStammdaten,
		schulbesuchsdaten: SchuelerSchulbesuchsdaten,
		lernabschnittsdaten: SchuelerLernabschnittsdaten,
		schuelerliste: SchuelerListe,
		schuljahresabschnitte: List<Schuljahresabschnitt>,
		einschulungsartenById: Map<number, EinschulungsartKatalogEintrag>,
		erzieherartenById: Map<number, Erzieherart>,
		faecherById: Map<number, FachDaten>,
		fahrschuelerartenById: Map<number, Fahrschuelerart>,
		haltestellenById: Map<number, Haltestelle>,
		jahrgaengeById: Map<number, JahrgangsDaten>,
		kindergaertenById: Map<number, Kindergarten>,
		religionenById: Map<number, ReligionEintrag>,
		schulenById: Map<number, SchulEintrag>,
		telefonartenById: Map<number, Telefonart>,
		vermerkartenById: Map<number, VermerkartEintrag>
	) {
		this._stammdaten = stammdaten;
		this._schulbesuchsdaten = schulbesuchsdaten;
		this._lernabschnittsdaten = lernabschnittsdaten;
		this._schuelerliste = schuelerliste;
		this._schuljahresabschnitte = schuljahresabschnitte;
		this._einschulungsartenById = einschulungsartenById;
		this._erzieherartenById = erzieherartenById;
		this._faecherById = faecherById;
		this._fahrschuelerartenById = fahrschuelerartenById;
		this._haltestellenById = haltestellenById;
		this._jahrgaengeById = jahrgaengeById;
		this._kindergaertenById = kindergaertenById;
		this._religionenById = religionenById;
		this._schulenById = schulenById;
		this._telefonartenById = telefonartenById;
		this._vermerkartenById = vermerkartenById;
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
		for (const schule of this.schulenById.values()) {
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

	get einschulungsartenById(): Map<number, EinschulungsartKatalogEintrag> {
		return this._einschulungsartenById;
	}

	get erzieherartenById(): Map<number, Erzieherart> {
		return this._erzieherartenById;
	}

	get faecherById(): Map<number, FachDaten> {
		return this._faecherById;
	}

	get fahrschuelerartenById(): Map<number, Fahrschuelerart> {
		return this._fahrschuelerartenById;
	}

	get haltestellenById(): Map<number, Haltestelle> {
		return this._haltestellenById;
	}

	get jahrgaengeById(): Map<number, JahrgangsDaten> {
		return this._jahrgaengeById;
	}

	get kindergaertenById(): Map<number, Kindergarten> {
		return this._kindergaertenById;
	}

	get religionenById(): Map<number, ReligionEintrag> {
		return this._religionenById;
	}

	get schulenById(): Map<number, SchulEintrag> {
		return this._schulenById;
	}

	get telefonartenById(): Map<number, Telefonart> {
		return this._telefonartenById;
	}

	get vermerkartenById(): Map<number, VermerkartEintrag> {
		return this._vermerkartenById;
	}

	get klassenAktuell(): List<KlassenDaten> {
		return this._klassenAktuell;
	}

	get schulenByExterneSchulnummer(): Map<string, SchulEintrag> {
		return this._schulenByExterneSchulnummer;
	}
}
