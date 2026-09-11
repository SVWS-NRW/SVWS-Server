<template>
	<div>
		<svws-ui-content-card :title>
			<template #actions v-if="hatKompetenzAendern && (props.addSchueler !== undefined || props.removeSchueler !== undefined)">
				<svws-ui-button v-if="props.addSchueler !== undefined" @click="showAddDialog = true" :disabled="loading" type="icon" title="Schüler hinzufügen">
					<span class="icon i-ri-add-line" />
				</svws-ui-button>
				<svws-ui-button v-if="props.removeSchueler !== undefined" @click="onRemove" type="trash" :disabled="loading || !selectedSchueler.length" title="Ausgewählte Schüler entfernen" />
			</template>
			<svws-ui-table :items="mitgliederFiltered"
				:columns
				:selectable="hatKompetenzAendern && !loading && removeSchueler !== undefined"
				v-model="selectedSchueler"
				count>
				<template #search><svws-ui-text-input v-model="search" type="search" placeholder="Schüler suchen" removable /></template>
				<template #cell(nachname)="{ rowData }">
					{{ rowData.daten.nachname }} <svws-ui-button type="icon" title="UV-Schüler öffnen" @click="gotoSchueler(rowData.idSchueler)"><span class="icon i-ri-link" /></svws-ui-button>
					<span v-if="!state.uvManager.planungsabschnittSchuelerHatZurGruppePassendenJahrgang(rowData, gruppe)" class="icon icon-ui-warning i-ri-alert-line" title="Der Jahrgang ist für diese Gruppe nicht freigegeben." />
				</template>
				<template #cell(vorname)="{ rowData }">
					{{ rowData.daten.vorname }}
				</template>
				<template #cell(klasse)="{ rowData }">
					{{ presenter.klasseKuerzel(rowData.idKlasse) }} <svws-ui-button v-if="rowData.idKlasse !== null" type="icon" title="UV-Klasse öffnen" @click="gotoKlasse(rowData.idKlasse)"><span class="icon i-ri-link" /></svws-ui-button>
				</template>
				<template #noData>
					{{ search.trim().length > 0 ? 'Keine Treffer für diese Suche.' : noDataText }}
				</template>
			</svws-ui-table>
		</svws-ui-content-card>

		<svws-ui-modal v-model:show="showAddDialog" size="big" :auto-close="!loading" :close-in-title="!loading">
			<template #modalTitle>Schüler hinzufügen</template>
			<template #modalContent>
				<div class="flex min-h-0 flex-col gap-4">
					<div class="grid grid-cols-3 gap-3">
						<svws-ui-text-input v-model="addSearch" type="search" placeholder="Name oder Klasse suchen" removable />
						<svws-ui-select v-model="addKlasse" :items="addKlassen" :item-text="item => item" title="Klasse" removable />
						<svws-ui-select v-model="addJahrgang" :items="addJahrgaenge" :item-text="item => item" title="Jahrgang" removable />
					</div>
					<div class="min-h-0 max-h-[50vh] overflow-auto">
						<svws-ui-table :items="verfuegbareFiltered" scroll
							:columns
							:selectable="hatKompetenzAendern && !loading"
							v-model="schuelerToAdd"
							count
							v-model:sort-by-and-order="addSortierung">
							<template #cell(nachname)="{ rowData }">
								{{ rowData.daten.nachname }} <svws-ui-button type="icon" title="UV-Schüler öffnen" @click="gotoSchueler(rowData.idSchueler)"><span class="icon i-ri-link" /></svws-ui-button>
							</template>
							<template #cell(vorname)="{ rowData }">
								{{ rowData.daten.vorname }}
							</template>
							<template #cell(klasse)="{ rowData }">
								{{ presenter.klasseKuerzel(rowData.idKlasse) }} <svws-ui-button v-if="rowData.idKlasse !== null" type="icon" title="UV-Klasse öffnen" @click="gotoKlasse(rowData.idKlasse)"><span class="icon i-ri-link" /></svws-ui-button>
							</template>
							<template #noData>
								{{ verfuegbareSchueler.length > 0 ? 'Keine Treffer für diese Suche oder Filter.' : 'Keine weiteren Schüler verfügbar' }}
							</template>
						</svws-ui-table>
					</div>
				</div>
			</template>
			<template #modalActions>
				<svws-ui-button @click="showAddDialog = false" :disabled="loading" type="secondary">Abbrechen</svws-ui-button>
				<svws-ui-button @click="onAdd" type="primary" :disabled="loading || !hatKompetenzAendern || !schuelerToAdd.length" :is-loading="loading">Hinzufügen</svws-ui-button>
			</template>
		</svws-ui-modal>
	</div>
</template>

<script setup lang="ts">
	import { computed, ref, watch } from "vue";

	import { UvPlanungsabschnittSchueler } from "@core/core/data/uv/UvPlanungsabschnittSchueler";
	import type { UvSchuelergruppe } from "@core/core/data/uv/UvSchuelergruppe";
	import { useUvState } from "@ui/states/UvState";

	import { useUvPresenter } from "../UvPresenter";

	const props = defineProps<{
		gruppe: UvSchuelergruppe;
		title?: string;
		noDataText?: string;
		addSchueler?: (schuelerIds: number[]) => Promise<void>;
		removeSchueler?: (schuelerIds: number[]) => Promise<void>;
		gotoSchueler: (id: number) => Promise<void>;
		gotoKlasse: (id: number) => Promise<void>;
	}>();

	const state = useUvState();
	const presenter = useUvPresenter(state.uvManager);
	const hatKompetenzAendern = computed(() => state.hatKompetenzAendern);
	const loading = ref(false);
	watch(() => props.gruppe.id, () => {
		selectedSchueler.value = [];
		schuelerToAdd.value = [];
		search.value = '';
		addSearch.value = '';
		addKlasse.value = undefined;
		addJahrgang.value = undefined;
		addSortierung.value = { key: 'klasse', order: true };
		showAddDialog.value = false;
	});

	const title = computed(() => props.title ?? "Schüler");
	const noDataText = computed(() => props.noDataText ?? "Keine Schüler vorhanden");

	const search = ref('');
	const addSearch = ref('');
	const addKlasse = ref<string | undefined>();
	const addJahrgang = ref<string | undefined>();
	const addSortierung = ref({ key: 'klasse', order: true });
	const collator = new Intl.Collator('de', { numeric: true });
	const addKlassen = computed(() => [...new Set(verfuegbareSchueler.value.map(s => toRow(s).klasse))].sort((a, b) => collator.compare(a, b)));
	const addJahrgaenge = computed(() => [...new Set(verfuegbareSchueler.value.map(s => presenter.jahrgangKuerzel(s.idJahrgang)))].sort((a, b) => collator.compare(a, b)));
	function toRow(schueler: UvPlanungsabschnittSchueler) {
		return Object.assign(new UvPlanungsabschnittSchueler(), schueler, {
			nachname: schueler.daten.nachname, vorname: schueler.daten.vorname,
			jahrgang: presenter.jahrgangKuerzel(schueler.idJahrgang),
			klasse: schueler.idKlasse === null ? '—' : presenter.klasseKuerzel(schueler.idKlasse),
		});
	}
	function matches(schueler: UvPlanungsabschnittSchueler, text: string): boolean {
		return `${schueler.daten.nachname} ${schueler.daten.vorname} ${presenter.jahrgangKuerzel(schueler.idJahrgang)} ${schueler.idKlasse === null ? '—' : presenter.klasseKuerzel(schueler.idKlasse)}`
			.toLocaleLowerCase().includes(text.trim().toLocaleLowerCase());
	}
	const mitgliederFiltered = computed(() => [...state.uvManager.planungsabschnittSchuelerGetMengeBySchuelergruppe(props.gruppe)]
		.filter(s => matches(s, search.value)).map(toRow));
	const verfuegbareFiltered = computed(() => verfuegbareSchueler.value.filter(s => matches(s, addSearch.value)).map(toRow)
		.filter(s => (addKlasse.value === undefined || s.klasse === addKlasse.value) && (addJahrgang.value === undefined || s.jahrgang === addJahrgang.value))
		.sort((a, b) => [collator.compare(a.klasse, b.klasse), collator.compare(a.nachname, b.nachname), collator.compare(a.vorname, b.vorname)].find(cmp => cmp !== 0) ?? a.idSchueler - b.idSchueler));

	const selectedSchueler = ref<UvPlanungsabschnittSchueler[]>([]);
	const showAddDialog = ref(false);
	const schuelerToAdd = ref<UvPlanungsabschnittSchueler[]>([]);

	const columns = [
		{ key: "klasse", label: "Klasse", sortable: true, defaultSort: "asc" as const, span: 0.4 },
		{ key: "nachname", label: "Nachname", sortable: true },
		{ key: "vorname", label: "Vorname", sortable: true },
	];

	const verfuegbareSchueler = computed(() => {
		const schuelerDerGruppe = [...state.uvManager.planungsabschnittSchuelerGetMengeBySchuelergruppe(props.gruppe)];
		const currentIds = new Set(schuelerDerGruppe.map(s => s.idSchueler));
		return [...state.uvManager.planungsabschnittSchuelerGetMengeAsList()]
			.filter(s => s.idPlanungsabschnitt === props.gruppe.idPlanungsabschnitt && !currentIds.has(s.idSchueler));
	});

	async function onRemove() {
		if (props.removeSchueler === undefined || selectedSchueler.value.length === 0) {
			return;
		}
		const ids = selectedSchueler.value.map(s => s.idSchueler);
		const action = props.removeSchueler;
		const idSchuelergruppe = props.gruppe.id;
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		try {
			await action(ids);
			if (props.gruppe.id === idSchuelergruppe) {
				selectedSchueler.value = [];
			}
		} finally {
			loading.value = false;
		}
	}

	async function onAdd() {
		if (props.addSchueler === undefined || schuelerToAdd.value.length === 0) {
			return;
		}
		const ids = schuelerToAdd.value.map(s => s.idSchueler);
		const action = props.addSchueler;
		const idSchuelergruppe = props.gruppe.id;
		if (loading.value || !hatKompetenzAendern.value) {
			return;
		}
		loading.value = true;
		try {
			await action(ids);
			if (props.gruppe.id === idSchuelergruppe) {
				schuelerToAdd.value = [];
				showAddDialog.value = false;
			}
		} finally {
			loading.value = false;
		}
	}

</script>
