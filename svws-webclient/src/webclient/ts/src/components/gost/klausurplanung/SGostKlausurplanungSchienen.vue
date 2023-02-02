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
			Konflikte: {{ props.manager.value.gibKonfliktTerminKursklausur(7,2029).toString() }}
		</div>
	</div>
</template>

<script setup lang="ts">

	import { GostJahrgang, GostHalbjahr, GostKursklausurManager, GostKursklausur, List, GostKlausurtermin } from '@svws-nrw/svws-core-ts';
	import { computed, ComputedRef, ShallowRef } from 'vue';
	import { DataGostFaecher } from '~/apps/gost/DataGostFaecher';
	import { DataGostJahrgang } from '~/apps/gost/DataGostJahrgang';
	import { DataSchuleStammdaten } from '~/apps/schule/DataSchuleStammdaten';

	const props = defineProps<{
		item: ShallowRef<GostJahrgang | undefined>;
		schule: DataSchuleStammdaten;
		jahrgangsdaten: DataGostJahrgang;
		dataFaecher: DataGostFaecher;
		halbjahr: ShallowRef<GostHalbjahr>;
		manager: ShallowRef<GostKursklausurManager>;
	}>();

	const kursklausuren: ComputedRef<List<GostKursklausur>> = computed(() => props.manager.value.getKursklausurenOhneTermin());
	const termine: ComputedRef<List<GostKlausurtermin>> = computed(() => props.manager.value.getKlausurtermine());

</script>
