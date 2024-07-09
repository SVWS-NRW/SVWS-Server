<template>
	<stundenplan-ansicht :mode-pausenaufsichten="modePausenaufsichten" :show-zeitachse="showZeitachse" :ignore-empty="ignoreEmpty" :id="id"
		:manager="manager" :wochentyp="wochentyp" :kalenderwoche="kalenderwoche" :use-drag-and-drop="useDragAndDrop" :drag-data="dragData"
		:get-schienen="getSchienen" :get-unterricht="getUnterricht" :zeitraster-hat-unterricht-mit-wochentyp="zeitrasterHatUnterrichtMitWochentyp"
		:get-pausenzeiten="getPausenzeiten" :get-pausenzeiten-wochentag="getPausenzeitenWochentag" :get-pausenaufsichten-pausenzeit="getPausenaufsichtenPausenzeit"
		:on-drag="onDrag" :on-drop="onDrop">
		<template #unterricht="{ unterricht }">
			<div class="font-bold flex place-items-center group col-span-2" title="Unterricht">
				<span v-if="useDragAndDrop" class="icon i-ri-draggable inline-block icon-dark -ml-1 opacity-60 group-hover:opacity-100 group-hover:icon-dark" />
				<span>{{ manager().unterrichtGetByIDStringOfFachOderKursKuerzel(unterricht.id) }}</span>
			</div>
			<div class="text-center">{{ unterricht.idKurs ? [...manager().kursGetByIdOrException(unterricht.idKurs).jahrgaenge].map(j => manager().jahrgangGetByIdOrException(j).kuerzel).join(', ') : [...unterricht.klassen].map(k => manager().klasseGetByIdOrException(k).kuerzel).join(', ') }}</div>
			<div class="text-center" title="Lehrkraft"> {{ manager().unterrichtGetByIDLehrerFirstAsStringOrEmpty(unterricht.id) }} </div>
		</template>
	</stundenplan-ansicht>
</template>

<script setup lang="ts">

	import type { StundenplanAnsichtDragData, StundenplanAnsichtDropZone } from "./StundenplanAnsichtProps";
	import type { StundenplanAnsichtRaumProps } from "./StundenplanAnsichtRaumProps";
	import type { StundenplanPausenzeit } from "../../../../core/src/core/data/stundenplan/StundenplanPausenzeit";
	import type { List } from "../../../../core/src/java/util/List";
	import { DeveloperNotificationException } from "../../../../core/src/core/exceptions/DeveloperNotificationException";
	import type { StundenplanPausenaufsicht } from "../../../../core/src/core/data/stundenplan/StundenplanPausenaufsicht";
	import type { StundenplanUnterricht } from "../../../../core/src/core/data/stundenplan/StundenplanUnterricht";
	import type { StundenplanSchiene } from "../../../../core/src/core/data/stundenplan/StundenplanSchiene";
	import { ArrayList } from "../../../../core/src/java/util/ArrayList";

	const props = withDefaults(defineProps<StundenplanAnsichtRaumProps>(), {
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

	function getPausenzeiten() : List<StundenplanPausenzeit> {
		throw new DeveloperNotificationException("Die Anzeige von Pausenzeiten wird bei der Raumansicht nicht unterstützt.");
	}

	function zeitrasterHatUnterrichtMitWochentyp(wochentag: number, stunde: number): boolean {
		//TODO
		return false//props.manager().zeitrasterHatUnterrichtMitWochentyp1BisNByRaumIdWochentagAndStunde(props.id, wochentag, stunde);
	}

	function getSchienen(wochentag: number, stunde: number, wochentyp: number) : List<StundenplanSchiene> {
		throw new DeveloperNotificationException("Die Anzeige von Schienen wird beim der Raumansicht nicht unterstützt.");
	}

	function getUnterricht(wochentag: number, stunde: number, wochentyp: number, schiene: number | null) : List<StundenplanUnterricht> {
		const unterricht = props.manager().unterrichtGetMengeAsList()
		const list: List<StundenplanUnterricht> = new ArrayList();
		const zeitraster = props.manager().zeitrasterGetByWochentagAndStundeOrException(wochentag, stunde);
		for (const u of unterricht)
			if (u.raeume.contains(props.id) && (u.wochentyp === wochentyp) && (u.idZeitraster === zeitraster.id))
				list.add(u);
		return list;
	}

	function getPausenzeitenWochentag(wochentag: number) : List<StundenplanPausenzeit> {
		throw new DeveloperNotificationException("Die Anzeige von Pausenzeiten wird bei der Raumansicht nicht unterstützt.");
	}

	function getPausenaufsichtenPausenzeit(idPausenzeit: number) : List<StundenplanPausenaufsicht> {
		throw new DeveloperNotificationException("Die Anzeige von Pausenzeiten wird bei der Raumansicht nicht unterstützt.");
	}

</script>
