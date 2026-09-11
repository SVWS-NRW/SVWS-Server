<template>
	<div v-if="klasse !== undefined" class="page page-grid-cards">
		<svws-ui-content-card title="Daten der Klasse" class="col-span-full">
			<template #title>
				<div class="content-card--header"><h3 class="content-card--headline" title="Daten der Klasse">Daten der Klasse</h3>&nbsp;<svws-ui-badge type="light" title="ID" class="font-mono" size="big"> ID: {{ klasse.id }} </svws-ui-badge></div>
			</template>
			<div class="mb-3 flex flex-wrap items-center gap-2">Schülergruppe <svws-ui-button v-if="klasse.idSchuelergruppe !== null" type="icon" title="Schülergruppe öffnen" @click="gotoSchuelergruppe(klasse.idSchuelergruppe)"><span class="icon i-ri-link" /></svws-ui-button></div>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input :readonly="!hatKompetenzAendern" :disabled="loading" placeholder="Kürzel" :required="true" :max-len="15"
					:model-value="form.kuerzel"
					@change="onKuerzelChange" />
				<svws-ui-text-input :readonly="!hatKompetenzAendern" :disabled="loading" placeholder="Bezeichnung" :max-len="150"
					:model-value="form.bezeichnung"
					@change="bezeichnung => patch({ bezeichnung })" />
				<svws-ui-select :readonly="!hatKompetenzAendern" :disabled="loading" title="Parallelität"
					:model-value="klasse.parallelitaet || '---'"
					@update:model-value="(value: string | null | undefined) => patch({ parallelitaet: (!value || value === '---') ? '' : value })"
					:items="parallelitaetItems"
					:item-text="p => p" />
				<svws-ui-select :readonly="!hatKompetenzAendern" :disabled="loading" title="Stundentafel"
					:model-value="selectedStundentafel"
					@update:model-value="onStundentafelChange"
					:items="stundentafelItems"
					:item-text="st => st.bezeichnung" />
				<svws-ui-select :readonly="!hatKompetenzAendern" :disabled="loading" title="Schuljahresabschnitt"
					:model-value="selectedSchuljahresabschnitt"
					@update:model-value="onSchuljahresabschnittChange"
					:items="schuljahresabschnittItems"
					:item-text="sja => `${sja.schuljahr}/${sja.schuljahr + 1} - ${sja.abschnitt}. Abschnitt`" />
				<svws-ui-select :readonly="!hatKompetenzAendern" :disabled="loading" title="Schülergruppe"
					:model-value="selectedSchuelergruppe"
					@update:model-value="onSchuelergruppeChange"
					:items="schuelergruppeItems"
					:item-text="sg => sg.bezeichnung" />
				<svws-ui-select :readonly="!hatKompetenzAendern" :disabled="loading" title="Organisationsform"
					:model-value="selectedOrganisationsform"
					@update:model-value="onOrganisationsformChange"
					:items="organisationsformItems"
					:item-text="organisationsformText" />
				<svws-ui-select :readonly="!hatKompetenzAendern" :disabled="loading" title="Schulgliederung"
					:model-value="selectedSchulgliederung"
					@update:model-value="onSchulgliederungChange"
					:items="schulgliederungItems"
					:item-text="sg => sg === null ? '---' : `${sg.kuerzel} - ${sg.text}`" />
				<svws-ui-input-number :readonly="!hatKompetenzAendern" :disabled="loading" placeholder="Fachklasse-ID" :model-value="form.idFachklasse"
					@change="idFachklasse => patch({ idFachklasse })" />
			</svws-ui-input-wrapper>
			<svws-ui-button v-if="klasse.idStundentafel !== null" type="icon" title="Stundentafel öffnen" @click="gotoStundentafel(klasse.idStundentafel)"><span class="icon i-ri-link" /></svws-ui-button>
		</svws-ui-content-card>
		<svws-ui-content-card title="Klassenlehrer" class="col-span-full">
			<svws-ui-table :items="klassenlehrer" :columns="columnsKlassenlehrer" :selectable="hatKompetenzAendern && !loading" v-model="selectedKlassenlehrer" count>
				<template #cell(kuerzel)="{ rowData }">
					{{ state.uvManager.lehrerGetByKlassenLehrer(rowData).kuerzel }} <svws-ui-button type="icon" title="UV-Lehrkraft öffnen" @click="gotoLehrer(rowData.idLehrer)"><span class="icon i-ri-link" /></svws-ui-button>
				</template>
				<template #cell(reihenfolge)="{ rowData }">
					<svws-ui-input-number :readonly="!hatKompetenzAendern" :disabled="loading" headless :model-value="rowData.reihenfolge"
						@change="val => patchKlassenLehrer(rowData, val)"
						placeholder="Reihenfolge" :min="1" />
				</template>
				<template #actions v-if="hatKompetenzAendern">
					<svws-ui-select :readonly="!hatKompetenzAendern" :disabled="loading" :model-value="undefined" @update:model-value="onAddKlassenLehrer"
						headless indeterminate autocomplete :items="availableKlassenLehrer" removable
						title="Lehrkraft hinzufuegen..."
						:item-text="(l: UvLehrer) => `${l.nachname}, ${l.vorname} (${l.kuerzel})`" />
					<svws-ui-button @click="deleteKlassenlehrer" type="trash" :disabled="loading || !selectedKlassenlehrer.length" />
				</template>
				<template #noData>
					Keine Klassenlehrer zugeordnet
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
		<s-uv-schuelergruppen-schuelerliste :gruppe="state.uvManager.schuelergruppeGetByKlasse(klasse)"
			class="col-span-full"
			title="Schüler der Klasse"
			no-data-text="Keine Schüler in dieser Klasse"
			:goto-schueler
			:goto-klasse />
	</div>
	<div v-else class="page page-flex-row max-w-480">
		<span class="text-ui-disabled">Keine Klasse ausgewählt</span>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";

	import type { Schuljahresabschnitt } from "@core/asd/data/schule/Schuljahresabschnitt";
	import { AllgemeinbildendOrganisationsformen } from "@core/asd/types/schule/AllgemeinbildendOrganisationsformen";
	import { Schulgliederung } from "@core/asd/types/schule/Schulgliederung";
	import { UvKlasse } from "@core/core/data/uv/UvKlasse";
	import type { UvKlassenLehrer } from "@core/core/data/uv/UvKlassenLehrer";
	import type { UvLehrer } from "@core/core/data/uv/UvLehrer";
	import type { UvSchuelergruppe } from "@core/core/data/uv/UvSchuelergruppe";
	import type { UvStundentafel } from "@core/core/data/uv/UvStundentafel";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useUvState } from "@ui/states/UvState";

	import SUvSchuelergruppenSchuelerliste from "~/components/unterrichtsverteilung/schuelergruppen/SUvSchuelergruppenSchuelerliste.vue";

	const props = defineProps<{
		klasse: UvKlasse | undefined;
		schuljahr: number;
		gotoSchuelergruppe: (idSchuelergruppe: number) => Promise<void>;
		gotoSchueler: (idSchueler: number) => Promise<void>;
		gotoKlasse: (idKlasse: number) => Promise<void>;
		gotoLehrer: (idLehrer: number) => Promise<void>;
		gotoStundentafel: (idStundentafel: number) => Promise<void>;
	}>();
	const abschnittState = useAbschnittState();
	const state = useUvState();
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);
	const form = computed(() => props.klasse ?? new UvKlasse());
	async function patch(changes: Partial<UvKlasse>): Promise<void> {
		if ((props.klasse === undefined) || loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		try {
			await state.patchKlasse(props.klasse.idPlanungsabschnitt, props.klasse.id, changes);
		} finally {
			loading.value = false;
		}
	}

	async function onKuerzelChange(kuerzel: string | null | undefined): Promise<void> {
		const klasse = props.klasse;
		if (klasse === undefined) {
			return;
		}
		await patch({ kuerzel: kuerzel ?? klasse.kuerzel });
	}


	const parallelitaetItems = ['---', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];

	function organisationsformText(organisationsform: AllgemeinbildendOrganisationsformen | null): string {
		if (organisationsform === null) {
			return '---';
		}
		const daten = organisationsform.daten(props.schuljahr);
		return `${daten?.kuerzel ?? '—'} - ${daten?.text ?? '—'}`;
	}

	const stundentafelItems = computed(() => {
		const list: Array<UvStundentafel | { id: null; bezeichnung: string }> = [{ id: null, bezeichnung: "Keine Stundentafel" }];
		list.push(...state.uvManager.stundentafelGetMengeAsList());
		return list;
	});

	const selectedStundentafel = computed(() => {
		if (form.value.idStundentafel === null) {
			return stundentafelItems.value[0];
		}
		return stundentafelItems.value.find(st => st.id === form.value.idStundentafel) ?? stundentafelItems.value[0];
	});

	async function onStundentafelChange(st: UvStundentafel | { id: null; bezeichnung: string } | null | undefined) {
		if (props.klasse === undefined) {
			return;
		}
		const idStundentafel = st?.id ?? null;
		await patch({ idStundentafel });
	}

	const schuljahresabschnittItems = computed(() => [...abschnittState.alle]);

	const selectedSchuljahresabschnitt = computed(() => {
		if (props.klasse === undefined) {
			return undefined;
		}
		return abschnittState.getOrNull(form.value.idSchuljahresabschnitt) ?? undefined;
	});

	async function onSchuljahresabschnittChange(sja: Schuljahresabschnitt | null | undefined) {
		if (props.klasse === undefined || sja === null || sja === undefined) {
			return;
		}
		await patch({ idSchuljahresabschnitt: sja.id });
	}

	const schuelergruppeItems = computed(() => [...state.uvManager.schuelergruppeGetMengeAsList()]);

	const selectedSchuelergruppe = computed(() => {
		if (props.klasse === undefined) {
			return undefined;
		}
		return schuelergruppeItems.value.find(sg => sg.id === form.value.idSchuelergruppe);
	});

	async function onSchuelergruppeChange(sg: UvSchuelergruppe | null | undefined) {
		if (props.klasse === undefined || sg === null || sg === undefined) {
			return;
		}
		await patch({ idSchuelergruppe: sg.id });
	}

	const organisationsformItems = computed(() => {
		const result: Array<AllgemeinbildendOrganisationsformen | null> = [null];
		for (const of of AllgemeinbildendOrganisationsformen.values()) {
			result.push(of);
		}
		return result;
	});

	const selectedOrganisationsform = computed(() => {
		if (props.klasse === undefined || form.value.orgFormKrz === null) {
			return null;
		}
		for (const of of AllgemeinbildendOrganisationsformen.values()) {
			const daten = of.daten(props.schuljahr);
			if (daten !== null && daten.kuerzel === form.value.orgFormKrz) {
				return of;
			}
		}
		return null;
	});

	async function onOrganisationsformChange(of: AllgemeinbildendOrganisationsformen | null | undefined) {
		if (props.klasse === undefined) {
			return;
		}
		const kuerzel = of?.daten(props.schuljahr)?.kuerzel ?? null;
		await patch({ orgFormKrz: kuerzel });
	}

	const schulgliederungItems = computed(() => {
		const result: Array<{ id: number; kuerzel: string; text: string } | null> = [null];
		for (const sg of Schulgliederung.values()) {
			const daten = sg.daten(props.schuljahr);
			if (daten !== null) {
				result.push({ id: daten.id, kuerzel: daten.kuerzel, text: daten.text });
			}
		}
		return result;
	});

	const selectedSchulgliederung = computed(() => {
		if (props.klasse === undefined || form.value.asdSchulformNr === null) {
			return null;
		}
		return schulgliederungItems.value.find(sg => sg !== null && sg.kuerzel === form.value.asdSchulformNr) ?? null;
	});

	async function onSchulgliederungChange(sg: { id: number; kuerzel: string; text: string } | null | undefined) {
		if (props.klasse === undefined) {
			return;
		}
		await patch({ asdSchulformNr: sg?.kuerzel ?? null });
	}

	const columnsKlassenlehrer = [
		{ key: "kuerzel", label: "Kürzel", sortable: true },
		{ key: "reihenfolge", label: "Reihenfolge", sortable: true, span: 0.4 },
	];

	const selectedKlassenlehrer = ref<UvKlassenLehrer[]>([]);

	async function patchKlassenLehrer(klassenlehrer: UvKlassenLehrer, reihenfolge: number | null) {
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		try {
			await state.patchKlassenLehrer(klassenlehrer.id, { reihenfolge: reihenfolge ?? klassenlehrer.reihenfolge }, klassenlehrer);
		} finally {
			loading.value = false;
		}
	}

	async function deleteKlassenlehrer() {
		if (loading.value || !hatKompetenzAendern.value || selectedKlassenlehrer.value.length === 0) {
			return;
		}
		loading.value = true;
		try {
			await state.delKlassenLehrer(selectedKlassenlehrer.value);
			selectedKlassenlehrer.value = [];
		} finally {
			loading.value = false;
		}
	}

	const klassenlehrer = computed(() => {
		if (props.klasse === undefined) {
			return new ArrayList<UvKlassenLehrer>();
		}
		return state.uvManager.klassenLehrerGetMengeByKlasse(props.klasse);
	});

	const availableKlassenLehrer = computed(() => {
		if (props.klasse === undefined || state.planungsabschnitt === null) {
			return [];
		}
		const assignedIds = new Set(
			[...klassenlehrer.value].map(kl => kl.idLehrer)
		);
		return [...state.uvManager.lehrerGetMengeByPlanungsabschnitt(state.planungsabschnitt)].filter(
			l => !assignedIds.has(l.id)
		);
	});

	async function onAddKlassenLehrer(value: unknown) {
		const lehrer = value as UvLehrer | null | undefined;
		if (lehrer === undefined || lehrer === null || props.klasse === undefined) {
			return;
		}
		const idKlasse = props.klasse.id;
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		try {
			await state.addKlassenLehrer({
				idKlasse,
				idLehrer: lehrer.id,
				reihenfolge: [...klassenlehrer.value].length + 1,
			});
		} finally {
			loading.value = false;
		}
	}

</script>
