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
	import { computed, ComputedRef, watch, WritableComputedRef } from "vue";
	import { KatalogEintrag, List, SchuelerStatus } from "@svws-nrw/svws-core-ts";
	import { injectMainApp, Main } from "~/apps/Main";
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";
	import { DataKatalogFahrschuelerarten } from "~/apps/schueler/DataKatalogFahrschuelerarten";
	import { useInputWithBaseData } from '../../../utils/composables/inputs';

	
	const props = defineProps<{ stammdaten: DataSchuelerStammdaten, fachschuelerarten: DataKatalogFahrschuelerarten }>();


	const main: Main = injectMainApp();
	const app = main.apps.schueler;

	const inputBinding = useInputWithBaseData(props.stammdaten)
	
	const inputStatus: WritableComputedRef<SchuelerStatus | undefined> = inputBinding('status', {
		get: (v: string): SchuelerStatus | undefined => v ? SchuelerStatus.fromBezeichnung(v) ?? undefined : undefined,
		set: (v: SchuelerStatus | undefined): string | undefined => v?.bezeichnung as string | undefined
	})
	watch(inputStatus, () => { app.auswahl.filter = app.auswahl.filter})
	
	const inputKatalogFahrschuelerarten: ComputedRef<KatalogEintrag[]> = computed(() =>  props.fachschuelerarten.daten?.toArray() as KatalogEintrag[] || [] as KatalogEintrag[]);
	const inputFahrschuelerArtID: WritableComputedRef<KatalogEintrag | undefined> = inputBinding('fahrschuelerArtID', {
		get: (v: number): KatalogEintrag | undefined => {
			for (const art of inputKatalogFahrschuelerarten.value)
			if (art.id === v)
			return art
		},
		set: (v: KatalogEintrag | undefined): number | undefined => v?.id
	})

	const inputKatalogHaltestellen: ComputedRef<List<KatalogEintrag>> = computed(() => main.kataloge.haltestellen);
	const inputHaltestelleID: WritableComputedRef<KatalogEintrag | undefined> = inputBinding('haltestelleID', {
		get: (v: number | undefined) => {
			for (const r of inputKatalogHaltestellen.value) 
				if (r.id === v)
					return r;
		},
		set: (v: KatalogEintrag | undefined) => v?.id
	})

	const inputAnmeldedatum: WritableComputedRef<string | undefined> = inputBinding('anmeldedatum')
	const inputAufnahmedatum: WritableComputedRef<string | undefined> = inputBinding('anmeldedatum')
	const inputIstDuplikat: WritableComputedRef<boolean | undefined> = inputBinding('istDuplikat')
	const inputIstSchulpflichtErfuellt: WritableComputedRef<boolean | undefined> = inputBinding('istSchulpflichtErfuellt')
	const inputHatMasernimpfnachweis: WritableComputedRef<boolean | undefined> = inputBinding('hatMasernimpfnachweis')
	const inputKeineAuskunftAnDritte: WritableComputedRef<boolean | undefined> = inputBinding('keineAuskunftAnDritte')
	const inputErhaeltSchuelerBAFOEG: WritableComputedRef<boolean | undefined> = inputBinding('erhaeltSchuelerBAFOEG')
	const inputIstBerufsschulpflichtErfuellt: WritableComputedRef<boolean | undefined> = inputBinding('istBerufsschulpflichtErfuellt')
	const inputIstVolljaehrig: WritableComputedRef<boolean | undefined> = inputBinding('istVolljaehrig')
</script>
