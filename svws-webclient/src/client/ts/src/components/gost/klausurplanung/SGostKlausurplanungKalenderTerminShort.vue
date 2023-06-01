<template>
	<div class="inline-block rounded bg-dark-20 ml-10 top-10 h-5">
		{{ klausurBezeichnungen() }}
	</div>
</template>

<script setup lang="ts">
	import type { GostKursklausurManager, GostFaecherManager, LehrerListeEintrag, GostKlausurtermin, KursManager} from "@svws-nrw/svws-core";

	const props = defineProps<{
		//itemTop: number;
		termin: GostKlausurtermin;
		kursklausurmanager: () => GostKursklausurManager;
		faecherManager: GostFaecherManager;
		kursmanager: KursManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
	}>();

	const klausuren = props.kursklausurmanager().getKursklausuren(props.termin.id);
	const klausurBezeichnungen = () => [...klausuren].reduce((retVal, klaus) => retVal += klaus.kursKurzbezeichnung + ", ", "");

</script>