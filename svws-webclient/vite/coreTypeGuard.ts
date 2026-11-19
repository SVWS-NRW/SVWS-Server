import { existsSync } from "node:fs";
import { resolve } from "node:path";
import type { Plugin } from "vite";

/** Der relative Pfad (ab dem Verzeichnis der Core-Types in core/src/asd/types) zu den JSON-Dateien im Verzeichnis der ASD-Ressourcen */
const RELATIVER_PFAD_JSON = "../../../../../svws-asd/src/main/resources/de/svws_nrw/asd/types";

/**
 * Bereinigt eine Modul-ID von Query-Parametern und Windows-Pfadtrennern.
 */
function normalisiere(id: string): string {
	return id.replace(/[?#].*$/, "").replaceAll("\\", "/");
}

/**
 * Dieses Plugin prüft beim Build, ob für alle Core-Types, die im Bundle enthalten sind, auch die zugehörige
 * JSON-Datei mit den Daten im Bundle enthalten ist. Damit wird bei Clients, die die Daten der Core-Types nicht
 * vollständig, sondern gezielt über JSON-Importe laden, vermieden, dass ein neu verwendeter Core-Type erst zur
 * Laufzeit mit einer CoreTypeException auffällt. Fehlt eine JSON-Datei, so bricht der Build mit einer Meldung ab.
 *
 * @returns das Plugin
 */
export function coreTypeGuard(): Plugin {
	return {
		name: "core-type-guard",
		apply: "build",
		generateBundle(_options, bundle) {
			const klassen = new Map<string, string>();
			const jsons = new Set<string>();
			for (const chunk of Object.values(bundle)) {
				if (chunk.type !== "chunk") {
					continue;
				}
				for (const rawId of Object.keys(chunk.modules)) {
					const id = normalisiere(rawId);
					const treffer = /^(.*\/core\/src\/asd\/types)\/(.+)\.ts$/.exec(id);
					if (treffer !== null) {
						// Ein Core-Type liegt vor, wenn es für die Klasse eine JSON-Datei am gleichen Pfad gibt
						if (existsSync(resolve(treffer[1], RELATIVER_PFAD_JSON, treffer[2] + ".json"))) {
							klassen.set(treffer[2], id);
						}
						continue;
					}
					const json = /\/svws-asd\/src\/main\/resources\/de\/svws_nrw\/asd\/types\/(.+)\.json$/.exec(id);
					if (json !== null) {
						jsons.add(json[1]);
					}
				}
			}
			const fehlend = [...klassen.keys()].filter(name => !jsons.has(name)).sort();
			if (fehlend.length > 0) {
				this.error("Für die folgenden Core-Types fehlen die JSON-Daten im Bundle: " + fehlend.join(", ")
					+ ". Bitte die zugehörige JSON-Datei importieren und den Core-Type bei der Initialisierung der Core-Types ergänzen "
					+ "(laufbahnplanung: src/main.ts, enmserver: src/main.ts).");
			}
		},
	};
}
