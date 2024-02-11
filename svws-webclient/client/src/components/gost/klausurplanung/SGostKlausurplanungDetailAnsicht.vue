<template>
	<Teleport to=".router-tab-bar--subnav" v-if="isMounted">
		<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl="quartalsauswahl" :halbjahr="halbjahr" />
	</Teleport>
	<div class="page--content page--content--full relative flex-col">
		<div class="text-headline">Klausurplan {{ jahrgangsdaten.jahrgang }}, {{ halbjahr.halbjahr }}. Halbjahr{{ quartalsauswahl.value === 0 ? "" : ", " + quartalsauswahl.value + ". Quartal" }}</div>

		<div v-if="kMan().terminMitDatumGetHTMengeByHalbjahrAndQuartal(halbjahr, quartalsauswahl.value, false).size() > 0">
			<s-gost-klausurplanung-detail-ansicht-termin class="mb-10"
				v-for="termin in kMan().terminMitDatumGetHTMengeByHalbjahrAndQuartal(halbjahr, quartalsauswahl.value, false)"
				:key="termin.id"
				:termin="termin"
				:k-man="kMan"
				:erzeuge-klausurraummanager="erzeugeKlausurraummanager"
				:stundenplanmanager="stundenplanmanager()" />
		</div>
		<div v-else>
			Es wurden noch keine Klausurtermine geplant.
		</div>
	</div>
</template>

<script setup lang="ts">
	import { onMounted, ref } from 'vue';
	import type { GostKlausurplanungDetailAnsichtProps } from './SGostKlausurplanungDetailAnsichtProps';

	const props = defineProps<GostKlausurplanungDetailAnsichtProps>();

	const isMounted = ref(false);

	onMounted(() => {
		isMounted.value = true;
	});

</script>
