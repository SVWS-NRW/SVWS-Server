<template>
	<tr :key="props.klausur.id" :data="klausur">
		<td>{{ kurs.kuerzel }}</td>
		<td>{{ mapLehrer.get(kurs.lehrer!)?.kuerzel }}</td>
		<td class="text-center">{{ props.klausur.schuelerIds.size() + "/" + kurs.schueler.size() }}</td>
		<td class="text-center">{{ props.klausur.dauer }}</td>
		<td>&nbsp;</td>
		<td>
			<svws-ui-text-input v-if="patchKlausurUhrzeit" :model-value="klausur.startzeit !== null ? DateUtils.getStringOfUhrzeitFromMinuten(klausur.startzeit) : ''" :placeholder="klausur.startzeit === null ? DateUtils.getStringOfUhrzeitFromMinuten(termin.startzeit!) : 'Startzeit'" @update:model-value="patchKlausurbeginn($event, klausur.id)" />
		</td>
	</tr>
</template>

<script setup lang="ts">

	import { DateUtils} from "@core";
	import type { GostKursklausur, LehrerListeEintrag, GostKursklausurManager, KursManager, GostKlausurtermin, GostSchuelerklausur } from "@core";
	import { computed } from "vue";

	const props = defineProps<{
		kursklausurmanager: () => GostKursklausurManager;
		klausur: GostKursklausur;
		termin: GostKlausurtermin;
		mapLehrer: Map<number, LehrerListeEintrag>;
		kursmanager: KursManager;
		patchKlausurUhrzeit?: (klausur: Partial<GostKursklausur> | Partial<GostSchuelerklausur>) => Promise<boolean>;
	}>();

	async function patchKlausurbeginn(event: string | number, id: number) {
		if (typeof event === 'number')
			return;
		try {
			const startzeit = event.trim().length > 0 ? DateUtils.gibMinutenOfZeitAsString(event) : null;
			await props.patchKlausurUhrzeit!({id, startzeit});
		} catch(e) {
			// Do nothing
		}
	}

	const kurs = props.kursmanager.get(props.klausur.idKurs)!;

	const startzeit = computed({
		get: () : number | undefined => props.klausur.startzeit === null ? undefined : props.klausur.startzeit,
		set: (value: number | undefined): void => {
			props.klausur.startzeit = value === undefined ? null : value;
		}
	});

</script>