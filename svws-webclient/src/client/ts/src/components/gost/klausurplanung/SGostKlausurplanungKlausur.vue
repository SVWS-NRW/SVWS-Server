<template>
	<svws-ui-drag-data tag="tr" :key="props.klausur.id" :data="klausur" @drag-start="dragStart" @drag-end="dragEnd">
		<td>{{ kurs.kuerzel }}</td>
		<td>{{ mapLehrer.get(kurs.lehrer!)?.kuerzel }}</td>
		<td class="text-center">{{ props.klausur.schuelerIds.size() + "/" + kurs.schueler.size() }}</td>
		<td class="text-center">{{ props.klausur.dauer }}</td>
		<td>&nbsp;</td>
		<td>
			<svws-ui-text-input v-if="patchKursklausur" :placeholder="termin.startzeit !== null ? termin.startzeit + '' : ''"
				v-model="startzeit"
				@update:model-value="patchKursklausur(klausur.id, { startzeit: klausur.startzeit })" />
		</td>
	</svws-ui-drag-data>
</template>

<script setup lang="ts">

	import type { GostKursklausur, LehrerListeEintrag, GostKursklausurManager, KursListeEintrag, KursManager, GostKlausurtermin } from "@core";
	import { computed } from "vue";

	const props = defineProps<{
		kursklausurmanager: () => GostKursklausurManager;
		klausur: GostKursklausur;
		termin: GostKlausurtermin;
		mapLehrer: Map<number, LehrerListeEintrag>;
		kursmanager: KursManager;
		patchKursklausur?: (id: number, klausur: Partial<GostKursklausur>) => Promise<boolean>;
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

	const kurs = props.kursmanager.get(props.klausur.idKurs)!;

	const startzeit = computed({
		get: () : number | undefined => props.klausur.startzeit === null ? undefined : props.klausur.startzeit,
		set: (value: number | undefined): void => {
			props.klausur.startzeit = value === undefined ? null : value;
		}
	});

</script>