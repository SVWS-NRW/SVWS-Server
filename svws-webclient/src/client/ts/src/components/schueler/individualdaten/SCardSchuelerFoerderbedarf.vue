<template>
	<svws-ui-content-card title="Sonderpädagogische Förderung">
		<div class="input-wrapper">
			<svws-ui-multi-select title="Haupt-Förderschwerpunkt" v-model="inputFoerderschwerpunktID" :items="mapFoerderschwerpunkte" />
			<svws-ui-multi-select title="Weiterer-Förderschwerpunkt" v-model="inputFoerderschwerpunkt2ID" :items="mapFoerderschwerpunkte" />
			<div class="flex flex-col">
				<svws-ui-checkbox :model-value="data().istAOSF || false" @update:model-value="doPatch({ istAOSF: Boolean($event) })">AOSF</svws-ui-checkbox>
				<svws-ui-checkbox :model-value="data().istLernenZieldifferent || false" @update:model-value="doPatch({ istLernenZieldifferent: Boolean($event) })">Zieldifferntes Lernen</svws-ui-checkbox>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";
	import type { FoerderschwerpunktEintrag, SchuelerStammdaten } from "@svws-nrw/svws-core";

	const props = defineProps<{
		data: () => SchuelerStammdaten;
		mapFoerderschwerpunkte: Map<number, FoerderschwerpunktEintrag>;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<SchuelerStammdaten>): void;
	}>()

	function doPatch(data: Partial<SchuelerStammdaten>) {
		emit('patch', data);
	}

	const inputFoerderschwerpunktID: WritableComputedRef<FoerderschwerpunktEintrag | undefined> = computed({
		get: () => {
			const id = props.data().foerderschwerpunktID;
			return id === null ? undefined : props.mapFoerderschwerpunkte.get(id)
		},
		set: (value) => doPatch({ foerderschwerpunktID: value === undefined ? null : value.id })
	});

	const inputFoerderschwerpunkt2ID: WritableComputedRef<FoerderschwerpunktEintrag | undefined> = computed({
		get: () => {
			const id = props.data().foerderschwerpunkt2ID;
			return id === null ? undefined : props.mapFoerderschwerpunkte.get(id)
		},
		set: (value) => doPatch({ foerderschwerpunkt2ID: value === undefined ? null : value.id })
	});

</script>
