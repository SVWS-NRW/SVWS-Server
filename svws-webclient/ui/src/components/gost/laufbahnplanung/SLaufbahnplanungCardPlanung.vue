<template>
	<svws-ui-table :items="manager.alleFaecher" :columns has-background scroll>
		<template #header>
			<div role="row" class="svws-ui-tr">
				<div role="columnheader" class="svws-ui-td col-span-3 svws-divider">
					Fachwahlen
				</div>
				<div role="columnheader" class="svws-ui-td svws-align-center col-span-2 svws-divider">
					Sprachen
				</div>
				<div role="columnheader" class="svws-ui-td svws-align-center col-span-2 svws-divider">
					EF
				</div>
				<div role="columnheader" class="svws-ui-td svws-align-center col-span-4 svws-divider">
					Qualifikationsphase
				</div>
				<div role="columnheader" class="svws-ui-td svws-align-center">
					Abitur
				</div>
			</div>
			<div role="row" class="svws-ui-tr">
				<div class="svws-ui-td">
					Kürzel
				</div>
				<div class="svws-ui-td">
					Fach
				</div>
				<div class="svws-ui-td svws-align-center svws-divider">
					<svws-ui-tooltip>
						<span>WS</span>
						<template #content>
							Wochenstunden
						</template>
					</svws-ui-tooltip>
				</div>
				<div class="svws-ui-td svws-align-center svws-no-padding items-center">
					Folge
				</div>
				<div class="svws-ui-td svws-align-center svws-divider svws-no-padding items-center">
					<svws-ui-tooltip>
						<span>ab JG</span>
						<template #content>
							Ab Jahrgang
						</template>
					</svws-ui-tooltip>
				</div>
				<template v-for="halbjahr in GostHalbjahr.values()" :key="halbjahr.id">
					<div class="svws-ui-td svws-align-center svws-divider svws-no-padding items-center">
						{{ halbjahr.kuerzel }}
						<svws-ui-tooltip v-if="gostJahrgangsdaten.anzahlKursblockungen[halbjahr.id] > 0">
							<span @click.stop="gotoKursblockung(halbjahr)" class="cursor-pointer"><span class="icon-sm i-ri-link inline-block" /></span>
							<template #content>
								Zur {{ halbjahr.kuerzel }}-Kursblockung
							</template>
						</svws-ui-tooltip>
					</div>
				</template>
				<div class="svws-ui-td svws-align-center svws-no-padding items-center">
					Fach
				</div>
			</div>
		</template>
		<template #body>
			<template v-for="fach in manager.faecherGefiltert" :key="fach.id">
				<s-laufbahnplanung-fach :manager :abiturdaten-manager :gost-jahrgangsdaten :fach :set-wahl :ignoriere-sprachenfolge :belegung-hat-immer-noten :hat-update-kompetenz :active-halbjahr-id
					:active-focus="fach.id === activeFachId" @keydown="switchFocus($event)" @update:focus="(fachId: number, halbjahrId: number) => updateFocusState(fachId, halbjahrId)"
					@update:focus:impossible="(fachId: number, halbjahrId: number) => retryFocus(fachId, halbjahrId)" />
			</template>
		</template>
		<template #dataFooter>
			<div role="row" class="svws-ui-tr">
				<div role="rowheader" class="svws-ui-td font-bold svws-align-right col-span-5 svws-divider gap-1">
					<span>Anrechenbare Kurse</span>
					<svws-ui-tooltip>
						<span class="icon i-ri-question-line -m-0.5 mx-0.5" />
						<template #content>
							Die Anzahl der anrechenbaren Kurse. Vertiefungskurse werden z.B. nicht mitgezählt.
						</template>
					</svws-ui-tooltip>
				</div>
				<div role="cell" class="svws-ui-td svws-align-center svws-no-padding items-center! " v-for="hj in GostHalbjahr.values()" :key="hj.id" :class="{'svws-divider': (hj.id === 1 || hj.id === 5)}">
					<span class="inline-flex justify-center items-center font-bold py-0.5 px-1.5 rounded-sm w-full m-0.5"
						:class="manager.getAnrechenbareKurseCSS(hj)">
						{{ manager.getAnrechenbareKurse(hj) }}
					</span>
				</div>
				<div role="cell" class="svws-ui-td svws-align-center svws-no-padding items-center!">
					<span class="inline-flex justify-center items-center font-bold py-0.5 px-1.5 rounded-sm w-full m-0.5"
						:class="manager.getSummeAnrechenbareKurseCSS()">
						{{ manager.summeAnrechenbareKurse }}
					</span>
				</div>
			</div>
			<div role="row" class="svws-ui-tr">
				<div role="rowheader" class="svws-ui-td font-bold svws-align-right col-span-5 svws-divider">
					<span>Wochenstunden</span>
					<svws-ui-tooltip>
						<span class="icon i-ri-question-line -m-0.5 mx-0.5" />
						<template #content>
							Die Anzahl der Wochenstunden. Pro Halbjahr sollten etwa <strong>33—36</strong> Wochenstunden gewählt werden.
						</template>
					</svws-ui-tooltip>
				</div>
				<div role="cell" class="svws-ui-td svws-align-center svws-no-padding items-center!" v-for="hj in GostHalbjahr.values()" :key="hj.id" :class="{'svws-divider': ((hj.id === 1) || (hj.id === 5))}">
					<span class="inline-flex justify-center items-center font-bold py-0.5 px-1.5 rounded-sm w-full m-0.5"
						:class="manager.getWochenstundenCSS(hj)">
						{{ manager.getWochenstunden(hj) }}
					</span>
				</div>
				<div role="cell" class="svws-ui-td svws-align-center svws-no-padding items-center!">
					<span class="inline-flex justify-center items-center font-bold py-0.5 px-1.5 rounded-sm w-full m-0.5"
						:class="manager.getWochenstundenJahressummeCSS()">
						{{ manager.wochenstundenJahressumme }}
					</span>
				</div>
			</div>
			<div role="row" class="svws-ui-tr">
				<div role="rowheader" class="svws-ui-td font-bold svws-align-right col-span-5 svws-divider">
					<span>Durchschnitt</span>
					<svws-ui-tooltip>
						<span class="icon i-ri-question-line -m-0.5 mx-0.5" />
						<template #content>
							In der EF und Qualifikationsphase sollten jeweils im Durchschnitt <strong>34—36</strong> Wochenstunden erreicht werden.
						</template>
					</svws-ui-tooltip>
				</div>
				<div role="cell" class="svws-ui-td svws-align-center svws-no-padding items-center! col-span-2 svws-divider">
					<span class="inline-flex justify-center items-center font-bold py-0.5 px-1.5 rounded-sm w-full m-0.5"
						:class="manager.getWochenstundenDurchschnittEFCSS()">
						{{ manager.wochenstundenDurchschnittEF }}
					</span>
				</div>
				<div role="cell" class="svws-ui-td svws-align-center svws-no-padding items-center! col-span-4 svws-divider">
					<span class="inline-flex justify-center items-center font-bold py-0.5 px-1.5 rounded-sm w-full m-0.5"
						:class="manager.getWochenstundenDurchschnittQCSS()">
						{{ manager.wochenstundenDurchschnittQ }}
					</span>
				</div>
				<div role="cell" class="svws-ui-td svws-align-center">
					<svws-ui-tooltip>
						<span class="icon i-ri-information-line -m-0.5" />
						<template #content>
							<div class="flex flex-col gap-0.5 text-center">
								<span class="flex gap-1 items-center">
									<span class="w-4 h-4 rounded-full svws-ergebnis--not-enough" />
									<span>Zu wenig</span>
								</span>
								<span class="flex gap-1 items-center">
									<span class="w-4 h-4 rounded-full svws-ergebnis--low" />
									<span>Ausreichend</span>
								</span>
								<span class="flex gap-1 items-center">
									<span class="w-4 h-4 rounded-full svws-ergebnis--good" />
									<span>Optimale Anzahl</span>
								</span>
								<span class="flex gap-1 items-center">
									<span class="w-4 h-4 rounded-full svws-ergebnis--more" />
									<span>Mehr als erforderlich</span>
								</span>
							</div>
						</template>
					</svws-ui-tooltip>
				</div>
			</div>
		</template>
	</svws-ui-table>
</template>

<script setup lang="ts">

	import { ref, onMounted } from "vue";
	import type { AbiturdatenManager } from "../../../../../core/src/core/abschluss/gost/AbiturdatenManager";
	import type { GostJahrgangsdaten } from "../../../../../core/src/core/data/gost/GostJahrgangsdaten";
	import type { GostSchuelerFachwahl } from "../../../../../core/src/core/data/gost/GostSchuelerFachwahl";
	import { GostHalbjahr } from "../../../../../core/src/core/types/gost/GostHalbjahr";
	import type { DataTableColumn } from "../../../types";
	import type { LaufbahnplanungUiManager } from "./LaufbahnplanungUiManager";

	const props = withDefaults(defineProps<{
		manager: LaufbahnplanungUiManager;
		abiturdatenManager: () => AbiturdatenManager;
		gostJahrgangsdaten: GostJahrgangsdaten;
		setWahl: (fachID: number, wahl: GostSchuelerFachwahl) => Promise<void>;
		gotoKursblockung: (halbjahr: GostHalbjahr) => Promise<unknown>
		ignoriereSprachenfolge? : boolean;
		title?: string | undefined;
		belegungHatImmerNoten?: boolean;
		hatUpdateKompetenz?: boolean;
	}>(), {
		title: undefined,
		modus: 'normal',
		ignoriereSprachenfolge: false,
		belegungHatImmerNoten: false,
		hatUpdateKompetenz: true,
	});

	const columns: Array<DataTableColumn> = [
		{ key: "kuerzel", label: "Kürzel", align: 'center', minWidth: 5, span: 0.75},
		{ key: "bezeichnung", label: "Bezeichnung", align: 'center', span: 3, minWidth: 12 },
		{ key: "wstd", label: "WS", tooltip: "Wochenstunden", align: 'center', fixedWidth: 3 },
		{ key: "folge", label: "Folge", align: 'center', fixedWidth: 4.5 },
		{ key: "ab_jg", label: "ab Jg", align: 'center', fixedWidth: 4.5 },
		{ key: "ef1", label: "EF.1", align: 'center', fixedWidth: 4.5 },
		{ key: "ef2", label: "EF.2", align: 'center', fixedWidth: 4.5 },
		{ key: "q1_1", label: "Q1.1", align: 'center', fixedWidth: 4.5 },
		{ key: "q1_2", label: "Q1.2", align: 'center', fixedWidth: 4.5 },
		{ key: "q2_1", label: "Q2.1", align: 'center', fixedWidth: 4.5 },
		{ key: "q2_2", label: "Q2.2", align: 'center', fixedWidth: 4.5 },
		{ key: "abiturfach", label: "Abiturfach", align: 'center', fixedWidth: 4.5 },
	];

	const faecherIds: number[] = [];

	const activeFachId = ref();
	const activeHalbjahrId = ref(0);
	const activeDirection = ref<string>("");

	// Aktives Fach und Halbjahr nach emit von "update:focus" durch Kindkomponente setzen
	function updateFocusState(fachId: number, halbjahrId: number) {
		activeFachId.value = fachId;
		activeHalbjahrId.value = halbjahrId;
	}

	onMounted(() => {
		// Fächer-IDs zu statischer Liste hinzufügen um Fächer durchschalten zu können
		for (const fach of props.manager.alleFaecher)
			faecherIds.push(fach.id);
	})

	// Fokus setzen: Fach wechseln (hoch/runter) oder Halbjahr wechseln (links/rechts)
	function switchFocus(event: KeyboardEvent) {
		if (!["ArrowDown", "ArrowUp", "ArrowRight", "ArrowLeft"].includes(event.key))
			return;
		activeDirection.value = event.key;
		event.preventDefault();
		switch (event.key) {
			case "ArrowDown":
				activeFachId.value = (activeFachId.value === faecherIds[faecherIds.length - 1]) ? faecherIds[0] : faecherIds[getFachIndexById(activeFachId.value) + 1];
				break;
			case "ArrowUp":
				activeFachId.value = (activeFachId.value === faecherIds[0]) ? faecherIds[faecherIds.length - 1] : faecherIds[getFachIndexById(activeFachId.value) - 1];
				break;
			case "ArrowRight":
				activeHalbjahrId.value = (activeHalbjahrId.value + 1) % (GostHalbjahr.values().length + 1);
				break;
			case "ArrowLeft":
				activeHalbjahrId.value = activeHalbjahrId.value === 0 ? GostHalbjahr.values().length : (activeHalbjahrId.value - 1);
				break;
		}
	}

	// Fokus neu setzen, wenn "update:focus:impossible" von der Kindkomponente emitted wurde
	function retryFocus(fachId: number, halbjahrId: number) {
		switch (activeDirection.value) {
			case "ArrowDown":
				activeFachId.value = (fachId === faecherIds[faecherIds.length - 1]) ? faecherIds[0] : faecherIds[getFachIndexById(fachId) + 1];
				activeHalbjahrId.value = halbjahrId;
				break;
			case "ArrowUp":
				activeFachId.value = (fachId === faecherIds[0]) ? faecherIds[faecherIds.length - 1] : faecherIds[getFachIndexById(fachId) - 1];
				activeHalbjahrId.value = halbjahrId;
				break;
			case "ArrowRight":
				activeFachId.value = fachId
				activeHalbjahrId.value = (halbjahrId + 1) % (GostHalbjahr.values().length + 1);
				break;
			case "ArrowLeft":
				activeFachId.value = fachId
				activeHalbjahrId.value = halbjahrId === 0 ? GostHalbjahr.values().length : (halbjahrId - 1);
				break;
		}
	}

	function getFachIndexById(fachId: number): number {
		for (let i=0; i <= faecherIds.length; i++)
			if (faecherIds[i] === fachId)
				return i;
		return -1;
	}

</script>

<style scoped>

	.svws-ui-tfoot--data {
		.tooltip-trigger {
			margin: -0.5rem;
		}
	}

	.svws-ergebnis--not-enough {
		background-color: var(--color-bg-ui-danger);
		color: var(--color-text-ui-ondanger);
	}

	.svws-ergebnis--low {
		background-color: var(--color-bg-ui-warning);
		color: var(--color-text-ui-onwarning);
	}

	.svws-ergebnis--good {
		background-color: var(--color-bg-ui-success-weak);
		color: var(--color-text-ui-onsuccess-weak);
	}

	.svws-ergebnis--more {
		background-color: var(--color-bg-ui-success);
		color: var(--color-text-ui-onsuccess);
	}

	.svws-ui-tr {
		grid-template-columns: minmax(5rem, 0.75fr) minmax(12rem, 3fr) 3rem repeat(9, 4.5rem);
	}

</style>
