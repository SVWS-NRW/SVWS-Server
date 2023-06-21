<template>
	<svws-ui-content-card title="Zeitraster">
		<SCardZeitrasterModal :add-zeitraster="addZeitraster" :patch-zeitraster="patchZeitraster" :remove-zeitraster="removeZeitraster" :list-katalogeintraege="listKatalogeintraege" v-slot="{ openModal }">
			<StundenplanWoche v-if="mapTage.size" :map-tage="mapTage">
				<template #zeiten>
					<div class="flex flex-col border w-full" v-if="gleicheZeiten !== undefined">
						<div class="font-bold text-headline-md">Zeiten</div>
						<div v-for="stunde of gleicheZeiten" :key="stunde.id" @click="openModal(stunde, true)" @mouseover="selected=stunde.unterrichtstunde" @mouseleave="selected=0" class="border rounded-md m-1 font-bold text-headline-md hover:bg-slate-400 select-none cursor-pointer">
							<div class="flex flex-col gap-1 h-14">{{ stunde.stundenbeginn }} - {{ stunde.stundenende }}</div>
						</div>
					</div>
				</template>
				<StundenplanTag v-for="tag of [...mapTage.keys()].sort()" :key="tag" :tag="tag">
					<StundenplanEntry v-for="stunde of mapTage.get(tag)" :key="stunde.hashCode()" :entry="stunde" @click="openModal(stunde, false)" class="hover:bg-slate-400 select-none cursor-pointer" :class="{'bg-slate-400': stunde.unterrichtstunde === selected}">
						<StundenplanStunde :stunde="stunde" />
					</StundenplanEntry>
				</StundenplanTag>
			</StundenplanWoche>
		</SCardZeitrasterModal>
		<SCardZeitrasterModal :add-zeitraster="addZeitraster" :patch-zeitraster="patchZeitraster" :list-katalogeintraege="listKatalogeintraege" v-slot="{ openModal }">
			<svws-ui-button type="transparent" size="small" @click="openModal()">Zeitraster hinzufügen</svws-ui-button>
		</SCardZeitrasterModal>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { List, StundenplanZeitraster} from "@core";
	import { computed, ref } from "vue";

	const props = defineProps<{
		listKatalogeintraege: () => List<StundenplanZeitraster>;
		patchZeitraster: (daten: StundenplanZeitraster, multi: StundenplanZeitraster[]) => Promise<void>;
		addZeitraster: (daten: StundenplanZeitraster, tage: number[]) => Promise<void>;
		removeZeitraster: (multi: StundenplanZeitraster[]) => Promise<void>;
	}>();

	const selected = ref();

	const mapTage = computed(()=>{
		const stunden = props.listKatalogeintraege();
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
		for (const key of mapTage.value.keys()) {
			let i = 0;
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
				if (stundenBezeichnungIdentisch === false || stundenBeginnIdentisch === false || stundenEndeIdentisch === false)
					return undefined;
				i++;
			}
		}
		return stunden;
	})
</script>
