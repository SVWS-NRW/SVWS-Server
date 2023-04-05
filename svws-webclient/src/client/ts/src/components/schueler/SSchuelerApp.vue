<template>
	<div v-if="stammdaten !== undefined" class="flex w-full flex-col h-full">
		<svws-ui-header>
			<div class="flex items-center">
				<div class="w-20 mr-4">
					<svws-ui-avatar :src="'data:image/png;base64, ' + foto" :alt="foto !== undefined ? 'Foto von ' + vorname + ' ' + nachname : ''" upload capture />
				</div>
				<div>
					<span class="inline-block mr-3"> {{ vorname }} {{ nachname }} </span>
					<svws-ui-badge type="light" title="ID" class="font-mono">
						<i-ri-fingerprint-line />
						{{ stammdaten === undefined ? "" : stammdaten.id }}
					</svws-ui-badge>
					<br>
					<span class="opacity-50"> {{ inputKlasse ? inputKlasse : '–' }} </span>
				</div>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="tabs" :hidden="tabsHidden" :model-value="tab" @update:model-value="setTab">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<i-ri-group-line />
	</div>
</template>

<script lang="ts" type="module">
</script>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";

	import { SchuelerAppProps } from "./SSchuelerAppProps";

	const props = defineProps<SchuelerAppProps>();

	const foto: ComputedRef<string | undefined> = computed(() => {
		return props.stammdaten?.foto ?? undefined;
	});

	const nachname: ComputedRef<string | undefined> = computed(() => {
		return props.stammdaten?.nachname;
	});

	const vorname: ComputedRef<string | undefined> = computed(() => {
		return props.stammdaten?.vorname;
	});

	const inputKlasse: ComputedRef<string | false> = computed(() => {
		if (props.auswahl === undefined)
			return false;
		return props.mapKlassen.get(props.auswahl.idKlasse)?.kuerzel ?? false;
	});

</script>
