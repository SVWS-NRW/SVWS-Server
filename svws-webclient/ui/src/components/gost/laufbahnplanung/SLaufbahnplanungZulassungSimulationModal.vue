<template>
	<svws-ui-modal :show @update:show="value => emit('update:show', value)" size="fit" :auto-close="false">
		<template #modalTitle>Simulation der Abiturzulassung – Noteneingaben werden nicht übernommen</template>
		<template #modalContent>
			<div class="flex flex-col gap-4 text-left">
				<!-- Erzeuge eine Leiste, die wie die Sub-Nav-Bar unter einer Tabbar aussieht. -->
				<div class="svws-ui-sub-nav">
					<div class="svws-ui-sub-nav--wrapper">
						<div class="router-tab-bar--subnav">
							<svws-ui-button type="transparent" @click="toggleFixiere">{{ fixiereNoten ? "Noteneingabe fixiert" : "Noteneingabe frei" }}</svws-ui-button>
							<svws-ui-button type="transparent" :disabled="!hatUpdateKompetenz" @click="save">Sichern</svws-ui-button>
							<svws-ui-button type="transparent" @click="load">Wiederherstellen</svws-ui-button>
							<svws-ui-button type="transparent" @click="reset">Leeren</svws-ui-button>
							<svws-ui-button type="transparent" @click="doExport"><span class="icon-sm i-ri-upload-2-line" />Exportieren</svws-ui-button>
							<svws-ui-button type="transparent" @click="importFileSelection?.click()"><span class="icon-sm i-ri-download-2-line" />Importieren…</svws-ui-button>
							<input ref="dateiauswahl" class="hidden" type="file" accept=".json" @change="doImport">
						</div>
					</div>
				</div>

				<!-- Fehlermeldung(-en) -->
				<div v-if="importFailed" class="text-ui-danger font-bold">Die Datei enthält keinen gültigen Stand der Simulation.</div>

				<!-- Das Modal ist in zwei Spalten geteilt (links die Noteneingabe und rechts die Zulassungsberechnung - ohne Eingabe) -->
				<div class="flex flex-row flex-wrap gap-4 items-start">
					<div class="flex flex-col gap-8 min-w-fit">
						<div class="flex flex-col gap-4">
							<div class="text-headline-md">Noteneingabe Block I</div>
							<s-laufbahnplanung-zulassung-simulation-noten :eingabe="() => ergebnis.eingabe" :faecher="() => faecher" :ist-gesperrt :set-note="doSetNote" />
						</div>
						<div class="flex flex-col gap-4">
							<div class="text-headline-md">Noteneingabe Block II</div>
							<s-laufbahnplanung-zulassung-simulation-pruefung :eingabe="() => ergebnis.eingabe" :ergebnis="() => ergebnis.manager.daten()" :faecher="() => faecher" :set-pruefung
								:set-besondere-lernleistung :set-besondere-lernleistung-notenpunkte />
						</div>
					</div>
					<div class="flex flex-col gap-4 min-w-fit">
						<div class="text-headline-md">Zulassungsberechnung</div>
						<schueler-abitur-zulassung-tabelle :manager="() => ergebnis.manager" :update-abiturpruefungsdaten="null" />
						<div v-if="logZulassung.size() > 0" class="flex flex-col max-h-64 overflow-y-auto">
							<div class="font-bold">Hinweise:</div>
							<div v-for="(eintrag, index) in logZulassung" :key="index" class="whitespace-pre-wrap">{{ eintrag }}</div>
						</div>
					</div>
				</div>
			</div>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref, shallowRef, triggerRef, useTemplateRef } from "vue";

	import { Note } from "@core/asd/types/Note";
	import type { AbiturdatenManager } from "@core/core/abschluss/gost/AbiturdatenManager";
	import { Abiturdaten } from "@core/core/data/gost/Abiturdaten";
	import { GostZulassungSimulation } from "@core/core/data/gost/GostZulassungSimulation";
	import { GostZulassungSimulationAbiturfach } from "@core/core/data/gost/GostZulassungSimulationAbiturfach";
	import { GostZulassungSimulationNote } from "@core/core/data/gost/GostZulassungSimulationNote";
	import type { GostHalbjahr } from "@core/core/types/gost/GostHalbjahr";
	import { GostZulassungSimulationUtils } from "@core/core/utils/gost/GostZulassungSimulationUtils";
	import { ArrayList } from "@core/java/util/ArrayList";
	import type { List } from "@core/java/util/List";
	import { useGostLaufbahnplanungState } from "@ui/states/GostLaufbahnplanungState";

	defineProps<{
		show: boolean;
		hatUpdateKompetenz: boolean;
	}>();

	const emit = defineEmits<{
		(e: 'update:show', value: boolean): void;
	}>();

	/**
	 * Der Typ für ein Simulationsergebnis mit den Abiturdaten, welche die Noteneingaben beinhalten und dem damit erzeugten
	 * Abiturdaten-Manager für die Zulassungsberechnung
	 */
	type ZulassungSimulationErgebnis = {
		eingabe: Abiturdaten;
		manager: AbiturdatenManager;
	};

	// Gibt an, ob die in den Abiturdaten enthaltenen Noten erhalten bleiben sollen und nicht in der Simulation angepasst werden dürfen.
	const fixiereNoten = ref<boolean>(true);

	// Der Status für den Import von Notendaten für die Simulation (Element für die Import-Datei und Import-Fehler-Status)
	const importFileSelection = useTemplateRef<HTMLInputElement>("dateiauswahl");
	const importFailed = ref<boolean>(false);

	// Die Eingabe von der linken Seite des Modals
	const simulation = shallowRef<GostZulassungSimulation>(new GostZulassungSimulation());

	// Bestimme den Zustand beim Öffnen des Modals. Dieser wird in dem Modal nicht verändert, aber als Grundlage genutzt
	const gostLaufbahnplanungState = useGostLaufbahnplanungState();
	const managerOriginal = gostLaufbahnplanungState.abiturdatenManager;
	const original = klonen(managerOriginal.daten());
	const faecher = managerOriginal.faecher();
	const jahrgangsdaten = managerOriginal.jahrgangsdaten();

	// Das Simulationsergebnis nach der Berechnung - initialisiert mit dem Zustand beim Öffnen des Modals
	const ergebnis = shallowRef<ZulassungSimulationErgebnis>(doSimulate());


	/**
	 * Dupliziert die Abiturdaten, damit die Original-Abiturdaten bei der Simulation nicht verändert werden.
	 *
	 * @param daten die Original-Abiturdaten
	 *
	 * @returns die Kopie der Abiturdaten
	 */
	function klonen(daten: Abiturdaten): Abiturdaten {
		return Abiturdaten.transpilerFromJSON(Abiturdaten.transpilerToJSON(daten));
	}

	/**
	 * Berechnet die Ergebnisse der Zulassungs-Simulation neu. Dabei wird auch das Abiturergebnis berechnet, sofern es vorliegt.
	 *
	 * @param refreshPflichtpruefungen   gibt an, ob im Abiturbereich die Pflichtprüfungen neu ermittelt werden müssen oder nicht
	 *
	 * @returns die Abiturdaten mit den Eingaben und der Abiturdaten-Manager mit dem Ergebnis der Berechnung
	 */
	function doSimulate(refreshPflichtpruefungen = false): ZulassungSimulationErgebnis {
		const eingabe = klonen(original);
		GostZulassungSimulationUtils.combine(eingabe, simulation.value, faecher, fixiereNoten.value);
		const manager = GostZulassungSimulationUtils.berechne(klonen(eingabe), jahrgangsdaten, faecher, refreshPflichtpruefungen);
		return { eingabe, manager };
	}

	/**
	 * Erstellt den Log der Zulassungsberechnung und gibt diesen im Fehlerfall aus.
	 */
	const logZulassung = computed<List<string>>(() => {
		const algorithmus = ergebnis.value.manager.getErgebnisMarkierungsalgorithmus();
		if (!algorithmus.erfolgreich) {
			return algorithmus.log;
		}
		const pruefung = ergebnis.value.manager.getErgebnisMarkierpruefung();
		return pruefung.erfolgreich ? new ArrayList<string>() : pruefung.log;
	});

	/**
	 * Aktualisiert das Ergebnis aufgrund von Änderungen in der Eingabe. Triggert auch die Referenz für das
	 * Eingabe-Objekt der linken Seite, damit alle davon abhängigen Sachen neu berechnet werden.
	 *
	 * @param refreshPflichtpruefungen   gibt an, ob auch Pflichtprüfungen im Bereich des Abiturs aktualisiert werden sollen
	 */
	function update(refreshPflichtpruefungen = false) {
		if (refreshPflichtpruefungen) {
			const daten = doSimulate(true).manager.daten();
			for (const fach of daten.fachbelegungen) {
				if (fach.abiturFach !== null) {
					const eintrag = getAbiturfachEintrag(fach.fachID);
					eintrag.muendlichBestehen = fach.block2MuendlichePruefungBestehen;
					eintrag.muendlichFreiwillig = fach.block2MuendlichePruefungFreiwillig;
					eintrag.muendlichReihenfolge = fach.block2MuendlichePruefungReihenfolge;
					eintrag.muendlichNotenpunkte = Note.fromKuerzel(fach.block2MuendlichePruefungNotenKuerzel).daten(daten.schuljahrAbitur)?.notenpunkte ?? null;
				}
			}
		}
		ergebnis.value = doSimulate();
		triggerRef(simulation);
	}


	/**
	 * Eine Methode, welche in der linken Seite bei der Eingabe genutzt wird und prüft, ob die Eingabe bei dem
	 * übergebenen Fach in dem übergebenen Halbjahr gesperrt ist oder nicht.
	 *
	 * Diese Prüfung findet auf den Originaldaten statt, damit die Zellen auch nach Eingabe einer Note veränderbar bleiben.
	 *
	 * @param idFach     die ID des Faches
	 * @param halbjahr   das zu prüfende Halbjahr
	 */
	function istGesperrt(idFach: number, halbjahr: GostHalbjahr): boolean {
		for (const fach of original.fachbelegungen) {
			if (fach.fachID === idFach) {
				const belegung = fach.belegungen[halbjahr.id];
				return (belegung !== null) && GostZulassungSimulationUtils.istEingabeGesperrt(faecher, idFach, belegung, original.schuljahrAbitur, fixiereNoten.value);
			}
		}
		return false;
	}


	/**
	 * Wechselt die Einstellung, ob die Noten, welche in den Originaldaten vorhanden sind, fixiert werden oder auch veränderbar sind.
	 */
	function toggleFixiere() {
		fixiereNoten.value = !fixiereNoten.value;
		update();
	}


	/**
	 * Setzt die Note für das übergebene Fach und das übergebene Halbjahr.
	 *
	 * @param idFach        die ID des Faches
	 * @param halbjahr      das Halbjahr
	 * @param notenpunkte   die Notenpunkte, die gesetzt werden sollen, oder null, falls die Note geleert werden soll
	 */
	function doSetNote(idFach: number, halbjahr: GostHalbjahr, notenpunkte: number | null) {
		let eintrag = simulation.value.noten.toArray(new Array<GostZulassungSimulationNote>()).find(n => (n.idFach === idFach) && (n.halbjahr === halbjahr.id));
		if (eintrag === undefined) {
			eintrag = new GostZulassungSimulationNote();
			eintrag.idFach = idFach;
			eintrag.halbjahr = halbjahr.id;
			simulation.value.noten.add(eintrag);
		}
		eintrag.notenpunkte = notenpunkte;
		update();
	}

	/**
	 * Ermittelt den Abiturfach-Eintrag für das übergebene Fach aus der aktuellen Simulationseingabe und legt ihn bei Bedarf an
	 *
	 * @param idFach   die ID des Faches
	 *
	 * @returns der Abiturfach-Eintrag
	 */
	function getAbiturfachEintrag(idFach: number): GostZulassungSimulationAbiturfach {
		let abiturfach = null;
		for (const f of simulation.value.abiturfaecher) {
			if (f.idFach === idFach) {
				abiturfach = f;
				break;
			}
		}
		if (abiturfach === null) {
			abiturfach = new GostZulassungSimulationAbiturfach();
			abiturfach.idFach = idFach;
			simulation.value.abiturfaecher.add(abiturfach);
		}
		return abiturfach;
	}

	/**
	 * Setzt die übergebenen Eingabewerte für die Abiturergebnisse in dem angegebenen Fach.
	 *
	 * @param idFach                     die ID des Faches
	 * @param patch                      die anzupassenden Eingabewerte
	 * @param refreshPflichtpruefungen   gibt an, ob auch die Informationen zu den Pflichtprüfungen aktualisiert werden sollen
	 */
	function setPruefung(idFach: number, patch: Partial<GostZulassungSimulationAbiturfach>, refreshPflichtpruefungen: boolean) {
		Object.assign(getAbiturfachEintrag(idFach), patch);
		update(refreshPflichtpruefungen);
	}

	/**
	 * Setzt die Art der Besonderen Lernleistung und aktualisiert das Simulationsergebnis
	 *
	 * @param art   die Art der Besonderen Lernleistung.
	 */
	function setBesondereLernleistung(art: string) {
		simulation.value.besondereLernleistung = art;
		update(true);
	}

	/**
	 * Setzt die Notenpunkte für eine Besondere Lernleistung und aktualisiert das Simulationsergebnis
	 *
	 * @param notenpunkte   die Notenpunkte oder null
	 */
	function setBesondereLernleistungNotenpunkte(notenpunkte: number | null) {
		simulation.value.besondereLernleistungNotenpunkte = notenpunkte;
		update(true);
	}


	/**
	 * Setzt die Eingabe zurück, so dass das Simulationsergebnis wieder ausschließlich auf den Originaldaten beruht.
	 */
	function reset() {
		simulation.value = new GostZulassungSimulation();
		fixiereNoten.value = true;
		importFailed.value = false;
		update();
	}

	/**
	 * Stellt die Eingaben aus der Sicherung im SVWS-Server wieder her und aktualisiert das Simulationsergebnis
	 */
	async function load() {
		simulation.value = await gostLaufbahnplanungState.getZulassungSimulation();
		importFailed.value = false;
		update();
	}

	/**
	 * Speichert die Eingaben in der Sicherung des SVWS-Servers. Eine ggf. vorhandene Sicherung wird dabei
	 * überschrieben.
	 */
	async function save() {
		await gostLaufbahnplanungState.setZulassungSimulation(simulation.value);
	}

	/**
	 * Exportiert die Eingaben für die Simulation in eine json-Datei.
	 */
	function doExport() {
		const link = document.createElement("a");
		link.href = URL.createObjectURL(new Blob([GostZulassungSimulation.transpilerToJSON(simulation.value)], { type: "application/json" }));
		link.download = "ZulassungSimulation_" + original.schuelerID + "_" + original.abiturjahr + ".json";
		link.click();
		URL.revokeObjectURL(link.href);
	}

	/**
	 * Importiert die Eingaben aus einer json-Datei
	 *
	 * @param event   das Ereignis, welches von dem Datei-Input ausgelöst wurde
	 */
	async function doImport(event: Event) {
		const eingabe = event.target as HTMLInputElement;
		const datei = eingabe.files?.[0] ?? null;
		if (datei === null) {
			return;
		}
		try {
			simulation.value = GostZulassungSimulation.transpilerFromJSON(await datei.text());
			importFailed.value = false;
			update();
		} catch {
			importFailed.value = true;
		}
		// Sonst würde die erneute Auswahl derselben Datei kein Ereignis auslösen
		eingabe.value = "";
	}

</script>
