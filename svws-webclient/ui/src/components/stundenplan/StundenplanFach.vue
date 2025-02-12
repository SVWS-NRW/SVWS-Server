<template>
	<stundenplan-ansicht :mode-pausenaufsichten="modePausenaufsichten" :show-zeitachse="showZeitachse" :ignore-empty="ignoreEmpty"
		:manager="manager" :wochentyp="wochentyp" :kalenderwoche="kalenderwoche" :use-drag-and-drop="useDragAndDrop" :drag-data="dragData"
		:get-schienen="getSchienen" :get-unterricht="getUnterricht" :zeitraster-hat-unterricht-mit-wochentyp="zeitrasterHatUnterrichtMitWochentyp"
		:get-pausenzeiten="getPausenzeiten" :schneiden-pausenzeiten-zeitraster="schneidenPausenzeitenZeitraster"
		:get-pausenzeiten-wochentag="getPausenzeitenWochentag" :get-pausenaufsichten-pausenzeit="getPausenaufsichtenPausenzeit"
		:on-drag="onDrag" :on-drop="onDrop">
		<template #unterricht="{ unterricht }">
			<div class="font-bold flex place-items-center group" title="Unterricht">
				<span v-if="useDragAndDrop" class="icon i-ri-draggable inline-block icon-ui-contrast-75 -ml-1 opacity-60 group-hover:opacity-100 group-hover:icon-ui-contrast-75" />
				<svws-ui-tooltip v-if="unterricht.schienen.size() > 0">
					<span class="break-keep">{{ manager().unterrichtGetByIDStringOfFachOderKurs(unterricht.id, false) }}&nbsp;({{ schienennummer(unterricht.schienen) }})</span>
					<template #content>{{ schienenbezeichnung(unterricht.schienen) }}</template>
				</svws-ui-tooltip>
				<span v-else>{{ manager().unterrichtGetByIDStringOfFachOderKurs(unterricht.id, false) }}</span>
			</div>
			<div class="text-center">{{ unterricht.idKurs ? [...manager().kursGetByIdOrException(unterricht.idKurs).jahrgaenge].map(j => manager().jahrgangGetByIdOrException(j).kuerzel).join(', ') : [...unterricht.klassen].map(k => manager().klasseGetByIdOrException(k).kuerzel).join(', ') }}</div>
			<div class="text-center" title="Lehrkraft"> {{ manager().unterrichtGetByIDLehrerFirstAsStringOrEmpty(unterricht.id) }} </div>
			<div class="text-center" title="Raum"> {{ manager().unterrichtGetByIDStringOfRaeume(unterricht.id) }} </div>
		</template>
	</stundenplan-ansicht>
</template>

<script setup lang="ts">

	import type { StundenplanAnsichtDragData, StundenplanAnsichtDropZone } from "./StundenplanAnsichtProps";
	import type { StundenplanFachProps } from "./StundenplanFachProps";
	import type { StundenplanPausenzeit } from "../../../../core/src/core/data/stundenplan/StundenplanPausenzeit";
	import type { List } from "../../../../core/src/java/util/List";
	import type { StundenplanPausenaufsicht } from "../../../../core/src/core/data/stundenplan/StundenplanPausenaufsicht";
	import type { StundenplanUnterricht } from "../../../../core/src/core/data/stundenplan/StundenplanUnterricht";
	import type { StundenplanSchiene } from "../../../../core/src/core/data/stundenplan/StundenplanSchiene";
	import { DeveloperNotificationException } from "../../../../core/src/core/exceptions/DeveloperNotificationException";
	import { Wochentag } from "../../../../core/src/core/types/Wochentag";

	const props = withDefaults(defineProps<StundenplanFachProps>(), {
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

	function schienenbezeichnung(ids: List<number>) {
		return (ids.toArray() as number[]).map(s => props.manager().schieneGetByIdOrException(s).bezeichnung).join(', ');
	}

	function schienennummer(ids: List<number>) {
		return (ids.toArray() as number[]).map(s => props.manager().schieneGetByIdOrException(s).nummer).join(', ');
	}

	function getSchienen(wochentag: number, stunde: number, wochentyp: number) : List<StundenplanSchiene> {
		throw new DeveloperNotificationException("Die Anzeige von Schienen wird beim der Fachansicht nicht unterstützt.");
	}

	function getUnterricht(wochentag: number, stunde: number, wochentyp: number, schiene: number | null) : List<StundenplanUnterricht> {
		return props.manager().unterrichtGetMengeByFachIdAndWochentagAndStundeAndWochentypAndSchieneAndInklusiveOrEmptyList(props.id, wochentag, stunde, wochentyp, schiene ?? -2, false);
	}

	function zeitrasterHatUnterrichtMitWochentyp(wochentag: number, stunde: number): boolean {
		return props.manager().zeitrasterHatUnterrichtMitWochentyp1BisNByFachIdWochentagAndStunde(props.id, Wochentag.fromIDorException(wochentag), stunde);
	}

	function getPausenzeiten() : List<StundenplanPausenzeit> {
		throw new DeveloperNotificationException("Die Anzeige von Pausenzeiten wird bei der Fachansicht nicht unterstützt.");
	}

	function schneidenPausenzeitenZeitraster(wochentag: number): boolean {
		return false;
	}

	function getPausenzeitenWochentag(wochentag: number) : List<StundenplanPausenzeit> {
		throw new DeveloperNotificationException("Die Anzeige von Pausenzeiten wird bei der Fachansicht nicht unterstützt.");
	}

	function getPausenaufsichtenPausenzeit(idPausenzeit: number) : List<StundenplanPausenaufsicht> {
		throw new DeveloperNotificationException("Die Anzeige von Pausenzeiten wird bei der Fachansicht nicht unterstützt.");
	}

</script>
