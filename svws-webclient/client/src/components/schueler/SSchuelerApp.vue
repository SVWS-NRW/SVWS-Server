<template>
	<div v-if="schuelerListeManager().hasDaten()" class="page--flex">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<svws-ui-avatar :src="'data:image/png;base64, ' + foto" :alt="foto !== undefined ? 'Foto von ' + vorname + ' ' + nachname : ''" upload capture />
				<div class="svws-headline-wrapper">
					<h2 class="svws-headline">
						<span>{{ vorname }} {{ nachname }}</span>
						<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
							ID:
							{{ schuelerListeManager().daten().id }}
						</svws-ui-badge>
					</h2>
					<span class="svws-subline">{{ inputKlasse === null ? '–' : inputKlasse }}</span>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<svws-ui-router-tab-bar :routes="tabs" :hidden="tabsHidden" :model-value="tab" @update:model-value="setTab">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app--content--placeholder">
		<i-ri-group-line />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";

	import type { SchuelerAppProps } from "./SSchuelerAppProps";

	const props = defineProps<SchuelerAppProps>();

	const foto = computed<string | undefined>(() => {
		return props.schuelerListeManager().daten().foto ?? undefined;
	});

	const nachname = computed<string>(() => {
		return props.schuelerListeManager().daten().nachname;
	});

	const vorname = computed<string>(() => {
		return props.schuelerListeManager().daten().vorname;
	});

	const inputKlasse = computed<string | null>(() => {
		if (!props.schuelerListeManager().hasDaten())
			return null;
		return props.schuelerListeManager().klassen.get(props.schuelerListeManager().auswahl().idKlasse)?.kuerzel ?? null;
	});

</script>
