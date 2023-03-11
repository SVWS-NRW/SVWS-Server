<template>
	<svws-ui-drag-data tag="tr" :key="props.klausur.id" :data="{...klausur}" @drag-start="dragStatus(klausur)" @drag-end="dragStatus(null)">
		<td>{{ props.klausur.kursartAllg }}</td>
		<td>{{ faecherManager.get(props.klausur.idFach)?.kuerzelAnzeige }}</td>
		<td><!--{{ props.klausur.kursKurzbezeichnung!.match(/(\d+)/)!.slice(-1)[0] }}--></td>
		<td>{{ mapLehrer.get(props.klausur.idLehrer)?.kuerzel }}</td>
		<td>{{ props.klausur.schuelerIds.size() }}</td>
		<td>{{ props.klausur.kursSchiene }}</td>
	</svws-ui-drag-data>
</template>

<script setup lang="ts">

	import { GostKursklausur, GostFaecherManager, LehrerListeEintrag } from "@svws-nrw/svws-core";

	const props = defineProps<{
		klausur: GostKursklausur;
		faecherManager: GostFaecherManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
		dragStatus: (klausur: GostKursklausur | null) => void;
	}>();

</script>