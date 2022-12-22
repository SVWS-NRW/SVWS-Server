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

	const props = defineProps<{ stammdaten: DataSchuelerStammdaten }>();

	const daten: ComputedRef<SchuelerStammdaten> = computed(() => props.stammdaten.daten || new SchuelerStammdaten());


	const main: Main = injectMainApp();
	const app = main.apps.schueler;

	const inputKatalogFahrschuelerarten: ComputedRef<List<KatalogEintrag>> = computed(() =>  app.katalogFahrschuelerarten);

	const inputKatalogHaltestellen: ComputedRef<List<KatalogEintrag>> = computed(() => main.kataloge.haltestellen);

	const inputStatus: WritableComputedRef<SchuelerStatus | undefined> = computed({
		//TODO id verwenden
		get(): SchuelerStatus | undefined {
			return (SchuelerStatus.fromBezeichnung(daten.value.status) || undefined);
		},
		set(val: SchuelerStatus | undefined): void {
			props.stammdaten.patch({ status: val?.bezeichnung });
			app.auswahl.filter = app.auswahl.filter
		}
	});

	const inputFahrschuelerArtID: WritableComputedRef<KatalogEintrag | undefined> = computed({
		get(): KatalogEintrag | undefined {
			const id = daten.value.fahrschuelerArtID;
			for (const art of inputKatalogFahrschuelerarten.value)
				if (art.id === id)
					return art
		},
		set(val) {
			props.stammdaten.patch({ fahrschuelerArtID: val?.id });
		}
	});

	const inputHaltestelleID: WritableComputedRef<KatalogEintrag | undefined> = computed({
		get(): KatalogEintrag | undefined {
			const id = daten.value.haltestelleID;
			for (const r of inputKatalogHaltestellen.value) 
				if (r.id === id)
					return r;
		},
		set(val) {
			props.stammdaten.patch({ haltestelleID: val?.id });
		}
	});

	const inputAnmeldedatum: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return daten.value.anmeldedatum?.toString();
		},
		set(val) {
			props.stammdaten.patch({ anmeldedatum: val });
		}
	});

	const inputAufnahmedatum: WritableComputedRef<string | undefined> = computed({
		get(): string | undefined {
			return daten.value.aufnahmedatum?.toString();
		},
		set(val) {
			props.stammdaten.patch({ aufnahmedatum: val });
		}
	});

	const inputIstDuplikat: WritableComputedRef<boolean> = computed({
		get(): boolean {
			return daten.value.istDuplikat;
		},
		set(val) {
			props.stammdaten.patch({ istDuplikat: val });
		}
	});

	const inputIstSchulpflichtErfuellt: WritableComputedRef<boolean | undefined> = computed({
		get(): boolean | undefined {
			return daten.value.istSchulpflichtErfuellt?.valueOf();
		},
		set(val: boolean | undefined) {
			props.stammdaten.patch({ istSchulpflichtErfuellt: val });
		}
	});

	const inputHatMasernimpfnachweis: WritableComputedRef<boolean> = computed({
		get(): boolean {
			return daten.value.hatMasernimpfnachweis;
		},
		set(val) {
			props.stammdaten.patch({ hatMasernimpfnachweis: val });
		}
	});

	const inputKeineAuskunftAnDritte: WritableComputedRef<boolean> = computed({
		get(): boolean {
			return daten.value.keineAuskunftAnDritte;
		},
		set(val) {
			props.stammdaten.patch({ keineAuskunftAnDritte: val });
		}
	});

	const inputErhaeltSchuelerBAFOEG: WritableComputedRef<boolean> = computed({
		get(): boolean {
			return daten.value.erhaeltSchuelerBAFOEG;
		},
		set(val) {
			props.stammdaten.patch({ erhaeltSchuelerBAFOEG: val });
		}
	});

	const inputIstBerufsschulpflichtErfuellt: WritableComputedRef<boolean | undefined> = computed({
		get(): boolean | undefined {
			return daten.value.istBerufsschulpflichtErfuellt?.valueOf();
		},
		set(val) {
			props.stammdaten.patch({ istBerufsschulpflichtErfuellt: val });
		}
	});

	const inputIstVolljaehrig: WritableComputedRef<boolean | undefined> = computed({
		get(): boolean | undefined {
			return daten.value.istVolljaehrig?.valueOf();
		},
		set(val) {
			props.stammdaten.patch({ istVolljaehrig: val });
		}
	});

</script>
