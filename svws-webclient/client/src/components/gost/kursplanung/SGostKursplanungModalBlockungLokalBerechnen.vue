<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="big" class="hidden">
		<template #modalTitle>Blockung lokal berechnen</template>
		<template #modalDescription>
			<div class="text-left pb-4">
				Zum Start auf „Berechnung starten“ klicken, sobald die Bedingungen erfüllt sind, mit denen die Berechnung durchgeführt wird,
				wird die Berechnung abgebrochen. Alternativ kann die Berechnung durch das Anklicken des „Berechnung beenden“ Knopfes beendet werden.
				Die Ergebnisse gehen dabei nicht verloren und können anschließend auf Wunsch in die Datenbank übernommen werden.
			</div>
			<svws-ui-table clickable v-model="selected" :selectable="liste.size() > 0 && !running" class="z-20 relative"
				:columns="[{ key: 'bewertung', label: 'Ergebnis' }]" :items="liste" :count="!liste.isEmpty()">
				<template #header(bewertung)>
					<svws-ui-tooltip indicator="help">
						<span class="my-0.5">Ergebnis</span>
						<template #content>
							<div class="normal-case text-base rich-text">
								<div class="my-1">Bewertung durch:</div>
								<ul class="mb-1 list-disc pl-4">
									<li>Regelverletzungen</li>
									<li>Wahlkonflikte</li>
									<li>max. Kursdifferenz</li>
									<li>Fächerparallelität</li>
								</ul>
							</div>
						</template>
					</svws-ui-tooltip>
				</template>
				<template #cell(bewertung)="{ rowData: row }">
					<div class="inline-flex flex-wrap w-full gap-x-1 gap-y-2.5">
						<span class="flex gap-1 items-center ml-0.5">
							<span class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :title="`${getBewertungWert(row, 1)} Regelverletzungen`"
								:style="{'background-color': getBewertungColor(row, 1)}">{{ getBewertungWert(row, 1) }}</span>
							<span class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :title="`${getBewertungWert(row, 2)} Wahlkonflikte`"
								:style="{'background-color': getBewertungColor(row, 2)}">{{ getBewertungWert(row, 2) }}</span>
							<span class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :title="`Maximale Kursdifferenz: ${getBewertungWert(row, 3)}`"
								:style="{'background-color': getBewertungColor(row, 3)}">{{ getBewertungWert(row, 3) }}</span>
							<span class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :title="`${getBewertungWert(row, 4)} Fächer parallel`"
								:style="{'background-color': getBewertungColor(row, 4)}">{{ getBewertungWert(row, 4) }}</span>
						</span>
					</div>
				</template>
			</svws-ui-table>
			<div class="flex flex-row gap-2 pt-4">
				<svws-ui-button v-if="!running" type="primary" @click="berechne">{{ liste.isEmpty() ? 'Berechnung starten' : 'Berechnung fortsetzen' }}</svws-ui-button>
				<svws-ui-button v-else type="primary" @click="pause">Berechnung pausieren</svws-ui-button>
				<svws-ui-button v-if="selected.length > 0" @click="ergebnisseUebernehmen" type="secondary" :disabled="selected.length === 0">
					<i-ri-download-2-line />
					<span>{{ selected.length }} {{ selected.length !== 1 ? 'Ergebnisse' : 'Ergebnis' }} importieren</span>
				</svws-ui-button>
				<svws-ui-button type="danger" @click="showModal().value = false">Abbrechen</svws-ui-button>
			</div>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref, shallowRef, watch } from 'vue';
	import { ArrayList, type GostBlockungsergebnis, type GostBlockungsdatenManager, GostBlockungsergebnisManager } from "@core";
	import { WorkerManagerKursblockung } from './WorkerManagerKursblockung';

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const workerManager = shallowRef<WorkerManagerKursblockung | undefined>(undefined);

	watch(_showModal, neu => {
		if (workerManager.value !== undefined) {
			workerManager.value.terminate();
			workerManager.value = undefined;
		}
		if (neu === true) {
			workerManager.value = new WorkerManagerKursblockung();
			workerManager.value.init(props.getDatenmanager().faecherManager().faecher(), props.getDatenmanager().daten());
		}
	});

	const selected = ref<GostBlockungsergebnis[]>([]);

	const running = computed<boolean>(() => workerManager.value?.isRunning() ?? false);

	const liste = computed(() => workerManager.value?.getErgebnisse() ?? new ArrayList<GostBlockungsergebnis>());

	async function berechne() {
		if (workerManager.value !== undefined) {
			workerManager.value.start(100);
		}
	}

	async function pause() {
		workerManager.value?.pause();
		selected.value = [...liste.value];
	}

	async function ergebnisseUebernehmen() {
		// TODO patch-Methode aufrufen
	}

	function getBewertungWert(ergebnis: GostBlockungsergebnis, value: number) : number {
		switch(value) {
			case 1: return GostBlockungsergebnisManager.getOfBewertung1WertStatic(ergebnis.bewertung);
			case 2: return GostBlockungsergebnisManager.getOfBewertung2WertStatic(ergebnis.bewertung);
			case 3: return GostBlockungsergebnisManager.getOfBewertung3WertStatic(ergebnis.bewertung);
			case 4: return GostBlockungsergebnisManager.getOfBewertung4WertStatic(ergebnis.bewertung);
		}
		return Number.MAX_SAFE_INTEGER;
	}

	function getBewertungColor(ergebnis: GostBlockungsergebnis, value: number) : string {
		const h = Math.round((1 - (getBewertungCode(ergebnis, value)||0)) * 120);
		return `hsl(${h},100%,75%)`;
	}

	function getBewertungCode(ergebnis: GostBlockungsergebnis, value: number) : number {
		switch(value) {
			case 1: return GostBlockungsergebnisManager.getOfBewertung1FarbcodeStatic(ergebnis.bewertung);
			case 2: return GostBlockungsergebnisManager.getOfBewertung2FarbcodeStatic(ergebnis.bewertung);
			case 3: return GostBlockungsergebnisManager.getOfBewertung3FarbcodeStatic(ergebnis.bewertung);
			case 4: return GostBlockungsergebnisManager.getOfBewertung4FarbcodeStatic(ergebnis.bewertung);
		}
		return 1;
	}

	const openModal = () => {
		showModal().value = true;
	}

</script>
