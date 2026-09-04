<template>
	<template v-if="(manager().hasDaten() && (activeViewType === ViewType.DEFAULT)) || (activeViewType !== ViewType.DEFAULT)">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<template v-if="activeViewType === ViewType.DEFAULT">
					<svws-ui-avatar :src="fotoSrc"
						:alt="fotoSrcAlt"
						@image:base64="foto => patch({ foto })"
						:upload="!readonly"
						:capture="!readonly"
						:removable="!readonly" />
					<div class="svws-headline-wrapper">
						<h2 class="svws-headline">
							{{ manager().daten().titel }} {{ manager().daten().vorname }} {{ manager().daten().nachname }}
							<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
								ID: {{ manager().daten().id }}
							</svws-ui-badge>
						</h2>
						<span class="svws-subline">{{ manager().daten().kuerzel }}</span>
					</div>
				</template>
				<template v-else-if="activeViewType === ViewType.HINZUFUEGEN">
					<div class="svws-headline-wrapper">
						<h2 class="svws-headline">Anlegen einer neuen Lehrkraft...</h2>
					</div>
				</template>
				<template v-else-if="activeViewType === ViewType.GRUPPENPROZESSE">
					<div class="svws-headline-wrapper">
						<div class="flex flex-row gap-3">
							<h2 class="svws-headline text-ui-brand">Mehrfachauswahl</h2>
							<svws-ui-button v-if="manager().liste.auswahlExists()" size="normal" type="danger" @click="resetSelection">
								Auswahl aufheben
							</svws-ui-button>
						</div>
						<span class="svws-subline">{{ lehrerSubline }}</span>
					</div>
				</template>
			</div>
			<div class="svws-ui-header--actions" />
		</header>

		<svws-ui-tab-bar :tab-manager :focus-switching-enabled :focus-help-visible>
			<router-view />
		</svws-ui-tab-bar>
	</template>

	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-group-line" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { LehrerAppProps } from "./LehrerAppProps";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import { ViewType } from "@ui/ui/nav/ViewType";

	const props = defineProps<LehrerAppProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const benutzerState = useBenutzerState();

	const readonly = computed<boolean>(() => !benutzerState.kompetenzen.has(BenutzerKompetenz.LEHRERDATEN_AENDERN) || !benutzerState.kompetenzen.has(BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN));
	const fotoSrc = computed<string | undefined>(() => {
		const base64Payload = props.manager().daten().foto;
		if (base64Payload !== null) {
			return `data:image/png;base64, ${base64Payload}`;
		}
		return undefined;
	});
	const fotoSrcAlt = computed<string>(() => (fotoSrc.value !== undefined) ? `Foto von ${vorname.value} ${nachname.value}` : '');
	const vorname = computed<string>(() => props.manager().daten().vorname);
	const nachname = computed<string>(() => props.manager().daten().nachname);

	const lehrerSubline = computed(() => {
		const auswahlLehrerList = props.manager().liste.auswahlSorted();
		if (auswahlLehrerList.size() > 5) {
			return `${auswahlLehrerList.size()} Lehrer ausgewählt`;
		}
		return [...auswahlLehrerList].map(k => k.kuerzel).join(', ');
	});

	async function resetSelection() {
		await props.gotoDefaultView(props.manager().getVorherigeAuswahl()?.id);
	}

</script>
