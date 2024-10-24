<!-- eslint-disable @typescript-eslint/consistent-type-imports -->
<template>
	<div class="page--content">
		<svws-ui-table :columns="cols" :items="props.schuelerKaoaManager().liste.list()" class="col-span-full" clickable :clicked="selectedEntry" @update:clicked="item => selectEntry(item)">
			<template #cell(idJahrgang)="{ value } : { value: number }">
				<span>{{ getJahrgangFullName(value) }}</span>
			</template>
			<template #cell(idKategorie)="{ value } : { value: number }">
				<span>{{ getKategorieFullName(value) }}</span>
			</template>
			<template #cell(idMerkmal)="{ value } : { value: number }">
				<span>{{ getMerkmalFullName(value) }}</span>
			</template>
			<template #cell(idZusatzmerkmal)="{ value } : { value: number }">
				<span>{{ getZusatzmerkmalFullName(value) }}</span>
			</template>
		</svws-ui-table>
		<div class="button-container">
			<svws-ui-button v-if="currentMode === Mode.DEFAULT" title="AddButton" @click="setMode(Mode.ADD)" autofocus>
				<span class="icon i-ri-add-line" />
			</svws-ui-button>
			<svws-ui-button v-if="currentMode === Mode.DEFAULT && selectedEntry" title="PatchButton" @click="patchSelectedIDs">
				<span class="icon i-ri-edit-2-line" />
			</svws-ui-button>
			<svws-ui-button v-if="currentMode === Mode.DEFAULT && selectedEntry" title="DeleteButton" @click="deleteEntry">
				<span class="icon i-ri-delete-bin-line" />
			</svws-ui-button>
		</div>
		<br>
		<div v-if="currentMode === Mode.ADD || currentMode === Mode.PATCH">
			<svws-ui-select title="Schuljahresabschnitt" :items="props.schuelerKaoaManager()._schuljahresabschnitteFiltered" :item-text="schuljahresabschnittText" v-model="selectedSchuljahresabschnitt"/>
			<svws-ui-select title="KAoAKategorie" v-if="selectedSchuljahresabschnitt" :items="KAOAKategorie.getListBySchuljahrAndJahrgang(schuljahr, selectedJahrgang.id)" :item-text="itemText" v-model="selectedKategorie" />
			<svws-ui-select title="KAoAMerkmal" v-if="selectedKategorie" :items="props.schuelerKaoaManager().getKAOAMerkmaleByKategorie(selectedKategorie)" :item-text="itemText" v-model="selectedMerkmal" />
			<svws-ui-select title="KAoAZusatzmerkmal" v-if="selectedMerkmal" :items="props.schuelerKaoaManager().getKAOAZusatzmerkmaleByMerkmal(selectedMerkmal)" :item-text="itemText" v-model="selectedZusatzmerkmal" />
			<svws-ui-select title="KAoAEbene4" v-if="showEbene4" :items="props.schuelerKaoaManager().getKAOAEbene4ByZusatzmerkmal(selectedZusatzmerkmal)" :item-text="itemText" v-model="selectedEbene4" />
			<svws-ui-select title="KAoAAnschlussoption" v-if="selectedZusatzmerkmal && showAnschlussoption" :items="props.schuelerKaoaManager().getKAOAAnschlussoptionenByZusatzmerkmal(selectedZusatzmerkmal)" :item-text="itemText" v-model="selectedAnschlussoption" />
			<svws-ui-select title="KAoABerufsfeld" v-if="showBerufsfeld" :items="props.schuelerKaoaManager().getKAOABerufsfelder()" :item-text="itemText" v-model="selectedBerufsfeld" />
			<svws-ui-text-input title="Bemerkung" v-if="showFreitext" v-model="selectedBemerkung" placeholder="Freitext" type="text" />
			<svws-ui-button title="Add" v-if="currentMode === Mode.ADD" :disabled="!validateRequiredFieldsFilled()" @click="addEntry">
				<span class="icon i-ri-add-line" />
			</svws-ui-button>
			<svws-ui-button title="Patch" v-if="currentMode === Mode.PATCH && validateRequiredFieldsFilled()" @click="patchEntry">
				<span class="icon i-ri-edit-2-line" />
			</svws-ui-button>
		</div>
	</div>
</template>

<script setup lang="ts">
	import type { SchuelerKAoAProps } from './SSchuelerKaoaProps';
	import {
		CoreTypeData, KAOAKategorieKatalogEintrag, KAOAMerkmalKatalogEintrag, KAOAZusatzmerkmalKatalogEintrag, SchuelerKAoADaten, Schuljahresabschnitt,
		KAOAEbene4KatalogEintrag, KAOAAnschlussoptionenKatalogEintrag, KAOABerufsfeldKatalogEintrag, KAOAAnschlussoptionen, KAOABerufsfeld, KAOAEbene4
	} from "@core";
	import { Jahrgaenge, KAOAKategorie, KAOAMerkmal, KAOAZusatzmerkmal } from "@core";
	import type { DataTableColumn } from "@ui";
	import { ref, computed, watch } from 'vue';

	const props = defineProps<SchuelerKAoAProps>();
	interface SelectedIDs {
		idSchuljahresabschnitt: number | null;
		idKategorie: number | null;
		idMerkmal: number | null;
		idZusatzmerkmal: number | null;
		idEbene4: number | null;
		idAnschlussoption: number | null;
		idBerufsfeld: number | null;
		bemerkung: string | null;
	}
	const selectedIDs = ref<SelectedIDs>({
		idSchuljahresabschnitt: null,
		idKategorie: null,
		idMerkmal: null,
		idZusatzmerkmal: null,
		idEbene4: null,
		idAnschlussoption: null,
		idBerufsfeld: null,
		bemerkung: null,
	});
	const selectedSchuljahresabschnitt = computed({
		get: () => selectedIDs.value.idSchuljahresabschnitt !== null ? props.schuelerKaoaManager().schuljahresabschnitte.get(selectedIDs.value.idSchuljahresabschnitt) : props.schuelerKaoaManager().schuljahresabschnitte.get(props.auswahl().idSchuljahresabschnitt),
		set: (val: Schuljahresabschnitt | null) => {
			resetMandatoryAndOptionalSelectedIDs();
			selectedIDs.value.idSchuljahresabschnitt = val ? val.id : null;
		}
	});
	const selectedKategorie = computed({
		get: () => selectedIDs.value.idKategorie !== null ? KAOAKategorie.data().getEintragByID(selectedIDs.value.idKategorie) : null,
		set: (val: KAOAKategorieKatalogEintrag | null) => {
			selectedIDs.value.idKategorie = val ? val.id : null;
			selectedIDs.value.idMerkmal = null;
			selectedIDs.value.idZusatzmerkmal = null;
			resetOptionalSelectedIDs();
		}
	});
	const selectedMerkmal = computed({
		get: () => selectedIDs.value.idMerkmal !== null ? KAOAMerkmal.data().getEintragByID(selectedIDs.value.idMerkmal) : null,
		set: (val: KAOAMerkmalKatalogEintrag | null) => {
			selectedIDs.value.idMerkmal = val ? val.id : null;
			selectedIDs.value.idZusatzmerkmal = null;
			resetOptionalSelectedIDs();
		}
	});
	const selectedZusatzmerkmal = computed({
		get: () => selectedIDs.value.idZusatzmerkmal !== null ? KAOAZusatzmerkmal.data().getEintragByID(selectedIDs.value.idZusatzmerkmal) : null,
		set: (val: KAOAZusatzmerkmalKatalogEintrag | null) => {
			selectedIDs.value.idZusatzmerkmal = val ? val.id : null;
			resetOptionalSelectedIDs();
		}
	});
	const selectedEbene4 = computed({
		get: () => selectedIDs.value.idEbene4 !== null ? KAOAEbene4.data().getEintragByID(selectedIDs.value.idEbene4) : null,
		set: (val: KAOAEbene4KatalogEintrag | null) => selectedIDs.value.idEbene4 = val ? val.id : null
	});
	const selectedAnschlussoption = computed({
		get: () => selectedIDs.value.idAnschlussoption !== null ? KAOAAnschlussoptionen.data().getEintragByID(selectedIDs.value.idAnschlussoption) : null,
		set: (val: KAOAAnschlussoptionenKatalogEintrag | null) => selectedIDs.value.idAnschlussoption = val ? val.id : null
	});
	const selectedBerufsfeld = computed({
		get: () => selectedIDs.value.idBerufsfeld !== null ? KAOABerufsfeld.data().getEintragByID(selectedIDs.value.idBerufsfeld) : null,
		set: (val: KAOABerufsfeldKatalogEintrag | null) => selectedIDs.value.idBerufsfeld = val ? val.id : null
	});
	const selectedBemerkung = computed({
		get: () => selectedIDs.value.bemerkung,
		set: (val: string | null) => selectedIDs.value.bemerkung = val
	});
	const selectedEntry = ref<SchuelerKAoADaten | null>();

	const optionsart = computed(() => selectedZusatzmerkmal.value?.optionsart);

	const showEbene4 = computed(() => (selectedZusatzmerkmal.value !== null) && (optionsart.value === 'SBO_EBENE_4'));
	const showAnschlussoption = computed(() => (selectedZusatzmerkmal.value !== null) && (optionsart.value === 'ANSCHLUSSOPTION'));
	const showBerufsfeld = computed(() => (selectedZusatzmerkmal.value !== null) && (optionsart.value === 'BERUFSFELD'));
	const showFreitext = computed(() => (selectedZusatzmerkmal.value !== null) && ((optionsart.value === 'FREITEXT') || (optionsart.value === 'FREITEXT_BERUF')));

	const selectedJahrgang = computed (() => Jahrgaenge.data().getWertByKuerzel(props.schuelerKaoaManager().getKuerzelJahrgangBySchuljahr(schuljahr.value))?.daten(schuljahr.value));
	const schuljahr = computed(() => selectedSchuljahresabschnitt.value?.schuljahr);
	const itemText = computed(() => (i: CoreTypeData) => i.kuerzel + '- ' + i.text);
	const schuljahresabschnittText = (item: Schuljahresabschnitt) => item.schuljahr > 0 ? `${item.schuljahr}/${(item.schuljahr + 1) % 100}.${item.abschnitt}` : "Abschnitt";
	type ModeType = 'add' | 'patch' | 'default';
	const Mode = Object.freeze({ADD: 'add' as ModeType, PATCH: 'patch' as ModeType, DEFAULT: 'default' as ModeType});
	const currentMode = ref(Mode.DEFAULT);
	const setMode = (newMode: ModeType) => currentMode.value = newMode;

	function validateRequiredFieldsFilled() {
		if (!selectedKategorie.value || !selectedMerkmal.value || !selectedZusatzmerkmal.value || optionsart.value === null)
			return false;
		switch(optionsart.value){
			case 'SBO_EBENE_4':
				return !!selectedEbene4.value;
			case 'ANSCHLUSSOPTION':
				return !!selectedAnschlussoption.value;
			case 'BERUFSFELD':
				return !!selectedBerufsfeld.value;
			case 'FREITEXT':
			case 'FREITEXT_BERUF':
				return (selectedBemerkung.value !== null) && (selectedBemerkung.value.trim() !== '');
			case 'KEINE':
				return true;
			default:
				return false;
		}
	}

	async function addEntry(){
		if (!validateRequiredFieldsFilled())
			return;
		const data: Partial<SchuelerKAoADaten> = {
			idSchuljahresabschnitt: selectedSchuljahresabschnitt.value?.id,
			idJahrgang: selectedJahrgang.value?.id,
			idKategorie: selectedKategorie.value?.id,
			idMerkmal: selectedMerkmal.value?.id,
			idZusatzmerkmal: selectedZusatzmerkmal.value?.id,
			idBerufsfeld: selectedBerufsfeld.value?.id,
			idEbene4: selectedEbene4.value?.id,
			idAnschlussoption: selectedAnschlussoption.value?.id,
			bemerkung: selectedBemerkung.value,
		};
		const idSchueler = props.auswahl().id;
		await props.add(data, idSchueler);
		selectedEntry.value = null;
		resetMandatoryAndOptionalSelectedIDs();
		setMode(Mode.DEFAULT);
	}
	async function deleteEntry() {
		if (selectedEntry.value === null || selectedEntry.value === undefined)
			return;
		await props.delete(props.auswahl().id, selectedEntry.value.id);
		selectedEntry.value = null;
		setMode(Mode.DEFAULT);
	}
	async function patchSelectedIDs() {
		setMode(Mode.PATCH);
		if (!selectedEntry.value)
			return
		selectedIDs.value.idSchuljahresabschnitt = selectedEntry.value.idSchuljahresabschnitt;
		selectedIDs.value.idKategorie = selectedEntry.value.idKategorie;
		selectedIDs.value.idMerkmal = selectedEntry.value.idMerkmal;
		selectedIDs.value.idZusatzmerkmal = selectedEntry.value.idZusatzmerkmal;
		if (!selectedZusatzmerkmal.value)
			return;
		switch (selectedZusatzmerkmal.value.optionsart) {
			case 'SBO_EBENE_4': {
				resetOptionalSelectedIDs();
				selectedIDs.value.idEbene4 = selectedEntry.value.idEbene4 !== null ? selectedEntry.value.idEbene4 : null;
				break;
			}
			case 'ANSCHLUSSOPTION': {
				resetOptionalSelectedIDs();
				selectedIDs.value.idAnschlussoption = selectedEntry.value.idAnschlussoption !== null ?selectedEntry.value.idAnschlussoption : null;
				break;
			}
			case 'BERUFSFELD': {
				resetOptionalSelectedIDs();
				selectedIDs.value.idBerufsfeld = selectedEntry.value.idBerufsfeld !== null ? selectedEntry.value.idBerufsfeld : null;
				break;
			}
			case 'FREITEXT':
			case 'FREITEXT_BERUF': {
				resetOptionalSelectedIDs();
				selectedIDs.value.bemerkung = selectedEntry.value.bemerkung;
				break;
			}
			case 'KEINE' : {
				resetOptionalSelectedIDs();
				break;
			}
		}
	}
	async function patchEntry() {
		if (!validateRequiredFieldsFilled() || !selectedEntry.value)
			return false;
		const data: Partial<SchuelerKAoADaten> = {
			idSchuljahresabschnitt: selectedSchuljahresabschnitt.value?.id,
			idKategorie: selectedKategorie.value?.id,
			idMerkmal: selectedMerkmal.value?.id,
			idZusatzmerkmal: selectedZusatzmerkmal.value?.id,
			idBerufsfeld: selectedIDs.value.idBerufsfeld !== null ? selectedBerufsfeld.value?.id : null,
			idEbene4: selectedIDs.value.idEbene4 !== null ? selectedEbene4.value?.id : null,
			idAnschlussoption: selectedIDs.value.idAnschlussoption !== null ? selectedAnschlussoption.value?.id : null,
			bemerkung: selectedBemerkung.value,
		};
		await props.patch(data, selectedEntry.value.id);
		setMode(Mode.DEFAULT);
	}
	function resetMandatoryAndOptionalSelectedIDs() {
		selectedIDs.value.idSchuljahresabschnitt = null;
		selectedIDs.value.idKategorie = null;
		selectedIDs.value.idMerkmal = null;
		selectedIDs.value.idZusatzmerkmal = null;
		resetOptionalSelectedIDs();
	}
	function resetOptionalSelectedIDs(){
		selectedIDs.value.idEbene4 = null;
		selectedIDs.value.idAnschlussoption = null;
		selectedIDs.value.idBerufsfeld = null;
		selectedIDs.value.bemerkung = null;
	}
	function selectEntry(item: SchuelerKAoADaten) {
		selectedEntry.value = item;
		resetMandatoryAndOptionalSelectedIDs();
		setMode(Mode.DEFAULT);
	}
	watch(props, () => {
		resetMandatoryAndOptionalSelectedIDs();
		selectedEntry.value = null;
		setMode(Mode.DEFAULT);
	});
	function getJahrgangFullName(idJahrgang: number) {
		const jahrgangEintrag = Jahrgaenge.data().getEintragByID(idJahrgang);
		return jahrgangEintrag?.kuerzel;
	}
	function getKategorieFullName(idKategorie: number) {
		const kategorieEintrag = KAOAKategorie.data().getEintragByID(idKategorie);
		return kategorieEintrag?.kuerzel + " " + kategorieEintrag?.text;
	}
	function getMerkmalFullName(idMerkmal: number) {
		const merkmalEintrag = KAOAMerkmal.data().getEintragByID(idMerkmal);
		return merkmalEintrag?.kuerzel + " " + merkmalEintrag?.text;
	}
	function getZusatzmerkmalFullName(idZusatzmerkmal: number) {
		const zusatzmerkmalEintrag = KAOAZusatzmerkmal.data().getEintragByID(idZusatzmerkmal);
		return zusatzmerkmalEintrag?.kuerzel + " " + zusatzmerkmalEintrag?.text;
	}
	const cols: DataTableColumn[] = [
		{ key: "id", label: "ID", sortable: true, fixedWidth: 5, align: "center" },
		{ key: "idJahrgang", label: "Jahrgang", sortable: true, align: "center" },
		{ key: "idKategorie", label: "Kategorie", sortable: true, align: "center" },
		{ key: "idMerkmal", label: "Merkmal", sortable: true, align: "center" },
		{ key: "idZusatzmerkmal", label: "Zusatzmerkmal", sortable: true, align: "center" },
	];
</script>

<style scoped>
.button-container {
	@apply mt-5 flex justify-between gap-5;
}

button {
	@apply px-3 py-3 cursor-pointer;
}
</style>
