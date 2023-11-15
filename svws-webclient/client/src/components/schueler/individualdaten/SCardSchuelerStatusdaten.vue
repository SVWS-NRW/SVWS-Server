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
			<svws-ui-text-input placeholder="Anmeldedatum" :model-value="data.anmeldedatum" @change="anmeldedatum => patch({anmeldedatum})" type="date" />
			<svws-ui-text-input placeholder="Aufnahmedatum" :model-value="data.aufnahmedatum" @change="aufnahmedatum => patch({aufnahmedatum})" type="date" statistics />
			<svws-ui-spacing />
			<svws-ui-input-wrapper :grid="2" class="input-wrapper--checkboxes">
				<svws-ui-checkbox v-model="inputIstVolljaehrig"> Volljährig </svws-ui-checkbox>
				<svws-ui-checkbox v-model="inputKeineAuskunftAnDritte"> Keine Auskunft an Dritte </svws-ui-checkbox>
				<svws-ui-checkbox :model-value="data.istSchulpflichtErfuellt === true" readonly> Schulpflicht erfüllt </svws-ui-checkbox>
				<svws-ui-checkbox v-model="inputIstBerufsschulpflichtErfuellt"> Schulpflicht SII erfüllt </svws-ui-checkbox>
				<svws-ui-checkbox v-model="inputHatMasernimpfnachweis"> Masern Impfnachweis </svws-ui-checkbox>
				<svws-ui-checkbox v-model="inputErhaeltSchuelerBAFOEG">BAFöG</svws-ui-checkbox>
			</svws-ui-input-wrapper>
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { KatalogEintrag, SchuelerListeManager, SchuelerStammdaten} from "@core";
	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";
	import { SchuelerStatus } from "@core";

	const props = defineProps<{
		schuelerListeManager: () => SchuelerListeManager;
		patch: (data: Partial<SchuelerStammdaten>) => Promise<void>;
		mapFahrschuelerarten: Map<number, KatalogEintrag>;
		mapHaltestellen: Map<number, KatalogEintrag>
	}>();

	const data = computed<SchuelerStammdaten>(() => props.schuelerListeManager().daten());

	const inputStatus: WritableComputedRef<SchuelerStatus | undefined> = computed({
		get: () => (SchuelerStatus.fromID(data.value.status) || undefined),
		set: (value) => void props.patch({ status: value?.id })
	});

	const inputFahrschuelerArtID: WritableComputedRef<KatalogEintrag | undefined> = computed({
		get: () => {
			const id = data.value.fahrschuelerArtID;
			return id === null ? undefined : props.mapFahrschuelerarten.get(id)
		},
		set: (value) => void props.patch({ fahrschuelerArtID: value === undefined ? null : value.id })
	});

	const inputHaltestelleID: WritableComputedRef<KatalogEintrag | undefined> = computed({
		get: () => {
			const id = data.value.haltestelleID;
			return id === null ? undefined : props.mapHaltestellen.get(id)
		},
		set: (value) => void props.patch({ haltestelleID: value === undefined ? null : value.id })
	});

	const inputIstDuplikat: WritableComputedRef<boolean> = computed({
		get: () => data.value.istDuplikat,
		set: (value) => void props.patch({ istDuplikat: value })
	});

	const inputIstSchulpflichtErfuellt: WritableComputedRef<boolean> = computed({
		get: () => data.value.istSchulpflichtErfuellt || false,
		set: (value) => void props.patch({ istSchulpflichtErfuellt: value })
	});

	const inputHatMasernimpfnachweis: WritableComputedRef<boolean> = computed({
		get: () => data.value.hatMasernimpfnachweis,
		set: (value) => void props.patch({ hatMasernimpfnachweis: value })
	});

	const inputKeineAuskunftAnDritte: WritableComputedRef<boolean> = computed({
		get: () => data.value.keineAuskunftAnDritte,
		set: (value) => void props.patch({ keineAuskunftAnDritte: value })
	});

	const inputErhaeltSchuelerBAFOEG: WritableComputedRef<boolean> = computed({
		get: () => data.value.erhaeltSchuelerBAFOEG,
		set: (value) => void props.patch({ erhaeltSchuelerBAFOEG: value })
	});

	const inputIstBerufsschulpflichtErfuellt: WritableComputedRef<boolean> = computed({
		get: () => data.value.istBerufsschulpflichtErfuellt || false,
		set: (value) => void props.patch({ istBerufsschulpflichtErfuellt: value })
	});

	const inputIstVolljaehrig: WritableComputedRef<boolean> = computed({
		get: () => data.value.istVolljaehrig || false,
		set: (value) => void props.patch({ istVolljaehrig: value })
	});

</script>
