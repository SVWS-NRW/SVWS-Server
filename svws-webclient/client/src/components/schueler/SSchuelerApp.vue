<template>
	<div v-if="stammdaten() !== null" class="page--flex">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<svws-ui-avatar :src="'data:image/png;base64, ' + foto" :alt="foto !== undefined ? 'Foto von ' + vorname + ' ' + nachname : ''" upload capture />
				<div class="svws-headline-wrapper">
					<h2 class="svws-headline">
						<span>{{ vorname }} {{ nachname }}</span>
						<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
							ID:
							{{ stammdaten()?.id || '—' }}
						</svws-ui-badge>
					</h2>
					<span class="svws-subline">{{ inputKlasse ? inputKlasse : '–' }}</span>
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

	import type { ComputedRef } from "vue";
	import { computed } from "vue";

	import type { SchuelerAppProps } from "./SSchuelerAppProps";

	const props = defineProps<SchuelerAppProps>();

	const foto: ComputedRef<string | undefined> = computed(() => {
		return props.stammdaten()?.foto ?? undefined;
	});

	const nachname: ComputedRef<string | undefined> = computed(() => {
		return props.stammdaten()?.nachname;
	});

	const vorname: ComputedRef<string | undefined> = computed(() => {
		return props.stammdaten()?.vorname;
	});

	const inputKlasse: ComputedRef<string | false> = computed(() => {
		if (props.auswahl === null)
			return false;
		return props.schuelerListeManager().klassen.get(props.auswahl.idKlasse)?.kuerzel ?? false;
	});

</script>
