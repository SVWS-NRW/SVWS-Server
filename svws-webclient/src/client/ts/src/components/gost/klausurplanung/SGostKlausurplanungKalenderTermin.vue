<template>
	<div class="rounded bg-dark-20 p-2">
		<s-gost-klausurplanung-termin :kursklausurmanager="kursklausurmanager"
			:jahrgangsdaten="jahrgangsdaten"
			:toggle-details="true"
			:show-details="false"
			:termin="termin"
			:map-lehrer="mapLehrer"
			:klausur-draggable="false"
			:kursmanager="kursmanager" />
	</div>
</template>

<script setup lang="ts">
	import type { GostKursklausurManager, GostFaecherManager, LehrerListeEintrag, GostKlausurtermin, KursManager, GostJahrgangsdaten} from "@core";
	import { computed } from "vue";

	const props = defineProps<{
		jahrgangsdaten: GostJahrgangsdaten;
		termin: GostKlausurtermin;
		kursklausurmanager: () => GostKursklausurManager;
		faecherManager: GostFaecherManager;
		kursmanager: KursManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
	}>();

</script>