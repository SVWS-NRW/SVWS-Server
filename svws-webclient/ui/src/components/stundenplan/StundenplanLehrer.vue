<template>
	<stundenplan-ansicht text-pausenzeit="Aufsicht" hide-zeitachse-pausenzeiten hide-pausenaufsicht grow-unterricht :mode-pausenaufsichten :show-zeitachse
		:ignore-empty :manager :wochentyp :kalenderwoche :use-drag-and-drop :drag-data :get-schienen :get-unterricht :zeitraster-hat-unterricht-mit-wochentyp
		:get-pausenzeiten :schneiden-pausenzeiten-zeitraster :get-pausenzeiten-wochentag :get-pausenaufsichten-pausenzeit :on-drag :on-drop>
		<template #unterricht="{ unterricht }">
			<div class="font-bold flex place-items-center group col-span-2" title="Unterricht">
				<span v-if="useDragAndDrop" class="icon i-ri-draggable inline-block icon-ui-contrast-75 -ml-1 opacity-60 group-hover:opacity-100 group-hover:icon-ui-contrast-75" />
				<span>{{ manager().unterrichtGetByIDStringOfFachOderKurs(unterricht.id, false) }}</span>
			</div>
			<div class="text-center">{{ unterricht.idKurs ? [...manager().kursGetByIdOrException(unterricht.idKurs).jahrgaenge].map(j => manager().jahrgangGetByIdOrException(j).kuerzel).join(', ') : [...unterricht.klassen].map(k => manager().klasseGetByIdOrException(k).kuerzel).join(', ') }}</div>
			<div class="text-center" title="Raum"> {{ manager().unterrichtGetByIDStringOfRaeume(unterricht.id) }} </div>
		</template>
	</stundenplan-ansicht>
</template>

<script setup lang="ts">

	import type { StundenplanAnsichtDragData, StundenplanAnsichtDropZone } from "./StundenplanAnsichtProps";
	import type { StundenplanLehrerProps } from "./StundenplanLehrerProps";
	import type { StundenplanPausenzeit } from "../../../../core/src/core/data/stundenplan/StundenplanPausenzeit";
	import type { List } from "../../../../core/src/java/util/List";
	import type { StundenplanPausenaufsicht } from "../../../../core/src/core/data/stundenplan/StundenplanPausenaufsicht";
	import type { StundenplanUnterricht } from "../../../../core/src/core/data/stundenplan/StundenplanUnterricht";
	import type { StundenplanSchiene } from "../../../../core/src/core/data/stundenplan/StundenplanSchiene";

	const props = withDefaults(defineProps<StundenplanLehrerProps>(), {
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
		return props.manager().schieneGetMengeByLehrerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(props.id, wochentag, stunde, wochentyp, false);
	}

	function getUnterricht(wochentag: number, stunde: number, wochentyp: number, schiene: number | null) : List<StundenplanUnterricht> {
		return props.manager().unterrichtGetMengeByLehrerIdAndWochentagAndStundeAndWochentypAndInklusiveOrEmptyList(props.id, wochentag, stunde, wochentyp, false);
	}

	function zeitrasterHatUnterrichtMitWochentyp(wochentag: number, stunde: number): boolean {
		return props.manager().zeitrasterHatUnterrichtMitWochentyp1BisNByLehrerIdWochentagAndStunde(props.id, wochentag, stunde);
	}

	function getPausenzeiten() : List<StundenplanPausenzeit> {
		return props.manager().pausenzeitGetMengeByLehrerIdAsList(props.id);
	}

	function schneidenPausenzeitenZeitraster(wochentag: number): boolean {
		return props.manager().pausenzeitHatSchnittMitZeitrasterByWochentagAndLehrerId(wochentag, props.id);
	}

	function getPausenzeitenWochentag(wochentag: number) : List<StundenplanPausenzeit> {
		return props.manager().pausenzeitGetMengeByLehrerIdAndWochentagAsList(props.id, wochentag);
	}

	function getPausenaufsichtenPausenzeit(idPausenzeit: number) : List<StundenplanPausenaufsicht> {
		return props.manager().pausenaufsichtGetMengeByLehrerIdAndPausenzeitIdAndWochentypAndInklusive(props.id, idPausenzeit, props.wochentyp(), true);
	}

</script>
