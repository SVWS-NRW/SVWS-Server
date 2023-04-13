<template>
	<template v-if="visible">
		<svws-ui-header>
			<div class="flex items-center">
				<span class="inline-block mr-3">{{ kuerzel }}</span>
				<svws-ui-badge type="light" title="ID" class="font-mono">
					<i-ri-fingerprint-line />
					{{ auswahl?.id }}
				</svws-ui-badge>
			</div>
			<div v-if="inputFachlehrer">
				<span class="opacity-50">{{ inputFachlehrer }}</span>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="tabs" :hidden="tabsHidden" :model-value="tab" @update:model-value="setTab">
			<router-view />
		</svws-ui-router-tab-bar>
	</template>
	<div v-else class="app-layout--main--placeholder">
		<i-ri-presentation-line />
	</div>
</template>

<script setup lang="ts">

	import type { ComputedRef } from "vue";
	import { computed } from "vue";
	import type { KurseAppProps } from "./SKurseAppProps";

	const props = defineProps<KurseAppProps>();

	const kuerzel: ComputedRef<string> = computed(() => props.auswahl?.kuerzel ?? "");

	const inputFachlehrer: ComputedRef<string> = computed(() => {
		const id = props.auswahl?.lehrer;
		const leer = "kein Lehrer festgelegt";
		if (!id)
			return leer;
		const lehrer = props.mapLehrer.get(id);
		return lehrer?.kuerzel ?? leer;
	});

	const visible: ComputedRef<boolean> = computed(() => props.auswahl !== undefined);

</script>
