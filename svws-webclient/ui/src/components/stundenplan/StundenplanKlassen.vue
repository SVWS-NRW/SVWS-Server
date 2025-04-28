<template>
	<stundenplan-ansicht show-schienen hide-zeitachse-pausenzeiten :mode-pausenaufsichten :show-zeitachse :ignore-empty :use-drag-and-drop
		:manager :wochentyp :kalenderwoche :drag-data :get-schienen :get-unterricht :zeitraster-hat-unterricht-mit-wochentyp
		:get-pausenzeiten :schneiden-pausenzeiten-zeitraster :get-pausenzeiten-wochentag :get-pausenaufsichten-pausenzeit :on-drag :on-drop
		@update:click="unterricht => emit('update:click', unterricht)">
		<template #unterricht="{ unterricht }">
			<div class="font-bold col-span-2 flex place-items-center group" title="Unterricht">
				<span v-if="useDragAndDrop" class="icon i-ri-draggable icon-uistatic-50 group-hover:icon-uistatic-100" />
				<span>{{ manager().unterrichtGetByIDStringOfFachOderKurs(unterricht.id, false) }}</span>
			</div>
			<div class="text-center flex place-items-center gap-2" title="Lehrkraft"> {{ manager().unterrichtGetByIDLehrerFirstAsStringOrEmpty(unterricht.id) }} <span v-if="unterricht.lehrer.size() > 1" class="icon i-ri-add-circle-line" /></div>
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
	import type { StundenplanKlassenProps } from "./StundenplanKlassenProps";

	const props = withDefaults(defineProps<StundenplanKlassenProps>(), {
		modePausenaufsichten: 'normal',
		showZeitachse: true,
		zeitrasterSteps: 5,
		ignoreEmpty: false,
		useDragAndDrop: false,
		dragData: () => undefined,
		onDrag: (data: StundenplanAnsichtDragData, event?: DragEvent) => {},
		onDrop: (zone: StundenplanAnsichtDropZone, wochentyp?: number) => {},
	});

	const emit = defineEmits<{
		"update:click": [value: StundenplanUnterricht];
	}>();


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
