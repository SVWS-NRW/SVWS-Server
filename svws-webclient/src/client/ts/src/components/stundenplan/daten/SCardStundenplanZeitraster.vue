<template>
	<svws-ui-content-card title="Zeitraster">
		<StundenplanWoche :map-tage="mapTage">
			<template #zeiten>
				<div class="flex flex-col border w-full" v-if="gleicheZeiten !== undefined">
					<div class="font-bold text-headline-md">Zeiten</div>
					<div v-for="stunde of gleicheZeiten" :key="stunde.id" class="border rounded-md m-1 font-bold text-headline-md hover:bg-slate-400 select-none cursor-pointer" @mouseover="selected=stunde.unterrichtstunde" @mouseleave="selected=0"><div class="flex flex-col gap-1 h-14">{{ stunde.stundenbeginn }} - {{ stunde.stundenende }}</div></div>
				</div>
			</template>
			<StundenplanTag v-for="[tag, listZeitraster] of mapTage.entries()" :key="tag" :tag="tag">
				<StundenplanEntry v-for="stunde of listZeitraster" :key="stunde.id" :entry="stunde" class="hover:bg-slate-400 select-none cursor-pointer" :class="{'bg-slate-400': stunde.unterrichtstunde === selected}">
					<StundenplanStunde :stunde="stunde" />
				</StundenplanEntry>
			</StundenplanTag>
		</StundenplanWoche>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { StundenplanManager, StundenplanZeitraster} from "@core";
	import { computed, ref } from "vue";

	const props = defineProps<{
		stundenplanManager: () => StundenplanManager;
	}>();

	const selected = ref();

	const mapTage = computed(()=>{
		const stunden = props.stundenplanManager().getListZeitraster()
		const map = new Map<number, StundenplanZeitraster[]>();
		for (const s of stunden) {
			if (!map.has(s.wochentag))
				map.set(s.wochentag, []);
			const a = map.get(s.wochentag)!;
			a.push(s);
		}
		return map;
	})

	const gleicheZeiten = computed(()=>{
		let stundenAnzahlIdentisch
		let stundenBezeichnungIdentisch
		let stundenBeginnIdentisch
		let stundenEndeIdentisch
		let anzahlStunden
		let stunden: StundenplanZeitraster[] | undefined = undefined;
		for (const stunden of mapTage.value.values())
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
		for (const key of mapTage.value.keys()) {
			for (const stunde of mapTage.value.get(key)!) {
				if (stundenBezeichnung[i] === undefined) {
					stunden = mapTage.value.get(key)!;
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
		return stunden;
	})
</script>
