<template>
	<svws-ui-content-card title="Statusdaten">
		<div class="input-wrapper">
			<svws-ui-multi-select title="Status" v-model="inputStatus" :items="SchuelerStatus.values()" :item-text="(i: SchuelerStatus) => i.bezeichnung" />
			<svws-ui-checkbox v-model="inputIstDuplikat">Ist Duplikat</svws-ui-checkbox>
			<svws-ui-multi-select title="Fahrschüler" v-model="inputFahrschuelerArtID" :items="inputKatalogFahrschuelerarten" />
			<svws-ui-multi-select title="Haltestelle" v-model="inputHaltestelleID" :items="haltestellen" />
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
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";
	import { DataKatalogFahrschuelerarten } from "~/apps/schueler/DataKatalogFahrschuelerarten";
	import { injectMainApp, Main } from "~/apps/Main";

	const props = defineProps<{
		stammdaten: DataSchuelerStammdaten;
		fachschuelerarten: DataKatalogFahrschuelerarten;
		haltestellen: List<KatalogEintrag>
	}>();

	const main: Main = injectMainApp();

	const daten: ComputedRef<SchuelerStammdaten> = computed(() => props.stammdaten.daten || new SchuelerStammdaten());

	const inputStatus: WritableComputedRef<SchuelerStatus | undefined> = computed({
		get: () => (SchuelerStatus.fromBezeichnung(daten.value.status) || undefined),
		set: (value) => void props.stammdaten.patch({ status: value?.bezeichnung })
	});

	const inputKatalogFahrschuelerarten: ComputedRef<KatalogEintrag[]> = computed(() => props.fachschuelerarten.daten?.toArray() as KatalogEintrag[] || []);
	const inputFahrschuelerArtID: WritableComputedRef<KatalogEintrag | undefined> = computed({
		get: () => inputKatalogFahrschuelerarten.value.find(n => n.id === daten.value.fahrschuelerArtID),
		set: (value) => void props.stammdaten.patch({ fahrschuelerArtID: value?.id })
	});

	const inputHaltestelleID: WritableComputedRef<KatalogEintrag | undefined> = computed({
		get: () => {
			for (const haltestelle of props.haltestellen)
				if (haltestelle.id === daten.value.haltestelleID)
					return haltestelle;
			return undefined;
		},
		set: (value) => void props.stammdaten.patch({ haltestelleID: value?.id })
	});

	const inputAnmeldedatum: WritableComputedRef<string | undefined> = computed({
		get: () => daten.value.anmeldedatum ?? undefined,
		set: (value) => void props.stammdaten.patch({ anmeldedatum: value })
	});

	const inputAufnahmedatum: WritableComputedRef<string | undefined> = computed({
		get: () => daten.value.aufnahmedatum ?? undefined,
		set: (value) => void props.stammdaten.patch({ aufnahmedatum: value })
	});

	const inputIstDuplikat: WritableComputedRef<boolean> = computed({
		get: () => daten.value.istDuplikat,
		set: (value) => void props.stammdaten.patch({ istDuplikat: value })
	});

	const inputIstSchulpflichtErfuellt: WritableComputedRef<boolean> = computed({
		get: () => daten.value.istSchulpflichtErfuellt === null ? false : daten.value.istSchulpflichtErfuellt,
		set: (value) => void props.stammdaten.patch({ istSchulpflichtErfuellt: value })
	});

	const inputHatMasernimpfnachweis: WritableComputedRef<boolean> = computed({
		get: () => daten.value.hatMasernimpfnachweis,
		set: (value) => void props.stammdaten.patch({ hatMasernimpfnachweis: value })
	});

	const inputKeineAuskunftAnDritte: WritableComputedRef<boolean> = computed({
		get: () => daten.value.keineAuskunftAnDritte,
		set: (value) => void props.stammdaten.patch({ keineAuskunftAnDritte: value })
	});

	const inputErhaeltSchuelerBAFOEG: WritableComputedRef<boolean> = computed({
		get: () => daten.value.erhaeltSchuelerBAFOEG,
		set: (value) => void props.stammdaten.patch({ erhaeltSchuelerBAFOEG: value })
	});

	const inputIstBerufsschulpflichtErfuellt: WritableComputedRef<boolean> = computed({
		get: () => daten.value.istBerufsschulpflichtErfuellt === null ? false : daten.value.istBerufsschulpflichtErfuellt,
		set: (value) => void props.stammdaten.patch({ istBerufsschulpflichtErfuellt: value })
	});

	const inputIstVolljaehrig: WritableComputedRef<boolean> = computed({
		get: () => daten.value.istVolljaehrig === null ? false : daten.value.istVolljaehrig,
		set: (value) => void props.stammdaten.patch({ istVolljaehrig: value })
	});

</script>
