<template>
	<div class="flex flex-row border justify-stretch">
		<div class="flex flex-col border w-full" v-if="gleicheZeiten !== undefined">
			<div class="font-bold text-headline-md">Zeiten</div>
			<div v-for="stunde of gleicheZeiten" :key="stunde.id" class="border rounded-md m-1 font-bold text-headline-md hover:bg-slate-400 select-none cursor-pointer" @mouseover="selected=stunde.unterrichtstunde" @mouseleave="selected=0"><div class="flex flex-col gap-1 h-14">{{ stunde.stundenbeginn }} - {{ stunde.stundenende }}</div></div>
		</div>
		<StundenplanTag v-for="[tag, listZeitraster] of mapTage.entries()" :key="tag" :tag="tag" :list-zeitraster="listZeitraster" :selected="selected" />
	</div>
</template>

<script setup lang="ts">
	import type { StundenplanZeitraster } from '@core';
	import { computed, ref } from 'vue';

	const props = defineProps<{
		mapTage: Map<number, StundenplanZeitraster[]>;
	}>()

	const selected = ref(0);

	const gleicheZeiten = computed(()=>{
		let stundenAnzahlIdentisch
		let stundenBezeichnungIdentisch
		let stundenBeginnIdentisch
		let stundenEndeIdentisch
		let anzahlStunden
		let stunden: StundenplanZeitraster[] | undefined = undefined;
		for (const stunden of props.mapTage.values())
			if (anzahlStunden === undefined)
				anzahlStunden = stunden.length;
			else
				stundenAnzahlIdentisch = anzahlStunden === stunden.length;
		if (stundenAnzahlIdentisch === false)
			return undefined;
		let stundenBezeichnung = [];
		let stundenBeginn = [];
		let stundenEnde = [];
		let i = 0;
		for (const key of props.mapTage.keys()) {
			for (const stunde of props.mapTage.get(key)!) {
				if (stundenBezeichnung[i] === undefined) {
					stunden = props.mapTage.get(key)!;
					stundenBezeichnung[i] = stunde.unterrichtstunde;
					stundenBeginn[i] = stunde.stundenbeginn;
					stundenEnde[i] = stunde.stundenende;
				}
				else {
					stundenBezeichnungIdentisch = stundenBezeichnung[i] === stunde.unterrichtstunde;
					stundenBeginnIdentisch = stundenBeginn[i] === stunde.stundenbeginn;
					stundenEndeIdentisch = stundenEnde[i] === stunde.stundenende;
				}
				i++;
				if (stundenBezeichnungIdentisch === false || stundenBeginnIdentisch === false || stundenEndeIdentisch === false)
					return undefined;
			}
		}
		console.log(stundenAnzahlIdentisch, stundenBeginnIdentisch, stundenBezeichnungIdentisch, stundenEndeIdentisch, stundenBezeichnung, stundenBeginn, stundenEnde)
		return stunden;
	})
</script>