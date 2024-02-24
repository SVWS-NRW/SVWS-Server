<template>
	<svws-ui-input-wrapper>
		<div class="flex flex-col w-full" v-for="(lehrer, i) of kurslehrer" :key="lehrer.id">
			<svws-ui-select :model-value="lehrer" @update:model-value="val => update_kurslehrer(val, lehrer)" class="flex-1"
				autocomplete :item-filter="lehrer_filter" :items="lehrer_liste" removable
				:item-text="l => `${i+1}: ${l.nachname}, ${l.vorname} (${l.kuerzel})`" />
			<svws-ui-button v-if="!new_kurs_lehrer && (i === kurslehrer.size() - 1)" @click="new_kurs_lehrer=true" type="transparent" class="col-span-full mt-3">
				Lehrkraft hinzufügen <i-ri-user-add-line />
			</svws-ui-button>
		</div>
		<div v-if="!kurslehrer.size() || new_kurs_lehrer">
			<svws-ui-select :model-value="undefined" @update:model-value="update_kurslehrer" class="flex-1" autocomplete
				:item-filter="lehrer_filter" :items="lehrer_liste" :item-text="l => `${l.nachname}, ${l.vorname} (${l.kuerzel})`" />
		</div>
	</svws-ui-input-wrapper>
</template>

<script setup lang="ts">

	import { computed, ref } from 'vue';
	import type { GostBlockungKurs, GostBlockungKursLehrer, GostBlockungsdatenManager, List } from "@core";
	import { ArrayList, LehrerListeEintrag } from "@core";
	import { lehrer_filter } from '~/utils/helfer';

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		addLehrerRegel: () => Promise<void>;
		addKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<GostBlockungKursLehrer | undefined>;
		removeKursLehrer: (kurs_id: number, lehrer_id: number) => Promise<void>;
		kurs: GostBlockungKurs;
		mapLehrer: Map<number, LehrerListeEintrag>;
	}>();

	const new_kurs_lehrer = ref<boolean>(false);

	const kurslehrer = computed<List<LehrerListeEintrag>>(() => {
		const liste = props.getDatenmanager().kursGetLehrkraefteSortiert(props.kurs.id);
		const tmp = new ArrayList<GostBlockungKursLehrer>(liste);
		tmp.sort({ compare(a : GostBlockungKursLehrer, b : GostBlockungKursLehrer) {
			return a.reihenfolge - b.reihenfolge;
		}});
		const result = new ArrayList<LehrerListeEintrag>();
		for (const l of tmp) {
			const lehrer = props.mapLehrer.get(l.id);
			if (lehrer !== undefined)
				result.add(lehrer);
		}
		return result;
	})

	const lehrer_liste = computed<LehrerListeEintrag[]>(() => {
		const vergeben = new Set();
		for (const l of kurslehrer.value)
			vergeben.add(l.id);
		const result = [];
		for (const l of props.mapLehrer.values())
			if ((!vergeben.has(l.id)) && (l.istSichtbar))
				result.push(l);
		return result;
	})

	async function update_kurslehrer(lehrer: unknown, lehrer_alt?: LehrerListeEintrag) {
		if ((lehrer === undefined) && lehrer_alt) {
			await props.removeKursLehrer(props.kurs.id, lehrer_alt.id);
			return;
		}
		if (lehrer instanceof LehrerListeEintrag) {
			const kurslehrer = await props.addKursLehrer(props.kurs.id, lehrer.id);
			if (!kurslehrer)
				throw new Error("Fehler beim Anlegen des Kurslehrers");
			await props.addLehrerRegel();
			new_kurs_lehrer.value = false;
		}
	}

</script>
