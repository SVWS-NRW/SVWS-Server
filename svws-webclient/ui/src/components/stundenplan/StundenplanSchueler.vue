<template>
	<stundenplan-ansicht grow-unterricht hide-zeitachse-pausenzeiten :mode-pausenaufsichten :show-zeitachse :ignore-empty :get-pausenzeiten-wochentag
		:get-pausenaufsichten-pausenzeit :manager :wochentyp :kalenderwoche :use-drag-and-drop :drag-data :get-schienen :get-unterricht
		:zeitraster-hat-unterricht-mit-wochentyp :get-pausenzeiten :schneiden-pausenzeiten-zeitraster :on-drag :on-drop>
		<template #unterricht="{ unterricht }">
			<div class="font-bold flex place-items-center group col-span-2" title="Unterricht">
				<span v-if="useDragAndDrop" class="icon i-ri-draggable icon-ui-75 -ml-1 opacity-60 group-hover:opacity-100 group-hover:icon-ui-75" />
				<span>{{ manager().unterrichtGetByIDStringOfFachOderKurs(unterricht.id, false) }}</span>
			</div>
			<div class="text-center" title="Lehrkraft"> {{ manager().unterrichtGetByIDLehrerFirstAsStringOrEmpty(unterricht.id) }} </div>
			<div class="text-center" title="Raum"> {{ manager().unterrichtGetByIDStringOfRaeume(unterricht.id) }} </div>
		</template>
	</stundenplan-ansicht>
</template>

<script setup lang="ts">

	import type { StundenplanAnsichtDragData, StundenplanAnsichtDropZone } from "./StundenplanAnsichtProps";
	import type { StundenplanSchuelerProps } from "./StundenplanSchuelerProps";
	import type { StundenplanPausenzeit } from "../../../../core/src/core/data/stundenplan/StundenplanPausenzeit";
	import type { List } from "../../../../core/src/java/util/List";
	import type { StundenplanPausenaufsicht } from "../../../../core/src/core/data/stundenplan/StundenplanPausenaufsicht";
	import type { StundenplanUnterricht } from "../../../../core/src/core/data/stundenplan/StundenplanUnterricht";
	import type { StundenplanSchiene } from "../../../../core/src/core/data/stundenplan/StundenplanSchiene";

	const props = withDefaults(defineProps<StundenplanSchuelerProps>(), {
		mode: 'schueler',
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
		return props.manager().schieneGetMengeBySchuelerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(props.id, wochentag, stunde, wochentyp, false);
	}

	function getUnterricht(wochentag: number, stunde: number, wochentyp: number, schiene: number | null) : List<StundenplanUnterricht> {
		return props.manager().unterrichtGetMengeBySchuelerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(props.id, wochentag, stunde, wochentyp, false);
	}

	function zeitrasterHatUnterrichtMitWochentyp(wochentag: number, stunde: number): boolean {
		return props.manager().zeitrasterHatUnterrichtMitWochentyp1BisNBySchuelerIdWochentagAndStunde(props.id, wochentag, stunde);
	}

	function getPausenzeiten() {
		return props.manager().pausenzeitGetMengeBySchuelerIdAsList(props.id);
	}

	function schneidenPausenzeitenZeitraster(wochentag: number): boolean {
		return props.manager().pausenzeitHatSchnittMitZeitrasterByWochentagAndSchuelerId(wochentag, props.id);
	}

	function getPausenzeitenWochentag(wochentag: number) : List<StundenplanPausenzeit> {
		return props.manager().pausenzeitGetMengeBySchuelerIdAndWochentagAsList(props.id, wochentag);
	}

	function getPausenaufsichtenPausenzeit(idPausenzeit: number) : List<StundenplanPausenaufsicht> {
		return props.manager().pausenaufsichtGetMengeBySchuelerIdAndPausenzeitIdAndWochentypAndInklusive(props.id, idPausenzeit, props.wochentyp(), true);
	}

</script>
