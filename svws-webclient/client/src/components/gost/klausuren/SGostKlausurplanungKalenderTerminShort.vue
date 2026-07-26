<template>
	<div class="inline-block rounded-sm bg-ui-neutral text-ui-onneutral ml-10 top-10 h-5">
		{{ klausurBezeichnungen }}
	</div>
</template>

<script setup lang="ts">
	import type { GostKlausurplanManager, GostFaecherManager, LehrerListeEintrag, GostKlausurtermin, KursManager } from "@core";

	const props = defineProps<{
		termin: GostKlausurtermin;
		kursklausurmanager: () => GostKlausurplanManager;
		faecherManager: GostFaecherManager;
		kursmanager: KursManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
	}>();

	const klausuren = () => props.kursklausurmanager().kursklausurGetMengeByTermin(props.termin);
	const klausurBezeichnungen = [...klausuren()].map(k => props.kursmanager.get(k.idKurs)?.kuerzel).join(", ");

</script>