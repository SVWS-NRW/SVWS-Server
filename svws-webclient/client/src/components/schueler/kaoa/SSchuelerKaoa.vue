<!-- eslint-disable @typescript-eslint/consistent-type-imports -->
<template>
	<div class="page--content">
		<svws-ui-table :columns="cols" :items="props.schuelerKaoaManager().getSchuelerKAoADatenAuswahl()" class="col-span-full" clickable :clicked="selectedEntry" @update:clicked="item => selectedEntry = item">
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
		<svws-ui-button title="DeleteButton" v-if="selectedEntry" @click="deleteEntry">
			<span class="icon i-ri-delete-bin-line" />
		</svws-ui-button>
		<br>
		<div>
			<svws-ui-select title="KAoAKategorie" :items="props.schuelerKaoaManager().getKAOAKategorienByJahrgangAuswahl(jahrgangAuswahl)" :item-text="itemText" v-model="selectedKategorie" />
			<svws-ui-select title="KAoAMerkmal" v-if="selectedKategorie" :items="props.schuelerKaoaManager().getKAOAMerkmaleByKategorie(selectedKategorie)" :item-text="itemText" v-model="selectedMerkmal" />
			<svws-ui-select title="KAoAZusatzmerkmal" v-if="selectedMerkmal" :items="props.schuelerKaoaManager().getKAOAZusatzmerkmaleByMerkmal(selectedMerkmal)" :item-text="itemText" v-model="selectedZusatzmerkmal" />
			<svws-ui-select title="KAoAEbene4" v-if="showEbene4" :items="props.schuelerKaoaManager().getKAOAEbene4ByZusatzmerkmal(selectedZusatzmerkmal)" :item-text="itemText" v-model="selectedEbene4" />
			<svws-ui-select title="KAoAAnschlussoption" v-if="selectedZusatzmerkmal && showAnschlussoption" :items="props.schuelerKaoaManager().getKAOAAnschlussoptionenByZusatzmerkmal(selectedZusatzmerkmal)" :item-text="itemText" v-model="selectedAnschlussoption" />
			<svws-ui-select title="KAoABerufsfeld" v-if="showBerufsfeld" :items="props.schuelerKaoaManager().getKAOABerufsfelder()" :item-text="itemText" v-model="selectedBerufsfeld" />
			<svws-ui-text-input title="Bemerkung" v-if="showFreitext" v-model="selectedBemerkung" placeholder="Freitext" type="text" />
			<svws-ui-button title="AddButton" v-if="validateRequiredFieldsFilled()" @click="add">
				<span class="icon i-ri-add-line" />
			</svws-ui-button>
		</div>
	</div>
</template>

<script setup lang="ts">
	import type { SchuelerKAoAProps } from './SSchuelerKaoaProps';
	import type {CoreTypeData, KAOAKategorieKatalogEintrag, KAOAMerkmalKatalogEintrag, KAOAZusatzmerkmalKatalogEintrag, SchuelerKAoADaten,
		KAOAEbene4KatalogEintrag, KAOAAnschlussoptionenKatalogEintrag, KAOABerufsfeldKatalogEintrag} from "@core";
	import { Jahrgaenge, KAOAKategorie, KAOAMerkmal, KAOAZusatzmerkmal } from "@core";
	import type { DataTableColumn } from "@ui";
	import { ref, computed, watch } from 'vue';

	const props = defineProps<SchuelerKAoAProps>();

	const selectedKategorie = ref<KAOAKategorieKatalogEintrag | null>(null);
	const selectedMerkmal = ref<KAOAMerkmalKatalogEintrag | null>(null);
	const selectedZusatzmerkmal = ref<KAOAZusatzmerkmalKatalogEintrag | null>(null);
	const selectedEbene4 = ref<KAOAEbene4KatalogEintrag | null>(null);
	const selectedAnschlussoption = ref<KAOAAnschlussoptionenKatalogEintrag | null>(null);
	const selectedBerufsfeld = ref<KAOABerufsfeldKatalogEintrag | null>(null);
	const selectedBemerkung = ref<string | null>(null)
	const selectedEntry = ref<SchuelerKAoADaten | null>()

	const schuljahr = computed(() => props.schuelerKaoaManager().getSchuljahr())
	const optionsart = computed(() => selectedZusatzmerkmal.value?.optionsart);

	const showEbene4 = computed(() => (selectedZusatzmerkmal.value !== null) && (optionsart.value === 'SBO_EBENE_4'));
	const showAnschlussoption = computed(() => (selectedZusatzmerkmal.value !== null) && (optionsart.value === 'ANSCHLUSSOPTION'));
	const showBerufsfeld = computed(() => (selectedZusatzmerkmal.value !== null) && (optionsart.value === 'BERUFSFELD'));
	const showFreitext = computed(() => (selectedZusatzmerkmal.value !== null) && ((optionsart.value === 'FREITEXT') || (optionsart.value === 'FREITEXT_BERUF')));

	const jahrgangAuswahl = computed (() => Jahrgaenge.data().getWertByKuerzel(props.auswahl().jahrgang)?.daten(schuljahr.value));

	const itemText = computed(() => (i: CoreTypeData) => i.kuerzel + '- ' + i.text);

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
				return (selectedBemerkung.value !== null) && (selectedBemerkung.value.trim() !== '');
			case 'FREITEXT_BERUF':
				return (selectedBemerkung.value !== null) && (selectedBemerkung.value.trim() !== '');
			case 'KEINE':
				return true;
			default:
				return false;
		}
	}
	async function add(){
		if (!validateRequiredFieldsFilled())
			return false //todo Fehlerbehandlung
		const data: Partial<SchuelerKAoADaten> = {
			idSchuljahresabschnitt: props.schuelerKaoaManager().getSchuljahresabschnittAuswahl()?.id,
			idJahrgang: jahrgangAuswahl.value?.id,
			idKategorie: selectedKategorie.value?.id,
			idMerkmal: selectedMerkmal.value?.id,
			idZusatzmerkmal: selectedZusatzmerkmal.value?.id,
			idBerufsfeld: selectedBerufsfeld.value?.id,
			idEbene4: selectedEbene4.value?.id,
			idAnschlussoption: selectedAnschlussoption.value?.id,
			bemerkung: selectedBemerkung.value,
		}
		const idSchueler = props.auswahl().id;
		await props.addKaoaDaten(data, idSchueler)
		resetFields();
	}
	async function deleteEntry() {
		if (selectedEntry.value === null || selectedEntry.value === undefined)
			return false //todo Fehlerbehandlung
		await props.deleteKaoaDaten(props.auswahl().id, selectedEntry.value.id)
		selectedEntry.value = null;
	}
	function resetFields() {
		selectedKategorie.value = null;
		selectedMerkmal.value = null;
		selectedZusatzmerkmal.value = null;
		selectedAnschlussoption.value = null;
		selectedBerufsfeld.value = null;
		selectedEbene4.value = null;
		selectedBemerkung.value = null;
	}
	watch(selectedKategorie, (value) => {
		if (value) {
			selectedMerkmal.value = null;
			selectedZusatzmerkmal.value = null;
			selectedAnschlussoption.value = null;
			selectedBerufsfeld.value = null;
			selectedEbene4.value = null;
			selectedBemerkung.value = null;
		}
	});
	watch(selectedMerkmal, (value) => {
		if (value) {
			selectedZusatzmerkmal.value = null;
			selectedAnschlussoption.value = null;
			selectedBerufsfeld.value = null;
			selectedEbene4.value = null;
			selectedBemerkung.value = null;
		}
	});
	watch(selectedZusatzmerkmal, (value) => {
		if (value) {
			selectedAnschlussoption.value = null;
			selectedBerufsfeld.value = null;
			selectedEbene4.value = null;
			selectedBemerkung.value = null;
		}
	});
	const cols: DataTableColumn[] = [
		{ key: "id", label: "ID", sortable: true, fixedWidth: 5, align: "center" },
		{ key: "idKategorie", label: "Kategorie", sortable: true, align: "center" },
		{ key: "idMerkmal", label: "Merkmal", sortable: true, align: "center" },
		{ key: "idZusatzmerkmal", label: "Zusatzmerkmal", sortable: true, align: "center" },
	];

	function getKategorieFullName(kategorieId: number) {
		const kategorieEintrag = KAOAKategorie.data().getEintragByID(kategorieId);
		return kategorieEintrag?.kuerzel + " " + kategorieEintrag?.text
	}

	function getMerkmalFullName(merkmalId: number) {
		const merkmalEintrag = KAOAMerkmal.data().getEintragByID(merkmalId);
		return merkmalEintrag?.kuerzel + " " + merkmalEintrag?.text
	}

	function getZusatzmerkmalFullName(zusatzmerkmalId: number) {
		const zusatzmerkmalEintrag = KAOAZusatzmerkmal.data().getEintragByID(zusatzmerkmalId);
		return zusatzmerkmalEintrag?.kuerzel + " " + zusatzmerkmalEintrag?.text
	}
</script>
