<template>
	<svws-ui-content-card class="pt-8">
		<div class="flex h-full gap-4 mt-4">
			<ul class="flex flex-col gap-y-1">
				<li v-for="termin in termine" :key="termin.id">
					<s-gost-klausurplanung-kalender-termin :kursklausurmanager="kursklausurmanager" :faecher-manager="faecherManager" :map-lehrer="mapLehrer" :termin="termin" :kursmanager="kursmanager" />
				</li>
			</ul>
			<div class="flex flex-row flex-wrap gap-4 w-full" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { GostJahrgangsdaten, GostKursklausurManager, GostFaecherManager, LehrerListeEintrag, GostKlausurtermin, KursManager } from '@svws-nrw/svws-core';
	import { computed } from 'vue';

	const props = defineProps<{
		jahrgangsdaten: GostJahrgangsdaten | undefined;
		kursklausurmanager: () => GostKursklausurManager;
		faecherManager: GostFaecherManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
		patchKlausurtermin: (termin: Partial<GostKlausurtermin>, id: number) => Promise<boolean>;
		kursmanager: KursManager;
	}>();

	const termine = computed(() => props.kursklausurmanager().getKlausurtermine());

</script>
