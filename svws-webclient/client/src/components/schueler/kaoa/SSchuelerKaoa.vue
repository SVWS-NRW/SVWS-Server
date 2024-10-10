<!-- eslint-disable @typescript-eslint/consistent-type-imports -->
<template>
	<div class="page--content">
		<svws-ui-table :columns="cols" :items="props.schuelerKaoaManager().getSchuelerKAoADatenAuswahl()" class="col-span-full">
			<template #cell(idKategorie)="{ value } : { value: number }">
				<span>
					{{ KAOAKategorie.data().getEintragByID(value)?.kuerzel || "" }}
					{{ KAOAKategorie.data().getEintragByID(value)?.text || "" }}
				</span>
			</template>
			<template #cell(idMerkmal)="{ value } : { value: number }">
				<span>
					{{ KAOAMerkmal.data().getEintragByID(value)?.kuerzel || "" }}
					{{ KAOAMerkmal.data().getEintragByID(value)?.text || "" }}
				</span>
			</template>
			<template #cell(idZusatzmerkmal)="{ value } : { value: number }">
				<span>
					{{ KAOAZusatzmerkmal.data().getEintragByID(value)?.kuerzel || "" }}
					{{ KAOAZusatzmerkmal.data().getEintragByID(value)?.text || "" }}
				</span>
			</template>
		</svws-ui-table>
		<br>
		<div>
			<svws-ui-select title="KAoAKategorie" :items="props.schuelerKaoaManager().getKAOAKategorien()" :item-text="itemText" v-model="selectedKategorie" />
			<svws-ui-select title="KAoAMerkmal" v-if="selectedKategorie" :items="props.schuelerKaoaManager().getKAOAMerkmaleByKategorie(kaoaKategorie)" :item-text="itemText" v-model="selectedMerkmal" />
			<svws-ui-select title="KAoAZusatzmerkmal" v-if="selectedMerkmal" :items="props.schuelerKaoaManager().getKAOAZusatzmerkmaleByMerkmal(kaoaMerkmal)" :item-text="itemText" v-model="selectedZusatzmerkmal" />
			<svws-ui-select title="KAoAEbene4" v-if="selectedZusatzmerkmal && optionsartIsEbene4" :items="props.schuelerKaoaManager().getKAOAEbene4ByZusatzmerkmal(kaoaZusatzmerkmal)" :item-text="itemText" v-model="selectedEbene4" />
			<svws-ui-select title="KAoAAnschlussoption" v-if="selectedZusatzmerkmal && optionsartIsAnschlussoption" :items="props.schuelerKaoaManager().getKAOAAnschlussoptionenByZusatzmerkmal(kaoaZusatzmerkmal)" :item-text="itemText" v-model="selectedAnschlussoption" />
			<svws-ui-select title="KAoABerufsfeld" v-if="selectedZusatzmerkmal && optionsartIsBerufsfeld" :items="props.schuelerKaoaManager().getKAOABerufsfelder()" :item-text="itemText" v-model="selectedBerufsfeld" />
			<svws-ui-text-input title="Bemerkung" v-if="selectedZusatzmerkmal && optionsartIsBemerkung" v-model="selectedBemerkung" placeholder="Freitext" type="text" />
			<svws-ui-button title="AddButton" v-if="validateRequiredFieldsFilled()" @click="add">
				<span class="icon i-ri-add-line" />
			</svws-ui-button>
		</div>
	</div>
</template>

<script setup lang="ts">
	import type { SchuelerKAoAProps } from './SSchuelerKaoaProps';
	import type { SchuelerKAoADaten } from "@core";
	import { Jahrgaenge, KAOAAnschlussoptionen, KAOABerufsfeld, KAOAEbene4, KAOAKategorie, KAOAMerkmal, KAOAZusatzmerkmal } from "@core";
	import type { DataTableColumn } from "@ui";
	import { ref, computed, watch } from 'vue';


	const props = defineProps<SchuelerKAoAProps>();
	const selectedKategorie = ref<KAOAKategorie | null>(null);
	const selectedMerkmal = ref<KAOAMerkmal | null>(null);
	const selectedZusatzmerkmal = ref<KAOAZusatzmerkmal | null>(null);
	const selectedEbene4 = ref<KAOAEbene4 | null>(null);
	const selectedAnschlussoption = ref<KAOAAnschlussoptionen | null>(null);
	const selectedBerufsfeld = ref<KAOABerufsfeld | null>(null);
	const selectedBemerkung = ref<string | null>(null)
	const schuljahr = computed(() => props.schuelerKaoaManager().getSchuljahr())
	const optionsart = computed(() => (kaoaZusatzmerkmal.value?.daten(schuljahr.value)) ? kaoaZusatzmerkmal.value.daten(schuljahr.value)?.optionsart : null);
	const optionsartIsEbene4 = computed(() => optionsart.value === 'SBO_EBENE_4');
	const optionsartIsAnschlussoption = computed(() => optionsart.value === 'ANSCHLUSSOPTION');
	const optionsartIsBerufsfeld = computed(() => optionsart.value === 'BERUFSFELD');
	const optionsartIsBemerkung = computed(() => (optionsart.value === 'FREITEXT') || (optionsart.value === 'FREITEXT_BERUF'));
	const kaoaKategorie = computed(() => (selectedKategorie.value?.name) ? KAOAKategorie.data().getWertByBezeichner(selectedKategorie.value.name()) : null);
	const kaoaMerkmal = computed(() => (selectedMerkmal.value?.name) ? KAOAMerkmal.data().getWertByBezeichner(selectedMerkmal.value.name()) : null);
	const kaoaZusatzmerkmal = computed(() => (selectedZusatzmerkmal.value?.name) ? KAOAZusatzmerkmal.data().getWertByBezeichner(selectedZusatzmerkmal.value.name()) : null);
	const kaoaBerufsfeld = computed(() => (selectedBerufsfeld.value?.name) ? KAOABerufsfeld.data().getWertByBezeichner(selectedBerufsfeld.value.name()) : null);
	const kaoaEbene4 = computed(() => (selectedEbene4.value?.name) ? KAOAEbene4.data().getWertByBezeichner(selectedEbene4.value.name()) : null);
	const kaoaAnschlussoption = computed(() => (selectedAnschlussoption.value?.name) ? KAOAAnschlussoptionen.data().getWertByBezeichner(selectedAnschlussoption.value.name()) : null);
	const jahrgangAuswahl = computed (() => Jahrgaenge.data().getWertByKuerzel(props.auswahl().jahrgang));
	const itemText = computed(() => (i: { daten: (schuljahr: number) => any }) => i.daten(schuljahr.value).kuerzel + '- ' + i.daten(schuljahr.value).text);
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
		if (jahrgangAuswahl.value === null)
			return false //todo Fehlerbehandlung
		const data: Partial<SchuelerKAoADaten> = {
			idSchuljahresabschnitt: props.schuelerKaoaManager().getSchuljahresabschnittAuswahl()?.id,
			idJahrgang: Jahrgaenge.data().getEintragBySchuljahrUndWert(schuljahr.value, jahrgangAuswahl.value)?.id,
			idKategorie: kaoaKategorie.value?.daten(schuljahr.value)?.id,
			idMerkmal: kaoaMerkmal.value?.daten(schuljahr.value)?.id,
			idZusatzmerkmal: kaoaZusatzmerkmal.value?.daten(schuljahr.value)?.id,
			idBerufsfeld: optionsart.value === 'BERUFSFELD' ? kaoaBerufsfeld.value?.daten(schuljahr.value)?.id : null,
			idEbene4: optionsart.value === 'SBO_EBENE_4' ? kaoaEbene4.value?.daten(schuljahr.value)?.id : null,
			idAnschlussoption: optionsart.value === 'ANSCHLUSSOPTION' ? kaoaAnschlussoption.value?.daten(schuljahr.value)?.id : null,
			bemerkung: (optionsart.value === 'FREITEXT' || optionsart.value === 'FREITEXT_BERUF') ? selectedBemerkung.value : null,
		}
		const idSchueler = props.auswahl().id;
		await props.addKaoaDaten(data, idSchueler)
		resetFields();
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
</script>
