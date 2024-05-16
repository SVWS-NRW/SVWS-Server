<template>

	<div v-for="(d, ix) in data" style="border: 3px solid red; margin-bottom: 3rem">
		<h1>Vermerk {{ ix }}</h1>
		<p>ID: {{ d.id }}</p>
		<p>schueler_id: {{ d.schueler_id }}</p>
		<svws-ui-text-input v-model="d.id" @change=" (newVal) => {patch(newVal, d.id)}"></svws-ui-text-input>
		<svws-ui-text-input v-model="d.Bemerkung" @change="patch"></svws-ui-text-input>
		<svws-ui-text-input v-model="d.Datum" @change="patch"></svws-ui-text-input>
		<svws-ui-text-input v-model="d.AngelegtVon" @change="patch"></svws-ui-text-input>
		<svws-ui-text-input v-model="d.GeaendertVon" @change="patch"></svws-ui-text-input>


	</div>
</template>

<script setup lang="ts">
import type {List, SchuelerVermerke} from "@core";
// const emit = defineEmits(['patch'])

defineProps<{
	data: List<SchuelerVermerke>;
	patch: () => {}
}>();

// const patch = (value: Partial<SchuelerVermerke>) => {
// 	console.log("VLAUE: ", value)
// 	emit('patch', value)
// }

</script>
