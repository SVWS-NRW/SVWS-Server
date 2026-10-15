// ═══════════════════════════════════════════════════════
// SVWS Katalog-Konfiguration
// Basis für ModelProxy-Scanner + Regression Suite
// ═══════════════════════════════════════════════════════

// ── Umgebungen ────────────────────────────────────────────────
// testdaten pro Umgebung: gleiche Export-NAMEN wie bisher (SCHUELER_ID
// etc., siehe unten) bleiben für alle ~97 abhängigen Dateien unverändert
// importierbar — nur die Quelle ist jetzt pro Umgebung konfigurierbar
// statt fest auf GymAbiLite verdrahtet.
// Die baseUrl-Werte unten werden nur als Fallback für lokale Entwickler-Runs
// verwendet. In der Pipeline überschreibt VITE_targetHost alle Werte.
export const ENVIRONMENTS = {
	nightly: {
		baseUrl: 'https://nightly.svws-nrw.de',
		// GEÄNDERT: Schema "GymAbiLite" existiert auf nightly nicht mehr (live
		// bestätigt — Schema-Dropdown zeigt nur noch Migration_BK, Migration_G,
		// Migration_GE, Migration_GymAbi, TestDB_BK, TestDB_G, TestDB_GE,
		// TestDB_GY). Auf TestDB_GY umgestellt, das bereits als eigener
		// "nightly-gy"-Eintrag unten mit denselben Testdaten-IDs (9311/9312)
		// dokumentiert war.
		schema: 'TestDB_GY',
		schemaId: '1',
		testdaten: {
			/** Eleonora Externa — bestehender Standard-Testschüler, unverändert */
			schuelerId: '9312',
			// TODO: Zweck/Inhalt dieser zweiten ID war in der bisherigen Config
			// nicht dokumentiert — übernommen, vor Nutzung live verifizieren.
			schuelerIdMitDaten: '9311',
		},
	},
	// GEÄNDERT ggü. bisherigem Stand (vorher fälschlich ebenfalls GymAbiLite):
	// stable hat tatsächlich das Schema TestDB_BK — ANDERE Testdaten als
	// GymAbiLite. IDs bewusst als TODO-Platzhalter (keine Vermutung), erst
	// am echten Host zu ermitteln. Kein Test bindet diese Umgebung statisch;
	// die generischen SVWS_ENV-Läufe dürfen schülerbezogene Tests erst nutzen,
	// wenn die TODO-IDs durch verifizierte Testdaten ersetzt wurden.
	stable: {
		baseUrl: 'https://stable.svws-devops.de',
		schema: 'TestDB_BK',
		schemaId: '1',
		testdaten: {
			schuelerId: 'TODO_STABLE_SCHUELER_ID',
			schuelerIdMitDaten: 'TODO_STABLE_SCHUELER_ID_MIT_DATEN',
		},
	},
	// NEU. Schema-Annahme analog stable (beide *-devops.de-Hosts) — TODO:
	// am echten Host bestätigen, könnte abweichen. IDs bewusst TODO. Kein Test
	// bindet diese Umgebung statisch; schülerbezogene Tests sind damit noch
	// nicht für einen generischen SVWS_ENV=alpha-Lauf freigegeben.
	alpha: {
		baseUrl: 'https://alpha.svws-devops.de',
		schema: 'TestDB_BK',
		schemaId: '1',
		testdaten: {
			schuelerId: 'TODO_ALPHA_SCHUELER_ID',
			schuelerIdMitDaten: 'TODO_ALPHA_SCHUELER_ID_MIT_DATEN',
		},
	},
	// NEU (Portabilitäts-Audit 2026-08-03): release-Proxy — dasselbe Schema wie
	// "release" (TestDB_GY), aber gehostet auf nightly statt auf dem aktuell
	// nicht erreichbaren release.svws-nrw.de. Ermöglicht ein Portabilitäts-
	// Audit gegen ein zweites Schema, ohne auf die echten Hosts (stable/alpha/
	// release, alle down) angewiesen zu sein.
	// IDs live verifiziert (Ground-Truth-Check 2026-08-03): 9312 = "Eleonora
	// Externa", existiert, aber 0 Telefone/0 Erzieher/idKlasse=-1 (Leer-Fall,
	// identisch zu nightly/GymAbiLite). 9311 = "TestLisa Testschueler",
	// aktiver Lernabschnitt (Klasse 08c) + 1 Telefon + 3 Erzieher + Leistungen-
	// Tabelle mit 26 Zeilen (live UI bestätigt) — identische Daten wie in
	// GymAbiLite, derselbe nightly-Host teilt sich offenbar die Testdaten-IDs
	// über beide Schemata hinweg.
	'nightly-gy': {
		baseUrl: 'https://nightly.svws-nrw.de',
		schema: 'TestDB_GY',
		schemaId: '1',
		testdaten: {
			schuelerId: '9312',
			schuelerIdMitDaten: '9311',
		},
	},
	// NEU (Schema-Matrix 2026-08-03). ACHTUNG: schemaId ist HIER 52, nicht 1
	// wie bei allen anderen Umgebungen — live bestätigt über den App-eigenen
	// Login-Default (Navigation landete auf .../TestDB_GE/52/schueler/4265/
	// daten). Abfragen mit schuljahresabschnitt=1 liefern für TestDB_GE 404.
	// IDs live verifiziert: 4265 = "Bican, Petra" (Klasse 470, 3 Telefone,
	// 2 Erzieher — kein echter Leer-Fall auffindbar, alle 239 Schüler der
	// Auswahlliste haben idKlasse>0). 4358 = "Braunschweig, Stephan",
	// identisches Datenprofil (Klasse 470, 3 Telefone, 2 Erzieher, aktiver
	// Lernabschnitt bestätigt) — als schuelerIdMitDaten gewählt.
	'nightly-ge': {
		baseUrl: 'https://nightly.svws-nrw.de',
		schema: 'TestDB_GE',
		schemaId: '52',
		testdaten: {
			schuelerId: '4265',
			schuelerIdMitDaten: '4358',
		},
	},
	// NEU (Schema-Matrix 2026-08-03). schemaId 1 bestätigt (wie grundschule-
	// Eintrag unten, gleiches Schema TestDB_G). 9311/9312 existieren in
	// TestDB_G NICHT (0 Telefone/0 Erzieher, kein Lernabschnitt-Treffer).
	// IDs live verifiziert: 2096 = "Ziegenhagen, Stefanie", idKlasse=-1 laut
	// Auswahlliste, aber NICHT ganz leer (1 Telefon, 2 Erzieher) — bester
	// verfügbarer Leer-Fall-Kandidat. 1900 = "Ammon, Christin" (bereits als
	// grundschule.testdaten.schuelerId bekannt) — voll verifiziert: aktiver
	// Lernabschnitt (Klasse MMA) + 3 Telefone + Erzieher + Leistungen-Tabelle
	// mit 19 Zeilen (live UI bestätigt).
	'nightly-g': {
		baseUrl: 'https://nightly.svws-nrw.de',
		schema: 'TestDB_G',
		schemaId: '1',
		testdaten: {
			schuelerId: '2096',
			schuelerIdMitDaten: '1900',
		},
	},
	// NEU (Schema-Matrix 2026-08-03). schemaId 1 bestätigt (idSchuljahres-
	// abschnitt laut Auswahlliste-Response = 1). 9311/9312 existieren in
	// TestDB_S NICHT. Kein Leer-Fall-Kandidat auffindbar — alle 571 Schüler
	// der Auswahlliste haben idKlasse>0. IDs live verifiziert: 712 = "Bald,
	// Lukas" (Klasse 5a, 3 Telefone, 2 Erzieher) als schuelerId gewählt.
	// 749 = "Schrade, Barbara" — voll verifiziert: aktiver Lernabschnitt
	// (Klasse 05x) + 2 Telefone + 2 Erzieher + Leistungen-Tabelle mit
	// 5 Zeilen (live UI bestätigt) — als schuelerIdMitDaten gewählt.
	'nightly-s': {
		baseUrl: 'https://nightly.svws-nrw.de',
		schema: 'TestDB_S',
		schemaId: '1',
		testdaten: {
			schuelerId: '712',
			schuelerIdMitDaten: '749',
		},
	},
	// NEU. Schema laut Vorgabe TestDB_GY. IDs bewusst TODO. Kein Test bindet
	// diese Umgebung statisch; schülerbezogene Tests sind damit noch nicht für
	// einen generischen SVWS_ENV=release-Lauf freigegeben.
	release: {
		baseUrl: 'https://release.svws-nrw.de',
		schema: 'TestDB_GY',
		schemaId: '1',
		testdaten: {
			schuelerId: 'TODO_RELEASE_SCHUELER_ID',
			schuelerIdMitDaten: 'TODO_RELEASE_SCHUELER_ID_MIT_DATEN',
		},
	},
	// NEU — Andockpunkt für CI/Docker-Container (SVWS-Server-smoke-Branch).
	// baseUrl wird NIE gelesen, wenn dieser Eintrag greift: die Pipeline
	// setzt IMMER VITE_targetHost (siehe getAppBase()/getEnv() unten), das
	// hat Vorrang. Platzhalter hier nur, damit ein versehentlicher Lauf
	// ohne VITE_targetHost sichtbar scheitert statt auf undefined zu laufen.
	// schema GymAbi01 ist aus der Docker-DB-Analyse belegt (subproject-
	// plugin.gradle). Testdaten-IDs: ANNAHME, KEIN Beweis — Indiz aus der
	// öffentlichen GymAbi.mdb-Quelle (Schüler 9312 = "Eleonora Externa",
	// identischer Name wie in der Branch-Docker-Suite UND im Live-Pilot
	// gegen nightly) — nicht bewiesen, dass das migrierte GymAbi01 exakt
	// diesen Stand hat. Am laufenden Container verifizieren.
	docker: {
		baseUrl: 'https://DOCKER-HOST-KOMMT-VON-VITE_targetHost.invalid',
		schema: 'GymAbi01',
		schemaId: '1',
		testdaten: {
			schuelerId: '9312', // ANNAHME, s.o.
			schuelerIdMitDaten: '9311', // ANNAHME analog
		},
	},
	// Grundschule-Schema für schulform-spezifische Tests (Kindergärten, etc.)
	// — unverändert, kein Teil der 4-Umgebungen-Migration.
	grundschule: {
		baseUrl: 'https://nightly.svws-nrw.de',
		schema: 'TestDB_G',
		schemaId: '1',
		testdaten: {
			schuelerId: '1900',
			// Kein separater VOLL-Referenzschüler für Grundschule bekannt/gebraucht bisher —
			// Platzhalter nur für Typ-Konsistenz mit den anderen testdaten-Blöcken (SCHUELER_ID_MIT_DATEN
			// wird für grundschule aktuell nirgends importiert/genutzt).
			schuelerIdMitDaten: 'TODO_GRUNDSCHULE_SCHUELER_ID_MIT_DATEN',
		},
	},
} as const;

export type EnvName = keyof typeof ENVIRONMENTS;

// ABSICHTLICH weiterhin SVWS_ENV (nicht SVWS_TEST_ENV): package.json nutzt
// bereits "SVWS_ENV=stable ..." in zwei Scripts (test:stable, scan:stable)
// — eine Umbenennung hätte die still auf 'nightly' zurückfallen lassen,
// ohne dass das auffällt. Gleicher Name, erweiterte Funktion.
export function getEnv(): EnvName {
	const env = process.env.SVWS_ENV;
	if (env !== undefined && env !== '') {
		if (!(env in ENVIRONMENTS)) {
			throw new Error(`Unbekannte Umgebung SVWS_ENV="${env}". Bekannt: ${Object.keys(ENVIRONMENTS).join(', ')}`);
		}
		return env as EnvName;
	}
	// CI (Gradle im smoke-Branch) setzt NUR VITE_targetHost, nie SVWS_ENV —
	// ohne diesen Zweig würden Testdaten-IDs dort fälschlich auf die
	// nightly-Werte zurückfallen, obwohl gegen den Docker-Container
	// (GymAbi01) gelaufen wird.
	if (process.env.VITE_targetHost !== undefined && process.env.VITE_targetHost !== '') {
		return 'docker';
	}
	return 'nightly';
}

export function getBaseUrl(): string {
	if (process.env.VITE_targetHost !== undefined && process.env.VITE_targetHost !== '') {
		return process.env.VITE_targetHost;
	}
	return ENVIRONMENTS[getEnv()].baseUrl;
}

export function getAppBase(): string {
	// VITE_targetHost hat Vorrang (CI setzt es automatisch für den Docker-
	// Container; ein expliziter lokaler Lauf kann es ebenso setzen) — dann
	// NICHT selbst aus schema/schemaId zusammenbauen, sondern wörtlich
	// übernehmen (identisches Verhalten zu APIUtils.ts:17 im smoke-Repo).
	if (process.env.VITE_targetHost !== undefined && process.env.VITE_targetHost !== '') {
		return process.env.VITE_targetHost;
	}
	const e = ENVIRONMENTS[getEnv()];
	return `${e.baseUrl}/#/${e.schema}/${e.schemaId}`;
}

// ── Test-Schüler-IDs ──────────────────────────────────────────
// Gleiche Export-Namen wie bisher — Quelle ist jetzt der testdaten-Block
// der per getEnv() gewählten Umgebung statt eines fest verdrahteten Werts.
/** GymAbiLite-Schüler: Klasse 10, hat Sprachen, 0 ERZB, 0 Betriebe */
export const SCHUELER_ID = ENVIRONMENTS[getEnv()].testdaten.schuelerId;
/** VOLL-Referenz-Schüler mit reichen Bestandsdaten (nightly: 9311, hat z.B. Telefonnummer, live per GET bestätigt 2026-08-01). Für rein lesende Tests, die echte Daten statt des Leer-Falls SCHUELER_ID brauchen — niemals mutieren. */
export const SCHUELER_ID_MIT_DATEN = ENVIRONMENTS[getEnv()].testdaten.schuelerIdMitDaten;
