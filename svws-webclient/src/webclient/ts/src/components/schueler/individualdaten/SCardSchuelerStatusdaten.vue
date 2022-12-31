<template>
	<svws-ui-content-card title="Statusdaten">
		<div class="input-wrapper">
			<svws-ui-multi-select title="Status" v-model="inputStatus" :items="SchuelerStatus.values()" :item-text="(i: SchuelerStatus) => i.bezeichnung" />
			<svws-ui-checkbox v-model="inputIstDuplikat">Ist Duplikat</svws-ui-checkbox>
			<svws-ui-multi-select title="Fahrschüler" v-model="inputFahrschuelerArtID" :items="inputKatalogFahrschuelerarten" />
			<svws-ui-multi-select title="Haltestelle" v-model="inputHaltestelleID" :items="inputKatalogHaltestellen" />
			<svws-ui-text-input placeholder="Anmeldedatum" v-model="inputAnmeldedatum" type="date" />
			<svws-ui-text-input placeholder="Aufnahmedatum" v-model="inputAufnahmedatum" type="date" />
			<svws-ui-checkbox v-model="inputIstVolljaehrig"> Volljährig </svws-ui-checkbox>
			<svws-ui-checkbox v-model="inputKeineAuskunftAnDritte"> Keine Auskunft an Dritte </svws-ui-checkbox>
			<svws-ui-checkbox v-model="inputIstSchulpflichtErfuellt"> Schulpflicht erfüllt </svws-ui-checkbox>
			<svws-ui-checkbox v-model="inputIstBerufsschulpflichtErfuellt"> Schulpflicht SII erfüllt </svws-ui-checkbox>
			<svws-ui-checkbox v-model="inputHatMasernimpfnachweis"> Masern Impfnachweis </svws-ui-checkbox>
			<svws-ui-checkbox v-model="inputErhaeltSchuelerBAFOEG">BAFöG</svws-ui-checkbox>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { KatalogEintrag, List, SchuelerStammdaten, SchuelerStatus } from "@svws-nrw/svws-core-ts";
	import { injectMainApp, Main } from "~/apps/Main";
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";
	import { DataKatalogFahrschuelerarten } from "~/apps/schueler/DataKatalogFahrschuelerarten";

	const props = defineProps<{ stammdaten: DataSchuelerStammdaten, fachschuelerarten: DataKatalogFahrschuelerarten }>();

	const daten: ComputedRef<SchuelerStammdaten> = computed(() => props.stammdaten.daten || new SchuelerStammdaten());


	const main: Main = injectMainApp();
	const app = main.apps.schueler;

	const inputStatus: WritableComputedRef<SchuelerStatus | undefined> = computed({
		get: () => (SchuelerStatus.fromBezeichnung(daten.value.status) || undefined),
		set: (value) => {
			props.stammdaten.patch({ status: value?.bezeichnung });
			app.auswahl.filter = app.auswahl.filter
		}
	});

	const inputKatalogFahrschuelerarten: ComputedRef<KatalogEintrag[]> = computed(() => props.fachschuelerarten.daten?.toArray() as KatalogEintrag[] || []);
	const inputFahrschuelerArtID: WritableComputedRef<KatalogEintrag | undefined> = computed({
		get: () => inputKatalogFahrschuelerarten.value.find(n => n.id === daten.value.fahrschuelerArtID),
		set: (value) => props.stammdaten.patch({ fahrschuelerArtID: value?.id })
	});

	const inputKatalogHaltestellen: ComputedRef<KatalogEintrag[]> = computed(() => main.kataloge.haltestellen.toArray() as KatalogEintrag[]);
	const inputHaltestelleID: WritableComputedRef<KatalogEintrag | undefined> = computed({
		get: () => inputKatalogHaltestellen.value.find(n => n.id === daten.value.haltestelleID),
		set: (value) => props.stammdaten.patch({ haltestelleID: value?.id })
	});

	const inputAnmeldedatum: WritableComputedRef<string | undefined> = computed({
		get: () => daten.value.anmeldedatum?.toString(),
		set: (value) => props.stammdaten.patch({ anmeldedatum: value })
	});

	const inputAufnahmedatum: WritableComputedRef<string | undefined> = computed({
		get: () => daten.value.aufnahmedatum?.toString(),
		set: (value) => props.stammdaten.patch({ aufnahmedatum: value })
	});

	const inputIstDuplikat: WritableComputedRef<boolean> = computed({
		get: () => daten.value.istDuplikat,
		set: (value) => props.stammdaten.patch({ istDuplikat: value })
	});

	const inputIstSchulpflichtErfuellt: WritableComputedRef<boolean | undefined> = computed({
		get: () => daten.value.istSchulpflichtErfuellt?.valueOf(),
		set: (value) => props.stammdaten.patch({ istSchulpflichtErfuellt: value })
	});

	const inputHatMasernimpfnachweis: WritableComputedRef<boolean> = computed({
		get: () => daten.value.hatMasernimpfnachweis,
		set: (value) => props.stammdaten.patch({ hatMasernimpfnachweis: value })
	});

	const inputKeineAuskunftAnDritte: WritableComputedRef<boolean> = computed({
		get: () => daten.value.keineAuskunftAnDritte,
		set: (value) => props.stammdaten.patch({ keineAuskunftAnDritte: value })
	});

	const inputErhaeltSchuelerBAFOEG: WritableComputedRef<boolean> = computed({
		get: () => daten.value.erhaeltSchuelerBAFOEG,
		set: (value) => props.stammdaten.patch({ erhaeltSchuelerBAFOEG: value })
	});

	const inputIstBerufsschulpflichtErfuellt: WritableComputedRef<boolean | undefined> = computed({
		get: () => daten.value.istBerufsschulpflichtErfuellt?.valueOf(),
		set: (value) => props.stammdaten.patch({ istBerufsschulpflichtErfuellt: value })
	});

	const inputIstVolljaehrig: WritableComputedRef<boolean | undefined> = computed({
		get: () => daten.value.istVolljaehrig?.valueOf(),
		set: (value) => props.stammdaten.patch({ istVolljaehrig: value })
	});

</script>
