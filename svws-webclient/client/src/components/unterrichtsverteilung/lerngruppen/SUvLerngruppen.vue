<template>
	<div v-if="lerngruppe !== undefined" class="page page-grid-cards">
		<svws-ui-content-card class="col-span-full">
			<template #title>
				<div class="content-card--header"><h3 class="content-card--headline" title="Daten der Lerngruppe">Daten der Lerngruppe</h3>&nbsp;<svws-ui-badge type="light" title="ID" class="font-mono" size="small"> ID: {{ lerngruppe.id }} </svws-ui-badge></div>
			</template>
			<div class="mb-3 flex flex-wrap items-center gap-2">Zugehöriger Kurs / Klasse <svws-ui-button v-if="lerngruppe.idKurs !== null" type="icon" title="Kurs öffnen" @click="gotoKurs(lerngruppe.idKurs)"><span class="icon i-ri-link" /></svws-ui-button><svws-ui-button v-if="lerngruppe.idKlasse !== null" type="icon" title="Klasse öffnen" @click="gotoKlasse(lerngruppe.idKlasse)"><span class="icon i-ri-link" /></svws-ui-button></div>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Typ" :model-value="typ" readonly />
				<svws-ui-text-input placeholder="Bezeichnung" :model-value="bezeichnung" readonly />
				<svws-ui-input-number placeholder="Wochenstunden" :model-value="form.wochenstunden" :readonly="!hatKompetenzAendern" :disabled="loading" :min="0" :steps="0.5" :decimal-places="2"
					@change="value => patch({ wochenstunden: value ?? -1 })" />
				<svws-ui-input-number placeholder="Wochenstunden unterrichtet" :model-value="form.wochenstundenUnterrichtet" :readonly="!hatKompetenzAendern" :disabled="loading" :min="0" :steps="0.5" :decimal-places="2"
					@change="value => patch({ wochenstundenUnterrichtet: value ?? -1 })" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Lehrkräfte der Lerngruppe" class="col-span-full">
			<svws-ui-table :items="lerngruppenlehrer" :columns="columnsLehrer" :selectable="hatKompetenzAendern && !loading" v-model="selectedLehrer" count>
				<template #cell(kuerzel)="{ rowData }">
					{{ state.uvManager.lehrerGetByLerngruppenLehrer(rowData).kuerzel }} <svws-ui-button type="icon" title="UV-Lehrkraft öffnen" @click="gotoLehrer(rowData.idLehrer)"><span class="icon i-ri-link" /></svws-ui-button>
				</template>
				<template #cell(reihenfolge)="{ rowData }">
					<svws-ui-input-number headless :model-value="lehrerDrafts.get(rowData.id)?.reihenfolge ?? rowData.reihenfolge" :readonly="!hatKompetenzAendern" :disabled="loading"
						@change="val => patchLehrer(rowData, { reihenfolge: val ?? -1 })"
						placeholder="Reihenfolge" :min="1" />
				</template>
				<template #cell(wochenstunden)="{ rowData }">
					<svws-ui-input-number headless :model-value="lehrerDrafts.get(rowData.id)?.wochenstunden ?? rowData.wochenstunden" :readonly="!hatKompetenzAendern" :disabled="loading"
						@change="val => patchLehrer(rowData, { wochenstunden: val ?? -1 })"
						placeholder="Wochenstunden" :steps="0.5" :decimal-places="2" :max="lerngruppe.wochenstundenUnterrichtet" />
				</template>
				<template #actions v-if="hatKompetenzAendern">
					<svws-ui-select :disabled="loading" :model-value="undefined" @update:model-value="onAddLehrer"
						headless indeterminate autocomplete :items="availableLehrer" removable
						title="Lehrkraft hinzufügen..."
						:item-text="(l: UvLehrer) => `${l.nachname}, ${l.vorname} (${l.kuerzel})`" />
					<svws-ui-button @click="removeLerngruppenLehrer" type="trash" :disabled="loading || !selectedLehrer.length" title="Ausgewählte Zuordnungen entfernen" />
				</template>
				<template #noData>
					Keine Lehrkräfte zugeordnet
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
		<svws-ui-content-card title="Schienen der Lerngruppe" class="col-span-full">
			<svws-ui-table :items="lerngruppenschienen" :columns="columnsSchienen" :selectable="hatKompetenzAendern && !loading" v-model="selectedSchienen" count>
				<template #cell(nummer)="{ rowData }">
					{{ state.uvManager.schieneGetByLerngruppenSchiene(rowData).nummer }} <svws-ui-button type="icon" title="Schiene öffnen" @click="gotoSchiene(rowData.idSchiene)"><span class="icon i-ri-link" /></svws-ui-button>
				</template>
				<template #cell(bezeichnung)="{ rowData }">
					{{ state.uvManager.schieneGetByLerngruppenSchiene(rowData).bezeichnung }}
				</template>
				<template #actions v-if="hatKompetenzAendern">
					<svws-ui-select :disabled="loading" :model-value="undefined" @update:model-value="onAddSchiene"
						headless indeterminate autocomplete :items="availableSchienen" removable
						title="Schiene hinzufügen..."
						:item-text="(s: UvSchiene) => `${s.nummer} ${s.bezeichnung ?? ''}`.trim()" />
					<svws-ui-button @click="removeLerngruppenSchiene" type="trash" :disabled="loading || !selectedSchienen.length" title="Ausgewählte Zuordnungen entfernen" />
				</template>
				<template #noData>
					Keine Schienen zugeordnet
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
	</div>
	<div v-else class="page page-flex-row max-w-480">
		<span class="text-ui-secondary">Keine Lerngruppe ausgewählt</span>
	</div>
</template>

<script setup lang="ts">
	import { computed, ref, shallowRef, watch } from "vue";

	import type { UvLehrer } from "@core/core/data/uv/UvLehrer";
	import { UvLerngruppe } from "@core/core/data/uv/UvLerngruppe";
	import type { UvLerngruppenLehrer } from "@core/core/data/uv/UvLerngruppenLehrer";
	import type { UvLerngruppenSchiene } from "@core/core/data/uv/UvLerngruppenSchiene";
	import type { UvSchiene } from "@core/core/data/uv/UvSchiene";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { useUvState } from "@ui/states/UvState";

	import { useUvPresenter } from "../UvPresenter";

	const { lerngruppe } = defineProps<{
		lerngruppe: UvLerngruppe | undefined;
		schuljahr: number;
		gotoKurs: (id: number) => Promise<void>;
		gotoKlasse: (id: number) => Promise<void>;
		gotoLehrer: (id: number) => Promise<void>;
		gotoSchiene: (id: number) => Promise<void>;
	}>();

	const state = useUvState();
	const presenter = useUvPresenter(state.uvManager);
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);
	const draft = shallowRef<Partial<UvLerngruppe>>({});
	const lehrerDrafts = shallowRef(new Map<number, Partial<UvLerngruppenLehrer>>());
	const form = computed(() => Object.assign(new UvLerngruppe(), lerngruppe, draft.value));
	watch(() => lerngruppe?.id, () => {
		draft.value = {};
		lehrerDrafts.value = new Map();
		selectedLehrer.value = [];
		selectedSchienen.value = [];
	});
	async function patch(changes: Partial<UvLerngruppe>) {
		if (!hatKompetenzAendern.value || loading.value || lerngruppe === undefined) {
			return;
		}
		draft.value = { ...draft.value, ...changes };
		if (![form.value.wochenstunden, form.value.wochenstundenUnterrichtet].every(value => Number.isFinite(value) && value >= 0)) {
			return;
		}
		const id = lerngruppe.id;
		const data = draft.value;
		loading.value = true;
		try {
			await state.patchLerngruppe(id, data);
			if (lerngruppe.id === id) {
				draft.value = {};
			}
		} finally {
			loading.value = false;
		}
	}
	async function patchLehrer(row: UvLerngruppenLehrer, changes: Partial<UvLerngruppenLehrer>) {
		if (!hatKompetenzAendern.value || loading.value || lerngruppe === undefined) {
			return;
		}
		const data = { ...lehrerDrafts.value.get(row.id), ...changes };
		lehrerDrafts.value = new Map(lehrerDrafts.value).set(row.id, data);
		const reihenfolge = data.reihenfolge ?? row.reihenfolge;
		const stunden = data.wochenstunden ?? row.wochenstunden;
		if (!Number.isInteger(reihenfolge) || reihenfolge < 1 || !Number.isFinite(stunden) || stunden < 0 || stunden > lerngruppe.wochenstundenUnterrichtet) {
			return;
		}
		const idLerngruppe = lerngruppe.id;
		loading.value = true;
		try {
			await state.patchLerngruppenLehrer(row.id, data, row);
			if (lerngruppe.id === idLerngruppe) {
				const remaining = new Map(lehrerDrafts.value);
				remaining.delete(row.id);
				lehrerDrafts.value = remaining;
			}
		} finally {
			loading.value = false;
		}
	}

	const typ = computed(() => lerngruppe === undefined ? '' : presenter.lerngruppeTyp(lerngruppe));
	const bezeichnung = computed(() => lerngruppe === undefined ? '' : presenter.lerngruppeBezeichnung(lerngruppe));

	const columnsLehrer = [
		{ key: "kuerzel", label: "Kürzel", sortable: true },
		{ key: "reihenfolge", label: "Reihenfolge", sortable: true, span: 0.4 },
		{ key: "wochenstunden", label: "Wochenstunden", sortable: true, span: 0.5 },
	];

	const selectedLehrer = ref<UvLerngruppenLehrer[]>([]);

	const lerngruppenlehrer = computed(() => {
		if (lerngruppe === undefined) {
			return new ArrayList<UvLerngruppenLehrer>();
		}
		return state.uvManager.lerngruppenLehrerGetMengeByLerngruppe(lerngruppe);
	});

	const availableLehrer = computed(() => {
		if (lerngruppe === undefined || state.planungsabschnitt === null) {
			return [];
		}
		const assignedIds = new Set(
			[...lerngruppenlehrer.value].map(lgl => lgl.idLehrer)
		);
		return [...state.uvManager.lehrerGetMengeByPlanungsabschnitt(state.planungsabschnitt)].filter(
			l => !assignedIds.has(l.id)
		);
	});

	async function onAddLehrer(value: unknown) {
		const lehrer = value as UvLehrer | null | undefined;
		if (lehrer === undefined || lehrer === null || lerngruppe === undefined) {
			return;
		}
		const data = {
			idLerngruppe: lerngruppe.id,
			idLehrer: lehrer.id,
			reihenfolge: [...lerngruppenlehrer.value].length + 1,
			wochenstunden: lerngruppe.wochenstunden,
			wochenstundenAngerechnet: 0,
		};
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		try {
			await state.addLerngruppenLehrer(data);
		} finally {
			loading.value = false;
		}
	}

	const columnsSchienen = [
		{ key: "nummer", label: "Nummer", sortable: true, span: 0.3 },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true },
	];

	const lerngruppenschienen = computed(() => {
		if (lerngruppe === undefined) {
			return new ArrayList<UvLerngruppenSchiene>();
		}
		return state.uvManager.lerngruppenSchieneGetMengeByLerngruppe(lerngruppe);
	});

	const selectedSchienen = ref<UvLerngruppenSchiene[]>([]);

	const availableSchienen = computed(() => {
		if (lerngruppe === undefined || state.planungsabschnitt === null) {
			return [];
		}
		const assignedIds = new Set(
			[...lerngruppenschienen.value].map(lgs => lgs.idSchiene)
		);
		return [...state.uvManager.schieneGetMengeByPlanungsabschnitt(state.planungsabschnitt)].filter(
			s => !assignedIds.has(s.id)
		);
	});

	async function onAddSchiene(value: unknown) {
		const schiene = value as UvSchiene | null | undefined;
		if (schiene === undefined || schiene === null || lerngruppe === undefined) {
			return;
		}
		const data = {
			idLerngruppe: lerngruppe.id,
			idSchiene: schiene.id,
		};
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		try {
			await state.addLerngruppenSchiene(data);
		} finally {
			loading.value = false;
		}
	}

	async function removeLerngruppenLehrer() {
		const items: UvLerngruppenLehrer[] = [...selectedLehrer.value];
		if (items.length === 0) {
			return;
		}
		const idLerngruppe = lerngruppe?.id;
		if (idLerngruppe === undefined) {
			return;
		}
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		try {
			await state.delLerngruppenLehrer([...items]);
			if (lerngruppe?.id === idLerngruppe) {
				selectedLehrer.value = [];
			}
		} finally {
			loading.value = false;
		}
	}

	async function removeLerngruppenSchiene() {
		const items: UvLerngruppenSchiene[] = [...selectedSchienen.value];
		if (items.length === 0) {
			return;
		}
		const idLerngruppe = lerngruppe?.id;
		if (idLerngruppe === undefined) {
			return;
		}
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		try {
			await state.delLerngruppenSchiene([...items]);
			if (lerngruppe?.id === idLerngruppe) {
				selectedSchienen.value = [];
			}
		} finally {
			loading.value = false;
		}
	}

</script>
