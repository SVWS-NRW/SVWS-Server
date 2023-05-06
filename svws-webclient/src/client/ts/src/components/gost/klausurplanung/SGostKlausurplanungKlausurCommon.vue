<template>
	<svws-ui-drag-data tag="tr" :key="props.klausur.id" :data="klausur" @drag-start="dragStart" @drag-end="dragEnd">
		<td>{{ kurs.kuerzel }}</td>
		<td>{{ mapLehrer.get(kurs.lehrer!)?.kuerzel }}</td>
		<td class="text-center">{{ props.klausur.schuelerIds.size() + "/" + kurs.schueler.size() }}</td>
		<td class="text-center">{{ props.klausur.dauer }}</td>
		<td>&nbsp;</td>
		<td></td>
	</svws-ui-drag-data>
</template>

<script setup lang="ts">

	import type { GostKursklausur, LehrerListeEintrag, GostKursklausurManager, KursListeEintrag, KursManager } from "@svws-nrw/svws-core";

	const props = defineProps<{
		kursklausurmanager: () => GostKursklausurManager;
		klausur: GostKursklausur;
		mapLehrer: Map<number, LehrerListeEintrag>;
		kursmanager: KursManager;
	}>();

	const emit = defineEmits<{
		(e: 'dragStartKlausur', data: DragEvent, klausur: GostKursklausur): void;
		(e: 'dragEndKlausur', data: DragEvent): void;
	}>()

	function dragStart(e: DragEvent) {
		emit("dragStartKlausur", e, props.klausur);
	}

	function dragEnd(e: DragEvent) {
		emit("dragEndKlausur", e);
	}

	const kurs: KursListeEintrag = props.kursmanager.get(props.klausur.idKurs)!;

</script>