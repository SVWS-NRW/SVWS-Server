<template>
	<svws-ui-content-card title="Zeitraster" class="col-span-full">
		<SCardStundenplanZeitrasterModal :add-zeitraster="addZeitraster" :patch-zeitraster="patchZeitraster" :remove-zeitraster="removeZeitraster" :stundenplan-manager="stundenplanManager" v-slot="{ openModal }">
			<StundenplanWoche :map-tage="mapTage">
				<template #zeiten>
					<div class="svws-ui-zeitraster--tag" v-if="gleicheZeiten !== undefined">
						<div class="svws-ui-zeitraster--head">Zeiten</div>
						<div v-for="stunde of gleicheZeiten" :key="stunde.id" @click="openModal(stunde, true)" @mouseover="selected=stunde.unterrichtstunde" @mouseleave="selected=0" class="svws-ui-zeitraster--entry svws-alternative">
							<div class="svws-ui-zeitraster--stunde text-button font-bold">{{ stunde.stundenbeginn }}–{{ stunde.stundenende }}</div>
						</div>
					</div>
				</template>
				<StundenplanTag v-for="[tag, listZeitraster] of mapTage.entries()" :key="tag" :tag="tag">
					<StundenplanEntry v-for="stunde of listZeitraster" :key="stunde.id" :entry="stunde" @click="openModal(stunde, false)" class="group" :class="{'svws-group-hover bg-svws/5 text-svws': stunde.unterrichtstunde === selected}">
						<StundenplanStunde :stunde="stunde" class="group-hover:border-svws" />
					</StundenplanEntry>
				</StundenplanTag>
			</StundenplanWoche>
		</SCardStundenplanZeitrasterModal>
		<div class="flex items-center gap-2 flex-wrap mt-5 justify-end">
			<SCardStundenplanZeitrasterImportModal :stundenplan-manager="stundenplanManager" :import-zeitraster="importZeitraster" :remove-zeitraster="removeZeitraster" v-slot="{ openModal }">
				<svws-ui-button type="secondary" @click="openModal()"><i-ri-archive-line /> Aus Katalog importieren</svws-ui-button>
			</SCardStundenplanZeitrasterImportModal>
			<SCardStundenplanZeitrasterModal :add-zeitraster="addZeitraster" :patch-zeitraster="patchZeitraster" :stundenplan-manager="stundenplanManager" v-slot="{ openModal }">
				<svws-ui-button type="primary" @click="openModal()">Zeitraster hinzufügen</svws-ui-button>
			</SCardStundenplanZeitrasterModal>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { StundenplanManager, StundenplanZeitraster} from "@core";
	import { computed, ref } from "vue";

	const props = defineProps<{
		stundenplanManager: () => StundenplanManager;
		patchZeitraster: (daten: StundenplanZeitraster, multi: StundenplanZeitraster[]) => Promise<void>;
		addZeitraster: (daten: StundenplanZeitraster, tage: number[]) => Promise<void>;
		removeZeitraster: (multi: StundenplanZeitraster[]) => Promise<void>;
		importZeitraster: () => Promise<void>;
	}>();

	const selected = ref();

	const mapTage = computed(()=>{
		const stunden = props.stundenplanManager().getListZeitraster();
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

<style lang="postcss" scoped>
.svws-group-hover {
  .svws-ui-zeitraster--stunde {
    @apply border-svws text-svws;
  }
}
</style>
