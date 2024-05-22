<template>

	<div class="card" style="border: 3px solid red; margin-bottom: 3rem">
<!--		<svws-ui-select title="Versetzung" v-model="vorigeArtLetzteVersetzung" :items="herkunftsarten" :item-text="(h: Herkunftsarten) => getBezeichnung(h) + ' (' + h.daten.kuerzel + ')'" :statistics="showstatistic" class="col-span-full" />-->

		<!-- HIER WIRD GEBAUT -->

		<svws-ui-select
						v-model="aktuelleVermerkArt"
						:items="[...props.mapVermerkArten.values()]" :item-text="(item) => {return item.bezeichnung}"
						class="SelectElement"
						@update:modelValue="(newVal: VermerkartEintrag) => { patch({VermerkArt_ID: newVal.id}, data.id)}"
		></svws-ui-select>

		<span class="icon i-ri-user-add-line "></span>
		<span class="icon i-ri-time-line " />
		<svws-ui-text-input v-model="data.Datum" @change=" (newVal) => {patch({Datum: newVal}, data.id)}"></svws-ui-text-input>
		<svws-ui-text-input v-model="data.Bemerkung" @change=" (newVal) => {patch({Bemerkung: newVal}, data.id)}"></svws-ui-text-input>
		<svws-ui-text-input v-model="data.AngelegtVon" @change=" (newVal) => {patch({AngelegtVon: newVal}, data.id)}"></svws-ui-text-input>
		<svws-ui-text-input v-model="data.GeaendertVon" @change=" (newVal) => {patch({GeaendertVon: newVal}, data.id)}"></svws-ui-text-input>
	</div>

</template>

<script setup lang="ts">
import {Herkunftsarten, List, SchuelerVermerke, VermerkartEintrag} from "@core";
import {computed, ref} from "vue";
// const emit = defineEmits(['patch'])

const props = defineProps<{
	data: SchuelerVermerke;
	mapVermerkArten: Map<number, VermerkartEintrag>
	patch: (data : Partial<SchuelerVermerke>, vid : number) => {}
}>();


const aktuelleVermerkArt = computed({
	get : () => {
		return [...props.mapVermerkArten.values()].find((elem) => {return elem.id == props.data.VermerkArt_ID})
	},

	set: (newVal) => {
		return newVal;
	}
})

</script>

<style scoped>
.icon {
	width: 10rem;
	height: 10rem;
}
.card {
	background: grey;
	display: flex;
}

.SelectElement {
}
</style>
