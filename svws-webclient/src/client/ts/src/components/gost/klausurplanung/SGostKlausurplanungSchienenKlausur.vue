<template>
	<svws-ui-drag-data tag="tr" :class="klausurCssClasses()" :key="props.klausur.id" :data="{...klausur}" @drag-start="dragStatus(klausur)" @drag-end="dragStatus(null)">
		<td>{{ kurs.kuerzel }}</td>
		<td>{{ mapLehrer.get(kurs.lehrer!)?.kuerzel }}</td>
		<td class="text-center">{{ props.klausur.schuelerIds.size() + "/" + kurs.schueler.size() }}</td>
		<td class="text-center">{{ props.klausur.dauer }}</td>
		<td>&nbsp;</td>
		<td></td>
	</svws-ui-drag-data>
</template>

<script setup lang="ts">

	import type { GostKursklausur, GostFaecherManager, LehrerListeEintrag, GostKlausurtermin, List, GostKursklausurManager, KursListeEintrag, KursManager } from "@svws-nrw/svws-core";

	const props = defineProps<{
		kursklausurmanager: () => GostKursklausurManager;
		klausur: GostKursklausur;
		termin?: GostKlausurtermin | null;
		alleTermine: List<GostKlausurtermin>;
		faecherManager: GostFaecherManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
		kursmanager: KursManager;
		dragStatus: (klausur: GostKursklausur | null) => void;
	}>();

	const kurs: KursListeEintrag = props.kursmanager.get(props.klausur.idKurs)!;

	const klausurCssClasses = () => {
		let konfliktfreiZuFremdtermin = false;
		for (const termin of props.alleTermine) {
			if (termin.id !== props.klausur.idTermin && termin.quartal === props.klausur.quartal)
				konfliktfreiZuFremdtermin = props.kursklausurmanager().gibKonfliktTerminKursklausur(termin.id, props.klausur.id).isEmpty();
			if (konfliktfreiZuFremdtermin)
				break;
		}
		const konfliktZuEigenemTermin = props.termin === undefined || props.termin === null || props.klausur === null ? false : props.kursklausurmanager().gibKonfliktTerminInternKursklausur(props.termin, props.klausur).size() > 0;
		return {
			"bg-success": !konfliktZuEigenemTermin && konfliktfreiZuFremdtermin,
			"bg-error": konfliktZuEigenemTermin,
		}
	};

</script>