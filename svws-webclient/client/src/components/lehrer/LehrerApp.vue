<template>
	<template v-if="(lehrerAuswahlState.stammdaten.hasDaten && (lehrerAuswahlState.navigator.activeViewType === ViewType.DEFAULT)) || (lehrerAuswahlState.navigator.activeViewType !== ViewType.DEFAULT)">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<template v-if="lehrerAuswahlState.navigator.activeViewType === ViewType.DEFAULT">
					<svws-ui-avatar :src="fotoSrc"
						:alt="fotoSrcAlt"
						@image:base64="foto => lehrerAuswahlState.stammdaten.patch({ foto })"
						:upload="!readonly"
						:capture="!readonly"
						:removable="!readonly" />
					<div class="svws-headline-wrapper">
						<h2 class="svws-headline">
							{{ lehrerAuswahlState.stammdaten.datenOrException().titel }} {{ lehrerAuswahlState.stammdaten.datenOrException().vorname }} {{ lehrerAuswahlState.stammdaten.datenOrException().nachname }}
							<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
								ID: {{ lehrerAuswahlState.stammdaten.datenOrException().id }}
							</svws-ui-badge>
						</h2>
						<span class="svws-subline">{{ lehrerAuswahlState.stammdaten.datenOrException().kuerzel }}</span>
					</div>
				</template>
				<template v-else-if="lehrerAuswahlState.navigator.activeViewType === ViewType.HINZUFUEGEN">
					<div class="svws-headline-wrapper">
						<h2 class="svws-headline">Anlegen einer neuen Lehrkraft...</h2>
					</div>
				</template>
				<template v-else-if="lehrerAuswahlState.navigator.activeViewType === ViewType.GRUPPENPROZESSE">
					<div class="svws-headline-wrapper">
						<div class="flex flex-row gap-3">
							<h2 class="svws-headline text-ui-brand">Mehrfachauswahl</h2>
							<svws-ui-button v-if="lehrerAuswahlState.liste.auswahlExists()" size="normal" type="danger" @click="resetSelection">
								Auswahl aufheben
							</svws-ui-button>
						</div>
						<span class="svws-subline">{{ lehrerSubline }}</span>
					</div>
				</template>
			</div>
			<div class="svws-ui-header--actions" />
		</header>

		<svws-ui-tab-bar :tab-manager="() => tabManager(lehrerAuswahlState.navigator.activeViewType)" :focus-switching-enabled :focus-help-visible>
			<router-view />
		</svws-ui-tab-bar>
	</template>

	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-group-line" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";

	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import type { TabManager } from "@ui/ui/nav/TabManager";
	import { ViewType } from "@ui/ui/nav/ViewType";

	import { useLehrerAuswahlState } from "~/states/lehrer/LehrerAuswahlState";

	const lehrerAuswahlState = useLehrerAuswahlState();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const benutzerState = useBenutzerState();

	const props = defineProps<{
		tabManager: (viewType: ViewType) => TabManager;
		activeViewType: ViewType;
	}>();

	const readonly = computed<boolean>(() => !benutzerState.kompetenzen.has(BenutzerKompetenz.LEHRERDATEN_AENDERN) || !benutzerState.kompetenzen.has(BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN));
	const fotoSrc = computed<string | undefined>(() => {
		const base64Payload = lehrerAuswahlState.stammdaten.datenOrException().foto;
		if (base64Payload !== null) {
			return `data:image/png;base64, ${base64Payload}`;
		}
		return undefined;
	});
	const fotoSrcAlt = computed<string>(() => (fotoSrc.value !== undefined) ? `Foto von ${vorname.value} ${nachname.value}` : '');
	const vorname = computed<string>(() => lehrerAuswahlState.stammdaten.datenOrException().vorname);
	const nachname = computed<string>(() => lehrerAuswahlState.stammdaten.datenOrException().nachname);

	const lehrerSubline = computed(() => {
		const auswahlLehrerList = lehrerAuswahlState.liste.auswahlSorted();
		if (auswahlLehrerList.size() > 5) {
			return `${auswahlLehrerList.size()} Lehrer ausgewählt`;
		}
		return [...auswahlLehrerList].map(k => k.kuerzel).join(', ');
	});

	async function resetSelection() {
		await lehrerAuswahlState.navigator.select(lehrerAuswahlState.stammdaten.vorherige?.id);
	}

</script>
