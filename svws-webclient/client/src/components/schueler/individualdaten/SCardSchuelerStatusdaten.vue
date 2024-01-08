<template>
	<svws-ui-content-card title="Statusdaten">
		<template #actions>
			<svws-ui-checkbox :model-value="data.istDuplikat" @update:model-value="istDuplikat => patch({istDuplikat})">Ist Duplikat</svws-ui-checkbox>
		</template>
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-select title="Status" :model-value="SchuelerStatus.fromID(data.status)" @update:model-value="status => patch({ status: status?.id })" :items="SchuelerStatus.values()" :item-text="i => i.bezeichnung" statistics />
			<svws-ui-select v-if="schuelerListeManager().daten().status === SchuelerStatus.EXTERN.id"
				title="Stammschule" v-model="inputStammschule" :items="mapSchulen.values()" :item-text="i => i.kuerzel ?? i.schulnummer" />
			<div v-else />
			<svws-ui-select title="Fahrschüler" v-model="inputFahrschuelerArtID" :items="mapFahrschuelerarten" :item-text="i => i.text ?? ''" />
			<svws-ui-select title="Haltestelle" v-model="inputHaltestelleID" :items="mapHaltestellen" :item-text="i => i.text ?? ''" />
			<svws-ui-text-input placeholder="Anmeldedatum" :model-value="data.anmeldedatum" @change="anmeldedatum => patch({anmeldedatum})" type="date" />
			<svws-ui-text-input placeholder="Aufnahmedatum" :model-value="data.aufnahmedatum" @change="aufnahmedatum => patch({aufnahmedatum})" type="date" statistics />
			<svws-ui-spacing />
			<svws-ui-input-wrapper :grid="2" class="input-wrapper--checkboxes">
				<svws-ui-checkbox :model-value="data.istVolljaehrig === true" @update:model-value="istVolljaehrig => patch({ istVolljaehrig })"> Volljährig </svws-ui-checkbox>
				<svws-ui-checkbox :model-value="data.keineAuskunftAnDritte" @update:model-value="keineAuskunftAnDritte => patch({ keineAuskunftAnDritte })"> Keine Auskunft an Dritte </svws-ui-checkbox>
				<svws-ui-checkbox :model-value="data.istSchulpflichtErfuellt === true" readonly> Schulpflicht erfüllt </svws-ui-checkbox>
				<svws-ui-checkbox :model-value="data.istBerufsschulpflichtErfuellt === true" @update:model-value="istBerufsschulpflichtErfuellt => patch({ istBerufsschulpflichtErfuellt })"> Schulpflicht SII erfüllt </svws-ui-checkbox>
				<svws-ui-checkbox :model-value="data.hatMasernimpfnachweis" @update:model-value="hatMasernimpfnachweis => patch({ hatMasernimpfnachweis })"> Masern Impfnachweis </svws-ui-checkbox>
				<svws-ui-checkbox :model-value="data.erhaeltSchuelerBAFOEG" @update:model-value="erhaeltSchuelerBAFOEG => patch({ erhaeltSchuelerBAFOEG })">BAFöG</svws-ui-checkbox>
			</svws-ui-input-wrapper>
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { HashMap, KatalogEintrag, SchuelerListeManager, SchuelerStammdaten, SchulEintrag } from "@core";
	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";
	import { SchuelerStatus } from "@core";

	const props = defineProps<{
		schuelerListeManager: () => SchuelerListeManager;
		patch: (data: Partial<SchuelerStammdaten>) => Promise<void>;
		mapFahrschuelerarten: Map<number, KatalogEintrag>;
		mapHaltestellen: Map<number, KatalogEintrag>;
		mapSchulen: HashMap<string, SchulEintrag>;
	}>();

	const data = computed<SchuelerStammdaten>(() => props.schuelerListeManager().daten());

	const inputStammschule: WritableComputedRef<SchulEintrag | undefined> = computed({
		get: () => (data.value.externeSchulNr === null) ? undefined : (props.mapSchulen.get(data.value.externeSchulNr) || undefined),
		set: (value) => void props.patch({ externeSchulNr: value === undefined ? null : value.schulnummer })
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

</script>
