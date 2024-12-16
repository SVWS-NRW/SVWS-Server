import { ArrayList, List } from "@core";

/**
 * Eine Schnittstelle für die Schema-Definitionen der Spalten der weiter unten definierten Untis-GPU-Formate
 */
interface UntisGPUColumns<GPUTYPE> {

	// Der Name der Spalte
	name: keyof GPUTYPE;

	// Der Typ der Spalte
	type: 'string' | 'number';

	// Gibt an, ob das Feld gefordert ist oder nicht
	required: boolean;

}

/**
 * Ein generischer Reader für Daten einer Untis-GPU-Datei
 *
 * @param <GPUTYPE> die Schnittstellenbeschreibung der GPU-Daten
 */
export class UntisGPUCsv<GPUTYPE> {

	// Die Schema-Beschreibung der CSV-Spalten der GPU-Datei
	private _schema: Array<UntisGPUColumns<GPUTYPE>>;

	// Die CSV-Daten
	private _csv: string;

	// Die eingelesenen GPU-Daten
	private _data: Array<GPUTYPE>;

	// Gibt an, ob die Daten fehlerfrei gelesen werden konnten
	private _valid: boolean;

	/**
	 * Initialisiert den Reader mit dem übergebenen Schema und den übergebenen CSV-Daten
	 * und bestimmt anschließend die einzelnen Datensätze aus den CSV-Daten
	 *
	 * @param schema   das Schema der GPU-Daten
	 * @param input    die CSV-Daten als UTF-8-String oder eine Liste mit den CSV-Daten
	 */
	public constructor(schema: Array<UntisGPUColumns<GPUTYPE>>, input: string | Array<GPUTYPE>, seperator: string = ";") {
		this._schema = schema;
		this._valid = true;
		if (typeof input ==='string') {
			this._csv = input;
			this._data = new Array<GPUTYPE>();
			const rows = this._csv.replaceAll("\r\n", "\n").split("\n");
			for (const row of rows) {
				let tmp = row;
				if (tmp.trim() === '')
					continue;
				const obj = <GPUTYPE>{};
				for (let index = 0; index < this._schema.length; index++) {
					if (tmp.at(0) === '"') {
						const result = /"([^"]|(\\"))*"/.exec(tmp);
						const str = result?.[0] ?? null;
						this.getData(obj, str === null ? null : str.substring(1, str.length-1), this._schema.at(index));
						tmp = tmp.substring((str?.length ?? 0) + 1); // Entferne den String und das Semikolon danach
					} else {
						const len = tmp.indexOf(seperator);
						if (((len >= 0) && (index === this._schema.length - 1) && (tmp.trim() !== seperator)) || ((len === -1) && (index !== this._schema.length - 1))) {
							this._valid = false;
							continue;
						}
						const str = (len === -1) ? tmp : tmp.substring(0, len);
						this.getData(obj, str, this._schema.at(index));
						tmp = tmp.substring(len + 1);
					}
				}
				if (this._valid)
					this._data.push(obj);
			}
		} else {
			this._data = input;
			this._csv = "";
			for (const obj of this._data) {
				for (let index = 0; index < this._schema.length; index++)
					this._csv = this.setData(obj, this._csv, this._schema.at(index)) + seperator;
				this._csv += "\n";
			}
		}
	}

	/**
	 * Hilfsmethode beim Schreiben eines Wertes einer Spalte
	 *
	 * @param obj   das Objekt, aus dem der Wert entnommen wird
	 * @param csv   der bisherige CSV-String, an den der Wert angehangen werden soll
	 * @param col   die Schema-Definition der Spalte
	 */
	private setData(obj: any, csv: string, col: UntisGPUColumns<GPUTYPE> | undefined) : string {
		if (col === undefined)
			return csv;
		if ((obj[col.name] === undefined) || (obj[col.name] === null)) {
			if (col.required)
				this._valid = false;
			return csv;
		}
		return csv + obj[col.name];
	}

	/**
	 * Hilfsmethode beim Einlesen eines Wertes einer Spalte
	 *
	 * @param obj     das Objekt, dem der Wert hinzuzufügen ist
	 * @param value   der hinzuzufügende Wert
	 * @param col     die Schema-Definition der Spalte
	 */
	private getData(obj: any, value: string | null, col: UntisGPUColumns<GPUTYPE> | undefined) : void {
		if (col === undefined)
			return;
		if ((value === null) && (col.required))
			this._valid = false;
		if ((value === null) || (col.type === 'string'))
			obj[col.name] = value;
		else // number type
			obj[col.name] = +value;
	}

	/** Gibt an, on die eingelesenen Daten gültig sind oder nicht */
	public get valid() : boolean {
		return this._valid;
	}

	/** Die eingelesenen Daten als Array des GPU-Datentyps */
	public get data() : Array<GPUTYPE> {
		return this._data;
	}

	/**
	 * Die Daten der GPU-Datei als CSV-String
	 */
	public get asString() : string {
		if (!this.valid)
			return '';
		let result : string = '';
		for (const row of this.data) {
			for (let index = 0; index < this._schema.length; index++) {
				if (index !== 0)
					result += ';';
				const col = this._schema[index];
				if (row[col.name] !== null)
					result += (col.type === 'string') ? ('"' + row[col.name] + '"') : row[col.name];
			}
			result += "\n";
		}
		return result;
	}

}


/**
 * Die Schnittstellen-Definition der GPU001-Datei für Stundenpläne aus Untis
 */
export interface UntisGPU001 {

	/** Eine numerische ID, welche den Unterricht eindeutig identifiziert (z.B. 42) */
	idUnterricht: number;

	/** Das Kürzel der Klasse (z.B. "05A") */
	klasseKuerzel: string;

	/** Das Kürzel des Lehrers (z.B. "BACH") */
	lehrerKuerzel: string;

	/** Das Kürzel des Fachen (z.B. "D") */
	fachKuerzel: string;

	/** Das Kürzel des Raumes (z.B. "") */
	raumKuerzel: string | null;

	/** Der Wochentag für den Unterricht */
	wochentag: number;

	/** Die Stunde im Zeitraster des Wochentags */
	stunde: number;

	/** Ggf. die Stundenlänge in Minuten oder leer */
	dauer: number | null;

	/** Dummy - Dieses Feld wird von Untis nicht belegt und kann genutzt werden, um den Wochentyp für den SVWS-Server anzugeben */
	wochentyp: string;

}


/**
 * Ein Reader für das Einlesen der Daten einer GPU001-Datei
 */
export class UntisGPU001Csv extends UntisGPUCsv<UntisGPU001> {

	/** Die Map mit den Unterrichts-Einträgen anhand ihrer ID - wird bei Bedarf über den Getter initialisiert */
	private _mapByUnterrichtsID : Map<number, UntisGPU001> | null = null;

	/**
	 * Erzeugt einen neuen Reader mit den übergebenen CSV-Daten als UTF-8-String
	 *
	 * @param input                        die CSV-Daten oder das Array mit den GPU-Objekten
	 * @param mapWochentypByUnterrichtID   eine Map mit den Wochentypzuordnungen
	 */
	public constructor(input: string | Array<UntisGPU001>, mapWochentypByUnterrichtID?: Map<number, number>) {
		super([
			{ name: 'idUnterricht', type: 'number', required: true },
			{ name: 'klasseKuerzel', type: 'string', required: true },
			{ name: 'lehrerKuerzel', type: 'string', required: true },
			{ name: 'fachKuerzel', type: 'string', required: true },
			{ name: 'raumKuerzel', type: 'string', required: false },
			{ name: 'wochentag', type: 'number', required: true },
			{ name: 'stunde', type: 'number', required: true },
			{ name: 'dauer', type: 'number', required: false },
			{ name: 'wochentyp', type: 'string', required: false },
		], input);
		if ((this.valid) && (mapWochentypByUnterrichtID !== undefined)) {
			for (const eintrag of this.data) {
				const wt = mapWochentypByUnterrichtID.get(eintrag.idUnterricht);
				if (wt !== undefined)
					eintrag.wochentyp = wt.toString();
			}
		}
	}

	/**
	 * Erstellt eine Map mit den Einträgen anhand der Unterrichts-ID
	 *
	 * @returns die Map mit den Einträgen anhand der Unterrichts-ID
	 */
	public get mapByUnterrichtsID() : Map<number, UntisGPU001> {
		if (this._mapByUnterrichtsID === null) {
			this._mapByUnterrichtsID = new Map<number, UntisGPU001>();
			if (this.valid)
				for (const eintrag of this.data)
					this._mapByUnterrichtsID.set(eintrag.idUnterricht, eintrag);
		}
		return this._mapByUnterrichtsID;
	}

}


/**
 * Die Schnittstellen-Definition der GPU002-Datei für die Unterrichte aus Untis
 */
export interface UntisGPU002 {

	/** Eine numerische ID, welche den Unterricht eindeutig identifiziert (z.B. 42) */
	idUnterricht : number;

	/** Die Wochenstunden des Unterrichtes */
	wochenstunden : number;

	/** Die Wochenstunden für die Klasse */
	wochenstundenKlasse : number;

	/** Die Wochenstunden des Lehrers */
	wochenstundenLehrer : number;

	/** Das Kürzel der Klasse (z.B. "05A") */
	klasseKuerzel : string;

	/** Das Kürzel des Lehrers (z.B. "BACH") */
	lehrerKuerzel : string | null;

	/** Das Kürzel des Fachen (z.B. "D") */
	fachKuerzel : string;

	/** Das Kürzel des Raumes (z.B. "") */
	raumKuerzel : string | null;

	/** Statistik 1 Unt */
	statistik1Unt : number | null;

	/** Studentenzahl */
	studentenZahl : number | null;

	/** Wochenwert */
	wochenwert : number | null;

	/** Die Gruppe, welche den Wochentyp bei AB-, ABC-, ABCD-Wochentypmodellen angibt */
	wochenTyp : string | null;

	/** Zeilentext 1 */
	zeilenText1 : string | null;

	/** Zeilenwert in Tausendstel */
	zeilenWert : string | null;

	/** Datum von */
	datumVon : string | null;

	/** Datum bis */
	datumBis : string | null;

	/** Jahreswert */
	jahreswert : number | null;

	/** Text */
	text : string | null;

	/** Teilungsnummer */
	teilungsnummer : string | null;

	/** Kürzel des Stammraums */
	stammraumKuerzel : string | null;

	/** Beschreibung */
	beschreibung : string | null;

	/** Farbe Vordergrund */
	farbeVordergrund : string | null;

	/** Farbe Hintergrund */
	farbeHintergrund : string | null;

	/** Kennzeichen */
	kennzeichen : string | null;

	/** Fachfolge Klassen */
	fachfolgeKlassen : string | null;

	/** Fachfolge Lehrer */
	fachfolgeLehrer : string | null;

	/** Klassen-Kollisions-Kennz. */
	klassenKollKennz : string | null;

	/** Doppelstd. min. */
	doppelStdMin : number | null;

	/** Doppelstd. max. */
	doppelStdMax : number | null;

	/** Blockgröße */
	blockgroesse : number | null;

	/** Stunden im Raum */
	stundenImRaum : number | null;

	/** Priorität */
	prioritaet : string | null;

	/** Statistik 1 Lehrer */
	statistik1Lehrer : string | null;

	/** Studenten männlich */
	studentenMaennlich : number | null;

	/** Studenten weiblich */
	studentenWeiblich : number | null;

	/** Wert bzw. Faktor */
	wert : string | null;

	/** 2. Block */
	block2 : string | null;

	/** 3. Block */
	block3 : string | null;

	/** Zeilentext 2 */
	zeilenText2 : string | null;

	/** Eigenwert */
	eigenwert : string | null;

	/** Eigenwert in hundertausendstel */
	eigenwertHunderttausendstel : string | null;

	/** Schülergruppe */
	schuelergruppe : string | null;

	/** Wochenstunden in Jahres-Perioden-Planung */
	wochenstundenJahresperioden : string | null;

	/** Jahresstunden */
	jahresstunden : string | null;

	/** Zeilen-Unterrichtsgruppe */
	zeilenUnterrichtsgruppe : string | null;

	/** Ignorieren - Dummy für das Einlesen der Daten */
	dummy : string | null;

}


/**
 * Ein Reader für das Einlesen der Daten einer GPU002-Datei
 */
export class UntisGPU002Csv extends UntisGPUCsv<UntisGPU002> {

	/** Die Map mit den Unterrichts-Einträgen anhand ihrer ID - wird bei Bedarf über den Getter initialisiert */
	private _mapByUnterrichtsID : Map<number, UntisGPU002> | null = null;

	/** Eine Map mit der Zuordnung von speziellen Wochentypen zu den jeweiligen Unterrichtsnummern */
	private _mapWochentypByUnterrichtsID : Map<number, number> | null = null;

	/**
	 * Erzeugt einen neuen Reader mit den übergebenen CSV-Daten als UTF-8-String
	 *
	 * @param csv   die CSV-Daten
	 */
	public constructor(csv: string) {
		super([
			{ name: 'idUnterricht', type: 'number', required: true },
			{ name: 'wochenstunden', type: 'number', required: true },
			{ name: 'wochenstundenKlasse', type: 'number', required: true },
			{ name: 'wochenstundenLehrer', type: 'number', required: true },
			{ name: 'klasseKuerzel', type: 'string', required: true },
			{ name: 'lehrerKuerzel', type: 'string', required: false },
			{ name: 'fachKuerzel', type: 'string', required: true },
			{ name: 'raumKuerzel', type: 'string', required: false },
			{ name: 'statistik1Unt', type: 'number', required: false },
			{ name: 'studentenZahl', type: 'number', required: false },
			{ name: 'wochenwert', type: 'number', required: false },
			{ name: 'wochenTyp', type: 'string', required: false },
			{ name: 'zeilenText1', type: 'string', required: false },
			{ name: 'zeilenWert', type: 'string', required: false },
			{ name: 'datumVon', type: 'string', required: false },
			{ name: 'datumBis', type: 'string', required: false },
			{ name: 'jahreswert', type: 'number', required: false },
			{ name: 'text', type: 'string', required: false },
			{ name: 'teilungsnummer', type: 'string', required: false },
			{ name: 'stammraumKuerzel', type: 'string', required: false },
			{ name: 'beschreibung', type: 'string', required: false },
			{ name: 'farbeVordergrund', type: 'string', required: false },
			{ name: 'farbeHintergrund', type: 'string', required: false },
			{ name: 'kennzeichen', type: 'string', required: false },
			{ name: 'fachfolgeKlassen', type: 'string', required: false },
			{ name: 'fachfolgeLehrer', type: 'string', required: false },
			{ name: 'klassenKollKennz', type: 'string', required: false },
			{ name: 'doppelStdMin', type: 'number', required: false },
			{ name: 'doppelStdMax', type: 'number', required: false },
			{ name: 'blockgroesse', type: 'number', required: false },
			{ name: 'stundenImRaum', type: 'number', required: false },
			{ name: 'prioritaet', type: 'string', required: false },
			{ name: 'statistik1Lehrer', type: 'string', required: false },
			{ name: 'studentenMaennlich', type: 'number', required: false },
			{ name: 'studentenWeiblich', type: 'number', required: false },
			{ name: 'wert', type: 'string', required: false },
			{ name: 'block2', type: 'string', required: false },
			{ name: 'block3', type: 'string', required: false },
			{ name: 'zeilenText2', type: 'string', required: false },
			{ name: 'eigenwert', type: 'string', required: false },
			{ name: 'eigenwertHunderttausendstel', type: 'string', required: false },
			{ name: 'schuelergruppe', type: 'string', required: false },
			{ name: 'wochenstundenJahresperioden', type: 'string', required: false },
			{ name: 'jahresstunden', type: 'string', required: false },
			{ name: 'zeilenUnterrichtsgruppe', type: 'string', required: false },
			{ name: 'dummy', type: 'string', required: false },
		], csv);
	}

	/**
	 * Erstellt eine Map mit den Einträgen anhand der Unterrichts-ID
	 *
	 * @returns die Map mit den Einträgen anhand der Unterrichts-ID
	 */
	public get mapByUnterrichtsID() : Map<number, UntisGPU002> {
		if (this._mapByUnterrichtsID === null) {
			this._mapByUnterrichtsID = new Map<number, UntisGPU002>();
			if (this.valid)
				for (const eintrag of this.data)
					this._mapByUnterrichtsID.set(eintrag.idUnterricht, eintrag);
		}
		return this._mapByUnterrichtsID;
	}

	/**
	 * Erstellt eine Map mit der Zuordnung von speziellen Wochentypen zu den jeweiligen Unterrichtsnummern
	 *
	 * @returns die Map mit der Zuordnung von speziellen Wochentypen zu den jeweiligen Unterrichtsnummern
	 */
	public getMapWochentypByUnterrichtsID(wochentypbezeichner: string[]) : Map<number, number> {
		const mapBezeichner = new Map<string, number>();
		for (let i = 0; i < wochentypbezeichner.length; i++)
			if (wochentypbezeichner[i].trim() !== '')
				mapBezeichner.set(wochentypbezeichner[i].trim(), i + 1);
		if (this._mapWochentypByUnterrichtsID === null) {
			this._mapWochentypByUnterrichtsID = new Map<number, number>();
			if (this.valid) {
				for (const eintrag of this.data) {
					if (eintrag.wochenTyp !== null) {
						const wt = mapBezeichner.get(eintrag.wochenTyp);
						if (wt !== undefined)
							this._mapWochentypByUnterrichtsID.set(eintrag.idUnterricht, wt);
					}
				}
			}
		}
		return this._mapWochentypByUnterrichtsID;
	}

}



/**
 * Die Schnittstellen-Definition der GPP002-Datei für die aktuellen (!) Unterrichte aus Untis
 */
export interface UntisGPP002 {

	/** Das Kürzel des Lehrers (z.B. "BACH") */
	lehrerKuerzel : string | null;

	/** Das Jahr in dem der Unterricht stattfindet */
	jahr : number;

	/** Der Monat in dem der Unterricht stattfindet */
	monat : number;

	/** Der Tag in dem der Unterricht stattfindet */
	tag : number;

	/** Die Stunde im Zeitraster des Wochentags in dem der Unterricht stattfindet, im Minutenmodus 0 */
	stunde: number;

	/** Das Kürzel des Fachen (z.B. "D") */
	fachKuerzel : string;

	/** Das Kürzel der Klasse (z.B. "05A") oder der Klassen (z.B. "05A~05B") */
	klasseKuerzel : string;

	/** Das Kürzel des Raumes (z.B. "") */
	raumKuerzel : string | null;

	/** Die Bezahlung der Vertretung (0 - unbezahlt, 1 - bezahlt) */
	bezahlt : string | null;

	/** Ein leeres Feld */
	empty : string | null;

	/** Eine numerische ID, welche den Unterricht eindeutig identifiziert (z.B. 42) */
	idUnterricht : number;

	/** Beginn (hhmm) */
	beginn : string | null;

	/** Dauer in Minuten */
	dauer : number | null;

	/** Fachgruppe */
	fachgruppe : string | null;

}


/**
 * Ein Reader für das Einlesen der Daten einer GPP002-Datei
 */
export class UntisGPP002Csv extends UntisGPUCsv<UntisGPP002> {

	/**
	 * Erzeugt einen neuen Reader mit den übergebenen CSV-Daten als UTF-8-String
	 *
	 * @param csv   die CSV-Daten
	 */
	public constructor(csv: string) {
		super([
			{ name: 'lehrerKuerzel', type: 'string', required: false },
			{ name: 'jahr', type: 'number', required: true },
			{ name: 'monat', type: 'number', required: true },
			{ name: 'tag', type: 'number', required: true },
			{ name: 'stunde', type: 'number', required: true },
			{ name: 'fachKuerzel', type: 'string', required: true },
			{ name: 'klasseKuerzel', type: 'string', required: true },
			{ name: 'raumKuerzel', type: 'string', required: false },
			{ name: 'bezahlt', type: 'string', required: false },
			{ name: 'empty', type: 'string', required: false },
			{ name: 'idUnterricht', type: 'number', required: true },
			{ name: 'beginn', type: 'string', required: false },
			{ name: 'dauer', type: 'number', required: false },
			{ name: 'fachgruppe', type: 'number', required: false },
		], csv, ",");
	}

	public getGPU001(gpu014 : UntisGPU014Csv) : Array<UntisGPU001> {
		if ((!this.valid) || (!gpu014.valid))
			return new Array<UntisGPU001>();
		// Ermittle zunächst alle Datumseinträge aus den beiden Listen
		const dateStrings = new Set<string>();
		for (const obj of this.data)
			dateStrings.add(`${obj.jahr}-${obj.monat < 10 ? "0" + obj.monat : obj.monat}-${obj.tag < 10 ? "0" + obj.tag : obj.tag}`);
		for (const obj of gpu014.data)
			dateStrings.add(`${obj.datum.substring(0, 4)}-${obj.datum.substring(4, 6)}-${obj.datum.substring(6)}`);
		const dates = [...dateStrings].sort();
		// Bestimme die Wochentypen zu den einzelnen Datumseinträgen
		const mapDateToWochentyp = new Map<string, number>();
		let letzterTag : number | null = null;
		let maxWochentyp = 1;
		for (const dateStr of dates) {
			const date = new Date(dateStr);
			const tag = date.getDay();
			if ((letzterTag !== null) && (tag < letzterTag))
				maxWochentyp++;
			letzterTag = tag;
			mapDateToWochentyp.set(dateStr, maxWochentyp);
		}
		// Bestimme die Unterrichte...
		const tmpResult = new Array<UntisGPU001>();
		// ... anhand der aktuellen Unterrichte
		for (const obj of this.data) {
			const dateStr = `${obj.jahr}-${obj.monat < 10 ? "0" + obj.monat : obj.monat}-${obj.tag < 10 ? "0" + obj.tag : obj.tag}`;
			const date = new Date(dateStr);
			const wochentyp = mapDateToWochentyp.get(dateStr);
			const gpu001 = <UntisGPU001>{
				idUnterricht: obj.idUnterricht,
				klasseKuerzel: obj.klasseKuerzel,
				lehrerKuerzel: obj.lehrerKuerzel,
				fachKuerzel: obj.fachKuerzel,
				raumKuerzel: obj.raumKuerzel,
				wochentag: date.getDay(),
				stunde: obj.stunde,
				dauer: null,
				wochentyp: wochentyp ?? '',
			};
			if (gpu001.wochentag === 0)
				gpu001.wochentag = 7;
			tmpResult.push(gpu001);
		}
		// ... und zusätzlich anhand der Vertretungsunterrichte
		for (const obj of gpu014.data) {
			// Klausuren müssen nicht berücksichtigt werden
			if ((obj.vertretungsart === 'E') && ((obj.lehrerKuerzelAbsent === null) || (obj.lehrerKuerzelAbsent === "")))
				continue;
			// Prüfe andere Vertretungsarten
			if (obj.lehrerKuerzelAbsent === obj.lehrerKuerzelVertretung) {
				// TODO Der Lehrer hat sich nicht geändert
			} else {
				// Der Lehrer hat sich geändert. Somit muss der Unterricht hinzugefügt werden, da dieser nicht bei den aktuellen Unterrichten auftaucht (z.B. bei Entfall)
				const dateStr = `${obj.datum.substring(0, 4)}-${obj.datum.substring(4, 6)}-${obj.datum.substring(6)}`;
				const date = new Date(dateStr);
				const wochentyp = mapDateToWochentyp.get(dateStr);
				for (const klassenKuerzel of obj.klasseKuerzel.split("~")) {
					const gpu001 = <UntisGPU001>{
						idUnterricht: obj.idUnterricht,
						klasseKuerzel: klassenKuerzel,
						lehrerKuerzel: obj.lehrerKuerzelAbsent,
						fachKuerzel: obj.fachKuerzel,
						raumKuerzel: obj.raumKuerzel,
						wochentag: date.getDay(),
						stunde: obj.stunde,
						dauer: null,
						wochentyp: wochentyp ?? '',
					};
					if (gpu001.wochentag === 0)
						gpu001.wochentag = 7;
					tmpResult.push(gpu001);
				}
			}
		}
		// Sortieren der Einträge nach Unterrichtsnummer
		tmpResult.sort((a, b) => {
			let tmp = a.idUnterricht - b.idUnterricht;
			if (tmp !== 0)
				return tmp;
			tmp = a.klasseKuerzel.localeCompare(b.klasseKuerzel);
			if (tmp !== 0)
				return tmp;
			tmp = a.fachKuerzel.localeCompare(b.fachKuerzel);
			if (tmp !== 0)
				return tmp;
			tmp = a.lehrerKuerzel.localeCompare(b.lehrerKuerzel);
			if (tmp !== 0)
				return tmp;
			return ((a.wochentyp.length === 0) ? 0 : Number(a.wochentyp)) - ((b.wochentyp.length === 0) ? 0 : Number(b.wochentyp));
		});
		//... Duplikate entfernen und zusammenfassen
		const result = new Array<UntisGPU001>();
		let last : UntisGPU001 | null = null;
		const wochentypen = new Set<number>();
		for (const obj of tmpResult) {
			const curWt = (obj.wochentyp === "") ? 0 : Number(obj.wochentyp);
			if (last !== null) {
				if ((last.idUnterricht === obj.idUnterricht) && (last.klasseKuerzel === obj.klasseKuerzel) && (last.fachKuerzel === obj.fachKuerzel)
					&& (last.lehrerKuerzel === obj.lehrerKuerzel)) {
					if (curWt !== 0)
						wochentypen.add(curWt);
					if ((curWt === 0) || (wochentypen.size === maxWochentyp)) {
						obj.wochentyp = "";
						result.push(obj);
						last = null;
						wochentypen.clear();
					}
					continue;
				} else {
					for (const wt of wochentypen) {
						const clone = { ...last };
						clone.wochentyp = "" + wt;
						result.push(clone);
					}
					last = null;
					wochentypen.clear();
				}
			}
			if (curWt === 0) {
				result.push(obj);
			} else {
				wochentypen.add(curWt);
				last = obj;
			}
		}
		if (last !== null) {
			for (const wt of wochentypen) {
				const clone = { ...last };
				clone.wochentyp = "" + wt;
				result.push(clone);
			}
		}
		console.log(result);
		return result;
	}

}


/**
 * Die Schnittstellen-Definition der GPU014-Datei für die Vertretungen aus Untis
 */
export interface UntisGPU014 {

	/** Eine numerische ID, welche die Vertretung eindeutig identifiziert (z.B. 42) */
	idVertretung : number;

	/** Das Datum im Format "YYYYMMDD" */
	datum: string;

	/** Die Stunde im Zeitraster des Wochentags an dem der Unterricht stattfindet */
	stunde: number;

	/** Eine numerische ID, welche die Absenz eindeutig identifiziert (z.B. 42) */
	idAbsenz : number | null;

	/** Eine numerische ID, welche den Unterricht eindeutig identifiziert (z.B. 42) */
	idUnterricht : number;

	/** Das Kürzel des Lehrers (z.B. "BACH") */
	lehrerKuerzelAbsent : string | null;

	/** Das Kürzel des Lehrers (z.B. "BACH") */
	lehrerKuerzelVertretung : string | null;

	/** Das Kürzel des Faches (z.B. "D") */
	fachKuerzel : string;

	/** Das Kürzel des Statistik-Faches (z.B. "D") */
	fachStatistikKuerzel : string | null;

	/** Das Kürzel des Faches (z.B. "D") */
	fachVertretungKuerzel : string | null;

	/** Das Kürzel des Statistik-Faches (z.B. "D") */
	fachVertretungStatistikKuerzel : string | null;

	/** Das Kürzel des Raumes (z.B. "") */
	raumKuerzel : string | null;

	/** Das Kürzel des Raumes (z.B. "") */
	raumKuerzelVertretung : string | null;

	/** Statistik-Kennzeichen */
	statistik : string | null;

	/** Das Kürzel der Klasse (z.B. "05A") bzw. der Klassen (z.B. "05A~05B") */
	klasseKuerzel : string;

	/** Der Grund für die Absenz */
	grundAbsenz : string | null;

	/** Ein Text für die Vertretung */
	text : string | null;

	/** Die Art der Vertretung (Bit-Feld: Bit 0 -> Entfall, Bit 1 -> Betreuung, Bit 2 -> Sondereinsatz, Bit 3 -> Wegverlegung, Bit 4 -> Freisetzung,
	 *      Bit 5 -> Plus als Vertreter, Bit 6 -> Teilvertretung, Bit 7 -> Hinverlegung, Bit 16 -> Raumvertretung, Bit 17 -> Pausenaufsichtsvertretung,
	 *      Bit 18 -> Stunde ist unterrichtsfrei, Bit 20 -> Kennzeichen nicht drucken, Bit 21 -> Kennzeichen neu */
	art : number;

	/** Das Kürzel der Vertretungs-Klasse (z.B. "05A") bzw. der Vertretungs-Klassen (z.B. "05A~05B") */
	klasseVertretungKuerzel : string | null;

	/** Die Art der Vertretung (T - verlegt, F - verlegt von, W - Tausch, S - Betreuung, A - Sondereinsatz, C - Entfall, L - Freisetzung, P - Teil-Vertretung,
	 *    R - Raumvertretung, B - Pausenaufsichtsvertretung, ~ - Lehrertausch, E - Klausur) */
	vertretungsart : string;

	/** Der Zeitpunkt der letzten Änderung (YYYYMMDDHHmm) */
	timestamp: string | null;

	/** Vertretungen/Entfälle zählen */
	zaehlen: string | null;

	/** Ignorieren - Dummy für das Einlesen der Daten */
	dummy : string | null;

}


/**
 * Ein Reader für das Einlesen der Daten einer GPU014-Datei
 */
export class UntisGPU014Csv extends UntisGPUCsv<UntisGPU014> {

	/**
	 * Erzeugt einen neuen Reader mit den übergebenen CSV-Daten als UTF-8-String
	 *
	 * @param csv   die CSV-Daten
	 */
	public constructor(csv: string) {
		super([
			{ name: 'idVertretung', type: 'number', required: true },
			{ name: 'datum', type: 'string', required: true },
			{ name: 'stunde', type: 'number', required: true },
			{ name: 'idAbsenz', type: 'number', required: false },
			{ name: 'idUnterricht', type: 'number', required: true },
			{ name: 'lehrerKuerzelAbsent', type: 'string', required: false },
			{ name: 'lehrerKuerzelVertretung', type: 'string', required: false },
			{ name: 'fachKuerzel', type: 'string', required: true },
			{ name: 'fachStatistikKuerzel', type: 'string', required: false },
			{ name: 'fachVertretungKuerzel', type: 'string', required: false },
			{ name: 'fachVertretungStatistikKuerzel', type: 'string', required: false },
			{ name: 'raumKuerzel', type: 'string', required: false },
			{ name: 'raumKuerzelVertretung', type: 'string', required: false },
			{ name: 'statistik', type: 'string', required: false },
			{ name: 'klasseKuerzel', type: 'string', required: true },
			{ name: 'grundAbsenz', type: 'string', required: false },
			{ name: 'text', type: 'string', required: false },
			{ name: 'art', type: 'number', required: true },
			{ name: 'klasseVertretungKuerzel', type: 'string', required: false },
			{ name: 'vertretungsart', type: 'string', required: true },
			{ name: 'timestamp', type: 'string', required: false },
			{ name: 'zaehlen', type: 'string', required: false },
			{ name: 'dummy', type: 'string', required: false },
		], csv);
	}

}
