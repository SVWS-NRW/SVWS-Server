<template>
	<Teleport to=".router-tab-bar--subnav" v-if="isMounted">
		<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl="quartalsauswahl" :halbjahr="halbjahr" />
	</Teleport>
	<div class="page--content relative flex-col">
		<svws-ui-content-card class="col-span-full" :title="`Klausurplan ${jahrgangsdaten.jahrgang}, ${halbjahr.halbjahr}. Halbjahr${quartalsauswahl.value === 0 ? '' : ', ' + quartalsauswahl.value + '. Quartal'}`">
			<div v-if="kMan().terminMitDatumGetHTMengeByHalbjahrAndQuartal(jahrgangsdaten.abiturjahr, halbjahr, quartalsauswahl.value).size() > 0" class="flex flex-col gap-20 mt-8">
				<s-gost-klausurplanung-detail-ansicht-termin v-for="termin in kMan().terminMitDatumGetHTMengeByHalbjahrAndQuartal(jahrgangsdaten.abiturjahr, halbjahr, quartalsauswahl.value)"
					:key="termin.id"
					:termin="termin"
					:k-man="kMan"
					:erzeuge-klausurraummanager="erzeugeKlausurraummanager"
					:stundenplanmanager="stundenplanmanager()" />
			</div>
			<div v-else>
				<span>Es wurden noch keine Klausurtermine geplant.</span>
			</div>
		</svws-ui-content-card>
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
