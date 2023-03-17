<template>
	<svws-ui-drag-data tag="tr" :class="klausurCssClasses()" :key="props.klausur.id" :data="{...klausur}" @drag-start="dragStatus(klausur)" @drag-end="dragStatus(null)">
		<td>{{ props.klausur.kursart }}</td>
		<td>{{ faecherManager.get(props.klausur.idFach)?.kuerzelAnzeige }}</td>
		<td><!--{{ props.klausur.kursKurzbezeichnung!.match(/(\d+)/)!.slice(-1)[0] }}--></td>
		<td>{{ mapLehrer.get(props.klausur.idLehrer)?.kuerzel }}</td>
		<td>{{ props.klausur.schuelerIds.size() }}</td>
		<td>{{ props.klausur.kursSchiene[0] }}</td>
	</svws-ui-drag-data>
</template>

<script setup lang="ts">

	import { GostKursklausur, GostFaecherManager, LehrerListeEintrag, GostKlausurtermin, List, GostKursklausurManager } from "@svws-nrw/svws-core";

	const props = defineProps<{
		kursklausurmanager: () => GostKursklausurManager;
		klausur: GostKursklausur;
		termin?: GostKlausurtermin | null;
		alleTermine: List<GostKlausurtermin>;
		faecherManager: GostFaecherManager;
		mapLehrer: Map<number, LehrerListeEintrag>;
		dragStatus: (klausur: GostKursklausur | null) => void;
	}>();

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