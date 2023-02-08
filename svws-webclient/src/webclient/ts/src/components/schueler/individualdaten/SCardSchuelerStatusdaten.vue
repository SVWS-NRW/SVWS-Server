<template>
	<svws-ui-content-card title="Statusdaten">
		<div class="input-wrapper">
			<svws-ui-multi-select title="Status" v-model="inputStatus" :items="SchuelerStatus.values()" :item-text="(i: SchuelerStatus) => i.bezeichnung" />
			<svws-ui-checkbox v-model="inputIstDuplikat">Ist Duplikat</svws-ui-checkbox>
			<svws-ui-multi-select title="Fahrschüler" v-model="inputFahrschuelerArtID" :items="mapFahrschuelerarten.values()" />
			<svws-ui-multi-select title="Haltestelle" v-model="inputHaltestelleID" :items="mapHaltestellen.values()" />
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

	import { computed, WritableComputedRef } from "vue";
	import { KatalogEintrag, SchuelerStammdaten, SchuelerStatus } from "@svws-nrw/svws-core-ts";

	const props = defineProps<{
		data: SchuelerStammdaten;
		mapFahrschuelerarten: Map<number, KatalogEintrag>;
		mapHaltestellen: Map<number, KatalogEintrag>
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<SchuelerStammdaten>): void;
	}>()

	function doPatch(data: Partial<SchuelerStammdaten>) {
		emit('patch', data);
	}

	const inputStatus: WritableComputedRef<SchuelerStatus | undefined> = computed({
		get: () => (SchuelerStatus.fromBezeichnung(props.data.status) || undefined),
		set: (value) => doPatch({ status: value?.bezeichnung })
	});

	const inputFahrschuelerArtID: WritableComputedRef<KatalogEintrag | undefined> = computed({
		get: () => props.data.fahrschuelerArtID === null ? undefined : props.mapFahrschuelerarten.get(props.data.fahrschuelerArtID),
		set: (value) => doPatch({ fahrschuelerArtID: value === undefined ? null : value.id })
	});

	const inputHaltestelleID: WritableComputedRef<KatalogEintrag | undefined> = computed({
		get: () => props.data.haltestelleID === null ? undefined : props.mapHaltestellen.get(props.data.haltestelleID),
		set: (value) => doPatch({ haltestelleID: value === undefined ? null : value.id })
	});

	const inputAnmeldedatum: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.anmeldedatum ?? undefined,
		set: (value) => doPatch({ anmeldedatum: value })
	});

	const inputAufnahmedatum: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.aufnahmedatum ?? undefined,
		set: (value) => doPatch({ aufnahmedatum: value })
	});

	const inputIstDuplikat: WritableComputedRef<boolean> = computed({
		get: () => props.data.istDuplikat,
		set: (value) => doPatch({ istDuplikat: value })
	});

	const inputIstSchulpflichtErfuellt: WritableComputedRef<boolean> = computed({
		get: () => props.data.istSchulpflichtErfuellt === null ? false : props.data.istSchulpflichtErfuellt,
		set: (value) => doPatch({ istSchulpflichtErfuellt: value })
	});

	const inputHatMasernimpfnachweis: WritableComputedRef<boolean> = computed({
		get: () => props.data.hatMasernimpfnachweis,
		set: (value) => doPatch({ hatMasernimpfnachweis: value })
	});

	const inputKeineAuskunftAnDritte: WritableComputedRef<boolean> = computed({
		get: () => props.data.keineAuskunftAnDritte,
		set: (value) => doPatch({ keineAuskunftAnDritte: value })
	});

	const inputErhaeltSchuelerBAFOEG: WritableComputedRef<boolean> = computed({
		get: () => props.data.erhaeltSchuelerBAFOEG,
		set: (value) => doPatch({ erhaeltSchuelerBAFOEG: value })
	});

	const inputIstBerufsschulpflichtErfuellt: WritableComputedRef<boolean> = computed({
		get: () => props.data.istBerufsschulpflichtErfuellt === null ? false : props.data.istBerufsschulpflichtErfuellt,
		set: (value) => doPatch({ istBerufsschulpflichtErfuellt: value })
	});

	const inputIstVolljaehrig: WritableComputedRef<boolean> = computed({
		get: () => props.data.istVolljaehrig === null ? false : props.data.istVolljaehrig,
		set: (value) => doPatch({ istVolljaehrig: value })
	});

</script>
