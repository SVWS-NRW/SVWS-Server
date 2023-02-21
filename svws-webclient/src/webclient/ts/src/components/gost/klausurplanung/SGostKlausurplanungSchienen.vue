<template>
	<div class="flex">
		<div class="flex flex-row flex-wrap gap-4">
			Hier kommt die Klausurschienen-Ansicht der Klausurplanung hin.
			Klausuren:<br>
			<template v-for="kursklausur of kursklausuren" :key="kursklausur.id">
				<p>{{ kursklausur.id }} {{ kursklausur.kursartAllg }} </p>
			</template>
			Termine:<br>
			<template v-for="termin of termine" :key="termin.id">
				<p>{{ termin.id }} {{ termin.datum }} </p>
			</template>
			Konflikte: {{ props.kursklausurmanager().gibKonfliktTerminKursklausur(7,2029).toString() }}
			{{ props.faecherManager.get(193)?.bezeichnung }}
		</div>
	</div>
</template>

<script setup lang="ts">

	import { GostKursklausurManager, GostKursklausur, List, GostKlausurtermin, GostFaecherManager } from '@svws-nrw/svws-core-ts';
	import { computed, ComputedRef } from 'vue';

	const props = defineProps<{
		faecherManager: GostFaecherManager;
		kursklausurmanager: () => GostKursklausurManager;
	}>();

	const kursklausuren: ComputedRef<List<GostKursklausur>> = computed(() => props.kursklausurmanager().getKursklausurenOhneTermin());
	const termine: ComputedRef<List<GostKlausurtermin>> = computed(() => props.kursklausurmanager().getKlausurtermine());

</script>
