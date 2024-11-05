<template>
	<div v-if="(schuelerListeManager().hasDaten() && (activeViewType === ViewType.DEFAULT)) || (activeViewType !== ViewType.DEFAULT)" class="page--flex">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<template v-if="activeViewType === ViewType.DEFAULT">
					<svws-ui-avatar :src="foto ? `data:image/png;base64, ${foto}` : undefined" :alt="foto !== null ? `Foto von ${vorname} ${nachname}` : ''" upload capture @image:base64="foto => patch({ foto })" />
					<div v-if="schuelerListeManager().hasDaten()" class="svws-headline-wrapper">
						<h2 class="svws-headline">
							<span>{{ vorname }} {{ nachname }}</span>
							<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
								ID: {{ schuelerListeManager().daten().id }}
							</svws-ui-badge>
						</h2>
						<span class="svws-subline">{{ klasse }}</span>
					</div>
					<div v-if="schuelerListeManager().daten().keineAuskunftAnDritte" class="svws-headline-wrapper">
						<span class="icon-xxl icon-error i-ri-alert-line inline-block" />
						<span class="text-error content-center"> Keine Auskunft an Dritte </span>
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
						<h2 class="svws-headline">Gruppenprozesse</h2>
						<span class="svws-subline">{{ schuelerSubline }}</span>
					</div>
				</template>
			</div>
			<div class="svws-ui-header--actions" />
		</header>

		<svws-ui-tab-bar :tab-manager>
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
	import { ViewType } from "@ui";

	const props = defineProps<SchuelerAppProps>();

	const schuelerSubline = computed(() => {
		const auswahlKlassenList = props.schuelerListeManager().liste.auswahlSorted();
		if (auswahlKlassenList.size() > 3)
			return `${auswahlKlassenList.size()} Schüler ausgewählt`;
		return [...auswahlKlassenList].map(k => `${k.vorname} ${k.nachname}`).join(', ');
	})

	const foto = computed<string | null>(() => {
		return props.schuelerListeManager().daten().foto;
	});

	const nachname = computed<string>(() => {
		return props.schuelerListeManager().daten().nachname;
	});

	const vorname = computed<string>(() => {
		return props.schuelerListeManager().daten().vorname;
	});

	const klasse = computed<string>(() => {
		if (!props.schuelerListeManager().hasDaten())
			return '—';
		return props.schuelerListeManager().klassen.get(props.schuelerListeManager().auswahl().idKlasse)?.kuerzel ?? '—';
	});

</script>
