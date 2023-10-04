<template>
	<svws-ui-content-card title="Statusdaten">
		<template #actions>
			<svws-ui-checkbox v-model="inputIstDuplikat">Ist Duplikat</svws-ui-checkbox>
		</template>
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-select title="Status" v-model="inputStatus" :items="SchuelerStatus.values()" :item-text="(i: SchuelerStatus) => i.bezeichnung" statistics />
			<div />
			<svws-ui-select title="Fahrschüler" v-model="inputFahrschuelerArtID" :items="mapFahrschuelerarten" :item-text="i=>i.text ?? ''" />
			<svws-ui-select title="Haltestelle" v-model="inputHaltestelleID" :items="mapHaltestellen" :item-text="i=>i.text ?? ''" />
			<svws-ui-text-input placeholder="Anmeldedatum" :model-value="data().anmeldedatum" @change="anmeldedatum=>doPatch({anmeldedatum})" type="date" />
			<svws-ui-text-input placeholder="Aufnahmedatum" :model-value="data().aufnahmedatum" @change="aufnahmedatum=>doPatch({aufnahmedatum})" type="date" statistics />
			<svws-ui-spacing />
			<svws-ui-input-wrapper :grid="2" class="input-wrapper--checkboxes">
				<svws-ui-checkbox v-model="inputIstVolljaehrig"> Volljährig </svws-ui-checkbox>
				<svws-ui-checkbox v-model="inputKeineAuskunftAnDritte"> Keine Auskunft an Dritte </svws-ui-checkbox>
				<svws-ui-checkbox v-model="inputIstSchulpflichtErfuellt"> Schulpflicht erfüllt </svws-ui-checkbox>
				<svws-ui-checkbox v-model="inputIstBerufsschulpflichtErfuellt"> Schulpflicht SII erfüllt </svws-ui-checkbox>
				<svws-ui-checkbox v-model="inputHatMasernimpfnachweis"> Masern Impfnachweis </svws-ui-checkbox>
				<svws-ui-checkbox v-model="inputErhaeltSchuelerBAFOEG">BAFöG</svws-ui-checkbox>
			</svws-ui-input-wrapper>
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { KatalogEintrag, SchuelerStammdaten} from "@core";
	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";
	import { SchuelerStatus } from "@core";

	const props = defineProps<{
		data: () => SchuelerStammdaten;
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
		get: () => (SchuelerStatus.fromID(props.data().status) || undefined),
		set: (value) => doPatch({ status: value?.id })
	});

	const inputFahrschuelerArtID: WritableComputedRef<KatalogEintrag | undefined> = computed({
		get: () => {
			const id = props.data().fahrschuelerArtID;
			return id === null ? undefined : props.mapFahrschuelerarten.get(id)
		},
		set: (value) => doPatch({ fahrschuelerArtID: value === undefined ? null : value.id })
	});

	const inputHaltestelleID: WritableComputedRef<KatalogEintrag | undefined> = computed({
		get: () => {
			const id = props.data().haltestelleID;
			return id === null ? undefined : props.mapHaltestellen.get(id)
		},
		set: (value) => doPatch({ haltestelleID: value === undefined ? null : value.id })
	});

	const inputIstDuplikat: WritableComputedRef<boolean> = computed({
		get: () => props.data().istDuplikat,
		set: (value) => doPatch({ istDuplikat: value })
	});

	const inputIstSchulpflichtErfuellt: WritableComputedRef<boolean> = computed({
		get: () => props.data().istSchulpflichtErfuellt || false,
		set: (value) => doPatch({ istSchulpflichtErfuellt: value })
	});

	const inputHatMasernimpfnachweis: WritableComputedRef<boolean> = computed({
		get: () => props.data().hatMasernimpfnachweis,
		set: (value) => doPatch({ hatMasernimpfnachweis: value })
	});

	const inputKeineAuskunftAnDritte: WritableComputedRef<boolean> = computed({
		get: () => props.data().keineAuskunftAnDritte,
		set: (value) => doPatch({ keineAuskunftAnDritte: value })
	});

	const inputErhaeltSchuelerBAFOEG: WritableComputedRef<boolean> = computed({
		get: () => props.data().erhaeltSchuelerBAFOEG,
		set: (value) => doPatch({ erhaeltSchuelerBAFOEG: value })
	});

	const inputIstBerufsschulpflichtErfuellt: WritableComputedRef<boolean> = computed({
		get: () => props.data().istBerufsschulpflichtErfuellt || false,
		set: (value) => doPatch({ istBerufsschulpflichtErfuellt: value })
	});

	const inputIstVolljaehrig: WritableComputedRef<boolean> = computed({
		get: () => props.data().istVolljaehrig || false,
		set: (value) => doPatch({ istVolljaehrig: value })
	});

</script>
