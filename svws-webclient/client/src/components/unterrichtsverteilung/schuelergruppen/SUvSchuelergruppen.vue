<template>
	<div v-if="schuelergruppe !== undefined" class="page page-grid-cards">
		<svws-ui-content-card class="col-span-full">
			<template #title>
				<div class="content-card--header"><h3 class="content-card--headline" title="Daten der Gruppe">Daten der Gruppe</h3>&nbsp;<svws-ui-badge type="light" title="ID" class="font-mono" size="small"> ID: {{ schuelergruppe.id }} </svws-ui-badge></div>
			</template>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Bezeichnung" :model-value="form.bezeichnung" :readonly="!hatKompetenzAendern" :disabled="loading" required @change="bezeichnung => patch({ bezeichnung: bezeichnung?.trim() ?? '' })" />
				<svws-ui-multi-select :readonly="!hatKompetenzAendern" :disabled="loading" :model-value="selectedJahrgaenge"
					@update:model-value="onJahrgaengeChange"
					:items="jahrgangItems"
					:item-text="item => item.kuerzel ?? ''"
					title="Erlaubte Jahrgänge" :valid="validiereJahrgaenge" :fehlerart="ValidatorFehlerart.KANN" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card class="col-span-full" title="Gruppe verwendet in Lerngruppen">
			<svws-ui-table :items="lerngruppen" :columns="lerngruppenColumns" count>
				<template #cell(typ)="{ rowData }">
					{{ presenter.lerngruppeTyp(rowData) }}
				</template>
				<template #cell(bezeichnung)="{ rowData }">
					{{ presenter.lerngruppeBezeichnung(rowData) }} <svws-ui-button type="icon" title="Lerngruppe öffnen" @click="gotoLerngruppe(rowData.id)"><span class="icon i-ri-link" /></svws-ui-button>
				</template>
				<template #cell(fach)="{ rowData }">
					{{ presenter.lerngruppeFach(rowData) }}
				</template>
				<template #cell(wochenstunden)="{ rowData }">
					{{ rowData.wochenstunden }}
				</template>
				<template #noData>
					Keine Lerngruppen für diese Gruppe
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
		<s-uv-schuelergruppen-schuelerliste :gruppe="schuelergruppe"
			:goto-schueler
			:goto-klasse
			:add-schueler
			:remove-schueler
			class="col-span-full"
			title="Mitglieder der Gruppe"
			no-data-text="Keine Schüler in dieser Gruppe" />
	</div>
	<div v-else class="page page-flex-row max-w-480">
		<span class="text-ui-secondary">Keine Schülergruppe ausgewählt</span>
	</div>
</template>

<script setup lang="ts">
	import { computed, ref, shallowRef, watch } from "vue";

	import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
	import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
	import { UvSchuelergruppe } from "@core/core/data/uv/UvSchuelergruppe";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { useUvState } from "@ui/states/UvState";

	import { useUvPresenter } from "../UvPresenter";
	import SUvSchuelergruppenSchuelerliste from "~/components/unterrichtsverteilung/schuelergruppen/SUvSchuelergruppenSchuelerliste.vue";

	const props = defineProps<{
		schuelergruppe: UvSchuelergruppe | undefined;
		gotoSchueler: (id: number) => Promise<void>;
		gotoKlasse: (id: number) => Promise<void>;
		gotoLerngruppe: (id: number) => Promise<void>;
	}>();

	const state = useUvState();
	const presenter = useUvPresenter(state.uvManager);
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);
	const draft = shallowRef<Partial<UvSchuelergruppe>>({});
	const form = computed(() => Object.assign(new UvSchuelergruppe(), props.schuelergruppe, draft.value));

	function validiereJahrgaenge(): boolean {
		const schuelergruppe = props.schuelergruppe;
		return schuelergruppe !== undefined && !state.uvManager.schuelergruppeHatSchuelerMitFalschemJahrgang(schuelergruppe);
	}

	async function addSchueler(ids: number[]): Promise<void> {
		const schuelergruppe = props.schuelergruppe;
		if (schuelergruppe === undefined) {
			return;
		}
		await state.addSchuelerToSchuelergruppe(schuelergruppe.id, ids);
	}

	async function removeSchueler(ids: number[]): Promise<void> {
		const schuelergruppe = props.schuelergruppe;
		if (schuelergruppe === undefined) {
			return;
		}
		await state.removeSchuelerFromSchuelergruppe(schuelergruppe.id, ids);
	}
	watch(() => props.schuelergruppe?.id, () => {
		draft.value = {};
	});
	async function patch(changes: Partial<UvSchuelergruppe>) {
		if (!hatKompetenzAendern.value || loading.value || props.schuelergruppe === undefined) {
			return;
		}
		draft.value = { ...draft.value, ...changes };
		if (form.value.bezeichnung.trim().length === 0) {
			return;
		}
		const id = props.schuelergruppe.id;
		const data = draft.value;
		loading.value = true;
		try {
			await state.patchSchuelergruppe(id, data);
			if (props.schuelergruppe.id === id) {
				draft.value = {};
			}
		} catch {
			// Der Entwurf bleibt für eine erneute Änderung erhalten.
		} finally {
			loading.value = false;
		}
	}

	const jahrgangItems = computed(() => [...state.uvManager.jahrgangsdatenGetMenge()]);

	const selectedJahrgaenge = computed(() => {
		if (props.schuelergruppe === undefined) {
			return [];
		}
		const idsErlaubt = new Set(form.value.idsJahrgaengeErlaubt);
		return jahrgangItems.value.filter(j => idsErlaubt.has(j.id));
	});

	const lerngruppen = computed(() => {
		if (props.schuelergruppe === undefined) {
			return [];
		}
		return [...state.uvManager.lerngruppeGetMengeBySchuelergruppe(props.schuelergruppe)];
	});

	const lerngruppenColumns = [
		{ key: "typ", label: "Typ", sortable: true, span: 0.3 },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true },
		{ key: "fach", label: "Fach", sortable: true, span: 0.4 },
		{ key: "wochenstunden", label: "Wstd.", sortable: true, span: 0.3 },
	];


	async function onJahrgaengeChange(jahrgaenge: JahrgangsDaten[]) {
		if (props.schuelergruppe === undefined) {
			return;
		}
		const idsJahrgaengeErlaubt = new ArrayList<number>();
		for (const j of jahrgaenge) {
			idsJahrgaengeErlaubt.add(j.id);
		}
		await patch({ idsJahrgaengeErlaubt });
	}

</script>
