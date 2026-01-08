<template>
	<div v-if="data" class="page page-flex-col gap-0 page--statistik">
		<svws-ui-header>
			<div class="flex flex-col">
				<div class="inline-block">{{ schulname }}</div>
				<!--TODO: Dynamischer Schuljahresabschnitt-->
				<div class="opacity-50">
					<svws-ui-tooltip>
						{{ schulform }} {{ schulNr }}
						<template #content>
							Schulform und Nummer
						</template>
					</svws-ui-tooltip>
				</div>
			</div>
		</svws-ui-header>
		<svws-ui-tab-bar :tab-manager :focus-switching-enabled :focus-help-visible>
			<template v-if="tabManager().tab.name === 'dashboard'">
				<div class="page page-grid grid-cols-2 lg:grid-cols-4" style="grid-auto-rows: min-content">
					<svws-ui-dashboard-tile :span="1" color="transparent" title="Adresse">
						<div class="mt-5 whitespace-pre-line">{{ adresse }}</div>
						<div class="mt-2 flex gap-1">
							<svws-ui-button type="secondary" size="small">Schuldaten bearbeiten</svws-ui-button>
						</div>
					</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile :span="2" color="dark" title="Nächster Termin" number="15.10.2025" number-label="Frist zur Einreichung">
						Meldung der Statistik
					</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile title="SchiPS-Schnellmeldung">
						Hier können Sie die Schnellmeldung der SchiPS-Zahlen an das Bildungsportal vornehmen.
						<template #number>
							<div class="flex gap-1">
								<svws-ui-button @click="openModal">
									Absenden
								</svws-ui-button>
							</div>
						</template>
					</svws-ui-dashboard-tile>
					<!--<svws-ui-dashboard-tile title="Kontaktdaten der Schule">
						<div class="flex flex-col gap-3">
							<div>
								<div v-if="telefon" title="Telefon">
									<span class="inline-block w-12">Tel</span>
									{{ telefon }}
								</div>
								<div v-if="fax" title="Fax">
									<span class="inline-block w-12">Fax</span>
									{{ fax }}
								</div>
							</div>
							<div>
								<div v-if="email" title="E-Mail">
									{{ email }}
								</div>
								<div title="Web-Adresse">
									{{ webAdresse || 'https://schule.nrw.de' }}
								</div>
							</div>
						</div>
					</svws-ui-dashboard-tile>-->
					<svws-ui-dashboard-tile span="full" color="transparent" title="Hinweis" class="my-20">
						Die Statistik ist noch in Entwicklung und aktuell nur zur Vorschau.<br>
						Alle Zahlen, Fehler und andere Inhalte in diesem Bereich sind Beispiele und keine aktuellen Daten aus dem Client.
					</svws-ui-dashboard-tile>
					<!--TODO: Dynamic Anzahl Schüler*innen und andere Daten-->
					<svws-ui-dashboard-tile color="dark" number="57" number-label="Fehler insgesamt">
						Relevante Daten für die Statistik müssen noch korrigiert werden.
					</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile title="Schülerdaten" number="30" number-label="zu korrigieren" @click="setTab({ name: 'Schüler', text: 'Schüler' })" clickable>
						1.425 Schüler angemeldet
					</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile title="Lehrerdaten" number="17" number-label="zu korrigieren" @click="setTab({ name: 'Lehrer', text: 'Lehrer' })" clickable>
						132 Lehrkräfte angestellt
					</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile title="Unterrichtsdaten" number="10" number-label="zu korrigieren" @click="setTab({ name: 'Unterrichts', text: 'Unterricht' })" clickable>
						58 Kurse angeboten
					</svws-ui-dashboard-tile>
				</div>
			</template>

			<template v-if="tabManager().tab.name === 'Schüler'">
				<div class="page page-grid-cards grid-cols-2 lg:grid-cols-4 gap-2">
					<svws-ui-dashboard-tile :span="2" color="transparent" :title="`${tabManager().tab.name}daten`">
						<p>
							Dieser Bereich ist aktuell nur eine Vorschau. Alle Inhalte sind Beispiele und keine aktuellen Daten aus dem Client.
						</p>
					</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile number="1.425" number-label="Datensätze" />
					<svws-ui-dashboard-tile color="dark" number="30" number-label="Fehler zu korrigieren" />
					<svws-ui-dashboard-tile span="full" color="transparent">
						<svws-ui-spacing :size="2" />
						<svws-ui-table :items="[]" :no-data="false" :columns="cols">
							<template #body>
								<div role="row" class="svws-ui-tr" v-for="(item) in schuelerFehler" :key="item.id">
									<div role="cell" class="svws-ui-td">{{ item.id }}</div>
									<div role="cell" class="svws-ui-td">---</div>
									<div role="cell" class="svws-ui-td">{{ item.details?.fehler }}</div>
									<div role="cell" class="svws-ui-td">{{ item.details?.fehlertext }}</div>
									<div role="cell" class="svws-ui-td svws-align-center">
										<svws-ui-tooltip>
											<span class="icon" :class="item.details?.fehlerart === 'Harter Fehler' ? 'i-ri-alert-fill' : item.details?.fehlerart === 'Muss-Fehler' ? 'i-ri-alert-line' : 'i-ri-information-line'" />
											<template #content>
												{{ item.details?.fehlerart }}
											</template>
										</svws-ui-tooltip>
									</div>
								</div>
							</template>
						</svws-ui-table>
					</svws-ui-dashboard-tile>
				</div>
			</template>

			<template v-if="tabManager().tab.name === 'Lehrer'">
				<div class="page page-grid-cards grid-cols-2 lg:grid-cols-4 gap-2">
					<svws-ui-dashboard-tile :span="2" color="transparent" :title="`${tabManager().tab.name}daten`">
						<p>
							Dieser Bereich ist aktuell nur eine Vorschau. Alle Inhalte sind Beispiele und keine aktuellen Daten aus dem Client.
						</p>
					</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile number="132" number-label="Datensätze" />
					<svws-ui-dashboard-tile color="dark" number="17" number-label="Fehler zu korrigieren" />
					<svws-ui-dashboard-tile span="full" color="transparent">
						<svws-ui-spacing :size="2" />
						<svws-ui-table :items="[]" :no-data="false" :columns="cols">
							<template #body>
								<template v-if="tabManager().tab.name === 'Lehrer'">
									<div role="row" class="svws-ui-tr" v-for="(item) in lehrerFehler" :key="item.id">
										<div role="cell" class="svws-ui-td">{{ item.id }}</div>
										<div role="cell" class="svws-ui-td">{{ item.kuerzel }}</div>
										<div role="cell" class="svws-ui-td">{{ item.details?.fehler }}</div>
										<div role="cell" class="svws-ui-td">{{ item.details?.fehlertext }}</div>
										<div role="cell" class="svws-ui-td svws-align-center">
											<svws-ui-tooltip>
												<span class="icon" :class="item.details?.fehlerart === 'Harter Fehler' ? 'i-ri-alert-fill' : item.details?.fehlerart === 'Muss-Fehler' ? 'i-ri-alert-line' : 'i-ri-information-line'" />
												<template #content>
													{{ item.details?.fehlerart }}
												</template>
											</svws-ui-tooltip>
										</div>
									</div>
								</template>
							</template>
						</svws-ui-table>
					</svws-ui-dashboard-tile>
				</div>
			</template>

			<template v-if="tabManager().tab.name === 'Unterricht'">
				<div class="page page-grid-cards grid-cols-2 lg:grid-cols-4 gap-2">
					<svws-ui-dashboard-tile :span="2" color="transparent" :title="`${tabManager().tab.name}daten`">
						<p>
							Dieser Bereich ist aktuell nur eine Vorschau. Alle Inhalte sind Beispiele und keine aktuellen Daten aus dem Client.
						</p>
					</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile number="58" number-label="Datensätze" />
					<svws-ui-dashboard-tile color="dark" number="10" number-label="Fehler zu korrigieren" />
					<svws-ui-dashboard-tile span="full" color="transparent">
						<svws-ui-spacing :size="2" />
						<svws-ui-table :items="[]" :no-data="false" :columns="cols">
							<template #body>
								<div role="row" class="svws-ui-tr" v-for="(item) in unterrichtFehler" :key="item.id">
									<div role="cell" class="svws-ui-td">{{ item.id }}</div>
									<div role="cell" class="svws-ui-td">---</div>
									<div role="cell" class="svws-ui-td">{{ item.details?.fehler }}</div>
									<div role="cell" class="svws-ui-td">{{ item.details?.fehlertext }}</div>
									<div role="cell" class="svws-ui-td svws-align-center">
										<svws-ui-tooltip>
											<span class="icon" :class="item.details?.fehlerart === 'Harter Fehler' ? 'i-ri-alert-fill' : item.details?.fehlerart === 'Muss-Fehler' ? 'i-ri-alert-line' : 'i-ri-information-line'" />
											<template #content>
												{{ item.details?.fehlerart }}
											</template>
										</svws-ui-tooltip>
									</div>
								</div>
							</template>
						</svws-ui-table>
					</svws-ui-dashboard-tile>
				</div>
			</template>
			<template v-if="tabManager().tab.name === 'Summen'">
				<div class="page page-grid-cards grid-cols-2 lg:grid-cols-4 gap-2">
					<svws-ui-dashboard-tile :span="2" color="transparent" title="Summendaten">
						<p class="text-red-600 font-semibold mb-2">
							Bitte die Schülersummen in KLD323 prüfen
						</p>
						<p class="text-red-600 font-semibold">
							Bitte die Schülersummen in Zuwanderungsgeschichte prüfen
						</p>
					</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile number="1425" number-label="Schülerzahl insg." />
					<svws-ui-dashboard-tile color="dark" number="30" number-label="Fehler zu korrigieren" />
					<svws-ui-dashboard-tile span="full" color="transparent">
						<svws-ui-spacing :size="2" />
						<svws-ui-table :items="summenData" :columns="summenCols" :no-data="false" />
						<svws-ui-spacing :size="2" />
						<div class="grid grid-cols-3 gap-4 text-sm">
							<div>
								<div class="font-semibold mb-2">
									<span class="text-blue-600">132</span> Lehrkräfte mit...
								</div>
								<div class="ml-4">
									<span class="text-blue-600">98653,25</span> Stunden Unterricht
								</div>
								<div class="ml-4 mt-1">
									<span class="text-blue-600">950</span> Nicht unterrichtliche Tätigkeiten [Std.]
								</div>
							</div>
							<div>
								<div class="font-semibold mb-2">
									<span class="text-blue-600">1425</span> Schülerzahl insg.
								</div>
								<div class="ml-4">
									<span class="text-blue-600">760</span> weiblich
								</div>
								<div class="ml-4 mt-1">
									<span class="text-blue-600">34</span> Ausländer insg.
								</div>
								<div class="ml-4 mt-1">
									<span class="text-blue-600">18</span> weiblich
								</div>
							</div>
							<div>
								<div class="font-semibold mb-2">
									<span class="text-blue-600">12</span> Schüler/innen im gemeinsamen Unterricht
								</div>
								<div class="ml-4 mt-1">
									<span class="text-blue-600">80</span> Teilnehmer/-innen "Betreuung"
								</div>
							</div>
						</div>
					</svws-ui-dashboard-tile>
				</div>
			</template>
		</svws-ui-tab-bar>
	</div>
	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-bar-chart-2-line" />
	</div>
	<!-- SchiPS-Modal -->
	<svws-ui-modal :show="isModalOpen" @update:show="isModalOpen = $event" size="medium">
		<template #modalTitle>
			<div class="bg-purple-800 text-white p-4 rounded-t-lg">SchiPS-Schnellmeldung</div>
		</template>
		<template #modalContent>
			<!-- Tabelle mit Zahlen -->
			<div class="mb-4">
				<svws-ui-dashboard-tile color="transparent" title="Statistik-Zahlen" class="mb-4">
					<svws-ui-table :items="schipsData" :columns="schipsColumns" :no-data="false">
						<template #footer>
							<div class="svws-ui-tr" :style="`grid-template-columns: ${schipsColumns.map(c => '1fr').join(' ')}`">
								<div class="svws-ui-td" />
								<div class="svws-ui-td font-semibold">{{ totalSchueler }}</div>
								<div class="svws-ui-td" />
								<div class="svws-ui-td" />
							</div>
						</template>
					</svws-ui-table>
				</svws-ui-dashboard-tile>
			</div>

			<!-- Eingabefelder für Benutzername und Passwort -->
			<div class="bg-purple-100 p-4 rounded-lg mb-4">
				<h4 class="text-lg font-semibold mb-2">Anmeldedaten Bildungsportal</h4>
				<div class="mb-4">
					<svws-ui-text-input v-model="username" label="Benutzername" placeholder="Benutzername eingeben" class="mb-2" />
					<svws-ui-text-input v-model="password" label="Passwort" type="password" placeholder="Passwort eingeben" />
				</div>

				<!-- Progress Bar -->
				<div v-if="isLoading" class="mb-4">
					<div class="w-full bg-gray-200 rounded-full h-4">
						<div class="bg-blue-600 h-4 rounded-full transition-all duration-300" :style="{ width: progress + '%' }" />
					</div>
					<p class="text-sm text-gray-600 mt-2">Daten werden übertragen... {{ progress }}%</p>
				</div>

				<!-- Senden-Button -->
				<div class="flex justify-end">
					<svws-ui-button @click="sendData" :disabled="!username || !password || isLoading">
						{{ isLoading ? 'Wird gesendet...' : 'Senden' }}
					</svws-ui-button>
				</div>

				<!-- Erfolgsmeldung -->
				<div v-if="successMessage" class="mt-4 p-2 bg-green-100 text-green-800 rounded">
					{{ successMessage }}
				</div>
			</div>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref, shallowRef } from "vue";
	import { type TabData, type DataTableColumn, TabManager, useRegionSwitch } from "@ui";
	import type { StatistikAppProps } from "./SStatistikAppProps";

	const props = defineProps<StatistikAppProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const schulname = computed(() => {
		const name = props.schule.bezeichnung1;
		return (name.length > 0) ? name : "Schule";
	});

	const schulNr = computed(() => {
		const nr = props.schule.schulNr;
		return nr.toString();
	});

	const schulform = computed(() => {
		const form = props.schule.schulform;
		return form;
	});

	const adresse = computed(() => {
		const strassenname = props.schule.strassenname;
		const hausnummer = props.schule.hausnummer;
		const hausnummerZusatz = props.schule.hausnummerZusatz;
		const plz = props.schule.plz;
		const ort = props.schule.ort;

		return `${strassenname} ${hausnummer}${hausnummerZusatz !== null ? " " + hausnummerZusatz : ""}\n${plz} ${ort}`;
	});

	const telefon = computed(() => {
		const telefon = props.schule.telefon;
		return telefon;
	});

	const fax = computed(() => {
		const fax = props.schule.fax;
		return fax;
	});

	const email = computed(() => {
		const email = props.schule.email;
		return email;
	});

	const webAdresse = computed(() => {
		const webAdresse = props.schule.webAdresse;
		return webAdresse;
	});

	const data = true;

	const cols: DataTableColumn[] = [
		{ key: "id", label: "ID", span: 0.25, fixedWidth: 5 },
		{ key: "kuerzel", label: "Kürzel", span: 0.25 },
		{ key: "fehler", label: "Fehler", span: 0.25 },
		{ key: "fehlertext", label: "Erläuterung", span: 2 },
		{ key: "fehlerart", label: "Typ", tooltip: "Harter Fehler, Muss-Fehler oder Hinweis", span: 0.25, fixedWidth: 4 },
	];

	const summenCols: DataTableColumn[] = [
		{ key: "kategorie", label: "Kategorie", span: 2 },
		{ key: "anzahlSaetze", label: "Anzahl Sätze", span: 1, align: "center" },
		{ key: "geprueft", label: "geprüft", span: 1, align: "center" },
		{ key: "fehlerfrei", label: "fehlerfrei", span: 1, align: "center" },
	];

	const summenData = [
		{ kategorie: "Adressen Ihrer Schule", anzahlSaetze: '0', geprueft: '0', fehlerfrei: '0' },
		{ kategorie: "Lehrerdaten LID 123", anzahlSaetze: 132, geprueft: 132, fehlerfrei: '0' },
		{ kategorie: "Unterrichtsverteilung UVD 223", anzahlSaetze: 262, geprueft: 262, fehlerfrei: 217 },
		{ kategorie: "Klassendaten KLD 323", anzahlSaetze: 28, geprueft: 28, fehlerfrei: '0' },
		{ kategorie: "Grunddaten SCD 011", anzahlSaetze: 8, geprueft: 8, fehlerfrei: 8 },
		{ kategorie: "Abgänge SCD 012", anzahlSaetze: '0', geprueft: '0', fehlerfrei: '0' },
		{ kategorie: "Abiturprüfungsergebnisse", anzahlSaetze: 65, geprueft: 65, fehlerfrei: 55 },
		{ kategorie: "Internatsplätze", anzahlSaetze: "kein Internat", geprueft: "1", fehlerfrei: "1" },
		{ kategorie: "Zuwanderungsgeschichte", anzahlSaetze: 42, geprueft: 42, fehlerfrei: 42 },
		{ kategorie: "Wohnorte", anzahlSaetze: 28, geprueft: 28, fehlerfrei: 27 },
		{ kategorie: "Altersstruktur", anzahlSaetze: 28, geprueft: 28, fehlerfrei: 1 },
	];

	async function setTab(tab: TabData) {
	}

	const refTabManager = shallowRef<TabManager>(new TabManager([
		{ name: "dashboard", text: "Übersicht" },
		{ name: "Schüler", text: "Schüler" },
		{ name: "Lehrer", text: "Lehrkräfte" },
		{ name: "Unterricht", text: "Unterricht" },
		{ name: "Summen", text: "Summen" },
	], { name: "dashboard", text: "Übersicht" }, setTab));

	function tabManager(): TabManager {
		return refTabManager.value;
	}

	const fehlerDetailsMap = new Map<number, { fehler: string; fehlertext: string; fehlerart: string }>([
		[10001, {
			fehler: "SSG2",
			fehlertext: "Unzulässige Eintragung im Feld Jahr (Geburtsdatum). Zulässig sind nur Werte 1971 bis 2020.",
			fehlerart: "Harter Fehler",
		}],
		[10002, {
			fehler: "SSH1",
			fehlertext: "Die Kombination aus Herkunft und höchster allgemein bildender Abschluss ist unzulässig.",
			fehlerart: "Harter Fehler",
		}],
		[10003, {
			fehler: "SUH3",
			fehlertext: "Schüler ist neu an der Schule und hat keine Herkunftsschule eingetragen.",
			fehlerart: "Muss-Fehler",
		}],
		[10004, {
			fehler: "SSS2",
			fehlertext: "Staatsangehörigkeit fehlt/fehlerhaft.",
			fehlerart: "Muss-Fehler",
		}],
		[10005, {
			fehler: "SSG3",
			fehlertext: "Entlassdatum der abgebenden Schule unplausibel.",
			fehlerart: "Harter Fehler",
		}],
		[10006, {
			fehler: "SSB1",
			fehlertext: "Schüler nimmt am offenen Ganztag und an anderer Betreuungsmaßnahme teil. Dies ist unzulässig.",
			fehlerart: "Harter Fehler",
		}],
	]);

	const schuelerFehler = Array.from({ length: 30 }, () => {
		const randomId = Math.floor(Math.random() * 9000) + 1000; // Zufällige 4-stellige Zahl
		const keys = Array.from(fehlerDetailsMap.keys());
		const randomKey = keys[Math.floor(Math.random() * keys.length)];
		return {
			id: randomId,
			kuerzel: "",
			details: fehlerDetailsMap.get(randomKey),
		};
	});




	const lehrerFehlerDetailsMap = new Map<number, { fehler: string; fehlertext: string; fehlerart: string }>([
		[20001, {
			fehler: "LSG1",
			fehlertext: "Unzulässige Eintragung im Feld Jahr (Geburtsdatum). Zulässig sind nur Werte 1938 bis 2000.",
			fehlerart: "Harter Fehler",
		}],
		[20002, {
			fehler: "LSP5",
			fehlertext: "Die Kombination aus Rechtsverhältnis und Beschäftigungsart ist unzulässig.",
			fehlerart: "Harter Fehler",
		}],
		[20003, {
			fehler: "LSP2",
			fehlertext: "Das Pflichtstundensoll kann in dieser Schulform 25,5 nicht ohne Minderleistung überschreiten.",
			fehlerart: "Harter Fehler",
		}],
		[20004, {
			fehler: "LSP4",
			fehlertext: "Bitte Altersermäßigung prüfen. Lehrkraft steht eine Minderleistung nach Schlüssel 200 zu.",
			fehlerart: "Hinweis",
		}],
		[20005, {
			fehler: "LSA1",
			fehlertext: "Staatsangehörigkeit fehlt/fehlerhaft.",
			fehlerart: "Muss-Fehler",
		}],
	]);

	const kuerzelList = ["MUE", "SCH", "KRA", "WIL", "BRA", "HER", "STE", "KOE", "FRE", "BAU", "ZIM", "VOG", "FIS", "WAG", "KUN"];

	const lehrerFehler = Array.from({ length: 17 }, () => {
		const randomId = Math.floor(Math.random() * 9000) + 1000; // Zufällige 4-stellige Zahl
		const keys = Array.from(lehrerFehlerDetailsMap.keys());
		const randomKey = keys[Math.floor(Math.random() * keys.length)];
		const randomKuerzel = kuerzelList[Math.floor(Math.random() * kuerzelList.length)]; // Zufälliges realistisches Kürzel
		return {
			id: randomId,
			kuerzel: randomKuerzel,
			details: lehrerFehlerDetailsMap.get(randomKey),
		};
	});

	const unterrichtFehlerDetailsMap = new Map<number, { fehler: string; fehlertext: string; fehlerart: string }>([
		[30001, {
			fehler: "UVG1",
			fehlertext: "Die Kombination aus Kursart und Jahrgangsstufe der Lerngruppe ist unzulässig.",
			fehlerart: "Harter Fehler",
		}],
		[30002, {
			fehler: "UVG3",
			fehlertext: "Lerngruppe Klasse 09E hat nicht die Mindestgröße 13. Bitte prüfen.",
			fehlerart: "Muss-Fehler",
		}],
		[30003, {
			fehler: "UVA2",
			fehlertext: "Das Fach dieser Unterrichtseinheit ist nicht zulässig.",
			fehlerart: "Harter Fehler",
		}],
		[30004, {
			fehler: "UVU1",
			fehlertext: "Die Lerngruppe enthält keine Schüler, bitte prüfen.",
			fehlerart: "Muss-Fehler",
		}],
		[30005, {
			fehler: "UVG2",
			fehlertext: "Lehrkraft fehlt/fehlerhaft.",
			fehlerart: "Harter Fehler",
		}],
	]);

	const unterrichtFehler = Array.from({ length: 10 }, () => {
		const randomId = Math.floor(Math.random() * 9000) + 1000; // Zufällige 4-stellige Zahl
		const keys = Array.from(unterrichtFehlerDetailsMap.keys());
		const randomKey = keys[Math.floor(Math.random() * keys.length)];
		const randomKuerzel = kuerzelList[Math.floor(Math.random() * kuerzelList.length)]; // Zufälliges realistisches Kürzel
		return {
			id: randomId,
			kuerzel: randomKuerzel,
			details: unterrichtFehlerDetailsMap.get(randomKey),
		};
	});


	/**
	 * Modal-Fenster
	 */

	// Reaktive Variablen für das Modal
	const isModalOpen = ref(false);
	const username = ref('');
	const password = ref('');
	const successMessage = ref('');
	const isLoading = ref(false);
	const progress = ref(0);

	// Beispiel-Daten für die Tabelle (kann später aus Props oder API geladen werden)
	const schipsData = ref([
		{ jahrgang: '6', anzahlSchueler: 300, letzteAenderung: '25.10.2025', aenderungDurch: 'Frau Müller' },
		{ jahrgang: '7', anzahlSchueler: 280, letzteAenderung: '24.10.2025', aenderungDurch: 'Herr Schmidt' },
		{ jahrgang: '8', anzahlSchueler: 260, letzteAenderung: '23.10.2025', aenderungDurch: 'Frau Bauer' },
		{ jahrgang: '9', anzahlSchueler: 240, letzteAenderung: '22.10.2025', aenderungDurch: 'Herr Wagner' },
		{ jahrgang: '10', anzahlSchueler: 220, letzteAenderung: '21.10.2025', aenderungDurch: 'Frau Müller' },
		{ jahrgang: 'EF', anzahlSchueler: 200, letzteAenderung: '20.10.2025', aenderungDurch: 'Herr Schmidt' },
		{ jahrgang: 'Q1', anzahlSchueler: 180, letzteAenderung: '19.10.2025', aenderungDurch: 'Frau Bauer' },
		{ jahrgang: 'Q2', anzahlSchueler: 145, letzteAenderung: '18.10.2025', aenderungDurch: 'Herr Wagner' },
	]);

	// Spalten für die Tabelle
	const schipsColumns = [
		{ key: 'jahrgang', label: 'Jahrgang', span: 1 },
		{ key: 'anzahlSchueler', label: 'Anzahl der Schüler', span: 1 },
		{ key: 'letzteAenderung', label: 'Letzte Änderung', span: 1 },
		{ key: 'aenderungDurch', label: 'Änderung durch', span: 1 },
	];

	// Funktion zum Öffnen des Modals (für den Button in der Kachel)
	function openModal() {
		isModalOpen.value = true;
	}

	// Berechnete Summe der Schüler
	const totalSchueler = computed(() => {
		return schipsData.value.reduce((sum, item) => sum + item.anzahlSchueler, 0);
	});

	// Funktion zum Senden der Daten
	async function sendData() {
		isLoading.value = true;
		progress.value = 0;

		// Simuliere Fortschritt
		const interval = setInterval(() => {
			progress.value += 10;
			if (progress.value >= 100) {
				clearInterval(interval);
				isLoading.value = false;
				successMessage.value = 'Erfolgreich gespeichert!';
				setTimeout(() => {
					isModalOpen.value = false;
					successMessage.value = '';
					username.value = '';
					password.value = '';
					progress.value = 0;
				}, 2000);
			}
		}, 500);

		// hier könntest du eine API-Anfrage an das Bildungsportal machen
		// Beispiel: await api.sendSchipsData({ username: username.value, password: password.value, data: schipsData.value });
	}





</script>

<style scoped>

	.svws-ui-tr {
		grid-template-columns: minmax(5rem, 5rem) minmax(4rem, 0.25fr) minmax(4rem, 0.25fr) minmax(4rem, 2fr) minmax(4rem, 4rem);
	}

	/* Spezifisches Grid-Layout für die Summentabelle */
	.page--statistik .svws-ui-table:has([v-if*="Summen"]) .svws-ui-tr {
		display: grid !important;
		grid-template-columns: 300px 120px 120px 120px !important;
	}

	/* Überschriften für Summentabelle zentrieren */
	.page--statistik .svws-ui-table:has([v-if*="Summen"]) .svws-ui-th {
		text-align: center;
	}

	.page--statistik .svws-ui-table:has([v-if*="Summen"]) .svws-ui-th:first-child {
		text-align: left;
	}

</style>

