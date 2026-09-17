<template>
	<template v-if="(schuelerAuswahlState.manager.hasDaten() && (schuelerAuswahlState.activeViewType === ViewType.DEFAULT)) || (schuelerAuswahlState.activeViewType !== ViewType.DEFAULT)">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<template v-if="((schuelerAuswahlState.activeViewType === ViewType.DEFAULT) || (schuelerAuswahlState.activeViewType === ViewType.NEU))">
					<svws-ui-avatar :src="fotoSrc"
						:alt="fotoSrcAlt"
						@image:base64="foto => schuelerAuswahlState.patch({ foto })"
						:upload="!readonly"
						:capture="!readonly"
						:removable="!readonly" />
					<div v-if="schuelerAuswahlState.manager.hasDaten()" class="svws-headline-wrapper">
						<h2 class="svws-headline">
							<span>{{ vorname }} {{ nachname }}</span>
							<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
								ID: {{ schuelerAuswahlState.manager.daten().id }}
							</svws-ui-badge>
						</h2>
						<span v-if="klasse !== null" class="svws-subline">{{ klasse.kuerzel }}&nbsp;
							<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
								<template v-for="l of klasse.klassenLeitungen">
									{{ schuelerAuswahlState.manager.lehrer.get(l)?.kuerzel ?? '—' }}&nbsp;
								</template>
							</svws-ui-badge>
							<svws-ui-badge v-if="epJahre !== null" type="light" title="EP-Jahre" class="font-mono ml-2" size="small">
								{{ epJahre }}
							</svws-ui-badge>
						</span>
					</div>
					<div v-if="schuelerAuswahlState.manager.daten().keineAuskunftAnDritte" class="svws-headline-wrapper">
						<span class="icon-xxl icon-ui-danger i-ri-alert-line" />
						<span class="text-ui-danger content-center"> Keine Auskunft an Dritte </span>
					</div>
				</template>

				<template v-else-if="schuelerAuswahlState.activeViewType === ViewType.HINZUFUEGEN">
					<div class="svws-headline-wrapper">
						<h2 class="svws-headline">
							<span>Neuen Schüler anlegen...</span>
						</h2>
					</div>
				</template>

				<template v-else-if="schuelerAuswahlState.activeViewType === ViewType.GRUPPENPROZESSE">
					<div class="svws-headline-wrapper">
						<div class="flex flex-row gap-3">
							<h2 class="svws-headline text-ui-brand">Mehrfachauswahl</h2>
							<svws-ui-button v-if="schuelerAuswahlState.manager.liste.auswahlExists()" size="normal" type="danger" @click="resetSelection">
								Auswahl aufheben
							</svws-ui-button>
						</div>
						<span class="svws-subline">{{ schuelerSubline }}</span>
					</div>
				</template>
			</div>
			<div class="svws-ui-header--actions print:hidden!" />
		</header>

		<svws-ui-tab-bar :tab-manager="() => tabManager(schuelerAuswahlState.activeViewType)" :focus-switching-enabled :focus-help-visible>
			<router-view />
		</svws-ui-tab-bar>
	</template>

	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-group-line" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";

	import type { KlassenDaten } from "@core/asd/data/klassen/KlassenDaten";
	import { PrimarstufeSchuleingangsphaseBesuchsjahre } from "@core/asd/types/jahrgang/PrimarstufeSchuleingangsphaseBesuchsjahre";
	import { Schulform } from "@core/asd/types/schule/Schulform";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import type { TabManager } from "@ui/ui/nav/TabManager";
	import { ViewType } from "@ui/ui/nav/ViewType";

	import { useSchuelerAuswahlState } from "~/states/schueler/SchuelerAuswahlState";

	const schuleState = useSchuleState();
	const benutzerState = useBenutzerState();
	const schuelerAuswahlState = useSchuelerAuswahlState();

	const props = defineProps<{
		tabManager: (viewType: ViewType) => TabManager;
		activeViewType: ViewType;
	}>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const primarschulformen = new Set(
		[Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.PS, Schulform.S, Schulform.KS, Schulform.V]
	);

	const readonly = computed<boolean>(() => !benutzerState.kompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));
	const primarstufe = computed<boolean>(() => primarschulformen.has(schuleState.schulform));
	const epJahre = computed<string | null>(() => {
		if (!primarstufe.value) {
			return null;
		}
		const ep = schuelerAuswahlState.manager.auswahl().epJahre;
		if (ep === null) {
			return null;
		}
		return PrimarstufeSchuleingangsphaseBesuchsjahre.data().getWertByIDOrNull(ep)?.daten(schuleState.schuljahr)?.kuerzel ?? null;
	});

	const schuelerSubline = computed(() => {
		const auswahlSchuelerList = schuelerAuswahlState.manager.liste.auswahlSorted();
		if (auswahlSchuelerList.isEmpty()) {
			return 'Keine Schüler ausgewählt';
		}
		if (auswahlSchuelerList.size() > 3) {
			return `${auswahlSchuelerList.size()} Schüler ausgewählt`;
		}
		return [...auswahlSchuelerList].map(k => `${k.vorname} ${k.nachname}`).join(', ');
	});

	const fotoSrc = computed<string | undefined>(() => {
		const base64Payload = schuelerAuswahlState.manager.daten().foto;
		if (base64Payload !== null) {
			return `data:image/png;base64, ${base64Payload}`;
		}
		return undefined;
	});
	const fotoSrcAlt = computed<string>(() => (fotoSrc.value !== undefined) ? `Foto von ${vorname.value} ${nachname.value}` : '');
	const nachname = computed<string>(() => schuelerAuswahlState.manager.daten().nachname);
	const vorname = computed<string>(() => schuelerAuswahlState.manager.daten().vorname);

	const klasse = computed<KlassenDaten | null>(() => {
		if (!schuelerAuswahlState.manager.hasDaten()) {
			return null;
		}
		return schuelerAuswahlState.manager.klassen.get(schuelerAuswahlState.manager.auswahl().idKlasse);
	});

	async function resetSelection() {
		await schuelerAuswahlState.gotoDefaultView(schuelerAuswahlState.manager.getVorherigeAuswahl()?.id);
	}

</script>
