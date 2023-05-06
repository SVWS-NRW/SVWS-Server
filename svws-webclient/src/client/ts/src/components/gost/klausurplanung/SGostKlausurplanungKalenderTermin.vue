<template>
	<div class="rounded bg-dark-20 p-2">
		<s-gost-klausurplanung-termin-common :kursklausurmanager="kursklausurmanager"
			:termin="termin"
			:map-lehrer="mapLehrer"
			:drag-status="null"
			:klausur-draggable="false"
			:kursmanager="kursmanager" />
	</div>
</template>

<script setup lang="ts">
	import type { GostKursklausurManager, GostFaecherManager, LehrerListeEintrag, GostKlausurtermin, KursManager} from "@svws-nrw/svws-core";
	import { computed } from "vue";

	const props = defineProps<{
		termin: GostKlausurtermin;
		kursklausurmanager: () => GostKursklausurManager;
		faecherManager: GostFaecherManager;
		kursmanager: KursManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
	}>();

</script>