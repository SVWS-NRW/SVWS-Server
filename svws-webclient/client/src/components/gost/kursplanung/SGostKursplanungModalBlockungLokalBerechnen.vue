<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="big" class="hidden">
		<template #modalTitle>Blockung lokal berechnen</template>
		<template #modalDescription>
			Zum Start auf „Berechnung starten“ klicken, sobald die Bedingungen erfüllt sind, mit denen die Berechnung durchgeführt wird,
			wird die Berechnung abgebrochen. Alternativ kann die Berechnung durch das Anklicken des „Berechnung beenden“ Knopfes beendet werden.
			Die Ergebnisse gehen dabei nicht verloren und können anschließend auf Wunsch in die Datenbank übernommen werden.
			<div class="flex flex-row gap-2">
				<svws-ui-button v-if="!running" type="secondary" @click="showModal().value = false">Abbrechen</svws-ui-button>
				<svws-ui-button v-if="!running" type="primary" @click="berechne">{{ running === undefined ? 'Berechnung starten' : 'Berechnung fortsetzen' }}</svws-ui-button>
				<svws-ui-button v-else type="primary" @click="beenden">Berechnung unterbrechen</svws-ui-button>
			</div>
			<svws-ui-table clickable v-model="selected" :selectable="liste.length > 0 && !running" class="z-20 relative"
				:columns="[{ key: 'bewertung', label: 'Ergebnis' }]" :items="liste" :count="liste.length > 1">
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
							<span class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :title="`${row.wert1} Regelverletzungen`" :style="{'background-color': row.color1}">{{ row.wert1 }}</span>
							<span class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :title="`${row.wert2} Wahlkonflikte`" :style="{'background-color': row.color2}">{{ row.wert2 }}</span>
							<span class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :title="`Maximale Kursdifferenz: ${row.wert3}`" :style="{'background-color': row.color3}">{{ row.wert3 }}</span>
							<span class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :title="`${row.wert4} Fächer parallel`" :style="{'background-color': row.color4}">{{ row.wert4 }}</span>
						</span>
					</div>
				</template>
				<template #actions v-if="selected.length">
					<svws-ui-button @click="undefined" type="transparent" :disabled="selected.length === 0">
						<i-ri-download-2-line />
						<span>{{ selected.length }} {{ selected.length > 1 ? 'Ergebnisse' : 'Ergebnis' }} importieren</span>
					</svws-ui-button>
				</template>
			</svws-ui-table>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	type Bewertung = {
		wert1: number;
		color1: string;
		wert2: number;
		color2: string;
		wert3: number;
		color3: string;
		wert4: number;
		color4: string;
	};

	import { computed, ref } from 'vue';
	import type { GostBlockungsdatenManager } from "@core";
	import type { ErgebnisWorker } from '~/router/apps/gost/kursplanung/ErgbnisWorker';

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		ergebnisWorker: ErgebnisWorker;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const selected = ref<Bewertung[]>([]);

	const liste = computed(()=> props.ergebnisWorker.liste.value);
	const running = computed(()=> props.ergebnisWorker.running.value)

	async function berechne() {
		props.ergebnisWorker.running.value = true;
		props.ergebnisWorker.next(100);
	}

	async function beenden() {
		props.ergebnisWorker.running.value = false;
		selected.value = liste.value;
	}

	const openModal = () => {
		showModal().value = true;
	}

</script>
