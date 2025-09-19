<template>
	<div v-if="(manager().hasDaten() && (activeViewType === ViewType.DEFAULT)) || (activeViewType !== ViewType.DEFAULT)"
		class="flex flex-col w-full h-full overflow-hidden">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<template v-if="((activeViewType === ViewType.DEFAULT) || (activeViewType === ViewType.NEU))">
					<svws-ui-avatar :src="foto ? `data:image/png;base64, ${foto}` : undefined" :alt="foto !== null ? `Foto von ${vorname} ${nachname}` : ''" upload capture @image:base64="foto => patch({ foto })" />
					<div v-if="manager().hasDaten()" class="svws-headline-wrapper">
						<h2 class="svws-headline">
							<span>{{ vorname }} {{ nachname }}</span>
							<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
								ID: {{ manager().daten().id }}
							</svws-ui-badge>
						</h2>
						<span v-if="klasse !== null" class="svws-subline">{{ klasse.kuerzel }}&nbsp;
							<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
								<template v-for="l of klasse.klassenLeitungen">
									{{ manager().lehrer.get(l)?.kuerzel ?? '—' }}&nbsp;
								</template>
							</svws-ui-badge>
							<svws-ui-badge v-if="epJahre !== null" type="light" title="EP-Jahre" class="font-mono ml-2" size="small">
								{{ epJahre }}
							</svws-ui-badge>
						</span>
					</div>
					<div v-if="manager().daten().keineAuskunftAnDritte" class="svws-headline-wrapper">
						<span class="icon-xxl icon-ui-danger i-ri-alert-line" />
						<span class="text-ui-danger content-center"> Keine Auskunft an Dritte </span>
					</div>
				</template>

				<template v-else-if="activeViewType === ViewType.HINZUFUEGEN">
					<div class="svws-headline-wrapper">
						<h2 class="svws-headline">
							<span>Neuen Schüler anlegen...</span>
						</h2>
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
						<span class="svws-subline">{{ schuelerSubline }}</span>
					</div>
				</template>
			</div>
			<div class="svws-ui-header--actions print:!hidden" />
		</header>

		<svws-ui-tab-bar :tab-manager :focus-switching-enabled :focus-help-visible>
			<router-view />
		</svws-ui-tab-bar>
	</div>

	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-group-line" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { SchuelerAppProps } from "./SSchuelerAppProps";
	import { useRegionSwitch, ViewType } from "@ui";
	import { PrimarstufeSchuleingangsphaseBesuchsjahre, Schulform, type KlassenDaten } from "@core";

	const props = defineProps<SchuelerAppProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const primarschulformen = new Set([Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.PS, Schulform.S, Schulform.KS, Schulform.V]);
	const primarstufe = computed(() => primarschulformen.has(props.schulform));
	const epJahre = computed<string | null>(() => {
		if (!primarstufe.value)
			return null;
		const ep = props.manager().auswahl().epJahre;
		if (ep === null)
			return null;
		const schuljahr = props.manager().getSchuljahr();
		return PrimarstufeSchuleingangsphaseBesuchsjahre.data().getWertByIDOrNull(ep)?.daten(schuljahr)?.kuerzel ?? null;
	});

	const schuelerSubline = computed(() => {
		const auswahlSchuelerList = props.manager().liste.auswahlSorted();
		if (auswahlSchuelerList.isEmpty())
			return 'Keine Schüler ausgewählt';
		if (auswahlSchuelerList.size() > 3)
			return `${auswahlSchuelerList.size()} Schüler ausgewählt`;
		return [...auswahlSchuelerList].map(k => `${k.vorname} ${k.nachname}`).join(', ');
	})

	const foto = computed<string | null>(() => props.manager().daten().foto);
	const nachname = computed<string>(() => props.manager().daten().nachname);
	const vorname = computed<string>(() => props.manager().daten().vorname);

	const klasse = computed<KlassenDaten | null>(() => {
		if (!props.manager().hasDaten())
			return null;
		return props.manager().klassen.get(props.manager().auswahl().idKlasse);
	});

	async function resetSelection() {
		await props.gotoDefaultView(props.manager().getVorherigeAuswahl()?.id);
	}

</script>
