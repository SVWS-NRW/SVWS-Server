<template>
	<div class="rounded bg-dark-20 p-2">
		<div class="justify-between py-1">
			<h3 class="text-sm">{{ props.termin.bezeichnung !== null && props.termin.bezeichnung !== undefined ? props.termin.bezeichnung : "Unbenannte Klausurschiene" }}</h3>
		</div>
		<div class="text-xs mt-1">
			<div v-for="klausur in klausuren" :key="klausur.id" class="bg-white p-2 rounded mt-1 border-b border-grey cursor-pointer hover:bg-grey-lighter">
				{{ klausur.kursKurzbezeichnung }}
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { GostKursklausurManager, GostFaecherManager, LehrerListeEintrag, GostKlausurtermin, GostKursklausur, List } from "@svws-nrw/svws-core";
	import { computed } from "vue";

	const props = defineProps<{
		termin: GostKlausurtermin;
		kursklausurmanager: () => GostKursklausurManager;
		faecherManager: GostFaecherManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
	}>();

	const klausuren = computed(() => props.kursklausurmanager().getKursklausuren(props.termin.id));

</script>