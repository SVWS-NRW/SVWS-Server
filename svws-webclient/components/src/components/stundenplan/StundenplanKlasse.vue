<template>
	<stundenplan-ansicht show-schienen hide-zeitachse-pausenzeiten :mode-pausenaufsichten="modePausenaufsichten" :show-zeitachse="showZeitachse" :ignore-empty="ignoreEmpty"
		:manager="manager" :wochentyp="wochentyp" :kalenderwoche="kalenderwoche" :use-drag-and-drop="useDragAndDrop" :drag-data="dragData"
		:get-schienen="getSchienen" :get-unterricht="getUnterricht" :zeitraster-hat-unterricht-mit-wochentyp="zeitrasterHatUnterrichtMitWochentyp"
		:get-pausenzeiten="getPausenzeiten" :schneiden-pausenzeiten-zeitraster="schneidenPausenzeitenZeitraster"
		:get-pausenzeiten-wochentag="getPausenzeitenWochentag" :get-pausenaufsichten-pausenzeit="getPausenaufsichtenPausenzeit"
		:on-drag="onDrag" :on-drop="onDrop">
		<template #unterricht="{ unterricht }">
			<div class="font-bold col-span-2 flex place-items-center group" title="Unterricht">
				<span v-if="useDragAndDrop" class="icon i-ri-draggable inline-block -ml-1 icon-dark opacity-60 group-hover:opacity-100 group-hover:icon-dark" />
				<span>{{ manager().unterrichtGetByIDStringOfFachOderKursKuerzel(unterricht.id) }}</span>
			</div>
			<div class="text-center" title="Lehrkraft"> {{ manager().unterrichtGetByIDLehrerFirstAsStringOrEmpty(unterricht.id) }} </div>
			<div class="text-center" title="Raum"> {{ manager().unterrichtGetByIDStringOfRaeume(unterricht.id) }} </div>
		</template>
	</stundenplan-ansicht>
</template>

<script setup lang="ts">

	import type { List } from "../../../../core/src/java/util/List";
	import type { StundenplanSchiene } from "../../../../core/src/core/data/stundenplan/StundenplanSchiene";
	import type { StundenplanAnsichtDragData, StundenplanAnsichtDropZone } from "./StundenplanAnsichtProps";
	import type { StundenplanUnterricht } from "../../../../core/src/core/data/stundenplan/StundenplanUnterricht";
	import type { StundenplanPausenzeit } from "../../../../core/src/core/data/stundenplan/StundenplanPausenzeit";
	import type { StundenplanPausenaufsicht } from "../../../../core/src/core/data/stundenplan/StundenplanPausenaufsicht";
	import type { StundenplanKlasseProps } from "./StundenplanKlasseProps";

	const props = withDefaults(defineProps<StundenplanKlasseProps>(), {
		modePausenaufsichten: 'normal',
		showZeitachse: true,
		zeitrasterSteps: 5,
		ignoreEmpty: false,
		useDragAndDrop: false,
		dragData: () => undefined,
		onDrag: (data: StundenplanAnsichtDragData, event?: DragEvent) => {},
		onDrop: (zone: StundenplanAnsichtDropZone, wochentyp?: number) => {},
	});

	function getSchienen(wochentag: number, stunde: number, wochentyp: number) : List<StundenplanSchiene> {
		return props.manager().schieneGetMengeByKlasseIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(props.id, wochentag, stunde, wochentyp, false);
	}

	function getUnterricht(wochentag: number, stunde: number, wochentyp: number, schiene: number | null) : List<StundenplanUnterricht> {
		if (schiene === null)
			return props.manager().unterrichtGetMengeByKlasseIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(props.id, wochentag, stunde, wochentyp, false);
		return props.manager().unterrichtGetMengeByKlasseIdAndWochentagAndStundeAndWochentypAndSchieneAndInklusiveOrEmptyList(props.id, wochentag, stunde, wochentyp, schiene, false);
	}

	function zeitrasterHatUnterrichtMitWochentyp(wochentag: number, stunde: number): boolean {
		return props.manager().zeitrasterHatUnterrichtMitWochentyp1BisNByKlasseIdWochentagAndStunde(props.id, wochentag, stunde);
	}

	function getPausenzeiten() {
		return props.manager().pausenzeitGetMengeByKlasseIdAsList(props.id);
	}

	function schneidenPausenzeitenZeitraster(wochentag: number): boolean {
		return props.manager().pausenzeitHatSchnittMitZeitrasterByWochentagAndKlassenId(wochentag, props.id);
	}

	function getPausenzeitenWochentag(wochentag: number) : List<StundenplanPausenzeit> {
		return props.manager().pausenzeitGetMengeByKlasseIdAndWochentagAsList(props.id, wochentag);
	}

	function getPausenaufsichtenPausenzeit(idPausenzeit: number) : List<StundenplanPausenaufsicht> {
		return props.manager().pausenaufsichtGetMengeByKlasseIdAndPausenzeitIdAndWochentypAndInklusive(props.id, idPausenzeit, props.wochentyp(), true);
	}

</script>
