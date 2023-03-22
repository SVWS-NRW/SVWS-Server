<template>
	<svws-ui-content-card title="Sonderpädagogische Förderung">
		<div class="input-wrapper">
			<svws-ui-multi-select title="Haupt-Förderschwerpunkt" v-model="inputFoerderschwerpunktID" :items="mapFoerderschwerpunkte" />
			<svws-ui-multi-select title="Weiterer-Förderschwerpunkt" v-model="inputFoerderschwerpunkt2ID" :items="mapFoerderschwerpunkte" />
			<div class="flex flex-col">
				<svws-ui-checkbox :model-value="data.istAOSF" @update:model-value="doPatch({ istAOSF: Boolean($event) })">AOSF</svws-ui-checkbox>
				<svws-ui-checkbox :model-value="data.istLernenZieldifferent" @update:model-value="doPatch({ istLernenZieldifferent: Boolean($event) })">Zieldifferntes Lernen</svws-ui-checkbox>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, WritableComputedRef } from "vue";
	import { FoerderschwerpunktEintrag, SchuelerStammdaten } from "@svws-nrw/svws-core";

	const props = defineProps<{
		data: SchuelerStammdaten;
		mapFoerderschwerpunkte: Map<number, FoerderschwerpunktEintrag>;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<SchuelerStammdaten>): void;
	}>()

	function doPatch(data: Partial<SchuelerStammdaten>) {
		emit('patch', data);
	}

	const inputFoerderschwerpunktID: WritableComputedRef<FoerderschwerpunktEintrag | undefined> = computed({
		get: () => props.data.foerderschwerpunktID === null ? undefined : props.mapFoerderschwerpunkte.get(props.data.foerderschwerpunktID),
		set: (value) => doPatch({ foerderschwerpunktID: value === undefined ? null : value.id })
	});

	const inputFoerderschwerpunkt2ID: WritableComputedRef<FoerderschwerpunktEintrag | undefined> = computed({
		get: () => props.data.foerderschwerpunkt2ID === null ? undefined : props.mapFoerderschwerpunkte.get(props.data.foerderschwerpunkt2ID),
		set: (value) => doPatch({ foerderschwerpunkt2ID: value === undefined ? null : value.id })
	});

</script>
