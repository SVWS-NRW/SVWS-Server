<template>
	<div v-if="schiene !== undefined" class="page page-grid-cards">
		<svws-ui-content-card class="col-span-full">
			<template #title>
				<div class="content-card--header"><h3 class="content-card--headline" title="Daten der Schiene">Daten der Schiene</h3>&nbsp;<svws-ui-badge type="light" title="ID" class="font-mono" size="big"> ID: {{ schiene.id }} </svws-ui-badge></div>
			</template>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-input-number :readonly="!hatKompetenzAendern" :disabled="loading" placeholder="Nummer" :model-value="form.nummer" @change="onNummerChange" :min="1" />
				<svws-ui-text-input :readonly="!hatKompetenzAendern" :disabled="loading" placeholder="Bezeichnung" :model-value="form.bezeichnung" @change="bezeichnung => patch({ bezeichnung })" />
				<svws-ui-multi-select :readonly="!hatKompetenzAendern" :disabled="loading" :model-value="selectedJahrgaenge"
					@update:model-value="onJahrgaengeChange"
					:items="jahrgangItems"
					:item-text="item => item.kuerzel ?? ''"
					title="Jahrgänge" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Lerngruppen in dieser Schiene" class="col-span-full">
			<svws-ui-table :items="lerngruppenRows" :columns count>
				<template #cell(bezeichnung)="{ rowData }">
					{{ rowData.bezeichnung }} <svws-ui-button type="icon" title="Lerngruppe öffnen" @click="gotoLerngruppe(rowData.lerngruppe.id)"><span class="icon i-ri-link" /></svws-ui-button>
				</template>
				<template #noData>
					Keine Lerngruppen in dieser Schiene
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
	</div>
	<div v-else class="page page-flex-row max-w-480">
		<span class="text-ui-disabled">Keine Schiene ausgewählt</span>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";

	import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
	import type { UvLerngruppe } from "@core/core/data/uv/UvLerngruppe";
	import { UvSchiene } from "@core/core/data/uv/UvSchiene";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { useUvState } from "@ui/states/UvState";

	import { useUvPresenter } from "../UvPresenter";

	const props = defineProps<{
		schiene: UvSchiene | undefined;
		gotoLerngruppe: (id: number) => Promise<void>;
	}>();
	const state = useUvState();
	const presenter = useUvPresenter(state.uvManager);
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);
	const form = computed(() => props.schiene ?? new UvSchiene());
	async function patch(changes: Partial<UvSchiene>): Promise<void> {
		if ((props.schiene === undefined) || loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		try {
			await state.patchSchiene(props.schiene.id, changes);
		} finally {
			loading.value = false;
		}
	}

	async function onNummerChange(nummer: number | null | undefined): Promise<void> {
		const schiene = props.schiene;
		if (schiene === undefined) {
			return;
		}
		await patch({ nummer: nummer ?? schiene.nummer });
	}


	const jahrgangItems = computed(() => [...state.uvManager.jahrgangsdatenGetMenge()]);

	const selectedJahrgaenge = computed(() => {
		if (props.schiene === undefined) {
			return [];
		}
		const idsErlaubt = new Set(form.value.idsJahrgaengeErlaubt);
		return jahrgangItems.value.filter(j => idsErlaubt.has(j.id));
	});

	async function onJahrgaengeChange(jahrgaenge: JahrgangsDaten[]) {
		if (props.schiene === undefined) {
			return;
		}
		const idsJahrgaengeErlaubt = new ArrayList<number>();
		for (const j of jahrgaenge) {
			idsJahrgaengeErlaubt.add(j.id);
		}
		await patch({ idsJahrgaengeErlaubt });
	}

	const lerngruppenRows = computed(() => props.schiene === undefined ? [] : [...state.uvManager.lerngruppenSchieneGetMengeBySchiene(props.schiene)]
		.map(zuordnung => createTableRow(state.uvManager.lerngruppeGetByLerngruppenSchiene(zuordnung))));

	const columns = [
		{ key: "typ", label: "Typ", sortable: true, span: 0.3 },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true },
		{ key: "fach", label: "Fach", sortable: true, span: 0.4 },
		{ key: "wochenstunden", label: "Wochenstunden", sortable: true, span: 0.4 },
	];

	interface LerngruppeTableRow {
		lerngruppe: UvLerngruppe;
		typ: string;
		bezeichnung: string;
		fach: string;
		wochenstunden: number;
	}

	function createTableRow(lerngruppe: UvLerngruppe): LerngruppeTableRow {
		return {
			lerngruppe,
			typ: presenter.lerngruppeTyp(lerngruppe),
			bezeichnung: presenter.lerngruppeBezeichnung(lerngruppe),
			fach: presenter.lerngruppeFach(lerngruppe),
			wochenstunden: lerngruppe.wochenstunden,
		};
	}

</script>
