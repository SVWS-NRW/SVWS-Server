<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="big" class="hidden" :auto-close="false" :close-in-title="false">
		<template #modalTitle>Ausführliche Berechnung lokal im Browser</template>
		<template #hilfe>
			Zum Start auf „Berechnung starten“ klicken. Sobald die Bedingungen erfüllt sind,
			<br>mit denen die Berechnung durchgeführt wird, wird die Berechnung abgebrochen.
			<br>Alternativ kann die Berechnung durch das Anklicken des Berechnung pausieren“ Knopfes unterbrochen werden.
			<br>Die Ergebnisse können anschließend auf Wunsch in die Datenbank importiert werden.
			<br>Der „Abbrechen“ Knopf beendet und löscht alle Berechnungen.
		</template>
		<template #modalDescription>
			<div v-if="workerManager !== undefined" class="text-left pb-4 flex flex-row">
				Anzahl der parallelen Berechnungen:
				<div class="pl-4 pr-2">
					<svws-ui-button type="secondary" size="small" title="" @click="removeWorker" :disabled="workerManager.threads === 1">
						<span class="icon i-ri-subtract-line py-3" />
					</svws-ui-button>
				</div>
				<span class="py-1">{{ workerManager.threads }}</span>
				<div class="pl-2">
					<svws-ui-button type="secondary" size="small" title="" @click="addWorker" :disabled="workerManager.threads === WorkerManagerKursblockung.MAX_WORKER">
						<span class="icon i-ri-add-line py-3" />
					</svws-ui-button>
				</div>
				<div class="pl-4"><svws-ui-button type="secondary" size="small" title="Maximum" @click="setWorkerMaximum" :disabled="workerManager.threads === WorkerManagerKursblockung.MAX_WORKER"> Maximum </svws-ui-button></div>
			</div>
			<div class="text-left pb-4 flex flex-row">
				<svws-ui-checkbox :model-value="ausfuehrlicheDarstellungKursdifferenz()" @update:model-value="setAusfuehrlicheDarstellungKursdifferenz">Ausführliche Darstellung für Kursdifferenz verwenden</svws-ui-checkbox>
			</div>
			<svws-ui-table clickable v-model="selected" :selectable="items.size() > 0 && !running" class="z-20 relative" :columns :items :count="!items.isEmpty()">
				<template #cell(wert1)="{ rowIndex }">
					<div class="table-cell">
						<svws-ui-tooltip v-if="listErgebnismanager.get(rowIndex).getOfBewertung1Wert() > 0" autosize>
							<span class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :style="{'background-color': getBewertungColor(listErgebnismanager.get(rowIndex).getOfBewertung1Farbcode())}">{{ listErgebnismanager.get(rowIndex).getOfBewertung1Wert() }}</span>
							<template #content>
								<pre>{{ listErgebnismanager.get(rowIndex).regelGetTooltipFuerRegelverletzungen() }}</pre>
							</template>
						</svws-ui-tooltip>
						<span v-else class="svws-ui-badge min-w-[2.75rem] text-center justify-center" style="background-color: rgb(128, 255, 128)">0</span>
					</div>
				</template>
				<template #cell(wert2)="{ rowIndex }">
					<div class="table-cell">
						<svws-ui-tooltip v-if="listErgebnismanager.get(rowIndex).getOfBewertung2Wert() > 0" autosize>
							<span class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :style="{'background-color': getBewertungColor(listErgebnismanager.get(rowIndex).getOfBewertung2Farbcode())}">{{ listErgebnismanager.get(rowIndex).getOfBewertung2Wert() }}</span>
							<template #content>
								<pre>{{ listErgebnismanager.get(rowIndex).regelGetTooltipFuerWahlkonflikte() }}</pre>
							</template>
						</svws-ui-tooltip>
						<span v-else class="svws-ui-badge min-w-[2.75rem] text-center justify-center" style="background-color: rgb(128, 255, 128)">0</span>
					</div>
				</template>
				<template #cell(wert3)="{ rowIndex }">
					<div class="table-cell">
						<svws-ui-tooltip autosize>
							<template class="svws-ui-badge min-w-[2.75rem] px-2 text-center " :class="ausfuehrlicheDarstellungKursdifferenz() ? ['justify-between flex gap-1']:['justify-center']" :style="{'background-color': getBewertungColor(listErgebnismanager.get(rowIndex).getOfBewertung3Farbcode())}">
								<template v-if="ausfuehrlicheDarstellungKursdifferenz()">
									<span class="svws-ui-badge min-w-12 text-center justify-center" :style="{'background-color': getBewertungColor(listErgebnismanager.get(rowIndex).getOfBewertung3Farbcode_nur_LK())}"> {{ listErgebnismanager.get(rowIndex).getOfBewertung3Wert_nur_LK() }} </span>
									<span class="svws-ui-badge min-w-12 text-center justify-center" :style="{'background-color': getBewertungColor(listErgebnismanager.get(rowIndex).getOfBewertung3Farbcode_nur_GK())}"> {{ listErgebnismanager.get(rowIndex).getOfBewertung3Wert_nur_GK() }} </span>
									<span class="svws-ui-badge min-w-12 text-center justify-center" :style="{'background-color': getBewertungColor(listErgebnismanager.get(rowIndex).getOfBewertung3Farbcode_nur_REST())}"> {{ listErgebnismanager.get(rowIndex).getOfBewertung3Wert_nur_REST() }} </span>
								</template>
								<span v-else>{{ listErgebnismanager.get(rowIndex).getOfBewertung3Wert() }}</span>
							</template>
							<template #content>
								<pre>{{ listErgebnismanager.get(rowIndex).regelGetTooltipFuerKursdifferenzen() }}</pre>
							</template>
						</svws-ui-tooltip>
					</div>
				</template>
				<template #cell(wert4)="{ rowIndex }">
					<div class="table-cell">
						<svws-ui-tooltip v-if="listErgebnismanager.get(rowIndex).getOfBewertung4Wert() > 0" autosize>
							<span class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :style="{'background-color': getBewertungColor(listErgebnismanager.get(rowIndex).getOfBewertung4Farbcode())}">{{ listErgebnismanager.get(rowIndex).getOfBewertung4Wert() }}</span>
							<template #content>
								<pre>{{ listErgebnismanager.get(rowIndex).regelGetTooltipFuerFaecherparallelitaet() }}</pre>
							</template>
						</svws-ui-tooltip>
						<span v-else class="svws-ui-badge min-w-[2.75rem] text-center justify-center" style="background-color: rgb(128, 255, 128)">0</span>
					</div>
				</template>
			</svws-ui-table>
			<div class="flex flex-row gap-2 pt-4">
				<template v-if="workerManager !== undefined">
					<svws-ui-button v-if="!running" type="primary" @click="berechne">{{ (workerManager.isInitialized() === false) ? 'Berechnung starten' : 'Berechnung fortsetzen' }}</svws-ui-button>
					<svws-ui-button v-else type="primary" @click="pause"><svws-ui-spinner spinning />&nbsp;Berechnung pausieren</svws-ui-button>
					<svws-ui-button v-if="selected.length > 0" @click="ergebnisseUebernehmen" type="secondary" :disabled="selected.length === 0">
						<span class="icon i-ri-download-2-line" />
						<span>{{ selected.length }} {{ selected.length !== 1 ? 'Ergebnisse' : 'Ergebnis' }} importieren und beenden</span>
					</svws-ui-button>
					<svws-ui-button v-if="!nachfragen" type="danger" @click="items.size() > 0 ? nachfragen = true : closeModal()">Abbrechen</svws-ui-button>
					<svws-ui-button v-else type="danger" @click="closeModal">Alle berechneten Ergebnisse verwerfen und schließen</svws-ui-button>
				</template>
				<template v-else>
					<div class="flex gap-2 w-full">
						<svws-ui-button v-if="!nachfragen" type="danger" @click="items.size() > 0 ? nachfragen = true : closeModal()">Abbrechen</svws-ui-button>
						<div>Der Worker zum Berechnen der Blockungen konnte nicht erstellt werden, bitte Fehlermeldungen überprüfen.</div>
					</div>
				</template>
			</div>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref, shallowRef, watch } from 'vue';
	import type { GostBlockungsdatenManager, GostBlockungsergebnisManager, GostBlockungsergebnis, List } from "@core";
	import { ArrayList } from "@core";
	import { WorkerManagerKursblockung } from './WorkerManagerKursblockung';

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		addErgebnisse: (ergebnisse: List<GostBlockungsergebnis>) => Promise<void>;
		ausfuehrlicheDarstellungKursdifferenz: () => boolean;
		setAusfuehrlicheDarstellungKursdifferenz: (value: boolean) => void;
	}>();

	const columns = [
		{ key: 'wert1', label: 'Regelverletzungen' },
		{ key: 'wert2', label: 'Wahlkonflikte' },
		{ key: 'wert3', label: 'Maximale Kursdifferenz' },
		{ key: 'wert4', label: 'Fächer parallel' },
	];

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const workerManager = shallowRef<WorkerManagerKursblockung | undefined>(undefined);

	watch(_showModal, neu => {
		if (workerManager.value !== undefined) {
			workerManager.value.terminate();
			workerManager.value = undefined;
			selected.value = [];
		}
		if (neu === true)
			workerManager.value = new WorkerManagerKursblockung(props.getDatenmanager().faecherManager().faecher(), props.getDatenmanager().daten());
	});

	const selected = ref<GostBlockungsergebnis[]>([]);

	const running = computed<boolean>(() => workerManager.value?.isRunning() ?? false);

	const items = computed<List<GostBlockungsergebnis>>(() => workerManager.value?.getErgebnisse() ?? new ArrayList<GostBlockungsergebnis>());
	const listErgebnismanager = computed<List<GostBlockungsergebnisManager>>(() => workerManager.value?.getErgebnisManager() ?? new ArrayList<GostBlockungsergebnisManager>());

	const nachfragen = ref(false);

	async function getWaiting() {
		const p = workerManager.value?.waiting;
		if (p !== undefined)
			await Promise.allSettled(p);
	}

	function addWorker() {
		if ((workerManager.value === undefined) || (workerManager.value.threads === WorkerManagerKursblockung.MAX_WORKER))
			return;
		workerManager.value.threads = workerManager.value.threads + 1;
	}

	function removeWorker() {
		if ((workerManager.value === undefined) || (workerManager.value.threads === 1))
			return;
		workerManager.value.threads = workerManager.value.threads - 1;
	}

	function setWorkerMaximum() {
		if (workerManager.value === undefined)
			return;
		workerManager.value.threads = WorkerManagerKursblockung.MAX_WORKER;
	}

	async function berechne() {
		if (workerManager.value !== undefined) {
			if (!workerManager.value.isInitialized())
				workerManager.value.init();
			workerManager.value.interval = 100;
			workerManager.value.start();
		}
	}

	async function pause() {
		workerManager.value?.pause();
		await getWaiting();
		selected.value = [...items.value];
	}

	async function ergebnisseUebernehmen() {
		if (selected.value.length === 0)
			return;
		const ergebnisse = new ArrayList<GostBlockungsergebnis>();
		for (const ergebnis of selected.value)
			ergebnisse.add(ergebnis);
		await props.addErgebnisse(ergebnisse);
		closeModal();
	}

	function getBewertungColor(farbcode: number) : string {
		const h = Math.round((1 - (farbcode || 0)) * 120);
		return `hsl(${h},100%,75%)`;
	}

	const openModal = () => showModal().value = true;
	const closeModal = () => (showModal().value = false) && (nachfragen.value = false);

</script>
